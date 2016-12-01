package edu.ucsb.cs273;

import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;

import java.io.Serializable;
import java.util.*;

/**
 * Created by john on 11/30/16.
 */
public class User implements Serializable {
    public String id;
    public String avatar;
    public ArrayList<String> commonWords = new ArrayList<String>();
    public ArrayList<StringDoubleTuple> wordScores = new ArrayList<>();
    public HashSet<Integer> issues = new HashSet<>();

    public double[] topicDist;

    public User(String id){
        this.id = id;
    }

    public void setTopics(double[] dist){
        topicDist = dist;
        setupCommonWords();
    }

    public double[] getTopics(){
        return topicDist;
    }

    public void setupCommonWords(){
        ArrayList<TreeSet<IDSorter>> topicSortedWords = Main.topicModel.model.getSortedWords();
        Alphabet dataAlphabet = Main.topicModel.instances.getDataAlphabet();
        wordScores = new ArrayList<>();
        commonWords = new ArrayList<>();
        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < Main.topicModel.model.getNumTopics(); topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                wordScores.add(new StringDoubleTuple((String)dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight() * topicDist[topic]));
                rank++;
            }
        }

        Collections.sort(wordScores, new Comparator<StringDoubleTuple>() {
            @Override
            public int compare(StringDoubleTuple tuple1, StringDoubleTuple tuple2)
            {
                return -1 * tuple1.compareTo(tuple2);
            }
        });

        for(int i = 0; i < 100; i++){
            commonWords.add(wordScores.get(i).str);
        }
    }
}
