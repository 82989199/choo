package l1j.server.MJBotSystem;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;

/**********************************
 * 
 * MJ Bot Inventory Item Template.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotInvItem {
	public int		id;
	public int 		itemId;
	public int 		enchant;
	public int 		attrLevel;
	public int		count;
	public boolean 	isEquip;
	
	public L1ItemInstance create(){
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
		if(item == null) return null;
		
		item.setEnchantLevel(enchant);
		item.setAttrEnchantLevel(attrLevel);
		item.setCount(count);
		return item;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(256);
		sb.append("[id : ").append(id).append("]\n");
		sb.append("[itemId : ").append(itemId).append("]\n");
		sb.append("[enchant : ").append(enchant).append("]\n");
		sb.append("[attrLevel : ").append(attrLevel).append("]\n");
		sb.append("[count : ").append(count).append("]\n");
		sb.append("[isEquip : ").append(isEquip).append("]\n");
		return sb.toString();
	}
}
