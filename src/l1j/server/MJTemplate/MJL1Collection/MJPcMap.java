package l1j.server.MJTemplate.MJL1Collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJPcMap extends MJAbstractPcCollection implements Map<Integer, L1PcInstance>{
	private Map<Integer, L1PcInstance> _map;

	protected void setMap(Map<Integer, L1PcInstance> map){
		_map = map;
	}
	
	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}

	@Override
	public boolean containsKey(Object key) {
		if(key instanceof Integer)
			return containsKey((Integer)key);
		return false;
	}
	
	public boolean containsKey(Integer key) {
		return _map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		if(value instanceof L1PcInstance)
			return containsValue((L1PcInstance)value);
		return false;
	}
	
	public boolean containsValue(L1PcInstance value) {
		return _map.containsKey(value);
	}	

	@Override
	public L1PcInstance get(Object key) {
		if(key instanceof Integer)
			return get((Integer)key);
		return null;
	}
	
	public L1PcInstance get(Integer key){
		return _map.get(key);
	}

	@Override
	public L1PcInstance put(Integer key, L1PcInstance value) {
		return _map.put(key, value);
	}
	
	public void add(L1PcInstance pc){
		put(pc.getId(), pc);
	}

	@Override
	public L1PcInstance remove(Object key) {
		if(key instanceof Integer)
			return remove((Integer)key);
		return null;
	}
	
	public L1PcInstance remove(Integer key){
		return _map.remove(key);
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends L1PcInstance> m) {
		_map.putAll(m);
	}

	@Override
	public void clear() {
		_map.clear();
	}

	@Override
	public Set<Integer> keySet() {
		return _map.keySet();
	}

	@Override
	public Collection<L1PcInstance> values() {
		return _map.values();
	}

	@Override
	public Set<java.util.Map.Entry<Integer, L1PcInstance>> entrySet() {
		return _map.entrySet();
	}
	
	public Iterator<L1PcInstance> toValuesIterator(){
		return _map.values().iterator();
	}
}
