package al.ali.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

import al.ali.mysql.MySQLAccess;

public class NCBIToDB {
	
	public void fill_names_db() throws Exception{
	MySQLAccess dao = new MySQLAccess();   
	Scanner stringScanner;
	
	
	// names.dmp to id_to_name_Table in alieng db in mysql
   // BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\ahmadvanda11\\Desktop\\taxdmp\\names.dmp"), 70*1024*1024);
	BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\ahmadvanda11\\Downloads\\taxdump\\names.dmp"));
    String tax_id;
    String name_txt;
    String unique_name;
    String name_class;
    Connection connect=null;
    connect = dao.newConnection();
    
    
	
    while (in.ready()) {
    
   // for(int i=0; i<100; i++){
        String s = in.readLine();
    	//System.out.println(s);
	    stringScanner = new Scanner(s).useDelimiter("\t|\t");
	    	
	    tax_id = stringScanner.next();
	    stringScanner.next();
	    name_txt = stringScanner.next();
	    stringScanner.next();
	    unique_name = stringScanner.next();
	    stringScanner.next();
	    // name_class or name_category
	    name_class = stringScanner.next();
	    	   
/*
 	    System.out.println(tax_id);
	    System.out.println(name_txt);
	    System.out.println(unique_name);
	    System.out.println(name_class);
*/	    
	    /*
	    if list_of_name_objs[0]._name_category == 'scientific name':
	        self._id_to_name_dictionary[list_of_name_objs[0]._tax_id] = list_of_name_objs[0]._name_with_whitespace

	      if self._name_to_id_dictionary.has_key(list_of_name_objs[0]._name):
	        self._name_to_id_dictionary[list_of_name_objs[0]._unique] = list_of_name_objs[0]

	      else:
	        if list_of_name_objs[0]._name:
	          self._name_to_id_dictionary[list_of_name_objs[0]._name] = list_of_name_objs[0]
	         */ 
	    
	    if(name_class.equals("scientific name"))
		    dao.insertToDB(connect, "id_to_name", tax_id.toString(), name_txt);

	    if(dao.hasKey(connect,"name_to_id",name_txt))
	    	if (!unique_name.isEmpty())
	    		dao.insertToDB(connect, "name_to_id",unique_name,tax_id.toString(),name_txt,unique_name, name_class );
	    
	    else if(!name_txt.isEmpty())
	    	dao.insertToDB(connect, "name_to_id",name_txt,tax_id.toString(),name_txt,unique_name, name_class );
	    	

	    // prepare the query
	    //String A = "insert into names values ('"+ tax_id + "'," + '"' + name_txt + '"' + ",'" + unique_name + "','" + name_class + "')";
	    //System.out.println(A);
	    //dao.insertToDB(connect,A);
	    //dao.insertToDB(connect, tax_id, name_txt, unique_name, name_class);
	    //dao.insertToDB(connect, "id_to_name", tax_id.toString(), name_txt);
    }
    dao.close(connect);
	in.close();
    System.out.println("done");

	}
		
}
