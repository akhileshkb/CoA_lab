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
import processor.pipeline.OperandFetch;
import processor.pipeline.OF_EX_LatchType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable())
		{
			ControlUnit control_unit = containingProcessor.getControlUnit();
			if(control_unit.isLoad())
			{
				int ldRes = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
				System.out.println(EX_MA_Latch.getALUResult());
				System.out.println(ldRes);
				MA_RW_Latch.setLoadResult(ldRes);
			}
			else if(control_unit.isStore())
			{
				int location = EX_MA_Latch.getALUResult();
				int data = EX_MA_Latch.getRs2();
				System.out.println(location);
				System.out.println(data);
				containingProcessor.getMainMemory().setWord(location,data);
			}

			if(EX_MA_Latch.isMA_enable())
			{
				MA_RW_Latch.setRW_enable(true);
			}			
			else
			{
				MA_RW_Latch.setRW_enable(false);
			}
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
