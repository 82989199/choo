package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.LinkedList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CRAFT_ID_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_CRAFT_ID_LIST_ACK newInstance(){
		return new SC_CRAFT_ID_LIST_ACK();
	}
	private eCraftIdListReqResultType _eResult;
	private LinkedList<CraftIdList> _craft_id_list;
	private int _eBlindType;
	private eCraftNpcType _npc_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_CRAFT_ID_LIST_ACK(){
	}
	public eCraftIdListReqResultType get_eResult(){
		return _eResult;
	}
	public void set_eResult(eCraftIdListReqResultType val){
		_bit |= 0x00000001;
		_eResult = val;
	}
	public boolean has_eResult(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public LinkedList<CraftIdList> get_craft_id_list(){
		return _craft_id_list;
	}
	public void add_craft_id_list(CraftIdList val){
		if(!has_craft_id_list()){
			_craft_id_list = new LinkedList<CraftIdList>();
			_bit |= 0x00000002;
		}
		_craft_id_list.add(val);
	}
	public boolean has_craft_id_list(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_eBlindType(){
		return _eBlindType;
	}
	public void set_eBlindType(int val){
		_bit |= 0x00000004;
		_eBlindType = val;
	}
	public boolean has_eBlindType(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public eCraftNpcType get_npc_type(){
		return _npc_type;
	}
	public void set_npc_type(eCraftNpcType val){
		_bit |= 0x00000008;
		_npc_type = val;
	}
	public boolean has_npc_type(){
		return (_bit & 0x00000008) == 0x00000008;
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
		if (has_craft_id_list()){
			for(CraftIdList val : _craft_id_list)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		if (has_eBlindType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _eBlindType);
		if (has_npc_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _npc_type.toInt());
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
		if (has_craft_id_list()){
			for(CraftIdList val : _craft_id_list){
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
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(MJEProtoMessages message){
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
		if (has_craft_id_list()){
			for(CraftIdList val : _craft_id_list){
				output.writeMessage(2, val);
			}
		}
		if (has_eBlindType()){
			output.wirteInt32(3, _eBlindType);
		}
		if (has_npc_type()){
			output.writeEnum(4, _npc_type.toInt());
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
					set_eResult(eCraftIdListReqResultType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_craft_id_list((CraftIdList)input.readMessage(CraftIdList.newInstance()));
					break;
				}
				case 0x00000018:{
					set_eBlindType(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_npc_type(eCraftNpcType.fromInt(input.readEnum()));
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
		return new SC_CRAFT_ID_LIST_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_craft_id_list()){
			for(CraftIdList val : _craft_id_list)
				val.dispose();
			_craft_id_list.clear();
			_craft_id_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CraftIdList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CraftIdList newInstance(){
			return new CraftIdList();
		}
		private int _craft_id;
		private int _max_success_cnt;
		private int _cur_success_cnt;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CraftIdList(){
		}
		public int get_craft_id(){
			return _craft_id;
		}
		public void set_craft_id(int val){
			_bit |= 0x00000001;
			_craft_id = val;
		}
		public boolean has_craft_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public int get_max_success_cnt(){
			return _max_success_cnt;
		}
		public void set_max_success_cnt(int val){
			_bit |= 0x00000002;
			_max_success_cnt = val;
		}
		public boolean has_max_success_cnt(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		public int get_cur_success_cnt(){
			return _cur_success_cnt;
		}
		public void set_cur_success_cnt(int val){
			_bit |= 0x00000004;
			_cur_success_cnt = val;
		}
		public boolean has_cur_success_cnt(){
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
			if (has_craft_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _craft_id);
			if (has_max_success_cnt())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _max_success_cnt);
			if (has_cur_success_cnt())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cur_success_cnt);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_craft_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
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
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(MJEProtoMessages message){
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
			if (has_craft_id()){
				output.wirteInt32(1, _craft_id);
			}
			if (has_max_success_cnt()){
				output.wirteInt32(2, _max_success_cnt);
			}
			if (has_cur_success_cnt()){
				output.wirteInt32(3, _cur_success_cnt);
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
						set_craft_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_max_success_cnt(input.readInt32());
						break;
					}
					case 0x00000018:{
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
			return new CraftIdList();
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
	public enum eCraftIdListReqResultType{
		RP_SUCCESS(0),
		RP_ERROR_INVALID_NPC_ID(1),
		RP_ERROR_OUT_OF_RANGE(2),
		RP_ERROR_UNKNOWN(9999);
		private int value;
		private ProtoOutputStream cachedMessage;
		eCraftIdListReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		
		public void sendCachedMessage(L1PcInstance pc){
			if(cachedMessage == null){
				SC_CRAFT_ID_LIST_ACK ack = new SC_CRAFT_ID_LIST_ACK();
				ack.set_eResult(this);
				cachedMessage = ack.writeTo(MJEProtoMessages.SC_CRAFT_ID_LIST_ACK);
				ack.dispose();
			}
			pc.sendPackets(cachedMessage, false);
		}
		
		public boolean equals(eCraftIdListReqResultType v){
			return value == v.value;
		}
		public static eCraftIdListReqResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_SUCCESS;
			case 1:
				return RP_ERROR_INVALID_NPC_ID;
			case 2:
				return RP_ERROR_OUT_OF_RANGE;
			case 9999:
				return RP_ERROR_UNKNOWN;
			default:
				return null;
			}
		}
	}
	public enum eCraftNpcType{
		DEFAULT(0),
		MAGIC_DOLL(1);
		private int value;
		eCraftNpcType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftNpcType v){
			return value == v.value;
		}
		public static eCraftNpcType fromInt(int i){
			switch(i){
			case 0:
				return DEFAULT;
			case 1:
				return MAGIC_DOLL;
			default:
				return null;
			}
		}
	}
}
