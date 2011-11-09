package owlaccessor;

import java.io.File;
import java.sql.*;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;



public class DBMigrater {

	/**
	 * @param args
	 */
	private Connection con;
	private Statement stmt;
	
	private String path = "C:/Users/Zilong Chang/Documents/WORK/Ontology/pato.owl";
	
	private String dburl = "jdbc:mysql://localhost:3306/phenoscape";
	private String uname= "termsuser";
	private String upw = "termspassword";
	
	public void work(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con = DriverManager.getConnection(dburl, uname, upw);
				stmt = con.createStatement();
				OWLAccessor oa = new OWLAccessorImpl(new File(path));
				for (OWLClass c : oa.getAllClasses()){
					Set<String> kw =oa.getKeywords(c);
					String label = oa.getLabel(c);
					if (!kw.isEmpty()&&!label.equals("")){
						for (String s : kw){
							try{
								stmt.executeUpdate(
										"INSERT INTO patokeywords(term, keyword) VALUES('"
										+label.trim().replaceAll("'", "''")+"','"+
										s.trim().replaceAll("'", "''")+"')");
						
							}catch(SQLException e){
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		DBMigrater dbm = new DBMigrater();
		dbm.work();
		System.out.println("DONE!");

	}

}
