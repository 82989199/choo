package l1j.server.MJDungeonTimer.Loader;

import java.util.Calendar;

import l1j.server.MJDungeonTimer.Updator.DungeonTimeUpdator;
import l1j.server.MJIndexStamp.MJEStampIndex;
import l1j.server.MJIndexStamp.MJIndexStampManager;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;

public class DungeonTimeLoadManager implements MJCommand{
	private static DungeonTimeLoadManager _instance;
	public static DungeonTimeLoadManager getInstance(){
		if(_instance == null)
			_instance = new DungeonTimeLoadManager();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		DungeonTimeLoadManager old = _instance;
		_instance = new DungeonTimeLoadManager();
		if(old != null){
			old.dispose();
			old = null;
		}
	}

	private MJCommandTree _commands;
	private DungeonTimeLoadManager(){
		_commands = createCommand();
	}
	
	public void do_truncate(Calendar current){
		Updator.truncate("tb_dungeon_time_account_information");
		Updator.truncate("tb_dungeon_time_char_information");
		MJIndexStampManager.update(MJEStampIndex.SIDX_DUNGEON_TIME_TRUNCATE, current);
	}
	
	private void do_truncate(){
	      Calendar cal = MJIndexStampManager.select(MJEStampIndex.SIDX_DUNGEON_TIME_TRUNCATE);
	      Calendar current = RealTimeClock.getInstance().getRealTimeCalendar();
	      if(cal == null ||
	            cal.get(Calendar.YEAR) != current.get(Calendar.YEAR) || 
	            cal.get(Calendar.MONTH) != current.get(Calendar.MONDAY) || 
	            cal.get(Calendar.DAY_OF_MONTH) != current.get(Calendar.DAY_OF_MONTH)){
	         
	         do_truncate(current);
	      }
	   }
	
	public void load(){
		do_truncate();
		DungeonTimeInformationLoader.getInstance();
		DungeonTimeUpdator.getInstance().run();
		DungeonTimePotionLoader.getInstance();
	}

	public void dispose(){
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}
	
	private MJCommandTree createCommand(){
		return new MJCommandTree(".던전타이머", "던전타이머 관련 명령을 수행합니다.", null)
				.add_command(createReloadCommand())
				.add_command(createInitializeCommand())
				.add_command(createSchedulerCommand());
	}
	
	private MJCommandTree createReloadCommand(){
		return new MJCommandTree("리로드", "리로드 관련 명령을 수행합니다.", null)
				.add_command(new MJCommandTree("정보", "", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						DungeonTimeInformationLoader.reload();
						args.notify("tb_dungeon_time_information 테이블을 리로드 하였습니다.");
					}
				})
				.add_command(new MJCommandTree("포션", "", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						DungeonTimePotionLoader.reload();
						args.notify("tb_dungeon_time_potion 테이블을 리로드 하였습니다.");
					}
				});
	}
	
	private MJCommandTree createInitializeCommand(){
		return new MJCommandTree("초기화", "초기화 관련 명령을 수행합니다.", null)
				.add_command(new MJCommandTree("캐릭터", "지정된 캐릭터의 던전시간을 초기화합니다.", new String[]{"캐릭터이름"}){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						String character_name = args.nextString();
						L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
						if(pc == null){
							args.notify(String.format("캐릭터 이름 %s을(를) 찾을 수 없습니다.", character_name));
							return;
						}
						DungeonTimeProgressLoader.delete(pc);
						pc.initialize_dungeon_progress();
						pc.sendPackets("모든 던전 시간이 초기화 되었습니다.");
						args.notify(String.format("%s의 모든 던전 시간을 초기화 했습니다.", character_name));
					}
				})
				.add_command(new MJCommandTree("전체초기화", "전체 케릭터의 던전시간을 초기화합니다.", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						DungeonTimeUpdator.getInstance().do_initialize(RealTimeClock.getInstance().getRealTimeCalendar());
						args.notify("전체 초기화를 완료했습니다.");
					}
				});
	}
	
	private MJCommandTree createSchedulerCommand(){
		return new MJCommandTree("스케줄", "스케줄 관련 명령을 수행합니다.", null)
				.add_command(new MJCommandTree("상태", "현재 스케줄의 상태를 나타냅니다.", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						args.notify(String.format("현재 스케줄링 상태 : %s", DungeonTimeUpdator.getInstance().is_running() ? "켜짐" : "꺼짐"));
					}
				})
				.add_command(new MJCommandTree("재시작", "스케줄을 재시작합니다.", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						DungeonTimeUpdator.getInstance().dispose();
						Thread.sleep(500L);
						DungeonTimeUpdator.getInstance().run();
						args.notify("스케줄링을 재시작 했습니다.");
					}
				})
				.add_command(new MJCommandTree("중지", "스케줄을 중지합니다.", null){
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception{
						DungeonTimeUpdator.getInstance().dispose();
						args.notify("스케줄링을 중지 했습니다.");
					}
				});
	}
}
