package l1j.server.MJBotSystem;

import java.util.Random;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBotDropItem {
	private static final Random _rnd = new Random(System.nanoTime());
	private static final int[]	_attrTypes = new int[]{
		0, 5, 10, 15
	};
	public int itemId;
	public int enchant;
	public int attrLevel;
	public int count;
	public int dice;
	
	public boolean toDrop(MJBotAI ai){
		try{
			L1ItemInstance 	item 	= ItemTable.getInstance().createItem(itemId);
			L1PcInstance	body	= ai.getBody();
			if(item == null || body == null)
				return false;
			
			if(_rnd.nextInt(100) > dice)
				return false;
			
			int iCount = 1;
			if(count > 1)	iCount = _rnd.nextInt(count) + 1;
			
			item.setCount(iCount);
			if(enchant > 0)
				item.setEnchantLevel(_rnd.nextInt(enchant) + 1);
			if(attrLevel > 0)
				item.setAttrEnchantLevel(_attrTypes[_rnd.nextInt(_attrTypes.length)] + attrLevel);
			item.setIdentified(true);
			L1Inventory inv = L1World.getInstance().getInventory(body.getX(), body.getY(), body.getMapId());
			inv.storeTradeItem(item);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
