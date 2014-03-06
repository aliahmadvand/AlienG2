package org.blast.parser.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Iteration {

	private int Iteration_iter_num;
	private String Iteration_query_ID;
	private String Iteration_query_def;
	private int Iteration_query_len;
	private List<Hit> Iteration_hits;
	private Iteration_stat Iteration_stat;
	
	@XmlElement(name="Iteration_iter-num")
	public int getIteration_iter_num() {
		return Iteration_iter_num;
	}
	public void setIteration_iter_num(int iteration_iter_num) {
		Iteration_iter_num = iteration_iter_num;
	}
	
	@XmlElement(name="Iteration_query-ID")
	public String getIteration_query_ID() {
		return Iteration_query_ID;
	}
	public void setIteration_query_ID(String iteration_query_ID) {
		Iteration_query_ID = iteration_query_ID;
	}
	
	@XmlElement(name="Iteration_query-def")
	public String getIteration_query_def() {
		return Iteration_query_def;
	}
	public void setIteration_query_def(String iteration_query_def) {
		Iteration_query_def = iteration_query_def;
	}
	
	@XmlElement(name="Iteration_query-len")
	public int getIteration_query_len() {
		return Iteration_query_len;
	}
	public void setIteration_query_len(int iteration_query_len) {
		Iteration_query_len = iteration_query_len;
	}
	
	@XmlElementWrapper(name="Iteration_hits")
	@XmlElement(name="Hit")
	public List<Hit> getIteration_hits() {
		return Iteration_hits;
	}
	public void setIteration_hits(List<Hit> iteration_hits) {
		Iteration_hits = iteration_hits;
	}
	
	@XmlElement(name="Iteration_stat")
	public Iteration_stat getIteration_stat() {
		return Iteration_stat;
	}
	public void setIteration_stat(Iteration_stat iteration_stat) {
		Iteration_stat = iteration_stat;
	}
	
}
