package l1j.server.server.clientpackets;

import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;

public class C_SecurityStatus extends ClientBasePacket {

	private static final String C_SECURITYSTATUS = "[C] C_SecurityStatus";

	public C_SecurityStatus(byte abyte0[], GameClient client) {
		super(abyte0);

		int objid = readD();

		L1PcInstance pc = client.getActiveChar();
		if ( pc == null)return;

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

		if (clan == null || clan.getCastleId() == 0) return;

		int castle_id = clan.getCastleId();
		String npcName = null;
		String status = null;
		switch (castle_id) {
		case 1: break;
		case 2: break;
		case 3: break;
		case 4: 
			npcName = "$1238"; break;
		case 5: break;
		default : break;
		}

		if (MJCastleWarBusiness.getInstance().getSecurity(castle_id) == 0) status = "$1118";
		else status = "$1117";

//		System.out.println("동작: " +npcName);
		String[] htmldata = new String[]{ npcName, status};

		pc.sendPackets(new S_NPCTalkReturn(objid, "CastleS", htmldata));
	}

	@Override
	public String getType() {
		return C_SECURITYSTATUS;
	}
}
