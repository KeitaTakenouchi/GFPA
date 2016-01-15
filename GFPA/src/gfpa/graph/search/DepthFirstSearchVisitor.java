package gfpa.graph.search;

public interface DepthFirstSearchVisitor {
	/**
	 * @param id
	 * @return Return true if continue to search.Return false if stop searching.
	 */
	public boolean onVisit(int id);

}
