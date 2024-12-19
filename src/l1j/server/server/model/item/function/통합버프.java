package l1j.server.server.model.item.function;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;

import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL4;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL6;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

public class 통합버프
  extends L1ItemInstance
{
  public 통합버프(L1Item item)
  {
    super(item);
  }
  
  public static void checkCondition(L1PcInstance pc, L1ItemInstance item)
  {
    int itemId = item.getItemId();
    if (pc.hasSkillEffect(71))
    {
      pc.sendPackets(new S_ServerMessage(698), true);
      return;
    }
    if ((pc.hasSkillEffect(157)) || 
      (pc.hasSkillEffect(33)) || 
      (pc.hasSkillEffect(10101)) || 
      (pc.hasSkillEffect(30007)) || 
      (pc.hasSkillEffect(30002)) || 
      (pc.hasSkillEffect(30006)) || 
      (pc.hasSkillEffect(40007)) || 
      (pc.hasSkillEffect(30081)) || 
      (pc.hasSkillEffect(22055)) || 
      (pc.hasSkillEffect(22025)) || 
      (pc.hasSkillEffect(22026)) || 
      (pc.hasSkillEffect(22027)) || 
      (pc.hasSkillEffect(30004)) || 
      (pc.hasSkillEffect(87)) || 
      (pc.hasSkillEffect(208)) || 
      (pc.hasSkillEffect(103)) || 
      (pc.hasSkillEffect(212)) || 
      (pc.hasSkillEffect(123))) {
      return;
    }
    통합(pc, itemId);
    pc.getInventory().removeItem(item, 1);
  }
  
  private static void 통합(L1PcInstance pc, int item_id)
  {
    pc.cancelAbsoluteBarrier();
    pc.cancelMoebius();
    
    
    int time = 1800;
	int scroll = 0;
	int[] allBuffSkill = { 14, 26, 42, 54, 48, 79, 160, 206, 211, 216, 4914,50007, 37};
	pc.setBuffnoch(1);
	L1SkillUse l1skilluse = null;
	l1skilluse = new L1SkillUse();
	for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
				L1SkillUse.TYPE_GMBUFF);
	}
	if (pc.hasSkillEffect(L1SkillId.God_buff))
		pc.removeSkillEffect(L1SkillId.God_buff);

	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL);
		pc.addMaxHp(-50);
		pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		if (pc.isInParty()) {
			// TODO 파티 프로토
			pc.getParty().refreshPartyMemberStatus(pc);
		}
		pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
	}
	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL2)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL2);
		pc.addMaxMp(-40);
		pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
	}
	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL3)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL3);
		pc.addDmgup(-3);
		pc.addHitup(-3);
		pc.getAbility().addSp(-3);
		pc.sendPackets(new S_SPMR(pc));
	}
	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL4)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL4);
		pc.getAbility().addSp(-3);
		pc.addBaseMagicHitUp(-5);
		pc.getResistance().addcalcPcDefense(-3);
		pc.sendPackets(new S_SPMR(pc));
	}
	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL5)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL5);
		pc.addBowDmgup(-3);
		pc.addBowHitup(-5);
		pc.getResistance().addcalcPcDefense(-3);
	}
	
	if (pc.hasSkillEffect(STATUS_CASHSCROLL6)) {
		pc.killSkillEffectTimer(STATUS_CASHSCROLL6);
		pc.addDmgRate(-3);
		pc.addHitup(-5);
		pc.getResistance().addcalcPcDefense(-3);
	}
	
	
	
	if(pc.isElf()){
		pc.get_skill().start_dex_ice();
		scroll = 16552;
		pc.addBowDmgup(3);
		pc.addBowHitup(5);
		pc.getResistance().addcalcPcDefense(3);
	}else if(pc.isWizard()){
		pc.get_skill().start_int_ice();
		scroll = 16553;
		pc.getAbility().addSp(3);
		pc.addBaseMagicHitUp(5);
		pc.getResistance().addcalcPcDefense(3);
		pc.sendPackets(new S_SPMR(pc));
	}else{
		pc.get_skill().start_str_ice();
		scroll = 16551;
		pc.addDmgRate(3);
		pc.addHitup(5);
		pc.getResistance().addcalcPcDefense(3);
	}
	pc.sendPackets(new S_SkillSound(pc.getId(), scroll));
	pc.broadcastPacket(new S_SkillSound(pc.getId(), scroll));
	pc.setSkillEffect(scroll, time * 1000);
	if (pc.getWeapon() != null) {
		if (pc.getWeapon().getItem().getType1() != 20) {
			l1skilluse.handleCommands(pc, 48, pc.getId(), pc.getX(), pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
		}else{
			l1skilluse.handleCommands(pc, 149, pc.getId(), pc.getX(), pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
		}
	}
	pc.sendPackets(new S_SkillSound(pc.getId(), 830));
	pc.curePoison();

  }
}
