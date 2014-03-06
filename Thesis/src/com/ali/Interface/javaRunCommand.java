package com.ali.Interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class javaRunCommand {
   // private static final String CMD = 
     //   "netsh int ip set address name = \"Local Area Connection\" source = static addr = 192.168.222.3 mask = 255.255.255.0";
    public void run(String CMD) {

        try {
            // Run "netsh" Windows command
            Process process = Runtime.getRuntime().exec(CMD);

            // Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read command standard output
            String s;
            //System.out.println("Standard output: ");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Read command errors
            //System.out.println("Standard error: ");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}