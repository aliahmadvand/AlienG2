package al.ali.main;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import ui.ali.Config;
import al.ali.blast.blastObj;
import al.ali.blast.localBlast;
import al.ali.mysql.MySQLAccess;
import al.ali.taxonomy.OneHitCounter;

public class Interface {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	/*
	
	public static void main(String[] args) throws Exception {
		
		
		
		localBlast LBlast = new localBlast();
		
		//AlienG config file
		blastObj AlienG_config = LBlast.readAlienGConfigFile("config.xml");
		
		//running the blast!
		//String BlastXMLoutput = LBlast.doBlast(AlienG_config);		
		//String BlastXMLoutput = LBlast.localBlast(AlienG_config);	 
		//	ArrayList<Integer> AlienGOutput =LBlast.ProcessBlastOutput(BlastXMLoutput, AlienG_config);
		ArrayList<Integer> AlienGOutput =LBlast.ProcessBlastOutput("blastOutput.xml", AlienG_config);

		//running the PhyloGenie
		com.ali.Interface.main.phylogenie(AlienGOutput);
	}
	*/
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("salam");
		while(true){
			Thread.sleep(5000);
			Config order = readOneRequest();
		/*
		localBlast LBlast = new localBlast();
		
		//AlienG config file
		blastObj AlienG_config = LBlast.readAlienGConfigFile("config.xml");
		
		//running the blast!
		//String BlastXMLoutput = LBlast.doBlast(AlienG_config);		
		//String BlastXMLoutput = LBlast.localBlast(AlienG_config);	 
		//	ArrayList<Integer> AlienGOutput =LBlast.ProcessBlastOutput(BlastXMLoutput, AlienG_config);
		ArrayList<Integer> AlienGOutput =LBlast.ProcessBlastOutput("blastOutput.xml", AlienG_config);

		//running the PhyloGenie
		com.ali.Interface.main.phylogenie(AlienGOutput);
		*/
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void alieng2(Config co) throws Exception{
						
		localBlast LBlast = new localBlast();
		
		//AlienG Config file
		
		blastObj AlienG_config = LBlast.readConfigFromUI(co);
		
		//running the blast!
		String BlastXMLoutput = LBlast.doBlast(AlienG_config);		

		//	ArrayList<Integer> AlienGOutput =LBlast.ProcessBlastOutput(BlastXMLoutput, AlienG_config);
//		ArrayList<Integer> AlienGOutput;
	
//		AlienGOutput = LBlast.ProcessBlastOutput("U:\\MWS\\Thesis\\Antonospora_locustae_100.xml", AlienG_config);
			//running the PhyloGenie
//		com.ali.Interface.main.phylogenie(AlienGOutput);

		
		
	}
	
	public static void alieng3(Config co){
		//System.out.println("Welcome to Abadan");
		System.out.println(co.getAlienG_AIEvalue());
		
		
		localBlast LBlast = new localBlast();
		
		
	}
	
	public static Config readOneRequest() throws Exception{
		Config res;
		MySQLAccess sql = new MySQLAccess();
	    Connection connect=null;
	    connect = sql.newConnection();
	    
	    res = sql.getOnerequest(connect);
	    
	    if (res == null)
	    	;
	    else{
	    	System.out.println("na baba");
	    }
		
		return res;
	}

}
