package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

public class S_AttackPacket extends ServerBasePacket {
	private static final String _S__1F_ATTACKPACKET = "[S] S_AttackPacket";

	public S_AttackPacket(L1Character cha, int objid, int type) {
		buildpacket(cha, objid, type);
	}
	
	public S_AttackPacket(L1Character cha, int objid, int type, int attacktype) {
		buildpacket(cha, objid, type, attacktype);
	}

	private void buildpacket(L1Character cha, int objid, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(objid);
		writeD(cha.getId());
		// writeD(_objid);
		writeC(0x4E);
		writeC(0);
		writeC(cha.getHeading());
		writeD(0x00000000);
		writeC(0x00);
	}
	
	private void buildpacket(L1Character cha, int objid, int type, int attacktype) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(cha.getId());
		writeD(objid);
		writeH(0x02); // damage
		writeC(cha.getHeading());
		writeH(0x0000); // target x
		writeH(0x0000); // target y
		writeC(attacktype); // 0:none 2:크로우 4:이도류 0x08:CounterMirror
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1F_ATTACKPACKET;
	}
}
