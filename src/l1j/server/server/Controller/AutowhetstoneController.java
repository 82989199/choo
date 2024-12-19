package l1j.server.server.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
//import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

public class AutowhetstoneController
  implements Runnable
{
  private static AutowhetstoneController _instance;
  
  public static AutowhetstoneController getInstance()
  {
    if (_instance == null) {
      _instance = new AutowhetstoneController();
    }
    return _instance;
  }
  
  public AutowhetstoneController()
  {
    GeneralThreadPool.getInstance().execute(this);
  }
  
  private Collection<L1PcInstance> list = null;
  
  private boolean 설정(L1PcInstance pc) {
		if (pc == null || pc.isDead()  || pc.is_자동숫돌사용() == false || pc.isFishing() == true || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		return false;
	}
  
  public void run()
  {
    try
    {
      for (;;)
      {
        this.list = L1World.getInstance().getAllPlayers();
        for (L1PcInstance pc : this.list) {
        	if (!설정(pc) && (pc.getInventory().checkItem(45450) /*||pc.getInventory().checkItem(45451)  || pc.getInventory().checkItem(45452) || pc.getInventory().checkItem(45453)*/)) {
            try
            {
              doAutowhetstoneAction(pc);
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
        }
        try
        {
          Thread.sleep(300L);
        }
        catch (Exception localException4) {}
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        Thread.sleep(300L);
      }
      catch (Exception localException3) {}
    }
  }
  
  private void doAutowhetstoneAction(L1PcInstance pc)
  {
    if (!pc.is_자동숫돌사용()) {
      return;
    }
    if ((pc.get_teleport()) || (pc.isDead())) {
      return;
    }
    if (pc.isFishing()) {
      return;
    }
    if (pc.isPrivateShop() || pc.isAutoClanjoin() /*|| pc.isAutoFish()*/) {
      return;
    }
    if (pc.getMapId() == 5166) {
      return;
    }
    if (pc.isPinkName())
    {
      pc.set_자동숫돌사용(false);
      pc.sendPackets(new S_SystemMessage("\\aE자동숫돌 사용이 해제되었습니다."));
      return;
    }
    if ((pc.hasSkillEffect(87)) || 
      (pc.hasSkillEffect(30081)) || 
      (pc.hasSkillEffect(30005)) || 
      (pc.hasSkillEffect(30006)) || 
      (pc.hasSkillEffect(157)) || 
      (pc.hasSkillEffect(30003)) || 
      (pc.hasSkillEffect(30004)) || 
      (pc.hasSkillEffect(208)) || 
      (pc.hasSkillEffect(212)) || 
      (pc.hasSkillEffect(103)) || 
      (pc.hasSkillEffect(66)) || 
      (pc.hasSkillEffect(33)) || 
      (pc.hasSkillEffect(10101))) {
      return;
    }
    if (pc.hasSkillEffect(71)) {
      return;
    }
    if ((!pc.getMap().isUsableItem()) && (!pc.isGm())) {
      return;
    }
    ArrayList<Integer> _물약리스트 = pc.get_자동물약리스트();
    if ((_물약리스트 == null) || (_물약리스트.isEmpty())) {
      return;
    }
    for (Iterator<Integer> localIterator = _물약리스트.iterator(); localIterator.hasNext();)
    {
      int itemId = ((Integer)localIterator.next()).intValue();
      L1ItemInstance item = pc.getInventory().findItemId(itemId);
      if ((item != null) && 
      
        (item.getItem().getType2() == 0))
      {
        int delay_id = ((L1EtcItem)item.getItem()).get_delayid();
        if ((delay_id == 0) || 
          (!pc.hasItemDelay(delay_id))) {
          switch (item.getItemId())
          {
          case 30087: 
          case 40317: 
            L1ItemInstance 수리무기 = pc.getWeapon();
            if ((수리무기 != null) && (수리무기.get_durability() > 0)) {
              dowork(pc);
            }
            break;
          }
        }
      }
    }
  }
  
  private void dowork(L1PcInstance pc)
  {
    L1ItemInstance 수리무기 = pc.getWeapon();
    pc.getInventory().recoveryDamage(수리무기);
    String msg0 = 수리무기.getLogName();
    if (수리무기.get_durability() == 0) {
      pc.sendPackets(new S_ServerMessage(464, msg0));
    } else {
      pc.sendPackets(new S_ServerMessage(463, msg0));
    }
    pc.getInventory().consumeItem(40317, 1);
  }
}
