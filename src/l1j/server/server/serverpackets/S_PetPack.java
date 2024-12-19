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
import l1j.server.server.model.Instance.L1PetInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_WorldPutObject.get

public class S_PetPack extends ServerBasePacket {

	private static final String S_PET_PACK = "[S] S_PetPack";
	
	private static final int STATUS_POISON = 1;

	public S_PetPack(L1PetInstance pet, L1PcInstance pc) {
		buildPacket(pet, pc);
	}

	private void buildPacket(L1PetInstance pet, L1PcInstance pc) {

		writeC(Opcodes.S_PUT_OBJECT);
		writeH(pet.getX());
		writeH(pet.getY());
		writeD(pet.getId());
		writeH(pet.getCurrentSpriteId()); // SpriteID in List.spr
		writeC(pet.getStatus()); // Modes in List.spr
		writeC(pet.getHeading());
		writeC(pet.getLight().getChaLightSize()); // (Bright) - 0~15
		writeC(pet.getMoveSpeed()); // 스피드 - 0:normal, 1:fast,
		// 2:slow
		writeD(pet.getExp());
		writeH(pet.getTempLawful());
		writeS(pet.getName());
		writeS(pet.getTitle());
		int status = 0;
		if (pet.getPoison() != null) {
			if (pet.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0); // ??
		writeS(null); // ??
		writeS(pet.getMaster() != null ? pet.getMaster().getName() : "");
		writeC(0); // ??
		// HP의 퍼센트
		if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
			int maxhp = pet.getMaxHp();
			try{
				writeC(100 * pet.getCurrentHp() / maxhp);
			}catch(ArithmeticException e) {
				writeC(0);
			}
		} else {
			writeC(0xFF);
		}
		writeC(0);
		writeC(pet.getLevel()); // PC = 0, Mon = Lv
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		// MP
		if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
			int maxmp = pet.getMaxMp();
			try{
				writeC(100 * pet.getCurrentMp() / maxmp);
			}catch(ArithmeticException e) {
				writeC(0);
			}
		} else {
			writeC(0xFF);
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_PET_PACK;
	}

}
