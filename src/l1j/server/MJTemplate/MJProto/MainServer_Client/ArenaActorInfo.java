package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ArenaUserInfo.eRole;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CharacterClass;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.Gender;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaTeam;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ArenaActorInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ArenaActorInfo newInstance(){
		return new ArenaActorInfo();
	}
	private long _arena_char_id;
	private eRole _role;
	private int _server_id;
	private byte[] _character_name;
	private CharacterClass _character_class;
	private Gender _gender;
	private eArenaTeam _team_id;
	private int _marker_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ArenaActorInfo(){
	}
	public long get_arena_char_id(){
		return _arena_char_id;
	}
	public void set_arena_char_id(long val){
		_bit |= 0x1;
		_arena_char_id = val;
	}
	public boolean has_arena_char_id(){
		return (_bit & 0x1) == 0x1;
	}
	public eRole get_role(){
		return _role;
	}
	public void set_role(eRole val){
		_bit |= 0x2;
		_role = val;
	}
	public boolean has_role(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_server_id(){
		return _server_id;
	}
	public void set_server_id(int val){
		_bit |= 0x4;
		_server_id = val;
	}
	public boolean has_server_id(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_character_name(){
		return _character_name;
	}
	public void set_character_name(byte[] val){
		_bit |= 0x8;
		_character_name = val;
	}
	public boolean has_character_name(){
		return (_bit & 0x8) == 0x8;
	}
	public CharacterClass get_character_class(){
		return _character_class;
	}
	public void set_character_class(CharacterClass val){
		_bit |= 0x10;
		_character_class = val;
	}
	public boolean has_character_class(){
		return (_bit & 0x10) == 0x10;
	}
	public Gender get_gender(){
		return _gender;
	}
	public void set_gender(Gender val){
		_bit |= 0x20;
		_gender = val;
	}
	public boolean has_gender(){
		return (_bit & 0x20) == 0x20;
	}
	public eArenaTeam get_team_id(){
		return _team_id;
	}
	public void set_team_id(eArenaTeam val){
		_bit |= 0x40;
		_team_id = val;
	}
	public boolean has_team_id(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_marker_id(){
		return _marker_id;
	}
	public void set_marker_id(int val){
		_bit |= 0x80;
		_marker_id = val;
	}
	public boolean has_marker_id(){
		return (_bit & 0x80) == 0x80;
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
		if (has_arena_char_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _arena_char_id);
		if (has_role())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _role.toInt());
		if (has_server_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _server_id);
		if (has_character_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _character_name);
		if (has_character_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _character_class.toInt());
		if (has_gender())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _gender.toInt());
		if (has_team_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _team_id.toInt());
		if (has_marker_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _marker_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_arena_char_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_role()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_arena_char_id()){
			output.writeInt64(1, _arena_char_id);
		}
		if (has_role()){
			output.writeEnum(2, _role.toInt());
		}
		if (has_server_id()){
			output.writeUInt32(3, _server_id);
		}
		if (has_character_name()){
			output.writeBytes(4, _character_name);
		}
		if (has_character_class()){
			output.writeEnum(5, _character_class.toInt());
		}
		if (has_gender()){
			output.writeEnum(6, _gender.toInt());
		}
		if (has_team_id()){
			output.writeEnum(7, _team_id.toInt());
		}
		if (has_marker_id()){
			output.wirteInt32(8, _marker_id);
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
					set_arena_char_id(input.readInt64());
					break;
				}
				case 0x00000010:{
					set_role(eRole.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018:{
					set_server_id(input.readUInt32());
					break;
				}
				case 0x00000022:{
					set_character_name(input.readBytes());
					break;
				}
				case 0x00000028:{
					set_character_class(CharacterClass.fromInt(input.readEnum()));
					break;
				}
				case 0x00000030:{
					set_gender(Gender.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038:{
					set_team_id(eArenaTeam.fromInt(input.readEnum()));
					break;
				}
				case 0x00000040:{
					set_marker_id(input.readInt32());
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
		return new ArenaActorInfo();
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
