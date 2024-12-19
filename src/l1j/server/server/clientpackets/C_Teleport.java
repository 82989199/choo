package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class C_Teleport extends ClientBasePacket {

	//	private static final String C_TELEPORT = "[C] C_Teleport";

	public C_Teleport(byte[] data, GameClient gc) throws Exception {
		super(data);
		//	Teleportation.Teleportation(clientthread.getActiveChar());

		try {
			if (gc == null) {
				return;
			}

			L1PcInstance pc = gc.getActiveChar();
			if (pc == null || pc.isPrivateShop() || pc.is무인상점()) {
				return;
			}
			
			ServerBasePacket pck = pc.getTemporaryEffect();
			if(pck != null){
				pc.sendPackets(pck, false);
				Broadcaster.broadcastPacket(pc, pck);
				pc.clearTemporaryEffect();
			}
			
			Runnable teleport = () -> 
			{
				L1Teleport.getInstance().doTeleportation(pc);
			};
			GeneralThreadPool.getInstance().schedule(teleport, 200L);
			//			Teleportation.doTeleportation(pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return "[C] C_Teleport";
	}
}
