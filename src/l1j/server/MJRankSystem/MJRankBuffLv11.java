package l1j.server.MJRankSystem;

import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class MJRankBuffLv11 extends MJRankBuff{
	private ProtoOutputStream _onBuffs_str;
	private ProtoOutputStream _onBuffs_dex;
	private ProtoOutputStream _onBuffs_int;
	
	private ProtoOutputStream _offBuffs_str;
	private ProtoOutputStream _offBuffs_dex;
	private ProtoOutputStream _offBuffs_int;
	
	public MJRankBuffLv11(){
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.NEW);
		noti.set_spell_id(L1SkillId.RANK_BUFF_11_STR);
		noti.set_duration(1);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(7096);
		noti.set_off_icon_id(7096);
		noti.set_icon_priority(3);
		noti.set_tooltip_str_id(5148);
		noti.set_new_str_id(0);
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		_onBuffs_str = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.set_spell_id(L1SkillId.RANK_BUFF_11_DEX);
		noti.set_on_icon_id(7096);
		noti.set_off_icon_id(7096);
		noti.set_tooltip_str_id(5149);
		_onBuffs_dex = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.set_spell_id(L1SkillId.RANK_BUFF_11_INT);
		noti.set_on_icon_id(7096);
		noti.set_off_icon_id(7096);
		noti.set_tooltip_str_id(5150);
		_onBuffs_int = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		
		noti.dispose();
		
		noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(L1SkillId.RANK_BUFF_11_STR);
		noti.set_off_icon_id(0);
		noti.set_end_str_id(0);
		_offBuffs_str = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);

		noti.set_spell_id(L1SkillId.RANK_BUFF_11_DEX);
		_offBuffs_dex = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
	
		noti.set_spell_id(L1SkillId.RANK_BUFF_11_INT);
		_offBuffs_int = noti.writeTo(MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		noti.dispose();
		
	}
	
	@Override
	public void onBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		MJEClassesType type = MJEClassesType.fromGfx(pc.getClassId());
		switch(type.toInt()){
		case 0:
		case 1:
		case 4:
		case 5:
		case 7:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_11_STR, -1);
			pc.sendPackets(_onBuffs_str, false);
			pc.getAbility().addAddedStr(1);
			break;
			
		case 2:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_11_DEX, -1);
			pc.sendPackets(_onBuffs_dex, false);
			pc.getAbility().addAddedDex(1);
			break;

		case 3:
		case 6:
			pc.setSkillEffect(L1SkillId.RANK_BUFF_11_INT, -1);
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
		
		L1ItemInstance item = ItemTable.getInstance().createItem(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		if(pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK && !pc.getInventory().checkItem(MJRankLoadManager.MRK_TOPPROTECTION_ID))
			pc.getInventory().storeItem(item);
	}

	@Override
	public void offBuff(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if(pc == null)
			return;
		
		if(pc.hasSkillEffect(L1SkillId.RANK_BUFF_11_STR)){
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_11_STR);
			pc.sendPackets(_offBuffs_str, false);
			pc.getAbility().addAddedStr(-1);
		}
		if(pc.hasSkillEffect(L1SkillId.RANK_BUFF_11_DEX)){
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_11_DEX);
			pc.sendPackets(_offBuffs_dex, false);
			pc.getAbility().addAddedDex(-1);
		}
		if(pc.hasSkillEffect(L1SkillId.RANK_BUFF_11_INT)){
			pc.killSkillEffectTimer(L1SkillId.RANK_BUFF_11_INT);
			pc.sendPackets(_offBuffs_int, false);
			pc.getAbility().addAddedInt(-1);
		}
		
		pc.addMaxHp(-200);//HP
		pc.getAC().addAc(3);
		pc.set_pvp_defense(-2);//PVP대미지 리덕
		pc.addDmgupByArmor(-1);
		pc.addBowDmgupByArmor(-1);
		pc.sendPackets(new S_OwnCharStatus(pc));
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
	}
}
