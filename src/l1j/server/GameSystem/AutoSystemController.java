package l1j.server.GameSystem;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.DANCING_BLADES;
import static l1j.server.server.model.skill.L1SkillId.DARKNESS;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DETECTION;
import static l1j.server.server.model.skill.L1SkillId.DISEASE;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.EXTRA_HEAL;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.HEAL;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;
import static l1j.server.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.STALAC;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_WISDOM_POTION;
import static l1j.server.server.model.skill.L1SkillId.WEAK_ELEMENTAL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SkillCheck;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.Teleportation;

public class AutoSystemController implements Runnable {

	private static final int MAGIC_PENALTY = 1000;
	private static final int AUTO_DUNGEON_TIMER_ID = 9;

	private Random _rnd = new Random(System.nanoTime());

	private final int _shopNpcId = 200006;//로봇전용 상점
	private final long _sleepTime = 20L;
	private static final int SKILL_SLEEP_TIME = 2;
	
	private static final int MOVE = 0;
	private static final int ATTACK = 1;
	private static final int SPELL_DIR = 2;
	private static final int SPELL_NODIR = 3;

	public final int AUTO_STATUS_NONE = -1;
	public final int AUTO_STATUS_SETTING = 0;
	public final int AUTO_STATUS_WALK = 1;
	public final int AUTO_STATUS_ATTACK = 2;
	public final int AUTO_STATUS_DEAD = 3;
	public final int AUTO_STATUS_MOVE_SHOP = 4;
	public final int AUTO_STATUS_MPREGEON = 5;
	public static final int AUTO_STATUS_WALK_MPREGEON = 6;
	
	private static final byte[] HEADING_TABLE_X = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte[] HEADING_TABLE_Y = { -1, -1, 0, 1, 1, 1, 0, -1 };

	/**자동 사냥 PC 리스트*/
	private ArrayList<L1PcInstance> _pcList = new ArrayList<L1PcInstance>();
	
	public static AutoSystemController _instance;

	public static AutoSystemController getInstance() {
		if(_instance == null) {
			_instance = new AutoSystemController();
		}
		return _instance;
	}
	//public AutoSystemController() {}	

	public void threadStart() {
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		while(true) {
			try {
				for(int i = 0; i < _pcList.size(); i++) {
					L1PcInstance current_pc = _pcList.get(i);
					try{
						getSource(current_pc);					
					}catch(Exception e){
						if(current_pc != null){
							System.out.println(String.format("예외 유저 정보 --> 아이디 : %s, 오브젝트 아이디 %d", current_pc.getName(), current_pc.getId()));
							if(current_pc.getNetConnection() == null || !current_pc.getNetConnection().isConnected()){
								System.out.println(String.format("%s 커넥션 정보 찾을 수 없음. 강제로 자동 사냥을 종료합니다.", current_pc.getName()));
								this.removeAuto(current_pc);
							}
						}else{
							System.out.println("예외 유저 정보를 찾을 수 없음.");
						}
						e.printStackTrace();						
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					Thread.sleep(_sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void getSource(L1PcInstance pc) {
		if(pc == null) {
			removeAuto(pc);
			return;
		}		
		/** 자동사냥 인증 아이템 체크 **/
		if(!pc.getInventory().checkItem(3000209) // 1일
			&& (!pc.getInventory().checkItem(3000213) //3일
			&& (!pc.getInventory().checkItem(3000214)))) { //7일
			pc.sendPackets(new S_SystemMessage("자동 사냥 종료: 자동 사냥 인증 아이템이 없습니다."));
			removeAuto(pc);
			return;
		}		
		
		 /** 무기 체크 **/
        if (pc.getWeapon() == null){
        	pc.sendPackets(new S_SystemMessage("자동 사냥 종료: 무기를 착용하지 않았습니다."));
        	removeAuto(pc);
            return;
        }
		if(pc.getInventory().getWeight100() > 82) {
			   pc.sendPackets(new S_SystemMessage("자동 사냥 종료: 무게 게이지가 초과되었습니다."));
			removeAuto(pc);
			return;
		}
		
		 /** 맵 체크 **/
		if(pc.getMapId() != 4 && pc.getMapId() != pc.getAutoMapId()) {
		  	pc.sendPackets(new S_SystemMessage("자동 사냥 종료: 허용된 사냥터가 아닙니다."));
			removeAuto(pc);
			return;
		}
		
		
		
		if((pc.getAutoStatus() != AUTO_STATUS_WALK && pc.getAutoStatus() != AUTO_STATUS_ATTACK) 
				&& pc.getMapId() == pc.getAutoMapId()) {
			pc.setAutoStatus(AUTO_STATUS_WALK);
		}
		if ((pc.getAutoStatus() != AUTO_STATUS_MOVE_SHOP && pc.getAutoStatus() != AUTO_STATUS_SETTING)
				&& pc.getMapId() == 4) {		
			pc.getAutoTargetList().clear();
			pc.setAutoTarget(null);			
			pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
		}
		
		 /** 상점 갈때 MP체크 후 엠탐 **/
        if (pc.getAutoStatus() == AUTO_STATUS_MOVE_SHOP){ 
        	if (pc.isWizard() && (int)((double) pc.getCurrentMp() / (double) pc.getMaxMp() * 100.0) < 80 && pc.get_food() >= 225) {
        		L1Skills _skill = SkillsTable.getInstance().getTemplate(L1SkillId.MEDITATION);
        		if (isHPMPConsume(pc, _skill, L1SkillId.MEDITATION)){
                    pc.setAutoStatus(AUTO_STATUS_MPREGEON);
        		}
        	} else if (pc.isElf() && (int)((double) pc.getCurrentMp() / (double) pc.getMaxMp() * 100.0) < 80 && pc.get_food() >= 225){
        		L1Skills _skill = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL);
        		if (isHPMPConsume(pc, _skill, L1SkillId.BLOODY_SOUL) && isItemConsume(pc, _skill)){
        			pc.setAutoStatus(AUTO_STATUS_MPREGEON);
        		}
        	}
        }
        
        /** 무빙 소울 **/
        if (pc.getAutoStatus() == AUTO_STATUS_WALK){
        	if (pc.getAutoTargetList().toTargetArrayList().size() == 0){
        		if (pc.isElf() && (int)((double) pc.getCurrentMp() / (double) pc.getMaxMp() * 100.0) < 80){
            		L1Skills _skill = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL);
            		if (isHPMPConsume(pc, _skill, L1SkillId.BLOODY_SOUL) && isItemConsume(pc, _skill)){
            			pc.setAutoStatus(AUTO_STATUS_WALK_MPREGEON);
            		}
            	}
        	}
        }
        
        
        /** 죽음 **/
		if(pc.isDead()) {
			restart(pc);
			return;
		}
		
		switch(pc.getAutoStatus()) {
		case AUTO_STATUS_MOVE_SHOP:
			toShopWalk(pc);		
			break;		
		case AUTO_STATUS_SETTING:
			toSettingBuyShop(pc);
			break;
		case AUTO_STATUS_WALK:
			noTargetTeleport(pc);
			toRandomWalk(pc);
			searchTarget(pc);
			if (pc.getAutoTargetList().toTargetArrayList().size() > 0) {				
				pc.setAutoStatus(AUTO_STATUS_ATTACK);				
			}
			toReturnScroll(pc);
			break;
		case AUTO_STATUS_ATTACK:
			if (pc.getAutoTargetList().toTargetArrayList().size() == 0) {				
				pc.getAutoTargetList().clear();
				pc.setAutoTarget(null);
				this.toUseScroll(pc, 40100);
				pc.setAutoStatus(AUTO_STATUS_WALK);
			}
			toAttackMonster(pc);
			toReturnScroll(pc);
			break;
		case AUTO_STATUS_MPREGEON:{
			toMpRegeon(pc);
			break;
		}
		}
		  if (pc.isParalyzed() || pc.isSleeped() || pc.isstop() || pc.isDead()) {
				return;
			}
	
		toUseItem(pc);
		toUseSkills(pc);
		  if (pc.isWizard() || pc.isElf() || pc.isDarkelf()){
			  toUseHealingMagic(pc);
	        }
		toPolyScroll(pc);
	}


	
	
	/** mp 회복 **/
	private void toMpRegeon(L1PcInstance pc) {
		int p = (int) (((double) pc.getCurrentMp() / (double) pc.getMaxMp()) * 100);
		if(p >= 80) {
			pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
			return;
		}
		
		  if (pc.get_food() < 225){
	        	pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
	            return;
	        }
		  
		  
		L1Skills _skill = SkillsTable.getInstance().getTemplate(L1SkillId.MEDITATION);
		L1SkillUse _skilluse;
		if(pc.isWizard()) {
			_skill = SkillsTable.getInstance().getTemplate(L1SkillId.MEDITATION);
			if(!isHPMPConsume(pc, _skill, L1SkillId.MEDITATION) || !isItemConsume(pc, _skill)) {
				return;
			}
			if(!pc.hasSkillEffect(L1SkillId.MEDITATION)) {
				_skilluse = new L1SkillUse();
				_skilluse.handleCommands(pc, L1SkillId.MEDITATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
			}
			if(p >= 70) {
				pc.removeSkillEffect(L1SkillId.MEDITATION);
				pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
			}	
			
		}else if(pc.isElf()) {
			_skill = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL);
			long current = System.currentTimeMillis();
			if(current - pc.getAutoSkillDelay() < 0)
				return;
			if(!isHPMPConsume(pc, _skill, L1SkillId.BLOODY_SOUL)) {
				return;
			}
			_skilluse = new L1SkillUse();
			_skilluse.handleCommands(pc, L1SkillId.BLOODY_SOUL, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
			pc.setAutoSkillDelay(System.currentTimeMillis() + _skill.getReuseDelay() + MAGIC_PENALTY);
			if(p >= 70) {
				pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
			}
		}
	}

	private void toTripleArrow(L1PcInstance pc, L1Character target) {			
		if(!SkillCheck.getInstance().CheckSkill(pc, L1SkillId.TRIPLE_ARROW)) return;
		L1Skills _skill = SkillsTable.getInstance().getTemplate(L1SkillId.TRIPLE_ARROW);
		long current = System.currentTimeMillis();
		if(current - pc.getAutoSkillDelay() < 0) 
			return;
		
		if(!isHPMPConsume(pc, _skill, L1SkillId.TRIPLE_ARROW)) {
			if(pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
				returnScroll(pc);
			}
			pc.setAutoStatus(AUTO_STATUS_MPREGEON);
			return;
		}
		L1SkillUse _skilluse = new L1SkillUse();
		_skilluse.handleCommands(pc, L1SkillId.TRIPLE_ARROW, target.getId(), target.getX(), target.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
		pc.setAutoSkillDelay(System.currentTimeMillis() + _skill.getReuseDelay() + MAGIC_PENALTY);
	}

	private void toUseSkills(L1PcInstance pc) {//오토 스킬
		int[] skillIds = null;
		if(pc.isCrown()) {
			skillIds = new int[]{L1SkillId.GLOWING_AURA, L1SkillId.BRAVE_AURA, L1SkillId.SHINING_AURA, L1SkillId.SHIELD};
		}else if(pc.isKnight()) {
			skillIds = new int[]{L1SkillId.SHIELD, L1SkillId.REDUCTION_ARMOR, L1SkillId.BOUNCE_ATTACK, L1SkillId.COUNTER_BARRIER};
		}else if(pc.is전사()) {
			skillIds = new int[]{L1SkillId.GIGANTIC, L1SkillId.SHIELD};
		}else if(pc.isDragonknight()) {
			skillIds = new int[]{L1SkillId.MORTAL_BODY, L1SkillId.DRAGON_SKIN, L1SkillId.BLOOD_LUST};
		}else if(pc.isElf()) {
			if(pc.getWeapon() == null) {
				return;
			}
			if(pc.getWeapon().getItem().getType1() == 20) {//활
				skillIds = new int[]{L1SkillId.PHYSICAL_ENCHANT_STR, 
						L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.SHIELD, L1SkillId.BLESSED_ARMOR};
			}else {
				skillIds = new int[]{L1SkillId.DANCING_BLADES, L1SkillId.BURNING_WEAPON, 
						L1SkillId.ELEMENTAL_FIRE, L1SkillId.ADDITIONAL_FIRE, L1SkillId.SOUL_OF_FLAME, 
						L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.PHYSICAL_ENCHANT_DEX,
						L1SkillId.BLESS_WEAPON, L1SkillId.SHIELD, L1SkillId.BLESSED_ARMOR};
			}
		}else if(pc.isDarkelf()) {
			skillIds = new int[]{L1SkillId.DRESS_MIGHTY, L1SkillId.SHADOW_ARMOR, L1SkillId.ENCHANT_VENOM, L1SkillId.MOVING_ACCELERATION,
					L1SkillId.DRESS_DEXTERITY, L1SkillId.BURNING_SPIRIT, L1SkillId.DOUBLE_BRAKE, L1SkillId.DRESS_EVASION,
					L1SkillId.SHADOW_FANG, L1SkillId.UNCANNY_DODGE};
		}else if(pc.isBlackwizard()) {
			skillIds = new int[]{L1SkillId.MIRROR_IMAGE, L1SkillId.IllUSION_LICH, L1SkillId.CONCENTRATION, L1SkillId.PATIENCE,
					L1SkillId.IllUSION_DIAMONDGOLEM, L1SkillId.INSIGHT};
		}else if(pc.isWizard()) {
			skillIds = new int[]{L1SkillId.HOLY_WALK, L1SkillId.SHIELD, L1SkillId.BLESSED_ARMOR, L1SkillId.BLESS_WEAPON,
					L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.ADVANCE_SPIRIT};
		}

		L1SkillUse _skilluse;
		L1Skills _skill;
		for(int i = 0; i < skillIds.length; i++) {
			try {	
				long current = System.currentTimeMillis();
				if(current - pc.getAutoSkillDelay() < 0)
					break;
				if(!SkillCheck.getInstance().CheckSkill(pc, skillIds[i])) continue;
				if(pc.hasSkillEffect(skillIds[i])) continue;				
				_skill = SkillsTable.getInstance().getTemplate(skillIds[i]);
				if(!isHPMPConsume(pc, _skill, skillIds[i])) {
					if(pc.isWizard() || pc.isElf()) {
						if(pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
							returnScroll(pc);
							pc.setAutoStatus(AUTO_STATUS_MPREGEON);
						}else {
							pc.setAutoStatus(AUTO_STATUS_MPREGEON);
						}
					}
					break;
				}else if(!isItemConsume(pc, _skill)){
					if(pc.isWizard() || pc.isElf()) {//
						if(pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
							returnScroll(pc);
						}else {
							pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
						}
					}
				}
				if(skillIds[i] == L1SkillId.BLESSED_ARMOR) {
					getBlessedArmor(pc, _skill);
					continue;
				}
				if(skillIds[i] == L1SkillId.BLESS_WEAPON) {
					if(pc.getWeapon() == null) continue;
					if(pc.getWeapon().hasSkillEffectTimer(L1SkillId.BLESS_WEAPON)) continue;
				}
				if(skillIds[i] == L1SkillId.SHADOW_FANG) {
					if(pc.getWeapon() == null) continue;
					if(pc.getWeapon().hasSkillEffectTimer(L1SkillId.SHADOW_FANG)) continue;
					pc.getWeapon().setSkillWeaponEnchant(pc, L1SkillId.SHADOW_FANG, _skill.getBuffDuration() * 1000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, _skill.getBuffDuration(), _skill.getCastGfx(),  0));
					S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), _skill.getActionId());
					pc.sendPackets(gfx);
					pc.broadcastPacket(gfx);
					pc.sendPackets(new S_SkillSound(pc.getId(), _skill.getCastGfx()));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), _skill.getCastGfx()));
					continue;
				}
				if(skillIds[i] == L1SkillId.DANCING_BLADES) {
					if(pc.getWeapon() == null) continue;
					if (pc.getWeapon().getItem().getType() != 1
							&& pc.getWeapon().getItem().getType() != 2) {
						continue;
					}
				}
				if(skillIds[i] == L1SkillId.COUNTER_BARRIER) {
					if (pc.getWeapon() == null || pc.getWeapon().getItem().getType() != 3) {
						continue;
					}
				}
				if(skillIds[i] == L1SkillId.HOLY_WALK) {
					if(pc.getAutoStatus() == AUTO_STATUS_MPREGEON) continue;
				}
				_skilluse = new L1SkillUse();
				_skilluse.handleCommands(pc, skillIds[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL);				
				pc.setAutoSkillDelay(System.currentTimeMillis() + _skill.getReuseDelay() + MAGIC_PENALTY);
			}catch(Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void getBlessedArmor(L1PcInstance pc, L1Skills _skill) {
		ArrayList<L1ItemInstance> item = pc.getEquipSlot().getArmors();
		L1ItemInstance armor = null;
		for(int i = 0; i < item.size(); i++) {
			if(item.get(i).getItem().getType() == 2) {
				armor = item.get(i);
				break;
			}
		}
		if(armor == null) return;
		if (armor != null) {
			if(armor.hasSkillEffectTimer(L1SkillId.BLESSED_ARMOR)) return;
			armor.setSkillArmorEnchant(pc, L1SkillId.BLESSED_ARMOR, _skill.getBuffDuration() * 1000);
			pc.sendPackets(new S_ServerMessage(161, String.valueOf(armor.getLogName()).trim(), "$245", "$247"));
			if(armor.isEquipped())
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, _skill.getBuffDuration(), _skill.getCastGfx(),  0));
			S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), _skill.getActionId());
			pc.sendPackets(gfx);
			pc.broadcastPacket(gfx);
			pc.sendPackets(new S_SkillSound(pc.getId(), _skill.getCastGfx()));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), _skill.getCastGfx()));
			pc.setAutoSkillDelay(System.currentTimeMillis() + _skill.getReuseDelay() + MAGIC_PENALTY);
		}
	}

	private boolean isItemConsume(L1PcInstance pc, L1Skills _skill) {
		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();
		if (itemConsume == 0) {
			return true;
		}
		if (itemConsume == 40318) { 
			if (pc.getInventory().checkItem(30079, itemConsumeCount)
					&& pc.getLevel() < 56) {
				return true;
			}
		} else if (itemConsume == 40321) { 
			if (pc.getInventory().checkItem(30080, itemConsumeCount)
					&& pc.getLevel() < 56) {
				return true;
			}
		} else if (itemConsume == 210035) { 
			if (pc.getInventory().checkItem(30081, itemConsumeCount)
					&& pc.getLevel() < 56) {
				return true;
			}
		} else if (itemConsume == 210038) { 
			if (pc.getInventory().checkItem(30082, itemConsumeCount)
					&& pc.getLevel() < 56) {
				return true;
			}
		} else if (itemConsume == 40319) { 
			if (pc.getInventory().checkItem(30078, itemConsumeCount)
					&& pc.getLevel() < 56) {
				return true;
			}
		}
		if (!pc.getInventory().checkItem(itemConsume, itemConsumeCount)) {
			return false;
		}
		return true;
	}

	private boolean isHPMPConsume(L1PcInstance pc, L1Skills _skill, int _skillId) {		
		int _hpConsume;
		int _mpConsume;
		_mpConsume = _skill.getMpConsume();
		_hpConsume = _skill.getHpConsume();
		int currentMp = 0;
		int currentHp = 0;

		currentMp = pc.getCurrentMp();
		currentHp = pc.getCurrentHp();

		if (pc.getAbility().getTotalInt() > 12
				&& _skillId > HOLY_WEAPON && _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 13 && _skillId > STALAC
				&& _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 14
				&& _skillId > WEAK_ELEMENTAL
				&& _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 15
				&& _skillId > MEDITATION && _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 16 && _skillId > DARKNESS
				&& _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 17
				&& _skillId > BLESS_WEAPON && _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}
		if (pc.getAbility().getTotalInt() > 18 && _skillId > DISEASE
				&& _skillId <= FREEZING_BLIZZARD) {
			_mpConsume--;
		}

		if (pc.getAbility().getTotalInt() > 12
				&& _skillId >= SHOCK_STUN && _skillId <= COUNTER_BARRIER) {
			_mpConsume -= (pc.getAbility().getTotalInt() - 12);
		}
		if (pc.isCrown()) {
			if (pc.getAbility().getBaseInt() >= 11) {
				_mpConsume--;
			}
			if (pc.getAbility().getBaseInt() >= 13) {
				_mpConsume--;
			}
		} else if (pc.isKnight()) {
			if (pc.getAbility().getBaseInt() >= 9) {
				_mpConsume--;
			}
			if (pc.getAbility().getBaseInt() >= 11) {
				_mpConsume--;
			}
		} else if (pc.isDarkelf()) {
			if (pc.getAbility().getBaseInt() >= 13) {
				_mpConsume--;
			}
			if (pc.getAbility().getBaseInt() >= 15) {
				_mpConsume--;
			}
		} else if (pc.isBlackwizard()) {
			if (pc.getAbility().getBaseInt() >= 14) {
				_mpConsume--;
			}
			if (pc.getAbility().getBaseInt() >= 15) {
				_mpConsume--;
			}
		} else if (pc.is전사()) {
			if (pc.getAbility().getBaseInt() >= 11) {
				_mpConsume--;
			}
			if (pc.getAbility().getBaseInt() >= 13) {
				_mpConsume--;
			}
		}

		if (_skillId == PHYSICAL_ENCHANT_DEX
				&& pc.getInventory().checkEquipped(20013)) {
			_mpConsume /= 2;
		}
		if (_skillId == HASTE
				&& pc.getInventory().checkEquipped(20013)) {
			_mpConsume /= 2;
		}
		if (_skillId == HEAL && pc.getInventory().checkEquipped(20014)) {
			_mpConsume /= 2;
		}
		if (_skillId == EXTRA_HEAL
				&& pc.getInventory().checkEquipped(20014)) {
			_mpConsume /= 2;
		}
		if (_skillId == ENCHANT_WEAPON
				&& pc.getInventory().checkEquipped(20015)) {
			_mpConsume /= 2;
		}
		if (_skillId == DETECTION
				&& pc.getInventory().checkEquipped(20015)) {
			_mpConsume /= 2;
		}
		if (_skillId == PHYSICAL_ENCHANT_STR
				&& pc.getInventory().checkEquipped(20015)) {
			_mpConsume /= 2;
		}
		if (_skillId == HASTE
				&& pc.getInventory().checkEquipped(20008)) {
			_mpConsume /= 2;
		}
		if (_skillId == GREATER_HASTE
				&& pc.getInventory().checkEquipped(20023)) {
			_mpConsume /= 2;
		}

		if (0 < _skill.getMpConsume()) {
			_mpConsume = Math.max(_mpConsume, 1);
		}

		if (currentHp < _hpConsume + 1) {
			return false;
		} else if (currentMp < _mpConsume) {
			return false;
		}
		return true;
	}

    /** 변신 **/
	
    private void toPolyScroll(L1PcInstance pc) {
        if (pc.getAutoPolyID() == 0) {
            return;
        }
        if (pc.getCurrentSpriteId() == pc.getAutoPolyID()) {
            return;
        }
        toUseScroll(pc, 40088);
    }

	 /** 귀환 **/
	private void toReturnScroll(L1PcInstance pc) {
		int percent = (int) Math.round(((double) pc.getCurrentHp() / (double) pc.getMaxHp()) * 100);
		if(percent <= 30) { // hp가 30프로 이하일시
			returnScroll(pc);
		}
	}
	
	
	private void returnScroll(L1PcInstance pc) {
		toUseScroll(pc, 40081);
		pc.setAutoTarget(null);
		pc.getAutoTargetList().clear();
	}
	
	/** 아이템 사용 **/
	private void toUseItem(L1PcInstance pc) {
		useHealPotion(pc); // 회복제
		useGreenPotion(pc); // 농축촐기
		 useBluePotion(pc); //농축마력
		 useCook(pc); //요리
	     useExpCook(pc); //스프
	     usefood(pc); //해독제
	     usePoisonPotion(pc); //해독제
	     useWeaponFix(pc); //숫돌
	   //  useCashScroll(pc); //전투강화주문서
	   //  useExpPotion(pc); //경험치 물약
		if(pc.isCrown() || pc.isKnight() || pc.is전사()) {
		    useBravePotion(pc, 41415); //농축용기
        } else if (pc.isElf()) {
        	if (!(SkillCheck.getInstance().CheckSkill(pc, L1SkillId.SAND_STORM) && pc.getElfAttr() == 1
        			&& (pc.getWeapon().getItem().getType() == 1 || pc.getWeapon().getItem().getType() == 2))
        		&& !(SkillCheck.getInstance().CheckSkill(pc, L1SkillId.DANCING_BLADES) && pc.getElfAttr() == 2
            		&& (pc.getWeapon().getItem().getType() == 1 || pc.getWeapon().getItem().getType() == 2))
            	&& !(SkillCheck.getInstance().CheckSkill(pc, L1SkillId.FOCUS_WAVE) && pc.getElfAttr() == 4
            		&& pc.getWeapon().getItem().getType1() == 20)
            	&& !(SkillCheck.getInstance().CheckSkill(pc, L1SkillId.HURRICANE) && pc.getElfAttr() == 8
            		&& pc.getWeapon().getItem().getType1() == 20)){
        		useBravePotion(pc, 210110); //농축집중
        	}
        } else if (pc.isWizard()) {
            useWisdomPotion(pc); //농축지혜
        } else if (pc.isBlackwizard()) {
            useWisdomPotion(pc); //농축지혜
            useFruit(pc); //유그드라
        }
    }
	
    /** 아이템 사용딜레이 체크 **/
	private boolean isUseCheck(L1PcInstance pc, L1ItemInstance item) {
		int delay_id = 0;
		if (item.getItem().getType2() == 0) { 
			if (item.getItem() instanceof L1EtcItem) {
				delay_id = ((L1EtcItem) item.getItem()).get_delayid();
			}
		}
		if (delay_id != 0) { 
			if (pc.hasItemDelay(delay_id) == true) {
				return false;
			}
		}
		return true;
	}
	
	 /** 회복제 **/
	private void useHealPotion(L1PcInstance pc) {		
		int itemId = pc.getAutoPotion();
		L1ItemInstance item = pc.getInventory().findItemId(itemId);
		if(item == null) {
			if(pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
				returnScroll(pc);
			}else {
				if(isFindShop(pc)) {
					pc.setAutoStatus(AUTO_STATUS_SETTING);
				}
			}
			return;			
		}
		if(!isUseCheck(pc, item)) return;		
		if(pc.getCurrentHp() == pc.getMaxHp()) {
			return;
		}
		L1HealingPotion healingPotion = L1HealingPotion.get(itemId);			
		healingPotion.use(pc, item);
		L1ItemDelay.onItemUse(pc, item);
	}

	/** 농축 속도의 물약 **/
	private void useGreenPotion(L1PcInstance pc) {
		L1ItemInstance item = pc.getInventory().findItemId(30158);
		  if (pc.getMoveSpeed() != 0){ //효과가 잇을경우
	            return;
	        }
	        if (item == null) { // 아이템이 없을때
	            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
	                returnScroll(pc);
	            } else if (isFindShop(pc)) {
	                pc.setAutoStatus(AUTO_STATUS_SETTING);
	            }
	            return;
	        }
	        
		if(!isUseCheck(pc, item)) return;			
		if (pc.hasSkillEffect(71) == true) {
			return;
		}
		int time = 1800;
		pc.sendPackets(new S_SkillSound(pc.getId(), 191));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		pc.setDrink(false);
		if (pc.hasSkillEffect(HASTE)) {
			pc.killSkillEffectTimer(HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(GREATER_HASTE)) {
			pc.killSkillEffectTimer(GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(STATUS_HASTE)) {
			pc.killSkillEffectTimer(STATUS_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		}
		if (pc.hasSkillEffect(SLOW)) {
			pc.killSkillEffectTimer(SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, time * 1000);
		}
		pc.getInventory().removeItem(item, 1);
		L1ItemDelay.onItemUse(pc, item);
	}
	
	 /** 2단가속 **/
	private void useBravePotion(L1PcInstance pc, int item_id) {		
		   if (pc.getBraveSpeed() != 0) { //효과가 잇을때
	            return;
	        }
	       L1ItemInstance item = pc.getInventory().findItemId(item_id);
	        if (item == null) { //아이템이 없을때
	            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
	                returnScroll(pc);
	            } else if (isFindShop(pc)) {
	                pc.setAutoStatus(AUTO_STATUS_SETTING);
	            }
	            return;
	        }
		if(!isUseCheck(pc, item)) return;
		if (pc.hasSkillEffect(71) == true) {
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = 0;
		if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY || item_id == 30073) {
			time = 300;
		} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) {
			time = 350;
		} else if (item_id == 41415) {
			time = 1800;
		} else if (item_id == 40068 || item_id == 30076) {
			time = 480;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(DANCING_BLADES)) {
				pc.killSkillEffectTimer(DANCING_BLADES);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 140068) {
			time = 700;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(DANCING_BLADES)) {
				pc.killSkillEffectTimer(DANCING_BLADES);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0,	0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 210110) {
			time = 1800;
			if (pc.hasSkillEffect(STATUS_BRAVE)) {
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 40031 || item_id == 30075) {
			time = 600;
		} else if (item_id == 210115) {
			time = 1800;
		} else if (item_id == 40733) {
			time = 600;
			if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {
				pc.killSkillEffectTimer(STATUS_ELFBRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(HOLY_WALK)) {
				pc.killSkillEffectTimer(HOLY_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(MOVING_ACCELERATION)) {
				pc.killSkillEffectTimer(MOVING_ACCELERATION);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(STATUS_FRUIT)) {
				pc.killSkillEffectTimer(STATUS_FRUIT);
				pc.setBraveSpeed(0);
			}
		}
		if (item_id == 40068 || item_id == 140068 || item_id == 210110
				|| item_id == 30076) { // 엘븐 와퍼
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.setSkillEffect(STATUS_ELFBRAVE, time * 1000);
		} else {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			pc.setSkillEffect(STATUS_BRAVE, time * 1000);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), 751));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
		pc.setBraveSpeed(1);
		pc.getInventory().removeItem(item, 1);
		L1ItemDelay.onItemUse(pc, item);
	}
	
	/** 농축 지혜의 물약 **/
	private void useWisdomPotion(L1PcInstance pc) {
		   if (pc.hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) { //효과가 잇을때
	            return;
	        }
	        L1ItemInstance item = pc.getInventory().findItemId(210113);
	        if (item == null) { //아이템이 없을때
	            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
	                returnScroll(pc);
	            } else if (isFindShop(pc)) {
	                pc.setAutoStatus(AUTO_STATUS_SETTING);
	            }
	            return;
	        }
		if(!isUseCheck(pc, item)) return;
		if (pc.hasSkillEffect(71) == true) { 
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = 1000;

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.getAbility().addSp(2);
			pc.addMpr(2);
		}
		pc.sendPackets(new S_SkillIconWisdomPotion((int) (time)));
		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
		pc.setSkillEffect(STATUS_WISDOM_POTION, time * 1000);
		pc.getInventory().removeItem(item, 1);
		L1ItemDelay.onItemUse(pc, item);		
	}

    /** 농축 유그드라 열매 **/
	private void useFruit(L1PcInstance pc) {
		  if (pc.getBraveSpeed() != 0) { //효과가 잇을때
	            return;
	        }
	        L1ItemInstance item = pc.getInventory().findItemId(713);
	        if (item == null) { //아이템이 없을때
	            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
	                returnScroll(pc);
	            } else if (isFindShop(pc)) {
	                pc.setAutoStatus(AUTO_STATUS_SETTING);
	            }
	            return;
	        }
		if (pc.hasSkillEffect(DECAY_POTION) == true) { 
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = 480;
		if (pc.hasSkillEffect(STATUS_BRAVE)) {
			pc.killSkillEffectTimer(STATUS_BRAVE);
			pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
			pc.setBraveSpeed(0);
		}

		pc.setBraveSpeed(4);
		pc.sendPackets(new S_SkillBrave(pc.getId(), 4, time));
		pc.sendPackets(new S_SkillSound(pc.getId(), 7110));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));
		pc.setSkillEffect(STATUS_FRUIT, time * 1000);
		pc.getInventory().removeItem(item, 1);
		L1ItemDelay.onItemUse(pc, item);
	}

	/** 농축 마력의 물약 **/
	private void useBluePotion(L1PcInstance pc) {
		  if (pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)) { //효과가 잇을때
	            return;
	        }
	        L1ItemInstance item = pc.getInventory().findItemId(210114);
	        if (item == null) { //아이템이 없을때
	            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
	                returnScroll(pc);
	            } else if (isFindShop(pc)) {
	                pc.setAutoStatus(AUTO_STATUS_SETTING);
	            }
	            return;
	        }
		if(!isUseCheck(pc, item)) return;
		if (pc.hasSkillEffect(DECAY_POTION)) {
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = 1800;

		pc.sendPackets(new S_SkillIconGFX(34, time, true));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);
		pc.sendPackets(new S_ServerMessage(1007));
		pc.getInventory().removeItem(item, 1);
		L1ItemDelay.onItemUse(pc, item);
	}
	
    /** 요리 **/
    private void useCook(L1PcInstance pc) {
    	if (pc.isElf() && pc.getWeapon().getItem().getType1() == 20){
    		if (pc.hasSkillEffect(L1SkillId.COOK_DEX)){
    			return;
    		}
    		L1ItemInstance item = pc.getInventory().findItemId(30052); //날쎈 연어찜
            if (item == null) { //아이템이 없을때
                if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                    returnScroll(pc);
                } else if (isFindShop(pc)) {
                    pc.setAutoStatus(AUTO_STATUS_SETTING);
                }
                return;
            }
            if (!isUseCheck(pc, item)) { //사용딜레이
                return;
            }
            pc.cancelAbsoluteBarrier();
            if (!pc.hasSkillEffect(L1SkillId.COOK_DEX)) {
            	pc.addBowDmgup(2);
    			pc.addBowHitup(1);
    			pc.addHpr(2);
    			pc.addMpr(2);
    			pc.getResistance().addMr(10);
    			pc.getResistance().addAllNaturalResistance(10);
    			pc.sendPackets(new S_SPMR(pc));
            }
            
            pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
            pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 158, 1800));
    		pc.setSkillEffect(L1SkillId.COOK_DEX, 1800 * 1000);
    		pc.getInventory().removeItem(item, 1);
            L1ItemDelay.onItemUse(pc, item);
    	} else if (pc.isWizard() || pc.isBlackwizard()){
    		if (pc.hasSkillEffect(L1SkillId.COOK_INT)){
    			return;
    		}
    		L1ItemInstance item = pc.getInventory().findItemId(30053); //영리한 칠면조
            if (item == null) { //아이템이 없을때
                if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                    returnScroll(pc);
                } else if (isFindShop(pc)) {
                    pc.setAutoStatus(AUTO_STATUS_SETTING);
                }
                return;
            }
            if (!isUseCheck(pc, item)) { //사용딜레이
                return;
            }
            pc.cancelAbsoluteBarrier();
            if (!pc.hasSkillEffect(L1SkillId.COOK_INT)) {
            	pc.getAbility().addSp(2);
    			pc.addHpr(2);
    			pc.addMpr(3);
    			pc.getResistance().addMr(10);
    			pc.getResistance().addAllNaturalResistance(10);
    			pc.sendPackets(new S_SPMR(pc));
            }
            pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
            pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 159, 1800));
    		pc.setSkillEffect(L1SkillId.COOK_INT, 1800 * 1000);
    		pc.getInventory().removeItem(item, 1);
            L1ItemDelay.onItemUse(pc, item);
    	} else {
    		if (pc.hasSkillEffect(L1SkillId.COOK_STR)){
    			return;
    		}
    		L1ItemInstance item = pc.getInventory().findItemId(30051); //한우 스테이크
            if (item == null) { //아이템이 없을때
                if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                    returnScroll(pc);
                } else if (isFindShop(pc)) {
                    pc.setAutoStatus(AUTO_STATUS_SETTING);
                }
                return;
            }
            if (!isUseCheck(pc, item)) { //사용딜레이
                return;
            }
            pc.cancelAbsoluteBarrier();
            if (!pc.hasSkillEffect(L1SkillId.COOK_STR)) {
            	pc.addDmgup(2);
    			pc.addHitup(1);
    			pc.addHpr(2);
    			pc.addMpr(2);
    			pc.getResistance().addMr(10);
    			pc.getResistance().addAllNaturalResistance(10);
    			pc.sendPackets(new S_SPMR(pc));
            }
            pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
            pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 157, 1800));
    		pc.setSkillEffect(L1SkillId.COOK_STR, 1800 * 1000);
    		pc.getInventory().removeItem(item, 1);
            L1ItemDelay.onItemUse(pc, item);
    	}
    }
    
    /** 스프 **/
    private void useExpCook(L1PcInstance pc) {
    	if (pc.hasSkillEffect(L1SkillId.COOK_GROW)){
			return;
		}
		L1ItemInstance item = pc.getInventory().findItemId(30054); //수련의닭고기 스프
        if (item == null) { //아이템이 없을때
            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                returnScroll(pc);
            } else if (isFindShop(pc)) {
                pc.setAutoStatus(AUTO_STATUS_SETTING);
            }
            return;
        }
        if (!isUseCheck(pc, item)) { //사용딜레이
            return;
        }
        pc.cancelAbsoluteBarrier();
        if (!pc.hasSkillEffect(L1SkillId.COOK_GROW)) {
        	pc.addDamageReductionByArmor(2);
        }
        pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
        pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 160, 1800));
		pc.setSkillEffect(L1SkillId.COOK_GROW, 1800 * 1000);
		pc.getInventory().removeItem(item, 1);
        L1ItemDelay.onItemUse(pc, item);   	
    }
    
    /** 허브 **/
    private void usefood(L1PcInstance pc) {
    	if (pc.get_food() >= 225){
			return;
		}
		L1ItemInstance item = pc.getInventory().findItemId(210039); //허브
        if (item == null) { //아이템이 없을때
            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                returnScroll(pc);
            } else if (isFindShop(pc)) {
                pc.setAutoStatus(AUTO_STATUS_SETTING);
            }
            return;
        }
        if (!isUseCheck(pc, item)) { //사용딜레이
            return;
        }
        pc.cancelAbsoluteBarrier();
		if (pc.get_food() < 225) {
			pc.set_food(pc.get_food() + 10);
			int foodvolume = (item.getItem().getFoodVolume() / 10);
			pc.add_food(foodvolume <= 0 ? 5 : foodvolume);
			pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
			pc.sendPackets(new S_ServerMessage(76, item.getItem().getNameId()));
			pc.getInventory().removeItem(item, 1);
		}
    }
    
    /** 해독제 **/
    private void usePoisonPotion(L1PcInstance pc) {
    	if (pc.getPoison() == null){
			return;
		}
		L1ItemInstance item = pc.getInventory().findItemId(40017); //해독제
        if (item == null) { //아이템이 없을때
            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                returnScroll(pc);
            } else if (isFindShop(pc)) {
                pc.setAutoStatus(AUTO_STATUS_SETTING);
            }
            return;
        }
        if (!isUseCheck(pc, item)) { //사용딜레이
            return;
        }
        if (pc.hasSkillEffect(L1SkillId.DECAY_POTION)) {
            return;
        }
        pc.cancelAbsoluteBarrier();
        if (pc.getPoison() != null) {
        	pc.sendPackets(new S_SkillSound(pc.getId(), 192));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
        	pc.curePoison();
        	pc.getInventory().removeItem(item, 1);
            L1ItemDelay.onItemUse(pc, item);  
        }	
    }
    
    /** 숫돌 **/
    private void useWeaponFix(L1PcInstance pc) {
    	L1ItemInstance weapon = pc.getWeapon();
    	if (weapon.get_durability() <= 0){
    		return;
    	}
    	
		L1ItemInstance item = pc.getInventory().findItemId(40317); //숫돌
        if (item == null) { //아이템이 없을때
            if (pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
                returnScroll(pc);
            } else if (isFindShop(pc)) {
                pc.setAutoStatus(AUTO_STATUS_SETTING);
            }
            return;
        }
        
        if (weapon.get_durability() > 0) {
			pc.getInventory().recoveryDamage(weapon);
			if (weapon.get_durability() == 0) {
				pc.sendPackets(new S_ServerMessage(464, weapon.getLogName())); // %0%s는 신품 같은 상태가 되었습니다.
			} else {
				pc.sendPackets(new S_ServerMessage(463, weapon.getLogName())); // %0 상태가 좋아졌습니다.
			}
        	pc.getInventory().removeItem(item, 1);
        }	
    }
    private int _cnt = 0;
   

	private void toUseHealingMagic(L1PcInstance pc) {
		if(!pc.isWizard()) return;
		int _hp = (int) (((double) pc.getCurrentHp() / (double) pc.getMaxHp()) * 100);
		L1SkillUse _skilluse;
		L1Skills _skill;
		if(_hp <= 70) {
			int _mp = (int) (((double) pc.getCurrentMp() / (double) pc.getMaxMp()) * 100);
			if(_mp <= 30) return;
			if(!SkillCheck.getInstance().CheckSkill(pc, L1SkillId.GREATER_HEAL)) return;
			long current = System.currentTimeMillis();
			if (current - pc.getAutoSkillDelay() < 0) 
				return;
			
			_skill = SkillsTable.getInstance().getTemplate(L1SkillId.GREATER_HEAL);
			_skilluse = new L1SkillUse();
			_skilluse.handleCommands(pc, L1SkillId.GREATER_HEAL, pc.getId(),
					pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
			pc.setAutoSkillDelay(System.currentTimeMillis() + _skill.getReuseDelay() + MAGIC_PENALTY);
		}
	}
	
    /** 몬스터 사냥 **/
	private void toAttackMonster(L1PcInstance pc) {	
		try {
			
			if (pc.isstop() || pc.isSleeped() || pc.isParalyzed() || pc.isDead()) {
                return;
            }
			if(!isAutoAttackTime(pc))  {
				return;
			}
			L1Character target = pc.getAutoTarget();
			if (target != null && target.isDead()) {
				pc.removeAutoTargetList(target);
				pc.setAutoTarget(null);
				return;
			}

			if(pc.getAutoTarget() ==null) {
				pc.setAutoTarget(getTarget(pc));
			}

			if(!isAttack(pc, target)) {
				pc.removeAutoTargetList(target);
				pc.setAutoTarget(null);
			}			

			if (pc.getAutoTarget() == null) {
				pc.setAutoStatus(AUTO_STATUS_WALK);					
				return;
			}
			
			if (isDistance(pc.getX(), pc.getY(), pc.getMapId(), target.getX(), target.getY(), target.getMapId(), pc.getAttackRang())) {					
				if (pc.glanceCheck(target.getX(), target.getY())) {
					toAttack(pc);				
				} else {
					toMoving(pc, target.getX(), target.getY(), 0, true);					
				}
			} else {				
				toMoving(pc, target.getX(), target.getY(), 0, true);	
			}			
		}catch(Exception e) {
			pc.removeAutoTargetList(pc.getAutoTarget());
			pc.setAutoTarget(null);
		}
	}

	public boolean isAttack(L1PcInstance pc, L1Character cha) {
		try {
			if (cha == null)
				return false;

			if (cha.hasSkillEffect(L1SkillId.EARTH_BIND) || 
					cha.hasSkillEffect(L1SkillId.ICE_LANCE)) {
				return false;
			}
			if (cha.getMap().isSafetyZone(cha.getLocation()))
				return false;		
			if (cha.isDead())
				return false;

			if (cha.isInvisble())
				return false;
			if (!isDistance(pc.getX(), pc.getY(), pc.getMapId(), cha.getX(), cha.getY(), cha.getMapId(), 16))
				return false;

			if (!pc.glanceCheck(cha.getX(), cha.getY())) 
				return false;

			return true;
		} catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
	}
	
    /** 공격 실행 **/
	private void toAttack(L1PcInstance pc) {
		try {
			L1Character target = pc.getAutoTarget();
			if (target == null) {
				pc.getAutoTargetList().clear();
				pc.setAutoStatus(AUTO_STATUS_WALK);
				return;
			}			

			if (pc.hasSkillEffect(L1SkillId.MEDITATION)) {
				pc.killSkillEffectTimer(L1SkillId.MEDITATION);
			}
			   if (pc.isInvisble()){
	            	pc.delInvis();
	            }	
			if(pc.isElf() && pc.getWeapon().getItem().getType1() == 20) {
				if(!pc.getInventory().checkItem(3000516)) {					
					returnScroll(pc);
					return;
				}
				int chance = _rnd.nextInt(100) + 1;
				if(chance <= 30)
					this.toTripleArrow(pc, target);
				else {
					target.onAction(pc);
				}
			}else {
				target.onAction(pc);
			}
		} catch (Exception e) {
			pc.setAutoTarget(null);
			pc.getAutoTargetList().clear();
		}
	}

	private L1Character getTarget(L1PcInstance pc) {		
		L1Character realTarget = null;
		try {	
			for (int i = 0; i < pc.getAutoTargetList().toTargetArrayList().size(); i++) {
				L1Character target = pc.getAutoTargetList().toTargetArrayList().get(i);
				if (target.isDead()) {
					pc.removeAutoTargetList(target);			
					pc.setAutoTarget(null);
					continue;
				}
				if (!pc.glanceCheck(target.getX(), target.getY())) {
					pc.removeAutoTargetList(target);			
					pc.setAutoTarget(null);
					continue;
				}
				if (realTarget == null) {
					realTarget = target;
				} else if (!target.isDead() &&  getDistance(pc.getX(), pc.getY(), target.getX(), target.getY()) < getDistance(pc.getX(), pc.getY(), realTarget.getX(), realTarget.getY())) {				
					realTarget = target;
				}
			}
			return realTarget;
		}catch(Exception e) {
			e.printStackTrace();
			pc.getAutoTargetList().clear();
			pc.setAutoTarget(null);
			return realTarget;
		}
	}

	private boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
		int distance = getDistance(x, y, tx, ty);
		if (loc < distance)
			return false;
		if (m != tm)
			return false;
		return true;
	}
	private int getDistance(int x, int y, int tx, int ty) {
		long dx = tx - x;
		long dy = ty - y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	private void searchTarget(L1PcInstance pc) {
		checkTarget(pc);
		for(L1Object obj : L1World.getInstance().getVisibleObjects(pc)) {
			if (obj == null) {
				continue;
			}
			   /** 몬스터만 검색 **/
			if(obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.isDead()) {
					continue;
				}
				if (mon.getHiddenStatus() >= 1) {
					continue;
				}

				if (!pc.glanceCheck(mon.getX(), mon.getY())) {
					continue;
				}				
				pc.addAutoTargetList(mon);				
			}
		}
	}
	public void checkTarget(L1PcInstance pc) {
		try {
			L1Character target = pc.getAutoTarget();
			if (target == null || target.getMapId() != pc.getMapId() || target.isDead() || target.getCurrentHp() <= 0
					|| (target.isInvisble() && !pc.getAutoTargetList().containsKey(target))) {
				if (target != null) {
					tagertClear(pc);
				}

				if (!pc.getAutoTargetList().isEmpty()) {
					pc.setAutoTarget(pc.getAutoTargetList().getMaxHateCharacter());
					checkTarget(pc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void tagertClear(L1PcInstance pc) {
		L1Character target = pc.getAutoTarget();
		if (target == null) {
			return;
		}
		pc.getAutoTargetList().remove(target);
		pc.setAutoTarget(null);
	}
	public void setHate(L1PcInstance pc, L1Character cha, int hate) {
		if (cha != null && cha.getId() != pc.getId()) {
			if (!pc.getAutoTargetList().containsKey(cha)) {
				pc.getAutoTargetList().add(cha, hate);
			}
			if (pc.getAutoTarget() == null) {
				pc.setAutoTarget(pc.getAutoTargetList().getMaxHateCharacter());
			}
			checkTarget(pc);
		}
	}


	private boolean isFindShop(L1PcInstance pc) {
		/*
		L1NpcInstance shop = null;
		for(L1NpcInstance npc : L1World.getInstance().getAllNpc()) {
			if(npc.getNpcId() == _shopNpcId && npc.getMapId() == 4) {//로봇잡화상점 맵
				shop = npc;
				break;
			}
		}*/

		L1NpcInstance shop = find_npc(_shopNpcId, 4);
		if(shop != null && isDistance(pc.getX(), pc.getY(), pc.getMapId(), shop.getX(), shop.getY(), shop.getMapId(), 3)) {
			return true;
		}
		return false;
	}
	
	private L1NpcInstance find_npc(int npc_id, int map_id){
		Map<Integer, L1Object> col = L1World.getInstance().getVisibleObjects(map_id);
		for(L1Object obj : col.values()){
			if(obj instanceof L1NpcInstance){
				L1NpcInstance npc = (L1NpcInstance)obj;
				if(npc.getNpcId() == npc_id){
					return npc;
				}
			}
		}
		return null;
	}
	/** 상점 이동 **/
    private void toShopWalk(L1PcInstance pc) {
		if(!isAutoMoveTime(pc)) return;
		L1NpcInstance shop = find_npc(_shopNpcId, 4);
		/*
		L1NpcInstance shop = null;
		for(L1NpcInstance npc : L1World.getInstance().getAllNpc()) {
			if(npc.getNpcId() == _shopNpcId){
				System.out.println(npc.getName() + " " + npc.getMapId());
			}
			if(npc.getNpcId() == _shopNpcId && npc.getMapId() == 4) {//로봇잡화상점 맵
				System.out.println(npc.getName());
				shop = npc;
				break;
			}
		}*/
		if(shop == null){
			return;
		}
		int x = shop.getX();
		int y = shop.getY();
		int dir = moveDirection(pc, x, y);
		dir = checkObject(pc.getX(), pc.getY(), pc.getMapId(), dir);
		if (!isDistance(pc.getX(), pc.getY(), pc.getMapId(), x, y, shop.getMapId(), 3)) {
			if(pc.getAutoMoveCount() == 0) {							
				toCharacterRefresh(pc);
			}
			pc.setAutoMoveCount(pc.getAutoMoveCount()+1);
			if(pc.getAutoMoveCount() >= 7) {
				pc.setAutoMoveCount(0);
			}
			setDirectionMove(pc, dir);
		}else {			
			pc.setAutoStatus(AUTO_STATUS_SETTING);
		}
	}
	private void toRandomWalk(L1PcInstance pc) {
		if(!isAutoMoveTime(pc)) return;

		if(pc.getAutoMoveCount() == 0) {
			int randomLocX = (int) ((Math.random() * 20) - 10);
			int randomLocY = (int) ((Math.random() * 20) - 10);
			int _locX = pc.getX() + randomLocX;
			int _locY = pc.getY() + randomLocY;	
			pc.setAutoLocX(_locX);
			pc.setAutoLocY(_locY);				
		}		
		int dir = pc.targetDirection(pc.getAutoLocX(), pc.getAutoLocY());		
		toMoving(pc, pc.getAutoLocX(), pc.getAutoLocY(), dir, true);
	}

	private void toMoving(L1PcInstance pc, int x, int y, int h, boolean astar) {	
		try {
			if (pc.isstop() || pc.isSleeped() || pc.isParalyzed() || pc.isDead() 
            		|| pc.hasSkillEffect(L1SkillId.THUNDER_GRAB)) {
                return;
            }
			
			if (astar) {
				pc.getAutoAstar().ResetPath();
				pc.setAutoTail(pc.getAutoAstar().FindPath(pc, x, y, pc.getMapId(), null));
				if (pc.getAutoTail() != null) {
					pc._autoCurrentPath = -1;					
					while (pc.getAutoTail() != null) {
						if (pc.getAutoTail().x == pc.getX() && pc.getAutoTail().y == pc.getY()) {						
							break;
						}
						pc.getAutoPath()[++pc._autoCurrentPath][0] = pc.getAutoTail().x;
						pc.getAutoPath()[pc._autoCurrentPath][1] = pc.getAutoTail().y;
						pc.setAutoTail(pc.getAutoTail().prev);
					}				
					if(pc._autoCurrentPath == -1) return;				
					toMoving(pc, pc.getAutoPath()[pc._autoCurrentPath][0], pc.getAutoPath()[pc._autoCurrentPath][1], calcheading(pc.getX(), pc.getY(), pc.getAutoPath()[pc._autoCurrentPath][0], pc.getAutoPath()[pc._autoCurrentPath][1]));

				} else {}
			} else {						
				toMoving(pc, x, y, h);
			}
		}catch(Exception e) {

		}
	}
	private void toMoving(L1PcInstance pc, final int x, final int y, final int h) {
		try {			
			pc.getMap().setPassable(pc.getLocation(), true);
			pc.getLocation().set(x, y);
			pc.setHeading(h);
			L1WorldTraps.getInstance().onPlayerMoved(pc);		
			pc.getMap().setPassable(pc.getLocation(), false);
			pc.sendPackets(new S_MoveCharPacket(pc));
			Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc));
			pc.setAutoMoveCount(pc.getAutoMoveCount()+1);
			if(pc.getAutoMoveCount() >= 7) {
				pc.setAutoMoveCount(0);
				toCharacterRefresh(pc);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}

	public int checkObject(int x, int y, short m, int d) {
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch (d) {
		case 1:
			if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			}
			break;
		case 2:
			if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			}
			break;
		case 3:
			if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			}
			break;
		case 4:
			if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			}
			break;
		case 5:
			if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			}
			break;
		case 6:
			if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			}
			break;
		case 7:
			if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			}
			break;
		case 0:
			if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			}
			break;
		default:
			break;
		}
		return -1;
	}

	

	private void setDirectionMove(L1PcInstance pc, int dir) {
		if (dir >= 0) {
			int nx = 0;
			int ny = 0;

			int heading = 0;
			nx = HEADING_TABLE_X[dir];
			ny = HEADING_TABLE_Y[dir];
			heading = dir;

			pc.setHeading(heading);
			pc.getMap().setPassable(pc.getLocation(), true);

			int nnx = pc.getX() + nx;
			int nny = pc.getY() + ny;
			pc.setX(nnx);
			pc.setY(nny);
			L1WorldTraps.getInstance().onPlayerMoved(pc);		
			pc.getMap().setPassable(pc.getLocation(), false);

			pc.sendPackets(new S_MoveCharPacket(pc));
			Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc));
		}
	}

	public int moveDirection(L1PcInstance pc, int x, int y)  {
		return moveDirection(pc, x, y, pc.getLocation().getLineDistance(new Point(x, y)));
	}

	public int moveDirection(L1PcInstance pc, int x, int y, double d) { 
		int dir = 0;
		if (d > 25) {
			dir = pc.targetDirection(x, y);
			dir = checkObject(pc.getX(), pc.getY(), pc.getMapId(), dir);
		} else { 
			dir = _serchCource(pc, x, y);
			if (dir == -1) { 
				dir = pc.targetDirection(x, y);
			}
		}
		return dir;
	}

	private int _serchCource(L1PcInstance pc, int x, int y){
		int i;
		int locCenter = 25 + 1;
		int diff_x = x - locCenter; 
		int diff_y = y - locCenter; 
		int[] locBace = { pc.getX() - diff_x, pc.getY() - diff_y, 0, 0 };
		int[] locNext = new int[4];
		int[] locCopy;
		int[] dirFront = new int[5];
		boolean serchMap[][] = new boolean[locCenter * 2 + 1][locCenter * 2 + 1];
		LinkedList<int[]> queueSerch = new LinkedList<int[]>();

		for (int j = 25 * 2 + 1; j > 0; j--) {
			for (i = 25 - Math.abs(locCenter - j); i >= 0; i--) {
				serchMap[j][locCenter + i] = true;
				serchMap[j][locCenter - i] = true;
			}
		}
		int[] firstCource = { 2, 4, 6, 0, 1, 3, 5, 7 };
		for (i = 0; i < 8; i++) {
			System.arraycopy(locBace, 0, locNext, 0, 4);
			_moveLocation(locNext, firstCource[i]);
			if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
				return firstCource[i];
			}
			if (serchMap[locNext[0]][locNext[1]]) {
				int tmpX = locNext[0] + diff_x;
				int tmpY = locNext[1] + diff_y;
				boolean found = false;
				switch(i){
				case 0: found = pc.getMap().isPassable(tmpX, tmpY + 1, i); break;
				case 1: found = pc.getMap().isPassable(tmpX - 1, tmpY + 1, i); break;
				case 2: found = pc.getMap().isPassable(tmpX - 1, tmpY, i); break;
				case 3: found = pc.getMap().isPassable(tmpX - 1, tmpY - 1, i); break;
				case 4: found = pc.getMap().isPassable(tmpX, tmpY - 1, i); break;
				case 5: found = pc.getMap().isPassable(tmpX + 1, tmpY - 1, i); break;
				case 6: found = pc.getMap().isPassable(tmpX + 1, tmpY, i); break;
				case 7: found = pc.getMap().isPassable(tmpX + 1, tmpY + 1, i); break;
				default: break;
				}
				if (found){
					locCopy = new int[4];
					System.arraycopy(locNext, 0, locCopy, 0, 4);
					locCopy[2] = firstCource[i];
					locCopy[3] = firstCource[i];
					queueSerch.add(locCopy);
				}
				serchMap[locNext[0]][locNext[1]] = false;
			}
		}
		locBace = null;
		while (queueSerch.size() > 0) {
			locBace = queueSerch.removeFirst();
			_getFront(dirFront, locBace[2]);
			for (i = 4; i >= 0; i--) {
				System.arraycopy(locBace, 0, locNext, 0, 4);
				_moveLocation(locNext, dirFront[i]);
				if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
					return locNext[3];
				}
				if (serchMap[locNext[0]][locNext[1]]) {
					int tmpX = locNext[0] + diff_x;
					int tmpY = locNext[1] + diff_y;
					boolean found = false;
					switch(i){
					case 0: found = pc.getMap().isPassable(tmpX, tmpY + 1, i); break;
					case 1: found = pc.getMap().isPassable(tmpX - 1, tmpY + 1, i); break;
					case 2: found = pc.getMap().isPassable(tmpX - 1, tmpY, i); break;
					case 3: found = pc.getMap().isPassable(tmpX - 1, tmpY - 1, i); break;
					case 4: found = pc.getMap().isPassable(tmpX, tmpY - 1, i); break;
					default: break;
					}
					if (found){
						locCopy = new int[4];
						System.arraycopy(locNext, 0, locCopy, 0, 4);
						locCopy[2] = dirFront[i];
						queueSerch.add(locCopy);
					}
					serchMap[locNext[0]][locNext[1]] = false;
				}
			}
			locBace = null;
		}
		return -1;
	}

	private void _getFront(int[] ary, int d) {
		switch(d){
		case 1:
			ary[4] = 2;
			ary[3] = 0;
			ary[2] = 1;
			ary[1] = 3;
			ary[0] = 7;
			break;
		case 2:
			ary[4] = 2;
			ary[3] = 4;
			ary[2] = 0;
			ary[1] = 1;
			ary[0] = 3;
			break;
		case 3:
			ary[4] = 2;
			ary[3] = 4;
			ary[2] = 1;
			ary[1] = 3;
			ary[0] = 5;
			break;
		case 4:
			ary[4] = 2;
			ary[3] = 4;
			ary[2] = 6;
			ary[1] = 3;
			ary[0] = 5;
			break;
		case 5:
			ary[4] = 4;
			ary[3] = 6;
			ary[2] = 3;
			ary[1] = 5;
			ary[0] = 7;
			break;
		case 6:
			ary[4] = 4;
			ary[3] = 6;
			ary[2] = 0;
			ary[1] = 5;
			ary[0] = 7;
			break;
		case 7:
			ary[4] = 6;
			ary[3] = 0;
			ary[2] = 1;
			ary[1] = 5;
			ary[0] = 7;
			break;
		case 0:
			ary[4] = 2;
			ary[3] = 6;
			ary[2] = 0;
			ary[1] = 1;
			ary[0] = 7;
			break;
		default:
			break;
		}
	}

	private void _moveLocation(int[] ary, int d) {
		ary[0] = ary[0] + HEADING_TABLE_X[d];
		ary[1] = ary[1] + HEADING_TABLE_Y[d];
		ary[2] = d;
	}

	private void cancelAbsoluteBarrier(L1PcInstance pc) {
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}
	
	/** 아이템 세팅 후 사냥터 이동 **/
	private void toSettingBuyShop(L1PcInstance pc) {
		int healId = pc.getAutoPotion();
		L1ShopItem shopItem;
		int price;		
		L1Shop shop = ShopTable.getInstance().get(_shopNpcId);
		
		/**통합적으로 사는 아이템 [잡화상점에 있어야함] 모든클래스: 강촐, 순간이동주문서, 기란귀환, 변줌, 허브, 결정체, 스프, 해독제, 농축마력, 숫돌 **/
		 // 공통구매
		int[] allItemIds = {30158,	// 농축 속도의 물약
							40100,	// 순간이동 주문서
							40081,	// 기란 귀환 주문서
							40088,	// 변신 주문서
							210039,	// 허브
							41246,	// 결정체
							30054,	// 수련의 닭고기 스프
							40017,	// 해독제
							210114,	// 농축 마력의 물약
							40317};	// 숫돌
		
		// 수량
		int[] allCounts = {10, 900, 50, 10, 20, 10, 10, 10, 20, 100};
	    // 클래스별 아이템 배열 초기화
	    int[] classItemId = null;
	    int[] classItemCount = null;
	    
		int currentCount;
		int needCount;
		L1PcInventory inv = pc.getInventory();

		
		 // 클래스별구매
        if (pc.isCrown() || pc.isKnight() || pc.is전사()) { // 군주 기사 전사
            classItemId = new int[] { healId, 41415, 30051 }; // 회복제, 농축용기, 힘센한우스테이크
            classItemCount = new int[] { 500, 10, 10 }; // 수량
			
		}else if(pc.isDragonknight()) { // 용기사
			  classItemId = new int[] { healId, 210035, 30051 }; // 회복제, 각인의뼈조각, 힘센한우스테이크
	            classItemCount = new int[] { 500, 100, 10 }; // 수량
			
		}else if(pc.isElf()) { // 요정
			/** 검요정 **/
			if (pc.getElfAttr() == 1 && SkillCheck.getInstance().CheckSkill(pc, L1SkillId.SAND_STORM)
        			&& (pc.getWeapon().getItem().getType() == 1 || pc.getWeapon().getItem().getType() == 2)){ // 스킬 근거리
        		classItemId = new int[] { healId, 40068, 40319, 30051 }; // 회복제, 엘븐와퍼, 정령옥, 힘센한우스테이크
                classItemCount = new int[] { 500, 10, 200, 10 }; // 수량
        	} else if (pc.getElfAttr() == 2 && SkillCheck.getInstance().CheckSkill(pc, L1SkillId.DANCING_BLADES)
        			&& (pc.getWeapon().getItem().getType() == 1 || pc.getWeapon().getItem().getType() == 2)){ // 스킬 근거리
        		classItemId = new int[] { healId, 40068, 40319, 30051 }; // 회복제, 엘븐와퍼, 정령옥, 힘센한우스테이크
                classItemCount = new int[] { 500, 10, 200, 10 }; // 수량
                
            /** 활요정 **/
        	} else if (pc.getElfAttr() == 4 && SkillCheck.getInstance().CheckSkill(pc, L1SkillId.FOCUS_WAVE)
                	&& pc.getWeapon().getItem().getType1() == 20){ // 스킬 원거리
        		classItemId = new int[] { healId, 40068, 31175, 40319, 30052 }; // 회복제, 엘븐와퍼, 화살, 정령옥, 날샌연어찜
                classItemCount = new int[] { 500, 10, 1000, 200, 10 }; // 수량
        	} else if (pc.getElfAttr() == 8 && SkillCheck.getInstance().CheckSkill(pc, L1SkillId.HURRICANE)
        			&& pc.getWeapon().getItem().getType1() == 20){ // 스킬 원거리
        		classItemId = new int[] { healId, 40068, 31175, 40319, 30052 }; // 회복제, 엘븐와퍼, 화살, 정령옥, 날샌연어찜
                classItemCount = new int[] { 500, 10, 1000, 200, 10 }; // 수량
                
            /** 노스킬 활요정 **/    
        	} else if (pc.getWeapon().getItem().getType1() == 20){ // 원거리
        		classItemId = new int[] { healId, 210110, 31175, 40319, 30052 }; // 회복제, 농축집중, 화살, 정령옥, 날샌연어찜
                classItemCount = new int[] { 500, 10, 1000, 200, 10 }; // 수량
                
            /** 노스킬 검요정 **/    
        	} else { // 근거리
        		classItemId = new int[] { healId, 210110, 40319, 30051 }; // 회복제, 농축집중, 정령옥, 힘센한우스테이크
                classItemCount = new int[] { 500, 10, 200, 10 }; // 수량
        	}
			
		}else if(pc.isDarkelf()) { // 다크엘프
			   classItemId = new int[] { healId, 40321, 30051 }; // 회복제, 흑요석, 힘센한우스테이크
	            classItemCount = new int[] { 500, 50, 10 }; // 수량
	            
		}else if(pc.isBlackwizard()) { // 환술사
			   classItemId = new int[] { healId, 713, 210113, 210038, 30053 }; // 회복제, 농축유그드라, 농축지혜, 속성석, 영리한칠면조구이
	            classItemCount = new int[] { 500, 10, 10, 50, 10 }; // 수량
	            
		}else if(pc.isWizard()) { // 법사
			 classItemId = new int[] { healId, 210113, 40318, 30053 }; // 회복제, 농축지혜, 마력의돌, 영리한칠면조구이
	            classItemCount = new int[] { 500, 10, 100, 10 }; // 수량
		}
        
        /** 신속물약 갯수 반감 **/
        if (healId >= 40022 && healId <= 40024){
        	classItemCount[0] /= 2; 
        }

	

		    /** 아이템 셋팅 **/
	     /*   for (int i = 0; i < allItemIds.length; ++i) {
	            if (pc.getAutoPolyID() != 0 || allItemIds[i] != 40088) {
	                int currentCount = inv.checkItemCount(allItemIds[i]);
	                if (currentCount < allCounts[i]) {
	                    int needCount = allCounts[i] - currentCount;
	                    L1ShopItem shopItem = shop.getSellItem(allItemIds[i]);
	                    if (shopItem != null){
	                    	int price = shopItem.getPrice();
	                        price *= needCount;
	                        if (!inv.consumeItem(40308, price)) {
	                        	pc.sendPackets(new S_SystemMessage("자동종료: 아데나가 부족합니다."));
	                            removeAuto(pc);
	                            return;
	                        }
	                        inv.storeItem(allItemIds[i], needCount);
	                    } else {
	                    	System.out.println("[AutoHunt] ShopItemID : " + allItemIds[i] + " Not Find");
	                    }
	                }
	            }
	        }*/
	        /*
	        for (int i = 0; i < classItemId.length; ++i) {
	            int currentCount = inv.checkItemCount(classItemId[i]);
	            if (currentCount < classItemCount[i]) {
	                int needCount = classItemCount[i] - currentCount;
	                L1ShopItem shopItem = shop.getSellItem(classItemId[i]);
	                if (shopItem != null){
	                	int price = shopItem.getPrice();
	                    price *= needCount;
	                    if (!inv.consumeItem(40308, price)) {
	                    	pc.sendPackets(new S_SystemMessage("자동종료: 아데나가 부족합니다."));
	                        removeAuto(pc);
	                        return;
	                    }
	                    inv.storeItem(classItemId[i], needCount);
	                } else {
	                	System.out.println("[AutoHunt] ShopItemID : " + classItemId[i] + " Not Find");
	                }
	            }
	        }*/
	        
	    	if(!buy_items(pc, allItemIds, allCounts))
				return;
			
			if(!buy_items(pc, classItemId, classItemCount))
				return;
	        
		 /** 사냥터 이동 **/
		int rx = _rnd.nextInt(1); //범위
		int ry = _rnd.nextInt(1); //범위
//		int ux = 32742 + rx;
//		int uy = 32857 + ry;
	    int ux = Config.자동사냥도착_X + rx;
	    int uy = Config.자동사냥도착_Y + ry;
		
		if(!check_auto_dungeon(pc))
		{
			pc.sendPackets("던전 이용 시간이 종료되었습니다.");
			pc.sendPackets("던전 시간 충전 아이템을 구매해 주세요.");
			removeAuto(pc);
			return;
		}
		teleport(pc, ux, uy, (short)pc.getAutoMapId());
//		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "현재 육성 사냥터에서는 PK가 불가능합니다."));
//		pc.sendPackets(new S_TestPacket(S_TestPacket.a, 6302, 2083, "00 ff ff"));//현재사냥터는 PK가불가능합니다.
	}
	
	private boolean buy_items(L1PcInstance pc, int[] item_id_array, int[] item_count_array){
	    // 배열 길이 확인
	    if(item_id_array.length != item_count_array.length) {
	        System.out.println("[에러] 아이템 ID 배열과 아이템 수량 배열의 길이가 서로 다릅니다.");
	        return false;
	    }
		int t_price = 0;
		L1PcInventory inv = pc.getInventory();
		L1ShopItem shopItem;
		L1Shop shop = ShopTable.getInstance().get(_shopNpcId);
		if (shop == null) {
		    System.out.println("[에러] 상점 ID: " + _shopNpcId + ", 해당 상점을 찾을 수 없습니다.");
		    return false; // 메서드 종료
		}
		ArrayList<SimplePurchasingItemFormat> purchasing_formats = new ArrayList<SimplePurchasingItemFormat>();
		for(int i = 0; i < item_id_array.length; i++) {
			if(pc.getAutoPolyID() == 0 && item_id_array[i] == 40088) 
				continue;
			
			int currentCount = inv.checkItemCount(item_id_array[i]);
	        // 여기서 발생하는 ArrayIndexOutOfBoundsException을 방지하기 위해 추가된 검사
	        if(i >= item_count_array.length) {
	            System.out.println("[에러] 아이템 수량 배열의 인덱스 범위를 초과했습니다. 인덱스: " + i);
	            break; // 더 이상 처리하지 않고 반복문을 종료합니다.
	        }			
			if(currentCount < item_count_array[i]) {
				SimplePurchasingItemFormat f = new SimplePurchasingItemFormat();
				f.needCount = item_count_array[i] - currentCount;
				f.itemId = item_id_array[i];
				shopItem = shop.getSellingItem(f.itemId);
				if (shopItem == null) {
				    System.out.println("[아이템 구매 로그] 아이템 ID: " + f.itemId + ", 이 상점에서 판매되지 않습니다.");
				    continue; // 다음 아이템으로 넘어갑니다.
				}
				f.price = shopItem.getPrice();
	             // 로그 추가 - 아이템 가격 계산
                System.out.println(String.format("[아이템 구매 로그] 아이템 ID: %d, 단가: %d, 필요 수량: %d", f.itemId, shopItem.getPrice(), f.needCount));
				f.price *= f.needCount;
				t_price += f.price;		
				purchasing_formats.add(f);
			}
		}	
		if(t_price > 0 && purchasing_formats.size() > 0){
			if(inv.consumeItem(L1ItemId.ADENA, t_price)){
				for(SimplePurchasingItemFormat f : purchasing_formats)
					inv.storeItem(f.itemId, f.needCount);
			}else {
				pc.sendPackets(String.format("아데나 %d가 부족하여 자동 사냥이 종료되었습니다.", t_price));
				this.removeAuto(pc);
				return false;
			}
		}
		return true;
	}

	static class SimplePurchasingItemFormat{
		int itemId;
		int needCount;
		int price;
	}	
	
	  /** 주문서 사용 **/
	private void toUseScroll(L1PcInstance pc, int itemId) {
		L1ItemInstance item = pc.getInventory().findItemId(itemId);
		if(item == null) {
			if(itemId == 40100 || itemId == 40088) {
				if(pc.getAutoStatus() == AUTO_STATUS_WALK || pc.getAutoStatus() == AUTO_STATUS_ATTACK) {
					returnScroll(pc);
				}else {
					if(isFindShop(pc)) {
						pc.setAutoStatus(AUTO_STATUS_SETTING);
					}
				}
			}
			return;
		}
		if(!isUseCheck(pc, item)) return;
		switch(itemId) {
		case 40081: // 기란 귀환 주문서
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				//pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, true);
				teleport(pc,loc[0], loc[1], (short)loc[2]);
				pc.getInventory().removeItem(item, 1);
				cancelAbsoluteBarrier(pc);
				L1ItemDelay.onItemUse(pc, item);
			}
			break;
		case 40100: // 순간이동 주문서
			//Telbookitem.clickItem(pc, itemId, 0, 0,	(short)0, item);
			L1Location newLocation = pc.getLocation().randomLocation(200, true);
			int newX = newLocation.getX();
			int newY = newLocation.getY();
			short mapId = (short) newLocation.getMapId();

			this.teleport(pc, newX, newY, mapId);
			pc.getInventory().removeItem(item, 1);
			L1ItemDelay.onItemUse(pc, item);
			break;
		case 40088:
			L1PolyMorph.doPoly(pc, pc.getAutoPolyID(), 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
			pc.getInventory().removeItem(item, 1);
			L1ItemDelay.onItemUse(pc, item);
			break;
		}	
	}

	private boolean isAutoAttackTime(L1PcInstance pc) {
		long temp = System.currentTimeMillis() - pc.getAutoTimeAttack();
		if (pc.hasSkillEffect(L1SkillId.EARTH_BIND) || 
				pc.hasSkillEffect(L1SkillId.SHOCK_STUN) || 
				pc.hasSkillEffect(L1SkillId.ICE_LANCE)  || 
				pc.hasSkillEffect(L1SkillId.CURSE_PARALYZE) || 
				pc.hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
				pc.hasSkillEffect(L1SkillId.BONE_BREAK)   || 
				pc.hasSkillEffect(L1SkillId.PHANTASM) || 
				pc.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
			return false;
		}
		long interval = pc.getCurrentSpriteInterval(EActionCodes.fromInt(pc.getCurrentWeapon() + 1));
		if(temp < interval) {
			return false;
		}		
		if (temp >= interval) {
			pc.setAutoTimeAttack(System.currentTimeMillis());
			return true;
		}			
		return false;
	}
	private boolean isAutoMoveTime(L1PcInstance pc) {
		long temp = System.currentTimeMillis() - pc.getAutoTimeMove();
		if (pc.hasSkillEffect(L1SkillId.EARTH_BIND) || 
				pc.hasSkillEffect(L1SkillId.SHOCK_STUN) || 
				pc.hasSkillEffect(L1SkillId.ICE_LANCE)  || 
				pc.hasSkillEffect(L1SkillId.CURSE_PARALYZE) || 
				pc.hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
				pc.hasSkillEffect(L1SkillId.BONE_BREAK)   || 
				pc.hasSkillEffect(L1SkillId.PHANTASM) || 
				pc.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
			return false;
		}
		long interval = pc.getCurrentSpriteInterval(EActionCodes.fromInt(pc.getCurrentWeapon()));

		if(temp < interval) {
			return false;
		}		
		if (temp >= interval) {
			pc.setAutoTimeMove(System.currentTimeMillis());
			return true;
		}		
		return false;
	}
	
	  /** 타겟 없을시 **/
	private void noTargetTeleport(L1PcInstance pc) {
		if(pc.getAutoAiTime() == 0) {
			pc.setAutoAiTime(System.currentTimeMillis());
		}else {
			if(pc.getAutoTargetList().toTargetArrayList().size() == 0 &&
					System.currentTimeMillis() >= pc.getAutoAiTime() + 3000) {	
				toUseScroll(pc, 40100);
				pc.setAutoAiTime(System.currentTimeMillis());
			}
		}
	}
	
	 /** 리스타트 **/
	private void restart(L1PcInstance pc) {		
		if (pc == null) {
			return;
		}
		if(pc.getAutoDeadTime() < 500) {
			pc.setAutoDeadTime(pc.getAutoDeadTime() + 1);
			return;
		}
		if (!pc.isDead()) {
			return;
		}
		int[] loc;
		loc = Getback.GetBack_Restart(pc);
		if (pc.getHellTime() > 0) {
			loc = new int[3];
			loc[0] = 32701;
			loc[1] = 32777;
			loc[2] = 666;
		} else {
			loc = Getback.GetBack_Location(pc, true);
		}
		pc.removeAllKnownObjects();
		pc.broadcastPacket(new S_RemoveObject(pc));
		pc.setCurrentHp(pc.getLevel());
		pc.set_food(39); 
		pc.setDead(false);
		pc.setAutoDead(true);
		pc.setStatus(0);
		L1World.getInstance().moveVisibleObject(pc, loc[2]);
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);
		pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
		MJExpAmplifierLoader.getInstance().set(pc);
		//pc.broadcastPacket(new S_OtherCharPacks(pc));
		pc.broadcastPacket(S_WorldPutObject.get(pc));
		pc.sendPackets(S_WorldPutObject.put(pc));
		//pc.sendPackets(new S_OwnCharPack(pc));
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.startHpMpRegeneration();
		pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));
		pc.setAutoDeadTime(0);
		pc.setAutoStatus(AUTO_STATUS_MOVE_SHOP);
	}

	 /** 시작 **/
	   public void addAuto(L1PcInstance pc) {
	        if (_pcList.contains(pc)) {
	            return;
	        }
	        _pcList.add(pc);
	    }
	   
	/** 종료 **/
	   public void removeAuto(L1PcInstance pc) {
		    if (!_pcList.contains(pc)) return; // 자동 사냥 리스트에 플레이어가 없는 경우 종료
		    _pcList.remove(pc); // 리스트에서 플레이어 제거
		    toCharacterRefresh(pc); // 캐릭터 상태 갱신

		    if (pc != null) {
		        resetAuto(pc); // 자동 사냥 설정 초기화
		        
		        // 자동 사냥 종료 시 사운드 스킬 전송
		        pc.sendPackets(new S_SkillSound(pc.getId(), 16771)); // 자동 사냥을 중지합니다.

		        // 타이머 및 태스크 준비
		        Timer timer = new Timer();
		        TimerTask task = new TimerTask() {
		            int countdown = 3;

		            public void run() {
		                if (countdown > 0) {
		                    pc.sendPackets(new S_ChatPacket(pc, countdown + "초 후 자동 사냥이 종료됩니다.", 1));
		                    countdown--;
		                } else {
		                    // 자동 사냥 종료 메시지 전송
		                    pc.sendPackets(new S_ChatPacket(pc, "자동 사냥이 종료되었습니다.", 1));

		                    // 랜덤 텔레포트 좌표 생성
		                    Random random = new Random(System.nanoTime());
		                    int locx = 33433 + random.nextInt(10);
		                    int locy = 32809 + random.nextInt(10);

		                    // 텔레포트 실행
		                    pc.start_teleport(locx, locy, 4, 5, 169, true, false);

		                    timer.cancel(); // 타이머 중지
		                }
		            }
		        };

		        // 타이머 스케줄 설정
		        timer.scheduleAtFixedRate(task, 0, 1000); // 즉시 시작하여 1초마다 반복
		    }
		}
	   
	public void resetAuto(L1PcInstance pc) {
		pc.setAutoStatus(AUTO_STATUS_NONE);
		pc.setAutoDead(false);
		pc.setAutoDeadTime(0);
		pc.setAutoMoveCount(0);
		pc.setAutoSkillDelay(0);		
		pc.setAutoTarget(null);
		pc.getAutoTargetList().clear();
		pc.setAutoAiTime(0);	
	}

	private void toCharacterRefresh(L1PcInstance pc) {
		pc.broadcastRemoveAllKnownObjects();
		pc.removeAllKnownObjects();
		pc.sendPackets(S_WorldPutObject.put(pc));
		//pc.sendPackets(new S_OwnCharPack(pc));
		pc.updateObject();
		pc.sendPackets(new S_CharVisualUpdate(pc));
	}

	class TeleportThread implements Runnable {
		L1PcInstance _pc;
		private TeleportThread(L1PcInstance pc) {
			_pc = pc;
		}
		public void begin() {
			GeneralThreadPool.getInstance().schedule(this, 20L);
		}
		@Override
		public void run() {
			try {			
				_pc.setAutoStatus(AUTO_STATUS_WALK);
			}catch(Exception e){					
			}
		}
	}

	public void clearList() {
		L1PcInstance pc;
		for(int i = 0; i < _pcList.size(); i++) {
			pc = _pcList.get(i);
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				teleport(pc,loc[0], loc[1], (short)loc[2]);
				pc.sendPackets("운영자 권한으로 인해 자동 사냥 이 중단되었습니다. 잠시 후 이용 바랍니다.");
			}
		}
		this._pcList.clear();
	}
	public void teleport(L1PcInstance pc, int x, int y, short mapid) {
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		S_SkillSound packet = new S_SkillSound(pc.getId(), 169);
		pc.broadcastPacket(packet);
		pc.sendPackets(packet);	
		try {
			Thread.sleep(280);
		} catch (Exception e) {
		}

		pc.set_teleport_x(x);
		pc.set_teleport_y(y);
		pc.set_teleport_map(mapid);
		Teleportation.doTeleportation(pc);
	}
	
	public static boolean check_auto_dungeon(L1PcInstance pc){
		DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(AUTO_DUNGEON_TIMER_ID);
		if(dtInfo != null){
			DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);
			if(progress != null && progress.get_remain_seconds() <= 0)
				return false;
		}
		return true;
	}
}
