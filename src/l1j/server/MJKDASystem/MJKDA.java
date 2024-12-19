package l1j.server.MJKDASystem;

/**********************************
 * 
 * MJ Kill Death Assist Object.
 * made by mjsoft, 2017.
 *  
 **********************************/
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJTemplate.Chain.KillChain.MJCharacterKillChain;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.server.GMCommands;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ShowCmd;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJBytesOutputStream;

public class MJKDA {
	private static final int KDAM_INIT 			= 0;
	private static final int KDAM_WP_INC 		= 1;
	
	private static ServerBasePacket[] _kdaMessages = new ServerBasePacket[]{
		new S_SystemMessage("당신의 킬데스가 초기화 되었습니다."),
	//	new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "PK에서 승리하여 혈맹 포인트가 1점이 상승 했습니다."),
		new S_SystemMessage("현재 보호모드로 인해, 페널티가 적용 되지 않습니다. 안심하시고 사냥하셔도 됩니다."),
	};
	
	public int 		objid;
	public String 	name;
	public int 		kill;
	public int 		death;
	public long		lastDeathMs;
	public long		lastKillMs;
	public int		lastKillId;
	public boolean	isBot;
	public boolean	isChartView;
	
	public void onInit(L1PcInstance owner){
		kill 	= 0;
		death 	= 0;
		owner.sendPackets(_kdaMessages[KDAM_INIT], false);
	}
	
	public void onProtection(L1PcInstance pc){
		pc.sendPackets(_kdaMessages[KDAM_INIT], false);
	}
	
	public void onKill(L1PcInstance killer, L1PcInstance victim){
		if(killer.getZoneType() != 0)
			return;
		
		if(GMCommands.IS_PROTECTION){
			onProtection(victim);
			return;
		}
		
		MJBotAI ai = killer.getAI();
		if(ai == null){
			if(killer.getClanid() != 0){
				L1Clan clan = killer.getClan();
				if(clan != null){
					clan.incWarPoint();
					killer.sendPackets(_kdaMessages[KDAM_WP_INC], false);
				}
			}
		}else{
			if(ai.getBotType() == MJBotType.REDKNIGHT || ai.getBotType() == MJBotType.PROTECTOR)
				return;
		}

		killer.sendPackets(new S_PacketBox(S_PacketBox.배틀샷, victim.getId()));
		MJKDA tKda = victim.getKDA();
		MJKDA oKda = killer.getKDA();
		if(oKda == null || tKda == null)
			return;

		MJCharacterKillChain.getInstance().on_kill(killer, victim);
		long cur = System.currentTimeMillis();
		try{
			long diff = cur - tKda.lastDeathMs;
			if(diff < MJKDALoadManager.KDA_DEATH_DELAY_MS){
//				killer.sendPackets(new S_SystemMessage(String.format("[%s] 케릭터는 짧은 시간에 너무 많이 사망하여 포인트를 지급하지 않습니다.", victim.getName())));
				return;
			}else if(tKda.objid == oKda.lastKillId && diff < MJKDALoadManager.KDA_KILL_DUPL_DELAY_MS){
//				killer.sendPackets(new S_SystemMessage(String.format("[%s] 케릭터를 짧은 시간에 너무 많이 죽여 포인트를 지급하지 않습니다.", victim.getName())));
				return;
			}

			PrideLoadManager.getInstance().on_kill(victim);
			tKda.death++;
			oKda.kill++;
			MJKDALoadManager.KDA_TOTAL_PVP++;
			if(killer.getMapId() == 170811 || killer.getMapId() == 170911){
				notifyKillForGotten(killer, victim);
				setLawful(killer, victim);
			}else{
				onProvideHuntPrice(killer, victim);
				notifyKill(killer, victim);
				if(!victim.isInWarAreaAndWarTime(victim, killer))
					setLawful(killer, victim);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			tKda.lastDeathMs 	= cur;
			oKda.lastKillMs		= cur;
			oKda.lastKillId 	= tKda.objid;
		}
	}
	
	//TODO 수배 시스템 아데나 돌려주는부분
	private void onProvideHuntPrice(L1PcInstance killer, L1PcInstance victim){
		if(victim.hasSkillEffect(L1SkillId.USER_WANTED)){
//			killer.getInventory().storeItem(L1ItemId.ADENA, 10000000); // 아데나
			killer.getInventory().storeItem(L1ItemId.DUNK_COIN, 1000); // 아데나
			victim.removeSkillEffect(L1SkillId.USER_WANTED);
			victim.doWanted(false);
		}
	}
	
	public void notifyKill(L1PcInstance killer, L1PcInstance victim){
		ServerBasePacket[] pcks = new ServerBasePacket[]{//\\da
			new S_ChatPacket(String.format("\\aG%s\\aA님이\\aA \\aG%s\\aA님과의 전투에서 승리했습니다.", killer.getName(), victim.getName()), Opcodes.S_MESSAGE),	
			new S_ChatPacket(String.format("\\aA위치: \\aG%s", MapsTable.getInstance().getMapName(killer.getMapId())), Opcodes.S_MESSAGE),
		};
		L1World.getInstance().broadcastPacketToAll(pcks, true);
	}
	
	public void notifyKillForGotten(L1PcInstance killer, L1PcInstance victim){
		int cid = victim.getClanid();
		if(cid == 0){
			ServerBasePacket nonEqualsPck	= S_ShowCmd.getPkMessageAtBattleServer(killer.getName(), victim.getName());
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)	
					continue;
				pc.sendPackets(nonEqualsPck, false);
			}	
			return;
		}
		ServerBasePacket equalsPck		= S_ShowCmd.getPkMessageAtBattleServer(String.format("\\aG%s", killer.getName()), victim.getName());
		ServerBasePacket nonEqualsPck	= S_ShowCmd.getPkMessageAtBattleServer(killer.getName(), victim.getName());
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null)	
				continue;
			if (pc.getClanid() == cid) 
				pc.sendPackets(equalsPck, false);
			else 
				pc.sendPackets(nonEqualsPck, false);
		}
	}
	
	private void setLawful(L1PcInstance killer, L1PcInstance victim){
		int kLaw = killer.getLawful();
		int vLaw = victim.getLawful(); 
		if(vLaw >= 0 && !victim.isPinkName()){
			if(kLaw < 30000){
				killer.set_PKcount(killer.get_PKcount() + 1);
				killer.setLastPk();
			}
			
			int nLaw = calculate_lawful(killer);
			killer.setLawful(nLaw);
			killer.send_lawful();
		}else
			victim.setPinkName(false);
	}
	
	public static int calculate_lawful(L1PcInstance killer){
		int nLaw = 0;
		if(killer.getLevel() < 50)
			nLaw = -1 * (int) ((Math.pow(killer.getLevel(), 2) * 4));
		else
			nLaw = -1 * (int) ((Math.pow(killer.getLevel(), 3) * 0.08));
		
		return Math.max(nLaw, -32768);
	}
	
	public byte[] serialize() throws Exception{
		MJBytesOutputStream bos = new MJBytesOutputStream(32);
		bos.write(0x0A);
		bos.writeS2(name);
		bos.write(0x10);
		bos.writeBit(kill);
		byte[] data = bos.toArray();
		bos.close();
		bos.dispose();
		bos = null;
		return data;
	}
}
