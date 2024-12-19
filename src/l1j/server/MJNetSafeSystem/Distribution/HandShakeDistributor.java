package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;

public class HandShakeDistributor extends Distributor{
	
	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		if(isBanned(clnt))
			return null;
		
		if(op == Opcodes.C_EXTENDED_PROTOBUF){
			if(MJEProtoMessages.existsProto(clnt, data)){
				clnt.setStatus(MJClientStatus.CLNT_STS_CONNECTED);
				return null;
			}
		}
		toInvalidOp(clnt, op, data.length, "HandShake.", true);
		return null;
	}

}
