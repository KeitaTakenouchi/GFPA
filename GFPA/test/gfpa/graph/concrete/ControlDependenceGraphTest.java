package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;



public class ControlDependenceGraphTest
{

	@Test
	public void test()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 7);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(3, 5);
		cfgraph.putEdge(4, 6);
		cfgraph.putEdge(5, 6);
		cfgraph.putEdge(6, 7);
		cfgraph.putEdge(7, 1);
		cfgraph.putEdge(7, 8);

		ControlDependenceGraph cdg = new ControlDependenceGraph(cfgraph);
		cdg.dumpEdges();
		{
			int[] expected = {1,7};
			int[] actual = cdg.getSuccessors(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,3,6};
			int[] actual = cdg.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = cdg.getSuccessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {4,5};
			int[] actual = cdg.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = cdg.getSuccessors(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = cdg.getSuccessors(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = cdg.getSuccessors(6);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1,7};
			int[] actual = cdg.getSuccessors(7);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}

	}

}
