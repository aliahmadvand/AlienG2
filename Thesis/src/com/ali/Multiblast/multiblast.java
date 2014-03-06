package com.ali.Multiblast;

/*
 * multiblast.java
 *
 * Created on July 15, 2004, 4:54 PM
 */
import java.io.*;
import java.util.*;
/**
 *
 * @author  tancred
 */
public class multiblast {
    
    /** Creates a new instance of multiblast */
    public multiblast() {
    }
    
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        if(java.lang.reflect.Array.getLength(args)<1){
            printinfo();
            System.exit(0);
        }
        if(readargs(args)==false){
            printinfo();
            System.exit(1);
        }else{
        	System.out.println("\nPrinting blast parameters: ");
            printargs();
        }
        //now create the threads
        Object syncon=new Object();
        blastthread[] threadarr=new blastthread[cpu];
        for(int i=0;i<cpu;i++){
            threadarr[i]=new blastthread("test","testout",outdir,basecommandarr,dbstring,syncon,i, redo);
            threadarr[i].done=true;
        }//end for i
        //now read from the input file
        try{
            BufferedReader inread=new BufferedReader(new FileReader(infile));
            String inline;
            String currname="";
            String currseq="";
            boolean started=false;
            int freethread=-1;
            int counter=0;
            while((inline=inread.readLine())!=null){
                inline=inline.trim();
                if(inline.length()<1){
                    continue;
                }
                if(inline.startsWith(">")){
                    counter++;
                    if(currseq.length()>0){
                        //here I need to start the threads
                        started=false;
                        while (started==false){
                            freethread=-1;
                            synchronized (syncon){
                                for(int i=0;i<cpu;i++){
                                    if(threadarr[i].done==true){
                                        freethread=i;
                                        break;
                                    }
                                }//end for i
                            }//end sync syncon
                            if(freethread!=-1){//if I had a free thread
                                threadarr[freethread]=new blastthread(currname, currseq, outdir, basecommandarr, dbstring, syncon, freethread, redo);
                                threadarr[freethread].start();
                                started=true;
                                System.out.println("started:"+counter+" on "+freethread);
                            }else{//if I did not have a free thread
                                try{
                                    synchronized(syncon){
                                        syncon.wait(60000);//max wait one minute before re-checking
                                    }
                                }catch (InterruptedException ie){
                                    System.err.println("ERROR interrupted wait in main");
                                    ie.printStackTrace();
                                }
                            }
                        }//end while started==false;
                    }//end if currseq.length>0
                    currseq="";
                    currname=inline;
                }else{
                    currseq+=inline;
                }
            }//end while reading
            counter++;
            if(currseq.length()>0){
                //here I need to start the threads
                started=false;
                while (started==false){
                    freethread=-1;
                    synchronized (syncon){
                        for(int i=0;i<cpu;i++){
                            if(threadarr[i].done==true){
                                freethread=i;
                                break;
                            }
                        }//end for i
                    }//end sync syncon
                    if(freethread!=-1){//if I had a free thread
                        threadarr[freethread]=new blastthread(currname, currseq, outdir, basecommandarr, dbstring, syncon, freethread, redo);
                        threadarr[freethread].start();
                        started=true;
                        System.out.println("started:"+counter+" on "+freethread);
                    }else{//if I did not have a free thread
                        try{
                            synchronized(syncon){
                                syncon.wait(60000);//max wait one minute before re-checking
                            }
                        }catch (InterruptedException ie){
                            System.err.println("ERROR interrupted wait in main");
                            ie.printStackTrace();
                        }
                    }
                }//end while started==false;
            }//end if currseq.length>0
            inread.close();
        }catch( IOException ioe){
            System.err.println("IOERROR reading from "+infile.getAbsolutePath());
            return;
        }
    }//end main
    
    static File infile=new File("infile");
    static File outdir=new File(".");
    static int cpu=1;
    static String[] basecommandarr =new String[0];
    static boolean checkblast=true;
    static boolean redo=false;
    static String dbstring="";
    
    //--------------------------------------------------------------------------
    
    static void printinfo(){
        System.out.println("options:");
        System.out.println("-infile input_file_name");
        System.out.println("-outdir output directory_name");
        System.out.println("-blast blast_command");
        System.out.println("-db databases_to_search_against");
        System.out.println("-cpu threads_to_spawn");
        System.out.println("-redo t/F");
    }
    
    static void printargs(){
        System.out.println("infile:"+infile.getAbsolutePath());
        System.out.println("outdir:"+outdir.getAbsolutePath());
        System.out.println("cpu:"+cpu);
        System.out.print("blast:");
        for(int i=0;i<java.lang.reflect.Array.getLength(basecommandarr);i++){
            System.out.print(basecommandarr[i]+"; ");
        }
        System.out.println("-db "+dbstring);
        System.out.println("-i; query_name; > ; outfile_name");
        System.out.println("redo:"+redo);
    }
    //--------------------------------------------------------------------------
    
    static boolean readargs(String[] args){
        int argsnum=java.lang.reflect.Array.getLength(args);
        for(int i=0;i<argsnum;i++){
            if(args[i].equalsIgnoreCase("-infile")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    infile=new File(args[i]);
                }
            }else if(args[i].equalsIgnoreCase("-outdir")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    outdir=new File(args[i]);
                    if(outdir.exists()==false){
                        outdir.mkdir();
                    }
                }
            }else if(args[i].equalsIgnoreCase("-db")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    dbstring=args[i];
                }
            }else if(args[i].equalsIgnoreCase("-blast")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    basecommandarr=args[i].split("\\s+");
                }
            }else if(args[i].equalsIgnoreCase("-cpu")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    try{
                        cpu=Integer.parseInt(args[i]);
                    }catch (NumberFormatException ne){
                        System.err.println("unable to parse int from "+args[i]);
                        return false;
                    }
                }
            }else if(args[i].equalsIgnoreCase("-redo")){
                i++;
                if(i>=argsnum){
                    System.err.println("missing parameter for "+args[i-1]);
                    return false;
                }else{
                    if(args[i].startsWith("t") || args[i].startsWith("T")){
                        redo=true;
                    }else{
                        redo=false;
                    }
                }
            }else{
                System.err.println("unknown argument "+args[i]);
                return false;
            }
        }//end for i
        return true;
    }//end readargs
    
}//end main

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

class blastthread extends java.lang.Thread{
    
    public blastthread(String currname, String currseq, File outdir, String[] basecommandarr, String dbstring, Object syncon, int freethread, boolean redo){
        this.queryname=currname;
        this.queryseq=currseq;
        if(queryname.indexOf(" ")>-1){
            this.basename=queryname.substring(1,queryname.indexOf(" "));
        }else{
            this.basename=queryname.substring(1);
        }
        this.outputfile=outdir.getAbsolutePath();
        if(outputfile.endsWith(java.io.File.separator)==false){
            outputfile+=java.io.File.separator;
        }
        outputfile+=basename+".bls";
        queryfile=new File(outdir,"query"+freethread+".fasta");
        this.cpunum=freethread;
        this.basecommandarr=basecommandarr;
        this.commandlength=java.lang.reflect.Array.getLength(basecommandarr);
        this.syncon=syncon;
        this.redo=redo;
        this.dbstring=dbstring;
        this.done=false;
    }
    
    int cpunum=0;
    String queryname="query";
    String queryseq="sequence";
    String basename="query";
    boolean done=true;
    String[] basecommandarr;
    String dbstring="";
    int commandlength=0;
    String outputfile="query.out";
    File queryfile=new File("query");
    Object syncon;
    boolean redo;
    
    public void run(){
        //first create the query file
        System.out.println("doing "+basename);
        if(redo==false){
            //check if the blast file is ok
            File checkfile=new File(outputfile);
            if(checkfile.canRead()){
                //System.out.println("file "+checkfile.getAbsolutePath()+" exists & can read");
                try{
                    BufferedReader inread=new BufferedReader(new FileReader(checkfile));
                    boolean ok=false;
                    String inline;
                    while((inline=inread.readLine())!=null){
                        if((inline.indexOf("effective search space used:")>-1)||(inline.indexOf("Effective search space used:")>-1)){
                            System.out.println("blast file "+basename+" ok, skipping blast run");
                            ok=true;
                            break;
                        }
                    }//end while reading
                    inread.close();
                    if(ok){
                        synchronized(syncon){
                            this.done=true;
                            syncon.notify();
                        }
                        return;
                    }else{
                        System.out.println("repeating BLAST for "+basename);
                    }
                }catch (IOException ioe){
                    System.err.println("IOERROR reading from "+outputfile);
                }
            }
        }
        //now create the command to run
        String[] commandarr=new String[commandlength+6];
        for(int i=0;i<commandlength;i++){
            commandarr[i]=basecommandarr[i];
        }//end for i
        commandarr[commandlength]="-d";
        commandarr[commandlength+1]=dbstring;
        commandarr[commandlength+2]="-i";
        commandarr[commandlength+3]=queryfile.getAbsolutePath();
        commandarr[commandlength+4]="-o";
        // I had to add  the replace method because Windows OS doesn't accept '|' in a file name!
        commandarr[commandlength+5]=outputfile.replace('|', '-');;
        System.out.println("doing:");
        for(int i=0;i<java.lang.reflect.Array.getLength(commandarr);i++){
            System.out.print(commandarr[i]+";");
        }
        try{
            PrintWriter outwrite=new PrintWriter(new BufferedWriter(new FileWriter(queryfile)));
            outwrite.println(queryname);
            outwrite.println(queryseq);
            outwrite.close();
        }catch (IOException ioe){
            System.err.println("unable to write to file "+queryfile.getAbsolutePath());
            synchronized(syncon){
                this.done=true;
                syncon.notify();
            }
            return;
        }
        StringBuffer errout=new StringBuffer();
        StringBuffer inout=new StringBuffer();
        threadstreamreader errread;
        threadstreamreader inread;
        Runtime rt=Runtime.getRuntime();
        try{
            try{
            	Process p=rt.exec(commandarr);
                errread=new threadstreamreader(new BufferedReader(new InputStreamReader(p.getErrorStream())),errout);
                inread=new threadstreamreader(new BufferedReader(new InputStreamReader(p.getInputStream())),inout);
                errread.start();
                inread.start();
                p.waitFor();
            }catch (InterruptedException ie){
                System.err.println("ERROR, interrupted wait for "+cpunum);
            }
            System.err.print(errout.toString());
            if(errout.length()>0){
                System.err.println("command was:");
                for(int i=0;i<java.lang.reflect.Array.getLength(commandarr);i++){
                    System.err.print(commandarr[i]+";");
                }//end for i
            }
        }catch (IOException ioe){
            System.err.println("IOERROR trying to run:");
            for(int i=0;i<java.lang.reflect.Array.getLength(commandarr);i++){
                System.err.print(commandarr[i]+";");
            }//end for i
        }
        System.out.println("\ndone "+basename);
        queryfile.delete();
        synchronized(syncon){
            this.done=true;
            syncon.notify();
        }
    }//end run method
    
}//end blastthread


