package org.blast.parser.objects;

import javax.xml.bind.annotation.XmlElement;

public class BlastOutput_param {

	private Parameters parameter;

	@XmlElement(name="Parameters")
	public Parameters getParameter() {
		return parameter;
	}

	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}
	
}
