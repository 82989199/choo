package l1j.server.server.serverpackets;

import java.util.ArrayList;

import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AddItem extends ServerBasePacket {

	private static final String S_ADD_ITEM = "[S] S_AddItem";

	/**
	 * 목록에 아이템을 1개 추가한다.
	 */
	public S_AddItem(L1PcInstance pc, L1ItemInstance item) {
		super(128);
		writeC(Opcodes.S_ADD_INVENTORY);
		writeD(item.getId());
		writeH(item.getItem().getItemDescId());

		/** MJCTSystem **/
		if(item.getItemId() == 600226 || item.getItemId() == 600227 || item.getItemId() == 3000235 ||  item.getItemId() == MJCTLoadManager.CTSYSTEM_STORE_ID) {
			writeH(0x0044);
		}else {
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
		//  if(item.getItem().getWareHouse()>0&&!item.getItem().getItem().isTradable()) bit += 16; // 창고보관가능
		if (item.getBless() >= 128) bit  = 46; 
		if (item.isIdentified())bit += 1;//확인
		writeC(bit);
		writeS(item.getViewName());

		if (!item.isIdentified()) {
			// 미감정의 경우 스테이터스를 보낼 필요는 없다
			writeC(0);
		} else {
			byte[] status = null;
			if((item.getItemId() >= 222295 && item.getItemId() <= 222299) ||
					(item.getItemId() >= 222312 && item.getItemId() <= 222316) ||
					(item.getItemId() >= 900135 && item.getItemId() <= 900149)){
				status = item.getStatusBytes(pc);
			} else {
				status = item.getStatusBytes();
			}
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		writeC(0x18);
		writeC(0);
		writeH(0);
		writeH(0);
		if(item.getItem().getType2() == 0){
			writeC(0);
		} else {
			writeC(item.getEnchantLevel());
		}
		writeD(item.getId());
		if(item.getItemId() >= 41296 && item.getItemId() <= 41301 || item.getItemId() == 41304 || item.getItemId() == 41303
				|| item.getItemId() == 600230 || item.getItemId() == 820018
				|| item.getItemId() == 49092 || item.getItemId() == 49093 || item.getItemId() == 49094 || item.getItemId() == 49095) {
			writeD(0x08);
		}else {
			writeD(item.getOpenEffect());
			item.setOpenEffect(0);
		}
		
		writeD(0);
		writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);//제작1102추가
		writeD(0);
		if(item.getItem().getType2() == 1)
			writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel()));
		else
			writeC(0);
		
	}
	
	public static ArrayList<L1ItemInstance> _list = new ArrayList<L1ItemInstance>(16);
	
	public S_AddItem(L1PcInstance pc, int useType) {
		super(128);
		L1ItemInstance item = ItemTable.getInstance().createItem(40005);
		_list.add(item);
		writeC(Opcodes.S_ADD_INVENTORY);
		writeD(item.getId());
		writeH(item.getItem().getItemDescId());

		writeH(useType);
		
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
		//  if(item.getItem().getWareHouse()>0&&!item.getItem().getItem().isTradable()) bit += 16; // 창고보관가능
		if (item.getBless() >= 128) bit  = 46; 
		if (item.isIdentified())bit += 1;//확인
		writeC(bit);
		writeS(String.valueOf(useType));

		if (!item.isIdentified()) {
			// 미감정의 경우 스테이터스를 보낼 필요는 없다
			writeC(0);
		} else {
			byte[] status = null;
			if((item.getItemId() >= 222295 && item.getItemId() <= 222299) ||
					(item.getItemId() >= 222312 && item.getItemId() <= 222316) ||
					(item.getItemId() >= 900135 && item.getItemId() <= 900149)){
				status = item.getStatusBytes(pc);
			} else {
				status = item.getStatusBytes();
			}
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		writeC(0x18);
		writeC(0);
		writeH(0);
		writeH(0);
		if(item.getItem().getType2() == 0){
			writeC(0);
		} else {
			writeC(item.getEnchantLevel());
		}
		writeD(item.getId());
		if(item.getItem().getUseType() == 73){//인형깔때 인벤이펙트 패킷
			writeD(0x20);
		} else if(item.getItemId() >= 41296 && item.getItemId() <= 41301 || item.getItemId() == 41304 || item.getItemId() == 41303
				|| item.getItemId() == 600230 || item.getItemId() == 820018
				|| item.getItemId() == 49092 || item.getItemId() == 49093 || item.getItemId() == 49094 || item.getItemId() == 49095) {
			writeD(0x08);
		}else {
			writeD(item.getOpenEffect());
			item.setOpenEffect(0);
		}
		
		writeD(0);
		writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);//제작1102추가
		writeD(0);
		if(item.getItem().getType2() == 1)
			writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel()));
		else
			writeC(0);
		
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override	
	public String getType() {
		return S_ADD_ITEM;
	}
}
