package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ATTENDANCE_REWARD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_ATTENDANCE_REWARD_REQ newInstance(){
		return new CS_ATTENDANCE_REWARD_REQ();
	}
	private int _attendance_id;
	private int _group_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_ATTENDANCE_REWARD_REQ(){
	}
	public int get_attendance_id(){
		return _attendance_id;
	}
	public void set_attendance_id(int val){
		_bit |= 0x1;
		_attendance_id = val;
	}
	public boolean has_attendance_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_group_id(){
		return _group_id;
	}
	public void set_group_id(int val){
		_bit |= 0x2;
		_group_id = val;
	}
	public boolean has_group_id(){
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
		if (has_attendance_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendance_id);
		if (has_group_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_attendance_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_attendance_id()){
			output.wirteInt32(1, _attendance_id);
		}
		if (has_group_id()){
			output.wirteInt32(2, _group_id);
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
					set_attendance_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_group_id(input.readInt32());
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
			
			SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
			if(userData == null)
				return this;
			
			AttendanceGroupType egroup = AttendanceGroupType.fromInt(_group_id);
			if(egroup == null){
				System.out.println(String.format("[출첵]알 수 없는 출첵 그룹! 케릭명 : %s", pc.getName()));
				return this;
			}
			
			UserAttendanceDataGroup group = userData.get_groups().get(egroup.toInt());
			UserAttendanceTimeStatus timeStatus = group.get_resultCode();
			if(timeStatus.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR)){
				System.out.println(String.format("[출첵]모든 보상을 받은 유저가 보상을 시도! 케릭명 : %s, 그룹 : %s", pc.getName(), egroup.name()));
				pc.sendPackets("이미 모든 보상을 받으셨습니다.");
				return this;
			}
			
			UserAttendanceDataExtend attendance_data = group.get_groupData().get(_attendance_id - 1);
			if(!attendance_data.get_state().equals(UserAttendanceState.COMPLETE)){
				System.out.println(String.format("[출첵]상태에 맞지 않는 보상을 요구! 케릭명 : %s, 그룹 : %s, 인덱스 : %d, 상태 : %s", pc.getName(), egroup.name(), attendance_data.get_index(), attendance_data.get_state().name()));
				pc.sendPackets("보상을 받을 수 없습니다.");
				return this;
			}
			
			egroup.doReward(pc, _attendance_id - 1);
			attendance_data.set_state(UserAttendanceState.CLEAR);
			
			SC_ATTENDANCE_DATA_UPDATE_EXTEND update = SC_ATTENDANCE_DATA_UPDATE_EXTEND.newInstance();
			update.set_index(_attendance_id);
			update.set_group(egroup);
			update.set_state(UserAttendanceState.CLEAR);
			pc.sendPackets(update, MJEProtoMessages.SC_ATTENDANCE_DATA_UPDATE_EXTEND, true);
			
			SC_ATTENDANCE_REWARD_ACK ack = SC_ATTENDANCE_REWARD_ACK.newInstance();
			ack.set_attendance_id(_attendance_id);
			ack.set_group_id(_group_id);
			ack.set_status(UserAttendanceState.CLEAR);
			pc.sendPackets(ack, MJEProtoMessages.SC_ATTENDANCE_REWARD_ACK, true);
			
			SC_ATTENDANCE_USER_DATA_EXTEND.update(pc.getAccountName(), userData);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_ATTENDANCE_REWARD_REQ();
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
