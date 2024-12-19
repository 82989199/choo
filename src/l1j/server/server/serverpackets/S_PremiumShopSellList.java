package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.DogFight.MJDogFightSettings;
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

public class S_PremiumShopSellList extends ServerBasePacket {

	/**
	 * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
	 */
	public S_PremiumShopSellList(int objId) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);

		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = null;
		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
			System.out.println("상점(엔피씨)클릭시 : NPCID(물품없음/이상) : " + npcId);
		}
		if (shopItems != null) {
			writeH(shopItems.size());
		} else {
			writeH(0);
			return;
		}

		// L1ItemInstance의 getStatusBytes를 이용하기 위해(때문에)
		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
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
		}
		/** 상점 탐,베리등 이미지패킷(데스크번호) **/
		// 탐상인
		if ((npcId == 7200002 /*크루나*/ || npcId == 7000097 /*탐 제작 테이블*/ )) {
			writeC(253);
			writeC(255);
			writeC(0);
			writeC(0);
			// 베리
		} else if ((npcId == 7000077)) {
			writeC(73);
			writeC(58);
			writeC(0);
			writeC(0);
			// 알수없음 0/0
		} else if ((npcId == 900107)) {
			writeC(255);
			writeC(255);
			writeC(0);
			writeC(0);
			// 픽시의 금빛 깃털
		} else if ((npcId == 7310101 || npcId == 6000002)) {
			writeH(0x3ccf);
		} else if (npcId == 7310119 || npcId == 7310120 || npcId == 7310126 || npcId == 7310124 || npcId == 520) {
			writeH(0x4215);
		} else if (npcId == 7310128) { // --여의주
			writeH(0x41D8);
		} else if (npcId == 91245) {// 판도라의증서
			writeH(0x3F23);
		} else if (npcId == 70017) {// 오림의 가넷
			writeH(0x4115);
		} else if (npcId == 7320250) {// 하딘의 가넷
			writeH(0x45AF);
		} else if (npcId == 8500140) {// 군터의 인장
			writeH(17368);
		} else if (npcId == 7322000) {// 우승 코인
			writeH(17735);
		} else if (npcId == 2999 || npcId == 2998) {
			writeH(30050);
		} else if (npcId == 199999 || npcId == 199998) { // 코인상점
			writeH(0x41D8); // 여의주 ->  코인으로 desc에서 이름 변경
		
		
		} else if (npcId == 521) {
			writeH(17619); // 루돌프의 종 -> 배팅코인으로 desc에서 이름 변경
		
		
		} else if (npcId == 200005) { // 1억 수표 상점
			writeH(0x3F23); // 판도라의 증서 -> 1억 수표로 desc에서 이름 변경
		
		} else if (npcId == 7320222 || npcId == 7320223) {
			writeH(7);
		} else {// 그외 깃털로 표시
			writeC(111);
			writeC(10);
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
