package ui.ali;

import java.sql.Connection;
import java.util.ArrayList;

import al.ali.mysql.MySQLAccess;

public class result {
	String user = null;
	String AlienG = null;
	String PhyloGenie = null;
	String Status = null;
	
	
	public result(String user, String alienG, String phyloGenie,
			String status) {
		super();
		this.user = user;
		AlienG = alienG;
		PhyloGenie = phyloGenie;
		Status = status;
	}


	


	


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getAlienG() {
		return AlienG;
	}


	public void setAlienG(String alienG) {
		AlienG = alienG;
	}


	public String getPhyloGenie() {
		return PhyloGenie;
	}


	public void setPhyloGenie(String phyloGenie) {
		PhyloGenie = phyloGenie;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}


	public static ArrayList<result> readResult(String user) throws Exception{
		ArrayList<result> res = null;
		
		MySQLAccess sql = new MySQLAccess();
	    Connection connect=null;
	    connect = sql.newConnection();
	    
	    res = sql.getResults(connect, user);
	    
		
		
		return res;
	}
}
