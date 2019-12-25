package TYPES;
import TYPES.*;
import SYMBOL_TABLE.*;

public class TYPE_CLASS_DATA_MEMBER extends TYPE{
	public String name;
	public TYPE type; //can be method or variable
	
	public TYPE_CLASS_DATA_MEMBER(String name, TYPE t){
		super();
		this.name = name;
		this.type = t;
	}
}
