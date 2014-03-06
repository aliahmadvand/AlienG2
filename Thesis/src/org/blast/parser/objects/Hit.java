package org.blast.parser.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This class is a BLAST Hit
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class Hit {

	private int Hit_num;
	private int Hit_accession;
	private int Hit_len;
	private String Hit_id;
	private String Hit_def;
	private List<Hsp> Hit_hsps;
	
	@XmlElement(name="Hit_num")
	public int getHit_num() {
		return Hit_num;
	}
	public void setHit_num(int hit_num) {
		Hit_num = hit_num;
	}
	
	@XmlElement(name="Hit_accession")
	public int getHit_accession() {
		return Hit_accession;
	}
	public void setHit_accession(int hit_accession) {
		Hit_accession = hit_accession;
	}
	
	@XmlElement(name="Hit_len")
	public int getHit_len() {
		return Hit_len;
	}
	public void setHit_len(int hit_len) {
		Hit_len = hit_len;
	}

	@XmlElement(name="Hit_id")
	public String getHit_id() {
		return Hit_id;
	}
	public void setHit_id(String hit_id) {
		Hit_id = hit_id;
	}
	
	@XmlElement(name="Hit_def")
	public String getHit_def() {
		return Hit_def;
	}
	public void setHit_def(String hit_def) {
		Hit_def = hit_def;
	}
	
	@XmlElementWrapper(name="Hit_hsps")
	@XmlElement(name="Hsp")
	public List<Hsp> getHit_hsps() {
		return Hit_hsps;
	}
	public void setHit_hsps(List<Hsp> hit_hsps) {
		Hit_hsps = hit_hsps;
	}



}