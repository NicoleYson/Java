package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences areF
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		HashMap<String,Occurrence> keywords = new HashMap<String,Occurrence>();	
//		System.out.println("LoadKeyWords 1" + docFile);
		Scanner sc = new Scanner(new File(docFile));
		int wordFreq = 1;		
		while ( sc.hasNext() )
		{
			String kw = getKeyWord(sc.next()); 
			if ( kw != null )
			{	
				if ( keywords.containsKey(kw) )
				{
					keywords.get(kw).frequency++;				// increment keyword frequency field 
				}
				else
				{
					Occurrence O = new Occurrence(docFile,wordFreq);
					keywords.put(kw, O);						// new keyword
				}
			}
		}
		if(sc!=null) sc.close();
//		System.out.println("LoadKeyWords 2" + keywords);		
		return keywords;
	}	
	
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		
		ArrayList<Occurrence> tempList = new ArrayList<Occurrence>();	
		for( String kw: kws.keySet() )
		{	
			Occurrence O = kws.get(kw);							// set occurrence of a keyword from a document
//			System.out.println ("MergeKey:  O = " + O + " kw = " + kw);
			if ( keywordsIndex.containsKey(kw) )
			{	// If already in the master keywords index, make sure insert appropriately maintaining descending sort order
				tempList = keywordsIndex.get(kw);
//				System.out.println ("MergeKey:  tempList1 = " + tempList);
				tempList.add(O);								// add occurrence O to tempList
				insertLastOccurrence(tempList);					// make sure it is still in descending order
//				System.out.println ("MergeKey:  tempList2 = " + tempList);
				keywordsIndex.put(kw, tempList);				// update a keyword in KWI table with docs/occurrences
//				System.out.println ("MergeKey:  KWI = " + keywordsIndex);
			}
			else
			{	// Doesn't exist yet in the master keywords index
				ArrayList<Occurrence> occurList = new ArrayList<Occurrence>();				
				occurList.add(O);
				keywordsIndex.put(kw, occurList);
			}	
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE

		String Word = word.trim().replaceAll("([a-z]+)[.,?:;!]*", "$1");		// Trim trailing punctuations
//		System.out.println("GetKeyWord2: " + word + " = " + Word);	
		Word = Word.toLowerCase();												// Convert to lowercase
		// Ignore noise word
		for(String noiseWord: noiseWords.keySet())
		{
			if ( Word.equalsIgnoreCase(noiseWord) )
			{
				return null;
			}
		}
		// Ignore words with special characters in the middle
		for(int j = 0; j < Word.length(); j++)
		{
			if ( !Character.isLetter(word.charAt(j)) )
			{
				return null;
			}
		}
//		System.out.println("GetKeyWord: " + word + " = " + Word);
		return Word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		if ( occs.size() == 1 ) return null;
		// REFERENCE http://www.programmingsimplified.com/java/source-code/java-program-for-binary-search
		Occurrence tOccs = occs.get(occs.size() -1);
		int lastFreq = occs.get(occs.size()-1).frequency;
		int last = occs.size() - 1;
		int first = 0;
		int middle;
		ArrayList<Integer> midPointIdx = new ArrayList<Integer>();
		
        while( first <= last )
        {
            middle = ( first + last ) / 2;
            midPointIdx.add(middle);

            if ( lastFreq > occs.get(middle).frequency )
            {            	
                last = middle - 1;
            }
            else if ( lastFreq < occs.get(middle).frequency )
            {
            	first = middle + 1;
            }
            else
            {
            	break;
            }
        }       
        if ( midPointIdx.get( midPointIdx.size() - 1) == 0 )
        {
        	if ( tOccs.frequency < occs.get(0).frequency )
        	{
        		occs.add(1, tOccs);
        		occs.remove(occs.size() - 1);     		
        		return midPointIdx;
        	}
        }        
        occs.add( midPointIdx.get( midPointIdx.size() - 1 ), tOccs );
        occs.remove( occs.size() - 1 );
//		System.out.println("Mid Point Index : " + midPointIdx);
		return midPointIdx;		
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		ArrayList<String> top5list = new ArrayList<String>();
		ArrayList<Occurrence> List1 = new ArrayList<Occurrence>();		
		kw1 = kw1.toLowerCase();	
		if ( keywordsIndex.get(kw1) != null )
		{
			List1 = keywordsIndex.get(kw1);
		}	
		ArrayList<Occurrence> List2 = new ArrayList<Occurrence>();		
		kw2 = kw2.toLowerCase();
		if ( keywordsIndex.get(kw2) != null )
		{
			List2 = keywordsIndex.get(kw2);
		}

// Start troubleshooting
//    	System.out.println(kw1 + ": " + List1);
//		System.out.println(kw2 + ": " + List2);
// End  troubleshooting		
 	
		for(int p = 0; p < List1.size(); p++)
		{
			if ( top5list.size() < 5 )
			{
				int I1 = List1.get(p).frequency;
				String doc1 = List1.get(p).document;			
				for(int w = 0; w < List2.size(); w++)
				{
					String doc2 = List2.get(w).document;
					int I2 = List2.get(w).frequency;					
					if ( I2 > I1 )
					{
						if ( !top5list.contains(doc2) && top5list.size() < 5)
						{
							top5list.add(doc2);
						}
					}
					else
					{
						if ( !top5list.contains(doc1) && top5list.size() < 5 )
						{
							top5list.add(doc1);
						}
					}
				}
			}
		}
//		System.out.println("Top 5: " + top5list);
		if (top5list.size() == 0) return null; 	
		return top5list;
	}
}
