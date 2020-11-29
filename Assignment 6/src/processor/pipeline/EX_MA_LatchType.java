package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable,MA_busy;
	ControlUnit cu;
	int ALUresult;
	// int rs2;
	// int rs1;
	int rd;
	int storeData;
	
	public void setMA_busy(boolean mA_busy){
		 MA_busy=mA_busy;
	}
	public boolean isMA_busy() {
		return MA_busy;
	}
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		MA_busy = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getALUResult()
	{
		return ALUresult;
	}

	public void setALUresult(int aluresult)
	{
		this.ALUresult = aluresult;
	}

	// public void setRs2(int rs_2)
	// {
	// 	this.rs2 = rs_2;
	// }

	// public int getRs2()
	// {
	// 	return rs2;
	// }

	public void setStoreData(int storeData)
	{
		this.storeData = storeData;
	}

	public int getStoreData()
	{
		return storeData;
	}

	public void setRd(int rd)
	{
		this.rd = rd;
	}

	public int getRd()
	{
		return rd;
	}

	public void setControlUnit(ControlUnit cu) {
		this.cu = cu;
	}

	public ControlUnit getControlUnit() {
		return cu;
	}

	// public void printState()
	// {  
	// 	System.out.println("rd = " + rd);
	// 	System.out.println("rs2 = " + rs2);
	// 	System.out.println("rs1 = " + rs1);
	// 	System.out.println("ALUresult = " + ALUresult);
	// }

}