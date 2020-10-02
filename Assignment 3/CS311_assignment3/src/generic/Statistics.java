package generic;

import java.io.*;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions = 10;
	static int numberOfCycles = 20;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			//FileOutputStream fos = new FileOutputStream(new File(statFile));
			PrintWriter writer = new PrintWriter(new File(statFile));
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			
			// String s ="Number of instructions executed = " + Integer.toString(numberOfInstructions) + "\n";
			// byte b[]=s.getBytes();
			// fos.write(b);

			// s = "Number of cycles taken = " + Integer.toString(numberOfCycles);
			// byte b1[]=s.getBytes();
			// fos.write(b1);

			// fos.flush();
			// fos.close();
			// TODO add code here to print statistics in the output file
			
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
