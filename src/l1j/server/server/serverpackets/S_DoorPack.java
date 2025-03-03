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

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DoorInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_DoorPack

public class S_DoorPack extends ServerBasePacket {

	private static final String S_DOOR_PACK = "[S] S_DoorPack";

	private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;

	public S_DoorPack(L1DoorInstance door) {
		buildPacket(door);
	}

	private void buildPacket(L1DoorInstance door) {
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(door.getX());
		writeH(door.getY());
		writeD(door.getId());
		
		int spriteId = door.getCurrentSpriteId();
		writeH(spriteId);
		int doorStatus = door.getStatus();
		int openStatus = door.getOpenStatus();
		if (door.isDead()) {
			writeC(doorStatus);
		} else if (openStatus == ActionCodes.ACTION_Open) {
			writeC(openStatus);
		} else if (door.getMaxHp() > 1 && doorStatus != 0) {
			writeC(doorStatus);
		} else {
			writeC(openStatus);
		}
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(1);
		writeH(0);
		if (spriteId == 12164 
				|| spriteId == 12167 
				|| spriteId == 12170 //64~70 오크요새 외성문
				|| spriteId == 12987
				|| spriteId == 12989
				|| spriteId == 12991 //87~91 켄트성 외성문
				|| spriteId == 12127
				|| spriteId == 12129
				|| spriteId == 12131
				|| spriteId == 12133 //29~33 기란성 외성문
		) { 
			writeS("$440");
		} else if (spriteId == 339 //켄트성 내성문
				|| spriteId == 1336 //기란성 내성문
				|| spriteId == 12163  //오크요새 내성문
		) {
			writeS("$441");
		} else {
			writeS(null);
		}
		writeS(null);
		int status = 0;
		if (door.getPoison() != null) {
			if (door.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0);
		writeS(null);
		writeS(null);
		writeC(0);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeH(0);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_DOOR_PACK;
	}

}
