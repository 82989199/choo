package l1j.server.MJBotSystem;

import java.util.HashMap;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.server.model.L1Character;

public class MJBotMent {
	// BMS is Bot Message Status
	public static final int BMS_DIE 		= 0;
	public static final int BMS_KILL 		= 1;
	public static final int BMS_ONTARGET	= 2;
	public static final int BMS_ONPERCEIVE	= 3;
	public static final int BMS_ONDAMAGE	= 4;
	public static final int BMS_IDLE		= 5;
	
	// BMT is Bot Message Type
	public static final int BMT_NORMAL		= 1;
	public static final int BMT_WORLD		= 2;
	public static final int BMT_WHISPER		= 4;
	
	public static final HashMap<Integer, String> _additions = new HashMap<Integer, String>();
	static{
		_additions.put(2, "ㅋ");
		_additions.put(4, "ㅎ");
		_additions.put(8, "ㅊ");
		_additions.put(16, "ㅂ");
		_additions.put(32, "ㅌ");
	}
	
	public static final int BMP_NONE 		= 1;
	public static final int BMP_CHARNAME 	= 2;
	
	public String 	message;
	public int		status;
	public int		type;
	public int		addition;
	public int		parameter;
	
	public String toString(MJBotAI ai, L1Character target){
		StringBuilder sb = new StringBuilder(message.length() + 32);
		sb.append(message);
		String s = _additions.get(addition);
		if(s != null){
			int size = ai.getBrain().toRand(ai.getBrain().getPride() / 10);
			for(int i=0; i<size; i++)
				sb.append(s);
		}
		
		if((parameter & BMP_CHARNAME) > 0)
			return String.format(sb.toString(), target.getName());
		return sb.toString();
	}
}
