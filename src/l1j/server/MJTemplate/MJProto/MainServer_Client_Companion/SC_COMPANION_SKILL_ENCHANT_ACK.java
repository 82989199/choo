package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPANION_SKILL_ENCHANT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, int skill_id, eResult result){
		SC_COMPANION_SKILL_ENCHANT_ACK ack = SC_COMPANION_SKILL_ENCHANT_ACK.newInstance();
		ack.set_skill_id(skill_id);
		ack.set_result(result);
		pc.sendPackets(ack, MJEProtoMessages.SC_COMPANION_SKILL_ENCHANT_ACK, true);
	}
	
	public static SC_COMPANION_SKILL_ENCHANT_ACK newInstance(){
		return new SC_COMPANION_SKILL_ENCHANT_ACK();
	}
	private int _skill_id;
	private eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_COMPANION_SKILL_ENCHANT_ACK(){
	}
	public int get_skill_id(){
		return _skill_id;
	}
	public void set_skill_id(int val){
		_bit |= 0x1;
		_skill_id = val;
	}
	public boolean has_skill_id(){
		return (_bit & 0x1) == 0x1;
	}
	public eResult get_result(){
		return _result;
	}
	public void set_result(eResult val){
		_bit |= 0x2;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_skill_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _skill_id);
		if (has_result())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _result.toInt());
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_skill_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_skill_id()){
			output.writeUInt32(1, _skill_id);
		}
		if (has_result()){
			output.writeEnum(2, _result.toInt());
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
					set_skill_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_result(eResult.fromInt(input.readEnum()));
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
		return new SC_COMPANION_SKILL_ENCHANT_ACK();
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
		SUCCESS(0),
		FAIL(1),
		NOTHING(2);
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
				return SUCCESS;
			case 1:
				return FAIL;
			case 2:
				return NOTHING;
			default:
				return null;
			}
		}
	}
}
