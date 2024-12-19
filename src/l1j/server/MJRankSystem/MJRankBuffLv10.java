package l1j.server.MJRankSystem;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class MJRankBuffLv10 extends MJRankBuff{
	private ProtoOutputStream _onBuffs_str;
	private ProtoOutputStream _onBuffs_dex;
	private ProtoOutputStream _onBuffs_int;
	
	private ProtoOutputStream _offBuffs_str;
	private ProtoOutputStream _offBuffs_dex;
	private ProtoOutputStream _offBuffs_int;
	
	public MJRankBuffLv10(){
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.NEW);
		noti.set_spell_id(L1SkillId.RANK_BUFF_10_STR);
		noti.set_duration(1);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(7095);
		noti.set_off_icon_id(7095);
		noti.set_icon_priority(3);
		noti.set_tooltip_str_id(5145);
		noti.set_new_str_id(0);
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		_onBuffs_str = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.set_spell_id(L1SkillId.RANK_BUFF_10_DEX);
		noti.set_on_icon_id(7095);
		noti.set_off_icon_id(7095);
		noti.set_tooltip_str_id(5146);
		_onBuffs_dex = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.set_spell_id(L1SkillId.RANK_BUFF_10_INT);
		noti.set_on_icon_id(7095);
		noti.set_off_icon_id(7095);
		noti.set_tooltip_str_id(5147);
		_onBuffs_int = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.dispose();
		
		noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(L1SkillId.RANK_BUFF_10_STR);
		noti.set_off_icon_id(0);
		noti.set_end_str_id(0);
		_offBuffs_str = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);

		noti.set_spell_id(L1SkillId.RANK_BUFF_10_DEX);
		_offBuffs_dex = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
	
		noti.set_spell_id(L1SkillId.RANK_BUFF_10_INT);
		_offBuffs_int = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		noti.dispose();
	}
	
	@Override
	public void onBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		MJEClassesType type = MJEClassesType.fromInt(rnk.get_class());
		switch(type.toInt()){
		case 0:
		case 1:
		case 4:
		case 5:
		case 7:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_10_STR, -1);
			pc.sendPackets(_onBuffs_str, false);
			pc.getAbility().addAddedStr(1);
			break;
			
		case 2:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_10_DEX, -1);
			pc.sendPackets(_onBuffs_dex, false);
			pc.getAbility().addAddedDex(1);
			break;

		case 3:
		case 6:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_10_INT, -1);
			pc.sendPackets(_onBuffs_int, false);
			pc.getAbility().addAddedInt(1);
			break;	
		
		}
		
		pc.addMaxHp(200);
		pc.getAC().addAc(-3);
		pc.set_pvp_defense(2);
		pc.addDmgupByArmor(1);
		pc.addBowDmgupByArmor(1);
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	@Override
	public void offBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		MJEClassesType type = MJEClassesType.fromInt(rnk.get_class());
		switch(type.toInt()){
		case 0:
		case 1:
		case 4:
		case 5:
		case 7:
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_10_STR);
			pc.sendPackets(_offBuffs_str, false);
			pc.getAbility().addAddedStr(-1);
			break;
			
		case 2:
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_10_DEX);
			pc.sendPackets(_offBuffs_dex, false);
			pc.getAbility().addAddedDex(-1);
			break;

		case 3:
		case 6:
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_10_INT);
			pc.sendPackets(_offBuffs_int, false);
			pc.getAbility().addAddedInt(-1);
			break;	
		
		}
		
		pc.addMaxHp(-200);//HP
		pc.getAC().addAc(3);
		pc.set_pvp_defense(-2);//PVP대미지 리덕
		pc.addDmgupByArmor(-1);
		pc.addBowDmgupByArmor(-1);
		pc.sendPackets(new S_OwnCharStatus(pc));
	}
}
