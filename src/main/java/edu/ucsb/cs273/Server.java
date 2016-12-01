package edu.ucsb.cs273;

/**
 * Created by john on 11/30/16.
 */

import com.google.gson.Gson;
import edu.ucsb.cs273.JSON.JsonUser;
import edu.ucsb.cs273.JSON.JsonUserQuery;
import edu.ucsb.cs273.JSON.UserQueryResponse;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.*;

public class Server {
    HashMap<String, User> users;
    Gson gson = new Gson();

    public Server() throws Exception{
        users = Main.scores.userScores;
        /*
        for(User u : users.values()){
            u.setupCommonWords();
        }
        Util.writeUsers(Main.usersSaveFile, users);
        */
    }

    public void run() throws Exception {
        System.out.println("Starting server");
        port(4567);

        staticFiles.externalLocation("public");

        get("/hello", (request, response) -> "Hello world");

        post("/api/users/search", (request, response) -> {
            return searchUsers(request, response);
        });

        get("/users/:name", (request, response) -> Util.readText("public/user.html"));

        get("/api/users/:name", (request, response) -> getUser(request, response));
    }

    private String getUser(Request request, Response response) {
        String userName = request.params("name");

        User userObj = users.get(userName);
        if(userObj == null){
            halt(404);
            return "";
        }
        JsonUser res = new JsonUser(userObj);

        response.status(201);
        response.header("Content-Type", "application/json");
        String resString = gson.toJson(res);
        return resString;
    }

    private String searchUsers(Request request, Response response) {
        JsonUserQuery query;
        try {
            query = gson.fromJson(request.body(), JsonUserQuery.class);
        } catch (Exception e) {
            System.out.println("Failed to prcess input: " + request.body());
            halt(400);
            return "";
        }
        ArrayList<StringDoubleTuple> topUsers = Main.scores.getUsersForString(query.query);
        UserQueryResponse res = new UserQueryResponse();
        for (int i = 0; i < 20; i++) {
            res.users.add(new JsonUser(users.get(topUsers.get(i).str)));
        }

        response.status(201);
        response.header("Content-Type", "application/json");
        String resString = gson.toJson(res);

        return resString;
    }
}
