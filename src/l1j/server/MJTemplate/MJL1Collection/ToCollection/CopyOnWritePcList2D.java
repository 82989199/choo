package l1j.server.MJTemplate.MJL1Collection.ToCollection;

import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.MJTemplate.MJL1Collection.MJPcList;

public class CopyOnWritePcList2D extends PcList2D<MJPcList>{
	public static CopyOnWritePcList2D newInstance(){
		return new CopyOnWritePcList2D();
	}
	
	protected CopyOnWritePcList2D(){
		set_list(new CopyOnWriteArrayList<MJPcList>());
	}
}
