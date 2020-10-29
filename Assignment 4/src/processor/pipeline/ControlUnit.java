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
    
    int instruction;
    int opCodeInt;
    int overflow;
    boolean isOverflow;
    String opcode="",rs1="",rs2="",rd="",Imm = "";

    public ControlUnit()
    {
        opcode = new String();
        isOverflow = false;
        opCodeInt = 0;
        overflow = 0;
        opcode="";
        rs1="";
        rs2="";
        rd="";
        Imm = "";   
    }

    public boolean r3(String op) {
        if(op.equals("")) return false;
        if(op.charAt(4)=='0' && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10110"))
            return true;
        else return false;
        
    }
    public boolean r2i(String op) {
        if(op.equals("")) return false;
        if( ((op.charAt(4)=='1') || op.equals("10110") || op.equals("11010") ||op.equals("11100") ) && !op.equals("11101"))
            return true;
        else return false;
        
    }
    
    public boolean ri(String op) {
        if(op.equals("")) return false;
        if(op.equals("11101") || op.equals("11000"))
            return true;
        else return false;
        
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
        String instructionString = Integer.toBinaryString(instruction);
        int n = instructionString.length();
        String todo="" ;
        for (int i=0;i<32-n;i++){
            todo = todo + "0" ;
        }
        instructionString = todo + instructionString;
        rs1 = instructionString.substring(5,10);
        rs2 = instructionString.substring(10,15);
        opcode=instructionString.substring(0,5);
        switch(String.valueOf(r3(opcode)))
        {
            case "true":
            rd=instructionString.substring(15,20);
        }
        switch(String.valueOf(r2i(opcode)))
        {
            case "true":
            rd=instructionString.substring(10,15);
            Imm=instructionString.substring(15,32);
        }
        switch(String.valueOf(ri(opcode)))
        {
            case "true":
            rd=instructionString.substring(5,10);
            Imm=instructionString.substring(10,32);
        }
    }

    public boolean getIsOverflow() {
        return this.isOverflow;
    }

    public void setIsOverflow(boolean isOverflow) {
        this.isOverflow = isOverflow;
    }

    public int getOverflow() {
        return this.overflow;
    }

    public void setOverflow(int overflow) {
        this.overflow = overflow;
    }

    public String getOpcode() {
        return opcode;
    }

    public int getOpcodeInt() {
        return opCodeInt;
    }

    public void setOpcode(String opCode) {
        this.opcode = opCode;
        this.opCodeInt = Integer.parseInt(this.opcode,2);
    }

    public String getInstructionFormat() {
        if(this.opcode == "11000" || this.opcode == "11101")
            return "RI";

        if((opCodeInt % 2 == 0) && (opCodeInt <= 20)) 
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