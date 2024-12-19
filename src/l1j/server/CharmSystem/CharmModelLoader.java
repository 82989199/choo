package l1j.server.CharmSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.CharmSystem.Model.Attack.Atk_ArtOfDoppelganger;
import l1j.server.CharmSystem.Model.Attack.Atk_Disease;
import l1j.server.CharmSystem.Model.Attack.Atk_HPDrain;
import l1j.server.CharmSystem.Model.Attack.Atk_Hold;
import l1j.server.CharmSystem.Model.Attack.Atk_MPDrain;
import l1j.server.CharmSystem.Model.Attack.Atk_NormalAttack;
import l1j.server.CharmSystem.Model.Attack.Atk_NormalRangeAttack;
import l1j.server.CharmSystem.Model.Attack.Atk_Silence;
import l1j.server.CharmSystem.Model.Attack.Atk_TargetWideAttack;
import l1j.server.CharmSystem.Model.Attack.Atk_TurnUndead;
import l1j.server.CharmSystem.Model.Attack.Atk_WideAttack;
import l1j.server.CharmSystem.Model.Defence.Def_Dmg_Mirror;
import l1j.server.CharmSystem.Model.Defence.Def_HpRegen;
import l1j.server.CharmSystem.Model.Defence.Def_MpRegen;
import l1j.server.CharmSystem.Model.Defence.Def_ProbReduction;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

public class CharmModelLoader {
	private static CharmModelLoader _instance;
	public static CharmModelLoader getInstance(){
		if(_instance == null)
			_instance = new CharmModelLoader();
		return _instance;
	}
	
	public static void reload(){
		CharmModelLoader tmp = _instance;
		_instance = new CharmModelLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	private FastMap<Integer, CharmSkillModel> _models_atk;
	private FastMap<Integer, CharmSkillModel> _models_def;
	
	private CharmModelLoader(){
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			con 		= L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("select * from charm_item_model");
			rs			= pstm.executeQuery();
			int rows	= SQLUtil.calcRows(rs);
			_models_atk = new FastMap<Integer, CharmSkillModel>(rows);
			_models_def = new FastMap<Integer, CharmSkillModel>(rows);
			while(rs.next()){
				CharmSkillModel model = parseType(rs.getString("type"));
				if(model == null)
					continue;
				
				model.itemId 			= rs.getInt("item_id");
				model.condition			= rs.getInt("condition");
				model.d_prob			= rs.getInt("default_prob");
				model.e_prob			= rs.getInt("enchant_prob");
				model.s_prob			= parseStat(rs.getString("stat_prob"));
				model.s_weight			= rs.getInt("stat_weight") * 0.01D;
				model.min_val			= rs.getInt("min_val");
				model.max_val			= rs.getInt("max_val");
				model.stat_val			= parseStat(rs.getString("stat_val"));
				model.stat_valweight 	= rs.getInt("stat_val_weight") * 0.01D;
				model.e_val				= rs.getInt("enchant_val");
				model.e_valweight		= rs.getInt("enchant_val_weight") * 0.01D;
				model.eff_id			= rs.getInt("effect");
				model.attr_type			= parseAttr(rs.getString("attr_type"));
				model.is_magic			= rs.getBoolean("is_magic");
				
				if(model.isAttack())
					_models_atk.put(model.itemId, model);
				else
					_models_def.put(model.itemId, model);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public CharmSkillModel getAtk(int i){
		return _models_atk.get(i);
	}
	
	public CharmSkillModel getDef(int i){
		return _models_def.get(i);		
	}
	
	public void clear(){
		if(_models_atk != null){
			_models_atk.clear();
			_models_atk = null;
		}
		
		if(_models_def != null){
			_models_def.clear();
			_models_def = null;
		}
	}
	
	private int parseAttr(String type){
		if(type.equalsIgnoreCase("earth"))
			return L1Skills.ATTR_EARTH;
		else if(type.equalsIgnoreCase("fire"))
			return L1Skills.ATTR_FIRE;
		else if(type.equalsIgnoreCase("water"))
			return L1Skills.ATTR_WATER;
		else if(type.equalsIgnoreCase("wind"))
			return L1Skills.ATTR_WIND;
		return L1Skills.ATTR_NONE;
	}
	
	private int parseStat(String type){
		if(type.equalsIgnoreCase("str"))
			return 1;
		else if(type.equalsIgnoreCase("dex"))
			return 2;
		else if(type.equalsIgnoreCase("con"))
			return 3;
		else if(type.equalsIgnoreCase("wis"))
			return 4;
		else if(type.equalsIgnoreCase("intel"))
			return 5;
		else if(type.equalsIgnoreCase("cha"))
			return 6;
		return 0;
	}

	private CharmSkillModel parseType(String type){
		// 攻击类技能
		if(type.equalsIgnoreCase("攻击-单体(近距离)"))      // 공격-단일(근거리)
			return new Atk_NormalAttack();
		else if(type.equalsIgnoreCase("攻击-单体(远距离)")) // 공격-단일(원거리)
			return new Atk_NormalRangeAttack();
		else if(type.equalsIgnoreCase("攻击-广域(施法者)")) // 공격-광역(타격자)
			return new Atk_WideAttack();
		else if(type.equalsIgnoreCase("攻击-广域(受击者)")) // 공격-광역(피격자)
			return new Atk_TargetWideAttack();
		else if(type.equalsIgnoreCase("攻击-疾病"))        // 공격-디지즈
			return new Atk_Disease();
		else if(type.equalsIgnoreCase("攻击-定身"))        // 공격-홀드
			return new Atk_Hold();
		else if(type.equalsIgnoreCase("攻击-生命吸收"))    // 공격-체력흡수
			return new Atk_HPDrain();
		else if(type.equalsIgnoreCase("攻击-魔力吸收"))    // 공격-마나흡수
			return new Atk_MPDrain();
		else if(type.equalsIgnoreCase("攻击-沉默"))        // 공격-사일런스
			return new Atk_Silence();
		else if(type.equalsIgnoreCase("攻击-转生防御"))    // 공격-턴 언데드
			return new Atk_TurnUndead();
		else if(type.equalsIgnoreCase("攻击-分身术"))      // 공격-분신술
			return new Atk_ArtOfDoppelganger();

			// 防御类技能
		else if(type.equalsIgnoreCase("防御-生命恢复"))    // 방어-체력회복
			return new Def_HpRegen();
		else if(type.equalsIgnoreCase("防御-魔力恢复"))    // 방어-마나회복
			return new Def_MpRegen();
		else if(type.equalsIgnoreCase("防御-概率减免"))    // 방어-확률리덕션
			return new Def_ProbReduction();
		else if(type.equalsIgnoreCase("防御-伤害反弹"))    // 방어-대미지반사
			return new Def_Dmg_Mirror();
		return null;
	}
}
