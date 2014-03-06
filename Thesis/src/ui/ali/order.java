package ui.ali;

import java.sql.Connection;

import al.ali.mysql.MySQLAccess;

public class order {
	String user;
	String configFile;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public static void addRequest(String user, Config configFile) throws Exception{
		MySQLAccess sql = new MySQLAccess();
	    Connection connect=null;
	    connect = sql.newConnection();
	    
	    sql.addRequest(connect, user, configFile);
		
	}
}
