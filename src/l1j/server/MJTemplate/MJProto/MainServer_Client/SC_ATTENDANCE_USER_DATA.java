package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_USER_DATA implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc){
		SC_ATTENDANCE_USER_DATA data = newInstance();
		SC_ATTENDANCE_USER_DATA_EXTEND extend = pc.getAttendanceData();
		for(UserAttendanceDataGroup dataGroup : extend.get_groups()){
			UserAttendanceData userData = UserAttendanceData.newInstance();
			userData.set_attendanceIndex(dataGroup.get_currentAttendanceIndex());
			int resultCode = dataGroup.get_resultCode().toInt();
			if(resultCode == UserAttendanceTimeStatus.ATTENDANCE_ALL_CLEAR.toInt()){
				userData.set_attendanceState(UserAttendanceState.CLEAR);				
			}else if(resultCode == UserAttendanceTimeStatus.ATTENDANCE_ALL_COMPLETE.toInt()){
				userData.set_attendanceState(UserAttendanceState.COMPLETE);								
			}else{
				userData.set_attendanceState(UserAttendanceState.INIT);				
			}
			data.add_attendanceData(userData);
		}
		pc.sendPackets(data.writeTo(MJEProtoMessages.SC_ATTENDANCE_USER_DATA), true);
	}
	
	public static SC_ATTENDANCE_USER_DATA newInstance(){
		return new SC_ATTENDANCE_USER_DATA();
	}
	private java.util.LinkedList<UserAttendanceData> _attendanceData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ATTENDANCE_USER_DATA(){
	}
	public java.util.LinkedList<UserAttendanceData> get_attendanceData(){
		return _attendanceData;
	}
	public void add_attendanceData(UserAttendanceData val){
		if(!has_attendanceData()){
			_attendanceData = new java.util.LinkedList<UserAttendanceData>();
			_bit |= 0x1;
		}
		_attendanceData.add(val);
	}
	public boolean has_attendanceData(){
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
		if (has_attendanceData()){
			for(UserAttendanceData val : _attendanceData)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_attendanceData()){
			for(UserAttendanceData val : _attendanceData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_attendanceData()){
			for(UserAttendanceData val : _attendanceData){
				output.writeMessage(1, val);
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
				case 0x0000000A:{
					add_attendanceData((UserAttendanceData)input.readMessage(UserAttendanceData.newInstance()));
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
		return new SC_ATTENDANCE_USER_DATA();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_attendanceData()){
			for(UserAttendanceData val : _attendanceData)
				val.dispose();
			_attendanceData.clear();
			_attendanceData = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class UserAttendanceData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static UserAttendanceData newInstance(){
			return new UserAttendanceData();
		}
		private int _attendanceIndex;
		private UserAttendanceState _attendanceState;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private UserAttendanceData(){
		}
		public int get_attendanceIndex(){
			return _attendanceIndex;
		}
		public void set_attendanceIndex(int val){
			_bit |= 0x1;
			_attendanceIndex = val;
		}
		public boolean has_attendanceIndex(){
			return (_bit & 0x1) == 0x1;
		}
		public UserAttendanceState get_attendanceState(){
			return _attendanceState;
		}
		public void set_attendanceState(UserAttendanceState val){
			_bit |= 0x2;
			_attendanceState = val;
		}
		public boolean has_attendanceState(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_attendanceIndex())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendanceIndex);
			if (has_attendanceState())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _attendanceState.toInt());
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_attendanceIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_attendanceState()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_attendanceIndex()){
				output.wirteInt32(1, _attendanceIndex);
			}
			if (has_attendanceState()){
				output.writeEnum(2, _attendanceState.toInt());
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
						set_attendanceIndex(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_attendanceState(UserAttendanceState.fromInt(input.readEnum()));
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
			return new UserAttendanceData();
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
}
