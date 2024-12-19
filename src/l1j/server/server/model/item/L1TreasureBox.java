package l1j.server.server.model.item;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1TreasureBox {

	private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TreasureBoxList")
	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
		@XmlElement(name = "TreasureBox")
		private List<L1TreasureBox> _list;

		public Iterator<L1TreasureBox> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		@XmlAttribute(name = "Enchant")
		private int _enchant;

		@XmlAttribute(name = "Attr")
		private int _attr;

		@XmlAttribute(name = "Identi")
		private boolean _identified;
		
		@XmlAttribute(name = "Time")
		private int _time;
		
		@XmlAttribute(name = "Bless")
		private int _bless = 1;
		
		private int _chance;

		@XmlAttribute(name = "Chance")
		private void setChance(double chance) {
			_chance = (int) (chance * 10000);
		}

		public int getItemId() {
			return _itemId;
		}

		public int getCount() {
			return _count;
		}

		// 아이템 인첸트 레벨
		public int getEnchant() {
			return _enchant;
		}

		// 속성 인첸트 레벨
		public int getAttr() {
			return _attr;
		}

		// 확인 상태
		public boolean getIdentified() {
			return _identified;
		}

		public double getChance() {
			return _chance;
		}
		
		public int getTime(){
			return _time;
		}
		
		public int getBless(){
			return _bless;
		}
	}

	private static enum TYPE {
		RANDOM, SPECIFIC, RANDOM_SPECIFIC
	}

	private static final String PATH = "./data/xml/Item/TreasureBox.xml";

	private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();

	public static L1TreasureBox get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private TYPE _type;

	private int getBoxId() {
		return _boxId;
	}

	private TYPE getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<Item> _items;

	private List<Item> getItems() {
		return _items;
	}

	private int _totalChance;

	private int getTotalChance() {
		return _totalChance;
	}

	private void init() {
		for (Item each : getItems()) {
			_totalChance += each.getChance();
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				getItems().remove(each);
				_log.warning("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
				System.out.println("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
			}
		}
		if (getType() == TYPE.RANDOM && getTotalChance() != 1000000) {
			_log.warning("ID " + getBoxId() + "의 확률의 합계가 100%가 되지 않습니다.");
			System.out.println("ID " + getBoxId() + "의 확률의 합계가 100%가 되지 않습니다.");
		}
	}

	public static boolean load() {
	//	 PerformanceTimer timer = new PerformanceTimer();
	//	 System.out.print("■ 트래져박스 데이터 .......................... ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1TreasureBox.TreasureBoxList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

			for (L1TreasureBox each : list) {
				each.init();
				_dataMap.put(each.getBoxId(), each);
			}
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
			return false;
			//System.exit(0);
		}
	//	 System.out.println("■ 로딩 정상 완료 " + timer.get() + "ms");
	}

	public boolean open(L1PcInstance pc) {
		L1ItemInstance item = null;
		Random random = null;

		if (pc._ErzabeBox == true && getBoxId() == 30102) {
			Random boxrandom = new Random();
			int[] itemrnd = { 3000090, 3000096, 41148 };// 랜덤아이템
			int ran1 = boxrandom.nextInt(3);
			item = pc.getInventory().storeItem(itemrnd[ran1], 1);
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 에르자베의 알에서 " + item.getName() + " 를(을) 획득하였습니다."));
			pc._ErzabeBox = false;
		} else if (pc._SandwormBox == true && getBoxId() == 30103) {
			Random boxrandom = new Random();
			int[] itemrnd = { 3000092, 3000091, 3000095, 3000094, 210125, 5559 };// 랜덤아이템
			int ran1 = boxrandom.nextInt(6);
			item = pc.getInventory().storeItem(itemrnd[ran1], 1);
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 샌드 웜의 모래 주머니에서 " + item.getName() + " 를(을) 획득하였습니다."));
			pc._SandwormBox = false;
		} else if (getType().equals(TYPE.SPECIFIC)) {
			for (Item each : getItems()) {
				item = ItemTable.getInstance().createItem(each.getItemId());
				if (item != null && !isOpen(pc)) {
					item.setCount(each.getCount());
					item.setEnchantLevel(each.getEnchant());
					item.setAttrEnchantLevel(each.getAttr());
					item.setIdentified(each.getIdentified());
					item.setBless(each.getBless());
					if(each.getTime() > 0){
						Timestamp deleteTime = null;
						deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
						item.setEndTime(deleteTime);
					}
					storeItem(pc, item);
				}
			}

		} else if (getType().equals(TYPE.RANDOM)) {
			random = new Random(System.nanoTime());
			int chance = 0;
			int r = random.nextInt(getTotalChance());
			for (Item each : getItems()) {
				chance += each.getChance();
				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
					    item.setEnchantLevel(each.getEnchant());
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						storeItem(pc, item);
						
						// TODO 상급 방어구 랜덤 상자
//						if (getBoxId() == 4100177) {
//							if (each.getItemId() == 900155 || each.getItemId() == 900156 || each.getItemId() == 900157 || each.getItemId() == 900158 || each.getItemId() == 900159
//									|| each.getItemId() == 900160 || each.getItemId() == 900161 || each.getItemId() == 900162 || each.getItemId() == 900163) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 상급 방어구 랜덤 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 상급 방어구 랜덤 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						// TODO 산타의 행운 양말
//						if (getBoxId() == 736) {
//							if (each.getItemId() == 737) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 산타의 행운 양말에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 산타의 행운 양말에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						// TODO 신비한 마법인형 상자(특수)
//						if (getBoxId() == 4100046) {
//							if (each.getItemId() == 4100007 || each.getItemId() == 4100008
//									|| each.getItemId() == 4100009 || each.getItemId() == 4100010 || each.getItemId() == 746) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 신비한 마법인형 상자(특수)에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 신비한 마법인형 상자(특수)에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						// TODO 보스 클리어 상자
//						if (getBoxId() == 3000125) {
//							if (each.getItemId() >= 4100100 && each.getItemId() <= 4100109
//									|| each.getItemId() == 4100112 || each.getItemId() == 40222
//									|| each.getItemId() == 41148 || each.getItemId() == 5559 || each.getItemId() == 210125) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 보스 클리어 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 보스 클리어 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						// TODO 오만한 황금 상자
//						if (getBoxId() == 4100138) {
//							if (each.getItemId() == 900162 || each.getItemId() == 900163
//									|| each.getItemId() == 4100136 || each.getItemId() == 900159
//									|| each.getItemId() == 900160 || each.getItemId() == 900161
//									|| each.getItemId() == 900157 || each.getItemId() == 900158
//									|| each.getItemId() == 900168 || each.getItemId() == 900156) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 오만한 황금 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 오만한 황금 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						// TODO 변신 랜덤 상자
//						if (getBoxId() == 4100020) {
//							if (each.getItemId() == 4100023 || each.getItemId() == 4100024
//									|| each.getItemId() == 4100025 || each.getItemId() == 4100026
//									|| each.getItemId() == 4100027 || each.getItemId() == 4100028
//									|| each.getItemId() == 4100029 || each.getItemId() == 4100030
//									|| each.getItemId() == 4100031 || each.getItemId() == 4100032) {
//								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,""+pc.getName()+"님께서 변신 랜덤 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(""+pc.getName()+"님께서 변신 랜덤 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						//TODO 룬스톤
//						if (getBoxId() >= 30152 && getBoxId() <= 30155) {
//							if (each.getItemId() == 40222 || each.getItemId() == 41148 || each.getItemId() == 210125
//								|| each.getItemId() == 5559 || each.getItemId() == 210130 || each.getItemId() == 210131
//								|| each.getItemId() == 210132 || each.getItemId() == 3000094 || each.getItemId() == 40346
//								|| each.getItemId() == 40354 || each.getItemId() == 40362 || each.getItemId() == 40370) {
//							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						
						//TODO 옥토끼 복 주머니
//						if (getBoxId() == 500707) {
//							if (each.getItemId() == 3000511 || each.getItemId() == 41149
//								|| each.getItemId() == 41148 || each.getItemId() == 5559 || each.getItemId() == 40219
//								|| each.getItemId() == 3000089 || each.getItemId() == 210131
//								|| each.getItemId() == 300510 || each.getItemId() == 300502 
//								|| each.getItemId() == 300521 || each.getItemId() == 300586
//								|| each.getItemId() == 300543 || each.getItemId() == 300504) {
//							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 옥토끼 복 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 옥토끼 복 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
//							}
//							break;
//						}
						
						// 에르자베의 알
						if (getBoxId() == 30102) {
							if (each.getItemId() == 3000090 || each.getItemId() == 3000096
								|| each.getItemId() == 41148 || each.getItemId() == 20049 || each.getItemId() == 20050
								|| each.getItemId() == 222306 || each.getItemId() == 222304
								|| each.getItemId() == 20271) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 에르자베의 알에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 에르자베의 알에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 무기 상자
						if (getBoxId() == 3020001) {
							if (each.getItemId() == 12		// 바람칼날의 단검
							 || each.getItemId() == 61		// 진명황의 집행검
							 || each.getItemId() == 66		// 드래곤 슬레이어
							 || each.getItemId() == 86		// 붉은그림자의 이도류
							 || each.getItemId() == 134		// 수정결정체 지팡이
							 || each.getItemId() == 202011	// 가이아의 격노
							 || each.getItemId() == 202012	// 히페리온의 절망
							 || each.getItemId() == 202013	// 크로노스의 공포
							 || each.getItemId() == 202014	// 타이탄의 분노
							){
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 무기 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 무기 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
									}
									break;
								}
						
						// 레어 투구 상자
						if (getBoxId() == 3020002) {
							if (each.getItemId() == 20010 || each.getItemId() == 20017 || each.getItemId() == 22360) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 투구 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 투구 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 티셔츠 상자
						if (getBoxId() == 3020003) {
							if (each.getItemId() == 300578	// 실프의 티셔츠(소생)
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 티셔츠 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 티셔츠 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 갑옷 상자
						if (getBoxId() == 3020004) {
							if (each.getItemId() == 20100	// 데스나이트의 갑옷
							 || each.getItemId() == 22211	// 발라카스의 마력
							 || each.getItemId() ==22209	// 발라카스의 예지력
							 || each.getItemId() ==22208	// 발라카스의 완력
							 || each.getItemId() ==22210	// 발라카스의 인내력
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 갑옷 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 갑옷 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 망토 상자
						if (getBoxId() == 3020005) {
							if (each.getItemId() == 900017	// 고대 마물의 망토
							 || each.getItemId() == 900013	// 고대 암석 망토
							 || each.getItemId() == 20079	// 뱀파이어의 망토
							 || each.getItemId() == 20077	// 투명 망토
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 망토 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 망토 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 장갑 상자
						if (getBoxId() == 3020006) {
							if (each.getItemId() == 20166	// 데스나이트의 장갑
							 || each.getItemId() == 222317	// 격분의 장갑
							 || each.getItemId() == 900016	// 고대 마물의 장갑
							 || each.getItemId() == 900014	// 고대 암석 장갑
							 || each.getItemId() == 900156	// 머미로드의 장갑
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 장갑 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 장갑 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 부츠 상자
						if (getBoxId() == 3020007) {
							if (each.getItemId() == 20198	// 데스나이트의 부츠
							 || each.getItemId() == 900018	// 고대 마물의 부츠
							 || each.getItemId() == 900012	// 고대 암석 부츠
							 || each.getItemId() == 900168	// 머미로드의 부츠
							 || each.getItemId() == 900155	// 아이리스의 부츠
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 부츠 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 부츠 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 각반 상자
						if (getBoxId() == 3020008) {
							if (each.getItemId() == 900015	// 고대 마물의 각반
							 || each.getItemId() == 900011	// 고대 암석 각반
							 || each.getItemId() == 900159	// 나이트발드의 각반
							 || each.getItemId() == 900200	// 드래곤 슬레이어의 각반
							 || each.getItemId() == 900166	// 머미로드의 각반
							 || each.getItemId() == 900161	// 뱀파이어의 각반
							 || each.getItemId() == 900160	// 아이리스의 각반
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 각반 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 각반 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 가더/방패 상자
						if (getBoxId() == 3020009) {
							if (each.getItemId() == 900165	// 리치의 수정구
							 || each.getItemId() == 22214	// 시어의 심안
							 || each.getItemId() == 22263	// 반역자의 방패
							 || each.getItemId() == 900157	// 쿠거의 가더
							 || each.getItemId() == 900158	// 우그누스의 가더
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 가더/방패 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 가더/방패 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 견갑 상자
						if (getBoxId() == 3020010) {
							if (each.getItemId() == 900203	// 대마법사의 견갑
							 || each.getItemId() == 900202	// 사이하의 견갑
							 || each.getItemId() == 900201	// 지휘관의 견갑
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 견갑 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 견갑 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 문장/휘장 상자
						if (getBoxId() == 3020011) {
							if (each.getItemId() == 900124	// 투사의 수호 문장
							 || each.getItemId() == 900081	// 투사의 수호 휘장
							 || each.getItemId() == 900126	// 현자의 수호 문장
							 || each.getItemId() == 900083	// 현자의 수호 휘장
							 || each.getItemId() == 900125	// 명궁의 수호 문장
							 || each.getItemId() == 900082	// 명궁의 수호 휘장
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 문장/휘장 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 문장/휘장 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 스킬 상자
						if (getBoxId() == 3020012) {
							if (each.getItemId() == 40223	// 마법서 (앱솔루트 배리어)
							 || each.getItemId() == 40222	// 마법서 (디스인티그레이트)
							 || each.getItemId() == 3000089	// 마법서 (엠파이어)
							 || each.getItemId() == 41148	// 기술서 (카운터 배리어)
							 || each.getItemId() == 3000092	// 기술서 (앱솔루트 블레이드)
							 || each.getItemId() == 4100102	// 기술서 (카운터 배리어:베테랑)
							 || each.getItemId() == 41149	// 정령의 수정 (소울 오브 프레임)
							 || each.getItemId() == 41152	// 정령의 수정 (폴루트 워터)
							 || each.getItemId() == 40249	// 정령의 수정 (어스 바인드)
							 || each.getItemId() == 41153	// 정령의 수정 (스트라이커 게일)
							 || each.getItemId() == 5559	// 흑정령의 수정 (아머 브레이크)
							 || each.getItemId() == 4100105	// 흑정령의 수정 (아머 브레이크:데스티니)
							 || each.getItemId() == 4100108	// 용기사의 서판 (포우 슬레이어:브레이브)
							 || each.getItemId() == 4100107	// 용기사의 서판 (썬더 그랩:브레이브)
							 || each.getItemId() == 4100448	// 기억의 수정 (다크호스)
							 || each.getItemId() == 4100449	// 기억의 수정 (뫼비우스)
							 || each.getItemId() == 210125	// 전사의 인장 (데스페라도)
							 || each.getItemId() == 4100112	// 전사의 인장 (데스페라도:앱솔루트)
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 스킬 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 스킬 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 귀걸이 상자
						if (getBoxId() == 3020013) {
							if (each.getItemId() == 900115	// 지령의 귀걸이
							 || each.getItemId() == 900114	// 수령의 귀걸이
							 || each.getItemId() == 900113	// 풍령의 귀걸이
							 || each.getItemId() == 900112	// 화령의 귀걸이
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 귀걸이 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 귀걸이 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 반지 상자
						if (getBoxId() == 3020021) {
							if (each.getItemId() == 20288	// 순간이동 조종 반지
							 || each.getItemId() == 20281	// 변신 조종 반지
							 || each.getItemId() == 20279	// 라이아의 반지
							 || each.getItemId() == 900167	// 머미로드의 반지
							 || each.getItemId() == 900163	// 리치의 반지
							 || each.getItemId() == 900162	// 사신의 반지
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 반지 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 반지 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 목걸이 상자
						if (getBoxId() == 3020022) {
							if (each.getItemId() == 222304	// 투사의 목걸이
							 || each.getItemId() == 222306	// 현자의 목걸이
							 || each.getItemId() == 20271	// 빛나는 사이하의 목걸이
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 목걸이 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 목걸이 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 벨트 상자
						if (getBoxId() == 3020023) {
							if (each.getItemId() == 20314	// 에이션트 자이언트의 반지
							 || each.getItemId() == 900048	// 오우거 킹의 벨트
							 || each.getItemId() == 900007	// 크로노스의 벨트
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 벨트 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 벨트 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 레어 룬/유물
						if (getBoxId() == 3020024) {
							if (each.getItemId() == 900116	// 고대의 룬
							 || each.getItemId() == 910323	// 강화된 진 데스나이트의 유물(민첩)
							 || each.getItemId() == 910324	// 강화된 진 데스나이트의 유물(지식)
							 || each.getItemId() == 910322	// 강화된 진 데스나이트의 유물(완력)
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 레어 룬/유물 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 레어 룬/유물 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
						
						// 보스 한입만 상자
						if (getBoxId() == 8332) {
							if (each.getItemId() == 900200	// 드래곤 슬레이어의 각반
							 ) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 보스 한입만 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 보스 한입만 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}

						//TODO 샌드웜의 모래주머니
						if (getBoxId() == 30103) {
							if (each.getItemId() == 3000092 || each.getItemId() == 3000091
								|| each.getItemId() == 3000095 || each.getItemId() == 3000094
								|| each.getItemId() == 210125 || each.getItemId() == 5559 || each.getItemId() == 20049
								|| each.getItemId() == 20050 || each.getItemId() == 222306 || each.getItemId() == 222304
								|| each.getItemId() == 20271) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							break;
							}
							
						}
					}
					break;
				}
			}
		} else if (getType().equals(TYPE.RANDOM_SPECIFIC)) {
			random = new Random(System.nanoTime());
			int chance = 0;

			int r = random.nextInt(getTotalChance());

			for (Item each : getItems()) {
				if (each.getChance() == 0) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
						item.setEnchantLevel(each.getEnchant());
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						storeItem(pc, item);
					}
					continue;
				}
				chance += each.getChance();
				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
						item.setEnchantLevel(each.getEnchant());
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						storeItem(pc, item);
						
						// TODO 진 데스나이트의 유물 상자
						if (getBoxId() == 4100145) {
							if (each.getItemId() == 505011 || each.getItemId() == 505015 || each.getItemId() == 505012 || each.getItemId() == 620 || each.getItemId() == 625
									 || each.getItemId() == 626 || each.getItemId() == 623 || each.getItemId() == 618 || each.getItemId() == 619 || each.getItemId() == 616 
									 || each.getItemId() == 622 || each.getItemId() == 617) {
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3(" + item.getName() + ")\\f2 를(을) 획득하였습니다."));
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
							}
							break;
						}
					}
					break;
				}
			}
		}

		if (item == null) {
			return false;
		} else {
			int itemId = getBoxId();
			
			if (itemId == 40576 || itemId == 40577 || itemId == 40578 || itemId == 40411 || itemId == 49013) {
				// pc.death(null, true);
			}
			if (itemId == 3000045) { // 고대 물품:무기
				int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1,
						1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 6, 3, 3, 3, 1, 2, 3, 4, 4, 5, 1, 2, 3, 7 };
				int RandomEchant = random.nextInt(enchantrnd.length);
				item.setEnchantLevel(enchantrnd[RandomEchant]);
			}
			if (itemId >= 3000038 && itemId <= 3000044) { // 고대 물품:방어구
				int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1,
						1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 5 };
				int RandomEchant = random.nextInt(enchantrnd.length);
				item.setEnchantLevel(enchantrnd[RandomEchant]);
			}
			return true;
		}
	}

	private boolean isOpen(L1PcInstance pc) {
		int totalCount = pc.getInventory().getSize();
		if (pc.getInventory().getWeight100() >= 82 || totalCount > 165) {
			pc.sendPackets(new S_SystemMessage("인벤 확인 : 무게/수량 초과 행동이 제한됩니다."));
			return false;
		}
		if (pc.getInventory().getSize() > 170) {
			pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
			return false;
		}
		return false;
	}

	private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
		L1Inventory inventory;
		if (pc.getInventory().checkAddItem(item, item.getCount()) != L1Inventory.OK) {
			pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
			return;
		} else {
			inventory = pc.getInventory();
		}
		
		if(item.getItem().isEndedTimeMessage())
			item.setOpenEffect(0x01);
		else 
			item.setOpenEffect(0x20);
		item.setIdentified(true);// 확인상태
		//inventory.storeItem(item);
		inventory.storeItemTrea(item);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
	}
}
