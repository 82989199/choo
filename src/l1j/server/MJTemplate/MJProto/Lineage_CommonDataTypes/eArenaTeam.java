package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum eArenaTeam{
	TEAM_NONE(0),
	TEAM_A(1),
	TEAM_B(2),
	TEAM_C(3);
	private int value;
	eArenaTeam(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eArenaTeam v){
		return value == v.value;
	}
	public static eArenaTeam fromInt(int i){
		switch(i){
		case 0:
			return TEAM_NONE;
		case 1:
			return TEAM_A;
		case 2:
			return TEAM_B;
		case 3:
			return TEAM_C;
		default:
			return null;
		}
	}
}
