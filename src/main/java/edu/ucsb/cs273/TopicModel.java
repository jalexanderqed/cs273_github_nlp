package edu.ucsb.cs273;

import cc.mallet.pipe.tsf.RegexMatches;
import cc.mallet.util.*;
import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

/**
 * Created by john on 11/22/16.
 */

public class TopicModel implements Serializable{
    private String directory;
    File modelLocation;
    File instancesLocation;
    InstanceList instances;
    ParallelTopicModel model;
    int numTopics = 100;

    public TopicModel(String targetDir, File modelLoc, File instancesLoc){
        directory = targetDir;
        modelLocation = modelLoc;
        instancesLocation = instancesLoc;
    }

    public TopicModel(){
        modelLocation = Main.modelSaveFile;
        instancesLocation = Main.instancesSaveFile;
    }

    public void load() throws Exception{
        model = ParallelTopicModel.read(modelLocation);
        instances = InstanceList.load(instancesLocation);
    }

    public void testLoad() throws Exception{
        load();

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.out.println(out);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t", topic);
            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
            System.out.println(out);
        }

        if(true) return;

        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPair = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
            rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        for(int i = 0; i < testProbabilities.length; i++) {
            System.out.println(i + "\t" + testProbabilities[i]);
        }
        System.out.println(topicZeroText.toString());
    }

    public void run() throws Exception {
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = Util.getStandardPipes();

        instances = new InstanceList(new SerialPipes(pipeList));

        instances.addThruPipe(new FileIterator(directory));

        model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        model.setNumIterations(2000);
        model.estimate();

        model.write(modelLocation);
        System.out.println("Saved model to " + modelLocation.getAbsolutePath());
        instances.save(instancesLocation);;
        System.out.println("Saved instances to " + instancesLocation.getAbsolutePath());
    }
}