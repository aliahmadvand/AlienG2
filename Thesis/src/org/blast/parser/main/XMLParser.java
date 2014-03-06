package org.blast.parser.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

//import org.apache.log4j.Logger;
import org.blast.parser.objects.BlastOutput;

public class XMLParser {

	//private Logger log = Logger.getLogger(XMLParser.class);
	private final String DTO_PACKAGE_PATH = "org.blast.parser.objects";
	/*
	public static void main(String[] args) throws Throwable{

		File file = null;
		BlastOutput blast_output = null;
		
		try{
						
			file = new File("blastOutput.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance( DTO_PACKAGE_PATH );
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			blast_output = (BlastOutput) unmarshaller.unmarshal(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
			XMLPrinter printer = null;
			try{
				printer = new XMLPrinter();
				printer.print(blast_output);
			} catch(Throwable u){
				log.error("Fail.", u);
				throw u;
			}
			
		} catch(Throwable t){
			
			log.error("Fail.", t);
			throw t;
			
		}
	}
	*/
	public BlastOutput parseXML(String address) throws JAXBException, UnsupportedEncodingException, FileNotFoundException{

			File file = null;
			BlastOutput blast_output = null;
							
			file = new File(address);
			JAXBContext jaxbContext = JAXBContext.newInstance( DTO_PACKAGE_PATH );
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			blast_output = (BlastOutput) unmarshaller.unmarshal(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
					
			return blast_output;
	}
}
