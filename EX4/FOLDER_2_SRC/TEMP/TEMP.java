/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import IR.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP
{
	private int serial=0;
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}
	
    public String getRegName()
	{
		return "$t"+getSerialNumber();
    	//return "$t"+IR.getInstance().getAllocationTranslationMap().get(this);
    }
}
