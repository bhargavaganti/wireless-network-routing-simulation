package machine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

public class Node
{
	public static int nodeID;
	public static int outgoingNodeID;
	public static int duration;
	public static String message;
	public static String[] lastRead = new String[10];

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
		    	writeFile("Hello "+nodeID+" "+System.currentTimeMillis());
		    	lastHello = System.currentTimeMillis();
		    }
		}
	}
	
    // Reading File
    public static void readFile()
    {
    	try
    	{
    		String str = "input_"+nodeID+".txt";
    		BufferedReader ReadFile = new BufferedReader(new FileReader(str));
    		while((str = ReadFile.readLine()) != null)
    		{
    			System.out.println("File Data:"+str);
    		}
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
