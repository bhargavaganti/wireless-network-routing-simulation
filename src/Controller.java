

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Controller
{
	public static int duration;
	//public static String[] lastRead = new String[10];
	public static int[] lastCount = new int[10];
	public static HashMap<String, ArrayList<String>> adjacency = new HashMap<String, ArrayList<String>>();

	public static void main(String args[])
	{
		// User will let the node know its nodeID
		// controller duration &
		if (args.length > 0)
		{
			try
			{
				duration = 1000*Integer.parseInt(args[0]);
				//System.out.println("duration:"+duration);
		    }
			catch (Exception e)
			{
				System.err.println("Argument must be in proper format");
				System.exit(1);
		    }
		}
		readControllerConfig();
		
		try
		{
			//Thread.sleep(5000);
			// Infinite loop
			long startTime = System.currentTimeMillis(); //fetch starting time
			while(System.currentTimeMillis()-startTime < duration)
			{
				// Read from all output files sequentially
				for(int i=0;i<10;i++)
				{
					readFile(i);
				}
				//System.out.println(System.currentTimeMillis()-startTime);
			    try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.exit(0);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		//System.exit(0);
		System.out.println("controller stopped");
	}
	
	 // Reading File
    static void readFile(int nodeID)
    {
    	try
    	{
    		Boolean readAllow = false;
    		String str = "output_"+nodeID+".txt";
    		BufferedReader ReadFile = new BufferedReader(new FileReader(str));
    		int temp = 0;
    		//System.out.println("Reading node:"+nodeID+" Last Read line:"+lastCount[nodeID]);
    		while((str = ReadFile.readLine()) != null)
    		{
    			String[] tokens = str.split(" ");
    			// First message
    			
    			++temp;
    			if(temp > lastCount[nodeID])
    			{
    				//System.out.println("Satisfied: temp:"+temp+" lastcount:"+lastCount[nodeID]);
    				if(tokens[0].equals("data") || tokens[0].equals("delete") || tokens[0].equals("hello") || tokens[0].equals("intree"))
    				{
    					// Find Neighbours
    					// data A E C B begin message
    					ArrayList<String> neighbours = new ArrayList<String>();
    					neighbours = adjacency.get(String.valueOf(nodeID));
    					Iterator<String> it = neighbours.iterator();
    					while(it.hasNext()){
    						writeFile(str, it.next());
    					}
    				}
    			}
    			
/*    			if(lastRead[nodeID] == null || readAllow)
    			{
    				lastRead[nodeID] = str;
    				if(tokens[0].equals("hello"))
    				{
    					// Find Neighbours
    					ArrayList<String> neighbours = new ArrayList<String>();
    					neighbours = adjacency.get(tokens[1]);
    					Iterator<String> it = neighbours.iterator();
    					while(it.hasNext()){
    						writeFile(str, it.next());
    					}
    				}
    			}
    			else if(lastRead[nodeID].equals(str))
    			{
    				readAllow = true;
    			}*/
    		}
    		lastCount[nodeID] = temp;
        }
        catch(Exception e)
        {
            // System.out.println(e + " in readFile()");
        }
    }
    
     // Writing File
    static void writeFile(String message, String nodeID)
    {
    	try
    	{                              
    		String str = message;
    		String filePath = "input_"+nodeID+".txt";
    		BufferedWriter WriteFile = new BufferedWriter(new FileWriter(filePath,true));
    		WriteFile.write(str);
    		WriteFile.write("\n");
    		WriteFile.close();
        }
        catch(Exception e)
        {
            // System.out.println(e + " in writeFile()");
        }
    }
    
    public static void readControllerConfig()
	{
		try (BufferedReader br = new BufferedReader(new FileReader("topology.txt")))
		{
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null)
			{
				ArrayList<String> array = new ArrayList<String>();
				//System.out.println("Reading Line");
				String[] tokens = sCurrentLine.split(" ");
				//System.out.println("First:"+tokens[0]+" Second:"+tokens[1]);
				if(adjacency.get(tokens[0]) == null)
				{
					array.clear();
					//System.out.println("Adding new element");
					array.add(tokens[1]);
					adjacency.put(tokens[0], array);
					//System.out.println("New at:"+tokens[0]+":Element:"+Controller.adjacency.get(tokens[0]));
				}
				else
				{
					//System.out.println("Appending element");
					array = adjacency.get(tokens[0]);
					array.add(tokens[1]);
					adjacency.put(tokens[0], array);
					//System.out.println("Appended at:"+tokens[0]+":Element:"+Controller.adjacency.get(tokens[0]));
				}
			}
			
			/*// Testing the HashMap output
			for (int i=0;i<10;i++)
			{
				System.out.println(Controller.adjacency.get(String.valueOf(i)));
			}*/
	
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
