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
import java.util.logging.Logger;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RetrieveList;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1InvGfxId implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1InvGfxId.class.getName());

	private L1InvGfxId() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1InvGfxId();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int gfxid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			if(count <= 200){
				SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newDisplayInstance(pc.getId(), 0, gfxid, count, true);
				pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
			}else{
				int page = (count / 200) + ((count % 200) > 0 ? 1 : 0);
				for(int i=0; i<page; ++i){
					int real_index = i * 200;
					SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newDisplayInstance(pc.getId(), real_index, gfxid + real_index, 200, (i + 1 == page));
					pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
				}
			}
			
			
			//pc.sendPackets(new S_RetrieveList(pc.getId(), pc, gfxid, count));
			
			pc.sendPackets(new S_SystemMessage("\\aHInvGfxId " + gfxid + "~"
					+ String.valueOf(count) + " 생성 되었습니다."));
		} catch (Exception exception) {
			int count = 0;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item.getItemId() == 40005) {
					pc.getInventory().deleteItem(item);
					count++;
				}
			}
			if (count > 0) {
				pc.sendPackets(new S_SystemMessage("\\aAInvGfxId 확인용 아이템("
						+ count + ")이 삭제되었습니다."));
			} else {
				pc.sendPackets(new S_SystemMessage("\\aH" + cmdName
						+ " [InvGfxId] [갯수]로 입력해 주세요."));
			}
		}
	}
}
