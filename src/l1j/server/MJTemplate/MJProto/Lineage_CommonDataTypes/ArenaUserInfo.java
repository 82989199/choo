package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ArenaUserInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ArenaUserInfo newInstance(){
		return new ArenaUserInfo();
	}
	private long _arena_char_id;
	private int _server_id;
	private byte[] _character_name;
	private CharacterClass _character_class;
	private Gender _gender;
	private eArenaTeam _team_id;
	private eRole _role;
	private boolean _room_owner;
	private boolean _ready;
	private boolean _in_room;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ArenaUserInfo(){
		set_team_id(eArenaTeam.TEAM_A);
		set_role(eRole.Player);
		set_room_owner(false);
		set_ready(false);
		set_in_room(false);
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
	public int get_server_id(){
		return _server_id;
	}
	public void set_server_id(int val){
		_bit |= 0x2;
		_server_id = val;
	}
	public boolean has_server_id(){
		return (_bit & 0x2) == 0x2;
	}
	public byte[] get_character_name(){
		return _character_name;
	}
	public void set_character_name(byte[] val){
		_bit |= 0x4;
		_character_name = val;
	}
	public boolean has_character_name(){
		return (_bit & 0x4) == 0x4;
	}
	public CharacterClass get_character_class(){
		return _character_class;
	}
	public void set_character_class(CharacterClass val){
		_bit |= 0x8;
		_character_class = val;
	}
	public boolean has_character_class(){
		return (_bit & 0x8) == 0x8;
	}
	public Gender get_gender(){
		return _gender;
	}
	public void set_gender(Gender val){
		_bit |= 0x10;
		_gender = val;
	}
	public boolean has_gender(){
		return (_bit & 0x10) == 0x10;
	}
	public eArenaTeam get_team_id(){
		return _team_id;
	}
	public void set_team_id(eArenaTeam val){
		_bit |= 0x20;
		_team_id = val;
	}
	public boolean has_team_id(){
		return (_bit & 0x20) == 0x20;
	}
	public eRole get_role(){
		return _role;
	}
	public void set_role(eRole val){
		_bit |= 0x40;
		_role = val;
	}
	public boolean has_role(){
		return (_bit & 0x40) == 0x40;
	}
	public boolean get_room_owner(){
		return _room_owner;
	}
	public void set_room_owner(boolean val){
		_bit |= 0x80;
		_room_owner = val;
	}
	public boolean has_room_owner(){
		return (_bit & 0x80) == 0x80;
	}
	public boolean get_ready(){
		return _ready;
	}
	public void set_ready(boolean val){
		_bit |= 0x100;
		_ready = val;
	}
	public boolean has_ready(){
		return (_bit & 0x100) == 0x100;
	}
	public boolean get_in_room(){
		return _in_room;
	}
	public void set_in_room(boolean val){
		_bit |= 0x200;
		_in_room = val;
	}
	public boolean has_in_room(){
		return (_bit & 0x200) == 0x200;
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
		if (has_server_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _server_id);
		if (has_character_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _character_name);
		if (has_character_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _character_class.toInt());
		if (has_gender())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _gender.toInt());
		if (has_team_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _team_id.toInt());
		if (has_role())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _role.toInt());
		if (has_room_owner())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(8, _room_owner);
		if (has_ready())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(9, _ready);
		if (has_in_room())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(10, _in_room);
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
		if (!has_server_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_character_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_character_class()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_gender()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_team_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_role()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_room_owner()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_ready()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_in_room()){
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
		if (has_server_id()){
			output.writeUInt32(2, _server_id);
		}
		if (has_character_name()){
			output.writeBytes(3, _character_name);
		}
		if (has_character_class()){
			output.writeEnum(4, _character_class.toInt());
		}
		if (has_gender()){
			output.writeEnum(5, _gender.toInt());
		}
		if (has_team_id()){
			output.writeEnum(6, _team_id.toInt());
		}
		if (has_role()){
			output.writeEnum(7, _role.toInt());
		}
		if (has_room_owner()){
			output.writeBool(8, _room_owner);
		}
		if (has_ready()){
			output.writeBool(9, _ready);
		}
		if (has_in_room()){
			output.writeBool(10, _in_room);
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
					set_server_id(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					set_character_name(input.readBytes());
					break;
				}
				case 0x00000020:{
					set_character_class(CharacterClass.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028:{
					set_gender(Gender.fromInt(input.readEnum()));
					break;
				}
				case 0x00000030:{
					set_team_id(eArenaTeam.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038:{
					set_role(eRole.fromInt(input.readEnum()));
					break;
				}
				case 0x00000040:{
					set_room_owner(input.readBool());
					break;
				}
				case 0x00000048:{
					set_ready(input.readBool());
					break;
				}
				case 0x00000050:{
					set_in_room(input.readBool());
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
		return new ArenaUserInfo();
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
	public enum eRole{
		Player(1),
		Observer(2),
		BuilderCaster(3);
		private int value;
		eRole(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eRole v){
			return value == v.value;
		}
		public static eRole fromInt(int i){
			switch(i){
			case 1:
				return Player;
			case 2:
				return Observer;
			case 3:
				return BuilderCaster;
			default:
				return null;
			}
		}
	}
}
