package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum Gender{
	MALE(0),
	FEMALE(1);
	private int value;
	Gender(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(Gender v){
		return value == v.value;
	}
	public static Gender fromInt(int i){
		switch(i){
		case 0:
			return MALE;
		case 1:
			return FEMALE;
		default:
			return null;
		}
	}
}
