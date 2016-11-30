package edu.ucsb.cs273;

/**
 * Created by john on 11/30/16.
 */

import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class IssueGetter {
    private String repository;

    public IssueGetter(String repoName) {
        repository = repoName;
    }

    public void read() throws IOException {
        String oauth;
        try {
            oauth = Files.readAllLines(Paths.get("target/classes/secrets/oauth.txt")).get(0);
        } catch (NoSuchFileException e) {
            System.out.println("Please place a personal GitHub access token in src/main/resources/secrets/oauth.txt");
            return;
        }
        GitHub gitHub = GitHub.connectUsingOAuth(oauth);


        new File("output/" + repository).mkdirs();
        String filePrefix = "output/" + repository + "/";

        int count = 0;
        GHRepository rocksdb = gitHub.getRepository(repository);
        PagedIterable<GHIssue> issues = rocksdb.listIssues(GHIssueState.ALL);
        for (GHIssue issue : issues) {
            try {
                PrintWriter writer = new PrintWriter(filePrefix + issue.getNumber() + ".txt", "UTF-8");
                writer.println(issue.getBody());
                writer.close();
            } catch (IOException e) {
                throw e;
            }
            count++;
        }

        System.out.println("Got " + count + " issues.");
    }
}
