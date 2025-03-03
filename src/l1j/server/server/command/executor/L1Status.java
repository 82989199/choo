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

import java.util.StringTokenizer;
//import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;


public class L1Status implements L1CommandExecutor {
//	private static Logger _log = Logger.getLogger(L1Status.class.getName());

	private L1Status() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Status();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String char_name = st.nextToken();
			String param = st.nextToken();
			int value = Integer.parseInt(st.nextToken());

			L1PcInstance target = null;
			target = L1World.getInstance(). getPlayer(char_name);

			if (target == null) {
				pc.sendPackets(new S_ServerMessage(73, char_name)); // \f1%0은 게임을 하고 있지 않습니다.
				return;
			}

			// -- not use DB --
			if (param.equalsIgnoreCase("방어")) {
				target.getAC().addAc((byte) (value - target.getAC().getAc()));
			} else if (param.equalsIgnoreCase("마방")) {
				target.getResistance().addMr((short) (value - target.getResistance().getMr()));
			} else if (param.equalsIgnoreCase("공성")) {
				target.addHitup((short) (value - target.getHitup()));
			} else if (param.equalsIgnoreCase("대미지")) {
				target.addDmgup((short) (value - target.getDmgup()));
				// -- use DB --
			} else {
				if (param.equalsIgnoreCase("피")) {
					target.addBaseMaxHp((short) (value - target.getBaseMaxHp()));
					target.setCurrentHp(target.getMaxHp());
				} else if (param.equalsIgnoreCase("엠피")) {
					target.addBaseMaxMp((short)(value - target.getBaseMaxMp()));
					target.setCurrentMp(target.getMaxMp());
				} else if (param.equalsIgnoreCase("성향")) {
					target.setLawful(value);
					S_Lawful s_lawful = new S_Lawful(target.getId(), target.getLawful());
					target.sendPackets(s_lawful);
					target.broadcastPacket(s_lawful);
				} else if (param.equalsIgnoreCase("우호도")) {
					target.setKarma(value);
				} else if (param.equalsIgnoreCase("지엠")) {
					if(value == Config.GMCODE){
						target.setAccessLevel((short) value);
						target.sendPackets(new S_SystemMessage("당신은 메티스께서 운영자권한을 부여 하였습니다."));
						GameClient clnt = target.getNetConnection();
						C_NewCharSelect.restartProcess(target);
						Account acc		= clnt.getAccount();
						clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
						if(acc.countCharacters() > 0)
							C_CommonClick.sendCharPacks(clnt);
					}else {
						target.sendPackets(new S_SystemMessage("GM번호가 일치하지 않습니다."));
					}
				} else if (param.equalsIgnoreCase("힘")) {
					target.getAbility().setStr((byte)value);
				} else if (param.equalsIgnoreCase("콘")) {
					target.getAbility().setCon((byte)value);
				} else if (param.equalsIgnoreCase("덱스")) {
					target.getAbility().setDex((byte)value);
				} else if (param.equalsIgnoreCase("인트")) {
					target.getAbility().setInt((byte)value);
				} else if (param.equalsIgnoreCase("위즈")) {
					target.getAbility().setWis((byte)value);
				} else if (param.equalsIgnoreCase("카리")) {
					target.getAbility().setCha((byte)value);
				} else {
					pc.sendPackets(new S_SystemMessage("스테이터스 " + param + " (은)는 불명합니다. "));
					return;
				}
				if (!param.equalsIgnoreCase("지엠"))
					target.save(); // DB에 캐릭터 정보를 기입한다
			}
			target.sendPackets(new S_OwnCharStatus(target));
			target.RenewStat();
			pc.sendPackets(new S_SystemMessage(target.getName() + "의 " + param + "(을)를 " + value + "로 변경했습니다. "));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [스텟] [변경치] 입력."));
			pc.sendPackets(new S_SystemMessage("피 엠피 성향 우호도 지엠 방어 마방 공성 대미지 힘 콘 덱스 인트 위즈 카리"));
		}
	}
}
