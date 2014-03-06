package org.blast.parser.objects;

import javax.xml.bind.annotation.XmlElement;

public class Statistics {

	private int Statistics_db_num;
	private long Statistics_db_len;
	private int Statistics_hsp_len;
	private long Statistics_eff_space;
	private double Statistics_kappa;
	private double Statistics_lambda;
	private double Statistics_entropy;
	
	@XmlElement(name="Statistics_db-num")
	public int getStatistics_db_num() {
		return Statistics_db_num;
	}
	public void setStatistics_db_num(int statistics_db_num) {
		Statistics_db_num = statistics_db_num;
	}
	
	@XmlElement(name="Statistics_db-len")
	public long getStatistics_db_len() {
		return Statistics_db_len;
	}
	public void setStatistics_db_len(long statistics_db_len) {
		Statistics_db_len = statistics_db_len;
	}
	
	@XmlElement(name="Statistics_hsp-len")
	public int getStatistics_hsp_len() {
		return Statistics_hsp_len;
	}
	public void setStatistics_hsp_len(int statistics_hsp_len) {
		Statistics_hsp_len = statistics_hsp_len;
	}
	
	@XmlElement(name="Statistics_eff-space")
	public long getStatistics_eff_space() {
		return Statistics_eff_space;
	}
	public void setStatistics_eff_space(long statistics_eff_space) {
		Statistics_eff_space = statistics_eff_space;
	}
	
	@XmlElement(name="Statistics_kappa")
	public double getStatistics_kappa() {
		return Statistics_kappa;
	}
	public void setStatistics_kappa(double statistics_kappa) {
		Statistics_kappa = statistics_kappa;
	}
	
	@XmlElement(name="Statistics_lambda")
	public double getStatistics_lambda() {
		return Statistics_lambda;
	}
	public void setStatistics_lambda(double statistics_lambda) {
		Statistics_lambda = statistics_lambda;
	}
	
	@XmlElement(name="Statistics_entropy")
	public double getStatistics_entropy() {
		return Statistics_entropy;
	}
	public void setStatistics_entropy(double statistics_entropy) {
		Statistics_entropy = statistics_entropy;
	}
	
	
}
