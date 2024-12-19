package l1j.server.MJTemplate.MJL1Collection.ToCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import l1j.server.MJTemplate.MJL1Collection.MJPcList;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class PcList2D<T extends MJPcList> extends AbstractPcCollectionToCollection<T> implements List<T>{
	
	protected List<T> _list;
	protected void set_list(List<T> list){
		_list = list;
	}
	
	@Override
	public int size() {
		return _list.size();
	}

	@Override
	public boolean isEmpty() {
		return _list == null || _list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return _list.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return _list.iterator();
	}

	@Override
	public Object[] toArray() {
		return _list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return _list.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return _list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return _list.remove(o);
	}
	
	public void remove(L1PcInstance pc){
		for(MJPcList t : _list){
			if(t == null)
				continue;
			
			if(t.remove(pc))
				break;
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return _list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return _list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
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
	public void clear() {
		_list.clear();
	}

	@Override
	public T get(int index) {
		return _list.get(index);
	}

	@Override
	public T set(int index, T element) {
		return _list.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		_list.add(index, element);
	}

	@Override
	public T remove(int index) {
		return _list.remove(index);
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
	public ListIterator<T> listIterator() {
		return _list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return _list.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return _list.subList(fromIndex, toIndex);
	}

	@Override
	public Iterator<T> to_values_iterator() {
		return _list.iterator();
	}
	
	public int to_total_size(){
		int size = 0;
		for(MJPcList list : _list){
			if(list != null)
				size += list.size();
		}
		return size;
	}
}
