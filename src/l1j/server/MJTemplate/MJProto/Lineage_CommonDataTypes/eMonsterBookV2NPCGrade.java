package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum eMonsterBookV2NPCGrade{
	NG_NORMAL(1),
	NG_RARE(2),
	NG_BOSS(3);
	private int value;
	eMonsterBookV2NPCGrade(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eMonsterBookV2NPCGrade v){
		return value == v.value;
	}
	public static eMonsterBookV2NPCGrade fromInt(int i){
		switch(i){
		case 1:
			return NG_NORMAL;
		case 2:
			return NG_RARE;
		case 3:
			return NG_BOSS;
		default:
			return null;
		}
	}
}
