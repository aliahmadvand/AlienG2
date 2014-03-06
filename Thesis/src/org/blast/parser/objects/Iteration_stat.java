package org.blast.parser.objects;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class is a BLAST Statistic
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class Iteration_stat {

	private Statistics statistics;

	@XmlElement(name="Statistics")
	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	
}
