package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_DATA_UPDATE_EXTEND implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ATTENDANCE_DATA_UPDATE_EXTEND newInstance(){
		return new SC_ATTENDANCE_DATA_UPDATE_EXTEND();
	}
	private int _index;
	private int _state;
	private AttendanceGroupType _group;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ATTENDANCE_DATA_UPDATE_EXTEND(){
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
	public int get_state(){
		return _state;
	}
	public void set_state(int val){
		_bit |= 0x00000002;
		_state = val;
	}
	
	public void set_state(UserAttendanceState val){
		set_state(val.toInt());
	}
	
	public boolean has_state(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public AttendanceGroupType get_group(){
		return _group;
	}
	public void set_group(AttendanceGroupType val){
		_bit |= 0x00000004;
		_group = val;
	}
	public boolean has_group(){
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
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _state);
		if (has_group())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _group.toInt());
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
		if (has_state()){
			output.wirteInt32(2, _state);
		}
		if (has_group()){
			output.writeEnum(3, _group.toInt());
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
					set_state(input.readInt32());
					break;
				}
				case 0x00000018:{
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

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_ATTENDANCE_DATA_UPDATE_EXTEND();
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
