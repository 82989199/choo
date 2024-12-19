package l1j.server.server.model.item.function;

import l1j.server.server.model.Ability;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

public class WisdomPotion
  extends L1ItemInstance
{
  public WisdomPotion(L1Item item)
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
    if ((pc.isWizard()) || (pc.isBlackwizard())) {
      useWisdomPotion(pc, itemId);
    } else {
      pc.sendPackets(new S_ServerMessage(79));
    }
    pc.getInventory().removeItem(item, 1);
  }
  
  private static void useWisdomPotion(L1PcInstance pc, int item_id)
  {
    pc.cancelAbsoluteBarrier();
    pc.cancelMoebius();
    
    int time = 0;
    if ((item_id == 40016) || (item_id == 30089)) {
      time = 300;
    } else if (item_id == 140016) {
      time = 360;
    } else if (item_id == 210113) {
      time = 1000;
    }
    if (!pc.hasSkillEffect(1004))
    {
      pc.getAbility().addSp(2);
      pc.addMpr(2);
    }
    pc.setSkillEffect(1004, time * 1000);
    pc.sendPackets(new S_SkillIconWisdomPotion(time), true);
    pc.sendPackets(new S_SkillSound(pc.getId(), 750), true);
    Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 750), true);
  }
}
