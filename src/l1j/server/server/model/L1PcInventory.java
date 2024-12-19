package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.QuestInfoTable.QuestItemTemp;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_AddItem;
import l1j.server.server.serverpackets.S_ChangeItemUseType;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_FishingAddItem;
import l1j.server.server.serverpackets.S_ItemColor;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_Weight;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1QuestInfo;

public class L1PcInventory extends L1Inventory {

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
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1PcInventory.class.getName());

	private static final int MAX_SIZE = 180;

	private final L1PcInstance _owner;

	private int _arrowId;

	private int _stingId;

	private long timeVisible = 0;
	private long timeVisibleDelay = 3000;
	private int _teleportRulerCount;

	public L1PcInventory(L1PcInstance owner) {
		_owner = owner;
		_arrowId = 0;
		_stingId = 0;
		_teleportRulerCount = 0;
	}

	public void setTimeVisible(long l) {
		timeVisible = l;
	}

	public L1PcInstance getOwner() {
		return _owner;
	}

	public int getWeight100() {
		return calcWeight100(getWeight());
	}

	public int calcWeight100(int weight) {
		return weight * 100 / _owner.getMaxWeight();
	}

	@Override
	public int checkAddItem(L1Item item, int count) {
		int code = super.checkAddItem(item, count);
		if (code == OK) {
			int weight = getWeight() + item.getWeight() * count / 1000 + 1;
			if (calcWeight100(weight) >= 240)
				return WEIGHT_OVER;
		}
		return code;
	}

	@Override
	public int checkAddItem(L1ItemInstance item, int count) {
		return checkAddItem(item, count, true);
	}

	public int checkAddItem(L1ItemInstance item, int count, boolean message) {
		if (item == null) {
			return -1;
		}

		if (count < 0 || count > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		if (getSize() > MAX_SIZE || (getSize() == MAX_SIZE && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
			if (message) {
				sendOverMessage(263);
			}
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000 + 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			if (message) {
				sendOverMessage(82); // 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			}
			return WEIGHT_OVER;
		}
		if (calcWeight100(weight) >= 240) {
			if (message) {
				sendOverMessage(82); // 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			}
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && ((itemExist.getCount() + count) < 0 || (itemExist.getCount() + count) > MAX_AMOUNT)) {
			if (message) {
				getOwner().sendPackets(new S_ServerMessage(166, "소지하고 있는 아데나", "2,000,000,000을 초과하고 있습니다."));
				// \f1%0이%4%1%3%2
			}
			return AMOUNT_OVER;
		}

		return OK;
	}

	public void sendOverMessage(int message_id) {
		_owner.sendPackets(new S_ServerMessage(message_id));
	}

	public void sendOptioon() {
		try {
			for (L1ItemInstance item : _items) {
				if (item.isEquipped()) {
					item.setEquipped(false);
					_owner.getEquipSlot().removeSetItems(item.getItemId());
					setEquipped(item, true, true, false, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadItems() {
		try {
			CharactersItemStorage storage = CharactersItemStorage.create();

			for (L1ItemInstance item : storage.loadItems(_owner.getId())) {
				item._cha = _owner;
				if (item.getItemId() == L1ItemId.ADENA) {
					L1ItemInstance itemExist = findItemId(item.getItemId());

					if (itemExist != null) {
						storage.deleteItem(item);

						int newCount = itemExist.getCount() + item.getCount();

						if (newCount <= MAX_AMOUNT) {
							if (newCount < 0) {
								newCount = 0;
							}
							itemExist.setCount(newCount);

							storage.updateItemCount(itemExist);
						}
					} else {
						_items.add(item);
						L1World.getInstance().storeObject(item);
					}
				} else {
					if (item.getItemId() == 900111) {
						if (++_teleportRulerCount == 1) {
							_owner.setSkillEffect(L1SkillId.TELEPORT_RULER, -1);
							SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
							noti.set_noti_type(eNotiType.NEW);
							noti.set_spell_id(L1SkillId.TELEPORT_RULER);
							noti.set_duration(1);
							noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
							noti.set_on_icon_id(L1SkillId.TELEPORT_RULER);
							noti.set_off_icon_id(0x00);
							noti.set_icon_priority(3);
							noti.set_tooltip_str_id(5119);
							noti.set_new_str_id(0);
							noti.set_end_str_id(0);
							noti.set_is_good(true);
							_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
						}
					}
					_items.add(item);
					L1World.getInstance().storeObject(item);
				}
				/** 변신 지배 반지 **/
                if (item.getItem().getUseType() == 79 && _owner.getInventory().checkItem(900075)){
                	_owner.sendPackets(new S_Ability(7, true));	
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertItem(L1ItemInstance item) {
		item._cha = _owner;
		if (item.getItemId() == 900111) {
			if (++_teleportRulerCount == 1) {
				_owner.setSkillEffect(L1SkillId.TELEPORT_RULER, -1);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.NEW);
				noti.set_spell_id(L1SkillId.TELEPORT_RULER);
				noti.set_duration(1);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
				noti.set_on_icon_id(L1SkillId.TELEPORT_RULER);
				noti.set_off_icon_id(0x00);
				noti.set_icon_priority(3);
				noti.set_tooltip_str_id(5119);
				noti.set_new_str_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
			}
		}
		
		/**변신 지배반지*/
		if (item.getItemId() == 900075) {
			_owner.sendPackets(new S_Ability(7, true));
			_owner.sendPackets(new S_Ability(2, true));
		}
				
		if (_owner.isFishing()) {
			switch (item.getItemId()) {
			case 41297:
			case 41296:
			case 41301:
			case 41304:
			case 41303:
			case 600230:
			case 820018:
			case 49092:
			case 49093:
			case 49094:
			case 49095:{
				SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
				noti.set_owner_oid(_owner.getId());
				noti.set_on_start(false);
				noti.add_item_info(ItemInfo.newInstance(item));
				_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
			}
				/*try {
					_owner.sendPackets(S_FishingAddItem.get(item));
				} catch (Exception e1) {
					e1.printStackTrace();
				}*/
				break;
			default:
			{
				SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
				noti.set_owner_oid(_owner.getId());
				noti.set_on_start(false);
				noti.add_item_info(ItemInfo.newInstance(item));
				_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
//				_owner.sendPackets(new S_AddItem(_owner, item));
			}
				break;
			}
		} else {
			
			SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
			noti.set_owner_oid(_owner.getId());
			noti.set_on_start(false);
			ItemInfo iInfo = null;
			if(item.getItemId() == 40100 && _owner.getInventory().checkEquippedAtOnce(new int[] { 900111, 20288 })){
				iInfo = ItemInfo.newInstance(item, 6);
			}else{
				iInfo = ItemInfo.newInstance(item);
			}
			noti.add_item_info(iInfo);
			_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
			
			/*
			if (item.getItem().isEndedTimeMessage()) {
				try {
					_owner.sendPackets(S_FishingAddItem.get(item));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				_owner.sendPackets(new S_AddItem(_owner, item));
			}*/
		}

		// _owner.sendPackets(new S_AddItem(item));
		if (item.getItem().getWeight() != 0) {
			_owner.sendPackets(new S_Weight(_owner));
		}

		if (!item.getItem().isEndedTimeMessage()) {
			if (item.getEnchantLevel() > 0 || item.getAttrEnchantLevel() > 0) {
				_owner.sendPackets(new S_ItemStatus(item));
				_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			}
		}
		
		if (item.getItemId() == 700024){
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getId(), "$13719",
					L1BookMark.ShowBookmarkitem(_owner, item.getItemId())));
		}
		
		try {
			CharactersItemStorage storage = CharactersItemStorage.create();
			storage.storeItem(_owner.getId(), item);

			// 클라우디아
			int itemId = item.getItem().getItemId();
			if (item.getItemId() == 6000 // 수련자의 식량 주머니
					|| item.getItemId() == 6001 // 뼈 조각
					|| item.getItemId() == 6002 // 무기 도면
					|| item.getItemId() == 6007 // 수련자의 은무기 상자
					|| item.getItemId() == 6008 // 이상한 뼈조각
					|| item.getItemId() == 6009 // 이상한 장신구
					|| item.getItemId() == 6010 // 이상한 눈
					|| item.getItemId() == 6011 // 단단한 실
					|| item.getItemId() == 6012 // 끈적한 거미줄
					|| item.getItemId() == 6013 // 늑대 가죽
					|| item.getItemId() == 6014 // 대지의 원소(소)
					|| item.getItemId() == 6015 // 대지의 원소(대)
					|| item.getItemId() == 6018 // 불의 원소(소)
					|| item.getItemId() == 6019 // 불의 원소(대)
					|| item.getItemId() == 6020 // 물의 원소(소)
					|| item.getItemId() == 6021 // 물의 원소(대)
					|| item.getItemId() == 6022 // 공기의 원소(소)
					|| item.getItemId() == 6023 // 공기의 원소(대)
			) {
				questitem(itemId, item.getCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
	}

	public static final int COL_SAVE_ALL = 4096;
	public static final int COL_SPECIAL_ENCHANT = 2048;
	public static final int COL_ATTRENCHANTLVL = 1024;
	public static final int COL_BLESS_LEVEL = 8192;
	public static final int COL_BLESS = 512;
	public static final int COL_REMAINING_TIME = 256;
	public static final int COL_CHARGE_COUNT = 128;
	public static final int COL_ITEMID = 64;
	public static final int COL_DELAY_EFFECT = 32;
	public static final int COL_COUNT = 16;
	public static final int COL_EQUIPPED = 8;
	public static final int COL_ENCHANTLVL = 4;
	public static final int COL_IS_ID = 2;
	public static final int COL_DURABILITY = 1;

	@Override
	public void updateItem(L1ItemInstance item) {
		updateItem(item, COL_COUNT);
		if (item.getItem().isToBeSavedAtOnce()) {
			saveItem(item, COL_COUNT);
		}
	}

	/**
	 * 목록내의 아이템 상태를 갱신한다.
	 * 
	 * @param item
	 *            - 갱신 대상의 아이템
	 * @param column
	 *            - 갱신하는 스테이터스의 종류
	 */
	@Override
	public void updateItem(L1ItemInstance item, int column) {
		if (column >= COL_SPECIAL_ENCHANT) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_SPECIAL_ENCHANT;
		}
		if (column >= COL_BLESS_LEVEL) {
			_owner.sendPackets(new S_ItemStatus(item));
			column -= COL_BLESS_LEVEL;
		}
		if (column >= COL_ATTRENCHANTLVL) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			column -= COL_ATTRENCHANTLVL;
		}
		if (column >= COL_BLESS) {
			_owner.sendPackets(new S_ItemColor(item));
			column -= COL_BLESS;
		}
		if (column >= COL_REMAINING_TIME) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_REMAINING_TIME;
		}
		if (column >= COL_CHARGE_COUNT) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_CHARGE_COUNT;
		}
		if (column >= COL_ITEMID) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_ItemColor(item));
			_owner.sendPackets(new S_Weight(_owner));
			column -= COL_ITEMID;
		}
		if (column >= COL_DELAY_EFFECT) {
			column -= COL_DELAY_EFFECT;
		}
		if (column >= COL_COUNT) {
			// _owner.sendPackets(new S_ItemAmount(item));
			_owner.sendPackets(new S_ItemStatus(item));

			int weight = item.getWeight();
			if (weight != item.getLastWeight()) {
				item.setLastWeight(weight);
				_owner.sendPackets(new S_ItemStatus(item));
			} else {
				_owner.sendPackets(new S_ItemName(item));
			}
			if (item.getItem().getWeight() != 0) {
				_owner.sendPackets(new S_Weight(_owner));
			}
			column -= COL_COUNT;

			// 클라우디아
			if (item.getItemId() == 6000 // 수련자의 식량 주머니
					|| item.getItemId() == 6001 // 뼈 조각
					|| item.getItemId() == 6002 // 무기 도면
					|| item.getItemId() == 6007 // 수련자의 은무기 상자
					|| item.getItemId() == 6008 // 이상한 뼈조각
					|| item.getItemId() == 6009 // 이상한 장신구
					|| item.getItemId() == 6010 // 이상한 눈
					|| item.getItemId() == 6011 // 단단한 실
					|| item.getItemId() == 6012 // 끈적한 거미줄
					|| item.getItemId() == 6013 // 늑대 가죽
					|| item.getItemId() == 6014 // 대지의 원소(소)
					|| item.getItemId() == 6015 // 대지의 원소(대)
					|| item.getItemId() == 6018 // 불의 원소(소)
					|| item.getItemId() == 6019 // 불의 원소(대)
					|| item.getItemId() == 6020 // 물의 원소(소)
					|| item.getItemId() == 6021 // 물의 원소(대)
					|| item.getItemId() == 6022 // 공기의 원소(소)
					|| item.getItemId() == 6023 // 공기의 원소(대)
			) {
				questitem(item.getItemId(), item.getCount());
			}
		}
		if (column >= COL_EQUIPPED) {
			_owner.sendPackets(new S_ItemName(item));
			column -= COL_EQUIPPED;
		}
		if (column >= COL_ENCHANTLVL) {
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_PacketBox(S_PacketBox.ITEM_ENCHANT_UPDATE, item));
			column -= COL_ENCHANTLVL;
		}
		if (column >= COL_IS_ID) {
			item._cha = _owner;
			_owner.sendPackets(new S_ItemStatus(item));
			_owner.sendPackets(new S_ItemColor(item));
			column -= COL_IS_ID;
		}
		if (column >= COL_DURABILITY) {
			_owner.sendPackets(new S_ItemStatus(item));
			column -= COL_DURABILITY;
		}
	}

	/**
	 * 목록내의 아이템 상태를 DB에 보존한다.
	 * 
	 * @param item
	 *            - 갱신 대상의 아이템
	 * @param column
	 *            - 갱신하는 스테이터스의 종류
	 */
	public void saveItem(L1ItemInstance item, int column) {
		if (column == 0) {
			return;
		}

		if (_owner != null && _owner.getAI() != null)
			return;

		try {
			CharactersItemStorage storage = CharactersItemStorage.create();

			if (column >= COL_SAVE_ALL) {
				storage.updateItemAll(item);
				return;
			}
			if (column >= COL_SPECIAL_ENCHANT) {
				storage.updateSpecialEnchant(item);
				column -= COL_SPECIAL_ENCHANT;
			}
			if (column >= COL_ATTRENCHANTLVL) {
				storage.updateItemAttrEnchantLevel(item);
				column -= COL_ATTRENCHANTLVL;
			}
			if (column >= COL_BLESS_LEVEL) {
				storage.updateItemBlessLevel(item);
				column -= COL_BLESS_LEVEL;
			}
			if (column >= COL_BLESS) {
				storage.updateItemBless(item);
				column -= COL_BLESS;
			}
			if (column >= COL_REMAINING_TIME) {
				storage.updateItemRemainingTime(item);
				column -= COL_REMAINING_TIME;
			}
			if (column >= COL_CHARGE_COUNT) {
				storage.updateItemChargeCount(item);
				column -= COL_CHARGE_COUNT;
			}
			if (column >= COL_ITEMID) {
				storage.updateItemId(item);
				column -= COL_ITEMID;
			}
			if (column >= COL_DELAY_EFFECT) {
				storage.updateItemDelayEffect(item);
				column -= COL_DELAY_EFFECT;
			}
			if (column >= COL_COUNT) {
				storage.updateItemCount(item);
				column -= COL_COUNT;
			}
			if (column >= COL_EQUIPPED) {
				storage.updateItemEquipped(item);
				column -= COL_EQUIPPED;
			}
			if (column >= COL_ENCHANTLVL) {
				storage.updateItemEnchantLevel(item);
				column -= COL_ENCHANTLVL;
			}
			if (column >= COL_IS_ID) {
				storage.updateItemIdentified(item);
				column -= COL_IS_ID;
			}
			if (column >= COL_DURABILITY) {
				storage.updateItemDurability(item);
				column -= COL_DURABILITY;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteItem(L1ItemInstance item) {
		if (item.getItemId() == 900111) {
			if (--_teleportRulerCount == 0) {
				_owner.removeSkillEffect(L1SkillId.TELEPORT_RULER);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(L1SkillId.TELEPORT_RULER);
				noti.set_off_icon_id(0x00);
				noti.set_end_str_id(0);
				_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
			}
		}

		try {
			CharactersItemStorage storage = CharactersItemStorage.create();
			storage.deleteItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (item.isEquipped()) {
			setEquipped(item, false);
		}
		_owner.sendPackets(new S_DeleteInventoryItem(item));
		_items.remove(item);
		if (item.getItem().getWeight() != 0) {
			_owner.sendPackets(new S_Weight(_owner));
		}
		
		/** 변신 지배 반지 **/
        if (item.getItem().getUseType() == 79 && !_owner.getInventory().checkItem(900075)){
        	_owner.sendPackets(new S_Ability(7, false));
        	if (_owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER)){
        	    _owner.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
        	}
		}
        
        if (item.getItemId() == 140009 && !_owner.getInventory().checkItem(140009)) {
        	if (_owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER)){
        	    _owner.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
        	}
        	if (_owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)){
        	    _owner.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
        	}
        }
	}

	public L1ItemInstance getItemEquippend(int itemId) {// 아이템 착용 상태 확인의 오브젝트 인식
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == itemId && item.isEquipped()) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public void setEquipped(L1ItemInstance item, boolean equipped) {
		setEquipped(item, equipped, false, false, false);
	}

	public L1ItemInstance getEquippedItem(int itemId) {
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == itemId) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public void setEquipped(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon, boolean shieldWeapon) {
		if (item.isEquipped() != equipped) {
			L1Item temp = item.getItem();
			if (equipped) {
				if (temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077) {
					if (System.currentTimeMillis() - timeVisible < timeVisibleDelay) {
						return;
					}
				}

				if (item.getItem().getType() != 4 && _owner.getInventory().getArrowItemId() != 0) {
					L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getArrowItemId());
					if (arrow != null) {
						_owner.getInventory().setArrow(0);
						_owner.sendPackets(new S_ItemName(arrow));
					}
				}

				item.onEquip(_owner);
				item.setEquipped(true);
				_owner.getEquipSlot().set(item);
			} else {
				if (!loaded) {
					if (temp.getItemId() == 20077 || temp.getItemId() == 20062 || temp.getItemId() == 120077) {
						if (_owner.isInvisble()) {
							_owner.delInvis();
							// return;
						}
						timeVisible = System.currentTimeMillis();
					}
				}
				if(item.getItem().getType2() == 1 && item.getItem().getType1() == 4) {
					if (_owner.hasSkillEffect(L1SkillId.INFERNO)) {
						_owner.removeSkillEffect(L1SkillId.INFERNO);
					}
				}
				// 양손검을 착용해제 했을때 카운터배리어 효과해제
				if (item.getItem().isTwohandedWeapon()) {
					if (_owner.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
						_owner.removeSkillEffect(L1SkillId.COUNTER_BARRIER);
						_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 71, false));
					}
				}
				if (item.getItem().getType2() == 1) {
					_owner.remove_elf_second_brave();
					if (item.getItem().getType() == 4 && _owner.getInventory().getArrowItemId() != 0) {
						L1ItemInstance arrow = _owner.getInventory().findItemId(_owner.getInventory().getArrowItemId());
						_owner.getInventory().setArrow(0);
						_owner.sendPackets(new S_ItemName(arrow));
					}
				}

				item.setEquipped(false);
				_owner.getEquipSlot().remove(item);

				item.onUnEquip();
			}
			if (!loaded) {
				_owner.setCurrentHp(_owner.getCurrentHp());
				_owner.setCurrentMp(_owner.getCurrentMp());
				updateItem(item, COL_EQUIPPED);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
				if (temp.getType2() == 1 && changeWeapon == false) {
					_owner.sendPackets(new S_CharVisualUpdate(_owner));
					_owner.broadcastPacket(new S_CharVisualUpdate(_owner));
				}
			}
			if (item.getItem().getType2() == 1)
				_owner.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, _owner, item), true);
			// 아이템 착용 처리에 대한 패킷 처리.
			_owner.getInventory().toSlotPacket(_owner, item, false);
			// 아이템패킷추가
		}
	}

	public boolean checkEquipped(int id) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.isEquipped()) {
				return true;
			}
		}
		return false;
	}

	/** '축복받은'이 아이템 접두어로 있다면 이를 제거하고 아이템 이름 비교. **/
	public int getNameEquipped(int type2, int type, String name) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		String tName = null;
		String aName = null;
		if (name.indexOf("축복받은") != -1) {
			aName = name.replace("축복받은 ", "");
		} else {
			aName = name;
		}

		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				if (item != null) {
					tName = item.getName();
					if (tName.indexOf("축복받은") != -1)
						tName = tName.replace("축복받은 ", "");
					if (tName.equals(aName))
						equipeCount++;
				}
			}
		}
		return equipeCount;
	}

	public int getTypeEquipped(int type2, int type) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				equipeCount++;
			}
		}
		return equipeCount;
	}

	public L1ItemInstance getItemEquipped(int type2, int type) {
		L1ItemInstance equipeitem = null;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
				equipeitem = item;
				break;
			}
		}
		return equipeitem;
	}

	public L1ItemInstance[] getRingEquipped() {
		L1ItemInstance equipeItem[] = new L1ItemInstance[4];
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getType2() == 2 && item.getItem().getType() == 9 && item.isEquipped()) {
				equipeItem[equipeCount] = item;
				equipeCount++;
				if (equipeCount == 4) {
					break;
				}
			}
		}
		return equipeItem;
	}

	public void takeoffEquip(int polyid) {
		takeoffWeapon(polyid);
		takeoffArmor(polyid);
	}

	private void takeoffWeapon(int polyid) {
		if (_owner.getWeapon() == null) {
			return;
		}

		boolean takeoff = false;
		int weapon_type = _owner.getWeapon().getItem().getType();
		takeoff = !L1PolyMorph.isEquipableWeapon(polyid, weapon_type);

		if (takeoff) {
			setEquipped(_owner.getWeapon(), false, false, false, false);
		}

		if (_owner.getSecondWeapon() != null) {// 변신버그추가
			boolean second_takeoff = false;
			int second_weapon_type = _owner.getSecondWeapon().getItem().getType();
			second_takeoff = !L1PolyMorph.isEquipableWeapon(polyid, second_weapon_type);

			if (second_takeoff) {
				setEquipped(_owner.getSecondWeapon(), false, false, false, false);
			}
		} // 변신버그추가

	}

	private void takeoffArmor(int polyid) {
		L1ItemInstance armor = null;

		for (int type = 0; type <= 12; type++) {
			if (getTypeEquipped(2, type) != 0 && !L1PolyMorph.isEquipableArmor(polyid, type)) {
				if (type == 9) {
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
				} else {
					armor = getItemEquipped(2, type);
					if (armor != null) {
						setEquipped(armor, false, false, false, false);
					}
				}
			}
		}
	}

	/** 로봇시스템 **/
	private L1ItemInstance _arrow;

	public L1ItemInstance getArrow() {
		if (_owner.getAI() != null) {
			if (_arrow == null) {
				_arrow = ItemTable.getInstance().createItem(3000516);
			}
			_arrow.setCount(2);
			return _arrow;
		} else {
			return getBullet(0);
		}
	}

	/** 로봇시스템 **/

	public L1ItemInstance getSting() {
		return getBullet(15);
	}

	// 스팅 타입수정
	private L1ItemInstance getBullet(int type) {
		L1ItemInstance bullet;
		int priorityId = 0;
		if (type == 0) {
			priorityId = _arrowId;
		}
		if (type == 15) {
			priorityId = _stingId;
		}
		if (priorityId > 0) {
			bullet = findItemId(priorityId);
			if (bullet != null) {
				return bullet;
			} else {
				if (type == 0) {
					_arrowId = 0;
				}
				if (type == 15) {
					_stingId = 0;
				}
			}
		}

		for (Object itemObject : _items) {
			bullet = (L1ItemInstance) itemObject;
			if (bullet.getItem().getType() == type) {
				if (type == 0) {
					_arrowId = bullet.getItem().getItemId();
				}
				if (type == 15) {
					_stingId = bullet.getItem().getItemId();
				}
				_owner.sendPackets(new S_ItemName(bullet));
				_owner.sendPackets(new S_ServerMessage(452, bullet.getName()));
				return bullet;
			}
		}
		return null;
	}

	public int getArrowItemId() {
		return _arrowId;
	}

	public void setArrow(int id) {
		_arrowId = id;
	}

	public int getStingItemId() {
		return _stingId;
	}

	public void setSting(int id) {
		_stingId = id;
	}

	public int hpRegenPerTick() {
		int hpr = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.isEquipped()) {
				hpr += item.getItem().get_addhpr();
			}
		}
		return hpr;
	}

	public int mpRegenPerTick() {
		int mpr = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.isEquipped()) {
				mpr += item.getItem().get_addmpr();
			}
		}
		return mpr;
	}

	public ArrayList<L1ItemInstance> getPossibleDropItems() {
		ArrayList<L1ItemInstance> possibles = new ArrayList<L1ItemInstance>(_items.size());
		Object[] petlist = _owner.getPetList().values().toArray();
		Object[] dollList = _owner.getDollList().values().toArray();
		HashMap<Integer, Integer> impossibles = new HashMap<Integer, Integer>();
		if(_owner.get_companion() != null){
			MJCompanionInstance companion = _owner.get_companion();
			impossibles.put(companion.get_control_object_id(), companion.get_control_object_id());
		}
		
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				Integer itg = pet.getItemObjId();
				impossibles.put(itg, itg);
			}
		}
		for (Object dollObject : dollList) {
			if (dollObject instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) dollObject;
				Integer itg = doll.getItemObjId();
				impossibles.put(itg, itg);
			}
		}

		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == L1ItemId.ADENA || !item.getItem().isTradable()) {
				continue;
			}
			if (impossibles.containsKey(item.getId())) {
				continue;
			}
			possibles.add(item);
		}
		return possibles;
	}

	// 해당아이템은 드랍불가
	public L1ItemInstance CaoPenalty() {
		Random random = new Random(System.nanoTime());
		int rnd = random.nextInt(_items.size());
		L1ItemInstance penaltyItem = _items.get(rnd);
		if (penaltyItem.getItem().getItemId() == L1ItemId.ADENA || penaltyItem.getItem().getItemId() == 80500 || !penaltyItem.getItem().isTradable()
		 || penaltyItem.getEndTime() != null) {
			return null;
		}
		if(!MJCompanionInstanceCache.is_companion_oblivion(penaltyItem.getId()))			
			return null;
		
		Object[] petlist = _owner.getPetList().values().toArray();
		L1PetInstance pet = null;
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				pet = (L1PetInstance) petObject;
				if (penaltyItem.getId() == pet.getItemObjId()) {
					return null;
				}
			}
		}
		Object[] dollList = _owner.getDollList().values().toArray();
		L1DollInstance doll = null;
		for (Object dollObject : dollList) {
			if (dollObject instanceof L1DollInstance) {
				doll = (L1DollInstance) dollObject;
				if (penaltyItem.getId() == doll.getItemObjId()) {
					return null;
				}
			}
		}

		setEquipped(penaltyItem, false);
		return penaltyItem;
	}

	/**
	 * 조우의 돌골렘 (인챈트 아이템 삭제)
	 * 
	 * @param itemid
	 *            - 제련시 필요한 무기번호
	 * @param enchantLevel
	 *            - 제련시 필요한 무기의 인챈트레벨
	 */
	public boolean MakeDeleteEnchant(int itemid, int enchantLevel) {
		L1ItemInstance[] items = findItemsId(itemid);

		for (L1ItemInstance item : items) {
			if (item.getEnchantLevel() == enchantLevel) {
				removeItem(item, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * 조우의 돌골렘 (인챈트 아이템 검사)
	 * 
	 * @param id
	 *            - 제련시 필요한 무기번호
	 * 
	 * @param enchantLevel
	 *            - 제련시 필요한 무기의 인챈트 레벨
	 * 
	 */
	public boolean MakeCheckEnchant(int id, int enchantLevel) {
		L1ItemInstance[] items = findItemsId(id);

		for (L1ItemInstance item : items) {
			if (item.getEnchantLevel() == enchantLevel && item.getCount() == 1) {
				return true;
			}
		}

		return false;
	}

	public boolean checkEnchant(int id, int enchant) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
				return true;
			}
		}
		return false;
	}

	public boolean DeleteEnchant(int id, int enchant) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.getEnchantLevel() == enchant) {
				removeItem(item, 1);
				return true;
			}
		}
		return false;
	}

	public int getEnchantCount(int id) {// 인첸 레벨
		int cnt = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItemId() == id) {
				cnt = item.getEnchantLevel();
			}
		}
		return cnt;
	}

	public L1ItemInstance get_etc_itemid(int itemid) {
		Iterator<L1ItemInstance> iter = _items.iterator();
		L1ItemInstance item = null;

		while (iter.hasNext()) {
			item = iter.next();
			if (item == null || item.getItem().getType2() != 0) { // -- 잡화만 검색
				continue;
			}
			if (item.getItemId() == itemid) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance get_set_item_eq(int itemid) {
		Iterator<L1ItemInstance> iter = _items.iterator();
		L1ItemInstance item = null;

		while (iter.hasNext()) {
			item = iter.next();
			if (item == null || item.getItem().getType2() == 0)
				continue; // -- 잡화는 검색 조건에서 제외
			if (item.getItemId() == itemid && item.isEquipped()) {
				return item;
			}
		}
		return null;
	}

	public int checkEquippedcount(int id) {
		int equipeCount = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItem().getItemId() == id && item.isEquipped())
				equipeCount++;
		}
		return equipeCount;
	}

	public boolean slotItemFind(L1PcInstance pc, int id, int itemobjId, int enchant, int bless, int attr) {
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id && item.getId() == itemobjId && item.getEnchantLevel() == enchant && item.getBless() == bless
					&& item.getAttrEnchantLevel() == attr) {
				pc.getInventory().setEquipped(item, true);
				return true;
			}
		}
		return false;
	}

	public int checkItemCount(int id) {
		int cnt = 0;
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getItemId() == id)
				cnt += item.getCount();
		}
		return cnt;
	}

	public L1ItemInstance findItemObjId(int id) {
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getId() == id) {
				return item;
			}
		}

		return null;
	}

	public boolean checkEquipped(int[] ids) {
		for (int id : ids) {
			if (!checkEquipped(id)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkEquippedAtOnce(int[] ids) {
		for (L1ItemInstance item : _items) {
			int itemId = item.getItemId();
			for (int id : ids) {
				if (id == itemId && item.isEquipped())
					return true;
			}
		}
		return false;
	}

	// 클라우디아
	private void questitem(int itemid, int itemcount) {
		if (QuestInfoTable.getInstance().getPickupItem(itemid)) { // 목표하는 아이템이
																	// 있는경우
			QuestItemTemp temp = QuestInfoTable.getInstance().getPickupInfo(itemid);
			L1QuestInfo info = _owner.getQuestList(temp.quest_id);
			if (info != null && info.end_time == 0) {// 활성화 상태라면
				info.ck[temp.type] = itemcount;
				if (itemcount >= temp.count) {
					info.ck[temp.type] = temp.count;
				}
				_owner.sendPackets(new S_QuestTalkIsland(_owner, temp.quest_id, info));
			}
		}
	}

	public boolean consumeItem(L1ItemInstance item, int count) {
		L1ItemInstance find = findItemObjId(item.getId());
		if (find == null || find.getCount() < count)
			return false;

		removeItem(item, count);
		return true;
	}

	public boolean consumeItem(L1ItemInstance[] items, int[] counts) {
		int size = items.length;
		for (int i = size - 1; i >= 0; --i) {
			L1ItemInstance item = items[i];
			int count = counts[i];
			L1ItemInstance find = findItemObjId(item.getId());
			if (find == null || find.getCount() < count)
				return false;
		}

		for (int i = size - 1; i >= 0; --i) {
			removeItem(items[i], counts[i]);
		}
		return true;
	}

	/** 인형 착용 여부 **/
	public void setDollOn(int itemId, boolean equipped) {
		L1ItemInstance item = getItem(itemId);
		setDollOn(item, equipped, false, false, false);
	}

	public void setDollOn(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon, boolean shieldWeapon) {
		if (item.isDollOn() != equipped) {
			if (equipped) {
				item.setDollOn(true);
			} else {
				item.setDollOn(false);
			}
		}
	}
	/** 인형 착용 여부 **/
}
