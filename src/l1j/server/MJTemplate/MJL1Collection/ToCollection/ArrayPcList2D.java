package l1j.server.MJTemplate.MJL1Collection.ToCollection;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJL1Collection.MJPcList;

public class ArrayPcList2D extends PcList2D<MJPcList>{
	public static ArrayPcList2D newInstance(int capacity){
		return new ArrayPcList2D(capacity);		
	}
	
	public static ArrayPcList2D newInstance(){
		return new ArrayPcList2D();
	}
	
	protected ArrayPcList2D(){
		set_list(new ArrayList<MJPcList>());
	}
	
	protected ArrayPcList2D(int capacity){
		set_list(new ArrayList<MJPcList>(capacity));
	}
}
