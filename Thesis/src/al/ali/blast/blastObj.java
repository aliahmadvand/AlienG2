package al.ali.blast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import al.ali.mysql.MySQLAccess;

public class blastObj {
	private double EvalueMax = 0;


	private float Coverage = 0;
	private double ScoreMin = 0;
	private String group1;
	//private String[] list_group1_taxID;
	//private String[] list_group2_taxID;
	private ArrayList<String> list_group1_taxID = new ArrayList<String>();
	private ArrayList<String> list_group2_taxID = new ArrayList<String>();
	private String group2;
	private String AIEvalue;
	private String ratioScore;
	private String infile;
	
	private String[] exclusion;
	private String[] exclusionTaxId;
	private String formatdb;
	private String blastparam;
	private String blastdb;
	private String blastoutput;
	
	private int numberOfTables;
	
	public blastObj(double evalueMax, float coverage, double scoreMin,
			String group1, String group2, String aIEvalue, String ratioScore, String infile, String formatdb, String blastparam, String blastdb, String blastoutput, String exclusion,int NofTables) throws Exception {
		
		EvalueMax = evalueMax;
		Coverage = coverage;
		ScoreMin = scoreMin;
		this.group1 = group1;
		this.group2 = group2;
		AIEvalue = aIEvalue;
		this.ratioScore = ratioScore;
		this.infile = infile;
		this.formatdb = formatdb;
		this.blastparam = blastparam;
		this.blastdb = blastdb;
		this.blastoutput = blastoutput;
		this.numberOfTables = NofTables;
		
		if(exclusion != null)
			this.exclusion = exclusion.split(";");
		else exclusion = null;
		
		MySQLAccess sql = new MySQLAccess();
		Connection connect=null;
		connect = sql.newConnection();
		
		
		if(exclusion != null){
			this.exclusionTaxId = new String[this.exclusion.length];
			for(int i=0; i<this.exclusion.length; i++){
				this.exclusionTaxId[i] = "No tax_id";
				String taxID = sql.mapNameToId(connect,this.exclusion[i]);
				if(taxID != null)
					exclusionTaxId[i] = taxID;
				
			}
		}
		else exclusionTaxId = null;
		
		
		
		for(int k=0; k<this.getGroup1().split(" 'OR' ").length; k++){
			String taxID = sql.mapNameToId(connect,this.getGroup1().split(" 'OR' ")[k]);
			
			if(taxID != null)
				list_group1_taxID.add(taxID);
			else list_group1_taxID.add("No tax_id");

		}
		
		for(int k=0; k<this.getGroup2().split(" 'OR' ").length; k++){
			String taxID = sql.mapNameToId(connect,this.getGroup2().split(" 'OR' ")[k]);
			
			if(taxID != null)
				list_group2_taxID.add(taxID);
			else list_group2_taxID.add("No tax_id");		}
		
		
		sql.close(connect);
	}
	

	
	
	public void setExclusionTaxId(String[] exclusionTaxId) {
		this.exclusionTaxId = exclusionTaxId;
	}




	public String[] getExclusionTaxId() {
		return exclusionTaxId;
	}




	public ArrayList<String> getList_group1_taxID() {
		return list_group1_taxID;
	}




	public void setList_group1_taxID(ArrayList<String> list_group1_taxID) {
		this.list_group1_taxID = list_group1_taxID;
	}




	public ArrayList<String> getList_group2_taxID() {
		return list_group2_taxID;
	}




	public void setList_group2_taxID(ArrayList<String> list_group2_taxID) {
		this.list_group2_taxID = list_group2_taxID;
	}




	public double getEvalueMax() {
		return EvalueMax;
	}

	public String[] getExclusion() {
		return exclusion;
	}

	public float getCoverage() {
		return Coverage;
	}

	public double getScoreMin() {
		return ScoreMin;
	}
	
	public int getNumberOfTables() {
		return numberOfTables;
	}


	public String getInfile() {
		return infile;
	}



	public String getFormatdb() {
		return formatdb;
	}



	public String getBlastparam() {
		return blastparam;
	}



	public String getBlastdb() {
		return blastdb;
	}



	public String getBlastoutput() {
		return blastoutput;
	}

	public String getGroup1() {
		return group1;
	}

	public String getGroup2() {
		return group2;
	}

	public String getAIEvalue() {
		return AIEvalue;
	}

	public String getRatioScore() {
		return ratioScore;
	}




	public void setGroup1(String group1) {
		this.group1 = group1;
	}




	public void setGroup2(String group2) {
		this.group2 = group2;
	}
	
	
	
	
}
