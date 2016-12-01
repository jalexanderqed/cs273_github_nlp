package edu.ucsb.cs273;

/**
 * Created by john on 11/30/16.
 */
public class StringDoubleTuple implements Comparable{
    public String str;
    public double val;

    public StringDoubleTuple(String s, double v){
        str = s;
        val = v;
    }

    public int compareTo(Object o){
        StringDoubleTuple other = (StringDoubleTuple)o;
        return new Double(val).compareTo(other.val);
    }

    public String toString(){
        return str + "\t" + val;
    }
}
