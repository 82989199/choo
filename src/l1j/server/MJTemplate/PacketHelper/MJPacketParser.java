package l1j.server.MJTemplate.PacketHelper;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;

public interface MJPacketParser {
	public void parse(L1PcInstance owner, ClientBasePacket pck);
	public void doWork();
}
