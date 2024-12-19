package kr.cholong.PushItemSystem.Templates;

public class PushItem {
	private int _itemid;
	private int _count;
	private boolean _is_iven;;
	
	public int getItemId(){
		return _itemid;
	}
	public void setItemId(int i){
		_itemid = i;
	}
	
	public int getItemCount(){
		return _count;
	}
	public void setItemCount(int i){
		_count = i;
	}
	
	public boolean isIventory(){
		return _is_iven;
	}
	public void setIventory(boolean flag){
		_is_iven = flag;
	}
}
