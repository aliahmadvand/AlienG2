package com.ali.Mtree;

/*
 * graph.java
 *
 * Created on August 21, 2000, 11:19 AM
 */

//package rates;
import javax.swing.JOptionPane;
/**
 *
 * @author  noname
 * @version 
 */
public class graph extends Object {

    /** Creates new graph */
    public graph() {
    }
    public static void message(String s){
        JOptionPane.showMessageDialog(null,s,"Message",JOptionPane.INFORMATION_MESSAGE);
    }//end message(s)
    
    public static void error(String s){
        JOptionPane.showMessageDialog(null,s,"ERROR",JOptionPane.ERROR_MESSAGE);
    
    }//end error(s)
    
    public void error(){
           JOptionPane.showMessageDialog(null,"error","ERROR",JOptionPane.ERROR_MESSAGE);        
    }//end error()
    
}//end class