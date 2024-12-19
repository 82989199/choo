package l1j.server.MJCharacterActionSystem.Spell;


import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class CubeActionHandler extends SpellActionHandler{
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
	}
	
	@Override
	public boolean validation(){
		return super.validation();
	}
	
	@Override
	public SpellActionHandler copy() {
		return new CubeActionHandler();
	}	
}
