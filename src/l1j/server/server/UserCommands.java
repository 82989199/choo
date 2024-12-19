package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.BUYER_COOLTIME;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.AutoSystemController;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJKDASystem.Chart.MJKDAChartScheduler;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.Payment.MJPaymentGmHandler;
import l1j.server.Payment.MJPaymentUserHandler;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.AuctionSystemTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_Light;
import l1j.server.server.serverpackets.S_MARK_SEE;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RetrieveAutoPotion;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowAutoInformation;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJFullStater;
import l1j.server.server.utils.SQLUtil;

public class UserCommands {

	boolean spawnTF = false;

	private static UserCommands _instance;

	private UserCommands() {
	}

	public static UserCommands getInstance() {
		if (_instance == null) {
			_instance = new UserCommands();
		}
		return _instance;
	}

	public void handleCommands(L1PcInstance pc, String cmdLine) {
		if (pc == null) {
			return;
		}
		// System.out.println(cmdLine);
		StringTokenizer token = new StringTokenizer(cmdLine);
		// System.out.println(token.hasMoreTokens());
		String cmd = "";
		if (token.hasMoreTokens())
			cmd = token.nextToken();
		else
			cmd = cmdLine;
		String param = "";
		// System.out.println(cmd);

		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();
		
		try {
			switch (cmd) {
//			case "시세":										AdSc(pc);											break;
			case "텔렉풀기":	case ".":						tell(pc);											break;
			case "도움말":										showHelp(pc);										break;
//			case "그랑카인":									testcheck(pc);										break;
			case "나이":										age(pc, param);										break;
			case "혈맹파티":									BloodParty(pc);										break;
			case "수배":										hunt(pc, param);									break;
			case "파티멘트":									Ment(pc, param);									break;
			case "무인상점":									privateShop(pc);									break;
//			case "할파스":										toHalpasTime(pc);									break;
			case "결투":										do_lfc(pc, param);									break;
			case "라이트": case "맵핵":							maphack(pc, param);									break;
			case "판매":										countR1(pc, param);									break;
			case "구매":										countR2(pc, param);									break;
			case "판매취소":									countR3(pc, param);									break;
			case "판매완료":									countR4(pc, param);									break;
			case "구매취소":									countR5(pc, param);									break;
			case "마크":										clanMark(pc, param);								break;
			case "캐릭명변경":	case "케릭명변경":	case "이름변경":	changename(pc, param);								break;
			case "외창":										outsideChat(pc, param);								break;
			case "암호변경":	case "비번변경":					changepassword(pc, param);							break;
			
			
			case "자동사냥":									UserCommands.getInstance().autoHunt(pc, param);		break;
	        
	        
			case "자동물약자동버프": 								help(pc); 											break;
//			case "복구":										복구(pc);												break;
//			case "고정":		case "고정신청":					phone(pc, param);									break;
//			case "원스텟":										fullstat(pc, param);								break;
//			case "차트":										setChart(pc, param);								break;
//			case "엔코인선물":									giftNCoin(pc, param);								break;
//			case "봉인해제신청":									Sealedoff(pc, param);								break;
//			case "보스알림":									spawnNotifyOnOff(pc, param);						break;
//			case "구슬":										MJCTLoadManager.commands(pc, param);				break;
//			case "타겟팅":										doTarget(new MJCommandArgs().setOwner(pc).setParam(param));		break;
//			case "충전":										MJPaymentUserHandler.do_execute(new MJCommandArgs().setOwner(pc).setParam(param));		break;
			case "마일리지":
			case "엔코인":
				if (pc != null) {
					pc.sendPackets("\\fH계정(\\aH" + pc.getAccountName() + "\\fH) | N코인:(\\aG" + pc.getNetConnection().getAccount().Ncoin_point + "\\fH)원");
					}
					break;
			
			default:
				pc.sendPackets("유저 명령어를 다시 입력하세요.");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void help(L1PcInstance pc) {
		showAutoPotionList(pc);
	}
	public void execute(L1PcInstance pc, String cmdName) {
		showAutoPotionList(pc);
	}
	
	private void toHalpasTime(L1PcInstance pc) {
		Timestamp time = pc.get_skill().getHalpasTime();
		if (time == null) {
			pc.sendPackets(new S_SystemMessage("할파스의 신의 발동 상태가 아닙니다."));
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());

		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		pc.sendPackets(new S_SystemMessage(String.format("할파스의 신의: [%s일 %s시 %s분]에 발동.", date, hour, minute)));
	}
	
	public static void setChart(L1PcInstance pc, String param) {
		boolean isOn;
		if (!MJKDAChartScheduler.isLoaded()) {
			pc.sendPackets(new S_SystemMessage("사용중이지 않습니다."));
			return;
		}

		if (param.equalsIgnoreCase("켬")) {
			isOn = true;
			pc.sendPackets(new S_SystemMessage("킬 랭킹 차트를 보입니다. 잠시 후 화면에 나타납니다."));
			MJKDAChartScheduler.getInstance().onLoginUser(pc);
		} else if (param.equalsIgnoreCase("끔")) {
			isOn = false;
			pc.sendPackets(new S_SystemMessage("킬 랭킹 차트를 숨깁니다. 리스 후 적용됩니다."));
		} else {
			pc.sendPackets(new S_SystemMessage(".차트 [켬/끔] (왼쪽상단 PK차트 리스트)"));
			return;
		}
		if (pc.getKDA() != null)
			pc.getKDA().isChartView = isOn;
	}
	
	private void testcheck(L1PcInstance pc){
		int grangKinOneStep = GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME;
		int grangKinTwoStep = GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME;
		int grangKinThreeStep = GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME;
		int grangKinFourStep = GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME;
		int grangKinFiveStep = GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME;
		int grangKinSixStep = GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME;
		
		int real_time = 0;
		int minute = 0;
		int hour = 0;
		if (pc.getAccount().getGrangKinAngerStat() == 1) {
			real_time = grangKinOneStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 2) {
			real_time = grangKinTwoStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 3) {
			real_time = grangKinThreeStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 4) {
			real_time = grangKinFourStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 5) {
			real_time = grangKinFiveStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 6) {
			real_time = grangKinSixStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		}

		if(real_time != 0){
			pc.sendPackets("그랑카인의 분노 해제시 까지 마을대기 " + hour + "시간 " + minute + "분 남았습니다.");
		} else {
			pc.sendPackets("그랑카인의 분노 상태가 아닙니다.");
		}
	}
	
	private void clanMark(L1PcInstance pc, String param) {
		// TODO 자동 생성된 메소드 스텁
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			if (onoff.equalsIgnoreCase("켬")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, true), true);
				pc.sendPackets(new S_MARK_SEE(pc, 0, true), true);
				pc.sendPackets("혈마크 표기를 시작 합니다.");
			} else if (onoff.equalsIgnoreCase("끔")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, false), true);
				pc.sendPackets(new S_MARK_SEE(pc, 1, false), true);
				pc.sendPackets("혈마크 표기를 종료 합니다.");
			} else {
				pc.sendPackets(".마크 [켬/끔]");
				return;
			}
		} catch (Exception e) {
			pc.sendPackets(".마크 [켬/끔]");
		}
	}
	
	// 사용하지 않음
//	private void giftNCoin(L1PcInstance pc, String param) {
//		try {
//			StringTokenizer tok = new StringTokenizer(param);
//			String targetName = tok.nextToken();
//			int count = Integer.parseInt(tok.nextToken());
//			L1PcInstance target = L1World.getInstance().getPlayer(targetName);
//			if(target == null) {
//				pc.sendPackets(targetName + " 님은 현재 접속중이지 않습니다.");
//				return;
//			}
//			if(target.getNetConnection() == null) {
//				pc.sendPackets(target + " 님은 올바른 접속상태가 아닙니다.");
//				return;
//			}
//			if(pc == target) {
//				pc.sendPackets("엔코인은 본인에게 양도할 수 없습니다.");
//				return;
//			}
//			if(count > pc.getNcoin()) {
//				pc.sendPackets("본인의 엔코인이 " + count + "(만)원 보다 적어 불가능합니다.");
//				return;
//			}			
//			pc.addNcoin1(count);
//			target.addNcoin(count);
//			pc.sendPackets("본인의 엔코인 ["+count+"](만)원을 ["+targetName+"]님께 선물 하였습니다.");
//			target.sendPackets("엔코인 ["+count+"](만)원을 ["+targetName+"]님께 선물 받으셨습니다.");
//		}catch(Exception e) {
//			pc.sendPackets(".엔코인선물 [케릭터명] [갯수]");
//		}
//	}
	
	private void showHelp(L1PcInstance pc) {
			pc.sendPackets(new S_SystemMessage("################## 유저 명령어 #################"));
			pc.sendPackets(new S_SystemMessage(".나이           .수배           .마크           .외창"));
			pc.sendPackets(new S_SystemMessage(".혈맹파티     .파티멘트     .암호변경"));
			pc.sendPackets(new S_SystemMessage(".무인상점     .라이트"));
			pc.sendPackets(new S_SystemMessage("\\aG.판매       .판매취소     .판매완료"));
			pc.sendPackets(new S_SystemMessage("\\aG.구매       .구매취소"));
//			pc.sendPackets(new S_SystemMessage(".그랑카인     .할파스        .무인상점     .화면밝기"));
	}
	
	void AdSc(L1PcInstance pc) {
		pc.sendPackets("----------------------------------------------------");
//		pc.sendPackets("현재 시세는 ("+Config.Ad_Sc+") 아데나 당 10,000원입니다.");
		pc.sendPackets("오늘 시세는 코인 1만 당 "+Config.Ad_Sc+"원입니다.");
		pc.sendPackets("\\aG시세 이하로 거래 시 즉시 압류 조치됩니다.");
		pc.sendPackets("----------------------------------------------------");
	}
	
	private void Ment(L1PcInstance pc, String param) {
		if (param.equalsIgnoreCase("끔")) {
			pc.sendPackets( "멘트: 끔");
			pc.RootMent = false;
		} else if (param.equalsIgnoreCase("켬")) {
			pc.sendPackets( "멘트: 켬");
			pc.RootMent = true;
		} else {
			pc.sendPackets( ".파티멘트 [켬/끔]");
		}
	}

	/*private void Sealedoff(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String param1 = tok.nextToken();
			int off = Integer.parseInt(param1);
			if (off > 10 || off < 0) {
				pc.sendPackets("해제 주문서는 [10]이상 신청불가능합니다.");
				return;
			}
			if (off == 0) {
				pc.setSealScrollCount(0);
				pc.setSealScrollTime(0);
				pc.sendPackets("해제주문서 신청이 초기화되었습니다.");
			} else {
				int sealScrollTime = (int) (System.currentTimeMillis() / 1000) + 1 * 24 * 3600;
				pc.setSealScrollTime(sealScrollTime);
				pc.setSealScrollCount(off);
				pc.sendPackets("해제 주문서 [" + off + "]장이 신청되었습니다.");
				pc.sendPackets("오늘날짜로부터 [1]일 뒤에 자동지급 됩니다.");
				pc.sendPackets("재신청할경우 [1]일 초기화 됩니다.");
			}
			pc.save();
		} catch (Exception e) {
			pc.sendPackets(".봉인해제신청 [신청할 장수]");
		}
	}*/

	public void autoHunt(L1PcInstance pc, String param) {
	    try {
	        StringTokenizer st = new StringTokenizer(param);
	        int type = Integer.parseInt(st.nextToken());
	        int mapId = Integer.parseInt(st.nextToken());

	        // 위치 및 조건 확인
	        if (!((pc.getX() >= 33418 && pc.getX() <= 33442) && (pc.getY() >= 32801 && pc.getY() <= 32825) && pc.getMapId() == 4)) {
	            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "기란 마을 중앙 근처에서만 시작할 수 있습니다."));
	            pc.setAutoSetting(false); // 자동 사냥 해제
	            return;
	        }
	        // 무기 착용 여부 확인
	        if (pc.getWeapon() == null) {
	            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "무기를 착용해야 자동 사냥을 시작할 수 있습니다."));
	            pc.setAutoSetting(false); // 자동 사냥 해제
	            return;
	        }
	        // 기타 조건 확인
	        if (!pc.getInventory().checkItem(3000209) && (!pc.getInventory().checkItem(3000213) && (!pc.getInventory().checkItem(3000214)))) {
	            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "자동 사냥 사용권이 있어야 사용 가능합니다."));
	            pc.setAutoSetting(false); // 자동 사냥 해제
	            return;
	        }
//	        if (!pc.getInventory().checkItem(40308, 10000000)) {
//	            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "최소 1000만 아데나가 있어야만 사용 가능합니다."));
//	            pc.setAutoSetting(false); // 자동 사냥 해제
//	            return;
//	        }
	        if (!AutoSystemController.check_auto_dungeon(pc)) {
	            pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "던전 이용 시간이 종료되었습니다."));
	            pc.setAutoSetting(false); // 자동 사냥 해제
	            return;
	        }

	        // 자동 변신 설정
	        pc.setAutoPolyID(0);
	        pc.sendPackets(new S_Ability(2, true));
	        pc.sendPackets(new S_Message_YN(180));
	        pc.sendPackets(new S_Ability(2, false));
	        pc.sendPackets(new S_ChatPacket(pc, "자동 사냥을 위해 변신을 지정하세요.", 1));

	        // 자동 사냥 시작 설정
	        AutoSystemController auto = AutoSystemController.getInstance();
	        pc.setAutoPotion(Config.자동사냥물약);
	        pc.setAutoMapId(Config.자동사냥맵번호);

	        if (pc.is_자동물약사용()) {
	            pc.set_자동물약사용(false);
	            pc.set_자동버프사용(false);
	            pc.set_자동숫돌사용(false);
	            pc.sendPackets("\\f3자동물약/버프 사용은 해제되었습니다.");
	        }

	        pc.setAutoSetting(true);

	        // 10초 카운트다운
	        Timer timer = new Timer();
	        TimerTask task = new TimerTask() {
	            int countdown = 10;

	            public void run() {
	                if (countdown > 0) {
	                    pc.sendPackets(new S_ChatPacket(pc, countdown + "초 후 자동 사냥이 시작됩니다.", 1));
	                    countdown--; // 카운트다운 감소
	                } else {
	                    // 무기 착용 여부를 다시 확인하여 자동 사냥 시작 여부 결정
	                    if (pc.getWeapon() == null) {
	                        pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "무기를 착용하지 않아서 자동 사냥이 종료됩니다."));
	                        pc.setAutoSetting(false);
	                        pc.setAutoHunt(false); // 자동 사냥 상태 해제
	                    } else {
	                        AutoHunt thread = new AutoHunt(pc, auto); // 자동 사냥 스레드 시작
	                        thread.begin(); // 스레드 시작
	                    }
	                    timer.cancel(); // 타이머 중지
	                }
	            }
	        };

	        timer.scheduleAtFixedRate(task, 0, 1000); // 1초마다 실행
	    } catch (Exception e) {
	        pc.sendPackets(new S_ChatPacket(pc, "자동 사냥 설정 중 오류 발생.", 1)); // 오류 메시지 전송
	    }
	}

	class AutoHunt extends Thread {
	    L1PcInstance _pc;
	    AutoSystemController _auto;

	    public AutoHunt(L1PcInstance pc, AutoSystemController auto) {
	        _pc = pc;
	        _auto = auto;
	    }

	    public void begin() {
	        GeneralThreadPool.getInstance().schedule(this, 0L); // 즉시 시작
	    }

	    @Override
	    public void run() {
	        try {
	            _pc.setAutoStatus(_auto.AUTO_STATUS_MOVE_SHOP);
	            _pc.sendPackets(new S_ChatPacket(_pc, "자동 사냥을 시작합니다.", 1));
	            _pc.setAutoSetting(false);
	            AutoSystemController.getInstance().addAuto(_pc);
	        } catch (Exception e) {
	            // 예외 처리
	        }
	    }
	}

	
	public static void doTarget(MJCommandArgs args) {
		try {
			String cmd = args.nextString();
			if (cmd.equalsIgnoreCase("켬")) {
				args.getOwner().setOnTargetEffect(true);
				args.notify("타겟팅 시스템이 활성화되었습니다.");
			} else if (cmd.equalsIgnoreCase("끔")) {
				args.getOwner().setOnTargetEffect(false);
				args.notify("타겟팅 시스템이 비활성화되었습니다.");
			} else if (cmd.equalsIgnoreCase("상태")) {
				args.notify(String.format("현재 타겟팅 시스템 상태 : %s", args.getOwner().isOnTargetEffect()));
			} else
				throw new Exception();
		} catch (Exception e) {
			args.notify(".타겟팅 [켬|끔|상태]");
		}
	}
	
	private void changename(L1PcInstance pc, String name) {
		try {
			if (pc.getLevel() >= 80) {
				int numOfNameBytes = 0;
				numOfNameBytes = name.getBytes("MS949").length;
				if (numOfNameBytes == 0) {
					pc.sendPackets(".이름변경 [변경할 이름]");
					return;
				}
				if (pc.getClanid() != 0) {
					pc.sendPackets("혈맹을 잠시 탈퇴한 후 변경할 수 있습니다.");
					return;
				}
				if (pc.isCrown()) {
					pc.sendPackets("군주는 운영자와 상담 후 변경할 수 있습니다.");
					return;
				}
				if (pc.hasSkillEffect(1005) || pc.hasSkillEffect(2005)) {
					pc.sendPackets("채금 상태에는 변경할 수 없습니다.");
					return;
				}
				if (numOfNameBytes < 2 || numOfNameBytes > 12) {
					pc.sendPackets("한글 1~6자 사이로 입력 하시길 바랍니다.");
					return;
				}
				
				if (CharacterTable.getInstance().isContainNameList(name) || MJBotNameLoader.isAlreadyName(name)) {
					pc.sendPackets("동일한 케릭명이 존재 합니다.");
					return;
				}
				
				if (BadNamesList.getInstance().isBadName(name)) {
					pc.sendPackets("생성 금지된 케릭명 입니다.");
					return;
				}
				for (int i = 0; i < name.length(); i++) {
					if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ'
							|| // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ'
							|| name.charAt(i) == 'ㅂ' || // 한문자(char)단위로 비교
							name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ'
							|| name.charAt(i) == 'ㅇ' || // 한문자(char)단위로 비교
							name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ'
							|| name.charAt(i) == 'ㅋ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ'
							|| name.charAt(i) == 'ㅛ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ'
							|| name.charAt(i) == 'ㅔ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ'
							|| name.charAt(i) == 'ㅣ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ'
							|| name.charAt(i) == 'ㅒ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ'
							|| name.charAt(i) == 'ㅝ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ'
							|| name.charAt(i) == 'ㅘ' || // 한문자(char)단위로 비교.
							name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆'
							|| name.charAt(i) == 'ㅤ') {
						pc.sendPackets("케릭명이 올바르지 않습니다.");
						return;
					}
				}
				for (int i = 0; i < name.length(); i++) {
					if (!Character.isLetterOrDigit(name.charAt(i))) {
						pc.sendPackets("케릭명이 올바르지 않습니다.");
						return;
					}
				}
				
				if (!isAlphaNumeric(name)) {// 특수문자
					pc.sendPackets("특수문자는 금지되어 있습니다.");
					return;
				}
				if (pc.getInventory().checkItem(408991, 1)) { // 인벤 아이템 체크
					Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setString(1, name);
							pstm.setString(2, pc.getName());
						}
					});
					pc.save(); // 저장

					 /****** LogDB 라는 폴더를 미리 생성 해두세요 *******/
					Calendar rightNow = Calendar.getInstance();
					int year = rightNow.get(Calendar.YEAR);
					int month = rightNow.get(Calendar.MONTH) + 1;
					int date = rightNow.get(Calendar.DATE);
					int hour = rightNow.get(Calendar.HOUR);
					int min = rightNow.get(Calendar.MINUTE);
					String stryyyy = "";
					String strmmmm = "";
					String strDate = "";
					String strhour = "";
					String strmin = "";
					stryyyy = Integer.toString(year);
					strmmmm = Integer.toString(month);
					strDate = Integer.toString(date);
					strhour = Integer.toString(hour);
					strmin = Integer.toString(min);
					String str = "";
					str = new String("[" + stryyyy + "-" + strmmmm + "-" + strDate + " " + strhour + ":" + strmin + "] "
							+ pc.getName() + " ---> " + name);
					StringBuffer FileName = new StringBuffer("LogDB/캐릭명변경.txt");
					PrintWriter out = null;
					try {
						out = new PrintWriter(new FileWriter(FileName.toString(), true));
						out.println(str);
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					str = "";// 초기화
					pc.getInventory().consumeItem(408991, 1); // 주문서 삭제
					buddys(pc); // 친구 삭제
					편지삭제(pc); // 편지삭제
					
					GeneralThreadPool.getInstance().schedule(new Runnable(){
						@Override
						public void run(){
							GameClient clnt = pc.getNetConnection();
							C_NewCharSelect.restartProcess(pc);
							Account acc		= clnt.getAccount();
							clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
							if(acc.countCharacters() > 0)
								C_CommonClick.sendCharPacks(clnt);							
						}
					}, 500L);
					
					Thread.sleep(500);
					pc.sendPackets(new S_Disconnect());
					pc.logout();
				} else {
					pc.sendPackets("캐릭명 변경권이 없습니다.");
				}
			} else {
				pc.sendPackets("레벨 80 이하는 불가능 합니다.");
			}
		} catch (Exception e) {
			pc.sendPackets(".이름변경 [변경할 이름]으로 입력하시길 바랍니다.");
		}
	}

	public static boolean isAlphaNumeric(String s) {
		char[] acArray = s.toCharArray();
		for (char ac : acArray) {
			if (((ac >= 'A') && (ac <= 'z')) || ((ac >= 'a') && (ac <= 'z')))
				return true;
			if ((ac >= '0') && (ac <= '9'))
				return true;
			if ((ac >= 44032) && (ac <= 55203)) {
				return true;
			}
		}
		return false;
	}

	/********* 디비 친구목록에서 변경된 아이디 지우기 ************/
	public static void buddys(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		String aaa = pc.getName();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buddys WHERE buddy_name=?");

			pstm.setString(1, aaa);
			pstm.execute();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void 편지삭제(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;

		String aaa = pc.getName();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter WHERE receiver=?");
			pstm.setString(1, aaa);
			pstm.execute();
			// System.out.println("....["+ aaa +"].....");
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void hunt(L1PcInstance pc, String cmd){
		int price = 1000; // 수배 금액
		try{
			StringTokenizer tok = new StringTokenizer(cmd);
			String name = tok.nextToken();
			if(name == null || name.equals(""))
				throw new Exception();
			
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if(target == null){
				pc.sendPackets(String.format("%s 님을 찾을 수 없습니다.", name));
				return;
			}
			if(target.hasSkillEffect(L1SkillId.USER_WANTED)){
				pc.sendPackets(String.format("%s 님은 이미 수배중입니다.", name));
				return;
			}
			if(target.isGm()){
				pc.sendPackets("감히 운영자에게 수배를 걸 수 없습니다.");
				return;
			}
//			if (!(pc.getInventory().checkItem(40308, price))) { // 아데나
			if (!(pc.getInventory().checkItem(3000181, price))) { // 후원 코인
				pc.sendPackets("후원 코인이 부족합니다.");
				return;
			}
			
			String message = String.format("%s 님께서 %s님에게 수배를 걸었습니다.", pc.getName(), target.getName());
			
			pc.sendPackets(message);
			target.sendPackets(message);
			target.setSkillEffect(L1SkillId.USER_WANTED, -1);
			target.doWanted(true);
//			pc.getInventory().consumeItem(40308, price); // 아데나
			pc.getInventory().consumeItem(3000181, price); // 후원 코인
		}catch(Exception e){
			pc.sendPackets("----------------------------------------------------");
			pc.sendPackets("근/원/마법 대미지/명중/AC/리덕션이 모두 +3씩 상승");
			pc.sendPackets("금액: 후원 코인 1000개 (자동 차감)");
			pc.sendPackets("\\aG.수배   캐릭터명");
			pc.sendPackets("----------------------------------------------------");
		}
	} 

	private void phone(L1PcInstance pc, String param) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 10 > curtime) {
				long sec = (pc.getQuizTime() + 10) - curtime;
				pc.sendPackets(sec + "초 후에 사용할 수 있습니다.");
				return;
			}
			
			StringTokenizer tok = new StringTokenizer(param);
			String phone = tok.nextToken();
			Account account = Account.load(pc.getAccountName());
			if (param.length() < 10) {
				pc.sendPackets("없는 번호입니다. 다시 입력해주세요.");
				pc.sendPackets("* 경고: 잘못된 정보 입력시 게임 이용에 제재를 받을 수 있습니다.");
				return;
			}
			if (param.length() > 11) {
				pc.sendPackets("잘못된 번호입니다. 다시 입력해주세요.");
				pc.sendPackets("* 경고: 잘못된 정보 입력시 게임 이용에 제재를 받을 수 있습니다.");
				return;
			}
			if (isDisitAlpha(phone) == false) {
				pc.sendPackets("숫자로만 입력하세요.");
				pc.sendPackets("* 경고: 잘못된 정보 입력시 게임 이용에 제재를 받을 수 있습니다.");
				return;
			}
			if (account.getphone() != null) {
				pc.sendPackets("이미 전화번호가 설정되어 있습니다.");
				pc.sendPackets("번호 변경시 메티스에게 편지로 연락처를 보내세요.");
				return;
			}
			
			account.setphone(phone);
			Account.updatePhone(account);
			pc.sendPackets(" " + phone + " 설정 완료. 초기화 시 문자발송됩니다.");
			보안버프(pc);
		} catch (Exception e) {
			pc.sendPackets("고정을 하시게 되시면 재오픈 또는 공지를 사전에 알수있습니다.");
		}
	}
	
	private static void 보안버프(L1PcInstance pc) {
		pc.getAC().addAc(-1);
		pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ICON_SECURITY_SERVICES));
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void spawnNotifyOnOff(L1PcInstance pc, String param){
		try{
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("켬")) {
				if(pc.isBossNotify()){
					pc.sendPackets("보스알림:보스소환 및 알림이 이미 활성화 상태입니다.");
					return;
				}
				pc.setBossNotify(true);
				pc.sendPackets("보스알림:켬 (보스소환 및 알림이 실행 되었습니다)");
			} else if (on.equals("끔")) {
				if(!pc.isBossNotify()){
					pc.sendPackets("보스알림:보스소환 및 알림이 활성화 상태가 아닙니다.");
					return;
				}
				pc.setBossNotify(false);
				pc.sendPackets("보스알림:끔 (보스소환 및 알림이 종료 되었습니다)");
			} else {
				pc.sendPackets("잘못된 요청입니다.");
			}
		}catch(Exception e){
			pc.sendPackets(".보스알림 [켬,끔]으로 설정하세요. 현재(" + (pc.isBossNotify() == true ? "켜짐" : "꺼짐") + ")입니다.");
		}
	}
	
//	private void maphack(L1PcInstance pc, String param) {
//		try {
//			StringTokenizer st = new StringTokenizer(param);
//			String on = st.nextToken();
//			if (pc.getMapId() == 132) {
//				pc.sendPackets(new S_Ability(3, false));
//				pc.sendPackets( "현재 맵에서는 해당명령어를 사용할 수 없습니다.");
//				return;
//			}
//			if (on.equalsIgnoreCase("켬")) {
//				pc.sendPackets(new S_Ability(3, true));
//				pc.sendPackets("화면밝기: 켜짐");
//			} else if (on.equals("끔")) {
//				pc.sendPackets(new S_Ability(3, false));
//				pc.sendPackets("화면밝기: 꺼짐");
//			}
//		} catch (Exception e) {
//			pc.sendPackets(".화면밝기 [켬,끔]");
//		}
//	}
	
	private void maphack(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (pc.getMapId() == 132) {
				pc.sendPackets(new S_Ability(3, false));
				pc.sendPackets( "현재 맵에서는 해당명령어를 사용할 수 없습니다.");
				return;
			}
			if (on.equalsIgnoreCase("켬")) {
				pc.sendPackets(new S_Ability(3, true));
				pc.sendPackets("화면밝기: 켜짐");
			} else if (on.equals("끔")) {
				pc.sendPackets(new S_Ability(3, false));
				pc.sendPackets("화면밝기: 꺼짐");
			}
		} catch (Exception e) {
			pc.sendPackets(".화면밝기 [켬,끔]");
		}
	}

	/*private void 복구(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 5 > curtime) {
				long time = (pc.getQuizTime2() + 5) - curtime;
				pc.sendPackets( time + "초 후 사용할 수 있습니다.");
				return;
			}
			Updator.exec("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)", new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, pc.getAccountName());
				}
			});			
			pc.sendPackets("모든 케릭터의 좌표가 정상적으로 복구 되었습니다.");
			pc.setQuizTime(curtime);
		} catch (Exception e) {
		}
	}*/

	private void tell(L1PcInstance pc) {

		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime2() + 5 > curtime) {
			long time = (pc.getQuizTime2() + 5) - curtime;
			pc.sendPackets( time + "초 후 사용할 수 있습니다.");
			return;
		}
		try {
			if (pc.getMapId() == 781) {
				if (pc.getLocation().getX() <= 32998 && pc.getLocation().getX() >= 32988
						&& pc.getLocation().getY() <= 32758 && pc.getLocation().getY() >= 32736) {
					pc.sendPackets( "사용할 수 없는 장소입니다.");
					return;
				}
			}
			if (pc.hasSkillEffect(SHOCK_STUN) || pc.hasSkillEffect(DESPERADO) || pc.hasSkillEffect(L1SkillId.EMPIRE) || pc.hasSkillEffect(EARTH_BIND)
					|| pc.hasSkillEffect(CURSE_PARALYZE) || pc.hasSkillEffect(ICE_LANCE)
					|| pc.hasSkillEffect(FOG_OF_SLEEPING) || pc.hasSkillEffect(BONE_BREAK)) {
				pc.sendPackets( "현재 사용이 불가능한 상태 입니다.");
				return;
			}
			if (pc.getMapId() == 132) {
				pc.sendPackets(new S_Ability(3, false));
			}
			/*
			 * if (CharPosUtil.getZoneType(pc) == 0 && castle_id != 0) { // 공성장
			 * 주변에서 불가능 pc.sendPackets("사용할 수 없는 장소입니다.");
			 * return; }
			 */
			if (pc.isPinkName() || pc.isDead() || pc.isParalyzed() || pc.isSleeped() || pc.getMapId() == 800 || pc.getMapId() == 12150 || pc.getMapId() == 12154
					|| pc.getMapId() == 5302 || pc.getMapId() == 5153 || pc.getMapId() == 5490) {
				pc.sendPackets( "사용할 수 없는 상태입니다.");
				return;
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, pc.getWeapon()), true);
			pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, false);
			pc.update_lastLocalTellTime();
			pc.setQuizTime2(curtime);
		} catch (Exception exception35) {
		}
	}

	public void BloodParty(L1PcInstance pc) {
		if (pc.isDead()) {
			pc.sendPackets( "죽은 상태에선 사용할 수 없습니다.");
			return;
		}
		int ClanId = pc.getClanid();
		if (ClanId != 0 && pc.getClanRank() == L1Clan.군주 || pc.getClanRank() == L1Clan.수호
				|| pc.getClanRank() == L1Clan.부군주) {
			for (L1PcInstance SearchBlood : L1World.getInstance().getAllPlayers()) {
				if (SearchBlood.getClanid() != ClanId || SearchBlood.isPrivateShop() || SearchBlood.isInParty()) { // 클랜이
					continue; // 포문탈출
				} else if (SearchBlood.getName() != pc.getName()) {
					pc.setPartyType(1); // 파티타입 설정
					SearchBlood.setPartyID(pc.getId()); // 파티아이디 설정
					SearchBlood.sendPackets(new S_Message_YN(954, pc.getName()));
					pc.sendPackets( SearchBlood.getName() + " 님에게 파티를 신청했습니다");
				}
			}
		} else { // 클랜이 없거나 군주 또는 수호기사 [X]
			pc.sendPackets( "혈맹이 있으면서 군주, 부군주, 수호기사라면 사용가능.");
		}
	}

	private void age(L1PcInstance pc, String cmd) {
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String AGE = tok.nextToken();
			int AGEint = Integer.parseInt(AGE);
			if (AGEint > 59 || AGEint < 14) {
				pc.sendPackets( "자신의 실제 나이로 설정하세요.");
				return;
			}
			pc.setAge(AGEint);
			pc.save();
			pc.sendPackets( "나이가 [" + AGEint + "] 설정되었습니다.");
		} catch (Exception e) {
			pc.sendPackets( ".나이   숫자");
		}
	}
	
	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // 숫자가 아니라면
					&& !Character.isUpperCase(str.charAt(i)) // 대문자가 아니라면
					&& !Character.isLowerCase(str.charAt(i))) { // 소문자가 아니라면
				check = false;
				break;
			}
		}
		return check;
	}

	private void changepassword(L1PcInstance pc, String param) {
		try {
			if (pc.get_lastPasswordChangeTime() + 10 * 60 * 1000 > System.currentTimeMillis()) {
				pc.sendPackets( "암호를 변경하신지 10분이 지나지 않았습니다. 잠시후 다시 변경하세요.");
				return;
			}
			StringTokenizer tok = new StringTokenizer(param);
			String newpasswd = tok.nextToken();
			if (newpasswd.length() < 6) {
				pc.sendPackets( "6자 ~ 16자 사이의 영어나 숫자로 입력하세요.");
				return;
			}
			if (newpasswd.length() > 16) {
				pc.sendPackets( "6자 ~ 16자 사이의 영어나 숫자로 입력하세요.");
				return;
			}
			if (isDisitAlpha(newpasswd) == false) {
				pc.sendPackets( "영어와 숫자로만 입력하세요.");
				return;
			}
			to_Change_Passwd(pc, newpasswd);

		} catch (Exception e) {
			pc.sendPackets( ".암호변경 [변경할암호]로 입력 하세요.");
		}
	}

	private void to_Change_Passwd(L1PcInstance pc, String passwd) {
		Selector.exec("select account_name from characters where char_name=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getName());
			}
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					final String login = rs.getString("account_name");
					Updator.exec("update accounts set password=? where login=?", new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setString(1, passwd);
							pstm.setString(2,  login);
						}
					});
				}
			}
		});
		pc.sendPackets(String.format("당신의 계정 암호가 (%s)로 변경되었습니다.", passwd));
	}

	// 패스워드 맞는지 여부 리턴
	public static boolean isPasswordTrue(String Password, String oldPassword) {
		String _rtnPwd = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT password(?) as pwd");

			pstm.setString(1, oldPassword);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_rtnPwd = rs.getString("pwd");
			}
			if (_rtnPwd.equals(Password)) { // 동일하다면
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public static void privateShop(L1PcInstance pc) {
		try {
			if (!pc.isPrivateShop()) {
				pc.sendPackets( "개인상점 상태에서 사용이 가능합니다.");
				return;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
				if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
					pc.sendPackets("\\f3이미 당신의 보조 캐릭터가 무인상점 상태입니다.");
					pc.sendPackets("\\f3상점을 종료하시길 바랍니다. /상점");
					return;
				}
			}
			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			pc.stopHpMpRegeneration();
			pc.set무인상점(true);
			try {
				pc.save();
				pc.saveInventory();
			} catch (Exception e) {
			}
			client.setActiveChar(null);
			client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
			client.sendPacket(new S_Unknown2(1)); // 리스버튼을 위한 구조변경 // Episode U

		} catch (Exception e) {
		}
	}
	
	private void outsideChat(L1PcInstance pc, String param){
		try{
			if(param.equalsIgnoreCase("켬")){
				pc.setOutSideChat(true);
			}else if(param.equalsIgnoreCase("끔")){
				pc.setOutSideChat(false);				
			}else if(param.equalsIgnoreCase("상태")){
				
			}else
				throw new Exception();
			
			pc.sendPackets(String.format("외창 : %s", pc.isOutsideChat() ? "켜짐" : "꺼짐"));
		}catch(Exception e){
			pc.sendPackets(".외창 [켬/끔/상태]");
		}
	}
	
	private String parseStat(String s) throws Exception{
		if(s.equalsIgnoreCase("힘"))
			return "str";
		else if(s.equalsIgnoreCase("덱스"))
			return "dex";
		else if(s.equalsIgnoreCase("콘"))
			return "con";
		else if(s.equalsIgnoreCase("인트"))
			return "int";
		else if(s.equalsIgnoreCase("위즈"))
			return "wis";
		else if(s.equalsIgnoreCase("카리"))
			return "cha";
		throw new Exception(s);
	}
	
	public void fullstat(L1PcInstance pc, String param){
		try{
			String[] arr = param.split(" ");
			if(arr == null || arr.length < 2)
				throw new Exception();
			
			String s = parseStat(arr[0]);
			MJFullStater.running(pc, s, Integer.parseInt(arr[1]));
		}catch(Exception e){
			pc.sendPackets(String.format(".원스텟 [힘/덱스/콘/인트/위즈/카리] [올릴수] 남은스텟 %d", (pc.getLevel() - 50 - pc.getBonusStats())));
		}
	}
	
	// TODO 중개 거래 게시판
			private static void LetterList(L1PcInstance pc, int type, int count) {
				pc.sendPackets(new S_LetterList(pc, type, count));
			}
			
			public static void WriteCancle(String buyer, String seller) {
				int nu1 = 949;
				SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
				Date currentTime = new Date();
				String dTime = formatter.format(currentTime);
				String subject = "구매취소내역";
				String content = "구매자 :" + buyer + "" + "\n\n상대방이 구매를 취소하엿습니다." + "\n입금 대기상태로 바뀝니다.";
				String name = "거래관리자";
				L1PcInstance target = L1World.getInstance().getPlayer(seller);
				LetterTable.getInstance().writeLetter(nu1, dTime, name, seller, 0, subject, content);
				if (target != null && target.getOnlineStatus() != 0) {
					target.sendPackets(
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "구매취소: " + buyer + "님이 구매를 취소하였습니다."));
					LetterList(target, 0, 20);
					target.sendPackets(new S_SkillSound(target.getId(), 1091));
					target.sendPackets(new S_ServerMessage(428));
				}
			}

			public static void WriteComplete(String buyer, int count, String seller) {
				int nu1 = 949;
				SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
				Date currentTime = new Date();
				String dTime = formatter.format(currentTime);
				String subject = "판매완료내역";
				String content = "후원 코인: " + count + "개" + "\n판매자: " + seller + "" + "\n물품 구매가 완료되었습니다. " + "\n\n① 캐릭터를 재접속 하세요."
						+ "\n② 인벤토리 창을 여세요." + "\n③ [부가 아이템 창고] 아이콘 클릭" + "\n④ 후원 코인을 수령하세요.";
				String name = "거래관리자";
				L1PcInstance target = L1World.getInstance().getPlayer(buyer);
				LetterTable.getInstance().writeLetter(nu1, dTime, name, buyer, 0, subject, content);
				if (target != null && target.getOnlineStatus() != 0) {
					target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "판매완료: '부가아이템창고'로 지급 되었습니다(리스타트후 수령가능)"));
					target.killSkillEffectTimer(BUYER_COOLTIME);
					LetterList(target, 0, 20);
					target.sendPackets(new S_SkillSound(target.getId(), 1091));
					target.sendPackets(new S_ServerMessage(428));
				}
			}

			public static void WriteBuyLetter(String player, int count, int sellcount, String bankname, String name,
					String number, String sellername, String buyername) {
				int nu1 = 949;
				SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
				Date currentTime = new Date();
				String dTime = formatter.format(currentTime);
				String subject = "구매 신청 내역";
				String content = "후원 코인: " + count + "개." + "\n구매 금액 : " + sellcount + "원. " + "\n은행 : " + bankname + ""
						+ "\n예금주 : " + name + "" + "\n계좌번호 : " + number + "" + "\n판매자 캐릭터 : " + sellername + "" + "\n구매자 캐릭터 : "
						+ buyername + "" + "\n\n판매자는 입금완료후 반드시" + "\n.판매완료 (게시번호) 를 하셔야" + "\n구매자에게 후원 코인이 지급됩니다."
						+ "\n\n판매자가 악의적으로 판매완료를" + "\n거부할 경우 메티스로 문의." + "\n입금전 반드시 상대방이" + "\n접속중인지 확인후 거래를"
						+ "\n진행해 주시기 바랍니다.";
				String project = "거래관리자";
				L1PcInstance target = L1World.getInstance().getPlayer(player);
				LetterTable.getInstance().writeLetter(nu1, dTime, project, player, 0, subject, content);
				if (target != null && target.getOnlineStatus() != 0) {
					LetterList(target, 0, 20);
					target.sendPackets(new S_SkillSound(target.getId(), 1091));
					target.sendPackets(new S_ServerMessage(428));
				}
			}

			public static void WriteSellLetter(String player, int count, int sellcount, String bankname, String name,
					String number, String charname) {
				int nu1 = 949;
				SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
				Date currentTime = new Date();
				String dTime = formatter.format(currentTime);
				String subject = "판매 등록 내역";
				String content = "후원 코인: " + count + "개." + "\n판매 금액 : " + sellcount + "원. " + "\n은행 : " + bankname + ""
						+ "\n예금주 : " + name + "" + "\n계좌번호 : " + number + "" + "\n판매자 캐릭터 : " + charname + ""
						+ "\n\n위 내용과 상이할 경우 " + "\n.판매취소 (게시글번호)" + "\n하신후 재등록 하시기 바랍니다." + "\n\n이용자간 거래로 발생한 피해는"
						+ "\n어떠한 경우에도 책임지지 않습니다.";
				String project = "거래관리자";
				L1PcInstance target = L1World.getInstance().getPlayer(player);
				LetterTable.getInstance().writeLetter(nu1, dTime, project, player, 0, subject, content);
				if (target != null && target.getOnlineStatus() != 0) {
					LetterList(target, 0, 20);
					target.sendPackets(new S_SkillSound(target.getId(), 1091));
					target.sendPackets(new S_ServerMessage(428));
				}
			}

			private static void countR1(L1PcInstance pc, String count) {
				StringTokenizer st = new StringTokenizer(count);
				try {
					int i = 0;
					int sellcount = 0;
					String bankname = null;
					String name = null;
					String numeber = null;
					try {
						i = Integer.parseInt(st.nextToken());
						sellcount = Integer.parseInt(st.nextToken());
						bankname = st.nextToken();
						name = st.nextToken();
						numeber = st.nextToken();
					} catch (NumberFormatException e) {
					}

					if (i <= 0) {
						pc.sendPackets(".판매  (판매금액)을 적어주세요.");
						return;
					}
					if (pc.getLevel() < Config.ADENASHOP_LEVEL) {
						pc.sendPackets("판매 최소 (" + Config.ADENASHOP_LEVEL + ")레벨 이상만 등록 가능합니다.");
						return;
					}
					if (i > Config.MAX_SELL_ADENA) {
						pc.sendPackets("최대판매: " + Config.MAX_SELL_ADENA + " 개 이상 판매하실 수 없습니다.");
						return;
					}
					if (i >= 1 && i < Config.MIN_SELL_ADENA) {
						pc.sendPackets("최소판매: " + Config.MIN_SELL_ADENA + " 개 이상 입니다.");
						return;
					}
					if (sellcount < Config.MIN_SELL_CASH) {
						pc.sendPackets("최소 판매금액: " + Config.MIN_SELL_CASH + "원 이상 입니다.");
						return;
					}
					if (sellcount > Config.MAX_SELL_CASH) {
						pc.sendPackets("최대 판매금액: " + Config.MAX_SELL_CASH + "원 이하 입니다.");
						return;
					}

					String selltype = "판매 중";
//					L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.ADENA); // 아데나 
//					L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.ADENA1); // 1억 아데나 수표
					L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.DUNK_COIN); // 후원 코인
					L1ItemInstance adena = new L1ItemInstance(temp, i);

//					if (pc.getInventory().checkItem(L1ItemId.ADENA, i)) {
//						pc.getInventory().consumeItem(L1ItemId.ADENA, i);
					if (pc.getInventory().checkItem(L1ItemId.DUNK_COIN, i)) {
						pc.getInventory().consumeItem(L1ItemId.DUNK_COIN, i);
						AuctionSystemTable.getInstance().writeTopic(pc, selltype, adena, i, sellcount, bankname, numeber, name);
						WriteSellLetter(pc.getName(), i, sellcount, bankname, name, numeber, pc.getName());
//						WriteSellLetter("메티스", i, sellcount, bankname, name, numeber, pc.getName()); // 운영자에게 편지
					} else {
						pc.sendPackets("수량이 맞는지 확인하시기 바랍니다.");
					}
				} catch (Exception exception) {
//					pc.sendPackets(".판매 (아데나) (판매금액) (은행명) (예금주) (계좌)");
					pc.sendPackets("----------------------------------------------------");
					pc.sendPackets("\\aG【예시】");
					pc.sendPackets("\\aG.판매  10000  30000  신한  홍길동  0123456789(-없이)");
					pc.sendPackets("----------------------------------------------------");
					pc.sendPackets(".판매   코인   현금   은행   예금주   계좌번호");
				}
			}

			private static void countR2(L1PcInstance pc, String count) {
				if (pc.hasSkillEffect(BUYER_COOLTIME)) {
					int n = pc.getSkillEffectTimeSec(BUYER_COOLTIME);
					pc.sendPackets("이미 다른 물품을 구매중이거나 쿨타임이 적용중입니다.");
					pc.sendPackets(new S_SystemMessage(String.format("%d초 후에 구매신청을 하실 수 있습니다.", n)));
					return;
				}
				int i = 0;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;

				try {
					i = Integer.parseInt(count);
				} catch (NumberFormatException e) {
				}
				String charname = null;
				int adenacount = 0;
				int sellcount = 0;
				int status = 0;
				String bank = null;
				String banknumeber = null;
				String bankname = null;
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement(
							"SELECT  id, name, count, sellcount, status, bank, banknumber, bankname from Auction where id Like '"
									+ i + "'");
					rs = pstm.executeQuery();
					while (rs.next()) {
						charname = rs.getString("name");
						adenacount = rs.getInt("count");
						sellcount = rs.getInt("sellcount");
						status = rs.getInt("status");
						bank = rs.getString("bank");
						banknumeber = rs.getString("banknumber");
						bankname = rs.getString("bankname");
					}

					if (charname == null) {
						pc.sendPackets("잘못된 게시번호이거나 게시번호를 입력하지 않았습니다.");
						return;
					}
					if (status == 1) {
						pc.sendPackets("이미 다른사람이 구매신청을 한 상태입니다.");
						return;
					}
					if (status == 2) {
						pc.sendPackets("해당 게시물은 이미 판매가 완료되었습니다.");
						return;
					}

					try {
						int time = 600;
						WriteBuyLetter(pc.getName(), adenacount, sellcount, bank, bankname, banknumeber, charname,
								pc.getName());
						WriteBuyLetter(charname, adenacount, sellcount, bank, bankname, banknumeber, charname, pc.getName());
//						WriteBuyLetter("메티스", adenacount, sellcount, bank, bankname, banknumeber, charname, pc.getName()); // 운영자에게 편지
						AuctionSystemTable.getInstance().AuctionUpdate(i, pc.getName(), pc.getAccountName(), "입금대기", 1);
						pc.setSkillEffect(BUYER_COOLTIME, time * 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (SQLException e) {
					pc.sendPackets(".구매 (게시글번호)");
					pc.sendPackets(".구매 0001");
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
			}

			private static void countR3(L1PcInstance pc, String count) {
				int i = 0;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				try {
					i = Integer.parseInt(count);
				} catch (NumberFormatException e) {
				}
				int itemid = 0;
				int itemcount = 0;
				String account = null;
				int status = 0;
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement(
							"SELECT  item_id ,count ,AccountName ,status from Auction where id Like '" + i + "'");
					rs = pstm.executeQuery();
					while (rs.next()) {
						itemid = rs.getInt("item_id");
						itemcount = rs.getInt("count");
						account = rs.getString("AccountName");
						status = rs.getInt("status");
					}
					if (status == 1) {
						pc.sendPackets("입금 대기상태 에서는 판매취소가 불가능 합니다.");
						return;
					}
					if (status == 2) {
						pc.sendPackets("판매가 완료된 상태입니다.");
						return;
					}
					if (pc.getAccountName().equalsIgnoreCase(account) || pc.isGm()) {
						pc.getInventory().storeItem(itemid, itemcount);
						AuctionSystemTable.getInstance().deleteTopic(i);
						pc.sendPackets("정상적으로 취소되었습니다");
					} else {
						pc.sendPackets("등록하신 물품이 아닙니다");
					}
				} catch (SQLException e) {
					pc.sendPackets(".판매취소 (게시글번호) 라고 적어주십시요.");
					pc.sendPackets("예)  .판매취소 0035");
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
			}
//			public static boolean checkAutoList(L1PcInstance pc, String s) {
//				String s2 = s;
//				switch (s2) {
//				case "cmd_u_buy":{
//					return true;
//				}
//				case "ps_potion_1":{
//					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() - 10, 10, 95));
//					showAutoPotionList(pc);
//					return true;
//				}
//				case "ps_potion_2":{
//					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() - 1, 10, 95));
//					showAutoPotionList(pc);
//					return true;
//			}
//				case "ps_potion_3":{
//					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() + 1, 10, 95));
//					showAutoPotionList(pc);
//					return true;
//					}
//				case "ps_potion_4":{
//					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() + 10, 10, 95));
//					showAutoPotionList(pc);
//					return true;
//				}
//				case "ps_potion_5":{
//					if (pc.is_자동물약사용()) {
//						pc.sendPackets(new S_SystemMessage("\\aB자동 물약: 이미 실행 중입니다."));
//						return true;
//					}
//					pc.set_자동물약사용(true);
//					AutoSystemController.getInstance().removeAuto(pc);
//					pc.sendPackets(new S_SystemMessage("\\f2자동 물약: 켜짐"));
//					showAutoPotionList(pc);
//					return true;
//				}
//				case "ps_potion_6":{
//					if (!pc.is_자동물약사용()) {
//						pc.sendPackets(new S_SystemMessage("\\aB자동 물약: 실행 중이지 않습니다."));
//						return true;
//					}
//					pc.set_자동물약사용(false);
//					pc.sendPackets(new S_SystemMessage("\\aG자동 물약: 꺼짐"));
//					showAutoPotionList(pc);
//					return true;
//				}
//				case "ps_potion_7":{
//					pc.sendPackets(new S_RetrieveAutoPotion(pc));
//					return true;
//				}												
//		        default: {
//		            return false;
//		        }
//		    }
//		}
//			public static void showAutoPotionList(L1PcInstance pc) {
//				ArrayList<String> result = new ArrayList<String>();
//				result.add(pc.is_자동물약사용() ? "5208" : "5209");
//				result.add(String.valueOf(pc.get_자동물약퍼센트()));
//				result.add("off");
//				result.add("-1");
//				result.add("5208");
//				result.add("5208");
//				result.add("5209");
//				result.add("5209");
//				result.add("5209");
//				result.add("5209");
//				result.add("5208");
//				result.add("");
//				result.add("5208");
//				result.add("5208");				
//				result.add("5209");
//				if (result.isEmpty()) {
//					return;
//				}
//				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "autopotion1", (String[]) result.toArray(new String[result.size()])));
//			}
			
			
			// Version 1.0
			public static boolean checkAutoList(L1PcInstance pc, String s) {
			    try {
			        switch (s) {
			        case "ps_autohunt_1":
			            // 자동 사냥 시작 가능 여부 확인
			            boolean canStartAutoHunt = true;
			            String errorMessage = "";

			            // 위치 범위 확인
			            if (!((pc.getX() >= 33418 && pc.getX() <= 33442) && (pc.getY() >= 32801 && pc.getY() <= 32825)
			                    && pc.getMapId() == 4)) {
			                canStartAutoHunt = false;
			                errorMessage = "기란 마을 중앙 근처에서만 시작할 수 있습니다.";
			            }

			            // 무기 착용 여부 확인
			            if (pc.getWeapon() == null) {
			                canStartAutoHunt = false;
			                errorMessage = "무기를 착용해야 자동 사냥을 시작할 수 있습니다.";
			            }

			            // 아이템 확인
			            if (!pc.getInventory().checkItem(3000209)
			                    && (!pc.getInventory().checkItem(3000213) && (!pc.getInventory().checkItem(3000214)))) {
			                canStartAutoHunt = false;
			                errorMessage = "자동 사냥 사용권이 있어야 사용 가능합니다.";
			            }

			            // 아데나 확인
//			            if (!pc.getInventory().checkItem(40308, 10000000)) {
//			                canStartAutoHunt = false;
//			                errorMessage = "최소 1000만 아데나가 있어야만 사용 가능합니다.";
//			            }

			            // 던전 이용 시간 확인
			            if (!AutoSystemController.check_auto_dungeon(pc)) {
			                canStartAutoHunt = false;
			                errorMessage = "던전 이용 시간이 종료되었습니다.";
			            }

			            // 자동 사냥 실행 가능 여부에 따라 로직 수행
			            if (canStartAutoHunt) {
			                if (!pc.isAutoHunt()) {
			                    pc.sendPackets(new S_SystemMessage("자동 사냥 시작"));
			                    pc.setAutoHunt(true);

			                    UserCommands commands = new UserCommands();
			                    commands.autoHunt(pc, "1 1"); // 자동 사냥 시작 호출
			                    pc.sendPackets(new S_SkillSound(pc.getId(), 16769)); // "자동 사냥을 시작합니다."
			                } else {
			                    pc.sendPackets(new S_SystemMessage("이미 자동 사냥 중입니다."));
			                }
			            } else {
			                pc.sendPackets(new S_SkillSound(pc.getId(), 16773)); // "자동 사냥을 실행할 수 없는 상태입니다."
			                pc.sendPackets(new S_SystemMessage(errorMessage)); // 예외 메시지 출력
			            }

			            return true;

			        case "ps_autohunt_2":
			            if (pc.isAutoHunt()) {
			                pc.sendPackets(new S_SystemMessage("자동 사냥 중지"));
			                pc.setAutoHunt(false);

			                AutoSystemController autoController = AutoSystemController.getInstance();
			                autoController.removeAuto(pc); // 자동 사냥 중지
			            } else {
			                pc.sendPackets(new S_SystemMessage("자동 사냥 중이 아닙니다."));
			            }
			            return true;

	//ver 2.0
//	public static boolean checkAutoList(L1PcInstance pc, String s) {
//		try {
//			switch (s) {
//			case "ps_autohunt_1":
//				// 자동 사냥 시작 가능 여부 확인
//				boolean canStartAutoHunt = true;
//				String errorMessage = "";
//
//				// 위치 범위 확인
//				if (!((pc.getX() >= 33424 && pc.getX() <= 33436) && (pc.getY() >= 32807 && pc.getY() <= 32819)
//						&& pc.getMapId() == 4)) {
//					canStartAutoHunt = false;
//					errorMessage = "기란 마을 십자가 근처에서만 시작할 수 있습니다.";
//				}
//
//				// 아이템 확인
//				if (!pc.getInventory().checkItem(3000209)
//						&& (!pc.getInventory().checkItem(3000213) && (!pc.getInventory().checkItem(3000214)))) {
//					canStartAutoHunt = false;
//					errorMessage = "자동 사냥 사용권이 있어야 사용 가능합니다.";
//				}
//
//				// 아데나 확인
//				if (!pc.getInventory().checkItem(40308, 10000000)) {
//					canStartAutoHunt = false;
//					errorMessage = "최소 1000만 아데나가 있어야만 사용 가능합니다.";
//				}
//
//				// 던전 이용 시간 확인
//				if (!AutoSystemController.check_auto_dungeon(pc)) {
//					canStartAutoHunt = false;
//					errorMessage = "던전 이용 시간이 종료되었습니다.";
//				}
//
//				// 자동 사냥 실행 가능 여부에 따라 로직 수행
//				if (canStartAutoHunt) {
//					if (!pc.isAutoHunt()) {
//						pc.sendPackets(new S_SystemMessage("자동 사냥 시작"));
//						pc.setAutoHunt(true);
//
//						UserCommands commands = new UserCommands();
//						commands.autoHunt(pc, "1 1"); // 자동 사냥 시작 호출
//						pc.sendPackets(new S_SkillSound(pc.getId(), 16769)); // "자동 사냥을 시작합니다."
//					} else {
//						pc.sendPackets(new S_SystemMessage("이미 자동 사냥 중입니다."));
//					}
//				} else {
//					pc.sendPackets(new S_SkillSound(pc.getId(), 16773)); // "자동 사냥을 실행할 수 없는 상태입니다."
//					pc.sendPackets(new S_SystemMessage(errorMessage)); // 예외 메시지 출력
//				}
//
//				return true;
//
//			case "ps_autohunt_2":
//				if (pc.isAutoHunt()) {
//					pc.sendPackets(new S_SystemMessage("자동 사냥 중지"));
//					pc.setAutoHunt(false);
//
//					AutoSystemController autoController = AutoSystemController.getInstance();
//					autoController.removeAuto(pc); // 자동 사냥 중지
//				} else {
//					pc.sendPackets(new S_SystemMessage("자동 사냥 중이 아닙니다."));
//				}
//				return true;

//			case "ps_autohunt_settings": // 자동 사냥 설정 페이지로 이동
//				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ps_9"));
//				return true;

				case "cmd_u_buy":{
					return true;
				}
				case "ps_potion_1":{
					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() - 10, 10, 95));
					showAutoPotionList(pc);
					return true;
				}
				case "ps_potion_2":{
					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() - 1, 10, 95));
					showAutoPotionList(pc);
					return true;
			}
				case "ps_potion_3":{
					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() + 1, 10, 95));
					showAutoPotionList(pc);
					return true;
					}
				case "ps_potion_4":{
					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() + 10, 10, 95));
					showAutoPotionList(pc);
					return true;
				}
				case "ps_potion_95":{
					pc.set_자동물약퍼센트(IntRange.ensure(pc.get_자동물약퍼센트() + 95, 10, 95));
					showAutoPotionList(pc);
					return true;
				}
				case "ps_potion_5":{
					if (Config.자동물약버프) {
					if (pc.is_자동물약사용()) {
						pc.sendPackets(new S_SystemMessage("\\aB이미 자동물약모드 시작 상태입니다."));
						return true;
					}
					pc.set_자동물약사용(true);
					pc.set_자동숫돌사용(true);
					AutoSystemController.getInstance().removeAuto(pc);
					pc.sendPackets(new S_SystemMessage("\\aG자동물약 모드 시작"));
					showAutoPotionList(pc);
					return true;
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY현재 자동물약/자동버프는 비활성화입니다."));
				}
				}
				case "ps_potion_6":{
					if (!pc.is_자동물약사용()) {
						pc.sendPackets(new S_SystemMessage("\\aB현재 자동물약모드 상태가 아닙니다."));
						return true;
					}
					pc.set_자동물약사용(false);
					pc.set_자동숫돌사용(false);
					pc.sendPackets(new S_SystemMessage("\\aG자동물약 모드 중지"));
					showAutoPotionList(pc);
					return true;
				}
				case "ps_potion_7":{
					pc.sendPackets(new S_RetrieveAutoPotion(pc));
					return true;
				}

				case "ps_autobuff_1":{
					showAutoBuffList(pc);
					return true;
				}
				case "ps_autobuff_8":{
					pc.sendPackets(new S_Ability(3, true));
					pc.sendPackets("라이트: 켜짐");
					return true;
				}
				case "ps_autobuff_9":{
					pc.sendPackets(new S_Ability(3, false));
					pc.sendPackets("라이트: 꺼짐");
					return true;
				}
				case "ps_autobuff_2":{
					
					if (Config.자동물약버프) {
					if (pc.is_자동버프사용()) {
						pc.sendPackets(new S_SystemMessage("\\aB이미 자동버프모드 시작 상태입니다."));
						return true;
					}
					pc.set_자동버프사용(true);
					AutoSystemController.getInstance().removeAuto(pc);
					pc.sendPackets(new S_SystemMessage("\\aG자동버프:\\aD 시작"));
					showAutoPotionList(pc);
					return true;
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY현재 자동물약/자동버프는 비활성화입니다."));
				}
				}
				case "ps_autobuff_3":{
					if (!pc.is_자동버프사용()) {
						pc.sendPackets(new S_SystemMessage("\\aB현재 자동버프모드 상태가 아닙니다."));
						return true;
					}
					pc.set_자동버프사용(false);
					pc.sendPackets(new S_SystemMessage("\\aG자동버프:\\aB 중지"));
					showAutoPotionList(pc);
					return true;
				}
				case "ps_autobuff_4":{
					if (!pc.is_자동버프전투시사용()) {
						pc.sendPackets(new S_SystemMessage("전투시 자동버프: 유지함"));
						pc.set_자동버프전투시사용(true);
					} else {
						pc.sendPackets(new S_SystemMessage("전투시 자동버프: 유지안함"));
						pc.set_자동버프전투시사용(false);
					}
					showAutoPotionList(pc);
					return true;
				}
				case "ps_autobuff_5":{
					if (!pc.is_자동버프세이프티존사용()) {
						pc.sendPackets(new S_SystemMessage("세이프존 자동버프: 유지함"));
						pc.set_자동버프세이프티존사용(true);
					} else {
						pc.sendPackets(new S_SystemMessage("세이프존 자동버프: 유지안함"));
						pc.set_자동버프세이프티존사용(false);
					}
					showAutoPotionList(pc);
					return true;
				}
				case "ps_2":
				case "ps_3":
				case "ps_8":
				case "ps_12":
				case "ps_14":
				case "ps_114":
				case "ps_115":
				case "ps_116":
				case "ps_117":
				case "ps_118":
				case "ps_88":
				case "ps_89":
				case "ps_90":
				case "ps_91":
				case "ps_93":
				case "ps_94":
				case "ps_21":
				case "ps_26":
				case "ps_42":
				case "ps_43":
				case "ps_48":
				case "ps_129":
				case "ps_134":
				case "ps_135":
				case "ps_137":
				case "ps_138":
				case "ps_147":
				case "ps_151":
				case "ps_155":
				case "ps_163":
				case "ps_171":
				case "ps_175":
				case "ps_176":
				case "ps_136":
				case "ps_149":
				case "ps_158":
				case "ps_160":
				case "ps_177":
				case "ps_150":
				case "ps_156":
				case "ps_166":
				case "ps_178":
				case "ps_148":
				case "ps_152":
				case "ps_159":
				case "ps_168":
				case "ps_169":
				case "ps_167":
				case "ps_179":
				case "ps_50":
				case "ps_52":
				case "ps_54":
				case "ps_56":
				case "ps_79":
				case "ps_98":
				case "ps_99":
				case "ps_101":
				case "ps_102":
				case "ps_104":
				case "ps_105":
				case "ps_106":
				case "ps_107":
				case "ps_109":
				case "ps_110":
				case "ps_111":
				case "ps_234":
				case "ps_181":
				case "ps_185":
				case "ps_186":
				case "ps_190":
				case "ps_191":
				case "ps_195":
				case "ps_197":
				case "ps_201":
				case "ps_204":
				case "ps_206":
				case "ps_209":
				case "ps_211":
				case "ps_214":
				case "ps_216":
				case "ps_218":
				case "ps_219":
				case "ps_223":
				case "ps_226":
				case "ps_231":{
					s = s.replace("ps_", "");
					int skillid = Integer.valueOf(s).intValue();
					int checkcount;
					java.util.Iterator<Integer> localIterator;
					if (!pc.isPassive(MJPassiveID.AURAKIA.toInt())) {
						if (skillid == 185) {
							pc.remove_자동버프리스트(190);
							pc.remove_자동버프리스트(195);
							pc.remove_자동버프리스트(197);
							pc.sendPackets(new S_SystemMessage("\\aG아우라키아를 배우지 않은 캐릭터는 1개의 각성만 적용."));
						} else if (skillid == 190) {
							pc.remove_자동버프리스트(185);
							pc.remove_자동버프리스트(195);
							pc.remove_자동버프리스트(197);
							pc.sendPackets(new S_SystemMessage("\\aG아우라키아를 배우지 않은 캐릭터는 1개의 각성만 적용."));
						} else if (skillid == 195) {
							pc.remove_자동버프리스트(185);
							pc.remove_자동버프리스트(190);
							pc.remove_자동버프리스트(197);
							pc.sendPackets(new S_SystemMessage("\\aG아우라키아를 배우지 않은 캐릭터는 1개의 각성만 적용."));
						} else if (skillid == 197) {
							pc.remove_자동버프리스트(185);
							pc.remove_자동버프리스트(195);
							pc.remove_자동버프리스트(190);
							pc.sendPackets(new S_SystemMessage("\\aG아우라키아를 배우지 않은 캐릭터는 1개의 각성만 적용."));
						}
					} else {
						checkcount = 0;
						if ((skillid == 185) || (skillid == 195) || (skillid == 190) || (skillid == 197)) {
							for (localIterator = pc.get_자동버프리스트().iterator(); localIterator.hasNext();) {
								int id = ((Integer) localIterator.next()).intValue();
								if (skillid != id) {
									   continue;
		                        }
									if ((id == 185) || (id == 195) || (id == 190) || (id == 197)) {
										checkcount++;
									}
									if (checkcount >= 2) {
										pc.sendPackets(new S_SystemMessage("이미 2개의 각성이 설정되었습니다."));
										pc.sendPackets(new S_SystemMessage("바꾸시려면 4개 각성을 다 선택하시고 확인하신 뒤 다시 2개를 해제해주시길 바랍니다."));
										return true;
									}
								}
							}
						}
					
					if (skillid == 8) {
						pc.remove_자동버프리스트(12);
						pc.remove_자동버프리스트(48);
						
					}
					if (skillid == 12) {
						pc.remove_자동버프리스트(8);
						pc.remove_자동버프리스트(48);
						
					}
					if (skillid == 48) {
						pc.remove_자동버프리스트(8);
						pc.remove_자동버프리스트(12);
						
					}
					
					
					if ((skillid == 168) && (pc.is_자동버프리스트(159))) {
						pc.sendPackets(new S_SystemMessage("어스가디언과 중복하여 사용할 수 없습니다."));
						return false;
					}
					if ((skillid == 159) && (pc.is_자동버프리스트(168))) {
						pc.sendPackets(new S_SystemMessage("아이언스킨과 중복하여 사용할 수 없습니다."));
						return false;
					}
					if ((skillid == 156) && (pc.is_자동버프리스트(166))) {
						pc.sendPackets(new S_SystemMessage("스톰샷과 중복하여 사용할 수 없습니다."));
						return false;
					}
					if ((skillid == 166) && (pc.is_자동버프리스트(156))) {
						pc.sendPackets(new S_SystemMessage("아이 오브 스톰과 중복하여 사용할 수 없습니다."));
						return false;
					}
					if (((skillid == 179) || (skillid == 177) || (skillid == 155) || (skillid == 178))
							&& ((pc.check_자동물약리스트(40068)) || (pc.check_자동물약리스트(30076)) || (pc.check_자동물약리스트(140068))
									|| (pc.check_자동물약리스트(210110)))) {
						pc.sendPackets(new S_SystemMessage("자동물약에 와퍼가 포함되어 있습니다."));
						pc.sendPackets(new S_SystemMessage("자동스킬에 (댄싱블레이즈/샌드스톰/포커스웨이브/허리케인)을 포함시킬려면 자동물약에 와퍼를 제외하시기 바랍니다."));
						return false;
					}
					if (!SkillsTable.getInstance().spellCheck(pc.getId(), skillid)) {
						pc.remove_자동버프리스트(skillid);
						L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
						if (skill != null) {
							pc.sendPackets(new S_SystemMessage("\\aD자동버프 설정 실패: \\aA" + skill.getName() + "(습득하지 않은 마법)"));
						}
					} else {
						int result = pc.check_자동버프리스트(skillid);
						if ((result == 0) && ((skillid == 185) || (skillid == 195) || (skillid == 190) || (skillid == 197))) {
							pc.removeSkillEffect(skillid);
						}
					}
					showAutoBuffList(pc);
					return true;
				}
				case "at_loot_del_0":
				case "at_loot_del_1":
				case "at_loot_del_2":
				case "at_loot_del_3":
				case "at_loot_del_4":
				case "at_loot_del_5":
				case "at_loot_del_6":
				case "at_loot_del_7":
				case "at_loot_del_8":
				case "at_loot_del_9":{
					if (pc.get_자동루팅리스트().isEmpty()) {
						return true;
					}
					s = s.replace("at_loot_del_", "");

					int index = Integer.valueOf(s).intValue();
					if (pc.get_자동루팅리스트().size() >= index + 1) {
						pc.get_자동루팅리스트().remove(index);
					}
					showAutoRootList(pc);

					return true;
				}
				case "at_rev_del_0":
				case "at_rev_del_1":
				case "at_rev_del_2":
				case "at_rev_del_3":
				case "at_rev_del_4":
				case "at_rev_del_5":
				case "at_rev_del_6":
				case "at_rev_del_7":
				case "at_rev_del_8":
				case "at_rev_del_9":{
					if (pc.get_자동용해리스트().isEmpty()) {
						return true;
					}
					s = s.replace("at_rev_del_", "");

					int index1 = Integer.valueOf(s).intValue();
					if (pc.get_자동용해리스트().size() >= index1 + 1) {
						pc.get_자동용해리스트().remove(index1);
					}
					showAutoResolList(pc);
				}

	            default:
	                return false; // 기본 처리
	        }
	    } catch (Exception e) {
	        // 오류 메시지 및 로그 추가
	        pc.sendPackets(new S_SystemMessage("오류가 발생했습니다. 다시 시도하십시오."));
	        return false; // 예외 처리 후 반환
	    }
	}
			
			
	
//	// 자동사냥 상태를 보여주는 메서드
//	public static void showAutoHuntStatus(L1PcInstance pc) {
//		ArrayList<String> result = new ArrayList<String>();
//
//		// 자동사냥 활성화/비활성화 상태를 기반으로 5208(활성화), 5209(비활성화)로 표시
//		result.add(pc.isAutoHunt() ? "5208" : "5209");
//
//		// 기타 표시할 항목 추가 (필요에 따라 확장 가능)
//		result.add(""); // 추가 항목을 위한 자리 확보
//
//		// NPC Talk Return을 사용하여 표시
//		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ps_9", (String[]) result.toArray(new String[result.size()])));
//	}

			
			
			

			public static void showAutoBuffList(L1PcInstance pc) {
				ArrayList<String> result = new ArrayList<String>();

				ArrayList<Integer> _자동버프리스트 = pc.get_자동버프리스트();

				String html = "";
				switch (pc.getType()) {
				case 0:
					result.add(_자동버프리스트.contains(Integer.valueOf(2)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(3)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(8)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(12)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(14)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(114)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(115)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(116)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(117)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(118)) ? "5208" : "5209");
					html = "ps_0";
					break;
				case 1:
					result.add(_자동버프리스트.contains(Integer.valueOf(2)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(3)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(8)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(88)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(89)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(90)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(91)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(93)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(94)) ? "5208" : "5209");
					html = "ps_1";
					break;
				case 2:
					result.add(_자동버프리스트.contains(Integer.valueOf(2)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(3)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(8)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(12)) ? "5208" : "5209");
//					result.add(_자동버프리스트.contains(Integer.valueOf(14)) ? "5208" : "5209");
				    result.add((_자동버프리스트.contains(14) && !_자동버프리스트.contains(134)) ? "5208" : "5209"); // 디크리즈 웨이트
					result.add(_자동버프리스트.contains(Integer.valueOf(21)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(26)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(42)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(43)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(48)) ? "5208" : "5209");

					result.add(_자동버프리스트.contains(Integer.valueOf(129)) ? "5208" : "5209");
//					result.add(_자동버프리스트.contains(Integer.valueOf(134)) ? "5208" : "5209");
				    result.add((_자동버프리스트.contains(134) && !_자동버프리스트.contains(14)) ? "5208" : "5209"); // 엘븐 그레비티
					result.add(_자동버프리스트.contains(Integer.valueOf(137)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(147)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(135)) ? "5208" : "5209");

					result.add(_자동버프리스트.contains(Integer.valueOf(151)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(155)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(163)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(171)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(175)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(176)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(136)) ? "5208" : "5209");

					result.add(_자동버프리스트.contains(Integer.valueOf(149)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(158)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(160)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(177)) ? "5208" : "5209");

					result.add(_자동버프리스트.contains(Integer.valueOf(150)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(156)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(166)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(178)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(167)) ? "5208" : "5209");
					
					result.add(_자동버프리스트.contains(Integer.valueOf(148)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(152)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(159)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(168)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(169)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(179)) ? "5208" : "5209");

					html = "ps_2";
					break;
		        case 3: // 법사
		            result.add(_자동버프리스트.contains(2) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(3) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(8) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(12) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(14) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(21) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(26) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(42) ? "5208" : "5209");

		            // 헤이스트와 그레이터 헤이스트 상호 배제 로직
		            result.add((_자동버프리스트.contains(43) && !_자동버프리스트.contains(54)) ? "5208" : "5209"); // 헤이스트
		            
		            result.add(_자동버프리스트.contains(48) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(52) ? "5208" : "5209");
		            
		         // 헤이스트와 그레이터 헤이스트 상호 배제 로직
		            result.add((_자동버프리스트.contains(54) && !_자동버프리스트.contains(43)) ? "5208" : "5209"); // 그레이터 헤이스트
		            
		            result.add(_자동버프리스트.contains(79) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(56) ? "5208" : "5209");
		            result.add(_자동버프리스트.contains(50) ? "5208" : "5209");
		            html = "ps_3";
		            break;
				case 4:
					result.add(_자동버프리스트.contains(Integer.valueOf(2)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(3)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(8)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(12)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(14)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(98)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(99)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(101)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(102)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(104)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(105)) ? "5208" : "5209");

					result.add(_자동버프리스트.contains(Integer.valueOf(106)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(107)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(109)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(110)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(111)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(234)) ? "5208" : "5209");
					html = "ps_4";
					break;
				case 5:
					result.add(_자동버프리스트.contains(Integer.valueOf(181)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(185)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(186)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(190)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(191)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(195)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(197)) ? "5208" : "5209");
					html = "ps_5";
					break;
				case 6:
					result.add(_자동버프리스트.contains(Integer.valueOf(201)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(204)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(206)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(209)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(211)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(214)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(216)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(218)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(219)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(223)) ? "5208" : "5209");
					html = "ps_6";
					break;
				case 7:
					result.add(_자동버프리스트.contains(Integer.valueOf(2)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(3)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(8)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(226)) ? "5208" : "5209");
					result.add(_자동버프리스트.contains(Integer.valueOf(231)) ? "5208" : "5209");
					html = "ps_7";
				}
				if (result.isEmpty()) {
					return;
				}
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), html, (String[]) result.toArray(new String[result.size()])));
			}

			public static void showAutoPotionList(L1PcInstance pc) {
				ArrayList<String> result = new ArrayList<String>();
				result.add(pc.is_자동물약사용() ? "5208" : "5209");
				result.add(String.valueOf(pc.get_자동물약퍼센트()));
				result.add("off");
				result.add("-1");
				result.add("5208");
				result.add("5208");
				result.add("5209");
				result.add("5209");
				result.add("5209");
				result.add("5209");
				result.add("5208");
				result.add("");
				result.add("5208");
				result.add("5208");
				result.add(pc.is_자동버프사용() ? "5208" : "5209");
				result.add(pc.is_자동버프전투시사용() ? "5208" : "5209");
				result.add(pc.is_자동버프세이프티존사용() ? "5208" : "5209");
				result.add("5209");
				if (result.isEmpty()) {
					return;
				}
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ps_u1", (String[]) result.toArray(new String[result.size()])));
			}
			public static void showrdun01(L1PcInstance pc) {
				ArrayList<String> result = new ArrayList<String>();
				result.add(pc.is_자동물약사용() ? "5208" : "5209");
				result.add(String.valueOf(pc.get_자동물약퍼센트()));
				result.add("off");
				result.add("-1");
				result.add("5208");
				result.add("5208");
				result.add("5209");
				result.add("5209");
				result.add("5209");
				result.add("5209");
				result.add("5208");
				result.add("");
				result.add("5208");
				result.add("5208");
				result.add(pc.is_자동버프사용() ? "5208" : "5209");
				result.add(pc.is_자동버프전투시사용() ? "5208" : "5209");
				result.add(pc.is_자동버프세이프티존사용() ? "5208" : "5209");
				result.add("5209");
				if (result.isEmpty()) {
					return;
				}
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ps_u1", (String[]) result.toArray(new String[result.size()])));
			}
			
			public static void showAutoRootList(L1PcInstance pc) {
				ArrayList<String> result = new ArrayList<String>();
				ArrayList<Integer> _자동루팅리스트 = pc.get_자동루팅리스트();

				result.add("");
				result.add("");
				for (java.util.Iterator<Integer> localIterator = _자동루팅리스트.iterator(); localIterator.hasNext();) {
					int itemIds = ((Integer) localIterator.next()).intValue();
					result.add(ItemTable.getInstance().findItemIdByName(itemIds));
				}
				try {
					for (int i = 0; i < 10 - _자동루팅리스트.size(); i++) {
						result.add("비어있음");
					}
					if (result.isEmpty()) {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(
						new S_NPCTalkReturn(pc.getId(), "atlootlist", (String[]) result.toArray(new String[result.size()])));
			}

			public static void showAutoResolList(L1PcInstance pc) {
				ArrayList<String> result = new ArrayList<String>();
				ArrayList<Integer> _자동용해리스트 = pc.get_자동용해리스트();

				result.add("");
				result.add("");
				for (java.util.Iterator<Integer> localIterator = _자동용해리스트.iterator(); localIterator.hasNext();) {
					int itemIds = ((Integer) localIterator.next()).intValue();
					result.add(ItemTable.getInstance().findItemIdByName(itemIds));
				}
				try {
					for (int i = 0; i < 10 - _자동용해리스트.size(); i++) {
						result.add("비어있음");
					}
					if (result.isEmpty()) {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(
						new S_NPCTalkReturn(pc.getId(), "resollist", (String[]) result.toArray(new String[result.size()])));
			}
			
//			public static void showAutoHuntList(L1PcInstance pc) {
//			    ArrayList<String> result = new ArrayList<>();
//			    
//			    // 자동 사냥 상태 확인 및 관련 정보 추가
//			    result.add(pc.isAutoHunt() ? "5208" : "5209");
//			    result.add("Map ID: " + pc.getAutoMapId());
//			    result.add("Status: " + (pc.isAutoSetting() ? "Active" : "Inactive"));
//			    
//			    // 기존 기능과 통합된 정보 추가
//			    result.add("Auto Potion: " + (pc.is_자동물약사용() ? "Enabled" : "Disabled"));
//			    result.add("Auto Buff: " + (pc.is_자동버프사용() ? "Enabled" : "Disabled"));
//			    
//			    // 필요한 경우 추가 정보
//			    result.add("Some other auto hunt-related data");
//			    
//			    if (!result.isEmpty()) {
//			        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ps_9", result.toArray(new String[0])));
//			    }
//			}

			private static void countR4(L1PcInstance pc, String count) {
				int i = 0;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;

				try {
					i = Integer.parseInt(count);
				} catch (NumberFormatException e) {
				}
				String charname = null;
				int sellcount = 0;
				int status = 0;
				String buyerName = null;
				String buyerAccountName = null;
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement(
							"SELECT  name, count, status, buyername, buyerAccountName from Auction where id Like '" + i + "'");
					rs = pstm.executeQuery();
					while (rs.next()) {
						charname = rs.getString("name");
						sellcount = rs.getInt("count");
						status = rs.getInt("status");
						buyerName = rs.getString("buyername");
						buyerAccountName = rs.getString("buyerAccountName");
					}

					if (status == 1) {
						if (pc.getName().equalsIgnoreCase(charname) || pc.isGm()) {
							AuctionSystemTable.getInstance().AuctionComplete(i);
							try {
								WriteComplete(buyerName, sellcount, charname);
//								WriteComplete("메티스", sellcount, charname); // 운영자에게 편지
//								Warehouse.present(buyerAccountName, 40308, 0, sellcount); // 아데나
//								Warehouse.present(buyerAccountName, 400254, 0, sellcount); // 1억 아데나 수표
								Warehouse.present(buyerAccountName, 3000181, 0, sellcount); // 후원 코인
							} catch (Exception e) {
								e.printStackTrace();
							}
							pc.sendPackets("정상적으로 판매 완료되었습니다.");
						} else {
							pc.sendPackets("당신은 해당 글의 판매자가 아닙니다.");
						}
					} else {
						pc.sendPackets("아직 구매 신청자가 없습니다.");
					}
				} catch (SQLException e) {
					pc.sendPackets(".판매완료(게시글번호)");
					pc.sendPackets(".판매완료 0017");
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
			}
			
			private static void countR5(L1PcInstance pc, String count) {
				int i = 0;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				try {
					i = Integer.parseInt(count);
				} catch (NumberFormatException e) {
				}
				int status = 0;
				String charname = null;
				String buyername = null;
				String buyeraccount = null;
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement(
							"SELECT  name, status, buyername, buyerAccountName from Auction where id Like '" + i + "'");
					rs = pstm.executeQuery();
					while (rs.next()) {
						charname = rs.getString("name");
						status = rs.getInt("status");
						buyeraccount = rs.getString("buyerAccountName");
						buyername = rs.getString("buyername");
					}
					if (status == 0) {
						pc.sendPackets("구매 취소가 불가능한 상태입니다.");
						return;
					}
					if (status == 2) {
						pc.sendPackets("판매가 완료된 상태입니다.");
						return;
					}
					if (pc.getAccountName().equalsIgnoreCase(buyeraccount) || pc.isGm()) {
						AuctionSystemTable.getInstance().AuctionUpdate(i, "", "", "입금대기", 0);
						WriteCancle(buyername, charname);
//						WriteCancle("메티스", charname); // 운영자에게 편지
						pc.sendPackets("정상적으로 취소되었습니다.");
					} else {
						pc.sendPackets("등록하신 물품이 아닙니다.");
					}
				} catch (SQLException e) {
					pc.sendPackets(".구매취소   [게시글번호] 를 입력하세요.");
					pc.sendPackets("예).구매취소   0035");
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
			}
			// TODO 중개 거래 게시판
			
			private static void do_lfc(L1PcInstance pc, String param){
				StringTokenizer token = new StringTokenizer(param);
				String name = token.nextToken();
				L1PcInstance target = L1World.getInstance().findpc(name);
				if(target == null){
					pc.sendPackets(String.format("%s님을 찾을 수 없습니다.", name));
				}
				L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", pc.getName()));
				MJLFCCreator.registLfc(pc, 3);
				pc.sendPackets(String.format("%s님에게 결투를 신청했습니다.", name));
				pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("%s님이 결투를 신청했습니다.", pc.getName()), bp.getId() + 1000, MJSurveyFactory.createLFCSurvey(), 30 * 1000));
			}
}

