package TYPES;
import TYPES.*;
import SYMBOL_TABLE.*;

public class TYPE_CLASS_DATA_MEMBER extends TYPE
{
	public String name;
	public TYPE type; //can be method or variable
	public int offset;
	public Object value;
	
	public TYPE_CLASS_DATA_MEMBER(String name, TYPE t, int offset, Object value)
	{
		super();
		this.name = name;
		this.type = t;
		this.offset = offset;
		this.value = value;
	}
}
