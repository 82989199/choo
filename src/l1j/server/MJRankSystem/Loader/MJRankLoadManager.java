package l1j.server.MJRankSystem.Loader;

import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJRankLoadManager implements MJCommand{
	private static MJRankLoadManager _instance;
	public static MJRankLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJRankLoadManager();
		return _instance;
	}
	
	public static int MRK_SYS_UPDATE_CLOCK;
	public static boolean MRK_SYS_ISON;
	public static int MRK_SYS_MINLEVEL;
	public static int MRK_SYS_TOTAL_RANGE;
	public static int MRK_SYS_CLASS_RANGE;
	public static int MRK_SYS_RANK_POTION;
	public static int MRK_TOPPROTECTION_ID;
	private static int MRK_LOADING_COUNT;
	private MJRankLoadManager(){
	}
	
	public void load(){
		loadConfig();
		MJRankUserLoader.getInstance();
		if(MRK_SYS_ISON)
			MJRankBusiness.getInstance().run();
	}
	
	public void loadConfig(){
		MJPropertyReader reader = null;
		try{
			reader 					= new MJPropertyReader("./config/mj_rank.properties");		
			MRK_SYS_UPDATE_CLOCK	= reader.readInt("UpdateClock", "60");
			if(MRK_LOADING_COUNT++ == 0)
				MRK_SYS_ISON		= reader.readBoolean("isStartupRankSystem", "true");
			MRK_SYS_MINLEVEL		= reader.readInt("InRankMinLevel", "60");
			MRK_SYS_TOTAL_RANGE		= reader.readInt("TotalRankRange", "200");
			MRK_SYS_CLASS_RANGE		= reader.readInt("ClassRankRange", "200");
			MRK_SYS_RANK_POTION		= reader.readInt("rankingPotionLevel", "30");
			MRK_TOPPROTECTION_ID	= reader.readInt("TopProtectionItemId", "5558");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.dispose();
		}
	}

	@Override
	public void execute(MJCommandArgs args) {
		try{
			switch(args.nextInt()){
			case 1:
				toggleCommand(args);
				break;
			case 2:
				reloadCommand(args);
				break;
			case 3:
				settingCommand(args);
				break;
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".랭킹시스템");
			args.notify("[1.토글][2.리로드][3.세팅]");
		}finally{
			args.dispose();
		}
	}
	
	private void toggleCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:
				if(MRK_SYS_ISON)
					args.notify("이미 활성화 중입니다.");
				else{
					MRK_SYS_ISON = true;
					MJRankUserLoader.reload();
					MJRankBusiness.getInstance().run();
					Thread.sleep(3000L);
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null || pc.getAI() != null)
							continue;
						
						MJRankUserLoader.getInstance().onUser(pc);
					}
					args.notify("시스템을 활성화합니다.");
				}
				break;
				
			case 2:
				if(!MRK_SYS_ISON)
					args.notify("이미 비활성화 중입니다.");
				else{
					MRK_SYS_ISON = false;
					MJRankBusiness.getInstance().dispose();
					MJRankUserLoader.getInstance().offBuff();
					args.notify("시스템을 비활성화 합니다.");
				}
				break;
				
			case 3:
				args.notify(String.format("활성화 상태 : %s", MRK_SYS_ISON));
				break;
			default:
				throw new Exception();
			}
		}catch(Exception e){
			args.notify(".랭킹시스템 1 [1.켬][2.끔][3.상태]");
		}
	}
	
	private void reloadCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:
				loadConfig();
				args.notify("랭킹 컨픽이 리로드되었습니다.");
				break;
				
			case 2:
				if(!MRK_SYS_ISON){
					MRK_SYS_ISON = true;
					MJRankBusiness.getInstance().run();
				}else{
					MRK_SYS_ISON = false;
					MJRankBusiness.getInstance().dispose();
					MRK_SYS_ISON = true;
					MJRankUserLoader.getInstance().offBuff();
					Thread.sleep(1000L);
					MJRankUserLoader.reload();
					MJRankBusiness.getInstance().run();
					Thread.sleep(3000L);
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null || pc.getAI() != null)
							continue;
						
						MJRankUserLoader.getInstance().onUser(pc);
					}
				}
				args.notify("시스템이 재가동되었습니다.(GM 1초렉 있을 수 있음(무시)");
				break;
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".랭킹시스템 2 [1.컨픽][2.시스템재가동]");
		}
	}
	
	private void settingCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:{
				int old = MJRankLoadManager.MRK_SYS_UPDATE_CLOCK;
				MJRankLoadManager.MRK_SYS_UPDATE_CLOCK = args.nextInt();
				args.notify(String.format("시스템 클럭이 %d초에서 %d초로 변경되었습니다.", old, MJRankLoadManager.MRK_SYS_UPDATE_CLOCK));
				break;
			}
			case 2:{
				int old = MJRankLoadManager.MRK_SYS_MINLEVEL;
				MJRankLoadManager.MRK_SYS_MINLEVEL = args.nextInt();
				args.notify(String.format("최소 레벨이 %d초에서 %d초로 변경되었습니다.", old, MJRankLoadManager.MRK_SYS_MINLEVEL));
				break;
			}
			case 3:{
				int old = MJRankLoadManager.MRK_SYS_RANK_POTION;
				MJRankLoadManager.MRK_SYS_RANK_POTION = args.nextInt();
				args.notify(String.format("랭킹포션 진입 레벨이 %d에서 %d로 변경되었습니다.", old, MJRankLoadManager.MRK_SYS_RANK_POTION));
				break;
			}
			case 4:{
				int old = MJRankLoadManager.MRK_SYS_TOTAL_RANGE;
				MJRankLoadManager.MRK_SYS_TOTAL_RANGE = args.nextInt();
				args.notify(String.format("전체랭킹 집계 범위가 %d에서 %d로 변경되었습니다.", old, MJRankLoadManager.MRK_SYS_TOTAL_RANGE));
				break;
			}
			case 5:{
				int old = MJRankLoadManager.MRK_SYS_CLASS_RANGE;
				MJRankLoadManager.MRK_SYS_CLASS_RANGE = args.nextInt();
				args.notify(String.format("클래스랭킹 집계 범위가 %d에서 %d로 변경되었습니다.", old, MJRankLoadManager.MRK_SYS_CLASS_RANGE));
				break;
			}
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".랭킹시스템 3 [옵션] [값]");
			args.notify("옵션 : [1.클럭][2.최소레벨][3.랭킹포션][4.전체범위][5.클래스범위]");
		}
	}
}
