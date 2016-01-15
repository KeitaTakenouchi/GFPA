package gpfa.graph.concrete;

import gfpa.graph.common.DirectedGraph;

public class ControlFlowGraph extends DirectedGraph
{
	private int entryId;
	private static final int dummyExit = -1;

	public ControlFlowGraph(int entryId)
	{
		this.entryId = entryId;
	}

	@Override
	protected void createReverｓeGraphInstance()
	{
		//entryId is not used.
		this.reverse = new ControlFlowGraph(dummyExit);
	}

	public int getEntryId()
	{
		return entryId;
	}

	@Override
	public ControlFlowGraph getReversedGraph()
	{
		ControlFlowGraph reverse = null;
		int[] sinks = getSinks();

		if(sinks.length == 1)
		{
			reverse = new ControlFlowGraph(sinks[0]);
		}
		else
		{
			reverse = new ControlFlowGraph(dummyExit);
			for(int i : sinks)
			{
				reverse.putEdge(dummyExit, i);
			}
		}
		copyReverseGraphTo(reverse);
		return reverse;
	}

}
