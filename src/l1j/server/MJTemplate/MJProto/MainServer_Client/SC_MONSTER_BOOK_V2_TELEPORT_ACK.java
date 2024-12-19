package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MONSTER_BOOK_V2_TELEPORT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, int deck_index, int card_index, eResultCode resultCode){
		SC_MONSTER_BOOK_V2_TELEPORT_ACK ack = newInstance();
		ack.set_deck_index(deck_index);
		ack.set_card_index(card_index);
		ack.set_result(resultCode);
		pc.sendPackets(ack, MJEProtoMessages.SC_MONSTER_BOOK_V2_TELEPORT_ACK, true);
	}
	
	public static SC_MONSTER_BOOK_V2_TELEPORT_ACK newInstance(){
		return new SC_MONSTER_BOOK_V2_TELEPORT_ACK();
	}
	private int _deck_index;
	private int _card_index;
	private eResultCode _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_MONSTER_BOOK_V2_TELEPORT_ACK(){
	}
	public int get_deck_index(){
		return _deck_index;
	}
	public void set_deck_index(int val){
		_bit |= 0x1;
		_deck_index = val;
	}
	public boolean has_deck_index(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_card_index(){
		return _card_index;
	}
	public void set_card_index(int val){
		_bit |= 0x2;
		_card_index = val;
	}
	public boolean has_card_index(){
		return (_bit & 0x2) == 0x2;
	}
	public eResultCode get_result(){
		return _result;
	}
	public void set_result(eResultCode val){
		_bit |= 0x4;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x4) == 0x4;
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
		if (has_deck_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _deck_index);
		if (has_card_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _card_index);
		if (has_result())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _result.toInt());
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_deck_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_card_index()){
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
		if (has_deck_index()){
			output.writeUInt32(1, _deck_index);
		}
		if (has_card_index()){
			output.writeUInt32(2, _card_index);
		}
		if (has_result()){
			output.writeEnum(3, _result.toInt());
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
					set_deck_index(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_card_index(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_result(eResultCode.fromInt(input.readEnum()));
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
		return new SC_MONSTER_BOOK_V2_TELEPORT_ACK();
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
		TELEPORT_SUCCESS(1),
		TELEPORT_FAIL(112),
		TELEPORT_FAIL_NOT_ENOUGH_ADENA(113),
		TELEPORT_FAIL_WRONG_LOCATION(114);
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
			case 1:
				return TELEPORT_SUCCESS;
			case 112:
				return TELEPORT_FAIL;
			case 113:
				return TELEPORT_FAIL_NOT_ENOUGH_ADENA;
			case 114:
				return TELEPORT_FAIL_WRONG_LOCATION;
			default:
				return null;
			}
		}
	}
}
