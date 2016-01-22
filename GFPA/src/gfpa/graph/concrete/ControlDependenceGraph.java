package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;

public class ControlDependenceGraph extends DirectedGraph
{
	private DominanceFrontier controlDependence;
	private int rootId;

	public ControlDependenceGraph(ControlFlowGraph cfgraph)
	{
		this.rootId = cfgraph.getEntryId();

		//add edge from entry to exit to show nodes.
		if(cfgraph.getSuccessors(rootId).length < 2)
		{
			int[] sinks = cfgraph.getSinks();
			if(sinks.length > 0)
				cfgraph.putEdge(rootId, sinks[0]);
		}

		this.controlDependence = new DominanceFrontier(cfgraph.getReversedGraph());
		//convert into edges.
		for(int i = 0 ; i < controlDependence.size() ; i++)
		{
			for(int j : controlDependence.dominanceFrontier(i))
			{
				putEdge(j, i);
			}
		}

	}

	public int[] controlDependence(int x)
	{
		return controlDependence.dominanceFrontier(x);
	}

	public int getEntryId()
	{
		return rootId;
	}
}
