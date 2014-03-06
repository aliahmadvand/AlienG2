package com.ali.Mtree;

/*
 * data.java
 *
 * Created on August 8, 2000, 10:10 AM
 */

//package rates;
import java.io.*;
import java.lang.*;
/**
 *
 * @author  noname
 * @version 
 */
public class dataf extends Object {

    /** Creates new data */
    public dataf() {
    }
    /*This method (dataf.stin(Stringname)) returns a Stringreader.
    USAGE: Stringreader <variable> = dataf.stin(Stringname);
    */
    public static StringReader stin(String stname)
    throws IOException{
        StringReader ststring=
        new StringReader(stname);
        return ststring;
    }
        
    /*This method (dataf.in(filename)) returns a BufferedReader
    USAGE: BufferedReader <variable> = dataf.in(filename);
    */
    
    public static BufferedReader in(File fname)
    throws IOException{
        BufferedReader datafile=
        new BufferedReader(
        new FileReader(fname));
        return datafile;
    }//end in
    public static BufferedReader in(String fname)
    throws IOException{
        BufferedReader datafile =
        new BufferedReader(
        new FileReader(fname));
        return datafile;
    }//end in
    public static PrintWriter out(File fname)
    throws IOException{
        PrintWriter out=
        new PrintWriter(
        new BufferedWriter(
        new FileWriter(fname)));
        return out;
    }//end out
    public static PrintWriter out(String fname)
    throws IOException{
        PrintWriter out=
        new PrintWriter(
        new BufferedWriter(
        new FileWriter(fname)));
        return out;
    }//end out
}//end class