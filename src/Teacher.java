import java.util.ArrayList;


/**
 * @author Martin
 *
 */
public class Teacher {

	Automata<String> automata;
	ArrayList<String> alphabet;
	
	/**
	 * @param alphabet
	 */
	public Teacher(ArrayList<String> alphabet) {
		
		this.alphabet = alphabet;
		setupAutomata();
		
	}
	
	/**
	 * @param sequence
	 * @return
	 */
	public boolean membershipQuery(ArrayList<String> sequence) {
		
		automata.backToStartState();
		return automata.validSequence(sequence);
		
	}
	
	/**
	 * @param automata
	 * @return
	 */
	public String checkConjecture(Automata<String> automata) {
		
		if (!this.automata.equals(automata)) {
			
			String[] charAlphabet = new String[alphabet.size() - 1];
			
			for (int i = 1; i < alphabet.size(); i++) {
				
				charAlphabet[i-1] = alphabet.get(i);
				
			}
	        
	        for (int j = 1; j < 5; j++) {
	        	
	        	getCombinations(j, charAlphabet,"");
	        	
	        }
	        
	        for (String combination : combinations) {
	        	
	        	// String is accepted by CORRECT automata, but not by submitted one
	        	if (this.automata.validSequence(combination.split("")) && !automata.validSequence(combination.split(""))) {
				
	        		System.out.println("Teacher: counterexample to submitted FSA: " + combination + "\n");
	        		return combination;
	        		
	        	}
	        	
	        	// String is accepted by SUBMITTED automata, but not by correct one
	        	if (automata.validSequence(combination.split("")) && !this.automata.validSequence(combination.split(""))) {
				
	        		System.out.println("Teacher: counterexample to submitted FSA: " + combination + "\n");
	        		return combination;
	        		
	        	}
	        
	        }
	        
	        return "Error";
	        
		} else {
			
			return "Confirm";
			
		}
		
	}
	
	ArrayList<String> combinations = new ArrayList<String>();
	
	/**
	 * @param maxLength
	 * @param alphabet
	 * @param curr
	 */
	private void getCombinations(int maxLength, String[] alphabet, String curr) {
		
        if( curr.length() == maxLength ) {
        	
            combinations.add(curr);
        	
        } else {
        	
            for(int i = 0; i < alphabet.length; i++) {
            	
                String newCurr = curr + alphabet[i];
            
                getCombinations(maxLength, alphabet, newCurr);
            
            }
            
        }
        
    }
	
	/**
	 * 
	 */
	private void setupAutomata() {
		
		automata = new Automata<String>(4, 1, alphabet);
		
		automata.transition(1, "0", 2);
		automata.transition(1, "1", 3);
		automata.transition(2, "0", 1);
		automata.transition(2, "1", 4);
		automata.transition(3, "1", 1);
		automata.transition(3, "0", 4);
		automata.transition(4, "0", 3);
		automata.transition(4, "1", 2);
		
		System.out.println("Trying to learn the following automata:\n");
		
		System.out.println(automata);
		
	}
	
}
