package processor.pipeline;

import processor.Processor;
import processor.pipeline.ControlUnit;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	int max = Integer.MAX_VALUE;
	ControlUnit controlUnit = new ControlUnit();

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = IF_OF_Latch.getInstruction();
			String instruction = String.format("%32s", Integer.toBinaryString(newInstruction)).replaceAll(" ","0");
			String opcode = instruction.substring(0,5);
			controlUnit.setOpcode(opcode);

			int immx = max;
			int rs1 = max;
			int rd = max;
			int rs2 = max;
			int branchTarget = max;

			if(controlUnit.getInstructionFormat() == "R3"){

				immx = max;
				rs1 = Integer.parseInt(instruction.substring(5,10),2);
				rs2 = Integer.parseInt(instruction.substring(10,15),2);
				rd = Integer.parseInt(instruction.substring(15,20),2);
				branchTarget = max;
			}

			if(controlUnit.getInstructionFormat() == "R2I"){
				
				rs1 = Integer.parseInt(instruction.substring(5,10),2);
				rd = Integer.parseInt(instruction.substring(10,15),2);
				rs2 = max;
				branchTarget = currentPC + immx;

				String immxStr = instruction.substring(15,32);

				if(immxStr.charAt(0) == '1'){
					immxStr = twosComplement(immxStr);	
					immxStr = "-" + immxStr;
				}
				immx = Integer.parseInt(immxStr,2);
			}

			if(controlUnit.getInstructionFormat() == "RI"){

				rd = Integer.parseInt(instruction.substring(5,10),2);
				rs1 = max;
				rs2 = max;
				branchTarget = currentPC + immx;
				
				String immxStr = instruction.substring(15,32);

				if(immxStr.charAt(0) == '1'){
					immxStr = twosComplement(immxStr);	
					immxStr = "-" + immxStr;
				}
				immx = Integer.parseInt(immxStr,2);
			}						

			OF_EX_Latch.setRd(rd);
			OF_EX_Latch.setRs1(rs1);
			OF_EX_Latch.setRs2(rs2);
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setInstructionFormat(controlUnit.getInstructionFormat());

			containingProcessor.setControlUnit(controlUnit);

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		
			OF_EX_Latch.printState();
		}
	}

	public String twosComplement(String bin) {
        String twos = "", ones = "";

        for (int i = 0; i < bin.length(); i++) {
            ones += flip(bin.charAt(i));
        }
        int number0 = Integer.parseInt(ones, 2);
        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        if (!b)
            builder.append("1", 0, 7);

        twos = builder.toString();

        return twos;
    }

// Returns '0' for '1' and '1' for '0'
    public char flip(char c) {
        return (c == '0') ? '1' : '0';
    } 

}
