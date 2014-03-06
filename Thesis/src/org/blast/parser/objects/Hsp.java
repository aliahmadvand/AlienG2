package org.blast.parser.objects;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is a BLAST Hsp
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class Hsp {
	
	private int Hsp_num; 
	private double Hsp_bit_score;
	private int Hsp_score;
	private double Hsp_evalue;
	private int Hsp_query_from;
	private int Hsp_query_to;	
	private int Hsp_hit_from;
	private int Hsp_hit_to;
	private int Hsp_query_frame;
	private int Hsp_hit_frame;
	private int Hsp_identity;
	private int Hsp_positive;
	private int Hsp_gaps;
	private int Hsp_align_len;
	private String Hsp_qseq;
	private String Hsp_hseq;
	private String Hsp_midline;
	
	@XmlElement(name="Hsp_num")
	public int getHsp_num() {
		return Hsp_num;
	}
	public void setHsp_num(int hsp_num) {
		Hsp_num = hsp_num;
	}
	
	@XmlElement(name="Hsp_bit-score")
	public double getHsp_bit_score() {
		return Hsp_bit_score;
	}
	public void setHsp_bit_score(double hsp_bit_score) {
		Hsp_bit_score = hsp_bit_score;
	}
	
	@XmlElement(name="Hsp_score")
	public int getHsp_score() {
		return Hsp_score;
	}
	public void setHsp_score(int hsp_score) {
		Hsp_score = hsp_score;
	}
	
	@XmlElement(name="Hsp_evalue")
	public double getHsp_evalue() {
		return Hsp_evalue;
	}
	public void setHsp_evalue(double hsp_evalue) {
		Hsp_evalue = hsp_evalue;
	}
	
	@XmlElement(name="Hsp_query-from")
	public int getHsp_query_from() {
		return Hsp_query_from;
	}
	public void setHsp_query_from(int hsp_query_from) {
		Hsp_query_from = hsp_query_from;
	}
	
	@XmlElement(name="Hsp_query-to")
	public int getHsp_query_to() {
		return Hsp_query_to;
	}
	public void setHsp_query_to(int hsp_query_to) {
		Hsp_query_to = hsp_query_to;
	}
	
	@XmlElement(name="Hsp_hit-from")
	public int getHsp_hit_from() {
		return Hsp_hit_from;
	}
	public void setHsp_hit_from(int hsp_hit_from) {
		Hsp_hit_from = hsp_hit_from;
	}
	
	@XmlElement(name="Hsp_hit-to")
	public int getHsp_hit_to() {
		return Hsp_hit_to;
	}
	public void setHsp_hit_to(int hsp_hit_to) {
		Hsp_hit_to = hsp_hit_to;
	}
	
	@XmlElement(name="Hsp_query-frame")
	public int getHsp_query_frame() {
		return Hsp_query_frame;
	}
	public void setHsp_query_frame(int hsp_query_frame) {
		Hsp_query_frame = hsp_query_frame;
	}
	
	@XmlElement(name="Hsp_hit-frame")
	public int getHsp_hit_frame() {
		return Hsp_hit_frame;
	}
	public void setHsp_hit_frame(int hsp_hit_frame) {
		Hsp_hit_frame = hsp_hit_frame;
	}
	
	@XmlElement(name="Hsp_identity")
	public int getHsp_identity() {
		return Hsp_identity;
	}
	public void setHsp_identity(int hsp_identity) {
		Hsp_identity = hsp_identity;
	}
	
	@XmlElement(name="Hsp_positive")
	public int getHsp_positive() {
		return Hsp_positive;
	}
	public void setHsp_positive(int hsp_positive) {
		Hsp_positive = hsp_positive;
	}
	
	@XmlElement(name="Hsp_gaps")
	public int getHsp_gaps() {
		return Hsp_gaps;
	}
	public void setHsp_gaps(int hsp_gaps) {
		Hsp_gaps = hsp_gaps;
	}
	
	@XmlElement(name="Hsp_align-len")
	public int getHsp_align_len() {
		return Hsp_align_len;
	}
	public void setHsp_align_len(int hsp_align_len) {
		Hsp_align_len = hsp_align_len;
	}
	
	@XmlElement(name="Hsp_qseq")
	public String getHsp_qseq() {
		return Hsp_qseq;
	}
	public void setHsp_qseq(String hsp_qseq) {
		Hsp_qseq = hsp_qseq;
	}
	
	@XmlElement(name="Hsp_hseq")
	public String getHsp_hseq() {
		return Hsp_hseq;
	}
	public void setHsp_hseq(String hsp_hseq) {
		Hsp_hseq = hsp_hseq;
	}
	
	@XmlElement(name="Hsp_midline")
	public String getHsp_midline() {
		return Hsp_midline;
	}
	public void setHsp_midline(String hsp_midline) {
		Hsp_midline = hsp_midline;
	}
		
}