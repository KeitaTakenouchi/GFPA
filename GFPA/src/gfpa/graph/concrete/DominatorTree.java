package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.search.DepthFirstSearch;
import gfpa.graph.search.DepthFirstSearchVisitor;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import java.util.HashMap;

public class DominatorTree extends DirectedGraph
{
	private int entryId;
	private HashMap<Integer, TIntArrayList> dominator = new HashMap<Integer, TIntArrayList>();

	public DominatorTree(ControlFlowGraph cfgraph)
	{
		this.entryId = cfgraph.getEntryId();

		{//initialize dominator value.
			TIntArrayList set = new TIntArrayList();
			set.add(entryId);
			dominator.put(entryId, set);
		}

		TIntArrayList notEntryList = new TIntArrayList();
		notEntryList.addAll(DepthFirstSearch.depthFirstOrderArray(cfgraph, entryId));
		notEntryList.remove(entryId);
		for(int i : notEntryList.toArray())
		{
			TIntArrayList set = new TIntArrayList();
			set.addAll(cfgraph.getNodes());
			dominator.put(i, set);
		}

		//calculate dominators with fixed point.
		HashMap<Integer, TIntArrayList> tmp;
		do
		{
			tmp = new HashMap<>(dominator);
			for(int n : notEntryList.toArray())
			{
				TIntArrayList intersection = new TIntArrayList();
				intersection.addAll(cfgraph.getNodes());
				for(int p : cfgraph.getPredecessors(n))
					intersection.retainAll(dominator.get(p));
				if(!intersection.contains(n))
					intersection.add(n);
				dominator.put(n, intersection);
			}
		} while (!tmp.equals(dominator));
//		dump(dominator);

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

	private void dump(HashMap<Integer, TIntHashSet> dominator)
	{
		System.out.println();
		for( int i : dominator.keySet())
			System.out.println("dom("+i +")=" + dominator.get(i));
	}

	public int getEntryId()
	{
		return entryId;
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
