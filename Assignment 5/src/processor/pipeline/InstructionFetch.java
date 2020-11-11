package processor.pipeline;

import configuration.Configuration;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import generic.Element;
import generic.Event;
import processor.Processor;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	boolean is_end = false;
	boolean hazard = false;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		System.out.println("Entered IF");
		if(IF_EnableLatch.isIF_enable() && !is_end)
		{
			if(IF_EnableLatch.isIF_busy()) 
			{
					return;
			}

			System.out.println("Performing IF");
			int currentPC;
			if(EX_IF_Latch.getIsBranchTaken())
			{
				currentPC = EX_IF_Latch.getbranchPC() - 1;
				containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getbranchPC());
			}
			
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
									Clock.getCurrentTime()+Configuration.mainMemoryLatency,
									this,containingProcessor.getMainMemory(),
									containingProcessor.getRegisterFile().getProgramCounter()));
			IF_EnableLatch.setIF_busy(true);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

			

			// System.out.println(newInstruction+"  "+instructionString);

			// if(!hazard){
			// 	IF_OF_Latch.setInstruction(newInstruction,opcode);	
			// 	containingProcessor.setNumIns(containingProcessor.getNumIns() + 1);
			// 	containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);	
			// 	if(opcode == "11101"){
			// 		is_end = true;
			// 	}
			// }
			// containingProcessor.setNumCycles(containingProcessor.getNumCycles() + 1);
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);
		}
	}

	@Override
	public void handleEvent(Event e) 
	{
		// TODO Auto-generated method stub
		if(IF_OF_Latch.OF_busy) 
		{
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else 
		{
			MemoryResponseEvent event=(MemoryResponseEvent) e;
			// int currentPC_temp = containingProcessor.getRegisterFile().getProgramCounter();
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC_temp);
			int newInstruction = event.getValue();
			String instructionString = String.format("%32s", Integer.toBinaryString(newInstruction)).replaceAll(" ","0");
			String opcode = instructionString.substring(0,5);
			IF_OF_Latch.setInstruction(newInstruction, opcode);
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);	
		}
		
	}

}