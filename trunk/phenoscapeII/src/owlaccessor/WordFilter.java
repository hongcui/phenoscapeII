package owlaccessor;

import java.util.ArrayList;
import java.util.List;

public class WordFilter {
	private List<String> wl;
	public WordFilter(){
		wl = new ArrayList<String>();
		wl.add("this");
		wl.add("that");
		wl.add("of");
		wl.add("is");
		wl.add("are");
		wl.add("with");
		wl.add("or");
		wl.add("some");
		wl.add("kind");
		wl.add("it");
		wl.add("on");
		wl.add("in");
		wl.add("for");
		wl.add("without");
		wl.add("to");
		wl.add("bearer");
		wl.add("A");
		wl.add("a");
		wl.add("the");
		wl.add("being");
		wl.add("into");
		wl.add("by");
		wl.add("inhering");
		wl.add("virtue");
		wl.add("pattern");
		wl.add("bearer's");
//		wl.add("");
//		wl.add("on");
//		wl.add("in");
//		wl.add("for");
//		wl.add("without");
	}
	
	public boolean isInList(String s){
		if(wl.contains(s.trim().toLowerCase()))
			return true;
		else
			return false;
	}
}
