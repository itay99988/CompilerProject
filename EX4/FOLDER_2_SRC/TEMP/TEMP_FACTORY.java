/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP_FACTORY
{
	private int counter = 1;
	public static ZERO zero;
	
	public TEMP getFreshTEMP()
	{
		return new TEMP(counter++);
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TEMP_FACTORY instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TEMP_FACTORY() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TEMP_FACTORY getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new TEMP_FACTORY();
			zero = new ZERO(0);
		}
		return instance;
	}

	/******************************/
	/* GET NUMBER OF EXISTING TEMP */
	/******************************/
	public int getTempsCount() 
	{ 
		return counter;
	}
}