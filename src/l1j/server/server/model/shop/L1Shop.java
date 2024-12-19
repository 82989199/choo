package l1j.server.server.model.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.BugRaceController.BugTicketInfo;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcCashShopTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcCashShopInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.IntRange;

public class L1Shop {
	public static boolean is_normal_shop(L1NpcInstance npc){
		return ShopTable.getInstance().get(npc.getNpcId()) != null;
	}
	
	public static L1Shop find_shop(L1NpcInstance npc){
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = null;
		if (npc instanceof L1NpcShopInstance) {
			shop = NpcShopTable.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable2.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable3.getInstance().get(npcId);
		} else if (npc instanceof L1NpcCashShopInstance) {
			shop = NpcCashShopTable.getInstance().get(npcId);
		}else{
			shop = ShopTable.getInstance().get(npcId);
		}
		if (shop == null) 
			System.out.println("엔피시 상점 오류 : 번호" + npc.getNpcId() + " x :" + npc.getX() + " y :" + npc.getY() + " map :" + npc.getMapId());
		return shop;
	}
	
	private final int _npcId;
	private final List<L1ShopItem> _sellingItems;
	private final List<L1ShopItem> _purchasingItems;

	private static final HashMap<Integer, Integer> _checkbox_rules;
	static{
		_checkbox_rules = new HashMap<Integer, Integer>();
		_checkbox_rules.put(4100251, 4100251);
		_checkbox_rules.put(4100252, 4100252);
		_checkbox_rules.put(4100253, 4100253);
		
/*		_checkbox_rules.put(40222, 40222);
		_checkbox_rules.put(41148, 41148);
		_checkbox_rules.put(5559, 5559);
		_checkbox_rules.put(210125, 210125);
		_checkbox_rules.put(4100046, 4100046);
		_checkbox_rules.put(4100079, 4100079);*/
	}
	
	public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
		if (sellingItems == null || purchasingItems == null) {
			throw new NullPointerException();
		}
		_npcId = npcId;
		_sellingItems = sellingItems;
		_purchasingItems = purchasingItems;
	}

	public int getNpcId() {
		return _npcId;
	}

	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}

	public List<L1ShopItem> getBuyingItems() {
		return _purchasingItems;
	}

	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null) {
			return false;
		}
		if (item.isEquipped()) {
			return false;
		}
		if (item.getBless() >= 128) {
			return false;
		}
		/** 인형 착용 여부 **/
		if (item.isDollOn()) {
			return false;
		}
		return true;
	}
	
	public L1ShopItem getSellItem(int itemid){
		for(L1ShopItem a : _sellingItems){
			if(a.getItemId() == itemid){
				return a;
			}
		}
		return null;
	}
	
	public L1ShopItem getBuyItem(int itemid){
		for(L1ShopItem a : _purchasingItems){
			if(a.getItemId() == itemid){
				return a;
			}
		}
		return null;
	}
	
	public boolean isSellingItem(int itemid){
		for(L1ShopItem a : _sellingItems){
			if(a.getItemId() == itemid){
				return true;
			}
		}
		return false;
	}

	private L1ShopItem getPurchasingItem(int itemId, int enchant) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId && shopItem.getEnchant() == enchant) {
				return shopItem;
			}
		}
		return null;
	}

	public L1ShopItem getSellingItem(int itemId) {
		for (L1ShopItem shopItem : _sellingItems) {
			if (shopItem.getItemId() == itemId) {
				return shopItem;
			}
		}
		return null;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId(), item.getEnchantLevel());
		if (shopItem == null) {
			return null;
		}
		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.RATE_SHOP_PURCHASING_PRICE / item.getPackCount());
	}

	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (item.getEnchant() == targetItem.getEnchantLevel()) { // 인챈트가 같은 아이템만
					result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
				}
			}
		}
		return result;
	}

	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPriceTaxIncluded();

		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189));
			return false;
		}

		int currentWeight = pc.getInventory().getWeight() * 2000;// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 3000))// 맥스
																					// 무게에
																					// 따른
		{
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}

		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		// ## (버그 방지) 상점 버그 방지
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void payCastleTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId);
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId == L1CastleLocation.DIAD_CASTLE_ID) {
			castleTax += nationalTax;
			nationalTax = 0;
		}

		if (castleId != 0 && castleTax > 0) {
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
			int money = war.getPublicMoney();
			if (2000000000 > money + castleTax) {
				money = money + castleTax;
				war.setPublicMoney(money);
				MJCastleWarBusiness.getInstance().updateCastle(castleId);
			}

			if (nationalTax > 0) {
				war = MJCastleWarBusiness.getInstance().get(L1CastleLocation.ADEN_CASTLE_ID);
				money = war.getPublicMoney();
				if (2000000000 > money + castleTax) {
					money = money + nationalTax;
					war.setPublicMoney(money);
					MJCastleWarBusiness.getInstance().updateCastle(L1CastleLocation.ADEN_CASTLE_ID);
				}

			}
		}
	}

	private void payDiadTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int diadTax = calc.calcDiadTaxPrice(price);
		if (diadTax <= 0) {
			return;
		}

		MJCastleWar war = MJCastleWarBusiness.getInstance().get(L1CastleLocation.DIAD_CASTLE_ID);
		int money = war.getPublicMoney();
		if (2000000000 > money + diadTax) {
			money = money + diadTax;
			war.setPublicMoney(money);
			MJCastleWarBusiness.getInstance().updateCastle(L1CastleLocation.DIAD_CASTLE_ID);
		}
	}

	private void payTax(L1ShopBuyOrderList orderList) {
		payCastleTax(orderList);
		payDiadTax(orderList);
	}

	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPriceTaxIncluded())) {
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		
		L1ItemInstance item = null;
		Random random = new Random(System.nanoTime());
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			if (_npcId == 70035 || _npcId == 70041 || _npcId == 70042) {
				BugTicketInfo tInfo = BugRaceController.getInstance().find_ticket_info(itemId);
				if(tInfo != null){
					itemId = tInfo.converter_itemid;
				}
			}
			
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			
			/**일일 구매제한 상점 체크*/
			if(LimitShopController.getInstance().CheckLimitShop(inv.getOwner(),
					_npcId, itemId, amount) == false){				
				inv.getOwner().sendPackets(new S_SystemMessage("더이상 구매할수없습니다."));
				return;
			}
			
			
			item.setCount(amount);
			item.setIdentified(true);
			if (_npcId == 70068 || _npcId == 70020 || _npcId == 70056) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(150) + 1;
				if (chance <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance >= 16 && chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance >= 31 && chance <= 89) {
					item.setEnchantLevel(0);
				} else if (chance >= 90 && chance <= 141) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance >= 142 && chance <= 147) {
					item.setEnchantLevel(random.nextInt(3) + 3);
				} else if (chance >= 148 && chance <= 149) {
					item.setEnchantLevel(6);
				} else if (chance == 150) {
					item.setEnchantLevel(7);
				}
			}
			if (_npcId == 900173) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(200) + 1;
				if (chance <= 20) {
					item.setEnchantLevel(-2);
				} else if (chance >= 25 && chance <= 35) {
					item.setEnchantLevel(-1);
				} else if (chance >= 40 && chance <= 55) {
					item.setEnchantLevel(0);
				} else if (chance >= 60 && chance <= 70) {
					item.setEnchantLevel(random.nextInt(1));
				} else if (chance >= 100 && chance <= 120) {
					item.setEnchantLevel(random.nextInt(2) +1);
				} else if (chance >= 150 && chance <= 170) {
					item.setEnchantLevel(3);
				}
			}
			if (_npcId == 7310125) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(200) + 1;
				if (chance <= 19) {
					item.setEnchantLevel(-2);
				} else if (chance >= 20 && chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance >= 31 && chance <= 49) {
					item.setEnchantLevel(0);
				} else if (chance >= 50 && chance <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 3);
				} else if (chance >= 70 && chance <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 2);
				} else if (chance >= 130 && chance <= 150) {
					item.setEnchantLevel(5);
				} else if (chance == 170) {
					item.setEnchantLevel(6);
				}
			}
			if (_npcId == 7310104) {// 마족
				item.setIdentified(false);
				int chance1 = random.nextInt(150) + 2;
				if (chance1 <= 15) {
					item.setEnchantLevel(-3);
				} else if (chance1 >= 31 && chance1 <= 40) {
					item.setEnchantLevel(-2);
				} else if (chance1 >= 41 && chance1 <= 50) {
					item.setEnchantLevel(-1);
				} else if (chance1 >= 10 && chance1 <= 20) {
					item.setEnchantLevel(0);
				} else if (chance1 >= 21 && chance1 <= 30) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance1 >= 51 && chance1 <= 60) {
					item.setEnchantLevel(random.nextInt(3) + 3);
				} else if (chance1 >= 61 && chance1 <= 70) {
					item.setEnchantLevel(2);
				} else if (chance1 >= 71 && chance1 <= 80) {
					item.setEnchantLevel(3);
				} else if (chance1 >= 81 && chance1 <= 90) {
					item.setEnchantLevel(4);
				} else if (chance1 == 100) {
					item.setEnchantLevel(5);
				}

				/** 아덴 인챈상점 추가 **/
			} else if (_npcId == 200004 || _npcId == 200002 || _npcId == 900171 || _npcId == 81008 || _npcId == 7320053 || _npcId == 526 || _npcId == 531 || _npcId == 70010
					|| _npcId == 200005 /*1억아데나상점*/ || _npcId == 199999 /*후원코인상점*/ || _npcId == 199998 /*후원코인상점*/
					|| _npcId == 7320222 || _npcId == 7320223 || _npcId == 2020561 || _npcId ==7321110
					|| _npcId ==7321111 || _npcId ==7321112 || _npcId ==7321113 || _npcId ==7321114 || _npcId ==7321115
					|| _npcId ==7321116 || _npcId ==7321117 || _npcId ==7321118 || _npcId ==7310112 || _npcId ==82000 || _npcId ==81006) {
				item.setEnchantLevel(enchant);
			}
			// 배당을 측정하기 위한 추가 부분
			if (_npcId == 70035 || _npcId == 70041 || _npcId == 70042) {
				BugRaceController.getInstance().on_buy_ticket(item.getItemId(), amount);
/*				for (int row = 0; row < 5; row++) {
					if (BugRaceController.getInstance()._ticketId[row] == item.getItemId()) {
						BugRaceController.getInstance()._ticketCount[row] += amount;
					}
				}*/
			}
			item = inv.storeItem(item);
		}
	}

	/** [ 구매할때 깃털상점 추가 [2] **/
	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		if (getNpcId() == 200060 || getNpcId() == 200061 || getNpcId() == 200062 || getNpcId() == 200063 || getNpcId() == 7310103 || getNpcId() == 523
				|| getNpcId() == 5000000 || getNpcId() == 900047 || getNpcId() == 5072 || getNpcId() == 5073 || getNpcId() == 519 || getNpcId()== 224
				|| getNpcId() == 7310101 || getNpcId() == 6000002 || getNpcId() == 7310113 || getNpcId() == 7320124) {
			if (!ensurePremiumSell(pc, orderList)) {
				return;
			}
			sellPremiumItems(pc.getInventory(), orderList);
			return;
		}
		if (getNpcId() == 5000006) {
			if (!ensureMarkSell1(pc, orderList)) {
				return;
			}
			sellMarkItems1(pc.getInventory(), orderList);
			return;
		}
		//TODO 행베리
		if (getNpcId() == 7000077) {
			if (!ensureMarkSell2(pc, orderList)) {
				return;
			}
			sellMarkItems2(pc.getInventory(), orderList);
			return;
		}
		//TODO 판도라의 증서
		if (this.getNpcId() == 91245) {
			if (!this.ensure_cert(pc, orderList)) {
				return;
			}
			this.sell_cert(pc.getInventory(), orderList);
			return;
		}
		//TODO 오림의 가넷
		if (this.getNpcId() == 70017) {
			if (!this.ensure_cert3(pc, orderList)) {
				return;
			}
			this.sell_cert3(pc.getInventory(), orderList);
			return;
		}
		//TODO 하딘의 가넷
		if (this.getNpcId() == 7320250) {
			if (!this.ensure_Hardin(pc, orderList)) {
				return;
			}
			this.sell_Hardin1(pc.getInventory(), orderList);
			return;
		}
		//TODO 군터의 인장
		if (this.getNpcId() == 8500140) {
			if (!this.ensure_certRNS(pc, orderList)) {
				return;
			}
			this.sell_certRNS(pc.getInventory(), orderList);
			return;
		}
		
		//TODO 후원 코인
		if (this.getNpcId() == 199999 || this.getNpcId() == 199998) {
			if (!this.ensure_certDunkCoin(pc, orderList)) {
				return;
			}
			this.sell_certDunkCoin(pc.getInventory(), orderList);
			return;
		}
		
		//TODO 루돌프의 종
		if (this.getNpcId() == 521 || this.getNpcId() == 8500300) {
			if (!this.ensure_cert5(pc, orderList)) {
				return;
			}
			this.sell_cert4(pc.getInventory(), orderList);
			return;
		}
		
		//TODO 이벤트기간에 구매
		if (this.getNpcId() == 200005 /*1억아데나상점*/ || this.getNpcId() == 199999 /*후원코인상점*/ || this.getNpcId() == 199998 /*후원코인상점*/
				|| this.getNpcId() == 7320222 || this.getNpcId() == 7320223 || this.getNpcId() == 6200010) {
			if (!this.ensure_cert6(pc, orderList)) {
				return;
			}
			this.sell_cert6(pc.getInventory(), orderList);
			return;
		}
		//TODO 종이학
		if (this.getNpcId() == 7310119 || this.getNpcId() == 7310120 || this.getNpcId() == 7310126 || this.getNpcId() == 7310124) {
			if (!this.ensure_cert1(pc, orderList)) {
				return;
			}
			this.sell_cert1(pc.getInventory(), orderList);
			return;
		}
		if (this.getNpcId() == 5000007 || this.getNpcId() == 5000008 || this.getNpcId() == 5000009 || this.getNpcId() == 5000010
				|| this.getNpcId() >= 5000001 && this.getNpcId() <= 5000004
				|| this.getNpcId() >= 5000011 && this.getNpcId() <= 5000018) {
			if (!this.ensure_cert7(pc, orderList)) {
				return;
			}
			this.sell_cert5(pc.getInventory(), orderList);
			return;
		}
		if (this.getNpcId() == 520) {
			if (!this.ensure_cert4(pc, orderList)) {
				return;
			}
			this.sell_cert1(pc.getInventory(), orderList);
			return;
		}
		//TODO N코인 상점
		if (this.getNpcId() == 7320055) {
			if (!this.ensure_certNcoin(pc, orderList)) {
				return;
			}
			this.sell_certNcoin(pc, orderList);
			return;
		}

	
		//TODO 우승 코인
		if (getNpcId() == 7322000) {
			if (!ensureMarkSell3(pc, orderList)) {
				return;
			}
			sellMarkItems3(pc.getInventory(), orderList);
			return;
		}
		
		//TODO 콜로세움
		if (this.getNpcId() == 2999 || this.getNpcId() == 2998) {
			if (!this.ensure_Colosseum(pc, orderList)) {
				return;
			}
			this.sell_Colosseum(pc.getInventory(), orderList);
			return;
		}
		//TODO 여의주
		if (this.getNpcId() == 7310128) {
			if (!this.ensure_cert2(pc, orderList)) {
				return;
			}
			this.sell_cert2(pc.getInventory(), orderList);
			return;
		}

		//TODO 탐(TAM)상인
		if (getNpcId() == 7200002 /*크루나*/ || getNpcId() == 7000097 /*탐 제작 테이블*/ ) {
			if (!탐상인1(pc, orderList)) {
				return;
			}
			탐상인2(pc, pc.getInventory(), orderList);
			return;
		}
		// 영자 엔피씨무인상점
		if (getNpcId() >= 4000001 && getNpcId() <= 4000061 || getNpcId() == 7320087 || getNpcId() == 7320122 || getNpcId() == 7320123
				|| getNpcId() == 7320157) {
			if (!NoTaxEnsureSell(pc, orderList)) {
				return;
			}
			NpcShopSellItems(pc.getInventory(), orderList);
			return;

		}
		/** 패키지상점 **/
		// 1차 코인 상인
		if (getNpcId() >= 6100000 && getNpcId() <= 6100011) {
			if (!ensureCashSell1(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems1(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}

		// 2차 코인 상인
		if (getNpcId() >= 6100012 && getNpcId() <= 6100023) {
			if (!ensureCashSell2(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems2(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}

		// 3차 코인 상인
		if (getNpcId() >= 6100024 && getNpcId() <= 6100035) {
			if (!ensureCashSell3(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems3(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}
		/** 패키지상점 **/

		// 장비 코인상점 (종합)
		if (getNpcId() >= 2 && getNpcId() <= 4) {
			if (!ensureCashSell4(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems4(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}

		// 신묘년 이벤트
		if (getNpcId() == 900107) {
			if (!ensureMarkSell(pc, orderList)) {
				return;
			}
			sellMarkItems(pc.getInventory(), orderList);
			return;
		}
		if (!ensureSell(pc, orderList)) {
			return;
		} else {
			sellItems(pc.getInventory(), orderList);
			payTax(orderList);
		}
	}

	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item == null)
				continue;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(item, order.getCount());
				if (totalPrice + order.getItem().getAssessedPrice() * count > 2147483647L) {
					return;
				}
				totalPrice += order.getItem().getAssessedPrice() * count;
			}
		}

		if(item == null){
			return;
		}

		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			/** 패키지상점 **/
			if (getNpcId() >= 6100000 && getNpcId() <= 6100035) {
				inv.storeItem(getNpcId() - 5299999, totalPrice);
			} else if (_npcId == 7000077) {// 행베리
				if (0 < totalPrice) {
					inv.storeItem(41302, totalPrice);
				}
			} else if (_npcId == 7320055) { // N코인 상점
				if (0 < totalPrice) {
//					inv.storeItem(4100056, totalPrice, true);
					inv.storeItem(3000181, totalPrice, true); // N코인
				}
			} else {
				inv.storeItem(L1ItemId.ADENA, totalPrice);
			}
		}
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}

	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}

	/** 깃털 상점관련 **/
	private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(41921, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 픽시의 깃털을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int amount = order.getCount();
			Random random = new Random(System.nanoTime());
			item = ItemTable.getInstance().createItem(itemId);
			item.setIdentified(true);
			if (getSellingItems().contains(item)) {
				return;
			}

			if (_npcId == 7310103) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance1 = random.nextInt(100) + 1;
				if (chance1 <= 30) {
					item.setEnchantLevel(1);
				} else if (chance1 >= 40 && chance1 <= 50) {
					item.setEnchantLevel(2);
				} else if (chance1 >= 60 && chance1 <= 70) {
					item.setEnchantLevel(3);
				} else if (chance1 >= 80 && chance1 <= 90) {
					item.setEnchantLevel(4);
				} else if (chance1 >= 95 && chance1 <= 100) {
					item.setEnchantLevel(5);
				}

				/** 깃털 인챈상점 추가 [1] **/
			} else if (_npcId == 5073 || _npcId == 7310101 || _npcId == 6000002 || _npcId == 7310113 || _npcId == 519  || _npcId == 224 || _npcId == 523) {
				item.setIdentified(true);
				item.setEnchantLevel(enchant);
				item.setCount(amount);// 추가
			}
			item = inv.storeItem(item);
		}
	}

	// 프리미엄 상인으로 부터 아이템을 살수 있는지 체크//
	private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		int FeatherCount = Config.FEATHER_SHOP_NUM;
		// 오버플로우 체크
		if (!IntRange.includes(price, 0, FeatherCount)) {
			pc.sendPackets(new S_SystemMessage( "픽시의 금빛 깃털은 한번에 " + Config.FEATHER_SHOP_NUM + "개 이상 사용할 수 없습니다."));
			return false;
		}
		// 구입할 수 있을까 체크
		if (!pc.getInventory().checkItem(41921, price)) {
			pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털이 부족합니다."));
			return false;
		}
		// 중량 체크
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void sellMarkItems1(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000032, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 징표를 소비할 수 없습니다.");

		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	private boolean NoTaxEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {

		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private synchronized void NpcShopSellItems_for_locked(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		int sellings_price = orderList.getTotalPrice();
		if(inv.countItems(L1ItemId.ADENA) < sellings_price){
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int amount = order.getCount();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
		}
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		L1ItemInstance item = null;
		boolean[] isRemoveFromList = new boolean[_sellingItems.size()];
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			if (remaindcount == amount) {
				isRemoveFromList[orderid] = true;
			} else
				_sellingItems.get(orderid).setCount(remaindcount - amount);
			inv.storeItem(item);
			for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					_sellingItems.remove(i);
				}
			}
			
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
			npc.updateSellings(item.getId(), item.getCount());
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/			
		}
	}
	
	private void NpcShopSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if(Config.IS_SELLINGS_SHOP_LOCK){
			NpcShopSellItems_for_locked(inv, orderList);
			return;
		}
		
		int sellings_price = orderList.getTotalPrice();
		if(inv.countItems(L1ItemId.ADENA) < sellings_price){
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int amount = order.getCount();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
		}
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		L1ItemInstance item = null;
		boolean[] isRemoveFromList = new boolean[_sellingItems.size()];
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			if (remaindcount == amount) {
				isRemoveFromList[orderid] = true;
			} else
				_sellingItems.get(orderid).setCount(remaindcount - amount);
			inv.storeItem(item);
			for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					_sellingItems.remove(i);
				}
			}
			
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
			npc.updateSellings(item.getId(), item.getCount());
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/			
		}
	}
	
	

	private void sellMarkItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(410093, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 만월의정기를 소비할 수 없습니다.");
		} // 신묘년
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	private void sellMarkItems3(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(202221, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 마일리지 코인이 부족 합니다.");
		} // 마일리지 코인
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}
	
	private boolean ensureMarkSell2(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 행베리
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage("베리는 한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(41302, price)) {
			pc.sendPackets(new S_ChatPacket(pc, "베리가 부족합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureMarkSell3(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 마일리지 코인
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 100000000)) {
			pc.sendPackets(new S_SystemMessage("마일리지 코인은 한번에 10,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(202221, price)) {
			pc.sendPackets(new S_ChatPacket(pc, "마일리지 코인이 부족합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	private void sell_cert1(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000156, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 종이학을 소비할 수 없습니다.");
		} // 종이학
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			Random random = new Random(System.nanoTime());
			item = ItemTable.getInstance().createItem(itemId);
			item.setIdentified(true);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			
			if (_npcId == 7310126) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance2 = random.nextInt(150) + 1;
				if (chance2 <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance2 >= 20 && chance2 <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance2 >= 31 && chance2 <= 40) {
					item.setEnchantLevel(0);
				} else if (chance2 >= 51 && chance2 <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 2);
				} else if (chance2 >= 70 && chance2 <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 3);
				} else if (chance2 >= 90 && chance2 <= 110) {
					item.setEnchantLevel(5);
				} else if (chance2 == 148) {
					item.setEnchantLevel(6);
				}
				//TODO 종이학 인챈아이템 구매시 NPC등록
		/*	}else if(_npcId == 5000017){
				item.setEnchantLevel(order.getItem().getEnchant());*/
			}
			item = inv.storeItem(item);
		}
	}

	private void sell_cert2(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000180, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 여의주을 소비할 수 없습니다.");
		} // 여의주
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int amount = order.getCount();
			Random random = new Random(System.nanoTime());
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			if (_npcId == 010) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance1 = random.nextInt(150) + 1;
				if (chance1 <= 15) {
					item.setEnchantLevel(-3);
				} else if (chance1 >= 31 && chance1 <= 40) {
					item.setEnchantLevel(-2);
				} else if (chance1 >= 21 && chance1 <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance1 >= 15 && chance1 <= 30) {
					item.setEnchantLevel(0);
				} else if (chance1 >= 41 && chance1 <= 50) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance1 >= 71 && chance1 <= 81) {
					item.setEnchantLevel(random.nextInt(2) + 3);
				} else if (chance1 >= 110 && chance1 <= 120) {
					item.setEnchantLevel(3);
				} else if (chance1 == 140) {
					item.setEnchantLevel(4);
				}
			}

			if (_npcId == 010) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance2 = random.nextInt(150) + 1;
				if (chance2 <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance2 >= 20 && chance2 <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance2 >= 31 && chance2 <= 40) {
					item.setEnchantLevel(0);
				} else if (chance2 >= 51 && chance2 <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 2);
				} else if (chance2 >= 70 && chance2 <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 3);
				} else if (chance2 >= 90 && chance2 <= 110) {
					item.setEnchantLevel(5);
				} else if (chance2 == 148) {
					item.setEnchantLevel(6);
				}

				/** 여의주 인챈상점 추가 [1] **/
			} else if (_npcId == 010) { // -- 깃털상점 수량2개이상 구매시 체크해야하니 번호적기
				item.setIdentified(true);
				item.setEnchantLevel(enchant);
				item.setCount(amount);// 추가
			}
			item = inv.storeItem(item);
		}
	}

	private void sell_Colosseum(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(710, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 콜로세움 코인를 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			} else if (_npcId == 2999 || _npcId == 2998) {
				item.setIdentified(true);
				item.setEnchantLevel(enchant);
				item.setCount(amount);
			}
			item = inv.storeItem(item);
		}
	}
	private void sell_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int ncoin = pc.getNcoin() - orderList.getTotalPrice();
		if(ncoin < 0 || pc.getAccount() == null)
			throw new IllegalStateException("구입에 필요한 N코인을 소비할 수 없습니다.");
		pc.getAccount().Ncoin_point = ncoin;
		pc.getAccount().updateNcoin();
		pc.sendPackets(new S_SurvivalCry(2, pc));
		L1Inventory inv = pc.getInventory();
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int amount = order.getCount();
			Random random = new Random(System.nanoTime());
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			if (_npcId == 010) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance2 = random.nextInt(150) + 1;
				if (chance2 <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance2 >= 20 && chance2 <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance2 >= 31 && chance2 <= 40) {
					item.setEnchantLevel(0);
				} else if (chance2 >= 51 && chance2 <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 2);
				} else if (chance2 >= 70 && chance2 <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 3);
				} else if (chance2 >= 90 && chance2 <= 110) {
					item.setEnchantLevel(5);
				} else if (chance2 == 148) {
					item.setEnchantLevel(6);
				}

				/** 여의주 인챈상점 추가 [1] **/
			} else if (_npcId == 010) { // -- 깃털상점 수량2개이상 구매시 체크해야하니 번호적기
				item.setIdentified(true);
				item.setEnchantLevel(enchant);
				item.setCount(amount);// 추가
			}
			item.setIdentified(true);
			item = inv.storeItem(item);
		}
	}
	
	private boolean ensure_cert1(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 썸타는 상점
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000156, price)) {
			pc.sendPackets(new S_SystemMessage( "종이학이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	private boolean ensure_cert4(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 썸타는 상점
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000156, price)) {
			pc.sendPackets(new S_SystemMessage( "LFC종이학이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	private boolean ensure_cert7(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000156, price)) {
			pc.sendPackets(new S_SystemMessage( "\\f2미리보기 입니다. 구매는 (메티스)에게 문의 바랍니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	private boolean ensure_cert2(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 10000개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000180, price)) {
			pc.sendPackets(new S_SystemMessage( "여의주가 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}	
	private void sell_cert5(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000156, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 종이학을 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setIdentified(true);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			
			if(_npcId == 5000009 || _npcId == 5000007 || _npcId == 5000008 || _npcId == 5000010
					|| _npcId >= 5000001 && _npcId <= 5000004 || _npcId >= 5000011 && _npcId <= 5000018){
				item.setEnchantLevel(order.getItem().getEnchant());
			}
			item = inv.storeItem(item);
		}
	}
	private boolean ensure_Colosseum(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 10000개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(710, price)) {
			pc.sendPackets(new S_SystemMessage( "구입: 콜로세움 코인가 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}	
	private boolean ensure_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 10000000개 이상 사용할 수 없습니다."));
			return false;
		}

		if(pc.getNcoin() < price){
			pc.sendPackets(new S_SystemMessage( "구입: N코인이 부족 합니다."));
			return false;
		}
		
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	// 루돌프의 종 → 배팅 코인으로 변경
	private boolean ensure_cert5(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 10000개 이상 사용할 수 없습니다."));
			return false;
		}

//		if (!pc.getInventory().checkItem(812, price)) { // 루돌프의 종
//		pc.sendPackets(new S_SystemMessage( "루돌프의 종 이 부족 합니다."));
		
		// 배팅코인
		if (!pc.getInventory().checkItem(3000184, price)) {
		pc.sendPackets(new S_SystemMessage( "배팅 코인이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void sell_cert(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(90738, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 판도라의 증서 를 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}
	private void sell_certRNS(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000367, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 군터의 인장 를 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	
	private void sell_certDunkCoin(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000181, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 후원 코인을 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	private void sell_Hardin1(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000520, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 하딘의 가넷 을 소비할 수 없습니다.");
		} // 하딘의 가넷
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}
	private void sell_cert3(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(3000246, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 오림의 가넷 을 소비할 수 없습니다.");
		} // 오림의 가넷
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			
			if(_npcId == 70017){
				item.setEnchantLevel(order.getItem().getEnchant());
			}
			
			
			inv.storeItem(item);
		}
	}
	private void sell_cert6(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(400254, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 아데나 수표(1억) 를 소비할 수 없습니다.");
//			throw new IllegalStateException("구입에 필요한 아데나를 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			
			if(_npcId == 200005 /*1억수표상점*/ || _npcId == 199999 /*후원코인상점*/ || _npcId == 199998 /*후원코인상점*/
					|| _npcId == 7320222 || _npcId == 7320223 || _npcId == 6200010){
				item.setEnchantLevel(order.getItem().getEnchant());
			}
			inv.storeItem(item);
		}
	}

	private boolean ensure_cert(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(90738, price)) {
			pc.sendPackets(new S_SystemMessage( "구입: 판도라의 증서가 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	private boolean ensure_certRNS(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000367, price)) {
			pc.sendPackets(new S_SystemMessage("군터의 인장이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	// 후원 코인
	private boolean ensure_certDunkCoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 1000000)) {
			pc.sendPackets(new S_SystemMessage( "구입: 한번에 100만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000181, price)) {
			pc.sendPackets(new S_SystemMessage("후원 코인이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensure_Hardin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 하딘의 가넷
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000520, price)) {
			pc.sendPackets(new S_SystemMessage( "하딘의 가넷이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensure_cert3(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 오림의 가넷
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage( "한번에 1,000만개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000246, price)) {
			pc.sendPackets(new S_SystemMessage( "오림의 가넷이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	private boolean ensure_cert6(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 1000000000)) {
			pc.sendPackets(new S_SystemMessage("한번에 10억 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(400254, price)) {
//			pc.sendPackets(new S_ServerMessage(189));
			pc.sendPackets(new S_SystemMessage("아데나수표(1억)이 부족 합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if(Config.IS_CHECKBOX_RULES_USE && _checkbox_rules.containsKey(temp.getItemId())){
				pc.sendPackets(String.format("%s는 이벤트기간에만 구매가능 합니다.", temp.getName()));
				return false;
			}
			
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void sellMarkItems2(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(41302, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 베리를 소비할 수 없습니다.");
		} // 행베리
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	private boolean 탐상인1(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 9000000)) {
			pc.sendPackets(new S_SystemMessage( "\\aATAM은 한번에 '\\aG900\\aA'만개 이상 사용할 수 없습니다."));
			return false;
		}
		if (pc.getAccount().getTamPoint() < price) {
			pc.sendPackets(new S_SystemMessage( "TAM이 부족합니다."));
			return false;
		} /*
			 * else { pc.getAccount().addTamPoint(-(price)); pc.sendPackets(new
			 * S_ACTION_UI(S_ACTION_UI.TAM, pc.getAccount().getTamPoint())); }
			 */

		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void 탐상인2(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (orderList.getTotalPrice() > pc.getAccount().getTamPoint()) {
			throw new IllegalStateException("구입에 필요한 TAM을 소비할 수 없습니다.");
		}
		if (orderList.getTotalPrice() <= pc.getAccount().getTamPoint()) {
			pc.getAccount().addTamPoint(-(orderList.getTotalPrice()));
			pc.getAccount().updateTam();
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.TAM, pc.getAccount().getTamPoint()));
		}

		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
		}
	}

	private void sellCashItems1(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(npcId - 5299999, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 코인을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
		}
	}

	private void sellCashItems2(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(npcId - 5299999, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 코인을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
		}
	}

	private void sellCashItems3(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(npcId - 5299999, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 코인을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
		}
	}

	private void sellCashItems4(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(747, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 장비 코인을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
		}
	}

	private boolean ensureCashSell1(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();
		// 9000001 - 5299999 =
		if (!pc.getInventory().checkItem(npcId - 5299999, price)) {
			pc.sendPackets(new S_SystemMessage( "\\aA[GM]: \\aG추가 구입은 \\aA'메티스'\\aG 문의주세요."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureCashSell2(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();

		if (!pc.getInventory().checkItem(npcId - 5299999, price)) {
			pc.sendPackets(new S_SystemMessage("\\aA[GM]: \\aG추가 구입은 \\aA'메티스'\\aG 문의주세요."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureCashSell3(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();

		if (!pc.getInventory().checkItem(npcId - 5299999, price)) {
			pc.sendPackets(new S_SystemMessage("\\aA[GM]: \\aG추가 구입은 \\aA'메티스'\\aG 문의주세요."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	private void sell_cert4(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(812, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 루돌프의 종 을 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			item = inv.storeItem(item);
		}
	}
	private boolean ensureCashSell4(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();

		if (!pc.getInventory().checkItem(747, price)) {
			pc.sendPackets(new S_SystemMessage("장비코인이 부족합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureMarkSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 신묘년
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 1000)) {
			pc.sendPackets(new S_SystemMessage("만월의 정기는 한번에 1,000개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(410093, price)) {
			pc.sendPackets(new S_ServerMessage(337, "$10196"));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureMarkSell1(L1PcInstance pc, L1ShopBuyOrderList orderList) {

		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 50000)) {
			pc.sendPackets(new S_ChatPacket(pc, "한번에 50,000개 이상 사용할 수 없습니다."));
			return false;
		}

		if (!pc.getInventory().checkItem(3000032, price)) {
			pc.sendPackets(new S_ChatPacket(pc, "징표가 부족합니다."));

			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
}
