package processor.pipeline;

public class IF_OF_LatchType {
	ControlUnit cu;
	boolean OF_enable;
	int instruction;
	boolean OF_busy;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction , String opcode) {
		this.instruction = instruction;
		ControlUnit cu1 = new ControlUnit();

		cu1.setOpcode(opcode);
		this.cu = cu1;
	}

	public ControlUnit getControlUnit() {
		return cu;
	}
}
