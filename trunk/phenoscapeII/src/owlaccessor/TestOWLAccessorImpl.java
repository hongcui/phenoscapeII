package owlaccessor;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class TestOWLAccessorImpl {

	@Test
	public void test() {
		OWLAccessor acc = new OWLAccessorImpl();
		acc.retrieveConcept("quality");
	
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		//df=manager.getOWLDataFactory();
		
		//access TAO Ontology on web
		//IRI iri = IRI.create("http://www.berkeleybop.org/ontologies/tao.owl");
		//access PATO Ontology on web
		IRI iri = IRI.create("http://www.berkeleybop.org/ontologies/pato.owl");
		//IRI iri=IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		try {
			OWLOntology ont = manager.loadOntologyFromOntologyDocument(iri);
			assertTrue(ont.containsClassInSignature(IRI.create("http://purl.obolibrary.org/obo/PATO_0000001")));
			Set<OWLEntity> en=ont.getEntitiesInSignature(IRI.create("http://purl.obolibrary.org/obo/PATO_0000001"));
			assertTrue(en.size()==1);
			for(OWLEntity ent:en){
				for(OWLAnnotation a:ent.getAnnotations(ont)){
					if(a.getProperty().isLabel()){
						String label=((OWLAccessorImpl)acc).getRefinedOutput(a.getValue().toString());
						assertTrue(label.contains("quality")||label.equals("quality"));
					}
				}
			}
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
