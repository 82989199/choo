package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ArenaUserInfo.eRole;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CharacterClass;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.Gender;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaTeam;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENA_PLAY_EVENT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static SC_ARENA_PLAY_EVENT_NOTI newInstance(){
		return new SC_ARENA_PLAY_EVENT_NOTI();
	}
	private eType _type;
	private ArenaActorInfo _actor_info;
	private long _arena_char_id_target;
	private long _arena_char_id_causer;
	private eArenaTeam _team_id;
	private int _spell_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private SC_ARENA_PLAY_EVENT_NOTI(){
	}
	public eType get_type(){
		return _type;
	}
	public void set_type(eType val){
		_bit |= 0x1L;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1L) == 0x1L;
	}
	public ArenaActorInfo get_actor_info(){
		return _actor_info;
	}
	public void set_actor_info(ArenaActorInfo val){
		_bit |= 0x200L;
		_actor_info = val;
	}
	public boolean has_actor_info(){
		return (_bit & 0x200L) == 0x200L;
	}
	public long get_arena_char_id_target(){
		return _arena_char_id_target;
	}
	public void set_arena_char_id_target(long val){
		_bit |= 0x80000L;
		_arena_char_id_target = val;
	}
	public boolean has_arena_char_id_target(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public long get_arena_char_id_causer(){
		return _arena_char_id_causer;
	}
	public void set_arena_char_id_causer(long val){
		_bit |= 0x100000L;
		_arena_char_id_causer = val;
	}
	public boolean has_arena_char_id_causer(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public eArenaTeam get_team_id(){
		return _team_id;
	}
	public void set_team_id(eArenaTeam val){
		_bit |= 0x20000000L;
		_team_id = val;
	}
	public boolean has_team_id(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public int get_spell_id(){
		return _spell_id;
	}
	public void set_spell_id(int val){
		_bit |= 0x8000000000L;
		_spell_id = val;
	}
	public boolean has_spell_id(){
		return (_bit & 0x8000000000L) == 0x8000000000L;
	}
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		if (has_actor_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(10, _actor_info);
		if (has_arena_char_id_target())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(20, _arena_char_id_target);
		if (has_arena_char_id_causer())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(21, _arena_char_id_causer);
		if (has_team_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(30, _team_id.toInt());
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(40, _spell_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
		}
		if (has_actor_info()){
			output.writeMessage(10, _actor_info);
		}
		if (has_arena_char_id_target()){
			output.writeInt64(20, _arena_char_id_target);
		}
		if (has_arena_char_id_causer()){
			output.writeInt64(21, _arena_char_id_causer);
		}
		if (has_team_id()){
			output.writeEnum(30, _team_id.toInt());
		}
		if (has_spell_id()){
			output.writeUInt32(40, _spell_id);
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
					set_type(eType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000052:{
					set_actor_info((ArenaActorInfo)input.readMessage(ArenaActorInfo.newInstance()));
					break;
				}
				case 0x000000A0:{
					set_arena_char_id_target(input.readInt64());
					break;
				}
				case 0x000000A8:{
					set_arena_char_id_causer(input.readInt64());
					break;
				}
				case 0x000000F0:{
					set_team_id(eArenaTeam.fromInt(input.readEnum()));
					break;
				}
				case 0x00000140:{
					set_spell_id(input.readUInt32());
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
		return new SC_ARENA_PLAY_EVENT_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_actor_info() && _actor_info != null){
			_actor_info.dispose();
			_actor_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public enum eType{
		GameForfeited(1),
		GameCountDown3Sec(2),
		GameCountDown10Sec(3),
		PlayerEntered(11),
		PlayerOut(12),
		PlayerDead(13),
		PlayerSpellSucceded(14),
		YourSelfEntered(21),
		BattleHunterWin(31),
		TeamTowerBroke(41);
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 1:
				return GameForfeited;
			case 2:
				return GameCountDown3Sec;
			case 3:
				return GameCountDown10Sec;
			case 11:
				return PlayerEntered;
			case 12:
				return PlayerOut;
			case 13:
				return PlayerDead;
			case 14:
				return PlayerSpellSucceded;
			case 21:
				return YourSelfEntered;
			case 31:
				return BattleHunterWin;
			case 41:
				return TeamTowerBroke;
			default:
				return null;
			}
		}
	}
}
