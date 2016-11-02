package edu.ucsb.cs273;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

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

        GHUser me = gitHub.getMyself();
        System.out.println(me.getName());
    }
}
