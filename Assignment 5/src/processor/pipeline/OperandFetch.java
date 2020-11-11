package processor.pipeline;

import generic.Statistics;
import processor.Processor;
import processor.pipeline.ControlUnit;
import processor.pipeline.ArithmeticLogicUnit;
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
	public IF_OF_LatchType IF_OF_Latch;
	public OF_EX_LatchType OF_EX_Latch;
	int max = Integer.MAX_VALUE;
	ControlUnit controlUnit = new ControlUnit();
	ArithmeticLogicUnit alu;
	boolean is_end = false;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}


	public boolean r3(String op) {
		if(op.equals("")) return false;
		if(op.charAt(4)=='0' && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10110"))
			return true;
		else return false;
		
	}
	public boolean r2i(String op) {
		if(op.equals("")) return false;
		if( ((op.charAt(4)=='1') || op.equals("10110") || op.equals("11010") ||op.equals("11100") ) && !op.equals("11101"))
			return true;
		else return false;
		
	}
	
	public boolean r2i1(String op) {
		if(op.equals("")) return false;
		if(op.charAt(4)=='1'  && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10111"))
			return true;
		else return false;
		
	}
	
	public boolean r2i2(String op) {//load or store
		if(op.equals("")) return false;
		if((op.equals("10110") || op.equals("10111")))
			return true;
		else return false;
		
	}
	
	public boolean r2i3(String op) { // branch
		if(op.equals("")) return false;
		if((op.charAt(0)=='1' && op.charAt(1)=='1')&&!op.equals("11000")&&!op.equals("11101")  )
			return true;
		else return false;
		
	}
	
	public boolean ri(String op) {
		if(op.equals("")) return false;
		if(op.equals("11101") || op.equals("11000"))
			return true;
		else return false;
		
	}

	public int convertbin( String s) {
		int n = s.length();
		int i = Integer.parseInt(s,2);
		if (s.charAt(0) == '1') {
			i = i - (int)Math.pow(2, n);

			
			return i;
		}
		else return i;
	}

	public void performOF()
	{
		System.out.println("Entered OF");
		if(IF_OF_Latch.isOF_enable() && !is_end)
		{
			//TODO
			if(OF_EX_Latch.isEX_busy()) {
				IF_OF_Latch.OF_busy=true;
				return;
			}
			else 
			{
				IF_OF_Latch.OF_busy=false;
			}
			System.out.println("Performing OF");
			controlUnit = IF_OF_Latch.getControlUnit();

			if (controlUnit.isEnd())
			{
				is_end = true;
			}


			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = IF_OF_Latch.getInstruction();
			String instruction = String.format("%32s", Integer.toBinaryString(newInstruction)).replaceAll(" ","0");
			String opcode = instruction.substring(0,5);

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

				String immxStr = instruction.substring(15,32);

				if(immxStr.charAt(0) == '1'){
					immxStr = twosComplement(immxStr);	
					immxStr = "-" + immxStr;
				}
				immx = Integer.parseInt(immxStr,2);
				branchTarget = currentPC + immx;
			}

			if(controlUnit.getInstructionFormat() == "RI"){

				rd = Integer.parseInt(instruction.substring(5,10),2);
				rs1 = max;
				rs2 = max;
				
				String immxStr = instruction.substring(15,32);

				if(immxStr.charAt(0) == '1'){
					immxStr = twosComplement(immxStr);	
					immxStr = "-" + immxStr;
				}
				immx = Integer.parseInt(immxStr,2);
				branchTarget = currentPC + immx;
			}						


			String rs1_str = String.format("%5s", Integer.toBinaryString(rs1)).replaceAll(" ","0");
			String rs2_str = String.format("%5s", Integer.toBinaryString(rs2)).replaceAll(" ","0");
			String rd_str = String.format("%5s", Integer.toBinaryString(rd)).replaceAll(" ","0");

			controlUnit.setInstruction(newInstruction);

			// Determine hazard
			boolean hazard = false;
			boolean conflict1 = false;
			boolean conflict2=false;
			boolean conflict3=false;
			switch(String.valueOf(r3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1_str == "11111"){
							hazard = true;
							conflict1 = true;
						}
						if(rs2_str == "11111"){
							hazard = true;
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
						if(rs2_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
								hazard = true;
								conflict1 = true;
						}
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
						if(rs2_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
							hazard = true;
							conflict3 = true;
						}
						if(rs2_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
							hazard = true;
							conflict3 = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1= true;
						}
						if(rs2_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1= true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
						if(rs2_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
					}
			}
			
			switch(String.valueOf(r2i1(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1_str.equals("11111")){
							hazard = true;
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
							
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
							hazard = true;
							conflict3 = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
					}
			}
			
			switch(String.valueOf(r2i3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1_str.equals("11111")){
							hazard = true;
							conflict1 = true;
						}
						if(rd_str.equals("11111")){
							hazard = true;
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
						if(rd_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
								hazard = true;
								conflict1 = true;
						}
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
						if(rd_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;	
							conflict2 = true;
						}
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
							hazard = true;
							conflict3 = true;
						}
						 if(rd_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
							hazard = true;
							conflict3 = true;
						}
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
						if(rd_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
							hazard = true;
							conflict1 = true;
						}
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
						if(rd_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
							hazard = true;
							conflict2 = true;
						}
					}
			}
			
			switch(String.valueOf(r2i2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10110":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1_str.equals("11111")){
									hazard = true;
									conflict1 = true;
								}
							}
							if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
									hazard = true;
									conflict1 = true;
								}
							}
							if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
							}
							if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
									hazard = true;
									conflict3 = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
									hazard = true;
									conflict1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1_str,2) + immx )== 
												Integer.parseInt((containingProcessor.getEXUnit().controlunit.rd),2) +
												convertbin(containingProcessor.getEXUnit().controlunit.Imm) ) {
									hazard = true;
									conflict1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1_str,2) + immx )== 
												Integer.parseInt((containingProcessor.getMAUnit().controlunit.rd),2) +
												convertbin(containingProcessor.getMAUnit().controlunit.Imm) ) {
									hazard = true;
									conflict2 = true;
								}
							}
					}
			}
			
			switch(String.valueOf(r2i2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10111":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1_str.equals("11111")){
									hazard = true;
									conflict1 = true;
								}
								if(rd_str.equals("11111")){
									hazard = true;
									conflict1 = true;
								}
							}
							if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
									hazard = true;
									conflict1 = true;
								}
								if(rd_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
										hazard = true;
										conflict1 = true;
								}
							}
							if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
								if(rd_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
							}
							if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
									hazard = true;
									conflict3 = true;
								}
								if(rd_str.equals(containingProcessor.getRWUnit().controlunit.rd)){
									hazard = true;
									conflict3 = true;
								}
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
									hazard = true;
									conflict1 = true;
								}
								if(rd_str.equals(containingProcessor.getEXUnit().controlunit.rd)){
									hazard = true;
									conflict1 = true;
								}
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
								if(rd_str.equals(containingProcessor.getMAUnit().controlunit.rd)){
									hazard = true;
									conflict2 = true;
								}
							}
					}
			}


			if(!hazard){
				OF_EX_Latch.setRd(rd);
				OF_EX_Latch.setRs1(rs1);
				OF_EX_Latch.setRs2(rs2);
				OF_EX_Latch.setImmx(immx);
				OF_EX_Latch.setBranchTarget(branchTarget);
				OF_EX_Latch.setInstructionFormat(controlUnit.getInstructionFormat());

				OF_EX_Latch.setControlUnit(controlUnit);
				containingProcessor.getIFUnit().hazard = false ;
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
			}		
			else{
				if(conflict1 && containingProcessor.getEXUnit().OF_EX_Latch.isEX_enable())
					Statistics.datahaz++;
				else if(conflict2 && containingProcessor.getMAUnit().EX_MA_Latch.isMA_enable() )
					Statistics.datahaz++;
				else if(conflict3 && containingProcessor.getRWUnit().MA_RW_Latch.isRW_enable())
					Statistics.datahaz++;
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false) ;
			}
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