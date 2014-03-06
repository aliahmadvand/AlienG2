package al.ali.main;

public class fillTheDataBase {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		NCBIToDB a = new NCBIToDB();
		a.fill_names_db();
	}

}
