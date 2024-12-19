package l1j.server.MJEffectSystem.Realize;

import static l1j.server.server.model.skill.L1SkillId.PATIENCE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import l1j.server.MJEffectSystem.MJEffectModel;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;

public class OnEffectThread implements Runnable{
	protected static Random _rnd = new Random(System.nanoTime());
	protected static final Object 						_lock = new Object();
	protected static HashMap<Integer, L1NpcInstance> 	_maps = new HashMap<Integer, L1NpcInstance>();
	
	protected static final int[][] _radiation_matrix = new int[][]{
		{0, -1},
		{1, -1},
		{1, 0},
		{1, 1},
		{0, 1},
		{-1, 1},
		{-1, 0},
		{-1, -1},
	};
	
	protected static final int[][] _headingTable = new int[][]{
		{7, 0, 1},
		{6, -1, 2},
		{5, 4, 3},
	};
	protected static int calcHeadingIdx(int i){
		if(i > 0) 			return 2;
		else if(i == 0 ) 	return 1;
		else 				return 0;
	}
	protected static int calcHeading(L1Character o, int x, int y) {
		return calcHeading(o.getX(), o.getY(), x, y);
	}
	protected static int calcHeading(L1Character c1, L1Character c2){
		return calcHeading(c1.getX(), c1.getY(), c2.getX(), c2.getY());
	}
	protected static int calcHeading(int x1, int y1, int x2, int y2){
		int calcX = x1 - x2;
		int calcY = y1 - y2;
		
		return _headingTable[calcHeadingIdx(calcX)][calcHeadingIdx(calcY)];		
	}
	
	protected MJEffectModel 	_model;
	protected L1Character		_owner;
	
	public OnEffectThread(MJEffectModel model, L1Character owner){
		_model	= model;
		_owner 	= owner;
	}

	@Override
	public void run() {
		if(_model == null || _owner == null || _owner.isDead())
			return;
		
		synchronized(_lock){
			L1NpcInstance npc = _maps.get(_owner.getId());
			if(npc != null)
				return;
			
			_maps.put(_owner.getId(), (L1NpcInstance) _owner);
		}
		
		try{
			Broadcaster.broadcastPacket(_owner, new S_DoActionGFX(_owner.getId(), _model.getActionId()));
			ArrayList<L1PcInstance> pcs = L1World.getInstance().getVisiblePlayer(_owner, _model.getRange());
			int pcsize					= pcs.size();
			int zPos	 				= _model.getZPos();
			int maxRange 				= _model.getRange();
			int cx						= 0;
			int cy						= 0;
			int left					= 0;
			int top						= 0;
			int right					= 0;
			int bottom					= 0;
			int impact					= _model.getImpact();
			int eff						= _model.getEffectId();
			int[][] cpos				= new int[4][2];
			S_EffectLocation pck		= null;
			L1PcInstance pc				= null;
			for(int i = maxRange; i > 0; i-= zPos){
				left 	= _owner.getX() - i;
				top		= _owner.getY() - i;
				right	= _owner.getX() + i;
				bottom	= _owner.getY() + i;
				
				for(int d = 0; d<i*2; d+=impact){
					cpos[0][0] = left + d;
					cpos[0][1] = top;
					
					cpos[1][0] = right;
					cpos[1][1] = top + d;
					
					cpos[2][0] = right - d;
					cpos[2][1] = bottom;
					
					cpos[3][0] = left;
					cpos[3][1] = bottom - d;
					
					for(int j=0; j<4; j++){
						pck = new S_EffectLocation(cpos[j][0], cpos[j][1], eff);
						for(int p=0; p<pcsize; p++){
							pc = pcs.get(p);
							if(pc == null || pc.getMapId() != _owner.getMapId())
								continue;
							
							pc.sendPackets(pck, false);
							if(pc.isDead() || isUnBeatable(pc) || isCounterMagic(pc))
								continue;
							
							cx = Math.abs(pc.getX() - cpos[j][0]);
							cy = Math.abs(pc.getY() - cpos[j][1]);
							if(cx <= impact && cy <= impact){
								S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
								pc.sendPackets(s_gfx, false);
								Broadcaster.broadcastPacket(pc, s_gfx, true);
								pc.receiveDamage(_owner, calcDamage(_owner, pc, _model.getDamageMin(), _model.getDamageMax()));
							}
						}
					}
				}
				Thread.sleep(_model.getDelay());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		synchronized(_lock){
			_maps.put(_owner.getId(), null);
		}
	}
	
	protected static boolean isCounterMagic(L1PcInstance pc) {
		if (pc.hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
			pc.removeSkillEffect(L1SkillId.COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(L1SkillId.COUNTER_MAGIC).getCastGfx();
			ServerBasePacket pck = new S_OnlyEffect(pc.getId(), castgfx);
			pc.sendPackets(pck, false);
			Broadcaster.broadcastPacket(pc, pck);
			return true;
		}
		return false;
	}
	
	protected static boolean isUnBeatable(L1PcInstance c){
		return c.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER) || 
				c.hasSkillEffect(L1SkillId.ICE_LANCE) || 
				c.hasSkillEffect(L1SkillId.EARTH_BIND);
	}
	
	protected static int calcDamage(L1Character attacker, L1PcInstance pc, int dmgMin, int dmgMax){
		double imm = pc.getImmuneReduction();
		int min = dmgMin;
		int max = dmgMax;
		if(imm != 0){
			min -= (int)((double)min * imm);
			max -= (int)((double)max * imm);
		}

		int dmg = 0;
		dmg = MJCommons.getMagicDamage(attacker, pc, min, max);
		if(pc.hasSkillEffect(L1SkillId.FAFU_MAAN) || pc.hasSkillEffect(L1SkillId.LIFE_MAAN) || 
				pc.hasSkillEffect(L1SkillId.SHAPE_MAAN) || pc.hasSkillEffect(L1SkillId.BIRTH_MAAN)){
			int MaanMagicCri = _rnd.nextInt(100) + 1;
		    if (MaanMagicCri <= 35)
		    	dmg /= 2;
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
	
	protected class SimplePoint{
		int x;
		int y;
		
		SimplePoint(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	protected class SimpleRectangle{
		int left;
		int top;
		int right;
		int bottom;
	}
}
