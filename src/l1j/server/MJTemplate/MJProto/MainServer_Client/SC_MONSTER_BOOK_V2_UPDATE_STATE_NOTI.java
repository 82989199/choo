package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2DeckState;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, MonsterBookV2Info.DeckT deck){
		SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI noti = newInstance();
		noti.set_deck_index(deck.get_index());
		noti.set_state(deck.get_state());
		pc.sendPackets(noti, MJEProtoMessages.SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI, true);
	}
	
	public static SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI newInstance(){
		return new SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI();
	}
	private int _deck_index;
	private eMonsterBookV2DeckState _state;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI(){
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
	public eMonsterBookV2DeckState get_state(){
		return _state;
	}
	public void set_state(eMonsterBookV2DeckState val){
		_bit |= 0x2;
		_state = val;
	}
	public boolean has_state(){
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
		if (has_deck_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _deck_index);
		if (has_state())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _state.toInt());
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
		if (!has_state()){
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
		if (has_state()){
			output.writeEnum(2, _state.toInt());
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
					set_state(eMonsterBookV2DeckState.fromInt(input.readEnum()));
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
		return new SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI();
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
