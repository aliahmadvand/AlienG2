package org.blast.parser.objects;

import javax.xml.bind.annotation.XmlElement;

public class Parameters {

	private String Parameters_matrix;
	private int Parameters_expect;
	private int Parameters_gap_open;
	private int Parameters_gap_extend;
	private String Parameters_filter;
	
	@XmlElement(name="Parameters_matrix")
	public String getParameters_matrix() {
		return Parameters_matrix;
	}
	public void setParameters_matrix(String parameters_matrix) {
		Parameters_matrix = parameters_matrix;
	}
	
	@XmlElement(name="Parameters_expect")
	public int getParameters_expect() {
		return Parameters_expect;
	}
	public void setParameters_expect(int parameters_expect) {
		Parameters_expect = parameters_expect;
	}
	
	@XmlElement(name="Parameters_gap-open")
	public int getParameters_gap_open() {
		return Parameters_gap_open;
	}
	public void setParameters_gap_open(int parameters_gap_open) {
		Parameters_gap_open = parameters_gap_open;
	}
	
	@XmlElement(name="Parameters_gap-extend")
	public int getParameters_gap_extend() {
		return Parameters_gap_extend;
	}
	public void setParameters_gap_extend(int parameters_gap_extend) {
		Parameters_gap_extend = parameters_gap_extend;
	}
	
	@XmlElement(name="Parameters_filter")
	public String getParameters_filter() {
		return Parameters_filter;
	}
	public void setParameters_filter(String parameters_filter) {
		Parameters_filter = parameters_filter;
	}
	
}
