package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_INVENTORY_EFFECT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_INVENTORY_EFFECT_NOTI newInstance(){
		return new SC_INVENTORY_EFFECT_NOTI();
	}
	private int _item_id;
	private eEffectCodeType _effect_code;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INVENTORY_EFFECT_NOTI(){
	}
	public int get_item_id(){
		return _item_id;
	}
	public void set_item_id(int val){
		_bit |= 0x1;
		_item_id = val;
	}
	public boolean has_item_id(){
		return (_bit & 0x1) == 0x1;
	}
	public eEffectCodeType get_effect_code(){
		return _effect_code;
	}
	public void set_effect_code(eEffectCodeType val){
		_bit |= 0x2;
		_effect_code = val;
	}
	public boolean has_effect_code(){
		return (_bit & 0x2) == 0x2;
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
		if (has_item_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _item_id);
		if (has_effect_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _effect_code.toInt());
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_item_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_effect_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_item_id()){
			output.writeUInt32(1, _item_id);
		}
		if (has_effect_code()){
			output.writeEnum(2, _effect_code.toInt());
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
					set_item_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_effect_code(eEffectCodeType.fromInt(input.readEnum()));
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
		return new SC_INVENTORY_EFFECT_NOTI();
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
	public enum eEffectCodeType{
		Enchant_Elemental_Fire(1),
		Enchant_Elemental_Water(2),
		Enchant_Elemental_Air(3),
		Enchant_Elemental_Earth(4),
		Gear_Enchant(5),
		Life(6);
		private int value;
		eEffectCodeType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eEffectCodeType v){
			return value == v.value;
		}
		public static eEffectCodeType fromInt(int i){
			switch(i){
			case 1:
				return Enchant_Elemental_Fire;
			case 2:
				return Enchant_Elemental_Water;
			case 3:
				return Enchant_Elemental_Air;
			case 4:
				return Enchant_Elemental_Earth;
			case 5:
				return Gear_Enchant;
			case 6:
				return Life;
			default:
				return null;
			}
		}
	}
}
