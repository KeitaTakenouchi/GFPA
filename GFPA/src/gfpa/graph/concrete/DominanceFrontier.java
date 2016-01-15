package gfpa.graph.concrete;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.HashMap;

public class DominanceFrontier
{
	private HashMap<Integer, TIntSet> dominanceForntiers = new HashMap<Integer, TIntSet>();

	public DominanceFrontier(ControlFlowGraph cfgraph)
	{
		DominatorTree domtree = new DominatorTree(cfgraph);
		int root = cfgraph.getEntryId();
		buildDominanceFrontier(root, cfgraph, domtree);
	}

	/**
	 * resolving dominance frontiers by recursive calls in depth first order.
	 */
	private TIntSet buildDominanceFrontier(int x, ControlFlowGraph cfgraph, DominatorTree domtree)
	{
		TIntSet dfrontier = new TIntHashSet();
		for(int y : cfgraph.getSuccessors(x)){
			if(domtree.immediateDominator(y) != x){
				dfrontier.add(y);
			}
		}
		for(int z : domtree.getSuccessors(x)){
			for(int y : buildDominanceFrontier(z,cfgraph,domtree).toArray()){
				if(domtree.immediateDominator(y) != x){
					dfrontier.add(y);
				}
			}
		}
		dominanceForntiers.put(x, dfrontier);

		return dfrontier;
	}

	public int[] dominanceFrontier(int id)
	{
		return dominanceForntiers.get(id).toArray();
	}

	public int size()
	{
		return dominanceForntiers.size();
	}

}
