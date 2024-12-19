package MJShiftObject.Battle.Thebe;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_POINTRANK_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_POINTRANK_NOTI_PACKET.PointRankInfoT;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJThebeCharacterInfo{
	public static MJThebeCharacterInfo newInstance(L1PcInstance pc, int destination_id, String source_name, MJThebeTeamInfo tInfo){
		MJThebeCharacterInfo bInfo = newInstance();
		bInfo.owner = pc;
		bInfo.owner_id = destination_id;
		bInfo.home_server_name = pc.get_server_description();
		bInfo.owner_name = source_name;
		bInfo.battle_point = 0;
		bInfo.battle_rank = 0;
		bInfo.team_info = tInfo;
		return bInfo;
	}
	
	public static MJThebeCharacterInfo newInstance(){
		return new MJThebeCharacterInfo();
	}
	public L1PcInstance owner;
	public String owner_name;
	public String home_server_name;
	public int owner_id;
	public long battle_point;
	public int battle_rank;
	public MJThebeTeamInfo team_info;
	
	public String to_name_pair(){
		return String.format("(%s)%s", home_server_name, owner_name);
	}
	
	public PointRankInfoT to_rank_info(){
		PointRankInfoT rInfo = PointRankInfoT.newInstance();
		try {
			rInfo.set_user_name(to_name_pair().getBytes("MS949"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		rInfo.set_user_points(battle_point);
		rInfo.set_user_rank(battle_rank);
		return rInfo;
	}
	
	public void on_update_rank(L1PcInstance pc, ArrayList<MJThebeCharacterInfo> ranks){
		SC_BASECAMP_POINTRANK_NOTI_PACKET noti = SC_BASECAMP_POINTRANK_NOTI_PACKET.newInstance();
		int size = Math.min(10, ranks.size());
		MJThebeCharacterInfo bInfo = null;
		for(int i=0; i<size; ++i){
			bInfo = ranks.get(i);
			noti.add_top_rankers(bInfo.to_rank_info());
		}
		noti.set_my_rank(to_rank_info());
		noti.set_team_points(team_info.team_point.longValue());
		pc.sendPackets(noti, MJEProtoMessages.SC_BASECAMP_POINTRANK_NOTI_PACKET, true);
	}
}
