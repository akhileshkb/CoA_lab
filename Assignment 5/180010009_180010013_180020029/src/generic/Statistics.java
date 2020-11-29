package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	public static int numberOfInstructions = 0;
	static int numberOfCycles;
	static double throughput;
	public static int datahaz=0;
	public static int controlhaz=0;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			throughput = ((double)numberOfInstructions)/numberOfCycles;
			// writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of dynamic instructions = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of data hazards = " + datahaz);
			writer.println("Number of control hazards = " + controlhaz);
			writer.println("Throughput = " + throughput);
			//  TODO add code here to print statistics in the output file

			// System.out.println("Number of instructions executed = " + numberOfInstructions);
			// System.out.println("Number of cycles taken = " + numberOfCycles);
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}
}
