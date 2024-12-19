package l1j.server.MJTemplate.MJL1Collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJPcList extends MJAbstractPcCollection implements List<L1PcInstance>{
	private List<L1PcInstance> _list;
	protected void setList(List<L1PcInstance> list){
		_list = list;
	}
	
	@Override
	public int size(){
		return _list.size();
	}
	
	@SuppressWarnings("hiding")
	@Override
	public <L1PcInstance> L1PcInstance[] toArray(L1PcInstance[] a) {
		return _list.toArray(a);
	}
	
	@Override
	public L1PcInstance[] toArray(){
		return toArray(new L1PcInstance[_list.size()]);
	}
	
	@Override
	public boolean contains(Object o){
		return _list.contains(o);
	}
	
	@Override
	public L1PcInstance remove(int i){
		return _list.remove(i);
	}
	
	@Override
	public L1PcInstance get(int i){
		return _list.get(i);
	}
	
	@Override
	public boolean add(L1PcInstance pc){
		return _list.add(pc);
	}
	
	@Override
	public void clear(){
		if(_list != null){
			_list.clear();
		}
	}
	
	@Override
	public boolean isEmpty() {
		return _list.isEmpty();
	}

	@Override
	public Iterator<L1PcInstance> iterator() {
		return _list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return _list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return _list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends L1PcInstance> c) {
		return _list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends L1PcInstance> c) {
		return _list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return _list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return _list.retainAll(c);
	}

	@Override
	public L1PcInstance set(int index, L1PcInstance element) {
		return _list.set(index, element);
	}

	@Override
	public void add(int index, L1PcInstance element) {
		_list.add(index, element);
	}

	@Override
	public int indexOf(Object o) {
		return _list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return _list.lastIndexOf(o);
	}

	@Override
	public ListIterator<L1PcInstance> listIterator() {
		return _list.listIterator();
	}

	@Override
	public ListIterator<L1PcInstance> listIterator(int index) {
		return _list.listIterator(index);
	}

	@Override
	public List<L1PcInstance> subList(int fromIndex, int toIndex) {
		return _list.subList(fromIndex, toIndex);
	}
	
	public Iterator<L1PcInstance> toValuesIterator(){
		return _list.iterator();
	}
}
