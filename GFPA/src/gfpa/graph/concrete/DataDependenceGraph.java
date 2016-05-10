package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.search.DepthFirstSearch;
import gfpa.graph.search.EdgeVisitor;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataDependenceGraph<V> extends LabeledDirectedGraph<V>
{
	/**
	 * id -> set of defined variables.
	 */
	private HashMap<Integer, HashSet<V>> definedVars = new HashMap<>();

	/**
	 * variable -> set of ids.
	 */
	private HashMap<V, TIntHashSet> definedIds = new HashMap<>();
	private HashMap<Integer, HashSet<V>> usedVars = new HashMap<>();
	private ControlFlowGraph cfgraph;
	private	TIntSet reachableNodes;

	public DataDependenceGraph(ControlFlowGraph cfgraph)
	{
		this.cfgraph = cfgraph;
		this.reachableNodes = new TIntHashSet(cfgraph.reachableFrom(cfgraph.getEntryId()));
	}

	public ControlFlowGraph getCFG()
	{
		return this.cfgraph;
	}

	/**
	 * Build data-flow edges with a fix-point algorithm.
	 */
	public void buildEdges()
	{

		TIntObjectHashMap<TIntLinkedList> origID2newIDs = new TIntObjectHashMap<TIntLinkedList>();
		TIntIntHashMap newID2origID = new TIntIntHashMap();

		//new id -> a defined variable
		TIntObjectHashMap<V> nDefinedVars = new TIntObjectHashMap<>();

		//create new graph nodes and map id -> def,use.
		int newIdCount = 0;
		for(int n : cfgraph.getNodes())
		{
			TIntLinkedList createdIDs = new TIntLinkedList();
			HashSet<V> defVars = definedVars.get(n);
			if(defVars == null)
			{
				createdIDs.add(newIdCount);
				origID2newIDs.put(n, createdIDs);
				newID2origID.put(newIdCount, n);
				newIdCount++;
				continue;
			}
			for(V v : defVars)
			{
				createdIDs.add(newIdCount);
				newID2origID.put(newIdCount, n);
				nDefinedVars.put(newIdCount, v);
				newIdCount++;
			}
			origID2newIDs.put(n, createdIDs);
		}

		ControlFlowGraph ncfgraph = new ControlFlowGraph(-1);
		//create new control flow graph edges within original nodes.
		for(int n : cfgraph.getNodes())
		{
			TIntLinkedList newIds = origID2newIDs.get(n);
			int previous = -3;
			for(int newId : newIds.toArray())
			{
				if(previous != -3)
					ncfgraph.putEdge(previous, newId);
				previous = newId;
			}
		}

		//create new control flow graph edges.
		cfgraph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				TIntLinkedList fromNewIds = origID2newIDs.get(from);
				TIntLinkedList toNewIds = origID2newIDs.get(to);
				ncfgraph.putEdge(fromNewIds.max(), toNewIds.min());
				return true;
			}
		});

		//add edges from entry node -1 to source ndoes.
		for(int s : ncfgraph.getSource())
			ncfgraph.putEdge(-1, s);

		//add def, use to new data dependence graph.
		DataDependenceGraph<V> nddgraph = new DataDependenceGraph<V>(ncfgraph);
		for(int newID : ncfgraph.getNodes())
		{
			{
				V var = nDefinedVars.get(newID);
				if(var != null)
					nddgraph.def(newID, var);
			}
			{
				int origId = newID2origID.get(newID);
				HashSet<V> vars = usedVars.get(origId);
				if(vars != null)
					nddgraph.use(newID, vars);
			}
		}
		nddgraph.buildEdgesWithSingleDef();

		//create original graph edges with label.
		nddgraph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				for(V v : nddgraph.getLabels(from, to))
					putEdge(newID2origID.get(from), newID2origID.get(to) , v);
				return true;
			}
		});
	}

	/**
	 * Build data-flow edges with a fixed-point algorithm.
	 * Each graph nodes has only one definition.
	 */
	private void buildEdgesWithSingleDef()
	{
		int[] nodes = DepthFirstSearch.depthFirstOrderArray(cfgraph, cfgraph.getEntryId());
		int size = nodes.length;

		//map : node id -> index
		TIntIntHashMap idIndexMap = new TIntIntHashMap();

		//calculate id -> index
		for (int i = 0 ; i < size ; i++)
			idIndexMap.put(nodes[i], i);

		//initialize each array.
		BitSet[] kill = new BitSet[size];
		for(int i = 0 ; i < size ; i++)	kill[i] = new BitSet(size);
		BitSet[] def = new BitSet[size];
		for(int i = 0 ; i < size ; i++)	def[i] = new BitSet(size);
		BitSet[] reach = new BitSet[size];
		for(int i = 0 ; i < size ; i++)	reach[i] = new BitSet(size);

		//calculate KILL(n)
		for (int i = 0 ; i < size ; i++)
		{
			int n = nodes[i];
			TIntHashSet killedIds = new TIntHashSet();
			if(definedVars.get(n) == null) continue;
			for(V var : definedVars.get(n))
			{
				if(definedIds.get(var) == null) continue;
				killedIds.addAll(definedIds.get(var));
			}
			killedIds.remove(n);

			for(int killedId : killedIds.toArray())
				kill[i].set(idIndexMap.get(killedId));
		}

		//calculate DEF(n)
		for (int i = 0 ; i < size ; i++)
		{
			int n = nodes[i];
			//check whether some variables are defined on the id.
			if(definedVars.get(n)==null) continue;
			def[i].set(i);
		}

		//calculate REACH(n) with the fixed point algorithm.
		boolean isChanged;
		do
		{
			isChanged = false;
			for(int i = 0 ; i < nodes.length ; i++)
			{
				BitSet newreach = new BitSet(size);
				for(int p : cfgraph.getPredecessors(nodes[i]))
				{
					int indexP = idIndexMap.get(p);
					BitSet bitsP = new BitSet(size);
					//REACH(P)
					bitsP.or(reach[indexP]);
					//REACH(P) - KILL(P)
					bitsP.andNot(kill[indexP]);
					//DEF(P) + {REACH(P) - KILL(P)}
					bitsP.or(def[indexP]);
					newreach.or(bitsP);
				}
				if(!newreach.equals(reach[i]))
				{
					isChanged = true;
					reach[i] = newreach;
				}
			}
		} while (isChanged);

		for(int i = 0 ; i < size ; i++)
		{
			int to = nodes[i];
			HashSet<V> usedVariables = usedVars.get(to);
			if(usedVariables == null) continue;
			for(int defIndex : reach[i].stream().toArray())
			{
				int from = nodes[defIndex];
				HashSet<V> defVariables = definedVars.get(from);
				if(defVariables == null) continue;
				HashSet<V> intersection = new HashSet<V>(defVariables);
				intersection.retainAll(usedVariables);
				for(V v : intersection)
					putEdge(from, to , v);
			}
		}
	}

	//for debug.
	private void dump(BitSet[] bits)
	{
		System.out.println();
		for(int i = 0 ; i < bits.length ; i++)
		{
			System.out.println("bit("+i+")="+ bits[i]);
		}
	}


	public void forAllEachEdge(EdgeVisitor visitor)
	{
		super.forEachEdge(visitor);
		cfgraph.forEachEdge(visitor);
	}

	public void def(int id, Set<V> vars)
	{
		for(V var : vars)
			def(id, var);
	}

	public void def(int id, V var)
	{
		if(!reachableNodes.contains(id)) return;

		HashSet<V> vars = definedVars.get(id);
		if(vars == null) vars = new HashSet<>();
		vars.add(var);
		definedVars.put(id, vars);

		TIntHashSet ids = definedIds.get(var);
		if(ids == null) ids = new TIntHashSet();
		ids.add(id);
		definedIds.put(var, ids);
	}

	public void use(int id, Set<V> vars)
	{
		for(V var : vars)
			use(id, var);
	}

	public void use(int id, V var)
	{
		if(!reachableNodes.contains(id)) return;

		HashSet<V> vars = usedVars.get(id);
		if(vars == null) vars = new HashSet<>();
		vars.add(var);
		usedVars.put(id, vars);
	}

}
