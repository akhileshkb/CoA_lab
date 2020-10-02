package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int ALUresult;
	int rs2;
	int rs1;
	int rd;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
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

	public void setRs2(int rs_2)
	{
		this.rs2 = rs_2;
	}

	public int getRs2()
	{
		return rs2;
	}

	public void setRs1(int rs_1)
	{
		this.rs1 = rs_1;
	}

	public int getRs1()
	{
		return rs1;
	}

	public void setRd(int rd)
	{
		this.rd = rd;
	}

	public int getRd()
	{
		return rd;
	}

	public void printState()
	{  
		System.out.println("rd = " + rd);
		System.out.println("rs2 = " + rs2);
		System.out.println("rs1 = " + rs1);
		System.out.println("ALUresult = " + ALUresult);
	}

}
