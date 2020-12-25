package TYPES;

import java.util.LinkedHashMap;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_DATA_MEMBER_LIST data_members;

	public int ObjSizeInBytes;

	public LinkedHashMap<String, VtableEntry> functionTable; //maps function name to VtableEntry
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_CLASS_DATA_MEMBER_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		
		this.ObjSizeInBytes = 0;
		this.functionTable = new LinkedHashMap<String, VtableEntry>();
		if(father != null)
			copyFunctionTable();
	}

	private void copyFunctionTable()
	{
		for(String i : father.functionTable.keySet())
		{
			VtableEntry src = father.functionTable.get(i);
			VtableEntry cpy = new VtableEntry(src.offset, src.definedInClass, src.funcName);
			this.functionTable.put(i, cpy);
		}
	}

	@Override
	public boolean isClass()
	{ 
		return true;
	}

	public boolean isDerivedFrom(TYPE_CLASS superClass)
	{
		TYPE_CLASS currType = this;

		while(currType != null)
		{
		    if(currType.name.equals(superClass.name))
		        return true;
		    currType = currType.father;
        }
        return false;
	}
}
