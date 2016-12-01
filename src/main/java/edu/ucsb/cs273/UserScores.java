package edu.ucsb.cs273;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by john on 11/30/16.
 */
public class UserScores implements Serializable{
    private String repository;
    HashMap<String, User> userScores;
    TopicModel tModel;

    public UserScores(TopicModel m) throws Exception{
        repository = Main.repository;
        tModel = m;
        userScores = Util.readUsers(Main.usersSaveFile);
    }

    public void run() throws Exception{
        HashMap<String, StringBuilder> userStrings = new HashMap<String, StringBuilder>();

        File usersDir = new File(Util.getUsersDir(repository));
        String issuesDir = Util.getIssuesDir(repository);
        for(File f : usersDir.listFiles()){
            String issueNum = f.getName().split("\\.")[0];
            String issueText = Util.readText(issuesDir + issueNum + ".txt");
            Properties users = new Properties();
            users.load(new FileInputStream(f.getAbsolutePath()));
            for(String user : users.stringPropertyNames()){
                if(!userStrings.containsKey(user)){
                    userStrings.put(user, new StringBuilder());
                }
                Integer weight = Integer.parseInt(users.getProperty(user));
                for(int i = 0; i < weight.intValue(); i++) {
                    userStrings.get(user).append(issueText);
                }
            }
        }

        TopicInferencer inferencer = tModel.model.getInferencer();
        for(String userId : userStrings.keySet()) {
            InstanceList temp = new InstanceList(tModel.instances.getPipe());
            temp.addThruPipe(new Instance(userStrings.get(userId).toString(), null, "test instance", null));
            double[] userTopics = inferencer.getSampledDistribution(temp.get(0), 1000, 1, 5);

            if(!userScores.containsKey(userId)){
                throw new IllegalStateException("User " + userId + " was not present when generating topic scores.");
            }
            userScores.get(userId).setTopics(userTopics);
        }

        Util.writeUsers(Main.usersSaveFile, userScores);
    }

    public ArrayList<StringDoubleTuple> getUsersForString(String query){
        ArrayList<StringDoubleTuple> scores = new ArrayList<StringDoubleTuple>();

        TopicInferencer inferencer = tModel.model.getInferencer();
        InstanceList temp = new InstanceList(tModel.instances.getPipe());
        temp.addThruPipe(new Instance(query, null, "test instance", null));
        double[] queryTopics = inferencer.getSampledDistribution(temp.get(0), 1000, 1, 5);

        for(String user : userScores.keySet()) {
            User currUser = userScores.get(user);
            scores.add(new StringDoubleTuple(user, Util.dot(currUser.getTopics(), queryTopics) * Math.log(currUser.issues.size() * 1)));
        }

        Collections.sort(scores, new Comparator<StringDoubleTuple>() {
            @Override
            public int compare(StringDoubleTuple tuple1, StringDoubleTuple tuple2)
            {
                return -1 * tuple1.compareTo(tuple2);
            }
        });
        return scores;
    }
}
