package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;
import gfpa.graph.search.EdgeVisitor;
import gnu.trove.set.hash.TIntHashSet;

import java.util.Arrays;
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

	public DataDependenceGraph(ControlFlowGraph cfgraph)
	{
		this.cfgraph = cfgraph;
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
		HashMap<Integer, TIntHashSet> kill = new HashMap<>();
		HashMap<Integer, TIntHashSet> reach = new HashMap<>();

		//calculate KILL(n)
		for (int n : cfgraph.getNodes())
		{
			TIntHashSet killSet = new TIntHashSet();
			if(definedVars.get(n) == null) continue;
			for(Variable var : definedVars.get(n))
			{
				if(definedIds.get(var) == null) continue;
				killSet.addAll(definedIds.get(var));
			}
			killSet.remove(n);
			kill.put(n, killSet);
		}

		//initialize REACH(n)
		for(int n : cfgraph.getNodes())
			reach.put(n, new TIntHashSet());

		//calculate REACH(n)
		boolean isChanged;
		do
		{
			isChanged = false;
			for(int n : cfgraph.getNodes())
			{
//				System.out.println(n);
				TIntHashSet	newreach = new TIntHashSet();
				for(int p : cfgraph.getPredecessors(n))
				{
					TIntHashSet	preach = new TIntHashSet();
					//REACH(P)
					preach.addAll(reach.get(p));
					//KILL(P)
					TIntHashSet killset = kill.get(p);
					//REACH(P) - KILL(P)
					if(killset != null)
						preach.removeAll(killset);
					//DEF(P) + {REACH(P) - KILL(P)}
					if(definedVars.get(p) != null)
						preach.add(p);
					newreach.addAll(preach);
				}
				if(!newreach.equals(reach.get(n)))
				{
					isChanged = true;
					reach.put(n, newreach);
				}
			}
		} while (isChanged);
		//		dumpReach(reach);

		//build edges
		for(int usedId : usedVars.keySet())
		{
			for(Variable usedVar : usedVars.get(usedId))
			{
				for(int reachableDef : reach.get(usedId).toArray())
				{
					if(definedVars.get(reachableDef).contains(usedVar))
						super.putEdge(reachableDef, usedId , usedVar);
				}
			}
		}

	}

	public void forAllEachEdge(EdgeVisitor visitor)
	{
		super.forEachEdge(visitor);
		cfgraph.forEachEdge(visitor);
	}

	/**
	 * for debug
	 */
	private void dumpReach(HashMap<Integer, TIntHashSet> reach)
	{
		for(int i : cfgraph.getNodes())
		{
			int[] arr = reach.get(i).toArray();
			Arrays.sort(arr);
			System.out.println("REACH("+i+") = "+ Arrays.toString(arr));
		}
	}

	public void def(int id, Set<Variable> vars)
	{
		for(Variable var : vars)
		{
			def(id, var);
		}
	}

	public void def(int id, Variable var)
	{
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
		{
			use(id, var);
		}
	}

	public void use(int id, Variable var)
	{
		HashSet<Variable> vars = usedVars.get(id);
		if(vars == null) vars = new HashSet<>();
		vars.add(var);
		usedVars.put(id, vars);
	}

	@Override
	public void putEdge(int from, int to, Variable label)
	{
		throw new AssertionError();
	}

	@Override
	public void putEdge(int from, int to)
	{
		throw new AssertionError();
	}

}
