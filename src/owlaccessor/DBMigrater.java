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
	
	public DBMigrater(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/","termsuser", "termspassword");
				OWLAccessor oa = new OWLAccessorImpl(new File("C:/Users/Zilong Chang/Documents/WORK/Ontology/pato.owl"));
				for (OWLClass c : oa.getAllClasses()){
					Set<String> kw =oa.getKeywords(c);
					oa.getLabels(c);
					if (!kw.isEmpty()){
						
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
		

	}

}
