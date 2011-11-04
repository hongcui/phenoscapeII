/**
 * 
 */
package owlaccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * @author Zilong Chang, Hong Cui
 *
 */
public interface OWLAccessor {
	public List<OWLClass> retrieveConcept(String con);
	public List<OWLClass> getParent(OWLClass c);
	public List<OWLClass> getAncestors(OWLClass c);
	public Set<String> getKeywords(OWLClass c);
	public Set<OWLClass> getAllClasses();
	public String getLabel(OWLClass c);
	public ArrayList<String> getSynonymLabels(OWLClass c);

}
