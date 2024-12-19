package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_BONUS_GROUP_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc){
		AttendanceGroupType.NORMAL.send(pc);
		AttendanceGroupType.PC_CAFE.send(pc);
	}
	
	public static void load(){
		SC_ATTENDANCE_BONUS_GROUP_INFO gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item");
		AttendanceGroupType.NORMAL.setGroupInfo(gInfo);
		
		gInfo = newInstance();
		createAttendanceBonusGroup(gInfo, "attendance_item_pc");
		AttendanceGroupType.PC_CAFE.setGroupInfo(gInfo);
	}
	
	private static void createAttendanceBonusGroup(SC_ATTENDANCE_BONUS_GROUP_INFO gInfo, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(String.format("SELECT * FROM %s ORDER BY id ASC", table));
			rs = pstm.executeQuery();
			int idx = 0;
			while(rs.next())
				gInfo.add_bonusGroup(AttendanceBonusGroup.newInstance(rs, ++idx));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static SC_ATTENDANCE_BONUS_GROUP_INFO newInstance(){
		return new SC_ATTENDANCE_BONUS_GROUP_INFO();
	}
	private AttendanceGroupType _groupType;
	private ArrayList<AttendanceBonusGroup> _bonusGroup;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ATTENDANCE_BONUS_GROUP_INFO(){
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
	public ArrayList<AttendanceBonusGroup> get_bonusGroup(){
		return _bonusGroup;
	}
	public void add_bonusGroup(AttendanceBonusGroup val){
		if(!has_bonusGroup()){
			_bonusGroup = new ArrayList<AttendanceBonusGroup>(32);
			_bit |= 0x00000002;
		}
		_bonusGroup.add(val);
	}
	public boolean has_bonusGroup(){
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
		if (has_groupType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _groupType.toInt());
		if (has_bonusGroup()){
			for(AttendanceBonusGroup val : _bonusGroup)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
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
		if (has_bonusGroup()){
			for(AttendanceBonusGroup val : _bonusGroup){
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
		if (has_groupType()){
			output.writeEnum(1, _groupType.toInt());
		}
		if (has_bonusGroup()){
			for(AttendanceBonusGroup val : _bonusGroup){
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
					set_groupType(AttendanceGroupType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_bonusGroup((AttendanceBonusGroup)input.readMessage(AttendanceBonusGroup.newInstance()));
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
		return new SC_ATTENDANCE_BONUS_GROUP_INFO();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_bonusGroup()){
			for(AttendanceBonusGroup val : _bonusGroup)
				val.dispose();
			_bonusGroup.clear();
			_bonusGroup = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
