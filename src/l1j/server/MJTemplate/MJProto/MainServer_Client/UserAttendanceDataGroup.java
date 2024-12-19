package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class UserAttendanceDataGroup implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{	
	public static UserAttendanceDataGroup newInstance(L1PcInstance pc, AttendanceGroupType gType){
		UserAttendanceDataGroup dGroup = newInstance();
		dGroup.set_groupType(gType);
		dGroup.set_attendanceTime(0);
		dGroup.set_totalAttendanceTime(MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND);
		dGroup.set_recvedRewardCount(0);
		dGroup.set_totalRewardCount(MJAttendanceLoadManager.ATTEN_DAILY_MAX_COUNT);
		dGroup.set_currentAttendanceIndex(0);
		int totalBonusCount = gType.getBonusSize();
		for(int i=0; i<totalBonusCount; ++i)
			dGroup.add_groupData(UserAttendanceDataExtend.newInstance(i+1, UserAttendanceState.INIT, 0));
		
		dGroup.set_resultCode(!gType.equals(AttendanceGroupType.PC_CAFE) ? UserAttendanceTimeStatus.ATTENDANCE_NORMAL : UserAttendanceTimeStatus.ATTENDANCE_CANT_BE_ACHIEVE_TIME);
		return dGroup;
	}
	
	public static UserAttendanceDataGroup newInstance(){
		return new UserAttendanceDataGroup();
	}
	private int	_currentAttendanceIndex;
	private AttendanceGroupType _groupType;
	private ArrayList<UserAttendanceDataExtend> _groupData;
	private UserAttendanceTimeStatus _resultCode;
	private int _recvedRewardCount;
	private int _totalRewardCount;
	private int _attendanceTime;
	private int _totalAttendanceTime;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	public boolean onUpdateDay(boolean isPcCafe){
		if(_resultCode.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR) || _resultCode.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_COMPLETE))
			return false;
		
		if(_currentAttendanceIndex > _groupData.size() - 1){
			int clearcount = 0;
			for(UserAttendanceDataExtend gData : _groupData){
				if(gData.get_state().equals(UserAttendanceState.CLEAR))
					++clearcount;
			}
			_resultCode = clearcount >= _groupData.size() ? UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR : UserAttendanceTimeStatus.ATTENDANCE_ALL_COMPLETE;
		}else{
			UserAttendanceDataExtend gData = _groupData.get(_currentAttendanceIndex);
			set_attendanceTime(0);
			set_totalAttendanceTime(MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND);
			set_recvedRewardCount(0);
			set_totalRewardCount(MJAttendanceLoadManager.ATTEN_DAILY_MAX_COUNT);
			if(gData.get_state().equals(UserAttendanceState.INIT)){
				gData.set_time(0);
			}else{
				++_currentAttendanceIndex;
			}
			if(_groupType.equals(AttendanceGroupType.PC_CAFE) && !isPcCafe)
				_resultCode = UserAttendanceTimeStatus.ATTENDANCE_CANT_BE_ACHIEVE_TIME;
			else
				_resultCode = UserAttendanceTimeStatus.ATTENDANCE_NORMAL;
		}
		return true;
	}
	
	public boolean onUpdateTime(int second){
		if(!_resultCode.equals(UserAttendanceTimeStatus.ATTENDANCE_NORMAL))
			return false;
		
		if(_currentAttendanceIndex > _groupData.size() - 1){
			_resultCode.equals(UserAttendanceTimeStatus.ATTENDANCE_ALL_COMPLETE);
			return true;
		}
		
		UserAttendanceDataExtend attendance_data = _groupData.get(_currentAttendanceIndex);
		if(!attendance_data.get_state().equals(UserAttendanceState.INIT))
			return false;
		
		boolean isUpdate = false;
		int updateTime = _attendanceTime + second;
		if(updateTime >= MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND){
			updateTime = MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND;
			set_recvedRewardCount(_recvedRewardCount + 1);
			attendance_data.set_state(UserAttendanceState.COMPLETE);
			if(get_recvedRewardCount() >= get_totalRewardCount()){
				set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_NO_MORE_TIME);
			}else{					
				if(++_currentAttendanceIndex > _groupData.size() - 1)
					set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_ALL_COMPLETE);
				attendance_data.set_time(updateTime);
				_attendanceTime = 0;
				return true;
			}
			isUpdate = true;
		}
		attendance_data.set_time(updateTime);
		_attendanceTime = updateTime;
		return isUpdate;
	}
	
	public void set_currentAttendanceIndex(int index){
		_currentAttendanceIndex = index;
	}
	
	public int get_currentAttendanceIndex(){
		return _currentAttendanceIndex;
	}
	
	private UserAttendanceDataGroup(){
	}
	public AttendanceGroupType get_groupType(){
		return _groupType;
	}
	public void set_groupType(AttendanceGroupType val){
		_bit |= 0x00000001;
		_groupType = val;
	}
	public boolean has_groupType(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public ArrayList<UserAttendanceDataExtend> get_groupData(){
		return _groupData;
	}
	public void add_groupData(UserAttendanceDataExtend val){
		if(!has_groupData()){
			_groupData = new ArrayList<UserAttendanceDataExtend>(48);
			_bit |= 0x00000002;
		}
		_groupData.add(val);
	}
	public boolean has_groupData(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public UserAttendanceTimeStatus get_resultCode(){
		return _resultCode;
	}
	public void set_resultCode(UserAttendanceTimeStatus val){
		_bit |= 0x00000004;
		_resultCode = val;
	}
	public boolean has_resultCode(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public int get_recvedRewardCount(){
		return _recvedRewardCount;
	}
	public void set_recvedRewardCount(int val){
		_bit |= 0x00000008;
		_recvedRewardCount = val;
	}
	public boolean has_recvedRewardCount(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_totalRewardCount(){
		return _totalRewardCount;
	}
	public void set_totalRewardCount(int val){
		_bit |= 0x00000010;
		_totalRewardCount = val;
	}
	public boolean has_totalRewardCount(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public int get_attendanceTime(){
		return _attendanceTime;
	}
	public void set_attendanceTime(int val){
		_bit |= 0x00000020;
		_attendanceTime = val;
	}
	public boolean has_attendanceTime(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	public int get_totalAttendanceTime(){
		return _totalAttendanceTime;
	}
	public void set_totalAttendanceTime(int val){
		_bit |= 0x00000040;
		_totalAttendanceTime = val;
	}
	public boolean has_totalAttendanceTime(){
		return (_bit & 0x00000040) == 0x00000040;
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
		
		if (has_groupType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _groupType.toInt());
		
		if (has_groupData()){
			for(UserAttendanceDataExtend val : _groupData)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		if (has_resultCode())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _resultCode.toInt());
		if (has_recvedRewardCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _recvedRewardCount);
		if (has_totalRewardCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _totalRewardCount);
		if (has_attendanceTime())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _attendanceTime);
		if (has_totalAttendanceTime())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _totalAttendanceTime);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_groupType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_groupData()){
			for(UserAttendanceDataExtend val : _groupData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_resultCode()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_recvedRewardCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalRewardCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_attendanceTime()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalAttendanceTime()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_groupType()){
			output.writeEnum(1, _groupType.toInt());
		}
		if (has_groupData()){
			for(UserAttendanceDataExtend val : _groupData){
				output.writeMessage(2, val);
			}
		}
		if (has_resultCode()){
			output.writeEnum(3, _resultCode.toInt());
		}
		if (has_recvedRewardCount()){
			output.wirteInt32(4, _recvedRewardCount);
		}
		if (has_totalRewardCount()){
			output.wirteInt32(5, _totalRewardCount);
		}
		if (has_attendanceTime()){
			output.wirteInt32(6, _attendanceTime);
		}
		if (has_totalAttendanceTime()){
			output.wirteInt32(7, _totalAttendanceTime);
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
					set_groupType(AttendanceGroupType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_groupData((UserAttendanceDataExtend)input.readMessage(UserAttendanceDataExtend.newInstance()));
					break;
				}
				case 0x00000018:{
					set_resultCode(UserAttendanceTimeStatus.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_recvedRewardCount(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_totalRewardCount(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_attendanceTime(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_totalAttendanceTime(input.readInt32());
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
		return new UserAttendanceDataGroup();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_groupData()){
			for(UserAttendanceDataExtend val : _groupData)
				val.dispose();
			_groupData.clear();
			_groupData = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
