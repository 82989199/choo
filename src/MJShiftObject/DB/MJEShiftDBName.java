package MJShiftObject.DB;
import java.util.HashMap;

import l1j.server.server.utils.MJCommons;

public enum MJEShiftDBName{
	CHARACTERS(0, "characters"),
	CHARACTERS_BUFF(1, "character_buff"),
	CHARACTERS_CONFIG(2, "character_config"),
	CHARACTERS_ITEMS(3, "character_items"),
	CHARACTERS_SKILLS(4, "character_skills"),
	PASSIVE_USER_INFO(5, "passive_user_info"),
	CHARACTERS_SLOT_ITEMS(6, "character_slot_items"),
	CHARACTERS_TAMS(7, "character_tams"),
	CHARACTERS_BONUS(8, "tb_character_bonus"),
	ACCOUNTS(9, "accounts"),
	OBJECT_CONVERTER(10, "object_converter"),
	CHARACTERS_QUEST(11, "character_quests"),
	CHARACTERS_PICKUP(12, "character_pickups");

	private int m_idx;
	private String m_name;
	MJEShiftDBName(int idx, String name){
		m_idx = idx;
		m_name = name;
	}
	
	public static final HashMap<Integer, MJEShiftDBName> m_names;
	static{
		MJEShiftDBName[] names = MJEShiftDBName.values();
		m_names = new HashMap<Integer, MJEShiftDBName>(names.length);
		for(MJEShiftDBName name : names)
			m_names.put(name.toInt(), name);
	}
	
	public static MJEShiftDBName fromInt(int i){
		return m_names.get(i);
	}
	
	public int toInt(){
		return m_idx;
	}
	
	public String get_table_name(String server_identity){
		return MJCommons.isNullOrEmpty(server_identity) ? m_name : String.format("%s_%s", server_identity, m_name);
	}
}
