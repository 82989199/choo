package l1j.server.MJINNSystem;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.MJTemplate.Builder.MJServerPacketBuilder;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CommonUtil;

public class MJINNHelper {
	public static final int 	INN_KEYID 			= 40312;		// 여관 열쇠 아이템 아이디
	public static final int 	INN_PRICE 			= 300;		// 여관 빌리는 가격(열쇠 하나당)
	public static final int 	HALL_PRICE 			= 600;		// 홀 빌리는 가격(열쇠 하나당)
	public static final int	SELL_PRICE			= 10;			// 되팔았을때의 가격
	public static int 			USAGECOUNT;						// 여관 룸 최대 생성갯수(마을마다 그리고 홀/여관 따로 각각 적용된다.)
	public static final long RENTAL_MINUTE 	= 240;		// 빌리고 반환될때까지의 유지시간(분)
	public static MJINNHelper create(){
		return new MJINNHelper();
	}
	
	private int 			_helperId;
	private int				_innId;
	private MJINNMapInfo[]	_mInfo;
	
	
	private MJINNHelper(){
	}
	
	public void setHelperId(int helperid){
		_helperId = helperid;
	}
	
	public int getHelperId(){
		return _helperId;
	}
	
	public void setInnId(int innid){
		_innId = innid;
	}
	
	public int getInnId(){
		return _innId;
	}
	
	public void setMapInfo(MJINNMapInfo[] mInfo){
		_mInfo = mInfo;
	}
	
	private ArrayList<Integer> rental(L1PcInstance pc, int price, int amount, int keyId){
		ArrayList<Integer> list = new ArrayList<Integer>(amount);
		L1Inventory inv = pc.getInventory();
		for(int i = amount - 1; i>=0; --i){
			L1ItemInstance item = ItemTable.getInstance().createItem(keyId);
			if(inv.checkAddItem(item, 1) != L1Inventory.OK || !inv.consumeItem(L1ItemId.ADENA, price)){
				pc.sendPackets("소지품이 너무 많아 더 이상 들 수 없습니다.");
				break;
			}
			CommonUtil.SetTodayDeleteTime(item, (int)RENTAL_MINUTE);
			CommonUtil.SetHotelTownName(item, _mInfo[0].town);
			list.add(item.getId());
			inv.storeItem(item);
		}
		return list;
	}
	
	@SuppressWarnings("resource")
	public void onResult(L1NpcInstance npc, L1PcInstance pc, String s, int amount) throws IOException{
		switch(s){
		case "inn2":{
			int price = amount * INN_PRICE;
			if(!pc.getInventory().checkItem(L1ItemId.ADENA, price)){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn3"));
				break;
			}
			ArrayList<Integer> list = rental(pc, INN_PRICE, amount, INN_KEYID);
			if(list.size() > 0){
				MJINNRoom.create(_mInfo[0], list.toArray(new Integer[list.size()]));
				_mInfo[0].usageCount.incrementAndGet();
				pc.sendPackets(createNormalPacket(npc.getId(), "inn4"));
			}
			list.clear();
			break;
		}
		case "inn12":{
			int price = amount * HALL_PRICE;
			if(!pc.getInventory().checkItem(L1ItemId.ADENA, price)){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn3"));
				break;
			}
			ArrayList<Integer> list = rental(pc, HALL_PRICE, amount, INN_KEYID);
			if(list.size() > 0){
				MJINNRoom.create(_mInfo[1], list.toArray(new Integer[list.size()]));
				_mInfo[1].usageCount.incrementAndGet();
				pc.sendPackets(createNormalPacket(npc.getId(), "inn4"));
			}
			list.clear();
			break;
		}
		}
	}
	
	public void onTalk(L1NpcInstance npc, L1PcInstance pc){
		try{
			pc.sendPackets(createNormalPacket(npc.getId(), pc.getLawful() < 0 ? "inn11" : "inn"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onAction(L1NpcInstance npc, L1PcInstance pc, String s) throws IOException{
		L1ItemInstance[] keys = pc.getInventory().findItemsId(INN_KEYID);
		switch(s){
		case "room":
			if(keys != null && keys.length > 0){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn5"));
				break;
			}
			
			if(_mInfo[0].usageCount.get() >= USAGECOUNT){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn6"));
				break;
			}
			
			pc.sendPackets(new MJServerPacketBuilder(16).addC(Opcodes.S_HYPERTEXT_INPUT).addD(npc.getId()).addD(INN_PRICE).addD(1).addD(1).addD(20).addH(0).addS("inn2").addS("inn2").addH(2).addS(npc.getName()).addS(Integer.toString(INN_PRICE)).build(true));
			break;
		case "hall":
			if(keys != null && keys.length > 0){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn15"));
				break;
			}
			
			if(_mInfo[1].usageCount.get() >= USAGECOUNT){
				pc.sendPackets(createNormalPacket(npc.getId(), "inn6"));
				break;
			}
			
			pc.sendPackets(
					pc.isCrown() ? 
							new MJServerPacketBuilder(32).addC(Opcodes.S_HYPERTEXT_INPUT).addD(npc.getId()).addD(HALL_PRICE).addD(1).addD(1).addD(20).addH(0).addS("inn12").addS("inn12").addH(2).addS(npc.getName()).addS(Integer.toString(HALL_PRICE)).build(true) 
							: createNormalPacket(npc.getId(), "inn10"));
		case "return":
			if(keys == null || keys.length <= 0)
				return;

			for(L1ItemInstance key : keys){
				MJINNRoom.remove(key);
				pc.getInventory().removeItem(key);
			}
			int price = keys.length * SELL_PRICE;
			pc.getInventory().storeItem(L1ItemId.ADENA, keys.length * SELL_PRICE);
			pc.sendPackets(new MJServerPacketBuilder(16).addC(Opcodes.S_HYPERTEXT).addD(npc.getId()).addS("inn20").addH(0x01).addH(2).addS(npc.getName()).addS(Integer.toString(price)).build(true));
			break;
		case "enter":
			if(keys == null || keys.length <= 0)
				return;
			
			MJINNRoom.input(keys, pc);
			break;
		}
	}
	
	@SuppressWarnings("resource")
	private ServerBasePacket createNormalPacket(int objid, String htmlId) throws IOException{
		return new MJServerPacketBuilder(16)
		.addC(Opcodes.S_HYPERTEXT)
		.addD(objid)
		.addS(htmlId)
		.addH(0x00)
		.addH(0x00)
		.build(true);
	}
}
