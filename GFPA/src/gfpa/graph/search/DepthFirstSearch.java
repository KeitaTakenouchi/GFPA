package gfpa.graph.search;

import gfpa.graph.common.DirectedGraph;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import java.util.Stack;

public class DepthFirstSearch {

	private DepthFirstSearch()
	{
	}

	public static void search(DirectedGraph graph, int startId, DepthFirstSearchVisitor visitor)
	{
		Stack<Node> stack = new Stack<Node>();
		TIntHashSet isPassed = new TIntHashSet();
		stack.push(new Node(startId));

		while (!stack.isEmpty())
		{
			Node node = stack.peek();

			if(!isPassed.contains(node.getId()))//first visit
			{
				isPassed.add(node.getId());
				boolean isContinue = visitor.onVisit(node.getId());
				if(!isContinue) break;
			}

			int[] succ = graph.getSuccessors(node.getId());
			int index = node.getEdgeIndex();
			if(index < succ.length)
			{
				int s = succ[index];
				if(!isPassed.contains(s))
					stack.push(new Node(s));
			}
			else
			{
				boolean isContinue = visitor.onLeave(node.getId());
				if(!isContinue) break;
				stack.pop();
			}

		}
	}

	private static class Node
	{
		private int id;
		private int edgeIndex = 0;

		public Node(int id)
		{
			this.id = id;
		}

		public int getId()
		{
			return id;
		}

		public int getEdgeIndex()
		{
			edgeIndex++;
			return edgeIndex - 1;
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

			@Override
			public boolean onLeave(int id)
			{
				return true;
			}
		});
		return ret.toArray();
	}
}
