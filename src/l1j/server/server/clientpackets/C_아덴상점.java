package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.shop.L1AdenShop;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RetrieveSupplementaryService;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_TestPacket;
import l1j.server.server.serverpackets.S_아덴상점;

public class C_아덴상점 extends ClientBasePacket {

	private static final String C_아덴상점 = "[C] C_아덴상점";

	public C_아덴상점(byte[] decrypt, GameClient client) {
		super(decrypt);
		try {
			int type = readH();
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)
				return;
			switch (type) {
			case 1: // 상점 열기
			/*	if (Config.STANDBY_SERVER) {
					pc.sendPackets(new S_ChatPacket(pc, "오픈대기 상태이므로 해당 기능은 불가능 합니다.", 1));
					pc.sendPackets(new S_PacketBox(84, "오픈대기 상태이므로 해당 기능은 불가능 합니다."));
					return;
				}*/
				if (Config.Arden) {
//					pc.sendPackets(new S_TestPacket(S_TestPacket.a, 16, 3590, "00 ff ff"));
					pc.sendPackets(new S_PacketBox(84, "현재 아덴상점이 점검 중입니다. 잠시 후 다시 이용해주세요"));
					pc.sendPackets(new S_ChatPacket(pc, "현재 아덴상점이 점검 중입니다. 잠시 후 다시 이용해주세요", 1));
					return;
				}
				if (Config.아덴상점타입 == Config.아덴상점엔샵아덴샵설정) {
					client.sendPacket(new S_SurvivalCry(S_SurvivalCry.LIST, pc));
					client.sendPacket(new S_SurvivalCry(S_SurvivalCry.EMAIL, pc));
					client.sendPacket(new S_SurvivalCry(S_SurvivalCry.POINT, pc));
					pc.sendPackets(new S_PacketBox(84, "[반복] N코인은 '리니지 n샵'에게서 구매가능 합니다."));
					pc.sendPackets("\\aG[반복] N코인은 '리니지 n샵'에게서 구매가능 합니다.");
				} else {
					pc.sendPackets(new S_아덴상점(pc));
				}
				break;
			case 4: { // OTP 입력
				for (int i = 0; i < 1000; i++) {
					int ff = readH();
					if (ff == 0)
						break;
				}
				for (int i = 0; i < 16 * 8 + 1; i++) {
					readC();
				}
				int size = readH();
				if (size == 0)
					return;

				L1AdenShop as = new L1AdenShop();
				for (int i = 0; i < size; i++) {
					int id = readD();
					int count = readH();
					if (count <= 0 || count >= 10000) {
						return;
					}
					as.add(pc, id, count);
				}

				if (!as.BugOk()) {
					if (as.commit(pc))
						client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_CHECK_MSG, pc));
				}
			}
				break;
			case 6: { // 부가서비스 창고
				SupplementaryService warehouse = WarehouseManager.getInstance()
						.getSupplementaryService(pc.getAccountName());
				int size = warehouse.getSize();
				if (size > 0)
					SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_supplementary_service(pc, pc.getId());
					//pc.sendPackets(new S_RetrieveSupplementaryService(pc.getId(), pc));
				else
					pc.sendPackets(new S_ServerMessage(1625));
			}
				break;
			case 0x32: // 동의 및 구매
				client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_SHOW, pc));
				break;
			}
		} catch (Exception e) {
		} finally {
		}
	}

	@Override
	public String getType() {
		return C_아덴상점;
	}
	
	public void test(){
		final byte[] b = {
				94, 4, 0, 34, 0, 107, 0, 111, 0, 107, 0, 111, 0, 49, 0, 50, 0, 51, 0, 64, 0, 110, 0, 97, 0, 116, 0, 101, 0, 46, 0, 99, 0, 111, 0, 109, 0, 0, 0, 8, -112, -71, 73, 34, -79, 112, 35, 2, 41, 61, 95, -39, 2, 100, -110, 108, 118, 7, 3, 125, -27, -25, -44, -20, 20, 123, -78, 103, 87, 65, -44, -44, 112, -40, 22, -70, -86, -125, -71, -120, -51, 11, -126, -42, 49, -13, 102, 103, 102, 93, -24, 99, 72, 118, -49, 55, -48, -13, -20, 121, -64, 70, -83, -50, -70, -36, -112, 127, -56, 126, 97, 79, 91, -6, 65, -76, -66, 70, -101, -20, -51, 20, 116, 98, -92, 98, 65, -122, 119, 44, -69, -48, 26, 85, 83, 55, 37, 76, 70, -120, -57, -113, 100, -75, -100, -65, -116, 47, 59, -20, -83, -77, 74, -8, 37, 20, -83, 72, -76, 81, 57, 107, -66, -96, -81, -3, -63, -65, 1, 0, -4, -95, 7, 0, 1, 0, 0, 0, 0, 0
		};
	}
}
