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

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_WarTime extends ServerBasePacket {
	private static final String S_WAR_TIME = "[S] S_WarTime";	

	public S_WarTime(Calendar cal) {
		// 1997/01/01 17:00(을)를 기점으로 하고 있다
		Calendar base_cal = Calendar.getInstance();
		base_cal.set(1997, 0, 1, 17, 0);
		long base_millis = base_cal.getTimeInMillis();
		long millis = cal.getTimeInMillis();
		long diff = millis - base_millis;
		diff -= 1200 * 60 * 1000; // 오차수정
		diff = diff / 60000; // 분 이하 잘라버림
		// time는 1 가산한다고3:02(182분 ) 진행된다
		int time = (int) (diff / 182);

		// writeD의 직전의 writeC로 시간의 조절을 할 수 있다
		// 0.7배가 된 시간만 줄어들지만
		// 1개 조정하면(자) 그 다음의 시간이 퍼져?
		writeC(Opcodes.S_SELECTABLE_TIME_LIST);
		writeH(6); // 리스트의 수(6이상은 무효)
		writeS(Config.TIME_ZONE); // 시간의 뒤() 중에 표시되는 캐릭터 라인
		writeH(1);// 순번
		writeC(136);
		writeH(time);// 6:00
		writeH(2);// 순번
		writeC(178);
		writeH(time);// 6:30
		writeH(3);// 순번
		writeC(220);
		writeH(time);// 7:00
		writeH(4);// 순번
		writeC(218);
		writeH(time + 1);// 10:00
		writeH(5);// 순번
		writeC(4);
		writeH(time + 2);// 10:30
		writeH(6);// 순번
		writeC(46);// 11:00
		writeD(time + 2);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_WAR_TIME;
	}
}
