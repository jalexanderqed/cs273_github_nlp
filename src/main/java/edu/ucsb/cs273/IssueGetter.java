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
import java.nio.charset.Charset;
import java.util.Properties;

public class IssueGetter {
    private String repository;
    File usersSaveFile = null;
    HashMap<String, User> users = new HashMap<>();

    public IssueGetter() {
        repository = Main.repository;
        usersSaveFile = Main.usersSaveFile;
    }

    public void read() throws Exception {
        String oauth;
        try {
            Charset charset = Charset.forName("ISO-8859-1");
            oauth = Util.readLines("target/classes/secrets/oauth.txt").get(0);
        } catch (NoSuchFileException e) {
            System.out.println("Please place a personal GitHub access token in src/main/resources/secrets/oauth.txt");
            return;
        }
        GitHub gitHub = GitHub.connectUsingOAuth(oauth);

        String issuesDir = Util.getIssuesDir(repository);
        String usersDir = Util.getUsersDir(repository);
        new File(issuesDir).mkdirs();
        new File(usersDir).mkdirs();

        int count = 0;
        int pullCount = 0;
        int pullIssCount = 0;
        GHRepository rocksdb = gitHub.getRepository(repository);
        PagedIterable<GHIssue> issues = rocksdb.listIssues(GHIssueState.ALL);
        for (GHIssue issue : issues) {
            try {
                PrintWriter issueWriter = new PrintWriter(issuesDir + issue.getNumber() + ".txt", "UTF-8");
                issueWriter.println(issue.getBody());
                List<GHIssueComment> comments = issue.getComments();
                GHUser originalPoster = issue.getUser();
                String originalPosterId = originalPoster.getLogin();

                Properties relatedUsers = new Properties();

                if (issue.isPullRequest()) {
                    addUser(originalPoster, issue.getNumber());
                    relatedUsers.setProperty(originalPosterId, "2");
                }

                for (GHIssueComment comment : comments) {
                    issueWriter.println(comment.getBody());
                    GHUser poster = comment.getUser();
                    String posterId = poster.getLogin();
                    if (!originalPoster.equals(posterId)) {
                        addUser(poster, issue.getNumber());
                        relatedUsers.setProperty(posterId, "1");
                    }
                }

                issueWriter.close();

                PrintWriter usersWriter = new PrintWriter(usersDir + issue.getNumber() + ".properties", "UTF-8");
                relatedUsers.store(usersWriter, "");
            } catch (IOException e) {
                throw e;
            }
            count++;
            System.out.println(count);
        }

        System.out.println("Got " + count + " issues.");
        Util.writeUsers(usersSaveFile, users);
    }

    private void addUser(GHUser ghUser, int issue) {
        String login = ghUser.getLogin();
        User user;
        if ((user = users.get(login)) == null) {
            user = new User(login);
            user.avatar = ghUser.getAvatarUrl();
            users.put(user.id, user);
        }
        user.issues.add(issue);
    }
}
