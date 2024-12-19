package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class UserAttendanceDataExtend implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static UserAttendanceDataExtend newInstance(){
		return new UserAttendanceDataExtend();
	}
	
	public static UserAttendanceDataExtend newInstance(int index, UserAttendanceState state, int time){
		UserAttendanceDataExtend data = newInstance();
		data.set_index(index);
		data.set_state(state);
		data.set_time(time);
		return data;
	}
	
	private int _index;
	private UserAttendanceState _state;
	private int _time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private UserAttendanceDataExtend(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x00000001;
		_index = val;
	}
	public boolean has_index(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public UserAttendanceState get_state(){
		return _state;
	}
	public void set_state(UserAttendanceState val){
		_bit |= 0x00000002;
		_state = val;
	}
	public boolean has_state(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_time(){
		return _time;
	}
	public void set_time(int val){
		_bit |= 0x00000004;
		_time = val;
	}
	
	public boolean addTimeAndOnUpdate(int second){
		if(!_state.equals(UserAttendanceState.INIT))
			return false;
		
		int updateTime = _time + second;
		if(updateTime >= MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND){
			updateTime = MJAttendanceLoadManager.ATTEN_TOTAL_TIMESECOND;
			set_state(UserAttendanceState.COMPLETE);
		}
		_time = updateTime;
		return !_state.equals(UserAttendanceState.INIT);
	}
	
	public boolean has_time(){
		return (_bit & 0x00000004) == 0x00000004;
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
		if (has_state())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _state.toInt());
		if (has_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _time);
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
		if (!has_state()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_time()){
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
		if (has_state()){
			output.writeEnum(2, _state.toInt());
		}
		if (has_time()){
			output.wirteInt32(3, _time);
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
					set_state(UserAttendanceState.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018:{
					set_time(input.readInt32());
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
		return new UserAttendanceDataExtend();
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
