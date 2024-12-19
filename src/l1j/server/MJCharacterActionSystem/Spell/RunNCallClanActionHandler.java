package l1j.server.MJCharacterActionSystem.Spell;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;

public class RunNCallClanActionHandler extends SpellActionHandler{
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		message = pck.readS();
	}
	
	@Override
	public boolean validation() {
		if(!super.validation())
			return false;
		
		L1PcInstance pc = L1World.getInstance().getPlayer(message);
		if(pc == null){
			owner.sendPackets(new S_ServerMessage(73, message));
			return false;
		}
		
		if(pc.getClanid() != owner.getClanid()){
			owner.sendPackets(new S_ServerMessage(414));
			return false;
		}
		
		tId = pc.getId();
		
		return true;
	}
	
	@Override
	public SpellActionHandler copy() {
		return new RunNCallClanActionHandler();
	}
}
