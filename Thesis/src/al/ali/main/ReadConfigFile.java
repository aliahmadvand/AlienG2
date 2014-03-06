package al.ali.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.*;

public class ReadConfigFile {
	/*
	 * Uses the properties in java
	public static void main(String[] args)
    {
    	Properties prop = new Properties();
    	try {

            //load a properties file
    		prop.load(new FileInputStream("config.properties"));
              
    		//get the property value and print it out
            System.out.println(prop.getProperty("infile"));
    		System.out.println(prop.getProperty("formatdb"));
    		System.out.println(prop.getProperty("blast"));
    		System.out.println(prop.getProperty("blastparams"));
     		System.out.println(prop.getProperty("blastdb"));
     		System.out.println(prop.getProperty("blastoutput"));
     		System.out.println(prop.getProperty("taxdir"));
     		System.out.println(prop.getProperty("taxmappingdir"));
     		System.out.println(prop.getProperty("exclusion"));
     		System.out.println(prop.getProperty("Evaluemax"));
     		System.out.println(prop.getProperty("coverage"));
     		System.out.println(prop.getProperty("scoremin"));
     		System.out.println(prop.getProperty("table-start"));
     		System.out.println(prop.getProperty("table1"));
     		System.out.println(prop.getProperty("group1"));
     		System.out.println(prop.getProperty("group2"));
     		System.out.println(prop.getProperty("AIEvalue"));
     		System.out.println(prop.getProperty("ratioscore"));
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
 
    }
	*/
	
	
	//using XML
    public static void main(String[] args)
    {
        
			try {
				XMLConfiguration config = new XMLConfiguration("config.xml");
				
				String taxdir = config.getString("taxonomy.taxdir");
				System.out.print(taxdir);

			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			String backColor = config.getString("colors.background");
			String backColor = config.getString("colors.background");
			String backColor = config.getString("colors.background");
			String backColor = config.getString("colors.background");
			 */
    }
}