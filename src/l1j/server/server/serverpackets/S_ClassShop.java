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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_ClassShop extends ServerBasePacket {
	public S_ClassShop(L1PcInstance pc, int objId) {
		writeC(Opcodes.S_BUY_LIST);
		L1Object npcObj = L1World.getInstance().findObject(objId);
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();
		// 클래스상점 상점 엔피씨번호
		if (npcId == 7320121 || npcId == 7320085){//사이먼,이센스
			if(pc.isCrown()) {
				npcId = 73201211;
			} else if (pc.isWizard()) {
				npcId = 73201212;
			} else if (pc.isElf()) {
				npcId = 73201213;
			} else if (pc.isDarkelf()) {
				npcId = 73201214;
			} else if (pc.isDragonknight()) {
				npcId = 73201215;
			} else if (pc.isBlackwizard()) {
				npcId = 73201216;
			} else if (pc.is전사()) {
				npcId = 73201217;
			} else if (pc.isKnight()) {
				npcId = 73201218;
			}
		} else if (npcId == 2020561){
			if(pc.isCrown()) {
				npcId = 2020562;
			} else if (pc.isWizard()) {
				npcId = 2020563;
			} else if (pc.isElf()) {
				npcId = 2020564;
			} else if (pc.isDarkelf()) {
				npcId = 2020565;
			} else if (pc.isDragonknight()) {
				npcId = 2020566;
			} else if (pc.isBlackwizard()) {
				npcId = 2020567;
			} else if (pc.is전사()) {
				npcId = 2020568;
			} else if (pc.isKnight()) {
				npcId = 2020569;
			}
		} else if (npcId == 415333){//무기상점
			if(pc.isCrown()) {
				npcId = 2020570;
			} else if (pc.isWizard()) {
				npcId = 2020571;
			} else if (pc.isElf()) {
				npcId = 2020572;
			} else if (pc.isDarkelf()) {
				npcId = 2020573;
			} else if (pc.isDragonknight()) {
				npcId = 2020574;
			} else if (pc.isBlackwizard()) {
				npcId = 2020575;
			} else if (pc.is전사()) {
				npcId = 2020576;
			} else if (pc.isKnight()) {
				npcId = 2020577;
			}
		}
		
		writeD(npcId);// 구매할때 체크패킷

		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = shop.getSellingItems();
		writeH(shopItems.size());

		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = shopItems.get(i);
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
			if (type < 0) {
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
		writeH(0x07);// 상점에 구매표기 패킷 그림
	}

	@Override
	public byte[] getContent() throws IOException {
		return this._bao.toByteArray();
	}
}
