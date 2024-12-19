package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPLETED_ACHIEVEMENT_REWARD_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, int achievement_id, eResultCode resultCode){
		pc.sendPackets(newInstance(achievement_id, resultCode), MJEProtoMessages.SC_COMPLETED_ACHIEVEMENT_REWARD_ACK, true);
	}
	
	public static SC_COMPLETED_ACHIEVEMENT_REWARD_ACK newInstance(int achievement_id, eResultCode resultCode){
		SC_COMPLETED_ACHIEVEMENT_REWARD_ACK ack = newInstance();
		ack.set_result(resultCode);
		ack.set_achievement_id(achievement_id);
		return ack;
	}
	
	public static SC_COMPLETED_ACHIEVEMENT_REWARD_ACK newInstance(){
		return new SC_COMPLETED_ACHIEVEMENT_REWARD_ACK();
	}
	private eResultCode _result;
	private int _achievement_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_COMPLETED_ACHIEVEMENT_REWARD_ACK(){
	}
	public eResultCode get_result(){
		return _result;
	}
	public void set_result(eResultCode val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_achievement_id(){
		return _achievement_id;
	}
	public void set_achievement_id(int val){
		_bit |= 0x2;
		_achievement_id = val;
	}
	public boolean has_achievement_id(){
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
		if (has_achievement_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _achievement_id);
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
		if (!has_achievement_id()){
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
		if (has_achievement_id()){
			output.writeUInt32(2, _achievement_id);
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
					set_result(eResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_achievement_id(input.readUInt32());
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
		return new SC_COMPLETED_ACHIEVEMENT_REWARD_ACK();
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
	public enum eResultCode{
		REQUEST_REWARD_SUCCESS(0),
		REQUEST_REWARD_FAIL(1),
		REQUEST_REWARD_FAIL_ALREADY_GET_REWARD(2),
		REQUEST_REWARD_FAIL_NOT_COMPLETED(3);
		private int value;
		eResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResultCode v){
			return value == v.value;
		}
		public static eResultCode fromInt(int i){
			switch(i){
			case 0:
				return REQUEST_REWARD_SUCCESS;
			case 1:
				return REQUEST_REWARD_FAIL;
			case 2:
				return REQUEST_REWARD_FAIL_ALREADY_GET_REWARD;
			case 3:
				return REQUEST_REWARD_FAIL_NOT_COMPLETED;
			default:
				return null;
			}
		}
	}
}
