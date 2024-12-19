package l1j.server.server.model;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import l1j.server.server.GameServer;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_TradeAddItem;
import l1j.server.server.serverpackets.S_TradeStatus;

public class L1Trade {
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

	private static Logger _log = Logger.getLogger(L1Trade.class.getName());

	public L1Trade() {
	}

	public void TradeAddItem(L1PcInstance player, int itemid, int itemcount) {
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		L1ItemInstance l1iteminstance = player.getInventory().getItem(itemid);
		if (l1iteminstance != null && trading_partner != null) {
			if (!l1iteminstance.isEquipped()) {
				if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) {
					// 허상버그 관련추가
					TradeCancel(player);
					return;
				}

				player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());

				int lv = player.getLevel();
				int currentLvExp = ExpTable.getExpByLevel(lv);
				int nextLvExp = ExpTable.getExpByLevel(lv + 1);
				double neededExp = nextLvExp - currentLvExp;
				double currentExp = player.getExp() - currentLvExp;
				int per = (int) ((currentExp / neededExp) * 100.0);

				if (l1iteminstance.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					String itemName = l1iteminstance.getViewName() + " " + player.getClassName() + "[" + Integer.toString(player.getLevel()) + "]";
					player.sendPackets(new S_TradeAddItem(l1iteminstance, itemName, itemcount, 0));
					trading_partner.sendPackets(new S_TradeAddItem(l1iteminstance, itemName, itemcount, 1));
					player.sendPackets(new S_ChatPacket(player, "--------------------------------------------------"));
					player.sendPackets(new S_ChatPacket(player, "캐릭터 판매중."));
					player.sendPackets(new S_ChatPacket(player, "거래후 거래한금액은 이 계정창고로 들어갑니다."));
					player.sendPackets(new S_ChatPacket(player, "--------------------------------------------------"));

					trading_partner.sendPackets(new S_ChatPacket(player, "--------------------------------------------------"));
					trading_partner.sendPackets(new S_ChatPacket(player, "상태방 캐릭정보."));
					trading_partner.sendPackets(new S_ChatPacket(player, "클래스: [" + player.getClassName() + "] 레벨: [" + Integer.toString(player.getLevel()) + "." + per
							+ "%] 엘릭서: [" + player.getElixirStats() + "]입니다."));
					trading_partner.sendPackets(new S_ChatPacket(player, "--------------------------------------------------"));
				} else {
					player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0));
					trading_partner.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 1));
				}
			}
		}
	}

	public void doCharacterTrade(L1PcInstance player, boolean characterTrade1, L1PcInstance target, boolean characterTrade2) {
		if (player.getNetConnection() == null || target.getNetConnection() == null) {
			player.sendPackets(new S_ChatPacket(player, "거래 대상이 비정상 접속중입니다."));
			target.sendPackets(new S_ChatPacket(player, "거래 대상이 비정상 접속중입니다."));

			TradeCancel(player);

			return;
		}
		if (characterTrade1 && target.getNetConnection().getAccount().countCharacters() >= target.getNetConnection().getAccount().getCharSlot()) {
			player.sendPackets(new S_ChatPacket(player, "거래 대상에게 빈 캐릭터 슬롯이 없습니다."));
			target.sendPackets(new S_ChatPacket(player, "빈 캐릭터 슬롯이 없습니다. 캐릭터 슬롯을 확보하고 다시 시도해주시기 바랍니다."));

			TradeCancel(player);

			return;
		}

		if (characterTrade2 && player.getNetConnection().getAccount().countCharacters() >= player.getNetConnection().getAccount().getCharSlot()) {
			target.sendPackets(new S_ChatPacket(player, "거래 대상에게 빈 캐릭터 슬롯이 없습니다."));
			player.sendPackets(new S_ChatPacket(player, "빈 캐릭터 슬롯이 없습니다. 캐릭터 슬롯을 확보하고 다시 시도해주시기 바랍니다."));

			TradeCancel(player);
			return;
		}

		if (characterTrade1) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());
			if (warehouse == null) {
				TradeCancel(player);
				return;
			}

			for (L1ItemInstance item : target.getTradeWindowInventory().getItems()) {
				if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == L1Inventory.SIZE_OVER) {
					target.sendPackets(new S_ServerMessage(75));
					// \f1상대가 물건을너무 가지고 있어거래할 수 없습니다.
					TradeCancel(player);
					return;
				}
			}
		}

		if (characterTrade2) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());

			if (warehouse == null) {
				TradeCancel(player);

				return;
			}

			for (L1ItemInstance item : player.getTradeWindowInventory().getItems()) {
				if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == L1Inventory.SIZE_OVER) {
					player.sendPackets(new S_ServerMessage(75));
					// \f1상대가 물건을 너무 가지고 있어거래할 수없습니다.
					TradeCancel(player);
					return;
				}
			}
		}

		if (characterTrade1) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());

			while (target.getTradeWindowInventory().getItems().size() > 0) {
				L1ItemInstance item = (L1ItemInstance) target.getTradeWindowInventory().getItems().get(0);
				if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
				} else {
					target.getTradeWindowInventory().tradeItem(item, item.getCount(), warehouse);
				}
			}

			if (!characterTrade2) {
				while (player.getTradeWindowInventory().getItems().size() > 0) {
					L1ItemInstance item = (L1ItemInstance) player.getTradeWindowInventory().getItems().get(0);
					if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
						player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
					} else {
						player.getTradeWindowInventory().tradeItem(item, item.getCount(), player.getInventory());
					}
				}
			}
		}

		if (characterTrade2) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());
			while (player.getTradeWindowInventory().getItems().size() > 0) {
				L1ItemInstance item = (L1ItemInstance) player.getTradeWindowInventory().getItems().get(0);
				if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
				} else {
					player.getTradeWindowInventory().tradeItem(item, item.getCount(), warehouse);
				}
			}

			if (!characterTrade1) {
				while (target.getTradeWindowInventory().getItems().size() > 0) {
					L1ItemInstance item = (L1ItemInstance) target.getTradeWindowInventory().getItems().get(0);
					if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
						target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
					} else {
						target.getTradeWindowInventory().tradeItem(item, item.getCount(), target.getInventory());
					}
				}
			}
		}

		player.sendPackets(new S_TradeStatus(0));
		target.sendPackets(new S_TradeStatus(0));
		player.setTradeOk(false);
		target.setTradeOk(false);
		player.setTradeID(0);
		target.setTradeID(0);
		player.getLight().turnOnOffLight();
		target.getLight().turnOnOffLight();

		String playerAccountName = player.getAccountName();
		String targetAccountName = target.getAccountName();

		if (characterTrade1) {
			player.setAccountName(targetAccountName);
			try {
				CharacterTable.getInstance().updateCharacterAccount(player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (characterTrade2) {
			target.setAccountName(playerAccountName);
			try {
				CharacterTable.getInstance().updateCharacterAccount(target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		GameServer.disconnectChar(player);
		GameServer.disconnectChar(target);
	}

	public void TradeOK(L1PcInstance player) {
		int cnt;
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
			List<?> player_tradelist 			= player.getTradeWindowInventory().getItems();
			int player_tradecount 				= player_tradelist.size();
			List<?> trading_partner_tradelist 	= trading_partner.getTradeWindowInventory().getItems();
			//int trading_partner_tradecount 		= trading_partner.getTradeWindowInventory().getSize();
			int trading_partner_tradecount 		= trading_partner_tradelist.size();
			
			L1ItemInstance l1iteminstance1 		= null;
			L1ItemInstance l1iteminstance2 		= null;

			// 캐릭 교환인지 본다.

			boolean characterTrade1 = false;
			boolean characterTrade2 = false;

			for (cnt = 0; cnt < player_tradecount; cnt++) {
				l1iteminstance1 = (L1ItemInstance) player_tradelist.get(cnt);

				if (l1iteminstance1.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance1.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					characterTrade1 = true;
					break;
				}
			}
			for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
				l1iteminstance2 = (L1ItemInstance) trading_partner_tradelist.get(cnt);
				if (l1iteminstance2.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance2.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					characterTrade2 = true;
					break;
				}
			}

			if (characterTrade1 || characterTrade2) {
				doCharacterTrade(player, characterTrade1, trading_partner, characterTrade2);
			} else {
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					l1iteminstance1 = (L1ItemInstance) player_tradelist.get(0);
					player.getTradeWindowInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), trading_partner.getInventory());

					/** 로그파일저장 **/
					LoggerInstance.getInstance().addTrade(true, player, trading_partner, l1iteminstance1, l1iteminstance1.getCount());

				}
				for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
					l1iteminstance2 = (L1ItemInstance) trading_partner_tradelist.get(0);
					trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance2, l1iteminstance2.getCount(), player.getInventory());
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addTrade(true, trading_partner, player, l1iteminstance2, l1iteminstance2.getCount());

				}

				player.sendPackets(new S_TradeStatus(0));
				trading_partner.sendPackets(new S_TradeStatus(0));
				player.setTradeOk(false);
				trading_partner.setTradeOk(false);
				player.setTradeID(0);
				trading_partner.setTradeID(0);
				player.getLight().turnOnOffLight();
				trading_partner.getLight().turnOnOffLight();
			}
		} else {
			TradeCancel(player);
		}
	}

	public void TradeCancel(L1PcInstance player) {
		int cnt;

		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());

		{
			List<?> player_tradelist = player.getTradeWindowInventory().getItems();
			int player_tradecount = player.getTradeWindowInventory().getSize();

			L1ItemInstance l1iteminstance1 = null;
			for (cnt = 0; cnt < player_tradecount; cnt++) {
				l1iteminstance1 = (L1ItemInstance) player_tradelist.get(0);
				player.getTradeWindowInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), player.getInventory());
				/** 로그파일저장 **/
				LoggerInstance.getInstance().addTrade(false, player, trading_partner, l1iteminstance1, l1iteminstance1.getCount());
			}

			player.sendPackets(new S_TradeStatus(1));
			player.setTradeOk(false);
			player.setTradeID(0);
		}

		if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
			List<?> trading_partner_tradelist = trading_partner.getTradeWindowInventory().getItems();
			int trading_partner_tradecount = trading_partner.getTradeWindowInventory().getSize();
			L1ItemInstance l1iteminstance2 = null;
			for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
				l1iteminstance2 = (L1ItemInstance) trading_partner_tradelist.get(0);
				trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance2, l1iteminstance2.getCount(), trading_partner.getInventory());
				/** 로그파일저장 **/
				LoggerInstance.getInstance().addTrade(false, trading_partner, player, l1iteminstance2, l1iteminstance2.getCount());
			}

			trading_partner.sendPackets(new S_TradeStatus(1));
			trading_partner.setTradeOk(false);
			trading_partner.setTradeID(0);
		}
	}
}
