public class WordCount
{
	public static void main(String[] args)
	{
		int wordCtr = 0;
		System.out.print("Enter sentence: "); 				
		String Sentence = IO.readString();
		System.out.print("Enter minimum word length: "); 	
		int minLetters = IO.readInt();
		
		String[] Words = Sentence.split(" ");				// Breakdown sentence into arrays of words
		for (int i=0; i<Words.length; i++)
		{
			if (letterCount(Words[i]) >= minLetters && Words[i].length() > 0 )
			{
				wordCtr++;
			}
		}
		IO.outputIntAnswer(wordCtr);
	}	
	// Count letters only - special characters, numbers, and spaces ignored
	private static int letterCount(String S) 
	{
		int letterCtr = 0;
		for (int i=0; i<S.length(); i++)
			if ((S.charAt(i) >='a' && S.charAt(i) <='z') || (S.charAt(i) >='A' && S.charAt(i) <='Z'))
				letterCtr++;
		return letterCtr;
	}
}	
