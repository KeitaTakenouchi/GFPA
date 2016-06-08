package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.search.GraphSearch;
import gfpa.graph.search.DepthFirstSearchVisitor;
import gnu.trove.map.hash.TIntIntHashMap;

import java.util.Arrays;
import java.util.BitSet;

public class DominatorTree extends DirectedGraph
{
	private int entryId;
	private int size;
	//	private HashMap<Integer, TIntArrayList> dominator = new HashMap<Integer, TIntArrayList>();
	private BitSet[] dominator;
	private TIntIntHashMap idIndexMap = new TIntIntHashMap();

	public DominatorTree(ControlFlowGraph cfgraph)
	{
		this.entryId = cfgraph.getEntryId();
		this.size = cfgraph.size();
		this.dominator = new BitSet[size];
		int[] nodes = GraphSearch.depthFirstOrderArray(cfgraph, entryId);
		//initialize dominator value.
		for(int i = 0  ; i < nodes.length ; i++ )
		{
			BitSet bits = new BitSet(size);
			if(nodes[i] != entryId)
				bits.set(0, bits.size());
			else
				bits.set(i);
			dominator[i] = bits;
			idIndexMap.put(nodes[i], i);
		}

		//calculate dominators with fixed point.
		BitSet[] tmp;
		do
		{
			tmp = new BitSet[size];
			for(int i = 0 ; i < size ; i++)
				tmp[i] = (BitSet) dominator[i].clone();
			//calculate dom(n)
			for(int i = 0 ; i < nodes.length; i++)
			{
				int n = nodes[i];
				if(n == entryId) continue;
				BitSet intersection = new BitSet(size);
				intersection.set(0,intersection.size());
				for(int p : cfgraph.getPredecessors(n))
					intersection.and(dominator[idIndexMap.get(p)]);
				intersection.set(i);
				dominator[i] = intersection;
			}
		} while (!Arrays.equals(tmp, dominator));
		//		dump(dominator);

		//build tree edges.
		//DFS is enough because each node has a single parent node.
		ControlFlowGraph reversedGraph = cfgraph.getReversedGraph();
		for(int to : nodes)
		{
			int i = idIndexMap.get(to);
			GraphSearch.depthFirstSearch(reversedGraph, to, new DepthFirstSearchVisitor()
			{
				@Override
				public boolean onVisit(int from)
				{
					if(to == from) return true;
					if(dominator[i].get(idIndexMap.get(from)))
					{
						putEdge(from, to);
						return false;
					}
					return true;
				}

				@Override
				public boolean onLeave(int id)
				{
					return true;
				}
			});
		}
	}

	private void dump(BitSet[] dominator)
	{
		System.out.println();
		for(int i = 0 ; i < dominator.length ; i++)
		{
			System.out.println("dom("+i +")="+ dominator[i]);
		}
	}

	public int getEntryId()
	{
		return entryId;
	}

	/**
	 * d dominates n if every path from the entry node to n must go through d.
	 * @param n specifies node ID.
	 * @return node IDs which dominate i, that is dom(i).
	 */
	public int[] dominator(int n)
	{
		return reachableTo(n);
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
