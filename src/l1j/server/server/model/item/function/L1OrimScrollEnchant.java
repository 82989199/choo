package l1j.server.server.model.item.function;

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

import l1j.server.Config;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1OrimScrollEnchant {
	private static Logger _log = Logger.getLogger(L1OrimScrollEnchant.class.getName());
	private static final String _path = "./data/xml/Item/OrimScrollEnchant.xml";
	private static HashMap<Integer, L1OrimScrollEnchant> _dataMap = new HashMap<Integer, L1OrimScrollEnchant>();

	@XmlAttribute(name = "ItemId")
	private int _antiqueBookId;

	@XmlAttribute(name = "TargetItemId")
	private String _targetItemIds;

	@XmlAttribute(name = "TargetType")
	private int _targetType;

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;

	public static L1OrimScrollEnchant get(int id) {
		return (L1OrimScrollEnchant) _dataMap.get(Integer.valueOf(id));
	}

	private int getAntiqueBookId() {
		return this._antiqueBookId;
	}

	public String getTargetItemIds() {
		return this._targetItemIds;
	}

	public int getTargetType() {
		return this._targetType;
	}

	private List<Effect> getEffects() {
		return this._effects;
	}

	private boolean init() {
		if (ItemTable.getInstance().getTemplate(getAntiqueBookId()) == null) {
			System.out.println("존재하지 않는 아이템번호입니다. " + getAntiqueBookId());
			return false;
		}
		String[] itemIdArray;
		if (getTargetItemIds() != null) {
			itemIdArray = getTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray.length; i++) {
				if (ItemTable.getInstance().getTemplate(Integer.valueOf(itemIdArray[i]).intValue()) == null) {
					System.out.println("존재하지 않는 아이템번호입니다. " + itemIdArray[i]);
					return false;
				}
			}
		}
		for (Effect each : getEffects()) {
			int totalChance = 0;
			String[] probArray = each.getProbs().split(",");
			for (int i = 0; i < probArray.length; i++) {
				totalChance += Integer.valueOf(probArray[i]).intValue();
			}
			if (totalChance != 100) {
				_log.warning("(ID:" + getAntiqueBookId() + " / Enchant:" + each.getEnchantLevel() + ") 확률 오류: 합계가 "
						+ totalChance + "%입니다. 100%로 설정해 주세요.");
			}
		}

		return true;
	}

	private static void loadXml(HashMap<Integer, L1OrimScrollEnchant> dataMap) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1OrimScrollEnchant each : list)
				if (each.init())
					dataMap.put(Integer.valueOf(each.getAntiqueBookId()), each);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "./data/xml/Item/AntiqueBook.xmlload failed.", e);
		}
	}

	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1OrimScrollEnchant> dataMap = new HashMap<Integer, L1OrimScrollEnchant>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item, L1ItemInstance target) {
		int armorId = target.getItem().getItemId();
		int armortype = target.getItem().getType();
		int safe_enchant = target.getItem().get_safeenchant();
		if (safe_enchant < 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		if (target.getItem().getType2() == 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}
		
		if (target.getItem().getType2() == 1) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		if (target.getItem().getType2() == 2) {
			if (target.getItem().getGrade() < 0) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		if (target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		if (getTargetItemIds() != null) {
			boolean check = false;
			String[] itemIdArray = getTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray.length; i++) {
				if (target.getItemId() == Integer.valueOf(itemIdArray[i]).intValue()) {
					check = true;
					break;
				}
			}
			if (check) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		int enchant_level = target.getEnchantLevel();

		if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
				|| armorId >= 222340 && armorId <= 222341) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		} else if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291
				|| armorId >= 222330 && armorId <= 222336) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		} else if (armorId >= 900020 && armorId <= 900021 || armorId >= 900049 && armorId <= 900051
				|| armorId >= 900093 && armorId <= 900099 || armorId >= 900124 && armorId <= 900126
				|| armorId >= 900127 && armorId <= 900130) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		} else if (armortype >= 8 && armortype <= 12) {
			if (!(armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
					|| armorId >= 222340 && armorId <= 222341 || armorId >= 22224 && armorId <= 22228
					|| armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336)) {
				if (enchant_level >= Config.악세사리) {
					pc.sendPackets(new S_SystemMessage("악세사리는 +" + Config.악세사리 + " 이상은 인챈할 수 없습니다."));
					pc.sendPackets(new S_SystemMessage("인챈트 제한이 걸려있습니다. 공지 확인하세요."));
					return false;
				}
			}
		}

		Effect effect = null;
		for (Effect each : getEffects()) {
			if (each.getEnchantLevel() == target.getEnchantLevel()) {
				effect = each;
				break;
			}
		}

		if (effect == null) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		String[] probArray = effect.getProbs().split(",");
		int prob = 0;
		int chance = CommonUtil.random(100);
		if (chance < prob + Integer.valueOf(probArray[0]).intValue()) {
			if (pc.isGm()) {
				pc.sendPackets("\\aH인챈트 - 레벨: " + target.getEnchantLevel() + " / 성공확률: "
						+ (prob + Integer.valueOf(probArray[0]).intValue()) + " / 랜덤: " + chance);
			}

			if (getTargetType() == 5 && item.getBless() % 128 == 0)
				pc.sendPackets(new S_ServerMessage(1310, target.getLogName()));
			else
				C_ItemUSe.SuccessEnchant(pc, target, pc.getNetConnection(), -1);

		} else if (chance < prob + Integer.valueOf(probArray[1]).intValue()) {
			pc.sendPackets(new S_ServerMessage(1310, target.getLogName()));
			if (pc.isGm()) {
				pc.sendPackets("\\aH인챈트 - 레벨: " + target.getEnchantLevel() + " / 성공확률: "
						+ (prob + Integer.valueOf(probArray[1]).intValue()) + " / 랜덤: " + chance);
			}
		} else {
			if (Integer.valueOf(probArray[2]).intValue() == 0) {
				pc.sendPackets(new S_ServerMessage(1310, target.getLogName()));
			} else {
				if (pc.isGm()) {
					pc.sendPackets("\\aH인챈트 - 레벨: " + target.getEnchantLevel() + " / 성공확률: "
							+ (prob + Integer.valueOf(probArray[2]).intValue()) + " / 랜덤: " + chance);
				}
				C_ItemUSe.SuccessEnchant(pc, target, pc.getNetConnection(), 1);
			}
		}

		if (item.getItem().getMaxChargeCount() > 0) {
			if (item.getChargeCount() > 0) {
				item.setChargeCount(item.getChargeCount() - 1);
				pc.getInventory().updateItem(item, 128);
			} else {
				pc.getInventory().removeItem(item, 1);
			}
		} else
			pc.getInventory().removeItem(item, 1);

		return true;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {

		@XmlAttribute(name = "Enchant")
		private int _enchantLevel;

		@XmlAttribute(name = "Prob")
		private String _probs;

		private int getEnchantLevel() {
			return this._enchantLevel;
		}

		public String getProbs() {
			return this._probs;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1OrimScrollEnchant> {

		@XmlElement(name = "Item")
		private List<L1OrimScrollEnchant> _list;

		public Iterator<L1OrimScrollEnchant> iterator() {
			return this._list.iterator();
		}
	}
}
