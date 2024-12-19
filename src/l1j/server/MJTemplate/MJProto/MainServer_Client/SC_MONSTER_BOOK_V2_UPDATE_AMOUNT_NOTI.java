package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI newInstance(){
		return new SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI();
	}
	private int _deck_index;
	private int _card_index;
	private int _amount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI(){
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
	public int get_amount(){
		return _amount;
	}
	public void set_amount(int val){
		_bit |= 0x4;
		_amount = val;
	}
	public boolean has_amount(){
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
		if (has_amount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _amount);
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
		if (!has_amount()){
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
		if (has_amount()){
			output.writeUInt32(3, _amount);
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
					set_amount(input.readUInt32());
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
		return new SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI();
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
