package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.KillChain.MJMonsterKillChain;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.Interface.MJMonsterTransformHandler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.CrockController;
import l1j.server.server.Controller.TimeMapController;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MapCharmItemTable;
import l1j.server.server.datatables.NCoinGiveMonsterTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.QuestInfoTable.QuestItemTemp;
import l1j.server.server.datatables.QuestInfoTable.QuestMonTemp;
import l1j.server.server.datatables.QuestMonsterTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1NCoinMonster;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1QuestMonster;
import l1j.server.server.templates.L1QuestMonsterInfo;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.templates.L1TimeMap;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;

public class L1MonsterInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	private static Random _random = new Random(System.nanoTime());
	private boolean _storeDroped;

	private MJMonsterDeathHandler _deathHandler;
	private MJMonsterTransformHandler _transformHandler;
	
	public L1MonsterInstance setDeathHandler(MJMonsterDeathHandler handler){
		_deathHandler = handler;
		return this;
	}
	
	public MJMonsterDeathHandler getDeathHandler(){
		return _deathHandler;
	}
	
	public L1MonsterInstance setTransformHandler(MJMonsterTransformHandler handler){
		_transformHandler = handler;
		return this;
	}
	
	@Override
	public void onItemUse() {
		if (!isActived() && _target != null) {
			if (getLevel() <= 45) {
				useItem(USEITEM_HASTE, 40);
			}

			if (getNpcTemplate().is_doppel() && _target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) _target;
				setName(_target.getName());
				setNameId(_target.getName());
				setTitle(_target.getTitle());
				setTempLawful(_target.getLawful());
				setCurrentSprite(targetPc.getClassId());
				for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
					if (pc == null)
						continue;
					pc.sendPackets(new S_RemoveObject(this));
					pc.removeKnownObject(this);
					pc.updateObject();
				}
			}
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			useItem(USEITEM_HEAL, 50);
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() != null)
			return;
		
		if (0 < getCurrentHp()) {
			perceivedFrom.sendPackets(S_WorldPutObject.get(this));
			perceivedFrom.sendPackets(new S_PinkName(getId(), 20));
			if (getHiddenStatus() == HIDDEN_STATUS_SINK) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide));
			} else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup));
			}
			onNpcAI();
			if (getBraveSpeed() == 1) {
				perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000));
			}
		}
	}

	public static int[][] _classGfxId = { { 0, 1 }, { 48, 61 }, { 37, 138 }, { 734, 1186 }, { 2786, 2796 } };

	@Override
	public void searchTarget() {

		L1PcInstance targetPlayer = null;
		L1MonsterInstance targetMonster = null;

		Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayer(this);
		if(col.size() > 0){
			PriorityQueue<L1PcInstance> target_q = new PriorityQueue<L1PcInstance>(col.size(), new TargetSorter());
			for (L1PcInstance pc : col) {
				if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isMonitor() || pc.isGhost()) {
					continue;
				}

				int mapId = getMapId();
				if (mapId == 88 || mapId == 98 || mapId == 92 || mapId == 91 || mapId == 95) {
					if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
						target_q.offer(pc);
						continue;
					}
				}

				if ((getNpcTemplate().getKarma() < 0 && pc.getKarmaLevel() >= 1)
						|| (getNpcTemplate().getKarma() > 0 && pc.getKarmaLevel() <= -1)) {
					continue;
				}

				int spriteId = pc.getCurrentSpriteId();
				// 버릴 수 있었던 사람들의 땅업 퀘스트의 변신중은, 각 진영의 monster로부터 선제 공격받지 않는다
				if (spriteId == 6034 && getNpcTemplate().getKarma() < 0
						|| spriteId == 6035 && getNpcTemplate().getKarma() > 0
						|| spriteId == 6035 && getNpcTemplate().get_npcId() == 46070
						|| spriteId == 6035 && getNpcTemplate().get_npcId() == 46072) {
					continue;
				}

				if (!getNpcTemplate().is_agro() && !getNpcTemplate().is_agrososc() && getNpcTemplate().is_agrogfxid1() < 0
						&& getNpcTemplate().is_agrogfxid2() < 0) {
					if (pc.getLawful() < -1000) {
						target_q.offer(pc);
						continue;
					}
					continue;
				}

				if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
					if (pc.hasSkillEffect(67)) {
						if (getNpcTemplate().is_agrososc()) {
							target_q.offer(pc);
							continue;
						}
					} else if (getNpcTemplate().is_agro()) {
						target_q.offer(pc);
						continue;
					}

					if (getNpcTemplate().is_agrogfxid1() >= 0 && getNpcTemplate().is_agrogfxid1() <= 4) {
						if (_classGfxId[getNpcTemplate().is_agrogfxid1()][0] == pc.getCurrentSpriteId()
								|| _classGfxId[getNpcTemplate().is_agrogfxid1()][1] == pc.getCurrentSpriteId()) {
							target_q.offer(pc);
							continue;
						}
					} else if (pc.getCurrentSpriteId() == getNpcTemplate().is_agrogfxid1()) {
						target_q.offer(pc);
						continue;
					}

					if (getNpcTemplate().is_agrogfxid2() >= 0 && getNpcTemplate().is_agrogfxid2() <= 4) {
						if (_classGfxId[getNpcTemplate().is_agrogfxid2()][0] == pc.getCurrentSpriteId()
								|| _classGfxId[getNpcTemplate().is_agrogfxid2()][1] == pc.getCurrentSpriteId()) {
							target_q.offer(pc);
							continue;
						}
					} else if (pc.getCurrentSpriteId() == getNpcTemplate().is_agrogfxid2()) {
						target_q.offer(pc);
						continue;
					}
				}
			}
			target_q.comparator();
			while (!target_q.isEmpty()) {
				L1PcInstance target = target_q.poll();
				if(target != null && !target.isDead())
					targetPlayer = target;
					break;
			}
		}
		
		/**
		 * @설명글// 추가 이후에있을지도모를 1.Monster vs Monster 2.Monster vs Guard 3.Monster vs Guardian 4.Monster vs Npc 위와같은 상황을 위해 오브젝트를 불러오도록 추가 현재는 1번만을위한 소스임 간단하게 오브젝트를 인스턴스of로 선언만해주면되게끔 설정
		 * 
		 */
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getHiddenStatus() != 0 || mon.isDead()) {
					continue;
				}
				if (this.getNpcTemplate().get_npcId() == 7310130 || this.getNpcTemplate().get_npcId() == 7310131
						|| this.getNpcTemplate().get_npcId() == 7310132
						|| this.getNpcTemplate().get_npcId() == 7310133) { // 적을
																			// 인식할
																			// 몬스터
																			// 잊섬
																			// 경비병
					if (mon.getNpcTemplate().get_npcId() >= 5000119 || mon.getNpcTemplate().get_npcId() <= 5000126) { // 적으로
																														// 인식될몬스터
																														// 잊섬
																														// 몬스터
						targetMonster = mon;
						break;
					}
				}
				if (this.getNpcTemplate().get_npcId() == 45570) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45571) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45582) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45587) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45605) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45685) { // 적을 인식할
																	// 몬스터(사제)
					if (mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450
							|| mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569
							|| mon.getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315
							|| mon.getNpcTemplate().get_npcId() == 45647) { // 적으로
																			// 인식될몬스터
																			// (발록의)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45391) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45450) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45482) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45569) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45579) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45315) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}

				if (this.getNpcTemplate().get_npcId() == 45647) { // 적을 인식할
																	// 몬스터(발록)
					if (mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571
							|| mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587
							|| mon.getNpcTemplate().get_npcId() == 45605) { // 적으로
																			// 인식될몬스터
																			// (사제)
						targetMonster = mon;
						break;
					}
				}
			}
		}
		if (getNpcId() >= 5100000 && getNpcId() <= 5100016) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() >= 7310081 && mon.getNpcTemplate().get_npcId() <= 7310091) {
						targetMonster = mon;
						break;
					}
				}
			}
		}
		if (getNpcId() >= 7310081 && getNpcId() <= 7310091) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() >= 5100000 && mon.getNpcTemplate().get_npcId() <= 5100016) {
						targetMonster = mon;
						break;
					}
				}
			}
		}
		if (getNpcId() >= 7200008 && getNpcId() <= 7200020 || getNpcId() == 14905 || getNpcId() == 14907) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 7200003) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}
		
		if (getNpcId() == 50000002) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance mon = (L1ScarecrowInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000005) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000004) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance mon = (L1ScarecrowInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000003) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000006) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1ScarecrowInstance) {
					L1ScarecrowInstance mon = (L1ScarecrowInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000007) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000008) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000009) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000010) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000011) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000012) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000013) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000014) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000015) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000009) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000008) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000011) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000010) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000013) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000012) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getNpcId() == 50000015) {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					if (mon.getNpcTemplate().get_npcId() == 50000014) {
						_hateList.add(mon, 0);
						_target = mon;
						return;
					}
				}
			}
		}

		if (getMap().getBaseMapId() == 1936 && targetPlayer != null) {
			targetPlayer = null;
		}
		
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
		if (targetMonster != null) {
			_hateList.add(targetMonster, 0);
			_target = targetMonster;
		}
	}

	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) {
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	public L1MonsterInstance(L1Npc template) {
		super(template);
		_storeDroped = false;
		synchronized (this) {
			if(template.get_npcId() == 707026)
				setStatus(6);

			if (this.getNpcTemplate().get_gfxid() == 7684 || this.getNpcTemplate().get_gfxid() == 7805
					|| this.getNpcTemplate().get_gfxid() == 8063) {
				_PapPearlMonster = new PapPearlMonitor(this);
				_PapPearlMonster.begin();
			}
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		if (!_storeDroped) {
			DropTable.getInstance().setDrop(this, getInventory());
			if(getInventory() == null)
				return;
			getInventory().shuffle();
			_storeDroped = true;
		}
		setActived(false);
		startAI();
	}

	@SuppressWarnings("unused")
	@Override
	public void onTalkAction(L1PcInstance pc) {
		if (pc == null)
			return;
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		if(talking == null){
			System.out.println(String.format("NPC Action을 찾을 수 없습니다. NPC ID : %d", getNpcTemplate().get_npcId()));
			return;
		}
		
		String htmlid = null;
		String[] htmldata = null;

		// html 표시 패킷 송신
		if (htmlid != null) { // htmlid가 지정되고 있는 경우
			if (htmldata != null) { // html 지정이 있는 경우는 표시
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			}
		} else {
			if (pc.getLawful() < -1000) { // 플레이어가 카오틱
				pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
			}
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (pc == null)
			return;
		if (getCurrentHp() > 0 && !isDead()) {
			// 여기다.
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
		if (attacker == null)
			return;
		if (mpDamage > 0 && !isDead()) {
			// int Hate = mpDamage / 10 + 10;
			// setHate(attacker, Hate);
			setHate(attacker, mpDamage); //나를때린놈들.

			onNpcAI();

			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().get_family());
			}

			int newMp = getCurrentMp() - mpDamage;
			if (newMp < 0) {
				newMp = 0;
			}
			setCurrentMp(newMp);
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (attacker == null)
			return;
		if (getCurrentHp() > 0 && !isDead()) {
			if (getHiddenStatus() != HIDDEN_STATUS_NONE) {
				return;
			}
			if (damage >= 0) {
				if (!(attacker instanceof L1EffectInstance)) {
					if(attacker instanceof L1MonsterInstance){
						L1MonsterInstance mon = (L1MonsterInstance) attacker;
							if(mon.getNpcTemplate().get_npcId() != 8500138){
						setHate(attacker, damage);
						}
					} else {
						setHate(attacker, damage);
					}
				}
			}
			if (damage > 0) {
				if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
					removeSkillEffect(L1SkillId.PHANTASM);
				}
			}

			onNpcAI();

			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().get_family());
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.set_pet_target(this);

				if (getNpcTemplate().get_npcId() == 45681 || getNpcTemplate().get_npcId() == 45682
						|| getNpcTemplate().get_npcId() == 45683 || getNpcTemplate().get_npcId() == 45684
						|| getNpcTemplate().get_npcId() == 45653 || getNpcTemplate().get_npcId() == 900011
						|| getNpcTemplate().get_npcId() == 900012 || getNpcTemplate().get_npcId() == 900013 // 안타라스
						|| getNpcTemplate().get_npcId() == 900038 || getNpcTemplate().get_npcId() == 900039
						|| getNpcTemplate().get_npcId() == 900040// 파푸리온 1차 ~ 3차
						|| getNpcTemplate().get_npcId() == 5096 || getNpcTemplate().get_npcId() == 5097
						|| getNpcTemplate().get_npcId() == 5098 || getNpcTemplate().get_npcId() == 5099
						|| getNpcTemplate().get_npcId() == 5100)// 린드비오르 1차 ~ 3차
				{
					recall(player);
				}
				/** 1대라도 치면 해당 아이템드랍 **/
				if (getNpcTemplate().get_npcId() == 707017 || getNpcTemplate().get_npcId() == 45617
						|| getNpcTemplate().get_npcId() == 707022 || getNpcTemplate().get_npcId() == 707023
						|| getNpcTemplate().get_npcId() == 707024 || getNpcTemplate().get_npcId() == 707025
						|| getNpcTemplate().get_npcId() == 5134 || getNpcTemplate().get_npcId() == 5047) {
					if (!player.isBosMon()) {
						player.setBosMon(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 707026) { // 에이션트가디언
					if (!player.isBosMon1()) {
						player.setBosMon1(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 5136) { // 에르자베
					if (!player.isElrzabe()) {
						player.setElrzabe(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 5135) { // 샌드웜
					if (!player.isSandWarm()) {
						player.setSandWarm(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 45529) { // 드레이크
					if (!player.is드레이크()) {
						player.set드레이크(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 7000093) { // 제로스
					if (!player.is제로스()) {
						player.set제로스(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 45684) { // 발라카스
					if (!player.is발라카스()) {
						player.set발라카스(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 900040) { // 파푸리온
					if (!player.is파푸리온()) {
						player.set파푸리온(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 5100) { // 린드비오르
					if (!player.is린드비오르()) {
						player.set린드비오르(true);
					}
				}
				if (getNpcTemplate().get_npcId() == 900013) { // 안타라스
					if (!player.is안타라스()) {
						player.set안타라스(true);
					}
				}
				//TODO N코인 300원
				if (NCoinGiveMonsterTable.getInstance().isNCoinMonster(getNpcTemplate().get_npcId())){
					if(!player.isNCoinMon()){
						player.setNCoinMon(true);
					}
				}
				
				if (SpecialMapTable.getInstance().isSpecialMap(getMapId())){
					L1SpecialMap SM = SpecialMapTable.getInstance().getSpecialMap(getMapId());
					if (SM.isGiveItem()) {
						if (!player.isSpecialMap()) {
							player.setSpecialMap(true);
						}
					}
				}
			}
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				if (attacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) attacker;
					MJMonsterKillChain.getInstance().on_kill(pc, this);
					if (!pc.noPlayerCK && BQSLoadManager.BQS_IS_ONUPDATE_BOOKS) {
						BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformationFromNpcId(getNpcId());
						BQSCharacterData bqs = pc.getBqs();
						if(bqsInfo != null && bqs != null)
							bqs.onUpdate(bqsInfo);
					}
					if (QuestInfoTable.getInstance().getQuestMonid(getNpcId())) {
						QuestMonTemp temp = QuestInfoTable.getInstance().getQuestMonTemp(getNpcId());
						if (temp != null) {
							for (L1Character cha : getHateList().toTargetArrayList()) {
								if (cha != null && cha instanceof L1PcInstance) {
									L1PcInstance use = (L1PcInstance) cha;
									L1QuestInfo info = use.getQuestList(temp.quest_id);
									//TODO 기존 몬스터처치 부근문제 해결 주석체크
									if (info == null || info.end_time != 0) {
										continue;
//										return;
									}
									if (info.ck[temp.type] >= temp.count) {
										info.ck[temp.type] = temp.count;
									}
									info.ck[temp.type] = temp.count; // 한마리잡으면
									use.sendPackets(new S_QuestTalkIsland(use, temp.quest_id, info));
								}
							}
						}
					}
					
					// 퀘스트 몬스터 시스템
					if (QuestMonsterTable.getInstance().isQuestMonster(getNpcId())) {
						L1QuestMonster temp = QuestMonsterTable.getInstance().getQuestMonster(getNpcId());
						if (temp != null) {
							int nextid = temp.getNpcId();
							L1QuestMonsterInfo Q_info = pc.getmonsterList(nextid);
							Q_info = new L1QuestMonsterInfo();
							if (!pc.monster_list.containsKey(nextid)) {
								Q_info.npc_id = nextid;
								pc.monster_list.put(nextid, Q_info);
								Q_info.npc_count += 1;
							}
							for (L1Character cha : getHateList().toTargetArrayList()) {
								if (cha != null && cha instanceof L1PcInstance) {
									L1PcInstance use = (L1PcInstance) cha;
									L1QuestMonsterInfo info = use.getmonsterList(temp.getNpcId());
									if (pc.getAI() != null)
										continue;
									if (info == null) {
										continue;
									}
									if (info.npc_count >= temp.getNpcCount()) {
										boolean effectOK = false;
										if (temp.getEffectNum() > 0) {
											effectOK = true;
										}

										if (temp.isGiveItem()) {
											L1ItemInstance giveItem = pc.getInventory().storeItem(temp.getItemId(),
													temp.getItemCount());
											pc.sendPackets("아이템 획득 : " + giveItem.getName() + " (" + temp.getItemCount()
													+ ")");
										}

										if (effectOK) {
											// 자신에게만 보이기
											pc.sendPackets(new S_SkillSound(pc.getId(), temp.getEffectNum()));

											// 주변유저들에게도 보이게 할것인가?
											if (temp.isAllEffect()) {
												Broadcaster.broadcastPacket(pc,
														new S_SkillSound(pc.getId(), temp.getEffectNum()));
											}
										}

										if (temp.isMentuse()) {
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, temp.getMent()));
										}

										info.npc_count = 0;
									}
									info.npc_count += 1;
									if (temp.isMentuse()) {
										pc.sendPackets("반복퀘스트: " + temp.getNpcName() + " 완료 까지 "
												+ (temp.getNpcCount() - info.npc_count + 1) + " 마리 남았습니다.");
									}
								}
							}
						}
					}
					
					if (getNpcTemplate().get_npcId() == 7320012) {
						pc.setBuffnoch(1);
						pc.setCurrentMp(pc.getCurrentMp() + 100);
						pc.setBuffnoch(0);
					}
					if (getNpcTemplate().get_npcId() == 7320013) {
						pc.setBuffnoch(1);
						new L1SkillUse().handleCommands(pc, L1SkillId.GREATER_HEAL, pc.getId(), pc.getX(), pc.getY(),null, 0, L1SkillUse.TYPE_GMBUFF);
						pc.setBuffnoch(0);
					}
					if (getNpcTemplate().get_npcId() == 7320014) {
						pc.setBuffnoch(1);
						new L1SkillUse().handleCommands(pc, L1SkillId.BLESS_WEAPON, pc.getId(), pc.getX(), pc.getY(),
								null, 0, L1SkillUse.TYPE_GMBUFF);
						pc.setBuffnoch(0);
					}
					if (getNpcTemplate().get_npcId() == 7320015) {
						pc.setBuffnoch(1);
						new L1SkillUse().handleCommands(pc, L1SkillId.PATIENCE, pc.getId(), pc.getX(), pc.getY(), null,
								0, L1SkillUse.TYPE_GMBUFF);
						pc.setBuffnoch(0);
					}
					if (getNpcTemplate().get_npcId() == 7320016) {
						pc.setBuffnoch(1);
						new L1SkillUse().handleCommands(pc, L1SkillId.ADVANCE_SPIRIT, pc.getId(), pc.getX(), pc.getY(),
								null, 1200, L1SkillUse.TYPE_GMBUFF);
						pc.setBuffnoch(0);
					}
					if (getNpcTemplate().get_npcId() == 7320017) {
						int time = (10 * 60 * 1000) + 1000; // 10분 1초
						if (pc.hasSkillEffect(STATUS_DRAGON_PEARL)) {
							pc.killSkillEffectTimer(STATUS_DRAGON_PEARL);
							pc.sendPackets(new S_Liquor(pc.getId(), 0));
							pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, 0));
							pc.setPearl(0);
						}
						pc.sendPackets(new S_ServerMessage(1065));
						pc.sendPackets(new S_SkillSound(pc.getId(), 13283));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 13283));
						pc.setSkillEffect(STATUS_DRAGON_PEARL, time);
						pc.sendPackets(new S_Liquor(pc.getId(), 8));
						pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
						pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, 151));
						pc.setPearl(1);
					}
				}

				/** 본던 리뉴얼 용아병의 혼령 **/
				if (getNpcTemplate().get_npcId() == 7000075) {// 파란색 동일층
					if (attacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) attacker;
						if (pc != null && pc.getMapId() >= 807 && pc.getMapId() <= 813) {
							L1Location newLocation = pc.getLocation().randomLocation(200, true);
							int x = newLocation.getX();
							int y = newLocation.getY();
							short mapid = (short) newLocation.getMapId();
							int heading = pc.getHeading();
							pc.start_teleport(x, y, mapid, heading, 169, true, false);
						}
					}
				}
				if (getNpcTemplate().get_npcId() == 7000074) {// 노란색 아래층
					if (attacker instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) attacker;
						if (pc != null && pc.getMapId() >= 807 && pc.getMapId() <= 813) {
							L1Location newLocation = pc.getLocation().randomLocation(200, true);
							int x = newLocation.getX();
							int y = newLocation.getY();
							short mapid = (short) newLocation.getMapId();
							int heading = pc.getHeading();
							if (pc.getMapId() == 807) {
								pc.start_teleport(x, y, mapid, heading, 169, true, false);
							} else {
								pc.start_teleport(x, y, (mapid - 1), heading, 169, true, false);
							}
						}
					}
				}
				
				/** 보스 영혼석 **/
//				L1SoulStoneSystem.getInstance().drop(attacker);

				int transformId = getNpcTemplate().getTransformId();
				if (transformId == -1) {
					setCurrentHp(0);
					setDead(true);
					setStatus(ActionCodes.ACTION_Die);
					if(_deathHandler != null){
						if(_deathHandler.onDeathNotify(this))
							return;
					}
					Death death = new Death(attacker);
					if ((getNpcTemplate().get_gfxid() == 7684 || getNpcTemplate().get_gfxid() == 7805
							|| getNpcTemplate().get_gfxid() == 8063) && _PapPearlMonster != null) {
						_PapPearlMonster.begin();
						_PapPearlMonster = null;
					}
					GeneralThreadPool.getInstance().execute(death);
				} else {
					transform(transformId);
				}
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
				hide();
			}
		} else if (!isDead()) {
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			if(_deathHandler != null){
				if(_deathHandler.onDeathNotify(this))
					return;
			}
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}

	private void recall(L1PcInstance pc) {
		if (pc == null || getMapId() != pc.getMapId() || pc.get_teleport()) {
			return;
		}
		if (getLocation().getTileLineDistance(pc.getLocation()) > 4) {
			int loc_x = this.getX() + CommonUtil.random(-3, 3);
			int loc_y = this.getY() + CommonUtil.random(-3, 3);

			if (glanceCheck(loc_x, loc_y)) {
				pc.start_teleport(loc_x, loc_y, this.getMapId(), CommonUtil.random(7), 169, true, false);
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (getNpcId() == 707026 && i > 0) {
			int p = (int) (((double) getCurrentHp() / (double) getMaxHp()) * 100D);
			if (getStatus() == 6) {
				if (p <= 90) {
					broadcastPacket(new S_DoActionGFX(getId(), 4));
					setStatus(0);
				}
			}
		}
		
		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	@Override
	public void deleteMe(){
		_PapPearlMonster = null;
		super.deleteMe();
	}

	private void Sahel(L1NpcInstance Sahel) { // 사엘
		L1NpcInstance Pearl = null;
		L1PcInstance PearlBuff = null;
		Sahel.receiveDamage(Sahel, 1);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(Sahel, 5)) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getNpcTemplate().get_gfxid() == 7684 || mon.getNpcTemplate().get_gfxid() == 7805)
					Pearl = mon;
			}
			if (obj instanceof L1PcInstance) {
				L1PcInstance Buff = (L1PcInstance) obj;
				if (!(Buff.hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF)
						|| Buff.hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF)))
					PearlBuff = Buff;
			}
		}
		if (Pearl.getNpcTemplate().get_gfxid() == 7684 && PearlBuff != null && Pearl.getCurrentHp() > 0
				&& PearlBuff.getCurrentHp() > 0) {
			PearlBuff.sendPackets(new S_SkillSound(PearlBuff.getId(), 7836));
			PearlBuff.setSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF, 60 * 1000);
			PearlBuff.broadcastPacket(new S_SkillSound(PearlBuff.getId(), 7836));
			Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8468", 0)); // 힐을
																			// 줍니다
		}
		if (Pearl.getNpcTemplate().get_gfxid() == 7805 && PearlBuff != null && Pearl.getCurrentHp() > 0
				&& PearlBuff.getCurrentHp() > 0) {
			PearlBuff.sendPackets(new S_SkillSound(PearlBuff.getId(), 7834));
			PearlBuff.setSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF, 60 * 1000);
			PearlBuff.broadcastPacket(new S_SkillSound(PearlBuff.getId(), 7834));
			if (PearlBuff.isKnight() || PearlBuff.isCrown() || PearlBuff.isDarkelf() || PearlBuff.isDragonknight()
					|| PearlBuff.is전사()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8471", 0)); // 근거리
																				// 물리력에
			} else if (PearlBuff.isElf()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8472", 0)); // 원거리
																				// 물리력에
			} else if (PearlBuff.isWizard() || PearlBuff.isBlackwizard()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8470", 0)); // 마법에
			} else {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8469", 0)); // 헤이스트
			}
		}
	}

	private void PapPearl(L1NpcInstance Pearl) { // 진주
		L1NpcInstance Pap = null;
		L1PcInstance PearlBuff = null;
		Random random = new Random();
		for (L1Object obj : L1World.getInstance().getVisibleObjects(Pearl, 10)) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getNpcTemplate().get_gfxid() == 7864 || mon.getNpcTemplate().get_gfxid() == 7869
						|| mon.getNpcTemplate().get_gfxid() == 7870)
					Pap = mon;
			}
			if (obj instanceof L1PcInstance) {
				L1PcInstance Buff = (L1PcInstance) obj;
				if (!(Buff.hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF)
						|| Buff.hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF)))
					PearlBuff = Buff;
			}
		}
		int PearlBuffRandom = random.nextInt(10) + 1;
		if (Pap != null && Pap.getCurrentHp() > 0 && Pearl.getNpcTemplate().get_gfxid() == 7684
				&& Pearl.getCurrentHp() > 0) {
			int newHp = Pap.getCurrentHp() + 3000;
			Pap.setCurrentHp(newHp);
			Pearl.broadcastPacket(new S_SkillSound(Pearl.getId(), 233));
			L1EffectSpawn.getInstance().spawnEffect(900055, 1 * 1000, Pap.getX(), Pap.getY(), Pap.getMapId());
		} else if (Pap != null && Pap.getCurrentHp() > 0 && Pearl.getNpcTemplate().get_gfxid() == 7805
				&& Pearl.getCurrentHp() > 0) {
			Pap.setMoveSpeed(1);
			Pap.setSkillEffect(L1SkillId.STATUS_HASTE, 30 * 1000);
			Pearl.broadcastPacket(new S_SkillSound(Pearl.getId(), 224));
		}
		if (PearlBuff != null && PearlBuffRandom == 3 && Pearl.getNpcTemplate().get_gfxid() == 7684) { // 오색
			PearlBuff.sendPackets(new S_SkillSound(PearlBuff.getId(), 7836));
			PearlBuff.setSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF, 60 * 1000);
			PearlBuff.broadcastPacket(new S_SkillSound(PearlBuff.getId(), 7836));
		} else if (PearlBuff != null && PearlBuffRandom == 5 && Pearl.getNpcTemplate().get_gfxid() == 7805) { // 신비
			PearlBuff.sendPackets(new S_SkillSound(PearlBuff.getId(), 7834));
			PearlBuff.setSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF, 60 * 1000);
			PearlBuff.broadcastPacket(new S_SkillSound(PearlBuff.getId(), 7834));
		}
	}

/*	private void OmanRiper() {
		int chance = _random.nextInt(1000) + 1;// 리퍼 소환 확률
		int boss = 0;
		switch (getNpcId()) {// 소막 버그 방지
		case 7310010:
		case 7310011:
		case 7310012:
		case 7310013:
		case 7310014:// 1층
		case 7310016:
		case 7310017:
		case 7310018:
		case 7310019:
		case 7310020:// 2층
		case 7310022:
		case 7310023:
		case 7310024:
		case 7310025:
		case 7310026:
		case 7310027:// 3층
		case 7310029:
		case 7310030:
		case 7310031:
		case 7310032:
		case 7310033:// 4층
		case 7310035:
		case 7310036:
		case 7310037:
		case 7310038:
		case 7310039:
		case 7310040:// 5층
		case 7310042:
		case 7310043:
		case 7310044:
		case 7310045:// 6층
		case 7310047:
		case 7310048:
		case 7310049:
		case 7310050:// 7층
		case 7310052:
		case 7310053:
		case 7310054:
		case 7310055:// 8층
		case 7310057:
		case 7310058:
		case 7310059:
		case 7310060:// 9층
		case 7310062:
		case 7310063:
		case 7310064:
		case 7310065:// 10층
		case 7310067:
		case 7310068:
		case 7310069:
		case 7310070:
		case 7310071:
		case 7310072:
		case 7310073:
		case 7310074:
		case 7310075:
		case 7310076:// 정상
			if ((getMapId() >= 101 && getMapId() <= 111) && getMapId() % 10 != 0) {
				if (chance < 5) { // 10
					L1SpawnUtil.spawn2(this.getX(), this.getY(), this.getMapId(), 45590, 5, 1800 * 1000, 0);
					broadcastPacket(new S_SkillSound(getId(), 4842));
				}
			}
			break;
		case 45590:
			if (chance < 3) {
				if (getMapId() == 101) {
					boss = 7310015;
				} else if (getMapId() == 102) {
					boss = 7310021;
				} else if (getMapId() == 103) {
					boss = 7310028;
				} else if (getMapId() == 104) {
					boss = 7310034;
				} else if (getMapId() == 105) {
					boss = 7310041;
				} else if (getMapId() == 106) {
					boss = 7310046;
				} else if (getMapId() == 107) {
					boss = 7310051;
				} else if (getMapId() == 108) {
					boss = 7310056;
				} else if (getMapId() == 109) {
					boss = 7310061;
				} else if (getMapId() == 110) {
					boss = 7310066;
				} else if (getMapId() == 200) {
					boss = 7310077;
				}
				if(boss != 0){				
					L1SpawnUtil.spawn2(this.getX(), this.getY(), this.getMapId(), boss, 2, 1800 * 1000, 0);
					broadcastPacket(new S_SkillSound(getId(), 4842));
				}else{
					System.out.println(String.format("%s 몬스터 %d에서 스폰됨.", getName(), getMapId()));
				}
			}
			break;
		}
	}*/

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			switch(getNpcId()){
			case 45263:
			case 7320176:
			case 7320180:
			case 7320182:
				for(L1Object obj : L1World.getInstance().getVisibleObjects(L1MonsterInstance.this, 2)){
					if(obj == null || !obj.instanceOf(MJL1Type.L1TYPE_PC))
						continue;
					
					L1PcInstance pc = (L1PcInstance)obj;
					if(pc.isDead())
						continue;
					
					if(!MJCommons.isCounterMagic(pc))
						pc.receiveDamage(L1MonsterInstance.this, MJRnd.next(200) + 200, 1);
				}
				break;
			}
			Quset_Drop(_lastAttacker);
			
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			getMap().setPassable(getLocation(), true);
			switch(getNpcId()){
			case 8500142:
			case 8500143:
			case 8500144:
				broadcastPacket(new S_DoActionGFX(getId(), 11));
				break;
			default:
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
				break;
			}
			startChat(CHAT_TIMING_DEAD);
			if (_lastAttacker != null)
				distributeExpDropKarma(_lastAttacker);
			giveUbSeal();
			setDeathProcessing(false);
			setExp(0);
			setLawful(0);
			setKarma(0);
			allTargetClear();
			startDeleteTimer();
//			OmanRiper();
			
			Elzabe(_lastAttacker);
			SandWarm(_lastAttacker);
			발라카스(_lastAttacker);
			파푸리온(_lastAttacker);
			린드비오르(_lastAttacker);
			안타라스(_lastAttacker);
			calcCombo(_lastAttacker);
			NCoinGiveMon(_lastAttacker);
			Special_Map_Result(_lastAttacker);
			
			
			

			if (getNpcTemplate().getDoor() > 0) {
				int doorId = getNpcTemplate().getDoor();
				if (getNpcTemplate().getCountId() > 0) {
					int sleepTime = 2 * 60 * 60; // 2시간
					TimeMapController.getInstance()
							.add(new L1TimeMap(getNpcTemplate().getCountId(), sleepTime, doorId));
				}
				L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(doorId);
				synchronized (this) {
					if (door != null)
						door.open();
				}
			}
			if (_lastAttacker instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _lastAttacker;
				
				
				/**소지 아이템으로 인한 아이템 드랍*/
				MapCharmItemTable.getInstance().getCharmItem(pc, L1MonsterInstance.this, getMap().getId());
				
				
				/** 시간의 균열 관련 오시리아 제단 열쇠 */
				if (getMap().getId() == 781) {
					int rnd = (int) (Math.random() * 100) + 1;
					// 5%
					if (rnd >= 85) {
						if (!pc.getInventory().checkItem(100036, 1)) {
							L1ItemInstance item = pc.getInventory().storeItem(100036, 1);
							String itemName = item.getItem().getName();
							String npcName = getNpcTemplate().get_name();
							pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
						}
					}
				} else if (getMap().getId() == 783) {
					int rnd = (int) (Math.random() * 100) + 1;// 5%
					if (rnd >= 85) {
						if (!pc.getInventory().checkItem(500210, 1)) {
							L1ItemInstance item = pc.getInventory().storeItem(500210, 1);
							String itemName = item.getItem().getName();
							String npcName = getNpcTemplate().get_name();
							pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
						}
					}
				}
			}

			if (getNpcTemplate().get_npcId() == 400016 || getNpcTemplate().get_npcId() == 400017) {
				int dieCount = CrockController.getInstance().dieCount();
				switch (dieCount) {
				// 2명의 보스중 한명도 죽이지 않았을때 둘중 하나를 죽였다면 +1
				case 0:
					CrockController.getInstance().dieCount(1);
					break;
				// 2명의 보스중 이미 한명이 죽였고. 이제 또한명이 죽으니 2
				case 1:
					CrockController.getInstance().dieCount(2);
					CrockController.getInstance().send();
					break;
				}
			}
			if (getNpcTemplate().get_npcId() == 8000181 || getNpcTemplate().get_npcId() == 8000191) {// 미사용
				int dieCountTikal = CrockController.getInstance().dieCount();
				switch (dieCountTikal) {
				// 2명의 보스중 한명도 죽이지 않았을때 둘중 하나를 죽였다면 +1
				case 0:
					CrockController.getInstance().dieCount(1);

					L1NpcInstance mob = null;

					if (getNpcTemplate().get_npcId() == 8000181) {
						mob = L1World.getInstance().findNpc(8000191);
						if (mob != null && !mob.isDead()) {
							mob.setSkillEffect(8000181, 60 * 1000);// 1분
						}
					} else {
						mob = L1World.getInstance().findNpc(8000181);
						if (mob != null && !mob.isDead()) {
							mob.setSkillEffect(8000191, 60 * 1000);// 1분
						}
					}

					break;
				// 2명의 보스중 이미 한명이 죽였고. 이제 또한명이 죽으니 2
				case 1:
					CrockController.getInstance().dieCount(2);
					CrockController.getInstance().sendTikal();
					break;
				}
			} // 시간의 균열 - 티칼용 주석
		}
	}

	private Random _rnd = new Random(System.nanoTime());

	private void Quset_Drop(L1Character lastAttacker) {
		if (!(lastAttacker instanceof L1PcInstance)) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) lastAttacker;
		int npcId = getNpcId();
		int itemid = 0;
		if (npcId == 50000029) { // 식량 주머니
			itemid = 6000; // 수련자의 식량 주머니
		} else if (npcId >= 50000034 && npcId <= 50000036) { // 뼈조각
			itemid = 6001; // 뼈조각
		} else if (npcId == 5029 || npcId == 5032 || npcId == 5033 || npcId == 5051) { // 무기
			L1QuestInfo weapon = pc.getQuestList(273);
			L1QuestInfo earthrun = pc.getQuestList(304);
			if (weapon != null && weapon.end_time == 0) {
				itemid = 6002; // 무기 도면
			} else if (earthrun != null && earthrun.end_time == 0) {
				itemid = 6014; // 대지의 원소(소)
			}
		} else if (npcId >= 77002 && npcId <= 77004) { // 이상한 뼈조각
			L1QuestInfo bone = pc.getQuestList(281);
			if (bone != null && bone.end_time == 0) {
				itemid = 6008; // 이상한 뼈조각
			}
		} else if (npcId >= 5034 && npcId <= 5037) { // 이상한 장신구
			L1QuestInfo bone = pc.getQuestList(285);
			L1QuestInfo firerun = pc.getQuestList(305);
			if (bone != null && bone.end_time == 0) {
				itemid = 6009; // 이상한 장신구
			} else if (firerun != null && firerun.end_time == 0) {
				itemid = 6018; // 불의 원소(소)
			}
		} else if (npcId == 5042 || npcId == 5043) { // 이상한 눈
			L1QuestInfo eye = pc.getQuestList(289);
			L1QuestInfo waterrun = pc.getQuestList(306);
			if (eye != null && eye.end_time == 0) {
				itemid = 6010; // 이상한 눈
			} else if (waterrun != null && waterrun.end_time == 0) {
				itemid = 6020; // 물의 원소(소)
			}
		} else if (npcId == 5017 || npcId == 5018) { // 장식용 보석
			L1QuestInfo jewel = pc.getQuestList(298);
			L1QuestInfo airrun = pc.getQuestList(307);
			if (jewel != null && jewel.end_time == 0) {
				itemid = 6011; // 장식용 보석
			} else if (airrun != null && airrun.end_time == 0) {
				itemid = 6022; // 공기의 원소(소)
			}
		} else if (npcId >= 50000043 && npcId <= 50000049 || npcId >= 77002 && npcId <= 77007) {
			L1QuestInfo dayquest = pc.getQuestList(361);
			if (dayquest != null && dayquest.end_time == 0) {
				itemid = 6003;
			}
		}

		if (itemid != 0) {
			if (QuestInfoTable.getInstance().getPickupItem(itemid)) {
				QuestItemTemp temp = QuestInfoTable.getInstance().getPickupInfo(itemid);
				L1QuestInfo info = pc.getQuestList(temp.quest_id);
				if (info != null && info.end_time == 0) {// 활성화 상태라면
					if (pc.getInventory().checkItemCount(itemid) < temp.count) {
						int chance = _rnd.nextInt(10000);
						if (chance < 9000) {
							L1QuestInfo dayquest = pc.getQuestList(361);
							if (dayquest != null && dayquest.end_time == 0) {
								int count = _rnd.nextInt(5) + 1;
								L1ItemInstance item = null;
								item = pc.getInventory().storeItem(itemid, count);
								ServerBasePacket pck = new S_ServerMessage(813, getNpcTemplate().get_name(),
										item.getLogName(), pc.getName());
								pc.sendPackets(pck);
								return;
							} else {
								L1ItemInstance item = null;
								item = pc.getInventory().storeItem(itemid, temp.count);
								ServerBasePacket pck = new S_ServerMessage(813, getNpcTemplate().get_name(),
										item.getLogName(), pc.getName());
								pc.sendPackets(pck);
							}
						}
					}
				}
			}
		}
	}
	
	private void distributeExpDropKarma(L1Character lastAttacker) {
		if (lastAttacker == null) {
			return;
		}
		L1PcInstance pc = null;
		if (lastAttacker instanceof L1PcInstance) {
			pc = (L1PcInstance) lastAttacker;
		} else if(lastAttacker instanceof MJCompanionInstance){
			pc = ((MJCompanionInstance) lastAttacker).get_master();
		} else if (lastAttacker instanceof L1PetInstance) {
			pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		}
		if (pc != null && !pc.noPlayerCK && !pc.noPlayerck2 && !pc.isDead()) {
			ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
			ArrayList<Integer> hateList = _hateList.toHateArrayList();
			
			int exp = getExp();
			CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
					
			if (isDead()) {
				distributeDrop(pc);
				giveKarma(pc);
			}

		} else if (lastAttacker instanceof L1EffectInstance)

		{
			ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
			ArrayList<Integer> hateList = _hateList.toHateArrayList();
			if (hateList.size() != 0) {
				int maxHate = 0;
				for (int i = hateList.size() - 1; i >= 0; i--) {
					if (maxHate < ((Integer) hateList.get(i))) {
						maxHate = (hateList.get(i));
						lastAttacker = targetList.get(i);
					}
				}
				if (lastAttacker instanceof L1PcInstance) {
					pc = (L1PcInstance) lastAttacker;
				} else if(lastAttacker instanceof MJCompanionInstance){
					pc = ((MJCompanionInstance) lastAttacker).get_master();
				} else if (lastAttacker instanceof L1PetInstance) {
					pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
				} else if (lastAttacker instanceof L1SummonInstance) {
					pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
				}
				int exp = getExp();
				CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
				if (isDead()) {
					distributeDrop(pc);
					giveKarma(pc);
				}
			}
		}
	}

	private void distributeDrop(L1PcInstance pc) {
		ArrayList<L1Character> dropTargetList = _dropHateList.toTargetArrayList();
		ArrayList<Integer> dropHateList = _dropHateList.toHateArrayList();
		try {
			int npcId = getNpcTemplate().get_npcId();
			if (npcId != 45640 || (npcId == 45640 && getCurrentSpriteId() == 2332)) {
				DropTable.getInstance().dropShare(L1MonsterInstance.this, dropTargetList, dropHateList, pc);
			}
		} catch (Exception e) {
		}
	}

	private void giveKarma(L1PcInstance pc) {
		int karma = getKarma();
		if (karma != 0) {
			int karmaSign = Integer.signum(karma);
			int pcKarmaLevel = pc.getKarmaLevel();
			int pcKarmaLevelSign = Integer.signum(pcKarmaLevel);
			if (pcKarmaLevelSign != 0 && karmaSign != pcKarmaLevelSign) {
				karma *= 5;
			}
			pc.addKarma((int) (karma * Config.RATE_KARMA));
			pc.sendPackets(new S_Karma(pc));
		}
	}

	private void giveUbSeal() {// 무한대전 보상
		if (getUbSealCount() != 0) {
			L1UltimateBattle ub = UBTable.getInstance().getUb(getUbId());
			if (ub != null) {
				L1ItemInstance item = null;
				for (L1PcInstance pc : ub.getMembersArray()) {
					if (pc != null && !pc.isDead() && !pc.isGhost()) {
						item = pc.getInventory().storeItem(201548, getUbSealCount());// 가디언의
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						if (_random.nextInt(10) <= 2) {
							pc.getInventory().storeItem(201548, 1);// 확률적으로 드랍
						}
					}
				}
			}
			setUbSealCount(0);
		}
	}

	public boolean is_storeDroped() {
		return _storeDroped;
	}

	public void set_storeDroped(boolean flag) {
		_storeDroped = flag;
	}

	private int _ubSealCount = 0;

	public int getUbSealCount() {
		return _ubSealCount;
	}

	public void setUbSealCount(int i) {
		_ubSealCount = i;
	}

	private int _ubId = 0; // UBID

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int i) {
		_ubId = i;
	}

	private void hide() {
		int npcid = getNpcTemplate().get_npcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(10);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_SINK);
					broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide));
					setStatus(13);
					broadcastPacket(S_WorldPutObject.get(this));
				}
			}
		} else if (npcid == 45682) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(50);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_SINK);
					broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_AntharasHide));
					setStatus(20);
					broadcastPacket(S_WorldPutObject.get(this));
				}
			}
		} else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321
				|| npcid == 45445 || npcid == 75000) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(10);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup));
					setStatus(4);
					broadcastPacket(S_WorldPutObject.get(this));
				}
			}
		} else if (npcid == 45681) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(50);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup));
					setStatus(11);
					broadcastPacket(S_WorldPutObject.get(this));
				}
			}
		}
	}

	public void initHide() {
		int npcid = getNpcTemplate().get_npcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000
				|| npcid == 400001) {
			int rnd = _random.nextInt(3);
			if (1 > rnd) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setStatus(13);
			}
		} else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281 || npcid == 75003) {
			int rnd = _random.nextInt(3);
			if (1 > rnd) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setStatus(4);
			}
		} else if (npcid == 217) {
			setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
			setStatus(6);
		} else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321
				|| npcid == 45445 || npcid == 75000) {
			setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
			setStatus(4);
		} else if (npcid == 45681) {
			setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
			setStatus(11);
		}
	}

	public void initHideForMinion(L1NpcInstance leader) {
		int npcid = getNpcTemplate().get_npcId();
		if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_SINK) {
			if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000
					|| npcid == 400001) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setStatus(13);
			} else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281 || npcid == 75003) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setStatus(4);
			}
		} else if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_FLY) {
			if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445
					|| npcid == 75000) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
				setStatus(4);
			} else if (npcid == 45681) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
				setStatus(11);
			}
		}
	}

	@Override
	protected void transform(int transformId) {
		if(_transformHandler != null)
			_transformHandler.onTransFormNotify(this);
		super.transform(transformId);
		getInventory().clearItems();
		DropTable.getInstance().setDrop(this, getInventory());
		getInventory().shuffle();
	}

	private PapPearlMonitor _PapPearlMonster;// 행동

	public class PapPearlMonitor implements Runnable {
		private final L1MonsterInstance _Pearl;

		public PapPearlMonitor(L1MonsterInstance npc) {
			_Pearl = npc;
		}

		public void begin() {
			GeneralThreadPool.getInstance().schedule(this, 3000);
		}

		@Override
		public void run() {
			try {
				if (_Pearl.getNpcTemplate().get_gfxid() == 7684 || _Pearl.getNpcTemplate().get_gfxid() == 7805)
					PapPearl(_Pearl);
				else if (_Pearl.getNpcTemplate().get_gfxid() == 8063)
					Sahel(_Pearl);
			} catch (Exception exception) {
			}
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count, int Bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

		if (item != null) {
			item.setCount(count);
			item.setBless(Bless);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			if (pc.isInParty()) { // 파티 인경우
				for (L1PcInstance partymember : pc.getParty().getMembers()) {
					partymember.sendPackets(new S_ServerMessage(813, getNpcTemplate().get_name(), item.getLogName(), pc.getName()));
				}
			} else { // 파티가 아닌 경우
				pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().get_name(), item.getLogName()));
			}
			return true;
		} else {
			return false;
		}
	}
	// 에르자베
	private void Elzabe(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 5136) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.isElrzabe() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 20) {
						createNewItem(pc, 30102, 1, 0); // 에르자베의 알
						pc.setCurrentHp(pc.getMaxHp());
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\f3에르자베 공략에 성공하였습니다. 에르자베를 공략한 용사들에게 에르자베의 알이 지급되었습니다."));
					}
				}
				pc.setElrzabe(false);
			}
		}
	}
	// 샌드 웜
	private void SandWarm(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 5135) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.isSandWarm() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 20) {
						createNewItem(pc, 30103, 1, 0); // 샌드웜의 모래 주머니
						pc.setCurrentHp(pc.getMaxHp());
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG샌드 웜 공략에 성공하였습니다. 샌드 웜을 공략한 용사들에게 모래주머니가 지급되었습니다."));
					}
				}
				pc.setSandWarm(false);
			}
		}
	}
	private void Special_Map_Result(L1Character lastAttacker) {
		if(Config.STANDBY_SERVER)
			return;
		int mapId = getMapId();
		if (SpecialMapTable.getInstance().isSpecialMap(mapId)) {
			L1SpecialMap SM = SpecialMapTable.getInstance().getSpecialMap(mapId);
			if (SM != null && SM.isGiveItem()) {
				if (CommonUtil.random(100) < SM.getChance()) {
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (lastAttacker.getMapId() == pc.getMapId() && pc.isSpecialMap() && !pc.isDead()) {
							if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 20) {
								int count = CommonUtil.random(SM.getItemMinCount(), SM.getItemMaxCount());
								L1ItemInstance ttem = pc.getInventory().storeItem(SM.getItemId(), count);
								// 아이템 획득 메세지
								if(ttem != null){
									pc.sendPackets(new S_ServerMessage(403, ttem.getName() + "(" + count + ")"));
								}
							}
							pc.setSpecialMap(false);
						}
					}
				}
			}
		}
	}
	
	/** 보스 한입만 상자 **/
	private void NCoinGiveMon(L1Character lastAttacker) {
		if(Config.STANDBY_SERVER)
			return;
		
		int npcId = getNpcTemplate().get_npcId(); 
		boolean effectOK = false;
		L1NCoinMonster coin = NCoinGiveMonsterTable.getInstance().getNCoinGiveMonster(npcId);
		if (coin != null) {
			if(coin.getEffectNum() > 0){
				effectOK = true;
				
			}
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if(pc.getAI() != null)
					continue;
				
				if (lastAttacker.getMapId() == pc.getMapId() && pc.isNCoinMon() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 20) {
						// 잡은유저들 만피채우고싶다면 주석 해제
						//pc.setCurrentHp(pc.getMaxHp());
						
						int point = coin.getNCoin();// 갯수
						pc.addNcoin(point);
						pc.getNetConnection().getAccount().updateNcoin();
						
						if(coin.isGiveItem()){
							L1ItemInstance giveItem = pc.getInventory().storeItem(coin.getItemId(), coin.getItemCount());
							// 아이템 획득 메세지
							pc.sendPackets(new S_ServerMessage(403, giveItem.getName()));
						}
						
						if(effectOK){
							// 자신에게만 보이기
							pc.sendPackets(new S_SkillSound (pc.getId(), coin.getEffectNum()));
							
							// 주변유저들에게도 보이게 할것인가?
							if(coin.isAllEffect()){
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), coin.getEffectNum()));
							}
						}
						
						if (coin.isMent()) {
//							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "공격 1회 이상으로 아이템을 획득 하였습니다."));
//							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "N코인 (" + point + ")원을 획득 하였습니다."));
//							pc.sendPackets("N코인 (" + point + ")원을 획득 하였습니다.");
						}
					}
				}
				pc.setNCoinMon(false);
			}
		}
	}
	// 발라카스
	private void 발라카스(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 45684) { // 발라카스 레이드 3차 생기면 몬스터 번호 넣으면 됨
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.is발라카스() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 30) {
						createNewItem(pc, 410164, 2, 0); // 화룡의표식
						pc.setCurrentHp(pc.getMaxHp());
					}
				}
				pc.set발라카스(false);
			}
		}
	}

	// 파푸리온
	private void 파푸리온(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 900040) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.is파푸리온() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 30) {
						createNewItem(pc, 410163, 2, 0); // 수룡의표식
						pc.setCurrentHp(pc.getMaxHp());
					}
				}
				pc.set파푸리온(false);
			}
		}
	}

	// 안타라스
	private void 안타라스(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 900013) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.is안타라스() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 30) {
						createNewItem(pc, 410162, 2, 0); // 지룡의표식
						pc.setCurrentHp(pc.getMaxHp());
					}
				}
				pc.set안타라스(false);
			}
		}
	}

	// 린드비오르
	private void 린드비오르(L1Character lastAttacker) {
		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 5100) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (lastAttacker.getMapId() == pc.getMapId() && pc.is린드비오르() && !pc.isDead()) {
					if (getLocation().getTileLineDistance(new Point(pc.getLocation())) < 30) {
						createNewItem(pc, 410165, 2, 0); // 풍령의표식
						pc.setCurrentHp(pc.getMaxHp());
					}
				}
				pc.set린드비오르(false);
			}
		}
	}

	private void calcCombo(L1Character lastAttacker) {
		if ((lastAttacker instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) lastAttacker;
			if(pc.getAccount() == null || pc.noPlayerCK)
				return;
			
			if (!pc.hasSkillEffect(L1SkillId.COMBO_BUFF)) {
				if ((pc.getAccount().getBlessOfAin() / 10000 > 100) && (CommonUtil.random(100) <= Config.COMBO_CHANCE)) {
					pc.setComboCount(1);
					pc.setSkillEffect(L1SkillId.COMBO_BUFF, 50000);
					pc.sendPackets(new S_PacketBox(204, pc.getComboCount()));
				}
			} else if (pc.getComboCount() < 30) {
				pc.setComboCount(pc.getComboCount() + 1);
				pc.sendPackets(new S_PacketBox(204, pc.getComboCount()));
			} else {
				pc.sendPackets(new S_PacketBox(204, 31));
			}
		}
	}

	public void re() {
		setDeathProcessing(true);
		setCurrentHp(0);
		setDead(true);
		getMap().setPassable(getLocation(), true);
		setDeathProcessing(false);
		setExp(0);
		setKarma(0);
		setLawful(0);
		allTargetClear();
		deleteRe();
	}

	// 오림 인던 관련
	private boolean _isCurseMimic;

	public void setCurseMimic(boolean curseMimic) {
		_isCurseMimic = curseMimic;
	}

	public boolean isCurseMimic() {
		return _isCurseMimic;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_MONSTER : _instanceType;
	}
	
	class TargetSorter implements Comparator<L1PcInstance>{
		@Override
		public int compare(L1PcInstance o1, L1PcInstance o2) {
			int law1 = o1.getLawful();
			int law2 = o2.getLawful();
			if(law1 < 0 || law2 < 0){
				if(law1 != law2)
					return law1 > law2 ? 1 : -1;
			}
			
			int dir1 = getLocation().getTileLineDistance(o1.getLocation());
			int dir2 = getLocation().getTileLineDistance(o2.getLocation());
			if(dir1 == dir2){
				return MJRnd.isBoolean() ? 1 : -1;
			}
			return dir1 > dir2 ? 1 : -1;
		}
	}
}
