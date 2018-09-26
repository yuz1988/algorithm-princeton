package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {

    // <36, "AND_circuit AND_gate">
    private final HashMap<Integer, String> idToSynsets;
    // Can a noun appear in more than one synset?
    // Absolutely. It will appear once for each meaning that the noun has.
    private final HashMap<String, List<Integer>> synsetToIds;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("synsets or hypernyms is null");
        }

        idToSynsets = new HashMap<>();
        synsetToIds = new HashMap<>();

        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);

        while (!synsetsIn.isEmpty()) {
            String[] tokens = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            String synsetStrings = tokens[1];
            idToSynsets.put(id, synsetStrings);

            String[] synsetTokens = synsetStrings.split(" ");
            for (String word : synsetTokens) {
                if (!synsetToIds.containsKey(word)) {
                    synsetToIds.put(word, new ArrayList<>());
                }
                synsetToIds.get(word).add(id);
            }
        }

        Digraph g = new Digraph(idToSynsets.size());
        while (!hypernymsIn.isEmpty()) {
            String[] tokens = hypernymsIn.readLine().split(",");
            int synsetId = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int hypernym = Integer.parseInt(tokens[i]);
                g.addEdge(synsetId, hypernym);
            }
        }

        // Sanity check.
        if (new DirectedCycle(g).hasCycle()) {
            throw new IllegalArgumentException("Not a DAG, has a cycle");
        }

        int numOfRoots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                numOfRoots++;
                if (numOfRoots > 1) {
                    throw new IllegalArgumentException("The input graph has more than one root");
                }
            }
        }

        sap = new SAP(g);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Searching word is null");
        }
        return synsetToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || !synsetToIds.containsKey(nounA) || nounB == null || !synsetToIds.containsKey(nounB)) {
            throw new IllegalArgumentException("Input of distance is null or not a WordNet noun");
        }
        return sap.length(synsetToIds.get(nounA), synsetToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || !synsetToIds.containsKey(nounA) || nounB == null || !synsetToIds.containsKey(nounB)) {
            throw new IllegalArgumentException("Input of distance is null or not a WordNet noun");
        }
        return idToSynsets.get(sap.ancestor(synsetToIds.get(nounA), synsetToIds.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("wordnet/graphs/digraph1.txt");
        Digraph g = new Digraph(in);
        SAP sap = new SAP(g);
        assert sap.length(3, 11) == 4 && sap.ancestor(3, 11) == 1;
        assert sap.length(9, 12) == 3 && sap.ancestor(9, 12) == 5;
        assert sap.length(7, 2) == 4 && sap.ancestor(7, 2) == 0;
        assert sap.length(1, 6) == -1 && sap.ancestor(1, 6) == -1;
    }
}