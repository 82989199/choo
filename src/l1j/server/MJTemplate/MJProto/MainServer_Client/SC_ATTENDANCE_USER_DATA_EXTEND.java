package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_USER_DATA_EXTEND implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static void send(L1PcInstance pc){
		if(pc.getAttendanceData() == null)
			register(pc);
		pc.sendPackets(pc.getAttendanceData().writeTo(MJEProtoMessages.SC_ATTENDANCE_USER_DATA_EXTEND), true);
	}
	
	public static void update(String account, SC_ATTENDANCE_USER_DATA_EXTEND userData) throws IOException{
		ProtoOutputStream stream = ProtoOutputStream.newLocalInstance(userData.getSerializedSize());
		userData.writeTo(stream);
		final byte[] b = stream.getBytes();
		update(account, b, userData.createIndices());
		stream.dispose();
	}
	
	public static void update(String account, byte[] data, String indices){
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			Timestamp ts = new Timestamp(MJAttendanceLoadManager.ATTEN_STARTUP_CALENDAR.getTimeInMillis());
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("update attendance_userInfo set attendance_data=?, attendance_indices=?, attendance_calendar=? where account=?");
			pstm.setBytes(1, data);
			pstm.setString(2, indices);
			pstm.setTimestamp(3, ts);
			pstm.setString(4, account);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void store(L1PcInstance pc){
		SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
		if(userData == null)
			return;
		
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			ProtoOutputStream stream = ProtoOutputStream.newLocalInstance(userData.getSerializedSize());
			userData.writeTo(stream);
			byte[] b = stream.getBytes();
			stream.dispose();
			
			Timestamp ts = new Timestamp(MJAttendanceLoadManager.ATTEN_STARTUP_CALENDAR.getTimeInMillis());

			String indices = userData.createIndices();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("insert into attendance_userInfo set account=?, attendance_data=?, attendance_indices=?, attendance_calendar=? on duplicate key update attendance_data=?, attendance_indices=?, attendance_calendar=?");
			pstm.setString(1, pc.getAccountName());
			pstm.setBytes(2, b);
			pstm.setString(3, indices);
			pstm.setTimestamp(4, ts);
			pstm.setBytes(5, b);
			pstm.setString(6, indices);
			pstm.setTimestamp(7, ts);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void register(L1PcInstance pc){
		SC_ATTENDANCE_USER_DATA_EXTEND userData = newInstance();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from attendance_userInfo where account=? limit 1");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			if(rs.next()){
				userData.readFrom(ProtoInputStream.newInstance(rs.getBytes("attendance_data")));
				pc.setAttendanceData(userData);
				Integer[] ids = (Integer[])MJArrangeParser.parsing(rs.getString("attendance_indices"), ",", MJArrangeParseeFactory.createIntArrange()).result();
				int size = ids.length;
				for(int i=0; i<size; ++i)
					userData.get_groups().get(i).set_currentAttendanceIndex(ids[i]);
				
				userData.set_userFlag(pc.PC방_버프 ? AttendanceUserFlag.USER_FLAG_PC_CAFE : AttendanceUserFlag.USER_FLAG_NORMAL);
				Calendar systemCal = MJAttendanceLoadManager.ATTEN_STARTUP_CALENDAR;
				Calendar userCal = (Calendar)systemCal.clone();
				userCal.setTimeInMillis(rs.getTimestamp("attendance_calendar").getTime());
				if((systemCal.getTimeInMillis() / 1000) != (userCal.getTimeInMillis() / 1000)){
					boolean isPcCafe = userData._userFlag.equals(AttendanceUserFlag.USER_FLAG_PC_CAFE);
					for(UserAttendanceDataGroup g : userData._groups)
						g.onUpdateDay(isPcCafe);
					store(pc);
				}
				userCal.clear();
			}else{
				for(AttendanceGroupType gType : AttendanceGroupType.values()){
					userData.add_groups(UserAttendanceDataGroup.newInstance(pc, gType));
					userData.add_active_group(gType);
				}
				pc.setAttendanceData(userData);
				userData.set_userFlag(pc.PC방_버프 ? AttendanceUserFlag.USER_FLAG_PC_CAFE : AttendanceUserFlag.USER_FLAG_NORMAL);
				store(pc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static SC_ATTENDANCE_USER_DATA_EXTEND newInstance(){
		return new SC_ATTENDANCE_USER_DATA_EXTEND();
	}
	
	private ArrayList<UserAttendanceDataGroup> _groups;
	private AttendanceUserFlag _userFlag;
	private ArrayList<AttendanceGroupType> _active_group;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	public void onUpdateDay(L1PcInstance pc, int second) throws IOException{
		boolean isUpdate = false;
		if(_userFlag == null)
			return;
		
		int size = _userFlag.toInt();
		for(int i=0; i<=size; ++i){
			UserAttendanceDataGroup dGroup = _groups.get(i);
			isUpdate |= dGroup.onUpdateTime(second);
		}
		
		boolean isPcCafe = _userFlag.equals(AttendanceUserFlag.USER_FLAG_PC_CAFE);
		for(UserAttendanceDataGroup g : _groups)
			isUpdate |= g.onUpdateDay(isPcCafe);
		
		if(isUpdate){
			ProtoOutputStream stream = ProtoOutputStream.newLocalInstance(getSerializedSize());
			writeTo(stream);
			final byte[] b = stream.getBytes();
			update(pc.getAccountName(), b, createIndices());
			pc.sendPackets(ProtoOutputStream.newInstance(b, MJEProtoMessages.SC_ATTENDANCE_USER_DATA_EXTEND.toInt()), true);
			stream.dispose();
		}
	}
	
	public void onUpdateTime(L1PcInstance pc, int second) throws IOException{
		boolean isUpdate = false;
		if(_userFlag == null)
			return;
		
		int size = _userFlag.toInt();
		for(int i=0; i<=size; ++i){
			UserAttendanceDataGroup dGroup = _groups.get(i);
			isUpdate |= dGroup.onUpdateTime(second);
		}
		
		if(isUpdate){
			ProtoOutputStream stream = ProtoOutputStream.newLocalInstance(getSerializedSize());
			writeTo(stream);
			final byte[] b = stream.getBytes();
			update(pc.getAccountName(), b, createIndices());		
			pc.sendPackets(ProtoOutputStream.newInstance(b, MJEProtoMessages.SC_ATTENDANCE_USER_DATA_EXTEND.toInt()), true);
			stream.dispose();
		}
	}
	
	public String createIndices(){
		if(has_groups()){
			int size = _groups.size();
			StringBuilder sb = new StringBuilder(size * 3);
			for(int i=0; i<size; ++i){
				if(i != 0)
					sb.append(",");
				sb.append(_groups.get(i).get_currentAttendanceIndex());
			}
			return sb.toString();
		}
		return "";
	}
	
	private SC_ATTENDANCE_USER_DATA_EXTEND(){
	}
	public ArrayList<UserAttendanceDataGroup> get_groups(){
		return _groups;
	}
	public void add_groups(UserAttendanceDataGroup val){
		if(!has_groups()){
			_groups = new ArrayList<UserAttendanceDataGroup>(AttendanceGroupType.values().length);
			_bit |= 0x00000001;
		}
		_groups.add(val);
	}
	public boolean has_groups(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public AttendanceUserFlag get_userFlag(){
		return _userFlag;
	}
	public void set_userFlag(AttendanceUserFlag val){
		_bit |= 0x00000002;
		_userFlag = val;
		
		if(!has_groups())
			return;
		
		UserAttendanceDataGroup dGroup = _groups.get(AttendanceUserFlag.USER_FLAG_PC_CAFE.toInt());
		if(val.equals(AttendanceUserFlag.USER_FLAG_NORMAL)){
			dGroup.set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_CANT_BE_ACHIEVE_TIME);
		}else{
			int index = dGroup.get_currentAttendanceIndex();
			if(index <= dGroup.get_groupData().size() - 1){
				UserAttendanceDataExtend gData = dGroup.get_groupData().get(index);
				if(gData.get_state().equals(UserAttendanceState.INIT)){
					dGroup.set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_NORMAL);
				}else{
					dGroup.set_resultCode(UserAttendanceTimeStatus.ATTENDANCE_NO_MORE_TIME);
				}
			}
		}
	}
	public boolean has_userFlag(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public ArrayList<AttendanceGroupType> get_active_group(){
		return _active_group;
	}
	public void add_active_group(AttendanceGroupType val){
		if(!has_active_group()){
			_active_group = new ArrayList<AttendanceGroupType>(AttendanceGroupType.values().length);
			_bit |= 0x00000004;
		}
		_active_group.add(val);
	}
	public boolean has_active_group(){
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
		if (has_groups()){
			for(UserAttendanceDataGroup val : _groups)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_userFlag())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _userFlag.toInt());
		if (has_active_group()){
			for(AttendanceGroupType val : _active_group)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, val.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_groups()){
			for(UserAttendanceDataGroup val : _groups){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_userFlag()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_active_group()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_groups()){
			for(UserAttendanceDataGroup val : _groups){
				output.writeMessage(1, val);
			}
		}
		if (has_userFlag()){
			output.writeEnum(2, _userFlag.toInt());
		}
		if (has_active_group()){
			for(AttendanceGroupType val : _active_group){
				output.writeEnum(3, val.toInt());
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
					add_groups((UserAttendanceDataGroup)input.readMessage(UserAttendanceDataGroup.newInstance()));
					break;
				}
				case 0x00000010:{
					set_userFlag(AttendanceUserFlag.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018:{
					add_active_group(AttendanceGroupType.fromInt(input.readEnum()));
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
		return new SC_ATTENDANCE_USER_DATA_EXTEND();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_groups()){
			for(UserAttendanceDataGroup val : _groups)
				val.dispose();
			_groups.clear();
			_groups = null;
		}
		if (has_active_group()){
			_active_group.clear();
			_active_group = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
