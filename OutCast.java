/*******************************************
 * Author(s): Pace Wasden, Spencer Rosenvall
 * Class: CSIS 2420
 * Professor: Posch
 * Assignment: WordNet
 *******************************************/

package a06;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

/**
 * Class Outcast performs given a list of wordnet nouns A1, A2, ..., An, and
 * provides the noun that is least related to the others. It identifies an
 * outcast, compute the sum of the distances between each noun and every other
 * one:
 * 
 * di = dist(Ai, A1) + dist(Ai, A2) + ... + dist(Ai, An)
 * 
 * and return a noun At for which dt is maximum.
 * 
 * @author Pace Wasden and Spencer Rosenvall
 *
 */
public class OutCast {
	private WordNet wordnet;

	/**
	 * Constructs a WordNet object.
	 * 
	 * @param wordnet
	 */
	public OutCast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	/**
	 * Given an array of WordNet nouns, return an outcast
	 * 
	 * @param nouns
	 * @return String
	 */
	public String outcast(String[] nouns) {

		ST<Integer, String> st = new ST<>();
		MaxPQ<Integer> mpq = new MaxPQ<>();

		for (int i = 0; i < nouns.length; i++) {
			int distance = 0;
			for (int j = 0; j < nouns.length; j++) {
				distance += wordnet.distance(nouns[i], nouns[j]);
			}
			mpq.insert(distance);
			st.put(distance, nouns[i]);
		}
		return st.get(mpq.max());
	}

	// see test client below (not graded)
	public static void main(String[] args) {
		WordNet wordnet = new WordNet("src/a06/synsets.txt", "src/a06/hypernyms.txt");
		OutCast outcast = new OutCast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
