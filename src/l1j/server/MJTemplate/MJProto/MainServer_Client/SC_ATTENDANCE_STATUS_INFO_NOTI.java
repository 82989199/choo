package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_STATUS_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ATTENDANCE_STATUS_INFO_NOTI newInstance(){
		return new SC_ATTENDANCE_STATUS_INFO_NOTI();
	}
	private ATTENDANCE_INFO _attendance_info;
	private int _extend_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ATTENDANCE_STATUS_INFO_NOTI(){
	}
	public ATTENDANCE_INFO get_attendance_info(){
		return _attendance_info;
	}
	public void set_attendance_info(ATTENDANCE_INFO val){
		_bit |= 0x1;
		_attendance_info = val;
	}
	public boolean has_attendance_info(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_extend_info(){
		return _extend_info;
	}
	public void set_extend_info(int val){
		_bit |= 0x2;
		_extend_info = val;
	}
	public boolean has_extend_info(){
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
		if (has_attendance_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _attendance_info);
		if (has_extend_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _extend_info);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_attendance_info()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_attendance_info()){
			output.writeMessage(1, _attendance_info);
		}
		if (has_extend_info()){
			output.wirteInt32(2, _extend_info);
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
					set_attendance_info((ATTENDANCE_INFO)input.readMessage(ATTENDANCE_INFO.newInstance()));
					break;
				}
				case 0x00000010:{
					set_extend_info(input.readInt32());
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
		return new SC_ATTENDANCE_STATUS_INFO_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_attendance_info() && _attendance_info != null){
			_attendance_info.dispose();
			_attendance_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
