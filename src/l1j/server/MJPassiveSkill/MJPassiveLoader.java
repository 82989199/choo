package l1j.server.MJPassiveSkill;

import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;

public class MJPassiveLoader{
	private static MJPassiveLoader _instance;
	public static MJPassiveLoader getInstance(){
		if(_instance == null)
			_instance = new MJPassiveLoader();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		MJPassiveLoader old = _instance;
		_instance = new MJPassiveLoader();
		if(old != null){
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, MJPassiveInfo> m_passives;
	private MJPassiveLoader(){
		m_passives = load();
	}
	
	private HashMap<Integer, MJPassiveInfo> load(){
		HashMap<Integer, MJPassiveInfo> passives = new HashMap<Integer, MJPassiveInfo>();
		Selector.exec("select * from passive_book_mapped", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					try{
						MJPassiveInfo pInfo = MJPassiveInfo.newInstance(rs);
						passives.put(pInfo.getPassiveId(), pInfo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		return passives;
	}

	public MJPassiveInfo fromPassiveId(int passiveId){
		return m_passives.get(passiveId);
	}
	
	public MJPassiveInfo fromBookId(int bookId){
		ArrayList<MJPassiveInfo> passives = values();
		if(passives == null)
			return null;
		
		for(MJPassiveInfo pInfo : passives){
			if(pInfo.getSpellBookId() == bookId)
				return pInfo;
		}
		return null;
	}
	
	public ArrayList<MJPassiveInfo> fromClassType(int type){
		ArrayList<MJPassiveInfo> passives = values();
		if(passives == null)
			return null;
		
		ArrayList<MJPassiveInfo> result = new ArrayList<MJPassiveInfo>();
		for(MJPassiveInfo pInfo : passives){
			if(pInfo.getClassType() == type)
				result.add(pInfo);
		}
		return result;
	}
	
	public ArrayList<MJPassiveInfo> values(){
		return new ArrayList<MJPassiveInfo>(m_passives.values());
	}
	
	private static final String[] CLASSE_NAMES = new String[]{
		"군주", "기사", "요정", "마법사", "다크엘프", "용기사", "환술사", "전사"	
	};
	public boolean masterPassive(L1PcInstance pc, MJPassiveInfo pInfo){
		if(pc.getPassive(pInfo.getPassiveId()) != null){
			pc.sendPackets("이미 배운 마법입니다.");
			return false;
		}
		
		if(pc.getLevel() < pInfo.getUseMinLevel()){
			pc.sendPackets(String.format("%d 레벨부터 사용 가능합니다.", pInfo.getUseMinLevel()));
			return false;
		}
		
		if(pc.getClassNumber() != pInfo.getClassType()){
			pc.sendPackets(String.format("해당 아이템은 %s만 사용할 수 있습니다.", CLASSE_NAMES[pInfo.getClassType()]));
			return false;
		}
		
		int passiveId = pInfo.getPassiveId();
		if(passiveId == MJPassiveID.DOUBLE_BREAK_DESTINY.toInt()){
			if(pc.hasSkillEffect(L1SkillId.DOUBLE_BRAKE))
				pc.removeSkillEffect(L1SkillId.DOUBLE_BRAKE);
		}

		pc.addPassive(pInfo);
		MJPassiveUserLoader.store(pc, pInfo);
		S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 231);
		pc.sendPackets(s_skillSound, false);
		Broadcaster.broadcastPacket(pc, s_skillSound);
		if(pInfo.getPassiveId() == MJPassiveID.DARK_HORSE.toInt()) {
			if(pc.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
				int sec = pc.getSkillEffectTimeSec(L1SkillId.STATUS_FRUIT);
				pc.removeSkillEffect(STATUS_FRUIT);
				pc.setSkillEffect(STATUS_FRUIT, sec * 1000);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 3, pc.getSkillEffectTimeSec(L1SkillId.STATUS_FRUIT)));
			}
		}
		else if(pInfo.getPassiveId() == MJPassiveID.RESIST_ELEMENT.toInt()) {
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_SPMR(pc));
		}
		
		return true;
	}
	
	public boolean useItem(L1PcInstance pc, L1ItemInstance item){
		MJPassiveInfo pInfo = fromBookId(item.getItemId());
		if(pInfo == null)
			return false;
		
		if(masterPassive(pc, pInfo))
			pc.getInventory().removeItem(item, 1);
		return true;
	}
	
	public void dispose(){
		if(m_passives != null){
			m_passives.clear();
			m_passives = null;
		}
	}
}
