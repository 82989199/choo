package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ServerVersion extends ServerBasePacket {
	private static final String S_SERVER_VERSION = "[S] ServerVersion";
	private static long _startMs = 0;
	private static final long VER_BUILD		= 1701101001L;
	private static final long VER_AUTH		= 2015090301L;
	private static final long VER_GC		= 151112700L;
	private static final long VER_TAM		= 161031701L;
	private static final long VER_ARCA		= 161111700L;
	private static final long VER_HIBREED	= 1701031002L;
	private static final long VER_ARENACO	= 160531700L;
	private static final long SWH_CLNT		= 889191819L;
	public S_ServerVersion() {
		super(128);
		if(_startMs <= 0)
			_startMs = (System.currentTimeMillis() / 1000);
		
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0335);
		writeC(0x08);				// version_check
		writeC(0x00);			
		writeC(0x10);				// server_id
		writeC(0x00);				
		writeC(0x18);				// build number
		writeBit(VER_BUILD);			
		writeC(0x20);				// cache_version
		writeBit(VER_BUILD);		
		writeC(0x28);				// auth_version
		writeBit(VER_AUTH);			
		writeC(0x30);				// npc_server_version
		writeBit(VER_BUILD);		
		writeC(0x38);				// status_start_time
		writeBit(_startMs);			
		writeC(0x40);				// english_only_config
		writeC(0x00);				
		writeC(0x48);				// country_code
		writeC(0x00);				
		writeC(0x50);				// client_setting_switch
		writeBit(SWH_CLNT);			
		writeC(0x58);				// game_real_time
		writeBit(System.currentTimeMillis() / 1000);	
		writeC(0x60);				// global_cache_version
		writeBit(VER_GC);			
		writeC(0x68);				// tam_server_version
		writeBit(VER_TAM);			
		writeC(0x70);				// arca_server_version
		writeBit(VER_ARCA);			
		writeC(0x78);				// hibreed_inter_server_version
		writeBit(VER_HIBREED);		
		writeBit(0x80);				// arenaco_server_version
		writeBit(VER_ARENACO);		
		writeBit(0x88);				// server_type
		writeC(0x00);				
		writeH(0x00);				
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SERVER_VERSION;
	}
}
