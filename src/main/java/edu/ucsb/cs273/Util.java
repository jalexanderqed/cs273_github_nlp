package edu.ucsb.cs273;

/**
 * Created by john on 11/30/16.
 */

import cc.mallet.pipe.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Util {
    public static String getIssuesDir(String repo){
        return "output/" + repo + "/issues/";
    }

    public static String getUsersDir(String repo){
        return "output/" + repo + "/users/";
    }

    public static List<String> readLines(String file) throws IOException{
        Charset charset = Charset.forName("ISO-8859-1");
        return Files.readAllLines(Paths.get(file), charset);
    }

    public static String readText(String file) throws IOException{
        Charset charset = Charset.forName("ISO-8859-1");
        StringBuilder ret = new StringBuilder();
        for(String line : Files.readAllLines(Paths.get(file), charset)){
            ret.append(line + "\n");
        }
        return ret.toString();
    }

    public static ArrayList<Pipe> getStandardPipes(){
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new Input2CharSequence());
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        return pipeList;
    }

    public static double dot(double[] a, double[] b) throws ArithmeticException{
        if(a.length != b.length){
            throw new ArithmeticException("Cannot dot product arrays of differen lengths");
        }
        double res = 0;
        for(int i = 0; i < a.length; i++){
            res += a[i] * b[i];
        }
        return res;
    }
}
