package l1j.server.DogFight.Packets;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.DogFight.History.MJDogFightTicketInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DogFightBuyList extends ServerBasePacket {

	public static S_DogFightBuyList newInstance(L1PcInstance pc, int object_id, ArrayList<MJDogFightTicketInfo> tickets_info, int price){
		S_DogFightBuyList buy = new S_DogFightBuyList();
		buy.writeD(object_id);
		int size = tickets_info.size();
//		buy.writeH(size + 6); // 아래 1만장, 10만장할때는 6으로 설정
		buy.writeH(size);
		for(int i=0; i<size; ++i){
			MJDogFightTicketInfo tInfo = tickets_info.get(i);
			buy.writeD(i);
			buy.writeH(tInfo.get_template().getGfxId());
			buy.writeD(price);
			buy.writeS(tInfo.get_template().getName());
			buy.writeD(tInfo.get_template().getUseType());
			L1ItemInstance dummy = new L1ItemInstance();
			dummy.setItem(tInfo.get_template());
			byte[] status = dummy.getStatusBytes();
			buy.writeC(status.length);
			for (byte b : status) {
				buy.writeC(b);
			}
		}
		
//		/**1만개 묶음*/
//		for(int i=0; i<size; ++i){
//			MJDogFightTicketInfo tInfo = tickets_info.get(i);
//			buy.writeD(i + 3);
//			buy.writeH(tInfo.get_template().getGfxId());
//			buy.writeD(price * 10000);
//			buy.writeS(tInfo.get_template().getName() + " [1만장]");
//			buy.writeD(tInfo.get_template().getUseType());
//			L1ItemInstance dummy = new L1ItemInstance();
//			dummy.setItem(tInfo.get_template());
//			byte[] status = dummy.getStatusBytes();
//			buy.writeC(status.length);
//			for (byte b : status) {
//				buy.writeC(b);
//			}
//		}
		
//		/**10만개 묶음*/
//		for(int i=0; i<size; ++i){
//			MJDogFightTicketInfo tInfo = tickets_info.get(i);
//			buy.writeD(i + 6);
//			buy.writeH(tInfo.get_template().getGfxId());
//			buy.writeD(price * 100000);
//			buy.writeS(tInfo.get_template().getName() + " [10만장]");
//			buy.writeD(tInfo.get_template().getUseType());
//			L1ItemInstance dummy = new L1ItemInstance();
//			dummy.setItem(tInfo.get_template());
//			byte[] status = dummy.getStatusBytes();
//			buy.writeC(status.length);
//			for (byte b : status) {
//				buy.writeC(b);
//			}
//		}
		
//		buy.writeH(7);//표기 타입부분
		buy.writeH(17619); // 배팅코인(루돌프의 종) 표기 타입부분
		return buy;
	}
	
	private S_DogFightBuyList(){
		writeC(Opcodes.S_BUY_LIST);
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
