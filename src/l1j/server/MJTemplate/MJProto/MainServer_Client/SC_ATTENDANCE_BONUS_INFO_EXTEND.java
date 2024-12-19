package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_BONUS_INFO_EXTEND implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static ProtoOutputStream _default_bonusInfo;

	public static void load(){
		try{
		SC_ATTENDANCE_BONUS_INFO_EXTEND bonusInfo = (SC_ATTENDANCE_BONUS_INFO_EXTEND)MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND.getMessageInstance();
		bonusInfo.set_checkInterval(MJAttendanceLoadManager.ATTEN_CHECK_INTERVAL);
		bonusInfo.set_resetPeriod(MJAttendanceLoadManager.ATTEN_RESET_PERIOD_SECOND);
		bonusInfo.set_dailyMaxCount(MJAttendanceLoadManager.ATTEN_DAILY_MAX_COUNT + 1);
		bonusInfo.set_weekendMaxCount(MJAttendanceLoadManager.ATTEN_WEEKEND_MAX_COUNT);
		bonusInfo.set_totalBonusGroupCount(AttendanceGroupType.values().length);
		
		ProtoOutputStream old_bonusInfo = _default_bonusInfo;
		_default_bonusInfo = bonusInfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_INFO_EXTEND);
		if(old_bonusInfo != null){
			old_bonusInfo.dispose();
			old_bonusInfo = null;
		}
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static void send(L1PcInstance pc){
		pc.sendPackets(_default_bonusInfo, false);
	}
	
	public static SC_ATTENDANCE_BONUS_INFO_EXTEND newInstance(){
		return new SC_ATTENDANCE_BONUS_INFO_EXTEND();
	}
	private int _checkInterval;
	private int _resetPeriod;
	private int _dailyMaxCount;
	private int _weekendMaxCount;
	private int _totalBonusGroupCount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ATTENDANCE_BONUS_INFO_EXTEND(){
	}
	public int get_checkInterval(){
		return _checkInterval;
	}
	public void set_checkInterval(int val){
		_bit |= 0x00000001;
		_checkInterval = val;
	}
	public boolean has_checkInterval(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_resetPeriod(){
		return _resetPeriod;
	}
	public void set_resetPeriod(int val){
		_bit |= 0x00000002;
		_resetPeriod = val;
	}
	public boolean has_resetPeriod(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_dailyMaxCount(){
		return _dailyMaxCount;
	}
	public void set_dailyMaxCount(int val){
		_bit |= 0x00000004;
		_dailyMaxCount = val;
	}
	public boolean has_dailyMaxCount(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public int get_weekendMaxCount(){
		return _weekendMaxCount;
	}
	public void set_weekendMaxCount(int val){
		_bit |= 0x00000008;
		_weekendMaxCount = val;
	}
	public boolean has_weekendMaxCount(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_totalBonusGroupCount(){
		return _totalBonusGroupCount;
	}
	public void set_totalBonusGroupCount(int val){
		_bit |= 0x00000010;
		_totalBonusGroupCount = val;
	}
	public boolean has_totalBonusGroupCount(){
		return (_bit & 0x00000010) == 0x00000010;
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
		if (has_checkInterval())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _checkInterval);
		if (has_resetPeriod())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _resetPeriod);
		if (has_dailyMaxCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _dailyMaxCount);
		if (has_weekendMaxCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _weekendMaxCount);
		if (has_totalBonusGroupCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _totalBonusGroupCount);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_checkInterval()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_resetPeriod()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dailyMaxCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weekendMaxCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalBonusGroupCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_checkInterval()){
			output.wirteInt32(1, _checkInterval);
		}
		if (has_resetPeriod()){
			output.wirteInt32(2, _resetPeriod);
		}
		if (has_dailyMaxCount()){
			output.wirteInt32(3, _dailyMaxCount);
		}
		if (has_weekendMaxCount()){
			output.wirteInt32(4, _weekendMaxCount);
		}
		if (has_totalBonusGroupCount()){
			output.wirteInt32(5, _totalBonusGroupCount);
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
					set_checkInterval(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_resetPeriod(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_dailyMaxCount(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_weekendMaxCount(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_totalBonusGroupCount(input.readInt32());
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
		return new SC_ATTENDANCE_BONUS_INFO_EXTEND();
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
