package l1j.server.DogFight.Packets;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DogFightSellList extends ServerBasePacket{
	
	private S_DogFightSellList(){
		writeC(Opcodes.S_SELL_LIST);
	}
	
	public static S_DogFightSellList newInstance(int npc_object_id, ArrayList<MJKeyValuePair<Integer, Integer>> pairs){
		S_DogFightSellList sell = new S_DogFightSellList();
		sell.writeD(npc_object_id);		
		sell.writeH(pairs.size());
		for(MJKeyValuePair<Integer, Integer> pair : pairs){
			sell.writeD(pair.key);
			sell.writeD(pair.value);
		}
//		sell.writeH(7);//표기 타입부분
		sell.writeH(17619); // 배팅코인(루돌프의 종) 표기 타입부분
		return sell;
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
