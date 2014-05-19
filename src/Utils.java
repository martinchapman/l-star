/**
 * @author Martin
 *
 */
public class Utils {

	/**
	 * @param str
	 * @return
	 */
	public static String[] split(String str) {
	
		String[] returner = new String[str.length()];
		
		for (int i = 0;i < str.length(); i++) {
			
		    returner[i] = "" + str.charAt(i);
		
		}
		
		return returner;
	
	}

}
