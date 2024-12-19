package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CRAFT_MAKE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_CRAFT_MAKE_ACK newInstance(){
		return new SC_CRAFT_MAKE_ACK();
	}
	
	public static void sendResultPacket(L1PcInstance pc, eCraftMakeReqResultType eResultType, CraftOutputItem outputItem, int currentSuccess, int maxSuccess){
		SC_CRAFT_MAKE_ACK ack = new SC_CRAFT_MAKE_ACK();
		ack.set_eResult(eResultType);
		if(outputItem != null)
			ack.add_output_items(outputItem);
		LimitCraftInfo lInfo = LimitCraftInfo.newInstance();
		lInfo.set_cur_success_cnt(currentSuccess);
		lInfo.set_max_success_cnt(maxSuccess);
		ack.set_limit_craft_info(lInfo);
		pc.sendPackets(ack, MJEProtoMessages.SC_CRAFT_MAKE_ACK.toInt(), true);
	}
	
	private eCraftMakeReqResultType _eResult;
	private LinkedList<CraftOutputItem> _output_items;
	private LimitCraftInfo _limit_craft_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_CRAFT_MAKE_ACK(){
	}
	public eCraftMakeReqResultType get_eResult(){
		return _eResult;
	}
	public void set_eResult(eCraftMakeReqResultType val){
		_bit |= 0x00000001;
		_eResult = val;
	}
	public boolean has_eResult(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public LinkedList<CraftOutputItem> get_output_items(){
		return _output_items;
	}
	public void add_output_items(CraftOutputItem val){
		if(!has_output_items()){
			_output_items = new LinkedList<CraftOutputItem>();
			_bit |= 0x00000002;
		}
		_output_items.add(val);
	}
	public boolean has_output_items(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public LimitCraftInfo get_limit_craft_info(){
		return _limit_craft_info;
	}
	public void set_limit_craft_info(LimitCraftInfo val){
		_bit |= 0x00000004;
		_limit_craft_info = val;
	}
	public boolean has_limit_craft_info(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_eResult())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _eResult.toInt());
		if (has_output_items()){
			for(CraftOutputItem val : _output_items)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		if (has_limit_craft_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _limit_craft_info);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eResult()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_output_items()){
			for(CraftOutputItem val : _output_items){
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
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_eResult()){
			output.writeEnum(1, _eResult.toInt());
		}
		if (has_output_items()){
			for(CraftOutputItem val : _output_items){
				output.writeMessage(2, val);
			}
		}
		if (has_limit_craft_info()){
			output.writeMessage(3, _limit_craft_info);
		}
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
					set_eResult(eCraftMakeReqResultType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_output_items((CraftOutputItem)input.readMessage(CraftOutputItem.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_limit_craft_info((LimitCraftInfo)input.readMessage(LimitCraftInfo.newInstance()));
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
		return new SC_CRAFT_MAKE_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_output_items()){
			for(CraftOutputItem val : _output_items)
				val.dispose();
			_output_items.clear();
			_output_items = null;
		}
		if (has_limit_craft_info() && _limit_craft_info != null){
			_limit_craft_info.dispose();
			_limit_craft_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CraftOutputItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CraftOutputItem newInstance(){
			return new CraftOutputItem();
		}
		private int _name_id;
		private int _count;
		private int _slot;
		private int _enchant;
		private int _bless;
		private int _elemental_type;
		private int _elemental_level;
		private String _desc;
		private int _system_desc;
		private int _broadcast_desc;
		private int _iconId;
		private String _url;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CraftOutputItem(){
		}
		public int get_name_id(){
			return _name_id;
		}
		public void set_name_id(int val){
			_bit |= 0x00000001;
			_name_id = val;
		}
		public boolean has_name_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public int get_count(){
			return _count;
		}
		public void set_count(int val){
			_bit |= 0x00000002;
			_count = val;
		}
		public boolean has_count(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		public int get_slot(){
			return _slot;
		}
		public void set_slot(int val){
			_bit |= 0x00000004;
			_slot = val;
		}
		public boolean has_slot(){
			return (_bit & 0x00000004) == 0x00000004;
		}
		public int get_enchant(){
			return _enchant;
		}
		public void set_enchant(int val){
			_bit |= 0x00000008;
			_enchant = val;
		}
		public boolean has_enchant(){
			return (_bit & 0x00000008) == 0x00000008;
		}
		public int get_bless(){
			return _bless;
		}
		public void set_bless(int val){
			_bit |= 0x00000010;
			_bless = val;
		}
		public boolean has_bless(){
			return (_bit & 0x00000010) == 0x00000010;
		}
		public int get_elemental_type(){
			return _elemental_type;
		}
		public void set_elemental_type(int val){
			_bit |= 0x00000020;
			_elemental_type = val;
		}
		public boolean has_elemental_type(){
			return (_bit & 0x00000020) == 0x00000020;
		}
		public int get_elemental_level(){
			return _elemental_level;
		}
		public void set_elemental_level(int val){
			_bit |= 0x00000040;
			_elemental_level = val;
		}
		public boolean has_elemental_level(){
			return (_bit & 0x00000040) == 0x00000040;
		}
		public String get_desc(){
			return _desc;
		}
		public void set_desc(String val){
			_bit |= 0x00000080;
			_desc = val;
		}
		public boolean has_desc(){
			return (_bit & 0x00000080) == 0x00000080;
		}
		public int get_system_desc(){
			return _system_desc;
		}
		public void set_system_desc(int val){
			_bit |= 0x00000100;
			_system_desc = val;
		}
		public boolean has_system_desc(){
			return (_bit & 0x00000100) == 0x00000100;
		}
		public int get_broadcast_desc(){
			return _broadcast_desc;
		}
		public void set_broadcast_desc(int val){
			_bit |= 0x00000200;
			_broadcast_desc = val;
		}
		public boolean has_broadcast_desc(){
			return (_bit & 0x00000200) == 0x00000200;
		}
		public int get_iconId(){
			return _iconId;
		}
		public void set_iconId(int val){
			_bit |= 0x00000400;
			_iconId = val;
		}
		public boolean has_iconId(){
			return (_bit & 0x00000400) == 0x00000400;
		}
		public String get_url(){
			return _url;
		}
		public void set_url(String val){
			_bit |= 0x00000800;
			_url = val;
		}
		public boolean has_url(){
			return (_bit & 0x00000800) == 0x00000800;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_name_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
			if (has_count())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
			if (has_slot())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _slot);
			if (has_enchant())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _enchant);
			if (has_bless())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _bless);
			if (has_elemental_type())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _elemental_type);
			if (has_elemental_level())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _elemental_level);
			if (has_desc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _desc);
			if (has_system_desc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _system_desc);
			if (has_broadcast_desc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _broadcast_desc);
			if (has_iconId())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _iconId);
			if (has_url())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(12, _url);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_slot()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_enchant()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_bless()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_elemental_type()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_elemental_level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_system_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_broadcast_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_iconId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_url()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_name_id()){
				output.wirteInt32(1, _name_id);
			}
			if (has_count()){
				output.wirteInt32(2, _count);
			}
			if (has_slot()){
				output.wirteInt32(3, _slot);
			}
			if (has_enchant()){
				output.wirteInt32(4, _enchant);
			}
			if (has_bless()){
				output.wirteInt32(5, _bless);
			}
			if (has_elemental_type()){
				output.wirteInt32(6, _elemental_type);
			}
			if (has_elemental_level()){
				output.wirteInt32(7, _elemental_level);
			}
			if (has_desc()){
				output.writeString(8, _desc);
			}
			if (has_system_desc()){
				output.wirteInt32(9, _system_desc);
			}
			if (has_broadcast_desc()){
				output.wirteInt32(10, _broadcast_desc);
			}
			if (has_iconId()){
				output.wirteInt32(11, _iconId);
			}
			if (has_url()){
				output.writeString(12, _url);
			}
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
						set_name_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_count(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_slot(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_enchant(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_bless(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_elemental_type(input.readInt32());
						break;
					}
					case 0x00000038:{
						set_elemental_level(input.readInt32());
						break;
					}
					case 0x00000042:{
						set_desc(input.readString());
						break;
					}
					case 0x00000048:{
						set_system_desc(input.readInt32());
						break;
					}
					case 0x00000050:{
						set_broadcast_desc(input.readInt32());
						break;
					}
					case 0x00000058:{
						set_iconId(input.readInt32());
						break;
					}
					case 0x00000062:{
						set_url(input.readString());
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
			return new CraftOutputItem();
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
	public static class LimitCraftInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static LimitCraftInfo newInstance(){
			return new LimitCraftInfo();
		}
		private int _max_success_cnt;
		private int _cur_success_cnt;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private LimitCraftInfo(){
		}
		public int get_max_success_cnt(){
			return _max_success_cnt;
		}
		public void set_max_success_cnt(int val){
			_bit |= 0x00000001;
			_max_success_cnt = val;
		}
		public boolean has_max_success_cnt(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public int get_cur_success_cnt(){
			return _cur_success_cnt;
		}
		public void set_cur_success_cnt(int val){
			_bit |= 0x00000002;
			_cur_success_cnt = val;
		}
		public boolean has_cur_success_cnt(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_max_success_cnt())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _max_success_cnt);
			if (has_cur_success_cnt())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _cur_success_cnt);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_max_success_cnt()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cur_success_cnt()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_max_success_cnt()){
				output.wirteInt32(1, _max_success_cnt);
			}
			if (has_cur_success_cnt()){
				output.wirteInt32(2, _cur_success_cnt);
			}
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
						set_max_success_cnt(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_cur_success_cnt(input.readInt32());
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
			return new LimitCraftInfo();
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
	public enum eCraftMakeReqResultType{
		RP_SUCCESS(0),						// 아이템 제작에 성공했습니다.
		RP_FAILURE(1),						// 아이템 제작에 실패했습니다.
		RP_ERROR_INVALID_INPUT(2),			// 재료 아이템이 잘못되었습니다.
		RP_ERROR_LACK_OF_ADENA(3),			// 아데나가 부족합니다.
		RP_ERROR_INVEN_OVER(4),				// 인벤토리 공간이 무족합니다.
		RP_ERROR_WEIGHT_OVER(5),			// 무게 게이지가 가득 찼습니다.
		RP_ERROR_INVALID_NPC(6),			// 잘못된 NPC의 제작 요청입니다.
		RP_ERROR_BAD_QUALIFICATION(7),		// 유저의 스탯이 부족합니다.
		RP_ERROR_CRAFT_DOES_NOT_EXIST(8),	// 잘못된 제작 요청입니다.
		RP_ERROR_NO_REQUIRED_ITEM(9),		// 제작 요구 아이템이 부족합니다.
		RP_ERROR_TOO_MANY_MAKE_REQ(10),		// 너무 많은 제작 요청으로 인해 제작이 실패했습니다.
		RP_ERROR_INPUT_ITEM_NOT_EXIST(11),	// 제작 재료 아이템이 부족합니다.
		RP_ERROR_OPTION_ITEM_NOT_EXIST(12),	// 제작 재료 옵션 아이템이 부족합니다.
		RP_ERROR_NPC_OUT_OF_RANGE(13),		// 제작 NPC가 범위 밖에 있습니다.
		RP_ERROR_BLOCKED_CRAFT_ID(14),		// 해당 제작은 서비스가 일시적으로 중단되었습니다.
		RP_ERROR_REJECTED_REQUIRED_ITEM(15),// 제작에 실패했습니다.
		RP_ERROR_SUCCESS_COUNT_EXCEED(16),	// 제작 불가 : 준비된 수량 매진
		RP_ERROR_BATCH_CRAFT(17),			// 제작에 실패했습니다.
		RP_ERROR_UNKNOWN(9999);				// 제작에 실패했습니다.
		private int value;
		private ProtoOutputStream cachedMessage;
		eCraftMakeReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		
		public void sendCachedMessage(L1PcInstance pc){
			if(cachedMessage == null){
				SC_CRAFT_MAKE_ACK ack = new SC_CRAFT_MAKE_ACK();
				ack.set_eResult(this);
				cachedMessage = ack.writeTo(MJEProtoMessages.SC_CRAFT_MAKE_ACK);
				ack.dispose();
			}
			pc.sendPackets(cachedMessage, false);
		}
		
		public boolean equals(eCraftMakeReqResultType v){
			return value == v.value;
		}
		private static final HashMap<Integer, eCraftMakeReqResultType> _eCraftMakeReqResultTypes;
		static{
			_eCraftMakeReqResultTypes = new HashMap<Integer, eCraftMakeReqResultType>(19);
			for(eCraftMakeReqResultType v : eCraftMakeReqResultType.values())
				_eCraftMakeReqResultTypes.put(v.toInt(), v);
		}
		public static eCraftMakeReqResultType fromInt(int i){
			return _eCraftMakeReqResultTypes.get(i);
		}
	}
}
