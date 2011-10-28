package owlaccessor;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOWLAccessor2 {

	@Test
	public void testGetKeyWords() {
		OWLAccessor a = new OWLAccessorImpl("http://www.berkeleybop.org/ontologies/pato.owl");
		a.getKeywords(a.retrieveConcept("congested").get(0));//the first match term 
		//(if PATO only contains unique terms, then only one term per list)
		
	}

}
