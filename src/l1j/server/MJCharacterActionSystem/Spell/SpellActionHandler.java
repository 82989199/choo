package l1j.server.MJCharacterActionSystem.Spell;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;

import java.util.HashMap;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.MJCommons;

public abstract class SpellActionHandler extends AbstractActionHandler implements MJPacketParser {
	protected int skillId;
	protected int tId;
	protected int tX;
	protected int tY;
	protected String message;

	@Override
	public void handle(){
		if(!validation())
			return;
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(owner, skillId, tId, tX, tY, message, 0, L1SkillUse.TYPE_NORMAL);
		l1skilluse = null;
	}
	
	public SpellActionHandler setSkillId(int skillId){
		this.skillId = skillId;
		return this;
	}

	@Override
	public void doWork(){
		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		owner.killSkillEffectTimer(MEDITATION);
		handle();
	}

	@Override
	public boolean validation() {
		owner.offFishing();
		L1Map m = owner.getMap();
		if (!m.isUsableSkill()) {
			owner.sendPackets(new S_ServerMessage(563)); // 여기에서는 사용할 수 없습니다.
			return false;
		}
		
		if(MJCommons.isNonAction(owner))
			return false;
		
		return check_morph();
	}
	
	private boolean check_morph(){
		if(owner.isGm() || tId == owner.getId() || tId == 0)
			return true;
		
		int spr_id = owner.getCurrentSpriteId();
		if(spr_id != 12015 && 
				spr_id != 5641 && 
				spr_id != 11685 && 
				spr_id != 11620 && 
				spr_id != 11621
				)
			return true;
		
		L1Object target = L1World.getInstance().findObject(tId);
		if(target == null || !(target instanceof L1PcInstance))
			return true;
		/*
		if(m_debuff_skill.containsKey(skillId)){
			owner.sendPackets("전설/신화 변신상태에서는 PVP가 불가합니다.");
			return false;
		}*/
		/*
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		if(skill != null){
			if(skill.getTarget().equals("attack") || skill.getTarget().equals("buff")){
				owner.sendPackets("전설/신화 변신상태에서는 PVP가 불가합니다.");
				return false;				
			}
		}*/
		return false;
	}
	
	@Override
	public void dispose(){
		message = null;
		super.dispose();
	}
	
	@Override
	public int getRegistIndex(){
		return CharacterActionExecutor.ACTION_IDX_SPELL;
	}
	
	@Override
	public long getInterval() {
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		return owner.getCurrentSpriteInterval(skill.getTarget().equals("attack") ? EActionCodes.spell_dir : EActionCodes.spell_nodir);
	}
	
	@Override
	public abstract void parse(L1PcInstance owner, ClientBasePacket pck);
	public abstract SpellActionHandler copy();
	
	
}
