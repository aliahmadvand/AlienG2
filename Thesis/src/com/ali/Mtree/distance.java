package com.ali.Mtree;

/*
 * distance.java
 *
 * Created on August 25, 2000, 10:56 AM
 */

//package rates;

/**
 *
 * @author  noname
 * @version 
 */
public class distance extends Object {

    /** Creates new distance */
    public distance() {
    }

    public static double log10(double x){
        double out=(java.lang.Math.log(x)/java.lang.Math.log(10));
        return out;
    }
    
    public static double log10(float x){
        double out=(java.lang.Math.log(x)/java.lang.Math.log(10));
        return out;
    }
    
    public static double log10(int x){
        double out=(java.lang.Math.log(x)/java.lang.Math.log(10));
        return out;
    }
    public static double poisson(double dissimilarity){
        double distance=((double)-java.lang.Math.log(((double)1)-dissimilarity));//log here is base "e"
        return distance;
    }//end poisson
    
    public static double kimura(double dissimilarity){
        double distance=((double)-java.lang.Math.log(((double)1)-dissimilarity -((double)0.2*(dissimilarity*dissimilarity))));
        return distance;
    }// end kimura
    
    public static double tajimanei(double dissimilarity){
        double distance=(((double)-0.95)*java.lang.Math.log(1-((1/0.95)*dissimilarity)));
        return distance;
    }//end tajimanei
    
    public static double nocorrect(double dissimilarity){
        double distance = dissimilarity;
        return distance;
    }//end nocorrect
}