public class StringRec
{
	// DO NOT DECLARE ANY VARIABLES HERE
	// (you may declare local variables inside methods)

//	public static void main(String[] args) 	// TEST 
//	{
//		String compressedText = "q9w5e2rt5y4qw2Er3T";
//		String compressedText = "q";
//		String compressedText = "3*3!2q21R3!3*";
//		System.out.println(decompress(compressedText));
//	}

	public static String decompress(String compressedText) 
	{
//	System.out.println(compressedText);
		if (compressedText.length() == 0) return "";
		return Decompress(compressedText, "", "");
	}

	public static String Decompress(String text, String repeat, String output) 
	{
//	System.out.println("Decompress = " + text + " " + repeat + " " + output);
		if (text.equals("")) 					// no more test to parse
		{ 
			return output;
		}
		if(Character.isDigit(text.charAt(0))) 	// process digit, keep it string to be appended if more than 1 digit
		{ 
			repeat += text.charAt(0);
		}
		else  									// process non-digit character
		{ 
			if (repeat == "") repeat = "1";		// if single char, "r" should be 1
//	System.out.println("Decompress = " + text.charAt(0) + " " + repeat + " " + output);
			output += processRepeat(text.charAt(0),Integer.parseInt(repeat));	// append one char at a time 
			repeat = "";
		}
		return Decompress(text.substring(1), repeat, output);	//parse the rest of the string one character at a time
	}

	public static String processRepeat(char C, int r) 			// Recurse here "r" times
	{
//	System.out.println("processRepeat = " + C + " " + r);
		if (r == 0) return "";									// no more characters to repeat, return repeated characters
		return "" + C + processRepeat(C, r-1);					// append one char at a time
	}
}
