package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.MJInstanceSystem.MJInstanceObject;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_OtherCharPacks extends ServerBasePacket {

	private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";

	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;
	private static final int BLOOD_LUST = 16;// 100
	private static final int DANCING_BLADES = 16;// 댄싱

	public S_OtherCharPacks(L1PcInstance pc, boolean isClan) {

		int status = STATUS_PC;

		// 굴독같은 초록의 독
		// if (pc.isPoison()) {
		// status |= STATUS_POISON;
		// }

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
		writeBit(pc.getCurrentSpriteId());

		writeC(32);
		if (pc.isDead()){
			//writeBit(pc.getStatus());
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
		String name = pc.getName();
		if (name.length() > 0) {
			writeC(name.getBytes().length);
			writeByte(name.getBytes());
		} else {
			writeC(0);
		}

		writeC(82);
		String title = pc.getTitle();
		if ((title == null) || (title.length() == 0)) {
			writeC(0);
		} else {
			writeC(title.getBytes().length);
			writeByte(title.getBytes());
		}

		writeC(88);
		writeBit(pc.isHaste() ? 1L : 0L);

		writeC(96);
		if (pc.isBrave())
			writeBit(1L);
		else if (pc.isElfBrave())
			writeBit(3L);
		else if ((pc.isFastMovable()) || (pc.isFruit() || pc.isUgdraFruit()))
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

		writeBit(128L);
		writeBit(1L);

		writeBit(136L);
		writeBit(pc.isInvisble() ? 1L : 0L);

		writeBit(144L);
		writeBit(0L);

		writeBit(152L);
		int emblemId = 0;
		if (pc.getClanid() > 0) {
			L1Clan clan = pc.getClan();
			if (clan != null) {
				emblemId = clan.getEmblemId();
			}
		}
		writeBit(emblemId);

		writeBit(162L);
		String clanName = pc.getClanname();
		if (((pc.getMapId() >= 1005) && (pc.getMapId() <= 1070)) || (pc.getMapId() == 501) || (pc.getMapId() == 2009)) {
			clanName = String.valueOf(pc.getMapId());
		}
		if ((clanName == null) || (clanName.length() == 0)) {
			writeC(0);
		} else {
			writeC(clanName.getBytes().length);
			writeByte(clanName.getBytes());
		}

		writeBit(170L);
		writeBit(0L);

		writeBit(176L);
		writeBit(0L);

		writeBit(184L);
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

		writeBit(192L);
		writeBit(0L);

		writeBit(202L);
		if ((pc.getShopChat() != null) && (pc.getShopChat().length > 0)) {
			writeC(pc.getShopChat().length);
			writeByte(pc.getShopChat());
		} else {
			writeBit(0L);
		}

		writeBit(208L);
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

		writeBit(216L);
		writeBit(0L);

		writeBit(224L);
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
				value = pc.getLevel() / 15;
			}
		}
		writeBit(value);

		writeBit(240L);
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

		/****/
		writeC(0x80);
		writeC(0x02);
		writeC(0);

		writeC(0x88);
		writeC(0x02);
		
		/** 2016.11.26 MJ 앱센터 LFC **/
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if(instobj != null)
			writeC(instobj.getMarkStatus(pc));
		else if(pc.getMapId() == 99/* && pc.getMapId() <= 1710*/) {//다른사람이 보여지는 기준
			if(isClan) {
				writeC(3);//같은혈 백조
			}else {
				writeC(9);//다른혈 뱀
			}
		}else {
			if(pc.getClanid() == 290040001){//붉은기사단
				writeC(7); // 혈마크 번호	
			} else {
				writeC(0); // 혈마크 번호
			}
		}

		writeC(0x98);
		writeC(0x02);
		writeC(0);
		/****/

		writeBit(256L);
		writeBit(0);//서버명

		writeH(0);
	}
	public S_OtherCharPacks(L1PcInstance pc) {		
		int status = STATUS_PC;

		// 굴독같은 초록의 독
		// if (pc.isPoison()) {
		// status |= STATUS_POISON;
		// }

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
		
		if (pc.isUgdraFruit()) {
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

		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(119);
		writeC(8);

		writeBit(pc.getX(), pc.getY());

		writeC(16);
		writeBit(pc.getId());

		writeC(24);
		writeBit(pc.getCurrentSpriteId());

		writeC(32);
		if (pc.isDead()){
			//writeBit(pc.getStatus());
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
		String name = pc.getName();

		if (name.length() > 0) {
			writeBit(name.getBytes().length);
			writeByte(name.getBytes());
		} else {
			writeC(0);
		}

		writeC(82);
		String title = pc.getTitle();
		if ((title == null) || (title.length() == 0)) {
			writeC(0);
		} else {
			writeC(title.getBytes().length);
			writeByte(title.getBytes());
		}

		writeC(88);
		writeBit(pc.isHaste() ? 1L : 0L);

		writeC(96);
		if (pc.isBrave() || pc.isBlood_lust() || pc.isElfBraveMagicShort())
			writeBit(1L);
		else if (pc.isElfBrave())
			writeBit(3L);
		else if ((pc.isFastMovable()) || (pc.isFruit()) || pc.isUgdraFruit())
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

		writeBit(128L);
		writeBit(1L);

		writeBit(136L);
		writeBit(pc.isInvisble() ? 1L : 0L);

		writeBit(144L);
		writeBit(0L);

		writeBit(152L);
		int emblemId = 0;
		if (pc.getClanid() > 0) {
			L1Clan clan;
			if(pc.getAccountName().equals("로봇시스템")) {
				clan = L1World.getInstance().getClan(pc.getClanid());
			}else {
				clan = pc.getClan();					
			}
			if (clan != null) {
				emblemId = clan.getEmblemId();				
			}
		}
		writeBit(emblemId);

		writeBit(162L);
		String clanName = pc.getClanname();
		if (((pc.getMapId() >= 1005) && (pc.getMapId() <= 1070)) || (pc.getMapId() == 501)
				|| (pc.getMapId() == 2009)) {
			clanName = String.valueOf(pc.getMapId());
		}
		if ((clanName == null) || (clanName.length() == 0)) {
			writeC(0);
		} else {
			writeC(clanName.getBytes().length);
			writeByte(clanName.getBytes());
		}

		writeBit(170L);
		writeBit(0L);

		writeBit(176L);
		writeBit(0L);

		writeBit(184L);
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

		writeBit(192L);
		writeBit(0L);

		writeBit(202L);
		if ((pc.getShopChat() != null) && (pc.getShopChat().length > 0)) {
			writeC(pc.getShopChat().length);
			writeByte(pc.getShopChat());
		} else {
			writeBit(0L);
		}

		writeBit(208L);
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

		writeBit(216L);
		writeBit(0L);

		writeBit(224L);
		int value = 0;
		if (pc.getLevel() >= 80) {
			value = 11;
		} else if (pc.getLevel() >= 55) {
			value = (pc.getLevel() - 25) / 5;
		} else if (pc.getLevel() >= 52) {
			value = 5;
		} else if (pc.getLevel() >= 50) {
			value = 4;
		} else if (pc.getLevel() >= 15) {
			value = pc.getLevel() / 15;
		}
		writeC(value);

		writeBit(240L);
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

		/****/
		writeC(0x80);
		writeC(0x02);
		writeC(0);

		writeC(0x88);
		writeC(0x02);
		/** 2016.11.26 MJ 앱센터 LFC **/
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if(instobj != null)
			writeC(instobj.getMarkStatus(pc));
		else if(pc.getClanid() == 290040001){//붉은기사단
			writeC(7); // 혈마크 번호			
		} else {
			writeC(0); // 혈마크 번호
		}

		writeC(0x98);
		writeC(0x02);
		writeC(0);
		/****/

		writeBit(256L);
		writeBit(0);//서버명

		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_OTHER_CHAR_PACKS;
	}

}



//package l1j.server.server.serverpackets;
//
//import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;
//
//import l1j.server.server.Opcodes;
//import l1j.server.server.model.Instance.L1PcInstance;
//
////Referenced classes of package l1j.server.server.serverpackets:
////ServerBasePacket, S_OtherCharPacks
//
//public class S_OtherCharPacks extends ServerBasePacket {
//
//	private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";
//	private static final int STATUS_POISON = 1;
//	private static final int STATUS_INVISIBLE = 2;
//	private static final int STATUS_PC = 4;
//	private static final int STATUS_FREEZE = 8;
//	private static final int STATUS_BRAVE = 16;
//	private static final int STATUS_ELFBRAVE = 32;
//	private static final int STATUS_FASTMOVABLE = 64;
//	private static final int STATUS_GHOST = 128;
//	private static final int BLOOD_LUST = 16;// 100
//	private static final int DANCING_BLADES = 16;// 댄싱
//
//	private byte[] _byte = null;
//
//	public S_OtherCharPacks(L1PcInstance pc) {
//		int status = STATUS_PC;
//
//		if (pc.getPoison() != null) {
//			if (pc.getPoison().getEffectId() == 1) {
//				status |= STATUS_POISON;
//			}
//		}
//		if (pc.isGhost()) {
//			status |= STATUS_GHOST;
//		}
//		if (pc.isInvisble()) {
//			status |= STATUS_INVISIBLE;
//		}
//		if (pc.isBrave()) {
//			status |= STATUS_BRAVE;
//		}
//		if (pc.isElfBrave()) {
//			status |= STATUS_BRAVE;
//			status |= STATUS_ELFBRAVE;
//		}
//		if (pc.isBlood_lust()) {
//			status |= BLOOD_LUST;
//		}
//		if (pc.isDancingBlades()) {
//			status |= DANCING_BLADES;
//		}
//		if (pc.isFastMovable() || pc.isFruit()) {// 유그드라 추가 변경1/19
//			status |= STATUS_FASTMOVABLE;
//		}
//		if (pc.isParalyzed()) {
//			status |= STATUS_FREEZE;
//		}
//
//		// int addbyte = 0;
//		// int addbyte1 = 1;
//
//		writeC(Opcodes.S_PUT_OBJECT);
//		writeH(pc.getX());
//		writeH(pc.getY());
//		writeD(pc.getId());
//		if (pc.isDead()) {
//			writeH(pc.getTempCharGfxAtDead());
//		} else if (pc.isPrivateShop()) {
//			if (pc.상점변신 != 0)
//				writeH(pc.상점변신);
//		} else {
//			writeH(pc.getTempCharGfx());
//		}
//		if (pc.isDead()) {
//			writeC(pc.getStatus());
//		} else if (pc.isPrivateShop()) {
//			writeC(70);
//		} else {
//			writeC(pc.getCurrentWeapon());
//		}
//		writeC(pc.getHeading());
//		// writeC(0); // makes char invis (0x01), cannot move. spells display
//		writeC(pc.getLight().getChaLightSize());
//		writeC(pc.getMoveSpeed());
//		writeD(0x0000); // exp
//		// writeC(0x00);
//		writeH(pc.getLawful());
//		// writeS(pc.getName());
//		if (pc.getHuntCount() == 0) {
//			writeS(pc.getName());
//		} else if (pc.getHuntCount() == 1) {
//			writeS(pc.getName() + "\\aA[1단계]\\fe 수배중");
//		} else if (pc.getHuntCount() == 2) {
//			writeS(pc.getName() + "\\aA[2단계]\\fe 수배중");
//		} else if (pc.getHuntCount() == 3) {
//			writeS(pc.getName() + "\\aG[3단계]\\fe 수배중");
//		} else if (pc.getHuntCount() == 4) {
//			writeS(pc.getName() + "\\aG[3단계]\\fe 수배중");
//		}
//		writeS(pc.getTitle());
//		writeC(status);
//		writeD(pc.getClanid() > 0 ? pc.getClan().getEmblemId() : 0);
//		writeS(pc.getClanname()); // 크란명
//		writeS(null); // 펫호팅?
//		writeC(0);
//		/*
//		 * if(pc.is_isInParty()) // 파티중 { writeC(100 * pc.get_currentHp() /
//		 * pc.get_maxHp()); } else { writeC(0xFF); }
//		 */
//
//		writeC(0xFF);
//		writeC(pc.hasSkillEffect(STATUS_DRAGON_PEARL) ? 0x08 : 0x00); // 타르쿡크
//																		// 거리(대로)
//		writeC(0); // PC = 0, Mon = Lv
//		writeC(0);
//		writeC(0xFF);
//		writeC(0xFF);
//		writeC(0x00);
//		// by.lins
//		int value = 0;
//		if (pc.getLevel() >= 80) {
//			value = 11;
//		} else if (pc.getLevel() >= 55) {
//			value = (pc.getLevel() - 25) / 5;
//		} else if (pc.getLevel() >= 52) {
//			value = 5;
//		} else if (pc.getLevel() >= 50) {
//			value = 4;
//		} else if (pc.getLevel() >= 15) {
//			value = pc.getLevel() / 15;
//		}
//		writeC(value);
//		writeH(0);
//	}
//
//	@Override
//	public byte[] getContent() {
//		if (_byte == null) {
//			_byte = getBytes();
//		}
//		return _byte;
//	}
//
//	@Override
//	public String getType() {
//		return S_OTHER_CHAR_PACKS;
//	}
//
//}
