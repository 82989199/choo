package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;

public class S_PetCtrlMenu extends ServerBasePacket {

    private static final String S_PetCtrlMenu = "[S] S_PetCtrlMenu";

    /** ui6 관련 펫파티,컨트롤 통으로추가 **/
    public S_PetCtrlMenu(L1PcInstance pc, L1NpcInstance npc, boolean open) {
        buildPacket(pc, npc, open);
    }

    private void buildPacket(L1PcInstance pc, L1NpcInstance npc, boolean open) {

    	writeC(Opcodes.S_VOICE_CHAT);
        writeC(0x0c);
        
        if (open) {
        	writeH(pc.getPetList().size() * 3);
        	writeD(0x00000000);
        	writeD(npc.getId());
        	writeH(npc.getMapId());
        	writeH(0x0000);
        	writeH(npc.getX());
        	writeH(npc.getY());
        	if(npc instanceof L1PetInstance){
        		writeC(0x01);
        	} else {
        		writeC(0x00);
        	}
        	writeS(npc.getNameId());
        } else {
        	writeH(pc.getPetList().size() * 3 - 3);
        	writeD(0x00000001);
        	writeD(npc.getId());
        }
    }

    @Override
    public byte[] getContent() {
    	return getBytes();
    }

    @Override
    public String getType() {
        return S_PetCtrlMenu;
    }
}
