package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.utils.IntRange;

public class S_HPMeter extends ServerBasePacket {
	private static final String _typeString = "[S] S_HPMeter";

	public S_HPMeter(int objId, int hpRatio, int mpRatio) {
		buildPacket(objId, hpRatio, mpRatio);
	}

	public S_HPMeter(L1Character cha) {
		int objId = cha.getId();
		int hpRatio = 0;
		int mpRatio = 0;
		if (0 < cha.getMaxHp()){
			hpRatio = (int)((100D / (double)cha.getMaxHp()) * (double)cha.getCurrentHp());
		}

		if (0 < cha.getMaxMp()){
			mpRatio = (int)((100D / (double)cha.getMaxMp()) * (double)cha.getCurrentMp());
		}

		buildPacket(objId, hpRatio, mpRatio);
	}

	private void buildPacket(int objId, int hpRatio, int mpRatio) {
		// 43 04 5d 91 05 00 00 93 1b
		writeC(Opcodes.S_HIT_RATIO);
		writeD(objId);
		writeC(hpRatio);
		writeC(mpRatio);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _typeString;
	}
}
