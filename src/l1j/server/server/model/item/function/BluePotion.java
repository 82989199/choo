package l1j.server.server.model.item.function;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

public class BluePotion
  extends L1ItemInstance
{
  public BluePotion(L1Item item)
  {
    super(item);
  }
  
  public static void checkCondition(L1PcInstance pc, L1ItemInstance item)
  {
    int itemId = item.getItemId();
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
    if (pc.hasSkillEffect(71))
    {
      pc.sendPackets(new S_ServerMessage(698), true);
      return;
    }
    useBluePotion(pc, itemId);
    pc.getInventory().removeItem(item, 1);
  }
  
  private static void useBluePotion(L1PcInstance pc, int item_id)
  {
    pc.cancelAbsoluteBarrier();
    pc.cancelMoebius();
    
    int time = 0;
    int type = 0;
    switch (item_id)
    {
    case 30083: 
    case 40015: 
    case 40736: 
      type = 1002;
      time = 600;
      break;
    case 41142: 
      type = 20082;
      time = 300;
      break;
    case 140015: 
      type = 1002;
      time = 700;
      break;
    case 210114: 
      type = 1002;
      time = 1800;
    }
    if (pc.hasSkillEffect(1002)) {
      pc.removeSkillEffect(1002);
    }
    if (pc.hasSkillEffect(20082)) {
      pc.removeSkillEffect(20082);
    }
    pc.sendPackets(new S_SkillIconGFX(34, time, true));
    pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
    Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
    pc.setSkillEffect(type, time * 1000);
    pc.sendPackets(new S_ServerMessage(1007), true);
  }
}
