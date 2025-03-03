package l1j.server.server.clientpackets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_CharCreateStatus;
import l1j.server.server.serverpackets.S_NewCharPacket;
import l1j.server.server.templates.L1Skills;

public class C_CreateChar extends ClientBasePacket {
	private static Logger _log = Logger.getLogger(C_CreateChar.class.getName());
	private static final String C_CREATE_CHAR = "[C] C_CreateChar";

	public C_CreateChar(byte[] abyte0, GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = new L1PcInstance();

		String name = readS();

		Calendar cal = Calendar.getInstance();
		int 시간 = Calendar.HOUR;
		int 분 = Calendar.MINUTE;
		/** 0 오전 , 1 오후 * */
		String 오전오후 = "오후";
		if (cal.get(Calendar.AM_PM) == 0) {
			오전오후 = "오전";
		}

		// TODO 버그차단
		if (name.length() > 20) {
			System.out.println("■ 패킷공격 ID ■ :" + name);
			System.out.println("■ 패킷공격 IP ■ :" + client.getIp());
			client.kick();
			return;
		}

		if (name.length() <= 0) { // 한글자 아이디 안되게 픽스
			// 영어는 한글자당 1의 길이, 한글은 한글자당 2의 길이
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
			return;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			// _log.info("생성 금지된 캐릭터 이름, 생성실패");
			client.sendPacket(s_charcreatestatus);
			return;
		}
		if (MJBotNameLoader.isAlreadyName(name)) {
			_log.fine("charname: " + pc.getName() + " already exists. creation failed.");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
			client.sendPacket(s_charcreatestatus1);
			return;
		}
		if (isInvalidName(name)) {
			S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
			client.sendPacket(s_charcreatestatus);
			return;
		}

		if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
			_log.fine("charname: " + pc.getName() + " already exists. creation failed.");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
			client.sendPacket(s_charcreatestatus1);
			return;
		}

		if (client.getAccount().countCharacters() >= 8) {
			// _log.fine("account: " + client.getAccountName() + " 8를 넘는 캐릭터 작성
			// 요구. ");
			S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
			client.sendPacket(s_charcreatestatus1);
			return;
		}

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || // 한문자(char)단위로
																													// 비교.
					name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || // 한문자(char)단위로
																														// 비교
					name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || // 한문자(char)단위로
																														// 비교
					name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || // 한문자(char)단위로
																														// 비교.
					name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
				S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
				client.sendPacket(s_charcreatestatus);
				return;
			}
		}
		
		pc.setName(name);
		pc.setType(readC());
		pc.set_sex(readC());
		pc.getAbility().setBaseStr((byte) readC());
		pc.getAbility().setBaseDex((byte) readC());
		pc.getAbility().setBaseCon((byte) readC());
		pc.getAbility().setBaseWis((byte) readC());
		pc.getAbility().setBaseCha((byte) readC());
		pc.getAbility().setBaseInt((byte) readC());

		int statusAmount = pc.getAbility().getAmount();

		if (pc.getAbility().getBaseStr() > 20 || pc.getAbility().getBaseDex() > 20 || pc.getAbility().getBaseCon() > 20
				|| pc.getAbility().getBaseWis() > 20 || pc.getAbility().getBaseCha() > 20
				|| pc.getAbility().getBaseInt() > 20 || statusAmount != 75) {
			_log.finest("Character have wrong value");
			S_CharCreateStatus s_charcreatestatus3 = new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
			client.sendPacket(s_charcreatestatus3);
			return;
		}

		_log.fine("charname: " + pc.getName() + " classId: " + pc.getClassId());
		S_CharCreateStatus s_charcreatestatus2 = new S_CharCreateStatus(S_CharCreateStatus.REASON_OK);
		client.sendPacket(s_charcreatestatus2);
		initNewChar(client, pc);
		// System.out.println("" + 오전오후 + " " + cal.get(시간) + "시" + cal.get(분) +
		// "분" + " ■ 신규 캐릭: [" + pc.getName() + "]님 생성완료 ■");
		System.out.println("케릭터 생성: [" + pc.getName() + "] 시간: [" + 오전오후 + " " + cal.get(시간) + "시" + cal.get(분) + "분" + "] 생성");
	}
				//TODO 서버 처음캐릭 생성시 시작 위치
	public static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
	public static final int[][] START_LOC_X = new int[][] { { 32731, 32733, 32731, 32728, 32730 } };// 리뉴얼 숨계 통합 X
	public static final int[][] START_LOC_Y = new int[][] { { 32811, 32812, 32814, 32812, 32809 } };// 리뉴얼 숨계 통합 Y
	public static final short[] MAPID_LIST = new short[] { 3, 3, 3, 3, 3, 3, 3, 3 };// 리뉴얼 숨계
	
	//TODO 서버 처음캐릭 생성시 시작 위치 말섬
//	public static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
//	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
//	public static final int[][] START_LOC_X = new int[][] { { 32581, 32575, 32572, 32562, 32559 } };// 리뉴얼 숨계 통합 X
//	public static final int[][] START_LOC_Y = new int[][] { { 32932, 32937, 32944, 32950, 32957 } };// 리뉴얼 숨계 통합 Y
//	public static final short[] MAPID_LIST = new short[] { 0, 0, 0, 0, 0, 0, 0, 0 };// 리뉴얼 숨계
	
	//TODO 기란마을탄생
//	public static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
//	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
//	public static final int[][] START_LOC_X = new int[][] { { 33443, 33445, 33444, 33436, 33441 } };// 리뉴얼 숨계 통합 X
//	public static final int[][] START_LOC_Y = new int[][] { { 32799, 32800, 32796, 32800, 32806 } };// 리뉴얼 숨계 통합 Y
//	public static final short[] MAPID_LIST = new short[] { 4, 4, 4, 4, 4, 4, 4, 4 };// 리뉴얼 숨계

	//TODO 캐릭생성시 동영상 출력
	/*public static final int[] MALE_LIST = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
	public static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
	public static final int[][] START_LOC_X = new int[][] { { 32780, 32781, 32781, 32782, 32779 } };// 리뉴얼
	public static final int[][] START_LOC_Y = new int[][] { { 32818, 32818, 32817, 32817, 32816 } };// 리뉴얼
	public static final short[] MAPID_LIST = new short[] { 7783, 7783, 7783, 7783, 7783, 7783, 7783, 7783 };// 리뉴얼
*/
	private static void initNewChar(GameClient client, L1PcInstance pc) throws IOException, Exception {
		short init_hp = 0, init_mp = 0;
		Random random = new Random();
		final int NewHi = 0; // 숨계

		int startPosType = NewHi; // defalut
		int startPos = random.nextInt(5);

		pc.setId(IdFactory.getInstance().nextId());

		if (pc.get_sex() == 0)
			pc.setClassId(MALE_LIST[pc.getType()]);
		else
			pc.setClassId(FEMALE_LIST[pc.getType()]);

		if (pc.isCrown()) { // CROWN
			init_hp = 14;
			switch (pc.getAbility().getBaseWis()) {
			case 11:
				init_mp = 2;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				init_mp = 3;
				break;
			case 16:
			case 17:
			case 18:
				init_mp = 4;
				break;
			default:
				init_mp = 2;
				break;
			}
			startPosType = NewHi;
		} else if (pc.isKnight()) { // KNIGHT
			init_hp = 16;
			switch (pc.getAbility().getBaseWis()) {
			case 9:
			case 10:
			case 11:
				init_mp = 1;
				break;
			case 12:
			case 13:
				init_mp = 2;
				break;
			default:
				init_mp = 1;
				break;
			}
			startPosType = NewHi;
		} else if (pc.isElf()) { // ELF
			init_hp = 15;
			switch (pc.getAbility().getBaseWis()) {
			case 12:
			case 13:
			case 14:
			case 15:
				init_mp = 4;
				break;
			case 16:
			case 17:
			case 18:
				init_mp = 6;
				break;
			default:
				init_mp = 4;
				break;
			}
			startPosType = NewHi;
		} else if (pc.isWizard()) { // WIZ
			init_hp = 12;
			switch (pc.getAbility().getBaseWis()) {
			case 12:
			case 13:
			case 14:
			case 15:
				init_mp = 6;
				break;
			case 16:
			case 17:
			case 18:
				init_mp = 8;
				break;
			default:
				init_mp = 6;
				break;
			}
			startPosType = NewHi;
		} else if (pc.isDarkelf()) { // DE
			init_hp = 12;
			switch (pc.getAbility().getBaseWis()) {
			case 10:
			case 11:
				init_mp = 3;
				break;
			case 12:
			case 13:
			case 14:
			case 15:
				init_mp = 4;
				break;
			case 16:
			case 17:
			case 18:
				init_mp = 6;
				break;
			default:
				init_mp = 3;
				break;
			}
			startPosType = NewHi;
		} else if (pc.isDragonknight()) { // 용기사
			init_hp = 16;
			init_mp = 2;
			startPosType = NewHi;
		} else if (pc.isBlackwizard()) { // 환술사
			init_hp = 14;
			switch (pc.getAbility().getBaseWis()) {
			case 12:
			case 13:
			case 14:
			case 15:
				init_mp = 5;
				break;
			case 16:
			case 17:
			case 18:
				init_mp = 6;
				break;
			default:
				init_mp = 5;
				break;
			}
			startPosType = NewHi;
		} else if (pc.is전사()) {
			init_hp = 16;
			switch (pc.getAbility().getBaseWis()) {
			case 9:
			case 10:
			case 11:
				init_mp = 1;
				break;
			case 12:
			case 13:
				init_mp = 2;
				break;
			default:
				init_mp = 1;
				break;
			}
			startPosType = NewHi;
		}
		
		pc.setX(START_LOC_X[startPosType][startPos]);
		pc.setY(START_LOC_Y[startPosType][startPos]);
		pc.setMap(MAPID_LIST[pc.getType()]);

		pc.setHeading(0);
		pc.setLawful(0);

		pc.addBaseMaxHp(init_hp);
		pc.setCurrentHp(init_hp);
		pc.addBaseMaxMp(init_mp);
		pc.setCurrentMp(init_mp);
		pc.resetBaseAc();
		pc.setTitle(Config.GAME_SERVER_NAME);
		pc.setClanid(0);
		pc.setClanRank(0);
		pc.set_food(39); // 17%
		pc.setAccessLevel((short) 0);
		pc.setGm(false);
		pc.setMonitor(false);
		pc.setGmInvis(false);
		pc.setExp(0);// 경험치 0
		pc.setHighLevel(1);
		pc.setStatus(0);
		pc.setAccessLevel((short) 0);
		pc.setClanname("");
		pc.setClanMemberNotes("");
		pc.setBonusStats(0);
		pc.setElixirStats(0);
		pc.resetBaseMr();
		pc.setElfAttr(0);
		pc.set_PKcount(0);
		pc.setExpRes(0);
		pc.setPartnerId(0);
		pc.setOnlineStatus(0);
		pc.setHomeTownId(0);
		pc.setContribution(0);
		pc.setBanned(false);
		pc.setKarma(0);
		pc.setReturnStat(0);
		pc.setMark_count(60);
		Calendar local_c = Calendar.getInstance();
		SimpleDateFormat local_sdf = new SimpleDateFormat("yyyyMMdd");
		local_c.setTimeInMillis(System.currentTimeMillis());
		pc.setBirthDay(Integer.parseInt(local_sdf.format(local_c.getTime())));
		if (pc.isWizard()) { // WIZ
			pc.sendPackets(new S_AddSkill(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0));
			int object_id = pc.getId();
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(4); // EB
			String skill_name = l1skills.getName();
			int skill_id = l1skills.getSkillId();
			SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB에
																							// 등록
		}
		if (pc.isElf()) { // 요정 텔리포터투마더 캐릭생성시 추가
			pc.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, pc.getElfAttr()));
			int object_id = pc.getId();
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(131); // 텔레포투마더
			String skill_name = l1skills.getName();
			int skill_id = l1skills.getSkillId();
			SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0);
		}
		Beginner.getInstance().GiveItem(pc);
		Beginner.getInstance().writeBookmark(pc);
		pc.setAccountName(client.getAccountName());
		CharacterTable.getInstance().storeNewCharacter(pc);
		S_NewCharPacket s_newcharpacket = new S_NewCharPacket(pc);
		client.sendPacket(s_newcharpacket);
		pc.save();
		pc.refresh();
	}

	private static boolean isAlphaNumeric(String s) {
		if (s == null) {
			return false;
		}
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}

	private static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("EUC-KR").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		// XXX - 본청의 사양과 동등한가 미확인
		// 전각 문자가 5 문자를 넘는지, 전체로 12바이트를 넘으면(자) 무효인 이름으로 한다
		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}

	@Override
	public String getType() {
		return C_CREATE_CHAR;
	}
}
