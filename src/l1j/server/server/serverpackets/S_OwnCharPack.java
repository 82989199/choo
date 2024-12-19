package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.MJInstanceSystem.MJInstanceObject;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_OwnCharPack extends ServerBasePacket {

	private static final String S_OWN_CHAR_PACK = "[S] S_OwnCharPack";

	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;
	private static final int BLOOD_LUST = 16;// 100
	private static final int DANCING_BLADES = 16;// 댄싱

	
	public S_OwnCharPack(L1PcInstance pc) {
		if(pc == null)
			return;
		
		// builderPacket(pc);
		
		int mid = pc.getMapId();
		if (mid == 1708 && mid <= 1710) 	// 잊섬
			buildPacket(pc);
		/** 2016.11.26 MJ 앱센터 LFC **/
		else if(MJInstanceSpace.isInInstance(mid))
			buildPacket(pc);
		else if(pc.getRedKnightClanId() != 0) 	//붉은기사단
			buildPacket(pc);			
		else
			oldBuildPacket(pc);
	}

	private void oldBuildPacket(L1PcInstance pc){
		int status = STATUS_PC;
 
				if (pc.isInvisble() || pc.isGmInvis()) {
					status |= STATUS_INVISIBLE;
				}
				if (pc.isBrave()) {
					status |= STATUS_BRAVE;
				}
				if (pc.isElfBrave()) {
					status |= STATUS_BRAVE;
					status |= STATUS_ELFBRAVE;
				}
				
				if(pc.isUgdraFruit()) { 
					status |= STATUS_FASTMOVABLE; 
				}
				
				if (pc.isBlood_lust()) {
					status |= BLOOD_LUST;
				}
				if (pc.isElfBraveMagicShort() || pc.isElfBraveMagicLong() || pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
					status |= DANCING_BLADES;
				}
				if (pc.isFastMovable() || pc.isFruit() || pc.isUgdraFruit()) {// 유그드라 추가 변경1/19
					status |= STATUS_FASTMOVABLE;
				}
				if (pc.isGhost()) {
					status |= STATUS_GHOST;
				}
				if (pc.isParalyzed()) {
					status |= STATUS_FREEZE;
				}
		
				writeC(Opcodes.S_PUT_OBJECT);
				writeH(pc.getX());
				writeH(pc.getY());
				writeD(pc.getId());
				if (pc.isDead()) {
					writeH(pc.getTempCharGfxAtDead());
				} else if (pc.isPrivateShop()) {
					if (pc.상점변신 != 0)
						writeH(pc.상점변신);
				} else {
					writeH(pc.getCurrentSpriteId());
				}
				if (pc.isDead()) {
					writeBit(ActionCodes.ACTION_Die);
				} else if (pc.isPrivateShop()) {
					writeC(70);
				} else {
					writeC(pc.getCurrentWeapon());
				}
				writeC(pc.getHeading());
				writeC(pc.getLight().getOwnLightSize());
				writeC(pc.getMoveSpeed());
				writeD(pc.getExp());
				writeH(pc.getLawful());
				writeS(pc.getName());
				writeS(pc.getTitle());
				writeC(status);
				if(pc.getClanid() > 0 && pc.getClan() != null)	writeD(pc.getClan().getEmblemId());
				else														writeD(0);
				writeS(pc.getClanname()); // 크란명
				writeS(null); // 펫호팅?
				writeC(0);
				if (pc.isInParty()) // 파티중
				{
					writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
				} else {
					writeC(0xFF);
				}
				writeC(pc.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL) ? 0x08 : 0x00); // 타르쿡크거리(대로)
				writeC(0); // PC = 0, Mon = Lv
				writeC(0); // ?
				writeC(0xFF);
				writeC(0xFF);
				writeC(0x00);
				int value = 0;
				if(Config.POLY_EVENT) {
					value = 11;
				}else {
					if (pc.getLevel() >= 80) {
						value = 11;
					} else if (pc.getLevel() >= 55) {
						value = (pc.getLevel() - 25) / 5;
					} else if (pc.getLevel() >= 52) {
						value = 5;
					} else if (pc.getLevel() >= 50) {
						value = 4;
					} else if (pc.getLevel() >= 15) {
						value = pc.getLevel() / 15; // 15~29 = 1, 30~44 = 2, 45~49 = 3
					}
				}
				writeC(value);
				if (pc.isInParty()) // 파티중
				{
					writeC(100 * pc.getCurrentMp() / pc.getMaxMp());
				} else {
					writeC(0xFF);
				}
				writeH(0);
	}
	
	private void buildPacket(L1PcInstance pc) {		
		int status = STATUS_PC;

		if (pc.isInvisble() || pc.isGmInvis()) {
			status |= STATUS_INVISIBLE;
		}
		if (pc.isBrave()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isElfBrave()) {
			status |= STATUS_BRAVE;
			status |= STATUS_ELFBRAVE;
		}
		if (pc.isBlood_lust()) {
			status |= BLOOD_LUST;
		}
		if (pc.isElfBraveMagicShort() || pc.isElfBraveMagicLong() || pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
			status |= DANCING_BLADES;
		}
		if (pc.isFastMovable() || pc.isFruit() || pc.isUgdraFruit()) {// 유그드라 추가 변경1/19
			status |= STATUS_FASTMOVABLE;
		}
		if (pc.isGhost()) {
			status |= STATUS_GHOST;
		}
		if (pc.isParalyzed()) {
			status |= STATUS_FREEZE;
		}

		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(119);
		writeC(8);
		writeBit(pc.getX(), pc.getY());

		writeC(16);
		writeBit(pc.getId());

		writeC(24);
		writeBit(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getCurrentSpriteId());

		writeC(32);
		if (pc.isDead()){
			writeBit(ActionCodes.ACTION_Die);
		}else if (pc.isPrivateShop())
			writeBit(70L);
		else if (pc.isFishing())
			writeBit(71L);
		else {
			writeBit(pc.getCurrentWeapon());
		}

		writeC(40);
		writeBit(pc.getHeading());

		writeC(48);
		writeBit(pc.getLight().getOwnLightSize());

		writeC(56);
		writeBit(1L);

		writeC(64);
		writeBit(pc.getLawful());

		writeC(74);
		writeC(pc.getName().getBytes().length);
		writeByte(pc.getName().getBytes());

		writeC(82);
		if ((pc.getTitle() == null) || (pc.getTitle().equals(""))) {
			writeC(0);
		} else {
			writeC(pc.getTitle().getBytes().length);
			writeByte(pc.getTitle().getBytes());
		}

		writeC(88);
		writeBit(pc.isHaste() ? 1L : 0L);

		writeC(96);
		if (pc.isBrave() || pc.isBlood_lust() || pc.isElfBraveMagicShort())
			writeBit(1L);
		else if (pc.isElfBrave())
			writeBit(3L);
		else if ((pc.isFastMovable()) || (pc.isFruit()|| pc.isUgdraFruit()))
			writeBit(4L);
		else {
			writeBit(0L);
		}

		writeC(104);
		writeBit(0L);

		writeC(112);
		writeBit(pc.isGhost() ? 1L : 0L);

		writeC(120);
		writeBit(pc.getParalysis() != null ? 1L : 0L);

		writeC(128);
		writeC(1);
		writeBit(1L);

		writeC(136);
		writeC(1);
		writeBit((pc.isInvisble()) || (pc.isGmInvis()) ? 1L : 0L);

		writeC(144);
		writeC(1);
		writeBit(0L); // 독인듯?

		writeC(152);
		writeC(1);
		int emblemId = 0;
		if (pc.getClanid() > 0) {
			L1Clan clan = pc.getClan();
			if (clan != null)
				emblemId = clan.getEmblemId();
		}
		writeBit(emblemId);

		writeC(162);
		writeC(1);
		String clanName = pc.getClanname();
		if(pc.getRedKnightClanId() != 0)
			clanName = "붉은 기사단";
		else if (((pc.getMapId() >= 1005) && (pc.getMapId() <= 1070)) || (pc.getMapId() == 501)
				|| (pc.getMapId() == 2009)) {
			clanName = String.valueOf(pc.getMapId());
		}
		if ((clanName == null) || (clanName.equals(""))) {
			writeC(0);
		} else {
			writeC(clanName.getBytes().length);
			writeByte(clanName.getBytes());
		}

		writeC(170);
		writeC(1);
		writeBit(0L);

		writeC(176);
		writeC(1);
		writeBit(0L);

		writeC(184);
		writeC(1);

		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(1);

		writeC(192);
		writeC(1);
		writeBit(0L);

		writeC(202);
		writeC(1); // 아이디가 보이고 안보이고함
		if ((pc.getShopChat() == null) || (pc.getShopChat().length <= 0)) {
			writeBit(0L);
		} else {
			writeC(pc.getShopChat().length);
			writeByte(pc.getShopChat());
		}

		writeC(208);
		writeC(1);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(1);

		writeC(216);
		writeC(1);
		writeBit(0L);

		writeC(224);
		writeC(1);
		int value = 0;
		if(Config.POLY_EVENT) {
			value = 11;
		}else {
			if (pc.getLevel() >= 80) {
				value = 11;
			} else if (pc.getLevel() >= 55) {
				value = (pc.getLevel() - 25) / 5;
			} else if (pc.getLevel() >= 52) {
				value = 5;
			} else if (pc.getLevel() >= 50) {
				value = 4;
			} else if (pc.getLevel() >= 15) {
				value = pc.getLevel() / 15; // 15~29 = 1, 30~44 = 2, 45~49 = 3
			}
		}
		writeC(value);

		writeC(240);
		writeC(1);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(255);
		writeC(1);

		writeC(0x80);
		writeC(0x02);
		writeC(0);

		writeC(0x88);
		writeC(0x02);
		
		/** 2016.11.26 MJ 앱센터 LFC **/
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if(instobj != null)
			writeC(instobj.getMarkStatus(pc));
		else if(pc.getRedKnightClanId() != 0){//붉은기사단
			writeC(7); // 혈마크 번호
		} else if (pc.getMapId() == 99/* && pc.getMapId() <= 1710*/) {//나에게보여지는 기준
			writeC(30); // 백조모양
		} else {
			writeC(29); // 혈마크 번호
		}

		writeC(0x98);
		writeC(0x02);
		writeC(0);
		
		writeC(128);
		writeC(2);
		writeBit(0);//서버명

		writeC(0);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_OWN_CHAR_PACK;
	}
}
