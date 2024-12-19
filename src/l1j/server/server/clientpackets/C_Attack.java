package l1j.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.AttackActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_Attack extends ClientBasePacket {

	public C_Attack(byte[] decrypt, GameClient client) {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		
		MJPacketParser parser = AttackActionHandlerFactory.create();
		if(parser == null)
			return;
		parser.parse(pc, this);
		parser.doWork();
	}
}
