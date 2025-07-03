package problemsolving;

public class DeleteAlternateChars {

	// Given a string s as input, delete the characters at odd indices of the string.
	// Return the final string after deleting the characters at odd indices.
	
	public static void main(String[] args) {
		String input = "Hello, how are you?";
		
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < input.length(); i+=2) {
			result.append(input.charAt(i));
		}
		
		System.out.println("Filtered string: " + result.toString());
	}

}
