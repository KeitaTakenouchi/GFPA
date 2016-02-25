package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;

public class ProgramDependenceGraph extends LabeledDirectedGraph<Variable>
{
	private DataDependenceGraph ddgraph;
	private ControlDependenceGraph cdgraph;
	public ProgramDependenceGraph(DataDependenceGraph ddgraph, ControlDependenceGraph cdgraph)
	{
		this.ddgraph = ddgraph;
		this.cdgraph = cdgraph;
		importEdgesFrom(ddgraph);
		importEdgesFrom(cdgraph);
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

	public boolean hasDataDependent(int parent, int child)
	{
		return ddgraph.isSuccessor(parent, child);
	}

	public boolean hasControlDependent(int parent, int child)
	{
		return cdgraph.isSuccessor(parent, child);
	}

}
