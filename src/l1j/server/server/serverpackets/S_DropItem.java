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

import java.text.SimpleDateFormat;
import java.util.Locale;

import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_DropItem extends ServerBasePacket {

	private static final String _S__OB_DropItem = "[S] S_DropItem";
	private static final SimpleDateFormat itemRemainTimeFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);
	public S_DropItem(L1ItemInstance item) {
		buildPacket(item);
	}

	private String parse_item_name(L1ItemInstance item){
		if(PrideLoadManager.USE_PRIDE_SYSTEM && item.getItemId() == PrideLoadManager.PRIDE_ITEM_ID)
			return item.getViewName();
		
		
		StringBuffer sb = null;
		sb = new StringBuffer();
		if (item.isIdentified()) {
			if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) {
				switch (item.getAttrEnchantLevel()) {
				case 1:sb.append("$6115");break;
				case 2:sb.append("$6116");break;
				case 3:sb.append("$6117");break;
				case 4:sb.append("$14361");break;
				case 5:sb.append("$14365");break;
				case 6:sb.append("$6118");break;
				case 7:	sb.append("$6119");	break;
				case 8:sb.append("$6120");break;
				case 9:sb.append("$14362");break;
				case 10:sb.append("$14366");break;
				case 11:sb.append("$6121");break;
				case 12:sb.append("$6122");break;
				case 13:sb.append("$6123");break;
				case 14:sb.append("$14363");break;
				case 15:sb.append("$14367");break;
				case 16:sb.append("$6124");break;
				case 17:sb.append("$6125");break;
				case 18:sb.append("$6126");break;
				case 19:sb.append("$14364");break;
				case 20:sb.append("$14368");break;
				default:
					sb.append(" ");
					break;
				}
				// 인첸 +0 일때도 표기되게 하실분은 밑에 if (item.getEnchantLevel() >= 0) {로 교체
				if (item.getEnchantLevel() > 0) {
					sb.append("+" + item.getEnchantLevel() + " ");
				} else if (item.getEnchantLevel() < 0) {
					sb.append(String.valueOf(item.getEnchantLevel()) + " ");
				}
			}
		}
		sb.append(item.getItem().getNameId());
		if (item.getCount() > 1) {
			sb.append(" (" + item.getCount() + ")");
		} else {
			int itemId = item.getItem().getItemId();
			int isId = item.isIdentified() ? 1 : 0;
			if (itemId == 20383 && isId == 1) {
				sb.append(" [" + item.getChargeCount() + "]");
			} else if ((itemId == 40006 || itemId == 40007 || itemId == 40008 || itemId == 46091 || itemId == 40009
					|| itemId == 45464 || itemId == 140006 || itemId == 140008)
					&& isId == 1) {
				sb.append(" (" + item.getChargeCount() + ")");
			} else if(itemId == MJINNHelper.INN_KEYID){
				sb.append(" [").append(itemRemainTimeFormat.format(item.getEndTime().getTime())).append("]");
			} else if (itemId == 80500) {
				sb.append(" [" + item.getEndTime().getTime() + "]");
			} else if (item.getItem().getLightRange() != 0 && item.isNowLighting()) {
				sb.append(" ($10)");
			}
		}
		return sb.toString();
	}
	
	private void buildPacket(L1ItemInstance item) {
		// int addbyte = 0;
		// int addbyte1 = 1;
		// int addbyte2 = 13;
		// int setting = 4;
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(item.getX());
		writeH(item.getY());
		writeD(item.getId());
		writeH(item.getItem().getGroundGfxId());
		writeC(0);
		writeC(0);
		if (item.isNowLighting()) {
			writeC(item.getItem().getLightRange());
		} else {
			writeC(0);
		}
		writeC(0);
		writeD(item.getCount());
		writeC(0);
		writeC(0);
		
		writeS(parse_item_name(item));
		writeC(0);
		writeD(0);
		writeD(0);
		writeC(255);
		writeC(0);
		writeC(0);
		writeC(0);
		writeH(65535);
		writeD(0);
		writeC(8);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__OB_DropItem;
	}

}
