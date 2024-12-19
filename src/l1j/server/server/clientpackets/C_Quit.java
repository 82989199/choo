package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;

public class C_Quit extends ClientBasePacket{
	public C_Quit(byte abyte0[], GameClient clnt) {
		super(abyte0);
		try{
			clnt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
