package l1j.server.MJCharacterActionSystem.Spell;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;

public class BackgroundActionHandler extends SpellActionHandler{
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tX = pck.readH();
		tY = pck.readH();
	}

	@Override
	public SpellActionHandler copy() {
		return new BackgroundActionHandler();
	}
}
