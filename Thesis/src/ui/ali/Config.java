package ui.ali;

public class Config {
	private double alienG_EvalueMax;
	private float alienG_Coverage;
	private double alienG_ScoreMin;
	private String alienG_group1;
	private String alienG_group2;
	private String alienG_AIEvalue;
	private String alienG_ratioScore;
	private String alienG_infile;
//	private String[] alienG_exclusion;
	private String alienG_exclusion;
	private String alienG_formatdb;
	private String alienG_blastparam;
	private String alienG_blastdb = "E:\\nr\\nr";
	private String alienG_blastoutput;
	private int alienG_numberOfTables;
	public double getAlienG_EvalueMax() {
		return alienG_EvalueMax;
	}
	public void setAlienG_EvalueMax(double alienG_EvalueMax) {
		this.alienG_EvalueMax = alienG_EvalueMax;
	}
	public float getAlienG_Coverage() {
		return alienG_Coverage;
	}
	public void setAlienG_Coverage(float alienG_Coverage) {
		this.alienG_Coverage = alienG_Coverage;
	}
	public double getAlienG_ScoreMin() {
		return alienG_ScoreMin;
	}
	public void setAlienG_ScoreMin(double alienG_ScoreMin) {
		this.alienG_ScoreMin = alienG_ScoreMin;
	}
	public String getAlienG_group1() {
		return alienG_group1;
	}
	public void setAlienG_group1(String alienG_group1) {
		this.alienG_group1 = alienG_group1;
	}
	public String getAlienG_group2() {
		return alienG_group2;
	}
	public void setAlienG_group2(String alienG_group2) {
		this.alienG_group2 = alienG_group2;
	}
	public String getAlienG_AIEvalue() {
		return alienG_AIEvalue;
	}
	public void setAlienG_AIEvalue(String alienG_AIEvalue) {
		this.alienG_AIEvalue = alienG_AIEvalue;
	}
	public String getAlienG_ratioScore() {
		return alienG_ratioScore;
	}
	public void setAlienG_ratioScore(String alienG_ratioScore) {
		this.alienG_ratioScore = alienG_ratioScore;
	}
	public String getAlienG_infile() {
		return alienG_infile;
	}
	public void setAlienG_infile(String alienG_infile) {
		this.alienG_infile = alienG_infile;
	}
	/*
	public String[] getAlienG_exclusion() {
		return alienG_exclusion;
	}
	*/
	
	public String getAlienG_exclusion() {
		return alienG_exclusion;
	}
	
	public void setAlienG_exclusion(String alienG_exclusion) {
		
		this.alienG_exclusion = alienG_exclusion;
				//.split(";");
	}
	public String getAlienG_formatdb() {
		return alienG_formatdb;
	}
	public void setAlienG_formatdb(String alienG_formatdb) {
		this.alienG_formatdb = alienG_formatdb;
	}
	public String getAlienG_blastparam() {
		return alienG_blastparam;
	}
	public void setAlienG_blastparam(String alienG_blastparam) {
		this.alienG_blastparam = alienG_blastparam;
	}
	public String getAlienG_blastdb() {
		return alienG_blastdb;
	}
	public void setAlienG_blastdb(String alienG_blastdb) {
		this.alienG_blastdb = alienG_blastdb;
	}
	public String getAlienG_blastoutput() {
		return alienG_blastoutput;
	}
	public void setAlienG_blastoutput(String alienG_blastoutput) {
		this.alienG_blastoutput = alienG_blastoutput;
	}
	public int getAlienG_numberOfTables() {
		return alienG_numberOfTables;
	}
	public void setAlienG_numberOfTables(int alienG_numberOfTables) {
		this.alienG_numberOfTables = alienG_numberOfTables;
	}
	
}
