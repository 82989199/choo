package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.utils.DelayClose;

public abstract class Distributor {
	protected void toInvalidOp(GameClient clnt, int op, int length, String state, boolean isClose) throws Exception{
		System.out.println(String.format("invalid opcode. [%s] op : %d, length : %d -> kick.(%s)", clnt.getIp(), op, length, state));
		if(isClose)
			clnt.close();
	}
	
	
	protected boolean isBanned(GameClient clnt){
		String ip = clnt.getIp();
		if (IpTable.getInstance().isBannedIp(ip)) {
			System.out.println("\n┌──────────────────────────────────────┐");
			System.out.println(String.format("\t 차단된 IP 접속 차단! 아이피=%s",ip));
			System.out.println("└──────────────────────────────────────┘\n");
			clnt.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCOUNT_ALREADY_EXISTS));
			GeneralThreadPool.getInstance().schedule(new DelayClose(clnt), 5000);
			return true;
		}
		return false;
	}

	public abstract ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception;
}
