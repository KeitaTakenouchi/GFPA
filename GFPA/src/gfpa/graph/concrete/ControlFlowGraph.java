package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class ControlFlowGraph extends DirectedGraph
{
	private int entryId;

	public ControlFlowGraph(int entryId)
	{
		this.entryId = entryId;
	}

	@Override
	protected void createReverseGraphInstance()
	{
		//entryId is not used.
		this.reverse = new ControlFlowGraph(-1000);
	}

	public int getEntryId()
	{
		return entryId;
	}

	public void removeUnreacableNodes()
	{
		TIntObjectHashMap<TIntHashSet> newEdges = new TIntObjectHashMap<TIntHashSet>();
		{
			TIntArrayList newNodes = new TIntArrayList(reachableFrom(entryId));
			for(int from : edges.keys())
			{
				if(newNodes.contains(from))
				{
					TIntHashSet to = edges.get(from);
					newEdges.put(from, to);
				}
			}
		}
		clear();
		for(int from : newEdges.keys())
		{
			for(int to : newEdges.get(from).toArray())
			{
				putEdge(from, to);
			}
		}
	}

	@Override
	public ControlFlowGraph getReversedGraph()
	{
		ControlFlowGraph reverse = null;
		int[] sinks = getSinks();

		if(sinks.length == 1)
		{
			reverse = new ControlFlowGraph(sinks[0]);
		}
		else
		{
			int tmpExit = min(nodes.toArray()) -1;
			reverse = new ControlFlowGraph(tmpExit);
			for(int i : sinks)
			{
				reverse.putEdge(tmpExit, i);
			}
		}
		copyReverseGraphTo(reverse);
		return reverse;
	}

	private int min(int[] arr)
	{
		int min = arr[0];
		for(int i = 0 ; i < arr.length ; i++)
			min = (arr[i] < min)? arr[i] : min;
			return min;
	}

}
