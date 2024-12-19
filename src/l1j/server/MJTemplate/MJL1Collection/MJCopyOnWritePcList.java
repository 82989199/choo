package l1j.server.MJTemplate.MJL1Collection;

import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.model.Instance.L1PcInstance;

public class MJCopyOnWritePcList extends MJPcList{
	public MJCopyOnWritePcList(){
		setList(new CopyOnWriteArrayList<L1PcInstance>());
	}
}
