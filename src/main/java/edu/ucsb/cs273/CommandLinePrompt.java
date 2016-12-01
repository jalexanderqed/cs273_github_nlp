package edu.ucsb.cs273;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by john on 11/30/16.
 */
public class CommandLinePrompt {
    UserScores userSet;

    public CommandLinePrompt(UserScores u) {
        userSet = u;
    }

    public void run() {
        Scanner reader = new Scanner(System.in);
        String query;
        do {
            System.out.println("Enter a query:");
            query = reader.nextLine();
            ArrayList<StringDoubleTuple> users = userSet.getUsersForString(query);
            for (StringDoubleTuple tup : users) {
                System.out.println(tup);
            }
        } while (!query.equals("exit"));
    }
}