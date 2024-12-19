package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ACHIEVEMENT_COMPLETE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ACHIEVEMENT_COMPLETE_NOTI newInstance(CompletedAchievement achievement){
		SC_ACHIEVEMENT_COMPLETE_NOTI noti = newInstance();
		noti.set_achievement_id(achievement.get_achievement_id());
		noti.set_completed_time(achievement.get_completed_time());
		return noti;
	}
	
	public static SC_ACHIEVEMENT_COMPLETE_NOTI newInstance(){
		return new SC_ACHIEVEMENT_COMPLETE_NOTI();
	}
	private int _achievement_id;
	private long _completed_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ACHIEVEMENT_COMPLETE_NOTI(){
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
	public long get_completed_time(){
		return _completed_time;
	}
	public void set_completed_time(long val){
		_bit |= 0x2;
		_completed_time = val;
	}
	public boolean has_completed_time(){
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
		if (has_achievement_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _achievement_id);
		if (has_completed_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(2, _completed_time);
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
		if (!has_completed_time()){
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
		if (has_completed_time()){
			output.wirteUInt64(2, _completed_time);
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
				case 0x00000010:{
					set_completed_time(input.readUInt64());
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
		return new SC_ACHIEVEMENT_COMPLETE_NOTI();
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
