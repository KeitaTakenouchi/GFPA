package gfpa.graph.search;

import gfpa.graph.common.DirectedGraph;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.stack.array.TIntArrayStack;

public class DepthFirstSearch {

	private DepthFirstSearch()
	{
	}

	public static void search(DirectedGraph graph, int startId, DepthFirstSearchVisitor visitor)
	{
		TIntArrayStack worklist = new TIntArrayStack();
		worklist.push(startId);
		TIntHashSet isPassed = new TIntHashSet();

		while (worklist.size() > 0)
		{
			int w = worklist.pop();
			if(isPassed.contains(w)) continue;
			isPassed.add(w);
			boolean isContinue = visitor.onVisit(w);
			if(!isContinue) break;

			for(int c : graph.getSuccessors(w))
			{
				if(!isPassed.contains(c))
				{
					worklist.push(c);
				}
			}
		}
	}

	public static int[] depthFirstOrderArray(DirectedGraph graph, int startId)
	{
		TIntList ret = new TIntArrayList();
		search(graph, startId, new DepthFirstSearchVisitor()
		{
			@Override
			public boolean onVisit(int id)
			{
				ret.add(id);
				return true;
			}
		});
		return ret.toArray();
	}
}
