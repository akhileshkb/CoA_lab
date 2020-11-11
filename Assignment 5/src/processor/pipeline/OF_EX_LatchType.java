package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable,EX_busy;
	ControlUnit cu;
	int rs1;       //if any of these integers are 2147483647 then that integer is not used in the format.
	int rs2;
	int rd;
	int immx;
	int branchTarget;

	String instructionFormat;

	public void setEX_busy(boolean eX_busy){
		 EX_busy=eX_busy;
	}
	public boolean isEX_busy() {
		return EX_busy;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
		rs1 = 0;
		rs2 = 0;
		rd = 0;
		immx = 0;
		branchTarget = 0;
		instructionFormat = new String();
	}

	public void setRs1(int rs_1) {
		this.rs1 = rs_1;
	}

	public int getRs1() {
		return rs1;
	}

	public void setRs2(int rs_2) {
		this.rs2 = rs_2;
	}

	public int getRs2() {
		return rs2;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

	public int getRd() {
		return rd;
	}

	public void setImmx(int immx) {
		this.immx = immx;
	}

	public int getImmx() {
		return immx;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}

	public int getBranchTarget() {
		return branchTarget;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setInstructionFormat(String format) {
		this.instructionFormat = format;
	}

	public String getInstructionFormat() {
		return instructionFormat;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setControlUnit(ControlUnit cu) {
		this.cu = cu;
	}

	public ControlUnit getControlUnit() {
		return cu;
	}

	public void printState(){  
		System.out.println("rs1 = " + rs1);
		System.out.println("rs2 = " + rs2);
		System.out.println("rd = " + rd);
		System.out.println("immx = " + immx);
		System.out.println("branchTarget = " + branchTarget);
		System.out.println("instructionFormat = " + instructionFormat + " Type.");
	}
}
