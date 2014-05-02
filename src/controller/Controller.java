package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Controller
{
	public static int duration;
	public static String[] lastRead = new String[10];
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
				System.out.println("duration:"+duration);
		    }
			catch (Exception e)
			{
				System.err.println("Argument must be in proper format");
				System.exit(1);
		    }
		}
		InputOutputHandler IOH = new InputOutputHandler();
		IOH.readControllerConfig();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Infinite loop
		long startTime = System.currentTimeMillis(); //fetch starting time
		while(System.currentTimeMillis()-startTime < duration)
		{
			// Read from all output files sequentially
			for(int i=0;i<3;i++)
			{
				readFile(i);
			}
		}
	}
	
	 // Reading File
    static void readFile(int nodeID)
    {
    	try
    	{
    		Boolean readAllow = false;
    		String str = "output_"+nodeID+".txt";
    		BufferedReader ReadFile = new BufferedReader(new FileReader(str));
    		while((str = ReadFile.readLine()) != null)
    		{
    			String[] tokens = str.split(" ");
    			// First message
    			if(lastRead[nodeID] == null || readAllow)
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
    			}
    		}
        }
        catch(Exception e)
        {
            System.out.println(e + " in readFile()");
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
            System.out.println(e + " in writeFile()");
        }
    }
}
