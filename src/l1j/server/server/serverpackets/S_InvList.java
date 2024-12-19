package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_InvList extends ServerBasePacket {

	private static final String S_INV_LIST = "[S] S_InvList";

	/**
	 * 목록에 아이템을 복수개 정리해 추가한다.
	 */
	public S_InvList(List<L1ItemInstance> items) {
		super(items.size() * 128);
		writeC(Opcodes.S_ADD_INVENTORY_BATCH);
		writeC(items.size());
		byte[] status = null;
		for (L1ItemInstance item : items) {
			writeD(item.getId());
			writeH(item.getItem().getItemDescId());

			/** MJCTSystem **/
			if(item.getItemId() == 600226 || item.getItemId() == 60022 || item.getItemId() == 3000235  ||  item.getItemId() == MJCTLoadManager.CTSYSTEM_STORE_ID) {
				writeH(0x0044);
			} else if (item.getItemId() == 41248 || item.getItemId() == 500214 || item.getItemId() == 740
					|| item.getItemId() == 741 || item.getItemId() == 742 || item.getItemId() == 743
					|| item.getItemId() == 744 || item.getItemId() == 745 || item.getItemId() == 746
					|| item.getItemId() == 750 || item.getItemId() == 30022 || item.getItemId() == 30023
					|| item.getItemId() == 30024 || item.getItemId() == 30025 || item.getItemId() == 41249
					|| item.getItemId() == 510222 || item.getItemId() == 41250 || item.getItemId() == 210070
					|| item.getItemId() == 210071 || item.getItemId() == 210072 || item.getItemId() == 210086
					|| item.getItemId() == 210096 || item.getItemId() == 210105 || item.getItemId() == 746
					|| item.getItemId() == 755 || item.getItemId() == 756 || item.getItemId() == 757 || item.getItemId() == 42654
					|| item.getItemId() == 410172 || item.getItemId() == 410173 || item.getItemId() == 447012
					|| item.getItemId() == 447013 || item.getItemId() == 447014 || item.getItemId() == 447015
					|| item.getItemId() == 447016 || item.getItemId() == 447017 || item.getItemId() == 500212
					|| item.getItemId() == 500213 || item.getItemId() == 500215
					|| item.getItemId() == 510216 || item.getItemId() == 510217 || item.getItemId() == 510218
					|| item.getItemId() == 510219 || item.getItemId() == 510220 || item.getItemId() == 510221
					|| item.getItemId() == 3000086 || item.getItemId() == 3000087 || item.getItemId() == 3000088
					|| item.getItemId() == 3000351 || item.getItemId() == 3000352
					|| item.getItemId() == 758 || item.getItemId() == 759 || item.getItemId() == 760 || item.getItemId() == 761
					|| item.getItemId() == 772 || item.getItemId() == 773 || item.getItemId() == 774 || item.getItemId() == 775
					|| item.getItemId() == 4100007 || item.getItemId() == 4100008 || item.getItemId() == 4100009 || item.getItemId() == 4100010) {
				writeH(0x0049);
			} else {
				int type = item.getItem().getUseType();
				if (type < 0) {
					type = 0;
				}
				writeC(type);
				int count = item.getChargeCount();
				if (count < 0) {
					count = 0;
				}
				writeC(count);
			}		

			if (item.getItemId() == 4100135)
				writeH(5556);
			else
				writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());

			int bit  = 0;  
			if (!item.getItem().isTradable()) bit += 2;//교환 불가능
			if (item.getItem().isCantDelete()) bit  += 4;//삭제 불가능
			if (item.getItem().get_safeenchant() < 0) bit += 8;//인챈불가능
			//  if(item.getItem().getWareHouse()>0&&!item.getItem().isTradable()) bit += 16; // 창고보관가능
			if (item.getBless() >= 128) bit  = 46;
			if (item.isIdentified())bit += 1;//확인
			writeC(bit);
			writeS(item.getViewName());
			if (!item.isIdentified()) {
				// 미감정의 경우 스테이터스를 보낼 필요는 없다
				writeC(0);
			} else {
				status = item.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
			writeC(24);
			writeC(0);
			writeH(0);
			writeH(0);
			if(item.getItem().getType2() == 0){
				writeC(0);
			} else {
				writeC(item.getEnchantLevel());
			}
			writeD(item.getId());
			writeD(0);
			writeD(0);
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);//1102제작패킷수정 근방으로
			writeD(0);
			int attrbit = item.getAttrEnchantBit(item.getAttrEnchantLevel());
			writeC(attrbit);
		}
		writeH(0);
	}
	
	public S_InvList(List<L1ItemInstance> items, List<L1ItemInstance> notsendList) {
		super(items.size() * 128);
		writeC(Opcodes.S_ADD_INVENTORY_BATCH);
		writeC(items.size() - notsendList.size());
		byte[] status = null;
		for (L1ItemInstance item : items) {
			if(notsendList.contains(item))
				continue;
			
			writeD(item.getId());
			writeH(item.getItem().getItemDescId());

			/** MJCTSystem **/
			if(item.getItemId() == 600226 || item.getItemId() == 60022 || item.getItemId() == 3000235  ||  item.getItemId() == MJCTLoadManager.CTSYSTEM_STORE_ID) {
				writeH(0x0044);
			} else if (item.getItemId() == 41248 || item.getItemId() == 500214 || item.getItemId() == 740
					|| item.getItemId() == 741 || item.getItemId() == 742 || item.getItemId() == 743
					|| item.getItemId() == 744 || item.getItemId() == 745 || item.getItemId() == 746
					|| item.getItemId() == 750 || item.getItemId() == 30022 || item.getItemId() == 30023
					|| item.getItemId() == 30024 || item.getItemId() == 30025 || item.getItemId() == 41249
					|| item.getItemId() == 510222 || item.getItemId() == 41250 || item.getItemId() == 210070
					|| item.getItemId() == 210071 || item.getItemId() == 210072 || item.getItemId() == 210086
					|| item.getItemId() == 210096 || item.getItemId() == 210105 || item.getItemId() == 746
					|| item.getItemId() == 755 || item.getItemId() == 756 || item.getItemId() == 757 || item.getItemId() == 42654
					|| item.getItemId() == 410172 || item.getItemId() == 410173 || item.getItemId() == 447012
					|| item.getItemId() == 447013 || item.getItemId() == 447014 || item.getItemId() == 447015
					|| item.getItemId() == 447016 || item.getItemId() == 447017 || item.getItemId() == 500212
					|| item.getItemId() == 500213 || item.getItemId() == 500215
					|| item.getItemId() == 510216 || item.getItemId() == 510217 || item.getItemId() == 510218
					|| item.getItemId() == 510219 || item.getItemId() == 510220 || item.getItemId() == 510221
					|| item.getItemId() == 3000086 || item.getItemId() == 3000087 || item.getItemId() == 3000088
					|| item.getItemId() == 3000351 || item.getItemId() == 3000352
					|| item.getItemId() == 758 || item.getItemId() == 759 || item.getItemId() == 760 || item.getItemId() == 761
					|| item.getItemId() == 772 || item.getItemId() == 773 || item.getItemId() == 774 || item.getItemId() == 775
					|| item.getItemId() == 4100007 || item.getItemId() == 4100008 || item.getItemId() == 4100009 || item.getItemId() == 4100010) {
				writeH(0x0049);
			} else {
				int type = item.getItem().getUseType();
				if (type < 0) {
					type = 0;
				}
				writeC(type);
				int count = item.getChargeCount();
				if (count < 0) {
					count = 0;
				}
				writeC(count);
			}		

			if (item.getItemId() == 4100135)
				writeH(5556);
			else
				writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());

			int bit  = 0;  
			if (!item.getItem().isTradable()) bit += 2;//교환 불가능
			if (item.getItem().isCantDelete()) bit  += 4;//삭제 불가능
			if (item.getItem().get_safeenchant() < 0) bit += 8;//인챈불가능
			//  if(item.getItem().getWareHouse()>0&&!item.getItem().isTradable()) bit += 16; // 창고보관가능
			if (item.getBless() >= 128) bit  = 46; 
			if (item.isIdentified())bit += 1;//확인
			writeC(bit);
			writeS(item.getViewName());
			if (!item.isIdentified()) {
				// 미감정의 경우 스테이터스를 보낼 필요는 없다
				writeC(0);
			} else {
				status = item.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
			writeC(24);
			writeC(0);
			writeH(0);
			writeH(0);
			if(item.getItem().getType2() == 0){
				writeC(0);
			} else {
				writeC(item.getEnchantLevel());
			}
			writeD(item.getId());
			writeD(0);
			writeD(0);
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);//1102제작패킷수정 근방으로
			writeD(0);
			int attrbit = item.getAttrEnchantBit(item.getAttrEnchantLevel());
			writeC(attrbit);
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_INV_LIST;
	}
}
