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
			int expected = -1;
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
			int expected = -1;
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
			int expected = -1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
		{
			int[] expected = {0};
			int[] actual = cfgraph.getReversedGraph().getSuccessors(1);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {-1};
			int[] actual = cfgraph.getReversedGraph().getPredecessors(1);
			assertArrayEquals(expected, actual);
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
			int expected = -1;
			int actual = cfgraph.getReversedGraph().getEntryId();
			assertEquals(expected, actual);
		}
	}

	@Test
	public void test05()
	{
		ControlFlowGraph a = new ControlFlowGraph(0);
		a.putEdge(0, 1);
		a.putEdge(1, 2);
		a.putEdge(2, 3);
		a.putEdge(1, 3);
		ControlFlowGraph b = new ControlFlowGraph(0);
		b.putEdge(0, 1);
		b.putEdge(1, 2);
		b.putEdge(2, 3);
		b.putEdge(1, 3);
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}

	@Test
	public void test06()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(3, 5);
		cfgraph.putEdge(8, 4);
		cfgraph.putEdge(4, 5);
		cfgraph.removeUnreacableNodes();
		{
			int[] expected = {3};
			int[] actual = cfgraph.getPredecessors(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1};
			int[] actual = cfgraph.getSuccessors(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int expected = 4;
			int actual = cfgraph.size();
			assertEquals(expected, actual);
		}
		{
			int expected = 3;
			int actual = cfgraph.edgeSize();
			assertEquals(expected, actual);
		}
	}

	@Test
	public void test07()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 3);
		ControlFlowGraph reverse = cfgraph.getReversedGraph();
		ControlFlowGraph revrev = reverse.getReversedGraph();
		{
			int[] expected = {1};
			int[] actual = revrev.getSuccessors(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,3};
			int[] actual = revrev.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {-1};
			int[] actual = revrev.getSuccessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {-1};
			int[] actual = revrev.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}
}
