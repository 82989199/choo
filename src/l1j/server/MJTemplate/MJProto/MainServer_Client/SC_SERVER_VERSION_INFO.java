package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SERVER_VERSION_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static SC_SERVER_VERSION_INFO 	_version;
	private static ProtoOutputStream 		_denial;
	
	public static void send(GameClient clnt){
		_version.set_game_real_time((int)(System.currentTimeMillis() / 1000L));
		clnt.sendPacket(_version.writeTo(MJEProtoMessages.SC_SERVER_VERSION_INFO), true);
	}
	
	public static void denial(GameClient clnt){
		if(_denial == null){
			SC_SERVER_VERSION_INFO denial = new SC_SERVER_VERSION_INFO();
			denial.set_version_check(1);
			_denial = denial.writeTo(MJEProtoMessages.SC_SERVER_VERSION_INFO);
			denial.dispose();
		}
		
		clnt.sendPacket(_denial, false);
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				try{
					clnt.close();
				}catch(Exception e){}
			}
		}, 3000L);
	}
	
	public static SC_SERVER_VERSION_INFO newInstance(){
		if(_version == null){
			_version = new SC_SERVER_VERSION_INFO();
			_version.set_version_check(0);
			_version.set_server_id(0);
			_version.set_build_number(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_cache_version(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_auth_version(MJNetSafeLoadManager.NS_AUTHSERVER_VERSION);
			_version.set_npc_server_version(MJNetSafeLoadManager.NS_CLIENT_VERSION);
			_version.set_status_start_time((int)(System.currentTimeMillis() / 1000L));
			_version.set_english_only_config(0x00);
			_version.set_country_code(0x00);
			_version.set_client_setting_switch(MJNetSafeLoadManager.NS_CLIENT_SETTING_SWITCH);
			_version.set_global_cache_version(MJNetSafeLoadManager.NS_CACHESERVER_VERSION);
			_version.set_tam_server_version(MJNetSafeLoadManager.NS_TAMSERVER_VERSION);
			_version.set_arca_server_version(MJNetSafeLoadManager.NS_ARCASERVER_VERSION);
			_version.set_hibreed_inter_server_version(MJNetSafeLoadManager.NS_HIBREED_INTERSERVER_VERSION);
			_version.set_arenaco_server_version(MJNetSafeLoadManager.NS_ARENACOSERVER_VERSION);
			_version.set_server_type(0x00);
		}
		return _version;
	}
	private int _version_check;
	private int _server_id;
	private int _build_number;
	private int _cache_version;
	private int _auth_version;
	private int _npc_server_version;
	private int _status_start_time;
	private int _english_only_config;
	private int _country_code;
	private int _client_setting_switch;
	private int _game_real_time;
	private int _global_cache_version;
	private int _tam_server_version;
	private int _arca_server_version;
	private int _hibreed_inter_server_version;
	private int _arenaco_server_version;
	private int _server_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SERVER_VERSION_INFO(){
	}
	public int get_version_check(){
		return _version_check;
	}
	public void set_version_check(int val){
		_bit |= 0x00000001;
		_version_check = val;
	}
	public boolean has_version_check(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_server_id(){
		return _server_id;
	}
	public void set_server_id(int val){
		_bit |= 0x00000002;
		_server_id = val;
	}
	public boolean has_server_id(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_build_number(){
		return _build_number;
	}
	public void set_build_number(int val){
		_bit |= 0x00000004;
		_build_number = val;
	}
	public boolean has_build_number(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public int get_cache_version(){
		return _cache_version;
	}
	public void set_cache_version(int val){
		_bit |= 0x00000008;
		_cache_version = val;
	}
	public boolean has_cache_version(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_auth_version(){
		return _auth_version;
	}
	public void set_auth_version(int val){
		_bit |= 0x00000010;
		_auth_version = val;
	}
	public boolean has_auth_version(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public int get_npc_server_version(){
		return _npc_server_version;
	}
	public void set_npc_server_version(int val){
		_bit |= 0x00000020;
		_npc_server_version = val;
	}
	public boolean has_npc_server_version(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	public int get_status_start_time(){
		return _status_start_time;
	}
	public void set_status_start_time(int val){
		_bit |= 0x00000040;
		_status_start_time = val;
	}
	public boolean has_status_start_time(){
		return (_bit & 0x00000040) == 0x00000040;
	}
	public int get_english_only_config(){
		return _english_only_config;
	}
	public void set_english_only_config(int val){
		_bit |= 0x00000080;
		_english_only_config = val;
	}
	public boolean has_english_only_config(){
		return (_bit & 0x00000080) == 0x00000080;
	}
	public int get_country_code(){
		return _country_code;
	}
	public void set_country_code(int val){
		_bit |= 0x00000100;
		_country_code = val;
	}
	public boolean has_country_code(){
		return (_bit & 0x00000100) == 0x00000100;
	}
	public int get_client_setting_switch(){
		return _client_setting_switch;
	}
	public void set_client_setting_switch(int val){
		_bit |= 0x00000200;
		_client_setting_switch = val;
	}
	public boolean has_client_setting_switch(){
		return (_bit & 0x00000200) == 0x00000200;
	}
	public int get_game_real_time(){
		return _game_real_time;
	}
	public void set_game_real_time(int val){
		_bit |= 0x00000400;
		_game_real_time = val;
	}
	public boolean has_game_real_time(){
		return (_bit & 0x00000400) == 0x00000400;
	}
	public int get_global_cache_version(){
		return _global_cache_version;
	}
	public void set_global_cache_version(int val){
		_bit |= 0x00000800;
		_global_cache_version = val;
	}
	public boolean has_global_cache_version(){
		return (_bit & 0x00000800) == 0x00000800;
	}
	public int get_tam_server_version(){
		return _tam_server_version;
	}
	public void set_tam_server_version(int val){
		_bit |= 0x00001000;
		_tam_server_version = val;
	}
	public boolean has_tam_server_version(){
		return (_bit & 0x00001000) == 0x00001000;
	}
	public int get_arca_server_version(){
		return _arca_server_version;
	}
	public void set_arca_server_version(int val){
		_bit |= 0x00002000;
		_arca_server_version = val;
	}
	public boolean has_arca_server_version(){
		return (_bit & 0x00002000) == 0x00002000;
	}
	public int get_hibreed_inter_server_version(){
		return _hibreed_inter_server_version;
	}
	public void set_hibreed_inter_server_version(int val){
		_bit |= 0x00004000;
		_hibreed_inter_server_version = val;
	}
	public boolean has_hibreed_inter_server_version(){
		return (_bit & 0x00004000) == 0x00004000;
	}
	public int get_arenaco_server_version(){
		return _arenaco_server_version;
	}
	public void set_arenaco_server_version(int val){
		_bit |= 0x00008000;
		_arenaco_server_version = val;
	}
	public boolean has_arenaco_server_version(){
		return (_bit & 0x00008000) == 0x00008000;
	}
	public int get_server_type(){
		return _server_type;
	}
	public void set_server_type(int val){
		_bit |= 0x00010000;
		_server_type = val;
	}
	public boolean has_server_type(){
		return (_bit & 0x00010000) == 0x00010000;
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
		if (has_version_check())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _version_check);
		if (has_server_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _server_id);
		if (has_build_number())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _build_number);
		if (has_cache_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _cache_version);
		if (has_auth_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _auth_version);
		if (has_npc_server_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _npc_server_version);
		if (has_status_start_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _status_start_time);
		if (has_english_only_config())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _english_only_config);
		if (has_country_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _country_code);
		if (has_client_setting_switch())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _client_setting_switch);
		if (has_game_real_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _game_real_time);
		if (has_global_cache_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _global_cache_version);
		if (has_tam_server_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _tam_server_version);
		if (has_arca_server_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(14, _arca_server_version);
		if (has_hibreed_inter_server_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(15, _hibreed_inter_server_version);
		if (has_arenaco_server_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(16, _arenaco_server_version);
		if (has_server_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(17, _server_type);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_version_check()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_version_check()){
			output.writeUInt32(1, _version_check);
		}
		if (has_server_id()){
			output.writeUInt32(2, _server_id);
		}
		if (has_build_number()){
			output.wirteInt32(3, _build_number);
		}
		if (has_cache_version()){
			output.wirteInt32(4, _cache_version);
		}
		if (has_auth_version()){
			output.wirteInt32(5, _auth_version);
		}
		if (has_npc_server_version()){
			output.wirteInt32(6, _npc_server_version);
		}
		if (has_status_start_time()){
			output.wirteInt32(7, _status_start_time);
		}
		if (has_english_only_config()){
			output.wirteInt32(8, _english_only_config);
		}
		if (has_country_code()){
			output.wirteInt32(9, _country_code);
		}
		if (has_client_setting_switch()){
			output.wirteInt32(10, _client_setting_switch);
		}
		if (has_game_real_time()){
			output.wirteInt32(11, _game_real_time);
		}
		if (has_global_cache_version()){
			output.wirteInt32(12, _global_cache_version);
		}
		if (has_tam_server_version()){
			output.wirteInt32(13, _tam_server_version);
		}
		if (has_arca_server_version()){
			output.wirteInt32(14, _arca_server_version);
		}
		if (has_hibreed_inter_server_version()){
			output.wirteInt32(15, _hibreed_inter_server_version);
		}
		if (has_arenaco_server_version()){
			output.wirteInt32(16, _arenaco_server_version);
		}
		if (has_server_type()){
			output.wirteInt32(17, _server_type);
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
					set_version_check(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_server_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_build_number(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_cache_version(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_auth_version(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_npc_server_version(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_status_start_time(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_english_only_config(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_country_code(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_client_setting_switch(input.readInt32());
					break;
				}
				case 0x00000058:{
					set_game_real_time(input.readInt32());
					break;
				}
				case 0x00000060:{
					set_global_cache_version(input.readInt32());
					break;
				}
				case 0x00000068:{
					set_tam_server_version(input.readInt32());
					break;
				}
				case 0x00000070:{
					set_arca_server_version(input.readInt32());
					break;
				}
				case 0x00000078:{
					set_hibreed_inter_server_version(input.readInt32());
					break;
				}
				case 0x00000080:{
					set_arenaco_server_version(input.readInt32());
					break;
				}
				case 0x00000088:{
					set_server_type(input.readInt32());
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
		return new SC_SERVER_VERSION_INFO();
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
