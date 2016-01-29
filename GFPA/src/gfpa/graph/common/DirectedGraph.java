package gfpa.graph.common;

import gfpa.graph.search.EdgeVisitor;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

import java.util.ArrayList;

/**
 * Directed graph class.
 * This graph cannot realize a duplicated edge.
 * @author Takenouchi Keita
 */
public class DirectedGraph
{
	protected TIntArrayList nodes = new TIntArrayList();
	protected TIntObjectHashMap<TIntHashSet> edges = new TIntObjectHashMap<TIntHashSet>();
	protected DirectedGraph reverse;

	/**
	 * @param from must be a positive integer.
	 * @param to must be a positive integer.
	 */
	public void putEdge(int from, int to)
	{
		if(reverse == null)
		{
			createReverseGraphInstance();
			reverse.reverse = this;
		}

		addEdge(from, to, this);
		addEdge(to, from, reverse);
	}

	private void addEdge(int from, int to, DirectedGraph graph)
	{
		if(!graph.nodes.contains(from))
			graph.nodes.add(from);
		if(!graph.nodes.contains(to))
			graph.nodes.add(to);
		TIntHashSet set = graph.edges.get(from);
		if(set == null) set = new TIntHashSet();
		set.add(to);
		graph.edges.put(from, set);
	}

	/**
	 * clear nodes and edges.
	 */
	public void clear()
	{
		this.nodes = new TIntArrayList();
		this.edges = new TIntObjectHashMap<TIntHashSet>();
		this.reverse = null;
	}

	/**
	 * @param parent
	 * @param child
	 * @return true if child is a successor of parent. otherwise false.
	 */
	public boolean isSuccessor(int parent, int child)
	{
		TIntHashSet set = edges.get(parent);
		return (set != null)? set.contains(child) : false;
	}

	public boolean isLeaf(int i)
	{
		return getSuccessors(i).length == 0;
	}

	public int[] getConnected(int i)
	{
		TIntHashSet ret = new TIntHashSet();
		ret.addAll(getPredecessors(i));
		ret.addAll(getSuccessors(i));
		return (ret != null)? ret.toArray() : new int[0];
	}

	public int[] getSuccessors(int i)
	{
		TIntHashSet set = edges.get(i);
		return (set != null) ? set.toArray() : new int[0];
	}

	public int[] getPredecessors(int i)
	{
		return reverse.getSuccessors(i);
	}

	/**
	 * @param i specifies node ID.
	 * @return IDs which are reachable from i in depth-first manner.
	 */
	public int[] reachableFrom(int i)
	{
		TIntSet ret = new TIntHashSet();
		TIntArrayStack worklist = new TIntArrayStack();
		worklist.push(i);

		while (worklist.size() > 0)
		{
			int w = worklist.pop();
			ret.add(w);
			for(int c : getSuccessors(w))
			{
				if(!ret.contains(c))
				{
					worklist.push(c);
				}
			}
		}
		return ret.toArray();
	}

	/**
	 * @param i specifies node ID.
	 * @return IDs which are reachable to i in depth-first manner.
	 */
	public int[] reachableTo(int i)
	{
		return reverse.reachableFrom(i);
	}

	public int[] getSource()
	{
		TIntHashSet ret = new TIntHashSet();
		ret.addAll(nodes);
		ret.removeAll(reverse.edges.keySet());
		return ret.toArray();
	}

	public int[] getSinks()
	{
		return reverse.getSource();
	}

	public DirectedGraph getReversedGraph()
	{
		return reverse;
	}

	/**
	 * @return node size
	 */
	public int size()
	{
		return nodes.size();
	}

	/**
	 * @return all node ids in added order.
	 */
	public int[] getNodes()
	{
		return nodes.toArray();
	}

	/**
	 * @param id is node id.
	 * @return true if the ndoe id is contained.
	 */
	public boolean contains(int id)
	{
		return nodes.contains(id);
	}

	/**
	 * @return edge size
	 */
	public int edgeSize()
	{
		int sum = 0;
		for(int from :edges.keys())
		{
			TIntHashSet set = edges.get(from);
			sum = sum + set.size();
		}
		return sum;
	}

	public void forEachEdge(EdgeVisitor visitor)
	{
		for(int from : edges.keys())
		{
			for(int to : edges.get(from).toArray())
			{
				boolean isContinue = visitor.perform(from, to);
				if(!isContinue) return;
			}
		}
	}

	public DirectedGraph[] dividedGraphs()
	{
		ArrayList<DirectedGraph> graphList = new ArrayList<DirectedGraph>();
		TIntLinkedList remaining = new TIntLinkedList();
		remaining.addAll(getNodes());

		while(!remaining.isEmpty()){
			TIntStack worklist = new TIntArrayStack();
			int start = remaining.removeAt(0);
			worklist.push(start);
			remaining.remove(start);
			DirectedGraph graph = new DirectedGraph();

			while(worklist.size() > 0)
			{
				int from = worklist.pop();
				int[] connected = getConnected(from);
				for(int to : connected)
				{
					if(isSuccessor(from, to))	graph.putEdge(from, to);
					if(remaining.contains(to)){
						worklist.push(to);
						remaining.remove(to);
					}
				}
			}
			graphList.add(graph);
		}
		DirectedGraph[] ret = new DirectedGraph[graphList.size()];
		graphList.toArray(ret);
		return ret;
	}

	/**
	 * for debug.
	 */
	final public void dumpEdges()
	{
		dumpEdges("");
	}

	/**
	 * for debug.
	 */
	final public void dumpEdges(String title)
	{
		System.out.println("----------"+title+"----------");
		for(int from :edges.keys())
		{
			TIntHashSet set = edges.get(from);
			for(int to : set.toArray())
			{
				System.out.println(dumpLineStr(from, to));
			}
		}
		for(int i = 0 ; i < title.length(); i++) System.out.print("-");
		System.out.println("--------------------");
	}

	protected String dumpLineStr(int from , int to)
	{
		return from +" -> "+ to;
	}

	protected void createReverseGraphInstance()
	{
		reverse = new DirectedGraph();
	}

	final protected void importEdgesFrom(DirectedGraph ret)
	{
		for(int from : ret.edges.keys())
		{
			for(int to : ret.edges.get(from).toArray())
			{
				putEdge(from, to);
			}
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
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
		DirectedGraph other = (DirectedGraph) obj;
		if (edges == null)
		{
			if (other.edges != null)
				return false;
		}
		else if (!edges.equals(other.edges))
			return false;
		if (nodes == null)
		{
			if (other.nodes != null)
				return false;
		}
		else if (!nodes.equals(other.nodes))
			return false;
		return true;
	}
}
