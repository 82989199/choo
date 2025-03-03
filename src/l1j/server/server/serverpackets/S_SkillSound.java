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
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillSound extends ServerBasePacket {
	private static final String S_SKILL_SOUND = "[S] S_SkillSound";

	public static void broadcast(L1Character cha, int id){
		S_SkillSound sound = new S_SkillSound(cha.getId(), id);
		if(cha instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance)cha;
			pc.sendPackets(sound, false);
		}
		cha.broadcastPacket(sound);
	}
	
	public S_SkillSound(int objid, int gfxid, int aid) {

		buildPacket(objid, gfxid, aid);
	}

	public S_SkillSound(int objid, int gfxid, int aid, int x, int y) {

		buildPacket(objid, gfxid, aid, x, y);
	}

	public S_SkillSound(int objid, int gfxid) {
		
		buildPacket(objid, gfxid, 0);
	}

	private void buildPacket(int objid, int gfxid, int aid) {
		// aid는 사용되지 않았다
		writeC(Opcodes.S_EFFECT);
		writeD(objid);
		writeH(gfxid);
		writeH(0);
		writeD(0x00000000);
	}

	private void buildPacket(int objid, int gfxid, int aid, int x, int y) {
		// aid는 사용되지 않았다
		writeC(Opcodes.S_EFFECT);
		writeD(objid);
		writeH(gfxid);
		writeH(0);
		writeD(0x00000000);
		writeH(x);
		writeH(y);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SKILL_SOUND;
	}
}
