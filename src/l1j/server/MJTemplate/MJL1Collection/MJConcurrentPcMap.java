package l1j.server.MJTemplate.MJL1Collection;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

public class MJConcurrentPcMap extends MJPcMap{
	public MJConcurrentPcMap(int capacity){
		setMap(new ConcurrentHashMap<Integer, L1PcInstance>(capacity));
	}
}
