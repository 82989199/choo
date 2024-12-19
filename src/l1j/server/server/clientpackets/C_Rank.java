package l1j.server.server.clientpackets;

import java.util.Collection;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.FaceToFace;

public class C_Rank extends ClientBasePacket {

	private static final String C_RANK = "[C] C_Rank";

	private L1ItemInstance weapon;

	public C_Rank(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		int type = readC();
		int rank = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		switch (type) {
		case 1:// 계급
			String name = readS();
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if ((!pc.isCrown()) && (pc.getClanRank() != L1Clan.수호) && (pc.getClanRank() != L1Clan.부군주)) {
				pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 계급 부여 권한이 없음."));
				return;
			}
			if (targetPc != null) {
				if (pc.getClanid() == targetPc.getClanid()) {
					try {
						if ((pc.getClanRank() != L1Clan.군주) && (pc.getClanRank() != L1Clan.수호) && (pc.getClanRank() != L1Clan.부군주)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 계급 부여 권한이 없음."));
							return;
						}
						if ((targetPc.isCrown()) && (targetPc.getId() == targetPc.getClan().getLeaderId())) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 대상이 혈맹의 군주"));
							return;
						}
						if ((pc.getClanRank() == L1Clan.부군주) && (rank == 3)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 부여 계급이 자신보다 높거나 같은 계급"));
							return;
						}
						if ((pc.getClanRank() == L1Clan.수호) && (rank == 9)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 부여 계급이 자신보다 높거나 같은 계급"));

							return;
						}
						if ((pc.getClanRank() == L1Clan.수호)
								&& ((targetPc.getClanRank() == L1Clan.군주) || (targetPc.getClanRank() == L1Clan.수호) || (targetPc.getClanRank() == L1Clan.부군주))) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 대상이 현재 자신보다 높거나 같은 계급"));
							return;
						}
						targetPc.setClanRank(rank);
						targetPc.save(); // DB에 캐릭터 정보를 기입한다
						// targetPc.sendPackets(new
						// S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank,
						// name));//PC에게 한번더 보낸다? 왜?중복일텐데
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name));// 군주에게
																									// 계급
																									// 알림
						clan.UpdataClanMember(targetPc.getName(), targetPc.getClanRank());
						String rankString = "일반";
						if (rank == 7)
							rankString = "수련";
						else if (rank == 3)
							rankString = "부군주";
						else if (rank == 8)
							rankString = "일반";
						else if (rank == 9)
							rankString = "수호기사";
						else if (rank == 13)
							rankString = "정예";
						targetPc.sendPackets(new S_SystemMessage("계급: " + rankString + "(으)로 계급 임명함"));
						targetPc.sendPackets(new S_ACTION_UI2(targetPc, S_ACTION_UI2.CLAN_RANK));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pc.sendPackets(new S_SystemMessage("같은 혈맹원이 아닙니다."));
					return;
				}
			} else {
				L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(name);
				if ((restorePc != null) && (restorePc.getClanid() == pc.getClanid())) {
					try {
						if ((restorePc.isCrown()) && (restorePc.getId() == restorePc.getClan().getLeaderId())) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 대상이 혈맹의 군주."));
							return;
						}
						if ((pc.getClanRank() != L1Clan.군주) && (pc.getClanRank() != L1Clan.수호) && (pc.getClanRank() != L1Clan.부군주)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 계급 부여 권한이 없음."));
							return;
						}
						if ((pc.getClanRank() == L1Clan.부군주) && (rank == 3)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 부여 계급이 자신보다 높거나 같은 계급"));
							return;
						}
						if ((pc.getClanRank() == L1Clan.수호) && (rank == 9)) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 부여 계급이 자신보다 높거나 같은 계급"));
							return;
						}
						if ((pc.getClanRank() == L1Clan.수호) && ((restorePc.getClanRank() == L1Clan.군주) || (restorePc.getClanRank() == L1Clan.수호)
								|| (restorePc.getClanRank() == L1Clan.부군주))) {
							pc.sendPackets(new S_SystemMessage("계급 부여 실패 : 대상이 현재 자신보다 높거나 같은 계급"));
							return;
						}
						restorePc.setClanRank(rank);
						restorePc.save(); // DB에 캐릭터 정보를 기입한다
						restorePc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name));
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name));
						clan.UpdataClanMember(restorePc.getName(), restorePc.getClanRank());
						String rankString = "일반";
						if (rank == 7)
							rankString = "수련";
						else if (rank == 3)
							rankString = "부군주";
						else if (rank == 8)
							rankString = "일반";
						else if (rank == 9)
							rankString = "수호기사";
						else if (rank == 13)
							rankString = "정예";
						for (L1PcInstance mem : clan.getOnlineClanMember()) {
							mem.sendPackets(new S_SystemMessage(restorePc.getName() + " 님의 계급이 " + rankString + "(으)로 변경되었습니다."));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pc.sendPackets(new S_SystemMessage("그러한 케릭터는 없습니다."));
					return;
				}
				restorePc = null;
			}
			break;
		case 2:// 목록
			try {
				if (clan.getAlliance() != 0) {
					S_PacketBox pb2 = new S_PacketBox(pc, S_PacketBox.ALLIANCE_LIST);
					pc.sendPackets(pb2, true);
				} else {
					return;
				}
			} catch (Exception e) {
			} // 오류X
			break;
		case 3:// 동맹가입
			L1PcInstance allianceLeader = FaceToFace.faceToFace(pc);
			if (allianceLeader == null)
				return;
			if (pc.getLevel() < 25 || !pc.isCrown()) {
				pc.sendPackets(new S_ServerMessage(1206));// 25레벨이상 혈맹 군주만 동맹신청을
															// 할 수 있습니다. 또한 연합
															// 군주는 동맹을 맺을 수
															// 없습니다.
				return;
			}
			if (pc.getClan().getAlliance() != 0) {
				pc.sendPackets(new S_ServerMessage(1202));// 이미 동맹에 가입된 상태입니다.
				return;
			}
			if (clan.getAlliance() > 4) {
				S_SystemMessage sm = new S_SystemMessage("동맹은 4개 혈맹 까지만 가능합니다.");
				pc.sendPackets(sm, true);
				return;
			}

			if (clan.getCurrentWar() != null) {
				pc.sendPackets(new S_ServerMessage(1234)); // 전쟁중에는 동맹에 가입할 수
															// 없습니다.
				return;
			}

			// 동맹 수 제한(4개혈맹) 추가해야함 // 1201 // 동맹에 가입할 수 없습니다.
			if (allianceLeader != null) {
				if (allianceLeader.getLevel() > 24 && allianceLeader.isCrown()) {
					allianceLeader.setTempID(pc.getId());
					allianceLeader.sendPackets(new S_Message_YN(223, pc.getName()));
				} else {
					pc.sendPackets(new S_ServerMessage(1201));// 동맹에 가입할 수 없습니다.
				}
			}
			break;
		case 4:// 탈퇴
			if (clan.getCurrentWar() != null) {
				pc.sendPackets(new S_ServerMessage(1203)); // 전쟁중에는 동맹을 탈퇴할 수
															// 없습니다.
				return;
			}

			if (clan.getAlliance() != 0) {
				pc.sendPackets(new S_Message_YN(1210, "")); // 정말로 동맹을 탈퇴하시겠습니까?
															// (Y/N)
			} else {
				pc.sendPackets(new S_ServerMessage(1233)); // 동맹이 없습니다.
			}
			break;

		case 5: // 생존의 외침 (CTRL + E)
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(1973));
				// 무기를 착용해야 사용할수 있습니다.
				return;
			}
			if (pc.get_food() >= 225) {
				int addHp = 0;
				int gfxId1 = 8683;
				int gfxId2 = 829;
				long curTime = System.currentTimeMillis() / 1000;
				int fullTime = (int) ((curTime - pc.getCryOfSurvivalTime()) / 60);
				if (fullTime < 30) {
					if (!(pc.getLevel() >= 90 && fullTime >= 10)) {
						long time = (pc.getCryOfSurvivalTime() + (1 * 60 * 30)) - curTime;
						// pc.sendPackets(new S_ServerMessage(1974));
						// 생존의 외침: 대기중
						pc.sendPackets(new S_SystemMessage("생존의 외침: " + (time / 60) + "분 " + (time % 60) + "초 후 사용가능."));
						return;
					}
				}
				int enchant = pc.getWeapon().getEnchantLevel();
				if (pc.getWeapon().getItem().get_safeenchant() == 0) {
					if (enchant >= 0 && enchant <= 9) {
						gfxId1 = 8773;
						gfxId2 = 8910;
						addHp = 1400;
					} else if (enchant >= 10) {
						gfxId1 = 8686;
						gfxId2 = 8908;
						addHp = 1600;
					}
				} else {
					if (enchant >= 0 && enchant <= 6) {
						gfxId1 = 8684;
						gfxId2 = 8907;
						addHp = 500;
					} else if (enchant == 7 || enchant == 8) {
						if (enchant == 7)
							addHp = 800;
						else
							addHp = 1000;
						gfxId1 = 8685;
						gfxId2 = 8909;
					} else if (enchant == 9 || enchant == 10) {
						if (enchant == 9)
							addHp = 1200;
						else
							addHp = 1400;
						gfxId1 = 8773;
						gfxId2 = 8910;
					} else if (enchant >= 11) {
						gfxId1 = 8686;
						gfxId2 = 8908;
						addHp = 1600;
					}
				}
				S_SkillSound sound = new S_SkillSound(pc.getId(), gfxId1);
				pc.sendPackets(sound);
				Broadcaster.broadcastPacket(pc, sound);
				sound = new S_SkillSound(pc.getId(), gfxId2);
				pc.sendPackets(sound);
				Broadcaster.broadcastPacket(pc, sound);
				pc.setCryOfSurvivalTime();
				pc.set_food(0);
				pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 0));
				pc.setCurrentHp(pc.getCurrentHp() + addHp);
			} else {
				pc.sendPackets(new S_ServerMessage(3461));
				// 포만감이 부족하여 사용할 수 없습니다.
			}
			break;
		case 6: // 무기 허세 떨기 Alt + 0(숫자)
			if (pc.getWeapon() == null) {
				pc.sendPackets(new S_ServerMessage(1973));
				return;
			}
			int gfx3 = 0;
			weapon = pc.getWeapon();
			int EnchantLevel2 = weapon.getEnchantLevel();
			if (EnchantLevel2 < 0) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			} else if (EnchantLevel2 >= 0 && EnchantLevel2 <= 6) {
				gfx3 = 8684;
			} else if (EnchantLevel2 >= 7 && EnchantLevel2 <= 8) {
				gfx3 = 8685;
			} else if (EnchantLevel2 >= 9 && EnchantLevel2 <= 10) {
				gfx3 = 8773;
			} else if (EnchantLevel2 >= 11) {
				gfx3 = 8686;
			}
			pc.sendPackets(new S_SkillSound(pc.getId(), gfx3));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), gfx3));
			break;
		case 8:
			send_dungeon_remains(pc, pc.get_account_progresses());
			send_dungeon_remains(pc, pc.get_character_progresses());
			break;
		/* case 9: *//** 리스창표기 **//*
									 * int setTimer1 = (pc.getGirandungeonTime()
									 * / 60);// 기감 int setTimer2 =
									 * (pc.getAccountRemains(AccountRemainType.
									 * 발록진영).remain / 60);// 발록진영 int setTimer3
									 * =
									 * (pc.getAccountRemains(AccountRemainType.
									 * 상아탑).remain / 60);// 야히진영 int setTimer4 =
									 * (pc.getSoulTime() / 60);// 고대정령무덤
									 * pc.sendPackets(new
									 * S_PacketBox(S_PacketBox.DungeonTime,
									 * setTimer1, setTimer2, setTimer3,
									 * setTimer4)); break;
									 */
		default:
			break;
		}
	}

	private void send_dungeon_remains(L1PcInstance pc, Collection<DungeonTimeProgress<?>> progresses) {
		DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
		for (DungeonTimeProgress<?> progress : progresses) {
			DungeonTimeInformation dtInfo = loader.from_timer_id(progress.get_timer_id());
			pc.sendPackets(new S_ServerMessage(2535, dtInfo.get_description(), String.valueOf((progress.get_remain_seconds() / 60))));
		}
	}

	@Override
	public String getType() {
		return C_RANK;
	}
}
