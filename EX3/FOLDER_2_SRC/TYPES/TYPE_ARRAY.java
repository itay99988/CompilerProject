package TYPES;

public class TYPE_ARRAY extends TYPE
{
    public int size;

    /****************/
    /* CTROR(S) ... */
    /****************/

    public TYPE_ARRAY(String name, int size)
    {
        this.name = name;
	    this.size = size;
    }

    @Override
    public boolean isArray()
    { 
        return true;
    }
}