package gpfa.graph.search;

public interface EdgeVisitor
{
	/**
	 * @param from specifies the start of a edge.
	 * @param to specifies the end of a edge.
	 * @return return true if continue searching or return false otherwise.
	 */
	public boolean perform(int from , int to);
}
