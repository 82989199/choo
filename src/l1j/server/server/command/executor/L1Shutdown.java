/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.logging.Logger;

import l1j.server.server.GameServer;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Shutdown implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Shutdown.class.getName());

	private L1Shutdown() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Shutdown();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (arg.equalsIgnoreCase("지금")) {
				GameServer.getInstance(). shutdownWithCountdown(0);
				return;
			}

			if (arg.equalsIgnoreCase("취소")) {
				GameServer.getInstance(). abortShutdown();
				return;
			}
			
			int sec = Math.max(5, Integer.parseInt(arg));
			GameServer.getInstance(). shutdownWithCountdown(sec);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".종료 [대기초, 지금, 취소] 라고 입력해 주세요."));
		}
	}
}
