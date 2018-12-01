/*******************************************
 * Author(s): Pace Wasden, Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Posch
 * Assignment: WordNet
 *******************************************/

package a06;

import java.util.Queue;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Class WordNet groups words into sets of synonyms called synsets and describes
 * semantic relationships between them.
 * 
 * @author Pace Wasden and Spencer Rosenvall
 *
 */
public class WordNet {
	private SAP sap;
	private Digraph dg;
	private SeparateChainingHashST<String, Queue<Integer>> w_i; // queue to
																// track words
																// becoming ints
	private SeparateChainingHashST<Integer, String> i_w; // queue to track ints
															// becoming words

	/**
	 * Constructor takes the name of the two input files and constructs a
	 * wordnet object.
	 * 
	 * @param synsets
	 * @param hypernyms
	 */
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException("Arguments cannot be null.");
		}

		In synset = new In(synsets);
		In hypernym = new In(hypernyms);
		int vertices = 0;

		w_i = new SeparateChainingHashST<>();
		i_w = new SeparateChainingHashST<>();

		while (synset.hasNextLine()) {
			String[] lines = synset.readLine().split(",");
			String[] words = lines[1].split(" ");
			Integer val = Integer.valueOf(lines[0]);
			i_w.put(Integer.valueOf(lines[0]), lines[1]);
			vertices++;

			for (int i = 0; i < words.length; i++) {
				Queue<Integer> wordToIntQueue = w_i.get(words[i]);

				if (wordToIntQueue != null) {
					if (!contains(wordToIntQueue, val)) {
						wordToIntQueue.enqueue(val);
					}
				} else {
					wordToIntQueue = new Queue<>();
					wordToIntQueue.enqueue(val);
					w_i.put(words[i], wordToIntQueue);
				}
			}
		}

		dg = new Digraph(vertices);

		while (hypernym.hasNextLine()) {
			String[] line = hypernym.readLine().split(",");
			for (int i = 1; i < line.length; i++) {
				dg.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
			}
		}
		sap = new SAP(dg);
	}

	/**
	 * Returns all WordNet nouns.
	 * 
	 * @return Iterable<String>
	 */
	public Iterable<String> nouns() {
		return w_i.keys();
	}

	/**
	 * Checks if word is a noun.
	 * 
	 * @return boolean
	 */
	public boolean isNoun(String word) {
		if (word == null) {
			throw new NullPointerException("Arguments cannot be null.");
		}
		return w_i.contains(word);
	}

	/**
	 * Returns distance between nounA and nounB (defined below).
	 * 
	 * @return int
	 */
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new NullPointerException("Arguments cannot be null.");
		}

		if (w_i.get(nounA) == null || w_i.get(nounB) == null) {
			throw new IllegalArgumentException("Nouns passed not included within the wordnet.");
		}

		Iterable<Integer> A = w_i.get(nounA);
		Iterable<Integer> B = w_i.get(nounB);
		return sap.length(A, B);
	}

	/**
	 * Returns a synset (second field of synsets.txt) that is the common
	 * ancestor of nounA and nounB in a shortest ancestral path (defined below)
	 * 
	 * @param nounA
	 * @param nounB
	 * @return String
	 */
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new NullPointerException("Arguments cannot be null.");
		}

		if (w_i.get(nounA) == null || w_i.get(nounB) == null) {
			throw new IllegalArgumentException("Nouns passed must be included within the wordnet.");
		}

		Iterable<Integer> intA = w_i.get(nounA);
		Iterable<Integer> intB = w_i.get(nounB);
		return i_w.get(sap.ancestor(intA, intB));
	}

	/**
	 * Private method to iterate through a data structure and checks for a
	 * specific item within it.
	 * 
	 * @param iterable
	 * @param item
	 * @return <Item>
	 */
	private <Item> boolean contains(Iterable<Item> iterable, Item item) {
		for (Item i : iterable) {
			if (i == item) {
				return true;
			}
		}
		return false;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
