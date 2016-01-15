package gfpa.graph.concrete;

import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;

public class ProgramDependenceGraph extends LabeledDirectedGraph<Variable>
{
	public ProgramDependenceGraph(DataDependenceGraph datafg, ControlDependenceGraph cdg)
	{
		importEdgesFrom(datafg);
		importEdgesFrom(cdg);
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

}
