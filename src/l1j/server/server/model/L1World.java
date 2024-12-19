/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import l1j.server.Config;
import l1j.server.MJ3SEx.MJNpcSpeedData;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJINNSystem.MJINNRoom;
import l1j.server.MJTemplate.MJFindObjectFilter;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.types.Point;

public class L1World {
	private static Logger _log = Logger.getLogger(L1World.class.getName());
	private final ConcurrentHashMap<String, L1PcInstance> 			_allPlayers;
	private final ConcurrentHashMap<Integer, L1PetInstance> 		_allPets;
	private final ConcurrentHashMap<Integer, MJCompanionInstance> 	_allCompanions;
	private final ConcurrentHashMap<Integer, L1SummonInstance> 		_allSummons;
	private final ConcurrentHashMap<Integer, L1SupportInstance> 	_allSupports;
	private final ConcurrentHashMap<Integer, L1Object> 				_allObjects;
	private final ConcurrentHashMap<Integer, L1Object>[] 			_visibleObjects;
	private final ConcurrentHashMap<Integer, L1PcInstance>[]		_visiblePlayers;
	private final ConcurrentHashMap<Integer, MJWar>					_allClanWars;
	private final ConcurrentHashMap<Integer, L1ItemInstance> 		_allitem;
	private final ConcurrentHashMap<Integer, L1Clan> 				_allClans;
	private final ConcurrentHashMap<Integer, L1NpcInstance> 		_allNpc;
	private final ConcurrentHashMap<Integer, L1NpcShopInstance> 	_allShopNpc;
	private final ConcurrentHashMap<Integer, L1GuardInstance> 		_allGuard;
	private final ConcurrentHashMap<Integer, L1CastleGuardInstance> _allCastleGuard;

	private int _weather = 4;

	private boolean _worldChatEnabled = true;

	private boolean _processingContributionTotal = false;

	private static final int MAX_MAP_ID = 32768;

	private static L1World _instance;

	@SuppressWarnings("unchecked")
	private L1World() {
		_allPlayers 	= new ConcurrentHashMap<String, L1PcInstance>(Config.MAX_ONLINE_USERS); 		// 모든 플레이어
		_allPets 		= new ConcurrentHashMap<Integer, L1PetInstance>(Config.MAX_ONLINE_USERS / 16); 		// 모든 애완동물
		_allCompanions = new ConcurrentHashMap<Integer, MJCompanionInstance>(Config.MAX_ONLINE_USERS / 16); // 모든 애완동물
		_allSummons 	= new ConcurrentHashMap<Integer, L1SummonInstance>(Config.MAX_ONLINE_USERS / 16);	// 모든 서먼몬스타
		_allSupports 	= new ConcurrentHashMap<Integer, L1SupportInstance>(64); 							// 모든 서포트
		_visibleObjects = new ConcurrentHashMap[MAX_MAP_ID + 1]; 											// MAP 마다의 오브젝트(L1Inventory 들어가, L1ItemInstance는 없음)
		_visiblePlayers = new ConcurrentHashMap[MAX_MAP_ID + 1];
		_allClanWars 	= new ConcurrentHashMap<Integer, MJWar>(64);										// 모든 전쟁
		_allClans 		= new ConcurrentHashMap<Integer, L1Clan>(64);										// 모든 크란(Online/Offline 어느쪽이나)
		_allNpc 		= new ConcurrentHashMap<Integer, L1NpcInstance>(5120+(Config.MAX_ONLINE_USERS/2));	// 모든 npc
		_allShopNpc 	= new ConcurrentHashMap<Integer, L1NpcShopInstance>(256); 							// 모든 무인NPC상점
		_allitem 		= new ConcurrentHashMap<Integer, L1ItemInstance>(180 * Config.MAX_ONLINE_USERS * 4);
		_allGuard 		= new ConcurrentHashMap<Integer, L1GuardInstance>(256); 							// 모든 경비병
		_allCastleGuard = new ConcurrentHashMap<Integer, L1CastleGuardInstance>(128);
		_allObjects 	= new ConcurrentHashMap<Integer, L1Object>(											// 모든 오브젝트(L1ItemInstance 들어가, L1Inventory는 없음)
				(Config.MAX_ONLINE_USERS * 4) + 5120 + 256 + 64 + (Config.MAX_ONLINE_USERS / 16));
		for (int i = 0; i <= MAX_MAP_ID; i++) {
			if(i == 4){
				_visibleObjects[i] = new ConcurrentHashMap<Integer, L1Object>(2048);
				_visiblePlayers[i] = new ConcurrentHashMap<Integer, L1PcInstance>(Config.MAX_ONLINE_USERS);
			} else {
				_visibleObjects[i] = new ConcurrentHashMap<Integer, L1Object>(512);
				_visiblePlayers[i] = new ConcurrentHashMap<Integer, L1PcInstance>(128);
			}
		}
	}

	public static L1World getInstance() {
		if (_instance == null) {
			_instance = new L1World();
		}
		return _instance;
	}

	/**
	 * 모든 상태를 클리어 한다.<br>
	 * 디버그, 테스트등이 특수한 목적 이외로 호출해서는 안 된다.
	 */
	public void clear() {
		_instance = new L1World();
	}

	public void storeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}

		_allObjects.put(object.getId(), object);
		if (object instanceof L1PcInstance) {
			_allPlayers.put(((L1PcInstance) object).getName().toUpperCase(), (L1PcInstance) object);
		}else if(object instanceof L1NpcInstance){
			L1NpcInstance npc = (L1NpcInstance) object;
			_allNpc.put(npc.getNpcTemplate().get_npcId(), npc);
			MJNpcSpeedData.install_npc(npc);
			if (object instanceof L1PetInstance) {
				_allPets.put(object.getId(), (L1PetInstance) object);
			}else if (object instanceof L1SummonInstance) {
				_allSummons.put(object.getId(), (L1SummonInstance) object);
			}else if (object instanceof L1SupportInstance) {
				_allSupports.put(object.getId(), (L1SupportInstance) object);	
			}else if (object instanceof L1NpcShopInstance) {
				_allShopNpc.put(object.getId(), (L1NpcShopInstance) object);
			}else if(object instanceof MJCompanionInstance){
				_allCompanions.put(object.getId(), (MJCompanionInstance)object);
			}
		}
	}

	public void removeObject(L1Object object) {
		if (object == null) {
			throw new NullPointerException();
		}

		if(object.instanceOf(MJL1Type.L1TYPE_ITEMINSTANCE)){
			L1ItemInstance item = (L1ItemInstance)object;
			if(item.getItemId() == MJINNHelper.INN_KEYID)
				MJINNRoom.remove(item);
		}
		
		_allObjects.remove(object.getId());
		if (object instanceof L1PcInstance) {
			_allPlayers.remove(((L1PcInstance) object).getName().toUpperCase());
		}else if(object instanceof L1NpcInstance){
			_allNpc.remove(((L1NpcInstance) object).getNpcTemplate().get_npcId());

			if (object instanceof L1PetInstance) {
				_allPets.remove(object.getId());
			}else if (object instanceof L1SummonInstance) {
				_allSummons.remove(object.getId());
			}else if (object instanceof L1SupportInstance) {
				_allSupports.remove(object.getId());
			}else if (object instanceof L1NpcShopInstance) {
				_allShopNpc.remove(object.getId());
			}else if(object instanceof MJCompanionInstance){
				_allCompanions.remove(object.getId());
			}
		}
	}

	public L1Object findObject(int oID) {
		return _allObjects.get(oID);
	}

	public L1Object findObject(String name) {
		/*if (_allObjects.contains(name)) {
			return _allObjects.get(name);
		}*/
		int nl = name.length();
		for (L1PcInstance each : getAllPlayers()) {
			String s = each.getName();
			if(s.length() != nl)
				continue;
			
			if (s.equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}

	// _allObjects의 뷰
	private Collection<L1Object> _allValues;
	public Collection<L1Object> getObject() {
		Collection<L1Object> vs = _allValues;
		return (vs != null) ? vs : (_allValues = Collections.unmodifiableCollection(_allObjects.values()));
	}

	public L1GroundInventory getInventory(int x, int y, short map) {
		int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1; // xy의 마이너스치를 인벤트리 키로서 사용
		Object object = _visibleObjects[map].get(inventoryKey);
		if (object == null) {
			return new L1GroundInventory(inventoryKey, x, y, map);
		} else {
			return (L1GroundInventory) object;
		}
	}

	public L1GroundInventory getInventory(L1Location loc) {
		return getInventory(loc.getX(), loc.getY(), (short) loc.getMap().getId());
	}

	public void addVisibleObject(L1Object object) {
		if (object == null)
			return;
		
		int map_id = object.getMapId();
		int id = object.getId();
		if (map_id <= MAX_MAP_ID) {
			_visibleObjects[map_id].put(id, object);
			if(object.instanceOf(MJL1Type.L1TYPE_PC)){
				_visiblePlayers[map_id].put(id, (L1PcInstance)object);
			}
		}
	}

	public void removeVisibleObject(L1Object object) {
		if (object == null)
			return;
		
		int map_id = object.getMapId();
		int id = object.getId();
		if (map_id <= MAX_MAP_ID) {
			_visibleObjects[map_id].remove(id);
			_visiblePlayers[map_id].remove(id);
		}
	}

	public void moveVisibleObject(L1Object object, int newMap){ // set_Map로 새로운 Map로 하기 전에 부르는 것
		if (object == null)
			return;
		
		int map_id = object.getMapId();
		int id = object.getId();
		if (map_id != newMap) {
			if (map_id <= MAX_MAP_ID) {
				_visibleObjects[map_id].remove(id);
				_visiblePlayers[map_id].remove(id);
			}
			if (newMap <= MAX_MAP_ID) {
				_visibleObjects[newMap].put(id, object);
				if(object.instanceOf(MJL1Type.L1TYPE_PC)){
					_visiblePlayers[newMap].put(id, (L1PcInstance)object);
				}
			}
		}
	}

	private ConcurrentHashMap<Integer, Integer> createLineMap(Point src, Point target) {
		ConcurrentHashMap<Integer, Integer> lineMap = new ConcurrentHashMap<Integer, Integer>(src.getTileDistance(target));
		
		/*
		 * http://www2.starcat.ne.jp/~fussy/algo/algo1-1.htm보다
		 */
		int E;
		int x;
		int y;
		int key;
		int i;
		int x0 = src.getX();
		int y0 = src.getY();
		int x1 = target.getX();
		int y1 = target.getY();
		int sx = (x1 > x0) ? 1 : -1;
		int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
		int sy = (y1 > y0) ? 1 : -1;
		int dy = (y1 > y0) ? y1 - y0 : y0 - y1;

		x = x0;
		y = y0;
		/* 기울기가 1 이하의 경우 */
		if (dx >= dy) {
			E = -dx;
			for (i = 0; i <= dx; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				x += sx;
				E += 2 * dy;
				if (E >= 0) {
					y += sy;
					E -= 2 * dx;
				}
			}
			/* 기울기가 1보다 큰 경우 */
		} else {
			E = -dy;
			for (i = 0; i <= dy; i++) {
				key = (x << 16) + y;
				lineMap.put(key, key);
				y += sy;
				E += 2 * dx;
				if (E >= 0) {
					x += sx;
					E -= 2 * dy;
				}
			}
		}

		return lineMap;
	}

	public ArrayList<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
		ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());

		int map = target.getMapId();
		Collection<L1Object> col	= _visibleObjects[map].values();
		if(col.size() <= 0 || map > MAX_MAP_ID)
			return null;
		
		ArrayList<L1Object> result 	= new ArrayList<L1Object>(lineMap.size());
		for (L1Object element : col) {
			if (element == null || element.equals(src))
				continue;
			
			int key = (element.getX() << 16) + element.getY();
			if (lineMap.containsKey(key))
				result.add(element);
		}

		return result;
	}

	public ArrayList<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
		int x = object.getX();
		int y = object.getY();
		int map = object.getMapId();
		Collection<L1Object> col = _visibleObjects[map].values();
		if(col.size() <= 0 || map > MAX_MAP_ID)
			return null;
		
		L1Location location = object.getLocation();
		ArrayList<L1Object> result = new ArrayList<L1Object>(width * height);
		int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
		double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
		double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);

		if (map <= MAX_MAP_ID) {
			for (L1Object element : col) {
				if (element == null || element.equals(object)) {
					continue;
				}
				if (map != element.getMapId()) {
					continue;
				}
				if (location.isSamePoint(element.getLocation())) {
					result.add(element);
					continue;
				}
				int distance = location.getTileLineDistance(element.getLocation());
				// 직선 거리가 높이, 폭어느 쪽보다 큰 경우, 계산할 것도 없이 범위외
				if (distance > height && distance > width) {
					continue;
				}

				// object의 위치를 원점과하기 위한 좌표 보정
				int x1 = element.getX() - x;
				int y1 = element.getY() - y;

				// Z축회전시키고 각도를 0번으로 한다.
				int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
				int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

				int xmin = 0;
				int xmax = height;
				int ymin = -width;
				int ymax = width;

				// 깊이가 사정과 맞물리지 않기 때문에 직선 거리로 판정하도록(듯이) 변경.
				// if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
				// ymax) {
				if (rotX > xmin && distance <= xmax && rotY >= ymin && rotY <= ymax)
					result.add(element);
			}
		}

		return result;
	}

	public final Collection<L1PcInstance> getVisiblePlayers(int map_id){
		return _visiblePlayers[map_id].values();
	}
	
	public void broadcast_map(int map_id, String message){
		broadcast_map(map_id, new S_SystemMessage(message));
	}
	
	public void broadcast_map(int map_id, ServerBasePacket pck){
		broadcast_map(map_id, pck, true);
	}
	
	public void broadcast_map(int map_id, ServerBasePacket pck, boolean is_clear){
		for(L1PcInstance pc : getVisiblePlayers(map_id)){
			if(pc != null)
				pc.sendPackets(pck, false);
		}
		if(is_clear)
			pck.clear();
	}
	
	public void broadcast_map(int map_id, ServerBasePacket[] pcks){
		broadcast_map(map_id, pcks, true);
	}
	
	public void broadcast_map(int map_id, ServerBasePacket[] pcks, boolean is_clear){
		for(L1PcInstance pc : getVisiblePlayers(map_id)){
			if(pc != null)
				pc.sendPackets(pcks, false);
		}
		if(is_clear){
			int len = pcks.length;
			for(int i=len - 1; i>=0; --i){
				pcks[i].clear();
				pcks[i] = null;
			}
		}
	}
	
	public ArrayList<L1Object> getVisibleObjects(L1Object object) {
		return getVisibleObjects(object, -1);
	}

	public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
		L1Map map = object.getMap();
		Point pt = object.getLocation();
		ArrayList<L1Object> result = null;

		if(radius < 0)
			result = new ArrayList<L1Object>(19 * 8);
		else if(radius == 0)
			result = new ArrayList<L1Object>(2);
		else
			result = new ArrayList<L1Object>(radius * 8);
		
		if (map.getId() <= MAX_MAP_ID) {
			Collection<L1Object> col = _visibleObjects[map.getId()].values();
			for (L1Object element : col) {
				if (element == null || element.equals(object)) {
					continue;
				}
				if (map != element.getMap()) {
					continue;
				}

				if (radius == -1) {
					if (pt.isInScreen(element.getLocation())) {
						result.add(element);
					}
				} else if (radius == 0) {
					if (pt.isSamePoint(element.getLocation())) {
						result.add(element);
					}
				} else {
					if (pt.getTileLineDistance(element.getLocation()) <= radius) {
						result.add(element);
					}
				}
			}
		}

		return result;
	}

	public boolean isVisibleNpc(int x, int y, int mid){
		Collection<L1Object> col = _visibleObjects[mid].values();
		for(L1Object obj : col){
			if(obj == null || obj.getMapId() != mid)
				continue;
			
			if(obj.getX() == x && obj.getY() == y){
				if(obj instanceof L1NpcInstance)
						return true;
			}
		}
		
		return false;
	}
	
	public boolean isVisibleObject(int x, int y, int mid){
		Collection<L1Object> col = _visibleObjects[mid].values();
		for(L1Object obj : col){
			if(obj == null || obj.getMapId() != mid)
				continue;
			
			if(obj.getX() == x && obj.getY() == y){
				if(obj instanceof L1PcInstance){
					if(!((L1PcInstance)obj).isInvisble())
						return true;
				}else if(obj instanceof L1MonsterInstance){
					if(((L1MonsterInstance)obj).getHiddenStatus() < 1)
						return true;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
		int mapId = loc.getMapId(); // 루프내에서 부르면(자) 무겁기 때문에
		Collection<L1Object> col 	= _visibleObjects[mapId].values();
		if(col.size() <= 0 || mapId > MAX_MAP_ID)
			return new ArrayList<L1Object>();
		ArrayList<L1Object> result 	= new ArrayList<L1Object>(radius * 8);
		for (L1Object element : _visibleObjects[mapId].values()) {
			if (element == null || mapId != element.getMapId()) 
				continue;

			if (loc.getTileLineDistance(element.getLocation()) <= radius)
				result.add(element);
		}
		return result;
	}

	public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
		return getVisiblePlayer(object, -1);
	}
	
	// 수정
	public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
		int map = object.getMapId();
		Point pt = object.getLocation();
		Collection<L1PcInstance> pc = _allPlayers.values();
		ArrayList<L1PcInstance> result = null;
		if(radius < 0)
			result = new ArrayList<L1PcInstance>(19 * 4);
		else if(radius == 0)
			result = new ArrayList<L1PcInstance>(2);
		else
			result = new ArrayList<L1PcInstance>(radius * 4);
		for (L1PcInstance element : pc) {
			if ((element == null) || (element.equals(object)) || (map != element.getMapId()))
				continue;
			if (radius == -1) {
				if (pt.isInScreen(element.getLocation()))
					result.add(element);
			} else if (radius == 0) {
				if (pt.isSamePoint(element.getLocation())) {
					result.add(element);
				}
			} else if (pt.getTileLineDistance(element.getLocation()) <= radius) {
				result.add(element);
			}
		}

		return result;
	}

	public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
		int map = object.getMapId();
		Point objectPt = object.getLocation();
		Point targetPt = target.getLocation();
		Collection<L1Object> col = _visibleObjects[map].values();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>(64);
		for (L1Object targetObject : col) {
			if (!(targetObject instanceof L1PcInstance)) {
				continue;
			}

			L1PcInstance element = (L1PcInstance) (targetObject);

			if (element == null || element.equals(object)) {
				continue;
			}

			if (Config.PC_RECOGNIZE_RANGE == -1) {
				if ((objectPt.isInScreen(element.getLocation())) && (!targetPt.isInScreen(element.getLocation()))) {
					result.add(element);
				}
			} else if ((objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE)
					&& (targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE)) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * object를 인식할 수 있는 범위에 있는 플레이어를 취득한다
	 * 
	 * @param object
	 * @return
	 */
	public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
		return getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
	}

	public L1PcInstance[] getAllPlayers3() {
		return _allPlayers.values().toArray(new L1PcInstance[_allPlayers.size()]);
	}

	private Collection<L1PcInstance> _allPlayerValues;

	public Collection<L1PcInstance> getAllPlayers() {
		Collection<L1PcInstance> vs = _allPlayerValues;
		return (vs != null) ? vs : (_allPlayerValues = Collections.unmodifiableCollection(_allPlayers.values()));
	}
	
	public int getAllPlayersSize(){
		return _allPlayers.size();
	}
	
	public Stream<L1PcInstance> getAllPlayerStream(){
		Collection<L1PcInstance> col = getAllPlayers();
		return col.size() > 1024 ? col.parallelStream() : col.stream();
	}
	
	public ArrayList<L1PcInstance> createAllPlayerArrayList(){
		return new ArrayList<L1PcInstance>(_allPlayers.values());
	}

	public Collection<L1NpcShopInstance> getAllNpcShop() {
		return Collections.unmodifiableCollection(_allShopNpc.values());
	}

	public Collection<L1NpcInstance> getAllNpc() {
		return Collections.unmodifiableCollection(_allNpc.values());
	}

	public Collection<L1Object> getAllObj() {
		return Collections.unmodifiableCollection(_allObjects.values());
	}

	public Collection<L1GuardInstance> getAllGuard() {
		return Collections.unmodifiableCollection(_allGuard.values());
	}

	public Collection<L1CastleGuardInstance> getAllCastleGuard() {
		return Collections.unmodifiableCollection(_allCastleGuard.values());
	}

	/**
	 * 월드내에 있는 지정된 이름의 플레이어를 취득한다.
	 * 
	 * @param name
	 *            - 플레이어명(소문자·대문자는 무시된다)
	 * @return 지정된 이름의 L1PcInstance. 해당 플레이어가 존재하지 않는 경우는 null를 돌려준다.
	 */
	public L1PcInstance getPlayer(String name) {
		if (null == name)
			return null;
		return _allPlayers.get(name.toUpperCase());
	}

	/**
	 * 월드내에 있는 지정된 이름의 무인NPC상점을 취득한다.
	 * 
	 * @param name
	 *            - 무인NPC상점명(소문자·대문자는 무시된다)
	 * @return 지정된 이름의 L1ShopNpcInstance. 해당 마네킹 존재하지 않는 경우는 null를 돌려준다.
	 */
	public L1NpcShopInstance getShopNpc(String name) {
		Collection<L1NpcShopInstance> npc = null;
		npc = getAllShopNpc();
		int nl = name.length();
		for (L1NpcShopInstance each : npc) {
			if (each == null)
				continue;
			
			String s = each.getName();
			if(s.length() != nl)
				continue;
			
			if (s.equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}

	// _allShopNpc의 뷰
	private Collection<L1NpcShopInstance> _allShopNpcValues;

	public Collection<L1NpcShopInstance> getAllShopNpc() {
		Collection<L1NpcShopInstance> vs = _allShopNpcValues;
		return (vs != null) ? vs : (_allShopNpcValues = Collections.unmodifiableCollection(_allShopNpc.values()));
	}

	// _allPets의 뷰
	private Collection<L1PetInstance> _allPetValues;

	public Collection<L1PetInstance> getAllPets() {
		Collection<L1PetInstance> vs = _allPetValues;
		return (vs != null) ? vs : (_allPetValues = Collections.unmodifiableCollection(_allPets.values()));
	}
	
	private Collection<MJCompanionInstance> _allCompanionValues;
	public Collection<MJCompanionInstance> getAllCompanions() {
		Collection<MJCompanionInstance> vs = _allCompanionValues;
		return (vs != null) ? vs : (_allCompanionValues = Collections.unmodifiableCollection(_allCompanions.values()));
	}

	public boolean contains_companion(int companion_id){
		return _allCompanions.contains(companion_id);
	}
	
	// _allSummons의 뷰
	private Collection<L1SummonInstance> _allSummonValues;

	public Collection<L1SummonInstance> getAllSummons() {
		Collection<L1SummonInstance> vs = _allSummonValues;
		return (vs != null) ? vs : (_allSummonValues = Collections.unmodifiableCollection(_allSummons.values()));
	}

	public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
		return _visibleObjects[mapId];
	}

	public void addWar(MJWar war) {
		_allClanWars.put(war.getId(), war);
	}

	public MJWar removeWar(MJWar war) {
		return _allClanWars.remove(war.getId());
	}
	
	public Stream<MJWar> createWarStream(){
		return _allClanWars.size() <= 0 ? null : _allClanWars.values().stream();
	}

	public void storeClan(L1Clan clan) {
		_allClans.put(clan.getClanId(), clan);
	}

	public void removeClan(L1Clan clan) {
		_allClans.remove(clan.getClanId());
	}

	public void clearClan() {
		_allClans.clear();
	}

	public L1Clan findClan(String clanName){
		int len = clanName.length();
		for(L1Clan clan : getAllClans()){
			String name = clan.getClanName();
			if(name.length() != len)
				continue;
			
			if(name.equalsIgnoreCase(clanName))
				return clan;
		}
		return null;
	}

	public L1Clan getClan(int clanId) {
		return _allClans.get(clanId);
	}

	// _allClans의 뷰
	private Collection<L1Clan> _allClanValues;

	public Collection<L1Clan> getAllClans() {
		Collection<L1Clan> vs = _allClanValues;
		return (vs != null) ? vs : (_allClanValues = Collections.unmodifiableCollection(_allClans.values()));
	}

	public void setWeather(int weather) {
		_weather = weather;
	}

	public int getWeather() {
		return _weather;
	}

	public void set_worldChatElabled(boolean flag) {
		_worldChatEnabled = flag;
	}

	public boolean isWorldChatElabled() {
		return _worldChatEnabled;
	}

	public void setProcessingContributionTotal(boolean flag) {
		_processingContributionTotal = flag;
	}

	public boolean isProcessingContributionTotal() {
		return _processingContributionTotal;
	}

	public L1Object[] getObject2() {
		return _allObjects.values().toArray(new L1Object[_allObjects.size()]);
	}

	/**
	 * 월드상에 존재하는 모든 플레이어에 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacketToAll(ServerBasePacket[] pcks) {
		for (L1PcInstance pc : getAllPlayers()) {
			if(pc == null)
				continue;
			
			for(ServerBasePacket pck : pcks)
				pc.sendPackets(pck, false);
		}
		
		for(ServerBasePacket pck : pcks)
			pck.clear();
	}
	
	public void broadcastPacketToAll(ServerBasePacket[] pcks, boolean isClear) {
		for (L1PcInstance pc : getAllPlayers()) {
			if(pc == null)
				continue;
			
			for(ServerBasePacket pck : pcks)
				pc.sendPackets(pck, false);
		}
		
		if(isClear){
			for(ServerBasePacket pck : pcks)
				pck.clear();
		}
	}
	
	public void broadcastPacketToAll(String message) {
		broadcastPacketToAll(new S_SystemMessage(message));
	}
	
	
	
	public void broadcastPacketToAll(ServerBasePacket packet) {
		for (L1PcInstance pc : getAllPlayers()) {
			if (pc != null)
				pc.sendPackets(packet, false);
		}
		
		packet.clear();
		packet = null;
	}

	public void broadcastPacketToAll(ServerBasePacket packet, boolean clear) {
		Collection<L1PcInstance> pclist = null;
		pclist = getAllPlayers();
		_log.finest("players to notify : " + pclist.size());
		for (L1PcInstance pc : pclist) {
			if (pc != null)
				pc.sendPackets(packet, false);
		}
		if (clear) {
			packet.clear();
			packet = null;
		}
	}

	public void getMapObject(int mapid) {
		Collection<L1Object> result = new ArrayList<L1Object>();
		result = _visibleObjects[mapid].values();
		for (L1Object obj : result) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getHiddenStatus() != 0 || mon.isDead())
					continue;

				if (mon.getMapId() == mapid)
					mon.re();
			}
		}
	}

	/**
	 * 월드상에 존재하는 모든 플레이어에 서버 메세지를 송신한다.
	 * 
	 * @param message
	 *            송신하는 메세지
	 */
	public void broadcastServerMessage(String message) {
		broadcastPacketToAll(new S_SystemMessage(message));
	}

	public L1NpcInstance findNpc(int id) {
		return _allNpc.get(id);
	}
	
	public L1NpcInstance findNpc(String name){
		int nl = name.length();
		for (L1NpcInstance each : getAllNpc()) {
			String s = each.getName();
			if(s.length() != nl)
				continue;
			
			if (s.equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}

	private Collection<L1ItemInstance> _allItemValues;

	public Collection<L1ItemInstance> getAllItem() {
		Collection<L1ItemInstance> vs = _allItemValues;
		return (vs != null) ? vs : (_allItemValues = Collections.unmodifiableCollection(_allitem.values()));
	}

	public L1PcInstance findpc(String name) {
		int nl = name.length();
		for (L1PcInstance each : getAllPlayers()) {
			String s = each.getName();
			if(nl != s.length())
				continue;
			
			if (s.equalsIgnoreCase(name)) {
				return each;
			}
		}
		return null;
	}
	
	public Stream<L1Object> createVisibleObjectsStream(int mapId){
		Collection<L1Object> col = _visibleObjects[mapId].values();
		return col.size() > 1024 ? col.parallelStream() : col.stream();
	}
	
	public Stream<L1Object> createVisibleObjectsStream(int mapId, MJFindObjectFilter filter){
		Stream<L1Object> stream = createVisibleObjectsStream(mapId);
		return stream.filter((L1Object vobj) ->
			!filter.isFilter(vobj)
		);
	}
	
	public Stream<L1Object> createVisibleObjectsStream(L1Object obj){
		return createVisibleObjectsStream(obj, -1);
	}
	
	public Stream<L1Object> createVisibleObjectsStream(L1Object obj, int radius){
		L1Map m = obj.getMap();
		int mid = m.getId();
		Point pt = obj.getLocation();
		Stream<L1Object> stream = null;
		if(mid <= MAX_MAP_ID){
			stream = createVisibleObjectsStream(mid);
			return stream.filter((L1Object vobj) ->			
				vobj != null && 
				vobj.getId() != obj.getId() && 
				vobj.getMapId() == mid &&
				(
						(radius > 0 && pt.getTileLineDistance(vobj.getLocation()) <= radius) ||
						(radius == -1 && pt.isInScreen(vobj.getLocation())) ||
						(radius == 0 && pt.isSamePoint(vobj.getLocation()))
				)
			);
		}
		return stream;
	}
	
	public ArrayList<L1Object> findVisibleObjectFromPosition(L1Object obj){
		int id = obj.getId();
		int mid = obj.getMapId();
		int x = obj.getX();
		int y = obj.getY();
		if(mid > MAX_MAP_ID)
			return new ArrayList<L1Object>();
		Stream<L1Object> stream = createVisibleObjectsStream(mid);
		return stream.filter((L1Object vobj) ->
			vobj != null && 
			vobj.getId() != id && 
			vobj.getMapId() == mid &&
			vobj.getX() == x &&
			vobj.getY() == y
		).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<L1Object> findVisibleObjectFromPosition(L1Object obj, MJFindObjectFilter filter){
		int id = obj.getId();
		int mid = obj.getMapId();
		int x = obj.getX();
		int y = obj.getY();
		if(mid > MAX_MAP_ID)
			return new ArrayList<L1Object>();
		Stream<L1Object> stream = createVisibleObjectsStream(mid);
		return stream.filter((L1Object vobj) ->
			vobj != null && 
			vobj.getId() != id && 
			vobj.getMapId() == mid &&
			vobj.getX() == x &&
			vobj.getY() == y &&
			!filter.isFilter(vobj)
		).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public long findVisibleObjectFromPositionCount(int id, int x, int y, int mid){
		if(mid > MAX_MAP_ID)
			return 0L;
		Stream<L1Object> stream = createVisibleObjectsStream(mid);
		return stream.filter((L1Object vobj) ->
			vobj != null && 
			vobj.getId() != id && 
			vobj.getMapId() == mid &&
			vobj.getX() == x &&
			vobj.getY() == y
		).count();
	}
	
	public long findVisibleObjectFromPositionCount(int id, int x, int y, int mid, MJFindObjectFilter filter){
		if(mid > MAX_MAP_ID)
			return 0L;
		Stream<L1Object> stream = createVisibleObjectsStream(mid);
		return stream.filter((L1Object vobj) ->
			vobj != null && 
			vobj.getId() != id && 
			vobj.getMapId() == mid &&
			vobj.getX() == x &&
			vobj.getY() == y &&
			!filter.isFilter(vobj)
		).count();
	}
	
	public void remove(int key) {
		try {
			L1PcInstance pc = (L1PcInstance) _allPlayers.get(Integer.valueOf(key));
			if (pc != null) {
				_allPlayers.remove(Integer.valueOf(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
