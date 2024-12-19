package l1j.server.server.model.Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJPickupChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetItemTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PetCtrlMenu;
import l1j.server.server.serverpackets.S_PetMenuPacket;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1PetItem;
import l1j.server.server.templates.L1PetType;

public class L1PetInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	private static Random _random = new Random(System.nanoTime());

	public synchronized void deleteMe() {
		Object aobj[] = _master.getPetList().values().toArray();
		for(int i = 0; i < aobj.length; i++)
			if(aobj[i] == this)
				_petMaster.sendPackets(new S_ReturnedStat(12, i * 3, getId(), false));
		super.deleteMe();
	}

	@Override
	public boolean noTarget() {
		if (_currentPetStatus == 3) {
			return true;
		} else if (_currentPetStatus == 4) { 
			if (_petMaster != null && _petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(_petMaster.getLocation()) < 5) {
				int dir = targetReverseDirection(_petMaster.getX(), _petMaster.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setSleepTime(setDirectionMove(dir));
			} else {
				_currentPetStatus = 3;
				return true;
			}
		} else if (_currentPetStatus == 5) {
			if (Math.abs(getHomeX() - getX()) > 1
					|| Math.abs(getHomeY() - getY()) > 1) {
				int dir = moveDirection(getHomeX(), getHomeY());
				if (dir == -1) {
					setHomeX(getX());
					setHomeY(getY());
				} else {
					setSleepTime(setDirectionMove(dir));
				}
			}
		} else if (_currentPetStatus == 7) { 
			if (_petMaster != null
					&& _petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(
							_petMaster.getLocation()) <= 1) {
				_currentPetStatus = 3;
				return true;
			}
			int locx = _petMaster.getX() + _random.nextInt(1);
			int locy = _petMaster.getY() + _random.nextInt(1);
			int dir = moveDirection(locx, locy);
			if (dir == -1) { 
				_currentPetStatus = 3;
				return true;
			}
			setSleepTime(setDirectionMove(dir));
		} else if (_petMaster != null && _petMaster.getMapId() == getMapId()) { 
			if (getLocation().getTileLineDistance(_petMaster.getLocation()) > 2) {
				int dir = moveDirection(_petMaster.getX(), _petMaster.getY());
				if (dir == -1) { 
					_currentPetStatus = 3;
					return true;
				}
				setSleepTime(setDirectionMove(dir));
				if (_currentPetStatus == 8) {
					collect();
				}
			}
		} else {
			_currentPetStatus = 3;
			return true;
		}
		return false;
	}

	public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet l1pet) {
		super(template);
		
		_petMaster = master;
		_petMasterId = master.getId();
		_itemObjId = l1pet.get_itemobjid();
		_type = PetTypeTable.getInstance().get(template.get_npcId());

		setId(l1pet.get_objid());
		setName(l1pet.get_name());
		setLevel(l1pet.get_level());
		setMaxHp(l1pet.get_hp());
		setCurrentHp(l1pet.get_hp());
		setMaxMp(l1pet.get_mp());
		setCurrentMp(l1pet.get_mp());
		setExp(l1pet.get_exp());
		setExpPercent(ExpTable.getExpPercentage(l1pet.get_level(), l1pet
				.get_exp()));
		setLawful(l1pet.get_lawful());
		setTempLawful(l1pet.get_lawful());

		setMaster(master);
		setX(master.getX() + _random.nextInt(5) - 2);
		setY(master.getY() + _random.nextInt(5) - 2);
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());

		_currentPetStatus = 3;

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.addPet(this);
		Object aobj[] = master.getPetList().values().toArray();
		master.sendPackets(new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));
	}

	public L1PetInstance(L1NpcInstance target, L1PcInstance master, int itemid) {
		super(null);
		
		_petMaster = master;
		_petMasterId = master.getId();
		_itemObjId = itemid;
		_type = PetTypeTable.getInstance().get(
				target.getNpcTemplate().get_npcId());

		setId(IdFactory.getInstance().nextId());
		setting_template(target.getNpcTemplate());
		setCurrentHp(target.getCurrentHp());
		setCurrentMp(target.getCurrentMp());
		setExp(750);
		setExpPercent(0);
		setLawful(0);
		setTempLawful(0);

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		setHeading(target.getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);
		setInventory(target.getInventory());
		target.setInventory(null);

		_currentPetStatus = 3;

		target.deleteMe();
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}

		master.addPet(this);
		Object aobj[] = master.getPetList().values().toArray();
		master.sendPackets(new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));	
		PetTable.getInstance().storeNewPet(target, getId(), itemid);
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0) {
			if (damage > 0) { 
				setHate(attacker, 0);
				if(hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)){
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				}else if (hasSkillEffect(L1SkillId.PHANTASM)){
					removeSkillEffect(L1SkillId.PHANTASM);
				}
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.set_pet_target(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			death(attacker);
		}
	}

	public synchronized void death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			setCurrentHp(0);

			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
		}
	}

	public void evolvePet(int new_itemobjid) {

		L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
		if (l1pet == null) {
			return;
		}

		int newNpcId = _type.getNpcIdForEvolving();
		int tmpMaxHp = getMaxHp();
		int tmpMaxMp = getMaxMp();

		transform(newNpcId);
		_type = PetTypeTable.getInstance().get(newNpcId);

		setLevel(1);
		setMaxHp(tmpMaxHp / 2);
		setMaxMp(tmpMaxMp / 2);
		setCurrentHp(getMaxHp());
		setCurrentMp(getMaxMp());
		setExp(0);
		setExpPercent(0);

		getInventory().clearItems();

		PetTable.getInstance().deletePet(_itemObjId);

		l1pet.set_itemobjid(new_itemobjid);
		l1pet.set_npcid(newNpcId);
		l1pet.set_name(getName());
		l1pet.set_level(getLevel());
		l1pet.set_hp(getMaxHp());
		l1pet.set_mp(getMaxMp());
		l1pet.set_exp(getExp());
		PetTable.getInstance().storeNewPet(this, getId(), new_itemobjid);

		_itemObjId = new_itemobjid;
	}

	public void liberate() {
		L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
		monster.setId(IdFactory.getInstance().nextId());

		monster.setX(getX());
		monster.setY(getY());
		monster.setMap(getMapId());
		monster.setHeading(getHeading());
		monster.set_storeDroped(true);
		monster.setInventory(getInventory());
		setInventory(null);
		monster.setLevel(getLevel());
		monster.setMaxHp(getMaxHp());
		monster.setCurrentHp(getCurrentHp());
		monster.setMaxMp(getMaxMp());
		monster.setCurrentMp(getCurrentMp());
		
		_petMaster.getPetList().remove(getId());
		deleteMe();

		if (_petMaster.getPetList().isEmpty()) {
			_petMaster.sendPackets(new S_PetCtrlMenu((L1PcInstance) _master, monster, false));
		}
		
		_petMaster.getInventory().removeItem(_itemObjId, 1);
		PetTable.getInstance().deletePet(_itemObjId);

		L1World.getInstance().storeObject(monster);
		L1World.getInstance().addVisibleObject(monster);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(monster)) {
			onPerceive(pc);
		}
	}

	public void collect() {
		L1Inventory targetInventory = _petMaster.getInventory();
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		L1ItemInstance item = null;
		
		L1Party party = _petMaster.getParty();
		
		for (int i = 0; i < size; i++) {
			item = items.get(0);
			if (item.isEquipped()) {
				continue;
			}
			
			try{
			/**파티 분배*/
			if(party != null){
				PartyCollect(party, item);
				continue;
			}
			}catch(Exception e){}
			
			if (_petMaster.getInventory().checkAddItem( 
					item, item.getCount()) == L1Inventory.OK) {
				_inventory.tradeItem(item, item.getCount(), targetInventory);
				_petMaster.sendPackets(new S_ServerMessage(143, getName(), item
						.getLogName())); 
			} else { 
				targetInventory = L1World.getInstance().getInventory(getX(),
						getY(), getMapId());
				_inventory.tradeItem(item, item.getCount(), targetInventory);
			}
			
		}
	}
	
	/**파티 분배*/
	public void PartyCollect(L1Party party, L1ItemInstance item){		
		if(party != null){
			L1Npc npc = NpcTable.getInstance().getTemplate(item.isDropMobId());
			L1Inventory inven = _petMaster.getInventory();
			int amount = item.getCount();
			if (party.getLeader().getPartyType() == 1 && item.isDropMobId() != 0 && party.getNumOfMembers() > 1) {
				try {
					item.setDropMobId(0);
					Stream<L1PcInstance> stream = party.createVisibleMembersStream(_petMaster);
					ArrayList<L1PcInstance> members = stream.collect(Collectors.toCollection(ArrayList::new));
					long len = members.size();
					if (item.getItemId() == L1ItemId.ADENA && amount > len) {
						int div = (int) (amount / len);
						int adder = (int) (amount % len);
						for (L1PcInstance pc : members) {
							int amt = div;
							if (pc.getId() == _petMaster.getId())
								amt += adder;
	
							if(MJPickupChain.getInstance().handle(pc, item, (int)amount))
								continue;
							
							System.out.println("펫아데나 : "+amt);
	
							_inventory.tradeItem(item, amt, pc.getInventory());
							
							if(npc != null){
							ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(),
									pc.getName());
							party.broadcastRootMent(pck);
							}
						}
					} else {
						L1PcInstance luck = members.get(MJRnd.next(members.size()));
						if(MJPickupChain.getInstance().handle(luck, item, (int)amount))
							return;
	
						_inventory.tradeItem(item, (int) amount, luck.getInventory());
						ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), luck.getName());
						party.broadcastRootMent(pck);
						members.clear();
					}
				} catch (Exception e) {
					if(MJPickupChain.getInstance().handle(_petMaster, item, (int)amount))
						return;
	
					_inventory.tradeItem(item, (int) amount, inven);
					if (npc != null) {
						ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), _petMaster.getName());
						party.broadcastRootMent(pck);
					}
				}
			} else {
				if(MJPickupChain.getInstance().handle(_petMaster, item, (int)amount))
					return;
	
				_inventory.tradeItem(item, (int) amount, inven);
				if (npc != null) {
					ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), _petMaster.getName());
					party.broadcastRootMent(pck);
				}
			}
		}
		
	}
	
	public void unloadMaster(){
		getMap().setPassable(getLocation(), true);
		dropItem();
		if(_petMaster != null)
			_petMaster.getPetList().remove(getId());
		deleteMe();
	}

	public void dropItem() {
		L1Inventory targetInventory = null;
		List<L1ItemInstance> items = _inventory.getItems();
		if(items == null || items.size() <= 0)
			return;
		
		int size = _inventory.getSize();
		L1ItemInstance item = null;
		if (_petMaster != null) {
			targetInventory = _petMaster.getInventory();
		} else {
			L1Object obj = L1World.getInstance().findObject(_petMasterId);
			if(obj == null || !(obj instanceof L1PcInstance)){
				if(!MySqlCharacterStorage.isValidUserObjectId(_petMasterId)){
					targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
				}
			}else{
				L1PcInstance pc = (L1PcInstance)obj;
				if(pc.getNetConnection() != null && pc.getNetConnection().isConnected()){
					targetInventory = pc.getInventory();	
				}
			}
		}
		
		// non-stop db inserted.
		if(targetInventory == null){
			CharactersItemStorage storage = CharactersItemStorage.create();
			for(int i=size - 1; i>=0; --i){
				item = items.get(0);
				item.setEquipped(false);
				try {
					storage.storeItem(_petMasterId, item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			_inventory.clearItems();
			
			// default owner trade item.
		}else{
			
			L1Party party = _petMaster.getParty();
			for (int i = 0; i < size; i++) {
				item = items.get(0);
				item.setEquipped(false);
				
				
				_inventory.tradeItem(item, item.getCount(), targetInventory);
			}
		}
	}

	public void call() {
		int id = _type.getMessageId(L1PetType.getMessageNumber(getLevel()));
		if (id != 0) {
			broadcastPacket(new S_NpcChatPacket(this, "$" + id, 0));
		}

		setCurrentPetStatus(7);
	}

	public void setTarget(L1Character target) {
		if (target != null
				&& (_currentPetStatus == 1 || _currentPetStatus == 2 || _currentPetStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public void setMasterTarget(L1Character target) {
		if (target != null && (_currentPetStatus == 1 || _currentPetStatus == 5)) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() != null)
			return;
		
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(this);
		perceivedFrom.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
		perceivedFrom.sendPackets(new S_PetPack(this, perceivedFrom));
		if (isDead()) {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
		}
		/** 피바 갱신 **/
		if (_petMaster != null && _petMaster == perceivedFrom) {
			_petMaster.sendPackets(new S_HPMeter(this));
		}
	}

	@Override
	public void onAction(L1PcInstance player) {
		if(player == null)
			return;
		
		L1Character cha = this.getMaster();
		L1PcInstance master = (L1PcInstance) cha;
		if ( master == null)return;
		if (master.get_teleport()) { 
			return;
		}
		if (getZoneType() == 1) {
			L1Attack attack_mortion = new L1Attack(player, this); 
			attack_mortion.action();
			return;
		}

		if (player.checkNonPvP(player, this)) {
			return;
		}

		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		if (_petMaster.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, getExpPercent()));
			L1Pet l1pet = PetTable.getInstance().getTemplate(_itemObjId);
			if (l1pet != null) {
				l1pet.set_exp(getExp());
				l1pet.set_level(getLevel());
				l1pet.set_hp(getMaxHp());
				l1pet.set_mp(getMaxMp());
				PetTable.getInstance().storePet(l1pet);
			}
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = actionType(action);
		if (status == 0) {
			return;
		}
		if (status == 6) {
			liberate();
		} else {
			Object[] petList = _petMaster.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1PetType type = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					if (_petMaster != null && _petMaster.getLevel() >= pet.getLevel()) {
						pet.setCurrentPetStatus(status);
					} else {
						type = PetTypeTable.getInstance().get(pet.getNpcTemplate().get_npcId());
						int id = type.getDefyMessageId();
						if (id != 0) {
							broadcastPacket(new S_NpcChatPacket(pet,
									"$" + id, 0));
						}
					}
				}
			}
			player.sendPackets(new S_PetMenuPacket(this, getExpPercent()));
		}
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			useItem(USEITEM_HASTE, 100);
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			useItem(USEITEM_HEAL, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		if (item == null || item.getItem() == null)
			return;
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays
				.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	private int actionType(String action) {
		int status = 0;
		if (action.equalsIgnoreCase("aggressive")) { 
			status = 1;
		} else if (action.equalsIgnoreCase("defensive")) { 
			status = 2;
		} else if (action.equalsIgnoreCase("stay")) {
			status = 3;
		} else if (action.equalsIgnoreCase("extend")) { 
			status = 4;
		} else if (action.equalsIgnoreCase("alert")) { 
			status = 5;
		} else if (action.equalsIgnoreCase("dismiss")) { 
			status = 6;
		} else if (action.equalsIgnoreCase("getitem")) {
			status = 8;
			//collect();
			collection();
		}
		return status;
	}

	private void collection() {
		ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();

		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 10)) {
			if (obj != null && obj instanceof L1GroundInventory) {
				gInventorys.add((L1GroundInventory) obj);
			}
		}

		int groundinv = gInventorys.size();
		for(int i=0; i < groundinv; i++) {
			L1GroundInventory inventory = gInventorys.get(i);
			for (L1ItemInstance item : inventory.getItems()) { 
				if (getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK
						&& !item.getItem().isUseHighPet()) {
					_targetItem = item;
					_targetItemList.add(_targetItem);
				}
			}
		}
	}
	
	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}

		if (_petMaster != null) {
			L1PcInstance Master = _petMaster;
			Master.sendPackets(new S_HPMeter(this));
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	public void setCurrentPetStatus(int i) {
		_currentPetStatus = i;
		if (_currentPetStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}
		if (_currentPetStatus == 7) {
			allTargetClear();
		}

		if (_currentPetStatus == 3) {
			allTargetClear();
		} else {
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	public void usePetWeapon(L1ItemInstance weapon) {
		if (getWeapon() == null) {
			setPetWeapon(weapon);
		} else { // 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (getWeapon().equals(weapon)) {
				removePetWeapon(getWeapon());
			} else {
				removePetWeapon(getWeapon());
				setPetWeapon(weapon);
			}
		}
	}

	public void usePetArmor(L1ItemInstance armor) {
		if (getArmor() == null) {
			setPetArmor(armor);
		} else { // 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (getArmor().equals(armor)) {
				removePetArmor(getArmor());
			} else {
				removePetArmor(getArmor());
				setPetArmor(armor);
			}
		}
	}

	private void setPetWeapon(L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

		if (petItem == null) { return; }

		setHitByWeapon(petItem.getHitModifier());
		setDamageByWeapon(petItem.getDamageModifier());
		getAbility().addAddedStr(petItem.getAddStr());
		getAbility().addAddedCon(petItem.getAddCon());
		getAbility().addAddedDex(petItem.getAddDex());
		getAbility().addAddedInt(petItem.getAddInt());
		getAbility().addAddedWis(petItem.getAddWis());
		addMaxHp(petItem.getAddHp());
		addMaxMp(petItem.getAddMp());
		getAbility().addSp(petItem.getAddSp());
		getResistance().addMr(petItem.getAddMr());

		setWeapon(weapon);
		weapon.setEquipped(true);
	}

	public void setPetArmor(L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

		if (petItem == null) { return; }

		getAC().addAc(petItem.getAddAc());
		getAbility().addAddedStr(petItem.getAddStr());
		getAbility().addAddedCon(petItem.getAddCon());
		getAbility().addAddedDex(petItem.getAddDex());
		getAbility().addAddedInt(petItem.getAddInt());
		getAbility().addAddedWis(petItem.getAddWis());
		addMaxHp(petItem.getAddHp());
		addMaxMp(petItem.getAddMp());
		getAbility().addSp(petItem.getAddSp());
		getResistance().addMr(petItem.getAddMr());

		setArmor(armor);
		armor.setEquipped(true);
	}

	public void removePetWeapon(L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

		if (petItem == null) { return; }

		setHitByWeapon(0);
		setDamageByWeapon(0);
		getAbility().addAddedStr(-petItem.getAddStr());
		getAbility().addAddedCon(-petItem.getAddCon());
		getAbility().addAddedDex(-petItem.getAddDex());
		getAbility().addAddedInt(-petItem.getAddInt());
		getAbility().addAddedWis(-petItem.getAddWis());
		addMaxHp(-petItem.getAddHp());
		addMaxMp(-petItem.getAddMp());
		getAbility().addSp(-petItem.getAddSp());
		getResistance().addMr(-petItem.getAddMr());

		setWeapon(null);
		weapon.setEquipped(false);
	}

	public void removePetArmor(L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

		if (petItem == null) { return; }

		getAC().addAc(-petItem.getAddAc());
		getAbility().addAddedStr(-petItem.getAddStr());
		getAbility().addAddedCon(-petItem.getAddCon());
		getAbility().addAddedDex(-petItem.getAddDex());
		getAbility().addAddedInt(-petItem.getAddInt());
		getAbility().addAddedWis(-petItem.getAddWis());
		addMaxHp(-petItem.getAddHp());
		addMaxMp(-petItem.getAddMp());
		getAbility().addSp(-petItem.getAddSp());
		getResistance().addMr(-petItem.getAddMr());

		setArmor(null);
		armor.setEquipped(false);
	}

	public int getCurrentPetStatus() {
		return _currentPetStatus;
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setExpPercent(int expPercent) {
		_expPercent = expPercent;
	}

	public int getExpPercent() {
		return _expPercent;
	}
	private L1ItemInstance _weapon;

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	private L1ItemInstance _armor;

	public void setArmor(L1ItemInstance armor) {
		_armor = armor;
	}

	public L1ItemInstance getArmor() {
		return _armor;
	}

	private int _hitByWeapon;

	public void setHitByWeapon(int i) {
		_hitByWeapon = i;
	}

	public int getHitByWeapon() {
		return _hitByWeapon;
	}

	private int _damageByWeapon;

	public void setDamageByWeapon(int i) {
		_damageByWeapon = i;
	}

	public int getDamageByWeapon() {
		return _damageByWeapon;
	}

	private int _currentPetStatus;
	private L1PcInstance _petMaster;
	private int _petMasterId;
	private int _itemObjId;
	private L1PetType _type;
	private int _expPercent;

	public L1PcInstance getMaster(){
		return _petMaster;
	}
	
	public int getMasterId(){
		return _petMasterId;
	}
	
	public L1PetType getPetType() {
		return _type;
	}

	@Override
	public boolean checkCondition() {
		if (_petMaster == null) {
			return true;
		}

		if (_petMaster.isInWarArea()) {
			dropItem();
			_petMaster.getPetList().remove(getId());
			deleteMe();

			return true;
		}

		return false;
	}
	
	public void usePetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		if (pet.getWeapon() == null) {
			setPetWeapon(pc, pet, weapon);
		} else { // 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (pet.getWeapon().equals(weapon)) {
				removePetWeapon(pc, pet, pet.getWeapon());
			} else {
				removePetWeapon(pc, pet, pet.getWeapon());
				setPetWeapon(pc, pet, weapon);
			}
		}
	}

	public void usePetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		if (pet.getArmor() == null) {
			setPetArmor(pc, pet, armor);
		} else { // 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (pet.getArmor().equals(armor)) {
				removePetArmor(pc, pet, pet.getArmor());
			} else {
				removePetArmor(pc, pet, pet.getArmor());
				setPetArmor(pc, pet, armor);
			}
		}
	}

	public void setPetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(petItem.getHitModifier());
		pet.setDamageByWeapon(petItem.getDamageModifier());
		pet.getAbility().addAddedStr(petItem.getAddStr());
		pet.getAbility().addAddedCon(petItem.getAddCon());
		pet.getAbility().addAddedDex(petItem.getAddDex());
		pet.getAbility().addAddedInt(petItem.getAddInt());
		pet.getAbility().addAddedWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.getResistance().addMr(petItem.getAddMr());

		pet.setWeapon(weapon);
		weapon.setEquipped(true);
	}

	public void removePetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(0);
		pet.setDamageByWeapon(0);
		pet.getAbility().addAddedStr(-petItem.getAddStr());
		pet.getAbility().addAddedCon(-petItem.getAddCon());
		pet.getAbility().addAddedDex(-petItem.getAddDex());
		pet.getAbility().addAddedInt(-petItem.getAddInt());
		pet.getAbility().addAddedWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.getResistance().addMr(-petItem.getAddMr());

		pet.setWeapon(null);
		weapon.setEquipped(false);
	}

	public void setPetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.getAC().addAc(petItem.getAddAc());
		pet.getAbility().addAddedStr(petItem.getAddStr());
		pet.getAbility().addAddedCon(petItem.getAddCon());
		pet.getAbility().addAddedDex(petItem.getAddDex());
		pet.getAbility().addAddedInt(petItem.getAddInt());
		pet.getAbility().addAddedWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.getResistance().addMr(petItem.getAddMr());

		pet.setArmor(armor);
		armor.setEquipped(true);
	}

	public void removePetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.getAC().addAc(-petItem.getAddAc());
		pet.getAbility().addAddedStr(-petItem.getAddStr());
		pet.getAbility().addAddedCon(-petItem.getAddCon());
		pet.getAbility().addAddedDex(-petItem.getAddDex());
		pet.getAbility().addAddedInt(-petItem.getAddInt());
		pet.getAbility().addAddedWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.getResistance().addMr(-petItem.getAddMr());

		pet.setArmor(null);
		armor.setEquipped(false);
	}

}
