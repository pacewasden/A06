/*******************************************
 * Author(s): Pace Wasden, Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Posch
 * Assignment: WordNet
 *******************************************/

package a06;

import java.util.ArrayList;
import java.util.Stack;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

/**
 * Class SAP provides the ancestral path between two vertices v and w in a
 * digraph. A shortest ancestral path is an ancestral path of minimum total
 * length.
 * 
 * @author Pace Wasden and Spencer Rosenvall
 *
 */
public class SAP {

	private Digraph graph;

	/**
	 * Constructor takes a digraph (not necessarily a DAG).
	 * 
	 * @param G
	 */
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException("Diagraph cannot be null");
		}
		this.graph = G;

	}

	/**
	 * Checks if the digraph a directed acyclic graph?
	 * 
	 * @return boolean
	 */
	public boolean isDAG() {
		return !new DirectedCycle(graph).hasCycle();

	}

	/**
	 * Checks if the digraph a rooted DAG.
	 * 
	 * @return boolean
	 */
	public boolean isRootedDAG() {
		DepthFirstOrder d = new DepthFirstOrder(this.graph);
		Integer root = d.post().iterator().next();

		DepthFirstDirectedPaths dfP = new DepthFirstDirectedPaths(graph.reverse(), root);
		for (int i = 0; i < graph.V(); i++) {
			if (!dfP.hasPathTo(i))
				return false;
		}
		return true;
	}

	/**
	 * Returns length of shortest ancestral path between v and w; -1 if no such
	 * path.
	 * 
	 * @param v
	 * @param w
	 * @return int
	 */
	public int length(int v, int w) {
		int root = 0;
		int length = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (!graph.adj(i).iterator().hasNext())
				root = i;
		}

		Stack<Integer> stackV = new Stack<>();
		stackV.push(v);
		Stack<Integer> stackW = new Stack<>();
		stackW.push(w);
		Stack<Integer> vStack2 = new Stack<Integer>();
		for (int i = 0; i < stackV.size(); i++) {
			stackV.push(stackV.pop());
		}
		Stack<Integer> w2Stack = new Stack<Integer>();
		for (int i = 0; i < stackW.size(); i++) {
			w2Stack.push(stackW.pop());
		}

		while (!vStack2.isEmpty()) {
			if (vStack2.peek().equals(w2Stack.peek())) {
				vStack2.pop();
				w2Stack.pop();
			} else
				break;
			length = vStack2.size() + w2Stack.size();
		}
		return length;
	}

	/**
	 * Returns the common ancestor of v and w that participates in a shortest
	 * ancestral path; -1 if no such path.
	 * 
	 * @param v
	 * @param w
	 * @return int
	 */
	public int ancestor(int v, int w) {
		int root = 0;
		int min = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (!graph.adj(i).iterator().hasNext())
				root = i;
		}

		Stack<Integer> stackV = new Stack<>();
		stackV.push(v);
		Stack<Integer> stackW = new Stack<>();
		stackW.push(w);
		Stack<Integer> vStack2 = new Stack<Integer>();
		for (int i = 0; i < stackV.size(); i++) {
			stackV.push(stackV.pop());
		}
		Stack<Integer> w2Stack = new Stack<Integer>();
		for (int i = 0; i < stackW.size(); i++) {
			w2Stack.push(stackW.pop());
		}

		while (!vStack2.isEmpty()) {
			if (vStack2.peek().equals(w2Stack.peek())) {
				vStack2.pop();
				w2Stack.pop();
			}
			min = vStack2.size() + w2Stack.size();
		}
		return min;
	}

	/**
	 * Returns the length of shortest ancestral path between any vertex in v and
	 * any vertex in w; -1 if no such path.
	 * 
	 * @param v
	 * @param w
	 * @return int
	 */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int min = Integer.MAX_VALUE;
		int l;
		for (int _v : v) {
			for (int _w : w) {
				l = length(_v, _w);
				if (l < min)
					min = l;
			}
		}
		return min;
	}

	/**
	 * Returns the common ancestor that participates in shortest ancestral path;
	 * -1 if no such path.
	 * 
	 * @param v
	 * @param w
	 * @return int
	 */
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		BreadthFirstDirectedPaths _v = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths _w = new BreadthFirstDirectedPaths(graph, w);
		ArrayList<Integer> ancestors = new ArrayList<Integer>();

		for (int i = 0; i < graph.V(); i++) {
			if (_v.hasPathTo(i) && _w.hasPathTo(i)) {
				ancestors.add(i);
			}
		}
		int min = Integer.MAX_VALUE;
		int ancestor = -1;
		for (int a : ancestors) {
			int dist = _v.distTo(a) + _w.distTo(a);
			if (dist < min) {
				min = dist;
				ancestor = a;
			}
		}
		return ancestor;
	}
}
