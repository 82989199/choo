package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class Buyer {
	private String buyername;
	private int level;				
	private String Memo;				
	
	public Buyer(String buyername, int level, String Memo){
		this.buyername = buyername;
		this.level = level;
		this.Memo = Memo;
	}
	
	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getMemo() {
		return Memo;
	}

	public void setMemo(String Memo) {
		this.Memo = Memo;
	}
	
	public void buyerGive() {
		L1PcInstance vip = L1World.getInstance().findpc(buyername);
		
		if(vip != null){
			SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(vip.getAccountName());		
			if(level == 1){
				vip.sendPackets(new S_SkillSound(vip.getId(), 15357));//이펙트발생
				L1ItemInstance item = ItemTable.getInstance().createItem(Config.level1[0]);
				item.setCount(Config.level1[1]);
				pwh.storeTradeItem(item);
			}else if(level == 2){
				vip.sendPackets(new S_SkillSound(vip.getId(), 15357));//이펙트발생
				L1ItemInstance item = ItemTable.getInstance().createItem(Config.level2_1[0]);
				item.setCount(Config.level2_1[1]);
				pwh.storeTradeItem(item);
				L1ItemInstance item2 = ItemTable.getInstance().createItem(Config.level2_2[0]);
				item.setCount(Config.level2_2[1]);
				pwh.storeTradeItem(item2);
			}else if(level == 3){
				vip.sendPackets(new S_SkillSound(vip.getId(), 15357));//이펙트발생
				L1ItemInstance item = ItemTable.getInstance().createItem(Config.level3_1[0]);
				item.setCount(Config.level3_1[1]);
				pwh.storeTradeItem(item);
				L1ItemInstance item2 = ItemTable.getInstance().createItem(Config.level3_2[0]);
				item.setCount(Config.level3_2[1]);
				pwh.storeTradeItem(item2);
				L1ItemInstance item3 = ItemTable.getInstance().createItem(Config.level3_3[0]);
				item.setCount(Config.level3_3[1]);
				pwh.storeTradeItem(item3);
			}
			vip.sendPackets(new S_SystemMessage("인벤토리->부가아이템 창고 에 선물이 도착 하였습니다."));
			vip.sendPackets(new S_PacketBox(S_PacketBox.RED_MESSAGE, "[부가 아이템 창고] 미수령 아이템이 있습니다."));			
		}else{
			String aname = getAccountName(buyername);
			if(aname == null){
				System.out.println("Buyer.java->getAccountName 등록 에러: "+buyername+"");
			}else{
				SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(aname);
				if(level == 1){
					L1ItemInstance item = ItemTable.getInstance().createItem(Config.level1[0]);
					item.setCount(Config.level1[1]);
					pwh.insertItem(item);
				}else if(level == 2){
					L1ItemInstance item = ItemTable.getInstance().createItem(Config.level2_1[0]);
					item.setCount(Config.level2_1[1]);
					pwh.insertItem(item);
					L1ItemInstance item2 = ItemTable.getInstance().createItem(Config.level2_2[0]);
					item.setCount(Config.level2_2[1]);
					pwh.insertItem(item2);
				}else if(level == 3){
					L1ItemInstance item = ItemTable.getInstance().createItem(Config.level3_1[0]);
					item.setCount(Config.level3_1[1]);
					pwh.insertItem(item);
					L1ItemInstance item2 = ItemTable.getInstance().createItem(Config.level3_2[0]);
					item.setCount(Config.level3_2[1]);
					pwh.insertItem(item2);
					L1ItemInstance item3 = ItemTable.getInstance().createItem(Config.level3_3[0]);
					item.setCount(Config.level3_3[1]);
					pwh.insertItem(item3);
				}
			}
		}
	}	
	
	private String getAccountName(String buyername) {
		String accountname = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select account_name from characters where char_name Like '" + buyername + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				accountname = rs.getString(1);
			}
//			System.out.println(accountname);
		} catch (Exception e) {
			System.out.println("getAccountName : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return accountname;
	}

}
