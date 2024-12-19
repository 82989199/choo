package l1j.server.MJCharacterActionSystem;

import static l1j.server.server.model.skill.L1SkillId.FIRE_WALL;
import static l1j.server.server.model.skill.L1SkillId.LIFE_STREAM;
import static l1j.server.server.model.skill.L1SkillId.MASS_TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.SUMMON_MONSTER;
import static l1j.server.server.model.skill.L1SkillId.TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.TRUE_TARGET;

import java.util.HashMap;

import l1j.server.MJCharacterActionSystem.Spell.BackgroundActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.DirectionActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.SpellActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.SummonMonsterActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.TeleportActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.TruetargetActionHandler;

public class SpellActionHandlerFactory {
	private static final HashMap<Integer, SpellActionHandler> _handlers;
	static{
		_handlers = new HashMap<Integer, SpellActionHandler>(256);
		for(int i=256; i>0; --i){
			switch(i){
			case TRUE_TARGET:
				_handlers.put(i, new TruetargetActionHandler());
				break;
			case TELEPORT: case MASS_TELEPORT:
				_handlers.put(i, new TeleportActionHandler());
				break;
			case SUMMON_MONSTER:
				_handlers.put(i, new SummonMonsterActionHandler());
				break;
			case FIRE_WALL: case LIFE_STREAM:
				_handlers.put(i, new BackgroundActionHandler());
				break;
			default:
				_handlers.put(i, new DirectionActionHandler());
				break;
			}
		}
	}
	
	public static SpellActionHandler create(int skillId){
		SpellActionHandler default_handler = _handlers.get(skillId);
		if(default_handler == null)
			return null;
		
		return default_handler.copy().setSkillId(skillId);
	}
}
