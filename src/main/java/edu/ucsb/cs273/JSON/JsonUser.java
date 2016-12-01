package edu.ucsb.cs273.JSON;

import edu.ucsb.cs273.Main;
import edu.ucsb.cs273.User;

import java.util.ArrayList;

/**
 * Created by john on 12/1/16.
 */
public class JsonUser {
    public String id;
    public String avatar;
    public ArrayList<String> keywords;
    public ArrayList<String> issues = new ArrayList<>();

    public JsonUser(User u){
        id = u.id;
        avatar = u.avatar;
        keywords = u.commonWords;
        for(Integer issueNum : u.issues){
            issues.add("https://github.com/" + Main.repository + "/issues/" + issueNum.toString());
        }
    }
}
