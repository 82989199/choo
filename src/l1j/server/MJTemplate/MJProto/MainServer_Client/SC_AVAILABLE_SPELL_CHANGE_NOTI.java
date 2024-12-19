package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_AVAILABLE_SPELL_CHANGE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_AVAILABLE_SPELL_CHANGE_NOTI newInstance(){
		return new SC_AVAILABLE_SPELL_CHANGE_NOTI();
	}
	private Type _type;
	private byte[] _spells;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_AVAILABLE_SPELL_CHANGE_NOTI(){
	}
	public Type get_type(){
		return _type;
	}
	public void set_type(Type val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
	}
	public byte[] get_spells(){
		return _spells;
	}
	public void set_spells(byte[] val){
		_bit |= 0x2;
		_spells = val;
	}
	public boolean has_spells(){
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
		if (has_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		if (has_spells())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _spells);
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
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
		}
		if (has_spells()){
			output.writeBytes(2, _spells);
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
					set_type(Type.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_spells(input.readBytes());
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
		return new SC_AVAILABLE_SPELL_CHANGE_NOTI();
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
	public enum Type{
		EQUIP_ADD(1),
		UNEQUIP_REMOVE(2);
		private int value;
		Type(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(Type v){
			return value == v.value;
		}
		public static Type fromInt(int i){
			switch(i){
			case 1:
				return EQUIP_ADD;
			case 2:
				return UNEQUIP_REMOVE;
			default:
				return null;
			}
		}
	}
}
