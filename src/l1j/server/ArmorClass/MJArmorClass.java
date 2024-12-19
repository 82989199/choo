package l1j.server.ArmorClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.utils.IntRange;

public class MJArmorClass {
	private static ArrayList<MJArmorClass> m_armor_classes;
	public static void do_load(){
		ArrayList<MJArmorClass> armor_classes = new ArrayList<MJArmorClass>();
		Selector.exec("select * from armor_class", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJArmorClass o = newInstance(rs);
					armor_classes.add(o);
				}
			}
		});
		m_armor_classes = armor_classes;
	}
	
	public static MJArmorClass find_armor_class(int ac){
		for(MJArmorClass armor_class : m_armor_classes){
			if(IntRange.includes(ac, armor_class.get_armor_min(), armor_class.get_armor_max()))
				return armor_class;
		}
		return null;
	}

	private static MJArmorClass newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_armor_min(rs.getInt("armor_min"))
				.set_armor_max(rs.getInt("armor_max"))
				.set_to_pc_dodge(rs.getInt("to_pc_dodge"))
				.set_to_pc_reduction(rs.getInt("to_pc_reduction"))
				.set_to_npc_dodge(rs.getInt("to_npc_dodge"))
				.set_to_npc_reduction(rs.getInt("to_npc_reduction"));
	}

	private static MJArmorClass newInstance(){
		return new MJArmorClass();
	}

	private int m_armor_min;
	private int m_armor_max;
	private int m_to_pc_dodge;
	private int m_to_pc_reduction;
	private int m_to_npc_dodge;
	private int m_to_npc_reduction;
	private MJArmorClass(){}

	public MJArmorClass set_armor_min(int armor_min){
		m_armor_min = armor_min;
		return this;
	}
	public MJArmorClass set_armor_max(int armor_max){
		m_armor_max = armor_max;
		return this;
	}
	public MJArmorClass set_to_pc_dodge(int to_pc_dodge){
		m_to_pc_dodge = to_pc_dodge;
		return this;
	}
	public MJArmorClass set_to_pc_reduction(int to_pc_reduction){
		m_to_pc_reduction = to_pc_reduction;
		return this;
	}
	public MJArmorClass set_to_npc_dodge(int to_npc_dodge){
		m_to_npc_dodge = to_npc_dodge;
		return this;
	}
	public MJArmorClass set_to_npc_reduction(int to_npc_reduction){
		m_to_npc_reduction = to_npc_reduction;
		return this;
	}
	public int get_armor_min(){
		return m_armor_min;
	}
	public int get_armor_max(){
		return m_armor_max;
	}
	public int get_to_pc_dodge(){
		return m_to_pc_dodge;
	}
	public int get_to_pc_reduction(){
		return m_to_pc_reduction;
	}
	public int get_to_npc_dodge(){
		return m_to_npc_dodge;
	}
	public int get_to_npc_reduction(){
		return m_to_npc_reduction;
	}

}

