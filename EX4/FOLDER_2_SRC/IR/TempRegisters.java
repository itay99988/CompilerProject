package IR;

public enum TempRegisters
{
	t0,
	t1,
	t2,
	t3,
	t4,
	t5,
	t6,
	t7,
	t8,
	t9;
		
	public String toMIPSString()
	{
		return "$" + this.toString();
	}
		
}