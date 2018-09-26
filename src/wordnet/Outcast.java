package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length < 2) {
            throw new IllegalArgumentException("illegal nouns");
        }

        String outCast = nouns[0];
        int outCastDist = 0;

        for (int i = 0; i < nouns.length; i++) {
            String s1 = nouns[i];
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    dist += wordNet.distance(s1, nouns[j]);
                }
            }
            if (dist > outCastDist) {
                outCastDist = dist;
                outCast = s1;
            }
        }

        return outCast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
