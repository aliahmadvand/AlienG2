package com.ali.Mtree;

/*
 * maketreethreadnograph.java
 *
 * Created on October 21, 2002, 6:57 PM
 */
import java.io.*;
/**
 *
 * @author  tancred
 */
public class maketreethreadnograph extends java.lang.Thread {
    
    /** Creates a new instance of maketreethreadnograph */
    boolean done=false;
    File[] allfiles;
    int cpu=1;
    int verbose=1;
    int bootstrap=0;
    String corrmet="none";
    String indir="";
    String outdir="";
    PrintWriter errorlog;
    int currtasknumber;
    String currtaskname;
    String extension="";
    String outextens="";
    
    public maketreethreadnograph(File[] allfiles, String indir, String outdir, int verbose, int cpunum, int bootstrap, String corrmet,String extension, String outextens,PrintWriter errorlog){
        //System.out.println("creating maketreethread");
        this.allfiles=allfiles;
        this.verbose=verbose;
        this.cpu=cpunum;
        this.bootstrap=bootstrap;
        this.corrmet=corrmet;
        this.indir=indir;
        this.outdir=outdir;
        this.errorlog=errorlog;
        this.extension=extension;
        this.outextens=outextens;
    }// end constructor
    
    public void run(){
        int filestodo=java.lang.reflect.Array.getLength(allfiles);
        if(filestodo<1){
            System.err.println("no "+extension+" files found in "+indir);
            return;
        }// end if
        treemakethreadnograph[] mytreemakes=new treemakethreadnograph[cpu];
        for(int i=0;(i<cpu)&&(i<filestodo);i++){
            //make the treemakethread objects
            //initialize the array
            mytreemakes[i]=new treemakethreadnograph(this);
        }
        if(verbose>0){
            System.out.println("done threadinit, "+filestodo+" files to do");
        }
        currtasknumber=0;
        while(currtasknumber<filestodo){
            currtaskname=allfiles[currtasknumber].getName();
            int isdone=-1;
            for(int i=0;(i<cpu)&&(i<filestodo);i++){
                if(mytreemakes[i].done){
                    isdone=i;
                    break;
                }
            }// end for i
            if(isdone==-1){
                synchronized(this){
                    for(int i=0;(i<cpu)&&(i<filestodo);i++){
                        if(mytreemakes[i].done){
                            isdone=i;
                            break;
                        }
                    }// end for i
                    if(isdone==-1){
                        try{
                            this.wait();
                        }catch (InterruptedException e){
                            System.out.println("interrupted wait in main");
                            e.printStackTrace();
                        }
                    }
                }// end synchr
                continue;//restart at top of while loop
            }//end if isdone==-1
            //the rest should only exec if I have a free mytreemakes
            //System.out.println("starting "+allfiles[currtasknumber].getName()+" on "+isdone);
            //Why the f... do I have to create a new thread ech time!
            mytreemakes[isdone]=new treemakethreadnograph(this,allfiles[currtasknumber],outdir,bootstrap,corrmet,extension,outextens,errorlog,verbose);
            //mytreemakes[isdone].init(allfiles[currtasknumber],outdir,bootreps,format,corrmet,extension,outextens,errorlog);
            currtasknumber+=1;
            //mytreemakes[isdone].start();
        }// end while all files are not being done
        boolean alldone=false;
        while (alldone==false){
            alldone=true;
            synchronized(this){
                for(int i=0;((i<cpu)&&(i<filestodo));i++){
                    if(mytreemakes[i].done==false){
                        alldone=false;
                        break;
                    }
                }// end for i
                if(alldone==false){
                    try{
                        System.out.println("waiting at end");
                        this.wait();
                    }catch (InterruptedException e){
                        System.out.println("interrupted wait in main");
                        e.printStackTrace();
                    }
                }
            }// end synchr
        }
        done=true;
        System.out.println("DONE");
        
    }// end run
    
    
    
}
