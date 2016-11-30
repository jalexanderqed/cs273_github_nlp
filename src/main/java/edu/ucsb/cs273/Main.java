package edu.ucsb.cs273;

/**
 * Created by jalexander on 10/20/2016.
 */

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            out.println("Options:");
            out.println("\tget:\tGet issues from GitHub");
            out.println("\trun:\tRun topic modelling");
            return;
        }

        String repository = "facebook/rocksdb";

        if(args[0].equals("get")) {
            IssueGetter rockDbIssues = new IssueGetter(repository);
            rockDbIssues.read();
        }
        else if(args[0].equals("run")) {
            TopicModel topicModel = new TopicModel("output/" + repository);
            topicModel.run();
        }
    }
}
