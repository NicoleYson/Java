public class PigLatin
{	
	public static void main (String[] args)
	{
		String Word = IO.readString();
		String beginningLetter = Word.substring(0,1);
		
		if (beginningLetter.equalsIgnoreCase("a"))
			IO.outputStringAnswer(Word+"way");
			else 
				if (beginningLetter.equalsIgnoreCase("e"))
				IO.outputStringAnswer(Word+"way");
				else 
					if (beginningLetter.equalsIgnoreCase("i"))
					IO.outputStringAnswer(Word+"way");
						else 
						if (beginningLetter.equalsIgnoreCase("o"))
						IO.outputStringAnswer(Word+"way");
							else 
							if (beginningLetter.equalsIgnoreCase("u"))
							IO.outputStringAnswer(Word+"way");
		else
		{
			IO.outputStringAnswer(Word.substring(1,(Word.length()))+beginningLetter+"ay");
		}
	}
}