package MJShiftObject;

import java.util.Calendar;
import java.util.List;

import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;

public class MJShiftObjectCommandExecutor implements MJCommand{
	private static final String OPENING_MESSAGE = "서버대항전이 시작 되었습니다. 기란마을 대항전 NPC를 클릭후 입장하시길 바랍니다.";
	public void execute(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:
				show_transfer_info(args);
				break;
			case 2:
				do_character_transfer(args);
				break;
			case 3:
				show_battle_info(args);
				break;
			case 4:
				enter_servers_battle(args);
				break;
			case 5:
				cancel_server_battle(args);
				break;
			case 6:
				reservation_server_battle(args);
				break;
			case 7:
				enter_all_player_server_battle(args);
				break;
			case 8:
				MJShiftObjectManager.getInstance().reload_config();
				args.notify("mj_shiftserver.properties가 리로드 되었습니다.");	
				break;
			default:
				throw new Exception();
			}
		}catch(Exception e){
			args.notify(".서버이동 [1.서버이전정보][2.서버이전]");
			args.notify(".서버이동 [3.대항전정보][4.대항전참가]");
			args.notify(".서버이동 [5.대항전참가취소][6.대항전개설]");
			args.notify(".서버이동 [7.대항전전체입장]");
			args.notify(".서버이동 [8.컨픽리로드]");
		}finally{
			args.dispose();
		}
	}
	
	private void show_transfer_info(MJCommandArgs args){
		List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(false);
		if(servers == null || servers.size() <= 0){
			args.notify("현재 이전할 수 있는 서버 정보가 없습니다.");
			return;
		}
		int success_count = servers.size();
		for(CommonServerInfo csInfo : servers){
			String message = "이전 가능";
			if(!csInfo.server_is_on){
				--success_count;
				message = "이전 불가능(서버OFF)";
			}
			if(!csInfo.server_is_transfer){
				--success_count;
				message = "이전 불가능(기능 닫힘)";
			}
			args.notify(String.format("- [%s] %s", csInfo.server_description, message));
		}
		if(success_count <= 0){
			args.notify("현재 이전할 수 있는 서버가 없습니다.");
		}
		return;
	}
	
	private void do_character_transfer(MJCommandArgs args){
		try{
			String character_name = args.nextString();
			String server_identity = args.nextString();
			if(MJCommons.isNullOrEmpty(character_name) || MJCommons.isNullOrEmpty(server_identity))
				throw new Exception();
			
			L1PcInstance pc = L1World.getInstance().findpc(character_name);
			if(pc == null){
				args.notify(String.format("%s님은 없는 현재 월드맵에 접속하지 않았습니다.", character_name));
				return;
			}
			if(MJShiftObjectManager.getInstance().is_battle_server_running()){
				args.notify("대항전 진행중에는 서버이전을 사용할 수 없습니다.");
				return;
			}
			
			try{
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, server_identity);
				args.notify(String.format("%s님을 %s서버로 이전시켰습니다.", character_name, server_identity));
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			args.notify(".서버이동 2 [캐릭명] [이전서버 식별이름]");
		}
	}
	
	private void show_battle_info(MJCommandArgs args){
		List<CommonServerBattleInfo> servers = MJShiftObjectManager.getInstance().get_battle_servers_info();
		if(servers == null || servers.size() <= 0){
			args.notify("현재 대항전을 진행하고 있는 서버 정보가 없습니다.");
			return;
		}
		String enter_server_identity = MJShiftObjectManager.getInstance().get_battle_server_identity();
		int success_count = servers.size();
		for(CommonServerBattleInfo bInfo : servers){
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar)start_cal.clone();
			start_cal.setTimeInMillis(bInfo.get_start_millis());
			ended_cal.setTimeInMillis(bInfo.get_ended_millis());
			String message = String.format("- [%s] 현재 %s %02d:%02d:%02d ~ %02d:%02d:%02d", 
					bInfo.get_server_identity(),
					enter_server_identity.equals(bInfo.get_server_identity()) ? "참가중" : bInfo.is_ended() ? "종료됨" : bInfo.is_run() ? "진행중" : "예약중",
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE),start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE),ended_cal.get(Calendar.SECOND)
					);
			if(!bInfo.is_run())
				--success_count;
			
			args.notify(message);
		}
		if(success_count <= 0){
			args.notify("현재 대항전을 진행하고 있는 서버가 없습니다.");
		}
		return;
	}
	
	private void enter_servers_battle(MJCommandArgs args){
		try{
			if(MJShiftObjectManager.getInstance().is_battle_server_enter()){
				args.notify(String.format("현재 %s 서버 대항전에 참가하고 있어, 새 참가가 불가능합니다.", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}
			
			String server_identity = args.nextString();
			CommonServerBattleInfo bInfo = MJShiftObjectHelper.get_battle_server_info(server_identity);
			if(bInfo == null){
				args.notify(String.format("%s에 예약된 대항전이 없습니다.", server_identity));
				return;
			}
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, null);
			args.notify(String.format("%s 대항전에 참가합니다.", server_identity));
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(OPENING_MESSAGE),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, OPENING_MESSAGE),
			});
		}catch(Exception e){
			e.printStackTrace();
			args.notify(".서버이동 4 [참가 서버 식별이름]");
		}
	}
	
	private void cancel_server_battle(MJCommandArgs args){
		if(MJShiftObjectManager.getInstance().is_battle_server_running()){
			args.notify("대항전이 시작된 이후에는 취소가 불가능합니다.");
			return;
		}
		if(MJShiftObjectManager.getInstance().is_my_battle_server()){
			args.notify("직접 개설한 대항전은 임의 취소가 불가능합니다.");
			return;
		}
		
		MJShiftObjectManager.getInstance().do_cancel_battle_server();
		args.notify("대항전에 참가를 취소합니다.");
	}

	private void reservation_server_battle(MJCommandArgs args){
		try{
			if(MJShiftObjectManager.getInstance().is_battle_server_enter()){
				args.notify(String.format("현재 %s 서버 대항전에 참가하고 있어, 새 대항전 등록이 불가능합니다.", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}

			int minute = args.nextInt();
			long current_millis = System.currentTimeMillis();
			long ended_millis = (minute * 60000) + current_millis;

			CommonServerBattleInfo bInfo = 
					CommonServerBattleInfo.newInstance()
					.set_server_identity(MJShiftObjectManager.getInstance().get_home_server_identity())
					.set_start_millis(current_millis)
					.set_ended_millis(ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_ready_seconds() * 1000));
			
			MJShiftObjectHelper.reservation_server_battle(
					MJShiftObjectManager.getInstance().get_home_server_identity(),
					current_millis, 
					ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_store_ready_seconds() * 1000));
			
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar)start_cal.clone();
			start_cal.setTimeInMillis(current_millis);
			ended_cal.setTimeInMillis(ended_millis);
			String message = String.format("[서버대항전 개설] %02d:%02d:%02d ~ %02d:%02d:%02d", 
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE),start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE),ended_cal.get(Calendar.SECOND)
					);
			args.notify(message);

			MJThebePlayManager manager = new MJThebePlayManager(bInfo.get_ended_millis());
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, manager);
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(OPENING_MESSAGE),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, OPENING_MESSAGE),
			});
		}catch(Exception e){
			e.printStackTrace();
			args.notify(".서버이동 6 [대항전 지속시간(분)]");
		}
	}
	
	private void enter_all_player_server_battle(MJCommandArgs args){
		try{
			if(!MJShiftObjectManager.getInstance().is_battle_server_running()){
				args.notify("현재 참가중인 대항전이 없습니다.");
				return;
			}
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers())
				MJShiftObjectManager.getInstance().do_send_battle_server(pc);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
