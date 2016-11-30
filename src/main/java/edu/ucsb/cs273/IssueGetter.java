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
import java.util.HashMap;
import java.util.List;

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
        int pullCount = 0;
        int pullIssCount = 0;
        GHRepository rocksdb = gitHub.getRepository(repository);
        PagedIterable<GHIssue> issues = rocksdb.listIssues(GHIssueState.ALL);
        for (GHIssue issue : issues) {
            try {
                PrintWriter issueWriter = new PrintWriter(filePrefix + issue.getNumber() + ".txt", "UTF-8");
                issueWriter.println(issue.getBody());
                List<GHIssueComment> comments = issue.getComments();
                String originalPoster = issue.getUser().getLogin();

                HashMap<String, Integer> relatedUsers = new HashMap<String, Integer>();

                for(GHIssueComment comment : comments){
                    issueWriter.println(comment.getBody());
                    String poster = comment.getUser().getLogin();
                    if(!originalPoster.equals(poster)) {
                        relatedUsers.put(poster, 1);
                    }
                }

                if(issue.isPullRequest()) {
                    //relatedUsers.put(originalPoster, 2);
                }

                issueWriter.close();
            } catch (IOException e) {
                throw e;
            }
            count++;
            System.out.println(count);
        }

        System.out.println("Got " + count + " issues.");
    }
}
