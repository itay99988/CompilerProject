package TYPES;

public abstract class TYPE
{
	/******************************/
	/*          type name         */
	/******************************/
	public String name;

	/*****************************************/
	/* isClass() - can be overriden later on */
	/*****************************************/
	public boolean isClass(){ return false;}

	/*****************************************/
	/* isArray() - can be overriden later on */
	/*****************************************/
	public boolean isArray(){ return false;}
}
