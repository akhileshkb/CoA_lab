package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class MemoryAccess{
	Processor containingProcessor;
	ControlUnit controlunit = new ControlUnit();
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	boolean is_end = false;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		// System.out.println("Entered MA");
		//TODO
		if(EX_MA_Latch.isMA_enable() && !is_end)
		{
			// System.out.println("Performing MA");
			
			MA_RW_Latch.setRd(EX_MA_Latch.getRd());
			MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());
			this.controlunit = EX_MA_Latch.getControlUnit();

			if (controlunit.isEnd()){
				is_end = true;
			}

			if(controlunit.isLoad())
			{
				int ldRes = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
				// System.out.println("Load Result : " + ldRes);
				MA_RW_Latch.setLoadResult(ldRes);
			}
			else if(controlunit.isStore())
			{
				int location = containingProcessor.getRegisterFile().getValue(EX_MA_Latch.getRd()) + EX_MA_Latch.getRs2();
				int data = EX_MA_Latch.getRs1();
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
			MA_RW_Latch.setControlUnit(EX_MA_Latch.getControlUnit());
			EX_MA_Latch.setMA_enable(false);
		}
		else{
			controlunit.opcode="";
			controlunit.rs1="";
			controlunit.rs2="";
			controlunit.rd="";
			controlunit.Imm = "";
		}
	}

}