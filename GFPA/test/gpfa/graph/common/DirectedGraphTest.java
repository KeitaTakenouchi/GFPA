package gpfa.graph.common;

import static org.junit.Assert.*;
import gfpa.graph.common.DirectedGraph;

import java.util.Arrays;

import org.junit.Test;

public class DirectedGraphTest
{
	private DirectedGraph getDCG()
	{
		DirectedGraph graph = new DirectedGraph();
		graph.putEdge(0, 1);
		graph.putEdge(0, 3);
		graph.putEdge(1, 2);
		graph.putEdge(3, 2);
		graph.putEdge(3, 4);//cyclic edge
		graph.putEdge(4, 3);//cyclic edge
		graph.putEdge(2, 5);
		graph.putEdge(4, 5);
		return graph;
	}

	private DirectedGraph getDAG()
	{
		DirectedGraph graph = new DirectedGraph();
		graph.putEdge(0, 1);
		graph.putEdge(0, 3);
		graph.putEdge(1, 2);
		graph.putEdge(3, 2);
		graph.putEdge(3, 4);
		graph.putEdge(2, 5);
		graph.putEdge(4, 5);
		return graph;
	}

	@Test
	public void test01()
	{
		DirectedGraph graph = getDAG();

		assertEquals(graph.size(), 6);
		{
			int[] expected = {2,4};
			int[] actual = graph.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
		{
			int[] expected = {5};
			int[] actual = graph.getSuccessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
		{
			int[] expected = {1,3};
			int[] actual = graph.getPredecessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
		{
			int[] expected = {2,4};
			int[] actual = graph.getPredecessors(5);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
	}

	@Test
	public void test02()
	{
		DirectedGraph graph = getDAG();
		{
			int[] expected = {5};
			int[] actual = graph.reachableFrom(5);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
		{
			int[] expected = {2,3,4,5};
			int[] actual = graph.reachableFrom(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1,2,5};
			int[] actual = graph.reachableFrom(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,1,2,3,4,5};
			int[] actual = graph.reachableFrom(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test03()
	{
		DirectedGraph graph = getDCG();
		{
			int[] expected = {5};
			int[] actual = graph.reachableFrom(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,3,4,5};
			int[] actual = graph.reachableFrom(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1,2,5};
			int[] actual = graph.reachableFrom(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,1,2,3,4,5};
			int[] actual = graph.reachableFrom(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test04()
	{
		DirectedGraph graph = getDCG();
		{
			int[] expected = {0,1,2,3,4,5};
			int[] actual = graph.reachableTo(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,3,4};
			int[] actual = graph.reachableTo(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,1};
			int[] actual = graph.reachableTo(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0};
			int[] actual = graph.reachableTo(0);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}


	@Test
	public void test05()
	{
		DirectedGraph graph = getDCG();
		{
			int[] expected = {0};
			int[] actual = graph.getSource();
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {5};
			int[] actual = graph.getSinks();
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

}
