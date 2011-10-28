package conceptmapping;

import java.util.TreeMap;

import org.junit.Test;

import edu.sussex.nlp.jws.*;

import rita.wordnet.RiWordnet;

public class TestMapToPATO {

	@Test
	public void test() {
		RiWordnet rwn=new RiWordnet(null, "C:\\Program Files\\WordNet\\2.1\\");
		String[] pos=rwn.getPos("cernuous");
		for (int i=0; i<pos.length; i++){
			System.out.println(pos[i]);
		}
		
		String[] syn=rwn.getSynonyms("cernuous", "a");
		for(int i=0; i<syn.length; i++){
			System.out.println(syn[i]);
		}
		
		//--------------------------
		JWS jws=new JWS("C:\\Program Files\\WordNet","2.1");
		JiangAndConrath jcn =jws.getJiangAndConrath();
		
		
// 2. EXAMPLES OF USE:
// 2.1 [JIANG & CONRATH MEASURE]		
		// all senses
				TreeMap<String, Double> 	scores1	=	jcn.jcn("cernuous", "unerect", "a");			// all senses
				//TreeMap<String, Double> 	scores1	=	jcn.jcn("apple", 1, "banana", "n"); 	// fixed;all
				//TreeMap<String, Double> 	scores1	=	jcn.jcn("apple", "banana", 2, "n"); 	// all;fixed
				for(String s : scores1.keySet())
					System.out.println(s + "\t" + scores1.get(s));
		// specific senses
				System.out.println("\nspecific pair\t=\t" + jcn.jcn("apple", 1, "banana", 1, "n") + "\n");
		// max.
				System.out.println("\nhighest score\t=\t" + jcn.max("apple", "banana", "n") + "\n\n\n");
				
// 2.2 [LIN MEASURE]				
				Lin lin = jws.getLin();
				System.out.println("Lin\n");
		// all senses
				TreeMap<String, Double> 	scores2	=	lin.lin("apple", "banana", "n");			// all senses
				//TreeMap<String, Double> 	scores2	=	lin.lin("apple", 1, "banana", "n"); 	// fixed;all
				//TreeMap<String, Double> 	scores2	=	lin.lin("apple", "banana", 2, "n"); 	// all;fixed
				for(String s : scores2.keySet())
					System.out.println(s + "\t" + scores2.get(s));
		// specific senses
				System.out.println("\nspecific pair\t=\t" + lin.lin("apple", 1, "banana", 1, "n") + "\n");
		// max.
				System.out.println("\nhighest score\t=\t" + lin.max("apple", "banana", "n") + "\n\n\n");
	
	}

}
