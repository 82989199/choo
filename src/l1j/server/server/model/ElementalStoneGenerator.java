package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.types.Point;

public class ElementalStoneGenerator implements Runnable {

	public static final int SLEEP_TIME = 300 * 1000; // 설치 종료후, 재설치까지의 시간 ms

	private static Logger _log = Logger.getLogger(ElementalStoneGenerator.class
			.getName());

	private static final int ELVEN_FOREST_MAPID = 4;
	private static final int MAX_COUNT = Config.ELEMENTAL_STONE_AMOUNT; // 설치 개수
	private static final int INTERVAL = 3; // 설치 간격초
	private static final int FIRST_X = 32911;
	private static final int FIRST_Y = 32210;
	private static final int LAST_X = 33141;
	private static final int LAST_Y = 32500;
	private static final int ELEMENTAL_STONE_ID = 40515; // 정령의 돌

	private ArrayList<L1GroundInventory> _itemList = new ArrayList<L1GroundInventory>(
			MAX_COUNT);
	private Random _random = new Random(System.nanoTime());

	private static ElementalStoneGenerator _instance = null;

	private ElementalStoneGenerator() {
	}

	public static ElementalStoneGenerator getInstance() {
		if (_instance == null) {
			_instance = new ElementalStoneGenerator();
		}
		return _instance;
	}

	private final L1Object _dummy = new L1Object();

	/**
	 * 지정된 위치에 돌을 둘 수 있을까를 돌려준다.
	 */
	private boolean canPut(L1Location loc) {
		_dummy.setMap(loc.getMap());
		_dummy.setX(loc.getX());
		_dummy.setY(loc.getY());

		// 가시 범위의 플레이어 체크
		if (L1World.getInstance().getVisiblePlayer(_dummy).size() > 0) {
			return false;
		}
		return true;
	}


	/**
	 * 다음의 설치 포인트를 결정한다.
	 */
	private Point nextPoint() {
		int newX = _random.nextInt(LAST_X - FIRST_X) + FIRST_X;
		int newY = _random.nextInt(LAST_Y - FIRST_Y) + FIRST_Y;

		return new Point(newX, newY);
	}


	/**
	 * 주워진 돌을 리스트로부터 삭제한다.
	 */
	private void removeItemsPickedUp() {
		L1GroundInventory gInventory  = null;
		for (int i = 0; i < _itemList.size(); i++) {
			gInventory = _itemList.get(i);
			if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
				_itemList.remove(i);
				i--;
			}
		}
	}

	/**
	 * 지정된 위치에 돌을 둔다.
	 */
	private void putElementalStone(L1Location loc) {
		L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

		L1ItemInstance item = ItemTable.getInstance().createItem(
				ELEMENTAL_STONE_ID);
		item.setEnchantLevel(0);
		item.setCount(1);
		gInventory.storeItem(item);
		_itemList.add(gInventory);
	}

	@Override
	public void run() {
		try {
			L1Map map = L1WorldMap.getInstance().getMap(
					(short) ELVEN_FOREST_MAPID);
			L1Location loc = null;

			removeItemsPickedUp();

			if (_itemList.size() < MAX_COUNT) { // 줄어들고 있는 경우 세트
				loc = new L1Location(nextPoint(), map);

				if (canPut(loc)) {
					// XXX 설치 범위내 모두에 PC가 있었을 경우 엔들레스 루프가 되지만…
					putElementalStone(loc);
				}

				// thread 최적화 관련. sleep 난무는 좀 없애자. -- Mazik
				GeneralThreadPool.getInstance().schedule(this, INTERVAL * 1000); // 일정시간마다 설치
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
