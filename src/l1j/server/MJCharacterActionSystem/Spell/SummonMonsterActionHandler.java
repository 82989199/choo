package l1j.server.MJCharacterActionSystem.Spell;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;

public class SummonMonsterActionHandler extends SpellActionHandler{
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tX = pck.readC();
	}
	
	@Override
	public SpellActionHandler copy() {
		return new SummonMonsterActionHandler();
	}
}
