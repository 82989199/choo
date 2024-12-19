package l1j.server.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJKDASystem.MJKDA;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.poison.L1Poison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillTimer;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_PetCtrlMenu;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.IntRange;

public class L1Character extends L1Object {
	private static final long serialVersionUID = 1L;
	// 캐릭터 기본
	private String _name;
	private String _title;
	private int _level;
	private int _exp;
	private int _currentHp;
	private int _currentMp;
	private int _prevHp;
	private int _prevMp;
	private int _maxHp = 0;
	private int _trueMaxHp = 0;
	private int _maxMp = 0;
	private int _trueMaxMp = 0;
	private int _lawful;
	private int _karma;

	// 상태
	private int _heading; // ● 방향 0. 좌상 1. 상 2. 우상 3. 오른쪽 4. 우하 5. 하 6. 좌하 7. 좌
	private int _moveSpeed; // ● 스피드 0. 통상 1. 헤이 파업 2. 슬로우
	private int _braveSpeed; // ● 치우침 이브 상태 0. 통상 1. 치우침 이브
	private L1Poison _poison = null;
	private boolean _paralyzed;
	private boolean _sleeped;
	private L1Paralysis _paralysis;
	private boolean _isDead;

	protected Light light = null; // 캐릭터 주위 빛
	private MoveState moveState; // 이동속도, 바라보는 방향
	protected Ability ability = null; // 능력치
	protected Resistance resistance = null; // 저항 (마방, 불, 물, 바람, 땅, 스턴, 동빙, 슬립,
											// 석화)
	protected AC ac = null; // AC 방어

	// 모르는거
	private boolean _isSkillDelay = false;
	private int _addAttrKind;
	private int _status;

	// 대미지
	private int _dmgup = 0;
	private int _trueDmgup = 0;
	private int _bowDmgup = 0;
	private int _trueBowDmgup = 0;
	private int _hitup = 0;
	private int _trueHitup = 0;
	private int _bowHitup = 0;
	private int _trueBowHitup = 0;

	private int _sp = 0; // sp

	private int _missile_critical_rate = 0;
	private int _melee_critical_rate = 0;
	private int _magic_critical_rate = 0;

	// 그외
	private static Random _rnd = new Random(System.nanoTime());
	private final Map<Integer, L1NpcInstance> _petlist = new HashMap<Integer, L1NpcInstance>();
	private final Map<Integer, L1SupportInstance> _supportlist = new HashMap<Integer, L1SupportInstance>();
	private final Map<Integer, L1DollInstance> _dolllist = new HashMap<Integer, L1DollInstance>();
	private final Map<Integer, L1SkillTimer> _skillEffect = new HashMap<Integer, L1SkillTimer>();
	private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();
	private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<Integer, L1FollowerInstance>();

	// ■■■■■■■■■■ L1PcInstance에 이동하는 프롭퍼티 ■■■■■■■■■■
	private final List<L1Object> _knownObjects = new CopyOnWriteArrayList<L1Object>();
	private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();

	public boolean isChangedHp() {
		return _currentHp != _prevHp;
	}

	public boolean isChangedMp() {
		return _currentMp != _prevMp;
	}

	public boolean isChangedHpAndUpdate() {
		boolean isChanged = isChangedHp();
		if (isChanged)
			_prevHp = _currentHp;
		return isChanged;
	}

	public boolean isChangedMpAndUpdate() {
		boolean isChanged = isChangedMp();
		if (isChanged)
			_prevMp = _currentMp;
		return isChanged;
	}

	private ConcurrentHashMap<Integer, Integer> m_classResistance; // 내성 데이터
	private ConcurrentHashMap<Integer, Integer> m_classPierce; // 적중 데이터
	/*
	 * - 기술: 군주, 기사 // SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY - 정령: 요정, 다크엘프
	 * // SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT - 용언: 용기사, 환술사 //
	 * SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL - 공포: 전사 //
	 * SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR
	 */
	
	public boolean isPassive(int passiveId) {
		return false;
	}

	public void addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
		int val = getSpecialResistance(kind);

		if (m_classResistance == null)
			m_classResistance = new ConcurrentHashMap<Integer, Integer>(4);
		m_classResistance.put(kind.toInt(), value + val);
	}

	public int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
		if (m_classResistance == null || !m_classResistance.containsKey(kind.toInt()))
			return 0;

		return m_classResistance.get(kind.toInt());
	}

	public ConcurrentHashMap<Integer, Integer> getSpecialResistanceMap() {
		return m_classResistance;
	}

	public void addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
		int val = getSpecialPierce(kind);

		if (m_classPierce == null)
			m_classPierce = new ConcurrentHashMap<Integer, Integer>(4);
		m_classPierce.put(kind.toInt(), value + val);
	}

	public int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
		if (m_classPierce == null || !m_classPierce.containsKey(kind.toInt()))
			return 0;

		return m_classPierce.get(kind.toInt());
	}

	public ConcurrentHashMap<Integer, Integer> getSpecialPierceMap() {
		return m_classPierce;
	}

	public void dispose() {
		if (resistance != null) {
			resistance.dispose();
			resistance = null;
		}

		if (light != null) {
			light.dispose();// 오류
			light = null;
		}

		disposeShopInfo();
		_petlist.clear();
		_supportlist.clear();
		_dolllist.clear();
		clearSkillEffectTimer();
		_followerlist.clear();
		_itemdelay.clear();
	}

	public L1Character() {
		_level = 1;
		ability = new Ability(this);
		resistance = new Resistance(this);
		ac = new AC(this);
		moveState = new MoveState();
		light = new Light(this);
	}

	public double getCurrentHpPercent() {
		return (100D / (double) getMaxHp()) * (double) getCurrentHp();
	}

	public double getCurrentMpPercent() {
		return (100D / (double) getMaxMp()) * (double) getCurrentMp();
	}

	public int getLongLocation() {
		int pt = (getX() << 16) & 0xffff0000;
		pt |= (getY() & 0x0000ffff);
		return pt;
	}

	public int getLongLocationReverse() {
		int pt = (getY() << 16) & 0xffff0000;
		pt |= (getX() & 0x0000ffff);
		return pt;
	}

	private int _effectedDG = 0;
	private int _characterDG = 0;
	private int _effectedER = 0;
	private int _characterER = 0;

	public void addDg(int i) {
		_effectedDG += i;
	}

	// TODO : 값 변경시 true 반환
	public boolean setCharacterDG(int i) {
		int old = _characterDG;
		_characterDG = i;
		return old != i;
	}

	public int getDg() {
		return _effectedDG + _characterDG;
	}

	// TODO : 값 변경시 true 반환
	public boolean setCharacterER(int i) {
		int old = _characterER;
		_characterER = i;
		return old != i;
	}

	public int getStatER() {
		int er = 0;
		er = (getAbility().getTotalDex() - 8) / 2;
		return er;
	}

	public int getDefaultER() {
		int er = 0;
		int BaseEr = CalcStat.원거리회피(getAbility().getTotalDex());
		er += BaseEr;
		return er;
	}

	public int getEffectedER() {
		return _effectedER;
	}

	public void addEffectedER(int i) {
		_effectedER += i;
	}

	public int getTotalER() {
		int er = 0;
		er += getDefaultER();
		er += getEffectedER();
		er += _characterER;
		if (er < 0) {
			er = 0;
		} else {
			if (hasSkillEffect(L1SkillId.STRIKER_GALE)) {
				er = er / 3;
			}
		}
		return er;
	}

	/**
	 * 캐릭터를 부활시킨다.
	 * 
	 * @param hp
	 *            부활 후의 HP
	 */
	public void resurrect(int hp) {
		if (!isDead())
			return;
		if (hp <= 0)
			hp = 1;

		setCurrentHp(hp);
		setDead(false);
		setStatus(0);
		L1PolyMorph.undoPoly(this);

		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_RemoveObject(this));
			pc.removeKnownObject(this);
			pc.updateObject();
		}
	}

	/**
	 * 캐릭터의 현재의 HP를 돌려준다.
	 * 
	 * @return 현재의 HP
	 */
	public int getCurrentHp() {
		return _currentHp;
	}

	/**
	 * 캐릭터의 HP를 설정한다.
	 * 
	 * @param i
	 *            캐릭터의 새로운 HP
	 */
	public void setCurrentHp(int i) {
		if (i >= getMaxHp()) {
			i = getMaxHp();
		}
		if (i < 0)
			i = 0;
		_prevHp = _currentHp;
		_currentHp = i;
	}

	/**
	 * 캐릭터의 현재의 MP를 돌려준다.
	 * 
	 * @return 현재의 MP
	 */
	public int getCurrentMp() {
		return _currentMp;
	}

	/**
	 * 캐릭터의 MP를 설정한다.
	 * 
	 * @param i
	 *            캐릭터의 새로운 MP
	 */
	public void setCurrentMp(int i) {
		if (i >= getMaxMp()) {
			i = getMaxMp();
		}
		if (i < 0)
			i = 0;

		_prevMp = _currentMp;
		_currentMp = i;
	}

	/**
	 * 캐릭터의 잠상태를 돌려준다.
	 * 
	 * @return 잠상태를 나타내는 값. 잠상태이면 true.
	 */
	public boolean isSleeped() {
		return _sleeped;
	}

	/**
	 * 캐릭터의 잠상태를 설정한다.
	 * 
	 * @param sleeped
	 *            잠상태를 나타내는 값. 잠상태이면 true.
	 */
	public void setSleeped(boolean sleeped) {
		_sleeped = sleeped;
	}

	/**
	 * 캐릭터의 마비 상태를 돌려준다.
	 * 
	 * @return 마비 상태를 나타내는 값. 마비 상태이면 true.
	 */
	public boolean isParalyzed() {
		return _paralyzed;
	}

	/**
	 * 캐릭터의 마비 상태를 돌려준다.
	 * 
	 * @return 마비 상태를 나타내는 값. 마비 상태이면 true.
	 */
	public void setParalyzed(boolean paralyzed) {
		_paralyzed = paralyzed;
	}

	///////////////// 임시임시임시
	private boolean _thunderGrab;

	public boolean isThunderGrab() {
		return _thunderGrab;
	}

	public void setThunderGrab(boolean thunderGrab) {
		_thunderGrab = thunderGrab;
	}

	public L1Paralysis getParalysis() {
		return _paralysis;
	}

	public void setParalaysis(L1Paralysis p) {
		_paralysis = p;
	}

	public void cureParalaysis() {
		if (_paralysis != null) {
			_paralysis.cure();
		}
	}

	/**
	 * 캐릭터의 가시 범위에 있는 플레이어에, 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacket(MJIProtoMessage message, MJEProtoMessages message_id, boolean is_clear, boolean is_this_send){		
		if(message.isInitialized()){
			broadcastPacket(message.writeTo(message_id), is_clear, is_this_send);
		}else{
			MJEProtoMessages.printNotInitialized("", message_id.toInt(), message.getInitializeBit());
		}
	}
	
	public void broadcastPacket(ProtoOutputStream stream, boolean is_clear, boolean is_this_send){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(stream, false);
		}
		if(is_this_send){
			sendPackets(stream, false);
		}
		if(is_clear)
			stream.dispose();
	}
	
	public void broadcastPacket(ProtoOutputStream[] streams, boolean is_clear, boolean is_this_send){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for(ProtoOutputStream stream : streams)
				pc.sendPackets(stream, false);
		}
		if(is_this_send){
			for(ProtoOutputStream stream : streams)
				sendPackets(stream, false);
		}
		if(is_clear){
			for(ProtoOutputStream stream : streams)
				stream.dispose();
		}
	}
	
	public void broadcastPacket(ServerBasePacket[] pcs) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for (int i = 0; i < pcs.length; i++)
				pc.sendPackets(pcs[i], false);
		}

		for (int i = 0; i < pcs.length; i++)
			pcs[i].clear();
	}

	public void broadcastPacket(ServerBasePacket[] pcs, boolean isClear) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for (int i = 0; i < pcs.length; i++)
				pc.sendPackets(pcs[i], false);
		}

		if (isClear) {
			for (int i = 0; i < pcs.length; i++)
				pcs[i].clear();
		}
	}

	public void broadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}
	
	public void broadcastPacket_party(ServerBasePacket packet, L1Party party) {
		if(party == null) {packet.clear();return;}
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if(party != null && party.isMember(pc)){	
				pc.sendPackets(packet, false);
			}
		}
		packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, boolean isClear) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		if (isClear)
			packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, L1Character target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, L1Character[] target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	/**
	 * 캐릭터의 가시 범위에 있는 플레이어에, 패킷을 송신한다. 다만 타겟의 화면내에는 송신하지 않는다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayerExceptTargetSight(this, target)) {
			if (pc.knownsObject(this)) {
				pc.sendPackets(packet, false);
			}
		}
		packet.clear();
	}

	/**
	 * 캐릭터의 50 매스 이내에 있는 플레이어에, 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void wideBroadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 50)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	/**
	 * 캐릭터의 정면의 좌표를 돌려준다.
	 * 
	 * @return 정면의 좌표
	 */
	public int[] getFrontLoc() {
		int[] loc = new int[2];
		int x = getX();
		int y = getY();
		int heading = getHeading();
		switch (heading) {
		case 0: {
			y--;
		}
			break;
		case 1: {
			x++;
			y--;
		}
			break;
		case 2: {
			x++;
		}
			break;
		case 3: {
			x++;
			y++;
		}
			break;
		case 4: {
			y++;
		}
			break;
		case 5: {
			x--;
			y++;
		}
			break;
		case 6: {
			x--;
		}
			break;
		case 7: {
			x--;
			y--;
		}
			break;
		default:
			break;
		}
		loc[0] = x;
		loc[1] = y;
		return loc;
	}

	/**
	 * 지정된 좌표에 대할 방향을 돌려준다.
	 * 
	 * @param tx
	 *            좌표의 X치
	 * @param ty
	 *            좌표의 Y치
	 * @return 지정된 좌표에 대할 방향
	 */
	public int targetDirection(int tx, int ty) {
		float dis_x = Math.abs(getX() - tx); // X방향의 타겟까지의 거리
		float dis_y = Math.abs(getY() - ty); // Y방향의 타겟까지의 거리
		float dis = Math.max(dis_x, dis_y); // 타겟까지의 거리

		if (dis == 0)
			return getHeading();

		int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 상하 좌우가 조금 우선인 둥근
		int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 상하 좌우가 조금 우선인 둥근

		int dir_x = 0;
		int dir_y = 0;

		if (getX() < tx)
			dir_x = 1;
		if (getX() > tx)
			dir_x = -1;

		if (getY() < ty)
			dir_y = 1;
		if (getY() > ty)
			dir_y = -1;

		if (avg_x == 0)
			dir_x = 0;
		if (avg_y == 0)
			dir_y = 0;

		if (dir_x == 1 && dir_y == -1)
			return 1; // 상
		if (dir_x == 1 && dir_y == 0)
			return 2; // 우상
		if (dir_x == 1 && dir_y == 1)
			return 3; // 오른쪽
		if (dir_x == 0 && dir_y == 1)
			return 4; // 우하
		if (dir_x == -1 && dir_y == 1)
			return 5; // 하
		if (dir_x == -1 && dir_y == 0)
			return 6; // 좌하
		if (dir_x == -1 && dir_y == -1)
			return 7; // 왼쪽
		if (dir_x == 0 && dir_y == -1)
			return 0; // 좌상

		return getHeading();
	}

	/**
	 * 지정된 좌표까지의 직선상에, 장애물이 존재*하지 않는가*를 돌려준다.
	 * 
	 * @param tx
	 *            좌표의 X치
	 * @param ty
	 *            좌표의 Y치
	 * @return 장애물이 없으면 true, 어느 false를 돌려준다.
	 */
	public boolean glanceCheck(int tx, int ty) {
		L1Map map = getMap();
		int chx = getX();
		int chy = getY();
		for (int i = 0; i < 15; i++) {
			/*
			 * if(chx == tx && chy == ty) break;
			 * 
			 * if(!map.isArrowPassable(chx, chy, MJBotUtil.calcheading(chx, chy,
			 * tx, ty))) return false;
			 */
			/*
			 * if ((chx == tx && chy == ty) || (chx == tx && chy + 1 == ty) ||
			 * (chx == tx && chy - 1 == ty) || (chx + 1 == tx && chy == ty) ||
			 * (chx + 1 == tx && chy + 1 == ty) || (chx + 1 == tx && chy - 1 ==
			 * ty) || (chx - 1 == tx && chy == ty) || (chx - 1 == tx && chy + 1
			 * == ty) || (chx - 1 == tx && chy - 1 == ty)) { // -1 -1 break; }
			 */

			int cx = Math.abs(chx - tx);
			int cy = Math.abs(chy - ty);
			if (cx <= 1 && cy <= 1)
				break;

			if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty)))
				return false;

			if (chx < tx)
				chx++;
			else if (chx > tx)
				chx--;
			if (chy < ty)
				chy++;
			else if (chy > ty)
				chy--;

			/*
			 * if (chx < tx && chy == ty) { chx++; } else if (chx > tx && chy ==
			 * ty) { chx--; } else if (chx == tx && chy < ty) { chy++; } else if
			 * (chx == tx && chy > ty) { chy--; } else if (chx < tx && chy < ty)
			 * { chx++; chy++; } else if (chx < tx && chy > ty) { chx++; chy--;
			 * } else if (chx > tx && chy < ty) { chx--; chy++; } else if (chx >
			 * tx && chy > ty) { chx--; chy--; }
			 */
		}

		return true;
	}

	/**
	 * 지정된 좌표에 공격 가능한가를 돌려준다.
	 * 
	 * @param x
	 *            좌표의 X치.
	 * @param y
	 *            좌표의 Y치.
	 * @param range
	 *            공격 가능한 범위(타일수)
	 * @return 공격 가능하면 true, 불가능하면 false
	 */
	public boolean isAttackPosition(int x, int y, int range) {
		if (range >= 7) {// 원격 무기(7이상의 경우 기울기를 고려하면(자) 화면외에 나온다)
			if (getLocation().getTileDistance(new Point(x, y)) > range)
				return false;
		} else {
			if (getLocation().getTileLineDistance(new Point(x, y)) > range)
				return false;
		}

		return glanceCheck(x, y);
	}

	public boolean isAttackPosition(L1Character target, int range) {
		if (range >= 7) {// 원격 무기(7이상의 경우 기울기를 고려하면(자) 화면외에 나온다)
			if (getLocation().getTileDistance(target.getLocation()) > range)
				return false;
		} else {
			if (getLocation().getTileLineDistance(target.getLocation()) > range)
				return false;
		}

		return glanceCheck(target.getX(), target.getY()) && target.glanceCheck(getX(), getY());
	}

	/**
	 * 캐릭터의 목록을 돌려준다.
	 * 
	 * @return 캐릭터의 목록을 나타내는, L1Inventory 오브젝트.
	 */
	public L1Inventory getInventory() {
		return null;
	}

	/**
	 * 캐릭터에, 새롭게 스킬 효과를 추가한다.
	 * 
	 * @param skillId
	 *            추가하는 효과의 스킬 ID.
	 * @param timeMillis
	 *            추가하는 효과의 지속 시간. 무한의 경우는 0.
	 */
	private void addSkillEffect(int skillId, int timeMillis) {
		L1SkillTimer timer = null;
		if (0 < timeMillis) {
			timer = new L1SkillTimer(this, skillId, timeMillis);
			timer.begin();
		}
		_skillEffect.put(skillId, timer);
	}

	/**
	 * 캐릭터에, 스킬 효과를 설정한다. <br>
	 * 중복 하는 스킬이 없는 경우는, 새롭게 스킬 효과를 추가한다. <br>
	 * 중복 하는 스킬이 있는 경우는, 나머지 효과 시간과 파라미터의 효과 시간의 긴 (분)편을 우선해 설정한다.
	 * 
	 * @param skillId
	 *            설정하는 효과의 스킬 ID.
	 * @param timeMillis
	 *            설정하는 효과의 지속 시간. 무한의 경우는 0.
	 */
	public void setSkillEffect(int skillId, int timeMillis) {
		if (hasSkillEffect(skillId)) {
			int remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000;
			if (remainingTimeMills >= 0 && (remainingTimeMills < timeMillis || timeMillis == 0)) {
				if (skillId == L1SkillId.DESPERADO)
					removeSkillEffect(skillId);
				else
					killSkillEffectTimer(skillId);
				addSkillEffect(skillId, timeMillis);
			}
		} else {
			addSkillEffect(skillId, timeMillis);
		}
	}

	/**
	 * 캐릭터로부터, 스킬 효과를 삭제한다.
	 * 
	 * @param skillId
	 *            삭제하는 효과의 스킬 ID
	 */
	public void removeSkillEffect(int skillId) {
		L1SkillTimer timer = _skillEffect.remove(skillId);
		if (timer != null) {
			timer.end();
		}
	}

	/**
	 * 캐릭터로부터, 스킬 효과의 타이머를 삭제한다. 스킬 효과는 삭제되지 않는다.
	 * 
	 * @param skillId
	 *            삭제하는 타이머의 스킬 ID
	 */
	public void killSkillEffectTimer(int skillId) {
		L1SkillTimer timer = _skillEffect.remove(skillId);
		if (timer != null)
			timer.kill();
	}

	/**
	 * 캐릭터로부터, 모든 스킬 효과 타이머를 삭제한다. 스킬 효과는 삭제되지 않는다.
	 */
	public void clearSkillEffectTimer() {
		for (L1SkillTimer timer : _skillEffect.values()) {
			if (timer != null) {
				timer.kill();
			}
		}
		_skillEffect.clear();
	}

	/**
	 * 캐릭터에, 해당 스킬 효과가 걸려있는지 알려줌
	 * 
	 * @param skillId
	 *            스킬 ID
	 * @return 마법 효과가 있으면 true, 없으면 false.
	 */
	public boolean hasSkillEffect(int skillId) {
		return _skillEffect.containsKey(skillId);
	}

	/**
	 * 캐릭터의 스킬 효과의 지속 시간을 돌려준다.
	 * 
	 * @param skillId
	 *            조사하는 효과의 스킬 ID
	 * @return 스킬 효과의 남은 시간(초). 스킬이 걸리지 않은가 효과 시간이 무한의 경우,-1.
	 */
	public int getSkillEffectTimeSec(int skillId) {
		L1SkillTimer timer = _skillEffect.get(skillId);
		if (timer == null) {
			return -1;
		}
		return timer.getRemainingTime();
	}

	/**
	 * 캐릭터에, skill delay 추가
	 * 
	 * @param flag
	 */
	public void setSkillDelay(boolean flag) {
		_isSkillDelay = flag;
	}

	/**
	 * 캐릭터의 독 상태를 돌려준다.
	 * 
	 * @return 스킬 지연중인가.
	 */
	public boolean isSkillDelay() {
		return _isSkillDelay;
	}

	/**
	 * 캐릭터에, Item delay 추가
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 * @param timer
	 *            지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer 오브젝트.
	 */
	public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
		_itemdelay.put(delayId, timer);
	}

	/**
	 * 캐릭터로부터, Item delay 삭제
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 */
	public void removeItemDelay(int delayId) {
		_itemdelay.remove(delayId);
	}

	/**
	 * 캐릭터에, Item delay 이 있을까
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연이 있으면 true, 없으면 false.
	 */
	public boolean hasItemDelay(int delayId) {
		return _itemdelay.containsKey(delayId);
	}

	/**
	 * 캐릭터의 item delay 시간을 나타내는, L1ItemDelay.ItemDelayTimer를 돌려준다.
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer.
	 */
	public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
		return _itemdelay.get(delayId);
	}

	/**
	 * 캐릭터에, pet, summon monster, tame monster, created zombie 를 추가한다.
	 * 
	 * @param npc
	 *            추가하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void addPet(L1NpcInstance npc) {
		_petlist.put(npc.getId(), npc);
		sendPetCtrlMenu(npc, true);
	}

	/**
	 * 캐릭터로부터, pet, summon monster, tame monster, created zombie 를 삭제한다.
	 * 
	 * @param npc
	 *            삭제하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void removePet(L1NpcInstance npc) {
		_petlist.remove(npc.getId());
		sendPetCtrlMenu(npc, true);
	}

	/**
	 * 캐릭터의 애완동물 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 애완동물 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1NpcInstance.
	 */
	public Map<Integer, L1NpcInstance> getPetList() {
		return _petlist;
	}

	/**
	 * 캐릭터에 doll을 추가한다.
	 * 
	 * @param doll
	 *            추가하는 doll를 나타내는, L1DollInstance 오브젝트.
	 */
	public void addDoll(L1DollInstance doll) {
		_dolllist.put(doll.getId(), doll);
	}

	/**
	 * 캐릭터로부터 dool을 삭제한다.
	 * 
	 * @param doll
	 *            삭제하는 doll를 나타내는, L1DollInstance 오브젝트.
	 */
	public void removeDoll(L1DollInstance doll) {
		_dolllist.remove(doll.getId());
	}

	/**
	 * 캐릭터의 doll 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 마법인형 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1DollInstance.
	 */
	public Map<Integer, L1DollInstance> getDollList() {
		return _dolllist;
	}

	/**
	 * 캐릭터에 쫄법사을 추가한다.
	 * 
	 * @param doll
	 *            추가하는 doll를 나타내는, L1DollInstance 오브젝트.
	 */
	public void addSupport(L1SupportInstance support) {
		_supportlist.put(support.getId(), support);
	}

	/**
	 * 캐릭터로부터 쫄법사을 삭제한다.
	 * 
	 * @param doll
	 *            삭제하는 doll를 나타내는, L1DollInstance 오브젝트.
	 */
	public void removeSupport(L1SupportInstance support) {
		_supportlist.remove(support.getId());
	}

	/**
	 * 캐릭터의 쫄법사 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 마법인형 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1DollInstance.
	 */
	public Map<Integer, L1SupportInstance> getSupportList() {
		return _supportlist;
	}

	/**
	 * 캐릭터에 이벤트 NPC(캐릭터를 따라다니는)를 추가한다.
	 * 
	 * @param follower
	 *            추가하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void addFollower(L1FollowerInstance follower) {
		_followerlist.put(follower.getId(), follower);
	}

	/**
	 * 캐릭터로부터 이벤트 NPC(캐릭터를 따라다니는)를 삭제한다.
	 * 
	 * @param follower
	 *            삭제하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void removeFollower(L1FollowerInstance follower) {
		_followerlist.remove(follower.getId());
	}

	/**
	 * 캐릭터의 이벤트 NPC(캐릭터를 따라다니는) 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 종자 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1FollowerInstance.
	 */
	public Map<Integer, L1FollowerInstance> getFollowerList() {
		return _followerlist;
	}

	/**
	 * 캐릭터에, 독을 추가한다.
	 * 
	 * @param poison
	 *            독을 나타내는, L1Poison 오브젝트.
	 */
	public void setPoison(L1Poison poison) {
		_poison = poison;
	}

	/**
	 * 캐릭터의 독을 치료한다.
	 */
	public void curePoison() {
		if (_poison == null) {
			return;
		}
		_poison.cure();
	}

	/**
	 * 캐릭터의 독상태를 돌려준다.
	 * 
	 * @return 캐릭터의 독을 나타내는, L1Poison 오브젝트.
	 */
	public L1Poison getPoison() {
		return _poison;
	}

	/**
	 * 캐릭터에 독의 효과를 부가한다
	 * 
	 * @param effectId
	 * @see S_Poison#S_Poison(int, int)
	 */
	public void setPoisonEffect(int effectId) {
		broadcastPacket(new S_Poison(getId(), effectId));
	}

	/**
	 * 캐릭터가 존재하는 좌표가, 어느 존에 속하고 있을까를 돌려준다.
	 * 
	 * @return 좌표의 존을 나타내는 값. 세이프티 존이면 1, 컴배트 존이면 -1, 노멀 존이면 0.
	 */

	public int getZoneType() {
		if (getMap().isSafetyZone(getLocation())) {
			/** 배틀존 **/
			if (getMapId() == 5153) {
				return -1;
			} else {
				return 1;
			}
		} else if (getMap().isCombatZone(getLocation())) {
			return -1;
		} else { // 노멀존
			return 0;
		}
	}

	public int getExp() {
		return _exp;
	}

	public void setExp(int exp) {
		_exp = exp;
	}

	/**
	 * 지정된 오브젝트를, 캐릭터가 인식하고 있을까를 돌려준다.
	 * 
	 * @param obj
	 *            조사하는 오브젝트.
	 * @return 오브젝트를 캐릭터가 인식하고 있으면 true, 하고 있지 않으면 false. 자기 자신에 대해서는 false를
	 *         돌려준다.
	 */
	public boolean knownsObject(L1Object obj) {
		return _knownObjects.contains(obj);
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 오브젝트를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1Object>.
	 */
	public List<L1Object> getKnownObjects() {
		return _knownObjects;
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 플레이어를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1PcInstance>
	 */
	public List<L1PcInstance> getKnownPlayers() {
		return _knownPlayer;
	}

	/**
	 * 캐릭터에, 새롭게 인식하는 오브젝트를 추가한다.
	 * 
	 * @param obj
	 *            새롭게 인식하는 오브젝트.
	 */
	public void addKnownObject(L1Object obj) {
		if (!_knownObjects.contains(obj)) {
			_knownObjects.add(obj);
			if (obj instanceof L1PcInstance) {
				_knownPlayer.add((L1PcInstance) obj);
			}
		}
	}

	/**
	 * 캐릭터로부터, 인식하고 있는 오브젝트를 삭제한다.
	 * 
	 * @param obj
	 *            삭제하는 오브젝트.
	 */
	public void removeKnownObject(L1Object obj) {
		if (_knownObjects.contains(obj))
			_knownObjects.remove(obj);
		if (obj instanceof L1PcInstance) {
			if (_knownPlayer.contains((L1PcInstance) obj))
				_knownPlayer.remove((L1PcInstance) obj);
		}
	}

	/**
	 * 캐릭터로부터, 모든 인식하고 있는 오브젝트를 삭제한다.
	 */
	/*
	 * public void removeAllKnownObjects() { _knownObjects.clear();
	 * _knownPlayer.clear(); }
	 */

	public void removeAllKnownObjects() {
		if (_knownObjects.size() > 0)
			_knownObjects.clear();
		if (_knownPlayer.size() > 0)
			_knownPlayer.clear();
	}

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	public int getLevel() {
		return _level;
	}

	public synchronized void setLevel(long level) {
		_level = (int) level;
	}

	public int getMaxHp() {

		if (getAbility() == null)
			return _maxHp;

		int maxhp = _maxHp;
		if(this instanceof L1PcInstance){
			if (getAbility().getTotalCon() >= 25)
				maxhp += 50;
			if (getAbility().getTotalCon() >= 35)
				maxhp += 100;
			if (getAbility().getTotalCon() >= 45)
				maxhp += 150;
		}
		return maxhp;
	}

	/*
	 * public int getMaxHp() { return _maxHp; }
	 */

	/**최대 HP 추가**/
	public void addMaxHp(int i) {
		setMaxHp(_trueMaxHp + i);
	}

	public void setMaxHp(int hp) {
		_trueMaxHp = hp;
		_maxHp = (int) IntRange.ensure(_trueMaxHp, 1, 10000000);
		_currentHp = Math.min(_currentHp, _maxHp);

	}

	// TODO 스텟에 따른 MAXMP 증가
	public int getMaxMp() {
		if (getAbility() == null)
			return _maxMp;

		int maxmp = _maxMp;
		int wis = getAbility().getTotalWis();
		if (wis >= 25)
			maxmp += 50;
		if (wis >= 35)
			maxmp += 100;
		if (wis >= 45)
			maxmp += 150;
		return maxmp;
	}

	/*
	 * public int getMaxMp() { //원본 return _maxMp; }
	 */

	public void setMaxMp(int mp) {
		_trueMaxMp = mp;
		_maxMp = (int) IntRange.ensure(_trueMaxMp, 0, 10000000);
		_currentMp = Math.min(_currentMp, _maxMp);
	}

	/**최대 MP 추가**/
	public void addMaxMp(int i) {
		setMaxMp(_trueMaxMp + i);
	}

	public void healHp(int pt) {
		setCurrentHp(getCurrentHp() + pt);
	}

	public int getAddAttrKind() {
		return _addAttrKind;
	}

	public void setAddAttrKind(int i) {
		_addAttrKind = i;
	}

	public int getDmgup() {
		return _dmgup;
	}

	/**근거리 대미지 추가**/
	public void addDmgup(int i) {
		_trueDmgup += i;
		if (_trueDmgup >= 127) {
			_dmgup = 127;
		} else if (_trueDmgup <= -128) {
			_dmgup = -128;
		} else {
			_dmgup = _trueDmgup;
		}
	}

	public int getBowDmgup() {
		return _bowDmgup;
	}

	/**원거리 대미지 추가**/
	public void addBowDmgup(int i) {
		_trueBowDmgup += i;
		if (_trueBowDmgup >= 127) {
			_bowDmgup = 127;
		} else if (_trueBowDmgup <= -128) {
			_bowDmgup = -128;
		} else {
			_bowDmgup = _trueBowDmgup;
		}
	}

	public int getHitup() {
		return _hitup;
	}

	/**근거리 명중 추가**/
	public void addHitup(int i) {
		_trueHitup += i;
		if (_trueHitup >= 127) {
			_hitup = 127;
		} else if (_trueHitup <= -128) {
			_hitup = -128;
		} else {
			_hitup = _trueHitup;
		}
	}

	public int getBowHitup() {
		return _bowHitup;
	}

	/**원거리 명중 추가**/
	public void addBowHitup(int i) {
		_trueBowHitup += i;
		if (_trueBowHitup >= 127) {
			_bowHitup = 127;
		} else if (_trueBowHitup <= -128) {
			_bowHitup = -128;
		} else {
			_bowHitup = _trueBowHitup;
		}
	}

	public void addSp(int i) {
		_sp += i;
	}

	public int getSp() {
		return getTrueSp() + _sp;
	}

	public int getTrueSp() {
		return getMagicLevel() + getMagicBonus();
	}

	public int getMagicLevel() {
		return getLevel() / 4;
	}

	public int getMagicBonus() {
		int i = getAbility().getTotalInt();
		if (i <= 5)
			return -2;
		else if (i <= 8)
			return -1;
		else if (i <= 11)
			return 0;
		else if (i <= 14)
			return 1;
		else if (i <= 17)
			return 2;
		else
			return i - 15;
	}

	// 분신 스킬 중첩 방지
	private long _doppeltime = 0;

	public long getDoppelTime() {
		return _doppeltime;
	}

	public void setDoppelTime(long l) {
		_doppeltime = l;
	}

	public boolean isDead() {
		return _isDead;
	}

	public void setDead(boolean flag) {
		_isDead = flag;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int i) {
		_status = i;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String s) {
		_title = s;
	}

	public int getLawful() {
		return _lawful;
	}

	public void setLawful(int i) {
		_lawful = i;
	}

	public synchronized void addLawful(int i) {
		if (Config.STANDBY_SERVER)
			return;

		_lawful += i;
		if (_lawful > 32767) {
			_lawful = 32767;
		} else if (_lawful < -32768) {
			_lawful = -32768;
		}
	}

	public int getHeading() {
		return _heading;
	}

	public void setHeading(int i) {
		_heading = i;
	}

	public int getMoveSpeed() {
		return _moveSpeed;
	}

	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}

	public int getBraveSpeed() {
		return _braveSpeed;
	}

	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}

	public boolean isInvisble() {
		return (hasSkillEffect(L1SkillId.INVISIBILITY) || hasSkillEffect(L1SkillId.BLIND_HIDING));
	}

	/** 캐릭터의 업을 돌려준다. */
	public int getKarma() {
		return _karma;
	}

	/** 캐릭터의 업을 설정한다. */
	public void setKarma(int karma) {
		_karma = karma;
	}

	private MJKDA _kda;

	public MJKDA getKDA() {
		return _kda;
	}

	public void setKDA(MJKDA kda) {
		_kda = kda;
	}

	// ** 도우너 딜레이 타이머 수정 **// by 도우너
	private long _skilldelay2;

	public long getSkilldelay2() {
		return _skilldelay2;
	}

	public void setSkilldelay2(long skilldelay2) {
		_skilldelay2 = skilldelay2;
	}
	
	
	private int _Spelldelay;  
	public int getSpelldelay()				{ return _Spelldelay; 		 } 
	public void setSpelldelay(int Spelldelay){ _Spelldelay= Spelldelay; } 

	// **지엠 버프 따로 저장 **// by 도우너
	private int _buffnoch;

	public int getBuffnoch() {
		return _buffnoch;
	}

	public void setBuffnoch(int buffnoch) {
		_buffnoch = buffnoch;
	}

	public static Random getRnd() {
		return _rnd;
	}

	public Light getLight() {
		return light;
	}

	/**능력치를 얻다**/
	public Ability getAbility() {
		return ability;
	}

	/**저항을 얻다**/
	public Resistance getResistance() {
		return resistance;
	}

	public AC getAC() {
		return ac;
	}

	public MoveState getMoveState() {
		return moveState;
	}

	private int _mr = 0; // ● 마법 방어(0)
	private int _trueMr = 0; // ● 정말의 마법 방어

	public int getMr() {
		if (hasSkillEffect(153) == true) {
			return _mr / 4;
		} else {
			return _mr;
		}
	} // 사용할 때

	public int getTrueMr() {
		return _trueMr;
	} // 세트 할 때

	public void addMr(int i) {
		_trueMr += i;
		if (_trueMr <= 0) {
			_mr = 0;
		} else {
			_mr = _trueMr;
		}
	}

	private int 락구간상승;

	public int get락구간상승() {
		return 락구간상승;
	}

	public void set락구간상승(int i) {
		락구간상승 = i;
	}

	/** ui6 관련 펫파티,컨트롤 **/
	public void sendPetCtrlMenu(L1NpcInstance npc, boolean type) {
		if (npc instanceof L1PetInstance) {
			L1PetInstance pet = (L1PetInstance) npc;
			L1Character cha = pet.getMaster();
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, npc, type));
			}
		} else if (npc instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) npc;
			L1Character cha = summon.getMaster();
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, npc, type));
			}
		}
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	private ArrayList<MJDShopItem> _sellings; // 판매 목록
	private ArrayList<MJDShopItem> _purchasings; // 매입 목록

	public void disposeShopInfo() {
		disposeSellings();
		disposePurchasings();
	}

	private MJDShopItem findDShopItem(ArrayList<MJDShopItem> list, int objid) {
		if (list == null)
			return null;

		int size = list.size();
		MJDShopItem item = null;
		for (int i = 0; i < size; i++) {
			item = list.get(i);
			if (item.objId == objid)
				return item;
		}
		return null;
	}

	public MJDShopItem findSellings(int objid) {
		return findDShopItem(_sellings, objid);
	}

	public void updateSellings(int objid, int count) {
		MJDShopItem item = findSellings(objid);
		if (item == null)
			return;

		if (item.count <= count) {
			_sellings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		} else {
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}

	public ArrayList<MJDShopItem> getSellings() {
		return _sellings;
	}

	public void setSellings(ArrayList<MJDShopItem> list) {
		_sellings = list;
	}

	public void addSellings(MJDShopItem item) {
		if (_sellings == null)
			_sellings = new ArrayList<MJDShopItem>(7);
		_sellings.add(item);

	}

	public void disposeSellings() {
		if (_sellings != null) {
			_sellings.clear();
			_sellings = null;
		}
	}

	public MJDShopItem findPurchasings(int objid) {
		return findDShopItem(_purchasings, objid);
	}

	public void updatePurchasings(int objid, int count) {
		MJDShopItem item = findPurchasings(objid);
		if (item == null)
			return;

		if (item.count <= count) {
			_purchasings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		} else {
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}

	public ArrayList<MJDShopItem> getPurchasings() {
		return _purchasings;
	}

	public void setPurchasings(ArrayList<MJDShopItem> list) {
		_purchasings = list;
	}

	public void addPurchasings(MJDShopItem item) {
		if (_purchasings == null)
			_purchasings = new ArrayList<MJDShopItem>(7);
		_purchasings.add(item);
	}

	public void disposePurchasings() {
		if (_purchasings != null) {
			_purchasings.clear();
			_purchasings = null;
		}
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/

	private MJBotAI _botAI;

	public MJBotAI getAI() {
		return _botAI;
	}

	public void setAI(MJBotAI ai) {
		_botAI = ai;
	}

	public boolean isHaste() {
		return (hasSkillEffect(L1SkillId.STATUS_HASTE) || hasSkillEffect(L1SkillId.HASTE)
				|| hasSkillEffect(L1SkillId.GREATER_HASTE) || getMoveSpeed() == 1);
	}

	public int _트루타켓 = 0;

	public int get트루타켓() {
		return _트루타켓;
	}

	public void set트루타켓(int i) {
		_트루타켓 = i;
	}

	public void sendPackets(ServerBasePacket pck, boolean clear) {
		if (clear)
			pck.clear();
	}

	public void sendPackets(ProtoOutputStream stream, boolean is_clear){
		if(is_clear)
			stream.dispose();
	}
	
	public void receiveDamage(L1Character attacker, int damage) {

	}

	public int Desperadolevel = 0;

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_CHARACTER : _instanceType;
	}

	public void sendPackets(ServerBasePacket sbp) {
		sbp.clear();
		sbp = null;
	}

	protected SpriteInformation _currentSpriteInfo;

	public int getCurrentSpriteId() {
		return _currentSpriteInfo == null ? 1120 : _currentSpriteInfo.getSpriteId();
	}

	public SpriteInformation getCurrentSprite() {
		return _currentSpriteInfo;
	}

	public void setCurrentSprite(int spriteId) {
		if (!equalsCurrentSprite(spriteId))
			_currentSpriteInfo = SpriteInformationLoader.getInstance().get(spriteId);
	}

	public boolean equalsCurrentSprite(int compareSpriteId) {
		return getCurrentSpriteId() == compareSpriteId;
	}

	public long getCurrentSpriteInterval(EActionCodes actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	public long getCurrentSpriteInterval(int actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	public void sendShape(int poly) {
		S_ChangeShape shape = new S_ChangeShape(getId(), poly, 0);
		sendPackets(shape, false);
		broadcastPacket(shape);
	}

	private boolean _isLock;

	public boolean isLock() {
		return _isLock;
	}

	public void setLock(boolean b) {
		_isLock = b;
	}

	public void set_missile_critical_rate(int rate) {
		_missile_critical_rate = rate;
	}

	public int add_missile_critical_rate(int rate) {
		return _missile_critical_rate += rate;
	}

	public int get_missile_critical_rate() {
		return _missile_critical_rate;
	}

	public void set_melee_critical_rate(int rate) {
		_melee_critical_rate = rate;
	}

	public int get_melee_critical_rate() {
		return _melee_critical_rate;
	}

	public int get_final_burn_critical_rate() {
		if(!isPassive(MJPassiveID.FINAL_BURN.toInt()) || getCurrentHpPercent() > 70)
			return 0;
		int level = getLevel();
		int rate = 5;
		if(level == 80) 
			rate = 6;
		
		if(level > 80)
			rate += ((level - 80) / 2);
		return rate;
	}
	
	public int add_melee_critical_rate(int rate) {
		return _melee_critical_rate += rate;
	}

	public void set_magic_critical_rate(int rate) {
		_magic_critical_rate = rate;
	}

	public int get_magic_critical_rate() {
		return _magic_critical_rate;
	}

	public int add_magic_critical_rate(int rate) {
		return _magic_critical_rate += rate;
	}

	public void send_effect(int effect_id) {
		if (effect_id > 0) {
			S_SkillSound sound = new S_SkillSound(getId(), effect_id);
			sendPackets(sound, false);
			broadcastPacket(sound, true);
		}
	}

	public void send_action(int action_id) {
		if (action_id > 0) {
			S_DoActionGFX gfx = new S_DoActionGFX(getId(), action_id);
			sendPackets(gfx, false);
			broadcastPacket(gfx, true);
		}
	}
	public void send_pink_name(int remain_seconds){
		S_PinkName pnk = new S_PinkName(getId(), remain_seconds);
		sendPackets(pnk, false);
		broadcastPacket(pnk, true);
	}
	
	public void send_lawful(){
		S_Lawful pck = new S_Lawful(getId(), getLawful());
		sendPackets(pck, false);
		broadcastPacket(pck);
	}

	private int _focus_wave_level;
	private int _last_focusing_target;

	public void set_focus_wave_level(int focus_wave_level) {
		_focus_wave_level = focus_wave_level;
	}

	public int get_focus_wave_level() {
		return _focus_wave_level;
	}

	public int inc_focus_wave_level() {
		return ++_focus_wave_level;
	}

	public int dec_focus_wave_level() {
		return --_focus_wave_level;
	}

	public void set_last_focusing_target(int last_focusing_target) {
		_last_focusing_target = last_focusing_target;
	}

	public int get_last_focusing_target() {
		return _last_focusing_target;
	}
	

	private static final int[] _elf_skill_braves = new int[] { L1SkillId.DANCING_BLADES, L1SkillId.SAND_STORM,
			L1SkillId.HURRICANE, L1SkillId.FOCUS_WAVE, };

	public void remove_elf_second_brave() {
		for (int skillId : _elf_skill_braves) {
			if (hasSkillEffect(skillId)) {
				removeSkillEffect(skillId);
				S_SkillBrave brave = new S_SkillBrave(getId(), 0, 0);
				sendPackets(brave, false);
				broadcastPacket(brave, true);
				setBraveSpeed(0);
			}
		}
	}
	private boolean _isbonusAdena;
	public boolean isBonusAdenaItem() {
		return _isbonusAdena;
	}
	public void setBounusAdenaItem(boolean flag) {
		_isbonusAdena = flag;
	}
	
	
	
	
	private int _armor_brake_attacker;
	public int ArmorBrakeAttackerID() {
		return _armor_brake_attacker;
	}
	public void setArmorBrakeAttackerID(int _attacker) {
		_armor_brake_attacker = _attacker;
	}
	
	
}
