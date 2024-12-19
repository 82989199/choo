package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.Controller.LimitShopController.ShopLimitItem;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PERSONAL_SHOP_ITEM_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static void do_send_buy(L1PcInstance pc, L1PcInstance shop_pc){
		ArrayList<MJDShopItem> list = shop_pc.getSellings();
		if(list == null)
			return;
		
		int size = list.size();
		pc.setPartnersPrivateShopItemCount(size);
		SC_PERSONAL_SHOP_ITEM_LIST_NOTI noti = newInstance();
		noti.set_type(ePersonalShopType.TRADE_BUY);
		noti.set_seller_id(shop_pc.getId());
		int real_size = 0;
		for(int i=0; i<size; ++i){
			MJDShopItem ditem = list.get(i);
			L1ItemInstance item = shop_pc.getInventory().getItem(ditem.objId);
			if(item == null || item.getCount() <= 0)
				continue;
			
			TradeItemInfo tInfo = TradeItemInfo.newInstance();
			tInfo.set_index(real_size);
			tInfo.set_price(ditem.price);
			tInfo.set_count(ditem.count);
			tInfo.set_trade_info(0);
			ItemInfo iInfo = ItemInfo.newInstance(item);
			tInfo.set_item_info(iInfo);
			noti.add_item_list(tInfo);
			++real_size;
		}
		noti.set_size(real_size);
		pc.sendPackets(noti, MJEProtoMessages.SC_PERSONAL_SHOP_ITEM_LIST_NOTI, true);
	}
	
	private static void do_send_sell(L1PcInstance pc, L1PcInstance shop_pc){
		ArrayList<MJDShopItem> purchasings_items = shop_pc.getPurchasings();
		if(purchasings_items == null)
			return;

		SC_PERSONAL_SHOP_ITEM_LIST_NOTI noti = newInstance();
		noti.set_type(ePersonalShopType.TRADE_SELL);
		noti.set_seller_id(shop_pc.getId());
		int purchasings_size = purchasings_items.size();
		List<L1ItemInstance> inventory_items = pc.getInventory().getItems();
		int inventory_size = inventory_items.size();
		int real_size = 0;
		for(int i=0; i<inventory_size; ++i){
			L1ItemInstance item = inventory_items.get(i);
			if(item == null || item.isEquipped())
				continue;
			
			if(real_size >= purchasings_size)
				break;
			
			for(int j=0; j<purchasings_size; ++j){
				MJDShopItem ditem = purchasings_items.get(j);
				if(item.getItemId() != ditem.itemId)
					continue;
				
				if(item.getEnchantLevel() != ditem.enchant || item.getAttrEnchantLevel() != ditem.attr)
					continue;
				
				int count = Math.min(ditem.count, item.getCount());
				TradeItemInfo tInfo = TradeItemInfo.newInstance();
				tInfo.set_index(real_size);
				tInfo.set_price(ditem.price);
				tInfo.set_count(count);
				tInfo.set_buy_count(count);
				tInfo.set_trade_info(0);
				ItemInfo iInfo = ItemInfo.newInstance(item);
				tInfo.set_item_info(iInfo);
				noti.add_item_list(tInfo);
				++real_size;
			}
		}
		noti.set_size(real_size);
		pc.sendPackets(noti, MJEProtoMessages.SC_PERSONAL_SHOP_ITEM_LIST_NOTI, true);
	}
	
	public static void do_send(L1PcInstance pc, int object_id, ePersonalShopType type){
		L1PcInstance shop_pc = (L1PcInstance)L1World.getInstance().findObject(object_id);
		if(shop_pc == null)
			return;
		
		if(type.equals(ePersonalShopType.TRADE_BUY))
			do_send_buy(pc, shop_pc);
		else
			do_send_sell(pc, shop_pc);
	}
	
	private static void do_send_buy_for_npc(L1PcInstance pc, L1NpcInstance npc, L1Shop shop){
		List<L1ShopItem> shop_items = shop.getSellingItems();
		int size = shop_items.size();
		int real_size = 0;
		boolean is_normal = L1Shop.is_normal_shop(npc);
		SC_PERSONAL_SHOP_ITEM_LIST_NOTI noti = newInstance();
		noti.set_type(ePersonalShopType.TRADE_BUY);
		noti.set_seller_id(npc.getId());
		for(int i=0; i<size; ++i){
			L1ShopItem s_item = shop_items.get(i);
			L1Item item = s_item.getItem();
			L1ItemInstance dummy = new L1ItemInstance();
			dummy.setItem(item);
			dummy.setEnchantLevel(s_item.getEnchant());
			dummy.setBless(item.getBless());
			dummy.setId(i);
			dummy.setIdentified(true);
			
			/**일일 구매제한*/
			int Limitcount = LimitShopController.getInstance().getLimitShop(npc.getNpcId());
			if(Limitcount > 0){
				for(ShopLimitItem _ShopLimitItem : pc.getLimitShop()){
					if(_ShopLimitItem._npcid == npc.getNpcId() && _ShopLimitItem._itemid == item.getItemId()){
						Limitcount -= _ShopLimitItem._count;
					}
				}
				dummy.setShopStr("\\aG계정당 구매 가능개수 : "+Limitcount+ "개");
			}
			
			
			
			TradeItemInfo tInfo = TradeItemInfo.newInstance();
			tInfo.set_index(real_size);
			int price = s_item.getPrice();
			tInfo.set_count(is_normal ? (price > 100000000 ? 1 : 99) : s_item.getCount());
			tInfo.set_price(price);
			tInfo.set_trade_info(0);
			ItemInfo iInfo = ItemInfo.newInstance(dummy);
			tInfo.set_item_info(iInfo);
			noti.add_item_list(tInfo);
			++real_size;
		}
		noti.set_size(real_size);
		pc.sendPackets(noti, MJEProtoMessages.SC_PERSONAL_SHOP_ITEM_LIST_NOTI, true);
	}
	
	private static void do_send_sell_for_npc(L1PcInstance pc, L1NpcInstance npc, L1Shop shop){
		List<L1ShopItem> shop_items = shop.getBuyingItems();
		int size = shop_items.size();
		int real_size = 0;
		SC_PERSONAL_SHOP_ITEM_LIST_NOTI noti = newInstance();
		noti.set_type(ePersonalShopType.TRADE_SELL);
		noti.set_seller_id(npc.getId());
		for(int i=0; i<size; ++i){
			L1ShopItem s_item = shop_items.get(i);		
			for (L1ItemInstance pc_item : pc.getInventory().getItems()) {	
				if(s_item.getItemId() == pc_item.getItemId() && s_item.getEnchant() == pc_item.getEnchantLevel()){
					L1Item item = s_item.getItem();
					L1ItemInstance dummy = new L1ItemInstance();
					dummy.setItem(item);
					dummy.setEnchantLevel(s_item.getEnchant());
					dummy.setId(i);
					dummy.setIdentified(true);
					
					
					
					
					
					TradeItemInfo tInfo = TradeItemInfo.newInstance();					
					tInfo.set_index(real_size);
					tInfo.set_count(s_item.getCount());
					tInfo.set_buy_count(s_item.getCount());
					tInfo.set_price(s_item.getPrice());
					tInfo.set_trade_info(0);
					ItemInfo iInfo = ItemInfo.newInstance(dummy);
					tInfo.set_item_info(iInfo);
					noti.add_item_list(tInfo);
					++real_size;
				}
			}
		}
		noti.set_size(real_size);
		pc.sendPackets(noti, MJEProtoMessages.SC_PERSONAL_SHOP_ITEM_LIST_NOTI, true);
	}
	
	public static void do_send_for_npc(L1PcInstance pc, int object_id, ePersonalShopType type){
		L1NpcInstance npc = (L1NpcInstance)L1World.getInstance().findObject(object_id);
		if(npc == null)
			return;
		
		L1Shop shop = L1Shop.find_shop(npc);
		if(shop == null)
			return;

		if(type.equals(ePersonalShopType.TRADE_BUY))
			do_send_buy_for_npc(pc, npc, shop);
		else
			do_send_sell_for_npc(pc, npc, shop);
	}
	
	public static SC_PERSONAL_SHOP_ITEM_LIST_NOTI newInstance(){
		return new SC_PERSONAL_SHOP_ITEM_LIST_NOTI();
	}
	private ePersonalShopType _type;
	private int _seller_id;
	private int _size;
	private java.util.LinkedList<TradeItemInfo> _item_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PERSONAL_SHOP_ITEM_LIST_NOTI(){
	}
	public ePersonalShopType get_type(){
		return _type;
	}
	public void set_type(ePersonalShopType val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_seller_id(){
		return _seller_id;
	}
	public void set_seller_id(int val){
		_bit |= 0x2;
		_seller_id = val;
	}
	public boolean has_seller_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_size(){
		return _size;
	}
	public void set_size(int val){
		_bit |= 0x4;
		_size = val;
	}
	public boolean has_size(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<TradeItemInfo> get_item_list(){
		return _item_list;
	}
	public void add_item_list(TradeItemInfo val){
		if(!has_item_list()){
			_item_list = new java.util.LinkedList<TradeItemInfo>();
			_bit |= 0x8;
		}
		_item_list.add(val);
	}
	public boolean has_item_list(){
		return (_bit & 0x8) == 0x8;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		if (has_seller_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _seller_id);
		if (has_size())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _size);
		if (has_item_list()){
			for(TradeItemInfo val : _item_list)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_seller_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_size()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_item_list()){
			for(TradeItemInfo val : _item_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
		}
		if (has_seller_id()){
			output.wirteInt32(2, _seller_id);
		}
		if (has_size()){
			output.wirteInt32(3, _size);
		}
		if (has_item_list()){
			for(TradeItemInfo val : _item_list){
				output.writeMessage(4, val);
			}
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_type(ePersonalShopType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_seller_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_size(input.readInt32());
					break;
				}
				case 0x00000022:{
					add_item_list((TradeItemInfo)input.readMessage(TradeItemInfo.newInstance()));
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_PERSONAL_SHOP_ITEM_LIST_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_list()){
			for(TradeItemInfo val : _item_list)
				val.dispose();
			_item_list.clear();
			_item_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public enum ePersonalShopType{
		TRADE_BUY(0),
		TRADE_SELL(1);
		private int value;
		ePersonalShopType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePersonalShopType v){
			return value == v.value;
		}
		public static ePersonalShopType fromInt(int i){
			switch(i){
			case 0:
				return TRADE_BUY;
			case 1:
				return TRADE_SELL;
			default:
				return null;
			}
		}
	}
}
