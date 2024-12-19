package l1j.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.WalkActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_MoveChar extends ClientBasePacket {
	public C_MoveChar(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		
		L1PcInstance pc = client.getActiveChar();
		if(pc == null || pc.get_teleport() || pc.isDead())
			return;
		
		MJPacketParser parser = WalkActionHandlerFactory.create();
		parser.parse(pc, this);
		parser.doWork();
	}
}
