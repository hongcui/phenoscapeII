/**
 * 
 */
package owlaccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

//import org.semanticweb.owlapi.util.SimpleIRIMapper;
/**
 * @author Zilong Chang
 * 
 */

public class OWLAccessorImpl implements OWLAccessor {

	private OWLOntologyManager manager;
	private OWLDataFactory df;
	private OWLOntology ont;

	public OWLAccessorImpl(String ontoURL) {
		manager = OWLManager.createOWLOntologyManager();
		df = manager.getOWLDataFactory();
		IRI iri = IRI.create(ontoURL);
		try {
			ont = manager.loadOntologyFromOntologyDocument(iri);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OWLAccessorImpl(File file) {
		manager = OWLManager.createOWLOntologyManager();
		df = manager.getOWLDataFactory();

		try {
			ont = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getKeywords(OWLClass c){
		WordFilter wf = new WordFilter();
		 Set<OWLAnnotation> ds= c.getAnnotations(ont, df.getOWLAnnotationProperty(IRI.create("http://purl.obolibrary.org/obo/IAO_0000115")));
		 if(ds.isEmpty()){
			 return new HashSet<String>();
		 }else{
			 Set<String> tresult = new HashSet<String>();
			 OWLAnnotation orgdef = (OWLAnnotation) ds.toArray()[0];
			 String def=this.getRefinedOutput(orgdef.toString()).replaceFirst("^A .* quality inhering in a bearer", "");
			 StringTokenizer st = new StringTokenizer(def);
			 while (st.hasMoreTokens()){
				 String temp = st.nextToken().trim();
				 if (!wf.isInList(temp))				 
					 tresult.add(temp);
			 }
			 return tresult;
		 }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see owlaccessor.OWLAccessor#retrieveConcept(java.lang.String)
	 */
	@Override
	public List<OWLClass> retrieveConcept(String con) {
		con = con.trim();
		List<OWLClass> result = new ArrayList<OWLClass>();
		try {
			for (OWLClass c : ont.getClassesInSignature()) {
				for (OWLAnnotation a : c.getAnnotations(ont)) {
					// match class concepts and also the synonyms
					ArrayList<String> syns = this.getSynonymLabels(c);
					if (a.getProperty().isLabel()) {
						String label = a.getValue().toString();
						boolean syn = matchSyn(con, syns, "p");
						if (label.contains(con) || label.equals(con) || syn) {
							result.add(c);
							if (syn && !label.contains(con)) {
								System.out.println("syn+:" + con);
							}
							break;
						}
					}
				}
			}

			/*
			 * for(OWLClass r:result){
			 * System.out.println("======================================");
			 * this.showClass(r);
			 * 
			 * System.out.println("--------------------------------------");
			 * System.out.println("Ancestors: "); for(OWLClass
			 * an:this.getAncestors(r)){ this.showClass(an); }
			 * 
			 * System.out.println("--------------------------------------");
			 * System.out.println("Parents: "); for(OWLClass
			 * p:this.getParent(r)){ this.showClass(p); }
			 * 
			 * System.out.println("======================================\n\n");
			 * 
			 * }
			 */

		} catch (Exception e) {
			System.out.println(e);

		}
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param syns
	 * @param mode
	 *            : exact="e" or partial="p"
	 * @return
	 */
	private boolean matchSyn(String label, ArrayList<String> synlabels,
			String mode) {
		Iterator<String> it = synlabels.iterator();
		while (it.hasNext()) {
			if (mode.compareTo("p") == 0 && it.next().contains(label)) {
				return true;
			}
			if (mode.compareTo("e") == 0 && it.next().compareTo(label) == 0) {
				return true;
			}
		}
		return false;
	}

	public String getRefinedOutput(String origin) {
		// Annotation(<http://www.geneontology.org/formats/oboInOwl#hasExactSynonym>
		// W)
		if (origin.startsWith("Annotation")) {
			origin = origin.replaceFirst("^Annotation.*>\\s+", "")
					.replaceFirst("\\)\\s*$", "").trim();
		}

		/*
		 * Remove the ^^xsd:string tail from the returned annotation value
		 */
		return origin.replaceAll("\\^\\^xsd:string", "").replaceAll("\"", "").replaceAll("\\.", "");
	}

	@Override
	public List<OWLClass> getAncestors(OWLClass c) {
		List<OWLClass> result = new ArrayList<OWLClass>();
		this.getAncestorsHelper(c, result);
		return result;
	}

	private void getAncestorsHelper(OWLClass c, List<OWLClass> l) {
		for (OWLClassExpression p : c.getSuperClasses(ont)) {
			if (!p.isAnonymous()) {
				l.add(p.asOWLClass());
				this.getAncestorsHelper(p.asOWLClass(), l);
			}
		}
	}

	@Override
	public List<OWLClass> getParent(OWLClass c) {
		List<OWLClass> parent = new ArrayList<OWLClass>();
		for (OWLClassExpression ce : c.getSuperClasses(ont)) {
			if (!ce.isAnonymous())
				parent.add(ce.asOWLClass());
		}
		return parent;
	}

	public Set<OWLAnnotation> getExactSynonyms(OWLClass c) {
		return c.getAnnotations(
				ont,
				df.getOWLAnnotationProperty(IRI
						.create("http://www.geneontology.org/formats/oboInOwl#hasExactSynonym")));
	}

	public Set<OWLAnnotation> getRelatedSynonyms(OWLClass c) {
		return c.getAnnotations(
				ont,
				df.getOWLAnnotationProperty(IRI
						.create("http://www.geneontology.org/formats/oboInOwl#hasRelatedSynonym")));
	}

	public Set<OWLAnnotation> getLabels(OWLClass c) {
		return c.getAnnotations(ont, df
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()));
	}

	@Override
	public String getLabel(OWLClass c) {
		OWLAnnotation label = (OWLAnnotation) this.getLabels(c).toArray()[0];
		return this.getRefinedOutput(label.getValue().toString());
	}
	
	@Override
	public ArrayList<String> getSynonymLabels(OWLClass c) {
		ArrayList<String> labels = new ArrayList<String>();
		Set<OWLAnnotation> anns = getExactSynonyms(c);
		anns.addAll(this.getRelatedSynonyms(c));
		Iterator<OWLAnnotation> it = anns.iterator();
		while (it.hasNext()) {
			// Annotation(<http://www.geneontology.org/formats/oboInOwl#hasExactSynonym>
			// W)
			labels.add(this.getRefinedOutput(it.next().toString()));
		}
		return labels;
	}

	@Override
	public Set<OWLClass> getAllClasses() {
		// TODO Auto-generated method stub
		return ont.getClassesInSignature();
	}
}
