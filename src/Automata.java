import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Martin
 *
 * @param <V>
 */
public class Automata<V> {

	private ArrayList<V> alphabet;
	
	/**
	 * @return
	 */
	public ArrayList<V> getAlphabet() {
		
		return alphabet;
	
	}
	
	private int[][] transitions;
	
	/**
	 * @return
	 */
	public int[][] getTransitions() {
		
		return transitions;
	
	}
	
	private int currentState;
	
	/**
	 * @return
	 */
	public int getCurrentState() {
		
		return currentState;
	
	}
	
	/**
	 * @param currentState
	 */
	public void setCurrentState(int currentState) {
		
		this.currentState = currentState;
		
	}

	private int startState;
	
	/**
	 * @return
	 */
	public int getStartState() {
		
		return startState;
	
	}
	
	/**
	 * @param startState
	 */
	public void setStartState(int startState) {
		
		this.startState = startState;
	
	}
	
	/**
	 * 
	 */
	public void backToStartState() {
		
		currentState = startState;
		
	}
	
	private int states = 1;
	
	/**
	 * @return
	 */
	public int getStates() {
		
		return states;
		
	}
	
	/**
	 * @return
	 */
	public boolean atMaxStates() {
		
		return states == maxStates;
		
	}
	
	/**
	 * @return
	 */
	public int incrementStates() {
		
		if ( states < maxStates) {
			
			states++;
		
		}
		
		return states;
	
	}
	
	private int maxStates;

	/**
	 * @return
	 */
	public int getMaxStates() {

		return maxStates;
	
	}

	/**
	 * @param maxStates
	 * @param startState
	 * @param alphabet
	 */
	public Automata(int maxStates, int startState, ArrayList<V> alphabet) {
		
		this.maxStates = maxStates;
		
		transitions = new int[maxStates+1][alphabet.size()];
		
		this.startState = startState;
		
		currentState = startState;
		
		this.alphabet = alphabet;
		
	}
	
	/**
	 * @return
	 */
	public boolean inAcceptingState() {
		
		return currentState == startState;
		
	}
	
	/**
	 * @param item
	 */
	public void addItemToAlphabet(V item) {
		
		alphabet.add(item);
		
	}
	
	/**
	 * @param sequence
	 * @return
	 */
	public boolean validSequence(ArrayList<V> sequence) {
		
		backToStartState();
		
		for (V item : sequence) {
			
			transist(item);
			
		}
		
		return inAcceptingState();
		
	}

	/**
	 * @param sequence
	 * @return
	 */
	public boolean validSequence(V[] sequence) {
		
		backToStartState();
		
		for (V item : sequence) {
			
			transist(item);
			
		}
		
		return inAcceptingState();
		
	}
	
	/**
	 * @param left
	 * @param top
	 * @param nextState
	 */
	public void transition(int left, int top, int nextState) {
		
		transitions[left][top] = nextState;
		
	}
	
	/**
	 * @param currentState
	 * @param transition
	 * @param nextState
	 */
	public void transition(int currentState, V transition, int nextState) {
		
		transitions[currentState][alphabet.indexOf(transition)] = nextState;
		
	}
	
	/**
	 * @param currentState
	 * @param transition
	 * @return
	 */
	public boolean transitionFilled(int currentState, V transition) {
		
		return transitions[currentState][alphabet.indexOf(transition)] != 0;
		
	}
	
	/**
	 * @param transition
	 */
	public void transist(V transition) {
		
		try {
		
			currentState = transitions[currentState][alphabet.indexOf(transition)];
		
		} catch (Exception e) { }
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		String output = "  ";
		
		for ( V letter : alphabet ) {
			
			if ( !letter.equals("-1") ) {
				
				output += (" " + letter);
				
			}
			
		}
		
		output += "\n";
		
		for (int i = 1; i < transitions.length; i++) {
			
			output += " " + i;
			
			for (int j = 1; j < transitions[0].length; j++) {
				
				//if ( transitions[i][j] != 0 ) {
				
					output += " " + transitions[i][j];
				
				//}
				
			}
			
			output += "\n";
			
		}
		
		return output;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		for (int i = 1; i < transitions.length; i++) {
			
			for (int j = 1; j < transitions[0].length; j++) {
				
				// Lazy!
				try {
					
					if (transitions[i][j] != ((Automata<Integer>)arg0).getTransitions()[i][j]) {
						
						return false;
				
					}
				
				} catch (Exception e) { }
				
			}
			
		}
		
		return true;
		
		//System.out.println(Arrays.deepToString(((Automata<Integer>)arg0).getTransitions()));
		
		//return Arrays.deepEquals(transitions, ((Automata<Integer>)arg0).getTransitions());
	
	}
	
}
