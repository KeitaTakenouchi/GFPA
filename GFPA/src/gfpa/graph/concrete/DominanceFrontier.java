package gfpa.graph.concrete;

import gfpa.graph.search.DepthFirstSearch;
import gfpa.graph.search.DepthFirstSearchVisitor;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class DominanceFrontier
{
	private TIntObjectHashMap<TIntSet>  dominanceFrontiers = new TIntObjectHashMap<>();;

	public DominanceFrontier(ControlFlowGraph cfgraph)
	{
		DominatorTree domtree = new DominatorTree(cfgraph);
		int root = cfgraph.getEntryId();
		buildDominanceFrontier(root, cfgraph, domtree);
	}

	/**
	 * resolving dominance frontiers in post order of depth first order.
	 */
	private void buildDominanceFrontier(int root, ControlFlowGraph cfgraph, DominatorTree domtree)
	{
		TIntList list = new TIntArrayList();
		DepthFirstSearch.search(domtree, root, new DepthFirstSearchVisitor()
		{
			@Override
			public boolean onVisit(int id)
			{
				return true;
			}

			@Override
			public boolean onLeave(int id)
			{
				list.add(id);
				return true;
			}
		});

		for(int x : list.toArray())
		{
			TIntSet dfrontier = new TIntHashSet();
			for(int y : cfgraph.getSuccessors(x)){
				if(domtree.immediateDominator(y) != x){
					dfrontier.add(y);
				}
			}
			for(int z : domtree.getSuccessors(x)){
				for(int y : dominanceFrontiers.get(z).toArray()){
					if(domtree.immediateDominator(y) != x){
						dfrontier.add(y);
					}
				}
			}
			dominanceFrontiers.put(x, dfrontier);
		}
	}

	public int[] dominanceFrontier(int id)
	{
		TIntSet set = dominanceFrontiers.get(id);
		return (set != null)? set.toArray() : new int[0];
	}

	public int size()
	{
		return dominanceFrontiers.size();
	}

}
