package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_EinhasadClanBuff extends ServerBasePacket {
    private static final String _S_EinhasadClanBuff = "[S] S_EinhasadClanBuff";

	public S_EinhasadClanBuff(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0xfb);
		writeC(0x03);
		
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if(clan == null){
			writeC(0xfb);
			writeC(0x03);			
			writeC(0x0a); 
			writeC(0x00);
			writeH(0x00);
			return;
		}
		
		if(clan.getBuffFirst() != 0){
			writeC(0x0a); //넘버 
			writeC(0x05); //사이즈
			writeC(0x08); //구분자
			writeBit(clan.getBuffFirst()); //메세지 넘버
			writeC(0x10); //구분자
			if(clan.getEinhasadBlessBuff() == 0){
				writeC(0x01); //온 오프
			}else if(clan.getEinhasadBlessBuff() == clan.getBuffFirst()){
				writeC(0x02); //온 오프
			}else writeC(0x03); //온 오프
		}
		
		//아래 동일 
		if(clan.getBuffSecond() != 0){
			writeC(0x0a);
			writeC(0x05);
			writeC(0x08);
			writeBit(clan.getBuffSecond());
			writeC(0x10);
			if(clan.getEinhasadBlessBuff() == 0){
				writeC(0x01); //온 오프
			}else if(clan.getEinhasadBlessBuff() == clan.getBuffSecond()){
				writeC(0x02); //온 오프
			}else writeC(0x03); //온 오프
		}
		
		//아래 동일 
		if(clan.getBuffThird() != 0){
			writeC(0x0a);
			writeC(0x05);
			writeC(0x08);
			writeBit(clan.getBuffThird());
			writeC(0x10);
			if(clan.getEinhasadBlessBuff() == 0){
				writeC(0x01); //온 오프
			}else if(clan.getEinhasadBlessBuff() == clan.getBuffThird()){
				writeC(0x02); //온 오프
			}else writeC(0x03); //온 오프
		}
		
		writeC(0x10); //구분자
		writeC(0x01); //전체변경 활성화 0이면 비할 1이면 활성
		
		writeH(0x00);
	}
    
    @Override
    public byte[] getContent() {
    	return getBytes();
    }

    @Override
    public String getType() {
        return _S_EinhasadClanBuff;
    }
}
