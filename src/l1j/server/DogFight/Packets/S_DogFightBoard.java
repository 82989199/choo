package l1j.server.DogFight.Packets;

import java.util.ArrayList;

import l1j.server.DogFight.Game.MJDogFightGame;
import l1j.server.DogFight.History.MJDogFightHistory;
import l1j.server.DogFight.History.MJDogFightTicketInfo;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DogFightBoard extends ServerBasePacket{

	public static S_DogFightBoard newInstance(boolean is_gm, ArrayList<MJDogFightTicketInfo> tickets_info){
		S_DogFightBoard board = new S_DogFightBoard();
		board.writeD(1);
		board.writeS("maeno4");
		board.writeC(0);
		board.writeH(15);
		if(tickets_info.size() == 3)
			
			create_normal_size(board, is_gm, tickets_info);
		else
			create_full_size(board, is_gm, tickets_info);
		return board;
	}
	
	private static void create_full_size(S_DogFightBoard board, boolean is_gm, ArrayList<MJDogFightTicketInfo> tickets_info){
		int remain = 5;
		for(MJDogFightTicketInfo tInfo : tickets_info){
			write_tickets_info(board, tInfo, is_gm);
			--remain;
		}
		if(remain > 0){
			for(int i = remain - 1; i>=0; --i){
				board.writeS("-");
				board.writeS("-");
				board.writeS("-");
			}
		}
	}
	
	private static void create_normal_size(S_DogFightBoard board, boolean is_gm, ArrayList<MJDogFightTicketInfo> tickets_info){
		board.writeS("-");
		board.writeS("-");
		board.writeS("-");
		write_tickets_info(board, tickets_info.get(0), is_gm);
		board.writeS("-");
		board.writeS("-");
		board.writeS("-");
		write_tickets_info(board, tickets_info.get(1), is_gm);
		board.writeS("-");
		board.writeS("-");
		board.writeS("-");
	}
	
	private static void write_tickets_info(S_DogFightBoard board, MJDogFightTicketInfo tInfo, boolean is_gm){
		/*if(is_gm){
			board.writeS(String.format("[%s]", tInfo.get_corner_name()));
			board.writeS(String.format("%.2f%%", tInfo.get_dividend()));	
			board.writeS(String.format("%.1f%%", tInfo.get_win_rate()));
			MJDogFightInstance fight = MJDogFightGame.getDogFight(tInfo.get_corner_id());
			if(fight != null) {
				board.writeS(String.format("[%d]", fight.getTotalPrice()));
			}
			
		}else{*/
			board.writeS(MJDogFightHistory.getInstance().get_leader_name(tInfo.get_corner_id()).replace("[홀]", "").replace("[짝]", ""));
			board.writeS(String.format("%s", tInfo.get_Condition()));
			board.writeS(String.format("%.1f%%", tInfo.get_win_rate()));
			
			/*board.writeS(MJDogFightHistory.getInstance().get_leader_name(tInfo.get_corner_id()).replace("[홀]", "").replace("[짝]", ""));
			board.writeS(String.format("[%s]", tInfo.get_corner_name()));
			board.writeS(String.format("%.1f%%", tInfo.get_win_rate()));		*/	
		//}
	}
	
	private S_DogFightBoard(){
		writeC(Opcodes.S_HYPERTEXT);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
