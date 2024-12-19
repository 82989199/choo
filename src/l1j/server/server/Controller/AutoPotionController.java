package l1j.server.server.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.function.BluePotion;
import l1j.server.server.model.item.function.BravePotion;
import l1j.server.server.model.item.function.CurePotion;
import l1j.server.server.model.item.function.DragonPearl;
import l1j.server.server.model.item.function.GreenPotion;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.item.function.WisdomPotion;
import l1j.server.server.model.item.function.메티스스프;
import l1j.server.server.model.item.function.메티스스프1;
import l1j.server.server.model.item.function.메티스스프2;
import l1j.server.server.model.item.function.메티스스프3;
import l1j.server.server.model.item.function.메티스스프4;
import l1j.server.server.model.item.function.메티스스프5;
import l1j.server.server.model.item.function.메티스스프6;
import l1j.server.server.model.item.function.메티스스프7;
import l1j.server.server.model.item.function.메티스스프8;
import l1j.server.server.model.item.function.메티스축복주문서;
import l1j.server.server.model.item.function.민첩빙수;
import l1j.server.server.model.item.function.블레스드아머;
import l1j.server.server.model.item.function.완력빙수;
import l1j.server.server.model.item.function.지식빙수;
import l1j.server.server.model.item.function.코마;
import l1j.server.server.model.item.function.통합버프;
import l1j.server.server.model.item.function.흑사의코인;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;

public class AutoPotionController implements Runnable {
	private static AutoPotionController _instance;

	public static AutoPotionController getInstance() {
		if (_instance == null) {
			_instance = new AutoPotionController();
		}
		return _instance;
	}

	public AutoPotionController() {
		GeneralThreadPool.getInstance().execute(this);
	}

	private Collection<L1PcInstance> list = null;
	private boolean 설정(L1PcInstance pc) {
		if (pc == null || pc.isDead() || pc.is_자동물약사용() == false || pc.isFishing() == true || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		return false;
	}
	
	public void run() {
		try {
			for (;;) {
				this.list = L1World.getInstance().getAllPlayers();
				for (L1PcInstance pc : this.list) {
					if (pc != null) {
						try {
							doAutoPotionAction(pc);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(300L);
				} catch (Exception localException4) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Thread.sleep(300L);
			} catch (Exception localException3) {
			}
		}
	}

	private void doAutoPotionAction(L1PcInstance pc)
  {
    if (!pc.is_자동물약사용()) {
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
    if (pc.getMapId() == 5166) {
      return;
    }
//    if (pc.isPinkName())
//    {
//      pc.set_자동물약사용(false);
//      pc.sendPackets(new S_SystemMessage("\\f3자동 물약이 해제되었습니다."));
//      return;
//    }
    if (!(/*pc.getInventory().checkItem(45453) || pc.getInventory().checkItem(45451) || pc.getInventory().checkItem(45452) || */pc.getInventory().checkItem(45450))) {
    	pc.set_자동물약사용(false);
    	 pc.sendPackets(new S_SystemMessage("\\f3자동 설정 아이템이 소멸하여 자동 설정이 종료되었습니다"));
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
    if (pc.hasSkillEffect(71)) {
      return;
    }
    if ((!pc.getMap().isUsableItem()) && (!pc.isGm())) {
      return;
    }
    ArrayList<Integer> _물약리스트 = pc.get_자동물약리스트();
    if ((_물약리스트 == null) || (_물약리스트.isEmpty())) {
      return;
    }
    for (Iterator<Integer> localIterator = _물약리스트.iterator(); localIterator.hasNext();)
    {
      int itemId = ((Integer)localIterator.next()).intValue();
      L1ItemInstance item = pc.getInventory().findItemId(itemId);
      if ((item != null) && 
      
        (item.getItem().getType2() == 0))
      {
        int delay_id = ((L1EtcItem)item.getItem()).get_delayid();
        if ((delay_id == 0) || 
          (!pc.hasItemDelay(delay_id))) {
          switch (item.getItemId())
          {
          case 40013: //속도향상 물약(일반)
          case 40018: //강화 속도향상 물약(일반)
          case 40030: //수련자의 강화 속도향상 물약(일반)
          case 30158: //농축 속도의 물약
          case 140013: //속도향상 물약(축복)
          case 140018: //강화 속도향상 물약(축복)
//          case 3000162: //저주받은 용의 티셔츠 강화 주문서(네쥬)
            if ((!pc.hasSkillEffect(43)) //헤이스트
             && (!pc.hasSkillEffect(54)) //그레이터 헤이스트
             && (!pc.hasSkillEffect(1001))) { //속도향상 물약
              GreenPotion.checkConditon(pc, item);
            }
            break;
          case 7007: //수련자의 고급 체력 회복제
          case 7008: //수련자의 강력 체력 회복제
          case 40010: //체력 회복제
          case 40011: //고급 체력 회복제
          case 40012: //강력 체력 회복제
          case 40019: //농축 체력 회복제
          case 40020: //농축 고급 체력 회복제
          case 40021: //농축 강력 체력 회복제
          case 40022: //신속 체력 회복제
          case 40023: //신속 고급 체력 회복제
          case 40024: //신속 강력 체력 회복제
          case 40026: //바나나 쥬스
          case 40027: //오렌지 쥬스
          case 40028: //사과 쥬스
          case 40029: //수련자의 체력 회복제
          case 40043: //토끼의 간
          case 40058: //그을린 빵조각
          case 40071: //타다남은 빵조각
          case 41141: //픽시의 힐링포션
          case 41337: //축복받은 보리빵
          case 41403: //쿠작의 식량
          case 60029: //난쟁이 포도 주스
          case 60030: //난쟁이 포도 진액
          case 140010: //체력 회복제
          case 140011: //고급 체력 회복제
          case 140012: //강력 체력 회복제
          case 140506: //엔트의 열매
          case 240010: //체력 회복제
          case 4100021: //수련자의 강력 체력 회복제
          case 4100152: //가벼운 신속 체력 회복제
          case 4100153: //가벼운 신속 고급 체력 회복제
          case 4100154: //가벼운 신속 강력 체력 회복제
            int 퍼센트 = 100 * pc.getCurrentHp() / pc.getMaxHp();
            if (퍼센트 < pc.get_자동물약퍼센트()) {
            	L1HealingPotion healingPotion = L1HealingPotion.get(itemId);
            	
				healingPotion.use(pc, item);
				L1ItemDelay.onItemUse(pc, item);
            }
            break;
          case 40014: //용기의 물약
          case 41415: //농축 용기의 물약
          case 140014: //용기의 물약(축복)
            if (!pc.hasSkillEffect(1000)) {
              BravePotion.checkConditon(pc, item);
            }
            break;
          case 4100040: //통합버프 물약
        	  if (!pc.hasSkillEffect(79)) { //어드밴스 스피릿
        		  통합버프.checkCondition(pc, item);
                }
        	  break;
          case 40068: //엘븐 와퍼
          case 140068: //엘븐 와퍼(축복)
          case 210110: //농축 집중의 물약
            if (!pc.hasSkillEffect(1016)) {
              BravePotion.checkConditon(pc, item);
            }
            break;
          case 30077: //수련자의 유그드라 열매
          case 713:	//농축 유그드라의 물약
          case 210036: //유그드라 열매
        	  if (pc.isPassive(MJPassiveID.DARK_HORSE.toInt())) {
              if (!pc.hasSkillEffect(1000)) {
                BravePotion.checkConditon(pc, item);
              }
            }
            else if (!pc.hasSkillEffect(20079)) {
              BravePotion.checkConditon(pc, item);
            }
            break;
          case 40015: //마나 회복 물약
          case 140015: //마나 회복 물약(축복)
          case 210114: //농축 마력의 물약
            if (!pc.hasSkillEffect(1002)) {
              BluePotion.checkCondition(pc, item);
            }
            break;
          case 41142: //픽시의 마나포션
            if (!pc.hasSkillEffect(20082)) {
              BluePotion.checkCondition(pc, item);
            }
            break;
					case 65648: //흑사의 코인

						if (!pc.hasSkillEffect(L1SkillId.God_buff)) {
							흑사의코인.checkCondition(pc, item);
						}
					break;
					case 30104: //코마의 축복 코인

						if (!pc.hasSkillEffect(L1SkillId.COMA_B)) {
							코마.checkCondition(pc, item);
						}
					break;
					case 60208: //완력의 체리 빙수

						if (!pc.hasSkillEffect(L1SkillId.완력빙수)) {
							완력빙수.checkCondition(pc, item);
						}
					break;
					case 60209: //민첩의 녹차 빙수

						if (!pc.hasSkillEffect(L1SkillId.민첩빙수)) {
							민첩빙수.checkCondition(pc, item);
						}
					break;
					case 60210: //지식의 단팥 빙수

						if (!pc.hasSkillEffect(L1SkillId.지식빙수)) {
							지식빙수.checkCondition(pc, item);
						}
					break;
					case 40879: //마법 주문서 (블레스드 아머)

						if (!pc.hasSkillEffect(L1SkillId.BLESSED_ARMOR)) {
							블레스드아머.checkCondition(pc, item);
						}
					break;
					case 3000128: //메티스의 축복 주문서

						if (!pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_A)) {
							메티스축복주문서.checkCondition(pc, item);
						}
					break;
					case 3000129: //메티스의 정성스런 스프

						if (!pc.hasSkillEffect(L1SkillId.메티스정성스프)) {
							pc.setCookingId(L1SkillId.메티스정성스프);
							메티스스프.checkCondition(pc, item);
						}
						break;

					case 30051: //힘센 한우 스테이크

						if (!pc.hasSkillEffect(L1SkillId.COOK_STR)) {
							pc.setCookingId(L1SkillId.COOK_STR);
							메티스스프1.checkCondition(pc, item);
						}
						break;

					case 4100156: //힘센 한우 스테이크(축복)
						if (!pc.hasSkillEffect(L1SkillId.COOK_STR_1)) {
							pc.setCookingId(L1SkillId.COOK_STR_1);
							메티스스프2.checkCondition(pc, item);
						}
						break;
						
					case 30052: //날샌 연어 찜
						if (!pc.hasSkillEffect(L1SkillId.COOK_DEX)) {
							pc.setCookingId(L1SkillId.COOK_DEX);
							메티스스프3.checkCondition(pc, item);
						}
						break;
					case 4100157: //날쌘 연어 찜(축복)
						if (!pc.hasSkillEffect(L1SkillId.COOK_DEX_1)) {
							pc.setCookingId(L1SkillId.COOK_DEX_1);
							메티스스프4.checkCondition(pc, item);
						}
						break;
						
					case 30053: //영리한 칠면조 구이
						if (!pc.hasSkillEffect(L1SkillId.COOK_INT)) {
							pc.setCookingId(L1SkillId.COOK_INT);
							메티스스프5.checkCondition(pc, item);
						}
						break;
					case 4100158: //영리한 칠면조 구이(축복)
						if (!pc.hasSkillEffect(L1SkillId.COOK_INT_1)) {
							pc.setCookingId(L1SkillId.COOK_INT_1);
							메티스스프6.checkCondition(pc, item);
						}
						break;
						
					case 30054: //수련의 닭고기 스프
						if (!pc.hasSkillEffect(L1SkillId.COOK_GROW)) {
							pc.setCookingId(L1SkillId.COOK_GROW);
							메티스스프7.checkCondition(pc, item);
						}
						break;
					case 4100159: //수련의 닭고기 스프(축복)
						if (!pc.hasSkillEffect(L1SkillId.COOK_GROW_1)) {
							pc.setCookingId(L1SkillId.COOK_GROW_1);
							메티스스프8.checkCondition(pc, item);
						}
						break;

			          case 410063: //드래곤의 진주
			          case 410137: //수련자의 드래곤의 진주
			        	  if (!(pc.hasSkillEffect(22017)
			        			  /* || pc.hasSkillEffect(L1SkillId.잠재력진주)*/)) {
			              DragonPearl.checkCondition(pc, item);
			            }
			            break;
          case 30089: //수련자의 지혜의 물약
          case 40016: //지혜의 물약
          case 140016: //지혜의 물약(축복)
          case 210113: //농축 지혜의 물약
            if (!pc.hasSkillEffect(1004)) {
              WisdomPotion.checkCondition(pc, item);
            }
            break;
          case 30084: //수련자의 해독제
          case 40017: //해독제
          case 40507: //엔트의 줄기
            if ((pc.hasSkillEffect(1006)) || (pc.hasSkillEffect(1008)) || 
              (pc.hasSkillEffect(1007)) || (pc.hasSkillEffect(30007)) || 
              (pc.hasSkillEffect(30002))) {
              CurePotion.checkCondition(pc, item);
            }
            break;
          }
        }
      }
    }
  }
}

