public class Compress
{
	public static void main(String[] args)
	{

		String iString = IO.readString();							// INPUT string
		String cString = "";										// NEW string
		int j, i = 0;	
		int L = iString.length();									// length of string
		
		while (i<L)
		{
			int repeatCtr = 1;										// always start with 1
			for (j = i+1; j<L; j++)									
			{
				if (iString.charAt(i) == iString.charAt(j))	
				{
					repeatCtr++;									// count it
				}
				else break;											// exit inner loop
			}
			if (repeatCtr == 1)
				cString += iString.charAt(i);						// JUST append the character to current string if no repeat
			else
				cString = cString + repeatCtr + iString.charAt(i);	// concatinate repeat counter and character to current string
			i=j;													// save last index for next while loop
		}
		IO.outputStringAnswer(cString);
	}	
}	
