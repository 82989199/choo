package MJShiftObject.Battle;

import MJShiftObject.Battle.Thebe.MJThebeMessage;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;

public class MJShiftBattleCommandExecutor implements MJCommand{
	public MJShiftBattleCommandExecutor(){}

	@Override
	public void execute(MJCommandArgs args) {
		try{
			switch(args.nextInt()){
			case 1:
				MJShiftBattleItemWhiteList.reload();
				args.notify("server_battle_white_list 테이블이 리로드 되었습니다.");
				break;
			case 2:
				MJShiftBattleArgs.load();
				args.notify("mj_shiftbattle.properties를 리로드했습니다.");
				break;
			case 3:
				MJThebeMessage.do_message_test(args.getOwner());
				break;
			default:
				throw new Exception();	
			}
		}catch(Exception e){
			args.notify(".대항전 [1.화이트리스트 리로드][2.컨픽리로드]");
			args.notify(".대항전 [3.테베라스 메시지 테스트]");
		}finally{
			args.dispose();
		}
	}
}
