package l1j.server.MJTemplate.MJProto.MainServer_Client;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum AttendanceBonusRewardType{
	AB_REWARD_NORMAL(0),
	AB_REWARD_PC_CAFE(1);
	private int value;
	AttendanceBonusRewardType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(AttendanceBonusRewardType v){
		return value == v.value;
	}
	public static AttendanceBonusRewardType fromInt(int i){
		switch(i){
		case 0:
			return AB_REWARD_NORMAL;
		case 1:
			return AB_REWARD_PC_CAFE;
		default:
			return null;
		}
	}
}
