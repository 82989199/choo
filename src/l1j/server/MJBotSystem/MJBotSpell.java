package l1j.server.MJBotSystem;

/**********************************
 * 
 * MJ Bot Spell structure.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotSpell {
	/** spt is spell type. **/
	public static final int SPT_NONE 	= 1;
	public static final int SPT_USER 	= 2;
	public static final int SPT_MOB 	= 4;
	public static final int SPT_ALWAYS	= SPT_USER | SPT_MOB;
	
	public int 		skillId;
	public int 		dice;
	public int 		delay;
	public int		direction;
	public int		spellTarget;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(256);
		sb.append("[skillId : ").append(skillId).append("]\n");
		sb.append("[dice : ").append(dice).append("]\n");
		sb.append("[delay : ").append(delay).append("]\n");
		sb.append("[direction : ").append(direction).append("]\n");
		sb.append("[spellTarget : ").append(spellTarget).append("]");
		return sb.toString();
	}
}
