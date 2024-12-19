package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_TELEPORT_ACk.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ACHIEVEMENT_TELEPORT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_ACHIEVEMENT_TELEPORT_REQ newInstance(){
		return new CS_ACHIEVEMENT_TELEPORT_REQ();
	}
	private int _achievement_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_ACHIEVEMENT_TELEPORT_REQ(){
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
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(!BQSLoadManager.BQS_IS_ONUPDATE_BOOKS){
				SC_ACHIEVEMENT_TELEPORT_ACk.send(pc, _achievement_id, eResultCode.TELEPORT_FAIL);
				pc.sendPackets("현재 도감 시스템이 일시 중지 상태입니다.");
				return this;
			}
			
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			int criteria_id = BQSLoadManager.achievementIdToCriteriaId(_achievement_id);
			BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformation(criteria_id);
			if(bqsInfo == null){
				System.out.println(String.format("[도감] 존재하지 않는 도감에 텔레포트를 시도했습니다. 캐릭터이름 : %s, 보상아이디 : %d, 북아이디 : %d", pc.getName(), _achievement_id, criteria_id));
				SC_ACHIEVEMENT_TELEPORT_ACk.send(pc, _achievement_id, eResultCode.TELEPORT_FAIL);
				return this;
			}
			SC_ACHIEVEMENT_TELEPORT_ACk.send(pc, _achievement_id, bqsInfo.doTeleport(pc));			
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_ACHIEVEMENT_TELEPORT_REQ();
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
