package TYPES;

public class TYPE_CLASS_DATA_MEMBER_LIST extends TYPE {
	public TYPE_CLASS_DATA_MEMBER head;
	public TYPE_CLASS_DATA_MEMBER_LIST tail;
	
	public TYPE_CLASS_DATA_MEMBER_LIST(TYPE_CLASS_DATA_MEMBER head, TYPE_CLASS_DATA_MEMBER_LIST tail) {
		super();
		this.head = head;
		this.tail = tail;
	}
}