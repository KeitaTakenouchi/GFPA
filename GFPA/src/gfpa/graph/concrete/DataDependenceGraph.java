package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;
import gfpa.graph.search.DepthFirstSearch;
import gfpa.graph.search.EdgeVisitor;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataDependenceGraph extends LabeledDirectedGraph<Variable>
{
	/**
	 * id -> set of defined variables.
	 */
	private HashMap<Integer, HashSet<Variable>> definedVars = new HashMap<>();

	/**
	 * variable -> set of ids.
	 */
	private HashMap<Variable, TIntHashSet> definedIds = new HashMap<>();
	private HashMap<Integer, HashSet<Variable>> usedVars = new HashMap<>();
	private ControlFlowGraph cfgraph;
	private	TIntSet reachableNodes;
	//for bit operations.
	private TIntIntHashMap idIndexMap = new TIntIntHashMap();
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
	 * Bit operations is faster but not implemented.
	 */
	public void buildEdges()
	{
		int[] nodes = DepthFirstSearch.depthFirstOrderArray(cfgraph, cfgraph.getEntryId());
		int size = nodes.length;

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
			for(Variable var : definedVars.get(n))
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

		//calculate REACH(n)
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
			HashSet<Variable> usedVariables = usedVars.get(to);
			if(usedVariables == null) continue;
			for(int defIndex : reach[i].stream().toArray())
			{
				int from = nodes[defIndex];
				HashSet<Variable> defVariables = definedVars.get(from);
				if(defVariables == null) continue;
				HashSet<Variable> intersection = new HashSet<Variable>(defVariables);
				intersection.retainAll(usedVariables);
				for(Variable v : intersection)
					super.putEdge(from, to , v);
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

	public void def(int id, Set<Variable> vars)
	{
		for(Variable var : vars)
			def(id, var);
	}

	public void def(int id, Variable var)
	{
		if(!reachableNodes.contains(id)) return;

		HashSet<Variable> vars = definedVars.get(id);
		if(vars == null) vars = new HashSet<>();
		vars.add(var);
		definedVars.put(id, vars);

		TIntHashSet ids = definedIds.get(var);
		if(ids == null) ids = new TIntHashSet();
		ids.add(id);
		definedIds.put(var, ids);
	}

	public void use(int id, Set<Variable> vars)
	{
		for(Variable var : vars)
			use(id, var);
	}

	public void use(int id, Variable var)
	{
		if(!reachableNodes.contains(id)) return;

		HashSet<Variable> vars = usedVars.get(id);
		if(vars == null) vars = new HashSet<>();
		vars.add(var);
		usedVars.put(id, vars);
	}

}
