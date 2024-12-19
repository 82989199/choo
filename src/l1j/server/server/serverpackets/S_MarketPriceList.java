package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1MarketPrice;
import l1j.server.server.utils.BinaryOutputStream;

public class S_MarketPriceList extends ServerBasePacket {
	public S_MarketPriceList(L1PcInstance pc, ArrayList<L1MarketPrice> list) {
		try {
			writeC(Opcodes.S_RETRIEVE_LIST);
			writeD(0);
			writeH(list.size());
			writeC(5); // 혈맹 창고
			L1Item item = null;
			L1Item template = null;
			
			Collections.sort(list, new NoAscCompare());
			int size = list.size();
			if(size > 200){
				size = 200;
			}
			for (int i = 0; i < size; i++) {
				L1MarketPrice auction = list.get(i);
				item = ItemTable.getInstance().getTemplate(auction.getItemId());
				writeD(auction.getOrder());
				writeC(item.getType2());
				writeH(item.getGfxId());
				writeC(auction.getIden());
				writeD(auction.getCount());
				writeC(1);
				
				
				StringBuilder name = new StringBuilder();
				switch (auction.getAttr()) {
				case 1:
					name.append("$6115");
					break; // 화령1단
				case 2:
					name.append("$6116");
					break; // 화령2단
				case 3:
					name.append("$6117");
					break; // 화령3단 (불의속성)
				case 4:
					name.append("$14361");
					break; // 화령4단
				case 5:
					name.append("$14365");
					break; // 화령5단

				case 6:
					name.append("$6118");
					break; // 수령1단
				case 7:
					name.append("$6119");
					break; // 수령2단
				case 8:
					name.append("$6120");
					break; // 수령3단 (물의속성)
				case 9:
					name.append("$14362");
					break; // 수령4단
				case 10:
					name.append("$14366");
					break; // 수령5단

				case 11:
					name.append("$6121");
					break; // 풍령1단
				case 12:
					name.append("$6122");
					break; // 풍령2단
				case 13:
					name.append("$6123");
					break; // 풍령3단 (바람의속성)
				case 14:
					name.append("$14363");
					break; // 풍령4단
				case 15:
					name.append("$14367");
					break; // 풍령5단

				case 16:
					name.append("$6124");
					break; // 지령1단
				case 17:
					name.append("$6125");
					break; // 지령2단
				case 18:
					name.append("$6126");
					break; // 지령3단 (땅의속성)
				case 19:
					name.append("$14364");
					break; // 지령4단
				case 20:
					name.append("$14368");
					break; // 지령5단
				default:
					break;
				}

				if (auction.getEnchant() >= 0) {
					name.append("+" + auction.getEnchant() + " ");
				} else if (auction.getEnchant() < 0) {
					name.append(String.valueOf(auction.getEnchant()) + " ");
				}

				if (auction.getIden() == 0) {// 축?
					writeS("\\fH축복받은 " + item.getName());// 아이템 이름
				} else if (auction.getIden() == 2) {// 저주
					writeS("\\f3저주받은 " + item.getName());// 아이템 이름
				} else {
					writeS(name + item.getName());// 아이템 이름
				}
				
				int type = item.getUseType();
				if (type < 0) {
					type = 0;
				}
				template = ItemTable.getInstance().getTemplate(item.getItemId());
				if (template == null) {
					writeC(0);
				} else {
					@SuppressWarnings("resource")
					BinaryOutputStream os = new BinaryOutputStream();
					os.writeC(39);
					if(auction.getCharName() != null){
						os.writeS("\\fY판매자 : " + auction.getCharName());
					} else {
						os.writeS("\\fU판매자 : 영자상점");
					}
					
					
					if(auction.getPrice() > 0){
						StringBuffer max = null;
						if(auction.getPrice() >= 1000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(3, ",");
						}
						if(auction.getPrice() >= 1000000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(6, ",");
							max.insert(3, ",");
						} 
						if(auction.getPrice() > 1000000000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(9, ",");
							max.insert(6, ",");
							max.insert(3, ",");
						}
						max = reverse(max.toString());
						max.insert(0, "\\aC");
						
						os.writeC(39);
						os.writeS("\\aG판매금 : " + max.toString());
					}
					
					os.writeC(39);
					if(auction.getCount() > 0){
						os.writeS("\\aH판매수량 : " + auction.getCount());
					}
					writeC(os.getBytes().length);
					for (byte b : os.getBytes()) {
						writeC(b);
					}
				}
			}
			writeD(0);
			writeD(0x00000000);
			writeH(0x00);
			
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	private StringBuffer reverse(String s) {
		return new StringBuffer(s).reverse();
	}
	
	static class NoAscCompare implements Comparator<L1MarketPrice> {
		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(L1MarketPrice arg0, L1MarketPrice arg1) {
			// TODO Auto-generated method stub
			return arg0.getPrice() < arg1.getPrice() ? -1 : arg0.getPrice() > arg1.getPrice() ? 1 : 0;
		}

	}

	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
