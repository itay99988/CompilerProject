package IR;

public enum SystemRegisters
{
	ra,
	sp,
	fp,
	v0,
	v1;
		
	public String toMIPSString()
	{
		return "$" + this.toString();
	}
		
}