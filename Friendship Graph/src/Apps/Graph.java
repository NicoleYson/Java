package Apps;
import java.util.*;
import java.io.*;

public class Graph
{
	ArrayList<Vertex> vertices;				// array of vertices
	HashMap<String, Vertex> hashTable;		// hashmap tables
	ArrayList<String> connectors;			// list of connectors
	
	public Graph()
	{
		this.vertices = new ArrayList<Vertex>();
		this.hashTable = new HashMap<String, Vertex>();
		this.connectors = new ArrayList<String>();
	}

	// helper to load a user info from source file - 1 line at a time
	public void addUser(String fields)
	{
		Vertex Users = new Vertex(fields);
		this.vertices.add(Users);
		hashTable.put(Users.name, Users);
	}
	
	// add edge
	public void addEdge(Vertex src, Vertex dest) 
	{
		Vertex srcVertex = this.vertices.get(this.vertices.indexOf(src));
		Edge newEdge = new Edge(dest);
		newEdge.next = srcVertex.head;
		srcVertex.head = newEdge;
	}
	
	// helper to add vertex to a temporary vertex (same school)
	public void addVertex(Vertex sVertex)
	{
		this.vertices.add(sVertex);
		hashTable.put(sVertex.name, sVertex);
	}
	
	 /**
	 * Input: Name of school for which to extract a subgraph and the original graph
	 * Output: The subgraphs of a given school. Output should be in the same format as the input described in the project
	 * desctiption. Note that if there is even one student at named school in the graph, there must be at least one 
	 * clique in the output. If the graph has no students at all at that school, then the output will be empty.
	 * 
	 * @param iGraph
	 * @param currSchool
	 * 
	 */
   public static Graph subGraph(Graph iGraph, String currSchool)
   {        
        Graph oGraph = new Graph();               
        for(int i = 0 ; i < iGraph.vertices.size() ; i++)
        {
            if(iGraph.vertices.get(i).student)
            {
                if(iGraph.vertices.get(i).school.equals(currSchool))
                {
            		Vertex sameSchool = new Vertex(iGraph.vertices.get(i)); 
                	oGraph.addVertex(sameSchool);
//                	System.out.println(currSchool + " == " + iGraph.vertices.get(i).name);
                }
            }    
        }             
        for(int i = 0 ; i < oGraph.vertices.size() ; i++)
        {
            if(oGraph.vertices.get(i).student)
            {
                if(oGraph.vertices.get(i).school.equals(currSchool))
                {
                	Vertex edge1 = oGraph.vertices.get(i);             	
                    for(Edge user = iGraph.hashTable.get(edge1.name).head ; user != null ; user = user.next)
                    {                      	
                    	if(user.dest.student)
                    	{
                    		if(user.dest.school.equals(currSchool))
                    		{
                    			Edge edge0 = new Edge(user.dest);
                    			edge0.next = edge1.head;
                    			edge1.head = edge0;
                    		}
//                    		System.out.println("Build Edge:  Name = " + edge1.name + " School = " + edge1.school + " User Dest = " + user.dest + " Edge1.next = " + edge1.head );  
                    	}
                    }                        
                }                    
            }
        }       
    return oGraph;
   }

   //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	   
   //  !!!!!!!!!!!!!!  Shortest Intro Chain HELPERS & METHODS !!!!!!!!!!!!!!!
   //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   /**
   * Method to extract shortest introduction to a student
   * @param name 
   * 
   */
	public void shortestIntro( String name )
	{
		Iterator I = hashTable.values().iterator();
		while(I.hasNext()) 
		{
	         Vertex nGraph = (Vertex)I.next();
	         nGraph.previous = null;
	         nGraph.distance = Double.MAX_VALUE;
//	         System.out.println("ShortestIntro1: Name = " + nGraph.name + " School  = " + nGraph.school + " Distance = " + nGraph.distance);
		}
		
	    Vertex sGraph = hashTable.get(name);      
	    LinkedList<Vertex> sList = new LinkedList<Vertex>();
	    sList.addLast(sGraph); 
	    sGraph.distance = 0;
	
	    while(!sList.isEmpty())
	    {
	        Vertex oGraph = (Vertex) sList.removeFirst();
	        Edge tEdge = oGraph.head;
 //           System.out.println("ShortestIntro2: Name = " + oGraph.name + " School  = " + oGraph.school + " Distance = " + oGraph.distance);
	        while(tEdge!=null)
	        {
	            Edge nEdge = (Edge)tEdge;
	            Vertex fGraph = nEdge.dest;
	            if( fGraph.distance == Double.MAX_VALUE )
	            {
	            	fGraph.distance = oGraph.distance + 1;
	            	fGraph.previous = oGraph;
	                sList.addLast(fGraph);
	            }
	            tEdge = tEdge.next;
//	            System.out.println("ShortestIntro3: Name = " + fGraph.name + " School  = " + fGraph.school + " Distance = " + fGraph.distance);
	        }
	    }
	}

	/**
	* Helper method to accompany shortestIntro.  Uses recursive to print each name
	* @param sPath 
	* @param noPath - indicate if it is just first path (name)
	* @param Separtator - separtor or delimiter that will appear in between names
	*/
	public static void printVertex( Vertex sPath, boolean noPath, String linkSign )
    {
        if( sPath.previous != null )
        {
            printVertex( sPath.previous, false, linkSign );					// RECURSE
            System.out.print( linkSign );
        }
        
        if(sPath.previous == null && noPath) {
        	System.out.print("There is no path to ");
        }
        
        System.out.print( sPath.name );
    } 
	
	//  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	   
	//  !!!!!!!!!!!!!!!  Cliques at School HELPER & METHODS  !!!!!!!!!!!!!!!!!
	//  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	/**
	 * Input: Name of school for which cliques are to be found, e.g. "rutgers"
	 * Result: The subgraphs for each of the cliques.
	 * Output: Print the subgraph for each clique, in the same format as the input 
	 * described in the Graph build section (a clique is a graph in its own right). 
	 * 
	   Students tend to form cliques with their friends, which creates islands 
	   that do not connect with each other. If these cliques could be identified, 
	   particularly in the student population at a particular school, 
	   introductions could be made between people in different cliques to build 
	   larger networks of friendships at that school.
	   
	   GUIDELINES:

		If there is an edge x--y in the graph, then your output must have either 
		x--y or y--x, but NOT both (so your output can be used as input for any 
		other application that might use your subgraph.)
		
		If there is even one student at the named school in the graph, then there 
		must be at least one clique in the output. If the graph has no students 
		at all at that school, then the output will be empty.
		
		In your code, each clique must be created as a separate Graph object: 
		every subgraph of some graph, is a graph in its own right
	 */
	public ArrayList<Graph> getCliques()
	{
		boolean[] visited = new boolean[vertices.size()];
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		int i = 0;
		while ( i < vertices.size() )
		{
			formatCliques(i, visited);
			Graph sGraph = new Graph();			// source graph
			Graph cGraph = new Graph();			// complement graph
			for(int x = 0; x < visited.length; x++) 
			{
				if(visited[x]) 
				{
					sGraph.vertices.add(vertices.get(x));
					sGraph.hashTable.put(vertices.get(x).name, vertices.get(x));
				} else 
				{
					cGraph.vertices.add(vertices.get(x));
					cGraph.hashTable.put(vertices.get(x).name, vertices.get(x));
				}
			}				
			graphs.add(sGraph);
			graphs.addAll(cGraph.getCliques());
			return graphs;
		}
		return graphs;  // just to make code happy
	}

	/**
	* Helper method to accompany getClicques.  Uses recursion to format each name
	* @param edge index
	* @param visited array in boolean
	*/

	private void formatCliques(int edgeIdx, boolean[] visited)
	{			
		if (visited[edgeIdx]) return;
		visited[edgeIdx] = true;		
		for (Edge ptr = vertices.get(edgeIdx).head ; ptr != null ; ptr = ptr.next)
		{
			Vertex destination = this.hashTable.get(ptr.dest.name);
			int nextIdx = this.vertices.indexOf(destination);
			formatCliques(nextIdx, visited);
//			System.out.println("Clique2: " + ptr.dest.name);
		}
	}

	//  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	   
	//  !!!!!!!!!!!!!!!!!!!!  Connectors HELPERS & METHODS  !!!!!!!!!!!!!!!!!!
	//  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	/**
	 * Friend who keep friends together
	 * Input: Nothing
	 * Result: Names of all people who are connectors in the graph
	 * Output: Print names of all people who are connectors in the graph, comma separated, in any order.

	 GUIDELINES:
		If jane were to leave rutgers, sam would no longer be able to connect with anyone else--
		jane was the "connector" who could pull sam in to hang out with her other friends. 
		Similarly, aparna is a connector, since without her, sergei would not be able to "reach" 
		anyone else. (And there are more connectors in the graph...)
		On the other hand, samir is not a connector. Even if he were to leave, everyone else 
		could still "reach" whoever they could when samir was there, even though they may have 
		to go through a longer chain.
	
		Definition: In an undirected graph, vertex v is a connector if there are at least two 
		other vertices x and w for which every path between x and w goes through v.
		For example, v=jane, x=sam, and w=bob.
*/	
	static int dfsIndex = 0;
	
	public void extractConnectors()
	{
	        boolean[] visited = new boolean[vertices.size()];
	               
	        for (int src = 0 ; src < vertices.size() ; src++)
	        {
	        	dfsIndex = 0;
	            searchConnector(src, visited, dfsIndex, vertices.get(src), null);		 // search through every vertex using recursion
	        }
	        
	        return;
	}
	
	private void searchConnector(int src, boolean[] visited, int dfsnum, Vertex User, Vertex w1)
	{
		Vertex x = User;
//		System.out.println("Current User = " + User.name);
        if (visited[src]) return;
        
        visited[src] = true;
        Vertex v = vertices.get(src);
        v.dfsnum = dfsnum;
        v.back = dfsnum;   
//        System.out.println("SearchConnector: " + v.name + " " + v.dfsnum + "/" + v.back);
        for (Edge ptr = vertices.get(src).head ; ptr != null ; ptr = ptr.next)
        {
            Vertex w = vertices.get(vertices.indexOf(ptr.dest)); 
//            System.out.println("v = " + v.name + " w = " + w.name );
            if(visited[vertices.indexOf(ptr.dest)]) 
            {
                v.back = Math.min(v.back, w.dfsnum);
                continue;
	        } 
            else 
            {
            	dfsIndex++;
            	searchConnector(vertices.indexOf(ptr.dest), visited, dfsIndex, User, w);
            }	            
            if(v.dfsnum > w.back)
                v.back = Math.min(v.back, w.back);           
            else
            if(v.dfsnum <= w.back)
            	if(v.equals(x)) x = null;
            	else
            	if(connectors.indexOf(v.name)==-1) 
            		{
            		connectors.add(v.name);				// Found connectors
//                    System.out.println("This is it ===  " + v.name);
            		}
        }
	}

}