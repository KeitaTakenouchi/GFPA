package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.V;
import gfpa.graph.search.EdgeVisitor;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class ProgramDependenceGraph extends LabeledDirectedGraph<V>
{
	private TIntObjectHashMap<TIntHashSet> ddedges = new TIntObjectHashMap<TIntHashSet>();
	private TIntObjectHashMap<TIntHashSet> cdedges = new TIntObjectHashMap<TIntHashSet>();

	public ProgramDependenceGraph(){}

	public ProgramDependenceGraph(DataDependenceGraph ddgraph, ControlDependenceGraph cdgraph)
	{
		importEdgesFrom(ddgraph);
		importEdgesFrom(cdgraph);

		ddgraph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				putddedge(from, to);
				return true;
			}
		});

		cdgraph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				putcdedge(from, to);
				return true;
			}
		});
	}

	public ProgramDependenceGraph(DataDependenceGraph datafg, ControlFlowGraph cfg)
	{
		this(datafg, new ControlDependenceGraph(cfg));
	}

	public int[] backwardSlice(int i)
	{
		return reachableTo(i);
	}

	public int[] forwardSlice(int i)
	{
		return reachableFrom(i);
	}

	@Override
	public void putEdge(int from, int to, V label)
	{
		putddedge(from, to);
		super.putEdge(from, to, label);
	}

	@Override
	public void putEdge(int from, int to)
	{
		putcdedge(from, to);
		super.putEdge(from, to);
	}

	public boolean hasDataDependent(int parent, int child)
	{
		TIntHashSet set = ddedges.get(parent);
		return (set != null)? set.contains(child):false;
	}

	public boolean hasControlDependent(int parent, int child)
	{
		TIntHashSet set = cdedges.get(parent);
		return (set != null)? set.contains(child):false;
	}

	private void putddedge(int from, int to)
	{
		TIntHashSet set = ddedges.get(from);
		if(set == null) set = new TIntHashSet();
		set.add(to);
		ddedges.put(from, set);
	}

	private void putcdedge(int from, int to)
	{
		TIntHashSet set = cdedges.get(from);
		if(set == null) set = new TIntHashSet();
		set.add(to);
		cdedges.put(from, set);
	}
}
