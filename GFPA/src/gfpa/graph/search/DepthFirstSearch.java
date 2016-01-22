package gfpa.graph.search;

import gfpa.graph.common.DirectedGraph;
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

	public static int[] DepthFirstOrder(DirectedGraph graph, int startId)
	{
		int[] ret =new int[graph.size()];
		search(graph, startId, new DepthFirstSearchVisitor()
		{
			int count = 0;
			@Override
			public boolean onVisit(int id)
			{
				ret[count] = id;
				count++;
				return true;
			}
		});
		return ret;
	}
}
