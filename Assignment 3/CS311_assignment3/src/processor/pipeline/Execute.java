package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		//TODO

		ControlUnit cu = containingProcessor.getControlUnit();
		ArithmeticLogicUnit alu = containingProcessor.getArithmeticLogicUnit(); 

		int rs1 = OF_EX_Latch.getRs1();
		int rs2;
		if(cu.isImmediate())
		{
			rs2 = OF_EX_Latch.getImmx();
		}
		else
		{
			rs2 = OF_EX_Latch.getRs2();
		}

		System.out.println("rs1: " + rs1);

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
		if(cu.isJmp() || (cu.isBeq() && alu.getFlag("E")) || (cu.isBgt() && alu.getFlag("GT")) || (cu.isBlt() && alu.getFlag("LT")) || (cu.isBne() && alu.getFlag("NE")))
		{
			isBranchTaken = true;
		}

		EX_IF_Latch.setbranchPC(OF_EX_Latch.getBranchTarget());
		EX_IF_Latch.setIsBranchTaken(isBranchTaken);

		EX_MA_Latch.setALUresult(aluout);
		EX_MA_Latch.setRs2(OF_EX_Latch.getRs2());

		OF_EX_Latch.setEX_enable(false);

		if(isBranchTaken)
		{
			EX_MA_Latch.setMA_enable(false);
		}
		else
		{
			EX_MA_Latch.setMA_enable(true);	
		}

		
	}

}
