package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import l1j.server.MJCompanion.Basic.ClassInfo.MJCompanionClassInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ItemInfo newInstance(int item_object_id, MJCompanionInstanceCache cache){
		ItemInfo iInfo = newInstance();
		iInfo.set_object_id(item_object_id);
		CompanionCard card = CompanionCard.newInstance();
		card.set_is_summoned(cache.get_is_summoned());
		card.set_is_dead(cache.get_is_dead());
		card.set_is_oblivion(cache.get_is_oblivion());
		card.set_level(cache.get_level());
		card.set_name(cache.get_name());
		card.set_elixir_use_count(cache.get_elixir_use_count());
		iInfo.set_companion_card(card);
		return iInfo;
	}
	public static ItemInfo newInstance(L1ItemInstance item, int use_type, byte[] view_name, int bless){
		L1Item item_t = item.getItem();
		ItemInfo iInfo = newInstance();
		iInfo.set_object_id(item.getId());
		iInfo.set_db_id(item.getId());
		iInfo.set_name_id(item_t.getItemDescId());
		iInfo.set_icon_id(item.get_gfxid());
		iInfo.set_bless_code_for_display(bless/*item.getBless() == 0 ? item_t.getBless() : item.getBless()*/);
		int bit = item.isIdentified() ? 1 : 0;
		if(item.getBless() == 129){
			bit = 2 | 4 | 8;
		}else{
			if(!item_t.isTradable())
				bit |= 2;
			if(item_t.isCantDelete())
				bit |= 4;
			if(item_t.get_safeenchant() < 0)
				bit |= 8;
		}
		// 2 교환불가
		// 4 삭제불가
		// 8 강화 불가
		iInfo.set_attribute_bit_set(bit);
		
		if(item_t.getType2() == 1){
			iInfo.set_elemental_enchant_type(L1ItemInstance.attrEnchantToElementalType(item.getAttrEnchantLevel()));
			iInfo.set_elemental_enchant_value(L1ItemInstance.pureAttrEnchantLevel(item.getAttrEnchantLevel()));
		}
		
		int category = 0;
		switch(item_t.getType2()){
		case 0:
			category = 23;
			break;
		case 1:
			category = 1;
			break;
		case 2:
			category = 19;
			break;
		}
		iInfo.set_category(category);
		iInfo.set_enchant(item.getEnchantLevel());
		iInfo.set_real_enchant(item.getEnchantLevel());
		iInfo.set_interact_type(use_type);
		iInfo.set_count(item.getCount());
		if (item_t.isEndedTimeMessage())
			iInfo.set_attribute_bit_set_ex(0x01);
		else{
			switch(item_t.getItemId()){
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
			case 49095:
				iInfo.set_attribute_bit_set_ex(0x08);
				break;
			default:
				break;
			}
		}
		
		iInfo.set_is_timeout(true);
		iInfo.set_deposit(item_t.isTradable() ? 7 : 0);
		iInfo.set_weight(item.getWeight());
		iInfo.set_is_identified(item.isIdentified());
		iInfo.set_number_of_use(0);
		iInfo.set_is_merging(false);
		iInfo.set_description(view_name);
		if(item.isIdentified())
			iInfo.set_extra_description(item.getStatusBytes());

		MJCompanionClassInfo cInfo = MJCompanionClassInfo.from_item_id(item.getItemId());
		if(cInfo == null)
			return iInfo;
		
		CompanionCard card = CompanionCard.newInstance();
		MJCompanionInstanceCache cache = MJCompanionInstanceCache.get(item.getId());
		if(cache != null){
			card.set_class_id(cInfo.get_class_id());
			card.set_is_dead(cache.get_is_dead());
			card.set_is_oblivion(cache.get_is_oblivion());
			card.set_is_summoned(cache.get_is_summoned());
			card.set_level(cache.get_level());
			card.set_name(cache.get_name());
			card.set_elixir_use_count(cache.get_elixir_use_count());
			try {
				iInfo.set_description(String.format("펫 목걸이 (%s LV %d)", cInfo.get_class_npc_name_id(), cache.get_level()).getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			card.set_class_id(cInfo.get_class_id());
			card.set_is_dead(false);
			card.set_is_oblivion(true);
			card.set_is_summoned(false);
			card.set_level(1);
			card.set_name(cInfo.get_class_npc_name_id());
			card.set_elixir_use_count(0);
			try {
				iInfo.set_description(String.format("펫 목걸이 (%s LV 1)", cInfo.get_class_npc_name_id()).getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		iInfo.set_companion_card(card);
		return iInfo;
	}
	
	public static ItemInfo newInstance(L1ItemInstance item, int use_type){
		try {
			return newInstance(item, use_type, item.getViewName().getBytes("MS949"), item.getBless());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static ItemInfo newInstance(L1ItemInstance item){
		return newInstance(item, item.getItem().getUseType());
	}
	
	public static ItemInfo newInstance(){
		return new ItemInfo();
	}
	private int _object_id;
	private int _name_id;
	private int _db_id;
	private int _count;
	private int _interact_type;
	private int _number_of_use;
	private int _icon_id;
	private int _bless_code_for_display;
	private int _attribute_bit_set;
	private long _attribute_bit_set_ex;
	private boolean _is_timeout;
	private int _category;
	private int _enchant;
	private int _deposit;
	private int _overlay_surf_id;
	private int _elemental_enchant_type;
	private int _elemental_enchant_value;
	private byte[] _description;
	private byte[] _extra_description;
	private int _left_time_for_pre_notify;
	private CompanionCard _companion_card;
	private int _bless_code;
	private int _real_enchant;
	private boolean _is_merging;
	private int _weight;
	private boolean _is_identified;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ItemInfo(){
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x1;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_name_id(){
		return _name_id;
	}
	public void set_name_id(int val){
		_bit |= 0x2;
		_name_id = val;
	}
	public boolean has_name_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_db_id(){
		return _db_id;
	}
	public void set_db_id(int val){
		_bit |= 0x4;
		_db_id = val;
	}
	public boolean has_db_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x8;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_interact_type(){
		return _interact_type;
	}
	public void set_interact_type(int val){
		_bit |= 0x10;
		_interact_type = val;
	}
	public boolean has_interact_type(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_number_of_use(){
		return _number_of_use;
	}
	public void set_number_of_use(int val){
		_bit |= 0x20;
		_number_of_use = val;
	}
	public boolean has_number_of_use(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_icon_id(){
		return _icon_id;
	}
	public void set_icon_id(int val){
		_bit |= 0x40;
		_icon_id = val;
	}
	public boolean has_icon_id(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_bless_code_for_display(){
		return _bless_code_for_display;
	}
	public void set_bless_code_for_display(int val){
		_bit |= 0x80;
		_bless_code_for_display = val;
	}
	public boolean has_bless_code_for_display(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_attribute_bit_set(){
		return _attribute_bit_set;
	}
	public void set_attribute_bit_set(int val){
		_bit |= 0x100;
		_attribute_bit_set = val;
	}
	public boolean has_attribute_bit_set(){
		return (_bit & 0x100) == 0x100;
	}
	public long get_attribute_bit_set_ex(){
		return _attribute_bit_set_ex;
	}
	public void set_attribute_bit_set_ex(long val){
		_bit |= 0x200;
		_attribute_bit_set_ex = val;
	}
	public boolean has_attribute_bit_set_ex(){
		return (_bit & 0x200) == 0x200;
	}
	public boolean get_is_timeout(){
		return _is_timeout;
	}
	public void set_is_timeout(boolean val){
		_bit |= 0x400;
		_is_timeout = val;
	}
	public boolean has_is_timeout(){
		return (_bit & 0x400) == 0x400;
	}
	public int get_category(){
		return _category;
	}
	public void set_category(int val){
		_bit |= 0x800;
		_category = val;
	}
	public boolean has_category(){
		return (_bit & 0x800) == 0x800;
	}
	public int get_enchant(){
		return _enchant;
	}
	public void set_enchant(int val){
		_bit |= 0x1000;
		_enchant = val;
	}
	public boolean has_enchant(){
		return (_bit & 0x1000) == 0x1000;
	}
	public int get_deposit(){
		return _deposit;
	}
	public void set_deposit(int val){
		_bit |= 0x2000;
		_deposit = val;
	}
	public boolean has_deposit(){
		return (_bit & 0x2000) == 0x2000;
	}
	public int get_overlay_surf_id(){
		return _overlay_surf_id;
	}
	public void set_overlay_surf_id(int val){
		_bit |= 0x4000;
		_overlay_surf_id = val;
	}
	public boolean has_overlay_surf_id(){
		return (_bit & 0x4000) == 0x4000;
	}
	public int get_elemental_enchant_type(){
		return _elemental_enchant_type;
	}
	public void set_elemental_enchant_type(int val){
		_bit |= 0x8000;
		_elemental_enchant_type = val;
	}
	public boolean has_elemental_enchant_type(){
		return (_bit & 0x8000) == 0x8000;
	}
	public int get_elemental_enchant_value(){
		return _elemental_enchant_value;
	}
	public void set_elemental_enchant_value(int val){
		_bit |= 0x10000;
		_elemental_enchant_value = val;
	}
	public boolean has_elemental_enchant_value(){
		return (_bit & 0x10000) == 0x10000;
	}
	public byte[] get_description(){
		return _description;
	}
	public void set_description(byte[] val){
		_bit |= 0x20000;
		_description = val;
	}
	public boolean has_description(){
		return (_bit & 0x20000) == 0x20000;
	}
	public byte[] get_extra_description(){
		return _extra_description;
	}
	public void set_extra_description(byte[] val){
		_bit |= 0x40000;
		_extra_description = val;
	}
	public boolean has_extra_description(){
		return (_bit & 0x40000) == 0x40000;
	}
	public int get_left_time_for_pre_notify(){
		return _left_time_for_pre_notify;
	}
	public void set_left_time_for_pre_notify(int val){
		_bit |= 0x80000;
		_left_time_for_pre_notify = val;
	}
	public boolean has_left_time_for_pre_notify(){
		return (_bit & 0x80000) == 0x80000;
	}
	public CompanionCard get_companion_card(){
		return _companion_card;
	}
	public void set_companion_card(CompanionCard val){
		_bit |= 0x100000;
		_companion_card = val;
	}
	public boolean has_companion_card(){
		return (_bit & 0x100000) == 0x100000;
	}
	public int get_bless_code(){
		return _bless_code;
	}
	public void set_bless_code(int val){
		_bit |= 0x200000;
		_bless_code = val;
	}
	public boolean has_bless_code(){
		return (_bit & 0x200000) == 0x200000;
	}
	public int get_real_enchant(){
		return _real_enchant;
	}
	public void set_real_enchant(int val){
		_bit |= 0x400000;
		_real_enchant = val;
	}
	public boolean has_real_enchant(){
		return (_bit & 0x400000) == 0x400000;
	}
	public boolean get_is_merging(){
		return _is_merging;
	}
	public void set_is_merging(boolean val){
		_bit |= 0x800000;
		_is_merging = val;
	}
	public boolean has_is_merging(){
		return (_bit & 0x800000) == 0x800000;
	}
	public int get_weight(){
		return _weight;
	}
	public void set_weight(int val){
		_bit |= 0x1000000;
		_weight = val;
	}
	public boolean has_weight(){
		return (_bit & 0x1000000) == 0x1000000;
	}
	public boolean get_is_identified(){
		return _is_identified;
	}
	public void set_is_identified(boolean val){
		_bit |= 0x2000000;
		_is_identified = val;
	}
	public boolean has_is_identified(){
		return (_bit & 0x2000000) == 0x2000000;
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
		if (has_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		if (has_name_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _name_id);
		if (has_db_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _db_id);
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _count);
		if (has_interact_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _interact_type);
		if (has_number_of_use())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _number_of_use);
		if (has_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _icon_id);
		if (has_bless_code_for_display())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _bless_code_for_display);
		if (has_attribute_bit_set())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _attribute_bit_set);
		if (has_attribute_bit_set_ex())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(10, _attribute_bit_set_ex);
		if (has_is_timeout())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _is_timeout);
		if (has_category())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _category);
		if (has_enchant())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _enchant);
		if (has_deposit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _deposit);
		if (has_overlay_surf_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(15, _overlay_surf_id);
		if (has_elemental_enchant_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(16, _elemental_enchant_type);
		if (has_elemental_enchant_value())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(17, _elemental_enchant_value);
		if (has_description())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(18, _description);
		if (has_extra_description())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(19, _extra_description);
		if (has_left_time_for_pre_notify())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(20, _left_time_for_pre_notify);
		if (has_companion_card())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(21, _companion_card);
		if (has_bless_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(22, _bless_code);
		if (has_real_enchant())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(23, _real_enchant);
		if (has_is_merging())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(24, _is_merging);
		if (has_weight())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(25, _weight);
		if (has_is_identified())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(26, _is_identified);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_object_id()){
			output.writeUInt32(1, _object_id);
		}
		if (has_name_id()){
			output.wirteInt32(2, _name_id);
		}
		if (has_db_id()){
			output.wirteInt32(3, _db_id);
		}
		if (has_count()){
			output.wirteInt32(4, _count);
		}
		if (has_interact_type()){
			output.wirteInt32(5, _interact_type);
		}
		if (has_number_of_use()){
			output.wirteInt32(6, _number_of_use);
		}
		if (has_icon_id()){
			output.wirteInt32(7, _icon_id);
		}
		if (has_bless_code_for_display()){
			output.wirteInt32(8, _bless_code_for_display);
		}
		if (has_attribute_bit_set()){
			output.wirteInt32(9, _attribute_bit_set);
		}
		if (has_attribute_bit_set_ex()){
			output.wirteUInt64(10, _attribute_bit_set_ex);
		}
		if (has_is_timeout()){
			output.writeBool(11, _is_timeout);
		}
		if (has_category()){
			output.wirteInt32(12, _category);
		}
		if (has_enchant()){
			output.wirteInt32(13, _enchant);
		}
		if (has_deposit()){
			output.writeUInt32(14, _deposit);
		}
		if (has_overlay_surf_id()){
			output.wirteInt32(15, _overlay_surf_id);
		}
		if (has_elemental_enchant_type()){
			output.wirteInt32(16, _elemental_enchant_type);
		}
		if (has_elemental_enchant_value()){
			output.wirteInt32(17, _elemental_enchant_value);
		}
		if (has_description()){
			output.writeBytes(18, _description);
		}
		if (has_extra_description()){
			output.writeBytes(19, _extra_description);
		}
		if (has_left_time_for_pre_notify()){
			output.wirteInt32(20, _left_time_for_pre_notify);
		}
		if (has_companion_card()){
			output.writeMessage(21, _companion_card);
		}
		if (has_bless_code()){
			output.wirteInt32(22, _bless_code);
		}
		if (has_real_enchant()){
			output.wirteInt32(23, _real_enchant);
		}
		if (has_is_merging()){
			output.writeBool(24, _is_merging);
		}
		if (has_weight()){
			output.wirteInt32(25, _weight);
		}
		if (has_is_identified()){
			output.writeBool(26, _is_identified);
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
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_name_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_db_id(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_interact_type(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_number_of_use(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_icon_id(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_bless_code_for_display(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_attribute_bit_set(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_attribute_bit_set_ex(input.readUInt64());
					break;
				}
				case 0x00000058:{
					set_is_timeout(input.readBool());
					break;
				}
				case 0x00000060:{
					set_category(input.readInt32());
					break;
				}
				case 0x00000068:{
					set_enchant(input.readInt32());
					break;
				}
				case 0x00000070:{
					set_deposit(input.readUInt32());
					break;
				}
				case 0x00000078:{
					set_overlay_surf_id(input.readInt32());
					break;
				}
				case 0x00000080:{
					set_elemental_enchant_type(input.readInt32());
					break;
				}
				case 0x00000088:{
					set_elemental_enchant_value(input.readInt32());
					break;
				}
				case 0x00000092:{
					set_description(input.readBytes());
					break;
				}
				case 0x0000009A:{
					set_extra_description(input.readBytes());
					break;
				}
				case 0x000000A0:{
					set_left_time_for_pre_notify(input.readInt32());
					break;
				}
				case 0x000000AA:{
					set_companion_card((CompanionCard)input.readMessage(CompanionCard.newInstance()));
					break;
				}
				case 0x000000B0:{
					set_bless_code(input.readInt32());
					break;
				}
				case 0x000000B8:{
					set_real_enchant(input.readInt32());
					break;
				}
				case 0x000000C0:{
					set_is_merging(input.readBool());
					break;
				}
				case 0x000000C8:{
					set_weight(input.readInt32());
					break;
				}
				case 0x000000D0:{
					set_is_identified(input.readBool());
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
		return new ItemInfo();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_companion_card() && _companion_card != null){
			_companion_card.dispose();
			_companion_card = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CompanionCard implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CompanionCard newInstance(){
			return new CompanionCard();
		}
		private boolean _is_oblivion;
		private boolean _is_dead;
		private boolean _is_summoned;
		private int _class_id;
		private int _level;
		private String _name;
		private int _elixir_use_count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CompanionCard(){
		}
		public boolean get_is_oblivion(){
			return _is_oblivion;
		}
		public void set_is_oblivion(boolean val){
			_bit |= 0x1;
			_is_oblivion = val;
		}
		public boolean has_is_oblivion(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_is_dead(){
			return _is_dead;
		}
		public void set_is_dead(boolean val){
			_bit |= 0x2;
			_is_dead = val;
		}
		public boolean has_is_dead(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_is_summoned(){
			return _is_summoned;
		}
		public void set_is_summoned(boolean val){
			_bit |= 0x4;
			_is_summoned = val;
		}
		public boolean has_is_summoned(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_class_id(){
			return _class_id;
		}
		public void set_class_id(int val){
			_bit |= 0x8;
			_class_id = val;
		}
		public boolean has_class_id(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_level(){
			return _level;
		}
		public void set_level(int val){
			_bit |= 0x10;
			_level = val;
		}
		public boolean has_level(){
			return (_bit & 0x10) == 0x10;
		}
		public String get_name(){
			return _name;
		}
		public void set_name(String val){
			_bit |= 0x20;
			_name = val;
		}
		public boolean has_name(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_elixir_use_count(){
			return _elixir_use_count;
		}
		public void set_elixir_use_count(int val){
			_bit |= 0x40;
			_elixir_use_count = val;
		}
		public boolean has_elixir_use_count(){
			return (_bit & 0x40) == 0x40;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_is_oblivion())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _is_oblivion);
			if (has_is_dead())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_dead);
			if (has_is_summoned())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _is_summoned);
			if (has_class_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _class_id);
			if (has_level())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _level);
			if (has_name())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(6, _name);
			if (has_elixir_use_count())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _elixir_use_count);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_is_oblivion()){
				output.writeBool(1, _is_oblivion);
			}
			if (has_is_dead()){
				output.writeBool(2, _is_dead);
			}
			if (has_is_summoned()){
				output.writeBool(3, _is_summoned);
			}
			if (has_class_id()){
				output.writeUInt32(4, _class_id);
			}
			if (has_level()){
				output.writeUInt32(5, _level);
			}
			if (has_name()){
				output.writeString(6, _name);
			}
			if (has_elixir_use_count()){
				output.writeUInt32(7, _elixir_use_count);
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
						set_is_oblivion(input.readBool());
						break;
					}
					case 0x00000010:{
						set_is_dead(input.readBool());
						break;
					}
					case 0x00000018:{
						set_is_summoned(input.readBool());
						break;
					}
					case 0x00000020:{
						set_class_id(input.readUInt32());
						break;
					}
					case 0x00000028:{
						set_level(input.readUInt32());
						break;
					}
					case 0x00000032:{
						set_name(input.readString());
						break;
					}
					case 0x00000038:{
						set_elixir_use_count(input.readUInt32());
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
			return new CompanionCard();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
