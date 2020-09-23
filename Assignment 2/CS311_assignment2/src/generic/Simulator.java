package generic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import generic.Operand.OperandType;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;


public class Simulator{
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file
		try 
		{
    		FileOutputStream fos = new FileOutputStream(new File(objectProgramFile));
			int firstCodeAddress = ParsedProgram.firstCodeAddress;\
			fos.write(firstCodeAddress);
			for (int i = 0; i < ParsedProgram.data.size(); i++)
			{
				fos.write(ParsedProgram.data.get(i));
			}
			for (int i = 0; i < ParsedProgram.code.size(); i++)
			{
				fos.write(ParsedProgram.code.get(i).toString().getBytes());
			} 
			fos.close();
		} 
		catch (IOException ex)
		{
	    	ex.printStackTrace();
		}
	}
	
}
