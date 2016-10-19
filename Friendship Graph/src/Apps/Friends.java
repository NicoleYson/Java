// Nicole Yson and Roshni Chiluka

package Apps;
import java.io.*;
import java.util.*;

public class Friends
{
    static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));   
    static Graph usersGraph = new Graph();
    static ArrayList<Vertex> Users = new ArrayList<Vertex>();
    static ArrayList<Edge> edges = new ArrayList<Edge>();

	public static void main(String[] args) throws IOException
	{
		
        System.out.print("Enter graph file name: ");
        String infile = keyboard.readLine();
        boolean fileFound = false;
        while (!fileFound)
        {
	        try
	        {
		        BufferedReader gFile = new BufferedReader(new FileReader(infile));	
		        buildGraph(gFile);
		        fileFound = true;
	        }
	        catch (FileNotFoundException e) 
	        {
				System.out.println("File not found.");
				System.exit(0);
			}
        }
		while (true)
		{
			System.out.println( "\nOPTIONS AVAILABLE: " +
				  	"\n    1. Students at School" +
					"\n    2. Shortest Intro Chain" + 
					"\n    3. Cliques" +
					"\n    4. Connectors" +
					"\n    5. Quit" + 
					"\nChoose an option: " );
			String choice = keyboard.readLine();
			switch (choice.charAt(0))
			{
			case '1': 
				System.out.println("Enter school: ");
				String school = keyboard.readLine();
				listBySchool(school);
				break;
			case '2':
				System.out.println("Enter the name of person who wants to get introduced:");
				String name1 = keyboard.readLine();
				System.out.println("Enter the person who he wants to get introduced to:");
				String name2 = keyboard.readLine();
				shortestPath(name1, name2);
			    break;
			case '3':
				System.out.println("Enter school:");
				school = keyboard.readLine();
				printCliques(school);
				break;
			case '4':	printConnectors(); break;
			case '5':	return;
			default: System.out.println("Invalid option entered!  Valid options are 1, 2, 3, 4 or 5.");
			}
	    }

    }
	
	/*
	 *  The vertices in the graphs for this assignment represent two kinds of people: students and non-students. 
	 *  Each vertex will store the name of the person. If the person is a student, the name of the school will 
	 *  also be stored.
	 */
    public static void buildGraph(BufferedReader gFile) throws IOException  
    {
        // Load users into usersGraph Vertex
		int totalUsers = Integer.parseInt(gFile.readLine());		// first line is total users
		for(int i = 0; i < totalUsers; i++)
		{
			String line = gFile.readLine();
			usersGraph.addUser(line);								// populate vertices
		}				
		
		String line;
		while((line = gFile.readLine ()) != null)
		{
		    Vertex User = usersGraph.hashTable.get(line.substring(0, line.indexOf('|'))); 		// user
		    Vertex Friend = usersGraph.hashTable.get(line.substring(line.indexOf('|') + 1));	// user's friend
		    usersGraph.addEdge(User, Friend);													// add User's adjacency link
		    usersGraph.addEdge(Friend, User);													// and vise versa
		}
		gFile.close();
		return;
	} 
    
    // Option 1 - Students at school
	static void listBySchool(String school)
	{
		Graph sGraph = Graph.subGraph(usersGraph, school);				// extract subgraph of specified school
		for(int i = 0 ; i < sGraph.vertices.size() ; i++) 	{ System.out.println("  " + sGraph.vertices.get(i).name); }
	}
	
	// Option 2 - Shortest Intro Chain
	static void shortestPath(String name1, String name2)
	{
		try
		{
			usersGraph.shortestIntro(name1);
			Vertex sPath = usersGraph.hashTable.get(name2);
		    Graph.printVertex(sPath, true, "---");
		}
		catch(Exception e)
		{
			System.out.println("No path exist between these users!");
		}
	}
	
	// Option 3 - Cliques at school
	static void printCliques(String school)
	{
		Graph sGraph = Graph.subGraph(usersGraph, school);				// focus on specified school using subgraph
		ArrayList<Graph> graphs = sGraph.getCliques();					
		for (int i = 0; i < graphs.size(); i++) 
		{
			int cliqueCtr = i + 1;
			System.out.println("\nClique " + cliqueCtr + ":");
			for (Vertex v3 : graphs.get(i).vertices) System.out.println("  " + v3.name);
		}
	}
	
	// Option 4 - Print Connectors
	static void printConnectors()
	{
		usersGraph.extractConnectors();
		boolean first = true;
		for(String name4 : usersGraph.connectors) 
		{
			if (!first)  System.out.print(","); else first = false;
			System.out.print(name4);
		}
	}
	 
}