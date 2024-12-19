package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1TradeItemBox.TradeItem;
import l1j.server.server.templates.L1Item;

public class S_TradeItemShop extends ServerBasePacket {

	public S_TradeItemShop(int objId, List<L1ItemInstance> _items) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(10);
		
		writeH(_items.size());
		for (int i = 0; i < _items.size(); i++) {
		   L1ItemInstance shopItem = _items.get(i);
		   L1Item item = shopItem.getItem();
			writeD(shopItem.getId());
			writeH(shopItem.getItem().getGfxId());
			writeD(1);//가격
			String BlessCheck = "";
			//writeS(shopItem.getViewName());			
			if(shopItem.getEnchantLevel() >0){
				if(shopItem.getItem().getBless() !=1 ){
					if(shopItem.getItem().getBless() == 0) BlessCheck = "$227";
					else BlessCheck = "$228";
				}
					writeS(BlessCheck+ " "+"+"+shopItem.getEnchantLevel()+" " + item.getNameId());	
			}else {
				if(shopItem.getItem().getBless() !=1 ){
					if(shopItem.getItem().getBless() == 0) BlessCheck = "$227";
					else BlessCheck = "$228";
				}
					writeS(BlessCheck+ " " + item.getNameId());
			}
			
			
			//한국옵 추가
			int type = shopItem.getItem().getUseType();
			if (type < 0){
				type = 0;
			}
			writeD(type);
			
			byte[] status = shopItem.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		writeH(7);
		writeH(0);	
	} 
	
	
	@Override
	public byte[] getContent() throws IOException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
}
