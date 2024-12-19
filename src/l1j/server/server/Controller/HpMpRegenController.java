package l1j.server.server.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;

public class HpMpRegenController extends TimerTask {
	private final L1PcInstance _pc;
	private final int attackSec = 6;
	private final int moveSec = 2;
	private int oldLevel = 0;
	private int oldLoc = 0;
	private int regenTimeCount = 0;
	private int levelBaseSec = 0;

	public HpMpRegenController(L1PcInstance pc) { //처음 스레드 실행시킬때 한번 그이후에는 바로 run 스레드만 실행하는듯.
		_pc = pc;
		levelBaseSecRefresh(pc.getLevel());
	}

	private void levelBaseSecRefresh(int lv) {
		if (lv < 55) { //55이하거나
			levelBaseSec = 12;
		} else if (lv >= 55 && lv <= 65) { //55~65 사이거나.
			levelBaseSec = 8;
		} else if (lv > 65) { //65 이상일때
			levelBaseSec = 4;
		}
		oldLevel = lv;
	}
	//리젠스테이터스를 갱신할때는 가장먼저 공격중인지를 우선시한다. 그다음 움직임 다음 제자리 순으로 갱신한다.
	private void regenStatusRefresh() { //1초마다 리프레쉬.
		if (oldLevel != _pc.getLevel()) {
			//System.out.println("레벨 변화감지");
			levelBaseSecRefresh(_pc.getLevel()); //올드 레벨과 다를경우 갱신
		}
		if (_pc.isPinkName()) { //현재 전투중인지.
			//System.out.println("전투감지");
			regenTimeCount = levelBaseSec + attackSec;
		} else {
			int newLoc = _pc.getX() + _pc.getY();		
			//System.out.println("oldLoc : " + oldLoc + " / newLoc : " + newLoc);
			if (oldLoc != newLoc) { //움직였을경우 리젠타임카운트 변동.
				//System.out.println("무빙감지 : " + newLoc);
				regenTimeCount = levelBaseSec + moveSec;
				oldLoc = newLoc;
			} else { //움직이지않고 자리에 가만히 있을때
				regenTimeCount = levelBaseSec;
			}
		}
		//System.out.println("regenTimeCount : " + regenTimeCount + " / levelBaseSec : " + levelBaseSec);
	}
	private void hpregen() {
		if (_pc.getCurrentHp() == _pc.getMaxHp() && !isUnderwater(_pc)) 
			return;
		_regenHpPoint += 1;
		synchronized (this) {
			if (regenTimeCount <= _regenHpPoint) {
				_regenHpPoint = 0;
				regenHp();
			}
		}
	}

	private int _regenHpPoint = 0;

	private int _regenMpPoint = 0;

	private int _curMpPoint = 4;

	private static Random _random = new Random();

	private boolean isPcCk(L1PcInstance pc) {
		if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		return false;
	}
	@Override
	public void run() {
		try { //차후 분기 시스템으로 짜면 시간손실을 최소화 할수있음. 하지만. 1초마다 실행되는 스레드라 과부화도 생각해야함. 분기를 하더라도 메소드를 두개씩 묶는 형식 추천.
			regenStatusRefresh(); //이건 신형 hp리젠 위하여
			if (!isPcCk(_pc)) {
				hpregen();
			}
			if (!isPcCk(_pc)) {
				mpregen();
			}
			if (!isPcCk(_pc)) {
				DanteasBuff();
			}
			if (!isPcCk(_pc)) {
				clanbuff();
			}
			if (!isPcCk(_pc)) {
				GotobokBuff();
			}
		} catch (Exception e) {
			_pc.setHpMpRegenActive(false);
			e.printStackTrace();
		}
	}

	private void mpregen() {
		int nowMaxMp = _pc.getMaxMp();
		if (_pc.isDead()) {
			return;
		}
		if (_pc.getCurrentMp() == nowMaxMp) {
			return;
		}
		_regenMpPoint += _curMpPoint;
		_curMpPoint = 4;
		if (64 <= _regenMpPoint) {
			_regenMpPoint = 0;
			regenMp();
		}
	}

	public void regenMp() {
		int baseMpr = 1;
		int wis = _pc.getAbility().getTotalWis();
		if (wis == 15 || wis == 16) {
			baseMpr = 2;
		} else if (wis == 17) {
			baseMpr = 3;
		} else if (wis >= 18) {
			baseMpr += wis - 14;
		}
		// 베이스 WIS 회복 보너스
		int baseStatMpr = CalcStat.calcMpr(_pc.getAbility().getBaseWis());

		if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2) == true) {
			baseMpr += 1;
		} else if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION) == true) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.MEDITATION) == true) {
			baseMpr += 5;
		}
		if (_pc.hasSkillEffect(L1SkillId.CONCENTRATION) == true) {
			baseMpr += 4;
		}
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_2_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_2_S)) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_20_N) // 레서 드레곤 꼬치 구이
				|| _pc.hasSkillEffect(L1SkillId.COOKING_1_20_S)) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_12_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_12_S)) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL2) == true) {
			baseMpr += 4;
		}
		if (isInn(_pc)) {
			baseMpr += 3;
		}
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) {
			baseMpr += 3;
		}

		int itemMpr = _pc.getInventory().mpRegenPerTick();
		itemMpr += _pc.getMpr();
		if (_pc.get_food() < 40 || isOverWeight(_pc)) {
			baseMpr = 0;
			if (itemMpr > 0) {
				itemMpr = 0;
			}
			return;
		}
		int mpr = baseMpr + itemMpr + baseStatMpr;
		int newMp = _pc.getCurrentMp() + mpr;

		_pc.setCurrentMp(newMp);
	}
	public void regenHp() {
		if (_pc.isDead()) {
			return;
		}
		if (_pc.getCurrentHp() == _pc.getMaxHp() && !isUnderwater(_pc)) {
			return;
		}	
		int maxBonus = 1;

		// CON 보너스
		if (_pc.getLevel() > 11 && _pc.getAbility().getTotalCon() >= 14) {
			maxBonus = _pc.getAbility().getTotalCon() - 12;
			if (25 < _pc.getAbility().getTotalCon()) {
				maxBonus = 14;
			}
		}
		// 베이스 CON 보너스
		int basebonus = CalcStat.calcHpr(_pc.getAbility().getBaseCon());

		int equipHpr = _pc.getInventory().hpRegenPerTick();
		equipHpr += _pc.getHpr();
		int bonus = _random.nextInt(maxBonus) + 1;

		if (_pc.hasSkillEffect(L1SkillId.NATURES_TOUCH)) {
			bonus += 15;
		}
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
			bonus += 5;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_12_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_12_S)) {
			bonus += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_19_N) // 대왕 거북 구이
				|| _pc.hasSkillEffect(L1SkillId.COOKING_1_19_S)) {
			bonus += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL)) {
			bonus += 4;
		}
		if (isInn(_pc)) {
			bonus += 5;
		}
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) { //엄마숲효과
			bonus += 5;
		}
		
		boolean inLifeStream = false;
		if (isPlayerInLifeStream(_pc)) {
			inLifeStream = true;
			// 고대의 공간, 마족의 신전에서는 HPR+3은 없어져?
			bonus += 3;
		}

		// 공복과 중량의 체크
		if (_pc.get_food() < 40 || isOverWeight(_pc) || _pc.hasSkillEffect(L1SkillId.BERSERKERS)) {
			bonus = 0;
			basebonus = 0;
			equipHpr = 0;
			// 장비에 의한 HPR 증가는 만복도, 중량에 의해 없어지지만, 감소인 경우는 만복도, 중량에 관계없이 효과가 남는다
			/*if (equipHpr > 0) {
				
			}
			return;*/
		}
		//System.out.println("bonus : " + bonus + " / equipHpr : " + equipHpr + " / basebonus : " + basebonus);
		int newHp = _pc.getCurrentHp();
		newHp += bonus + equipHpr + basebonus;

		if (newHp < 1) {
			newHp = 1; // HPR 감소 장비에 의해 사망은 하지 않는다
		}
		// 수중에서의 감소 처리
		// 라이프 시냇물로 감소를 없앨 수 있을까 불명
		if (isUnderwater(_pc)) {
			newHp -= 20;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // HP가 0이 되었을 경우는 사망한다.
				}
			}
		}
		// Lv50 퀘스트의 고대의 공간 1 F2F에서의 감소 처리
		if (isLv50Quest(_pc) && !inLifeStream) {
			newHp -= 10;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // HP가 0이 되었을 경우는 사망한다.
				}
			}
		}
		// 마족의 신전에서의 감소 처리
		if (_pc.getMapId() == 410 && !inLifeStream) {
			newHp -= 10;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // HP가 0이 되었을 경우는 사망한다.
				}
			}
		}
		//System.out.println("regenHp 실행1 " + newHp + " maxhp : " + _pc.getMaxHp() + " / min : "+ Math.min(newHp, _pc.getMaxHp()));
		
		if (!_pc.isDead()) {
			_pc.setCurrentHp(Math.min(newHp, _pc.getMaxHp()));
		}
	}

	private boolean isUnderwater(L1PcInstance pc) {
		// 워터 부츠 장비시인가, 에바의 축복 상태이면, 수중은 아니면 간주한다.
		if (pc.getInventory().checkEquipped(20207)) {
			return false;
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
			return false;
		}
		if (pc.getInventory().checkEquipped(21048) && pc.getInventory().checkEquipped(21049) && pc.getInventory().checkEquipped(21050)) {
			return false;
		}
		return pc.getMap().isUnderwater();
	}

	private boolean isOverWeight(L1PcInstance pc) {
		// 에키조틱크바이타라이즈 상태, 아디쇼나르파이아 상태인가
		// 골든 윙 장비시이면, 중량 오버이지 않으면 간주한다.
		if (pc.hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) || pc.hasSkillEffect(L1SkillId.ADDITIONAL_FIRE) || pc.hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		// 상위랭커 피,엠 회복문제 해결.
		if (pc.is_top_ranker()) {
			return false;
		}
		if (isInn(pc)) {
			return false;
		}
		return (50 <= pc.getInventory().getWeight100()) ? true : false;
	}

	private boolean isLv50Quest(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 2000 || mapId == 2001) ? true : false;
	}
	
	private void DanteasBuff() {
		if (_pc.isDanteasBuff == false) {
			if (_pc.getMapId() == 479) {
				_pc.addDmgup(2);
				_pc.addBowDmgup(2);
				_pc.getAbility().addSp(1);
				_pc.addMpr(2);
				_pc.sendPackets(new S_SPMR(_pc));
				_pc.isDanteasBuff = true;
				_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, true));
				_pc.sendPackets(new S_SystemMessage("단테스 버프 : 근거리/원거리 대미지+2, SP+1, MP 회복+2 "));
			}
		} else {
			boolean DanteasOk = false;
			if (_pc.getMapId() == 479) {
				DanteasOk = true;
			}
			if (DanteasOk == false) {
				_pc.addDmgup(-2);
				_pc.addBowDmgup(-2);
				_pc.getAbility().addSp(-1);
				_pc.addMpr(-2);
				_pc.sendPackets(new S_SPMR(_pc));
				_pc.isDanteasBuff = false;
				_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, false));
				_pc.sendPackets(new S_SystemMessage("단테스의 버프 : 버프가 사라짐"));
			}
		}
	}

	private void GotobokBuff() {
		if (_pc.isGotobokBuff == false) {
			if (_pc.getMapId() == 1710) {
				_pc.addMpr(20);
				_pc.addHpr(30);
				_pc.isGotobokBuff = true;
//				_pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS2, 993, true));
//				_pc.sendPackets("\\f2잊혀진 섬 대기실:HP/MP 회복률이 증가 됩니다.");
			}
		} else {
			boolean GotobokOk = false;
			if (_pc.getMapId() == 1710) {
				GotobokOk = true;
			}
			if (GotobokOk == false) {
				_pc.addMpr(-20);
				_pc.addHpr(-30);
				_pc.isGotobokBuff = false;
//				_pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS2));				
//				_pc.sendPackets("\\f2잊혀진 섬 대기실에서 벗어나 버프가 해제 되었습니다.");
			}
		}
	}
	private void clanbuff() {
		L1Clan clan = L1World.getInstance().getClan(_pc.getClanid());
		if (_pc.getClanid() != 0 && clan.getOnlineClanMember().length >= Config.CLAN_COUNT && !_pc.isClanBuff()) {
			_pc.setSkillEffect(L1SkillId.CLANBUFF_YES, 0);
			//pc.sendPackets(new S_PacketBox(S_PacketBox.CLAN_BUFF_ICON, 1));
			_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, true));
			_pc.setClanBuff(true);
		} else if (_pc.getClanid() != 0 && clan.getOnlineClanMember().length < Config.CLAN_COUNT && _pc.isClanBuff()) {
			_pc.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			_pc.setClanBuff(false);
		}
	}

	/**
	 * 지정한 PC가 라이프 시냇물의 범위내에 있는지 체크한다
	 * 
	 * @param pc
	 *            PC
	 * @return true PC가 라이프 시냇물의 범위내에 있는 경우
	 */
	private static boolean isPlayerInLifeStream(L1PcInstance pc) {
		for (L1Object object : pc.getKnownObjects()) {
			if (object instanceof L1EffectInstance == false) {
				continue;
			}
			L1EffectInstance effect = (L1EffectInstance) object;
			if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(pc.getLocation()) < 4) {
				return true;
			}
		}
		return false;
	}
	private boolean isInn(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820
				|| mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968
				|| mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528
				|| mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088) ? true
						: false;
	}

	 private void doAutoBuffAction(L1PcInstance pc)
	  {
	    if (!pc.is_자동버프사용()) {
	      return;
	    }
	    
	    if ((!pc.is_자동버프세이프티존사용()) && (pc.getZoneType() == 1)) {
	      return;
	    }
	    if (!( pc.getInventory().checkItem(45450) /*|| pc.getInventory().checkItem(45453) || pc.getInventory().checkItem(45451) || pc.getInventory().checkItem(45452)*/)) {
	    	
	    	return;
	    }
	    if ((pc.get_teleport()) || (pc.isDead())) {
	      return;
	    }
	    if (pc.isFishing()) {
	      return;
	    }
	    if (pc.isPrivateShop() || pc.isAutoClanjoin()) {
	      return;
	    }
	    if ((pc.isPinkName()) && (!pc.is_자동버프전투시사용())) {
	    return;
	    }
	    if (!pc.getMap().isUsableSkill()) {
	      return;
	    }
	    if (pc.getMapId() == 5166) {
	      return;
	    }
	    if (pc.getCurrentMp() == 0) {
	      return;
	    }
	    if ((pc.hasSkillEffect(87)) || 
	      (pc.hasSkillEffect(30081)) || 
	      (pc.hasSkillEffect(40007)) || 
	      (pc.hasSkillEffect(30005)) || 
	      (pc.hasSkillEffect(30006)) || 
	      (pc.hasSkillEffect(22055)) || 
	      (pc.hasSkillEffect(22025)) || 
	      (pc.hasSkillEffect(22026)) || 
	      (pc.hasSkillEffect(22027)) || 
	      (pc.hasSkillEffect(157)) || 
	      (pc.hasSkillEffect(30003)) || 
	      (pc.hasSkillEffect(30004)) || 
	      (pc.hasSkillEffect(208)) || 
	      (pc.hasSkillEffect(212)) || 
	      (pc.hasSkillEffect(103)) || 
	      (pc.hasSkillEffect(66)) || 
	      (pc.hasSkillEffect(33)) || 
	      (pc.hasSkillEffect(10101))) {
	      return;
	    }
	    ArrayList<Integer> _버프리스트 = new ArrayList<Integer>();
	    
	    _버프리스트 = pc.get_자동버프리스트();
	    if ((_버프리스트 == null) || (_버프리스트.isEmpty())) {
	      return;
	    }
	    for (Iterator<Integer> localIterator1 = _버프리스트.iterator(); localIterator1.hasNext();)
	    {
	      int skillId = ((Integer)localIterator1.next()).intValue();
	      if (SkillsTable.getInstance().spellCheck(pc.getId(), skillId)) {
	        if (pc.getType() == 0) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 12: 
	          case 14: 
	          case 114: 
	          case 115: 
	          case 116: 
	          case 117: 
	          case 118: 
	            if (skillId == 8 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(48)) : 
	              
	              (skillId == 12) && (
	              (pc.hasSkillEffect(8)) || 
	              (pc.hasSkillEffect(48)))) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp())
	              {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	                L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	              }
	            }
	          }
	        } else if (pc.getType() == 1) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 88: 
	          case 89: 
	          case 90: 
	          case 91: 
	          case 93: 
	          case 94: 
	            if (pc.isSkillDelay()) {
	              continue;
	            }
	            if (skillId == 91)
	            {
	              L1ItemInstance weapon = pc.getWeapon();
	              if ((weapon == null) || 
	              
	                (weapon.getItem().getType1() != 50)) {
	                continue;
	              }
	            }
	            if (skillId == 94)
	            {
	              L1ItemInstance weapon = pc.getWeapon();
	              if ((weapon == null) || (
	              
	                (weapon.getItem().getType1() != 4) && (weapon.getItem().getType1() != 46))) {
	                continue;
	              }
	              boolean isUseSkill = false;
	              try
	              {
	                for (L1ItemInstance armor : pc.getEquipSlot().getArmors()) {
	                  if (armor.getItem().getType() == 7)
	                  {
	                    isUseSkill = true;
	                    break;
	                  }
	                }
	              }
	              catch (Exception localException1) {}
	              if (!isUseSkill) {
	                continue;
	              }
	            }
	            if (skillId == 8 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(48)) : 
	              
	              (skillId == 3) && (
	              (pc.hasSkillEffect(151)) || 
	              (pc.hasSkillEffect(168)))) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 2) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 12: 
	          case 14: 
	          case 21: 
	          case 26: 
	          case 42: 
	          case 43: 
	          case 48: 
	          case 129: 
	          case 134: 
	          case 135: 
	          case 136: 
	          case 137: 
	          case 147: 
	          case 148: 
	          case 149: 
	          case 150: 
	          case 151: 
	          case 152: 
	          case 155: 
	          case 156: 
	          case 158: 
	          case 159: 
	          case 160: 
	          case 167:
	          case 163: 
	          case 166: 
	          case 168: 
	          case 169: 
	          case 171: 
	          case 175: 
	          case 176: 
	          case 177: 
	          case 178: 
	          case 179: 
	            if (pc.isSkillDelay()) {
	              continue;
	            }
	            if ((skillId == 155) || (skillId == 179))
	            {
	              L1ItemInstance weapon = pc.getWeapon();
	              if ((weapon == null) || 
	              
	                (weapon.getItem().getType1() == 24)) {
	                continue;
	              }
	            }
	            if (skillId == 136)
	            {
	              L1ItemInstance weapon = pc.getWeapon();
	              if ((weapon == null) || 
	              
	                (weapon.getItem().getType1() != 4)) {
	                continue;
	              }
	            }
	            if ((pc.getElfAttr() == 0) || 
	            
	              (pc.getElfAttr() == 1 ? 
	              (skillId == 177) && (skillId == 178) : 
	              
	              pc.getElfAttr() == 2 ? 
	              (skillId == 177) && (skillId == 178) && (skillId == 179) : 
	              
	              pc.getElfAttr() == 4 ? 
	              (skillId == 178) && (skillId == 179) : 
	              
	              (pc.getElfAttr() == 8) && (
	              (skillId == 177) || (skillId == 179)))) {
	              continue;
	            }
	            if (skillId == 48 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(8)) : 
	              
	              skillId == 43 ? 
	              (pc.hasSkillEffect(1001)) && 
	              (pc.hasSkillEffect(54)) : 
	              
	              skillId == 3 ? 
	              (pc.hasSkillEffect(151)) && 
	              (pc.hasSkillEffect(168)) : 
	              
	              (skillId == 151) && 
	              (pc.hasSkillEffect(168))) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 3) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 12: 
	          case 14: 
	          case 21: 
	          case 26: 
	          case 42: 
	          case 43: 
	          case 48: 
	          case 50: 
	          case 52: 
	          case 54: 
	          case 56: 
	          case 79: 
	            if ((pc.isSkillDelay()) || 
	            
	              (skillId == 8 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(48)) : 
	              
	              skillId == 12 ? 
	              (pc.hasSkillEffect(8)) && 
	              (pc.hasSkillEffect(48)) : 
	              
	              skillId == 48 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(8)) : 
	              
	              skillId == 43 ? 
	              (pc.hasSkillEffect(1001)) && 
	              (pc.hasSkillEffect(54)) : 
	              
	              (skillId == 3) && (
	              (pc.hasSkillEffect(151)) || 
	              (pc.hasSkillEffect(168))))) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 4) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 12: 
	          case 14: 
	          case 98: 
	          case 99: 
	          case 101: 
	          case 102: 
	          case 105: 
	          case 106: 
	          case 107: 
	          case 109: 
	          case 110: 
	          case 111: 
	          case 234: 
	            if ((pc.isSkillDelay()) || 
	            
	              (skillId == 8 ? 
	              (pc.hasSkillEffect(12)) && 
	              (pc.hasSkillEffect(48)) && 
	              (pc.hasSkillEffect(107)) : 
	              
	              skillId == 12 ? 
	              (pc.hasSkillEffect(8)) && 
	              (pc.hasSkillEffect(48)) && 
	              (pc.hasSkillEffect(107)) : 
	              
	              skillId == 109 ? 
	              pc.hasSkillEffect(42) : 
	              
	              skillId == 110 ? 
	              pc.hasSkillEffect(26) : 
	              
	              (skillId == 3) && (
	              (pc.hasSkillEffect(151)) || 
	              (pc.hasSkillEffect(168))))) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 5) {
	          switch (skillId)
	          {
	          case 181: 
	          case 185: 
	          case 186: 
	          case 190: 
	          case 191: 
	          case 195: 
	          case 197: 
	            if (pc.isSkillDelay()) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 6) {
	          switch (skillId)
	          {
	          case 201: 
	          case 204: 
	          case 206: 
	          case 209: 
	          case 211: 
	          case 214: 
	          case 216: 
	          case 218: 
	          case 219: 
	          case 223: 
	            if (pc.isSkillDelay()) {
	              continue;
	            }
	            if (!pc.hasSkillEffect(skillId))
	            {
	              L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	              if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	              }
	              L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	            }
	          }
	        } else if (pc.getType() == 7) {
	          switch (skillId)
	          {
	          case 2: 
	          case 3: 
	          case 8: 
	          case 226: 
	          case 231: 
	            if ((!pc.isSkillDelay()) && 
	            
	              (skillId == 8 ? 
	              (!pc.hasSkillEffect(12)) || 
	              (!pc.hasSkillEffect(48)) : 
	              
	              (skillId != 3) || (
	              (!pc.hasSkillEffect(151)) && 
	              (!pc.hasSkillEffect(168))))) {
	              if ((!pc.hasSkillEffect(skillId)) && (!pc.isSkillDelay()))
	              {
	                L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	                if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                  new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	                }
	                L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
	              }
	            }
	            break;
	          }
	        }
	      }
	    }
	  }
	  
	  private void doAutoResolAction(L1PcInstance pc)
	  {
	    if (pc.get_자동용해리스트().isEmpty()) {
	      return;
	    }
	    if ((pc.get_teleport()) || (pc.isDead())) {
	      return;
	    }
	    if (pc.isFishing()) {
	      return;
	    }
	    if (pc.isPrivateShop() ) {
	      return;
	    }
	    if (pc.getMapId() == 5166) {
	      return;
	    }
	    if ((!pc.getMap().isUsableItem()) && (!pc.isGm())) {
	      return;
	    }
	    ArrayList<Integer> _용해리스트 = pc.get_자동용해리스트();
	    if ((_용해리스트 == null) || (_용해리스트.isEmpty())) {
	      return;
	    }
	    for (Iterator<Integer> localIterator = _용해리스트.iterator(); localIterator.hasNext();)
	    {
	      int itemId = ((Integer)localIterator.next()).intValue();
	      L1ItemInstance item = pc.getInventory().findItemId(itemId);
	      if (item != null)
	      {
	        int crystalCount = ResolventTable.getInstance().getCrystalCount(item.getItem().getItemId());
	        if (crystalCount > 0) {
	          C_ItemUSe.useResolvent(pc, item, null, crystalCount);
	        }
	      }
	    }
	  }
	  
	 

}
