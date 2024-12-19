package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ATTENDANCE_BONUS_REQUEST_EXTEND implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_ATTENDANCE_BONUS_REQUEST_EXTEND newInstance(){
		return new CS_ATTENDANCE_BONUS_REQUEST_EXTEND();
	}
	private int _index;
	private AttendanceGroupType _group;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_ATTENDANCE_BONUS_REQUEST_EXTEND(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x00000001;
		_memorizedSerializedSize = -1;
		_index = val;
	}
	public boolean has_index(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public AttendanceGroupType get_group(){
		return _group;
	}
	public void set_group(AttendanceGroupType val){
		_bit |= 0x00000002;
		_memorizedSerializedSize = -1;
		_group = val;
	}
	public boolean has_group(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		if (has_group())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _group.toInt());
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_index()){
			output.wirteInt32(1, _index);
		}
		if (has_group()){
			output.writeEnum(2, _group.toInt());
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
					set_index(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_group(AttendanceGroupType.fromInt(input.readEnum()));
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
			
			if(_group == null){
				System.out.println(String.format("[출첵]알 수 없는 출첵 그룹! 케릭명 : %s", pc.getName()));
				return this;
			}
			
			UserAttendanceDataGroup group = userData.get_groups().get(_group.toInt());
			UserAttendanceTimeStatus timeStatus = group.get_resultCode();
			if(timeStatus.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR)){
				System.out.println(String.format("[출첵]모든 보상을 받은 유저가 보상을 시도! 케릭명 : %s, 그룹 : %s", pc.getName(), _group.name()));
				pc.sendPackets("이미 모든 보상을 받으셨습니다.");
				return this;
			}
			
			UserAttendanceDataExtend attendance_data = group.get_groupData().get(_index - 1);
			if(!attendance_data.get_state().equals(UserAttendanceState.COMPLETE)){
				System.out.println(String.format("[출첵]상태에 맞지 않는 보상을 요구! 케릭명 : %s, 그룹 : %s, 인덱스 : %d, 상태 : %s", pc.getName(), _group.name(), attendance_data.get_index(), attendance_data.get_state().name()));
				pc.sendPackets("보상을 받을 수 없습니다.");
				return this;
			}
			
			_group.doReward(pc, _index - 1);
			attendance_data.set_state(UserAttendanceState.CLEAR);
			SC_ATTENDANCE_DATA_UPDATE_EXTEND update = SC_ATTENDANCE_DATA_UPDATE_EXTEND.newInstance();
			update.set_index(_index);
			update.set_group(_group);
			update.set_state(UserAttendanceState.CLEAR);
			pc.sendPackets(update, MJEProtoMessages.SC_ATTENDANCE_DATA_UPDATE_EXTEND, true);
			SC_ATTENDANCE_USER_DATA_EXTEND.update(pc.getAccountName(), userData);
		} catch(Exception e){
			System.out.println(clnt.getIp());
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_ATTENDANCE_BONUS_REQUEST_EXTEND();
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
