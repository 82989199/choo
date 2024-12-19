package l1j.server.MJPassiveSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_ACTIVE_SPELL_EX_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALL_SPELL_PASSIVE_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPassiveUserLoader {
	public static void load(L1PcInstance pc){
		MJPassiveLoader loader = MJPassiveLoader.getInstance();
		Selector.exec("select * from passive_user_info where character_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					int passiveId = rs.getInt("passive_id");
					MJPassiveInfo pInfo = loader.fromPassiveId(passiveId);
					if(pInfo == null)
						continue;
					
					pc.addPassive(pInfo);
				}
			}
		});
		final ArrayList<MJPassiveInfo> passives = pc.getPassives();
		if(passives == null || passives.size() <= 0)
			return;
		
		SC_ALL_SPELL_PASSIVE_NOTI noti = SC_ALL_SPELL_PASSIVE_NOTI.newInstance();
		for(MJPassiveInfo pInfo : passives){
			int passiveId = pInfo.getPassiveId();
			if(passiveId == MJPassiveID.ARMOR_GUARD.toInt()){
				noti.add_passives(pInfo.getPassiveId(), 10);
			}else{
				noti.add_passives(pInfo.getPassiveId());
			}
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_ALL_SPELL_PASSIVE_NOTI, true);
	}
	
	public static void store(L1PcInstance pc, MJPassiveInfo pInfo){
		final int characterId = pc.getId();
		final int passvieId = pInfo.getPassiveId();
		final String passiveName = pInfo.getPassiveName();
		Updator.exec("insert into passive_user_info set character_id=?, passive_id=?, passive_name=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, passvieId);
				pstm.setString(++idx, passiveName);
			}
		});
		if(passvieId == MJPassiveID.ARMOR_GUARD.toInt())
			SC_ADD_ACTIVE_SPELL_EX_NOTI.send(pc, pInfo.getPassiveId(), 10);
		else
			SC_ADD_ACTIVE_SPELL_EX_NOTI.send(pc, pInfo.getPassiveId(), 0);
	}
	
	public static void store(L1PcInstance pc){
		final ArrayList<MJPassiveInfo> passives = pc.getPassives();
		if(passives == null || passives.size() <= 0)
			return;
		
		final int characterId = pc.getId();
		Updator.batch("insert ignore into passive_user_info set character_id=?, passive_id=?, passive_name=?", new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				MJPassiveInfo pInfo = passives.get(callNumber);
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, pInfo.getPassiveId());
				pstm.setString(++idx, pInfo.getPassiveName());
			}
		}, passives.size());
	}
}
