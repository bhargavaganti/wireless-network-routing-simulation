package machine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Node
{
	public static int nodeID;
	public static int outgoingNodeID;
	public static int duration;
	public static String message;
	public static String lastRead;
	public static int lastCount;
	public static ArrayList<String> neighbours = new ArrayList<String>();

	public static void main(String args[])
	{
		// User will let the node know its nodeID
		// node 9 100 5 "this is a message" &
		if (args.length > 0)
		{
			try
			{
				nodeID = Integer.parseInt(args[0]);
				duration = 1000*Integer.parseInt(args[1]);
				outgoingNodeID = Integer.parseInt(args[2]);
				message = args[3];
				System.out.println("nodeID:"+nodeID);
				System.out.println("duration:"+duration);
				System.out.println("outgoingNodeID:"+outgoingNodeID);
				System.out.println("message:"+message);
		    }
			catch (Exception e)
			{
				System.err.println("Argument must be in proper format");
				System.exit(1);
		    }
		}
		
		// Infinite loop
		long startTime = System.currentTimeMillis(); //fetch starting time
		long lastHello = startTime;
		while(System.currentTimeMillis()-startTime < duration)
		{
			// Hello message every 5 seconds
		    if(System.currentTimeMillis()-lastHello > 5000)
		    {
		    	writeFile("hello "+nodeID+" "+System.currentTimeMillis());
		    	lastHello = System.currentTimeMillis();
		    }
		    // Reading input file for new messages
		    readFile();
		    System.out.println(neighbours);
		}
	}
	
    // Reading File
    public static void readFile()
    {
    	try
    	{
    		Boolean readAllow = false;
    		String str = "input_"+nodeID+".txt";
    		BufferedReader ReadFile = new BufferedReader(new FileReader(str));
    		int temp = 0;
    		while((str = ReadFile.readLine()) != null)
    		{
    			String[] tokens = str.split(" ");
    			// First message
    			
    			++temp;
    			if(temp > lastCount)
    			{
    				if(tokens[0].equals("hello"))
    				{
    					// Find Neighbours
    					if(!neighbours.contains(tokens[1]))
    					{
    						neighbours.add(tokens[1]);
    					}
    				}
    			}
    			
    			/*if(lastRead == null || readAllow)
    			{
    				lastRead = str;
    				if(tokens[0].equals("hello"))
    				{
    					// Find Neighbours
    					if(!neighbours.contains(tokens[1]))
    					{
    						neighbours.add(tokens[1]);
    					}
    				}
    			}
    			else if(lastRead.equals(str))
    			{
    				readAllow = true;
    			}*/
    		}
    		lastCount = temp;
        }
        catch(Exception e)
        {
            System.out.println(e + " in readFile()");
        }
    }
    
    // Writing File
    public static void writeFile(String message)
    {
    	try
    	{                              
    		String str = message;
    		String filePath = "output_"+nodeID+".txt";
    		// Append mode
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
