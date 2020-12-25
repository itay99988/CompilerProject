package TYPES;

import MIPS.sir_MIPS_a_lot;

public class VtableEntry 
{
	public int offset;
	public String definedInClass;
	public String funcName;
	
	public VtableEntry(int offset, String className, String funcName)
    {
		this.offset = offset;
		this.definedInClass = className;
		this.funcName = funcName;
	}
    
	public String toString()
    {
		return sir_MIPS_a_lot.getFunctionLabel(definedInClass, funcName);
	}
}