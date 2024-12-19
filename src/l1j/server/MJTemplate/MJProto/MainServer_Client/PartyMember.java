package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class PartyMember implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PartyMember newInstance(L1PcInstance pc){
		return newInstance(pc, 0);
	}
	
	public static PartyMember newInstance(L1PcInstance pc, int markNo){
		PartyMember pm = PartyMember.newInstance();
		pm.set_accountid(0);
		pm.set_alive_time_stamp(System.currentTimeMillis());
		pm.set_game_class(pc.getClassNumber());
		pm.set_gender(pc.get_sex());
		pm.set_hp_ratio((int)pc.getCurrentHpPercent());
		pm.set_mp_ratio((int)pc.getCurrentMpPercent());
		pm.set_location(pc.getLongLocation());
		pm.set_name(pc.getName());
		pm.set_object_id(pc.getId());
		pm.set_party_mark(markNo);
		pm.set_server_no(0);
		pm.set_world(pc.getMapId());
		return pm;
	}
	
	public static PartyMember newInstance(){
		return new PartyMember();
	}
	private String _name;
	private int _accountid;
	private int _object_id;
	private int _game_class;
	private int _gender;
	private int _hp_ratio;
	private int _mp_ratio;
	private int _world;
	private int _location;
	private long _party_mark;
	private long _alive_time_stamp;
	private int _server_no;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PartyMember(){
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x1;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_accountid(){
		return _accountid;
	}
	public void set_accountid(int val){
		_bit |= 0x2;
		_accountid = val;
	}
	public boolean has_accountid(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x4;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_game_class(){
		return _game_class;
	}
	public void set_game_class(int val){
		_bit |= 0x8;
		_game_class = val;
	}
	public boolean has_game_class(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_gender(){
		return _gender;
	}
	public void set_gender(int val){
		_bit |= 0x10;
		_gender = val;
	}
	public boolean has_gender(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_hp_ratio(){
		return _hp_ratio;
	}
	public void set_hp_ratio(int val){
		_bit |= 0x20;
		_hp_ratio = val;
	}
	public boolean has_hp_ratio(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_mp_ratio(){
		return _mp_ratio;
	}
	public void set_mp_ratio(int val){
		_bit |= 0x40;
		_mp_ratio = val;
	}
	public boolean has_mp_ratio(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_world(){
		return _world;
	}
	public void set_world(int val){
		_bit |= 0x80;
		_world = val;
	}
	public boolean has_world(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_location(){
		return _location;
	}
	public void set_location(int val){
		_bit |= 0x100;
		_location = val;
	}
	public boolean has_location(){
		return (_bit & 0x100) == 0x100;
	}
	public long get_party_mark(){
		return _party_mark;
	}
	public void set_party_mark(long val){
		_bit |= 0x200;
		_party_mark = val;
	}
	public boolean has_party_mark(){
		return (_bit & 0x200) == 0x200;
	}
	public long get_alive_time_stamp(){
		return _alive_time_stamp;
	}
	public void set_alive_time_stamp(long val){
		_bit |= 0x400;
		_alive_time_stamp = val;
	}
	public boolean has_alive_time_stamp(){
		return (_bit & 0x400) == 0x400;
	}
	public int get_server_no(){
		return _server_no;
	}
	public void set_server_no(int val){
		_bit |= 0x800;
		_server_no = val;
	}
	public boolean has_server_no(){
		return (_bit & 0x800) == 0x800;
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
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _name);
		if (has_accountid())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _accountid);
		if (has_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _object_id);
		if (has_game_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _game_class);
		if (has_gender())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _gender);
		if (has_hp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _hp_ratio);
		if (has_mp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _mp_ratio);
		if (has_world())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _world);
		if (has_location())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _location);
		if (has_party_mark())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(10, _party_mark);
		if (has_alive_time_stamp())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(11, _alive_time_stamp);
		if (has_server_no())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _server_no);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_game_class()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_hp_ratio()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_mp_ratio()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_name()){
			output.writeString(1, _name);
		}
		if (has_accountid()){
			output.writeUInt32(2, _accountid);
		}
		if (has_object_id()){
			output.writeUInt32(3, _object_id);
		}
		if (has_game_class()){
			output.wirteInt32(4, _game_class);
		}
		if (has_gender()){
			output.wirteInt32(5, _gender);
		}
		if (has_hp_ratio()){
			output.wirteInt32(6, _hp_ratio);
		}
		if (has_mp_ratio()){
			output.wirteInt32(7, _mp_ratio);
		}
		if (has_world()){
			output.wirteInt32(8, _world);
		}
		if (has_location()){
			output.wirteInt32(9, _location);
		}
		if (has_party_mark()){
			output.writeInt64(10, _party_mark);
		}
		if (has_alive_time_stamp()){
			output.wirteUInt64(11, _alive_time_stamp);
		}
		if (has_server_no()){
			output.writeUInt32(12, _server_no);
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
					set_name(input.readString());
					break;
				}
				case 0x00000010:{
					set_accountid(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_game_class(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_gender(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_hp_ratio(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_mp_ratio(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_world(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_location(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_party_mark(input.readInt64());
					break;
				}
				case 0x00000058:{
					set_alive_time_stamp(input.readUInt64());
					break;
				}
				case 0x00000060:{
					set_server_no(input.readUInt32());
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
		return new PartyMember();
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
