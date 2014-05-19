import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Martin
 *
 */
public class Learner {

	private Teacher teacher;
	
	private ArrayList<String> alphabet;
	
	private ArrayList<ArrayList<Boolean>> topTable;
	private ArrayList<ArrayList<Boolean>> bottomTable;
	// S
	private ArrayList<String> topOfTableRow;
	// S . A
	private ArrayList<String> bottomOfTableRow;
	// E
	private ArrayList<String> columns;
	
	/**
	 * @param teacher
	 * @param alphabet
	 */
	public Learner(Teacher teacher, ArrayList<String> alphabet) {
		
		this.teacher = teacher;
		
		this.alphabet = alphabet;
		
		topTable = new ArrayList<ArrayList<Boolean>>();
		bottomTable = new ArrayList<ArrayList<Boolean>>();
		
		topOfTableRow = new ArrayList<String>();
		
		bottomOfTableRow = new ArrayList<String>();
		
		columns = new ArrayList<String>();
		
		//
		
		constructInitialTable();
		
		updateTableMembership();
		
		learnAutomata();
		
	}
	
	/**
	 * 
	 */
	private void constructInitialTable() {
	
		columns.add("-1");
		
		topOfTableRow.add("-1");
		
		for ( String letter : alphabet ) {
			
			if (!letter.equals("-1")) {
			
				bottomOfTableRow.add(letter);
			
			}
			
		}
		
	}
	
	/**
	 * 
	 */
	private void updateTableMembership() {
		
		topTable.clear();
		bottomTable.clear();
		
		for ( String rowItem : topOfTableRow ) {
			
			for ( String columnItem : columns) {
				
				addToTopOfTable(rowItem, columnItem, issueMembershipQuery(rowItem, columnItem));
				
			}
			
		}
		
		for ( String rowItem : bottomOfTableRow ) {
			
			for (String columnItem : columns) {
				
				addToBottomOfTable(rowItem, columnItem, issueMembershipQuery(rowItem, columnItem));
				
			}
			
		}
		
	}
	
	/**
	 * @param rowItem
	 * @param columnItem
	 * @return
	 */
	private boolean issueMembershipQuery(String rowItem, String columnItem) {
		
		ArrayList<String> query = new ArrayList<String>();
		
		rowItem = rowItem.replaceAll("-1", "");
		columnItem = columnItem.replaceAll("-1", "");
		
		String[] rowItems = rowItem.split("");
		String[] columnItems = columnItem.split("");
		
		for (String item : rowItems) {
			
			 query.add(item);
			
		}
		
		for (String item: columnItems) {
			
			query.add(item);
			
		}
		
		return teacher.membershipQuery(query);
		
	}
	
	/**
	 * @param rowItem
	 * @param columnItem
	 * @param value
	 */
	private void addToTopOfTable(String rowItem, String columnItem, boolean value) {
		
		int row = topOfTableRow.indexOf(rowItem);
		int column = columns.indexOf(columnItem);
		
		if (row >= topTable.size()) {
			
			ArrayList<Boolean> newColumn = new ArrayList<Boolean>();
			newColumn.add(column, value);
			topTable.add(row, newColumn);
			
		} else {
			
			topTable.get(row).add(column, value);
			
		}
		
	}
	
	/**
	 * @param rowItem
	 * @param columnItem
	 * @param value
	 */
	private void addToBottomOfTable(String rowItem, String columnItem, boolean value) {
		
		int row = bottomOfTableRow.indexOf(rowItem);
		int column = columns.indexOf(columnItem);
		
		if (row >= bottomTable.size()) {
			
			ArrayList<Boolean> newColumn = new ArrayList<Boolean>();
			newColumn.add(column, value);
			bottomTable.add(row, newColumn);
			
		} else {
		
			bottomTable.get(row).add(column, value);
		
		}
		
	}
	
	/**
	 * @param rowItem
	 * @param columnItem
	 * @return
	 */
	private boolean getValue(String rowItem, String columnItem) {
		
		try {
			
			return topTable.get(topOfTableRow.indexOf(rowItem)).get(columns.indexOf(columnItem));
		
		} catch (Exception e) {
			
			return bottomTable.get(bottomOfTableRow.indexOf(rowItem)).get(columns.indexOf(columnItem));
			
		}
		
	}
	
	/**
	 * @return
	 */
	private String checkForTableClosedness() {
		
		for (String bottomState : bottomOfTableRow) {
		
			Boolean found = false;
			
			// Compare to each row in top table
			for (String topState : topOfTableRow) {
				
				if (getStringByLeftState(bottomState, "bottom").equals(getStringByLeftState(topState, "top"))) { found = true; break; }
				
			}
			
			if (!found) return bottomState;
			
		}
		
		return "true";
		
	}
	
	/**
	 * @return
	 */
	private String checkForTableConsistency() {
		
		// For every state (left hand most column, 'S') in the top of the table
		for ( String Key : topOfTableRow ) {
			
			if (Key.equals("-1")) continue;
			
			// Pick another state in the top of the table
			for ( String Value : topOfTableRow ) {
				
				if (Value.equals("-1")) continue;
				
				// If it's not itself
				if (!Key.equals(Value)) {
					
					// See if 'experiments' (remaining rows) match
					if (getStringByLeftState(Key, "top").equals(getStringByLeftState(Value, "top"))) {
						
						// If they do, check that for each of their extensions, the experiments still match
						ArrayList<String> successorsOfKey = getSuccessors(Key);
						ArrayList<String> successorsOfValue = getSuccessors(Value);
						
						for ( String keySuccessor : successorsOfKey ) {
							
							for ( String valueSuccessor : successorsOfValue ) {
								
								if ( 
									 
									 ( !(getStringByLeftState(keySuccessor, "top").equals(getStringByLeftState(valueSuccessor, "top"))) 
									 && !(getStringByLeftState(keySuccessor, "top").equals(getStringByLeftState(valueSuccessor, "bottom"))) )
									 ||
									 ( !(getStringByLeftState(keySuccessor, "bottom").equals(getStringByLeftState(valueSuccessor, "bottom")))
									 && !(getStringByLeftState(keySuccessor, "bottom").equals(getStringByLeftState(valueSuccessor, "top"))) )
									 
								   ) {
									 
									if (keySuccessor.charAt(keySuccessor.length() - 1) == (valueSuccessor.charAt(valueSuccessor.length() - 1))) {
									
										return "" + keySuccessor.charAt(keySuccessor.length() - 1);
									
									} 
									
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return "true";
		
	}
	
	/**
	 * @param state
	 * @param topOrBottom
	 * @return
	 */
	private String getStringByLeftState(String state, String topOrBottom) {
		
		String fullRow = "";
		
		if (topOrBottom.equals("top")) {
			
			int index = topOfTableRow.indexOf(state);
			
			if (index == -1 ) return "";
			
			ArrayList<Boolean> row = topTable.get(index);
			
			for (Boolean rowItem : row ) {
				
				fullRow += rowItem;
				
			}
						
		} else if (topOrBottom.equals("bottom")) {
			
			int index = bottomOfTableRow.indexOf(state);
			
			if (index == -1 ) return "";
			
			ArrayList<Boolean> row = bottomTable.get(index);
			
			for (Boolean rowItem : row ) {
				
				fullRow += rowItem;
				
			}
			
		}
		
		return fullRow;
		
	}

	/**
	 * @param state
	 * @param topOrBottom
	 * @return
	 */
	private ArrayList<Boolean> getRowByLeftState(String state, String topOrBottom) {
		
		ArrayList<Boolean> fullRow = new ArrayList<Boolean>();
		
		if (topOrBottom.equals("top")) {
			
			int index = topOfTableRow.indexOf(state);
			
			if (index == -1 ) return fullRow;
			
			fullRow = topTable.get(index);
			
						
		} else if (topOrBottom.equals("bottom")) {
			
			int index = bottomOfTableRow.indexOf(state);
			
			if (index == -1 ) return fullRow;
			
			fullRow = bottomTable.get(index);
			
		}
		
		return fullRow;
		
	}
	
	/**
	 * 
	 */
	private void updateSuccessors() {
		
		bottomOfTableRow.clear();
		bottomTable.clear();
		
		for ( String state : topOfTableRow ) {
			
			ArrayList<String> successors = getSuccessors(state);
			
			for (String successor : successors) {
				
				if (!topOfTableRow.contains(successor) && !bottomOfTableRow.contains(successor)) {
					
					bottomOfTableRow.add(successor);
					
					for (String columnItem : columns) {
						
						addToBottomOfTable(successor, columnItem, issueMembershipQuery(successor, columnItem));
					
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * @param letter
	 * @return
	 */
	private ArrayList<String> getSuccessors( String letter ) {
		
		ArrayList<String> successors = new ArrayList<String>();
		
		for ( String letterInAlphabet : alphabet ) {
			
			// Given a letter, append each letter from the alphabet to it,
			// except itself
			if ( !letterInAlphabet.equals("-1") ) {
				
				if ( letter.equals("-1") ) {
				
					successors.add(letterInAlphabet);
				
				} else {
					
					successors.add(letter + letterInAlphabet);
					
				}
				
			}
			
		}
		
		return successors;
		
	}
	
	/**
	 * @return
	 */
	private Automata<String> createAutomataFromTable() {
		
		Automata<String> guessedAutomata = new Automata<String>(bottomOfTableRow.get(bottomOfTableRow.size() - 1).length(), 1, alphabet);
		
		for ( String rowState : topOfTableRow ) {
			
			//if (rowState.equals("-1")) continue;
			
			for ( String columnExperiment : columns ) {
				
				guessedAutomata = computeValidAutomata(rowState, columnExperiment, guessedAutomata);
				
			}
			
		}
		
		for ( String rowState : bottomOfTableRow ) {
			
			//if (rowState.equals("-1")) continue;
			
			for ( String columnExperiment : columns ) {
				
				guessedAutomata = computeValidAutomata(rowState, columnExperiment, guessedAutomata);
				
			}
			
		}
		
		// This fills those transitions which have not been filled in by the 'correct'
		// transitions from the table with the last state, as seems to be the pattern.
		// Again, crude, needs formalising.
        int[][] transitions = guessedAutomata.getTransitions();
		
		for ( int i = 0; i < transitions.length; i++ ) {
			
			for ( int j = 1; j < alphabet.size(); j++ ) {
				
				if ( transitions[i][j] == 0) {
					
					guessedAutomata.transition(i, j, guessedAutomata.getMaxStates());
					
				}
				
			}
			
		}
		
		System.out.println("");
		System.out.println(guessedAutomata);
		
		return guessedAutomata;
		
	}
	
	// ~MDC To be improved to be more generalisable
	/**
	 * @param rowState
	 * @param columnExperiment
	 * @param guessedAutomata
	 * @return
	 */
	private Automata<String> computeValidAutomata(String rowState, String columnExperiment, Automata<String> guessedAutomata) {
		
		//if (columnExperiment.equals("-1") && rowState.length() == 1) continue;
		
		String stateSequence = rowState + columnExperiment;
		stateSequence = stateSequence.replaceAll("-1", "");
		
		if ( stateSequence.length() > 0 ) {
			
			String[] individualStates = Utils.split(stateSequence);
		
			// If the table tells us that the sequence acquired by combining the row and column
			// values should 
			if ( getValue(rowState, columnExperiment) == true ) {
				
				// System.out.println("Adding sequence " + stateSequence + " to FSM.");
				
				// If the sequence that SHOULD be accepted by automata (according to the table)
				// is not, then we need to add these transitions:
				if (!guessedAutomata.validSequence(individualStates)) {
					
					// Revert automata back to start state (just incase)
					guessedAutomata.backToStartState();	
					
					// Record first state
					int startState = guessedAutomata.getStartState();
				
					// Find at which point the input string which SHOULD be accepted is no longer
					// accounted for by the automata
					for (int i = 0; i < individualStates.length - 1; i++) {
						
						// Is where I am now, plus the next state from the string in the FSA?
						// If not, we need to break and something needs to be added
						if (!guessedAutomata.transitionFilled(guessedAutomata.getCurrentState(), individualStates[i])) { 
							
							//System.out.println("Transition not filled " + guessedAutomata.getCurrentState() + " --> " + individualStates[i]);
							
							// ~MDC Needs work - how to determine which state to link to next: A new one? An existing one?
							// This is very crude, and very unlikely to be generalisable to a number of FSA.
							
							// If we have additional states to go to, go to them
							if (!guessedAutomata.atMaxStates()) {
							
								guessedAutomata.transition(guessedAutomata.getCurrentState(), individualStates[i], guessedAutomata.incrementStates());
							
							// If we have used up our alloted states, means we must go back. 
							} else {
								
								//System.out.println("Cannot create another state, so must go back.");
								
								int currentState = guessedAutomata.getCurrentState();
								
								// Try various paths to return to accepting state
								
								// Try all possible next states (bar starting state which is 1)
								// Backwards, why not.
								for (int j = guessedAutomata.getMaxStates(); j > 1; j--) {
									
									// Transition to this state
									guessedAutomata.setCurrentState(j);
									
									// Then try the remainder of the sequence to see if we return to the start
									// ~MDC Heavy assumption that the remainder of the path is filled!
									for (int k = i + 1; k < individualStates.length; k++) {
										
										guessedAutomata.transist(individualStates[k]);
										
									}
									
									// If we're back to an accepting state...
									if (guessedAutomata.inAcceptingState()) {
										
										//...add the initial step to the automata
										guessedAutomata.transition(currentState, individualStates[i], j);
										break;
									}
									
								}
								
								guessedAutomata.setCurrentState(currentState);
								
							}
							
						}
						
						// If it is, advance our current state by advancing the automata
						guessedAutomata.transist(individualStates[i]);
						
					}
					
					// Whatever happens, the LAST state in our string must take us back to the 
					// start state, as it is valid
					guessedAutomata.transition(guessedAutomata.getCurrentState(), individualStates[individualStates.length - 1], startState);
					
					//System.out.println(guessedAutomata);
					
				} else {
					
					//System.out.println("Sequence " + stateSequence + " already accounted for by FSM.");
					
				}
				
			} 
			
		}
		
		return guessedAutomata;
		
	}
	
	
	/**
	 * 
	 */
	private void learnAutomata() {
		
		String counterexample = "";
		Automata<String> guessedAutomata = null;
		
		// While the guessed automata we have does not equal the
		// 'secret' (to be learned) automata held by the teacher
		while (!counterexample.equals("Confirm")) {
			
			printTable();
			
			String closed = checkForTableClosedness();
			
			while (closed != "true") {
				
				System.out.println("Table is not closed.");
				
				// If the table is not closed, find the thing that is causing it not to be,
				// and move it to the top of the table
				topTable.add(getRowByLeftState(closed, "bottom"));
				bottomTable.remove(bottomOfTableRow.indexOf(closed));
				topOfTableRow.add(bottomOfTableRow.remove(bottomOfTableRow.indexOf(closed)));
				
				// Add successors as necessary
				updateSuccessors();
				
				closed = checkForTableClosedness();
				
				printTable();
			
			}
			
			System.out.println("Table is closed.");
			
			String consistent = checkForTableConsistency();
			
			while (consistent != "true") {
				
				System.out.println("Table is not consistent.");
				
				columns.add(consistent);
				
				updateTableMembership();
				
				consistent = checkForTableConsistency();
				
				printTable();
				
			}
			
			System.out.println("Table is consistent.\n");

			System.out.println("Converting table to FSA.");
			
			guessedAutomata = createAutomataFromTable();
			
			counterexample = teacher.checkConjecture(guessedAutomata);
			
			if (counterexample.equals("Confirm")) break;
			
			System.out.println("Incorporating " + counterexample + " and prefixes in top of table. \n");
			
			for (int i = 1; i <= counterexample.length(); i++) {
				
				String prefix = counterexample.substring(0, i);
				
				if (!topOfTableRow.contains(prefix)) {
					
					topOfTableRow.add(prefix);
					
				}
				
			}
			
			System.out.println("Adding counterexamples successors. \n");
			
			updateSuccessors();
			
			System.out.println("Updating membership queries. \n");
			
			updateTableMembership();
			
		}
		
		System.out.println("Automata Learnt.\n");
		
		System.out.println(guessedAutomata);
		
		printTable();
		
	}
	
	/**
	 * 
	 */
	private void printTable() {
		
		System.out.println("");
		
		int row = 0;
		
		for (String columnHead : columns) {
			
			System.out.print("     " + columnHead + " ");
			
		}
		
		System.out.println("\n------------------");
		
		for (ArrayList<Boolean> tableRow : topTable) {
			
			System.out.print(topOfTableRow.get(row));
			
			row++;
			
			for ( Boolean validMember : tableRow ) {
				
				System.out.print("  " + validMember);
				
			}
			
			System.out.println();
			
		}
		
		System.out.println("------------------");
		
		row = 0;
		
		for (ArrayList<Boolean> tableRow : bottomTable) {
			
			System.out.print(bottomOfTableRow.get(row));
			
			row++;
			
			for ( Boolean validMember : tableRow ) {
				
				System.out.print("  " + validMember);
				
			}
			
			System.out.println("");
			
		}
		
		System.out.println("");
		
	}
	
}
