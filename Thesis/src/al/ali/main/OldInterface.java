package al.ali.main;


import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class OldInterface {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args)  {

		System.out.println(args.length);
		for(int i=0; i <args.length; i++)
			System.out.println(args[i]);
		//Check that arguments were given, otherwise show the help screen
		
		// conf_file = ''
		// out_file = ''


		
		
		
		if(args.length <= 1)
			DisplayHelpInfo();
		else{
			
			// create Options object
			Options options = new Options();
			options.addOption("c", "config", true, "Specify the name of config file,default config name is default.conf.");
			options.addOption("b", "doblast", false, "Runing BLAST executable.");
			options.addOption("t", "loadtaxonomy", true, "Only load NCBI taxonomy database.");
			options.addOption("x", "loadblastxml", true, "Only load BLAST XML output file, but need to do after loading NCBI taxonomy database.");
			options.addOption("o", "output", true, "This option is followed by output file name, will save result to this file.");
			options.addOption("a", "doall", false, "Automatically do all steps to get the result,the result is saved as default outfile outfile.txt.");
			options.addOption("h", "help", false, "what you're looking at right now.");

			
			
			CommandLineParser parser = new PosixParser();
			CommandLine cmd;
			try {
				cmd = parser.parse( options, args);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
				return;
			}
			
			if(cmd.hasOption("c")) {
				System.out.println("salaaaaaaaaam");
			   String config = cmd.getOptionValue("c");
			   //System.out.println(config);
			   if(config == null){
				   System.out.println("You should specify the name to config file");
				   DisplayHelpInfo();
				   return;
			   }
			   else{	
				   	File conf_file = new File(config);
				   	
				   	if(!conf_file.exists()){	
				   		
				        String currentDir = System.getProperty("user.dir");
				        System.out.println("Current dir using System:" +currentDir);
				        currentDir = combine(currentDir, config);
				        System.out.println(currentDir);
				        conf_file = new File(currentDir);
				        if(!conf_file.exists())
				        	return;
				   	}
			   }
			}
			else {;
			//	File conf_file = new File(config);	
			 //   String config = "Default.conf";
			  //  String currentDir = System.getProperty("user.dir");
			}
		
			if(cmd.hasOption("b")) {
				;
			}
			else{
				;
			}
			
			if(cmd.hasOption("t")) {
				;
			}
			else{
				;
			}
			
			if(cmd.hasOption("x")) {
				;
			}
			else{
				;
			}
			
			if(cmd.hasOption("o")) {
				;
			}
			else{
				;
			}
			
			if(cmd.hasOption("a")) {
				;
			}
			else{
				;
			}
			
			if(cmd.hasOption("h")) {
				DisplayHelpInfo();
				return;
			}
			
			
		}
	}
		
	public static void DisplayHelpInfo(){
		System.out.println("NAME:");
		System.out.println("interface.py: a program to do automated parse and analysis to BLAST XML output.");
		System.out.println("");
		System.out.println("SYNOPSIS:");
		System.out.println("interface.py [OPTIONS] file");
		System.out.println("");
		System.out.println("OPTIONS:");
		System.out.println("-c or --config: Specify the name of config file,default config name is default.conf.");
		System.out.println("-b or --doblast: Runing BLAST executable.");
		System.out.println("-t or --loadtaxonomy: Only load NCBI taxonomy database.");
		System.out.println("-x or --loadblastxml: Only load BLAST XML output file, but need to do after loading NCBI taxonomy database.");
		System.out.println("-o or --output: This option is followed by output file name, will save result to this file.");
		System.out.println("-a or --doall: Automatically do all steps to get the result,the result is saved as default outfile outfile.txt.");
		System.out.println("-h or --help: what you're looking at right now.");
		System.out.println("");
		System.out.println("AUTHOR:");
		System.out.println("Ali Ahmadvand (ahmadvanda11@students.ecu.edu)");
		
		return;
	
		 
		}
	
	public static String combine(String path1, String path2)
	{
	    File file1 = new File(path1);
	    File file2 = new File(file1, path2);
	    return file2.getPath();
	}

}
