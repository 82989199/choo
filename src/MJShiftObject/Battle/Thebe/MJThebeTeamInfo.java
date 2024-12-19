package MJShiftObject.Battle.Thebe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleArgs;
import l1j.server.MJTemplate.Builder.MJMonsterSpawnBuilder;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_THEBE_CAPTURE_INFO_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_THEBE_CAPTURE_INFO_NOTI_PACKET.CapturePointT;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;

public class MJThebeTeamInfo {
	public static MJThebeTeamInfo newInstance(int team_id, MJRectangle team_rt, MJRectangle inner_team_rt, MJPoint stone_pt){
		MJThebeTeamInfo bInfo = newInstance(team_id);
		bInfo.homeserverno = team_id;
		bInfo.capture_point = 0;
		bInfo.team_point = new AtomicLong(0);
		bInfo.m_rt = team_rt;
		bInfo.m_inner_rt = inner_team_rt;
		bInfo.m_stone_pt = stone_pt;
		return bInfo;
	}
	
	public static MJThebeTeamInfo newInstance(int team_id){
		return new MJThebeTeamInfo(team_id);
	}
	
	public int team_id;
	public int homeserverno;
	public int capture_point;
	public AtomicLong team_point;
	public int stone_kill_count;
	public int stone_summon_seconds;
	public String server_description;
	private boolean is_stone_summon;
	private ConcurrentHashMap<Integer, MJThebeCharacterInfo> m_players;
	private MJRectangle m_rt;
	private MJRectangle m_inner_rt;
	private MJPoint m_stone_pt;
	private MJMonsterSpawnBuilder m_stone_builder;
	private L1MonsterInstance m_stone;
	
	private MJThebeTeamInfo(int team_id){
		this.team_id = team_id;
		m_players = new ConcurrentHashMap<Integer, MJThebeCharacterInfo>();
		stone_summon_seconds = MJShiftBattleArgs.THEBE_STONE_REGEN_SECONDS;
		is_stone_summon = false;
		m_stone_builder = new MJMonsterSpawnBuilder().setNpc(new int[]{MJShiftBattleArgs.THEBE_STONE_NPC_ID[team_id - 4]});
		stone_kill_count = 0;
		server_description = "";
	}
	
	public CapturePointT to_capture_point(){
		CapturePointT cpt = CapturePointT.newInstance(); 
		cpt.set_capture_point(capture_point);
		cpt.set_team_id(team_id);
		cpt.set_homeserverno(homeserverno);
		return cpt;
	}
	
	public void do_enter(L1PcInstance pc, int rank){
		int object_id = pc.getId();
		MJThebeCharacterInfo cInfo = m_players.get(object_id);
		if(cInfo == null){
			String source_name = MJShiftObjectManager.getInstance().get_source_character_name(pc);
			if(MJCommons.isNullOrEmpty(source_name))
				source_name = pc.getName();
			cInfo = MJThebeCharacterInfo.newInstance(pc, object_id, source_name, this);
			cInfo.battle_rank = rank;
			server_description = cInfo.home_server_name;
			m_players.put(object_id, cInfo);
		}else{
			cInfo.owner = pc;
		}
		MJPoint pt = m_rt.toRandPoint(50);
		pc.set_thebe_info(cInfo);
		pc.start_teleportForGM(pt.x, pt.y, m_rt.mapId, pc.getHeading(), 169, true, true);
	}
	
	public int[] next_position(){
		MJPoint pt = m_inner_rt.toRandPoint(50);
		return new int[]{
				pt.x, pt.y, pt.mapId
		};
	}
	
	public void do_inner_enter(L1PcInstance pc){
		MJPoint pt = m_inner_rt.toRandPoint(50);
		pc.start_teleportForGM(pt.x, pt.y, m_inner_rt.mapId, pc.getHeading(), 169, true, true);
	}
	
	public void clear(){
		m_players.clear();
		if(m_stone != null){
			m_stone.deleteMe();
			m_stone = null;
		}
	}
	
	public ArrayList<MJThebeCharacterInfo> get_characters(){
		return new ArrayList<MJThebeCharacterInfo>(m_players.values());
	}
	
	public void do_tick(){
		if(is_stone_summon)
			return;
		
		if(--stone_summon_seconds <= 0){
			is_stone_summon = true;
			m_stone = m_stone_builder.build(m_stone_pt.x, m_stone_pt.y, 1, m_stone_pt.mapId, new MJMonsterDeathHandler(){
				@Override
				public boolean onDeathNotify(L1MonsterInstance m) {
					if(m._destroyed)
						return false;
					
					m_stone = null;
					m.broadcastPacket(new S_DoActionGFX(m.getId(), 11));
					m.setDeathProcessing(true);
					m.allTargetClear();
					m.setCurrentHp(0);
					m.setDead(true);
					m.setStatus(ActionCodes.ACTION_Die);
					m.getMap().setPassable(m.getLocation(), true);
					m.deleteMe();
					
					if(++stone_kill_count >= 3)
						return true;
					
					is_stone_summon = false;
					stone_summon_seconds = MJShiftBattleArgs.THEBE_STONE_REGEN_SECONDS;
					return true;
				}
			});
		}
	}
	
	public void broadcast(ServerBasePacket pck, boolean is_clear){
		for(MJThebeCharacterInfo cInfo : m_players.values()){
			if(cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed())
				continue;
			
			cInfo.owner.sendPackets(pck, false);
		}
		if(is_clear)
			pck.clear();
	}
	
	public void broadcast(ProtoOutputStream stream, boolean is_clear){
		for(MJThebeCharacterInfo cInfo : m_players.values()){
			if(cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed())
				continue;
			
			cInfo.owner.sendPackets(stream, false);
		}
		if(is_clear)
			stream.dispose();
	}
	
	public void broadcast(MJIProtoMessage message, MJEProtoMessages messageid){
		ProtoOutputStream stream = message.writeTo(messageid);
		broadcast(stream, true);
	}
	
	public void on_update_rank(Collection<MJThebeTeamInfo> teams, ArrayList<MJThebeCharacterInfo> ranks){
		SC_THEBE_CAPTURE_INFO_NOTI_PACKET noti = SC_THEBE_CAPTURE_INFO_NOTI_PACKET.newInstance();
		for(MJThebeTeamInfo tInfo : teams)
			noti.add_points(tInfo.to_capture_point());
		noti.set_remain_time_for_next_capture_event(stone_summon_seconds);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_THEBE_CAPTURE_INFO_NOTI_PACKET);
		for(MJThebeCharacterInfo cInfo : m_players.values()){
			if(cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed())
				continue;
			
			cInfo.on_update_rank(cInfo.owner, ranks);
			cInfo.owner.sendPackets(stream, false);
		}
		stream.dispose();
	}
}
