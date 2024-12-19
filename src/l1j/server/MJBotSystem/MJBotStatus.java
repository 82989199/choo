package l1j.server.MJBotSystem;

public enum MJBotStatus {
	SETTING(0),
	WALK(1),
	ATTACK(2),
	DEAD(3),
	CORPSE(4),
	SPAWN(5),
	ESCAPE(6),
	PICKUP(7),
	SHOP(8),
	SEARCHMOVE(9);
	
	private int _sts;
	MJBotStatus(int i){
		_sts = i;
	}
	
	public int toInt(){
		return _sts;
	}
	
	public static MJBotStatus fromInt(int i){
		MJBotStatus[] array = MJBotStatus.values();
		for(int idx=0; idx<array.length; idx++){
			if(array[idx]._sts == i)
				return array[idx];
		}
		return null;
	}
}
