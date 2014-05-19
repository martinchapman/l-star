import java.util.ArrayList;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Alphabet should always start with -1 to indicate empty string
		ArrayList<String> alphabet = new ArrayList<String>();		
		alphabet.add("-1");
		alphabet.add("0");
		alphabet.add("1");
		
		Teacher teacher = new Teacher(alphabet);
		Learner learner = new Learner(teacher, alphabet);
		
	}

}
