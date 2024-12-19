package l1j.server.MJINNSystem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import l1j.server.MJTemplate.ObServer.MJAbstractCopyMapObserver;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJINNRoom extends MJAbstractCopyMapObserver implements Runnable{
	private static ConcurrentHashMap<Integer, MJINNRoom> _keys = new ConcurrentHashMap<Integer, MJINNRoom>(256);
	public static MJINNRoom create(MJINNMapInfo mapInfo, Integer[] keyIds){
		MJINNRoom room 	= new MJINNRoom();
		room._mapInfo 	= mapInfo;
		room._usageCount = keyIds.length;
		room._keyIds	= keyIds;
		MJCopyMapObservable.getInstance().register(mapInfo.mapId, room);
		for(Integer i : keyIds)
			_keys.put(i, room);
		room.on();
		return room;
	}
	
	public static boolean input(L1ItemInstance item, L1PcInstance pc){
		MJINNRoom room = _keys.get(item.getId());
		if(room != null){
			room.in(pc);
			return true;
		}
		pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, true, false, false);
		return false;
	}
	
	public static boolean input(L1ItemInstance[] items, L1PcInstance pc){
		for(L1ItemInstance item : items){
			MJINNRoom room = _keys.get(item.getId());
			if(room == null)
				continue;
			
			room.in(pc);
			return true;
		}
		return false;
	}
	
	public static boolean input(L1PcInstance pc, int innId){
		L1ItemInstance[] items = pc.getInventory().findItemsId(MJINNHelper.INN_KEYID);
		if(items == null || items.length <= 0)
			return false;
		
		for(L1ItemInstance item : items){
			MJINNRoom room = _keys.get(item.getId());
			if(room == null)
				continue;
			
			if(room._mapInfo.innId == innId){
				room.in(pc);
				return true;
			}
		}
		return false;
		
	}
	
	public static boolean input(L1PcInstance pc){
		L1ItemInstance[] items = pc.getInventory().findItemsId(MJINNHelper.INN_KEYID);
		if(items == null || items.length <= 0)
			return false;
		return input(items, pc);
	}
	
	public static void remove(L1ItemInstance item){
		MJINNRoom room = _keys.remove(item.getId());
		if(room == null)
			return;
		
		if(--room._usageCount <= 0)
			room.off();
	}
	
	public static void remove(L1ItemInstance[] items){
		for(L1ItemInstance item : items)
			remove(item);
	}
	
	private MJINNMapInfo 		_mapInfo;
	private int							_usageCount;
	private Integer[] 				_keyIds;
	private ScheduledFuture<?>	_future;
	
	private void on(){
		_future = GeneralThreadPool.getInstance().schedule(this, MJINNHelper.RENTAL_MINUTE * 60L * 1000L);
	}
	
	private void off(){
		if(_future != null){
			try{ _future.cancel(true); }catch(Exception e){}
			_future = null;
			notifyObservable();
		}
	}
	
	@Override
	public void run() {
		if(_isDispose)
			return;
		_future = null;
		notifyObservable();
	}
	
	@Override
	public void notifyObservable(){
		for(Integer i : _keyIds)
			_keys.remove(i);
		_mapInfo.usageCount.decrementAndGet();
		super.notifyObservable();
	}
	
	private void in(L1PcInstance pc){
		pc.start_teleport(_mapInfo.x, _mapInfo.y, _copyMapId, pc.getHeading(), 169, true, false, false);
	}
}
