package controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputOutputHandler
{
	//public static HashMap<String, ArrayList<String>> adjacency = new HashMap<String, ArrayList<String>>();
	public static void readControllerConfig()
	{
		try (BufferedReader br = new BufferedReader(new FileReader("topology.txt")))
		{
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null)
			{
				ArrayList<String> array = new ArrayList<String>();
				System.out.println("Reading Line");
				String[] tokens = sCurrentLine.split(" ");
				System.out.println("First:"+tokens[0]+" Second:"+tokens[1]);
				if(Controller.adjacency.get(tokens[0]) == null)
				{
					array.clear();
					//System.out.println("Adding new element");
					array.add(tokens[1]);
					Controller.adjacency.put(tokens[0], array);
					//System.out.println("New at:"+tokens[0]+":Element:"+Controller.adjacency.get(tokens[0]));
				}
				else
				{
					
					//System.out.println("Appending element");
					array = Controller.adjacency.get(tokens[0]);
					array.add(tokens[1]);
					Controller.adjacency.put(tokens[0], array);
					//System.out.println("Appended at:"+tokens[0]+":Element:"+Controller.adjacency.get(tokens[0]));
				}
			}
			
			/*// Testing the HashMap output
			for (int i=0;i<10;i++)
			{
				System.out.println(Controller.adjacency.get(String.valueOf(i)));
			}
			*/
	
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
