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
package l1j.server.server.templates;

import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1Weapon extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1Weapon() {
	}

	private int _doubleDmgChance;
	@Override
	public int getDoubleDmgChance() {
		return _doubleDmgChance;
	}

	public void setDoubleDmgChance(int i) {
		_doubleDmgChance = i;
	}

	private int _magicDmgModifier = 0;

	@Override
	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void setMagicDmgModifier(int i) {
		_magicDmgModifier = i;
	}

	private int _canbedmg = 0;

	@Override
	public int get_canbedmg() {
		return _canbedmg;
	}

	public void set_canbedmg(int i) {
		_canbedmg = i;
	}

	@Override
	public boolean isTwohandedWeapon() {
		int weapon_type = getType();
		
		boolean bool = (weapon_type == 3 || weapon_type == 4
				|| weapon_type == 5 || weapon_type == 11
				|| weapon_type == 12 || weapon_type == 15
				|| weapon_type == 16 || weapon_type == 18);

		return bool;
	}
	
	// 내성 데이터
	private HashMap<Integer, Integer> m_itemResistance;	
	// 적중 데이터
	private HashMap<Integer, Integer> m_itemPierce;
	
	@Override
	public void setSpecialResistance(eKind kind, int value) {
		if(value > 0){
			if(m_itemResistance == null)
				m_itemResistance = new HashMap<Integer, Integer>(4);
			m_itemResistance.put(kind.toInt(), value);
		}
	}
	@Override
	public int getSpecialResistance(eKind kind) {
		if(m_itemResistance == null || !m_itemResistance.containsKey(kind.toInt()))
			return 0;
		
		return m_itemResistance.get(kind.toInt());
	}
	@Override
	public void setSpecialResistanceMap(HashMap<Integer, Integer> itemResistance){
		m_itemResistance = itemResistance;
	}
	@Override
	public HashMap<Integer, Integer> getSpecialResistanceMap(){
		return m_itemResistance;
	}
	@Override
	public void setSpecialPierce(eKind kind, int value) {
		if(value > 0){
			if(m_itemPierce == null)
				m_itemPierce = new HashMap<Integer, Integer>(4);
			m_itemPierce.put(kind.toInt(), value);
		}
	}
	@Override
	public int getSpecialPierce(eKind kind) {
		if(m_itemPierce == null || !m_itemPierce.containsKey(kind.toInt()))
			return 0;
		
		return  m_itemPierce.get(kind.toInt());
	}
	@Override
	public void setSpecialPierceMap(HashMap<Integer, Integer> itemPierce){
		m_itemPierce = itemPierce;
	}
	@Override
	public HashMap<Integer, Integer> getSpecialPierceMap(){
		return m_itemPierce;
	}
	@Override
	public void equipmentItem(L1PcInstance pc, boolean isEquipped) {
		int signed = isEquipped ? 1 : -1;
		if(m_itemResistance != null){
			for(Integer key : m_itemResistance.keySet())
				pc.addSpecialResistance(eKind.fromInt(key), m_itemResistance.get(key) * signed);
		}
		if(m_itemPierce != null){
			for(Integer key : m_itemPierce.keySet())
				pc.addSpecialPierce(eKind.fromInt(key), m_itemPierce.get(key) * signed);
		}
	}
	
	@Override
	public int getMagicHitup(){
		return 0;
	}
	
	@Override
	public void setMagicHitup(int i){
	}
}
