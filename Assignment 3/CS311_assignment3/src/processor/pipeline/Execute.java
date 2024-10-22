package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performEX()
	{
		//TODO
		if(OF_EX_Latch.isEX_enable())
		{
			ControlUnit cu = containingProcessor.getControlUnit();
			ArithmeticLogicUnit alu = containingProcessor.getArithmeticLogicUnit();
			alu.setControlUnit(cu); 

			int rs1;
			int rs2;

			rs1 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRs1());
			if(cu.getInstructionFormat() != "R3")
			{
				rs2 = OF_EX_Latch.getImmx();
			} 
			else
			{
				rs2 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRs2());
			}

			if(cu.getOpcodeInt() > 23){
				rs1 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRs1());
				rs2 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRd());
			}

			alu.setA(rs1);	
			alu.setB(rs2);

			int aluout = alu.getALUResult();

			if(cu.isDiv())
			{
				int mod = rs1%rs2;
				containingProcessor.getRegisterFile().setValue(31, mod);
			}
			if(cu.getIsOverflow()) 
			{
				int overflow = cu.getOverflow();
				containingProcessor.getRegisterFile().setValue(31, overflow);
			}

			boolean isBranchTaken = false;

			if(cu.isJmp()) {
				isBranchTaken = true;
			} else if(cu.isBeq() && alu.getFlag("E")) {
				isBranchTaken = true;
			} else if(cu.isBgt() && alu.getFlag("GT")) {
				isBranchTaken = true;
			} else if(cu.isBlt() && alu.getFlag("LT")) {
				isBranchTaken = true;
			} else if(cu.isBne() && alu.getFlag("NE")) {
				isBranchTaken = true;
			}

			EX_IF_Latch.setbranchPC(OF_EX_Latch.getBranchTarget());
			EX_IF_Latch.setIsBranchTaken(isBranchTaken);

			EX_MA_Latch.setALUresult(aluout);
			EX_MA_Latch.setRs2(rs2);
			EX_MA_Latch.setRs1(rs1);
			EX_MA_Latch.setRd(OF_EX_Latch.getRd());

			OF_EX_Latch.setEX_enable(false);
			if(isBranchTaken)
			{
				EX_MA_Latch.setMA_enable(false);
				IF_EnableLatch.setIF_enable(true);
			}
			else
			{
				EX_MA_Latch.setMA_enable(true);	
			}
		}
	}

}
