package al.ali.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import ui.ali.Config;
import ui.ali.result;



public class MySQLAccess {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  
  
  public Connection newConnection() throws Exception{
	  Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      Connection connect = DriverManager
          .getConnection("jdbc:mysql://localhost/alieng?"
              + "user=root&password=1234");
	  return connect;
  }
  
  /*
  public void insertToDB(Connection connection, String...strings ) throws SQLException
  {
	  PreparedStatement ps = connection.prepareStatement("insert into id_to_name values (?, ?, ?, ?)");
	  ps.setString(1, strings[0]);
	  ps.setString(2, strings[1]);
	  ps.setString(3, strings[2]);
	  ps.setString(4, strings[3]);
	  //System.out.println(ps.toString());
	  ps.execute();
  }
  */
  public void insertToDB(Connection connect,String A) throws Exception{
	  
	  try{
		  

	     
	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      PreparedStatement ps = connect.prepareStatement(A);
	      // Result set get the result of the SQL query
	     //statement.executeUpdate(A);
	      ps.execute();
	  }catch (Exception e) {
		  throw e;
	  } finally {
	      
		  close();
	  }
	  
  }
  
 public void insertToDB(Connection connect,String Table, String A,String B) throws SQLException{
	  
	 PreparedStatement ps = connect.prepareStatement("insert into " + Table + " values (?, ?)");
	  ps.setString(1, A);
	  ps.setString(2, B);
	  //System.out.println(ps.toString());
	  ps.execute();
	  
  }
 
 public void insertToDB(Connection connect,String Table, String A,String B, String C, String D, String E) throws SQLException{
	  
	 PreparedStatement ps = connect.prepareStatement("insert into " + Table + " values (?, ?, ?, ?, ?)");
	  ps.setString(1, A);
	  ps.setString(2, B);
	  ps.setString(3, C);
	  ps.setString(4, D);
	  ps.setString(5, E);

	  //System.out.println(ps.toString());
	  ps.execute();
	  
  }
 

	public String mapNameToId(Connection connect, String name) throws SQLException{
		String taxID = null;

		PreparedStatement ps = connect.prepareStatement("SELECT obj_id FROM name_to_id WHERE obj_name = ?");
		ps.setString(1, name);
		resultSet = ps.executeQuery();

		
		if(resultSet.next()){
			taxID = resultSet.getString("obj_id");
			return taxID;
			
		}
		return taxID;
	}

	public String getParent_IdToNode(Connection connect, String child_tax_id) throws SQLException{
		String result = null;
		
		PreparedStatement ps = connect.prepareStatement("SELECT parent_tax_id FROM id_to_node WHERE tax_id = ?");
		ps.setString(1, child_tax_id);
		resultSet = ps.executeQuery();

		
		if(resultSet.next()){
			result = resultSet.getString("parent_tax_id");
			return result;
			
		}
		return result;
	}

	public String mapIdToName(Connection connect, String taxId) throws SQLException{
		String name = null;

		PreparedStatement ps = connect.prepareStatement("SELECT name FROM id_to_name WHERE tax_id = ?");
		ps.setString(1, taxId);
		resultSet = ps.executeQuery();

		
		if(resultSet.next()){
			name = resultSet.getString("name");
			return name;
			
		}
		return name;
	}
	
	public ArrayList<String> get_id_to_node_dictionaryKeys(Connection connect) throws SQLException{
		PreparedStatement ps = connect.prepareStatement("SELECT tax_id FROM id_to_node");
		resultSet = ps.executeQuery();
		ArrayList<String> result = new ArrayList<String>();
		while(resultSet.next()){
			result.add(resultSet.getString("tax_id"));
		}
		return result;
	}
  
  public void close(Connection connect) {
	    try {
	      
	      if (connect != null) {
	        connect.close();
	      }
	    } catch (Exception e) {
	    }
	  }
  
  public boolean hasKey(Connection connect,String Table,String key) throws SQLException{
	  ResultSet resultSet = null;
	  //Statement a = connect.createStatement();
	  //String query = "select 1 from " + Table + " where name= '" + key + "'";
	  PreparedStatement ps = connect.prepareStatement("select 1 from " + Table + " where name= ?");
	  ps.setString(1, key);
	  
	  //resultSet = a.executeQuery(query);
	  resultSet = ps.executeQuery();

	  if (!resultSet.next())
		  return false;
	  return true;
  }
  
  public void readDataBase() throws Exception {
    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost/feedback?"
              + "user=sqluser&password=sqluserpw");

      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      // Result set get the result of the SQL query
      resultSet = statement
          .executeQuery("select * from FEEDBACK.COMMENTS");
      writeResultSet(resultSet);

      // PreparedStatements can use variables and are more efficient
      preparedStatement = connect
          .prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
      // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
      // Parameters start with 1
      preparedStatement.setString(1, "Test");
      preparedStatement.setString(2, "TestEmail");
      preparedStatement.setString(3, "TestWebpage");
      preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
      preparedStatement.setString(5, "TestSummary");
      preparedStatement.setString(6, "TestComment");
      preparedStatement.executeUpdate();

      preparedStatement = connect
          .prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);

      // Remove again the insert comment
      preparedStatement = connect
      .prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
      preparedStatement.setString(1, "Test");
      preparedStatement.executeUpdate();
      
      resultSet = statement
      .executeQuery("select * from FEEDBACK.COMMENTS");
      writeMetaData(resultSet);
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }

  private void writeMetaData(ResultSet resultSet) throws SQLException {
    //   Now get some metadata from the database
    // Result set get the result of the SQL query
    
    System.out.println("The columns in the table are: ");
    
    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
    }
  }

  private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String user = resultSet.getString("myuser");
      String website = resultSet.getString("webpage");
      String summery = resultSet.getString("summery");
      Date date = resultSet.getDate("datum");
      String comment = resultSet.getString("comments");
      System.out.println("User: " + user);
      System.out.println("Website: " + website);
      System.out.println("Summery: " + summery);
      System.out.println("Date: " + date);
      System.out.println("Comment: " + comment);
    }
  }

  // You need to close the resultSet
  private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }
  
  public ArrayList<result> getResults(Connection connect, String user) throws SQLException{
		ArrayList<result> ress = new ArrayList<result>();
		PreparedStatement ps = connect.prepareStatement("select * from orders");
		resultSet = ps.executeQuery();

		
		while(resultSet.next()){
			
			if(user.equals(resultSet.getString("User"))){
				result j = new result(resultSet.getString("User"),resultSet.getString("AlienG"),resultSet.getString("PhyloGenie"),resultSet.getString("Status"));

				ress.add(j);
			}
			
		}
		return ress;
  }
  
  public boolean validUser(Connection connect, String userName, String pass) throws SQLException{
	  PreparedStatement ps = connect.prepareStatement("select * from users where User=? and Password =? ");
	  ps.setString(1, userName);
	  ps.setString(2, pass);

		resultSet = ps.executeQuery();
		
		if(resultSet.next()) return true;
		else return false;
		
  }
  
  public boolean newUser(Connection connect, String userName) throws SQLException{
	  PreparedStatement ps = connect.prepareStatement("select * from users where User=?");
	  ps.setString(1, userName);

		resultSet = ps.executeQuery();
		
		if(!resultSet.next()) return true;
		else return false;
		
  }
  
  public void addUser(Connection connect, String userName, String pass, String firstname, String lastname) throws SQLException{
	  PreparedStatement ps = connect.prepareStatement("insert into users  values (?, ?,?,?)");
	  ps.setString(1, userName);
	  ps.setString(2, pass);
	  ps.setString(3, firstname);
	  ps.setString(4, lastname);

		ps.execute();

  }
  
  
  public void addRequest(Connection connect, String user, Config configFile) throws SQLException{
	  PreparedStatement ps = connect.prepareStatement("insert into orders (User, alienG_EvalueMax , alienG_Coverage , alienG_ScoreMin , alienG_group1 , alienG_group2 , alienG_AIEvalue , alienG_ratioScore , alienG_infile , alienG_exclusion , alienG_formatdb , alienG_blastparam , alienG_blastdb , alienG_blastoutput , alienG_numberOfTables , Status , AlienG , PhyloGenie) values (?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	  ps.setString(1, user);
	  ps.setString(2, String.valueOf(configFile.getAlienG_EvalueMax()));
	  ps.setString(3, String.valueOf(configFile.getAlienG_Coverage()));
	  ps.setString(4, String.valueOf(configFile.getAlienG_ScoreMin()));
	  ps.setString(5, configFile.getAlienG_group1());
	  ps.setString(6, configFile.getAlienG_group2());
	  ps.setString(7, configFile.getAlienG_AIEvalue());
	  ps.setString(8, configFile.getAlienG_ratioScore());
	  ps.setString(9, configFile.getAlienG_infile());
	  ps.setString(10, configFile.getAlienG_exclusion());
	  ps.setString(11, configFile.getAlienG_formatdb());
	  ps.setString(12, configFile.getAlienG_blastparam());
	  ps.setString(13, configFile.getAlienG_blastdb());
	  ps.setString(14, configFile.getAlienG_blastoutput());
	  ps.setString(15, String.valueOf(configFile.getAlienG_numberOfTables()));
	  ps.setString(16,"Waiting To Be Proceed");
	  ps.setString(17, "Not Ready Yet");
	  ps.setString(18, "Not Ready Yet");

	  ps.execute();

  }
  
  
  public Config getOnerequest(Connection connect) throws SQLException{
	  PreparedStatement ps = connect.prepareStatement("select * from orders where  Status = \"Waiting To be Proceed\" LIMIT 0, 1");
	  
	  Config res = new Config();

		resultSet = ps.executeQuery();
		  if(!resultSet.next()) return null;
		  
		  
		  else {
			  String user = resultSet.getString("User");
			  String status = resultSet.getString("Status");
			  res.setAlienG_AIEvalue(resultSet.getString("alienG_AIEvalue"));
			  res.setAlienG_blastdb(resultSet.getString("alienG_blastdb"));
			  res.setAlienG_blastoutput(resultSet.getString("alienG_blastoutput"));
			  res.setAlienG_blastparam(resultSet.getString("alienG_blastparam"));
			  res.setAlienG_Coverage(Float.valueOf(resultSet.getString("alienG_Coverage")));
			  res.setAlienG_EvalueMax(Double.valueOf(resultSet.getString("alienG_EvalueMax")));
			  res.setAlienG_exclusion(resultSet.getString("alienG_exclusion"));
			  res.setAlienG_formatdb(resultSet.getString("alienG_formatdb"));
			  res.setAlienG_group1(resultSet.getString("alienG_group1"));
			  res.setAlienG_group2(resultSet.getString("alienG_group2"));
			  res.setAlienG_infile(resultSet.getString("alienG_infile"));
			  res.setAlienG_numberOfTables(Integer.valueOf(resultSet.getString("alienG_numberOfTables")));
			  res.setAlienG_ratioScore(resultSet.getString("alienG_ratioScore"));
			  res.setAlienG_ScoreMin(Double.valueOf(resultSet.getString("alienG_ScoreMin")));
			  
			  return res;
		  }

  }
  
} 