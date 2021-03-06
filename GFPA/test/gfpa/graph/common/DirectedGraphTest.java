package gfpa.graph.common;

import static org.junit.Assert.*;
import gfpa.graph.search.EdgeVisitor;
import gnu.trove.set.hash.TIntHashSet;

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
		{
			int[] expected = {1,3,5};
			int[] actual = graph.getConnected(2);
			Arrays.sort(actual);
			assertArrayEquals(expected,actual);
		}
		{
			int[] expected = {1,3};
			int[] actual = graph.getConnected(0);
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

	@Test
	public void test06()
	{
		DirectedGraph graph = new DirectedGraph();
		graph.putEdge(0, 1);
		graph.putEdge(1, 4);
		graph.putEdge(1, 5);
		graph.putEdge(4, 5);

		TIntHashSet fromSet = new TIntHashSet();
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				fromSet.add(from);
				return true;
			}
		});

		{
			int[] expected = {0,1,4};
			int[] actual = fromSet.toArray();
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test07()
	{
		DirectedGraph a = new DirectedGraph();
		a.putEdge(0, 1);
		a.putEdge(1, 2);
		a.putEdge(2, 3);
		a.putEdge(1, 3);
		DirectedGraph b = new DirectedGraph();
		b.putEdge(0, 1);
		b.putEdge(1, 2);
		b.putEdge(2, 3);
		b.putEdge(1, 3);
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}

	@Test
	public void test08()
	{
		DirectedGraph a = new DirectedGraph();
		a.putEdge(0, 1);
		a.putEdge(1, 2);
		a.putEdge(2, 3);
		a.putEdge(1, 3);
		a.clear();

		assertEquals(0, a.size());
		assertEquals(0, a.edgeSize());

		a.putEdge(0, 1);
		assertEquals(2, a.size());
		assertEquals(1, a.edgeSize());
	}


	@Test
	public void test09()
	{
		DirectedGraph a = new DirectedGraph();
		a.putEdge(0, 1);
		a.putEdge(1, 2);
		a.putEdge(2, 3);
		a.putEdge(1, 3);

		assertTrue(a.isLeaf(3));
		assertFalse(a.isLeaf(2));
	}

	@Test
	public void test10()
	{
		DirectedGraph g = new DirectedGraph();
		g.putEdge(0, 1);
		g.putEdge(1, 2);
		g.putEdge(1, 3);
		g.putEdge(10,11);
		g.putEdge(10,12);
		g.putEdge(11,13);
		g.putEdge(13,14);
		g.putEdge(20,21);
		g.putEdge(21,22);
		g.putEdge(30,31);
		g.putEdge(32,31);
		DirectedGraph[] graphs = g.dividedGraphs();

		int nodes = 0;
		for(int i = 0 ; i < graphs.length ; i++)
			nodes = nodes + graphs[i].size();

		int edges = 0;
		for(int i = 0 ; i < graphs.length ; i++)
			edges = edges + graphs[i].edgeSize();

		assertEquals(g.size(), nodes);
		assertEquals(g.edgeSize(), edges);
		assertEquals(4, graphs.length);
	}

	@Test
	public void test11()
	{
		DirectedGraph graph = new DirectedGraph();
		assertNull(graph.getReversedGraph());
	}

	@Test
	public void test12()
	{
		DirectedGraph graph = new DirectedGraph();
		assertFalse(graph.isLeaf(1000));
	}

	@Test
	public void test13()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.edgeSize(), 0);
	}

	@Test
	public void test14()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.size(), 0);
	}

	@Test
	public void test15()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.dividedGraphs().length, 0);
	}

	@Test
	public void test16()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.getNodes().length, 0);
	}

	@Test
	public void test17()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.getSource().length, 0);
	}

	@Test
	public void test18()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.getSinks().length, 0);
	}

	@Test
	public void test19()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.getSuccessors(1).length, 0);
	}

	@Test
	public void test20()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.reachableFrom(1).length, 0);
	}

	@Test
	public void test21()
	{
		DirectedGraph graph = new DirectedGraph();
		assertEquals(graph.reachableTo(1).length, 0);
	}

}
