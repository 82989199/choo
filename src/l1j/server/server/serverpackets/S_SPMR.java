package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_SPMR extends ServerBasePacket {

	private static final String S_SPMR = "[S] S_S_SPMR";

	public S_SPMR(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_MAGIC_STATUS);
		if (pc.hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) {
			writeC(pc.getAbility().getSp() - pc.getAbility().getTrueSp() - 2);
		} else {
			writeC(pc.getAbility().getSp() - pc.getAbility().getTrueSp());
		}
		writeH(pc.getResistance().getMr() - pc.getResistance().getBaseMr());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SPMR;
	}
}
