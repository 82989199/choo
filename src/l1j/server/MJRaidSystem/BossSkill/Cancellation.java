package l1j.server.MJRaidSystem.BossSkill;

import static l1j.server.server.model.skill.L1SkillId.COOKING_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.COOKING_END;
import static l1j.server.server.model.skill.L1SkillId.SKILLS_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.SKILLS_END;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CANCLEEND;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.util.ArrayList;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_Sound;

public class Cancellation extends MJRaidBossSkill{
	@Override
	public String toString(){
		return Cancellation.class.getName();
	}
	
	@Override
	public void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0 || own.isParalyzed())
			return;

		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			synchronized(own.synchObject){
				own.setParalyzed(true);
				if(_actid >= 0)
					Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
				
				if((_type & BSRTYPE_ONCE) > 0){
					pc = pcs.get(_rnd.nextInt(size));
					if(isSkillSet(own, pc))
						processAction(own, pc);
				}else{
					for(int i=0; i<size; i++){
						pc = pcs.get(i);
						if(!isSkillSet(own, pc))
							continue;

						processAction(own, pc);
					}
				}
				sleepAction(own, _actid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			own.setParalyzed(false);			
		}
	}
	
	@Override
	public void actionNoDelay(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0)
			return;
		
		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			if(_actid >= 0)
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
			
			if((_type & BSRTYPE_ONCE) > 0){
				pc = pcs.get(_rnd.nextInt(size));
				if(isSkillSet(own, pc))
					processAction(own, pc);
			}else{
				for(int i=0; i<size; i++){
					pc = pcs.get(i);
					
					if(!isSkillSet(own, pc))
						continue;
					
					processAction(own, pc);
				}
			}
			sleepAction(own, _actid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void processAction(L1NpcInstance own, L1PcInstance pc){
		if(_effectId >= 0){
			S_OnlyEffect oe = new S_OnlyEffect(pc.getId(), _effectId);
			pc.sendPackets(oe, false);
			Broadcaster.broadcastPacket(pc, oe, true);
		}
		
		for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
			if (L1SkillUse.isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.removeSkillEffect(skillNum);
		}
	
		for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
			if (L1SkillUse.isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.removeSkillEffect(skillNum);
		}

		pc.curePoison();
		pc.cureParalaysis();

		for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
			if (L1SkillUse.isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.removeSkillEffect(skillNum);
		}

		L1PolyMorph.undoPoly(pc);
		S_CharVisualUpdate cvu = new S_CharVisualUpdate(pc);
		pc.sendPackets(cvu);
		Broadcaster.broadcastPacket(pc, cvu);
		cvu = null;

		if (pc.getHasteItemEquipped() > 0) {
			pc.setMoveSpeed(0);
			S_SkillHaste sh = new S_SkillHaste(pc.getId(), 0, 0);
			pc.sendPackets(sh);
			Broadcaster.broadcastPacket(pc, sh);
			sh = null;
		}
		if (pc != null && pc.isInvisble()) {
			if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
				pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
				S_Invis iv = new S_Invis(pc.getId(), 0);
				pc.sendPackets(iv);
				Broadcaster.broadcastPacket(pc, iv);
				S_Sound ss = new S_Sound(147);
				pc.sendPackets(ss);
				iv = null;
				ss = null;

				S_RemoveObject iv2 = new S_RemoveObject(pc.getId());
				pc.sendPackets(iv2);
				Broadcaster.broadcastPacket(pc, iv2);
			}
			if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
				pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
				S_Invis iv = new S_Invis(pc.getId(), 0);
				pc.sendPackets(iv);
				Broadcaster.broadcastPacket(pc, iv);
				iv = null;

				S_RemoveObject iv2 = new S_RemoveObject(pc.getId());
				pc.sendPackets(iv2);
				Broadcaster.broadcastPacket(pc, iv2);
			}
		}
		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), timeSec), true);
		}
		pc.removeSkillEffect(STATUS_FREEZE);
	}
}
