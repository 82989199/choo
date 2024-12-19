package l1j.server.server.model.item;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;


public class L1TradeItemBox {

	
	private static Logger _log =
			Logger.getLogger(L1TradeItemBox.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TradeItemList")
	private static class TradeItemList implements Iterable<L1TradeItemBox> {
		@XmlElement(name = "TradeItem")
		private List<L1TradeItemBox> _list;

		public Iterator<L1TradeItemBox> iterator() {
			return _list.iterator();
		}
	}
	
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TradeItem {
		@XmlAttribute(name = "ItemId")
		private int _itemId;
		
		@XmlAttribute(name = "Enchant")
		private int _enchant;


		public int getItemId() {
			return _itemId;
		}
		
		public void setEnchant(int enchant) {
			_enchant = enchant;
		}
		
		public int getEnchant() {
			return _enchant;
		}
	}
	
	private static final String PATH = "./data/xml/Item/TradeItemBox.xml";

	private static final HashMap<Integer, L1TradeItemBox> _dataMap =
			new HashMap<Integer, L1TradeItemBox>();
	
	private static final HashMap<String, Integer> _tempList =
			new HashMap<String, Integer>();
	
	private static final HashMap<Integer, L1ItemInstance> _ResultList =
			new HashMap<Integer, L1ItemInstance>();

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private int _type;

	private int getBoxId() {
		return _boxId;
	}

	private int getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<TradeItem> _items;
	
	private List<TradeItem> getItems() {
		return _items;
	}
	
	private CopyOnWriteArrayList<L1ItemInstance> _Iteminstance = new CopyOnWriteArrayList<L1ItemInstance>();
	private List<L1ItemInstance> getItemInstance() {
		return _Iteminstance;
	}
	
	public static L1ItemInstance getResultItem(int key) {
		return _ResultList.get(key);
	}
	
	private void init() {
		for (TradeItem each : getItems()) {			
			L1Item item = ItemTable.getInstance().getTemplate(each.getItemId());
			if (item == null) {
				getItems().remove(each);
				_log.warning("아이템 ID " + each.getItemId()
						+ " 의 템플릿이 발견되지 않았습니다.");	
			}else {
			
				L1ItemInstance _item = ItemTable.getInstance().createItem(item.getItemId());
				if(_item == null)continue;
				int enchant = each.getEnchant();
				_item.setEnchantLevel(enchant);
				
				
				_Iteminstance.add(_item);				
				_ResultList.put(_item.getId(), _item);
				_tempList.put(""+each.getItemId()+"+"+each.getEnchant(), getType());
				
			}
		}
	}
	
	public static void load() {
		
		for(L1TradeItemBox TradeItemBox: _dataMap.values()){
			TradeItemBox.getItemInstance().clear();
		}
		_ResultList.clear();
		_tempList.clear();
		_dataMap.clear();
//		System.out.println("[Server Ready] TradeItemBox  OK!");
		try {
			JAXBContext context =JAXBContext.newInstance(L1TradeItemBox.TradeItemList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TradeItemList list = (TradeItemList) um.unmarshal(file);
			
			for (L1TradeItemBox each : list) {
				each.init();
				_dataMap.put(each.getType(), each);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
			System.exit(0);
		}
	}
	
	public static List<TradeItem> GetItemList(int itemid, int enchent) {
		if(_tempList.containsKey(""+itemid+"+"+enchent) == false)return null;
		int boxid = _tempList.get(""+itemid+"+"+enchent);
		L1TradeItemBox TradeItemBox = _dataMap.get(boxid);
		if(TradeItemBox == null)return null;
		return TradeItemBox.getItems();
	}
	
	public static List<L1ItemInstance> GetItemInstanceList(int itemid, int enchent) {
		if(_tempList.containsKey(""+itemid+"+"+enchent) == false)return null;
		int boxid = _tempList.get(""+itemid+"+"+enchent);
		L1TradeItemBox TradeItemBox = _dataMap.get(boxid);
		if(TradeItemBox == null)return null;		
		return TradeItemBox.getItemInstance();
	}
	
	
	
}
