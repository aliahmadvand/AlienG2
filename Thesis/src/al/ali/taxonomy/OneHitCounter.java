package al.ali.taxonomy;

import java.io.Serializable;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class OneHitCounter implements Serializable {

	String query_id;
	int hit_counter = 1;
	
	public OneHitCounter(String qi, int ht){
		this.query_id = qi;
		this.hit_counter = ht;
		
	}
	
	public String getQuery_id() {
		return query_id;
	}

	public int getHit_counter() {
		return hit_counter;
	}


}
