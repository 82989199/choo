package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;

import java.util.Arrays;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.RepeatTask;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DollOnOff;
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Npc;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	// TODO 1단계 마법인형
	public static final int DOLLTYPE_STONEGOLEM = 1; // 돌골렘
	public static final int DOLLTYPE_WAREWOLF = 2; // 늑대인간
	public static final int DOLLTYPE_BUGBEAR = 3; // 버그베어
	public static final int DOLLTYPE_CRUSTACEA = 4; // 크러스트 시안
	public static final int DOLLTYPE_SNOWMAN = 5; // 에티
	public static final int DOLLTYPE_MOKAK = 6; // 목각

	// TODO 2단계 마법인형
	public static final int DOLLTYPE_SUCCUBUS = 7; // 서큐버스
	public static final int DOLLTYPE_ELDER = 8; // 장로
	public static final int DOLLTYPE_COCA = 9; // 코카
	public static final int DOLLTYPE_SNOWMAN_NEW = 10; // 눈사람
	public static final int DOLLTYPE_SNOWMAN_A = 11; // 눈사람(A)
	public static final int DOLLTYPE_SNOWMAN_B = 12; // 눈사람(B)
	public static final int DOLLTYPE_SNOWMAN_C = 13; // 눈사람(C)
	public static final int DOLLTYPE_인어 = 14; // 인어
	public static final int DOLLTYPE_LAVAGOLREM = 15; // 라바 골렘

	// TODO 3단계 마법인형
	public static final int DOLLTYPE_자이언트 = 16; // 자이언트
	public static final int DOLLTYPE_흑장로 = 17; // 흑장로
	public static final int DOLLTYPE_SUCCUBUSQUEEN = 18; // 서큐버스 퀸
	public static final int DOLLTYPE_DRAKE = 19;// 드레이크
	public static final int DOLLTYPE_킹버그베어 = 20; // 킹 버그베어
	public static final int DOLLTYPE_DIAMONDGOLREM = 21; // 다이아몬드 골렘

	// TODO 4단계 마법인형
	public static final int DOLLTYPE_LICH = 22; // 리치
	public static final int DOLLTYPE_사이클롭스 = 23; // 사이클롭스
	public static final int DOLLTYPE_NIGHTBALD = 24; // 나이트발드
	public static final int DOLLTYPE_SIER = 25; // 시어
	public static final int DOLL_Iris = 26; // 아이리스
	public static final int DOLL_vampire = 27; // 뱀파이어
	public static final int DOLL_mummy = 28; // 머미로드

	// TODO 5단계 마법인형
	public static final int DOLLTYPE_DEMON = 29; // 데몬
	public static final int DOLLTYPE_DEATHNIGHT = 30; // 데스나이트
	public static final int DOLL_barranca = 31; // 바란카
	public static final int DOLL_corruption = 32; // 타락
	public static final int DOLL_Baphomet = 33; // 바포메트
	public static final int DOLL_icequeen = 34; // 얼음여왕
	public static final int DOLL_Kouts = 35; // 커츠

	// TODO 기타 마법인형
	public static final int DOLLTYPE_SEADANCER = 36; // 시댄서
	public static final int DOLLTYPE_SKELETON = 37; // 스파토이
	public static final int DOLLTYPE_SCARECROW = 38; // 허수아비
	public static final int DOLLTYPE_ETHYNE = 39; // 에틴
	public static final int DOLLTYPE_PIXIE_BLAG = 40; // 블레그
	public static final int DOLLTYPE_PIXIE_LESDAG = 41; // 레더그
	public static final int DOLLTYPE_PIXIE_ELREGEU = 42; // 엘레그
	public static final int DOLLTYPE_PIXIE_GREG = 43; // 그레그
	public static final int DOLLTYPE_HATCHLING = 44; // 해츨링

	// TODO 이벤트 마법인형
	public static final int DOLLTYPE_PSY_CHAMPION = 45; // 싸이
	public static final int DOLLTYPE_PSY_BIRD = 46; // 싸이
	public static final int DOLLTYPE_PSY_GANGNAM_STYLE = 47; // 싸이
	public static final int DOLLTYPE_GREMLIN = 48; // 그렘린
	public static final int DOLLTYPE_COBO = 49; // 다이노스
	public static final int 수련자마법인형 = 50; // 스파토이 수련자
	public static final int ruler1 = 51; // 지배자의 현신 (1등급)
	public static final int ruler2 = 52; // 지배자의 현신 (2등급)
	public static final int ruler3 = 53; // 지배자의 현신 (3등급)
	public static final int ruler4 = 54; // 지배자의 현신 (4등급)
	public static final int ruler5 = 55; // 지배자의 현신 (1등급) 1일
	public static final int ruler6 = 56; // 지배자의 현신 (2등급) 1일
	public static final int ruler7 = 57; // 지배자의 현신 (3등급) 1일
	public static final int ruler8 = 58; // 지배자의 현신 (4등급) 1일
	public static final int Antaras = 59; // 마법인형: 안타라스
	public static final int Papou = 60; // 마법인형: 파푸리온
	public static final int Lynd = 61; // 마법인형: 린드비오르
	public static final int Valakas = 62; // 마법인형: 발라카스
	public static final int jindeathknight = 63; // 마법인형: 진 데스나이트

/////
	public static final int DOLLTYPE_축서큐퀸 = 64;
	public static final int DOLLTYPE_축흑장로 = 65;
	public static final int DOLLTYPE_축자이언트 = 66;
	public static final int DOLLTYPE_축드레이크 = 67;
	public static final int DOLLTYPE_축킹버그 = 68;
	public static final int DOLLTYPE_축다이아골렘 = 69;
	public static final int DOLLTYPE_축사이클롭스 = 70;
	public static final int DOLLTYPE_축리치 = 71;
	public static final int DOLLTYPE_축나발 = 72;
	public static final int DOLLTYPE_축시어 = 73;
	public static final int DOLLTYPE_축아이리스 = 74;
	public static final int DOLLTYPE_축뱀파 = 75;
	public static final int DOLLTYPE_축머미 = 76;
	public static final int DOLLTYPE_축데몬 = 77;
	public static final int DOLLTYPE_축데스 = 78;
	public static final int DOLLTYPE_축바란카 = 79;
	public static final int DOLLTYPE_축타락 = 80;
	public static final int DOLLTYPE_축바포 = 81;
	public static final int DOLLTYPE_축얼녀 = 82;
	public static final int DOLLTYPE_축커츠 = 83;
	public static final int DOLLTYPE_할파스 = 84;
	public static final int DOLLTYPE_아우라키아 = 85;
	public static final int DOLLTYPE_베히모스 = 86;
	public static final int DOLLTYPE_기르타스 = 87;
	public static final int DOLLTYPE_알비노데몬 = 88;
	public static final int DOLLTYPE_알비노피닉스 = 89;
	public static final int DOLLTYPE_알비노유니콘 = 90;
	public static final int DOLLTYPE_유니콘 = 91;
	public static final int DOLLTYPE_그림리퍼 = 92;
	public static final int DOLLTYPE_다크스타조우 = 93;
	public static final int DOLLTYPE_디바인크루세이더 = 94;
	public static final int DOLLTYPE_군터 = 95;
	public static final int DOLLTYPE_오우거킹 = 96;
	public static final int DOLLTYPE_다크하딘 = 97;
	public static final int DOLLTYPE_드래곤슬레이어 = 98;
	public static final int DOLLTYPE_피닉스 = 99;
	public static final int DOLLTYPE_암흑대장로 = 100;
	
	// TODO 그외 설정
	public static final int DOLL_TIME = 1800000;

	private static Random _random = new Random(System.nanoTime());
	private int _dollType;
	private int _itemObjId;
	private DollItemTimer _itemTimer;
	private int _DoActioncount = 0;
	// 타겟이 없는 경우의 처리
	@Override
	public boolean noTarget() {
		if ((_master != null) && !_master.isDead() && (_master.getMapId() == getMapId())) {
			if (getLocation().getTileLineDistance(_master.getLocation()) > 1) {
				int dir = moveDirection(_master.getX(), _master.getY());
				if (dir == -1) {
					teleport(_master.getX(), _master.getY(), getHeading());
					
					if (!isAiRunning()) {
						startAI();
					}
				} else {
					setSleepTime(setDirectionMove(dir));
				}
				_DoActioncount = 0;
			}else {
				if(++_DoActioncount > 60){
					broadcastPacket(new S_DoActionGFX(this.getId(), 66));
					broadcastPacket(new S_DoActionGFX(this.getId(), 67));
					_DoActioncount = 0;
				}
			}
		} else {
			deleteDoll(true);
			return true;
		}
		return false;
	}

	// 시간 계측용
	class DollTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) { // 이미 파기되어 있지 않은가 체크
				return;
			}
			deleteDoll(true);
		}
	}

	// 시간 계측용
	class DollItemTimer extends RepeatTask {
		private int _itemId;

		DollItemTimer(int itemId, int time) {
			super(time);
			_itemId = itemId;
		}

		@Override
		public void execute() {
			if (_destroyed) { // 이미 파기되어 있지 않은가 체크
				cancel();
				return;
			}

			L1PcInstance pc = (L1PcInstance) _master;
			if (pc == null) {
				cancel();
				return;
			}
			L1ItemInstance item = ItemTable.getInstance().createItem(_itemId);
			item.setCount(1);
			if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(_itemId, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			}
		}
	}

	public L1DollInstance(L1Npc template, L1PcInstance master, int itemlevel, int dollType, int itemObjId, int bless) {
		super(template);
		setId(IdFactory.getInstance().nextId());
		setDollType(dollType);
//		setDollLevel(itemlevel);
		setItemObjId(itemObjId);
		GeneralThreadPool.getInstance().schedule(new DollTimer(), DOLL_TIME);
		setMaster(master);
		setDollBless(bless);
		setX(master.getX() + _random.nextInt(5) - 2);
		setY(master.getY() + _random.nextInt(5) - 2);
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addDoll(this);
		if (!isAiRunning()) {
			startAI();
		}
		if (isMpRegeneration()) {
			master.startMpRegenerationByDoll();
		}
		if (isHpRegeneration()) {
			master.startHpRegenerationByDoll();
		}
		// TODO 마법인형 효과 부여

		int einhasadEffect = getEinhasadEffect();
		if (einhasadEffect > 0) {
			SC_REST_EXP_INFO_NOTI.send(master);
		}

		int type = getDollType();
		switch (type) {
		// TODO 1단계 마법인형
		case DOLLTYPE_BUGBEAR:
			((L1PcInstance) _master).addWeightReduction(500);
		break;
		case DOLLTYPE_SNOWMAN:
			_master.getAC().addAc(-3);
			_master.addSpecialResistance(eKind.SPIRIT, 7);
		break;
		case DOLLTYPE_MOKAK:
			_master.addMaxHp(50);
		break;
		// TODO 2단계 마법인형
		case DOLLTYPE_COCA:
			_master.addBowDmgup(1);
			_master.addBowHitup(1);
		break;
		case DOLLTYPE_SNOWMAN_NEW:
			_master.addDmgup(1);
			_master.addHitup(1);
		break;
		case DOLLTYPE_SNOWMAN_A:
			_master.addBowHitup(5);
		break;
		case DOLLTYPE_LAVAGOLREM:
			_master.addDmgup(1);
		break;
		// TODO 3단계 마법인형
		case DOLLTYPE_SUCCUBUSQUEEN:
			_master.getAbility().addSp(1);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_DRAKE:
			_master.addBowDmgup(2);
		break;
		case DOLLTYPE_킹버그베어:
			_master.addSpecialResistance(eKind.ABILITY, 8);
		break;
		// TODO 4단계 마법인형
		case DOLLTYPE_LICH:
			_master.addMaxHp(80);
			_master.getAbility().addSp(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_사이클롭스:
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.addSpecialResistance(eKind.ABILITY, 12);
		break;
		case DOLLTYPE_NIGHTBALD:
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.addSpecialPierce(eKind.ABILITY, 5);
		break;
		case DOLLTYPE_SIER:
			_master.addBowDmgup(5);
		break;
		case DOLL_Iris:
			_master.getResistance().addcalcPcDefense(3);
		break;
		case DOLL_vampire:
			_master.addDmgup(1);
			_master.addHitup(2);
			_master.addSpecialPierce(eKind.FEAR, 3);
			_master.addSpecialResistance(eKind.ABILITY, 5);
		break;
		// TODO 5단계 마법인형
		case DOLLTYPE_DEMON:
			_master.addSpecialResistance(eKind.ABILITY, 12);
			_master.addSpecialPierce(eKind.ABILITY, 10);
		break;
		case DOLL_barranca:
			_master.addSpecialResistance(eKind.ABILITY, 12);
			_master.addSpecialPierce(eKind.SPIRIT, 10);
		break;
		case DOLL_corruption:
			_master.getAbility().addSp(3);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.SPIRIT, 5);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLL_Baphomet:
			_master.addSpecialPierce(eKind.FEAR, 5);
			_master.addSpecialResistance(eKind.ABILITY, 10);
		break;
		case DOLL_icequeen:
			_master.addBowDmgup(5);
			_master.addBowHitup(5);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.SPIRIT, 5);
		break;
		case DOLL_Kouts:
			_master.getAC().addAc(-3);
			_master.getResistance().addcalcPcDefense(3);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.DRAGON_SPELL, 5);
		break;
		/*
		 * case ruler1:
		 * 
		 * ((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance)
		 * _master))); break;
		 */
		case Papou:
			_master.addSpecialResistance(eKind.ALL, 8);
			_master.addSpecialPierce(eKind.ALL, 3);
			_master.getAbility().addSp(4);
			((L1PcInstance) _master).addBaseMagicHitUp(8);// 마법 적중
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case Lynd:
			_master.addSpecialResistance(eKind.ALL, 8);
			_master.addSpecialPierce(eKind.ALL, 3);
			_master.addBowHitup(8);
			_master.addBowDmgup(4);
		break;
		case Valakas:
			_master.addSpecialResistance(eKind.ALL, 8);
			_master.addSpecialPierce(eKind.ALL, 3);
			_master.addDmgup(4);
			_master.addHitup(8);
		break;
		// TODO 기타 이벤트 마법인형
		case DOLLTYPE_ETHYNE:
			_master.getAC().addAc(-2);
			_master.addSpecialResistance(eKind.SPIRIT, 10);
			master.removeHasteSkillEffect();
			if (master.getMoveSpeed() != 1) {
				master.setMoveSpeed(1);
				master.sendPackets(new S_SkillHaste(master.getId(), 1, -1));
				master.broadcastPacket(new S_SkillHaste(master.getId(), 1, 0));
			}
			master.setSkillEffect(STATUS_HASTE, 600 * 3200);
		break;
		case DOLLTYPE_SKELETON:
			_master.addDmgup(2);
		break;
		case DOLLTYPE_SCARECROW:
			_master.addBowDmgup(2);
			_master.addBowHitup(2);
			_master.addMaxHp(50);
			_master.addMaxMp(30);
		break;
		case DOLLTYPE_PSY_CHAMPION:
			_master.addMaxHp(30);
			_master.addDmgup(2);
		break;
		case DOLLTYPE_PSY_BIRD:
			_master.addBowHitup(2);
			_master.addMaxHp(30);
		break;
		case DOLLTYPE_PSY_GANGNAM_STYLE:
			_master.getAbility().addSp(1);
			_master.addMaxHp(30);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_GREMLIN:
			_master.addMaxHp(30);
			_master.getAbility().addSp(1);
			_master.addDmgup(2);
			_master.addBowDmgup(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_HATCHLING:
			if (_itemTimer == null) {
				_itemTimer = new DollItemTimer(40024, 240 * 1000);
				GeneralThreadPool.getInstance().schedule(_itemTimer, 240 * 1000);
			}
			((L1PcInstance) _master).addWeightReduction(10);
		break;
		case DOLLTYPE_PIXIE_BLAG:
		case DOLLTYPE_PIXIE_LESDAG:
		case DOLLTYPE_PIXIE_ELREGEU:
		case DOLLTYPE_PIXIE_GREG:
			_master.getAbility().addSp(1);
			_master.addDmgup(2);
			_master.addBowDmgup(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case 수련자마법인형:
			_master.getAC().addAc(-1);
			_master.getResistance().addcalcPcDefense(1);
		break;
		case DOLLTYPE_COBO:
			_master.getAbility().addSp(1);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축서큐퀸:
			_master.getAbility().addSp(-1);
			_master.getAC().addAc(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축흑장로:
			_master.getAC().addAc(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축자이언트:
			_master.getAC().addAc(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축드레이크:
			_master.getAC().addAc(-2);
			_master.addBowDmgup(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축킹버그:
			_master.getAC().addAc(-2);
			_master.addSpecialResistance(eKind.ABILITY, 8);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축다이아골렘:
			_master.getAC().addAc(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축사이클롭스:
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.getAC().addAc(-2);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.addSpecialResistance(eKind.ABILITY, 12);
		break;
		case DOLLTYPE_축리치:
			_master.addMaxHp(80);
			_master.getAbility().addSp(2);
			_master.getAC().addAc(-2);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축나발:
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.getAC().addAc(-2);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.addSpecialPierce(eKind.ABILITY, 5);
		break;
		case DOLLTYPE_축시어:
			_master.addBowDmgup(5);
			_master.getAC().addAc(-2);
			_master.getResistance().addPVPweaponTotalDamage(2);
		break;
		case DOLLTYPE_축아이리스:
			_master.getAC().addAc(-2);
			_master.getResistance().addPVPweaponTotalDamage(2);
		break;
		case DOLLTYPE_축뱀파:
			_master.getAC().addAc(-2);
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.addSpecialPierce(eKind.FEAR, 3);
			_master.addSpecialResistance(eKind.ABILITY, 5);
		break;
		case DOLLTYPE_축머미:
			_master.getAC().addAc(-2);
			_master.getResistance().addcalcPcDefense(3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축데몬:
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.addSpecialResistance(eKind.ABILITY, 12);
			_master.addSpecialPierce(eKind.ABILITY, 10);
		break;
		case DOLLTYPE_축데스:
			_master.getAC().addAc(-5);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
		break;
		case DOLLTYPE_축바란카:
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.addSpecialResistance(eKind.ABILITY, 12);
			_master.addSpecialPierce(eKind.SPIRIT, 10);
		break;
		case DOLLTYPE_축타락:
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.getAbility().addSp(3);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.SPIRIT, 5);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_축바포:
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.addSpecialPierce(eKind.FEAR, 5);
			_master.addSpecialResistance(eKind.ABILITY, 10);
		break;
		case DOLLTYPE_축얼녀:
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.addBowDmgup(5);
			_master.addBowHitup(5);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.SPIRIT, 5);
		break;
		case DOLLTYPE_축커츠:
			_master.getAC().addAc(-5);
			_master.getResistance().addPVPweaponTotalDamage(2);
			_master.getResistance().addcalcPcDefense(4);
			_master.getResistance().addcalcPcDefense(3);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialPierce(eKind.DRAGON_SPELL, 5);
		break;
		case DOLLTYPE_할파스:
			_master.getAC().addAc(-6);
			_master.addDmgup(2);
			_master.addHitup(2);
			_master.add_melee_critical_rate(10);
			_master.addSpecialResistance(eKind.ALL, 11);
			_master.addSpecialPierce(eKind.ALL, 10);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_아우라키아:
			_master.getAC().addAc(-6);
			_master.getAbility().addSp(10);
			((L1PcInstance) _master).addBaseMagicHitUp(12);
			_master.add_magic_critical_rate(5);
			_master.addMaxHp(300);
			_master.addSpecialResistance(eKind.ALL, 11);
			_master.addSpecialPierce(eKind.ALL, 10);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_베히모스:
			_master.getAC().addAc(-6);
			_master.addBowDmgup(10);
			_master.addBowHitup(10);
			_master.add_missile_critical_rate(10);
			_master.addMaxHp(200);
			_master.addMaxMp(100);
			_master.addSpecialResistance(eKind.ALL, 11);
			_master.addSpecialPierce(eKind.ALL, 10);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
		break;
		case DOLLTYPE_기르타스:
			_master.getAC().addAc(-12);
			_master.addDmgup(20);
			_master.addHitup(20);
			_master.add_melee_critical_rate(20);
			_master.getAbility().addSp(20);
			((L1PcInstance) _master).addBaseMagicHitUp(20);
			_master.add_magic_critical_rate(20);
			_master.addBowDmgup(20);
			_master.addBowHitup(20);
			_master.add_missile_critical_rate(20);
			_master.addMaxHp(2500);
			_master.addMaxMp(2500);
			_master.addSpecialResistance(eKind.ALL, 20);
			_master.addSpecialPierce(eKind.ALL, 20);
			_master.getResistance().addPVPweaponTotalDamage(10);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 10);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			((L1PcInstance) _master).addWeightReduction(5000);
		break;
		case DOLLTYPE_알비노데몬:
			_master.addDmgup(3);
			_master.addSpecialPierce(eKind.ABILITY, 12);
			_master.addSpecialResistance(eKind.ABILITY, 5);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_알비노피닉스:
			_master.addBowDmgup(3);
			_master.addSpecialPierce(eKind.ABILITY, 12);
			_master.addSpecialResistance(eKind.ABILITY, 5);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_알비노유니콘:
			_master.getAbility().addSp(3);
			_master.addSpecialPierce(eKind.ABILITY, 7);
			_master.addSpecialResistance(eKind.ABILITY, 5);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_유니콘:
			_master.addDmgup(5);
			_master.getAbility().addSp(3);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_피닉스:
			_master.addBowDmgup(5);
			_master.add_missile_critical_rate(2);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_암흑대장로:
			_master.getAbility().addSp(3);
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			_master.addSpecialResistance(eKind.ABILITY, 10);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_디바인크루세이더:
			_master.addDmgup(4);
			_master.getResistance().addcalcPcDefense(2);
			_master.getResistance().addPVPweaponTotalDamage(2);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_군터:
			_master.addDmgup(5);
			_master.add_melee_critical_rate(2);
			_master.addSpecialPierce(eKind.ABILITY, 8);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_오우거킹:
			_master.addDmgup(4);
			_master.addMaxHp(150);
			_master.addSpecialResistance(eKind.ABILITY, 10);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_다크스타조우:
			_master.getAbility().addSp(3);
			((L1PcInstance) _master).addBaseMagicHitUp(1);
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_그림리퍼:
			_master.addSpecialPierce(eKind.ABILITY, 8);
			_master.addSpecialResistance(eKind.ABILITY, 8);
			_master.addSpecialResistance(eKind.FEAR, 5);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_다크하딘:
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 5);
			_master.addSpecialResistance(eKind.ABILITY, 8);
			_master.addSpecialResistance(eKind.FEAR, 5);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		case DOLLTYPE_드래곤슬레이어:
			_master.getResistance().addcalcPcDefense(2);
			_master.addDmgup(3);
			_master.addBowDmgup(3);
			_master.addSpecialResistance(eKind.ABILITY, 8);
			_master.addSpecialResistance(eKind.FEAR, 8);
			((L1PcInstance) _master).addWeightReduction(2400);
		break;
		
		default:
		break;
		}
		/** 특수 마법인형 인챈트 시스템 **/
		if (getDollLevel() == 1) {
			_master.addSpecialResistance(eKind.ABILITY, 1);
			_master.addSpecialResistance(eKind.FEAR, 1);
		} else if (getDollLevel() == 2) {
			_master.addSpecialResistance(eKind.ABILITY, 2);
			_master.addSpecialResistance(eKind.FEAR, 2);
		} else if (getDollLevel() == 3) {
			_master.addSpecialResistance(eKind.ABILITY, 4);
			_master.addSpecialResistance(eKind.FEAR, 4);
		} else if (getDollLevel() == 4) {
			_master.addSpecialResistance(eKind.ABILITY, 7);
			_master.addSpecialResistance(eKind.FEAR, 7);
		} else if (getDollLevel() == 5) {
			_master.addSpecialResistance(eKind.ABILITY, 10);
			_master.addSpecialResistance(eKind.FEAR, 10);
		}

		/**
		 * 인형 축복효과
		 */
		if (getDollBless() % 128 == 0) {
			if (type >= DOLLTYPE_자이언트 && type <= DOLL_mummy) {
				_master.getAC().addAc(-2);
			}
			if (type >= DOLLTYPE_LICH && type <= DOLL_mummy) {
				_master.getResistance().addPVPweaponTotalDamage(2);
			}
			if (type >= DOLLTYPE_DEMON && type <= DOLL_Kouts) {
				_master.getAC().addAc(-3);
				_master.getResistance().addPVPweaponTotalDamage(2);
				((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 4);
			}
		}
		
		/**
		 * 용인형 추가효과
		 */
		if (type >= Antaras && type <= Valakas) {
			_master.getAC().addAc(-3);
			_master.getResistance().addPVPweaponTotalDamage(4);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() + 2);
		}

		if (_master instanceof L1PcInstance)
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo((L1PcInstance) _master);
	}

	public void deleteDoll(boolean flag) {
		if (((L1PcInstance) _master) == null) {
			return;
		}
		if (isMpRegeneration()) {
			((L1PcInstance) _master).stopMpRegenerationByDoll();
		} else if (isHpRegeneration()) {
			((L1PcInstance) _master).stopHpRegenerationByDoll();
		}

		// TODO 마법인형 효과 제거
		int type = getDollType();
		switch (type) {
		// TODO 1단계 마법인형
		case DOLLTYPE_BUGBEAR:
			((L1PcInstance) _master).addWeightReduction(-500);
			break;
		case DOLLTYPE_SNOWMAN:
			_master.getAC().addAc(3);
			_master.addSpecialResistance(eKind.SPIRIT, -7);
			break;
		case DOLLTYPE_MOKAK:
			_master.addMaxHp(-50);
			break;
		// TODO 2단계 마법인형
		case DOLLTYPE_COCA:
			_master.addBowDmgup(-1);
			_master.addBowHitup(-1);
			break;
		case DOLLTYPE_SNOWMAN_NEW:
			_master.addDmgup(-1);
			_master.addHitup(-1);
			break;
		case DOLLTYPE_SNOWMAN_A:
			_master.addBowHitup(-5);
			break;
		case DOLLTYPE_LAVAGOLREM:
			_master.addDmgup(-1);
			break;
		// TODO 3단계 마법인형
		case DOLLTYPE_SUCCUBUSQUEEN:
			_master.getAbility().addSp(-1);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_DRAKE:
			_master.addBowDmgup(-2);
			break;
		case DOLLTYPE_킹버그베어:
			_master.addSpecialResistance(eKind.ABILITY, -8);
			break;
		// TODO 4단계 마법인형
		case DOLLTYPE_LICH:
			_master.addMaxHp(-80);
			_master.getAbility().addSp(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_사이클롭스:
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.addSpecialResistance(eKind.ABILITY, -12);
			break;
		case DOLLTYPE_NIGHTBALD:
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.addSpecialPierce(eKind.ABILITY, -5);
			break;
		case DOLLTYPE_SIER:
			_master.addBowDmgup(-5);
			break;
		case DOLL_Iris:
			_master.getResistance().addcalcPcDefense(-3);
			break;
		case DOLL_vampire:
			_master.addDmgup(-1);
			_master.addHitup(-2);
			_master.addSpecialPierce(eKind.FEAR, -3);
			_master.addSpecialResistance(eKind.ABILITY, -5);
			break;
		// TODO 5단계 마법인형
		case DOLLTYPE_DEMON:
			_master.addSpecialResistance(eKind.ABILITY, -12);
			_master.addSpecialPierce(eKind.ABILITY, -10);
			break;
		case DOLL_barranca:
			_master.addSpecialResistance(eKind.ABILITY, -12);
			_master.addSpecialPierce(eKind.SPIRIT, -10);
			break;
		case DOLL_corruption:
			_master.getAbility().addSp(-3);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.SPIRIT, -5);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLL_Baphomet:
			_master.addSpecialPierce(eKind.FEAR, -5);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			break;
		case DOLL_icequeen:
			_master.addBowDmgup(-5);
			_master.addBowHitup(-5);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.SPIRIT, -5);
			break;
		case DOLL_Kouts:
			_master.getAC().addAc(3);
			_master.getResistance().addcalcPcDefense(-3);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.DRAGON_SPELL, -5);
			break;
		/*case ruler1:
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;*/
		case Papou:
			_master.addSpecialResistance(eKind.ALL, -8);
			_master.addSpecialPierce(eKind.ALL, -3);
			_master.getAbility().addSp(-4);
			((L1PcInstance) _master).addBaseMagicHitUp(-8);// 마법 적중
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case Lynd:
			_master.addSpecialResistance(eKind.ALL, -8);
			_master.addSpecialPierce(eKind.ALL, -3);
			_master.addBowHitup(-8);
			_master.addBowDmgup(-4);
			break;
		case Valakas:
			_master.addSpecialResistance(eKind.ALL, -8);
			_master.addSpecialPierce(eKind.ALL, -3);
			_master.addDmgup(-4);
			_master.addHitup(-8);
			break;
		// TODO 기타 이벤트 마법인형
		case DOLLTYPE_ETHYNE:
			_master.getAC().addAc(2);
			_master.addSpecialResistance(eKind.SPIRIT, -10);
			_master.setMoveSpeed(0);
			((L1PcInstance) _master).sendPackets(new S_SkillHaste(_master.getId(), 0, 0));
			_master.broadcastPacket(new S_SkillHaste(_master.getId(), 0, 0));
			_master.removeSkillEffect(STATUS_HASTE);
			break;
		case DOLLTYPE_SKELETON:
			_master.addDmgup(-2);
			break;
		case DOLLTYPE_SCARECROW:
			_master.addBowDmgup(-2);
			_master.addBowHitup(-2);
			_master.addMaxHp(-50);
			_master.addMaxMp(-30);
			break;
		case DOLLTYPE_PSY_CHAMPION:
			_master.addMaxHp(-30);
			_master.addDmgup(-2);
			break;
		case DOLLTYPE_PSY_BIRD:
			_master.addBowHitup(-2);
			_master.addMaxHp(-30);
			break;
		case DOLLTYPE_PSY_GANGNAM_STYLE:
			_master.getAbility().addSp(-1);
			_master.addMaxHp(-30);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_GREMLIN:
			_master.addMaxHp(-30);
			_master.getAbility().addSp(-1);
			_master.addDmgup(-2);
			_master.addBowDmgup(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_HATCHLING:
			if (_itemTimer != null) {
				_itemTimer.cancel();
				_itemTimer = null;
			}
			((L1PcInstance) _master).addWeightReduction(-10);
			break;
		case DOLLTYPE_PIXIE_BLAG:
		case DOLLTYPE_PIXIE_LESDAG:
		case DOLLTYPE_PIXIE_ELREGEU:
		case DOLLTYPE_PIXIE_GREG:
			_master.getAbility().addSp(-1);
			_master.addDmgup(-2);
			_master.addBowDmgup(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case 수련자마법인형:
			_master.getAC().addAc(1);
			_master.getResistance().addcalcPcDefense(-1);
			break;
		case DOLLTYPE_COBO:
			_master.getAbility().addSp(-1);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축서큐퀸:
			_master.getAbility().addSp(-1);
			_master.getAC().addAc(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축흑장로:
			_master.getAC().addAc(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축자이언트:
			_master.getAC().addAc(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축드레이크:
			_master.getAC().addAc(2);
			_master.addBowDmgup(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축킹버그:
			_master.getAC().addAc(2);
			_master.addSpecialResistance(eKind.ABILITY, -8);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축다이아골렘:
			_master.getAC().addAc(2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축사이클롭스:
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.getAC().addAc(2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.addSpecialResistance(eKind.ABILITY, -12);
			break;
		case DOLLTYPE_축리치:
			_master.addMaxHp(-80);
			_master.getAbility().addSp(-2);
			_master.getAC().addAc(2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축나발:
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.getAC().addAc(2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.addSpecialPierce(eKind.ABILITY, -5);
			break;
		case DOLLTYPE_축시어:
			_master.addBowDmgup(-5);
			_master.getAC().addAc(2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			break;
		case DOLLTYPE_축아이리스:
			_master.getAC().addAc(2);
		_master.getResistance().addPVPweaponTotalDamage(-2);
		break;
		case DOLLTYPE_축뱀파:
			_master.getAC().addAc(2);
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.addSpecialPierce(eKind.FEAR, -3);
			_master.addSpecialResistance(eKind.ABILITY, -5);
			break;
		case DOLLTYPE_축머미:
			_master.getAC().addAc(2);
			_master.getResistance().addcalcPcDefense(-3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축데몬:
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.addSpecialResistance(eKind.ABILITY, -12);
			_master.addSpecialPierce(eKind.ABILITY, -10);
			break;
		case DOLLTYPE_축데스:
			_master.getAC().addAc(5);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			break;
		case DOLLTYPE_축바란카:
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.addSpecialResistance(eKind.ABILITY, -12);
			_master.addSpecialPierce(eKind.SPIRIT, -10);
			break;
		case DOLLTYPE_축타락:
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.getAbility().addSp(-3);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.SPIRIT, -5);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			break;
		case DOLLTYPE_축바포:
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.addSpecialPierce(eKind.FEAR, -5);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			break;
		case DOLLTYPE_축얼녀:
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.addBowDmgup(5);
			_master.addBowHitup(5);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.SPIRIT, -5);
			break;
		case DOLLTYPE_축커츠:
			_master.getAC().addAc(5);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			_master.getResistance().addcalcPcDefense(-4);
			_master.getResistance().addcalcPcDefense(-3);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialPierce(eKind.DRAGON_SPELL, -5);
			break;
		case DOLLTYPE_할파스:
			_master.getAC().addAc(6);
			_master.addDmgup(-2);
			_master.addHitup(-2);
			_master.add_melee_critical_rate(-10);
			_master.addSpecialResistance(eKind.ALL, -11);
			_master.addSpecialPierce(eKind.ALL, -10);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			break;
		case DOLLTYPE_아우라키아:
			_master.getAC().addAc(6);
			_master.getAbility().addSp(-10);
			((L1PcInstance) _master).addBaseMagicHitUp(-12);
			_master.add_magic_critical_rate(-5);
			_master.addMaxHp(-300);
			_master.addSpecialResistance(eKind.ALL, -11);
			_master.addSpecialPierce(eKind.ALL, -10);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			break;
		case DOLLTYPE_베히모스:
			_master.getAC().addAc(6);
			_master.addBowDmgup(-10);
			_master.addBowHitup(-10);
			_master.addMaxHp(-200);
			_master.addMaxMp(-100);
			_master.addSpecialResistance(eKind.ALL, -11);
			_master.addSpecialPierce(eKind.ALL, -10);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 2);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			break;
		case DOLLTYPE_기르타스:
			_master.getAC().addAc(12);
			_master.addDmgup(-20);
			_master.addHitup(-20);
			_master.add_melee_critical_rate(-20);
			_master.getAbility().addSp(-20);
			((L1PcInstance) _master).addBaseMagicHitUp(-20);
			_master.add_magic_critical_rate(-20);
			_master.addBowDmgup(-20);
			_master.addBowHitup(-20);
			_master.add_missile_critical_rate(-20);
			_master.addMaxHp(-2500);
			_master.addMaxMp(-2500);
			_master.addSpecialResistance(eKind.ALL, -20);
			_master.addSpecialPierce(eKind.ALL, -20);
			_master.getResistance().addPVPweaponTotalDamage(-10);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 10);
			((L1PcInstance) _master).sendPackets(new S_SPMR(((L1PcInstance) _master)));
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			((L1PcInstance) _master).addWeightReduction(-5000);
			break;
		case DOLLTYPE_알비노데몬:
			_master.addDmgup(-3);
			_master.addSpecialResistance(eKind.ABILITY, -5);
			_master.addSpecialPierce(eKind.ABILITY, -12);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_알비노피닉스:
			_master.addBowDmgup(-3);
			_master.addSpecialPierce(eKind.ABILITY, -12);
			_master.addSpecialResistance(eKind.ABILITY, -5);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_알비노유니콘:
			_master.getAbility().addSp(-3);
			_master.addSpecialPierce(eKind.ABILITY, -7);
			_master.addSpecialResistance(eKind.ABILITY, -5);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_유니콘:
			_master.addDmgup(-5);
			_master.getAbility().addSp(-3);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_피닉스:
			_master.addBowDmgup(-5);
			_master.add_missile_critical_rate(-2);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_암흑대장로:
			_master.getAbility().addSp(-3);
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			_master.addSpecialResistance(eKind.ABILITY, -10);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_디바인크루세이더:
			_master.addDmgup(-4);
			_master.getResistance().addcalcPcDefense(-2);
			_master.getResistance().addPVPweaponTotalDamage(-2);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_군터:
			_master.addDmgup(-5);
			_master.add_melee_critical_rate(-2);
			_master.addSpecialPierce(eKind.ABILITY, -8);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_오우거킹:
			_master.addDmgup(-4);
			_master.addMaxHp(-150);
			_master.addSpecialResistance(eKind.ABILITY, -10);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;	
		case DOLLTYPE_다크스타조우:
			_master.getAbility().addSp(-3);
			((L1PcInstance) _master).addBaseMagicHitUp(-1);
			_master.sendPackets(new S_MPUpdate(_master.getCurrentMp(), _master.getMaxMp()));
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_그림리퍼:
			_master.addSpecialPierce(eKind.ABILITY, -8);
			_master.addSpecialResistance(eKind.ABILITY, -8);
			_master.addSpecialResistance(eKind.FEAR, -5);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_다크하딘:
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 5);
			_master.addSpecialResistance(eKind.ABILITY, -8);
			_master.addSpecialResistance(eKind.FEAR, -5);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		case DOLLTYPE_드래곤슬레이어:
			_master.getResistance().addcalcPcDefense(-2);
			_master.addDmgup(-3);
			_master.addBowDmgup(-3);
			_master.addSpecialResistance(eKind.ABILITY, -8);
			_master.addSpecialResistance(eKind.FEAR, -8);
			((L1PcInstance) _master).addWeightReduction(-2400);
		break;
		default:
			break;
		}

		/** 특수 마법인형 인챈트 시스템 **/
		if (getDollLevel() == 1) {
			_master.addSpecialResistance(eKind.ABILITY, -1);
			_master.addSpecialResistance(eKind.FEAR, -1);
		} else if (getDollLevel() == 2) {
			_master.addSpecialResistance(eKind.ABILITY, -2);
			_master.addSpecialResistance(eKind.FEAR, -2);
		} else if (getDollLevel() == 3) {
			_master.addSpecialResistance(eKind.ABILITY, -4);
			_master.addSpecialResistance(eKind.FEAR, -4);
		} else if (getDollLevel() == 4) {
			_master.addSpecialResistance(eKind.ABILITY, -7);
			_master.addSpecialResistance(eKind.FEAR, -7);
		} else if (getDollLevel() == 5) {
			_master.addSpecialResistance(eKind.ABILITY, -10);
			_master.addSpecialResistance(eKind.FEAR, -10);
		}

		if (_master instanceof L1PcInstance)
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo((L1PcInstance) _master);

		/**
		 * 인형 축복효과
		 */
		if (getDollBless() % 128 == 0) {
			if (type >= DOLLTYPE_자이언트 && type <= DOLL_mummy) {
				_master.getAC().addAc(2);
			}
			if (type >= DOLLTYPE_LICH && type <= DOLL_mummy) {
				_master.getResistance().addPVPweaponTotalDamage(-2);
			}
			if (type >= DOLLTYPE_DEMON && type <= DOLL_Kouts) {
				_master.getAC().addAc(3);
				_master.getResistance().addPVPweaponTotalDamage(-2);
				((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 4);
			}
		}
		
		/**
		 * 용인형 추가효과
		 */
		if (type >= Antaras && type <= Valakas) {
			_master.getAC().addAc(3);
			_master.getResistance().addPVPweaponTotalDamage(-4);
			((L1PcInstance) _master).set_pvp_defense(((L1PcInstance) _master).get_pvp_defense() - 2);
		}

		if (_master != null)
			_master.removeDoll(this);

		if (flag) {// 자동소환
			if (!getMaster().getInventory().checkItem(41246, 50)) {
				((L1PcInstance) _master).sendPackets(new S_DollOnOff(null, null, 0, false));
				((L1PcInstance) _master).sendPackets(new S_ServerMessage(337, "$5240"));
			} else {
				((L1PcInstance) _master).sendPackets(new S_DollOnOff(null, null, 0, false));
			}
		} else {
			((L1PcInstance) _master).sendPackets(new S_DollOnOff(null, null, 0, false));
		}
		((L1PcInstance) _master).sendPackets(new S_DollOnOff(null, null, 0, false));

		getMap().setPassable(getLocation(), true);
		((L1PcInstance) _master).sendPackets(new S_SkillSound(getId(), 5936));
		if (_master.getDollList().containsKey(getId())) {
			_master.getDollList().remove(getId());
		}
		deleteMe();
		int einhasadEffect = getEinhasadEffect();
		if (einhasadEffect > 0) {
			SC_REST_EXP_INFO_NOTI.send((L1PcInstance) _master);
		}
		setMaster(null);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		if (perceivedFrom.getAI() == null) {
			perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));

		}
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			// 100%의 확률로 헤이 파업 일부 사용
			useItem(USEITEM_HASTE, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}
	
	private int _dollBless;
	
	public int getDollBless() {
		return _dollBless;
	}
	
	public void setDollBless(int i) {
		_dollBless = i;
	}

	public int getDollType() {
		return _dollType;
	}

	public void setDollType(int i) {
		_dollType = i;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

	/** 특수 마법인형 인챈트 시스템 **/
	private static int _dollLevel;

	public static int getDollLevel() {
		return _dollLevel;
	}

	public static void setDollLevel(int i) {
		_dollLevel = i;
	}

	/** 특수 마법인형 인챈트 시스템 **/

	public int getDamageByDoll() {// 근접무기 착용시에만 불려간다.
		int damage = 0;
		if (getDollType() == DOLLTYPE_WAREWOLF) { // 늑인
			int chance = _random.nextInt(100) + 1;
			if (chance <= 5) {
				damage = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
				}
				_master.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
			}
		}

		if (getDollType() == DOLLTYPE_CRUSTACEA) { // 시안
			int chance = _random.nextInt(100) + 1;
			if (chance <= 10) {
				damage = 15;
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPackets(new S_SkillSound(_master.getId(), 6319));
				}
				_master.broadcastPacket(new S_SkillSound(_master.getId(), 6319));
			}
		}
		return damage;
	}

	public int getSpellPowerByDoll() {
		int sp = 0;
		return sp;
	}

	public int getStunLevelAdd() {
		int addStun = 0;
		if (getDollType() == DOLLTYPE_NIGHTBALD) {
			addStun = 5;
		}
		return addStun;
	}

	/**대미지 감소**/
	public int getDamageReductionByDoll() {
		int DamageReduction = 0;
		if (getDollType() == DOLLTYPE_DEATHNIGHT	|| getDollType() == ruler1) {
			DamageReduction = 5;
			
		} else if (getDollType() == DOLLTYPE_DIAMONDGOLREM	|| getDollType() == DOLL_mummy) {
			DamageReduction = 2;
			
		} else if (getDollType() == DOLLTYPE_자이언트		|| getDollType() == DOLLTYPE_LAVAGOLREM
				|| getDollType() == DOLLTYPE_STONEGOLEM		|| getDollType() == DOLLTYPE_축자이언트
				|| getDollType() == DOLLTYPE_축다이아골렘) {
			DamageReduction = 1;
			
		} else if (getDollType() == ruler5) {
			DamageReduction = 4;
			
		} else if (getDollType() == ruler2	|| getDollType() == ruler6	|| getDollType() == DOLLTYPE_축아이리스) {
			DamageReduction = 3;
			
		} else if (getDollType() == ruler3	|| getDollType() == ruler7) {
			DamageReduction = 2;
			
		} else if (getDollType() == Antaras	|| getDollType() == DOLLTYPE_축데스) {
			DamageReduction = 6;
			
		} else if (getDollType() == DOLLTYPE_기르타스) {
			DamageReduction = 16;
			
		} else if (getDollType() == DOLLTYPE_디바인크루세이더 || getDollType() == DOLLTYPE_드래곤슬레이어) {
			DamageReduction = 2;
			
		} else if (getDollType() == DOLLTYPE_할파스 || getDollType() == DOLLTYPE_아우라키아 || getDollType() == DOLLTYPE_베히모스) {
			DamageReduction = 8;
		}

		return DamageReduction;
	}

	/**포우 슬레이어 대미지**/
	public int fou_DamageUp() {
		int fou = 0;
		switch (getDollType()) {
		case DOLLTYPE_축커츠: 
		case DOLLTYPE_축아이리스:
		case DOLL_Iris:
		case DOLL_Kouts:
			fou = 10;
			break;
		}
		return fou;
	}

	/**MP 회복하는 인형**/
	public boolean isMpRegeneration() {
		return (getDollType() == DOLLTYPE_SUCCUBUS			|| getDollType() == DOLLTYPE_ELDER
				|| getDollType() == DOLLTYPE_HATCHLING		|| getDollType() == DOLLTYPE_PSY_CHAMPION
				|| getDollType() == DOLLTYPE_PSY_BIRD		|| getDollType() == DOLLTYPE_PSY_GANGNAM_STYLE
				|| getDollType() == DOLLTYPE_SUCCUBUSQUEEN	|| getDollType() == DOLLTYPE_GREMLIN
				|| getDollType() == DOLLTYPE_SNOWMAN_B		|| getDollType() == DOLLTYPE_흑장로
				|| getDollType() == Antaras					|| getDollType() == Papou
				|| getDollType() == Lynd					|| getDollType() == Valakas
				|| getDollType() == DOLL_mummy				|| getDollType() == DOLLTYPE_축서큐퀸
				|| getDollType() == DOLLTYPE_축흑장로		|| getDollType() == DOLLTYPE_축머미
				|| getDollType() == DOLLTYPE_축시어			|| getDollType() == DOLLTYPE_아우라키아
				|| getDollType() == DOLLTYPE_할파스			|| getDollType() == DOLLTYPE_베히모스
				|| getDollType() == DOLLTYPE_기르타스						
				|| getDollType() == DOLLTYPE_암흑대장로		|| getDollType() == DOLLTYPE_다크스타조우);
		}

	/**MP 회복하는 값**/
	public int getMpRegenAmount() {
			if (getDollType() == DOLLTYPE_축시어)
				return 32;
			
			if (getDollType() == DOLLTYPE_SUCCUBUS			|| getDollType() == DOLLTYPE_ELDER
			 || getDollType() == DOLLTYPE_PSY_CHAMPION		|| getDollType() == DOLLTYPE_PSY_BIRD
			 || getDollType() == DOLLTYPE_PSY_GANGNAM_STYLE || getDollType() == DOLLTYPE_SUCCUBUSQUEEN
			 || getDollType() == DOLLTYPE_흑장로			|| getDollType() == DOLL_mummy
			 || getDollType() == Antaras					|| getDollType() == DOLLTYPE_COBO
			 || getDollType() == DOLLTYPE_축서큐퀸			|| getDollType() == DOLLTYPE_축흑장로
			 || getDollType() == DOLLTYPE_축머미			|| getDollType() == DOLLTYPE_베히모스) {
			return 15;
			
		} else if (getDollType() == DOLLTYPE_GREMLIN		|| getDollType() == DOLLTYPE_킹버그베어
				|| getDollType() == DOLLTYPE_축킹버그		|| getDollType() == DOLLTYPE_할파스) {
			return 10;
			
		} else if (getDollType() == DOLLTYPE_HATCHLING		|| getDollType() == Papou
				|| getDollType() == Lynd					|| getDollType() == Valakas) {
			return 5;
			
		} else if (getDollType() == DOLLTYPE_SNOWMAN_B) {
			
		} else if (getDollType() == DOLLTYPE_기르타스) {
			return 30;
			
		} else if (getDollType() == DOLLTYPE_아우라키아) {
			return 20;
			
		} else if (getDollType() == DOLLTYPE_암흑대장로		|| getDollType() == DOLLTYPE_다크스타조우) {
			return 16;
			
		} else if (getDollType() == DOLLTYPE_베히모스) {
			return 15;
			}
		return 0;
	}

	/**HP 회복하는 인형 리스트**/
	public boolean isHpRegeneration() {
		boolean isHpRegeneration = false;
		if (getDollType() == DOLLTYPE_SEADANCER		|| getDollType() == DOLLTYPE_SNOWMAN_C		||getDollType() == DOLLTYPE_SIER
		 || getDollType() == DOLLTYPE_축시어) {
			isHpRegeneration = true;
		}
		return isHpRegeneration;
	}

	/**무게 감소**/
	public int getWeightReductionByDoll() {
	    int weightReduction = 0;
	    if (getDollType() == DOLLTYPE_BUGBEAR		|| getDollType() == DOLLTYPE_HATCHLING) {
	        weightReduction = 500;
	    }
	    if (getDollType() == DOLLTYPE_기르타스) {
	   	        weightReduction = 5000;
	    }
		if (getDollType() == DOLLTYPE_알비노데몬		|| getDollType() == DOLLTYPE_알비노피닉스	|| getDollType() == DOLLTYPE_알비노유니콘
		 || getDollType() == DOLLTYPE_알비노유니콘		|| getDollType() == DOLLTYPE_피닉스			|| getDollType() == DOLLTYPE_암흑대장로
		 || getDollType() == DOLLTYPE_디바인크루세이더	|| getDollType() == DOLLTYPE_군터			|| getDollType() == DOLLTYPE_오우거킹
		 || getDollType() == DOLLTYPE_다크스타조우		|| getDollType() == DOLLTYPE_그림리퍼		|| getDollType() == DOLLTYPE_다크하딘
		 || getDollType() == DOLLTYPE_드래곤슬레이어) {
			weightReduction = 2400;
		}
		return weightReduction;
	}


	// 픽시 마법인형 [ 브레그, 레데그, 엘레그 ]
	@SuppressWarnings("unused") // 추가 데스나이트 데미지
	public double attackPixieDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int type = getDollType();
		int effect = 0;
		switch (type) {
		case DOLLTYPE_흑장로:
		case DOLLTYPE_축흑장로:
			effect = 11736; // 콜라이트닝
			break;
		case DOLLTYPE_PIXIE_BLAG:
			effect = 1809; // 콘 오브 콜드
			break;
		case DOLLTYPE_PIXIE_LESDAG:
			effect = 1583; // 파이어 애로우
			break;
		case DOLLTYPE_PIXIE_ELREGEU:
			effect = 7331; // 회오리
			break;
		case DOLLTYPE_DEATHNIGHT:
		case DOLLTYPE_축데스:	
			effect = 11660; // 헬파이어
			break;
		case ruler1:
		case ruler2:
		case ruler3:
		case ruler4:
		case ruler5:
		case ruler6:
		case ruler7:
		case ruler8:
			effect = 13340; // 파이어 밤
			break;
		default:
			break;
		}
		if (type >= DOLLTYPE_PIXIE_BLAG && type <= DOLLTYPE_PIXIE_ELREGEU) {
			int chance = _random.nextInt(100) + 1;
			if (10 >= chance) {
				dmg = 10;
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == DOLLTYPE_흑장로 || type == DOLLTYPE_축흑장로) {
			int chance = _random.nextInt(100) + 1;
			if (10 >= chance) {
				dmg = 20;
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == DOLLTYPE_DEATHNIGHT || type == DOLLTYPE_축데스) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= Config.데스나이트헬파이어) {
				if (cha.hasSkillEffect(ERASE_MAGIC)) {
					cha.killSkillEffectTimer(ERASE_MAGIC);
				}
				dmg = Config.DEDMG + chance2 + (TotalInt * 3); // 데미지 : 고정25 + 랜덤 35+ 케릭인트수치 *2 = 35인트일경우 80~110데미지
				// S_UseAttackSkill packet = new S_UseAttackSkill(this,
				// cha.getId(), effect, cha.getX(), cha.getY(),
				// ActionCodes.ACTION_SkillAttack, false);
				S_UseAttackSkill packet = new S_UseAttackSkill(cha, pc.getId(), effect, pc.getX(), pc.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == ruler1) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= Config.지배자헌신마법확률) {
				dmg = Config.DEDMG1 + chance2 + (TotalInt * 3); // 데미지 : 고정25 + 랜덤 35+ 케릭인트수치 *2 = 35인트일경우 80~110데미지
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == ruler5) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= 20) {
				dmg = 40 + chance2 + (TotalInt * 3); // 데미지 : 고정25 + 랜덤 35+
														// 케릭인트수치 *2 = 35인트일경우
														// 80~110데미지
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == ruler2 || type == ruler6) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= 8) {
				dmg = 12 + chance2 + (TotalInt * 2); // 데미지 : 고정25 + 랜덤 35+
														// 케릭인트수치 *2 = 35인트일경우
														// 80~110데미지
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == ruler3 || type == ruler7) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= 6) {
				dmg = 9 + chance2 + (TotalInt * 2); // 데미지 : 고정25 + 랜덤 35+
													// 케릭인트수치 *2 = 35인트일경우
													// 80~110데미지
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		if (type == ruler4 || type == ruler8) {
			int chance = _random.nextInt(100) + 1;
			int chance2 = _random.nextInt(35) + 1;
			int chance3 = _random.nextInt(10) + 1;
			int TotalInt = cha.getAbility().getTotalInt();
			if (chance <= 5) {
				dmg = 7 + chance2 + (TotalInt * 2); // 데미지 : 고정25 + 랜덤 35+
													// 케릭인트수치 *2 = 35인트일경우
													// 80~110데미지
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		return dmg;
	}

	// 픽시 마법인형 [ 그레그 ]
	public void getPixieGreg(L1PcInstance pc, L1Character cha) {
		L1Attack attack = new L1Attack(pc, cha);
		int type = getDollType();
		if (type == DOLLTYPE_PIXIE_GREG) {
			int chance = _random.nextInt(100) + 1;
			if (10 >= chance) {
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), 4022, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
	}

	/**축복 소모 효율**/
	public int getEinhasadEffect() {
		int dollType = getDollType();
		switch (dollType) {
		case DOLLTYPE_기르타스:
			return 35;
		case DOLLTYPE_알비노데몬:
		case DOLLTYPE_알비노피닉스:
		case DOLLTYPE_알비노유니콘:
		case DOLLTYPE_유니콘:
		case DOLLTYPE_피닉스:
		case DOLLTYPE_암흑대장로:		
		case DOLLTYPE_디바인크루세이더:
		case DOLLTYPE_군터:
		case DOLLTYPE_오우거킹:	
		case DOLLTYPE_다크스타조우:
		case DOLLTYPE_그림리퍼:
		case DOLLTYPE_다크하딘:
			return 21;
		case DOLLTYPE_드래곤슬레이어:	
			return 23;
		case DOLLTYPE_할파스:
		case DOLLTYPE_아우라키아:
		case DOLLTYPE_베히모스:	
			return 10;
		case DOLL_mummy:
			return 2;
		case DOLLTYPE_DEATHNIGHT:
		case DOLLTYPE_축데스:
		case Antaras:
			return 7;
/*		case ruler1:
			return 30;*/
		case ruler2:
			return 4;
		case ruler3:
			return 3;
		case ruler4:
		case DOLLTYPE_축머미:
			return 2;
		case jindeathknight:
			return 5;
		case DOLLTYPE_SKELETON:
			return 5;
		default:
		}
		return 0;
	}

	/** 특수 마법인형 인챈트 시스템 **/
	public double magic_doll_enchant_dmg(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int type = getDollLevel();
		int effect = 0;
		int damage = 0;
		int chance = _random.nextInt(100) + 1;
		int chance2 = _random.nextInt(35) + 1;
		// int TotalStat = cha.getAbility().getTotalInt();
		int TotalStat = 0;

		if (pc.isWizard() || pc.isBlackwizard()) {
			TotalStat = cha.getAbility().getTotalInt();
		}
		if (pc.isElf()) {
			TotalStat = cha.getAbility().getTotalDex();
		} else {
			TotalStat = cha.getAbility().getTotalStr();
		}

		switch (type) {
		case 1:
			effect = 1809; // 콘 오브 콜드
			damage = Config.Doll_Attack_Dmg_Lvl1 + chance2 + TotalStat;
			break;
		case 2:
			effect = 1583; // 파이어 애로우
			damage = Config.Doll_Attack_Dmg_Lvl2 + chance2 + TotalStat;
			break;
		case 3:
			effect = 7331; // 회오리
			damage = Config.Doll_Attack_Dmg_Lvl3 + chance2 + TotalStat;
			break;
		case 4:
			effect = 7004; // 콜라이트닝
			damage = Config.Doll_Attack_Dmg_Lvl4 + chance2 + TotalStat;
			break;
		case 5:
			effect = 11660; // 헬파이어+
			damage = Config.Doll_Attack_Dmg_Lvl5 + chance2 + TotalStat;
			break;
		default:
			break;
		}
		if (type != 0) {
			if (chance <= Config.Doll_Attack_Chance + getDollLevel()) {
				if (cha.hasSkillEffect(ERASE_MAGIC)) {
					cha.killSkillEffectTimer(ERASE_MAGIC);
				}
				dmg += damage;
				S_UseAttackSkill packet = new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(),
						ActionCodes.ACTION_SkillAttack, false);
				pc.sendPackets(packet, false);
				Broadcaster.broadcastPacket(pc, packet);
			}
		}
		return dmg;
	}

	/** 특수 마법인형 인챈트 시스템 **/

	@Override
	public boolean checkCondition() {
		if (_master == null) {
			return true;
		}
		/*
		 * if (_master instanceof L1PcInstance && ((L1PcInstance)
		 * _master).isInWarArea()) {//공성중 인형을 지운다 deleteDoll(false); return
		 * true; }
		 */
		return false;
	}

	private int _invGfx;

	public int getInvGfx() {
		return _invGfx;
	}

	public void setInvGfx(int i) {
		_invGfx = i;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_DOLL : _instanceType;
	}
}
