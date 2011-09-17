/**
 * 
 */
package owlaccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	
	public OWLAccessorImpl(){
		manager=OWLManager.createOWLOntologyManager();
		df=manager.getOWLDataFactory();
		
		//access TAO Ontology on web
		//IRI iri = IRI.create("http://www.berkeleybop.org/ontologies/tao.owl");
		//access PATO Ontology on web
		IRI iri = IRI.create("http://www.berkeleybop.org/ontologies/pato.owl");
		//IRI iri=IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		try {
			ont = manager.loadOntologyFromOntologyDocument(iri);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(ont);
	}
	
	/* (non-Javadoc)
	 * @see owlaccessor.OWLAccessor#retrieveConcept(java.lang.String)
	 */
	@Override
	public void retrieveConcept(String con) {
		try{
					
			List<OWLClass> result=new ArrayList<OWLClass>();
			
			for(OWLClass c:ont.getClassesInSignature()){
				for(OWLAnnotation a:c.getAnnotations(ont)){
					if(a.getProperty().isLabel()){
						String label=a.getValue().toString();
						if(label.contains(con)||label.equals(con)){
							result.add(c);
							break;
						}
					}
				}
			}
			
			for(OWLClass r:result){
				System.out.println("======================================");
				this.showClass(r);
				
				System.out.println("--------------------------------------");
				System.out.println("Ancestors: ");
				for(OWLClass an:this.getAncestors(r)){
					this.showClass(an);
				}				
				
				System.out.println("--------------------------------------");
				System.out.println("Parents: ");
				for(OWLClass p:this.getParent(r)){
					this.showClass(p);
				}	
								
				System.out.println("======================================\n\n");
				
			}
			
		}catch(Exception e){
			System.out.println(e);
			
		}
	}

	public String getRefinedOutput(String origin) {
		/*
		 *Remove the ^^xsd:string tail from the returned annotation value
		 */
		return origin.replaceAll("\\^\\^xsd:string", "");
	}

	@Override
	public void showClass(OWLClass c) {
		System.out.println("URL: \n"+c.toString());
		
		System.out.println();
		
		System.out.println("Labels: ");
		for(OWLAnnotation l:this.getLabels(c)){
			System.out.println(this.getRefinedOutput(l.getValue().toString()));
		}
		
		System.out.println();
		
		System.out.println("Synonyms: ");
		for(OWLAnnotation s:this.getExactSynonyms(c)){
			System.out.println(this.getRefinedOutput(s.getValue().toString()));
		}
		
		System.out.println("++++++++");
		
	}

	@Override
	public void showSuperClass(OWLClass c,int d) {
//		System.out.println("SuperClass of Depth "+d+":");
//		for(OWLClassExpression p:c.getSuperClasses(o)){
//			this.showClass(p.asOWLClass(), o);
//		}
	}

	@Override
	public List<OWLClass> getAncestors(OWLClass c) {
		List<OWLClass> result=new ArrayList<OWLClass>();
		this.getAncestorsHelper(c, result);
		return result;
	}
	
	private void getAncestorsHelper(OWLClass c, List<OWLClass> l){
		for (OWLClassExpression p:c.getSuperClasses(ont)){
			if(!p.isAnonymous()){
				l.add(p.asOWLClass());
				this.getAncestorsHelper(p.asOWLClass(), l);
			}
		}		
	}

	@Override
	public List<OWLClass> getParent(OWLClass c) {
		List<OWLClass> parent=new ArrayList<OWLClass>();
		for (OWLClassExpression ce:c.getSuperClasses(ont)){
			if(!ce.isAnonymous())
				parent.add(ce.asOWLClass());
		}
		return parent;
	}

	@Override
	public Set<OWLAnnotation> getExactSynonyms(OWLClass c) {
		return c.getAnnotations(ont, df.getOWLAnnotationProperty(IRI.create("http://www.geneontology.org/formats/oboInOwl#hasExactSynonym")));
	}

	@Override
	public Set<OWLAnnotation> getLabels(OWLClass c) {
		
		return c.getAnnotations(ont, df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()));
	}

}
