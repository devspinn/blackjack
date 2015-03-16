
public class Blackjack {

	public static void main(String[] args) {
		new Table();
	}
	
	/*
	 * Helper method to process user input.  Takes a number in the form of a string and returns it as an int.  
	 * Throws an exception of number is not formatted properly.  Do not use this to parse negative numbers.
	 */
	public static int convertStringNumToInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException nfe) {
			return -1;
		}
	}


}
