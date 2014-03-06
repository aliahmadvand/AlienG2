package com.ali.Mtree;

/*
 * mainline.java
 *
 * Created on October 21, 2002, 1:50 PM
 */
import java.io.*;

/**
 *
 * @author  tancred
 */
public class mainline {
    
    /** Creates a new instance of mainline */
    public mainline() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //this is the command line driven pendant to malign2mtrees.
        //loop through the input arguments to get -conf and -verbose
        if(checkargs(args)!=true){
            System.err.println("unable to check arguments");
            return;
        }
        //now read the conffile
        if(readconf(conffile)!=true){
            System.err.println("unable to read configuration from "+conffile+" using defaults.");
        }
        //now read the command line arguments
        if(readargs(args)!=true){
            System.err.println("unable to read command line arguments");
            return;
        }
        if(verbose>0){
            printconfig();
        }
        if(auto==false){
            System.out.println("are those values correct?(y/N)");
            try{
                BufferedReader stdinread=new BufferedReader(new InputStreamReader(System.in));
                String reply=stdinread.readLine();
                stdinread.close();
                if(reply.equalsIgnoreCase("yes")||reply.equalsIgnoreCase("y")){
                    System.out.println("Values accepted.");
                }else{
                    System.out.println("Values not accepted, aborting.");
                    return;
                }
            }catch (IOException e){
                System.err.println("unable to read from stdin");
                e.printStackTrace();
            }
        }
        //now start the computation
        File[] allfiles=getfiles(new File(indir),extension);
        File[] donefiles=new File[0];
        if(redo==false){
            donefiles=getfiles(new File(outdir),".tre");
            System.out.println("files done:"+java.lang.reflect.Array.getLength(donefiles));
            System.out.println("files todo:"+(java.lang.reflect.Array.getLength(allfiles)-java.lang.reflect.Array.getLength(donefiles)));
            java.util.HashMap tmphash=new java.util.HashMap();
            int allfilesnum=java.lang.reflect.Array.getLength(allfiles);
            for(int i=0;i<allfilesnum;i++){
                tmphash.put(allfiles[i].getName(),allfiles[i]);
            }//end for i
            //now search through the hash & remove the files that already have computed trees
            allfilesnum=java.lang.reflect.Array.getLength(donefiles);
            String tmpstr;
            String tmpext=extension;
            if(tmpext.startsWith(".")==false){
                tmpext="."+tmpext;
            }
            for(int i=0;i<allfilesnum;i++){
                tmpstr=donefiles[i].getName().substring(0,donefiles[i].getName().indexOf(".tre"));
                if(tmphash.containsKey(tmpstr+tmpext)){
                    tmphash.remove(tmpstr+tmpext);
                }else{
                    System.err.println("input file "+tmpstr+tmpext+" not found.");
                }
            }//end for i
        }//end if redo==false
        File outdirfile=new File(outdir);
        if(outdirfile.exists()&&outdirfile.isDirectory()){
            //do nothing
        }else{
            //create a directory
            outdirfile.mkdirs();
        }
        File logfile=new File(outdir,"mtrees.log");
        try{
            PrintWriter errorlog=new PrintWriter(new BufferedWriter(new FileWriter(logfile)));
            maketreethreadnograph maketrees= new maketreethreadnograph(allfiles, indir, outdir, verbose, cpu, bootstrap, corrmet, extension, outextens, errorlog);
            maketrees.start();
            errorlog.close();
        }catch (IOException e){
            System.err.println("IOError in errorlog\n");
            e.printStackTrace();
        }
        
        if(verbose>0){
            System.out.println("DONE");
        }
    }
    
    static String conffile="mtrees.conf";
    static String indir=System.getProperty("user.dir","");
    static String outdir=indir;
    static String corrmet="P";
    static String extension=".cln";
    static String outextens=".tre";
    //static String db="";
    static int bootstrap=0;
    static int verbose=1;
    static int cpu=1;
    static boolean auto=false;
    static boolean redo=true;
    
    //--------------------------------------------------------------------------
    
    static void printinfo(){
        System.out.println("mtrees build trees for all alignments in the input directory with the specified filename ending");
        System.out.println("USAGE: java -Xmx###m -jar mtrees.jar [options]");
        System.out.println("where ### is the maximum number of megabytes to allocate in memory for this process");
        System.out.println("OPTIONS:");
        System.out.println("-conf config_file_name");
        System.out.println("-in input_directory");
        System.out.println("-out output_directory");
        System.out.println("-ext alignment_name_extension");
        System.out.println("-corr correction method (P(poisson),TN(Tajima-Nei),K(Kimura),NONE)");
        System.out.println("-verbose verbose_mode(0-10)");
        System.out.println("-bootstrap number_of_replicates");
        System.out.println("-cpu #_of_cpu_to_use");
        System.out.println("-auto t/F interactive/automatic mode");
        System.out.println("-redo T/f replace existing trees?");
        //System.out.println("-db database to look up gi numbers");
        System.out.println();
        System.out.println("This program was developed at the MPI for developmental biology, Dept. 1");
        System.out.println("Tuebingen, Germany");
        System.out.println("Tancred Frickey 2003");
    }//end printinfo
    
    //--------------------------------------------------------------------------
    
    static boolean readargs(String[] args){
        int argslength=java.lang.reflect.Array.getLength(args);
        for(int i=0;i<argslength;i++){
            if(args[i].equalsIgnoreCase("-conf")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -conf, missing argument!");
                    return false;
                }
                conffile=args[i];
                continue;
            }// end if -conffile
            //if(args[i].equalsIgnoreCase("-db")){
            //    i+=1;
            //    if(i>=argslength){
            //        System.err.println("Error at -db, missing argument!");
            //        return false;
            //    }
            //    db=args[i];
            //    continue;
            //}// end if -conffile
            if(args[i].equalsIgnoreCase("-in")||args[i].equalsIgnoreCase("-i")||args[i].equalsIgnoreCase("-indir")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -indir, missing argument!");
                    return false;
                }
                indir=args[i];
                continue;
            }// end if -indir
            if(args[i].equalsIgnoreCase("-out")||args[i].equalsIgnoreCase("-o")||args[i].equalsIgnoreCase("-outdir")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -outdir, missing argument!");
                    return false;
                }
                outdir=args[i];
                continue;
            }// end if -outdir
            if((args[i].equalsIgnoreCase("-ext"))||(args[i].equalsIgnoreCase("-e"))){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -ext, missing argument!");
                    return false;
                }
                extension=args[i];
                continue;
            }// end if -ext
            if((args[i].equalsIgnoreCase("-corr"))||(args[i].equalsIgnoreCase("-c"))){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -corr, missing argument!");
                    return false;
                }
                corrmet=args[i];
                if(corrmet.equalsIgnoreCase("POISSON")||corrmet.equalsIgnoreCase("P")
                ||corrmet.equalsIgnoreCase("Tajimanei")||corrmet.equalsIgnoreCase("TN")
                ||corrmet.equalsIgnoreCase("Kimura")||corrmet.equalsIgnoreCase("K")
                ||corrmet.equalsIgnoreCase("NONE")){
                    continue;
                }else{
                    System.err.println("unknown distance correction method "+corrmet);
                    return false;
                }
            }// end if -corr
            if(args[i].equalsIgnoreCase("-verbose")||args[i].equalsIgnoreCase("-v")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -verbose, missing argument!");
                    return false;
                }
                try{
                    verbose=Integer.parseInt(args[i]);
                }catch(NumberFormatException e){
                    System.err.println("unable to parse int from "+args[i]+" in verbose");
                    verbose=1;//default
                    return false;
                }
                continue;
            }// end if -verbose
            if(args[i].equalsIgnoreCase("-bootstrap")||args[i].equalsIgnoreCase("-b")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -bootstrap, missing argument!");
                    return false;
                }
                try{
                    bootstrap=Integer.parseInt(args[i]);
                }catch(NumberFormatException e){
                    System.err.println("unable to parse int from "+args[i]+" in -bootstrap");
                    bootstrap=0;//default
                    return false;
                }
                continue;
            }// end if -bootstrap
            if(args[i].equalsIgnoreCase("-cpu")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -cpu, missing argument!");
                    return false;
                }
                try{
                    cpu=Integer.parseInt(args[i]);
                }catch(NumberFormatException e){
                    System.err.println("unable to parse int from "+args[i]+" in -cpu");
                    cpu=1;//default
                    return false;
                }
                continue;
            }// end if -cpu
            if(args[i].equalsIgnoreCase("-auto")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -auto, missing argument!");
                    return false;
                }
                if((args[i].equalsIgnoreCase("true"))||(args[i].equalsIgnoreCase("t"))){
                    auto=true;
                }else{
                    auto=false;
                }
                continue;
            }// end if -cpu
            if(args[i].equalsIgnoreCase("-redo")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -redo, missing argument!");
                    return false;
                }
                if((args[i].equalsIgnoreCase("true"))||(args[i].equalsIgnoreCase("t"))){
                    redo=true;
                }else{
                    redo=false;
                }
                continue;
            }// end if -cpu
            System.err.println("unknown command "+args[i]);
            return false;
        }// end for i
        return true;
    }// end readargs
    
    //--------------------------------------------------------------------------------------
    
    static boolean readconf(String conffile){
        if((new File(conffile).canRead())==false){
            return false;
        }
        try{
            BufferedReader inread=new BufferedReader(new FileReader(conffile));
            String currstr;
            String[] comarr;
            while((currstr=inread.readLine())!=null){
                if(currstr.indexOf("#")>-1){// if this is a comment
                    continue;
                }
                comarr=currstr.split("\\s",0);
                if(java.lang.reflect.Array.getLength(comarr)<2){//if the line has less than two arguments (i.e. is wrong or empty) skip it.
                    continue;
                }
                if(readargs(comarr)==false){
                    System.err.println("unknown command '"+currstr+"'");
                    return false;
                }
            }// end while
            if(verbose>0){
                System.out.println("done readconf");
            }
            inread.close();
        }catch (IOException e){
            System.err.println("ERROR reading "+conffile);
            e.printStackTrace();
            return false;
        }
        return true;
    }// end readconf
    
    //--------------------------------------------------------------------------
    
    static boolean checkargs(String[] args){
        int argslength=java.lang.reflect.Array.getLength(args);
        for(int i=0;i<argslength;i++){
            if(args[i].equalsIgnoreCase("-conf")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -conf, missing argument!");
                    return false;
                }
                conffile=args[i];
                continue;
            }// end if -conffile
            if(args[i].equalsIgnoreCase("-verbose")||args[i].equalsIgnoreCase("-v")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -verbose, missing argument!");
                    return false;
                }
                try{
                    verbose=Integer.parseInt(args[i]);
                }catch(NumberFormatException e){
                    System.err.println("unable to parse int from "+args[i]+" in verbose");
                    verbose=1;//default
                    return false;
                }
                continue;
            }// end if -verbose
            if(args[i].equalsIgnoreCase("-auto")){
                i+=1;
                if(i>=argslength){
                    System.err.println("Error at -auto, missing argument!");
                    return false;
                }
                if((args[i].equalsIgnoreCase("true"))||(args[i].equalsIgnoreCase("t"))){
                    auto=true;
                }else{
                    auto=false;
                }
                continue;
            }// end if -cpu
            if(args[i].equalsIgnoreCase("-h")||args[i].equalsIgnoreCase("-help")||args[i].equalsIgnoreCase("-?")||args[i].equalsIgnoreCase("?")||args[i].equalsIgnoreCase("-about")){
                printinfo();
                System.exit(0);
            }
        }// end for i
        return true;
    }// end checkargs
    
    //--------------------------------------------------------------------------
    
    static void printconfig(){
        //this should print the current configuratio to screen
        System.out.println("configuration file="+conffile);
        System.out.println("indir="+indir);
        System.out.println("file extension="+extension);
        System.out.println("outdir="+outdir);
        System.out.println("distance correction="+corrmet);
        System.out.println("bootstraps="+bootstrap);
        System.out.println("cpu="+cpu);
        System.out.println("auto="+auto);
        System.out.println("redo="+redo);
        //System.out.println("db="+db);
    }// end printconfig
    
    //--------------------------------------------------------------------------
    
    static File[] getfiles(File dir,String ending){
        File[] retarr=new File [0];
        extensionfilter tmpfilter=new extensionfilter(ending);
        if((ending.length()==0)||(ending.equals(".*"))||(ending.equals("*.*"))||(ending.equals("*"))){
            return dir.listFiles();
        }
        retarr=dir.listFiles(tmpfilter);
        return retarr;
    }// end getfilenames
    
    //--------------------------------------------------------------------------
    
    static class extensionfilter implements FileFilter{
        String ext;
        public extensionfilter(String ext){
            this.ext=ext;
        }
        public boolean accept(java.io.File file) {
            if((file.getName().indexOf(ext))!=-1){
                return true;
            }
            return false;
        }// end accept
    }// end class extensionfilter
    
}// end class
