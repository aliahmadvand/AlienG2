package al.ali.blast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.blast.parser.main.XMLParser;
import org.blast.parser.objects.BlastOutput;
import org.blast.parser.objects.Hit;
import org.blast.parser.objects.Hsp;
import org.blast.parser.objects.Iteration;

import ui.ali.Config;

import com.ali.Interface.javaRunCommand;









//import al.ali.main.serialize;
import al.ali.mysql.MySQLAccess;
import al.ali.taxonomy.OneHit;
import al.ali.taxonomy.OneHitCounter;
public class localBlast {
	static int eleven = 0;
	String program = "blastp";
	int alignment = 100;
	String expectation = "1e-5";
	public String doBlast(blastObj bo){
		String sys_cmd = "E:\\blast\\blastall "+ "-p " + this.program+ " -d " + bo.getBlastdb()+" -i " + bo.getInfile() + " -b "+ this.alignment +" -e "+ this.expectation +" -m 7 -o " + bo.getBlastoutput();
		//sys_cmd = "E:\\blast\\blastall -p blastp -d E:\\nr\\nr -i U:\\MWS\\Thesis\\UserUploadedFiles\\Antonospora_locustae.fas_new -b 100 -e 1e-5 -m 7 -o blastOutput.xml";
		javaRunCommand s = new javaRunCommand();
		//s.run(sys_cmd);
		
		long startTime = System.nanoTime();
		s.run(sys_cmd);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)/ 1E9 + " seconds"); 
		
		
		
		
		return bo.getBlastoutput();
	}
	
	public ArrayList<Integer> ProcessBlastOutput(String BlastXMLoutput, blastObj AlienG_config) throws Exception{
		
		
		ArrayList<Integer> Results = new ArrayList<Integer>();

		MySQLAccess sql = new MySQLAccess();
	    Connection connect=null;
	    connect = sql.newConnection();
		
		
		//reading the blast output
		XMLParser parser = new XMLParser();
		
		BlastOutput blastOutput = null;
		blastOutput = parser.parseXML(BlastXMLoutput);
		
		
		boolean skip_flag;
		int hsp_count;
		int hsp_len;
		float coverage;
		String hit_lineage = "";
		
		
		ArrayList<String> gi_list = new ArrayList<String>();
		
		
		//ArrayList<OneHit> Hit_obj_list = new ArrayList<OneHit>();//#Create Hit list for blast xml output file

		//ArrayList<OneHitCounter> Hit_counter_obj_list = new ArrayList<OneHitCounter>();//Create Hit counter list for blast xml output file,record valid number of hits for each query_id
/*
 		HashMap<String, OneHit> Hit_obj_Map = new HashMap<String, OneHit>();

		HashMap<String, OneHitCounter> Hit_counter_obj_list = new HashMap<String, OneHitCounter>();
		ArrayList<String> Hit_tax_id_list = new ArrayList<String>();//Create tax_id list for every hit of different seq_ID

		
		for (Iteration iteration : blastOutput.getBlastOutput_iterations()){

		
			//String query_id = iteration.getIteration_query_ID();
			String query_id = iteration.getIteration_query_def();
			
		//	if (query_id.equals("Antonospora11  [Antonospora locustae]"))
	//			System.out.print("bale");
				
			int query_len = iteration.getIteration_query_len();
			int hit_no = 0;
			int query_hit_count = 0;
			if(iteration.getIteration_hits() != null)	
			for (Hit alignment : iteration.getIteration_hits()){
				hit_no++;
				skip_flag = false;
				hsp_count = 0;
				for(Hsp hsp : alignment.getHit_hsps()){
					if(hsp_count == 0){//Only get the first hsp
						hsp_len = hsp.getHsp_align_len();
						coverage = ((float)hsp_len/(float)query_len);
						
						if((Double.compare(hsp.getHsp_evalue(), AlienG_config.getEvalueMax()) < 0 ) && ( Double.compare(hsp.getHsp_bit_score(), AlienG_config.getScoreMin()) > 0 ) && (Float.compare(coverage, AlienG_config.getCoverage()) > 0)){
							
							String title = alignment.getHit_id() + " "+ alignment.getHit_def();
							 //never had a case to test this for 
							if(AlienG_config.getExclusion() != null){ 
								for(String excluded: AlienG_config.getExclusion()){
									 if(alignment.getHit_def().toUpperCase().contains(excluded.toUpperCase())){
										 skip_flag = true;
										 break;
									 }
								 }
							}
							if (skip_flag == true){
								System.out.println("For sequence: " + query_id + " Hit No.:  "+  hit_no + " filtered");
								continue;
							}
							
							//#Rule out:Hit without species name,Hit_def:No definition line found
							// i don't really see any case in which this condition wouldn't pass
							if(title.contains("No definition line found") == false){  
								System.out.println("For sequence: " + query_id + " Hit No.:  "+  hit_no + "not filtered");
								System.out.println("title=  " + title);
								
								String t[] = title.split("\\]");
								int h = t.length - 2;
								if(h < 0) h += t.length;
								
								String t1[] = t[h].split("\\[");
								String id[] = alignment.getHit_id().split("\\|");
								String hit_id = id[1];//get gi
								
								boolean if_exist_species_name = false;
								boolean if_species_name_match = false;
								
								if(t1.length == 2){
									if_exist_species_name = true;
									String hit_name = t1[1];
									//String a = "Vavraia culicis 'floridensis'";
                                    int hit_len = alignment.getHit_len();
                                    //strip ...
                                    String hit_annotation = t1[0].substring(t1[0].lastIndexOf('|')+1);
                                   // String[] hit_annotation = temp.split(" ");
                                    
                                    String taxID = sql.mapNameToId(connect,hit_name);
                                    //taxID could be null. need to be checked
								//	System.out.println("sign [] happend !!!!!!");


                                    System.out.println("taxID= " + taxID);
									if(taxID != null){//if species do not have tax_id,no record
										if_species_name_match = true;
	                                     String hit_tax_id = taxID;
	                                     Hit_tax_id_list.add(taxID);
	                                     double hit_score = hsp.getHsp_bit_score();
	                                     double hit_evalue = hsp.getHsp_evalue();
	                                     int hit_identity = hsp.getHsp_identity();
	                                     int hit_align_len = hsp.getHsp_align_len();
	                                     // not sure if these are actually working or not!
	                                     String Key = query_id + "|" + Integer.toString(hit_no);
	                                     Hit_obj_Map.put(Key, new OneHit(query_id, hit_no, hit_id, hit_len, coverage, hit_name, hit_tax_id, hit_score, hit_evalue, hit_lineage, hit_annotation,hit_identity,hit_align_len));
	                                     //Hit_obj_list.add(new OneHit(query_id, hit_no, hit_id, hit_len, coverage, hit_name, hit_tax_id, hit_score, hit_evalue, hit_lineage, hit_annotation,hit_identity,hit_align_len));
	                                     query_hit_count++;
									}
									
									//just for test
									//if(taxID == null)
									//	 System.out.println("ali= ");
                                     
								}
								if(if_exist_species_name == false || if_species_name_match == false){
									// this is test: String hit_def = alignment.getHit_id();
									String hit_def = alignment.getHit_def();
									System.out.println("hit_def= " + alignment.getHit_def() );
									if(hit_def.contains("gi|")){//find gi in hit_def
										String pos[] = hit_def.split("\\|");
										
										if(pos.length >= 2){ // exist gi
											hit_id = pos[1];//get gi
											if(!gi_list.contains(hit_id)){
												//sys_call                                                                                                                                                                                                                                                                                                                                                                                                            
												String sys_cal = "C:\\Program Files\\NCBI\\blast-2.2.28+\\bin\\blastdbcmd -entry " + hit_id + " -db E:\\nr\\nr -outfmt \"%S,%T\" -out tempFile.txt";
												System.out.println("sys_cal= " + sys_cal );
												//call sys_call
												javaRunCommand a = new javaRunCommand();
												a.run(sys_cal);
												String fileLocation = "tempFile.txt";
												
												BufferedReader temp_handle = null;										 
												temp_handle = new BufferedReader(new FileReader(fileLocation));
												String temp_line = temp_handle.readLine();
												//split ..
												//we need strips here
												
												String gi_pos[] = temp_line.split("\\,");
												
												if(gi_pos.length == 2){
													String hit_name = gi_pos[0];
													Integer taxID = Integer.parseInt(gi_pos[1]);
													System.out.println("gi: " + hit_id + " ,hit_name: "+ hit_name + " ,tax_id: " + taxID);
													int hit_len = alignment.getHit_len();
													String hit_annotation = "No species name in BLAST output, so get tax_id from gi";
													String hit_tax_id = taxID.toString();
													Hit_tax_id_list.add(hit_tax_id);
													double hit_score = hsp.getHsp_bit_score();
													double hit_evalue = hsp.getHsp_evalue();
													int hit_identity = hsp.getHsp_identity();
													int hit_align_len = hsp.getHsp_align_len();
													//OneHit a = new OneHit(query_id, hit_no, hit_id, hit_len, coverage, hit_name, hit_tax_id, hit_score, hit_evalue, hit_lineage, hit_annotation,hit_identity,hit_align_len);
													//Hit_obj_list.add(new OneHit(query_id, hit_no, hit_id, hit_len, coverage, hit_name, hit_tax_id, hit_score, hit_evalue, hit_lineage, hit_annotation,hit_identity,hit_align_len));
													String Key = query_id + "|" + Integer.toString(hit_no);
				                                    Hit_obj_Map.put(Key, new OneHit(query_id, hit_no, hit_id, hit_len, coverage, hit_name, hit_tax_id, hit_score, hit_evalue, hit_lineage, hit_annotation,hit_identity,hit_align_len));
													query_hit_count++;	
												}
												else{//using gi could not find tax_id
													if(!gi_list.contains(hit_id)){
														gi_list.add(hit_id);	
													}
													System.out.println("For sequence: " + query_id + " Hit No.:  "+  hit_no + ", we could not find species name and gi,so ignore this record!");
												}
												temp_handle.close();										
											}
										}
										
									}
									else{//could not find gi
										System.out.println("For sequence: " + query_id + " Hit No.:  "+  hit_no + " we could not find species name and gi,so ignore this record!");
									}
								}
								
							}
						}
						else{
							System.out.println("For sequence: " + query_id + " Hit No.:  "+  hit_no + " cut off by Evalue_threshold,ScoreMin,coverage");
						}
						hsp_count++;
							
					}
					
					
				}
			}
			
			//Hit_counter_obj_list.add(qnew OneHitCounter(query_id, query_hit_count));
			Hit_counter_obj_list.put(query_id, new OneHitCounter(query_id, query_hit_count));
		}
	
		
		System.out.println("Hits Done!");
		System.out.println("Start creating lineage information.....");
		
		
		//just for serialization
		serialize("Hit_counter_obj_list_serializatio.txt", Hit_counter_obj_list);
		serialize("Hit_tax_id_list.txt", Hit_tax_id_list);
		serialize("Hit_obj_Map.txt", Hit_obj_Map);
*/	
		
		HashMap<String, OneHitCounter> Hit_counter_obj_list = (HashMap<String, OneHitCounter>)deSerialize("Hit_counter_obj_list_serializatio.txt");
		ArrayList<String> Hit_tax_id_list  = (ArrayList<String>)deSerialize("Hit_tax_id_list.txt");
 		HashMap<String, OneHit> Hit_obj_Map = (HashMap<String, OneHit>)deSerialize("Hit_obj_Map.txt");

 	//	OneHit k = Hit_obj_Map.get("Antonospora11  [Antonospora locustae]|2");
		/*
		 
	        keys_list = node_dictionary._id_to_node_dictionary.keys() 
		*/
		//HashMap<Character, Integer> Map = new HashMap<Character, Integer>();

		
		//same as the getLineage in original AlienG
		//used for getting lineage information for a list of tax_id(s),the lineage information represented as a list of species name with tax_id
		HashMap<String, Zip> child_to_parent_dic = new HashMap<String, Zip>();
		
		ArrayList<String> child_to_parent_taxID = new ArrayList<String>();
		ArrayList<String> child_to_parent_name = new ArrayList<String>();
		
		
		HashMap<String, String> child_to_parent_Map = new HashMap<String, String>();
		ArrayList<String> keys_list = new ArrayList<String>();
		keys_list =	sql.get_id_to_node_dictionaryKeys(connect);
		
		
		//just to make sure we got the keys correctly
		if(keys_list.size() == 0){
			System.out.println("there is no key in the keys_list which means something is wrong");
			return null;
		}
		
		String child_tax_id = null;
		while(Hit_tax_id_list.size() > 0){
			child_tax_id = Hit_tax_id_list.get(0);
			
			if(child_to_parent_Map.containsKey(child_tax_id) || child_to_parent_dic.containsKey(child_tax_id)){ // there was another condition related to child_to_parent_dictionary
				Hit_tax_id_list.remove(0);
				continue;
			}
			if(keys_list.contains(child_tax_id)){
				child_to_parent_taxID.clear();
				child_to_parent_name.clear();
				child_to_parent_taxID.add(child_tax_id);
				child_to_parent_name.add(sql.mapIdToName(connect, child_tax_id));
				
				while(true){
					String parent_tax_id = sql.getParent_IdToNode(connect, child_tax_id);
					if(parent_tax_id.equals("1")){
						break;
					}
					else{
						if(keys_list.contains(parent_tax_id)){
							child_to_parent_taxID.add(parent_tax_id);
							String name = sql.mapIdToName(connect, parent_tax_id);
							child_to_parent_name.add(name);
							
						}
						child_tax_id = parent_tax_id;
					}
				}
				child_to_parent_dic.put(Hit_tax_id_list.get(0), new Zip(child_to_parent_name, child_to_parent_taxID));
				Hit_tax_id_list.remove(0);
			}
			else{
				child_to_parent_taxID.clear();
				child_to_parent_name.clear();
				child_to_parent_taxID.add(child_tax_id);
				child_to_parent_name.add("could not find lineage");
				child_to_parent_dic.put(Hit_tax_id_list.get(0), new Zip(child_to_parent_name, child_to_parent_taxID));
				Hit_tax_id_list.remove(0);
			}
			
			
		}
		System.out.println("Linking children to parents done!");
		
		System.out.println("Create lineage information done successfully!");
		
	//child_to_parent_dic = 
		//getLineage(Hit_tax_id_list);
		
		//Result should be filled here!
		
		Results = getReport(connect,AlienG_config,Hit_counter_obj_list,Hit_obj_Map,child_to_parent_dic,blastOutput);
		sql.close(connect);
		
		
		return Results;
	} 
	
	
	
	
	ArrayList<Integer> getReport(Connection connect,blastObj AlienG_config,HashMap<String,OneHitCounter> hit_counter_obj,HashMap<String, OneHit> hit_dic_obj,HashMap<String,Zip> child_to_parent_dic_obj,BlastOutput blastOutput ) throws FileNotFoundException, UnsupportedEncodingException, SQLException{
		int alignment = 50;
		ArrayList<Integer> result = new ArrayList<Integer>();
		//ArrayList<String> list_of_group1 = new ArrayList<String>();
		int hit_no_1 = -1;
		int hit_no_2 = -1;
		boolean belong_hit1_flag = false;
		//Naming ?
		PrintWriter onlyGroup1File_handle = new PrintWriter("only_group1_file.txt", "UTF-8");
		PrintWriter outfile_handle = new PrintWriter("outfile.txt", "UTF-8");
		
		
		boolean proxyDB = true; //this is just for test
		

		
		
		int count = 0;
        int hit_no = 0;
        double hit1_evalue = 0;
        double hit2_evalue = 0;
        double hit1_score = 0;
        double hit2_score = 0;
        boolean hit1_flag = false;
        boolean hit2_flag = false;
     	boolean if_filtered_flag = false;
		int counter = 0;
		int group1_count = 0;
		int group2_count = 0;
		boolean evalue_flag = false;
        boolean score_flag = false;
        double AIEvalue;
    	double RatioScore;
    	
    	
    	if(proxyDB){
        	ArrayList<String> all = new ArrayList<String>();
        	String[] ee = {"No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "No tax_id", "OR", "33090", "OR", "38254", "OR", "2763", "OR", "No tax_id"};
        	for(int ii=0;ii<ee.length; ii++){
        		all.add(ee[ii]);
        	}
    		AlienG_config.setList_group1_taxID(all );
    		all.clear();
    		String eee = "BACTERIA\" \"OR\" \"ARCHAEA\" \"OR\" \"ALVEOLATA\" \"OR\" \"CRYPTOPHYTA\" \"OR\" \"EUGLENIDA\" \"OR\" \"HAPTOPHYCEAE\" \"OR\" \"RHIZARIA\" \"OR\" \"STRAMENOPILES\" \"OR\" \"VIRIDIPLANTAE\" \"OR\" \"GLAUCOCYSTOPHYCEAE\" \"OR\" \"RHODOPHYTA\" \"OR\" \"VIRUES\"";
    		//all.add(eee);
    		//	for(int ii=0;ii<eee.length; ii++){
        //		all.add(eee[ii]);
        //	}
    		AlienG_config.setGroup1(eee);
    		
    		String[] eTaxID = {"No tax_id", "No tax_id", "28384", "No tax_id", "No tax_id", "No tax_id", "81077", "No tax_id", "No tax_id", "No tax_id", "No tax_id", "No tax_id"};
    		AlienG_config.setExclusionTaxId(eTaxID);
    		
    		all.clear();
    		String[] log1 = {"No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","33090","38254","2763","No tax_id"};
    		for(int ii=0;ii<log1.length; ii++){
        		all.add(log1[ii]);
        	}
    		AlienG_config.setList_group1_taxID(all);		

    	}
    	
/*    	
    	//this is just for debuggin, this part should be deleted later
    	String[]  al = {"No tax_id", "No tax_id", "28384", "No tax_id", "No tax_id", "No tax_id", "81077", "No tax_id", "No tax_id", "No tax_id", "No tax_id", "No tax_id"};
    	AlienG_config.setExclusionTaxId(al);
    	
    	
    	ArrayList<String> all = new ArrayList<String>();
    	String[] ee = {"No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","No tax_id","33090","38254","2763","No tax_id"};
    	for(int ii=0;ii<ee.length; ii++){
    		all.add(ee[ii]);
    	}
		AlienG_config.setList_group1_taxID(all );
		
*/		
		
		
		
		
		int time = 0;
		String TITLE = "query_id\tfirst_hit_id\tsecond_hit_id\tfirst_hit_len\tsecond_hit_len\tfirst_hit_coverage\tsecond_hit_coverage\tfirst_hit_Evalue\tsecond_hit_Evalue \tfirst_hit_Score\tsecond_hit_Score\tfirst_hit_lineage\tfirst_hit_species\tfirst_hit_tax_id\tfirst_hit_identities\tfirst_hit_align_len\tsecond_hit_lineage\tsecond_hit_species\tsecond_hit_tax_id\tsecond_hit_identities\tsecond_hit_align_len\tfirst/second_score_ratio\tAI_Evalue\tfirst_annotation\tsecond_annotation\n";

		// didn't care about this         result = parse_conf_file(conf_file,self._name_dic_obj)
		for(int i=0; i<AlienG_config.getNumberOfTables(); i++){
			System.out.println("this is table" + (i+1));
			outfile_handle.append("Table " + (i+1) + "'s results:\n");
			outfile_handle.write(TITLE + "\n");
			onlyGroup1File_handle.append("Table " + (i+1) + "'s only group 1 results:\n");
			
			counter = 0;
            ArrayList<String> list_of_seqid = new ArrayList<String>();
            
            

            for (Iteration iteration : blastOutput.getBlastOutput_iterations()){
    			time++;
    			System.out.println("time: " + time);
    			if (time == 14) {
    				outfile_handle.close();
    				onlyGroup1File_handle.close();
    				return null;
    			}
    			//String query_id = iteration.getIteration_query_ID();
    			String query_id = iteration.getIteration_query_def();
    			
    			System.out.println("start analysis query_id= " + query_id);
                count = 0;
                hit_no = 0;
                hit1_evalue = 0;
                hit2_evalue = 0;
                hit1_score = 0;
                hit2_score = 0;
                hit1_flag = false;
                hit2_flag = false;
                while(true){
                	hit_no++;
                //	String hit_no_str = Integer.toString(hit_no);
                	String hit_key =query_id + "|" + Integer.toString(hit_no);
                	
                	if(count >= hit_counter_obj.get(query_id).getHit_counter()){
                		if(hit_no == 1){
                			
                			System.out.println("This query_id is prefiltered");
                			System.out.println("query_id: " + query_id);
                		}
                		else{
                			//here we just support one table! if the inputs are more than one table we should do something about it!
                			//System.out.println("this is group 2 : " + AlienG_config.getGroup2());
                			if(hit1_flag && !hit2_flag && AlienG_config.getList_group2_taxID().size() != 0){
                				onlyGroup1File_handle.append(query_id + "\n");
                			}
                		}
                		break;
                	}
                	if(!hit_dic_obj.containsKey(hit_key)){//because when load BLAST XML,some hits have already filtered,so here hor this hits,do not need to consider again
                		System.out.println("hit_no=" + hit_no);
                		System.out.println("this key is pre-filtered");
                		continue;
                	}

                	if_filtered_flag = false;
                	String temp = query_id + "|" + Integer.toString(hit_no);
                	OneHit t = hit_dic_obj.get(temp);
                	String taxID = t.getTaxId();
                	Zip a = child_to_parent_dic_obj.get(taxID);
                	ArrayList<String> lineage_name = null;
            		ArrayList<String> lineage_taxid = null;
                	
                	if (proxyDB){
                		lineage_name = proxyDatabaseName(time);
                		lineage_taxid = proxyDatabaseTaxID(time);
                	}
                	else{
                		lineage_name = a.getChild_to_parent_name();
                		lineage_taxid = a.getChild_to_parent_taxID();
                	}
  /*
                	//just test
                	lineage_name.clear();
                	String[] eee = {"Vavraia culicis 'floridensis'", "Vavraia culicis", "Vavraia", "Pleistophoridae", "Pansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
                	for(int ii=0;ii<eee.length; ii++){
                		lineage_name.add(eee[ii]);
                	}
                	lineage_taxid.clear();
                	String[] eeee = {"948595", "103449", "35235", "35232", "6036", "6029", "4751", "33154", "2759", "131567"};
                	for(int ii=0;ii<eeee.length; ii++){
                		lineage_taxid.add(eeee[ii]);
                	}
                	
                	///////
 */               	
                	
                	
                	
                	System.out.println("lineage_name " + lineage_name);
                	System.out.println("lineage_taxid" + lineage_taxid);
                	
                	
                	for(int j=0; j<AlienG_config.getExclusion().length; j++){
                		String each_filtered_taxid = AlienG_config.getExclusionTaxId()[j];
                		
                		
                		
                		System.out.println("filtered_taxid =  " + each_filtered_taxid);
                		System.out.println("hit_no= " + hit_no);
                		
                		if (each_filtered_taxid == "No tax_id"){
                			continue;
                		}
                		
                		if(lineage_taxid.contains(each_filtered_taxid)){
                			if_filtered_flag = true;
                			System.out.println("this is filtered:");
                			System.out.println("defined filtered_taxid= " + each_filtered_taxid );
                			System.out.println("searched lineage= " + child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId()));
                			
                			
                			
                			break;
                		}
                	}
                	if(if_filtered_flag){
            			count++;
            			continue;
                	}
            		else if(!hit1_flag){
            			count++;
            			
            			System.out.println("this is not filtered: start search hit1");
            			System.out.println("hit1_group_path = " + AlienG_config.getGroup1());
            			

            			a = child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId());
            			if (proxyDB){
            				lineage_name = proxyDatabaseName(time);
                    		lineage_taxid = proxyDatabaseTaxID(time);
                    	}
                    	else{
                    		lineage_name = a.getChild_to_parent_name();
                    		lineage_taxid = a.getChild_to_parent_taxID();
                    	}
//                        System.out.println("lineage1=" + lineage_taxid);
                    	
            			System.out.println("lineage_name " + lineage_name);
                    	System.out.println("lineage_taxid" + lineage_taxid);

                    	group1_count = 0;
                    	
                    	for(String group1:AlienG_config.getList_group1_taxID()){
                            System.out.println("group1= " + group1);
                            
                    		group1_count++;
                    		if(!group1.equals("No tax_id")){
                    			//a little different than the original version of code!
                    			if(lineage_taxid.contains(group1)){
                        			hit1_flag = true;
                        			hit_no_1 = hit_no;
                        			hit1_evalue = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getEvalue();
                        			hit1_score = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getScore();
                        			break;
                    			}
                    			else{
                    				if(group1_count == AlienG_config.getList_group1_taxID().size()) break; //the first is not the specified hit,skip,go to check next seq_id
                    				else continue;//continue to check every group1 defined in OR combination
                    			}
                    			
                    		}
                    		else{
                            	//System.out.println("lineage1= " + lineage_taxid );

                    			continue; //continue to check other defined group1
                    		}
                    			
                    	}
                    	if(hit1_flag){
                    		System.out.println("find hit1 group,start search hit2 group");
                    		System.out.println("query_id= "+ query_id);
                    		System.out.println("hit no= "+ hit_no);
//                    		Zip tempp = child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId());
                    		System.out.println("hit1 tax_id= " + child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId()));
                    		System.out.println("hit1 evalue= " + hit1_evalue);
                    		System.out.println("hit1 score= " + hit1_score);
                    		continue;
                    	}
                    	else{
                    		System.out.println("This query_id do not have top first hit!");
                    		System.out.println("query_id= "+ query_id);

                    		break;
                    	}
                    	
                    	
                    	
            		}
            		else if(hit1_flag && AlienG_config.getGroup2().length() == 0){//when group2 is null,do not need to check whether there has group2 existed or not
            			System.out.println("grou2 is defined as null");
            			if(hit_no_1 == -1) System.out.println("WRONG WRONG ");
            			list_of_seqid.add(query_id + "|" + Integer.toString(hit_no_1));
            			WriteToFile(outfile_handle, query_id + "|" + Integer.toString(hit_no_1),"","","",hit_dic_obj, child_to_parent_dic_obj);
            			counter++;
            			break;
            		}
            		else if(hit1_flag && !hit2_flag && AlienG_config.getGroup2().length() != 0){
//            			System.out.println("we are in and this is h:" + time);
            			System.out.println("group2 is defined = " + AlienG_config.getGroup2());
            			belong_hit1_flag = false;
                        count++;
//                      System.out.println("this is not filtered: start search hit2");
                        a = child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId());
                    	
                        if (proxyDB){
                        	lineage_name = proxyDatabaseName(time);
                    		lineage_taxid = proxyDatabaseTaxID(time);
                    	}
                    	else{
                    		lineage_name = a.getChild_to_parent_name();
                    		lineage_taxid = a.getChild_to_parent_taxID();
                    	}

                    	System.out.println("lineage_name " + lineage_name);
                    	System.out.println("lineage_taxid" + lineage_taxid);
                    	System.out.println("hit_no= " + hit_no + " , lineage2= " + lineage_taxid);

//                    	System.out.println("list_of_groip1" + AlienG_config.getGroup1());

                        group1_count = 0;
                        
                        for(String group1 : AlienG_config.getList_group1_taxID()){
//                        	System.out.println("GGroup1" + group1);

                        	// reverse and list been missed again!
                        	System.out.println("find hit2 process:group1= " + group1);
                        	group1_count ++;
                        	if(!group1.equals("No tax_id")){
                            

                        		if(lineage_taxid.contains(group1)){
                        			belong_hit1_flag = true;
                        			System.out.println("this hit belongs to group 1, skip");
                        			break;
                        		}
                        		else{
                        			if(group1_count == AlienG_config.getList_group1_taxID().size())
                        				break;//the first is not the specified hit,skip,go to check next seq_id
                        			else
                        				continue;//continue to check every group1 defined in OR combination
                        		}
                        	}
                        	else
                        		continue;//continue to check other defined group1
                        }
                        
            		}
        			

                	if (belong_hit1_flag) //this hit belongs to one of defined group1,ignore 
                		continue;
                	if(AlienG_config.getGroup2().equals("*")){
                		System.out.println("definied group2 is *");
                		hit2_flag = true;
                		hit_no_2 = hit_no;
                		hit2_evalue = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getEvalue();
                		hit2_score = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getScore();
                		System.out.println("find hit2 group!");
                		System.out.println("query_id= " + query_id);
                		System.out.println("hit no= " + hit_no);
                		System.out.println("hit2 tax_id= " + child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId()));
                		System.out.println("hit2 evalue= " + hit2_evalue);
                		System.out.println("hit2 score= " + hit2_score);
                	}
                	else{
                		System.out.println("group2 is not *");
                		
                		group2_count = 0;
                		
                		a = child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId());
                     	
                		if (proxyDB){
                			lineage_name = proxyDatabaseName(time);
                    		lineage_taxid = proxyDatabaseTaxID(time);
                    	}
                    	else{
                    		lineage_name = a.getChild_to_parent_name();
                     		lineage_taxid = a.getChild_to_parent_taxID();
                    	}
                		System.out.println("lineage_name " + lineage_name);
                    	System.out.println("lineage_taxid" + lineage_taxid);
                     	for(String group2:AlienG_config.getList_group2_taxID()){
                    		System.out.println("group2=" + group2 );

                     		group2_count++;
                    		if(!group2.equals("No tax_id")){
                    			//a little different than the original version of code!
                    			if(lineage_taxid.contains(group2)){
                        			hit2_flag = true;
                        			hit_no_2 = hit_no;
                        			hit2_evalue = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getEvalue();
                        			hit2_score = hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getScore();
                        			System.out.println("find hit2 group!");
                            		System.out.println("query_id= " + query_id);
                            		System.out.println("hit no= " + hit_no);
                            		System.out.println("hit2 tax_id= " + child_to_parent_dic_obj.get(hit_dic_obj.get(query_id + "|" + Integer.toString(hit_no)).getTaxId()));
                            		System.out.println("hit2 evalue= " + hit2_evalue);
                            		System.out.println("hit2 score= " + hit2_score);
                        			break;
                    			}
                    			else{
                    				if(group2_count == AlienG_config.getList_group1_taxID().size()){//could not find hit belongs to group2
                                		System.out.println("This query_id has first top hit,but do not have second top hit!");
                    					System.out.println("query_id= " + query_id);
                    					break;//the second is not the specified hit,skip,go to check next seq_id
                    				}
                    				else continue;//continue to check every group2 defined in OR combination
                    			}
                    			
                    		}
                    		else{
                    			continue;//continue to check other defined group2 in relation OR
                    		}
                    			
                    	}
                	}
                	
                	if(hit1_flag && hit2_flag){//find first top hit and second top hit group,continue to check if meet the required condition
                		System.out.println("find hit2 group,continue to check if this sequence id meet the required condition");
						evalue_flag = false;
                        score_flag = false;
                        
                        if(hit1_evalue != 0){
                        	AIEvalue = GetAIEvalue(hit1_evalue,hit2_evalue);
                        }
                        else{
                        	AIEvalue = Double.parseDouble(AlienG_config.getAIEvalue());
                        }
                        System.out.println("seq_id AIEvalue=" + AIEvalue);
                        System.out.println("value[condition]=" + Double.parseDouble(AlienG_config.getAIEvalue()));
                        if(AIEvalue >= Double.parseDouble(AlienG_config.getAIEvalue())){
                        	System.out.println("SEQ_ID AIEVALUE ( " + AIEvalue + " ) IS LARGER THAN GIVEN AIEVALUE"); 
                            evalue_flag = true;
                		}
                        
                        RatioScore =GetRatio(hit1_score,hit2_score);//Caculate the this seq_id's RatioScore
                        System.out.println("seq_id RatioScore=" + RatioScore);
                        System.out.println("ScoreMin=" + AlienG_config.getRatioScore());
                        if (RatioScore >= Double.parseDouble(AlienG_config.getRatioScore())){
            				score_flag = true;
            			}
                        if(evalue_flag && score_flag){
                        	System.out.println("This " + query_id +" is selected as HGT candidate");
                        	System.out.println("query_id= " + query_id + " hit_1= " + hit_no_1 + " hit_2= " + hit_no_2 + " AIEvalue= " + AIEvalue + " ratioscore= " + RatioScore);
                        	list_of_seqid.add(query_id + "|" + Integer.toString(hit_no_1));
                        	WriteToFile(outfile_handle,query_id + "|" + Integer.toString(hit_no_1),query_id + "|" + Integer.toString(hit_no_2),String.valueOf(AIEvalue), String.valueOf(RatioScore), hit_dic_obj, child_to_parent_dic_obj);
                            counter++;
                        }
                        else{
                        	System.out.println("This " +  query_id + " meet first top hit and second top hit,but do not meet the required condition!");
                        	System.out.println("query_id= " + query_id + " hit_1= " + hit_no_1 + " hit_2= " + hit_no_2 + " AIEvalue= " + AIEvalue + " ratioscore= " + RatioScore);
                        }
                        break;
                	}
                	else{
                		System.out.println("This " + query_id + " meet first top hit, but do not have second top hit!");
                		System.out.println("query_id= " + query_id);
                		break;
                	}
                	
                }
    		}
			
		}

		outfile_handle.close();
		onlyGroup1File_handle.close();
		
		return result;
	}

	public blastObj readConfigFromUI(Config co) throws Exception{
		
		double EvalueMax = 0;
		float Coverage = 0;
		double ScoreMin = 0;
		String group1 = "";
		String group2 = "";
		String AIEvalue = "";
		String ratioScore = "";
		String infile = "";
		String formatdb = "";
		String blastparam = "";
		String blastdb = "";
		String blastoutput = "blastOutput.xml";
		String exclusion = "";
		int NofTables = 1;
		
		AIEvalue = co.getAlienG_AIEvalue();
		EvalueMax = co.getAlienG_EvalueMax();
		Coverage = co.getAlienG_Coverage();
		ScoreMin = co.getAlienG_ScoreMin();
		group1 = co.getAlienG_group1();
		group2 = co.getAlienG_group2();
		ratioScore = co.getAlienG_ratioScore();
		infile = co.getAlienG_infile();
		blastparam = co.getAlienG_blastparam();
		exclusion = co.getAlienG_exclusion();
		blastdb = co.getAlienG_blastdb();
		
		
		return (new blastObj(EvalueMax,Coverage,ScoreMin,group1,group2,AIEvalue,ratioScore, infile, formatdb, blastparam, blastdb,blastoutput,exclusion,NofTables));
	}
	public blastObj readAlienGConfigFile(String fileName) throws Exception{
		
		//BlastXML parser
		//Evalue-thresh
		//coverage_thresh
		//scoremin_thresh
	double EvalueMax = 0;
	float Coverage = 0;
	double ScoreMin = 0;
	String group1 = "";
	String group2 = "";
	String AIEvalue = "";
	String ratioScore = "";
	String infile = "";
	String formatdb = "";
	String blastparam = "";
	String blastdb = "";
	String blastoutput = "";
	String exclusion = "";
	int NofTables = 0;
	
	try {
		XMLConfiguration config = new XMLConfiguration(fileName);
		
		EvalueMax = Double.parseDouble(config.getString("parsing.Evaluemax"));
		Coverage  = Float.parseFloat(config.getString("parsing.coverage"));
		ScoreMin = Double.parseDouble(config.getString("parsing.scoremin"));
		group1 = config.getString("parsing.table1.group1");
		group2 = config.getString("parsing.table1.group2");
		AIEvalue = config.getString("parsing.table1.AIEvalue");
		ratioScore = config.getString("parsing.table1.ratioscore");
		NofTables = config.getInt("parsing.numberOfTables");
		
		infile = config.getString("blast.infile");
		formatdb = config.getString("blast.formatdb");
		blastparam = config.getString("blast.blastparam");
		blastdb = config.getString("blast.blastdb");
		blastoutput = config.getString("blast.blastoutput");
		
		exclusion = config.getString("parsing.exclusion");
		System.out.println("NofTables: " + NofTables);

		System.out.println("exclusion: " + exclusion);
		System.out.println("EvalueMax: " + EvalueMax);
		System.out.println("coverge: " + Coverage);
		System.out.println("scoreMin: " + ScoreMin);
		System.out.println("group1: " + group1);
		System.out.println("group2: " + group2);
		System.out.println("AIEvalue: " + AIEvalue);
		System.out.println("ratioScore: " + ratioScore);
		
		System.out.println("infile: " + infile);
		System.out.println("formatdb: " + formatdb);
		System.out.println("blastparam: " + blastparam);
		System.out.println("blastdb: " + blastdb);
		System.out.println("blastoutput: " + blastoutput);
		

	} catch (ConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
	return (new blastObj(EvalueMax,Coverage,ScoreMin,group1,group2,AIEvalue,ratioScore, infile, formatdb, blastparam, blastdb,blastoutput,exclusion,NofTables));
}
	
	
	void WriteToFile(PrintWriter outfile, String first_seq_id, String second_seq_id, String aievalue, String ratioscore, HashMap<String, OneHit> hit_dic_obj,HashMap<String,Zip> child_to_parent_dic_obj){
		boolean proxyDB = true; // just for test
		String string_line = "";
		
		if(second_seq_id == ""){
			string_line = hit_dic_obj.get(first_seq_id).getQueryId() + "\t";
			string_line = string_line + ""+"\t";
			string_line = string_line + hit_dic_obj.get(first_seq_id).getHitId() + "\t";
			string_line = string_line + ""+"\t";
			string_line = string_line + hit_dic_obj.get(first_seq_id).getHitLen() + "\t";
			string_line = string_line + ""+"\t";
			string_line = string_line + hit_dic_obj.get(first_seq_id).getCoverage() + "\t";
			string_line = string_line + ""+"\t";
			string_line = string_line + hit_dic_obj.get(first_seq_id).getEvalue() + "\t";
			string_line = string_line + ""+"\t";
			string_line = string_line + hit_dic_obj.get(first_seq_id).getScore() + "\t";
			string_line = string_line + ""+"\t";

		}
		else{
			string_line = hit_dic_obj.get(first_seq_id).getQueryId() + "\t";
			System.out.println(string_line);
			string_line = string_line + hit_dic_obj.get(first_seq_id).getHitId() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getHitId() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(first_seq_id).getHitLen() + "\t";
			System.out.println(string_line);
			string_line = string_line + hit_dic_obj.get(second_seq_id).getHitLen() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(first_seq_id).getCoverage() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getCoverage() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(first_seq_id).getEvalue() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getEvalue() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(first_seq_id).getScore() + "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getScore() + "\t";
			System.out.println(string_line);
		}	
		Zip a = child_to_parent_dic_obj.get(hit_dic_obj.get(first_seq_id).getTaxId());
		ArrayList<String> lineage_name = new ArrayList<String>();
		ArrayList<String> lineage_taxid = new ArrayList<String>();
		if (proxyDB){
			eleven=1;
			lineage_name = proxyDatabaseName(11);
    		lineage_taxid = proxyDatabaseTaxID(11);
    	}
    	else{
    		lineage_name = a.getChild_to_parent_name();
    		lineage_taxid = a.getChild_to_parent_taxID();
    	}
		System.out.println("lineage_name " + lineage_name);
    	System.out.println("lineage_taxid" + lineage_taxid);
		/*
		 lineage_name = list(lineage_name)
                lineage_name.reverse()
		string_line = string_line + str(lineage_name[1:]) + '\t'
		 
		 * 
		 * 
		 */
    	Collections.reverse(lineage_name);
    	string_line = string_line + lineage_name.subList(1, lineage_name.size()) + "\t";
		System.out.println(string_line);

		string_line = string_line + hit_dic_obj.get(first_seq_id).getNameWithWhiteSpace()+ "\t";
		System.out.println(string_line);

		string_line = string_line + hit_dic_obj.get(first_seq_id).getTaxId()+ "\t";
		System.out.println(string_line);

		string_line = string_line + hit_dic_obj.get(first_seq_id).getIdentities()+ "\t";
		System.out.println(string_line);

		string_line = string_line + hit_dic_obj.get(first_seq_id).getAlignLen()+ "\t";
		System.out.println(string_line);
		if(second_seq_id == ""){
			string_line = string_line + "" + "\t";
            string_line = string_line + "" + "\t";
            string_line = string_line + "" + "\t";
            string_line = string_line + "" + "\t";
            string_line = string_line + "" + "\t";
		}
		else{
			a = child_to_parent_dic_obj.get(hit_dic_obj.get(second_seq_id).getTaxId());
			if (proxyDB){
				eleven=2;
				lineage_name = proxyDatabaseName(11);
	    		lineage_taxid = proxyDatabaseTaxID(11);
	    	}
	    	else{
	    		lineage_name = a.getChild_to_parent_name();
				lineage_taxid = a.getChild_to_parent_taxID();
	    	}
			System.out.println("lineage_name " + lineage_name);
        	System.out.println("lineage_taxid" + lineage_taxid);
			/*
			 lineage_name = list(lineage_name)
             lineage_name.reverse()
             string_line = string_line + str(lineage_name[1:]) + '\t'
			 * 
			 */
        	Collections.reverse(lineage_name);
        	string_line = string_line + lineage_name.subList(1, lineage_name.size()) + "\t";
			System.out.println(string_line);

			OneHit k = hit_dic_obj.get(second_seq_id);
			string_line = string_line + hit_dic_obj.get(second_seq_id).getNameWithWhiteSpace()+ "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getTaxId()+ "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getIdentities()+ "\t";
			System.out.println(string_line);

			string_line = string_line + hit_dic_obj.get(second_seq_id).getAlignLen()+ "\t";
			System.out.println(string_line);

			string_line = string_line + ratioscore + "\t";
			System.out.println(string_line);

			string_line = string_line + aievalue + "\t";

		}
		string_line = string_line + hit_dic_obj.get(first_seq_id).getAnnotation() + "\t";
		System.out.println(string_line);

		if(second_seq_id == ""){
			string_line = string_line + "" + "\n";
		}
		else{
			string_line = string_line + hit_dic_obj.get(second_seq_id).getAnnotation() + "\n";
			System.out.println(string_line);

		}
		outfile.append(string_line);
			
		
	}
	
	double GetAIEvalue(double hit1_evalue,double hit2_evalue){
		return Math.log10(hit2_evalue/hit1_evalue);
	}
	
	double GetRatio(double num1, double num2){
		if (num2 !=0){
			double result = num1/num2;
			return result;
		}
		else return -1;
	}
	
	
	
	
	//These next two methods are here just to test!
	public static void serialize(String outFile, Object serializableObject) throws IOException {
	    FileOutputStream fos = new FileOutputStream(outFile);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(serializableObject);
	}
		 
	public static Object deSerialize(String serilizedObject) throws FileNotFoundException, IOException, ClassNotFoundException {
	    FileInputStream fis = new FileInputStream(serilizedObject);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    return ois.readObject();
	}
	
	private static ArrayList<String> proxyDatabaseName(int time){
		ArrayList<String> lineage_name = new ArrayList<String>();
		
		if (time == 1){
			
        	String[] eee = {"Vavraia culicis 'floridensis'", "Vavraia culicis", "Vavraia", "Pleistophoridae", "Pansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
	
		}
		else if (time == 2){
			String[] eee = {"Encephalitozoon cuniculi GB-M1", "Encephalitozoon cuniculi", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 3){
			String[] eee = {"Encephalitozoon cuniculi GB-M1", "Encephalitozoon cuniculi", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}	
		}
		else if (time == 4){
			String[] eee = {"Encephalitozoon hellem ATCC 50504", "Encephalitozoon hellem", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 5){
			String[] eee = {"Nosema bombycis CQ1", "Nosema bombycis", "Nosema", "Nosematidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 6){
			String[] eee = {"Vavraia culicis 'floridensis'", "Vavraia culicis", "Vavraia", "Pleistophoridae", "Pansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 7){
			String[] eee = {"Encephalitozoon intestinalis ATCC 50506", "Encephalitozoon intestinalis", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 8){
			String[] eee = {"Edhazardia aedis USNM 41457", "Edhazardia aedis", "Edhazardia", "Culicosporidae", "Microsporidia incertae sedis", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}		
		}
		else if (time == 9){
			System.out.print("yani che");
		}
		else if (time == 10){
			String[] eee = {"Pseudomonas sp. CF150", "Pseudomonas", "Pseudomonadaceae", "Pseudomonadales", "Gammaproteobacteria", "Proteobacteria", "Bacteria", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}		
		else if (time == 11){
			if (eleven >= 2){
				String[] eee = {"Planctomyces brasiliensis DSM 5305", "Planctomyces brasiliensis", "Planctomyces", "Planctomycetaceae", "Planctomycetales", "Planctomycetia", "Planctomycetes", "Bacteria", "cellular organisms"};
	        	for(int ii=0;ii<eee.length; ii++){
	        		lineage_name.add(eee[ii]);
	        	}
				
			}
			else if(eleven <= 1){
				String[] eee = {"Prunus persica", "Prunus", "Amygdaleae", "Maloideae", "Rosaceae", "Rosales", "fabids", "rosids", "core eudicotyledons", "eudicotyledons", "Magnoliophyta", "Spermatophyta", "Euphyllophyta", "Tracheophyta", "Embryophyta", "Streptophytina", "Streptophyta", "Viridiplantae", "Eukaryota", "cellular organisms"};
	        	for(int ii=0;ii<eee.length; ii++){
	        		lineage_name.add(eee[ii]);
	        	}
			}
			
		}
		else if (time == 12){
			String[] eee = {"Encephalitozoon cuniculi", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}
		}
		else if (time == 13){
			String[] eee = {"Encephalitozoon intestinalis ATCC 50506", "Encephalitozoon intestinalis", "Encephalitozoon", "Unikaryonidae", "Apansporoblastina", "Microsporidia", "Fungi", "Opisthokonta", "Eukaryota", "cellular organisms"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_name.add(eee[ii]);
        	}	
		}
		
		return lineage_name;
		
	}
	private static ArrayList<String> proxyDatabaseTaxID(int time){
		
ArrayList<String> lineage_taxid = new ArrayList<String>();
		
		if (time == 1){
			
        	String[] eee = {"948595", "103449", "35235", "35232", "6036", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
	
		}
		else if (time == 2){
			String[] eee = {"284813", "6035", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 3){
			String[] eee = {"284813", "6035", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}	
		}
		else if (time == 4){
			String[] eee = {"907965", "27973", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 5){
			String[] eee = {"578461", "27978", "27977", "27974", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 6){
			String[] eee = {"948595", "103449", "35235", "35232", "6036", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 7){
			String[] eee = {"876142", "58839", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 8){
			String[] eee = {"1003232", "70536", "70535", "322152", "469895", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}		
		}
		else if (time == 9){
			System.out.print("yani che");
		}
		else if (time == 10){
			String[] eee = {"911240", "286", "135621", "72274", "1236", "1224", "2", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}		
		else if (time == 11){
			if (eleven >= 2){
				String[] eee = {"756272", "119", "118", "126", "112", "203683", "203682", "2", "131567"};
	        	for(int ii=0;ii<eee.length; ii++){
	        		lineage_taxid.add(eee[ii]);
	        	}
				
			}
			else if(eleven <= 1){
				String[] eee = {"3760", "3754", "721805", "171637", "3745", "3744", "91835", "71275", "91827", "71240", "3398", "58024", "78536", "58023", "3193", "131221", "35493", "33090", "2759", "131567"};
	        	for(int ii=0;ii<eee.length; ii++){
	        		lineage_taxid.add(eee[ii]);
	        	}
			}
						
			
			
			eleven++;
		}
		else if (time == 12){
			String[] eee = {"6035", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}
		}
		else if (time == 13){
			String[] eee = {"876142", "58839", "6033", "36734", "6032", "6029", "4751", "33154", "2759", "131567"};
        	for(int ii=0;ii<eee.length; ii++){
        		lineage_taxid.add(eee[ii]);
        	}	
		}
		
		return lineage_taxid;
	
		
	}
}


