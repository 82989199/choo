package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.GameClient;
import l1j.server.server.utils.MJCommons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPANION_NAME_CHANGE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(GameClient clnt, eResult result, String new_name){
		byte[] buff = null;
		if(!MJCommons.isNullOrEmpty(new_name)){
			try {
				buff = new_name.getBytes("MS949");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		send_bytes(clnt, result, buff);
	}
	
	public static void send_bytes(GameClient clnt, eResult result, byte[] new_name){
		SC_COMPANION_NAME_CHANGE_ACK ack = newInstance();
		ack.set_result(result);
		if(new_name != null)
			ack.set_new_name(new_name);
		clnt.sendPacket(ack, MJEProtoMessages.SC_COMPANION_NAME_CHANGE_ACK.toInt(), true);
	}
	
	public static SC_COMPANION_NAME_CHANGE_ACK newInstance(){
		return new SC_COMPANION_NAME_CHANGE_ACK();
	}
	private eResult _result;
	private byte[] _new_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_COMPANION_NAME_CHANGE_ACK(){
	}
	public eResult get_result(){
		return _result;
	}
	public void set_result(eResult val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public byte[] get_new_name(){
		return _new_name;
	}
	public void set_new_name(byte[] val){
		_bit |= 0x2;
		_new_name = val;
	}
	public boolean has_new_name(){
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
		if (has_result())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		if (has_new_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _new_name);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_new_name()){
			output.writeBytes(2, _new_name);
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
					set_result(eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_new_name(input.readBytes());
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
		return new SC_COMPANION_NAME_CHANGE_ACK();
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
	public enum eResult{
		Success(0),
		SameNameExisted(1),
		SystemError(2);
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return Success;
			case 1:
				return SameNameExisted;
			case 2:
				return SystemError;
			default:
				return null;
			}
		}
	}
}
