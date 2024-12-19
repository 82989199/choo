package l1j.server.MJNetSafeSystem.Distribution;

import static l1j.server.server.Opcodes.C_QUIT;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.C_Craft;
import l1j.server.server.clientpackets.C_CreateChar;
import l1j.server.server.clientpackets.C_DeleteChar;
import l1j.server.server.clientpackets.C_LoginToServerWrap;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.clientpackets.C_Quit;
import l1j.server.server.clientpackets.C_Report;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.C_ReturnToLogin;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.ServerBasePacket;

public class SelectCharacterDistributor  extends Distributor{

	public static ServerBasePacket _passwordInputWindow = null;
	
	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		switch(op){
		case Opcodes.C_EXTENDED_PROTOBUF:
			return new C_Craft(data, clnt);
	
		case Opcodes.C_CREATE_CUSTOM_CHARACTER:
			if(data.length > 0x20)
				break;
			
			return new C_CreateChar(data, clnt);
			
		case Opcodes.C_DELETE_CHARACTER:
			if(clnt.getAccount().getCPW() != null){
				clnt.getAccount().setwaitpacket(data);
				clnt.sendPacket(new S_CharPass(S_CharPass._비번입력창));
				return null;
			}
			return new C_DeleteChar(data, clnt);
		
		case Opcodes.C_CHANNEL:
			return new C_Report(data, clnt);
			
		case Opcodes.C_LOGOUT:
			return new C_ReturnToLogin(data, clnt);
			
		case Opcodes.C_ENTER_WORLD:
			if(Config.CHAR_PASSWORD && clnt.getAccount().getCPW() != null && !clnt.isLoginRecord()){
				clnt.getAccount().setwaitpacket(data);
				if(_passwordInputWindow == null)
					_passwordInputWindow = new S_CharPass(S_CharPass._비번입력창);
				clnt.sendPacket(_passwordInputWindow, false);
				return null;
			}
			return new C_LoginToServerWrap(data, clnt);
		
		case Opcodes.C_VOICE_CHAT:
			return new C_ReturnStaus(data, clnt); 
			
		case Opcodes.C_READ_NEWS:
			ConnectedDistributor.sendNotice(clnt, clnt.getAccountName());
			return null;
			
		case C_QUIT:
			return new C_Quit(data, clnt);

		case Opcodes.C_RESTART:
			return new C_NewCharSelect(data, clnt); 
			
		// TODO 리스 누를때 동시에 들어오는 패킷 원인파악완료-> 메세지미출력하게
		case Opcodes.C_MOVE:
		case Opcodes.C_USE_ITEM:
		case Opcodes.C_USE_SPELL:
		case Opcodes.C_BUY_SELL:
		case Opcodes.C_ONOFF:
		case Opcodes.C_SAVEIO:
			return null;

		default:
			break;
		}
		toInvalidOp(clnt, op, data.length, "SelectCharacter", false);
		return null;
	}

}
