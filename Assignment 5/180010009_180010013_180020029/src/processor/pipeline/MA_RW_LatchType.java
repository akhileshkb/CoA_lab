package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	ControlUnit cu;
	int loadresult;
	int aluResult;
	int rd;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setLoadResult(int loadresult)
	{
		this.loadresult = loadresult;
	}

	public int getLoadResult()
	{
		return this.loadresult;
	}
	public void setALUResult(int aluResult)
	{
		this.aluResult = aluResult;
	}

	public int getALUResult()
	{
		return this.aluResult;
	}
	public void setRd(int rd)
	{
		this.rd = rd;
	}

	public int getRd()
	{
		return this.rd;
	}

	public void setControlUnit(ControlUnit cu) {
		this.cu = cu;
	}

	public ControlUnit getControlUnit() {
		return this.cu;
	}

}