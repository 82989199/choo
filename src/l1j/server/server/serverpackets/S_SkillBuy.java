package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillBuy extends ServerBasePacket {
	
	public S_SkillBuy(int o, L1PcInstance pc) {
		int count = Scount(pc);
		int inCount = 0;
		for (int k = 0; k < count; k++) {
			if (!pc.isSkillMastery((k + 1))) {
				inCount++;
			}
		}

		try {
			writeC(Opcodes.S_BUYABLE_SPELL_LIST);
			writeD(100);
			writeH(inCount);
			for (int k = 0; k < count; k++) {
				if (!pc.isSkillMastery((k + 1))) {
					writeD(k);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int Scount(L1PcInstance player) {
		int RC = 0;
		// int TC = 0;
		switch (player.getType()) {
		case 0: // 군주
			/*
			 * if (player.get_level() >= 10 && player.get_level() <= 19) { RC =
			 * 8; } else if (player.get_level() >= 20) { RC = 16; }
			 */

			RC = 16;
			break;

		case 1: // 기사
			/*
			 * if (player.get_level() >= 50) { RC = 8; }
			 */

			RC = 8;
			break;

		case 2: // 엘프
			/*
			 * if (player.get_level() >= 8 && player.get_level() <= 15) { RC =
			 * 8; } else if (player.get_level() >= 16 && player.get_level() <=
			 * 23) { RC = 16; } else if (player.get_level() >= 24) { RC = 23; }
			 */

			RC = 23;
			break;

		case 3: // WIZ
			/*
			 * if (player.get_level() >= 4 && player.get_level() <= 7) { RC = 8; }
			 * else if (player.get_level() >= 8 && player.get_level() <= 11) {
			 * RC = 16; } else if (player.get_level() >= 12) { RC = 23; }
			 */

			RC = 23;
			break;

		case 4: // DE
			/*
			 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC =
			 * 8; } else if (player.get_level() >= 24) { RC = 16; }
			 */

			RC = 16;
			break;
		case 7: // 전사
			/*
			 * if (player.get_level() >= 50) { RC = 8; }
			 */

			RC = 8;
			break;
		default:
			break;
		}
		return RC;
		/*
		 * for(int i = 0 ; i < RC ; ++i) { if(chk(player, i) == false) { TC++; } }
		 * return TC;
		 */
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1B_WAR;
	}

	private static final String _S__1B_WAR = "[S] S_SkillBuy";
}
