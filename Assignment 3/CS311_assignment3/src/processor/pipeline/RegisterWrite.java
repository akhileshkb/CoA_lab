package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
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
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			ControlUnit control_unit = containingProcessor.getControlUnit();
			ArithmeticLogicUnit alu = containingProcessor.getArithmeticLogicUnit();
			alu.setControlUnit(control_unit);

			int result = alu.getALUResult();
			if(control_unit.isLoad())
				result = MA_RW_Latch.getLoadResult();
			System.out.println(result);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int inst = containingProcessor.getMainMemory().getWord(currentPC-1);
			String instStr = Integer.toBinaryString(inst);
			if( inst > 0 ){
                instStr = String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
            }
            String rdStr = instStr.substring(10,15);

            if(control_unit.isJmp())
            	rdStr = instStr.substring(5,10);
            if(control_unit.getInstructionFormat() == "R3")
            	rdStr = instStr.substring(15,20);

            int rd = Integer.parseInt(rdStr,2);
            System.out.println("rd:" + Integer.toString(rd));

            if (control_unit.isWb())
            	containingProcessor.getRegisterFile().setValue(rd,result);
            if(control_unit.isEnd()){
            	// containingProcessor.setIsEnd(true);
            	Simulator.setSimulationComplete(true);
            }
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
