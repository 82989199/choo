package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ShowAutoInformation extends ServerBasePacket {
	private static final String S_ShowAutoInformation = "[S] S_SHOW_AUTOINFORMATION";
	public S_ShowAutoInformation(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(0);
		writeS("자동사냥");
		writeS("사용 설명서");
		writeS("");

		StringBuffer type1 = new StringBuffer();		
		String type1Potion = "   물약 : 체력 회복제(500개)\r\n";
		String type2Potion = "   물약 : 고급 체력 회복제(500개)\r\n";		
		String etcItem = "  주문서 : 순간이동 (300장)\r\n" +
				"  주문서 : 기란마을 귀환(5장)\r\n" +
				"  주문서 : 변신 주문서(20장)";

		if(pc.isCrown()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   용기 : 악마의 피(30개)\r\n");
		}else if(pc.isKnight()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   용기 : 농축 용기(5개)\r\n");
		}else if(pc.is전사()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   용기 : 농축 용기(5개)\r\n");		
			type1.append("   용기 : 결정체(2000)개\r\n");	
		}else if(pc.isDragonknight()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   재료 : 각인의 뼈조각(100개)\r\n");
		}else if(pc.isElf()) {
			if(pc.getWeapon() == null || pc.getWeapon().getItem().getType1() != 20) {
				type1.append("   촐기 : 강촐(5개)\r\n");
				type1.append("   재료 : 정령옥(100개)\r\n");
				type1.append("   재료 : 엘븐와퍼(30개)\r\n");
			}else {
				type1.append("   촐기 : 강촐(5개)\r\n");
				type1.append("   용기 : 눙축 집중(5개)\r\n");
				type1.append("   화살 : 은 화살(3000발)\r\n");
			} 
		}else if(pc.isDarkelf()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   재료 : 흑요석(100개)\r\n");
		}else if(pc.isWizard()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   보조 : 농축 지혜(3개)\r\n");
			type1.append("   보조 : 농축 마력(3개)\r\n");
			type1.append("   재료 : 마력의 돌(100개)\r\n");
		}else if(pc.isBlackwizard()) {
			type1.append("   촐기 : 강촐(5개)\r\n");
			type1.append("   보조 : 농축 지혜(3개)\r\n");
			type1.append("   재료 : 유그드라(300개)\r\n");			
		}

		writeS("타입 1.\r\n" +
				type1Potion +
				type1.toString()  +
				etcItem + "\r\n" +
				"타입 2.\r\n" +
				type2Potion +
				type1.toString()+
				etcItem);//내용
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ShowAutoInformation;
	}
}
