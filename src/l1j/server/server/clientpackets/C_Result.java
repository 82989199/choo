package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.DogFight.History.MJDogFightHistory;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.server.GameClient;
import l1j.server.server.IdFactory;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.datatables.ItemShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NoShopAndWare;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.ElfWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1TradeItemBox;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrder;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.Logger.WarehouseType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_RetrievePledgeList;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class C_Result extends ClientBasePacket {
	/** 날짜 및 시간 기록 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int sec = rightNow.get(Calendar.SECOND);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + "]";
	String totime1 = "[" + hour + ":" + min + ":" + sec + "]";
	String date = +year + "_" + month + "_" + day;

	public C_Result(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		int npcObjectId = readD();
		int resultType = readC();
		int size = readC();
		@SuppressWarnings("unused")
		int unknown = readC();

		if (size < 0)
			return;

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null)
			return;

		boolean isPrivateShop = false;

		// add
		boolean isPrivateNpcShop = false;
		// add
		ArrayList<Integer> 물약리스트 = new ArrayList<Integer>();
		if (npcObjectId == -1) {
			if (size > 10) {
				pc.sendPackets(new S_SystemMessage("뮬약은 최대 10개 선택가능합니다.."));
				return;
			}
			int index = 0;
			for (int i = 0; i < size; i++) {
				index = readD();
				L1ItemInstance item = (L1ItemInstance) pc._자동물약초이스리스트.get(index);
				if (item != null) {
					if (((item.getItemId() == 30076) || (item.getItemId() == 40068) || (item.getItemId() == 140068)
							|| (item.getItemId() == 210110))
							&& ((pc.is_자동버프리스트(179)) || (pc.is_자동버프리스트(155)) || (pc.is_자동버프리스트(178))
									|| (pc.is_자동버프리스트(177)))) {
						pc.sendPackets(new S_SystemMessage("자동물약에 와퍼를 포함시키기 위해서는 자동버프에서 (댄싱 블레이즈/샌드스톰/포커스 웨이브/허리케인)을 제외시키시기 바랍니다."));
						return;
					}
					물약리스트.add(Integer.valueOf(item.getItemId()));
					pc.sendPackets(new S_SystemMessage("자동물약: " + ItemTable.getInstance().getTemplate(item.getItemId()).getName()));
				}
				readD();
			}
			pc._자동물약리스트 = new ArrayList<Integer>();

			pc.set_자동물약리스트(물약리스트);
			return;
		}
		
		
		if(size != 0 &&npcObjectId == 10){//장비교환 리스트
			if(size > 1){
				 pc.sendPackets(new S_SystemMessage("1개만 교환 할수있습니다."));
				return;
			}
			
				int objectId = readD();
				int count = readD();
				L1ItemInstance item = L1TradeItemBox.getResultItem(objectId);
				if(item != null){
					L1ItemInstance _item = ItemTable.getInstance().createItem(item.getItemId());
					_item.setEnchantLevel(item.getEnchantLevel());
					
					
					L1ItemInstance TradeItem = pc.getTradeItem();
					if(TradeItem == null){
						pc.sendPackets(new S_SystemMessage("교환에 필요한 아이템이 없습니다."));
						return;
					}
					if(TradeItem.isEquipped()){
						pc.sendPackets(new S_SystemMessage("장착중인 템은 교환할수없습니다."));
						return;
					}					
					if (TradeItem.getBless() >= 128) { 
						pc.sendPackets(new S_SystemMessage("봉인된 템은 교환할수없습니다."));
						return;
					}
					
					int enchant = TradeItem.getEnchantLevel();
					boolean check = false;
					for(L1ItemInstance temp : L1TradeItemBox.GetItemInstanceList(TradeItem.getItemId(), enchant)){
						if(TradeItem.getItemId() == temp.getItemId())continue;
						
						
						if(_item.getItemId() == temp.getItemId()){
							
							if(_item.getEnchantLevel() == temp.getEnchantLevel()){
								check = true;
							}
						}
					}					
					if(check == false) return;
					
					
					if (pc.getInventory().checkAddItem(_item, 1) == L1Inventory.OK) // 용량 중량 확인 및 메세지 송신
					{						
						/**장비교환 주문서*/
						if(pc.getInventory().checkItem(202220, 1) &&
								pc.getInventory().getItem(TradeItem.getId()) != null) {
								_item.setIdentified(true);
								pc.getInventory().deleteItem(TradeItem);
								pc.getInventory().consumeItem(202220, 1);							
								pc.getInventory().storeItem(_item);	
						} else {
							 pc.sendPackets(new S_SystemMessage("교환에 필요한 아이템이 없습니다."));
							return;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(270)); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
						return;
					}
				}else {
					pc.sendPackets(new S_SystemMessage("장비교환 목록에 없는 아이템입니다."));
					return;
				}
			
			return;
		}

		int level = pc.getLevel();
		int npcId = 0;
		String npcImpl = "";
		boolean tradable = true;
		L1Object findObject = L1World.getInstance().findObject(npcObjectId);

		String npcName = "";
		
		
		
		if (findObject != null) { // 3셀
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			boolean locck = false;
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance findNpc = (L1NpcInstance) findObject;
				if(ItemShopTable.getInstance().isNpc(findNpc.getNpcId()) == false){
					locck = true;
				}
			}
			if(locck){
				if (diffLocX > 12 || diffLocY > 12) {
					L1NpcInstance targetNpc = (L1NpcInstance) findObject;
					if(resultType == 5 && size == 0 && targetNpc.getNpcTemplate().getImpl().equalsIgnoreCase("L1Dwarf")){
						L1Clan clan = pc.getClan();
						if(clan == null)
							return;
						
						clan.deleteClanRetrieveUser(pc.getId());
						pc.sendPackets("창고와 거리가 멀어 아이템을 찾을 수 없습니다.");
					}else{
						pc.sendPackets("판매/구매 NPC와 거리가 멀어 판매가 취소 되었습니다.");
					}
					return;
				}
			}
			
			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				npcId = targetNpc.getNpcTemplate().get_npcId();
				npcImpl = targetNpc.getNpcTemplate().getImpl();
				npcName = targetNpc.getName();

				// npcshop add
				if (npcImpl.equals("L1NpcShop"))
					isPrivateNpcShop = true;

			} else if (findObject instanceof L1PcInstance) {
				if(npcObjectId == pc.getId() && resultType == 9){
					S_ItemExSelectPacket select_packet = pc.get_select_item();
					if(select_packet != null){
						int select_index = readD();
						int select_count = readD();
						if(select_count <= 0)
							return;
						select_packet.do_select(pc, select_index);
						select_packet.dispose();
						return;
					}
					pc.sendPackets("시간이 초과되었습니다.");
					return;
				}
				
				L1PcInstance gm = (L1PcInstance)findObject;
				if(gm.isGm() && resultType == 3){
					int objectId 	= readD();
					int count 		= readD();
					L1Object tmp 	= L1World.getInstance().findObject(objectId);
					if(tmp != null && tmp instanceof L1PcInstance){
						gm.start_teleport(tmp.getX(), tmp.getY(), tmp.getMapId(), gm.getHeading(), 169, false, false);
					}else
						return;
				}
				isPrivateShop = true;
			}
		}
		if (pc.getOnlineStatus() == 0) {
			clientthread.kick();
			return;
		}

		if (npcObjectId == 7626) {
			npcId = 5; // 아덴상점 엔피씨번호
			npcImpl = "L1Merchant";
		}
		
		if (npcObjectId == 73201211 || npcObjectId == 73201212 || npcObjectId == 73201213 || npcObjectId == 73201214
				 || npcObjectId == 73201215 || npcObjectId == 73201216 || npcObjectId == 73201217 || npcObjectId == 73201218) {
			npcId = npcObjectId; // 클래스 상점 엔피씨 번호
			npcImpl = "L1Merchant";
		}
		
		if (npcObjectId == 2020562 || npcObjectId == 2020563 || npcObjectId == 2020564 || npcObjectId == 2020565
				 || npcObjectId == 2020566 || npcObjectId == 2020567 || npcObjectId == 2020568 || npcObjectId == 2020569) {
			npcId = npcObjectId; // 클래스 상점 엔피씨 번호
			npcImpl = "L1Merchant";
		}
		if (npcObjectId == 2020570 || npcObjectId == 2020571 || npcObjectId == 2020572 || npcObjectId == 2020573
				 || npcObjectId == 2020574 || npcObjectId == 2020575 || npcObjectId == 2020576 || npcObjectId == 2020577) {
			npcId = npcObjectId; // 클래스 상점 엔피씨 번호
			npcImpl = "L1Merchant";
		}
		/********************************************************************************************************
		 ****************************************** 아이템 구매 ***********************************************
		 *********************************************************************************************************/
		if (resultType == 0 && size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) {
			// 후원유저, 전차보상 유저가 오픈 대기 중에 장비 셋팅(구매)을 할 수 없어서 주석처리
//			if (Config.STANDBY_SERVER) {
//				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
//				return;
//			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			// 배팅코인 (투견 티켓 상인인듯?)
			if(MJDogFightSettings.CASTER_NPC_ID == npcId){
				do_dogfight_buy_ticket(pc, size);
				return;
			}

			// 아이템 구입
			L1Shop shop = ShopTable.getInstance().get(npcId);
			if(shop == null){
				System.out.println(String.format("[샵]%d 엔피씨를 찾을 수 없습니다.", npcId));
				return;
			}
			L1ShopBuyOrderList orderList = shop.newBuyOrderList();
			int itemNumber = 0;
			long itemcount = 0;
			if (npcId == 7200002 /*크루나*/ || npcId == 7000097 /*탐제작테이블*/ ) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.TAM, pc.getAccount().getTamPoint()));
			}

			if (npcId == 70035 /*티켓상인레티*/ || npcId == 70041 /*티켓상인모리*/ || npcId == 70042 /*티켓상인리타*/ ) {
				if (BugRaceController.getInstance().getBugState() != 0) {
					return;
				}
			}
			if (pc.서버다운중 == true) {
				if (npcId == 70035 /*티켓상인레티*/ || npcId == 70041 /*티켓상인모리*/ || npcId == 70042 /*티켓상인리타*/ ) {
					pc.sendPackets(new S_SystemMessage("서버다운 진행중에는 구매가 불가능합니다."));
					return;
				}
			}

			if (shop.getSellingItems().size() < size) {
				if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
					pc.sendPackets("이미 경기가 시작되었습니다.");
					return;
				}
				System.out.println("■[버그차단]■: " + pc.getName() + "님이 상점이 판매하는 아이템 수(" + shop.getSellingItems().size() + ")보다 더 많이 사려고 함.(" + size + ")개");
				pc.getNetConnection().kick();
				pc.getNetConnection().close();
				return;
			}
			for (int i = 0; i < size; i++) {
				itemNumber = readD();
				
				itemcount = readD();
				if (itemcount <= 0) {
					return;
				}
				if (npcId >= 6100000 && npcId <= 6100035) {
					if (itemcount > 10) {
						pc.sendPackets(new S_SystemMessage("1개씩 구입할 수 있습니다."));
						return;
					}
				}

				if (itemcount <= 0 || itemcount >= 10000) {
					return;
				}
				try{
					orderList.add(itemNumber, (int) itemcount, pc);
					if(shop.getSellingItems().size() > itemNumber){
						LoggerInstance.getInstance().addShop(shop.getSellingItems().get(itemNumber).getItem().getName(), (int) itemcount, (long) shop.getSellingItems().get(itemNumber).getPrice() * itemcount, npcName,pc.getName());
						if (orderList.BugOk() != 0) {
							for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
								if (player.isGm() || pc == player) {
									player.sendPackets(new S_SystemMessage(pc.getName() + "님 상점 최대구매 수량 (" + itemcount + ")를 초과 하였습니다."));
								}
							}
						}
					}else{
						System.out.println(String.format("[상점 구매 예외 정보] %s 인벤토리 검사해보세요.", pc.getName()));
					}
				}catch(Exception e){
					System.out.println(String.format("[상점 구매 예외 정보] 캐릭터명 : %s, itemNumber : %d, itemcount : %d", pc.getName(), itemNumber, itemcount));
					e.printStackTrace();
					return;
				}
			}
			int bugok = orderList.BugOk();
			if (bugok == 0) {
				// '영양 미끼' 아이템일경우 시간값 갱신해주기 상점 타임지정
				for (L1ShopBuyOrder sbo : orderList.getList()) {
					if (sbo.getItem().getItemId() == 41295)
						pc.setFishingShopBuyTime_1(System.currentTimeMillis());
				}
				shop.sellItems(pc, orderList);
				// 아이템저장시킴
				pc.saveInventory();
				// 아이템저장시킴
			}

			/********************************************************************************************************
			 ****************************************** 아이템 판매 *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 1 && size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) {
			// 후원유저, 전차보상 유저가 오픈 대기 중에 장비 셋팅(구매)을 할 수 없어서 주석처리
//			if(Config.STANDBY_SERVER){
//				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
//				return;
//			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			// 배팅코인 (투견 티켓 상인인듯?)
			if(MJDogFightSettings.CASTER_NPC_ID == npcId){
				do_dogfight_sell_ticket(pc, size, npcObjectId);
				return;
			}

			// 아이템 매각
			L1Shop shop = ShopTable.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			int itemNumber;
			long itemcount;

			for (int i = 0; i < size; i++) {
				itemNumber = readD();
				itemcount = readD();
				if (itemcount <= 0) {
					return;
				}
				if (npcId >= 6100000 && npcId <= 6100035 && !pc.getInventory().getItem(itemNumber).isPackage()) {
					pc.sendPackets(new S_SystemMessage("패킷상점에서 구매하지 않은 아이템이 포함되어 있습니다."));
					return;
				}
				orderList.add(itemNumber, (int) itemcount, pc);
			}
			int bugok = orderList.BugOk();
			if (bugok == 0) {
				shop.buyItems(orderList);
				// 백섭복사 방지 수량성버그방지
				pc.saveInventory();
				// 백섭복사 방지 수량성버그방지
			}

			/********************************************************************************************************
			 ****************************************** 개인 창고 맡기기 **************************************************
			 *********************************************************************************************************/
		} else if (resultType == 2 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
			int objectId, count;
			L1Object object = null;
			L1ItemInstance item = null;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();
				object = pc.getInventory().getItem(objectId);
				item = (L1ItemInstance) object;
				if (item == null)
					return;
				// 창고불가아이템 디비연동 NoShopAndWare
				int itemId = item.getItem().getItemId();
				if (!pc.isGm() && NoShopAndWare.getInstance().isNoShopAndWare(itemId)) {//
					pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."));
					return;
				}

				long nowtime = System.currentTimeMillis();
				if (item.getItemdelay3() >= nowtime) {
					break;
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}

				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}

				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}
	
				if (item.getCount() > 2000000000) {
					return;
				}
				if (count > 2000000000) {
					return;
				}
				/** 창고 맡기기 부분 버그 방지 **/

				if (!item.getItem().isTradable()) {
					tradable = false;
					// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));

				}
				if(!MJCompanionInstanceCache.is_companion_oblivion(item.getId())){				
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					break;
				}
				
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}

				Object[] dollList = pc.getDollList().values().toArray();
				for (Object dollObject : dollList) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							tradable = false;
							// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}

				PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
				if (warehouse == null)
					return;

				if (warehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1상대가 물건을 너무
																// 가지고 있어 거래할 수
																// 없습니다.
					break;
				}

				if (item.getBless() >= 128) {
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					break;
				}

				if (tradable) {
					pc.getInventory().tradeItem(objectId, count, warehouse);
					pc.getLight().turnOnOffLight();
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, true, pc, item, count);
					// [창고맡기기:일반] 큐브 : 단검(1)
					if (count >= 500) {
					} else {
					}
				}
			}

			/********************************************************************************************************
			 ****************************************** 개인 창고 찾기 **************************************************
			 *********************************************************************************************************/
		} else if (resultType == 3 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
			int objectId, count;
			L1ItemInstance item = null;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();

				PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
				if (warehouse == null)
					return;
				
				item = warehouse.getItem(objectId);

				/** 창고 찾기 부분 버그 방지 **/
				if (item == null) {
					return;
				}

				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 30 + 1)) {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다.."));
					return;
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}

				/** 창고 찾기 부분 버그 방지 **/

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) // 용량
																					// 중량
																					// 확인
																					// 및
																					// 메세지
																					// 송신
				{
					if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
						warehouse.tradeItem(item, count, pc.getInventory());
						/** 로그파일저장 **/
						LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, false, pc, item, count);
						if (count >= 500) {
						} else {
						}
					} else {
						pc.sendPackets(new S_ServerMessage(189));
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); 
					break;
				}
			}

			/********************************************************************************************************
			 *************************************** 혈맹 창고 맡기기 *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 4 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
			int objectId, count;
			L1Object object = null;
			L1ItemInstance item = null;
			L1Clan clan = null;
			if (pc.getClanid() != 0) { // 크란 소속
				for (int i = 0; i < size; i++) {
					tradable = true;
					objectId = readD();
					count = readD();

					clan = L1World.getInstance().getClan(pc.getClanid());
					object = pc.getInventory().getItem(objectId);
					item = (L1ItemInstance) object;
					if (item == null) {
						return;
					}
					// 창고불가아이템 디비연동 NoShopAndWare
					int itemId = item.getItem().getItemId();
					if (!pc.isGm() && NoShopAndWare.getInstance().isNoShopAndWare(itemId)) {//
						pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."));
						return;
					}
					long nowtime = System.currentTimeMillis();
					if (item.getItemdelay3() >= nowtime) {
						break;
					}

					if (objectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (!item.isStackable() && count != 1) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (count <= 0 || item.getCount() <= 0) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (item.getCount() > 2000000000) {
						return;
					}
					if (count > 2000000000) {
						return;
					}
					if (count > item.getCount()) {
						count = item.getCount();
					}
					/** 창고 맡기기 부분 버그 방지 **/

					if (item.getBless() >= 128) {
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); 
						return;
					}
					if(!MJCompanionInstanceCache.is_companion_oblivion(item.getId())){				
						tradable = false;
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
						return;
					}
					
					if (clan != null) {
						if (!item.getItem().isTradable()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); 
						}
						Object[] petlist = pc.getPetList().values().toArray();
						for (Object petObject : petlist) {
							if (petObject instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) petObject;
								if (item.getId() == pet.getItemObjId()) {
									tradable = false;
									// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
									pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
									break;
								}
							}
						}
						ClanWarehouse clanWarehouse = WarehouseManager.getInstance()
								.getClanWarehouse(clan.getClanName());
						if (clanWarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
							pc.sendPackets(new S_ServerMessage(75)); 
							break;
						}
						if (tradable) {
							pc.getInventory().tradeItem(objectId, count, clanWarehouse);
							pc.getLight().turnOnOffLight();
							history(pc, item, count, 1);
							/** 로그파일저장 **/
							LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, true, pc, item, count);
							if (count >= 500) {
							} else {
							}
						}
					}
				}
			} else {
				pc.sendPackets(new S_ServerMessage(208)); 
			}

			/********************************************************************************************************
			 *************************************** 혈맹 창고 찾기 *****************************************************
			 *********************************************************************************************************/

		} else if (resultType == 5 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5) {
			// ** 창고이용 5렙으로 수정**//
			if (pc.getInventory().checkEnchantItem(40308, 0, 71)) {
				pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."));
				return;
			}
			int objectId, count;

			L1ItemInstance item;

			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if(clan != null){
				clan.deleteClanRetrieveUser(pc.getId());
			}
			ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());

			if (clan != null) {
				for (int i = 0; i < size; i++) {
					objectId = readD();
					count = readD();
					item = clanWarehouse.getItem(objectId);

					// ** 클랜 창고 찾기 부분 방어 **//
					if (item == null) {
						return;
					}
					if (objectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						return;
					}

					if (!item.isStackable() && count != 1) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (count <= 0 || item.getCount() <= 0 || item.getCount() > 2000000000) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (count >= item.getCount()) {
						count = item.getCount();
					}

					// ** 클랜 창고 찾기 부분 방어 **//

					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 용량
																							// 중량
																							// 확인
																							// 및
																							// 메세지
																							// 송신
						if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30)) {
							clanWarehouse.tradeItem(item, count, pc.getInventory());
							history(pc, item, count, 2);
							/** 로그파일저장 **/
							LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, false, pc, item, count);
							if (count >= 500) {
							} else {
							}
						} else {
							// \f1아데나가 부족합니다.
							pc.sendPackets(new S_ServerMessage(189));
							break;
						}
					} else {
						// \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(270));
						break;
					}
				}
			}

			/**
			 * 크란 창고로 꺼낸다 Cancel, 또는, ESC 키
			 */
		} else if (resultType == 5 && size == 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

			if (pc.hasSkillEffect(L1SkillId.SetBuff)) {
				return;
			}

			if(clan != null){
				clan.deleteClanRetrieveUser(pc.getId());
			}

			/********************************************************************************************************
			 *************************************** 요정 창고 맡기기 *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 8 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc.isElf()) {
			int objectId, count;
			L1Object object = null;
			L1ItemInstance item = null;
			for (int i = 0; i < size; i++) {
				tradable = true;
				objectId = readD();
				count = readD();

				object = pc.getInventory().getItem(objectId);
				item = (L1ItemInstance) object;
				if (item == null) {
					return;
				}
				// 창고불가아이템 디비연동 NoShopAndWare
				int itemId = item.getItem().getItemId();
				if (!pc.isGm() && NoShopAndWare.getInstance().isNoShopAndWare(itemId)) {//
					pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."));
					return;
				}
				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}
				if (item.getCount() > 2000000000) {
					return;
				}
				if (count > 2000000000) {
					return;
				}
				/** 창고 맡기기 부분 버그 방지 **/

				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				}

				if(!MJCompanionInstanceCache.is_companion_oblivion(item.getId())){				
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					return;
				}
				
				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}
				ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
				if (elfwarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75)); // \f1상대가 물건을 너무
																// 가지고 있어 거래할 수
																// 없습니다.
					break;
				}
				if (tradable) {
					pc.getInventory().tradeItem(objectId, count, elfwarehouse);
					pc.getLight().turnOnOffLight();

					/** 로그파일저장 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, true, pc, item, count);
					if (count >= 500) {
					} else {
					}
				}
			}
			/********************************************************************************************************
			 *************************************** 요정 창고 찾기 *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 9 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf") && level >= 5 && pc.isElf()) {
			int objectId, count;
			L1ItemInstance item;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();

				ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
				item = elfwarehouse.getItem(objectId);

				/** 창고 찾기 부분 버그 방지 **/
				if (item == null) {
					return;
				}

				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 30 + 1)) {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다.."));
					return;
				}
				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}
				/** 창고 찾기 부분 버그 방지 **/

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					if (pc.getInventory().consumeItem(40494, 2)) {
						elfwarehouse.tradeItem(item, count, pc.getInventory());
						/** 로그파일저장 **/
						LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, false, pc, item, count);
						if (count >= 500) {
						} else {
						}
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$767"));
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); 
					break;
				}
			}
		} else if (resultType == 10 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
			int objectId, count;
			L1ItemInstance item = null;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfForPackageInventory().getItem(objectId);

				/** 창고 찾기 부분 버그 방지 **/
				if (item == null) {
					return;
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}
				/** 창고 찾기 부분 버그 방지 **/

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { 
					pc.getDwarfForPackageInventory().tradeItem(item, count, pc.getInventory());
				} else {
					pc.sendPackets(new S_ServerMessage(270)); 
					break;
				}
			}
			/********************************************************************************************************
			 *************************************** npc 상점 아이템 구매 ***************************************
			 *********************************************************************************************************/
		} else if (resultType == 0 && size != 0 && isPrivateNpcShop) {
			// 후원유저, 전차보상 유저가 오픈 대기 중에 장비 셋팅(구매)을 할 수 없어서 주석처리
//			if(Config.STANDBY_SERVER){
//				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
//				return;
//			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			
			L1Shop shop = NpcShopTable.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable2.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable3.getInstance().get(npcId);
			
			L1ShopBuyOrderList orderList = shop.newBuyOrderList();
			int itemNumber;
			long itemcount;

			for (int i = 0; i < size; i++) {
				itemNumber = readD();
				itemcount = readD();
				if (itemcount <= 0) {
					return;
				}
				if (size >= 2) { // 동시에 다른물건을 살수없게 2개가 선택된다면,
					pc.sendPackets(new S_SystemMessage("한번에 서로 다른아이템을 구입할 수 없습니다."));
					return;
				}
				if (pc.getMapId() == 800) {
					if (itemcount > 1000) {
						pc.sendPackets(new S_SystemMessage("최대 구매수량 : 잡템류(1000) / 장비(1)"));
						return;
					}
				}
				orderList.add(itemNumber, (int) itemcount, pc);
				if(shop.getSellingItems().size() <= itemNumber){
					pc.sendPackets("현재 구매할 수 없는 상태입니다.");
					return;
				}
				L1ShopItem shopItem = shop.getSellingItems().get(itemNumber);
				LoggerInstance.getInstance().addShop(String.format("+%d %s", shopItem.getEnchant(), shopItem.getItem().getName()),
				(int) itemcount, (long)shopItem.getPrice() * itemcount, npcName, pc.getName());
			}
			int bugok = orderList.BugOk();
			if (bugok == 0) {
				
				shop.sellItems(pc, orderList);
				// 백섭복사 방지 수량성버그방지
				pc.saveInventory();
				// 백섭복사 방지 수량성버그방지
			}
			/********************************************************************************************************
			 *************************************** 개인 상점 아이템 구매 ***************************************
			 *********************************************************************************************************/
		} else if (resultType == 0 && size != 0 && isPrivateShop) {
			if(Config.STANDBY_SERVER){
				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
				return;
			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			
			int order;
			int count;
			int price;
			int itemObjectId;
			int sellPrice;
			int sellCount;
			L1ItemInstance item;

			L1PcInstance targetPc = null;
			if (findObject instanceof L1PcInstance) {
				targetPc = (L1PcInstance) findObject;
			}
			if (targetPc == null) {
				return;
			}
			
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
			ArrayList<MJDShopItem> sells = targetPc.getSellings();
			MJDShopItem	ditem	= null;
			synchronized (sells) {
				// 품절이 발생해, 열람중의 아이템수와 리스트수가 다르다
				if (pc.getPartnersPrivateShopItemCount() != sells.size()) {
					return;
				}

				int[] orders = new int[size];
				int[] counts = new int[size];
				MJDShopItem[] dItems = new MJDShopItem[size];
				for(int i=0; i<size; i++){
					orders[i] = readD();
					counts[i] = readD();
					dItems[i] = sells.get(orders[i]);
				}
				
				for (int i = 0; i < size; i++) { // 구입 예정의 상품
					order = orders[i];
					count = counts[i];
					ditem	= dItems[i];
					//order 		= readD();
					//count 	= readD();
					//ditem		= sells.get(order);
					itemObjectId = ditem.objId;
					sellPrice = ditem.price;
					sellCount = ditem.count;
					item = targetPc.getInventory().getItem(itemObjectId);
					if (item == null) 
						continue;

					long nowtime = System.currentTimeMillis();
					if (item.getItemdelay3() >= nowtime)
						break;
					
					if (count > sellCount)
						count = sellCount;
			
					if (count <= 0)
						continue;
					
					if (item.isEquipped()) {
						pc.sendPackets(new S_ServerMessage(905, "")); 
						continue;
					}

					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { 
						for (int j = 0; j < count; j++) { // 오버플로우를 체크
							if (sellPrice * j > 2000000000 || sellPrice * j < 0) {
								pc.sendPackets(new S_ServerMessage(904, "2000000000"));
								return;
							}
						}
						price = count * sellPrice;

						/** 개인상점 버그방지 **/

						if (itemObjectId != item.getId()) {
							pc.sendPackets(new S_Disconnect());
							targetPc.sendPackets(new S_Disconnect());
							return;
						}
						if (!item.isStackable() && count != 1) {
							pc.sendPackets(new S_Disconnect());
							targetPc.sendPackets(new S_Disconnect());
							return;
						}
						if (count <= 0 || item.getCount() <= 0 || item.getCount() < count) {
							pc.sendPackets(new S_Disconnect());
							targetPc.sendPackets(new S_Disconnect());
							return;
						}
						if (count >= item.getCount()) {
							count = item.getCount();
						}

						if (item.isEquipped()) {
							pc.sendPackets(new S_SystemMessage("상대방이 착용중인 아이템입니다."));
							return;
						}
						if (price <= 0 || price > 2000000000)
							return;
						/** 개인상점 버그방지 **/
						
						if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
							try{
								L1ItemInstance adena = pc.getInventory().findItemId(L1ItemId.ADENA);
								if (targetPc != null && adena != null) {
									if (targetPc.getInventory().tradeItem(item, count, pc.getInventory()) == null) {
										return;
									}
									pc.getInventory().tradeItem(adena, price, targetPc.getInventory());
									
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									targetPc.updateSellings(itemObjectId, count);
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									
									String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";
									targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), message));
									// %1%o %0에 판매했습니다.
									writeLogbuyPrivateShop(pc, targetPc, item, count, price);
									try {
										pc.saveInventory();
										targetPc.saveInventory();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						} else {
							pc.sendPackets(new S_ServerMessage(189)); // \f1아데나가  부족합니다.
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(270)); 
						break;
					}
				}
			}
			/********************************************************************************************************
			 *************************************** Npc 개인 상점 판매
			 * *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 1 && size != 0 && isPrivateNpcShop) { // 개인 상점에 아이템 매각
			if(Config.STANDBY_SERVER){
				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
				return;
			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			
			L1Shop shop = NpcShopTable.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable2.getInstance().get(npcId);
			if(shop == null)
				shop = NpcShopTable3.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			int itemNumber;
			long itemcount;

			for (int i = 0; i < size; i++) {
				itemNumber = readD();
				itemcount = readD();
				if (itemcount <= 0) {
					return;
				}
				orderList.add(itemNumber, (int) itemcount, pc);
			}
			int bugok = orderList.BugOk();
			if (bugok == 0) {
				shop.buyItems(orderList);
				// 백섭복사 방지 수량성버그방지
				pc.saveInventory();
				// 백섭복사 방지 수량성버그방지
			}

		} else if (resultType == 20 && size != 0 && pc == findObject) { // 부가창
			int objectId, count;
			L1ItemInstance item = null;
			SupplementaryService warehouse = WarehouseManager.getInstance()
					.getSupplementaryService(pc.getAccountName());
			if (warehouse == null)
				return;
			if (size > 100)
				return;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = warehouse.getItem(objectId);

				/** 창고 찾기 부분 버그 방지 **/
				if (item == null) {
					return;
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) // 용량
				{
					/** 아이템 시간표기 **/
					if (item.getItem().getItemId() == 3000214) {
						SetDeleteTime(item, 8640);
					} else if (item.getItem().getItemId() == 3000213 || item.getItem().getItemId() >= 758 && item.getItem().getItemId() <= 761) {
						SetDeleteTime(item, 4320); // 3일
					} else if (item.getItem().getItemId() == 3000209 || item.getItem().getItemId() == 41922
							|| item.getItem().getItemId() == 41923 || item.getItem().getItemId() == 41924
							|| item.getItem().getItemId() == 41925 || item.getItem().getItemId() == 41925
							|| item.getItem().getItemId() == 900077
							|| item.getItem().getItemId() == 772 || item.getItem().getItemId() == 773 || item.getItem().getItemId() == 774 || item.getItem().getItemId() == 775) {
						SetDeleteTime(item, 1440); // 1일
					} else if (item.getItem().getItemId() == 210095 || (item.getItem().getItemId() == 2100950)) {
						SetDeleteTime(item, 180); // 3시간
					}
					warehouse.tradeItem(item, count, pc.getInventory());
					pc.saveInventory();
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, false, pc, item, count);
					// [창고찾기:일반] 큐브 : 단검(1)
					if (count >= 500) {
					} else {
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1 가지고 있는 것이
					break;
				}
			}

			/********************************************************************************************************
			 *************************************** 개인 상점 판매 *****************************************************
			 *********************************************************************************************************/
		} else if (resultType == 1 && size != 0 && isPrivateShop) { // 개인 상점에 아이템 매각
			if(Config.STANDBY_SERVER){
				pc.sendPackets("오픈대기 상태에선 해당 행동이 불가능 합니다.");
				return;
			}
			if ((pc.getLevel() >= Config.PC_SHOP && pc.getClanid() <= 0) && !pc.isGm()) {
				pc.sendPackets("\\f2" + Config.PC_SHOP + "레벨 이상: 혈맹에 가입해야만 상점 이용할 수 있습니다.");
				pc.sendPackets("\\f2혈맹 효과: 경험치 +50% 추가 상승");
				return;
			}
			
			int count;
			int order;
			int itemObjectId;
			L1ItemInstance item = null;
			int buyPrice;
			int buyCount;

			L1PcInstance targetPc = null;
			if (findObject instanceof L1PcInstance) {
				targetPc = (L1PcInstance) findObject;
			}
			if (targetPc == null) {
				return;
			}
			
			/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
			ArrayList<MJDShopItem> purs = targetPc.getPurchasings();
			MJDShopItem ditem	= null;
			synchronized (purs) {
				for (int i = 0; i < size; i++) {
					itemObjectId = readD();
					count = readCH();
					order = readC();
					item = pc.getInventory().getItem(itemObjectId);
					if (item == null) {
						continue;
					}

					ditem = purs.get(order);
					buyPrice = ditem.price;
					buyCount = ditem.count;
				if (count > buyCount) {
						count = buyCount;
					}
					int buyItemObjectId = ditem.objId;
					L1ItemInstance buyItem = targetPc.getInventory().getItem(buyItemObjectId);

					if (buyItem == null) {
						return;
					}
					if (item.isEquipped()) {
						pc.sendPackets(new S_ServerMessage(905)); 
						continue;
					}

					if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						for (int j = 0; j < count; j++) { // 오버플로우를 체크
							if (buyPrice * j > 2000000000 || buyPrice * j < 0) {
								targetPc.sendPackets(new S_ServerMessage(904, "2000000000"));
								return;
							}
						}
						/** 버그 방지 **/
						if (itemObjectId != item.getId()) {
							pc.sendPackets(new S_Disconnect());
							targetPc.sendPackets(new S_Disconnect());
							return;
						}

						if (count >= item.getCount()) {
							count = item.getCount();
						}

						if (item.getItemId() != buyItem.getItemId())
							return;
						if (!item.isStackable() && count != 1)
							return;
						if (item.getCount() <= 0 || count <= 0)
							return;
						if (buyPrice * count <= 0 || buyPrice * count > 2000000000)
							return;
						// ** 개인상점 부분 비셔스 방어 **//

						if (targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
							L1ItemInstance adena = targetPc.getInventory().findItemId(L1ItemId.ADENA);
							if (adena != null) {
								targetPc.getInventory().tradeItem(adena, count * buyPrice, pc.getInventory());
								pc.getInventory().tradeItem(item, count, targetPc.getInventory());
								/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								targetPc.updatePurchasings(itemObjectId, count);
								/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								
								try {
									pc.saveInventory();
									targetPc.saveInventory();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							targetPc.sendPackets(new S_ServerMessage(189)); // \f1아데나가부족합니다.
							break;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(271)); // \f1상대가 물건을너무 가지고 있어거래할 수없습니다.
						break;
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return "[C] C_Result";
	}

	private void writeLogbuyPrivateShop(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance item, int count,
			int price) {
		String itemadena = item.getName() + "(" + price + ")";
		/** 로그파일저장 **/
		LoggerInstance.getInstance().개인상점구매(true, pc, targetPc, item, item.getCount());
	}

	private void history(L1PcInstance pc, L1ItemInstance item, int count, int i) {
		StringBuilder itemname = new StringBuilder();
		Connection con = null;
		PreparedStatement pstm = null;
		int clanid = pc.getClanid();
		String char_name = pc.getName();
		int item_enchant = item.getEnchantLevel();
		int elapsed_time = (int) (System.currentTimeMillis() / 1000);
		String type = null;
		if (i == 1) {
			type = "맡겼습니다.";
		} else {
			type = "찾았습니다.";
		}
		if (item.getItem().getType2() != 0) {
			if (item_enchant >= 0) {
				itemname.append("+" + item_enchant + " ");
			} else {
				itemname.append(item_enchant + " ");
			}
		}
		itemname.append(item.getName());
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO clan_warehousehistory SET id =?, clan_id = ?, char_name = ?, item_name = ?, item_count = ?, elapsed_time = ?, item_getorput = ?");
			pstm.setInt(1, IdFactory.getInstance().nextId());
			pstm.setInt(2, clanid);
			pstm.setString(3, char_name);
			pstm.setString(4, itemname.toString());
			pstm.setInt(5, count);
			pstm.setInt(6, elapsed_time);
			pstm.setString(7, type);
			pstm.execute();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void SetDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}
	/**
	 * 월드상에 있는 모든 캐릭의 계정을 비교해 같은 계정이 있다면 true 없다면 false
	 * 
	 * @param c
	 *            L1PcInstance
	 * @return 있다면 true
	 */
	
	private void do_dogfight_buy_ticket(L1PcInstance pc, int size){
		MJDogFightHistory.getInstance().on_buy_ticket(pc, this, size);
	}
	
	private void do_dogfight_sell_ticket(L1PcInstance pc, int size, int npc_object_id){
		MJDogFightHistory.getInstance().on_sell_ticket(pc, this, size, npc_object_id);
	}

}
