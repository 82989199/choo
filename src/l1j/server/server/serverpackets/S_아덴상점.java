package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_아덴상점 extends ServerBasePacket {
	public S_아덴상점(L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(7626);//구매할때 체크패킷
		 //아덴상점 엔피씨번호 
		int npcId = 5; //아덴상점 하고자하는 엔피씨번호 월드상에 없어도됨 
		
		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = shop.getSellingItems();
		writeH(shopItems.size());

		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			writeH(shopItem.getItem().getGfxId());
			writeD(price);
			if (shopItem.getPackCount() > 1)
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			else {
				writeS(item.getName());
			}
			int type = shopItem.getItem().getUseType();
			if (type < 0){
				type = 0;
			}
			writeD(type);
			template = ItemTable.getInstance().getTemplate(item.getItemId());
			if (template == null) {
				writeC(0);
			} else {
				dummy.setItem(template);
				byte[] status = dummy.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
		}
		writeH(0x07);//상점에 구매표기 패킷 그림
	}

	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
