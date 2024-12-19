package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.utils.MJCommons;

public abstract class MJRaidBossSkill{
	// bsr is boss skill range.
	public static final int BSRTYPE_ONCE	= 1;	// 한 대상만을
	public static final int BSRTYPE_LINE	= 2;	// 한 라인을
	public static final int BSRTYPE_FRONT	= 4;	// 전방
	public static final int BSRTYPE_RECT	= 8;	// 정 사각형
	public static final int BSRTYPE_SCREEN	= 16;	// 화면 내 전체.
	
	protected static Random	_rnd = new Random(System.nanoTime());
	protected int			_actid; 
	protected int			_type;
	protected int			_range;
	protected int			_dmgMin;
	protected int			_dmgMax;
	protected int			_effectId;
	protected int			_ratio;
	protected boolean		_isMagic;

	public void setActionId(int actid){
		_actid 	= actid;
	}
	public void setType(int type){
		_type	= type;
	}
	public void setRange(int range){
		_range 	= range;
	}
	public void setDamageMin(int dmg){
		_dmgMin = dmg;
	}
	public void setDamageMax(int dmg){
		_dmgMax = dmg;
	}
	public void setEffectId(int effectId){
		_effectId = effectId;
	}
	public void setRatio(int raito){
		_ratio = raito;
	}
	public void setIsMagic(boolean b){
		_isMagic = b;
	}
	
	public void sleepAction(L1NpcInstance own, int actionId) throws Exception{
		Thread.sleep(own.getCurrentSpriteInterval(EActionCodes.fromInt(actionId)));
	}
	
	public void sleep(long l) throws Exception{
		Thread.sleep(l);
	}
	public abstract void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs);
	public abstract void actionNoDelay(L1NpcInstance own, ArrayList<L1PcInstance> pcs);
	
	protected boolean isRange(L1NpcInstance own, L1PcInstance pc){
		if(own.getMapId() != pc.getMapId())
			return false;
		
		if((_type & BSRTYPE_ONCE) > 0)
			return isDistance(own.getX(), own.getY(), pc.getX(), pc.getY());
		else if((_type & BSRTYPE_LINE) > 0)
			return isLine(own, pc);
		else if((_type & BSRTYPE_FRONT) > 0)
			return isInFront(own, pc);
		else if((_type & BSRTYPE_RECT) > 0){
			int div = _range / 2;
			return isInRectangle(own.getX() - div, own.getY() - div, own.getX() + div, own.getY() + div, pc.getLocation());
		}else if((_type & BSRTYPE_SCREEN) > 0)
			return isInScreen(own.getLocation(), pc.getLocation());
		else
			return false;
	}
	
	protected boolean isInRectangle(int left, int top, int right, int bottom, L1Location tLoc){
		int x 	= tLoc.getX();
		int y	= tLoc.getY();
		return (left <= x && top <= y && right >= x && bottom >= y); 
	}
	
	protected boolean isInFront(L1Character own, L1Character tar){
		if(!MJCommons.isWideAroundDirection(own, tar))
			return false;
		return isDistance(own.getX(), own.getY(), tar.getX(), tar.getY());
	}
	
	protected boolean isInScreen(L1Location oloc, L1Location tloc){
		return oloc.isInScreen(tloc);
	}
	
	protected boolean isLine(L1Character own, L1Character tar){
		return own.glanceCheck(tar.getX(), tar.getY());
	}
	
	protected boolean isDistance(int ox, int oy, int tx, int ty){
		return MJCommons.getDistance(ox, oy, tx, ty) <= _range;
	}
	
	protected int calcDamageForValakas(L1Character attacker, L1PcInstance pc){
		if(pc.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM) && _rnd.nextInt(100) <= MJRaidLoadManager.MRS_BS_VALA_IMMUNEBLADE){
			S_OnlyEffect s_oe = new S_OnlyEffect(pc.getId(), 15961);
			pc.sendPackets(s_oe, false);
			Broadcaster.broadcastPacket(pc, s_oe, true);
			pc.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
		}
		
		double imm = pc.getImmuneReduction();
		int min = _dmgMin;
		int max = _dmgMax;
		if(imm != 0){
			min -= (int)((double)min * imm);
			max -= (int)((double)max * imm);
		}

		int dmg = 0;
		if(_isMagic){
			dmg = MJCommons.getMagicDamage(attacker, pc, min, max);
			if(pc.hasSkillEffect(L1SkillId.FAFU_MAAN) || pc.hasSkillEffect(L1SkillId.LIFE_MAAN) || 
					pc.hasSkillEffect(L1SkillId.SHAPE_MAAN) || pc.hasSkillEffect(L1SkillId.BIRTH_MAAN)){
				int MaanMagicCri = _rnd.nextInt(100) + 1;
			    if (MaanMagicCri <= 35)
			    	dmg /= 2;
			}
		}else{
			dmg = _rnd.nextInt(max - min) + min;
		}
		
		dmg -= pc.getDamageReductionByArmor();

		if (pc.hasSkillEffect(L1SkillId.REDUCTION_ARMOR)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl < 50) 
				targetPcLvl = 50;
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		
		if (pc.hasSkillEffect(L1SkillId.MAJESTY)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl < 80)
				targetPcLvl = 80;
			dmg -= (targetPcLvl - 80) / 2 + 1;
		}

		if (pc.hasSkillEffect(L1SkillId.IllUSION_AVATAR))
			dmg += dmg / 5;
		
		if (pc.hasSkillEffect(L1SkillId.PATIENCE)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl >= 80) {
				dmg -= 2 + ((targetPcLvl - 80) / 4);
			} else { 
				dmg -= 2;
			}
		}
		
		if (pc.hasSkillEffect(L1SkillId.DRAGON_SKIN)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl >= 80) {
				dmg -= 5 + ((targetPcLvl - 80) / 2);
			} else {
				dmg -= 5;
			}
		}
		
		

		return dmg;
	}
	
	protected int calcDamage(L1Character attacker, L1PcInstance pc){
		double imm = pc.getImmuneReduction();
		int min = _dmgMin;
		int max = _dmgMax;
		if(imm != 0){
			min -= (int)((double)min * imm);
			max -= (int)((double)max * imm);
		}

		int dmg = 0;
		if(_isMagic){
			dmg = MJCommons.getMagicDamage(attacker, pc, min, max);
			if(pc.hasSkillEffect(L1SkillId.FAFU_MAAN) || pc.hasSkillEffect(L1SkillId.LIFE_MAAN) || 
					pc.hasSkillEffect(L1SkillId.SHAPE_MAAN) || pc.hasSkillEffect(L1SkillId.BIRTH_MAAN)){
				int MaanMagicCri = _rnd.nextInt(100) + 1;
			    if (MaanMagicCri <= 35)
			    	dmg /= 2;
			}
		}else{
			dmg = _rnd.nextInt(max - min) + min;
		}
		
		dmg -= pc.getDamageReductionByArmor();

		if (pc.hasSkillEffect(L1SkillId.REDUCTION_ARMOR)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl < 50) 
				targetPcLvl = 50;
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}

		if (pc.hasSkillEffect(L1SkillId.IllUSION_AVATAR))
			dmg += dmg / 5;
		
		if (pc.hasSkillEffect(L1SkillId.PATIENCE)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl >= 80) {
				dmg -= 2 + ((targetPcLvl - 80) / 4);
			} else { 
				dmg -= 2;
			}
		}
		
		if (pc.hasSkillEffect(L1SkillId.DRAGON_SKIN)) {
			int targetPcLvl = pc.getLevel();
			if (targetPcLvl >= 80) {
				dmg -= 5 + ((targetPcLvl - 80) / 2);
		} else {
			dmg -= 5;
		}
		}
		
		return dmg;
	}
	
	protected boolean isSkillSet(L1NpcInstance own, L1PcInstance pc){
		if(pc == null || pc.isDead() || !isRange(own, pc))
			return false;
		
		if(MJCommons.isUnbeatable(pc))
			return false;
		
		if(_isMagic){
			if(MJCommons.isCounterMagic(pc))
				return false;
			
			if(!MJCommons.isMagicSuccess(own, pc, _ratio))
				return false;
		}
		
		return true;
	}
	
	public boolean isAbsoluteBarrier(L1PcInstance pc){
		return pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
	}
	
	public boolean setAbsoluteBarrierDestroy(L1PcInstance pc){
		if(_rnd.nextInt(100) <= MJRaidLoadManager.MRS_BS_VALA_ABSOLUTEBLADE){
			pc.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
			S_OnlyEffect s_oe = new S_OnlyEffect(pc.getId(), 15835);
			pc.sendPackets(s_oe, false);
			Broadcaster.broadcastPacket(pc, s_oe, true);
			return true;
		}
		return false;
	}
}

