package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PARTY_OPERATION_RESULT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PARTY_OPERATION_RESULT_NOTI newInstance(){
		return new SC_PARTY_OPERATION_RESULT_NOTI();
	}
	private ePARTY_OPERATION_TYPE _type;
	private String _actor_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PARTY_OPERATION_RESULT_NOTI(){
	}
	public ePARTY_OPERATION_TYPE get_type(){
		return _type;
	}
	public void set_type(ePARTY_OPERATION_TYPE val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_actor_name(){
		return _actor_name;
	}
	public void set_actor_name(String val){
		_bit |= 0x2;
		_actor_name = val;
	}
	public boolean has_actor_name(){
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
		if (has_actor_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _actor_name);
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
		if (!has_actor_name()){
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
		if (has_actor_name()){
			output.writeString(2, _actor_name);
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
					set_type(ePARTY_OPERATION_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_actor_name(input.readString());
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
		return new SC_PARTY_OPERATION_RESULT_NOTI();
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
	public enum ePARTY_OPERATION_TYPE{
		ePARTY_OPERATION_TYPE_INVITE_REJECT(1),
		ePARTY_OPERATION_TYPE_INVITE_ACCEPT(2);
		private int value;
		ePARTY_OPERATION_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePARTY_OPERATION_TYPE v){
			return value == v.value;
		}
		public static ePARTY_OPERATION_TYPE fromInt(int i){
			switch(i){
			case 1:
				return ePARTY_OPERATION_TYPE_INVITE_REJECT;
			case 2:
				return ePARTY_OPERATION_TYPE_INVITE_ACCEPT;
			default:
				return null;
			}
		}
	}
}
