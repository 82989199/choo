package MJShiftObject;

public enum MJEShiftObjectType {
	NONE(-1, "NONE"),
	BATTLE(0, "BATTLE"),
	TRANSFER(1, "TRANSFER");
	
	private int m_val;
	private String m_name;
	MJEShiftObjectType(int val, String name){
		m_val = val;
		m_name = name;
	}
	
	public int to_int(){
		return m_val;
	}
	
	public String to_name(){
		return m_name;
	}
	
	public boolean equals(MJEShiftObjectType type){
		return to_int() == type.to_int();
	}
	
	public static MJEShiftObjectType from_name(String name){
		for(MJEShiftObjectType type : MJEShiftObjectType.values()){
			if(type.m_name.equals(name))
				return type;
		}
		return null;
	}
	
	public static MJEShiftObjectType from_val(int val){
		for(MJEShiftObjectType type : MJEShiftObjectType.values()){
			if(type.to_int() == val)
				return type;
		}
		return null;
	}
}
