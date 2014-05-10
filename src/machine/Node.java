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
		// 0 100 3 "message from 0 to 3" &
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
		long lastData = startTime;
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
		    if(outgoingNodeID != -1 && (System.currentTimeMillis()-lastData > 15000))
		    {
		    	sendData(nodeID, outgoingNodeID, message);
		    	lastData = System.currentTimeMillis();
		    }
		    // Reading input file for new messages
		    readFile();
		    //System.out.println("neighbours:"+neighbours);
		    // TODO: SEND MESSAGE
		}
	}
	
	public static void sendData(int source, int destination, String message)
	{
		// 0 100 3 "message from 0 to 3" &
		int incomingNeighbour = -1;
		try
		{
			if (!neighbours.contains(destination))
			{
				System.out.println("destination not neighbour");
				incomingNeighbour = calculateIncomingNeighbour(destination);
			}
			else
			{
				System.out.println("destination neighbour");
				incomingNeighbour = destination;
			}
			
			String path = pathToIncomingNeighbour(incomingNeighbour);
			System.out.println("PATH:"+path);
			// data A E C B begin message
			String finalMessage = "data "+source+" "+destination+" "+path+"begin "+message;
			System.out.println("finalmessage:"+finalMessage);
			writeFile(finalMessage);
		}
		catch(Exception e)
		{
			// Message cannt be sent. No Path.
			System.out.println("NO PATH");
		}
	}
	
	public static String pathToIncomingNeighbour(int incomingNeighbour)
	{
		int[][] readIntreeMatrix = returnMatrix(intree[incomingNeighbour]);
		String path = "";
		List<Integer> nextHop = new ArrayList<>();
		nextHop.add(nodeID);
		while(!nextHop.isEmpty())
		{
			for(int i=0;i<6;i++)
			{
				if(readIntreeMatrix[nextHop.get(0)][i] == 1)
				{
					if (i == incomingNeighbour)
					{
						path += i+" ";
						//System.out.println("edge:"+nextHop.get(0)+"-"+i);
						//System.out.println("PATH:"+ path);
						return path;
					}
					//System.out.println("edge:"+nextHop.get(0)+"-"+i);
					path += i+" ";
					nextHop.clear();
					nextHop.add(i);
				}
			}
		}
		//System.out.println("PATH:"+ path);
		return null;
	}
	
	public static int calculateIncomingNeighbour(int destination)
	{
		int destNode = destination;
		int[][] myIntreeMatrix = returnMatrix(intree[nodeID]);
		List<Integer> nextHop = new ArrayList<>();
		nextHop.add(destNode);
		while(!nextHop.isEmpty())
		{
			for(int i=0;i<6;i++)
			{
				if(myIntreeMatrix[nextHop.get(0)][i] == 1)
				{
					if (i == nodeID)
					{
						System.out.println("DEST:"+nextHop.get(0));
						return nextHop.get(0);
					}
					nextHop.clear();
					nextHop.add(i);
				}
			}
		}

		return -1;
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
    					// storing intree for source routing
    					intree[Integer.valueOf(tokens[1])] = str;
    					mergeIntree(str);
    				}
    				// data A E C B begin message
    				// 0    1 2 3 4 5
    				// data 0 3 3 begin message from 0 to 3
    				if(tokens[0].equals("data"))
    				{
    					// storing intree for source routing
    					// Destination check
    					if(Integer.valueOf(tokens[2]) == nodeID)
    					{
    						System.out.println("Hurray MESSAGE RECEIVED");
    					}
    					// Intermediate check
    					else if(Integer.valueOf(tokens[3]) == nodeID)
    					{
    						if(tokens[4].equals("begin"))
    						{
    							// New Source Routing
    						}
    						else
    						{
    							// Forward data to tokens[4]
    							String newMessage="data "+tokens[1]+" "+tokens[2];
    							for(int i=4;i<tokens.length;i++)
    							{
    								newMessage += " "+tokens[i];
    							}
    							writeFile(newMessage);
    						}
    					}
    					else
    					{
    						// IGNORE MESSAGE
    					}
    				}
    			}
    			
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
