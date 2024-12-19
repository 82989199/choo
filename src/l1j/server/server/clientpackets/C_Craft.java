package l1j.server.server.clientpackets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.IndunSystem.MiniGame.L1Gambling;
import l1j.server.IndunSystem.MiniGame.L1Gambling3;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Chat.MJNormalChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.Stadium.StadiumManager;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.UserCommands;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanBuffTable.ClanBuff;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.HelpBySupport;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanRanking;
import l1j.server.server.model.L1ClanRanking.RankData;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.Logger.ChatType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_CharStat;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_EinhasadClanBuff;
import l1j.server.server.serverpackets.S_IconMessage;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_NewChat;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_RankingClan;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SlotChange;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class C_Craft extends ClientBasePacket {

	private static final int Chat = 0x02;
	private static final int Exclude = 0x1f;
	private static final int NewStat = 228;
	private static final int SEAL = 0x39; // 봉인 아이콘
	private static final int CLAN_BUFF = 140; // 혈맹버프
	private static final int CLAN_RANKING = 146;
	private static final int 장비 = 33; // 장비스왑
	
	// TODO 혈맹 버프 리뉴얼 2017-11-12
	private static final int CLAN_BUFF_CHANGE = 0xf9;
	private static final int CLAN_BUFF_CHOICE = 0xf8;

	//private static Random _random = new Random(System.nanoTime());

	public C_Craft(byte[] data, GameClient client) throws IOException {
		super(data);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		int type = readC();

		if (type != NewStat && pc == null)
			return;

		switch (type) {
		// TODO 혈맹 버프 리뉴얼 2017-11-12
				case CLAN_BUFF_CHANGE: {
					L1Clan clan = pc.getClan();
					if(clan == null || !pc.getInventory().checkItem(40308, 300000)) return;
					pc.getInventory().consumeItem(40308, 300000);
					clan.setEinhasadBlessBuff(0);
					clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));
					clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));
					clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));
					pc.sendPackets(new S_EinhasadClanBuff(pc), true);	
					ClanTable.getInstance().updateClan(clan);
					break;
				}
				case CLAN_BUFF_CHOICE: {
					readH();
					readD();
					L1Clan clan = pc.getClan();
					if(clan == null) return;
					int buffnum = readBit();
					readC();
					/** 1번 선택 2번 이동 3번 변경 */
					int BuffCheck = readC();
					switch (BuffCheck) {
						case 1:
							clan.setEinhasadBlessBuff(buffnum);
							pc.sendPackets(new S_EinhasadClanBuff(pc), true);
							break;
						case 2:
							if (!pc.getMap().isEscapable()) {
								pc.sendPackets(new S_SystemMessage("이곳에서는 사용할수 없습니다."), true);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
								return;
							}
							ClanBuff Buff = ClanBuffTable.getBuffList(buffnum);
							if(Buff == null || !pc.getInventory().checkItem(40308, 1000)) return;
							pc.getInventory().consumeItem(40308, 1000);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
							pc.start_teleport(Buff.teleportX, Buff.teleportY, (short) Buff.teleportM, pc.getHeading(), 169, true, false);
							break;
						case 3:
							if(!pc.getInventory().checkItem(40308, 500000)) return;
							pc.getInventory().consumeItem(40308, 500000);
							clan.setEinhasadBlessBuff(buffnum);
							pc.sendPackets(new S_EinhasadClanBuff(pc), true);
							break;
					}
					ClanTable.getInstance().updateClan(clan);
					break;
				}
		case 장비:
			readH();
			readC();
			int _type = readC();
			int _slot = readC();
			if (_type == 16) {
				if (_slot == 1) { // 2번슬롯
					pc.sendPackets(new S_SlotChange(S_SlotChange.SLOT_CHANGE, _slot));
					pc.setSlotNumber(_slot);
					pc.getChangeSlot(_slot);
				} else if (_slot == 0) {
					pc.sendPackets(new S_SlotChange(S_SlotChange.SLOT_CHANGE, _slot));
					pc.setSlotNumber(_slot);
					pc.getChangeSlot(_slot);
				}
			} else if (_type == 8) {
				if (_slot == 1) { // 2번슬롯 저장
					pc.addSlotItem(_slot, 0, true);
				} else if (_slot == 0) { // 1번슬롯 저장
					pc.addSlotItem(_slot, 0, true);
				}
			}
			break;
		case CLAN_BUFF: {
			readH();
			readH();// 08
			L1Clan clan = pc.getClan();
			int buffId = read4(read_size()) - 2724;// 2724:일반공격 2725:일반방어
													// 2726:전투공격 2727:전투방어
			int consume = 300000000;
			int time = 172800;
			if (clan.getBuffTime()[buffId] != 0) {
				consume = 10000000;
				time = clan.getBuffTime()[buffId];
			}
			if (!pc.isGm() && !pc.isCrown() && !(pc.getClanRank() == 9) && !(pc.getClanRank() == 3)) {
				pc.sendPackets(new S_ServerMessage(4648));
				return;
			}
			if (pc.isGm() || clan.getBlessCount() >= consume) {
				int oldbless = clan.getBless();
				clan.setBless(buffId + 1);
				clan.setBuffTime(buffId, time);
				int[] times = clan.getBuffTime();
				ClanTable.getInstance().updateBless(clan.getClanId(), buffId + 1);
				ClanTable.getInstance().updateBuffTime(times[0], times[1], times[2], times[3], clan.getClanId());
				if (!pc.isGm()) {
					clan.setBlessCount(clan.getBlessCount() - consume);
					ClanTable.getInstance().updateBlessCount(clan.getClanId(), clan.getBlessCount());
				}
				for (L1PcInstance member : clan.getOnlineClanMember()) {
					if (oldbless != 0 && member.hasSkillEffect(504 + oldbless)) {
						member.removeSkillEffect(504 + oldbless);
						member.sendPackets(new S_ACTION_UI2(2723 + oldbless, 1, 0, 7231 + (oldbless * 2), 0));
					}
					member.sendPackets(new S_Pledge(clan, buffId + 1));
					new L1SkillUse().handleCommands(member, buffId + 505, member.getId(), member.getX(), member.getY(),
							null, time, L1SkillUse.TYPE_GMBUFF);
				}
			} else
				pc.sendPackets(new S_ServerMessage(4620));
		}
			break;
		case SEAL: // 봉인안되도록
			readH();
			readH();
		/*	pc.sendPackets("지원하지 않는 기능 입니다.");
			 break;*/
			
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(read4(read_size()));
			if (l1iteminstance1.getItem().getType2() == 0) { // etc 아이템이라면
				pc.sendPackets(new S_ServerMessage(79)); // 아무일도 일어나지 않는다 (멘트)
				return;
			}
			
			if (l1iteminstance1.getItem().isEndedTimeMessage()){
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			
			if (!pc.getInventory().checkItem(40308, 10000)) {
				pc.sendPackets("아데나 10,000원이 부족 합니다.");
				return;
			}
			
			if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 || l1iteminstance1.getBless() == 2
					|| l1iteminstance1.getBless() == 3) {
				int Bless = 0;
				switch (l1iteminstance1.getBless()) {
				case 0:
					Bless = 128;
					break; // 축
				case 1:
					Bless = 129;
					break; // 보통 case 2: Bless = 130; break; // 저주
				case 3:
					Bless = 131;
					break; // 미확인
				}
				l1iteminstance1.setBless(Bless);
				int st = 0;
				if (l1iteminstance1.isIdentified())
					st += 1;
				if (!l1iteminstance1.getItem().isTradable())
					st += 2;
				if (l1iteminstance1.getItem().isCantDelete())
					st += 4;
				if (l1iteminstance1.getItem().get_safeenchant() < 0)
					st += 8;
				if (l1iteminstance1.getBless() >= 128) {
					st = 32;
					if (l1iteminstance1.isIdentified()) {
						st += 15;
					} else {
						st += 14;
					}
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
				pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().consumeItem(40308, 100000);
				pc.sendPackets("봉인해제시 .봉인해제신청 명령어를 사용하세요.");
			} else
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
			break;
			
		case CLAN_RANKING: {
			readH();
			RankData[] allDatas = L1ClanRanking.getInstance().getRankerDatas();
			pc.sendPackets(new S_RankingClan(allDatas));
			break;
		}
		case NewStat:
			boolean isStr = false;
			boolean isInt = false;
			boolean isWis = false;
			boolean isDex = false;
			boolean isCon = false;
			boolean isCha = false;
			readC();
			int totallength = readH(); // size
			readH(); // 08 01
			readC(); // 0X10 클래스구분
			int Classtype = readC();
			if (pc != null) {
				Classtype = pc.getType();
			}
			readC(); // 0x18 초기화구분
			int value = readC(); // 0x01:최초생성,초기화 0x08:혼합,개별 0x10:보너스스탯
			// System.out.println(value);
			for (int i = 0; i < (totallength - 6) / 2; i++) {
				int charstat = readC();
				if (charstat == 0 || (charstat % 8) != 0) {
					break;
				}
				int stat = readC();
				// System.out.println(stat);
				switch (charstat) {
				case 0x30:
					boolean check = false;
					try {
						if (pc != null) {
							if (value == 0x10) {
								if (stat == pc.getAbility().getTotalStr()) {
									client.charStat[0] = stat;
									check = true;
								}
							}
						}
						if (!check) {
							client.charStat[0] = stat;
							isStr = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// if (value == 0x10 && stat ==
					// pc.getAbility().getTotalStr()) {//오류부근
					// client.charStat[0] = stat;
					// } else {
					// client.charStat[0] = stat;
					// isStr = true;
					// }
					break;
				case 0x38:
					client.charStat[1] = stat;
					isInt = true;
					break;
				case 0x40:
					client.charStat[2] = stat;
					isWis = true;
					break;
				case 0x48:
					client.charStat[3] = stat;
					isDex = true;
					break;
				case 0x50:
					if (value == 0x10 && stat == pc.getAbility().getTotalCon()) {
						client.charStat[4] = stat;
					} else {
						client.charStat[4] = stat;
						isCon = true;
					}
					break;
				case 0x58:
					client.charStat[5] = stat;
					isCha = true;
					break;
				}
			}
			if (value == 0x10 && !isStr && !isInt && !isWis && !isDex && !isCon && !isCha) {
				if (!isStr)
					isStr = true;
				if (!isCon)
					isCon = true;
			}
			if (isStr) {
				client.sendPacket(new S_CharStat(client, 1, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isStr = false;
			}
			if (isInt) {
				client.sendPacket(new S_CharStat(client, 2, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isInt = false;
			}
			if (isWis) {
				client.sendPacket(new S_CharStat(client, 3, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isWis = false;
			}
			if (isDex) {
				client.sendPacket(new S_CharStat(client, 4, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isDex = false;
			}
			if (isCon) {
				client.sendPacket(new S_CharStat(client, 5, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isCon = false;
			}
			if (isCha) {
				client.sendPacket(new S_CharStat(client, 6, Classtype, value, client.charStat[0], client.charStat[1],
						client.charStat[2], client.charStat[3], client.charStat[4]));
				isCha = false;
			}
			break;
		case Exclude: {
			readC();
			readC();
			readH();
			if (pc == null)
				return;
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
			int Type = readC(); // 0:리스트, 1:추가, 2:삭제
			if (Type == 0) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.SHOW_LIST_EXCLUDE, exList.getExcludeList(0), 0));
				pc.sendPackets(new S_PacketBox(S_PacketBox.SHOW_LIST_EXCLUDE, exList.getExcludeList(1), 1));
			} else {
				readC();
				int subType = readC();
				while (true) {
					int dummy = readC();
					if (dummy == 0 || dummy == 64)
						break;
					int enamelength = readC();
					if (enamelength == 0 || enamelength > 12)
						break;
					String charName = readS2(enamelength);
					if (charName.equalsIgnoreCase(pc.getName())) {
						pc.sendPackets(new S_SystemMessage("자기 자신은 차단 할 수 없습니다."));
						break;
					}
					if (exList.contains(subType, charName)) {
						delExclude(pc, subType, charName);
						exList.remove(subType, charName);
						pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, subType, charName));
					} else {
						for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
							if (charName.equalsIgnoreCase(cn.getName())) {
								int objId = cn.getId();
								String name = cn.getName();
								exList.add(subType, name);
								insertExclude(pc, subType, objId, name);
								pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, subType, charName));
								return;
							}
						}
					}
				}
			}
			break;
		}
		case Chat:
			try {
				if (pc != null && !pc.isGm() && pc.isGhost()) {
					pc.sendPackets(new S_SystemMessage("현재 채팅을 하실 수 없는 상태입니다."));
					return;
				}
				if(!pc.isGm() && StadiumManager.getInstance().is_on_stadium(pc.getMapId())){
					pc.sendPackets(new S_SystemMessage("현재 채팅을 하실 수 없는 상태입니다."));
					return;
				}
				readP(4);
				int chatcount = 0;
				int valueK = (int) (client.getChatCount() / 128);
				if (valueK == 0) {
					chatcount = readC();
				} else if (valueK <= 127) {
					chatcount = readKH();
				} else if (valueK <= 16383) {
					chatcount = readKCH();
				} else {
					chatcount = readK();
				}
				client.setChatCount(chatcount + 1);

				readC();
				int chatType = readC();
				readC();
				int chatlength = readC();
				BinaryOutputStream os = new BinaryOutputStream();
				for (int i = 0; i < chatlength; i++) {
					os.writeC(readC());
				}

				String chat2 = new String(os.getBytes(), "MS949");
				os.close();
				if (chatType == 1) {
					readC();
					chatlength = readC();
					os = new BinaryOutputStream();
					for (int i = 0; i < chatlength; i++) {
						os.writeC(readC());
					}
					String name = new String(os.getBytes(), "MS949");
					ChatWhisper(pc, chatType, chatcount, chat2, name);
				} else { // 일반 및 전쳇등
					Chat(pc, chatType, chatcount, chat2);
				}
				os.close();
			} catch (Exception e) {
			}
			break;
		default:
			break;
		}
	}

	private void ChatWhisper(L1PcInstance whisperFrom, int chatType, int chatcount, String text, String targetName) {
		if(MJWhisperChatFilterChain.getInstance().handle(whisperFrom, targetName, text))
			return;
		
		if(whisperFrom.getPinkNameTime() > 0){
			whisperFrom.sendPackets(new S_SystemMessage("현재 채팅을 하실 수 없는 상태입니다."));
			return;
		}
		
		if (targetName.length() > 50)
			return;
		if (text.length() > 45) {
			whisperFrom.sendPackets(new S_SystemMessage("귓말로 보낼 수 있는 글자수를 초과하였습니다."));
			return;
		}

		if (whisperFrom.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(new S_ServerMessage(242));
			return;
		}
		if (whisperFrom.getLevel() < Config.WHISPER_CHAT_LEVEL) {
			whisperFrom.sendPackets(new S_ServerMessage(404, String.valueOf(Config.WHISPER_CHAT_LEVEL)));
			return;
		}

		L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName);

		// 월드에 없는 경우
		if (whisperTo == null) {
			whisperFrom.sendPackets(new S_ServerMessage(73, targetName));
			return;
		}
		if (whisperTo.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(new S_SystemMessage("채팅금지중인 유저입니다. 답변을 받을 수 없습니다."));
//			return;
		}

		// 자기 자신에 대한 wis의 경우
		if (whisperTo.equals(whisperFrom)) {
			return;
		}

		// 차단되고 있는 경우
		if (whisperTo != null) {
			L1ExcludingList spamList2 = SpamTable.getInstance().getExcludeTable(whisperTo.getId());
			if (spamList2.contains(0, whisperFrom.getName())) {
				whisperFrom.sendPackets(new S_ServerMessage(117, whisperTo.getName()));
				return;
			}
		}

		if (!whisperTo.isCanWhisper()) {
			whisperFrom.sendPackets(new S_ServerMessage(205, whisperTo.getName()));
			return;
		}

		if (whisperFrom.getAccessLevel() == 0) {
//			if (whisperTo.getName().equalsIgnoreCase("메티스") || whisperTo.getName().equalsIgnoreCase("미소피아")) {
			if (whisperTo.getName().equalsIgnoreCase("메티스")) {
				whisperTo.sendPackets(new S_NewChat(whisperFrom, 4, chatType, text, whisperTo.getName()));
//				whisperFrom.sendPackets(new S_SystemMessage("" + whisperTo.getName() + "게임 내에서는 편지/귓말을 받지 않습니다."));
				whisperFrom.sendPackets(new S_SystemMessage("==============================================="));
				whisperFrom.sendPackets(new S_SystemMessage("후원: 홈페이지 → 충전하기 순서대로 접수해주세요."));
				whisperFrom.sendPackets(new S_SystemMessage("긴급: 홈페이지 → 실시간 채팅을 이용해주세요."));
				whisperFrom.sendPackets(new S_SystemMessage("게임 내에서는 편지/귓말을 받지 않습니다."));
				whisperFrom.sendPackets(new S_SystemMessage("==============================================="));
				return;
			}
		}
		whisperFrom.sendPackets(new S_NewChat(whisperFrom, 3, chatType, text, whisperTo.getName()));
		whisperTo.sendPackets(new S_NewChat(whisperFrom, 4, chatType, text, whisperFrom.getName()));
		LoggerInstance.getInstance().addWhisper(whisperFrom, whisperTo, text);
	}

	private void Chat(L1PcInstance pc, int chatType, int chatcount, String chatText) {
		MJCaptchaLoadManager.getInstance().do_auth_captcha(pc, chatText);
		if(Config.USE_SHIFT_SERVER && pc.is_shift_transfer()){
			if(chatType != 0){
				pc.sendPackets("현재 채팅을 하실 수 없는 상태입니다.");
				return;
			}else{
				if(!MJCommons.isLetterOrDigitString(chatText, 5, 12)){
					pc.sendPackets(String.format("%s(은)는 사용할 수 없는 계정입니다.", chatText));
					return;
				}
				
				Account account = Account.load(chatText);
				if(account != null){
					pc.sendPackets(String.format("%s(은)는 이미 존재하는 계정입니다.", chatText));
					return;
				}
				account = pc.getAccount();
				GameClient clnt = pc.getNetConnection();
				LoginController.getInstance().logout(clnt);
				clnt.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
				MJShiftObjectHelper.update_account_name(pc.getAccount(), chatText);
				MJShiftObjectHelper.update_account_name(pc, chatText);
				pc.setAccountName(chatText);
				account.setName(chatText);
				pc.getNetConnection().setAccount(account);
				try {
					LoginController.getInstance().login(pc.getNetConnection(), pc.getAccount());
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(String.format("%s(으)로 계정이 변경되었습니다.", chatText));
				pc.start_teleportForGM(33443 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 32797 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 4, pc.getHeading(), 169, true, true);
				int locx = 32723 + CommonUtil.random(10);
				int locy = 32851 + CommonUtil.random(10);
				pc.start_teleport(locx, locy, 5166, 5, 169, false, false);
				pc.sendPackets(new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, true));
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "캐릭명 변경창으로 이동합니다 잠시만 기다려 주세요..."));
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						pc.getNetConnection().setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
						pc.sendPackets(S_ChangeCharName.getChangedStart());
						int[] loc = null;
						loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
						pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, true);
					}
				}, 2500L);
				return;
			}
		}
		if(pc.is_ready_server_shift()){
			do_shift_server(pc, chatText);
			pc.set_ready_server_shift(false);
			return;
		}

		if (pc.hasSkillEffect(L1SkillId.SILENCE) || pc.hasSkillEffect(L1SkillId.AREA_OF_SILENCE) || pc.hasSkillEffect(L1SkillId.CONFUSION)
				|| pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE)) {
			return;
		}
		if (pc.hasSkillEffect(1005)) { // 채팅 금지중
			pc.sendPackets(new S_ServerMessage(242)); // 현재 채팅 금지중입니다.
			return;
		}
		if (pc.getMapId() == 631 && !pc.isGm()) {
			pc.sendPackets(new S_ServerMessage(912)); // 채팅을 할 수 없습니다.
			return;
		}

		if (pc.isDeathMatch() && !pc.isGm() && !pc.isGhost()) {
			pc.sendPackets(new S_SystemMessage("데스매치 경기중에는 채팅이 금지됩니다.")); // 현재채팅 금지중입니다.
			return;
		}
		/** 배틀존 **/
		if (!pc.isGm() && pc.getMapId() == 5153) {
			if (chatType != 0) {
				pc.sendPackets(new S_SystemMessage("프리미엄 배틀존 진행중에는 일반채팅만 가능합니다."));
				return;
			}
		}

		switch (chatType) {
		case 0: {
			if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
				return;
			}
			
			// GM커멘드
			if (chatText.startsWith(".") && (pc.getAccessLevel() == Config.GMCODE || pc.isMonitor() || pc.getAccessLevel() == Config.SUB_GMCODE)) {// +
				String cmd = chatText.substring(1);
				GMCommands.getInstance().handleCommands(pc, cmd);
				return;
			}
			if (chatText.startsWith(".")) {
				String cmd = chatText.substring(1);
				UserCommands.getInstance().handleCommands(pc, cmd);
				return;
			}

			if(MJNormalChatFilterChain.getInstance().handle(pc, chatText)){
				return;
			}
			
			if (pc.isSupporting()) {
				new HelpBySupport(pc, 0).npctalk3(chatText);
				if (chatText.startsWith("속이 기")) {
					return;
				}
			}

			int temporaryItemObjectId = pc.getTemporaryItemObjectId();
			if (temporaryItemObjectId > 0) {
				L1Object obj = L1World.getInstance().findObject(temporaryItemObjectId);
				if (obj.instanceOf(MJL1Type.L1TYPE_ITEMINSTANCE)) {
					L1ItemInstance item = (L1ItemInstance) obj;
					int itemId = item.getItemId();
					if (itemId == 700085 || itemId == 700086) {
						if (MJSurveyFactory.isMegaphoneSpeaking) {
							pc.sendPackets("이미 확성기 메시지가 재생되고 있습니다. 잠시 후 다시 이용해주세요.");
							pc.clearTemporaryItemObjectId();
							return;
						}

						pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(
								String.format("입력하신 문자가 : \"%s\"입니다. 이대로 전송하시겠습니까?", chatText), temporaryItemObjectId,
								MJSurveyFactory.createMegaphoneSurvey(temporaryItemObjectId, chatText,
										itemId == 700085 ? 20 : 40),
								10000L));

						pc.clearTemporaryItemObjectId();
						return;
					}
				}
			}
			
			L1Gambling gam = new L1Gambling();
			if (pc.isGambling()) {
				if (chatText.startsWith("홀")) {
					gam.Gambling2(pc, chatText, 1);
					return;
				} else if (chatText.startsWith("짝")) {
					gam.Gambling2(pc, chatText, 2);
					return;
				} else if (chatText.startsWith("1")) {
					gam.Gambling2(pc, chatText, 3);
					return;
				} else if (chatText.startsWith("2")) {
					gam.Gambling2(pc, chatText, 4);
					return;
				} else if (chatText.startsWith("3")) {
					gam.Gambling2(pc, chatText, 5);
					return;
				} else if (chatText.startsWith("4")) {
					gam.Gambling2(pc, chatText, 6);
					return;
				} else if (chatText.startsWith("5")) {
					gam.Gambling2(pc, chatText, 7);
					return;
				} else if (chatText.startsWith("6")) {
					gam.Gambling2(pc, chatText, 8);
					return;
				}
			}
			if (pc.isGambling3()) {
				L1Gambling3 gam1 = new L1Gambling3();
				if (chatText.startsWith("오크전사")) {
					gam1.Gambling3(pc, chatText, 1);
					return;
				} else if (chatText.startsWith("스파토이")) {
					gam1.Gambling3(pc, chatText, 2);
					return;
				} else if (chatText.startsWith("멧돼지")) {
					gam1.Gambling3(pc, chatText, 3);
					return;
				} else if (chatText.startsWith("슬라임")) {
					gam1.Gambling3(pc, chatText, 4);
					return;
				} else if (chatText.startsWith("해골")) {
					gam1.Gambling3(pc, chatText, 5);
					return;
				} else if (chatText.startsWith("늑대인간")) {
					gam1.Gambling3(pc, chatText, 6);
					return;
				} else if (chatText.startsWith("버그베어")) {
					gam1.Gambling3(pc, chatText, 7);
					return;
				} else if (chatText.startsWith("장로")) {
					gam1.Gambling3(pc, chatText, 8);
					return;
				} else if (chatText.startsWith("괴물눈")) {
					gam1.Gambling3(pc, chatText, 9);
					return;
				}
			}

			if(pc.is_combat_field())
				return;

//			if(pc.getPinkNameTime() > 0){
//				pc.sendPackets(new S_SystemMessage("정당방위 상태에서는 몇초간 채팅이 불가 합니다."));
//				return;
//			}//보라일때채팅금지
			
			pc.sendPackets(new S_NewChat(pc, 3, chatType, chatText, ""));
			S_NewChat s_chatpacket = new S_NewChat(pc, 4, chatType, chatText, "");
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (!spamList.contains(0, pc.getName())) {
				pc.sendPackets(s_chatpacket);
			}

			for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
				if(listner.getMapId() == 5554 && !listner.isGm() && !pc.isGm())
					continue;
				
				if(!listner.isOutsideChat() && !pc.isGm()){
					L1Buddy buddyList = BuddyTable.getInstance().getBuddyTable(listner.getId());
					L1Party party = listner.getParty();
					L1ChatParty cparty = listner.getChatParty();
					
					if(!(buddyList.containsId(pc.getId()) || (listner.getClanid() > 0 && listner.getClanid() == pc.getClanid()) || (party != null && party.isMember(pc)) || (cparty != null && cparty.isMember(pc))))
						continue;
				}
				
				L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
				if (!spamList3.contains(0, pc.getName())) {
					listner.sendPackets(s_chatpacket);
				}
			}
			// 돕펠 처리
			L1MonsterInstance mob = null;
			for (L1Object obj : pc.getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
						mob.broadcastPacket(new S_NpcChatPacket(mob, chatText, 0));
					}
				}
			}
		}
		if (pc.getLevel() >= Config.LIMITLEVEL && !pc.isGm()) {// 경험치
			Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
			pc.sendPackets(new S_SystemMessage(pc.getName() + " 를 계정압류 하였습니다."));
			pc.sendPackets(new S_Disconnect());
			
			if (pc.getOnlineStatus() == 1) {
				pc.sendPackets(new S_Disconnect());
			}
			System.out.println("▶ 컨피그레벨버그 일반채팅으로 [압류] : "+ pc.getName());
		}
			// manager.LogChatNormalAppend("[일반]", pc.getName(), chatText);
			// CodesManager.getInstance().NomalchatAppend(pc.getName(),
			// chatText);//일반채팅
			/** 파일로그저장 **/
			// ChatLogTable.getInstance().storeChat(pc, null, chatText,
			// chatType);//DB에 저장
		ChatLogTable.getInstance().storeChat(pc, null, chatText, chatType);
		LoggerInstance.getInstance().addChat(ChatType.Normal, pc, chatText);
			break;

		case 3: {
			if(MJWorldChatFilterChain.getInstance().handle(pc, chatText)){
				return;
			}
			
			if(pc.getPinkNameTime() > 0){
				pc.sendPackets(new S_SystemMessage("현재 채팅을 하실 수 없는 상태입니다."));
				return;
			}
			
			chatWorld(pc, chatType, chatcount, chatText);
			/** 파일로그저장 **/
			// ChatLogTable.getInstance().storeChat(pc, null, chatText,
			// chatType);
		}
			break;
		case 4: {
			if (pc.getClanid() != 0) { // 크란 소속중
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					S_NewChat s_chatpacket1 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(ChatType.Clan, pc, chatText);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						L1ExcludingList spamList4 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList4.contains(0, pc.getName())) {
							listner.sendPackets(s_chatpacket1);
						}
					}
				}
			}
		}
			/** 파일로그저장 **/
			break;
		case 11: {
			if (pc.isInParty()) { // 파티중
				S_NewChat s_chatpacket2 = new S_NewChat(pc, 4, chatType, chatText, "");
				LoggerInstance.getInstance().addChat(ChatType.Party, pc, chatText);
				for (L1PcInstance listner : pc.getParty().getMembers()) {
					L1ExcludingList spamList11 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList11.contains(0, pc.getName())) {
						listner.sendPackets(s_chatpacket2);
					}
				}
			}
		}
			break;
		case 12:
			if (pc.isGm())
				chatWorld(pc, chatType, chatcount, chatText);
			else{
				if(MJWorldChatFilterChain.getInstance().handle(pc, chatText)){
					return;
				}
				
				if(pc.getPinkNameTime() > 0){
					pc.sendPackets(new S_SystemMessage("현재 채팅을 하실 수 없는 상태입니다."));
					return;
				}
				chatWorld(pc, 12, chatcount, chatText);
			}
			break;
		case 13: { // 연합 채팅
			if (pc.getClanid() != 0) { // 크란 소속중
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				int rank = pc.getClanRank();
				if (clan != null && (rank == L1Clan.군주 || (rank == L1Clan.수호))) {
					S_NewChat s_chatpacket3 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(ChatType.Alliance, pc, chatText);
					
					/** 파일로그저장 **/
					// ChatLogTable.getInstance().storeChat(pc, null, chatText,
					// chatType);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						int listnerRank = listner.getClanRank();
						L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList13.contains(0, pc.getName())
								&& (listnerRank == L1Clan.군주 || (listnerRank == L1Clan.수호))) {
							listner.sendPackets(s_chatpacket3);
						}
					}
				}
			}
		}
			break;
		case 14: { // 채팅 파티
			if (pc.isInChatParty()) { // 채팅 파티중
				S_NewChat s_chatpacket4 = new S_NewChat(pc, 4, chatType, chatText, "");
				LoggerInstance.getInstance().addChat(ChatType.Party, pc, chatText);
				/** 파일로그저장 **/
				// ChatLogTable.getInstance().storeChat(pc, null, chatText,
				// chatType);
				for (L1PcInstance listner : pc.getChatParty().getMembers()) {
					L1ExcludingList spamList14 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList14.contains(0, pc.getName())) {
						listner.sendPackets(s_chatpacket4);
					}
				}
			}
		}
			break;

		case 17:
			if (pc.getClanid() != 0) { // 혈맹 소속중
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null && (pc.isCrown() && pc.getId() == clan.getLeaderId())) {
					S_NewChat s_chatpacket5 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(ChatType.Guardian, pc, chatText);
					
					/** 파일로그저장 **/
					// ChatLogTable.getInstance().storeChat(pc, null, chatText,
					// chatType);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						L1ExcludingList spamList17 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList17.contains(0, pc.getName())) {
							listner.sendPackets(s_chatpacket5);
						}
					}
				}
			}
			break;
		}
		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
	}

	private void chatWorld(L1PcInstance pc, int chatType, int chatcount, String text) {
		try {
			if (pc.getLevel() >= Config.LIMITLEVEL && !pc.isGm()) {// 경험치
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(new S_SystemMessage(pc.getName() + " 를 계정압류 하였습니다."));
				pc.sendPackets(new S_Disconnect());
				
				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect());
				}
				System.out.println("▶ 컨피그레벨버그 월드채팅으로 [압류] : "+ pc.getName());
			}
			if (pc.isGm() || pc.getAccessLevel() == 1) {
				if (chatType == 3) {
					//L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 3, "\\aD" + text, "[******] "));
					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aD[******] " + text));
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aD" + text));
					LoggerInstance.getInstance().addChat(ChatType.Global, pc, "\\aD" + text);
				} else if (chatType == 12) {
					L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 12, "\\aD" + text, "[******] "));
					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aD[******] " + text));
					//L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 3, "\\aD" + text, "[******] "));
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aD" + text));
//					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, text));
//					L1World.getInstance().broadcastPacketToAll(S_NotificationMessage.getGmMessage(text));
					L1World.getInstance().broadcastPacketToAll(S_IconMessage.getGmMessage(text));
				}
			} else if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
				if (L1World.getInstance().isWorldChatElabled()) {
					if (pc.get_food() >= 12) { // 5%겟지?
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						if (chatType == 12) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						} else if (chatType == 3) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
							LoggerInstance.getInstance().addChat(ChatType.Global, pc, text);
						}
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
							L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
							if (!spamList15.contains(0, pc.getName())) {
								if (listner.isShowTradeChat() && chatType == 12) {
									listner.sendPackets(new S_NewChat(pc, 4, chatType, text, ""));
									listner.sendPackets(new S_NewChat(pc, 4, 3, text, ""));
								} else if (listner.isShowWorldChat() && chatType == 3) {
									listner.sendPackets(new S_NewChat(pc, 4, chatType, text, ""));
								}
							}
						}

					} else {
						pc.sendPackets(new S_ServerMessage(462));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(510));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(195, String.valueOf(Config.GLOBAL_CHAT_LEVEL)));
			}
		} catch (Exception e) {
		}
	}

	private void insertExclude(L1PcInstance pc, int subType, int objId, String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO character_exclude SET char_id=?, type=?, exclude_id=?, exclude_name=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, subType);
			pstm.setInt(3, objId);
			pstm.setString(4, name);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void delExclude(L1PcInstance pc, int subType, String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_exclude WHERE char_id=? AND type=? AND exclude_name=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, subType);
			pstm.setString(3, name);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	class dollment implements Runnable {
		// private L1LineageClient client = null;
		String ment = null;

		public dollment(String _ment) {
			// client = _client;
			ment = _ment;
		}

		@Override
		public void run() {
			try {
				// L1World.getInstance().broadcastPacketToAll(new
				// S_ServerMessage(4433, ment, ment));
				L1World.getInstance()
						.broadcastPacketToAll(new S_SystemMessage("누군가가 " + ment + " 합성에 성공 하였습니다."));
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 " + ment + " 합성에 성공 하였습니다."));
				// System.out.println(1313);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getType() {
		return "[C] C_Craft";
	}
	
	private void do_shift_server(L1PcInstance pc, String chatText){
		if(!pc.getInventory().checkItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid())){
			pc.sendPackets("인벤토리에서 서버이전권을 찾을 수 없어 이전 작업을 취소합니다.");
			return;
		}

		try{
			List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true);
			if(servers == null || servers.size() <= 0){
				pc.sendPackets("현재 이동할 수 있는 서버가 없습니다.");
				return;
			}
			CommonServerInfo select_server_info = null;
			for(CommonServerInfo csInfo : servers){
				if(csInfo.server_description.equals(chatText)){
					select_server_info = csInfo;
					break;
				}
			}
			if(select_server_info == null){
				pc.sendPackets(String.format("%s을(를) 찾을 수 없습니다.", chatText));
				return;
			}
			if(!select_server_info.server_is_on){
				pc.sendPackets(String.format("%s 이전 불가능(서버 OFF)", chatText));
				return;
			}
			if(!select_server_info.server_is_transfer){
				pc.sendPackets(String.format("%s 이전 불가능(기능 닫힘)", chatText));
				return;
			}

			if(!pc.getInventory().consumeItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid(), 1))
				return;
			MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, select_server_info.server_identity);
			System.out.println(String.format("%s님이 서버 변경권(%s)을 사용했습니다.", pc.getName(), select_server_info.server_description));
			return;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
