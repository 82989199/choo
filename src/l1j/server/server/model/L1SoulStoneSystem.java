package l1j.server.server.model;

import java.util.HashMap;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1SoulStoneSystem {
	private static L1SoulStoneSystem 	_instance;
	private static final Random 			_rnd = new Random(System.nanoTime());
	public static L1SoulStoneSystem getInstance(){
		if(_instance == null)
			_instance = new L1SoulStoneSystem();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		L1SoulStoneSystem tmp = _instance;
		_instance = new L1SoulStoneSystem();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
//	private HashMap<Integer, int[]> _soulsInfo;
	/*private L1SoulStoneSystem(){
		_soulsInfo = new HashMap<Integer, int[]>();
		int[] array = null;*/
		/** lastabard **/
		/*array = new int[]{ 80453, 80454, 80455, 80456, 80457, 80458, 80459, 80460, 80461, 80462, 80463,80452 };
		_soulsInfo.put(479, array);
		_soulsInfo.put(475, array);
		_soulsInfo.put(462, array);
		_soulsInfo.put(453, array);
		_soulsInfo.put(492, array);
		*/
		/*array = new int[]{ 80450, 80451, 80466, 80467 }; // ivory tower
		_soulsInfo.put(280, array);
		_soulsInfo.put(281, array);
		_soulsInfo.put(282, array);
		_soulsInfo.put(283, array);
		_soulsInfo.put(284, array);
		_soulsInfo.put(285, array);
		_soulsInfo.put(286, array);
		_soulsInfo.put(287, array);
		_soulsInfo.put(288, array);
		_soulsInfo.put(289, array);
		
		array = new int[]{ 80464, 80465 }; // gludio
		_soulsInfo.put(807, array);
		_soulsInfo.put(808, array);
		_soulsInfo.put(809, array);
		_soulsInfo.put(810, array);
		_soulsInfo.put(811, array);
		_soulsInfo.put(812, array);
		_soulsInfo.put(813, array);
		
		array = new int[]{ 80468, 80469, 80470, 80471, 80472, 80473, 80474, 80475, 80476, 80477 };	// oman
		//TODO 오만의 탑 (일반)
		_soulsInfo.put(101, array);
		_soulsInfo.put(102, array);
		_soulsInfo.put(103, array);
		_soulsInfo.put(104, array);
		_soulsInfo.put(105, array);
		_soulsInfo.put(106, array);
		_soulsInfo.put(107, array);
		_soulsInfo.put(108, array);
		_soulsInfo.put(109, array);
		_soulsInfo.put(110, array);
		_soulsInfo.put(111, array);
		//TODO 오만의 탑 (PC)
		_soulsInfo.put(121, array);
		_soulsInfo.put(122, array);
		_soulsInfo.put(123, array);
		_soulsInfo.put(124, array);
		_soulsInfo.put(125, array);
		_soulsInfo.put(126, array);
		_soulsInfo.put(127, array);
		_soulsInfo.put(128, array);
		_soulsInfo.put(129, array);
		_soulsInfo.put(130, array);
		_soulsInfo.put(131, array);*/
//	}
	
	/*public void drop(L1Character c){
		int[] array = _soulsInfo.get(c.getMapId());
		
		if(array == null)
			return;
		
		if(c instanceof L1PcInstance){
			toDrop((L1PcInstance)c, array);
		}else if(c instanceof L1PetInstance){
			L1PetInstance pet = (L1PetInstance)c;
			L1PcInstance pc = pet.getMaster();
			if(pc != null)
				toDrop(pc, array);
		}
	}*/
	
/*	private void toDrop(L1PcInstance pc, int[] array){
		if(_rnd.nextInt(100) <= Config.BossGht_CHANCE){
			pc.getInventory().storeItem(array[_rnd.nextInt(array.length)], 1);
			pc.sendPackets(new S_SystemMessage("보스의 영혼석을 획득하였습니다."));
		}
	}*/
	
	public void clear(){
		
	}
	
}
