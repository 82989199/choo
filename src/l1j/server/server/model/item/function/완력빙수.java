package l1j.server.server.model.item.function;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

public class 완력빙수
  extends L1ItemInstance
{
  public 완력빙수(L1Item item)
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
    완력빙수1(pc, itemId);
    pc.getInventory().removeItem(item, 1);
  }
  
  private static void 완력빙수1(L1PcInstance pc, int item_id)
  {
    pc.cancelAbsoluteBarrier();
    pc.cancelMoebius();
    
    if (pc.hasSkillEffect(L1SkillId.민첩빙수)) {
		pc.removeSkillEffect(L1SkillId.민첩빙수);
	} else if (pc.hasSkillEffect(L1SkillId.지식빙수)) {
		pc.removeSkillEffect(L1SkillId.지식빙수);
	} else if (pc.hasSkillEffect(L1SkillId.완력빙수)) {
		pc.removeSkillEffect(L1SkillId.완력빙수);
	}
	pc.setSkillEffect(L1SkillId.완력빙수, 900000);
	pc.addHitup(5);
	pc.addDmgup(3);
	pc.getAbility().addAddedStr(1);
	pc.sendPackets(new S_OwnCharStatus2(pc));
	pc.sendPackets(new S_SkillSound(pc.getId(), 7954), true);
	Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7954), true);
  }
}
