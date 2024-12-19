package l1j.server.server;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.datatables.NpcShopSpawnTable2;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.templates.L1ShopItem;

// npc shop add
/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
public class NpcShopSystem2 implements Runnable {

	private static NpcShopSystem2 _instance;

	private boolean _power = false;

	// private int Count = 0;
	// private final int Time = 2;

	public static NpcShopSystem2 getInstance() {
		if (_instance == null) {
			_instance = new NpcShopSystem2();
		}
		return _instance;
	}

	private boolean isReload = false;
	private static ArrayList<L1NpcInstance> _shops = new ArrayList<L1NpcInstance>(20);
	
	@Override
	public void run(){
		try{
			if(isPower()){
				NpcShopTable2.reloding();
				int size = _shops.size();
				for(int i=0; i<size; i++)
					shopRefill(_shops.get(i));
				
				MJDShopStorage.updateProcess(_shops);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	@Override
	public void run() {

		// 1시간마다 shop 리로드
		String currentTime = "";
		while (true) {
			try {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.KOREA);
				currentTime = sdf.format(c.getTime());
				if (!isReload) {
					if (isOwnHour(currentTime)) {
						// 리로드
						if (isPower()) {
							NpcShopTable.reloding();
							isReload = true;
						}
					} else {
						isReload = false;
					}
				} else {
					isReload = false;
				}
				Thread.sleep(1000L); //
			} catch (Exception e) {
			}
		}
	}*/

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	private void shopRefill(L1NpcInstance npc){
		try{
			L1Shop shop = NpcShopTable2.getInstance().get(npc.getNpcId());
			if(shop == null)
				return;
			
			List<L1ShopItem> list = shop.getSellingItems();
			if(list == null)
				return;
			
			int size = list.size();
			MJDShopItem ditem 	= null;
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, false);
				npc.addSellings(ditem);
			}
			
			list = shop.getBuyingItems();
			if(list == null)
				return;
			
			size = list.size();
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, true);
				npc.addPurchasings(ditem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/

	private boolean isOwnHour(String time) {
		// System.out.println("시간 : " + time);
		if (time == null || time.equals(""))
			return false;
		return time.equals("0000") || time.equals("0100") || time.equals("0200") || time.equals("0300")
				|| time.equals("0400") || time.equals("0500") || time.equals("0600") || time.equals("0700")
				|| time.equals("0800") || time.equals("0900") || time.equals("1000") || time.equals("1100")
				|| time.equals("1200") || time.equals("1300") || time.equals("1400") || time.equals("1500")
				|| time.equals("1600") || time.equals("1700") || time.equals("1800") || time.equals("1900")
				|| time.equals("2000") || time.equals("2100") || time.equals("2200") || time.equals("2300");
	}

	static class NpcShopTimer implements Runnable {

		public NpcShopTimer() {
		}

		public void run() {
			try {
				ArrayList<L1NpcShop> list = NpcShopSpawnTable2.getInstance().getList();
				for (int i = 0; i < list.size(); i++) {
					L1NpcShop shop = list.get(i);
					L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
					npc.setId(IdFactory.getInstance().nextId());
					npc.setMap(shop.getMapId());
					npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
					npc.getLocation().forward(5);
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.setHeading(shop.getHeading());
					npc.setName(shop.getName());
					npc.setTitle(shop.getTitle());
					L1NpcShopInstance obj = (L1NpcShopInstance) npc;
					obj.setShopName(shop.getShopName());
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					npc.getLight().turnOnOffLight();
					Thread.sleep(30);
					obj.setState(1);
					Broadcaster.broadcastPacket(npc, new S_DoActionShop(npc.getId(), ActionCodes.ACTION_Shop, shop.getShopName().getBytes("MS949")));
					_shops.add(npc);
					Thread.sleep(10);
				}
				// list.clear();

			} catch (Exception exception) {
				return;
			}finally{
				GeneralThreadPool.getInstance().scheduleAtFixedRate(NpcShopSystem2.getInstance(), 0, 3600000);
			}
		}
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	public void npcShopStart() {
		//NpcShopTable.reloding();
		NpcShopTimer ns = new NpcShopTimer();
		GeneralThreadPool.getInstance().execute(ns);
		_power = true;
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	public void npcShopStop() {
		_power = false;
		int size = _shops.size();
		for(int i=0; i<size; i++){
			L1NpcInstance npc = _shops.get(i);
			if(npc == null)
				continue;
			
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(npc, true));
			npc.deleteMe();
		}
	}

	public boolean isPower() {
		return _power;
	}
}
