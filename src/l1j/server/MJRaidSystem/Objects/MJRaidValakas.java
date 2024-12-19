package l1j.server.MJRaidSystem.Objects;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.MJRaidSystem.MJRaidObject;
import l1j.server.MJRaidSystem.MJRaidType;
import l1j.server.MJRaidSystem.BossSkill.MJRaidBossCombo;
import l1j.server.MJRaidSystem.BossSkill.MJRaidBossSkill;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJRaidSystem.Spawn.MJRaidNpcSpawn;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJRaidValakas extends MJRaidObject{
	private static final ServerBasePacket[] s_startMessages = new ServerBasePacket[]{
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$25548"),
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$25549"),
	};
	
	private static final ServerBasePacket[] s_startMessagesValakas = new ServerBasePacket[]{
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fA$25533"),
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fA$25534"),
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fA$25535"),
	};
	
	private static final ServerBasePacket s_blackDisplay = new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY);
	private static final ServerBasePacket s_valaksDisplay = new S_DisplayEffect(S_DisplayEffect.VALAKAS_BORDER_DISPLAY);
	
	public MJRaidValakas(L1PcInstance owner, MJRaidType type){
		super(owner, type);
	}

	@Override
	public void init(){
		super.init();
		
		if(_fires == null || _fires.size() <= 0){	
			if(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE_IS_POSSPAWN)
				_fires =  createFires2();
			else
				_fires =  createFires();
		}
	}
	
	@Override
	protected void resetRaid(){
		super.resetRaid();
		
		if(_fires == null || _fires.size() <= 0){	
			if(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE_IS_POSSPAWN)
				_fires =  createFires2();
			else
				_fires =  createFires();
		}
	}
	
	@Override
	protected void setNormalSpawn(MJRaidNpcSpawn spawn) {
		_npcList.add(spawn.spawn(getCopyMapId()));
	}

	@Override
	protected void setRectSpawn(MJRaidNpcSpawn spawn) {
		ArrayList<L1NpcInstance> list = spawn.spawnRectangle(getCopyMapId());
		if(list != null)
			_npcList.addAll(list);
	}

	@Override
	protected void setCircleSpawn(MJRaidNpcSpawn spawn) {
		ArrayList<L1NpcInstance> list = spawn.spawnCircle(getCopyMapId());
		if(list != null)
			_npcList.addAll(list);
	}

	@Override
	protected void setBossSpawn(MJRaidNpcSpawn spawn) {
		_bossSpawn = spawn;
	}

	@Override
	public void dispose(){
		super.dispose();
		
		if(_npcList != null){
			_npcList.clear();
			_npcList = null;
		}
	}
	
	private MJRaidNpcSpawn createSpawn(int npcId, int type, int numOfSpawn, int range, int heading){
		return new MJRaidNpcSpawn(
				npcId, 
				type, 
				numOfSpawn, 
				_bossSpawn.getLeft() - range,
				_bossSpawn.getTop() - range,
				_bossSpawn.getLeft() + range,
				_bossSpawn.getTop() + range,
				0);
	}
	
	
	
	private static MJRaidNpcSpawn _dustRain = null;
	private L1NpcInstance createDustRain(){
		if(_dustRain == null){
			_dustRain = createSpawn(
					MJRaidLoadManager.MRS_SP_VALAKAS_DUSTRAIN_ID,
					MJRaidNpcSpawn.ST_NORMAL,
					1,
					0,
					0);
		}
		
		return _dustRain.spawn(getCopyMapId());
	}
	
	private static MJRaidNpcSpawn _valakasEye = null;
	private L1NpcInstance createValakasEye(){
		if(_valakasEye == null){
			_valakasEye = createSpawn(
					MJRaidLoadManager.MRS_SP_VALAKAS_EYE_ID,
					MJRaidNpcSpawn.ST_NORMAL,
					1,
					0,
					0);
		}
		
		return _valakasEye.spawn(getCopyMapId());
	}
	
	private static S_EffectLocation _startMeteorEffect = null;
	private void createStartMeteor(){
		if(_startMeteorEffect == null){
			_startMeteorEffect = new S_EffectLocation(_bossSpawn.getLeft(), _bossSpawn.getTop(), 15930);
		}
		
		int size = _users.size();
		L1PcInstance pc = null;
		for(int i=0; i<size; i++){
			pc = _users.get(i);
			if(pc == null || pc.getMapId() != _boss.getMapId())
				continue;
			
			pc.sendPackets(_startMeteorEffect, false);
			if(pc.isDead())
				continue;
			
			pc.receiveDamage(_boss, 100);
		}
	}
	
	private ArrayList<L1NpcInstance> _fires;
	@Override
	protected void readyRaid() {
		L1PcInstance 	pc 		= null;
		int 			size 	= 0;
		int				mid		= getCopyMapId();
		try{
			Thread.sleep(MJRaidLoadManager.MRS_SP_VALAKAS_READY_TIME);
			
			if(_users == null)
				return;
			
			size = _users.size();
			
			for(int i=0; i<s_startMessages.length; i++){
				Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
				for(int j=0; j<size; j++){
					pc = _users.get(j);
					if(pc != null && pc.getMapId() == mid){
						pc.sendPackets(s_startMessages[i], false);
						
						if(i == 0)
							pc.sendPackets(s_quakeDisplay, false);
					}
				}
			}
			Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
			L1NpcInstance npc = null;
			
			for(int i=0; i<s_startMessagesValakas.length; i++){
				Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
				for(int j=0; j<size; j++){
					pc = _users.get(j);
					if(pc != null && pc.getMapId() == mid){
						if(i == 0){
							pc.sendPackets(s_blackDisplay, false);
							Thread.sleep(500);
							pc.sendPackets(s_valaksDisplay, false);
						}
						pc.sendPackets(s_startMessagesValakas[i], false);
					}
				}
				if(i == 0){
					npc = createDustRain();
					if(npc != null){
						Thread.sleep(1000);
						npc.deleteMe();
						npc = null;
					}					
				}
			}
			Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
			npc	= createValakasEye();
			if(npc != null){
				Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
				npc.deleteMe();
				npc = null;
			}
			Thread.sleep(MJRaidLoadManager.MRS_MESSAGE_DELAY);
			
			for(int i=0; i<size; i++){
				pc = _users.get(i);
				if(pc != null && pc.getMapId() == mid)
					pc.sendPackets(s_quakeDisplay,false);
			}
			
			if(_bossSpawn != null){
				_boss = _bossSpawn.spawnBoss(getCopyMapId());
				createStartMeteor();
				deleteFires(_fires);
				_state = RS_START;
				GeneralThreadPool.getInstance().schedule(this, MJRaidLoadManager.MRS_MESSAGE_DELAY);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void runRaid() {
		try{
			int ssize 				= 0;
			int csize				= 0;
			int count				= 0;
			int phase				= 0;
			double perToHp			= 0;
			long startTime			= System.currentTimeMillis();
			long cur				= 0;
			MJRaidBossSkill skill 	= null;
			MJRaidBossCombo combo	= null;
			
			S_NpcChatPacket s_chat	= new S_NpcChatPacket(_boss, "$25562", 0);
			perToHp = ((double)_boss.getMaxHp() / 100D);
			if(_skills != null)
				ssize = _skills.size();
			
			if(_combos != null)
				csize = _combos.size();
			
			while((_state & RS_START) > 0){
				Thread.sleep(MJRaidLoadManager.MRS_THREAD_CLOCK);
				
				// 한번 더 검사.
				if((_state & RS_START) == 0)
					return;
				
				if(!isInRaid()){
					resetRaid();
					return;
				}
				
				if(_boss == null)
					continue;
				
				if(_boss.isDead()){
					successRaid();
					waitClose();
					closeRaid();
					return;
				}
				
				cur = System.currentTimeMillis();
				if(cur - startTime >= MJRaidLoadManager.MRS_SP_VALAKAS_PRODUCT_TIME){
					startTime = cur;
					Broadcaster.broadcastPacket(_boss, s_chat, false);
					new SpawnProductTask();
					Thread.sleep(3000);
					continue;
				}
				
				if(csize != 0 && (++count % MJRaidLoadManager.MRS_CB_COUNT) == 0){
					combo = _combos.get(_rnd.nextInt(csize));
					phase = combo.getPhase();
					if(((int)(perToHp * phase)) > _boss.getCurrentHp()){
						combo.action(_boss, _users);
						continue;
					}
				}
				
				if(ssize != 0 && _rnd.nextInt(100) <= MJRaidLoadManager.MRS_BS_USE_RATE){
					skill = _skills.get(_rnd.nextInt(ssize));
					skill.action(_boss, _users);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void bombHp(L1NpcInstance npc){
		int size 	= _users.size();
		int cx 		= 0;
		int cy 		= 0;
		L1PcInstance pc = null;
		for(int i=0; i<size; i++){
			pc = _users.get(i);
			if(pc == null || pc.isDead() || pc.getMapId() != _boss.getMapId())
				continue;
			
			cx = Math.abs(pc.getX() - npc.getX());
			cy = Math.abs(pc.getY() - npc.getY());
			
			if(cx <= MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_RANGE && cy <= MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_RANGE){
				pc.receiveDamage(npc, MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_HPDRAIN);
				S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
				pc.sendPackets(s_gfx, false);
				Broadcaster.broadcastPacket(pc, s_gfx, true);
			}
		}
	}
	
	private void bombMp(L1NpcInstance npc){
		int size 	= _users.size();
		int cx 		= 0;
		int cy 		= 0;
		L1PcInstance pc = null;
		for(int i=0; i<size; i++){
			pc = _users.get(i);
			if(pc == null || pc.isDead() || pc.getMapId() != _boss.getMapId())
				continue;
			
			cx = Math.abs(pc.getX() - npc.getX());
			cy = Math.abs(pc.getY() - npc.getY());
			
			if(cx <= MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_RANGE && cy <= MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_RANGE){
				pc.setCurrentMp(pc.getCurrentMp() - MJRaidLoadManager.MRS_SP_VALAKAS_ABSORB_MPDRAIN);
				S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
				pc.sendPackets(s_gfx, false);
				Broadcaster.broadcastPacket(pc, s_gfx, true);
			}
		}
	}
	
	class SpawnProductTask extends TimerTask{
		private Timer			_spTimer;
		private ArrayList<L1NpcInstance> _mpAbsorb;
		private ArrayList<L1NpcInstance> _hpAbsorb;
		
		public SpawnProductTask(){
			if(_users == null || _users.size() <= 0)
				return;
			
			int mid = getCopyMapId();
			MJRaidNpcSpawn sp = createSpawn(
					MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_ID, 
					MJRaidNpcSpawn.ST_RECT,
					MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_NUM,
					10,
					0);
			_mpAbsorb = sp.spawnRectangleBossAndExplosion(mid, MJRaidLoadManager.MRS_SP_VALAKAS_PRODUCT_DESTTIME);
			sp = createSpawn(
					MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_ID, 
					MJRaidNpcSpawn.ST_RECT,
					MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_NUM,
					10,
					0);
			_hpAbsorb = sp.spawnRectangleBossAndExplosion(mid, MJRaidLoadManager.MRS_SP_VALAKAS_PRODUCT_DESTTIME);
			
			int maxCount 	= MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_NUM + MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_NUM + 1;
			int size		= _users.size();
			int curCount	= 0;
			for(int i=0; i<size; i++){
				L1PcInstance pc = _users.get(i);
				if(pc == null || pc.isDead() || pc.getMapId() != mid)
					continue;
				
				if(++curCount > maxCount)
					break;
				
				pc.sendPackets(new S_SkillSound(pc.getId(), 15837));
				pc.setValakasProduct(true);
			}	
			_spTimer 			= new Timer();
			_spTimer.schedule(this, MJRaidLoadManager.MRS_SP_VALAKAS_PRODUCT_DESTTIME - 50);
		}
		
		@Override
		public void run() {
			if((_state & RS_START) == 0 || _boss == null || _boss.isDead())
				return;
			
			if(_users == null || _users.size() <= 0)
				return;
			
			for(L1PcInstance pc : _users){
				if(pc == null)
					continue;
				
				pc.setValakasProduct(false);
			}
			
			L1NpcInstance npc = null;
			if(_mpAbsorb != null){
				int size = _mpAbsorb.size();
				for(int i=0; i<size; i++){
					npc = _mpAbsorb.get(i);
					if(npc != null){
						if(npc.isDead()){
							npc.deleteMe();
							npc = null;
						}else{
							Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 19));
							bombMp(npc);
							npc.deleteMe();
							npc = null;
						}
					}
				}
				_mpAbsorb.clear();
				_mpAbsorb = null;
			}
			
			if(_hpAbsorb != null){
				int size = _hpAbsorb.size();
				for(int i=0; i<size; i++){
					npc = _hpAbsorb.get(i);
					if(npc != null){
						if(npc.isDead()){
							npc.deleteMe();
							npc = null;
						}else{
							Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 19));
							bombHp(npc);
							npc.deleteMe();
							npc = null;
						}
					}
				}
				_hpAbsorb.clear();
				_hpAbsorb = null;
			}
		}
	}
	
	
	private static final int[][] GROUND_FIRE1 = new int[][]{
		{32766, 32896},
		{32765, 32895},
		{32763, 32896},
		{32764, 32897},
		{32769, 32898},
		{32777, 32896},
		{32778, 32895},
		{32779, 32896},
		{32778, 32893},
		{32778, 32889},
		{32781, 32890},
		{32779, 32887},
		{32778, 32886},
		{32779, 32884},
		{32780, 32883},
		{32779, 32885},
		{32777, 32882},
		{32776, 32881},
		{32777, 32880},
		{32775, 32881},
		{32774, 32879},
		{32768, 32882},
		{32767, 32884},
		{32764, 32884},
		{32763, 32887},
		{32763, 32893},
		{32762, 32894},
		{32789, 32896},
	};
	
	private static final int[][] GROUND_FIRE2 = new int[][]{
		{32770, 32897},
		{32768, 32897},
		{32771, 32899},
		{32772, 32898},
		{32773, 32899},
		{32774, 32898},
		{32776, 32897},
		{32776, 32895},
		{32779, 32890},
		{32778, 32885},
		{32778, 32883},
		{32775, 32882},
		{32769, 32883},
		{32769, 32881},
		{32767, 32881},
		{32776, 32884},
		{32762, 32886},
		{32764, 32893},
		{32763, 32883},
		{32763, 32882},
	};
	
	private static final int[][] GROUND_FIRE3 = new int[][]{
		{32763, 32897},
		{32769, 32899},
		{32776, 32898},
		{32779, 32892},
		{32779, 32893},
		{32780, 32888},
		{32777, 32884},
		{32779, 32881},
		{32773, 32882},
		{32772, 32881},
		{32763, 32889},
		{32762, 32890},
		{32763, 32890},
		{32765, 32881},
		{32778, 32904},
	};
	
	private static final int[][] GROUND_FIRE4 = new int[][]{
		{32766, 32898},
		{32765, 32898},
		{32763, 32898},
		{32773, 32898},
		{32779, 32898},
		{32771, 32882},
		{32770, 32881},
		{32765, 32889},
		{32764, 32891},
		{32765, 32882},
		{32770, 32877},
		{32757, 32885},
	};
	
	private ArrayList<L1NpcInstance> createFires2(){
		if(_fireSpawn == null){
			_fireSpawn = new MJRaidNpcSpawn();			
		}
		ArrayList<L1NpcInstance> npcs = null;
		npcs = _fireSpawn.spawnForArray(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE1_ID, getCopyMapId(), GROUND_FIRE1);
		npcs.addAll(_fireSpawn.spawnForArray(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE2_ID, getCopyMapId(), GROUND_FIRE2));
		npcs.addAll(_fireSpawn.spawnForArray(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE3_ID, getCopyMapId(), GROUND_FIRE3));
		npcs.addAll(_fireSpawn.spawnForArray(MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE4_ID, getCopyMapId(), GROUND_FIRE4));
		return npcs;
	}
	
	private static MJRaidNpcSpawn _fireSpawn = null;
	private ArrayList<L1NpcInstance> createFires(){
		try{
			if(_fireSpawn == null)
				_fireSpawn = createSpawn(0, MJRaidNpcSpawn.ST_RECT, 0, MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE_RANGE, 0);
			
			return _fireSpawn.spawnRectLine(
					new int[]{
							MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE1_ID,
							MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE2_ID,
							MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE3_ID,
							MJRaidLoadManager.MRS_SP_VALAKAS_GROUNDFIRE4_ID
					}, 
					getCopyMapId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void deleteFires(ArrayList<L1NpcInstance> fires){
		L1NpcInstance[] fireArray 	= fires.toArray(new L1NpcInstance[fires.size()]);
		L1NpcInstance fire 			= null;
		int size 					= fireArray.length;
		for(int i=0; i<size; i++){
			fire = fireArray[i];
			if(fire == null)
				continue;
			
			Broadcaster.broadcastPacket(fire, new S_DoActionGFX(fire.getId(), 28));
		}
		try{
			Thread.sleep(500);
		}catch(Exception e){
			
		}
		for(int i=0; i<size; i++){
			fire = fireArray[i];
			if(fire == null)
				continue;
			
			fire.deleteMe();
			fire = null;
		}
		
		fires.clear();
		fires 		= null;
		fireArray 	= null;
	}
}
