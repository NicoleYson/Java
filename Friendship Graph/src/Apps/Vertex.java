package Apps;

import java.util.*;

public class Vertex 
{

    String name;				// Person's name  
    boolean student;			// true if student, false if not
    String school;				// School Name   
    Edge head;    
    int dfsnum;
    int back;    
    double distance;  
    Vertex previous; 

    // This will be referred to when input is a vertex
    public Vertex(Vertex old)
    {
    	this.name = old.name;
        this.student = old.student;
        this.school = old.school;
        this.distance = old.distance;
        this.head = null;
    }
   
    // This will be referred to when building/loading vertices.  Input is string
    public Vertex(String newInfo)
    {       
        String newname = "";
        String newstudent = "";
        String newschool = "";
        StringTokenizer st = new StringTokenizer(newInfo, "|");        
        newname = st.nextToken();
        newstudent = st.nextToken();
        if(st.hasMoreTokens())
        {
            newschool = st.nextToken();
        }
        this.name = newname;
        this.student = false;
        this.school = null;
        this.distance = Double.MAX_VALUE;
        if(newstudent.equals("y"))
        {
            this.student = true;
            this.school = newschool;
        }           
    }
}