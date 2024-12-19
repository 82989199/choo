package l1j.server.MJRankSystem;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class MJRankBuffLv7 extends MJRankBuff{
	private ProtoOutputStream _onBuffs;
	private ProtoOutputStream _offBuffs;
	
	public MJRankBuffLv7(){
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.NEW);
		noti.set_spell_id(L1SkillId.RANK_BUFF_7);
		noti.set_duration(1);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(7093);
		noti.set_off_icon_id(7093);
		noti.set_icon_priority(3);
		noti.set_tooltip_str_id(5142);
		noti.set_new_str_id(0);
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		_onBuffs = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		noti.dispose();
		
		noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(L1SkillId.RANK_BUFF_7);
		noti.set_off_icon_id(0);
		noti.set_end_str_id(0);
		_offBuffs = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		noti.dispose();
	}
	
	@Override
	public void onBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		pc.setSkillEffect(L1SkillId.RANK_BUFF_7, -1);
		pc.sendPackets(_onBuffs, false);
		pc.addMaxHp(200);//HP
		pc.getAC().addAc(-3);
		pc.set_pvp_defense(1);//PVP대미지 리덕
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	@Override
	public void offBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_7);
		pc.sendPackets(_offBuffs, false);		
		pc.addMaxHp(-200);//HP
		pc.getAC().addAc(3);
		pc.set_pvp_defense(-1);//PVP대미지 리덕
		pc.sendPackets(new S_OwnCharStatus(pc));
	}
}
