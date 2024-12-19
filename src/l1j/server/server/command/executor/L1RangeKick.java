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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.MJCommons;


public class L1RangeKick implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1RangeKick.class.getName());

	private L1RangeKick() {}

	public static L1CommandExecutor getInstance() {
		return new L1RangeKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String				sname	= st.nextToken();
			if(sname == null || sname.equalsIgnoreCase(""))
				throw new Exception("");
			
			Integer reason = S_LoginResult.banServerCodes.get(Integer.parseInt(st.nextToken()));
			if(reason == null)
				throw new Exception("");
			
			L1PcInstance target = L1World.getInstance().getPlayer(sname);
//			if (target == null) {
//				target = CharacterTable.getInstance().restoreCharacter(arg);
//			}

			if (target != null) {
				IpTable ip = IpTable.getInstance();

				Account.ban(target.getAccountName(), reason); // 계정을 BAN시킨다.
				ip.rangeBanIp(target.getNetConnection().getHostname());
				pc.sendPackets(new S_SystemMessage(target.getName() + "[" + pc.getNetConnection() + "] 를 광역추방 했습니다."));						
				L1World.getInstance().removeObject(target);
				L1PowerKick.duplicateKick(target.getNetConnection().getIp(), target, reason);
				
				/*target.getNetConnection().kick();
				target.getNetConnection().close();
				target.logout();	
				target.sendPackets(new S_Disconnect());*/
//				if (target.getOnlineStatus() == 1) {
//					target.sendPackets(new S_Disconnect());
//				}
			} else {
				final MJObjectWrapper<String> wrapper = new MJObjectWrapper<String>();
				wrapper.value = "";
				Selector.exec("select account_name from characters where char_name=?", new SelectorHandler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setString(1, sname);
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						while(rs.next()){
							wrapper.value = rs.getString("account_name");
						}
					}
					
				});
				if(MJCommons.isNullOrEmpty(wrapper.value)){
					pc.sendPackets(new S_SystemMessage(String.format("%s은(는) 존재하지 않는 캐릭터명입니다.", sname)));					
				}else{
					IpTable ip = IpTable.getInstance();
					Account.ban(wrapper.value, reason); // 계정을 BAN시킨다.
					Integer[] octet = Account.loadAccountAddress(wrapper.value);
					StringBuilder sb = new StringBuilder(256);
					sb.append(sname).append("을 광역 추방했습니다.(").append(wrapper.value);
					if(octet != null){
						sb.append(", ").append(octet[0]).append(".").append(octet[1]).append(".").append(octet[2]).append(".").append("*");
						ip.rangeBanIp(octet);
					}
					sb.append(")");
					pc.sendPackets(sb.toString());
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] [정지사유번호]으로 입력해 주세요. "));
		}
	}
}
