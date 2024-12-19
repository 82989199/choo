package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.SkillCheck;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

public class SkillsTable {

	private static Logger _log = Logger.getLogger(SkillsTable.class.getName());

	private static SkillsTable _instance;

	private final Map<Integer, L1Skills> _skills = new HashMap<Integer, L1Skills>();

	private final boolean _initialized;

	public static SkillsTable getInstance() {
		if (_instance == null) {
			_instance = new SkillsTable();
		}
		return _instance;
	}

	private SkillsTable() {
		_initialized = true;
		RestoreSkills();
	}
	
	public static void reload() {
		SkillsTable oldInstance = _instance;
		_instance = new SkillsTable();
		oldInstance._skills.clear();
		oldInstance = null;
	}

	private void RestoreSkills() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM skills");
			rs = pstm.executeQuery();
			FillSkillsTable(rs);

		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating skills table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void FillSkillsTable(ResultSet rs) throws SQLException {
		L1Skills l1skills = null;
		while (rs.next()) {
			l1skills = new L1Skills();
			int skill_id = rs.getInt("skill_id");
			l1skills.setSkillId(skill_id);
			l1skills.setName(rs.getString("name"));
			l1skills.setSkillLevel(rs.getInt("skill_level"));
			l1skills.setSkillNumber(rs.getInt("skill_number"));
			l1skills.setMpConsume(rs.getInt("mpConsume"));
			l1skills.setHpConsume(rs.getInt("hpConsume"));
			l1skills.setItemConsumeId(rs.getInt("itemConsumeId"));
			l1skills.setItemConsumeCount(rs.getInt("itemConsumeCount"));
			l1skills.setReuseDelay(rs.getInt("reuseDelay"));
			l1skills.setBuffDuration(rs.getInt("buffDuration"));
			l1skills.setTarget(rs.getString("target")); //none불명확한대상, buff버프스킬, attack공격스킬
			l1skills.setTargetTo(rs.getInt("target_to")); //0나자신, 1다른캐릭, 2NPC, 3다른 캐릭터+NPC (1 + 2), 4혈맹, 8파티, 16펫, 32위치(지면클릭)
			l1skills.setDamageValue(rs.getInt("damage_value")); //값이 높을수록 대미지 쎄짐
			l1skills.setDamageDice(rs.getInt("damage_dice")); // 랜덤 값으로 6이라면 1~6사이의 랜덤한 대미지를 준다
			l1skills.setDamageDiceCount(rs.getInt("damage_dice_count")); // 대미지를 더욱 랜덤하게, damage_dice가 6이고 damage_dice_count가 2라면 2개의 6면체 주사위를 굴려 피해량 계산
			l1skills.setProbabilityValue(rs.getInt("probability_value")); // 값이 높을수록 스킬 성공확률 올라감, 30이라면 30%
			l1skills.setProbabilityDice(rs.getInt("probability_dice")); // 랜덤 성공 확률값, 6이라면 1~6사이의 랜덤값으로 성공확률을 결정
			l1skills.setAttr(rs.getInt("attr")); // 0무속성, 1땅, 2불, 4물, 8바람, 16빛
			l1skills.setType(rs.getInt("type")); // 1확률계, 2버프, 4저주, 8즉사(미사용), 16회복, 32부활, 64공격, 128기타 
			l1skills.setLawful(rs.getInt("lawful"));
			l1skills.setRanged(rs.getInt("ranged")); // 스킬의 사정거리
			l1skills.setArea(rs.getInt("area")); // 스킬의 공격 범위
			l1skills.setIsThrough(rs.getInt("through")); // 장애물 통과여부
			l1skills.setId(rs.getInt("id"));
			l1skills.setNameId(rs.getString("nameid"));
			l1skills.setActionId(rs.getInt("action_id"));
			l1skills.setActionId2(rs.getInt("action_id2"));
			l1skills.setActionId3(rs.getInt("action_id3"));
			l1skills.setCastGfx(rs.getInt("castgfx"));
			l1skills.setCastGfx2(rs.getInt("castgfx2"));
			l1skills.setCastGfx3(rs.getInt("castgfx3"));
			l1skills.setSysmsgIdHappen(rs.getInt("sysmsgID_happen"));
			l1skills.setSysmsgIdStop(rs.getInt("sysmsgID_stop"));
			l1skills.setSysmsgIdFail(rs.getInt("sysmsgID_fail"));

			_skills.put(new Integer(skill_id), l1skills);
		}
		_log.config("스킬 " + _skills.size() + "건 로드");
	}

	public void spellMastery(int playerobjid, int skillid, String skillname, int active, int time) {

		if (spellCheck(playerobjid, skillid)) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(playerobjid);
		if (pc != null) {
			pc.setSkillMastery(skillid);
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
			.prepareStatement("INSERT INTO character_skills SET char_obj_id=?, skill_id=?, skill_name=?, is_active=?, activetimeleft=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			pstm.setString(3, skillname);
			pstm.setInt(4, active);
			pstm.setInt(5, time);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}		
		
		SkillCheck.getInstance().AddSkill(playerobjid, skillid);//

	}

	public void spellLost(int playerobjid, int skillid) {
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(playerobjid);
		if (pc != null) {
			pc.removeSkillMastery(skillid);
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
			.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND skill_id=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
		SkillCheck.getInstance().DelSkill(playerobjid, skillid);

	}

	public boolean spellCheck(int playerobjid, int skillid) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
			.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?");
			pstm.setInt(1, playerobjid);
			pstm.setInt(2, skillid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				ret = true;
			} else {
				ret = false;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return ret;
	}

	public boolean isInitialized() {
		return _initialized;
	}

	public L1Skills getTemplate(int i) {
		return _skills.get(new Integer(i));
	}
	
	public L1Skills getTemplateByItem(int itemid) {
		int skillid = 0;
		switch (itemid) {
		case 210121: skillid = 225; break;
		case 210122: skillid = 226; break;
		case 210123: skillid = 228; break;
		case 210124: skillid = 229; break;
		case 210125: skillid = 230; break;
		case 3000094: skillid = 231; break;
		//case 210126: skillid = 233; break;
		case 210127: skillid = 234; break;
		case 210128: skillid = 235; break;
		case 210126: skillid = 236; break;
		case 210129: skillid = 237; break;
		case 210130: skillid = 238; break;
		case 210131: skillid = 239; break;
		case 210132: skillid = 240; break;
		}
		if (skillid == 0) return null;
		return _skills.get(new Integer(skillid));
	}
	
	  public L1Skills findBySkillId(int skillId) {
		    return (L1Skills)_skills.get(Integer.valueOf(skillId));
		 }

}
