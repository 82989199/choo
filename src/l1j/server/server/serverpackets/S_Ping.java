package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Ping extends ServerBasePacket {
	private static final int SC_PING_REQ = 0x03E8;
	
	/** for gm command. **/
	private static S_Ping 	_pck;
	public static long		_lastMs;
	public static boolean	_isRun;
	
	private static final S_Ping[] _pings = new S_Ping[]{
		new S_Ping(1),
		new S_Ping(2),
		new S_Ping(3),
		new S_Ping(4),
	};
	
	public static S_Ping get(int transaction){
		return _pings[transaction - 1];
	}
	
	public static S_Ping getForGM(){
		if(_pck == null){
			_pck = new S_Ping();
			_pck.writeC(0x08);
			_pck.writeC(0x01);
			_pck.writeH(0x00);
		}
		_lastMs = System.currentTimeMillis();
		return _pck;
	}
	
	public static void reqForGM(L1PcInstance pc){
		if(!_isRun)
			return;
		
		long del = System.currentTimeMillis() - _lastMs;
		String s = String.format("[PING CHECK] %dms.", del);
		System.out.println(s);
		pc.sendPackets(new S_SystemMessage(s));
		
		GeneralThreadPool.getInstance().schedule(new request(pc), 1000 - del);
	}
	
	static class request implements Runnable{
		private L1PcInstance _pc;
		request(L1PcInstance pc){
			_pc = pc;
		}
		@Override
		public void run() {
			_pc.sendPackets(getForGM(), false);
		}
	}
	
	private S_Ping(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_PING_REQ);
	}
	
	private S_Ping(int i){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_PING_REQ);
		writeC(0x08);
		writeC(i);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
