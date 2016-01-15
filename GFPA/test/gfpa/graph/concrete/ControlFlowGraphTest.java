package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ControlFlowGraphTest {

	@Test
	public void test01() {
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 5);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 4);
		cfgraph.putEdge(5, 4);
		cfgraph.putEdge(4, 1);
		cfgraph.putEdge(4, 3);
		cfgraph.putEdge(3, 6);
		cfgraph.putEdge(6, 3);
		cfgraph.putEdge(6, 7);

		{
			int expected = 0;
			int actual = cfgraph.getEntryId();
			assertEquals(expected, actual);
		}
		{
			int expected = 7;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
	}

	/**
	 * The target is cfg graph which have some exits.
	 */
	@Test
	public void test02() {
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(0, 2);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(3, 5);
		cfgraph.putEdge(5, 6);
		cfgraph.putEdge(6, 5);
		cfgraph.putEdge(6, 7);
		{
			int expected = 0;
			int actual = cfgraph.getEntryId();
			assertEquals(expected, actual);
		}
		{
			int expected = -1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
		{
			int[] expected = {1,2};
			int[] actual = cfgraph.getPredecessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test03() {
		ControlFlowGraph original = new ControlFlowGraph(0);
		original.putEdge(0, 1);
		original.putEdge(0, 2);
		original.putEdge(1, 3);
		original.putEdge(2, 3);
		original.putEdge(3, 4);//4 is exit.
		original.putEdge(3, 5);
		original.putEdge(5, 6);
		original.putEdge(6, 5);
		original.putEdge(6, 7);//7 is exit.
		ControlFlowGraph reversedGraph = original.getReversedGraph();

		{
			int expected = -5;
			int actual = reversedGraph.getEntryId();
			assertEquals(expected, actual);
		}
		{
			int[] expected = {1,2};
			int[] actual = reversedGraph.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1,2};
			int[] actual = original.getPredecessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test04() {
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		{
			int expected = 1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
		cfgraph.putEdge(0, 2);
		{
			int expected = -1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
		cfgraph.putEdge(1, 3);
		{
			int expected = -1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
		cfgraph.putEdge(2, 3);
		{
			int expected = 3;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
	}

}
