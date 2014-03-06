package org.blast.parser.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BlastOutput")
public class BlastOutput {

	private String BlastOutput_program;
	private String BlastOutput_version;
	private String BlastOutput_reference;
	private String BlastOutput_db;
	private String BlastOutput_query_ID;
	private String BlastOutput_query_def;
	private String BlastOutput_query_len;
	private BlastOutput_param BlastOutput_param;
	private List<Iteration> BlastOutput_iterations;
	
	@XmlElement(name="BlastOutput_program")
	public String getBlastOutput_program() {
		return BlastOutput_program;
	}
	public void setBlastOutput_program(String blastOutput_program) {
		BlastOutput_program = blastOutput_program;
	}
	
	@XmlElement(name="BlastOutput_version")
	public String getBlastOutput_version() {
		return BlastOutput_version;
	}
	public void setBlastOutput_version(String blastOutput_version) {
		BlastOutput_version = blastOutput_version;
	}
	
	@XmlElement(name="BlastOutput_reference")
	public String getBlastOutput_reference() {
		return BlastOutput_reference;
	}
	public void setBlastOutput_reference(String blastOutput_reference) {
		BlastOutput_reference = blastOutput_reference;
	}
	
	@XmlElement(name="BlastOutput_db")
	public String getBlastOutput_db() {
		return BlastOutput_db;
	}
	public void setBlastOutput_db(String blastOutput_db) {
		BlastOutput_db = blastOutput_db;
	}
	
	@XmlElement(name="BlastOutput_query-ID")
	public String getBlastOutput_query_ID() {
		return BlastOutput_query_ID;
	}
	public void setBlastOutput_query_ID(String blastOutput_query_ID) {
		BlastOutput_query_ID = blastOutput_query_ID;
	}
	
	@XmlElement(name="BlastOutput_query-def")
	public String getBlastOutput_query_def() {
		return BlastOutput_query_def;
	}
	public void setBlastOutput_query_def(String blastOutput_query_def) {
		BlastOutput_query_def = blastOutput_query_def;
	}
	
	@XmlElement(name="BlastOutput_query-len")
	public String getBlastOutput_query_len() {
		return BlastOutput_query_len;
	}
	public void setBlastOutput_query_len(String blastOutput_query_len) {
		BlastOutput_query_len = blastOutput_query_len;
	}

	@XmlElement(name="BlastOutput_param")
	public BlastOutput_param getBlastOutput_param() {
		return BlastOutput_param;
	}
	public void setBlastOutput_param(BlastOutput_param blastOutput_param) {
		BlastOutput_param = blastOutput_param;
	}
	
	@XmlElementWrapper(name="BlastOutput_iterations")
	@XmlElement(name="Iteration")
	public List<Iteration> getBlastOutput_iterations() {
		return BlastOutput_iterations;
	}
	public void setBlastOutput_iterations(
			List<Iteration> blastOutput_iterations) {
		BlastOutput_iterations = blastOutput_iterations;
	}

}
