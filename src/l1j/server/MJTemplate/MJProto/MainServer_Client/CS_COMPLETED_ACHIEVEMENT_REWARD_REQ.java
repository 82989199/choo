package l1j.server.MJTemplate.MJProto.MainServer_Client;

import static l1j.server.server.model.skill.L1SkillId.천하장사버프;

import java.io.IOException;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSRewardsLoader;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterCriteriaProgress;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPLETED_ACHIEVEMENT_REWARD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPLETED_ACHIEVEMENT_REWARD_REQ newInstance(){
		return new CS_COMPLETED_ACHIEVEMENT_REWARD_REQ();
	}
	private int _achievement_id;
	private java.util.LinkedList<Integer> _optional_reward_indexes;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPLETED_ACHIEVEMENT_REWARD_REQ(){
	}
	public int get_achievement_id(){
		return _achievement_id;
	}
	public void set_achievement_id(int val){
		_bit |= 0x1;
		_achievement_id = val;
	}
	public boolean has_achievement_id(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<Integer> get_optional_reward_indexes(){
		return _optional_reward_indexes;
	}
	public void add_optional_reward_indexes(int val){
		if(!has_optional_reward_indexes()){
			_optional_reward_indexes = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_optional_reward_indexes.add(val);
	}
	public boolean has_optional_reward_indexes(){
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
		if (has_achievement_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _achievement_id);
		if (has_optional_reward_indexes()){
			for(int val : _optional_reward_indexes)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_achievement_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_achievement_id()){
			output.writeUInt32(1, _achievement_id);
		}
		if (has_optional_reward_indexes()){
			for(int val : _optional_reward_indexes){
				output.writeUInt32(2, val);
			}
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
					set_achievement_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					add_optional_reward_indexes(input.readUInt32());
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
				SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.send(pc, _achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_FAIL_ALREADY_GET_REWARD);
				pc.sendPackets("현재 도감 시스템이 일시 중지 상태입니다.");
				return this;
			}
			
			int reward_index = BQSLoadManager.achievementIdToAchievementLevel(_achievement_id);
			int criteria_id = BQSLoadManager.achievementIdToCriteriaId(_achievement_id);
			BQSCharacterData bqs = pc.getBqs();
			if(bqs == null)
				return this;
			
			CompletedAchievement achievement = bqs.get_achievement(_achievement_id);
			if(achievement == null){
				SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.send(pc, _achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_FAIL_NOT_COMPLETED);
				System.out.println(String.format("[도감] 할당되지 않은 보상을 시도했습니다. 캐릭터이름 : %s, 보상아이디 : %d, 북아이디 : %d", pc.getName(), _achievement_id, criteria_id));
				return this;
			}
			
			if(achievement.get_get_reward()){
				SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.send(pc, _achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_FAIL_ALREADY_GET_REWARD);
				System.out.println(String.format("[도감] 이미 받은 보상에 대해 재 보상을 시도했습니다. 캐릭터이름 : %s, 보상아이디 : %d, 북아이디 : %d", pc.getName(), _achievement_id, criteria_id));
				return this;
			}
			
			BQSCharacterCriteriaProgress progress = bqs.get_progress(criteria_id);
			BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformation(criteria_id);
			if(progress == null || bqsInfo == null){
				SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.send(pc, _achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_FAIL);
				System.out.println(String.format("[도감] 할당되지 않은 도감에 대해 보상을 시도했습니다. 캐릭터이름 : %s, 보상아이디 : %d, 북아이디 : %d", pc.getName(), _achievement_id, criteria_id));
				return this;
			}
			
			achievement.set_get_reward(true);
			progress.increase_current_achievement_level();
			if (pc.hasSkillEffect(천하장사버프))
				pc.removeSkillEffect(천하장사버프);
			pc.setSkillEffect(천하장사버프, 1800 * 1000);
			pc.addDamageReductionByArmor(5);
			pc.setDessertId(천하장사버프);
			pc.sendPackets(new S_ServerMessage(1426));
			L1Cooking.eatCooking(pc, 천하장사버프, 1800);
			
			BQSRewardsLoader.getInstance().doReward(pc, reward_index);
			SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.send(pc, _achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_SUCCESS);
			BQSCharacterDataLoader.storeCharacterBqs(bqs, false);
		} catch(Exception e){
			System.out.println(String.format("[도감] 몬스터북 보상 예외 %s", clnt.getIp()));
			e.printStackTrace();
			clnt.sendPacket(SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.newInstance(_achievement_id, SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.eResultCode.REQUEST_REWARD_FAIL), MJEProtoMessages.SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.toInt(), true);
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPLETED_ACHIEVEMENT_REWARD_REQ();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_optional_reward_indexes()){
			_optional_reward_indexes.clear();
			_optional_reward_indexes = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
