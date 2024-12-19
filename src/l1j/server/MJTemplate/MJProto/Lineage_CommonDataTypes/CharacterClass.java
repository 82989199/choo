package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum CharacterClass{
	PRINCE(0),
	KNIGHT(1),
	ELF(2),
	MAGICIAN(3),
	DARKELF(4),
	DRAGON_KNIGHT(5),
	ILLUSIONIST(6),
	WARRIOR(7),
	CLASS_ALL(255);
	private int value;
	CharacterClass(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(CharacterClass v){
		return value == v.value;
	}
	public static CharacterClass fromInt(int i){
		switch(i){
		case 0:
			return PRINCE;
		case 1:
			return KNIGHT;
		case 2:
			return ELF;
		case 3:
			return MAGICIAN;
		case 4:
			return DARKELF;
		case 5:
			return DRAGON_KNIGHT;
		case 6:
			return ILLUSIONIST;
		case 7:
			return WARRIOR;
		case 255:
			return CLASS_ALL;
		default:
			return null;
		}
	}
}
