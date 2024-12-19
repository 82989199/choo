package l1j.server.MJCharacterActionSystem.Wand;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.utils.MJCommons;

public class WandActionHandler extends AbstractActionHandler implements MJPacketParser{
	public static WandActionHandler create(L1ItemInstance item){
		switch(item.getItemId()){
		case 40007:
		case 40412:
			return new WandActionHandler(item, 10, 0.16D);
		case 40006:
		case 140006:
			return new WandActionHandler(item, 11736, 0.5D);
		}
		return null;
	}
	
	protected L1Character		target;
	protected int					tId;
	protected int					tX;
	protected int					tY;
	protected L1ItemInstance item;
	protected int					spriteId;
	protected double			damageRevision;
	
	public WandActionHandler(L1ItemInstance item, int spriteId, double damageRevision){
		this.item = item;
		this.spriteId = spriteId;
		this.damageRevision = damageRevision;
	}
	
	@Override
	public void dispose(){
		target 	= null;
		item		= null;
		super.dispose();
	}
	
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner 	= owner;
		tId			= pck.readD();
		tX 			= pck.readH();
		tY 			= pck.readH();
	}

	@Override
	public void doWork() {
		int delayId = item.getItem().get_delayid();
		if(delayId > 0){
			if(owner.hasItemDelay(delayId)){
				dispose();
				return;
			}
			L1ItemDelay.onItemUse(owner, item);
		}
		
		owner.killSkillEffectTimer(L1SkillId.MEDITATION);
		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		owner.delInvis();
		register();
	}
	
	@Override
	public void handle() {
		if(!validation()){
			sendEffect(tX, tY);
			return;
		}
		sendEffect(target == null ? 0 : target.getId());
		
		try{
			if(target.instanceOf(MJL1Type.L1TYPE_PC)){
				int dmg = 0;
				if(!MJCommons.isCounterMagic(target)){
					dmg = 60 - (target.getResistance().getMrAfterEraseRemove() / 10);
					dmg = (int)(dmg * damageRevision);
					dmg += MJRnd.next(5);
				}
				dmg = Math.max(1, dmg);
				target.receiveDamage(owner, dmg);
				L1PinkName.onAction(target, owner);
			}else if(target.instanceOf(MJL1Type.L1TYPE_MONSTER)){
				int dmg = 10 + (owner.getLevel() / 4);
				dmg = (int)(dmg * damageRevision);
				dmg += MJRnd.next(5);
				target.receiveDamage(owner, dmg);
			}
		}finally{
		}
	}

	@Override
	public boolean validation() {
		if(owner == null || owner.is_non_action() || owner.isDead() || MJCommons.isNonAction(owner) || owner.isGhost() || owner.get_teleport() || owner.isInvisDelay())
			return false;
		
		try{
			if(owner.isOnTargetEffect()){
				long cur = System.currentTimeMillis();
				if(cur - owner.getLastNpcClickMs() > 1000){
					owner.setLastNpcClickMs(cur);
					owner.sendPackets(new S_SkillSound(tId, 14486));
				}
			}
			
			L1Object obj = L1World.getInstance().findObject(tId);
			if(obj == null || !obj.instanceOf(MJL1Type.L1TYPE_CHARACTER))
				return false;
			
			if(!owner.glanceCheck(tX, tY) || owner.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
				return false;
		
			target = (L1Character)obj;
			if(target.isDead() || MJCommons.isUnbeatable(target) || target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
				return false;
			
			if(target.instanceOf(MJL1Type.L1TYPE_PC) && (owner.getZoneType() == 1 || target.getZoneType() == 1))
				return false;
			else if(target.instanceOf(MJL1Type.L1TYPE_MONSTER) && target.getKarma() > 0)
				return false;
		}finally{
			consumeWand();
		}
		return true;
	}
	
	private void sendEffect(int targetId){
		S_UseAttackSkill s = new S_UseAttackSkill(owner, targetId, spriteId, tX, tY, EActionCodes.wand.toInt());
		owner.sendPackets(s, false);
		owner.broadcastPacket(s);
	}
	
	private void sendEffect(int x, int y){
		S_DoActionGFX gfx = new S_DoActionGFX(owner.getId(), EActionCodes.wand.toInt());
		owner.sendPackets(gfx, false);
		owner.broadcastPacket(gfx);
		S_EffectLocation s = new S_EffectLocation(x, y, spriteId);
		owner.sendPackets(s, false);
		owner.broadcastPacket(s);
	}
	
	private void consumeWand(){
		if(item.getItemId() == 40412)
			owner.getInventory().removeItem(item, 1);
		else{
			item.setChargeCount(item.getChargeCount() - 1);
			if(item.getChargeCount() <= 0)
				owner.getInventory().removeItem(item);
			else
				owner.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_WAND;
	}

	@Override
	public long getInterval() {
		return owner.hasSkillEffect(L1SkillId.SHAPE_CHANGE) || owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER) || owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER2) ? 10L : owner.getCurrentSpriteInterval(EActionCodes.wand);
	}
}
