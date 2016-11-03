package edu.ucsb.cs273;

import org.kohsuke.github.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

/**
 * Created by jalexander on 10/20/2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String oauth;
        try {
            oauth = Files.readAllLines(Paths.get("target/classes/secrets/oauth.txt")).get(0);
        }
        catch(NoSuchFileException e){
            System.out.println("Please place a personal GitHub access token in src/main/resources/secrets/oauth.txt");
            return;
        }
        GitHub gitHub = GitHub.connectUsingOAuth(oauth);

        int count = 0;
        GHRepository rocksdb = gitHub.getRepository("facebook/rocksdb");
        PagedIterable<GHIssue> issues = rocksdb.listIssues(GHIssueState.OPEN);
        for(GHIssue issue : issues){
            System.out.println(issue.getBody());
            count++;
        }

        System.out.println("Count: " + count);
    }
}
