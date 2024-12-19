package l1j.server.MJNetServer.ClientManager;
import java.net.InetAddress;
import java.util.HashMap;

import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJNetServer.Util.MJDataParser;

/**********************************
 * 
 * MJ Network Server System Client Counter.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSClientCounter {
	private static MJNSClientCounter _instance;
	public static MJNSClientCounter getInstance(){
		if(_instance == null)
			_instance = new MJNSClientCounter();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJNSClientCounter tmp = _instance;
		_instance = new MJNSClientCounter();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<Integer, MJNSCounter> _counters;
	private MJNSClientCounter(){
		_counters = new HashMap<Integer, MJNSCounter>(MJNetServerLoadManager.NETWORK_BACK_LOG);
	}
	
	public int isPermission(InetAddress addr){
		int		nAddr	= MJDataParser.parseInt(addr);
		synchronized(_counters){
			MJNSCounter itg	= _counters.get(nAddr);
			if(itg == null){
				itg = new MJNSCounter();
				_counters.put(nAddr, itg);
			}
			
			return itg.val >= MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION ? -1 : itg.inc();
		}
	}
	
	public void decrese(InetAddress addr){
		if(_counters == null)
			return;
		
		int		nAddr	= MJDataParser.parseInt(addr);
		synchronized(_counters){
			MJNSCounter itg		= _counters.get(nAddr);
			if(itg == null)
				return;
			
			if(itg.dec() <= 0)
				_counters.remove(nAddr);
		}
	}
	
	public void clear(){
		if(_counters != null){
			_counters.clear();
		}
	}
	
	class MJNSCounter {
		public int val;
		
		int inc(){
			return ++val;
		}
		
		int dec(){
			return --val;
		}
	}
}
