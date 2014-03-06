package com.ali.Mtree;

/*
 * treemakethread.java
 *
 * Created on March 23, 2002, 11:27 AM
 */
import java.lang.*;
import java.io.*;
/**
 *
 * @author  tancred
 * @version
 */
public class treemakethreadnograph extends java.lang.Thread{
    
    boolean killthread=false;
    boolean done=true;
    boolean skipthis=false;
    int currtasknumber=0;
    int maxtask=0;
    int verbose=1;
    String currtaskname="initializing";
    File dofile;
    PrintWriter errorlog;
    String format;
    String corrmet;
    int alltasknumber=0;
    int bootreps;
    methodsnograph mymethods;
    java.util.Random rand=new java.util.Random(System.currentTimeMillis());
    String extension;
    String outextens;
    String outdir;
    Object parent;
    
    /** Creates new treemakethread */
    public treemakethreadnograph(Object parent) {
        this.parent=parent;
    }
    
    public treemakethreadnograph(Object parent, File infile, String outdir, int bootreps, String corrmet, String extension, String outextens, PrintWriter errlog,int verbose) {
        this.parent=parent;
        this.init(infile,outdir,bootreps,corrmet,extension,outextens,errlog,verbose);
    }
    
    public void init(File infile,String outdir, int bootreps,String corrmet,String extension,String outextens,PrintWriter errlog,int verbose){
        this.done=false;
        this.errorlog=errlog;
        this.outdir=outdir;
        this.extension=extension;
        this.outextens=outextens;
        this.format=format;
        this.corrmet=corrmet;
        this.bootreps=bootreps;
        this.dofile=infile;
        this.killthread=false;
        this.currtasknumber=0;
        this.currtaskname="initializing";
        this.maxtask=(bootreps+1);
        this.mymethods=new methodsnograph();
        this.skipthis=false;
        this.verbose=verbose;
        //System.out.println("starting run for "+infile.getName());
        this.start();
    }
    String getcurrtaskname(){
        return currtaskname;
    }
    
    int getmaxtask(){
        return maxtask;
    }
    
    void killthread(){
        this.killthread=true;
    }
    
    int getcurrtasknumber(){
        return currtasknumber;
    }
    
    boolean getdone(){
        return done;
    }
    
    public void run(){
        //System.out.println("started run "+dofile.getName());
        if(verbose>0){
            System.out.println("computing tree for "+dofile.getName());
        }
        double[][][] distances;
        String[] speciesarr;
        //for(int i=0;i<filestodo;i++){
        boolean foundlogerr=false;
        int remakecount=0;
        //System.out.println(dofile.getName());
        currtasknumber+=1;
        if(killthread){
            done=true;
            synchronized(parent){//tell the parent object that I am done
                parent.notify();
            }
            return;
        }
        currtaskname=new String(dofile.getName());
        aaseq[] orgseqs=new aaseq[0];
        int species=0;
        distances=new double[0][0][0];
        speciesarr=new String[0];
        orgseqs=readaln.read(dofile);
        species=java.lang.reflect.Array.getLength(orgseqs);
        if(species<=1){
            String errstr=dofile.getName()+": "+species+" sequence read, skipping tree construction";
            errorlog.println(errstr);
            System.err.println(errstr);
            synchronized(parent){//tell the parent object that I am done
                done=true;
                parent.notify();
            }
            return;
        }
        if(species>0){
            //System.out.println("Species="+species);
            distances=new double[bootreps+1][species][species];
            distances[0]=mymethods.getdistances(orgseqs,corrmet,this);
            speciesarr=new String[species];
            for(int j=0;j<species;j++){
                speciesarr[j]=orgseqs[j].seqname;
            }// end for j
        }// end if
        if(skipthis){// if I have a log error in the distance computation
            skipthis=false;
            errorlog.println("log error in "+dofile.getName());
            synchronized(parent){//tell the parent object that I am done
                done=true;
                parent.notify();
            }
            return;
            //continue;//next for i
        }
        //System.out.println("done parent, starting bootreps");
        boolean gotonextfile=false;
        for(int j=0;j<bootreps;j++){
            if(killthread){
                synchronized(parent){//tell the parent object that I am done
                    done=true;
                    parent.notify();
                }
                return;
            }
            currtaskname=new String(dofile.getName()+"("+j+")");
            currtasknumber+=1;
            //char[][] replarr=mymethods.makereplicate(orgseqs,rand);
            //testreplicate(replarr);
            //distances[j+1]=mymethods.getdistances(replarr,corrmet,this);
            distances[j+1]=mymethods.getdistances(mymethods.makereplicate(orgseqs,rand),corrmet,this);
            if(skipthis){// generate bootreps without log errors
                j=j-1;
                currtasknumber=currtasknumber-1;
                if(foundlogerr==false){//only print this once for each file (i.e. set flag foundlogerr
                    errorlog.println("log problems in bootstrap replicates for "+dofile.getName());
                    foundlogerr=true;
                }
                remakecount+=1;
                if(remakecount>bootreps){
                    //System.err.println(remakecount+" retries for "+bootreps+" bootreps; stopping this tree");
                    gotonextfile=true;
                    j=bootreps+1;
                    break;// exit the for loop
                }
                skipthis=false;
            }
        }// end for
        if(gotonextfile){
            errorlog.println("too many retries for bootstrap replicates in "+dofile.getName());
            System.out.println("too many retries for bootstrap replicates in "+dofile.getName());
            synchronized(parent){//tell the parent object that I am done
                done=true;
                parent.notify();
            }
            return;
            //continue; //next file
        }
        if(killthread){
            System.out.println("Thread killed");
            synchronized(parent){//tell the parent object that I am done
                done=true;
                parent.notify();
            }
            return;
        }
        currtaskname=new String(dofile.getName()+"(tree)");
        currtasknumber+=1;
        String rootname=dofile.getName().substring(0,dofile.getName().indexOf(extension));
        treenode thisnode=mymethods.computetree(distances,speciesarr,verbose);
        if(thisnode.gettipnodes()>2){
            thisnode=mymethods.roottree(thisnode,rootname);
        }
        if(thisnode.kids()==0){
            errorlog.println("maketree error on "+dofile.getName()+" "+thisnode.name);
            thisnode=new treenode();
        }
        if(skipthis==false){
            String treestring=treefiletostring(thisnode);
            //System.out.println("testtreestringinput");
            //testnode(thisnode,0);
            //System.out.println(treestring);
            String outfilename=dofile.getName().substring(0,dofile.getName().indexOf('.'))+outextens;
            try{
                File outfile=new File(outdir,outfilename);
                PrintWriter out=dataf.out(outfile);
                out.println(treestring+";");
                out.close();
            }catch (IOException e){
                System.out.println("error writing to "+outfilename);
            }
        }// end if skipthis
        if(skipthis){
            skipthis=false;
            //System.out.println("log error in "+dofile.getName());
            errorlog.println("log error in "+dofile.getName());
        }
        //}// end for filestodo
        if(verbose>0){
            System.out.println("tree for "+dofile.getName()+" DONE");
        }
        alltasknumber+=currtasknumber;
        currtasknumber=0;
        synchronized(parent){//tell the parent object that I am done
            //System.out.println("notifying parent");
            done=true;
            parent.notify();
        }
    }// end run
    
    //---------------------------------------------------------------------------------------------------------
    
    String treefiletostring(treenode innode){
        int kids=innode.kids();
        float distancetoparent=innode.length;
        float bootval=innode.getboot();
        String name=innode.name;
        String outputstring="";
        String s=null;
        java.util.Vector stringvector=new java.util.Vector();
        if(kids==0){
            if(distancetoparent>0){
                outputstring=new String(name+":"+distancetoparent);
            }
            if(distancetoparent<=0){
                outputstring=new String(name+":0");
            }
        }else{
            s=new String("(");
            stringvector.addElement(s);
            for (int i=0;i<kids;i++){
                if(i>0){// if not first loop add a ","
                    s=new String(",");
                    stringvector.addElement(s);
                }
                //System.out.println(innode.name);
                s=treefiletostring(innode.kids[i]);
                stringvector.addElement(s);
            }// end for
            if(bootval<=0){
                if(bootreps>0){
                    if(distancetoparent>0){
                        s=new String(")0:"+distancetoparent);
                    }
                    if(distancetoparent<=0){
                        s=new String(")0:0");
                    }
                }
                if(bootreps<=0){
                    if(distancetoparent>0){
                        s=new String("):"+distancetoparent);
                    }
                    if(distancetoparent<=0){
                        s=new String("):0");
                    }
                }
            }
            if(bootval>0){
                if(distancetoparent>0){
                    s=new String(")"+bootval+":"+distancetoparent);
                }
                if(distancetoparent<=0){
                    s=new String(")"+bootval+":0");
                }
            }
            stringvector.addElement(s);
            //outputstring=new String();
            StringBuffer tmpstring=new StringBuffer();
            for (int i=0;i<(stringvector.size());i++){// don't use the last element (distance of origin=not needed)
                //outputstring=outputstring+(String)stringvector.elementAt(i);// concatenate all the elements of the stringvector
                tmpstring=tmpstring.append((String)stringvector.elementAt(i));
            }// end for
            outputstring=tmpstring.toString();
        }// end else
        //System.out.println("outstring="+outputstring);
        return outputstring;
    }// end treefiletostring
    
    
    void testnode(treenode innode,int lvl){
        System.out.println(lvl+" - "+innode.name+","+innode.length+","+innode.getboot());
        for(int i=0;i<innode.kids();i++){
            testnode(innode.kids[i],lvl+1);
        }
    }// end testnode
    
    void testreplicate(char[][] inarr){
        int species=java.lang.reflect.Array.getLength(inarr);
        int seqlength=java.lang.reflect.Array.getLength(inarr[0]);
        for(int i=0;i<species;i++){
            System.out.println(i+" seq:"+new String(inarr[i]));
        }
    }// end testreplicate
    
}// end class treemakethread
