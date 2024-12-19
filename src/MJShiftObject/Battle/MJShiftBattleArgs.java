package MJShiftObject.Battle;

import l1j.server.MJTemplate.MJPropertyReader;

public class MJShiftBattleArgs {
	public static boolean THEBE_IS_AUTO_SEQUENCE_MESSAGE;
	public static int THEBE_AUTO_MESSAGE_REMAIN_SECONDS;
	public static int THEBE_TEAM_CHART_UPDATE_SECONDS;
	public static int THEBE_READY_SECONDS;
	public static long THEBE_MONSTER_KILL_POINT;
	public static long THEBE_CHARACTER_KILL_POINT;
	public static long THEBE_INNER_MOVE_TEAM_POINT;
	public static long THEBE_STONE_KILL_POINT;
	public static int THEBE_CHART_BALL_POINT;
	public static int[] THEBE_STONE_NPC_ID;
	public static int THEBE_STONE_REGEN_SECONDS;
	public static int THEBE_BOSS_NPC_ID;
	
	public static void load(){
		MJPropertyReader reader = new MJPropertyReader("./config/mj_shiftbattle.properties");
		try{
			THEBE_IS_AUTO_SEQUENCE_MESSAGE = reader.readBoolean("ThebeIsAutoSequenceMessage", "true");
			THEBE_AUTO_MESSAGE_REMAIN_SECONDS = reader.readInt("ThebeAutoMessageRemainSeconds", "30");
			THEBE_TEAM_CHART_UPDATE_SECONDS = reader.readInt("ThebeTeamChartUpdateSeconds", "5");
			THEBE_READY_SECONDS = reader.readInt("ThebeReadySeconds", "180");
			THEBE_MONSTER_KILL_POINT = reader.readLong("ThebeMonsterKillPoint", "1");
			THEBE_STONE_KILL_POINT = reader.readLong("ThebeStoneKillPoint", "100");
			THEBE_CHARACTER_KILL_POINT = reader.readLong("ThebeCharacterKillPoint", "10");
			THEBE_INNER_MOVE_TEAM_POINT = reader.readLong("ThebeInnerMoveTeamPoint", "300");
			THEBE_CHART_BALL_POINT = reader.readInt("ThebeChartBallPoint", "100");
			THEBE_STONE_NPC_ID = reader.readIntArray("ThebeStoneNpcId", "8500142,8500143,8500144", "\\,");
			THEBE_STONE_REGEN_SECONDS = reader.readInt("ThebeStoneRegenSeconds", "600");
			THEBE_BOSS_NPC_ID = reader.readInt("ThebeBossNpcId", "7310117");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				reader.dispose();
				reader = null;
			}
		}
	}
	
	public static boolean is_thebe_stone_npcid(int npc_id){
		for(int id : MJShiftBattleArgs.THEBE_STONE_NPC_ID){
			if(id == npc_id)
				return true;
		}
		return false;
	}
}
