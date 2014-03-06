package com.ali.Interface;

import java.util.ArrayList;

import com.ali.Blammer.blammer;
import com.ali.Mtree.mainline;
import com.ali.Multiblast.multiblast;

public class main {

	/**
	 * @param args
	 */
	
	static String formatdbpath = "C:\\Users\\ahmadvanda11\\Dropbox\\blast\\formatdb ";
	static String infilename = "blastquery.fasta";
	static String blastPath = "C:\\Users\\ahmadvanda11\\Dropbox\\blast\\blastall ";
	
	static String cpu = "1";
	static String blastDB = "E:\\nr";
	static String blammerConf = "blammer.conf";
	static boolean doBlast = true;
	static String blastDir = "blastresults";
	//static String[] treeParams = {"-ext", ".cln", "-i", "C:\\Users\\ahmadvanda11\\Dropbox\\ThesisSourceCode\\PhyloGenie\\blastresults", "-o", "C:\\Users\\ahmadvanda11\\Dropbox\\ThesisSourceCode\\PhyloGenie\\blastresults\\trees", "-bootstrap", "100", "-auto", "t"};
	
	//static String[] treeParams = {"-ext", ".hln", "-i", "blastresults", "-o", "blastresults\\trees", "-bootstrap", "100", "-auto", "t"};
	static String treeDirName = blastDir + "\\trees";
	static Integer useCln = 0;
	static char redo = 'F';
	static String[] doRedo= {"-redo","T"};
	static String[] noRedo= {"-redo","F"};
	
	
	//static String blammerParams = "-doext f -dotax t -taxid t -dohmmb f -dohmms f -dohmma f";
	
	public static void main(String[] args) {
		
		
		
		
		
		long startTime = System.nanoTime();
		
		//System.out.print(System.getProperty("sun.arch.data.model")); 
		// TODO Auto-generated method stub
		
		//first step would be having the config file
		
		// I HAVE TO READ FROM THE CONFIG FILE!!!!! %^*&%^&(^*& I HAVE TO
		
		//default version
		/*
		String[] blammerParams = {"-doext", "f", "-dotax", "f", "-taxid" ,"t", "-dohmmb", "f", "-dohmms", "f", "-dohmma", "f"};	
		String[] treeParams = {"-ext", ".hln", "-i", "blastresults", "-o", "blastresults\\trees", "-bootstrap", "100", "-auto", "t"};	
		String blastParams = "-p blastp -v 1000 -b 500";	
		*/
		//	phlogenie config file
		
		
		 //  from working machine
		String blastParams = "-p blastp -e 10";
		String[] blammerParams = {"-coverage" ,"0.6" ,"-blastmax" ,"1e-5" ,"-doext" ,"t" ,"-dotax" ,"t" ,"-taxid" ,"f" ,"-verbose" ,"1"};
		String[] treeParams = {"-ext" ,".hln" ,"-in" ,blastDir ,"-out" ,blastDir + "\\trees" ,"-bootstrap" ,"100" ,"-auto" ,"t" ,"-verbose" ,"1"};


		/*

#-blastdb databases to blast against (input file is appended)
-blastdb=/home/sungl/swissprot/uniprot_sprot.fasta

		 */
		
		
		//after the config
		//First Format the database
		
		String formatdbcommand = formatdbpath + " -p T -i " + infilename;
		System.out.println("Doing " + formatdbcommand);
		javaRunCommand cmd = new javaRunCommand();
		cmd.run(formatdbcommand);
		System.out.println("#### formatdb done!");
		
		//Create the directory for output
		
		
		//Now I have a blastable database and need to run the blast searches
		
		
		if(doBlast == true){
			String baseBlastCommand = blastPath + blastParams + " -T T -I T";
			System.out.println("Starting blast runs ;baseCommand = " + baseBlastCommand);
			multiblast blast = new multiblast();
			//String[] argM = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",blastDB ,"-infile",infilename,"-outdir", "blastresults"};
			//String[] tempArray = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",infilename+ " "+blastDB ,"-infile",infilename,"-outdir", "blastresults"};
			String[] tempArray = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",infilename ,"-infile",infilename,"-outdir", blastDir};
			
			
			String[] argM;
			
			
			//check if we have to redo or not
			if (redo == 'T') argM = concat(tempArray, doRedo);
			else argM = concat(tempArray, noRedo);
			
			blast.main(argM);
			System.out.println("DONE BLAST");
		}
		
		//Next use blammer to create alignments
		blammer bl = new blammer();
		//String[] argB = {"-conf",blammerConf,blammerParams, "-cpu", cpu, "-i","/blastresults/*.bls", "-db", infilename + " " + blastDB };
		//String[] argB = {"-conf",blammerConf, "-cpu", cpu, "-i","C:\\Users\\ahmadvanda11\\Dropbox\\ThesisSourceCode\\PhyloGenie\\blastresults\\gi-10639186-emb-CAC11188.1-.bls", "-db", infilename + " " + blastDB };
		
		// this is just a test to put -db as infilename!
		
		String[] j = {"-conf",blammerConf, "-cpu", cpu, "-i",blastDir + "\\*.bls", "-db", infilename};
		//String[] j = {"-conf",blammerConf, "-cpu", cpu, "-i","blastresults\\*.bls", "-db", infilename + " " + blastDB };
		String[] tempArray2 = concat(j, blammerParams);
		
		String[] argB;
		if (redo == 'T') argB = concat(tempArray2, doRedo);
		else argB = concat(tempArray2, noRedo);
		
		System.out.println("Starting Blammer run");
		bl.main(argB);
		System.out.println("Done Blammer");
		
		//now blammer should have produced all the alignments necessary
		
		String extStr;
		
		if(useCln == 1){
			extStr = ".cln";	
		}
		else{
			extStr = ".hln";
		}
		
		mainline tr = new mainline();
		String[] argT = {"-in",blastDir, "-out", treeDirName, "-ext", extStr };
		String[] f = concat(argT, treeParams);
		System.out.println("Calculating Trees");
		tr.main(f);
		System.out.println("Done tree building");
		System.out.println("Done All");
		
		
		
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 
		
	}
	
	
public static void phylogenie(ArrayList<Integer> input) {
		
		
		
		
		
		long startTime = System.nanoTime();
		
		//System.out.print(System.getProperty("sun.arch.data.model")); 
		// TODO Auto-generated method stub
		
		//first step would be having the config file
		
		// I HAVE TO READ FROM THE CONFIG FILE!!!!! %^*&%^&(^*& I HAVE TO
		
		//default version
		/*
		String[] blammerParams = {"-doext", "f", "-dotax", "f", "-taxid" ,"t", "-dohmmb", "f", "-dohmms", "f", "-dohmma", "f"};	
		String[] treeParams = {"-ext", ".hln", "-i", "blastresults", "-o", "blastresults\\trees", "-bootstrap", "100", "-auto", "t"};	
		String blastParams = "-p blastp -v 1000 -b 500";	
		*/
		//	phlogenie config file
		
		 //  from working machine
		String blastParams = "-p blastp -e 10";
		String[] blammerParams = {"-coverage" ,"0.6" ,"-blastmax" ,"1e-5" ,"-doext" ,"t" ,"-dotax" ,"t" ,"-taxid" ,"f" ,"-verbose" ,"1"};
		String[] treeParams = {"-ext" ,".hln" ,"-in" ,blastDir ,"-out" ,blastDir + "\\trees" ,"-bootstrap" ,"100" ,"-auto" ,"t" ,"-verbose" ,"1"};


		/*

#-blastdb databases to blast against (input file is appended)
-blastdb=/home/sungl/swissprot/uniprot_sprot.fasta

		 */
		
		
		//after the config
		//First Format the database
		
		String formatdbcommand = formatdbpath + " -p T -i " + infilename;
		System.out.println("Doing " + formatdbcommand);
		javaRunCommand cmd = new javaRunCommand();
		cmd.run(formatdbcommand);
		System.out.println("#### formatdb done!");
		
		//Create the directory for output
		
		
		//Now I have a blastable database and need to run the blast searches
		
		
		if(doBlast == true){
			String baseBlastCommand = blastPath + blastParams + " -T T -I T";
			System.out.println("Starting blast runs ;baseCommand = " + baseBlastCommand);
			multiblast blast = new multiblast();
			//String[] argM = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",blastDB ,"-infile",infilename,"-outdir", "blastresults"};
			//String[] tempArray = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",infilename+ " "+blastDB ,"-infile",infilename,"-outdir", "blastresults"};
			String[] tempArray = {"-cpu",cpu,"-blast", baseBlastCommand, "-db",infilename ,"-infile",infilename,"-outdir", blastDir};
			
			
			String[] argM;
			
			
			//check if we have to redo or not
			if (redo == 'T') argM = concat(tempArray, doRedo);
			else argM = concat(tempArray, noRedo);
			
			blast.main(argM);
			System.out.println("DONE BLAST");
		}
		
		//Next use blammer to create alignments
		blammer bl = new blammer();
		//String[] argB = {"-conf",blammerConf,blammerParams, "-cpu", cpu, "-i","/blastresults/*.bls", "-db", infilename + " " + blastDB };
		//String[] argB = {"-conf",blammerConf, "-cpu", cpu, "-i","C:\\Users\\ahmadvanda11\\Dropbox\\ThesisSourceCode\\PhyloGenie\\blastresults\\gi-10639186-emb-CAC11188.1-.bls", "-db", infilename + " " + blastDB };
		
		// this is just a test to put -db as infilename!
		
		String[] j = {"-conf",blammerConf, "-cpu", cpu, "-i",blastDir + "\\*.bls", "-db", infilename};
		//String[] j = {"-conf",blammerConf, "-cpu", cpu, "-i","blastresults\\*.bls", "-db", infilename + " " + blastDB };
		String[] tempArray2 = concat(j, blammerParams);
		
		String[] argB;
		if (redo == 'T') argB = concat(tempArray2, doRedo);
		else argB = concat(tempArray2, noRedo);
		
		System.out.println("Starting Blammer run");
		bl.main(argB);
		System.out.println("Done Blammer");
		
		//now blammer should have produced all the alignments necessary
		
		String extStr;
		
		if(useCln == 1){
			extStr = ".cln";	
		}
		else{
			extStr = ".hln";
		}
		
		mainline tr = new mainline();
		String[] argT = {"-in",blastDir, "-out", treeDirName, "-ext", extStr };
		String[] f = concat(argT, treeParams);
		System.out.println("Calculating Trees");
		tr.main(f);
		System.out.println("Done tree building");
		System.out.println("Done All");
		
		
		
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 
		
	}
	static String[] concat(String[] A, String[] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   String[] C= new String[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}

}
