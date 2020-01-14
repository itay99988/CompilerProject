package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;

	/*************************/
	/* number of parameters  */
	/*************************/
	public int paramsLen;

	/*************************/
	/* class name (if any )  */
	/*************************/
	public String className; //if null - function is in global scope, otherwise it is a method
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params, int paramsLen, String className)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.paramsLen = paramsLen;
		this.className = className;
	}
}
