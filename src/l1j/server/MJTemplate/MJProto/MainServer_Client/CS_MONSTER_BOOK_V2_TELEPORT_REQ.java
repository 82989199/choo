package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSWQDecksLoader;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_TELEPORT_ACK.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_MONSTER_BOOK_V2_TELEPORT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_MONSTER_BOOK_V2_TELEPORT_REQ newInstance(){
		return new CS_MONSTER_BOOK_V2_TELEPORT_REQ();
	}
	private int _deck_index;
	private int _card_index;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_MONSTER_BOOK_V2_TELEPORT_REQ(){
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
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(!BQSLoadManager.BQS_IS_ONUPDATE_BOOKS){
				SC_MONSTER_BOOK_V2_TELEPORT_ACK.send(pc, _deck_index, _card_index, eResultCode.TELEPORT_FAIL);
				pc.sendPackets("현재 도감 시스템이 일시 중지 상태입니다.");
				return this;
			}
			
			if(_deck_index >= BQSLoadManager.BQS_WQ_HEIGHT || _card_index >= BQSLoadManager.BQS_WQ_WIDTH){
				SC_MONSTER_BOOK_V2_TELEPORT_ACK.send(pc, _deck_index, _card_index, eResultCode.TELEPORT_FAIL);
				System.out.println(String.format("[도감] 존재하지 않는 주간퀘스트 인덱스로 접근을 시도했습니다.(텔레포트). 캐릭터이름 : %s, 난이도 : %d, 순번 : %d", pc.getName(), _deck_index, _card_index));
				return this;
			}
			
			MonsterBookV2Info.DeckT.CardT card = BQSWQDecksLoader.getInstance().findDeck(_deck_index, _card_index);
			if(card == null){
				SC_MONSTER_BOOK_V2_TELEPORT_ACK.send(pc, _deck_index, _card_index, eResultCode.TELEPORT_FAIL);
				System.out.println(String.format("[도감] 존재하지 않는 주간퀘스트로 접근을 시도했습니다.(텔레포트). 캐릭터이름 : %s, 난이도 : %d, 순번 : %d", pc.getName(), _deck_index, _card_index));
				return this;
			}
			BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformation(card.get_criteria_id());
			SC_MONSTER_BOOK_V2_TELEPORT_ACK.send(pc, _deck_index, _card_index, bqsInfo.doTeleport(pc).modulation());
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_MONSTER_BOOK_V2_TELEPORT_REQ();
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
