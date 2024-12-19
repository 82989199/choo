package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;
import l1j.server.server.utils.MJCommons;

public class S_Attack extends ServerBasePacket {
	private static AtomicInteger _sequence = new AtomicInteger(0);
	
	public static S_Attack getKeylink2(L1Object own, L1Object tar, int type, boolean isHit){
		S_Attack s = new S_Attack();
		s.writeC(ActionCodes.ACTION_ClawAttack);
		s.writeD(own.getId());
		s.writeD(tar.getId());
		if(isHit)
			s.writeH(ActionCodes.ACTION_ClawDamage);
		else
			s.writeH(0x00);
		int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
		s.writeC(h);
		s.writeD(_sequence.incrementAndGet());
		s.writeH(13923);//키링크이펙트
		s.writeC(0x00);
		s.writeH(own.getX());
		s.writeH(own.getY());
		s.writeH(tar.getX());
		s.writeH(tar.getY());
		s.writeD(0x00);
		s.writeC(0x00);
		return s;
	}
	
	public static S_Attack getKeylink(L1Object own, L1Object tar, int type, boolean isHit){
		S_Attack s = new S_Attack();
		s.writeC(ActionCodes.ACTION_ClawAttack);
		s.writeD(own.getId());
		s.writeD(tar.getId());
		if(isHit)
			s.writeH(ActionCodes.ACTION_ClawDamage);
		else
			s.writeH(0x00);
		int h = MJCommons.calcheading(own.getX(), own.getY(), tar.getX(), tar.getY());
		s.writeC(h);
		s.writeD(_sequence.incrementAndGet());
		s.writeH(7049);//키링크이펙트
		s.writeC(0x00);
		s.writeH(own.getX());
		s.writeH(own.getY());
		s.writeH(tar.getX());
		s.writeH(tar.getY());
		s.writeD(0x00);
		s.writeC(0x00);
		return s;
	}
	
	private S_Attack(){
		writeC(Opcodes.S_ATTACK);
	}
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
