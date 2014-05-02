package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Controller
{
	public static int duration;
	public static String[] lastRead = new String[10];

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
	}
	
	 // Reading File
    void readFile(String nodeID)
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
    void writeFile(String message, String nodeID)
    {
    	try
    	{                              
    		String str = message;
    		String filePath = "output_"+nodeID+".txt";
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
