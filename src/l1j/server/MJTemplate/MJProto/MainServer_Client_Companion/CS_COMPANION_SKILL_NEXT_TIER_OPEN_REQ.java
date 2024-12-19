package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.Skill;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;

import java.io.IOException;

import l1j.server.MJCompanion.Basic.Skills.MJCompanionClassSkillInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillTierOpenInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ newInstance(){
		return new CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ();
	}
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ(){
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
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
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
			if(companion == null)
				return this;

			int source_tier = companion.get_tier();
			int next_tier = source_tier + 1;
			MJCompanionSkillTierOpenInfo oInfo = MJCompanionSkillTierOpenInfo.get_tier_open_info(next_tier);
			MJCompanionClassSkillInfo next_class_skill_info = MJCompanionClassSkillInfo.get_companion_skills(companion.get_class_info().get_class_name(), next_tier);
			if(oInfo == null || next_class_skill_info == null){
				pc.sendPackets("다음 단계 정보를 찾을 수 없습니다.");
				System.out.println(String.format("%님의 펫 %s(%d) 야성 열기에 실패했습니다. 현재 티어 : %d, 다음 티어 : %d", pc.getName(), companion.getName(), companion.getId(), source_tier, next_tier));
				return this;
			}
			MJCompanionClassSkillInfo source_class_skill_info = MJCompanionClassSkillInfo.get_companion_skills(companion.get_class_info().get_class_name(), source_tier);
			if(source_class_skill_info == null){
				pc.sendPackets("현재 단계 정보를 찾을 수 없습니다.");
				System.out.println(String.format("%님의 펫 %s(%d)[%s] 야성 열기에 실패했습니다. 현재 티어 : %d, 다음 티어 : %d", pc.getName(), companion.getName(), companion.getId(), companion.get_class_info().get_class_name(), source_tier, next_tier));
				return this;				
			}
			if(companion.getLevel() < oInfo.get_open_cost_level()){
				pc.sendPackets(String.format("%d레벨 이상부터 사용 가능합니다.", oInfo.get_open_cost_level()));
				return this;
			}
			if(!check_enchant(companion, source_class_skill_info.get_skills_id(), oInfo.get_open_cost_min_enchant())){
				pc.sendPackets(String.format("%d단계 야성의 인챈트가 부족합니다. (필요 인챈트:%d)", source_tier, oInfo.get_open_cost_min_enchant()));
				return this;
			}
			if(!pc.getInventory().checkItem(L1ItemId.ADENA, oInfo.get_open_cost_adena()) ||
					!pc.getInventory().consumeItem(L1ItemId.ADENA,  oInfo.get_open_cost_adena())){
				pc.sendPackets(String.format("%d단계 야성을 활성화 하기 위해서는 아데나 %d이(가) 필요합니다.", next_tier, oInfo.get_open_cost_adena()));
				return this;
			}
			companion.do_open_tier(next_class_skill_info);
			companion.update_tier();
			companion.update_skill_effect();
			SC_COMPANION_SKILL_NOTI.send(pc, companion);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	private boolean check_enchant(MJCompanionInstance companion, int[] check_skills_id, int min_enchant){
		for(int skill_id : check_skills_id){
			Skill sk = companion.find_skills(skill_id);
			if(sk == null || sk.get_enchant() < min_enchant)
				return false;
		}
		return true;
	}
	
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ();
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
