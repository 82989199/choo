package l1j.server.MJWarSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT.SIEGE_KIND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT.SIEGE_ZONE_KIND;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;

public class MJCastleWarBusiness implements TimeListener{
	private static final ServerBasePacket[] _viewMessages = new ServerBasePacket[]{
		new S_SystemMessage("공성전이 진행중입니다."),
		new S_SystemMessage("성을 소유하고 있는 혈맹은 다음과 같습니다.")
	};
	
	private static ProtoOutputStream _offInjury;
	static{
		SC_SIEGE_INJURY_TIME_NOIT noti = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
		noti.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
		noti.set_remainSecond(0);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
		noti.dispose();
		_offInjury = stream;
	}
	
	public static void move(L1PcInstance pc){
		if(pc.war_zone){
			int castleId = L1CastleLocation.getCastleIdByArea(pc);
			if(castleId == 0 || !getInstance().isNowWar(castleId)){
				pc.war_zone = false;
				SC_SIEGE_ZONE_UPDATE_NOT noti = SC_SIEGE_ZONE_UPDATE_NOT.newInstance();
				noti.set_siegeZoneKind(SIEGE_ZONE_KIND.SIEGE_ZONE_END);
				pc.sendPackets(noti, MJEProtoMessages.SC_SIEGE_ZONE_UPDATE_NOT, true);
				pc.sendPackets(_offInjury, false);
			}
		}else{
			int castleId = L1CastleLocation.getCastleIdByArea(pc);
			MJCastleWar war = getInstance().get(castleId);
			if(castleId != 0 && war.isRun()){
				pc.war_zone = true;
				SC_SIEGE_ZONE_UPDATE_NOT noti = SC_SIEGE_ZONE_UPDATE_NOT.newInstance();
				noti.set_siegeZoneKind(SIEGE_ZONE_KIND.SIEGE_ZONE_BEGIN);
				pc.sendPackets(noti, MJEProtoMessages.SC_SIEGE_ZONE_UPDATE_NOT, true);
				
				SC_SIEGE_INJURY_TIME_NOIT injury = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
				injury.set_remainSecond(war.getSpareSeconds());
				L1Clan clan = pc.getClan();
				if(MJWar.isSameWar(clan, war.getDefenseClan())){
					if(war.getOffenseClan(clan.getClanId()) != null)
						injury.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
					else
						injury.set_siegeKind(SIEGE_KIND.SIEGE_DEFFENCE);
					injury.set_pledgeName(clan.getClanName());
				}else{
					injury.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
				}				
				ProtoOutputStream stream = injury.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
				injury.dispose();
				pc.sendPackets(stream, true);
			}
		}
	}
	
	private static MJCastleWarBusiness _instance;
	public static MJCastleWarBusiness getInstance(){
		if(_instance == null)
			_instance = new MJCastleWarBusiness();
		return _instance;
	}

	private ConcurrentHashMap<Integer, MJCastleWar> _castleWars;
	private MJCastleWarBusiness(){
		_castleWars = loadedCastleWars();
	}
	
	public void reload(){
		ConcurrentHashMap<Integer, MJCastleWar> tmp = _castleWars;
		_castleWars = loadedCastleWars();
		if(tmp != null){
			for(MJCastleWar war : tmp.values())
				war.dispose();
			tmp.clear();
			tmp = null;
		}
	}
	
	public ConcurrentHashMap<Integer, MJCastleWar> loadedCastleWars(){
		ConcurrentHashMap<Integer, MJCastleWar> wars = new ConcurrentHashMap<Integer, MJCastleWar>(6);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM castle");
			rs = pstm.executeQuery();
			while(rs.next()){
				int castleId = rs.getInt("castle_id");
				L1Clan clan = ClanTable.getInstance().findCastleClan(castleId);
				MJCastleWar war = MJWarFactory.createCastleWar(clan, castleId, rs.getString("name"));
				war.nextCalendar(rs.getTimestamp("war_time"));
				war.setTaxRate(rs.getInt("tax_rate"));
				war.setPublicMoney(rs.getInt("public_money"));
				wars.put(castleId, war);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return wars;
	}
	
	public void updateCastle(int castleId){
		Connection con = null;
		PreparedStatement pstm = null;
		MJCastleWar war = _castleWars.get(castleId);
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE castle SET name=?, war_time=?, tax_rate=?, public_money=? WHERE castle_id=?");
			int idx = 0;
			
			pstm.setString(++idx, war.getCastleName());
			pstm.setTimestamp(++idx, new Timestamp(war.nextCal().getTimeInMillis()));
			pstm.setInt(++idx, war.getTaxRate());
			pstm.setInt(++idx, war.getPublicMoney());
			pstm.setInt(++idx, war.getCastleId());
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public void run(){
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		Calendar cal = time.getCalendar();
		for(MJCastleWar war : _castleWars.values()){
			if(war.isClosing()){
				continue;
			}else if(war.isRun()){
				if(war.isClosingTime(cal))
					war.close();
			}else{
				if(war.isReadyTime(cal))
					war.ready();
				else if(war.isRunTime(cal))
					war.run();
			}
		}
	}
	
	public void dispose(){
		if(_castleWars != null){
			for(MJCastleWar war : _castleWars.values())
				war.dispose();
			_castleWars.clear();
			_castleWars = null;
		}
	}
	
	public boolean isNowWar(int castleId){
		MJCastleWar war = get(castleId);
		if(war == null)
			return false;
		return war.isRun();
	}
	
	public boolean isNowReady(int castleId){
		return get(castleId).isReady();
	}
	
	public MJCastleWar get(int castleId){
		return _castleWars.get(castleId);
	}
	
	public void proclaim(L1PcInstance pc, int castleId){
		L1Clan clan = pc.getClan();
		if(clan == null){
			pc.sendPackets(272);
			return;
		}
		
		if(pc.getRedKnightClanId() != 0 || !pc.isCrown() || pc.getId() != clan.getLeaderId()){
			pc.sendPackets(478);
			return;
		}
		
		if(clan.getCastleId() != 0){
			pc.sendPackets(474);
			return;
		}
			
		if(!isNowWar(castleId)){
			pc.sendPackets("공성전이 진행중이지 않습니다.");
			return;
		}
		
		if(pc.getLevel() < Config.선포레벨){
			pc.sendPackets(String.format("공성: 군주/공주 레벨 [%d]부터 선포할 수 있습니다.", Config.선포레벨));
			return;
		}
		
		if(clan.getCurrentOnlineMemebers() < Config.혈맹접속인원){
			pc.sendPackets(String.format("공성: 접속중인 혈맹 구성원이 [%d]명 이상되어야 선포가 가능합니다.", Config.혈맹접속인원));
			return;
		}

		get(castleId).proclaim(pc);
	}
	
	public void viewNowCastleWarState(L1PcInstance pc){
		boolean isFirst = true;
		for(MJCastleWar war : _castleWars.values()){
			if(!war.isRun())
				continue;
			
			if(isFirst){
				isFirst = false;
				pc.sendPackets(_viewMessages, false);
			}
			pc.sendPackets(String.format("[%s=%s혈맹]", war.getCastleName(), war.getDefenseClan().getClanName()));				
		}
	}
	
	public int getTaxRate(int castleId){
		return get(castleId).getTaxRate();
	}
	
	public void setTaxRate(int castleId, int tax){
		get(castleId).setTaxRate(tax);
	}
	
	public int getPublicMoney(int castleId){
		return get(castleId).getPublicMoney();
	}
	
	public void setPublicMoney(int castleId, int publicMoney){
		get(castleId).setPublicMoney(publicMoney);
	}
	
	public int getSecurity(int castleId){
		return get(castleId).getCastleSecurity();
	}
	
	public void setSecurity(int castleId, int security){
		get(castleId).setCastleSecurity(security);
	}
	
	public MJCastleWar findWar(String name){
		for(MJCastleWar war : _castleWars.values()){
			if(war.getCastleName().startsWith(name))
				return war;
		}
		return null;
	}
}
