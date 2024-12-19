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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Sound extends ServerBasePacket {

	private static final String S_SOUND = "[S] S_Sound";

	/**
	 * 효과음을 울린다(sound 폴더의 wav 파일).
	 * @param sound
	 */
	public static void broadcast(L1Character cha, int id){
		S_Sound sound = new S_Sound(id);
		if(cha instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance)cha;
			pc.sendPackets(sound, false);
		}
		cha.broadcastPacket(sound);
	}
	
	public S_Sound(int sound) {
		buildPacket(sound);
	}

	private void buildPacket(int sound) {
		writeC(Opcodes.S_SOUND_EFFECT);
		writeC(0); // repeat
		writeH(sound);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SOUND;
	}
}
