package gfpa.graph.search;

import gfpa.graph.common.DirectedGraph;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import java.util.LinkedList;
import java.util.Stack;

public class GraphSearch {

	private GraphSearch()
	{
	}

	public static void depthFirstSearch(DirectedGraph graph, int startId, GraphSearchVisitor visitor)
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

	public static void widthFirstSearch(DirectedGraph graph, int startId, GraphSearchVisitor visitor)
	{
		LinkedList<Node> queue = new LinkedList<Node>();
		TIntHashSet isPassed = new TIntHashSet();
		queue.add(new Node(startId));

		while (!queue.isEmpty())
		{
			Node node = queue.getFirst();

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
				Node n = new Node(s);
				if(!isPassed.contains(s) && !queue.contains(n))
					queue.add(n);
			}
			else
			{
				queue.removeFirst();
				boolean isContinue = visitor.onLeave(node.getId());
				if(!isContinue) break;
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

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (id != other.id)
				return false;
			return true;
		}
	}

	public static int[] depthFirstOrderArray(DirectedGraph graph, int startId)
	{
		TIntList ret = new TIntArrayList();
		depthFirstSearch(graph, startId, new GraphSearchVisitor()
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
