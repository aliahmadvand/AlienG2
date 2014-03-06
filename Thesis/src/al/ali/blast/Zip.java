package al.ali.blast;

import java.util.ArrayList;

public class Zip {
	private ArrayList<String> child_to_parent_name;
	private ArrayList<String> child_to_parent_taxID;

	public Zip(ArrayList<String> child_to_parent_name,
			ArrayList<String> child_to_parent_taxID) {
		this.child_to_parent_name = child_to_parent_name;
		this.child_to_parent_taxID = child_to_parent_taxID;
	}
	
	public ArrayList<String> getChild_to_parent_name() {
		return child_to_parent_name;
	}

	public ArrayList<String> getChild_to_parent_taxID() {
		return child_to_parent_taxID;
	}
	
	
}
