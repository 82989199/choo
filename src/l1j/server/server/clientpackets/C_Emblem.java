package l1j.server.server.clientpackets;

import java.io.FileOutputStream;
import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ReturnedStat;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Emblem extends ClientBasePacket {

	private static final String C_EMBLEM = "[C] C_Emblem";

	public C_Emblem(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if (player == null) {
			return;
		} else if (player.getClanRank() != 4 && player.getClanRank() != 10) {
			return;
		}
		if (player.getClanid() != 0) {
			
			FileOutputStream fos = null;
			try {
				byte[] buff = new byte[384];
				boolean empt = true;
				for (short cnt = 0; cnt < 384; cnt++) {
					buff[cnt] = (byte)(readC() & 0xff);
					if(buff[cnt] != 0){
						empt = false;
					}
				}
				int newEmblemdId = IdFactory.getInstance().nextId();
				String emblem_file = String.valueOf(newEmblemdId);
				fos = new FileOutputStream("emblem/" + emblem_file);
				fos.write(buff,  0, 384);
				L1Clan clan = ClanTable.getInstance().getTemplate(player.getClanid());
				clan.setEmblemId(newEmblemdId);
				ClanTable.getInstance().updateClan(clan);
				for(L1PcInstance pc : clan.getOnlineClanMember()){
					pc.sendPackets(new S_ReturnedStat(pc.getId(), newEmblemdId));
					if(empt == true)
					pc.broadcastPacket(new S_ReturnedStat(pc.getId(), newEmblemdId));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(String.format("%s 엠블럼 등록 오류", player.getName()));
			} finally {
				if (null != fos) {
					fos.close();
				}
				fos = null;
			}
		}
	}

	@Override
	public String getType() {
		return C_EMBLEM;
	}
}
