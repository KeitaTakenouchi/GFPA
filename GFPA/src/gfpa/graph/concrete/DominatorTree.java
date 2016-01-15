package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.search.DepthFirstSearch;
import gfpa.graph.search.DepthFirstSearchVisitor;
import gnu.trove.set.hash.TIntHashSet;

import java.util.HashMap;

public class DominatorTree extends DirectedGraph
{
	private int rootId;
	private HashMap<Integer, TIntHashSet> dominator = new HashMap<Integer, TIntHashSet>();

	public DominatorTree(ControlFlowGraph cfgraph)
	{
		this.rootId = cfgraph.getEntryId();

		//initialize dominator value.
		{
			TIntHashSet set = new TIntHashSet();
			set.add(rootId);
			dominator.put(rootId, set);
		}
		TIntHashSet notEntrySet = new TIntHashSet();
		notEntrySet.addAll(cfgraph.getNodes());
		notEntrySet.remove(rootId);
		for(int i : notEntrySet.toArray())
		{
			TIntHashSet set = new TIntHashSet();
			set.addAll(cfgraph.getNodes());
			dominator.put(i, set);
		}

		//calculate dominators with fixed point.

		HashMap<Integer, TIntHashSet> tmp;
		do
		{
			tmp = copy(dominator);
			for(int n : notEntrySet.toArray())
			{
				TIntHashSet intersection = resolveIntersection(cfgraph, n);
				intersection.add(n);
				dominator.put(n, intersection);
			}
		} while (!isSame(tmp, dominator));
//				dump(dominator);

		//build tree edges.
		//DFS is enough because each node has a single parent node.
		ControlFlowGraph reversedGraph = cfgraph.getReversedGraph();
		for(int to : dominator.keySet())
		{
			DepthFirstSearch.search(reversedGraph, to, new DepthFirstSearchVisitor()
			{
				@Override
				public boolean onVisit(int from)
				{
					if(to == from) return true;
					if(dominator.get(to).contains(from))
					{
						putEdge(from, to);
						return false;
					}
					return true;
				}
			});
		}
	}

	private TIntHashSet resolveIntersection(ControlFlowGraph cfgraph, int n)
	{
		TIntHashSet intersection = new TIntHashSet();
		for(int p : cfgraph.getPredecessors(n))
		{
			intersection.addAll(dominator.get(p));
		}
		for(int e : intersection.toArray())
		{
			for(int p : cfgraph.getPredecessors(n))
			{
				if(!dominator.get(p).contains(e))
				{
					intersection.remove(e);
					break;
				}
			}
		}
		return intersection;
	}

	private void dump(HashMap<Integer, TIntHashSet> dominator)
	{
		System.out.println();
		for( int i : dominator.keySet())
			System.out.println("dom("+i +")=" + dominator.get(i));
	}

	private boolean isSame(HashMap<Integer, TIntHashSet> one, HashMap<Integer, TIntHashSet> two)
	{
		if(one.size() != two.size()) return false;

		for(int i : one.keySet())
		{
			if(!one.get(i).equals(two.get(i)))
				return false;
		}
		return true;
	}

	private HashMap<Integer, TIntHashSet> copy(HashMap<Integer, TIntHashSet> original)
	{
		return  new HashMap<Integer, TIntHashSet>(original);
	}

	public int getRootId()
	{
		return rootId;
	}

	/**
	 * d dominates n if every path from the entry node to n must go through d.
	 * @param i specifies node ID.
	 * @return node IDs which dominate i, that is dom(i).
	 */
	public int[] dominator(int i)
	{
		return dominator.get(i).toArray();
	}

	/**
	 * @param i specifies node ID.
	 * @return Return idom(i) and return a negative value if it doesn't exist.
	 */
	public int immediateDominator(int i)
	{
		int[] predecessors = getPredecessors(i);
		return (predecessors.length > 0) ? predecessors[0]: (-1);
	}
}
