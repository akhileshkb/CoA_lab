package processor.pipeline;

import processor.memorysystem.MainMemory;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;

public class ControlUnit {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	String opcode;
	int opCodeInt;

	public ControlUnit()
	{
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		opcode = new String();
		opCodeInt = 0;	
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opCode) {
		this.opcode = opCode;
		this.opCodeInt = Integer.parseInt(this.opcode);
	}

	public String getInstructionFormat() {
		if(this.opcode == "11000" || this.opcode == "11101")
			return "RI";

		if(this.opcode.substring(4,5) == "0" && opCodeInt <= 20) 
			return "R3";

		return "R2I";
	}

	//Memory Instruction Flags
	public boolean isStore() {
        if( opcode.equals("10111") ){
            return true;
        }
        return false; 
    }

    public boolean isLoad() {
        if( opcode.equals("10110") ){
            return true;
        }
        return false; 
    }

    // Control Flow Instruction Flags
    public boolean isJmp() {
        if( opcode.equals("11000") ){
            return true;
        }
        return false; 
    }

    public boolean isBeq() {
        if( opcode.equals("11001") ){
            return true;
        }
        return false; 
    }

    public boolean isBne() {
        if( opcode.equals("11010") ){
            return true;
        }
        return false; 
    }

    public boolean isBlt() {
        if( opcode.equals("11011") ){
            return true;
        }
        return false; 
    }

    public boolean isBgt() {
        if( opcode.equals("11100") ){
            return true;
        }
        return false; 
    }

    public boolean isImmediate() {
        
        if( opCodeInt <= 23 && (opCodeInt % 2 == 1) ) {
            return true;
        } else if(opCodeInt == 22) {
            return true;
        }
        return false;
    }

    public boolean isWb() {
        if( opCodeInt >= 23) {
            return false;
        }
        return true; 
    }

    public boolean isAdd(){
        if( opCodeInt <=1 ){
            return true;
        }
        return false;
    }

    public boolean isSub(){
        if( opCodeInt == 2 || opCodeInt == 3 ){
            return true;
        }
        return false;
    }


    public boolean isMul(){
        if( opCodeInt == 4 || opCodeInt == 5 ){
            return true;
        }
        return false;
    }

    public boolean isDiv(){
        if( opCodeInt == 6 || opCodeInt == 7 ){
            return true;
        }
        return false;
    }

    public boolean isAnd(){
        if( opCodeInt == 8 || opCodeInt == 9 ){
            return true;
        }
        return false;
    }

    public boolean isOr(){
        if( opCodeInt == 10 || opCodeInt == 11 ){
            return true;
        }
        return false;
    }

    public boolean isXor(){
        if( opCodeInt == 12 || opCodeInt == 13 ){
            return true;
        }
        return false;
    }

    public boolean isSlt(){
        if( opCodeInt == 14 || opCodeInt == 15 ){
            return true;
        }
        return false;
    }

    public boolean isSll(){
        if( opCodeInt == 16 || opCodeInt == 17 ){
            return true;
        }
        return false;
    }

    public boolean isSrl(){
        if( opCodeInt == 18 || opCodeInt == 19 ){
            return true;
        }
        return false;
    }

    public boolean isSra(){
        if( opCodeInt == 20 || opCodeInt == 21 ){
            return true;
        }
        return false;
    }

    // Special Instruction Flags (end) 
    public boolean isEnd() {
        if( opcode.equals("11101") ){
            return true;
        }
        return false; 
    }

}