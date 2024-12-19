package l1j.server.server.utils;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class DelaySender implements Runnable{
	public static void send(L1PcInstance pc, ServerBasePacket pck, long delay){
		if(delay <= 0L)
			pc.sendPackets(pck, true);
		else
			GeneralThreadPool.getInstance().schedule(new DelaySender(pc, pck),  delay);
	}

	private L1PcInstance 		_pc;
	private ServerBasePacket _pck;
	private DelaySender(L1PcInstance pc, ServerBasePacket pck){
		_pc 	= pc;
		_pck 	= pck;
	}
	
	@Override
	public void run() {
		try{
		if(_pc != null)
			_pc.sendPackets(_pck, true);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			_pc = null;
			_pck = null;			
		}
	}
}
