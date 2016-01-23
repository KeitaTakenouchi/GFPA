package gfpa.graph.concrete;

import gfpa.graph.common.DirectedGraph;

public class ControlDependenceGraph extends DirectedGraph
{
	private DominanceFrontier controlDependence;
	private int rootId;

	public ControlDependenceGraph(ControlFlowGraph cfgraph)
	{
		this.rootId = cfgraph.getEntryId();

		ControlFlowGraph reversedGraph = cfgraph.getReversedGraph();
		reversedGraph.putEdge(reversedGraph.getEntryId(), rootId);

		this.controlDependence = new DominanceFrontier(reversedGraph);
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
