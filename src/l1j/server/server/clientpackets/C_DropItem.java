package l1j.server.server.clientpackets;

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NoDropItem;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_DropItem extends ClientBasePacket {

	private static final String C_DROP_ITEM = "[C] C_DropItem";
	/** 날짜 , 시간 기록 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + ":" + hour + ":" + min + "]";

	public C_DropItem(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		int length = 1;
		length = readD();
		for (int i = 0; i < length; ++i) {
			int x = readH();
			int y = readH();
			int objectId = readD();
			int count = readD();

			L1PcInstance pc = client.getActiveChar();

			if (pc == null || pc.isGhost()) {
				return;
			}

			if (pc.getOnlineStatus() != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}

			L1ItemInstance item = pc.getInventory().getItem(objectId);
			if (item != null) {
				long nowtime = System.currentTimeMillis();
				if (item.getItemdelay3() >= nowtime) {
					return;
				}
				if (!pc.isGm() && !item.getItem().isTradable() || item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE
						|| item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					return;
				}

				/** 버그 방지 **/
				int itemType = item.getItem().getType2();
				int itemId = item.getItem().getItemId();

				if ((itemType == 1 && count != 1) || (itemType == 2 && count != 1)) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (item.getCount() < count || count <= 0 || count > 2000000000) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}

				/** 드랍방지 외부화 db nodropitem 테이블 에서 리스트 추가 **/
				if (!pc.isGm() && NoDropItem.getInstance().isNoDropItem(itemId)) {
					String itemName = ItemTable.getInstance().findItemIdByName(itemId);
					pc.sendPackets(new S_SystemMessage("[" + itemName + "] 아이템(은)는 버릴수 없습니다."));
					return;
				}
				// 인첸된 아이템 버릴수 없게 하자!
				if (!pc.isGm() && pc.getLevel() < Config.ALT_DROPLEVELLIMIT) {
					pc.sendPackets(new S_SystemMessage("레벨 " + Config.ALT_DROPLEVELLIMIT + "부터 버릴 수 있습니다."));
					return;
				}
				if (item.getId() >= 0 && (pc.getMapId() == 350 || pc.getMapId() == 340 || pc.getMapId() == 370
						|| pc.getMapId() == 800)) {
					pc.sendPackets(new S_SystemMessage("시장에서는 아이템을 버릴수 없습니다."));
					return;
				}
				
				if (item.getId() == 80500) {
					pc.sendPackets(new S_SystemMessage("훈련소 열쇠는 드랍이 불가능 합니다."));
					return;
				}
				
				
				
				/*
				String[] NonDropItem = null;
				NonDropItem = Config.DROP_MENT_ITEM.split(",");

				for (int j = 0; j < NonDropItem.length; j++) {
					int itemid = 0;
					itemid = Integer.parseInt(NonDropItem[j]);
					if (item.getItemId() == itemid) {
						if (!pc.isGm()) {
							pc.sendPackets("" + item.getItem().getName() + "(은)는 고가의 아이템으로 버릴수없지만 교환은 가능합니다.");
							return;
						}
					}
				}*/

				/** 버그 방지 **/
				if (!pc.isGm() && item.getBless() >= 128) {
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0은
					return;
				}

				if(!MJCompanionInstanceCache.is_companion_oblivion(item.getId())){				
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					return;					
				}
				
				Object[] petlist = pc.getPetList().values().toArray();
				L1PetInstance pet = null;
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							return;
						}
					}
				}

				Object[] dollList = pc.getDollList().values().toArray();
				for (Object dollObject : dollList) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (item.getId() == doll.getItemObjId()) {
							// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							return;
						}
					}
				}

				if (item.isEquipped()) {
					// \f1삭제할 수 없는 아이템이나 장비 하고 있는 아이템은 버릴 수 없습니다.
					pc.sendPackets(new S_ServerMessage(125));
					return;
				}
				if (x > pc.getX() + 1 || x < pc.getX() - 1 || y > pc.getY() + 1 || y < pc.getY() - 1) {
					return;
				}
				int delay_time = 2000;
				if (item != null) {
					if (item.isStackable()) {
						if (item.getItemdelay3() <= nowtime) {
							item.setItemdelay3(nowtime + delay_time);
						}
					}
				}
				if (!pc.isGm())
					item.setGiveItem(true);
				pc.getInventory().tradeItem(item, count, L1World.getInstance().getInventory(x, y, pc.getMapId()));
				pc.getLight().turnOnOffLight();
				/** 파일로그저장 **/
				LoggerInstance.getInstance().addItemAction(ItemActionType.Drop, pc, item, count);
			}
		}
	}

	@Override
	public String getType() {
		return C_DROP_ITEM;
	}

	/*
	 * private boolean isTwoLogin(L1PcInstance c) {// 중복체크 변경 boolean bool =
	 * false; for (L1PcInstance target : L1World.getInstance().getAllPlayers())
	 * { if (target.noPlayerCK || target.noPlayerck2)continue;
	 * 
	 * if(target.getRobotAi() != null) continue;
	 * 
	 * if (c.getId() != target.getId() && !target.isPrivateShop()) { if
	 * (c.getNetConnection().getAccountName().equalsIgnoreCase(target.
	 * getNetConnection().getAccountName())) { bool = true; break; } } } return
	 * bool; }
	 */
}
