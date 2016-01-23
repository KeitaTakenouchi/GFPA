package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class DominanceFrontierTest
{

	@Test
	public void test()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(1);
		cfgraph.putEdge(1,2);
		cfgraph.putEdge(2,3);
		cfgraph.putEdge(2,7);
		cfgraph.putEdge(3,4);
		cfgraph.putEdge(3,5);
		cfgraph.putEdge(4,6);
		cfgraph.putEdge(5,6);
		cfgraph.putEdge(6,8);
		cfgraph.putEdge(7,8);
		cfgraph.putEdge(1,8);

		DominanceFrontier df = new DominanceFrontier(cfgraph);
		{
			int[] expected = {8};
			int[] actual = df.dominanceFrontier(7);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {8};
			int[] actual = df.dominanceFrontier(6);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {6};
			int[] actual = df.dominanceFrontier(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {6};
			int[] actual = df.dominanceFrontier(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {8};
			int[] actual = df.dominanceFrontier(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {8};
			int[] actual = df.dominanceFrontier(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = df.dominanceFrontier(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}

	}

}
