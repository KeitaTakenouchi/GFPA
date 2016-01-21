package gfpa.graph.common;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.junit.Test;

public class LabeledDirectedGraphTest
{

	@Test
	public void test01()
	{
		LabeledDirectedGraph<String> graph = new LabeledDirectedGraph<String>();
		graph.putEdge(0, 1, "0-1");
		graph.putEdge(0, 3);
		graph.putEdge(1, 2);
		graph.putEdge(3, 2);
		graph.putEdge(3, 4, "Hello");
		graph.putEdge(3, 4, "World");
		graph.putEdge(2, 5);
		graph.putEdge(4, 5);

		{
			HashSet<String> set = graph.getLabels(0,1);
			String[] label = new String[1];
			set.toArray(label);
			assertEquals("0-1"  ,label[0]);
		}
		{
			HashSet<String> set = graph.getLabels(3,4);
			String[] label = new String[2];
			set.toArray(label);
			Arrays.sort(label);
			assertEquals("Hello"  ,label[0]);
			assertEquals("World"  ,label[1]);
		}
		{
			assertNull(graph.getLabels(3,2));
		}
	}

	@Test
	public void test02()
	{
		LabeledDirectedGraph<Date> graph = new LabeledDirectedGraph<Date>();
		graph.putEdge(0, 1, new Date(1450422222222L));
		graph.putEdge(0, 3);
		graph.putEdge(1, 2);
		graph.putEdge(3, 2);
		graph.putEdge(3, 4, new Date(1450422235220L));
		graph.putEdge(2, 5);
		graph.putEdge(4, 5);

		{
			HashSet<Date> set = graph.getLabels(0,1);
			Date[] label = new Date[1];
			set.toArray(label);
			assertEquals(new Date(1450422222222L) ,label[0]);
		}
		{
			HashSet<Date> set = graph.getLabels(3,4);
			Date[] label = new Date[1];
			set.toArray(label);
			assertEquals(new Date(1450422235220L) ,label[0]);
		}
		{
			assertNull(graph.getLabels(3,2));
		}

	}

	@Test
	public void test03()
	{
		LabeledDirectedGraph<String> a = new LabeledDirectedGraph<String>();
		a.putEdge(0, 1, "hoge");
		a.putEdge(1, 2);
		a.putEdge(2, 3);
		a.putEdge(1, 3 , "aaa");

		LabeledDirectedGraph<String> b = new LabeledDirectedGraph<String>();
		b.putEdge(0, 1, "hoge");
		b.putEdge(1, 2);
		b.putEdge(2, 3);
		b.putEdge(1, 3 , "aaa");
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}

}
