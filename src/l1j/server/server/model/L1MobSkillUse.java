package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;

public class L1MobSkillUse {
	private static Random _rnd = new Random(System.nanoTime());

	private L1MobSkill _mobSkillTemplate = null;

	private L1NpcInstance _attacker = null;

	private L1Character _target = null;


	private long _sleepTime = 0;

	private int _skillUseCount[];

	public void dispose(){
		_mobSkillTemplate 	= null;
		_attacker 			= null;
		_target				= null;
	}
	
	public L1MobSkillUse(L1NpcInstance npc) {
		try {
			if (npc == null)
				return;
			_sleepTime = 0;
			_mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
			if (_mobSkillTemplate == null) {
				return;
			}
			_attacker = npc;
			_skillUseCount = new int[getMobSkillTemplate().getSkillSize()];
		} catch (Exception e) {
		}
	}

	private int getSkillUseCount(int idx) {
		return _skillUseCount[idx];
	}

	private void skillUseCountUp(int idx) {
		_skillUseCount[idx]++;
	}

	public void resetAllSkillUseCount() {
		if (getMobSkillTemplate() == null) {
			return;
		}

		for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
			_skillUseCount[i] = 0;
		}
	}

	public long getSleepTime() {
		return _sleepTime;
	}

	public void setSleepTime(long i) {
		_sleepTime = i;
	}

	public L1MobSkill getMobSkillTemplate() {
		return _mobSkillTemplate;
	}

	public boolean skillUse(L1Character tg) {
		try {
			if (tg == null || _mobSkillTemplate == null) {
				return false;
			}
			_target = tg;

			int type;
			type = getMobSkillTemplate().getType(0);

			if (type == L1MobSkill.TYPE_NONE) {
				return false;
			}

			int i = 0;
			for (i = 0; i < getMobSkillTemplate().getSkillSize()
					&& getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {

				int changeType = getMobSkillTemplate().getChangeTarget(i);
				if (changeType > 0) {
					_target = changeTarget(changeType, i);
				} else {
					_target = tg;
				}

				if (isSkillUseble(i) == false) {
					continue;
				}

				type = getMobSkillTemplate().getType(i);
				if (type == L1MobSkill.TYPE_PHYSICAL_ATTACK) {
					if (physicalAttack(i) == true) {
						skillUseCountUp(i);
						return true;
					}
				} else if (type == L1MobSkill.TYPE_MAGIC_ATTACK) {
					if (magicAttack(i) == true) {
						skillUseCountUp(i);
						return true;
					}
				} else if (type == L1MobSkill.TYPE_SUMMON) {
					if (summon(i) == true) {
						skillUseCountUp(i);
						return true;
					}
				} else if (type == L1MobSkill.TYPE_POLY) {
					if (poly(i) == true) {
						skillUseCountUp(i);
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	private boolean summon(int idx) {
		int summonId = getMobSkillTemplate().getSummon(idx);
		int min = getMobSkillTemplate().getSummonMin(idx);
		int max = getMobSkillTemplate().getSummonMax(idx);
		int count = 0;

		if (summonId == 0) {
			return false;
		}

		count = _rnd.nextInt(max) + min;
		mobspawn(summonId, count);

		_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), 761));

		S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
		_attacker.broadcastPacket(gfx);

		_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		return true;
	}

	private boolean poly(int idx) {
		int polyId = getMobSkillTemplate().getPolyId(idx);
		boolean usePoly = false;

		if (polyId == 0) {
			return false;
		}

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
			if (pc == null || pc.isDead()) {
				continue;
			}
			if (pc.isGhost()) {
				continue;
			}
			if (pc.isGmInvis()) {
				continue;
			}
			if (_attacker.glanceCheck(pc.getX(), pc.getY()) == false) {
				continue;
			}

			int npcId = _attacker.getNpcTemplate().get_npcId();
			switch (npcId) {
			case 81082:
				pc.getInventory().takeoffEquip(945);
				break;
			default:
				break;
			}
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC, false);

			usePoly = true;
		}
		if (usePoly) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
				if (pc == null)
					continue;
				pc.sendPackets(new S_SkillSound(pc.getId(), 230));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 230));
				break;
			}
			S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
			_attacker.broadcastPacket(gfx);

			_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		}

		return usePoly;
	}

	private boolean magicAttack(int idx) {
		L1SkillUse skillUse = new L1SkillUse();
		int skillid = getMobSkillTemplate().getSkillId(idx);
		boolean canUseSkill = false;
		if (_attacker.hasSkillEffect(L1SkillId.SILENCE)) {
			return false;
		}

		if ((skillid >= 22020 && skillid <= 22029 || skillid >= 22041 && skillid <= 22052)
				|| (skillid >= 7001 && skillid <= 7050)) {
			if (_attacker.hasSkillEffect(L1SkillId.PREDICATEDELAY)) {// 용언 딜레이
																		// 시작.
				return false;
			} else {
				_attacker.setSkillEffect(L1SkillId.PREDICATEDELAY, 10 * 1000);
			}
		}

		int npcId = _attacker.getNpcTemplate().get_npcId();
		switch (npcId) {//몬스터 스킬채팅
		case 45545: // 흑장로
			if (skillid == 7031) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$2966", 0));
			} else if (skillid == 7030) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$3717", 0));
			}
			break;
		case 5096: // 린드 비오르
		case 5097:
		case 5098:
		case 5099:
		case 5100:
			if (skillid == 7018 || skillid == 7023) { // 광물골렘 소환
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10158", 0));
			} else if (skillid == 7001) { // 윈드 셰클
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10152", 0));
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 6));
				}
			} else if (skillid == 7013) { // 사일런스
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10152", 0));
				if (_target instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 8));
				}
			} else if (skillid == 7002 || skillid == 7004) { // 리콜 캔슬레이션, 웨폰
																// 브레이커
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10162", 0));
			} else if (skillid == 7003) { // 회오리 4개
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10150", 0));
			} else if (skillid == 7022) { // 발작
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10165", 0));
			} else if (skillid == 7023) { // 구름대정령
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10158", 0));
			} else if (skillid == 7009) { // 광역 마법 [전기]
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10162", 0));
			} else if (skillid == 7007) { // 윈드 세클
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10152", 0));
			} else if (skillid == 7010 || skillid == 7008) { // 캔슬레이션, 웨폰 브레이커
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$10162", 0));
			}
			break;
		case 7310015://제니스퀸 오만1층 보스
			if (skillid == 249) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "가소롭구나 먹이주제에", 0));
			} else if (skillid == 10004) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "네 이놈들 네 놈들을 모두 녹여버릴테다!", 0));
			}
			break;
		case 7310028://뱀파이어 오만3층 보스
			if (skillid == 10006) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이 정도로는 어림없습니다.", 0));
			} else if (skillid == 28) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이익..노예 주제에 건방지구나 죽어라!!", 0));
			}
			break;
		case 7310041://쿠거 오만5층 보스
			if (skillid == 10006) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이 정도로는 어림없습니다.", 0));
			} else if (skillid == 28) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이익..노예 주제에 건방지구나 죽어라!!", 0));
			}
			break;
		case 45601://본던 데스나이트
			if (skillid == 10056) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "환상에서 벗어날 시간이다..", 0));
			} else if (skillid == 28) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "크윽..나에게 치명상을 입히다니..", 0));
			}
			break;
		case 45752://발록 1차
		case 45753://발록 2차
			if (skillid == 80006) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$26286", 0));
			} else if (skillid == 10070) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$26285", 0));
			} else if (skillid == 10069) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$26285", 0));
			} else if (skillid == 707040) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$26284", 0));
			}
			break;
		case 7310092://화염의 지배자
			if (skillid == 707031) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "네 녀석이 어디까지 나를 방해할 수 있을까? 후후후..!", 0));
			} else if (skillid == 707027) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "어둠의 힘을 보여주마..", 0));
			}
			break;
		case 45610://거인 모닝스타
			if (skillid == 10099) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "모두 저주 할꺼야", 0));
			} else if (skillid == 10019) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "으으..이놈들 !!", 0));
			}
			break;
		case 7310051://아이리스 오만7층 보스
			if (skillid == 707071) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "내앞에 무릎을 꿇어라", 0));
			}
			break;
		case 7310056://나이트발드 오만8층 보스
			if (skillid == 22055) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이 정도로 나를 이길 수 있겠느냐?", 0));
			} else if (skillid == 27) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "진정한 기사의 힘을 보여주마 버텨보아라!!", 0));
			}
			break;
		case 7310061://리치 오만9층 보스
			if (skillid == 10010) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "죽음의 기운을 느껴보아라..", 0));
			} else if (skillid == 10061) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "영생을 얻는 나에게 도전하는가 !!", 0));
			}
			break;
		case 7310066://우그누스 오만10층 보스
			if (skillid == 20061) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "겨우 이 정도 힘으로 이 곳까지 왔느냐?", 0));
			} else if (skillid == 20091) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "나의 분노를 자극하지 마라", 0));
			}
			break;
		case 7310077://사신 그림 리퍼 오만정상 보스
			if (skillid == 10062) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "감히 신에게 도전하는가?", 0));
			} else if (skillid == 10012) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "힘의 차이를 느껴보아라", 0));
			}
			break;
		case 91200://대왕 오징어 보스
			if (skillid == 20086) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "죽는건 한 순간이다..어서 가거라!!", 0));
			} else if (skillid == 20089) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "곧 너희들은 죽을 것이다!!", 0));
			}
			break;
		// 에르자베
		case 5136:
			if (skillid == 7048) { // 토네이도
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14234", 0));
			} else if (skillid == 7044) { // 독구름
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14233", 0));
			} else if (skillid == 7049) { // 서먼
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14236", 0));
			} else if (skillid == 7042) { // 어스바인드
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14237", 0));
			} else if (skillid == 7041) { // 커스 패럴라이즈
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14238", 0));
			} else if (skillid == 7050) { // 모래 폭풍 소환
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14235", 0));
			}
			break;
		// 샌드웜
		case 5135:
			if (skillid == 7045) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14055", 0));
			} else if (skillid == 7046) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14056", 0));
			} else if (skillid == 7047) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14057", 0));
			} else if (skillid == 7044) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14058", 0));
			} else if (skillid == 7004) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			} else if (skillid == 7004) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			} else if (skillid == 7055) {// 이럽
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			} else if (skillid == 7056) {// 범우 스킬1
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			} else if (skillid == 7057) {// 범우 스킬2
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			} else if (skillid == 7058) {// 범우 스킬3
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "$14059", 0));
			}
			break;
		case 8500044://왜곡의 제니스 퀸
			if (skillid == 707076) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "네놈들의 선물. 잘 받겠다", 0));
			} else if (skillid == 707077) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "죽어라아아아!!!", 0));
			}
			break;
		case 8500060://공포의 뱀파이어
			if (skillid == 707079) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "날 자극하지 마라, 내 먹이가 되기 전에..", 0));
			} else if (skillid == 707180) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "네놈들이 자초한 일이다.. 모조리 불타올라라", 0));
			}
			break;
		case 8500063://죽음의 좀비 로드
			if (skillid == 64) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "살아있는 존재.. 죽인다.. 파묻는다..", 0));
			} else if (skillid == 707181) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "힘 없는... 육체로.. 날.. 죽일 수 없다..", 0));
			} else if (skillid == 0) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "죽인다.. 찢는다.. 장식한다.... 기쁘다..", 0));
			}
			break;
		case 8500071://지옥의 쿠거
			if (skillid == 7059) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "캬릉 도망가지 못한다!", 0));
			}
			break;
		case 8500090://잔혹한 아이리스
			if (skillid == 707082) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "얌전히 있어라.. 가지고 놀기 어렵단다..", 0));
			} else if (skillid == 707081) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "자아, 선물이다", 0));
			}
			break;
		case 8500095:// 어둠의 나이트 발드
			if (skillid == 220551) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "애송이들 싸움은 이렇게 하는 것이다", 0));
			} else if (skillid == 707083) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "내가 시범을 보여주지, 받아 보아라", 0));
			} else if (skillid == 7060) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "못봐주겠군, 이쯤에서 끝내주지", 0));
			}
			break;
		case 8500105:// 불멸의 리치
			if (skillid == 707086) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "어둠의 마법은 영원하다", 0));
			} else if (skillid == 707085) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "죽어라. 이것이 위대한 왕의 힘이다", 0));
			} else if (skillid == 707092) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "보아라, 너희들은 결코 날 죽일 수 없다.", 0));
			}
			break;
		case 8500111:// 오만한 우그누스
			if (skillid == 707090) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이것이 감히 신에게 도전하는 불손한 자에게 내리는 징벌이다", 0));
			} else if (skillid == 70241) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이것은 함부로 힘을 과신하는 오만한 자에게 내리는 징벌이다.", 0));
			} else if (skillid == 70241) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이것은 주제를 모르고 까부는 철없는 자에게 내리는 징벌이다.", 0));
			}
			break;
		case 8500129:// 사신 그림 리퍼
			if (skillid == 707091) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "오만한 자들이여 그 영혼을 내가 거두어주마", 0));
			} else if (skillid == 22056) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "불멸의 존재가 가진 무한한 힘을 맛보아라", 0));
			} else if (skillid == 22055) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "이제 마지막이다. 얌전히 탑의 재물이 되어라", 0));
			} else if (skillid == 707099) {
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, "오만한 자들이여 이곳이 너희들의 무덤이다.", 0));
			}
			break;
		case 7320219:
		case 45617: 
		case 45529:
			if(skillid == 707049 || skillid == 707050 || skillid == 707051 || skillid == 707052){
				boolean isuse = false;
				for(L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)){
					if(pc == null || pc.isDead())
						continue;
					
					if(skillUse.checkUseSkill(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL, _attacker)){
						isuse = true;
						skillUse.handleCommands(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL, _attacker);
					}
					
					if(isuse){
						if (getMobSkillTemplate().getLeverage(idx) > 0) {
							skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
						}
						_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
					}
				}
				return true;
			}
			break;
		default:
			break;
		}

		if (skillid > 0) {
			canUseSkill = skillUse.checkUseSkill(null, skillid, _target.getId(), _target.getX(), _target.getY(), null,0, L1SkillUse.TYPE_NORMAL, _attacker);
		}
		if (canUseSkill == true) {
			if (getMobSkillTemplate().getLeverage(idx) > 0) {
				skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
			}
			skillUse.handleCommands(null, skillid, _target.getId(), _target.getX(), _target.getY(), null, 0,L1SkillUse.TYPE_NORMAL, _attacker);
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
			if (skill.getTarget().equals("attack") && skillid != 18) {
				_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
			} else {
				_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
			}

			return true;
		}
		return false;
	}

	private boolean physicalAttack(int idx) {
		Map<Integer, Integer> targetList = new ConcurrentHashMap<Integer, Integer>();
		int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
		int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
		int range = getMobSkillTemplate().getRange(idx);
		int actId = getMobSkillTemplate().getActid(idx);
		int gfxId = getMobSkillTemplate().getGfxid(idx);

		if (_attacker.getLocation().getTileLineDistance(_target.getLocation()) > range) {
			return false;
		}

		if (!_attacker.glanceCheck(_target.getX(), _target.getY())) {
			return false;
		}

		_attacker.setHeading(_attacker.targetDirection(_target.getX(), _target.getY()));

		if (areaHeight > 0) {
			L1Character cha = null;
			ArrayList<L1Object> olist = L1World.getInstance().getVisibleBoxObjects(_attacker, _attacker.getHeading(), areaWidth,areaHeight);
			if(olist != null){
				for (L1Object obj : olist) {
					if (obj == null || !(obj instanceof L1Character))
						continue;

					cha = (L1Character) obj;
					if (cha.isDead())
						continue;

					if (!_attacker.glanceCheck(cha.getX(), cha.getY()))
						continue;
					
					if (_target instanceof L1PcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance)cha;
							if (pc.isGhost() || pc.isGmInvis())
								continue;
							
							if(_attacker instanceof L1SummonInstance || _attacker instanceof L1PetInstance || _target instanceof MJCompanionInstance){
								if (cha.getId() == _attacker.getMaster().getId() || cha.getZoneType() == 1)
									continue;
							}
						}else if(!(obj instanceof L1SummonInstance) && !(obj instanceof L1PetInstance) && !(obj instanceof MJCompanionInstance))
							continue;
						
						targetList.put(obj.getId(), 0);
					}else{
						if (obj instanceof L1MonsterInstance) {
							targetList.put(obj.getId(), 0);
						}
					}
				}
			}
		} else {
			targetList.put(_target.getId(), 0);
		}

		if (targetList.size() == 0) {
			return false;
		}

		Iterator<Integer> ite = targetList.keySet().iterator();
		L1Attack attack = null;

		EActionCodes actionCode = EActionCodes.attack;
		while (ite.hasNext()) {
			int targetId = ite.next();
			actionCode = EActionCodes.attack;
			attack = new L1Attack(_attacker, (L1Character) L1World.getInstance().findObject(targetId));
			if (attack.calcHit()) {
				if (getMobSkillTemplate().getLeverage(idx) > 0) {
					attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
				}
				attack.calcDamage();
			}
			if (actId > 0) {
				attack.setActId(actId);
			}
			
			if (targetId == _target.getId()) {
				if (gfxId > 0) {
					switch (_attacker.getNpcId()) {
					case 7320176: case 45263: case 7320180: case 7320182:
						_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), 18));
						ServerBasePacket[] pcks = new ServerBasePacket[] { new S_SkillSound(_target.getId(), gfxId),
								new S_DoActionGFX(_target.getId(), EActionCodes.damage.toInt()) };
						_target.broadcastPacket(pcks, false);
						_target.sendPackets(pcks[0], true);
						if (!_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
							_target.sendPackets(pcks[1], true);
						actionCode = EActionCodes.spell_dir;
						break;
					default:
						_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), gfxId));
						break;
					}
				}
				attack.action();
			}
			attack.commit();
		}

		_sleepTime = _attacker.getCurrentSpriteInterval(actionCode);
		return true;
	}

	private boolean isSkillUseble(int skillIdx) {
		boolean useble = false;

		if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
			int chance = _rnd.nextInt(100) + 1;
			if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
			int hpRatio = (_attacker.getCurrentHp() * 100) / _attacker.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
			L1NpcInstance companionNpc = searchMinCompanionHp();
			if (companionNpc == null) {
				return false;
			}

			int hpRatio = (companionNpc.getCurrentHp() * 100) / companionNpc.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerCompanionHp(skillIdx)) {
				useble = true;
				_target = companionNpc;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
			int distance = _attacker.getLocation().getTileLineDistance(_target.getLocation());

			if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
			if (getSkillUseCount(skillIdx) < getMobSkillTemplate().getTriggerCount(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}
		return useble;
	}

	private L1NpcInstance searchMinCompanionHp() {
		L1NpcInstance npc;
		L1NpcInstance minHpNpc = null;
		int hpRatio = 100;
		int companionHpRatio;
		int family = _attacker.getNpcTemplate().get_family();

		for (L1Object object : L1World.getInstance().getVisibleObjects(_attacker)) {
			if (object == null)
				continue;
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().get_family() == family) {
					companionHpRatio = (npc.getCurrentHp() * 100) / npc.getMaxHp();
					if (companionHpRatio < hpRatio) {
						hpRatio = companionHpRatio;
						minHpNpc = npc;
					}
				}
			}
		}
		return minHpNpc;
	}

	private void mobspawn(int summonId, int count) {
		int i;

		for (i = 0; i < count; i++) {
			mobspawn(summonId);
		}
	}

	private void mobspawn(int summonId) {
		try {
			L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
			if (spawnmonster != null) {
				L1NpcInstance mob = null;
				try {
					String implementationName = spawnmonster.getImpl();
					Constructor<?> _constructor = Class
							.forName((new StringBuilder()).append("l1j.server.server.model.Instance.")
									.append(implementationName).append("Instance").toString())
							.getConstructors()[0];
					mob = (L1NpcInstance) _constructor.newInstance(new Object[] { spawnmonster });
					mob.setId(IdFactory.getInstance().nextId());
					L1Location loc = _attacker.getLocation().randomLocation(8, false);
					int heading = _rnd.nextInt(8);
					mob.setX(loc.getX());
					mob.setY(loc.getY());
					mob.setHomeX(loc.getX());
					mob.setHomeY(loc.getY());
					short mapid = _attacker.getMapId();
					mob.setMap(mapid);
					mob.setHeading(heading);
					L1World.getInstance().storeObject(mob);
					L1World.getInstance().addVisibleObject(mob);
					L1Object object = L1World.getInstance().findObject(mob.getId());
					L1MonsterInstance newnpc = (L1MonsterInstance) object;
					newnpc.set_storeDroped(true);
					if (summonId == 45061 || summonId == 45161 || summonId == 45181 || summonId == 45455) {
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Hide));
						newnpc.setStatus(13);
						newnpc.broadcastPacket(S_WorldPutObject.get(newnpc));
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear));
						newnpc.setStatus(0);
						newnpc.broadcastPacket(S_WorldPutObject.get(newnpc));
					}
					newnpc.onNpcAI();
					newnpc.getLight().turnOnOffLight();
					newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private L1Character changeTarget(int type, int idx) {
		L1Character target;

		switch (type) {
		case L1MobSkill.CHANGE_TARGET_ME:
			target = _attacker;
			break;
		case L1MobSkill.CHANGE_TARGET_RANDOM:
			List<L1Character> targetList = new ArrayList<L1Character>();
			L1Character cha = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker)) {
				if (obj == null)
					continue;
				if (obj instanceof L1PcInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance || obj instanceof MJCompanionInstance) {
					cha = (L1Character) obj;
					int distance = _attacker.getLocation().getTileLineDistance(cha.getLocation());

					if (!getMobSkillTemplate().isTriggerDistance(idx, distance)) {
						continue;
					}

					if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
						continue;
					}

					if (!_attacker.getHateList().containsKey(cha)) {
						continue;
					}

					if (cha.isDead()) {
						continue;
					}

					if (cha instanceof L1PcInstance) {
						if (((L1PcInstance) cha).isGhost()) {
							continue;
						}
					}
					targetList.add((L1Character) obj);
				}
			}

			if (targetList.size() == 0) {
				target = _target;
			} else {
				int randomSize = targetList.size() * 100;
				int targetIndex = _rnd.nextInt(randomSize) / 100;
				target = targetList.get(targetIndex);
			}
			break;

		default:
			target = _target;
			break;
		}
		return target;
	}
}
