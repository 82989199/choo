package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSWQRewardsLoader;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2DeckState;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2RewardGrade;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_MONSTER_BOOK_V2_REWARD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_MONSTER_BOOK_V2_REWARD_REQ newInstance(){
		return new CS_MONSTER_BOOK_V2_REWARD_REQ();
	}
	private int _deck_index;
	private eMonsterBookV2RewardGrade _grade;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_MONSTER_BOOK_V2_REWARD_REQ(){
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
	public eMonsterBookV2RewardGrade get_grade(){
		return _grade;
	}
	public void set_grade(eMonsterBookV2RewardGrade val){
		_bit |= 0x2;
		_grade = val;
	}
	public boolean has_grade(){
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
		if (has_grade())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _grade.toInt());
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
		if (!has_grade()){
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
		if (has_grade()){
			output.writeEnum(2, _grade.toInt());
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
					set_grade(eMonsterBookV2RewardGrade.fromInt(input.readEnum()));
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
				SC_MONSTER_BOOK_V2_REWARD_ACK.send(pc, _deck_index, _grade, eResultCode.REWARD_FAIL);
				pc.sendPackets("현재 도감 시스템이 일시 중지 상태입니다.");
				return this;
			}
			
			BQSCharacterData bqs = pc.getBqs();
			if(bqs == null)
				return this;
			
			MonsterBookV2Info.DeckT deck = bqs.get_deck(_deck_index);
			if(deck == null){
				SC_MONSTER_BOOK_V2_REWARD_ACK.send(pc, _deck_index, _grade, eResultCode.REWARD_FAIL_INVALID_INDEX);
				System.out.println(String.format("[도감] 할당되지 않은 주간퀘스트 보상을 시도했습니다. 캐릭터이름 : %s, 보상라인 : %d, 보상종류 : %s", pc.getName(), _deck_index, _grade.name()));
				return this;
			}
			
			eMonsterBookV2DeckState state = deck.get_state();
			if(!state.equals(eMonsterBookV2DeckState.DS_COMPLETED)){
				SC_MONSTER_BOOK_V2_REWARD_ACK.send(pc, _deck_index, _grade, 
						state.equals(eMonsterBookV2DeckState.DS_IN_PROGRESS) ? eResultCode.REWARD_FAIL_NOT_ENOUGH_AMOUNT : eResultCode.REWARD_FAIL);
				System.out.println(String.format("[도감] 완료상태가 아닌 상태에서 주간퀘스트 보상을 시도했습니다. 캐릭터이름 : %s, 보상라인 : %d, 보상종류 : %s, 상태 : %s", pc.getName(), _deck_index, _grade.name(), state.name()));
				return this;
			}
			
			SC_MONSTER_BOOK_V2_REWARD_ACK.send(pc, _deck_index, _grade, BQSWQRewardsLoader.getInstance().doReward(pc, _grade));
			deck.set_state(eMonsterBookV2DeckState.DS_REWARDED);
			SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI.send(pc, deck);
			BQSCharacterDataLoader.storeCharacterBqs(bqs, false);
		} catch(Exception e){
			System.out.println(String.format("[도감] 주간퀘스트 보상 예외. %s", clnt.getIp()));
			e.printStackTrace();
			clnt.sendPacket(SC_MONSTER_BOOK_V2_REWARD_ACK.newInstance(_deck_index, _grade, eResultCode.REWARD_FAIL), MJEProtoMessages.SC_MONSTER_BOOK_V2_REWARD_ACK.toInt());
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_MONSTER_BOOK_V2_REWARD_REQ();
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
