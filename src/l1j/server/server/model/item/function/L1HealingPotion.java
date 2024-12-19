/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package l1j.server.server.model.item.function;

import java.io.File;
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

import l1j.server.Config;
import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.PerformanceTimer;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1HealingPotion {

	private static Logger _log = Logger.getLogger(L1HealingPotion.class.getName());

	private static Random _random = new Random(System.nanoTime());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1HealingPotion> {
		@XmlElement(name = "Item")
		private List<L1HealingPotion> _list;

		public Iterator<L1HealingPotion> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {
		@XmlAttribute(name = "Min")
		private int _min;

		private int getMin() {
			return _min;
		}

		@XmlAttribute(name = "Max")
		private int _max;

		private int getMax() {
			return _max;
		}

		@XmlAttribute(name = "GfxId")
		private int _gfxid;

		private int getGfxId() {
			return _gfxid;
		}

		@XmlAttribute(name = "MapId")
		private int _mapid;

		private int getMapId() {
			return _mapid;
		}
	}

	private static final String _path = "./data/xml/Item/HealingPotion.xml";

	private static HashMap<Integer, L1HealingPotion> _dataMap = new HashMap<Integer, L1HealingPotion>();

	public static L1HealingPotion get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _itemId;

	private int getItemId() {
		return _itemId;
	}

	@XmlAttribute(name = "Remove")
	private int _remove;

	private int getRemove() {
		return _remove;
	}

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;

	private List<Effect> getEffects() {
		return _effects;
	}

	private static void loadXml(HashMap<Integer, L1HealingPotion> dataMap) {
		 PerformanceTimer timer = new PerformanceTimer();
		 System.out.print("** data/xml/Item/HealingPotion.xml: ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1HealingPotion.ItemEffectList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1HealingPotion each : list) {
				if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
					System.out.print("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
				} else {
					dataMap.put(each.getItemId(), each);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, _path + "의 로드에 실패.", e);
		}
		 System.out.println("加载完成 [" + timer.get() + "ms]");
	}
	
	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1HealingPotion> dataMap = new HashMap<Integer, L1HealingPotion>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698)); // 마력에 의해 아무것도 마실 수가 없습니다.
			return false;
		}

		cancelAbsoluteBarrier(pc);

		int maxChargeCount = item.getItem().getMaxChargeCount();
		int chargeCount = item.getChargeCount();
		if (maxChargeCount > 0 && chargeCount <= 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		Effect effect = null;
		for (Effect each : getEffects()) {
			if (each.getMapId() != 0 && pc.getMapId() != each.getMapId()) {
				continue;
			}
			effect = each;
			break;
		}

		if (effect == null) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), effect.getGfxId()));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), effect.getGfxId()));
//		pc.sendPackets(new S_ServerMessage(77)); // \f1기분이 좋아졌습니다.

		int chance = effect.getMax() - effect.getMin();
		double healHp = effect.getMin();
		if (chance > 0) {
			healHp += _random.nextInt(chance) + 1;
		}

		
		healHp = MJHealingPotionDrinkChain.getInstance().do_drink(pc, healHp);
		pc.setCurrentHp(pc.getCurrentHp() + (int) healHp);
		// System.out.println("물약 회복량 : " + healHp);
		if (getRemove() > 0) {
			if (chargeCount > 0) {
				item.setChargeCount(chargeCount - getRemove());
				pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
			} else {
				pc.getInventory().removeItem(item, getRemove());
			}
		}
		return true;
	}

	private void cancelAbsoluteBarrier(L1PcInstance pc) { // 아브소르트바리아의 해제
		if (pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 43, false));
		}
	}
}
