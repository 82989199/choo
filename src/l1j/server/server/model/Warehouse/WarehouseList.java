package l1j.server.server.model.Warehouse;

import java.util.Vector;

public abstract class WarehouseList {
	private Vector<Warehouse> warehouseList; 
	
	public WarehouseList() {
		warehouseList = new Vector<Warehouse>();
	}
	
	protected abstract Warehouse createWarehouse(String name);

	public synchronized Warehouse findWarehouse(String name) {
		Warehouse warehouse = null;
		for(Warehouse wh : warehouseList) {			
			if(wh.getName() == name) {
				return wh;
			}
		}
		warehouse = createWarehouse(name);
		warehouseList.add(warehouse);
		return warehouse;
	}	
	public synchronized void delWarehouse(String accountName) {
		Warehouse iwilldie = null;
		for(Warehouse wh : warehouseList) {
			if(wh.getName() == accountName && wh != null) {
				iwilldie = wh;
			}
		}
		if(iwilldie != null) {
			iwilldie.clearItems();
			warehouseList.remove(iwilldie);
			iwilldie = null;
		}
	}
	
	public synchronized Warehouse findWarehouse1(String name) {
		Warehouse warehouse = null;
		for(Warehouse wh : warehouseList) {		
			if(wh.getName().equals(name)) {
				return wh;
			}
		}
		warehouse = createWarehouse(name);
		warehouseList.add(warehouse);
		return warehouse;
	}
	public synchronized void delWarehouse1(String accountName) {
		Warehouse iwilldie = null;
		for(Warehouse wh : warehouseList) {
			if(wh.getName().equals(accountName)  && wh != null) {
				iwilldie = wh;
			}
		}
		if(iwilldie != null) {
			iwilldie.clearItems();
			warehouseList.remove(iwilldie);
			iwilldie = null;
		}
	}
}
