package gfpa.graph.common;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Type variable is the type of Label.
 * @author Takenouchi Keita
 */
public class LabeledDirectedGraph<T> extends DirectedGraph
{

	private HashMap<Edge, HashSet<T>> labels = new HashMap<>();

	public void putEdge(int from, int to, T label)
	{
		super.putEdge(from, to);
		Edge edge = new Edge(from, to);
		HashSet<T> set = labels.get(edge);
		if(set == null) set = new HashSet<>();
		set.add(label);
		labels.put(edge,set);
	}

	/**
	 * @param from
	 * @param to
	 * @return corresponding label or null otherwise.
	 */
	public HashSet<T> getLabels(int from, int to)
	{
		Edge edge = new Edge(from, to);
		HashSet<T> set = labels.get(edge);
		return (set != null)? set : null;
	}

	protected void importEdgesFrom(LabeledDirectedGraph<T> ret)
	{
		super.importEdgesFrom(ret);
		this.labels.putAll(ret.labels);
	}

	@Override
	protected String dumpLineStr(int from, int to)
	{
		HashSet<T> labelSet = getLabels(from, to);
		if(labelSet != null)
			return super.dumpLineStr(from, to) + " :"+ labelSet;
		else
			return super.dumpLineStr(from, to);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		LabeledDirectedGraph<?> other = (LabeledDirectedGraph<?>) obj;
		if (labels == null)
		{
			if (other.labels != null) return false;
		}
		else if (!labels.equals(other.labels)) return false;
		return true;
	}

	private class Edge
	{
		public int from;
		public int to;

		public Edge(int from, int to)
		{
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + from;
			result = prime * result + to;
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
			@SuppressWarnings("unchecked")
			Edge other = (Edge) obj;
			if (from != other.from)
				return false;
			if (to != other.to)
				return false;
			return true;
		}
	}

}
