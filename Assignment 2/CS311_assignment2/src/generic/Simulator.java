package generic;

// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import generic.Operand.OperandType;
// import java.io.ObjectOutputStream;
// import java.io.File;
// import java.io.IOException;
import java.io.*;

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
    		FileOutputStream fout = new FileOutputStream(new File(objectProgramFile));
    		ByteArrayOutputStream bout = new ByteArrayOutputStream(4);
			DataOutputStream dout = new DataOutputStream(bout);

			int firstCodeAddress = ParsedProgram.firstCodeAddress;
			dout.writeInt(firstCodeAddress);

			for (int i = 0; i < ParsedProgram.data.size(); i++)
			{
				dout.writeInt(ParsedProgram.data.get(i));
			}
			String result = "";
			int res=0;
			for (int i = 0; i < ParsedProgram.code.size(); i++)
			{
				result = "";
				int pc = ParsedProgram.code.get(i).getProgramCounter();
				// dout.writeInt(pc);
				Instruction.OperationType operation_type = ParsedProgram.code.get(i).operationType;
				int opcode = operation_type.ordinal();
				
				result = result + String.format("%5s", Integer.toBinaryString(opcode));
				
				System.out.println(res);
				

				Operand sop1 = new Operand();
				Operand sop2 = new Operand();
				Operand dop = new Operand();

				sop1= ParsedProgram.code.get(i).getSourceOperand1();
				sop2= ParsedProgram.code.get(i).getSourceOperand2();
				dop= ParsedProgram.code.get(i).getDestinationOperand();

				if(opcode != 29 && opcode != 24)
				{
					int optype1 = sop1.getOperandType().ordinal();
					int optype2 = sop2.getOperandType().ordinal();
					int optype3 = dop.getOperandType().ordinal();	
					
					if(optype1 == 0 && optype2 == 0 && optype3 == 0)		//R3 type Operation
					{
						int rs1 = sop1.getValue();
						int rs2 = sop2.getValue();
						int rd = dop.getValue();
						result = result + String.format("%5s", Integer.toBinaryString(rs1));

						result = result + String.format("%5s", Integer.toBinaryString(rs2));

						result = result + String.format("%5s", Integer.toBinaryString(rd));
					}
					
				}
				res=Integer.parseInt(result); 
				dout.writeInt(res);
			}
			
			bout.writeTo(fout);

			fout.flush();
			fout.close();
		} 
		catch (IOException ex)
		{
	    	ex.printStackTrace();
		}
	}
	
}
