package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	ControlUnit controlunit = new ControlUnit();
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	boolean is_end = false;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		System.out.println("Entered RW");
		if(MA_RW_Latch.isRW_enable())
		{
			System.out.println("performing RW");
			//TODO
			controlunit = MA_RW_Latch.getControlUnit(); 
			int result = MA_RW_Latch.getALUResult();
			int rd = MA_RW_Latch.getRd();

			if(controlunit.isLoad())
				result = MA_RW_Latch.getLoadResult();
			
			System.out.println(Integer.toString(result)+"  "+rd);

            if (controlunit.isWb())
            	containingProcessor.getRegisterFile().setValue(rd,result);
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);

			System.out.println(controlunit.opCodeInt);

			if(controlunit.opCodeInt == 29){
            	Simulator.setSimulationComplete(true);
				MA_RW_Latch.setRW_enable(false);
				IF_EnableLatch.setIF_enable(false);
				containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() - 3);

            }
		}
		else{
			IF_EnableLatch.setIF_enable(true);	
			controlunit.opcode="";
			controlunit.rs1="";
			controlunit.rs2="";
			controlunit.rd="";
			controlunit.Imm = "";
		}
	}

}