package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SiegeInjuryTimeNoti extends ServerBasePacket{
	private static final int SC_SIEGE_INJURY_TIME_NOIT = 0x4C;
	
	public static S_SiegeInjuryTimeNoti get(){
		S_SiegeInjuryTimeNoti s = new S_SiegeInjuryTimeNoti();
		s.writeC(0x08);
		s.writeC(0x01);
		s.writeC(0x10);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_SiegeInjuryTimeNoti get(boolean isDefence, long sec, String cname){
		S_SiegeInjuryTimeNoti s = new S_SiegeInjuryTimeNoti();
		s.writeC(0x08);
		s.writeC(isDefence ? 0x01 : 0x02);
		s.writeC(0x10);
		if(sec > 0){
			s.writeBit(sec * 2);
			s.writeC(0x1A);
			s.writeS2(cname);
		}else
			s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	private S_SiegeInjuryTimeNoti(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_SIEGE_INJURY_TIME_NOIT);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
