package edu.ucsb.cs273;

/**
 * Created by jalexander on 10/20/2016.
 */

import java.io.*;
import java.util.HashMap;

import static java.lang.System.out;

public class Main {
    public static TopicModel topicModel;
    public static UserScores scores;
    public static File modelSaveFile = null;
    public static File instancesSaveFile = null;
    public static File usersSaveFile = null;
    public static String repository;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            out.println("Options:");
            out.println("\tget:\tGet issues from GitHub");
            out.println("\trun:\tRun topic modelling");
            out.println("\tload:\tLoad existing topic model");
            return;
        }

        if (args.length < 2) {
            out.println("Please provide a model data identifier as the second argument.");
            return;
        }

        repository = "facebook/rocksdb";
        modelSaveFile = new File("output/" + args[1] + "model.dat");
        instancesSaveFile = new File("output/" + args[1] + "instances.dat");
        usersSaveFile = new File("output/" + args[1] + "users.ser");

        if (args[0].equals("get")) {
            IssueGetter rockDbIssues = new IssueGetter();
            rockDbIssues.read();
        } else if (args[0].equals("run")) {
            topicModel = new TopicModel(Util.getIssuesDir(repository), modelSaveFile, instancesSaveFile);
            topicModel.run();
        } else if (args[0].equals("load")) {
            topicModel = new TopicModel();
            topicModel.testLoad();
        } else if (args[0].equals("score")) {
            topicModel = new TopicModel();
            topicModel.load();
            scores = new UserScores(topicModel);
            scores.run();
        } else if (args[0].equals("prompt")) {
            loadModelAndScores();
            CommandLinePrompt prompt = new CommandLinePrompt(scores);
            prompt.run();
        } else if (args[0].equals("serve")) {
            loadModelAndScores();
            Server server = new Server();
            server.run();
        }
    }

    private static void loadModelAndScores() throws Exception {
        topicModel = new TopicModel();
        topicModel.load();
        scores = new UserScores(topicModel);
    }
}
