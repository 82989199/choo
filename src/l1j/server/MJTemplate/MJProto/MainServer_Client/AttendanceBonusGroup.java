package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class AttendanceBonusGroup implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static AttendanceBonusGroup newInstance(ResultSet rs, int index) throws SQLException{
		AttendanceBonusGroup group = newInstance();
		group.set_attendaceIndex(index);
		group.add_bonuses(AttendanceBonusInfo.newInstance(rs));
		return group;
	}
	
	public static AttendanceBonusGroup newInstance(){
		return new AttendanceBonusGroup();
	}
	private int _attendaceIndex;
	private LinkedList<AttendanceBonusInfo> _bonuses;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private AttendanceBonusGroup(){
	}
	public int get_attendaceIndex(){
		return _attendaceIndex;
	}
	public void set_attendaceIndex(int val){
		_bit |= 0x00000001;
		_attendaceIndex = val;
	}
	public boolean has_attendaceIndex(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public LinkedList<AttendanceBonusInfo> get_bonuses(){
		return _bonuses;
	}
	public void add_bonuses(AttendanceBonusInfo val){
		if(!has_bonuses()){
			_bonuses = new LinkedList<AttendanceBonusInfo>();
			_bit |= 0x00000002;
		}
		_bonuses.add(val);
	}
	public boolean has_bonuses(){
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
		if (has_attendaceIndex())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendaceIndex);
		if (has_bonuses()){
			for(AttendanceBonusInfo val : _bonuses)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_attendaceIndex()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_bonuses()){
			for(AttendanceBonusInfo val : _bonuses){
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
		if (has_attendaceIndex()){
			output.wirteInt32(1, _attendaceIndex);
		}
		if (has_bonuses()){
			for(AttendanceBonusInfo val : _bonuses){
				output.writeMessage(2, val);
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
				case 0x00000008:{
					set_attendaceIndex(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_bonuses((AttendanceBonusInfo)input.readMessage(AttendanceBonusInfo.newInstance()));
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
		return new AttendanceBonusGroup();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_bonuses()){
			for(AttendanceBonusInfo val : _bonuses)
				val.dispose();
			_bonuses.clear();
			_bonuses = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
