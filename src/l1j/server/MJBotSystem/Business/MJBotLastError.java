package l1j.server.MJBotSystem.Business;

import l1j.server.MJBotSystem.AI.MJBotAI;

public class MJBotLastError {
	public MJBotAI 	ai;
	public String 	message;
	
	public MJBotLastError(){
		ai 		= null;
		message = null;
	}
	
	public MJBotLastError(MJBotAI ai, String message){
		this.ai 		= ai;
		this.message 	= message;
	}
}
