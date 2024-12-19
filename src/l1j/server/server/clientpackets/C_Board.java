package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;

public class C_Board extends ClientBasePacket {

	private static final String C_BOARD = "[C] C_Board";

	private boolean isBoardInstance(L1Object obj) {
		return (obj instanceof L1BoardInstance || obj instanceof L1AuctionBoardInstance);
	}	
	private boolean isNewBoard(L1Object obj) {
		if(obj instanceof L1NpcInstance) {
			L1NpcInstance b = (L1NpcInstance) obj;
			if(b.getNpcTemplate().get_npcId() == 7310127) {//사용않함 액션번호
				return true;
			}
		}
		return false;
	}

	public C_Board(byte abyte0[], GameClient clientthread) {
		super(abyte0);	
		
		int objectId = readD();
		L1PcInstance pc = clientthread.getActiveChar();	
		L1Object obj = L1World.getInstance().findObject(objectId);	
		if (!isBoardInstance(obj)) {			
			return; // 부정 클라이언트가 아니면 있을 수 없겠지만···
		}

		if(isNewBoard(obj)) {			
			if (obj != null && pc != null) {
				L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(7310127);	//사용않함 액션번호
				pc.sendPackets(new S_NPCTalkReturn(talking, objectId, 1));
			}
			return;
		}
		obj.onAction(clientthread.getActiveChar());
	}

	@Override
	public String getType() {
		return C_BOARD;
	}

}
