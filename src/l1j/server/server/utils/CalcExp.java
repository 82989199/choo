package l1j.server.server.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.MJCaptchaSystem.MJCaptcha;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Basic.HuntingGround.MJCompanionHuntingGround;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.PartyMapInfoTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1SpecialMap;

public class CalcExp {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(CalcExp.class.getName());

	public static final int MAX_EXP = ExpTable.getExpByLevel(100) - 1;

	private CalcExp() {
	}

	public static void calcExp(L1PcInstance l1pcinstance, int targetid, ArrayList<?> acquisitorList,
			ArrayList<?> hateList, int exp) {

		if(l1pcinstance == null)
			return;
		
		int i = 0;
		double party_level = 0;
		double dist = 0;
		int member_exp = 0;
		int member_lawful = 0;
		L1Object l1object = L1World.getInstance().findObject(targetid);
		L1NpcInstance npc = (L1NpcInstance) l1object;

		
		if (l1pcinstance.getLevel() < 55) { // 클라우디아
			int add_lawful = (int) (npc.getLawful() * Config.RATE_LAWFUL) * -1;
			l1pcinstance.addLawful(add_lawful);
			double exppenalty = ExpTable.getPenaltyRate(l1pcinstance.getLevel());
			int add_exp = (int) (exp * exppenalty * Config.RATE_XP_CLAUDIA); // 배율로 올라가는 경험치제외
			if (l1pcinstance.getLevel() < 5)
				add_exp *= 5;
			l1pcinstance.addExp(add_exp);
			return;
		}
		// 헤이트의 합계를 취득
		L1Character acquisitor;
		int hate = 0;
		int acquire_exp = 0;
		int acquire_lawful = 0;
		int party_exp = 0;
		int party_lawful = 0;
		int totalHateExp = 0;
		int totalHateLawful = 0;
		int partyHateExp = 0;
		int partyHateLawful = 0;
		int ownHateExp = 0;

		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		for (i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if(acquisitor instanceof MJCompanionInstance)
				acquisitor = ((MJCompanionInstance) acquisitor).get_master();
			hate = (Integer) hateList.get(i);
			if (acquisitor != null && !acquisitor.isDead()) {
				totalHateExp += hate;
				if (acquisitor instanceof L1PcInstance) {
					totalHateLawful += hate;
				}
			} else { // null였거나 죽어 있으면(자) 배제
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}
		if (totalHateExp == 0) { // 취득자가 없는 경우
			return;
		}

		if (l1object != null && !(npc instanceof L1PetInstance) && !(npc instanceof L1SummonInstance)) {
			// int exp = npc.get_exp();
			if (!L1World.getInstance().isProcessingContributionTotal() && l1pcinstance.getHomeTownId() > 0) {
				int contribution = npc.getLevel() / 10;
				l1pcinstance.addContribution(contribution);
			}
			int lawful = npc.getLawful();

			if (l1pcinstance.isInParty() && getPartyIsinScreen(l1pcinstance) > 1) { // 파티중
				partyHateExp = 0;
				partyHateLawful = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if(acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();
					
					hate = (Integer) hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							partyHateExp += hate;
							partyHateLawful += hate;
						} else if (l1pcinstance.getParty().isMember(pc)) {
							partyHateExp += hate;
							partyHateLawful += hate;
						} else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							if (totalHateLawful > 0) {
								acquire_lawful = (lawful * hate / totalHateLawful);
							}
							AddExp(pc, acquire_exp, acquire_lawful);
						}
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						} else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						} else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							AddExpPet(pet, acquire_exp);
						}
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						} else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						} else {
						}
					}

				}
				if (totalHateExp > 0) {
					party_exp = (exp * partyHateExp / totalHateExp);
				}
				if (totalHateLawful > 0) {
					party_lawful = (lawful * partyHateLawful / totalHateLawful);
				}

				// EXP, 로우훌 배분
				double pri_bonus = 0;
				L1PcInstance leader = l1pcinstance.getParty().getLeader();
				if (leader.isCrown() && (l1pcinstance.knownsObject(leader) || l1pcinstance.equals(leader))) {
					pri_bonus = 0.059;
				}

				// PT경험치의 계산
				L1PcInstance[] ptMembers = l1pcinstance.getParty().getMembers();
				double pt_bonus = 0;
				for (L1PcInstance each : l1pcinstance.getParty().getMembers()) {
					if (l1pcinstance.knownsObject(each) || l1pcinstance.equals(each)) {
						party_level += each.getLevel() * each.getLevel();
					}
					if (l1pcinstance.knownsObject(each)) {
						pt_bonus += 0.04;
					}
				}

				party_exp = (int) (party_exp * (0.5 + pt_bonus + pri_bonus));
				if (Config.IS_PARTY_EXP) {
					party_exp *= (int) Config.ADD_PARTY_EXP;
				}

				// 자캐릭터와 그 애완동물·사몬의 헤이트의 합계를 산출
				if (party_level > 0) {
					dist = ((l1pcinstance.getLevel() * l1pcinstance.getLevel()) / party_level);
				}
				member_exp = (int) (party_exp * dist);
				member_lawful = (int) (party_lawful * dist);

				ownHateExp = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if(acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();
					hate = (Integer) hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							ownHateExp += hate;
						}
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					}
				}
				// 자캐릭터와 그 애완동물·사몬에 분배
				if (ownHateExp != 0) { // 공격에 참가하고 있었다
					for (i = hateList.size() - 1; i >= 0; i--) {
						acquisitor = (L1Character) acquisitorList.get(i);
						if(acquisitor instanceof MJCompanionInstance)
							acquisitor = ((MJCompanionInstance) acquisitor).get_master();
						hate = (Integer) hateList.get(i);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (pc == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExp(pc, acquire_exp, member_lawful);
							}
						} else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) pet.getMaster();
							if (master == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExpPet(pet, acquire_exp);
							}
						} else if (acquisitor instanceof L1SummonInstance) {
							L1SummonInstance sum = (L1SummonInstance) acquisitor;
							if (sum.getNpcTemplate().get_npcId() == 7320000) {
								L1PcInstance master = (L1PcInstance) sum.getMaster();
								if (master == l1pcinstance) {
									if (ownHateExp > 0) {
										acquire_exp = (member_exp * hate / ownHateExp);
									}
									AddExp(master, acquire_exp, member_lawful);
								}
							}
						}
					}
				} else { // 공격에 참가하고 있지 않았다
					// 자캐릭터에만 분배
					AddExp(l1pcinstance, member_exp, member_lawful);
				}

				// 파티 멤버와 그 애완동물·사몬의 헤이트의 합계를 산출
				for (int cnt = 0; cnt < ptMembers.length; cnt++) {
					if (l1pcinstance.knownsObject(ptMembers[cnt])) {
						if (party_level > 0) {
							dist = ((ptMembers[cnt].getLevel() * ptMembers[cnt].getLevel()) / party_level);
						}
						member_exp = (int) (party_exp * dist);
						member_lawful = (int) (party_lawful * dist);

						ownHateExp = 0;
						for (i = hateList.size() - 1; i >= 0; i--) {
							acquisitor = (L1Character) acquisitorList.get(i);
							if(acquisitor instanceof MJCompanionInstance)
								acquisitor = ((MJCompanionInstance) acquisitor).get_master();
							hate = (Integer) hateList.get(i);
							if (acquisitor instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) acquisitor;
								if (pc == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							} else if (acquisitor instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) pet.getMaster();
								if (master == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							} else if (acquisitor instanceof L1SummonInstance) {
								L1SummonInstance summon = (L1SummonInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) summon.getMaster();
								if (master == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							}
						}
						// 파티 멤버와 그 애완동물·사몬에 분배
						if (ownHateExp != 0) { // 공격에 참가하고 있었다
							for (i = hateList.size() - 1; i >= 0; i--) {
								acquisitor = (L1Character) acquisitorList.get(i);
								if(acquisitor instanceof MJCompanionInstance)
									acquisitor = ((MJCompanionInstance) acquisitor).get_master();
								hate = (Integer) hateList.get(i);
								if (acquisitor instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) acquisitor;
									if (pc == ptMembers[cnt]) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExp(pc, acquire_exp, member_lawful);
									}
								} else if (acquisitor instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) acquisitor;
									L1PcInstance master = (L1PcInstance) pet.getMaster();
									if (master == ptMembers[cnt]) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExpPet(pet, acquire_exp);
									}
								} else if (acquisitor instanceof L1SummonInstance) {
									L1SummonInstance sum = (L1SummonInstance) acquisitor;
									if (sum.getNpcTemplate().get_npcId() == 7320000) {
										L1PcInstance pc = (L1PcInstance) sum.getMaster();
										AddExp(pc, acquire_exp, acquire_lawful);
									}
								}
							}
						} else { // 공격에 참가하고 있지 않았다
							// 파티 멤버에만 분배
							AddExp(ptMembers[cnt], member_exp, member_lawful);
						}
					}
				}
			} else { // 파티를 짜지 않았다
				// EXP, 로우훌의 분배
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if(acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();
					hate = (Integer) hateList.get(i);
					acquire_exp = (exp * hate / totalHateExp);
					if (acquisitor instanceof L1PcInstance) {
						if (totalHateLawful > 0) {
							acquire_lawful = (lawful * hate / totalHateLawful);
						}
					}

					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						AddExp(pc, acquire_exp, acquire_lawful);
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						AddExpPet(pet, acquire_exp);
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance sum = (L1SummonInstance) acquisitor;
						if (sum.getNpcTemplate().get_npcId() == 7320000) {
							L1PcInstance pc = (L1PcInstance) sum.getMaster();
							AddExp(pc, acquire_exp, acquire_lawful);
						}
					}
				}
			}
		}
	}

	private static void AddExp(L1PcInstance pc, int exp, int lawful) {
		if (Config.STANDBY_SERVER)
			return;

		if (pc.isGhost()) {
			return;
		}

		int add_lawful = (int) (lawful * Config.RATE_LAWFUL) * -1;
		pc.addLawful(add_lawful);
		MJCompanionHuntingGround hground = MJCompanionHuntingGround.get_hunting_ground(pc.getMapId());
		if(hground != null){
			double magnification_exp = (double)exp * hground.get_magnification_by_exp();
			exp -= magnification_exp;
			MJCompanionInstance companion = pc.get_companion();
			if(companion != null){
				companion.update_exp((int)magnification_exp);
			}
			if(exp <= 0)
				return;
		}

		/** 로봇시스템 **/
		if (pc.getAI() != null) {
			return;
		}

		/*
		 * if (pc.getLevel() >= Config.LIMITLEVEL) {// 경험치 pc.sendPackets(new
		 * S_SystemMessage("레벨제한으로 더이상 경험치 획득이 불가능합니다")); return; }
		 */

		if (pc.getLevel() >= Config.New_LevelMin1 && pc.getLevel() <= Config.New_LevelMax1) {
			exp *= Config.New_LevelExp1;
		} else if (pc.getLevel() >= Config.New_LevelMin2 && pc.getLevel() <= Config.New_LevelMax2) {
			exp *= Config.New_LevelExp2;
		} else if (pc.getLevel() >= Config.New_LevelMin3 && pc.getLevel() <= Config.New_LevelMax3) {
			exp *= Config.New_LevelExp3;
		} else if (pc.getLevel() >= Config.New_LevelMin4 && pc.getLevel() <= Config.New_LevelMax4) {
			exp *= Config.New_LevelExp4;
		}
		
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		double foodBonus = 1;
		double expposion = 1;// 성장물약 및 pc방
		double dollBonus = 1; // 마법인형 경험치 보너스
		double levelupBonus = 1;
		double clanBonus = 1;
		double comboBonus = 1; // 콤보시스템
		double addexp1 = 1; // 성장의문장
		double addexp2 = 1; // 완력의 문장:성장
		double addexp3 = 1; // 민첩의 문장:성장
		double addexp4 = 1; // 지식의 문장:성장
		double addexp5 = 1; // 회복의 문장:성장
		double comab = 1;// 코마
		double Arka = 1;// 아르카의유물
		double jinDEATHstr = 1;// 진데스 완력
		double dragon_hunter_bless = 1;
		double dragon_SET_bless = 1;
		double HUNTER_BLESS = 1;
		double HUNTER_BLESS3 = 1;
		double HUNTER_BLESS4 = 1;
		double special_map = 1;
		double party_map_rate = 1;
		double exp_posion = 1;

		if (SpecialMapTable.getInstance().isSpecialMap(pc.getMapId())) {
			L1SpecialMap SM = SpecialMapTable.getInstance().getSpecialMap(pc.getMapId());
			if (SM != null) {
				special_map += SM.getExpRate();
			}
		}

		if (pc.isInParty()) {
			Double pmr = PartyMapInfoTable.getInstance().getPartyMapExpRate(pc.getMapId());
			if (pmr != 0) {
				int member_count = 0;
				for (L1PcInstance member : pc.getParty().getList()) {
					if (pc.getId() == member.getId())
						continue;
					if (pc.getLocation().isInScreen(member.getLocation())) {
						member_count++;
					}
				}

				if (member_count != 0) {
					party_map_rate += pmr;
				}
			}
		}

		if (pc.hasSkillEffect(L1SkillId.DRAGON_HUNTER_BLESS)) {
			dragon_hunter_bless += 0.2;
		}
		if (pc.hasSkillEffect(L1SkillId.DRAGON_SET)) {
			dragon_SET_bless += 0.2;
		}
		if (pc.hasSkillEffect(L1SkillId.HUNTER_BLESS)) {
			HUNTER_BLESS += 0.05;
		}
		if (pc.hasSkillEffect(L1SkillId.HUNTER_BLESS3)) {
			HUNTER_BLESS3 += 0.02;
		}
		if (pc.hasSkillEffect(L1SkillId.TOGO_BUFF)) {
			HUNTER_BLESS4 += 0.05;
		}

		for (L1DollInstance doll : pc.getDollList().values()) {
			int dollType = doll.getDollType();
			if (dollType == L1DollInstance.DOLLTYPE_SNOWMAN_A	|| dollType == L1DollInstance.DOLLTYPE_SNOWMAN_B
			 || dollType == L1DollInstance.DOLLTYPE_SNOWMAN_C	|| dollType == L1DollInstance.DOLLTYPE_자이언트
			 || dollType == L1DollInstance.DOLL_mummy			|| dollType == L1DollInstance.DOLLTYPE_축자이언트) {
				dollBonus += 0.1; // EXP +10%
				
			} else if (dollType == L1DollInstance.jindeathknight) {
				dollBonus += 0.07; // EXP +7%
				
			} else if (dollType == L1DollInstance.DOLLTYPE_DEATHNIGHT || dollType == L1DollInstance.DOLLTYPE_축데스) {
				dollBonus += 0.2; // EXP +20%
				
			} else if (dollType == L1DollInstance.DOLLTYPE_인어 || dollType == L1DollInstance.DOLLTYPE_COBO) {//<다이노스
				dollBonus += 0.03; // EXP +3%
				
			} else if (dollType == L1DollInstance.ruler1) {
				dollBonus += 0.30; // EXP +30%
				
			} else if (dollType == L1DollInstance.ruler5) {
				dollBonus += 0.15; // EXP +15%
				
			} else if (dollType == L1DollInstance.ruler2 || dollType == L1DollInstance.ruler6) {
				dollBonus += 0.10; // EXP +10%
				
			} else if (dollType == L1DollInstance.ruler3 || dollType == L1DollInstance.ruler7) {
				dollBonus += 0.05; // EXP +5%
				
			} else if (dollType == L1DollInstance.ruler4 || dollType == L1DollInstance.ruler8) {
				dollBonus += 0.02; // EXP +2%
				
			} else if (dollType == L1DollInstance.DOLLTYPE_기르타스) {
				dollBonus += 0.5; // EXP +50%
				
			} else if (dollType == L1DollInstance.DOLLTYPE_알비노데몬 || dollType == L1DollInstance.DOLLTYPE_알비노피닉스 || dollType == L1DollInstance.DOLLTYPE_알비노유니콘) {
				dollBonus += 0.27; // EXP +27%
				
			} else if (dollType == L1DollInstance.Antaras
					|| dollType == L1DollInstance.DOLLTYPE_할파스		|| dollType == L1DollInstance.DOLLTYPE_아우라키아		|| dollType == L1DollInstance.DOLLTYPE_베히모스			
					|| dollType == L1DollInstance.DOLLTYPE_유니콘		|| dollType == L1DollInstance.DOLLTYPE_피닉스			|| dollType == L1DollInstance.DOLLTYPE_그림리퍼
					|| dollType == L1DollInstance.DOLLTYPE_다크스타조우	|| dollType == L1DollInstance.DOLLTYPE_디바인크루세이더 || dollType == L1DollInstance.DOLLTYPE_군터
					|| dollType == L1DollInstance.DOLLTYPE_오우거킹		|| dollType == L1DollInstance.DOLLTYPE_다크하딘			|| dollType == L1DollInstance.DOLLTYPE_드래곤슬레이어
					|| dollType == L1DollInstance.DOLLTYPE_암흑대장로) {
				dollBonus += 0.25; // EXP +25%
			}
		}
		if (pc.hasSkillEffect(L1SkillId.COOKING_1_7_N) || pc.hasSkillEffect(L1SkillId.COOKING_1_7_S)) {
			foodBonus += 0.01;
		} else if (pc.hasSkillEffect(L1SkillId.COOKING_1_15_N) || pc.hasSkillEffect(L1SkillId.COOKING_1_15_S)) {
			foodBonus += 0.05;
		} else if (pc.hasSkillEffect(L1SkillId.COOKING_1_23_N) || pc.hasSkillEffect(L1SkillId.COOKING_1_23_S)) {
			foodBonus += 0.09;
		} else if (pc.hasSkillEffect(L1SkillId.수련자의닭고기스프) || pc.hasSkillEffect(L1SkillId.COOK_GROW) || pc.hasSkillEffect(L1SkillId.COOK_GROW_1)) {
			foodBonus += 0.04;
		} else if (pc.hasSkillEffect(L1SkillId.천하장사버프)) {
			foodBonus += 0.2;
		} else if (pc.hasSkillEffect(L1SkillId.miso1)) {
			foodBonus += 0.1;
		} else if (pc.hasSkillEffect(L1SkillId.메티스정성스프)) {
			foodBonus += 0.05;// 5%
		}
		if (pc.hasSkillEffect(L1SkillId.COMA_B)) {
			comab += 0.2;
		}


		if (pc.hasSkillEffect(L1SkillId.EXP_POTION)) {
			expposion += pc.PC방_버프 ? 0.4D : 0.2D;
		}
		
		if (pc.hasSkillEffect(L1SkillId.Tam_Fruit5))
			expposion += 0.05;

		if (pc.hasSkillEffect(L1SkillId.EXP_BUFF) && pc.getAccount().getBlessOfAin() >= 10000) {
			int lvl = pc.getLevel();
			if (lvl <= 79)
				exp_posion += 1.30D;
			else if (lvl <= 81)
				exp_posion += 1.20D;
			else if (lvl <= 83)
				exp_posion += 1.10D;
			else if (lvl <= 85)
				exp_posion += 1.00D;
			else if (lvl <= 89)
				exp_posion += 0.90D;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

		double beginnerBonus = 1;

		/** 경험치 기준 2%면 0.2 설정해야한다 **/
		if (Config.ALT_BEGINNER_BONUS && pc.getLevel() <= 65) {
			beginnerBonus += 0.5;
		}
		if (pc.getInventory().checkEquipped(900033) || pc.getInventory().checkEquipped(9961)
				|| pc.getInventory().checkEquipped(900116)) {
			Arka += 0.2;// 2%
		}
		if (pc.getInventory().checkEquipped(910322) || pc.getInventory().checkEquipped(910323) || pc.getInventory().checkEquipped(910324)) {
			jinDEATHstr += 0.4;//10%
		}

		int 성장의문장 = pc.getInventory().getEnchantCount(900020);
		if (pc.getInventory().checkEquipped(900020)) {
			addexp1 += (0.01 * 성장의문장);
		}
		int 완력의문장성장 = pc.getInventory().getEnchantCount(900093);
		if (pc.getInventory().checkEquipped(900093)) {
			addexp2 += (0.04 * 완력의문장성장);
		}
		int 민첩의문장성장 = pc.getInventory().getEnchantCount(900094);
		if (pc.getInventory().checkEquipped(900094)) {
			addexp3 += (0.04 * 민첩의문장성장);
		}
		int 지식의문장성장 = pc.getInventory().getEnchantCount(900095);
		if (pc.getInventory().checkEquipped(900095)) {
			addexp4 += (0.04 * 지식의문장성장);
		}
		int 회복의문장성장 = pc.getInventory().getEnchantCount(900099);
		if (pc.getInventory().checkEquipped(900099)) {
			addexp5 += (0.04 * 회복의문장성장);
		}

		if (pc.hasSkillEffect(L1SkillId.레벨업보너스))
			levelupBonus += 1.23;

		try{
			if (pc.getAccount().getBlessOfAin() > 0) {
				if (pc.hasSkillEffect(80006)) {
					if (pc.getComboCount() <= 20) {
						comboBonus += (pc.getComboCount() * 0.1);
					}
					if (comboBonus > 1.0D) {
						pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, (exp * comboBonus)), pc);
						//pc.getAccount().addBlessOfAin(-(int) (exp * comboBonus));
						if (pc.getClan() != null)
							clan.addBlessCount((int) (exp * comboBonus));
					}
				}
			}

			double einhasadBonus = 1;
			if (pc.getAccount().getBlessOfAin() > 10000) {
				int blessOfAin_result = (int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp);
				if (pc.getAccount().getBlessOfAin() <= 2000000) {
					einhasadBonus += 1.0D;
				} else if (pc.getAccount().getBlessOfAin() >= 2000001 && pc.getAccount().getBlessOfAin() <= 18000000) {
					einhasadBonus += 1.0D;
				} else if (pc.getAccount().getBlessOfAin() >= 1800001 && pc.getAccount().getBlessOfAin() <= 34000000) {
					einhasadBonus += 1.3D;
				} else if (pc.getAccount().getBlessOfAin() >= 34000001){
					einhasadBonus += 1.6D;
				}
				pc.getAccount().addBlessOfAin(-blessOfAin_result, pc);
				//pc.getAccount().addBlessOfAin(-exp);
				if (pc.getClan() != null)
					clan.addBlessCount(exp);
				//einhasadBonus += 0.77;
				// einhasadBonus = 1.00;
				if (pc.PC방_버프) {
					einhasadBonus += 0.20;
					pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
					//pc.getAccount().addBlessOfAin(-exp);
					if (pc.getClan() != null)
						clan.addBlessCount(exp);
					pc.sendPackets(new S_PacketBox(82, pc));
				}
				//SC_REST_EXP_INFO_NOTI.send(pc);
			}

			if (pc.getExpAmplifier() != null)
				einhasadBonus += pc.getExpAmplifier().getMagnifier();

			double emeraldBonus = 1;

			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) && pc.getAccount().getBlessOfAin() > 10000) {
				emeraldBonus += 0.80;
				
				pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
				//pc.getAccount().addBlessOfAin(-exp);
				if (pc.getClan() != null)
					clan.addBlessCount(exp);
				if (pc.PC방_버프) {
					einhasadBonus += 0.20;
					pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
					//pc.getAccount().addBlessOfAin(-exp);
					if (pc.getClan() != null)
						clan.addBlessCount(exp);
					if (pc.hasSkillEffect(80006)) {
						if (pc.getComboCount() <= 10) {
							// comboBonus = 0.1D * pc.getComboCount();
						} else if ((pc.getComboCount() > 10) && (pc.getComboCount() <= 15)) {
							// comboBonus = 0.1D * pc.getComboCount();
							// comboBonus += 0.2D * (pc.getComboCount() - 10);
						} else if (pc.getComboCount() > 15) {
							// comboBonus = 3.0D;
						}
						if (comboBonus > 0.0D) {
							pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, (exp * comboBonus)), pc);
							//pc.getAccount().addBlessOfAin(-(int) (exp * comboBonus));
							if (pc.getClan() != null)
								clan.addBlessCount((int) (exp * comboBonus));
						}
					}
					pc.sendPackets(new S_PacketBox(82, pc));
				}
				//SC_REST_EXP_INFO_NOTI.send(pc);
			} else if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE) && pc.getAccount().getBlessOfAin() > 10000) {
				if (pc.getLevel() >= 49 && pc.getLevel() <= 54)
					einhasadBonus += 0.53;
				else if (pc.getLevel() >= 55 && pc.getLevel() <= 59)
					einhasadBonus += 0.43;
				else if (pc.getLevel() >= 60 && pc.getLevel() <= 64)
					einhasadBonus += 0.33;
				else if (pc.getLevel() >= 65)
					einhasadBonus += 0.23;
				pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
				//pc.getAccount().addBlessOfAin(-exp);
				if (pc.getClan() != null)
					clan.addBlessCount(exp);
				//SC_REST_EXP_INFO_NOTI.send(pc);
				//pc.sendPackets(new S_PacketBox(S_PacketBox.EINHASAD, pc));
				if (pc.getAccount().getBlessOfAin() <= 10000) {
					pc.removeSkillEffect(L1SkillId.DRAGON_PUPLE);
				}
			} else if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) && pc.getAccount().getBlessOfAin() > 10000) {
				einhasadBonus += 0.8;
				pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
				//pc.getAccount().addBlessOfAin(-exp);
				if (pc.getClan() != null)
					clan.addBlessCount(exp);
				if (pc.hasSkillEffect(80006)) {
					if (pc.getComboCount() <= 10) {
						// comboBonus = 0.1D * pc.getComboCount();
					} else if ((pc.getComboCount() > 10) && (pc.getComboCount() <= 15)) {
						// comboBonus = 0.1D * pc.getComboCount();
						// comboBonus += 0.2D * (pc.getComboCount() - 10);
					} else if (pc.getComboCount() > 15) {
						// comboBonus = 3.0D;
					}
					if (comboBonus > 0.0D) {
						pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp * comboBonus), pc);
						//pc.getAccount().addBlessOfAin(-(int) (exp * comboBonus));
						if (pc.getClan() != null)
							clan.addBlessCount((int) (exp * comboBonus));
					}
				}
				//SC_REST_EXP_INFO_NOTI.send(pc);
				//pc.sendPackets(new S_PacketBox(S_PacketBox.EINHASAD, pc));
				if (pc.getAccount().getBlessOfAin() <= 10000) {
					pc.removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
				}
			}

			if (pc.getAccount().getBlessOfAin() > 10000 || einhasadBonus != 0){
				SC_REST_EXP_INFO_NOTI.send(pc);
			}
			
			double clanOnlineBonus = 1;

			/** 혈맹버프 추가경험치 50% **/
			if (pc.getClanid() != 0) {
				if (pc.getClan().getOnlineClanMember().length >= Config.CLAN_COUNT) {
//					clanOnlineBonus += 0.10;
					clanOnlineBonus += 0.50; //50% 맞나? 1.50이 되야되는거 아닌가?
				}
			}

			/** 성혈추가경험치지급 **/
			double BloodBonus = 1;
			if (clan != null && clan.getCastleId() != 0) {
				BloodBonus += Config.성혈경험치;
			}

			/** 천상의 계곡 해당레벨 부터 경험치안받도록 **/
			if ((pc.getLevel() >= 99) && (pc.getMapId() >= 1911 && pc.getMapId() <= 1912)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "해당 사냥터에서 레벨 99이상은 경험치가 오르지 않습니다."));
				return;
			}
			/** 말하는 섬 던전 해당레벨 부터 경험치 안받도록 **/
			if ((pc.getLevel() > Config.New_Cha1) && (pc.getMapId() >= 1 && pc.getMapId() <= 2)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "해당 사냥터에서 레벨 [" + Config.New_Cha1 + "] 이상은 경험치가 오르지 않습니다."));
				return;
			}
			
			int rank_level = pc.getRankLevel();
			double rank_rate = 1;
			if(rank_level >= 1 && rank_level <= 5){
				if(pc.getAccount().getBlessOfAin() > 2000000){
					rank_rate += 0.10;
				}
			}
			
			int add_exp = (int) (exp * exppenalty * Config.RATE_XP * levelupBonus * beginnerBonus * foodBonus * clanBonus
					* expposion * comab * comboBonus * einhasadBonus * emeraldBonus * clanOnlineBonus * BloodBonus
					* dollBonus * addexp1 * addexp2 * addexp3 * addexp4 * addexp5 * Arka * jinDEATHstr * dragon_hunter_bless * dragon_SET_bless
					* HUNTER_BLESS * HUNTER_BLESS3 * HUNTER_BLESS4 * special_map * party_map_rate * exp_posion * rank_rate);
			
			if(GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE){
				if(pc.getAccount() != null && pc.getAccount().getGrangKinAngerStat() != 0){
					add_exp = (int) pc.getAccount().getGrangKinAngerExpCalc();
				}
			}
			
			// 폭렙방지
			if (pc.getLevel() >= 1) {
				if ((add_exp + pc.getExp()) > ExpTable.getExpByLevel((pc.getLevel() + 1))) {
					add_exp = (ExpTable.getExpByLevel((pc.getLevel() + 1)) - pc.getExp());
				}
			}

			if (pc.getLevel() >= Config.LIMITLEVEL && !pc.isGm()) {// 경험치
				add_exp = 0;
				pc.sendPackets(new S_SystemMessage("레벨제한으로 더이상 경험치 획득이 불가능합니다"));
			}

			// 레벨제한
			if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {
				// 다음레벨에 필요한 경험치
				int maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
				if (pc.getExp() + add_exp >= maxexp) {
					// return;
					add_exp = 0;
				}
			}

			if (add_exp < 0) {
				return;
			}
			//TODO 오토방지코드 해당 맵에선 무시
			if (pc.getAI() == null && !pc.getSafetyZone() && !(pc.getMapId() == 1936 || pc.getMapId() == 107 || pc.getMapId() == 2101 || pc.getMapId() == 2151
					|| pc.getMapId() == 612 || pc.getMapId() == 254 || pc.getMapId() == 1930)) {
				MJCaptcha captcha = pc.get_captcha();
				if (captcha == null)
					captcha = pc.create_captcha();

				if (captcha.is_keep_captcha()) {
					if (captcha.inc_relay_count() >= MJCaptchaLoadManager.CAPTCHA_RELAY_COUNT)
						captcha.do_fail(pc);
				} else {
					if (MJCaptchaLoadManager.CAPTCHA_IS_RUNNING) {
						if (!captcha.is_pass_captcha() && MJRnd.isWinning(1000000, MJCaptchaLoadManager.CAPTCHA_SHOW_PROBABILITY_BYMILLIMON))
							captcha.drain_captcha(pc);
					}
				}
			}

			if(pc.get_companion() != null){
				pc.get_companion().update_exp((int)(add_exp * MJCompanionSettings.EXP_BY_MASTER_EXP));
			}
			pc.addExp(add_exp);
			pc.addMonsterKill(1);
		}catch(Exception e){
			if(!(e instanceof NullPointerException))
				e.printStackTrace();
		}
	}


	private static void AddExpPet(L1PetInstance pet, int exp) {
		L1PcInstance pc = (L1PcInstance) pet.getMaster();

		// int petNpcId = pet.getNpcTemplate().get_npcId();
		int petItemObjId = pet.getItemObjId();

		int levelBefore = pet.getLevel();
		int totalExp = (int) (exp * 50 + pet.getExp());
		if (totalExp >= ExpTable.getExpByLevel(51)) {
			totalExp = ExpTable.getExpByLevel(51) - 1;
		}
		pet.setExp(totalExp);

		pet.setLevel(ExpTable.getLevelByExp(totalExp));

		int expPercentage = ExpTable.getExpPercentage(pet.getLevel(), totalExp);

		int gap = pet.getLevel() - levelBefore;
		for (int i = 1; i <= gap; i++) {
			IntRange hpUpRange = pet.getPetType().getHpUpRange();
			IntRange mpUpRange = pet.getPetType().getMpUpRange();
			pet.addMaxHp(hpUpRange.randomValue());
			pet.addMaxMp(mpUpRange.randomValue());
		}

		pet.setExpPercent(expPercentage);
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
		pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
		
		//pc.sendPackets(new S_PetPack(pet, pc));

		if (gap != 0) { // 레벨업하면(자) DB에 기입한다
			pc.sendPackets(new S_SkillSound(pet.getId(), 6353));// /이건 자기한데 보이게
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pet.getId(), 6353));
			L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
			if (petTemplate == null) { // PetTable에 없다
				_log.warning("L1Pet == null");
				System.out.println("L1Pet == null");
				return;
			}
			petTemplate.set_exp(pet.getExp());
			petTemplate.set_level(pet.getLevel());
			petTemplate.set_hp(pet.getMaxHp());
			petTemplate.set_mp(pet.getMaxMp());
			PetTable.getInstance().storePet(petTemplate); // DB에 기입해
			pc.sendPackets(new S_ServerMessage(320, pet.getName())); // \f1%0의
		}
	}

	/**
	 * 클라우디아 경험치 지급용
	 * 
	 * @param pc
	 * @param exp
	 */
	public static void AddExp(L1PcInstance pc, int exp) {
		/** 서버 오픈 대기 */
		if (Config.STANDBY_SERVER) {
			return;
		}

		if (pc.getLevel() > GameServerSetting.getInstance().get_maxLevel()) {
			return;
		}
		if (pc.isDead())
			return;

		// double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		// int add_exp = (int) (exp * Config.RATE_XP * exppenalty );
		int add_exp = (int) (exp); // 배율로 올라가는 경험치 제외

		if (add_exp < 0) {
			return;
		}

		if (ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) <= pc.getExp() + add_exp) {
			pc.setExp(ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) - 1);
		} else {
			pc.addExp(add_exp);
		}
		pc.onChangeExp();// 강제로 경험치갱신.
	}
	
	private static int getPartyIsinScreen(L1PcInstance pc){
		int count = 0;
		
		for(L1PcInstance member : pc.getParty().getMembers()){
			if(pc.getLocation().isInScreen(member.getLocation())){
				count++;
			}
		}
		
		return count;
	}
		
}
