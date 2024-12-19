package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;


public class S_TestShop extends ServerBasePacket {

		/**
		 * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
		 */
		public S_TestShop(int objId, int code) {
			writeC(Opcodes.S_BUY_LIST);
			writeD(objId);

			writeH(1);
			writeD(0);
			writeH(5);
			writeD(1000);
			writeS("아데나");
			writeD(5);
			writeC(0);
			/*for (int i = 0; i < shopItems.size(); i++) {
				shopItem = (L1ShopItem) shopItems.get(i);
				item = shopItem.getItem();
				int price = shopItem.getPrice();
				writeD(i);
				try {
					writeH(shopItem.getItem().getGfxId());
				} catch (Exception e) {
					System.out.println("상점(엔피씨)클릭시 : NPCID(물품없음/이상) : " + npcId);
				}
				writeD(price);
				if (shopItem.getPackCount() > 1) {
					writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
				} else if (shopItem.getEnchant() > 0) {
					writeS("+" + shopItem.getEnchant() + " " + item.getName());
				} else if (shopItem.getItem().getMaxUseTime() > 0) {
					writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
				} else {
					writeS(item.getName());
				}
				int type = shopItem.getItem().getUseType();
				if (type < 0) {
					type = 0;
				}

				writeD(type);
				template = ItemTable.getInstance().getTemplate(item.getItemId());
				if (template == null) {
					writeC(0);
				} else {
					dummy.setItem(template);
					dummy.setEnchantLevel(shopItem.getEnchant());
					byte[] status = dummy.getStatusBytes();
					writeC(status.length);
					for (byte b : status) {
						writeC(b);
					}
				}
			}*/
			
			writeH(code);
			writeH(0);			
		}

		@Override
		public byte[] getContent() throws IOException {
			return getBytes();
		}
}
