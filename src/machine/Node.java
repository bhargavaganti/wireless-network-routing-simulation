package machine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Node
{
	public static int nodeID;
	public static int outgoingNodeID;
	public static int duration;
	public static String message;
	//public static String lastRead;
	public static int lastCount;
	public static ArrayList<String> neighbours = new ArrayList<String>();
	public static String[] intree = new String[10];
	
	static List<Integer> usedNodes = new ArrayList<>();
	static List<Integer> nextCheck = new ArrayList<>();
	static List<Integer> tempCheck = new ArrayList<>();
	static String newIntree;

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
		long lastIntree = startTime;
		while(System.currentTimeMillis()-startTime < duration)
		{
			// Hello message every 5 seconds
		    if(System.currentTimeMillis()-lastHello > 5000)
		    {
		    	writeFile("hello "+nodeID+" "+System.currentTimeMillis());
		    	lastHello = System.currentTimeMillis();
		    }
		    if(System.currentTimeMillis()-lastIntree > 10000)
		    {
		    	writeFile(intree[nodeID]);
		    	lastIntree = System.currentTimeMillis();
		    }
		    // Reading input file for new messages
		    readFile();
		    //System.out.println("neighbours:"+neighbours);
		    // TODO:
		}
	}
	
	// Merging Intree
	public static void mergeIntree(String readIntree)
	{
		int[][] myIntreeMatrix = returnMatrix(intree[nodeID]);
		int[][] readIntreeMatrix = returnMatrix(readIntree);
		int[][] newMatrix = new int[6][6];
		
		System.out.println("Old Intree:"+intree[nodeID]);
		System.out.println("Received Intree:"+readIntree);
		
		// Merge Matrix
		for(int i=0;i<newMatrix.length;i++)
		{
			for(int j=0;j<newMatrix.length;j++)
			{
				// Boolean addition
				if(myIntreeMatrix[i][j] == 1 || readIntreeMatrix[i][j] == 1)
				{
					newMatrix[i][j] = 1;
				}
			}
		}
		printMatrix(newMatrix);
		// Converting matrix to string
		usedNodes.clear();
		newIntree = "intree "+nodeID;
		nextCheck.add(nodeID);
		usedNodes.add(nodeID);
		while(!nextCheck.isEmpty())
		{
			generateIntree(newMatrix);
		}
		
		System.out.println("INTREE:"+newIntree);
		intree[nodeID] = newIntree;
		//System.exit(0);
		
	}

	public static void generateIntree(int[][] newMatrix)
	{
		
		//printMatrix(newMatrix);
		tempCheck.clear();
		tempCheck.addAll(nextCheck);
		nextCheck.clear();
		//System.out.println("tempCheck:"+tempCheck);
		for(int nextInt: tempCheck)
		{
			System.out.println("nextInt:"+nextInt);
			for(int i=0;i<newMatrix.length;i++)
			{
				//usedNodes.add(nextInt);
				//System.out.println("Added to used nodes:"+nextInt);
				//System.out.println("iXnextInt:"+newMatrix[i][nextInt]);
				if(newMatrix[i][nextInt] == 1 && !usedNodes.contains(i))
				{
					newIntree += " ( "+ i + " " + nextInt + " )";
					nextCheck.add(i);
					usedNodes.add(i);
					//System.out.println("Added to next nodes:"+i);
				}
			}
		}
	}
	
	public static int[][] returnMatrix(String tree)
	{
		int[][] matrix = new int[6][6];
		String[] tokens = tree.split(" ");
		for(int i=0;i<tokens.length;i++)
		{
			if(tokens[i].equals("("))
			{
				//System.out.println("Row:"+Integer.valueOf(tokens[i+1]));
				//System.out.println("Column:"+Integer.valueOf(tokens[i+2]));
				matrix[Integer.valueOf(tokens[i+1])][Integer.valueOf(tokens[i+2])] = 1;
			}
		}
		return matrix;
	}

	// Making default Intree from Neighbours List
	public static void createIntree()
	{
		intree[nodeID] = "intree "+nodeID;
		for(String neighbour : neighbours)
		{
			intree[nodeID] += " ( "+neighbour+" "+nodeID+" )";
		}
		System.out.println("Hello Intree at "+nodeID+" :"+intree[nodeID]);
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
    				// Reading Hello Protocol messages
    				// hello 2 1399591802089
    				if(tokens[0].equals("hello"))
    				{
    					// Find Neighbours
    					if(!neighbours.contains(tokens[1]))
    					{
    						// New neighbours discovered
    						neighbours.add(tokens[1]);
    						createIntree();
    					}
    				}
    				
    				// Reading intree protocol messages
    				// intree 2 ( 0 2 ) ( 1 2 )
    				if(tokens[0].equals("intree"))
    				{
    					intree[Integer.valueOf(tokens[1])] = str;
    					mergeIntree(str);
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
            // System.out.println(e + " in readFile()");
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
            //System.out.println(e + " in writeFile()");
        }
    }
    
	public static void printMatrix(int[][] matrix)
	{
		System.out.println("Length:"+matrix.length);
		for (int i = 0; i < 6; i++) {
		    for (int j = 0; j < 6; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	}
    
}
