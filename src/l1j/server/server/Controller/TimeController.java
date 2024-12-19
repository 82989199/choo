package l1j.server.server.Controller;

public class TimeController implements Runnable {
		
	private static TimeController _instance;
	
	public static final int SLEEP_TIME = 1000;
		
	public static TimeController getInstance() {
		if(_instance == null) _instance = new TimeController();
		return _instance;
	}
	
	@Override
	public void run() {		
		try{
			BuyerController.toTimer(System.currentTimeMillis());
		} catch(Exception e){
			e.printStackTrace();
		}
	}	
}
