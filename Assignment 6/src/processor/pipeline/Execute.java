package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.Simulator;
import generic.Statistics;
import processor.Clock;
import processor.Processor;

public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	ControlUnit cu = new ControlUnit();
	public ControlUnit controlunit = new ControlUnit();
	boolean is_end = false;
	

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public ControlUnit getControlUnit(){
		return this.cu;
	}

	public void setControlUnit(ControlUnit cu){
		this.controlunit = cu;
	}

	public void aluEvent(Execute execute){

		if(this.cu.isDiv()){
			Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent( Clock.getCurrentTime() + Configuration.divider_latency , execute , execute ));
		}

		else if(this.cu.isMul()){
			Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent( Clock.getCurrentTime() + Configuration.multiplier_latency , execute , execute ));
		}

		else{
			Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent( Clock.getCurrentTime() + Configuration.ALU_latency , execute , execute ));
		}

		OF_EX_Latch.setEX_busy(true);

	}

	public void performEX()
	{
		//TODO
		if(OF_EX_Latch.isEX_enable() && !is_end)
		{
			if(OF_EX_Latch.isEX_busy()){
				// System.out.println("EX Busy");
				return;
			}

			// System.out.println("Performing EX");

			int rs1,rs2;
			setControlUnit(OF_EX_Latch.getControlUnit());

			this.cu = this.controlunit;
			ArithmeticLogicUnit alu = new ArithmeticLogicUnit();
			alu.setControlUnit(cu);

			if (cu.isEnd()){
				is_end = true;
			}

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

			if(cu.getOpcodeInt() == 23){
				EX_MA_Latch.setStoreData(rs1);
				rs1 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRd());
			}			

			alu.setA(rs1);	
			alu.setB(rs2);

			int aluout = alu.getALUResult();
			if(cu.isStore()){
				// System.out.println("ALU output "+aluout+"  "+rs1+"  "+rs2);
			}

			if(cu.isDiv())
			{
				// System.out.println(aluout);
				// System.out.println("Imm = "+ rs2);
				// System.out.println("rs1 = "+ rs1);
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
			// EX_MA_Latch.setRs2(rs2);
			// EX_MA_Latch.setRs1(rs1);
			EX_MA_Latch.setRd(OF_EX_Latch.getRd());
			EX_MA_Latch.setControlUnit(cu);

			// System.out.println("OPCODE: EX " + cu.getOpcode());

			if(isBranchTaken)
			{
				if(containingProcessor.getIFUnit().is_end == true) {
					Statistics.controlhaz +=1 ;
				}
				else {
					Statistics.controlhaz +=2 ;
				}
				EX_MA_Latch.setMA_enable(false);
				IF_EnableLatch.setIF_enable(true);
				containingProcessor.getOFUnit().IF_OF_Latch.OF_enable=false;
				containingProcessor.getIFUnit().IF_EnableLatch.IF_enable = false;
				Simulator.getEventQueue().deleteIF();
				OF_EX_Latch.setEX_enable(false);
			}
			else{
				aluEvent(this);
			}

		}
		else {
			// controlunit.opcode="";
			// controlunit.rs1="";
			// controlunit.rs2="";
			// controlunit.rd="";
			// controlunit.Imm = "";
			controlunit = new ControlUnit();
		}
	}

	@Override
	public void handleEvent(Event e) {
		
		if(EX_MA_Latch.isMA_busy()) {
			// System.out.println("not Handling");
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			// System.out.println("Handling");
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_busy(false);
		}
		
	}

}