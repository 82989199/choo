package l1j.server.server.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
//import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Skills;
import java.util.List;
import java.util.Arrays;

public class AutoBuffController implements Runnable {
	private static AutoBuffController _instance;

	public static AutoBuffController getInstance() {
		if (_instance == null) {
			_instance = new AutoBuffController();
		}
		return _instance;
	}

	public AutoBuffController() {
		GeneralThreadPool.getInstance().execute(this);
	}

	private Collection<L1PcInstance> list = null;

	private boolean 설정(L1PcInstance pc) {
		//if (pc == null || pc.isDead() || pc.is_자동버프사용() == false || pc.isFishing() == true || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		if (pc == null || pc.isDead() ||  pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		return false;
	}
	
	public void run() {
		try {
			for (;;) {
				this.list = L1World.getInstance().getAllPlayers();
				for (L1PcInstance pc : this.list) {
					if (!설정(pc) && (pc.getInventory().checkItem(45450)
							     /*||pc.getInventory().checkItem(45451) || pc.getInventory().checkItem(45452) || pc.getInventory().checkItem(45453)*/)) {
						try {
							doAutoBuffAction(pc);
							
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

	private void doAutoBuffAction(L1PcInstance pc) {
		// 자동 버프 사용 설정이 활성화되어 있지 않으면 함수 종료
		if (!pc.is_자동버프사용()) {
		    return;
		}
		// 자동 버프를 세이프티 존에서만 사용하도록 설정되어 있고 현재 플레이어가 세이프티 존이 아니면 함수 종료
		if ((!pc.is_자동버프세이프티존사용()) && (pc.getZoneType() == 1)) {
		    return;
		}
		// 필요한 아이템(예:자동물약)이 인벤토리에 없으면 함수 종료
		if (!(pc.getInventory().checkItem(45450) /*|| pc.getInventory().checkItem(45453)
				|| pc.getInventory().checkItem(45451) || pc.getInventory().checkItem(45452)*/)) {

			return;
		}
		// 텔레포트 중이거나 사망 상태이면 함수 종료
		if ((pc.get_teleport()) || (pc.isDead())) {
			return;
		}
		// 낚시 중이면 함수 종료
		if (pc.isFishing()) {
			return;
		}
		// 개인 상점이 열려 있거나 클랜 자동 가입 중이면 함수 종료
		if (pc.isPrivateShop() || pc.isAutoClanjoin()/* || pc.isAutoFish()*/) {
			return;
		}
		// 핑크 네임 상태이고 전투 시 자동 버프 사용이 비활성화되어 있으면 함수 종료
		if ((pc.isPinkName()) && (!pc.is_자동버프전투시사용())) {
			return;
		}
		// 현재 맵에서 스킬 사용이 불가능하면 함수 종료
		if (!pc.getMap().isUsableSkill()) {
			return;
		}
		// 회상의 땅(맵 ID: 5166)에 있으면 함수 종료
		if (pc.getMapId() == 5166) {
			return;
		}
		// 현재 MP가 0이면 함수 종료
		if (pc.getCurrentMp() == 0) {
			return;
		}
		// 아래의 특정 스킬 효과가 있으면 함수 종료
		if ((pc.hasSkillEffect(87)) || (pc.hasSkillEffect(30081)) || (pc.hasSkillEffect(40007))
				|| (pc.hasSkillEffect(30005)) || (pc.hasSkillEffect(30006)) || (pc.hasSkillEffect(22055))
				|| (pc.hasSkillEffect(22025)) || (pc.hasSkillEffect(22026)) || (pc.hasSkillEffect(22027))
				|| (pc.hasSkillEffect(157)) || (pc.hasSkillEffect(30003)) || (pc.hasSkillEffect(30004))
				|| (pc.hasSkillEffect(208)) || (pc.hasSkillEffect(212)) || (pc.hasSkillEffect(103))
				|| (pc.hasSkillEffect(66)) || (pc.hasSkillEffect(33)) || (pc.hasSkillEffect(10101))) {
			return;
		}
		// 자동 버프 리스트를 가져옴
		ArrayList<Integer> _버프리스트 = new ArrayList<Integer>();
		_버프리스트 = pc.get_자동버프리스트();
		
		// 버프 리스트가 비어있으면 함수 종료
		if ((_버프리스트 == null) || (_버프리스트.isEmpty())) {
			return;
		}
		//기존 로직
//		for (Iterator<Integer> localIterator1 = _버프리스트.iterator(); localIterator1.hasNext();) {
//			int skillId = ((Integer) localIterator1.next()).intValue();
//			if (SkillsTable.getInstance().spellCheck(pc.getId(), skillId)) {
		
	    HashSet<Integer> activeSkills = new HashSet<>();
	    for (int skillId : _버프리스트) {
	        // 스킬이 이미 활성화되어 있거나, 이전에 사용한 스킬 목록에 있으면 건너뜀
	        if (pc.hasSkillEffect(skillId) || !activeSkills.add(skillId)) {
	            continue;
	        }
	        // SkillsTable에서 스킬 정보를 확인하고 스킬 사용 조건을 검사하는 로직
	        if (SkillsTable.getInstance().spellCheck(pc.getId(), skillId)) {
	            if (pc.getType() == 0) { // 군주
	                switch (skillId) {
	                    case 2: //라이트
	                    case 3: //실드
	                    case 8: //홀리 웨폰
	                    case 12: //인챈트 웨폰
	                    case 14: //디크리즈 웨이트
	                    case 114: //글로잉 웨폰
	                    case 115: //샤이닝 실드
	                    case 116: //샤이닝 아머
	                    case 117: //브레이브 멘탈
	                    case 118: //마제스티
	                        // 스킬 사용 후 딜레이 체크
	                        long current = System.currentTimeMillis();
	                        if (current - pc.getAutoSkillDelay() < 0) return;

	                        L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
	                        if (skill.getMpConsume() <= pc.getCurrentMp()) {
	                            new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0);
	                            pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000);
//	                            pc.updateLight(); // 라이트 업데이트
	                        }
	                        break;
	                }
				} else if (pc.getType() == 1) { //기사
					switch (skillId) {
					case 2: //라이트
					case 3: //실드
					case 8: //홀리 웨폰
					case 88: //리덕션 아머
					case 89: //바운스 어택
					case 90: //솔리드 캐리지
					case 91: //카운터 배리어
					case 93: //프라이드
					case 94: //블로우 어택
						if (pc.isSkillDelay()) { // 스킬 딜레이 중인지 확인
						    continue; // 딜레이 중이면 다음 스킬로 넘어감
						}
						// 카운터 배리어(91) 사용 조건: 플레이어가 특정 타입(50)의 무기를 들고 있어야 함
						if (skillId == 91) {
						    L1ItemInstance weapon = pc.getWeapon();
						    if ((weapon == null) || (weapon.getItem().getType1() != 50)) {
						        continue; // 무기가 없거나 조건에 맞지 않으면 스킬 사용 불가
						    }
						}
						// 블로우 어택(94) 사용 조건: 플레이어가 특정 타입(4, 46, 50)의 무기를 들고 있어야 함
						if (skillId == 94) {
						    L1ItemInstance weapon = pc.getWeapon();
						    if ((weapon == null) || (weapon.getItem().getType1() != 4 && weapon.getItem().getType1() != 46 && weapon.getItem().getType1() != 50)) {
						        continue; // 무기가 없거나 조건에 맞지 않으면 스킬 사용 불가
						    }
						    boolean isUseSkill = false;
						    try {
						        for (L1ItemInstance armor : pc.getEquipSlot().getArmors()) {
						            if (armor.getItem().getType() == 7 || armor.getItem().getType() == 13) {
						                isUseSkill = true; // 특정 타입(7, 13)의 갑옷을 착용하고 있어야 함
						                break;
						            }
						        }
						    } catch (Exception localException1) {
						    }
						    if (!isUseSkill) {
						        continue; // 조건에 맞는 갑옷을 착용하지 않으면 스킬 사용 불가
						    }
						}
						// 홀리 웨폰(8)과 인챈트 웨폰(12) 중복 사용 방지 로직
						if (skillId == 8 ? (pc.hasSkillEffect(12)) && (pc.hasSkillEffect(48)) :
						    // 실드(3) 사용 시 파이어 실드(151) 또는 아이언 스킨(168) 효과가 없어야 함
						    (skillId == 3) && ((pc.hasSkillEffect(151)) || (pc.hasSkillEffect(168)))) {
						    continue; // 중복 조건에 해당하면 스킬 사용 불가
						}
						if (!pc.hasSkillEffect(skillId)) { // 선택한 스킬이 현재 활성화 상태가 아니라면
						    long current = System.currentTimeMillis();
						    if (current - pc.getAutoSkillDelay() < 0) // 스킬 사용 후 딜레이가 남아있는지 확인
						        return; // 딜레이가 남아있다면 함수 종료
						    
						    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); // 스킬 정보 불러오기
						    if (skill.getMpConsume() <= pc.getCurrentMp()) { // 현재 MP가 스킬 사용에 필요한 MP보다 많거나 같은지 확인
						        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0,
						                                        0); // 조건을 만족하면 스킬 사용
						    }
						    //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
						    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 사용 후 딜레이 시간 설정
//						    pc.updateLight(); // 라이트 업데이트
						}
					}
				} else if (pc.getType() == 2) { //요정
					switch (skillId) {
					case 2: //라이트
					case 3: //실드
					case 8: //홀리 웨폰
					case 12: //인챈트 웨폰
					case 14: //디크리즈 웨이트
					case 21: //블레스드 아머
					case 26: //인챈트 덱스터리티
					case 42: //인챈트 마이티
					case 43: //헤이스트
					case 48: //블레스 웨폰
					case 129: //레지스트 매직
					case 134: //엘븐 그레비티
					case 135: //소울 배리어
					case 136: //인페르노
					case 137: //클리어 마인드
					case 147: //프로텍션 프롬 엘리멘트
					case 148: //어스 웨폰
					case 149: //아쿠아 샷
					case 150: //이글 아이
					case 151: //파이어 실드
					case 152: //퀘이크
					case 155: //댄싱 블레이즈
					case 156: //아이 오브 스톰
					case 158: //네이쳐스 터치
					case 159: //어스 가디언
					case 160: //아쿠아 프로텍트
					case 167: //싸이클론
					case 163: //버닝 웨폰
					case 166: //스톰 샷
					case 168: //아이언 스킨
					case 169: //엑조틱 바이탈라이즈
					case 171: //엘리멘탈 파이어
					case 175: //소울 오브 프레임
					case 176: //어디셔널 파이어
					case 177: //포커스 웨이브
					case 178: //허리케인
					case 179: //샌드 스톰
						if (pc.isSkillDelay()) {
							continue;
						}
					    // 디크리즈 웨이트와 엘븐 그레비티 상호 배제 로직 추가
					    if (skillId == 14 && pc.hasSkillEffect(134)) {
					        continue; // 엘븐 그레비티가 활성화된 경우 디크리즈 웨이트를 사용하지 않음
					    }
					    if (skillId == 134 && pc.hasSkillEffect(14)) {
					        continue; // 디크리즈 웨이트가 활성화된 경우 엘븐 그레비티를 사용하지 않음
					    }
						// 댄싱 블레이즈(155), 샌드 스톰(179) 스킬은 플레이어 캐릭터가 타입1이 24인 무기를 들고 있지 않을 경우에만 실행될 수 있다. 
						if ((skillId == 155) || (skillId == 179)) {
							L1ItemInstance weapon = pc.getWeapon();
							if ((weapon == null) ||

									(weapon.getItem().getType1() == 24)) {
								continue;
							}
						}
						if (skillId == 178) { // 허리케인 스킬 사용 시, 플레이어가 타입1이 20인 무기를 들고 있어야 함
						    L1ItemInstance weapon = pc.getWeapon();
						    if ((weapon == null) || (weapon.getItem().getType1() != 20)) {
						        continue; // 조건 불충족 시 다음 스킬로 넘어감
						    }
						}
						if (skillId == 136) { // 인페르노 스킬 사용 시, 플레이어가 타입1이 4인 무기를 들고 있어야 함
						    L1ItemInstance weapon = pc.getWeapon();
						    if ((weapon == null) || (weapon.getItem().getType1() != 4)) {
						        continue; // 조건 불충족 시 다음 스킬로 넘어감
						    }
						}
						if ((pc.getElfAttr() == 0) || // 플레이어의 요정 속성에 따라 스킬 사용 제한
						    (pc.getElfAttr() == 1 ? (skillId == 177) && (skillId == 178) :
						    pc.getElfAttr() == 2 ? (skillId == 177) && (skillId == 178) && (skillId == 179) :
						    pc.getElfAttr() == 4 ? (skillId == 178) && (skillId == 179) :
						    (pc.getElfAttr() == 8) && ((skillId == 177) || (skillId == 179)))) {
						    continue; // 조건 불충족 시 다음 스킬로 넘어감
						}
						if (skillId == 48 ? (pc.hasSkillEffect(12)) && (pc.hasSkillEffect(8)) : // 스킬 중복 사용 제한
						    skillId == 43 ? (pc.hasSkillEffect(1001)) && (pc.hasSkillEffect(54)) :
						    skillId == 3 ? (pc.hasSkillEffect(151)) && (pc.hasSkillEffect(168)) :
						    (skillId == 151) && (pc.hasSkillEffect(168))) {
						    continue; // 조건 불충족 시 다음 스킬로 넘어감
						}
						if (!pc.hasSkillEffect(skillId)) { // 스킬이 현재 활성화 상태가 아니라면
						    long current = System.currentTimeMillis();
						    if (current - pc.getAutoSkillDelay() < 0)
						        return; // 스킬 딜레이 중이라면 함수 종료
						    
						    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
						    // 요정의 속성과 다를 경우 스킬 사용 불가
						    if(pc.getElfAttr() != 0){
						        if(skillId >= 148 && skillId <= 179){
						            if(pc.getElfAttr() != skill.getAttr()){
						                return; // 속성 불일치 시 함수 종료
						            }
						        }
						    }
						    
						    if (skill.getMpConsume() <= pc.getCurrentMp()) {
						        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0,
						                                        0); // 스킬 사용
						    }
						    //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
						    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 딜레이 설정
//						    pc.updateLight(); // 라이트 업데이트
						}
					}
				} else if (pc.getType() == 3) { // 법사
					switch (skillId) {
					case 2: //라이트
					case 3: //실드
					case 8: //홀리 웨폰
					case 12: //인챈트 웨폰
					case 14: //디크리즈 웨이트
					case 21: //블레스드 아머
					case 26: //인챈트 덱스터리티
					case 42: //인챈트 마이티
					case 43: //헤이스트
					case 48: //블레스 웨폰
					case 50: //프리징 아머
					case 52: //홀리 워크
					case 54: //그레이터 헤이스트
					case 56: //인챈트 어큐러시
					case 79: //어드밴스 스피릿
						if ((pc.isSkillDelay()) || // 스킬 딜레이 중인지 확인

							    // 스킬 중복 사용 제한
							    (skillId == 8 ? (pc.hasSkillEffect(12)) && (pc.hasSkillEffect(48)) : // 홀리 웨폰 사용 시 인챈트 웨폰과 블레스 웨폰 효과가 있으면 중복 사용 불가
							    skillId == 12 ? (pc.hasSkillEffect(8)) && (pc.hasSkillEffect(48)) : // 인챈트 웨폰 사용 시 홀리 웨폰과 블레스 웨폰 효과가 있으면 중복 사용 불가
							    skillId == 48 ? (pc.hasSkillEffect(12)) && (pc.hasSkillEffect(8)) : // 블레스 웨폰 사용 시 인챈트 웨폰과 홀리 웨폰 효과가 있으면 중복 사용 불가
//							    skillId == 43 ? (pc.hasSkillEffect(1001)) && (pc.hasSkillEffect(54)) : // 헤이스트 사용 시 특정 조건 하에 그레이터 헤이스트와 중복 사용 불가
							    skillId == 43 ? (pc.hasSkillEffect(54)) : // 헤이스트가 활성화된 경우 그레이터 헤이스트를 사용하지 않음
						        skillId == 54 ? (pc.hasSkillEffect(43)) : // 그레이터 헤이스트가 활성화된 경우 헤이스트를 사용하지 않음
							    (skillId == 3) && ((pc.hasSkillEffect(151)) || (pc.hasSkillEffect(168))))) { // 실드 사용 시 특정 스킬 효과가 있으면 중복 사용 불가
							    continue; // 조건 불충족 시 다음 스킬로 넘어감
							}
							if (!pc.hasSkillEffect(skillId)) { // 스킬이 현재 활성화 상태가 아니라면
							    long current = System.currentTimeMillis();
							    if (current - pc.getAutoSkillDelay() < 0)
							        return; // 스킬 딜레이 중이라면 함수 종료
							    
							    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
							    if (skill.getMpConsume() <= pc.getCurrentMp()) {
							        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0); // 스킬 사용
							    }
							    //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
							    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 딜레이 설정
//							    pc.updateLight(); // 라이트 업데이트
							}
						}
				} else if (pc.getType() == 4) { //다크 엘프
					switch (skillId) {
					case 2: //라이트
					case 3: //실드
					case 8: //홀리 웨폰
					case 12: //인챈트 웨폰
					case 14: //디크리즈 웨이트
					case 98: //인챈트 베놈
					case 99: //쉐도우 아머
					case 101: //무빙 악셀레이션
					case 102: //버닝 스피릿츠
					case 105: //더블 브레이크
					case 106: //언케니 닷지
					case 107: //쉐도우 팽
					case 109: //드레스 마이티
					case 110: //드레스 덱스터리티
					case 111: //드레스 이베이젼
					case 234: //루시퍼
						if ((pc.isSkillDelay()) || // 스킬 딜레이 중인지 확인

							    // 스킬 중복 사용 제한
							    (skillId == 8 ? (pc.hasSkillEffect(12)) && (pc.hasSkillEffect(48)) && (pc.hasSkillEffect(107)) : // 홀리 웨폰 사용 시 인챈트 웨폰, 블레스 웨폰, 쉐도우 팽 효과가 있으면 중복 사용 불가
							    skillId == 12 ? (pc.hasSkillEffect(8)) && (pc.hasSkillEffect(48)) && (pc.hasSkillEffect(107)) : // 인챈트 웨폰 사용 시 홀리 웨폰, 블레스 웨폰, 쉐도우 팽 효과가 있으면 중복 사용 불가
							    skillId == 109 ? pc.hasSkillEffect(42) : // 드레스 마이티 사용 시 인챈트 마이티 효과가 있으면 중복 사용 불가
							    skillId == 110 ? pc.hasSkillEffect(26) : // 드레스 덱스터리티 사용 시 인챈트 덱스터리티 효과가 있으면 중복 사용 불가
							    (skillId == 3) && ((pc.hasSkillEffect(151)) || (pc.hasSkillEffect(168))))) { // 실드 사용 시 특정 스킬 효과가 있으면 중복 사용 불가
							    continue; // 조건 불충족 시 다음 스킬로 넘어감
							}
							if (!pc.hasSkillEffect(skillId)) { // 스킬이 현재 활성화 상태가 아니라면
							    long current = System.currentTimeMillis();
							    if (current - pc.getAutoSkillDelay() < 0)
							        return; // 스킬 딜레이 중이라면 함수 종료
							    
							    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
							    if (skill.getMpConsume() <= pc.getCurrentMp()) {
							        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0); // 스킬 사용
							    }
							    //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
							    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 딜레이 설정
//							    pc.updateLight(); // 라이트 업데이트
							}
					}
				} else if (pc.getType() == 5) {
					switch (skillId) {
					case 181: //드래곤 스킨
					case 185: //각성:안타라스
					case 186: //블러드러스트
					case 190: //각성:파푸리온
					case 191: //모탈 바디
					case 195: //각성:발라카스
					case 197: //각성:린드비오르
						
						final int AURAKIA_PASSIVE_SKILL_ID = 16; // 아우라키아 패시브 스킬 ID
						final int AWAKEN_ANTARAS = 185;
						final int AWAKEN_FAFURION = 190;
						final int AWAKEN_VALAKAS = 195;
						final int AWAKEN_LINDVIOR = 197;	
						
						// 아우라키아 패시브 스킬을 배웠는지 확인
						boolean hasAurakia = pc.hasSkillEffect(AURAKIA_PASSIVE_SKILL_ID);

						// 안타라스, 파푸리온, 발라카스 중 하나만 활성화되어야 함 (린드비오르는 별도로 취급)
						List<Integer> exclusiveAwakeningSkills = Arrays.asList(AWAKEN_ANTARAS, AWAKEN_FAFURION, AWAKEN_VALAKAS);

						// 이미 사용된 각성 스킬이 있는지 확인 (린드비오르 제외)
						boolean hasAnyExclusiveAwakeningEffect = exclusiveAwakeningSkills.stream()
						    .anyMatch(id -> pc.hasSkillEffect(id));

						// 현재 시도하는 스킬이 안타라스, 파푸리온, 발라카스 중 하나이고 아우라키아 스킬이 없으면서 다른 각성 스킬이 활성화되어 있으면 함수 종료
						if (exclusiveAwakeningSkills.contains(skillId) && !hasAurakia && hasAnyExclusiveAwakeningEffect) {
						    return;
						}

						// 스킬 딜레이 상태 확인
						if (pc.isSkillDelay()) {
						    return; // 딜레이 중이면 함수 종료
						}

						// 현재 선택된 스킬이 활성화 상태가 아니라면
						if (!pc.hasSkillEffect(skillId)) {
						    long current = System.currentTimeMillis();
						    // 스킬 사용 후 딜레이가 남아있는지 확인
						    if (current - pc.getAutoSkillDelay() < 0) {
						        return; // 딜레이가 남아있다면 함수 종료
						    }

						    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); // 스킬 정보 불러오기
						    // 현재 MP가 스킬 사용에 필요한 MP보다 많거나 같은지 확인
						    if (skill.getMpConsume() <= pc.getCurrentMp()) {
						        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0); // 조건을 만족하면 스킬 사용
						    }
						    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 사용 후 딜레이 시간 설정
//						    pc.updateLight(); // 라이트 업데이트
						}
					}
				} else if (pc.getType() == 6) { //환술사
					switch (skillId) {
					case 201: //미러 이미지
					case 204: //일루션:오우거
					case 206: //컨센트레이션
					case 209: //일루션:리치
					case 211: //페이션스
					case 214: //일루션:골렘
					case 216: //인사이트
					case 218: //리듀스 웨이트
					case 219: //일루션:아바타
					case 223: //포커스 스피릿츠
						if (skillId == 204) { // 일루션:오우거 스킬 사용 시 중복 사용 체크
						    if (pc.hasSkillEffect(L1SkillId.CUBE_OGRE)) { // 이미 CUBE_OGRE 스킬 효과가 있다면
						        continue; // 중복 사용 불가, 다음 스킬로 넘어감
						    }
						}
						if (skillId == 219) { // 일루션:아바타 스킬 사용 시 중복 사용 체크
						    if (pc.hasSkillEffect(L1SkillId.CUBE_AVATAR)) { // 이미 CUBE_AVATAR 스킬 효과가 있다면
						        continue; // 중복 사용 불가, 다음 스킬로 넘어감
						    }
						}
						if (pc.isSkillDelay()) { // 스킬 딜레이 중인지 확인
						    continue; // 딜레이 중이면 다음 스킬로 넘어감
						}
						if (!pc.hasSkillEffect(skillId)) { // 현재 선택된 스킬이 활성화 상태가 아니라면
						    long current = System.currentTimeMillis();
						    if (current - pc.getAutoSkillDelay() < 0) // 스킬 사용 후 딜레이가 남아있는지 확인
						        return; // 딜레이가 남아있다면 함수 종료
						    
						    L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); // 스킬 정보 불러오기
						    if (skill.getMpConsume() <= pc.getCurrentMp()) { // 현재 MP가 스킬 사용에 필요한 MP보다 많거나 같은지 확인
						        new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0); // 조건을 만족하면 스킬 사용
						    }
						    //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
						    pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 사용 후 딜레이 시간 설정
//						    pc.updateLight(); // 라이트 업데이트
						}
					}
				} else if (pc.getType() == 7) { //전사
					switch (skillId) {
					case 2: //라이트
					case 3: //실드
					case 8: //홀리 웨폰
					case 226: //기간틱
					case 231: //타이탄:라이징
						if ((!pc.isSkillDelay()) && // 스킬 딜레이 상태가 아닌 경우에만 조건 확인

							    // 홀리 웨폰 스킬 사용 조건: 인챈트 웨폰(12) 또는 블레스 웨폰(48) 중 하나라도 활성화 상태가 아니어야 함
							    (skillId == 8 ? (!pc.hasSkillEffect(12)) || (!pc.hasSkillEffect(48)) :

							    // 실드 스킬 사용 조건: 파이어 실드(151)와 아이언 스킨(168) 스킬이 모두 비활성화 상태여야 함
							    (skillId != 3) || ((!pc.hasSkillEffect(151)) && (!pc.hasSkillEffect(168))))) {
							    
							    if ((!pc.hasSkillEffect(skillId)) && (!pc.isSkillDelay())) { // 선택한 스킬이 현재 활성화되어 있지 않고, 스킬 딜레이도 아닌 경우
							        long current = System.currentTimeMillis();
							        if (current - pc.getAutoSkillDelay() < 0)
							            return; // 현재 시간이 스킬 자동 딜레이 시간보다 작으면, 즉 딜레이 중이면 함수 종료
							        
							        L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); // 스킬 정보 불러오기
							        if (skill.getMpConsume() <= pc.getCurrentMp()) { // 현재 MP가 스킬 사용에 필요한 MP보다 많거나 같은지 확인
							            new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 0); // 조건을 만족하면 스킬 사용
							        }
							        //L1SkillDelay.onSkillUse(pc, skill.getReuseDelay());
							        pc.setAutoSkillDelay(System.currentTimeMillis() + skill.getReuseDelay() + 1000); // 스킬 사용 후 딜레이 시간 설정
//							        pc.updateLight(); // 라이트 업데이트
							    }
							}
							break;
					}
				}
			}
		}
	}
}
