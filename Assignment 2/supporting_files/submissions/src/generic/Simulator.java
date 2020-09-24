package generic;

// import java.io.FileInputStream;
// import java.io.FileOutputStream;
import generic.Operand.OperandType;
import generic.Instruction.OperationType;
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

	static int binaryToDecimal(String n) 
    { 
        String num = n; 
        int dec_value = 0; 
        int base = 1; 
  
        int len = num.length(); 
        for (int i = len - 1; i >= 0; i--) { 
            if (num.charAt(i) == '1') 
                dec_value += base; 
            base = base * 2; 
        } 
  
        return dec_value; 
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
			
			for (int i = 0; i < ParsedProgram.code.size(); i++)
			{
				int pc = ParsedProgram.code.get(i).programCounter;
				int operation_type = ParsedProgram.code.get(i).operationType.ordinal();
				String opcode = String.format("%5s", Integer.toBinaryString(operation_type));
				String res = "";
				if(operation_type != 29 && operation_type != 24)
				{
					Operand op1 = ParsedProgram.code.get(i).getSourceOperand1();
					Operand op2 = ParsedProgram.code.get(i).getSourceOperand2();
					Operand dop_o = ParsedProgram.code.get(i).getDestinationOperand();
					int sop1 = op1.getOperandType().ordinal();
					int sop2 = op2.getOperandType().ordinal();
					int dop = dop_o.getOperandType().ordinal();
					
					if(sop1 == 0 && sop2 == 0 && dop == 0)
					{
						String op1_S = String.format("%5s", Integer.toBinaryString(op1.getValue())).replaceAll(" ","0");
						String op2_S = String.format("%5s", Integer.toBinaryString(op2.getValue())).replaceAll(" ","0");
						String dop_S = String.format("%5s", Integer.toBinaryString(dop_o.getValue())).replaceAll(" ","0");

						res = opcode + op1_S + op2_S + dop_S + "000000000000";
					}
					else if(operation_type%2 == 1 || operation_type == 22 || operation_type == 23 || operation_type >=24)
					{
						if(operation_type >24 && operation_type<29)
						{
							String op1_S = String.format("%5s", Integer.toBinaryString(op1.getValue())).replaceAll(" ","0");
							String op2_S = String.format("%5s", Integer.toBinaryString(op2.getValue())).replaceAll(" ","0");
							res = opcode + op1_S + op2_S;
							if(dop == 1)
							{
								String op = Integer.toBinaryString(dop_o.getValue());
								if(op.length()>17)
								{
									op = op.substring(op.length()-17);
								}
								else
									op = String.format("%17s", op).replaceAll(" ", "0");
								res = res + op;
							}
							else
							{
								String op = Integer.toBinaryString(- pc + ParsedProgram.symtab.get(dop_o.getLabelValue()));
								if(op.length()>17)
								{
									op = op.substring(op.length()-17);
								}
								else
									op = String.format("%17s", op).replaceAll(" ", "0");
								res = res + op;
							}
						}
						else if(operation_type%2 == 1 || operation_type == 22 || operation_type == 23)
						{
							String op1_S = String.format("%5s", Integer.toBinaryString(op1.getValue())).replaceAll(" ","0");
							String dop_S = String.format("%5s", Integer.toBinaryString(dop_o.getValue())).replaceAll(" ","0");
							String op2_S = String.format("%17s", Integer.toBinaryString(op2.getValue())).replaceAll(" ","0");

							res = opcode + op1_S + dop_S + op2_S;
						}
					}
				}
				else 
				{
					if(operation_type==29)
					{
						String unused = String.format("%27s", Integer.toBinaryString(0)).replaceAll(" ","0");
						res =  opcode + unused;
					}
					if(operation_type == 24)
					{
						Operand dop_o = ParsedProgram.code.get(i).getDestinationOperand();
						int dop = dop_o.getOperandType().ordinal();
						if(dop == 0)
						{
							int pc1 = pc + dop_o.getValue();
							res = opcode + String.format("%5s", Integer.toBinaryString(pc1)).replaceAll(" ","0") + String.format("%22s", Integer.toBinaryString(0)).replaceAll(" ","0");
						}
						else if(dop == 1)
						{
							int pc1 = pc + dop_o.getValue();
							res = opcode + "00000" +String.format("%22s", Integer.toBinaryString(pc1)).replaceAll(" ","0");
						}
						else
						{
							int pc1 = ParsedProgram.symtab.get(dop_o.getLabelValue()) - pc;
							res = opcode + "00000" +String.format("%22s", Integer.toBinaryString(pc1)).replaceAll(" ","0");
						}
						 
					}
				}
				int res_int = binaryToDecimal(res);
				dout.writeInt(res_int);
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
