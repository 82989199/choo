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

package l1j.server.server.model;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.AuctionBoardTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.utils.SQLUtil;

public class L1ClanJoin {

	private static Logger _log = Logger.getLogger(L1ClanJoin.class.getName());
	private static L1ClanJoin _instance;
	public static L1ClanJoin getInstance() {
		if (_instance == null) { _instance = new L1ClanJoin(); }
		return _instance;
	}
	private L1ClanJoin() { }
	
	public void tutorialJoin(L1Clan clan, L1PcInstance pc){
		S_ServerMessage msg = new S_ServerMessage(94, pc.getName());
		for (L1PcInstance clanMembers : clan.getOnlineClanMember()) 
			clanMembers.sendPackets(msg, false);
		msg.clear();
		
		pc.setClanid(clan.getClanId());
		pc.setClanname(clan.getClanName());
		pc.setClanRank(L1Clan.수련);
		pc.setClanMemberNotes("");					
		pc.setTitle(Config.GAME_SERVER_NAME);
		pc.sendPackets(new S_SystemMessage("신규보호혈은 PK에 대미지 제한을 받습니다."));
		S_CharTitle title = new S_CharTitle(pc.getId(), pc.getTitle());
		pc.sendPackets(title, false);
		Broadcaster.broadcastPacket(pc, title);
		
		
		try{
			pc.save(); // DB에 캐릭터 정보를 기입한다
		}catch(Exception e){
			e.printStackTrace();
		}
		
		clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
		pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.수련, pc.getName()));
		pc.sendPackets(new S_ServerMessage(95, pc.getClanname())); // \f1%0z			
		pc.sendPackets(new S_ClanName(pc, clan.getEmblemId(), pc.getClanRank()));	
		pc.sendPackets(new S_ReturnedStat(pc.getId(), clan.getClanId()));
		pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, clan.getEmblemStatus()));
		pc.sendPackets(new S_ACTION_UI(clan.getClanName(), pc.getClanRank()));
		for(L1PcInstance player : clan.getOnlineClanMember()){
			player.sendPackets(new S_ReturnedStat(pc.getId(), pc.getClan().getEmblemId()));
			player.broadcastPacket(new S_ReturnedStat(player.getId(), pc.getClan().getEmblemId()));
		}
	}
	
	private boolean move_clan(L1PcInstance join_pc, L1Clan move_clan) throws Exception{
	      L1Clan clan = join_pc.getClan();
	      if(clan == null)
	         return true;

	      if(clan.getLeaderId() != join_pc.getId()){
	         join_pc.sendPackets("혈맹의 군주만 혈 이동이 가능합니다.");
	         return false;
	      }
	      remove_clan_house(join_pc, clan);
	      delete_clan(join_pc, clan, move_clan);
	      return true;
	   }
	
	private void delete_clan(L1PcInstance leader, L1Clan clan, L1Clan move_clan) throws Exception{
		L1PcInstance pc = null;
		int clan_id = clan.getClanId();
		String leader_name = leader.getName();
		String clan_name = clan.getClanName();
		S_ServerMessage msg = new S_ServerMessage(269, leader_name, clan_name);
		for (int i = 0; i < clan.getClanMemberList().size(); i++) { 
			// 혈맹원들의혈맹정보를초기화
			pc = L1World.getInstance().getPlayer(clan.getClanMemberList().get(i).name);
			if (pc == null) {
				// 혈맹원이 오프라인인 경우
				pc = CharacterTable.getInstance().restoreCharacter(clan.getClanMemberList().get(i).name);
				pc.movePlayerClanData(move_clan);
				
			} else {
				// %1혈맹의 군주 %0가 혈맹을 해산시켰습니다.
				pc.sendPackets(msg, false);
				pc.ClearPlayerClanData(clan);
				ClanJoin(move_clan, pc);
			}
		}
		msg.clear();
		String emblem_file = String.valueOf(clan_id);
		File file = new File("emblem/" + emblem_file);
		file.delete();
		ClanTable.getInstance().deleteClan(clan_name);
	}
	
	private void remove_clan_house(L1PcInstance leader, L1Clan clan){
		int clan_house_id = clan.getHouseId();
		if(clan_house_id <= 0)
			return;
		
		AuctionBoardTable boardTable = new AuctionBoardTable();
		L1AuctionBoard board = new L1AuctionBoard();
		L1House house = HouseTable.getInstance().getHouseTable(clan_house_id);
		if(house == null || clan_house_id != house.getHouseId())
			return;
		
		board.setHouseId(clan_house_id);
		board.setHouseName(house.getHouseName());
		board.setHouseArea(house.getHouseArea());
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		cal.add(Calendar.DATE, 1); // 경매장 갱신 1일 후
		cal.set(Calendar.MINUTE, 0); // 분 , 초는 잘라서 버림
		cal.set(Calendar.SECOND, 0);
		board.setDeadline(cal);
		board.setPrice(1000000);
		board.setLocation(house.getLocation());
		board.setOldOwner(leader.getName());
		board.setOldOwnerId(leader.getId());
		board.setBidder("");
		board.setBidderId(0);
		boardTable.insertAuctionBoard(board);
		house.setOnSale(true);
		house.setPurchaseBasement(true);
		HouseTable.getInstance().updateHouse(house);
	}
	
	/** 2016.11.25 MJ 앱센터 혈맹 
	 * @throws Exception **/
	public boolean ClanJoin(L1Clan clan, L1PcInstance joinPc) throws Exception{
		int clan_id 		= clan.getClanId();
		String clanName 	= clan.getClanName();
		LeaderInfo info 	= this.getOfflineClanLeaderInfo(clan.getLeaderId());	
		if(info == null)
			return false;
		
		int maxMember  		= info.is45Quest?3:1;
		if(info.lvl >= 50)	maxMember *= 3;
		else				maxMember *= 2;
		if (Config.MAX_CLAN_MEMBER > 0) // Clan 인원수의 상한의 설정  있어
			maxMember 		= Config.MAX_CLAN_MEMBER;

		if (maxMember <= clan.getClanMemberList().size()) {
			joinPc.sendPackets(new S_ServerMessage(188, info.name)); 
			return false;
		}
		
		if (!move_clan(joinPc, clan))
			return false;

		move_clan(joinPc, clan);
		for (L1PcInstance clanMembers : clan.getOnlineClanMember()) 
			clanMembers.sendPackets(new S_ServerMessage(94,joinPc.getName()));
		
		joinPc.setClanid(clan_id);
		joinPc.setClanname(clanName);
		joinPc.setClanRank(L1Clan.수련);
		joinPc.setClanMemberNotes("");					
		joinPc.setTitle("");
		joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
		Broadcaster.broadcastPacket(joinPc, new S_CharTitle(joinPc.getId(), ""));
		try{
			joinPc.save(); // DB에 캐릭터 정보를 기입한다
		} catch(Exception e) {}
		clan.addClanMember(joinPc.getName(), joinPc.getClanRank(), joinPc.getLevel(), "", joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), joinPc);
		joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.수련, joinPc.getName()));
		joinPc.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0z			
		joinPc.sendPackets(new S_ClanName(joinPc, clan.getEmblemId(), joinPc.getClanRank()));	
		joinPc.sendPackets(new S_ReturnedStat(joinPc.getId(), clan.getClanId()));
		joinPc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, clan.getEmblemStatus()));
		joinPc.sendPackets(new S_ACTION_UI(clan.getClanName(), joinPc.getClanRank()));
		for(L1PcInstance player : clan.getOnlineClanMember()){
			player.sendPackets(new S_ReturnedStat(joinPc.getId(), joinPc.getClan().getEmblemId()));
			player.broadcastPacket(new S_ReturnedStat(player.getId(), joinPc.getClan().getEmblemId()));
		}
		return true;
	}
	
	public boolean ClanJoin(L1PcInstance pc, L1PcInstance joinPc){
		int clan_id = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clan_id);
		if (clan != null) {
			int maxMember = 0;
			///////////혈맹리뉴얼//////////////
			int charisma = 0;
			if(pc.getId() != clan.getLeaderId())
				charisma = pc.getAbility().getTotalCha();
			else
				charisma = getOfflineClanLeaderCha(clan.getLeaderId());
			///////////혈맹리뉴얼//////////////
			boolean lv45quest = false;
			if (pc.getQuest().isEnd(L1Quest.QUEST_LEVEL45)) {
				lv45quest = true;
			}
			if (pc.getLevel() >= 50) { // Lv50 이상
				if (lv45quest == true) { // Lv45 퀘스트 클리어가 끝난 상태
					maxMember = charisma * 9;
				} else {
					maxMember = charisma * 3;
				}
			} else { // Lv50 미만
				if (lv45quest == true) { // Lv45 퀘스트 클리어가 끝난 상태
					maxMember = charisma * 6;
				} else {
					maxMember = charisma * 2;
				}
			}
			if (Config.MAX_CLAN_MEMBER > 0) { // Clan 인원수의 상한의 설정  있어
				maxMember = Config.MAX_CLAN_MEMBER;
			}

			if (joinPc.getClanid() == 0) { // 크란미가입
				if (maxMember <= clan.getClanMemberList().size()) {
					joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); 
					return false;
				}
				for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
					clanMembers.sendPackets(new S_ServerMessage(94,joinPc.getName()));
				}

				joinPc.setClanid(clan_id);
				joinPc.setClanname(clanName);
				joinPc.setClanRank(L1Clan.수련);
				joinPc.setClanMemberNotes("");					
				joinPc.setTitle("");
				joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
				Broadcaster.broadcastPacket(joinPc, new S_CharTitle(joinPc.getId(), ""));
				try{
					joinPc.save(); // DB에 캐릭터 정보를 기입한다
				} catch(Exception e) {}
				clan.addClanMember(joinPc.getName(), joinPc.getClanRank(), joinPc.getLevel(), "", joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), joinPc);
				joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.수련, joinPc.getName()));
				joinPc.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0z				
				joinPc.sendPackets(new S_ClanName(joinPc, clan.getEmblemId(), joinPc.getClanRank()));	
				joinPc.sendPackets(new S_ReturnedStat(joinPc.getId(), clan.getClanId()));
				joinPc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus())); // TODO
				//joinPc.sendPackets(new S_ClanAttention());
				joinPc.sendPackets(new S_ACTION_UI(clan.getClanName(), joinPc.getClanRank()));
				for(L1PcInstance player : clan.getOnlineClanMember()){
					player.sendPackets(new S_ReturnedStat(joinPc.getId(), joinPc.getClan().getEmblemId()));
					player.broadcastPacket(new S_ReturnedStat(player.getId(), joinPc.getClan().getEmblemId()));
				}
				if (joinPc.getClanid() != 0) {
					L1QuestInfo info = joinPc.getQuestList(271);
					if (info != null && info.end_time == 0) {
						info.ck[0] = joinPc.getClanid();
						if (info.ck[0] > 1) {
							info.ck[0] = 1;
						}
						joinPc.sendPackets(new S_QuestTalkIsland(joinPc, 271, info));
					}
				}
				//L1Teleport.teleport(joinPc, joinPc.getX(), joinPc.getY(), joinPc.getMapId(),joinPc.getHeading(), false);
				// 혈맹에
				// 가입했습니다.
			} else { // 크란 가입이 끝난 상태(크란 연합)
				if (Config.CLAN_ALLIANCE) {
					changeClan(pc, joinPc, maxMember);
				} else {
					joinPc.sendPackets(new S_ServerMessage(89)); 
					// \f1당신은벌써혈맹에가입하고있습니다.
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private void changeClan(L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
		int clanId = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanId);
		int clanNum = clan.getClanMemberList().size();

		int oldClanId = joinPc.getClanid();
		String oldClanName = joinPc.getClanname();
		L1Clan oldClan = L1World.getInstance().getClan(oldClanId);
		int oldClanNum = oldClan.getClanMemberList().size();
		if (clan != null && oldClan != null && joinPc.isCrown()
				&& joinPc.getId() == oldClan.getLeaderId()) {
			if (maxMember < clanNum + oldClanNum) { // 빈 곳이 없다
				joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); 
				// %0는당신을혈맹원으로서받아들일수가없습니다.
				return;
			}
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for (int cnt = 0; cnt < clanMember.length; cnt++) {
				clanMember[cnt].sendPackets(new S_ServerMessage(94, joinPc.getName())); 
				// \f1%0이혈맹의일원으로서받아들여졌습니다.
			}
			pc.setClanRank(L1Clan.수련);
			pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.수련, pc.getName())); 
			try {
				pc.save();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < oldClan.getClanMemberList().size(); i++) {
				L1PcInstance oldClanMember = L1World.getInstance().getPlayer(
						oldClan.getClanMemberList().get(i).name);
				if (oldClanMember != null) { // 온라인중의 구크란 멤버
					oldClanMember.setClanid(clanId);
					oldClanMember.setClanname(clanName);
					oldClanMember.setClanRank(L1Clan.수련);
					try {
						// DB에 캐릭터 정보를 기입한다
						oldClanMember.save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
					}
					clan.addClanMember(oldClanMember.getName(), oldClanMember.getClanRank(), oldClanMember.getLevel(),
							oldClanMember.getClanMemberNotes(), oldClanMember.getId(), oldClanMember.getType(), oldClanMember.getOnlineStatus(), oldClanMember);

					oldClanMember.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, L1Clan.수련, oldClanMember.getName()));
					oldClanMember.sendPackets(new S_ServerMessage(95, clanName));
					oldClanMember.sendPackets(new S_ClanName(oldClanMember, clan.getEmblemId(), oldClanMember.getClanRank()));	
					oldClanMember.sendPackets(new S_ReturnedStat(oldClanMember.getId(), clan.getClanId()));
					oldClanMember.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
					//oldClanMember.sendPackets(new S_ClanAttention());
					for(L1PcInstance player : clan.getOnlineClanMember()){
						player.sendPackets(new S_ReturnedStat(oldClanMember.getId(), oldClanMember.getClan().getEmblemId()));
						player.broadcastPacket(new S_ReturnedStat(player.getId(), oldClanMember.getClan().getEmblemId()));
					}
					
							 // \f1%0
					
					// 혈맹에
					// 가입했습니다.
				} else { // 오프 라인중의 구크란 멤버
					try {
						L1PcInstance offClanMember = CharacterTable.getInstance().restoreCharacter(oldClan.getClanMemberList().get(i).name);
						offClanMember.setClanid(clanId);
						offClanMember.setClanname(clanName);
						offClanMember.setClanRank(L1Clan.수련);
						offClanMember.save();
						clan.addClanMember(offClanMember.getName(), offClanMember.getClanRank(), offClanMember.getLevel(), 
								offClanMember.getClanMemberNotes(), offClanMember.getId(), offClanMember.getType(), offClanMember.getOnlineStatus(), offClanMember);
					} catch (Exception e) {
						_log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
					}
				}
			}
			// 이전혈맹 삭제
			String emblem_file = String.valueOf(oldClanId);
			File file = new File("emblem/" + emblem_file);
			file.delete();
			ClanTable.getInstance().deleteClan(oldClanName);
		}
	}
	// 오프라인중의 군주 카리스마
	///////////혈맹리뉴얼//////////////
	public int getOfflineClanLeaderCha(int member) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Cha FROM characters WHERE objid=?");
			pstm.setInt(1, member);
			rs = pstm.executeQuery();
			if(!rs.next()) return 0;

			return rs.getInt("Cha");

		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
			System.out.println("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return 0;
	}
	
	/** 2016.11.25 MJ 앱센터 혈맹 **/
	public LeaderInfo getOfflineClanLeaderInfo(int objid) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT char_name, level, Cha FROM characters where objid=?");
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if(!rs.next())
				return null;
			
			LeaderInfo info = new LeaderInfo();
			info.name		= rs.getString("char_name");
			info.cha 		= rs.getInt("Cha");
			info.lvl 		= rs.getInt("level");
			
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			pstm = con.prepareStatement("SELECT * FROM character_quests where char_id=? and quest_id=?");
			pstm.setInt(1, objid);
			pstm.setInt(2, L1Quest.QUEST_LEVEL45);
			rs = pstm.executeQuery();
			if(!rs.next())	info.is45Quest 	= false;
			else			info.is45Quest	= rs.getInt("quest_step") == 0xff;
			return info;
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
			System.out.println("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return null;
	}
	
	class LeaderInfo{
		public String 	name;
		public int 		lvl;
		public int 		cha;
		public boolean 	is45Quest;
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder(128);
			sb.append("NAME : ").append(name).append("\n");
			sb.append("LEVEL : ").append(lvl).append("\n");
			sb.append("CHARISMA : ").append(cha).append("\n");
			sb.append("QUESTSTATE : ").append(is45Quest).append("\n");
			return sb.toString();
		}
	}
	/** 2016.11.25 MJ 앱센터 혈맹 **/
}
