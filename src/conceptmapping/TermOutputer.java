/**
 * 
 */
package conceptmapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Hong Updates
 * This class output extracted terms and their mapping PATO concepts to a table, including source info
 *
 */
public class TermOutputer {
	private Connection conn;
	private String username = "phenoscape";
	private String password = "pheno!scape";
	private String entitytable = "entity";
	private String qualitytable = "quality";
	
	/**
	 * 
	 */
	public TermOutputer(String database, String tableprefix) {
		this.entitytable = tableprefix+"_"+entitytable;
		this.qualitytable = tableprefix+"_"+qualitytable;
		try{
			if(conn == null){
				Class.forName("com.mysql.jdbc.Driver");
				String URL = "jdbc:mysql://localhost/"+database+"?user="+username+"&password="+password;
				conn = DriverManager.getConnection(URL);
				Statement stmt = conn.createStatement();
				stmt.execute("drop table if exists "+ entitytable);
				stmt.execute("create table if not exists "+entitytable+" (id int(11) not null unique auto_increment primary key, term varchar(100), ontoid varchar(20), source varchar(500), sentence text");
				stmt.execute("drop table if exists "+qualitytable);
				stmt.execute("create table if not exists "+tableprefix+"_"+qualitytable+" (id int(11) not null unique auto_increment primary key, term varchar(100), ontoid varchar(20), source varchar(500), sentence text");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void output(){
		ArrayList<String> entities =getEterms();
		ArrayList<String> qualities = getQterms();
		outputTerms(entities, entitytable);
		outputTerms(qualities, qualitytable);
	}
	
	
	private void outputTerms(ArrayList<String> entities, String outtable) {
		Iterator<String> it = entities.iterator();
		String outtableo = outtable;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			while(it.hasNext()){
				String term = it.next();
				outtable = outtableo;
				String[] ontoidinfo = findID(term);
				String ontoid = "";
				if(ontoidinfo !=null){
					outtable = ontoidinfo[0];
					ontoid = ontoidinfo[1];
				}
				rs = stmt.executeQuery("select pdf, charnumber, sentence from fish_original where sentence rlike '[^a-z]"+term+"[^a-z]'" );
				StringBuffer source = new StringBuffer();
				String sentence = "";
				while(rs.next()){
					source.append(rs.getString("pdf")+":"+rs.getString("charnumber")+";");
					sentence = rs.getString("sentence");
					addrecord(term, ontoid, source, sentence, outtable);
				}
			}
			rs.close();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void addrecord(String term, String ontoid, StringBuffer source,
			String sentence, String outtable) {
		try{
			Statement stmt = conn.createStatement();
			stmt.execute("insert into "+outtable+"(term, ontoid, source, sentence) values ('"+term+"','"+ontoid+"','"+source+"','"+sentence+"')");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String[] findID(String term) {
		String qualityid = "";
		String entityid = "";
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select qualityontoid from fish_orginal where qualitylabel='"+term+"')");
			if(rs.next()){
				qualityid = rs.getString("qualityontoid");				
			}
			rs = stmt.executeQuery("select entityontoid from fish_orginal where entitylabel='"+term+"')");
			if(rs.next()){
				entityid = rs.getString("entityontoid");				
			}
			if((entityid+qualityid).trim().length()==0){
				return null;
			}else if(entityid.length()>0){
				return new String[]{this.entitytable, entityid};
			}else if(qualityid.length()>0){
				return new String[]{this.qualitytable, qualityid};
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<String> getQterms() {
		ArrayList<String> qterms = new ArrayList<String>();
		try{
			String q = "SELECT distinct word FROM pheno_fish_allwords where "+
			"word in (select term from antglossaryfixed where category !='structure') or "+
			"word in (select word from pheno_fish_wordroles p where semanticrole ='c')";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while(rs.next()){
				qterms.add(rs.getString("word"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return qterms;
	}

	private ArrayList<String> getEterms() {
		ArrayList<String> eterms = new ArrayList<String>();
		try{
			String q = "SELECT distinct word FROM pheno_fish_allwords where "+
			"word in (select term from antglossaryfixed where category ='structure') or "+
			"word in (select word from pheno_fish_wordroles p where semanticrole in ('os', 'op'))";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			while(rs.next()){
				eterms.add(rs.getString("word"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TermOutputer to = new TermOutputer("phonescape", "phone_fish");
		to.output();
		
	}

}
