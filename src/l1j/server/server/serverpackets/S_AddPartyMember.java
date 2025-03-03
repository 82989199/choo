package l1j.server.server.serverpackets;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_AddPartyMember extends ServerBasePacket {
	private static final String _TYPE = "[S] S_AddPartyMember";

	public S_AddPartyMember(L1PcInstance cha)
	{
		writeC(Opcodes.S_EVENT);
		writeC(0x69);
		double nowhp = 0.0d;
		double maxhp = 0.0d;
		nowhp = cha.getCurrentHp();
		maxhp = cha.getMaxHp();
		writeD(cha.getId());
		writeS(cha.getName());
		writeC(0x01);	// class type
		writeC(0x00);
		writeC(0x00);
		writeD(cha.getMapId());	// 맵아이디라고 유추한거같은데 다른용도인듯. 
		writeC((int)((nowhp / maxhp) * 100));
		writeC(0x00);
		writeC(0x00);
		//
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _TYPE;
	}
}
