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

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_UseArrowSkill extends ServerBasePacket {

	private static final String S_USE_ARROW_SKILL = "[S] S_UseArrowSkill";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	public S_UseArrowSkill(int attacker_id, int ox, int oy, int oh, int act_id, int target_id, int spellgfx, int tx, int ty, boolean is_hit){
		writeC(Opcodes.S_ATTACK);
		writeC(act_id);
		writeD(attacker_id);
		writeD(target_id);
		writeC(is_hit ? 6 : 0);
		writeC(0);
		writeC(oh);
		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(127);
		writeH(ox);
		writeH(oy);
		writeH(tx);
		writeH(ty);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}
	
	public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx, int x,
			int y, boolean isHit) {

		int aid = 1;
		// 오크 궁수에만 변경
		int spriteId = cha.getCurrentSpriteId();		
		if (spriteId == 3860 || spriteId == 15053 || spriteId == 14170) {//다크엘프 공격모션 오류없이
			aid = 21;
		}
		writeC(Opcodes.S_ATTACK);
		writeC(aid);
		writeD(cha.getId());
		writeD(targetobj);
		writeC(isHit ? 6 : 0);
		writeC(0);
		writeC(cha.getHeading());
		// writeD(0x12000000);
		// writeD(246);
		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(127); // 스킬 사용시의 광원의 넓이?
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		// writeC(228);
		// writeC(231);
		// writeC(95);
		// writeC(82);
		// writeC(170);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}
	
	@Override
	public byte[] getContent() {
		byte[] b = getBytes();
		int seq = _sequentialNumber.incrementAndGet();
		b[12] = (byte) (seq & 0xff);
		b[13] = (byte) (seq >> 8 & 0xff);
		b[14] = (byte) (seq >> 16 & 0xff);
		b[15] = (byte) (seq >> 24 & 0xff);
		return b;
	}
	@Override
	public String getType() {
		return S_USE_ARROW_SKILL;
	}

}
