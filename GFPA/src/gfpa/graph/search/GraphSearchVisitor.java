package gfpa.graph.search;

public interface GraphSearchVisitor {
	/**
	 * @param id
	 * @return Return true if continue to search.Return false if stop searching.
	 */
	public boolean onVisit(int id);

	/**
	 * @param id
	 * @return Return true if continue to search.Return false if stop searching.
	 */
	public boolean onLeave(int id);

}
