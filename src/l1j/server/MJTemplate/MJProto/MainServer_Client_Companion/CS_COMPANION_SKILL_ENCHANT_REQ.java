package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_ENCHANT_ACK.eResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.Skill;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJCompanion.Basic.Skills.MJCompanionClassSkillInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillEnchantInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionUpdateFlag;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPANION_SKILL_ENCHANT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPANION_SKILL_ENCHANT_REQ newInstance(){
		return new CS_COMPANION_SKILL_ENCHANT_REQ();
	}
	private int _skill_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPANION_SKILL_ENCHANT_REQ(){
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
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_skill_id()){
			output.writeUInt32(1, _skill_id);
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
			
			MJCompanionInstance companion = pc.get_companion();
			if(companion == null){
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;
			}
			int tier = MJCompanionClassSkillInfo.id_to_tier(_skill_id);
			if(tier == -1){
				System.out.println(String.format("%s님이 알 수 없는 스킬아이디로 인챈트를 시도했습니다. 스킬아이디 : %d", pc.getName(), _skill_id));
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;
			}
			if(companion.get_tier() < tier){
				System.out.println(String.format("%s님이 높은 티어의 스킬아이디로 인챈트를 시도했습니다. 스킬아이디 : %d, 스킬티어 : %d, 펫 티어 : %d", pc.getName(), _skill_id, tier, companion.get_tier()));
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;
			}
			
			Skill sk = companion.find_skills(_skill_id);
			if(sk == null){
				System.out.println(String.format("%s님이 획득되지 않은 스킬아이디로 인챈트를 시도했습니다. 스킬아이디 : %d, 스킬티어 : %d, 펫 티어 : %d", pc.getName(), _skill_id, tier, companion.get_tier()));
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;				
			}
			MJCompanionSkillEnchantInfo eInfo = MJCompanionSkillEnchantInfo.get_enchant_info(tier, sk.get_enchant() + 1);
			if(eInfo == null){
				System.out.println(String.format("%s님이 시도한 인챈트 스킬에 대한 다음 정보를 찾을 수 없습니다. 스킬아이디 : %d, 스킬티어 : %d, 펫 티어 : %d, 현재인챈트 : %d", pc.getName(), _skill_id, tier, companion.get_tier(), sk.get_enchant()));
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;								
			}
			if(companion.get_friend_ship_marble() < eInfo.get_enchant_cost_friend_ship() || !pc.getInventory().checkItem(eInfo.get_enchant_cost_item_id())){
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;
			}
			if(!pc.getInventory().consumeItem(eInfo.get_enchant_cost_item_id(), 1)){
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.NOTHING);
				return this;				
			}
			companion.set_friend_ship_marble(companion.get_friend_ship_marble() - eInfo.get_enchant_cost_friend_ship());
			if(!MJRnd.isWinning(1000000, eInfo.get_probability())){
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.FAIL);
				SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP);
			}else{
				sk.set_enchant(eInfo.get_enchant());
				companion.update_skill_effect();
				SC_COMPANION_SKILL_ENCHANT_ACK.send(pc, _skill_id, eResult.SUCCESS);
				SC_COMPANION_SKILL_NOTI.send(pc, sk);
				SC_COMPANION_STATUS_NOTI.send(
						pc, companion, 
						MJCompanionUpdateFlag.UPDATE_HP | 
						MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE | 
						MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP | 
						MJCompanionUpdateFlag.UPDATE_MR | 
						MJCompanionUpdateFlag.UPDATE_PVP_DAMAGE | 
						MJCompanionUpdateFlag.UPDATE_AC);
			}
			companion.update_friend_ship_marble();
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPANION_SKILL_ENCHANT_REQ();
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
