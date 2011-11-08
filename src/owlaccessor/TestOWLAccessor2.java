package owlaccessor;

import org.junit.Test;

public class TestOWLAccessor2 {

	@Test
	public void testGetKeyWords() {
		OWLAccessor a = new OWLAccessorImpl("http://www.berkeleybop.org/ontologies/pato.owl");
		for (String k :a.getKeywords(a.retrieveConcept("radiation absorbed dose").get(0))){
			//the first match term 
			//(if PATO only contains unique terms, then only one term per list)
			System.out.println(k);
			
		}
		
		System.out.println(a.getAllClasses().size());
	}

}