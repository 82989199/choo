/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_AttackStatus extends ServerBasePacket {
	private static final String _S__1F_ATTACKPACKET = "[S] S_AttackStatus";
	public S_AttackStatus(int attacker_id, int attacker_h, int receiver_id, int type){
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(attacker_id);
		writeD(receiver_id);
		writeC(0x01);
		writeC(0);
		writeC(attacker_h);
		writeD(0x00000000);
	}
	
	public S_AttackStatus(L1PcInstance pc, int objid, int type) {
		buildpacket(pc, objid, type);
	}

	public S_AttackStatus(L1PcInstance pc, int objid, int type, int gfxid) {
		buildpacket(pc, type, gfxid);
	}

	private void buildpacket(L1PcInstance pc, int objid, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(pc.getId());
		writeD(objid);
		writeC(0x01);
		writeC(0);
		writeC(pc.getHeading());
		writeD(0x00000000);
	}
	
	public S_AttackStatus(L1PcInstance pc, int gfxId){
		writeC(Opcodes.S_ATTACK);
		writeC(0x01);
		writeD(pc.getId());
		writeD(pc.getId());
		writeH(0x01);
		writeC(pc.getHeading());
		writeD(0x00);
		writeC(2);
		writeD(gfxId);
		writeH(0);
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
