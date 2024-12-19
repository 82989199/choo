package l1j.server.server.clientpackets;

import java.io.FileNotFoundException;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_CharPass;

public class C_LoginToServerWrap extends ClientBasePacket {
	public C_LoginToServerWrap(byte[] abyte0, GameClient client)
			throws FileNotFoundException, Exception {
		super(abyte0);
		
		if(Config.CHAR_PASSWORD && client.getAccount().getCPW() != null && !client.isLoginRecord()){
			client.getAccount().setwaitpacket(abyte0);
			client.sendPacket(new S_CharPass(S_CharPass._비번입력창));
			return;
		}
		ClientBasePacket pck = new C_LoginToServer(abyte0, client);
		//ClientBasePacket pck = Config.IS_USE_LOGIN_THREAD_VERSION ? new C_LoginToServer(abyte0, client) : new C_LoginToServer2(abyte0, client);
		pck.clear();
	}


	private static final String C_LoginToServerWrap = "[C] C_LoginToServerWrap";

	
	@Override
	public String getType() {
		return C_LoginToServerWrap;
	}
}
