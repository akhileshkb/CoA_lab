package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	ControlUnit control_unit;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
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
			control_unit = MA_RW_Latch.getControlUnit();
			int result = MA_RW_Latch.getALUResult();
			int rd = MA_RW_Latch.getRd();

			if(control_unit.isLoad())
				result = MA_RW_Latch.getLoadResult();
			
			System.out.println(Integer.toString(result)+"  "+rd);

            if (control_unit.isWb())
            	containingProcessor.getRegisterFile().setValue(rd,result);
            if(control_unit.isEnd()){
            	System.out.println("BYE BYE");
            	Simulator.setSimulationComplete(true);
            }
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
		else{
			IF_EnableLatch.setIF_enable(true);	
		}
	}

}