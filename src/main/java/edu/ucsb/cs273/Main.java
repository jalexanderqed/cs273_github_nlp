package edu.ucsb.cs273;

/**
 * Created by jalexander on 10/20/2016.
 */

import java.io.File;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 1){
            out.println("Options:");
            out.println("\tget:\tGet issues from GitHub");
            out.println("\trun:\tRun topic modelling");
            out.println("\tload:\tLoad existing topic model");
            return;
        }

        String repository = "facebook/rocksdb";

        File modelSaveFile = null;
        File instancesSaveFile = null;

        if(args[0].equals("run") || args[0].equals("load")) {
            if(args.length < 2){
                out.println("Please provide a model data identifier as the second argument.");
                return;
            }
            modelSaveFile = new File(args[1] + "model.ser");
            instancesSaveFile = new File(args[1] + "instances.ser");
        }

        if(args[0].equals("get")) {
            IssueGetter rockDbIssues = new IssueGetter(repository);
            rockDbIssues.read();
        }
        else if(args[0].equals("run")) {
            TopicModel topicModel = new TopicModel("output/" + repository, modelSaveFile, instancesSaveFile);
            topicModel.run();
        }
        else if(args[0].equals("load")){
            TopicModel topicModel = new TopicModel(modelSaveFile, instancesSaveFile);
            topicModel.load();
        }
    }
}
