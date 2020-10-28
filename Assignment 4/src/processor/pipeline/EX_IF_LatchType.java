package processor.pipeline;

public class EX_IF_LatchType {
	
	int branchPC;
	boolean isBranchTaken;

	public EX_IF_LatchType()
	{
		isBranchTaken = false;
	}

	public void setbranchPC(int branchPC)
	{
		this.branchPC = branchPC;
	}

	public int getbranchPC()
	{
		return branchPC;
	}

	public void setIsBranchTaken(boolean isbranchtaken)
	{
		this.isBranchTaken = isbranchtaken;
	}

	public boolean getIsBranchTaken()
	{
		return isBranchTaken;
	}

	public void printState()
	{  
		System.out.println("branchPC = " + branchPC);
		System.out.println("isBranchTaken = " + isBranchTaken);
	}

}
