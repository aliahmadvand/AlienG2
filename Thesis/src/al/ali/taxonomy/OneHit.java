package al.ali.taxonomy;

import java.io.Serializable;

/**
 * @author AHMADVANDA11
 * @date Jan 26, 2013
 */
public class OneHit implements Serializable{
	private String queryId;
	private int hitNo = 1;
	private String hitId;
	private int hitLen = 0;
	private float coverage = 0;
	private String name = "";
	private String nameWithWhiteSpace = "";
	private String taxId;
	private int identities = 0;
	private int alignLen = 0;
	private double score = 0;
	private double evalue;
	private String lineage = "";
	private String annotation = "";
	
public OneHit(String query_id,int hit_no, String hit_id, int hit_len, float cov ,String hit_name, String hit_tax_id, double s, double ev, String lin, String hit_annotation, int iden, int align_len  ){
	this.queryId = query_id;
	this.hitNo = hit_no;
	this.hitId = hit_id;
	this.hitLen = hit_len;
	this.coverage = cov;
	this.name = hit_name;
	this.nameWithWhiteSpace = hit_name;
	this.taxId = hit_tax_id;
	this.identities = iden;
	this.alignLen = align_len;
	this.score = s;
	this.evalue = ev;
	this.lineage = lin;
	this.annotation = hit_annotation;
}

public String getQueryId() {
	return queryId;
}

public int getHitNo() {
	return hitNo;
}

public String getHitId() {
	return hitId;
}

public int getHitLen() {
	return hitLen;
}

public float getCoverage() {
	return coverage;
}

public String getName() {
	return name;
}

public String getNameWithWhiteSpace() {
	return nameWithWhiteSpace;
}

public String getTaxId() {
	return taxId;
}

public int getIdentities() {
	return identities;
}

public int getAlignLen() {
	return alignLen;
}

public double getScore() {
	return score;
}

public double getEvalue() {
	return evalue;
}

public String getLineage() {
	return lineage;
}

public String getAnnotation() {
	return annotation;
}
  



}
