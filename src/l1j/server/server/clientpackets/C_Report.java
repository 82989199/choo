package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_케릭터생성;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class C_Report extends ClientBasePacket {

	private static final String C_REPORT = "[C] C_Report";

	public static final int DragonMenu = 0x06;

	public static final int MINI_MAP_SEND = 0x0b;
	/**몬스터킬*/
	public static final int MonsterKill = 0x2c;
	/** 홈페이지 연동 아이콘 **/
	//	public static final int HTTP = 0x13;
	public static final int BOOKMARK_SAVE = 0x22;
	public static final int BOOKMARK_COLOR = 0x27;
	public static final int BOOKMARK_LOADING_SAVE = 0x28;
	public static final int EMBLEM = 0x2e; //문장주시
	public static final int TELPORT = 0x30; // 마을텔레포트
	public static final int 케릭터생성 = 0x2b; //43
	//public static final int 페어리 = 0x37;
	public static final int 파워북검색 = 0x13;
	public static final int 상인찾기 = 0x31;
	public static final int 상점개설횟수 = 0x39;
	public static final int 자동신고 = 0x00;
	public static final int 시세검색 = 0xff;
	public static final int 케릭터비번생성 = 0x0e;
	public static final int 케릭터비번변경 = 0x10;
	public static final int 케릭터비번인증 = 0x11;
	
	public static final int CHARNAME_CHANGED = 0x1A;
	
	public C_Report(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int type = readC();
		L1PcInstance pc = client.getActiveChar();
		if(pc == null){
			switch(type){
			case 케릭터생성:
				if(Config.CHAR_PASSWORD && (client.getAccount().getCPW() == null || client.getAccount().getCPW().equalsIgnoreCase(""))){
					client.sendPacket(new S_CharPass(S_CharPass._비번생성창));
					return;
				}
			case 케릭터비번생성:
			case 케릭터비번변경:
			case 케릭터비번인증:
				break;				
			default:
				return;
			}
		}
		
		switch(type){
		case 케릭터비번생성: {
			if(!Config.CHAR_PASSWORD)
				return;
			
			String password = readSecondPassword();
//			System.out.println(password);
			/** 잘못된 비밀번호 포맷일 경우 킥 **/
			if(password == null){
				System.out.println("kick1");
				client.kick();
				return;
			}
					
			/** 2차 비번이 이미 있는 계정이 또 생성 패킷 보냈을 경우 킥. **/
			if (client.getAccount().getCPW() != null && !client.getAccount().getCPW().equalsIgnoreCase("")) {
				client.kick();
				return;
			} else {
				client.getAccount().setCPW(password);
				client.getAccount().UpdateCharPassword(password);
				client.sendPacket(new S_CharPass(S_CharPass._비번생성완료창));
			}
			//String password = readSecondPassword();
			//String pw = readS();
			/*
			String pw = readSecondPassword();
			System.out.println("2차비번생성 : " + pw);
			
			if (client.getAccount().getCPW() != null) {
				return;
			} else {
				client.getAccount().setCPW(pw);
				client.getAccount().UpdateCharPassword(pw);
				client.sendPacket(new S_CharPass(S_CharPass._비번생성완료창));
			}*/
		}
			break;
		case 케릭터비번변경: {
			if(!Config.CHAR_PASSWORD)
				return;
			
			String password = readSecondPassword();
			readC();	// is null
			String modPassword = readSecondPassword();

			/** 알 수 없는 포맷 킥. **/
			if(password == null || modPassword == null){
				client.kick();
				return;
			}
			
			// 비번이 없는데 어떻게 변경을 ?? 이것도 공격의심.
			if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
				return;
			} else {
				if (client.getAccount().getCPW().equals(password)) {
					client.getAccount().setCPW(modPassword);
					client.getAccount().UpdateCharPassword(modPassword);
					client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, true));
				} else {
					client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, false));
				}
			}
			/*
			String password = readSecondPassword();
			readC(); // is null
			 String modPassword = readSecondPassword();
			 if(client.getAccount().getCPW() == null)
				 return;
			 
			 if(client.getAccount().getCPW().equals(password)){
				 client.getAccount().setCPW(modPassword);
				 client.getAccount().UpdateCharPassword(modPassword);
				 client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, true));
			 }else
				 client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, false));
			 */
			 
			/*
			String npw = readS();
			String pw = readS();
			//System.out.println("2차비번변경 NEW : " + npw + " / OLD : " + pw);

			if (client.getAccount().getCPW() == null) {
				return;
			} else {
				if (client.getAccount().getCPW().equals(pw)) {
					// client.getAccount().setcpwok(true);
					client.getAccount().setCPW(npw);
					client.getAccount().UpdateCharPassword(npw);
					client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, true));
				} else {
					client.sendPacket(new S_CharPass(S_CharPass._비번변경답변, false));
				}
			}*/

			break;
		}
		case 케릭터비번인증: {
			if(!Config.CHAR_PASSWORD)
				return;
			
			String password = readSecondPassword();
			/** 알 수 없는 포맷 **/
			if(password == null){
				client.kick();
				return;
			}
			
			if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
				return;
			}
			
			if (client.getAccount().getCPW().equals(password)) {
				client.reset_second_password_failure_count();
				if (client.getAccount().getwaitpacket() != null) {
					int op = client.getAccount().getwaitpacket()[0] & 0xff;
					if(op  == Opcodes.C_ENTER_WORLD)
						new C_LoginToServer(client.getAccount().getwaitpacket(), client);
					else if(op == Opcodes.C_DELETE_CHARACTER)
						new C_DeleteChar(client.getAccount().getwaitpacket(), client);
					else
						System.out.println("invalid wait packet : " + op);
				}
			} else {
				int failure_count = client.inc_second_password_failure_count();
				if(failure_count >= Config.CHAR_PASSWORD_MAXIMUM_FAILCOUNT){
					client.kick();
					client.close();
					return;
				}
				client.sendPacket(S_CharPass.do_fail_password(failure_count, Config.CHAR_PASSWORD_MAXIMUM_FAILCOUNT));
			}
		}
			break;
		case 케릭터생성: {
			client.sendPacket(new S_케릭터생성());
			/*
			if (!client.getAccount().iscpwok()) {
				if (client.getAccount().getCPW() == null) {
					client.sendPacket(new S_CharPass(S_CharPass._비번생성창));
				} else {
					client.getAccount().setwaitpacket(abyte0);
					client.sendPacket(new S_CharPass(S_CharPass._비번입력창));
				}
			} else {
				//client.sendPacket(new S_NewCharSelect());
				client.sendPacket(new S_케릭터생성());
			}*/
		}
	
			break;
		case 자동신고: {
			int objid = readD(); // 케릭터 오브젝트
			L1Object obj = L1World.getInstance().findObject(objid);//오류부근
			if(!(obj instanceof L1PcInstance)){
				return;
			}
			L1PcInstance target = (L1PcInstance) obj;//오류부근
			if (target == null || target.isGm()) {
				pc.sendPackets(new S_SystemMessage("신고 대상이 존재하지않습니다."));
				return;
			}
			if (!pc.isReport()) {
				pc.sendPackets(new S_ServerMessage(1021)); // 잠시후 다시 신고 해주세요.
				return;
			}
			ReportTable rt = ReportTable.getInstance();
			if (rt.isReport(target.getName())) {
				pc.sendPackets(new S_ServerMessage(1020)); // 이미 등록 되었습니다.
				return;
			}
			Timestamp date = new Timestamp(System.currentTimeMillis());
			rt.reportUpdate(target.getName(), pc.getName(), date);
			pc.sendPackets(new S_ServerMessage(1019)); // 등록되었습니다.
			pc.startReportDeley();
		}
			break;	
		case 시세검색:
			int searchtype = readC();
			String itemname = readS();
			int shopitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(itemname);
//			pc.sendPackets(new S_SystemMessage("아이템이름: "+ itemname));
			
			if (shopitemid == 0) {
				pc.sendPackets(new S_SystemMessage("아이템 이름을 검색할 수 없습니다. 검색어를 다시 입력해 주세요."));
				return;
			}

			break;
		case 상인찾기: {
			/** 2016.11.26 MJ 앱센터 LFC **/
			String name = readS();
			if (name == null)
				return;
				try {
					String[] rep = name.split("-");
					if(rep.length >= 2){
						if(rep[0].equalsIgnoreCase("LFC")){
							pc.sendPackets(".결투를 사용해주세요.");
							/*							if(rep[1].equalsIgnoreCase("R"))
								MJLFCCreator.registLfc(pc, Integer.parseInt(rep[2]));
							else
								MJLFCCreator.create(L1BoardPost.findByIdLfc(Integer.parseInt(rep[1])), pc);
								*/
							break;
						}
					}
					if (pc.getMapId() == 800) {
						Random rnd = new Random(System.nanoTime());
						L1PcInstance pn = L1World.getInstance().getPlayer(name);
						if (pn != null && pn.getMapId() == 800 && pn.isPrivateShop()) {
							/** 2016.12.01 MJ 앱센터 LFC **/
							pc.setFindMerchantId(pn.getId());
							pc.start_teleport(pn.getX() + CommonUtil.random(3)-1, pn.getY() + CommonUtil.random(3)-1, pn.getMapId(), 0, 169, false, false);
						} else {
							L1NpcShopInstance nn = L1World.getInstance().getShopNpc(name);
							
							if (nn != null && nn.getMapId() == 800 && nn.getState() == 1) {
								/** 2019.07.04 MJcodes 앱센터 LFC **/
								pc.setFindMerchantId(nn.getId());
								pc.start_teleport(nn.getX() + CommonUtil.random(3)-1, nn.getY() + CommonUtil.random(3)-1, nn.getMapId(), 0, 169, false, false);
								/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
							} else {
								pc.sendPackets(new S_SystemMessage("상인찾기 : 찾으시는 상인이 없습니다."), true);
							}
						}
						rnd = null;
					}
				} catch (Exception e) {
				}
		}
		break;
		case 상점개설횟수:
			if (pc.getNetConnection() == null || pc.getNetConnection().getAccount() == null)
				return;
			pc.sendPackets(new S_PacketBox(S_PacketBox.상점개설횟수, pc
					.getNetConnection().getAccount().Shop_open_count), true);
			break;
		case BOOKMARK_COLOR:
			int sizeColor = readD();
			int Numid;
			String name;
			Connection con = null;
			PreparedStatement pstm = null;
			try {
				if (sizeColor != 0) {
					con = L1DatabaseFactory.getInstance().getConnection();
				}
				for (int i = 0; i < sizeColor; i++) {
					Numid = readD();
					int id = 0;
					for (L1BookMark book : pc.getBookMarkArray()) {
						if (book.getNumId() == Numid) {
							id = book.getId();
						}
					}
					name = readS();
					name = name.replace("\\", "\\\\");
					pstm = con.prepareStatement("UPDATE character_teleport SET name='" + name + "' WHERE id='" + id + "'");
					pstm.execute();
				}
			} catch (SQLException e) {
			} finally {
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
			break;
		case BOOKMARK_SAVE:
			readC();
			int num;
			int size = pc._bookmarks.size();
			for (int i = 0; i < size; i++) {
				num = readC();
				pc._bookmarks.get(i).setTemp_id(num);
			}
			pc._speedbookmarks.clear();
			for (int i = 0; i < 5; i++) {
				num = readC();
				if (num == 255) return;
				if(pc._bookmarks.size()-1 < num) {
					System.out.println("book 마크사이즈에러 "+pc.getName() + " num=  "+ " size=  " +pc._bookmarks.size());
					return;
				}
				pc._bookmarks.get(num).setSpeed_id(i);
				pc._speedbookmarks.add(pc._bookmarks.get(num));
			}
			break;
		case BOOKMARK_LOADING_SAVE:{
			/*if (pc._speedbookmarks.size() == 0) {
				pc.sendPackets(new S_SystemMessage("빠른기억창에 기억을 등록시켜주세요."));
				return;
			}*/
			if(pc.getBookMarkSize() <= 0) {
				pc.sendPackets(new S_ServerMessage(2963));
				return;
			}
			int totalCount = pc.getInventory().getSize();
			if (pc.getInventory().getWeight100() > 82 || totalCount > 180) {
				pc.sendPackets(new S_SystemMessage("인벤이 가득차서 기억구슬을 생성할수없습니다."));
				return;
			}
			int itemId = readD();
			L1ItemInstance useItem = pc.getInventory().getItem(itemId);
			this.기억저장구슬(pc, useItem);


			/*L1ItemInstance SaveMarble = pc.getInventory().getItem(itemId);
			pc.getInventory().removeItem(SaveMarble);
			createNewItem(pc, pc.getId());
			pc.sendPackets(new S_ServerMessage(2920));// 기억 저장 구슬: 기억 장소 목록 저장 완료*/
		}
		break;
		case 파워북검색:
				//		Domain="http://g.lineage.power.plaync.com/wiki/샌드 웜";
			break;
		case MonsterKill:
			pc.setMonsterkill(0);
			break;
		case EMBLEM:
			if (pc.getClanRank() != 4 && pc.getClanRank() != 10) {
				return;
			}
			int emblemStatus = readC();
			L1Clan clan = pc.getClan();
			clan.setEmblemStatus(emblemStatus);
			ClanTable.getInstance().updateClan(clan);

			for (L1PcInstance member : clan.getOnlineClanMember()) {
				member.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, emblemStatus));
			}
			break;
		case TELPORT:
			int mapIndex = readH();
			int point = readH();
			int locx = 0;
			int locy = 0;
			if (mapIndex == 1) {//아덴
				if (point == 0) {
					locx = 34079 + (int) (Math.random() * 12);
					locy = 33136 + (int) (Math.random() * 15);
				} else if (point == 1) {
					locx = 33970 + (int) (Math.random() * 10);
					locy = 33243 + (int) (Math.random() * 14);
				} else if (point == 2) {
					locx = 33925 + (int) (Math.random() * 14);
					locy = 33351 + (int) (Math.random() * 9);
				}
			} else if (mapIndex == 2) {//글루딘
				if (point == 0) {
					locx = 32615 + (int) (Math.random() * 11);
					locy = 32719 + (int) (Math.random() * 7);
				} else if (point == 1) {
					locx = 32621 + (int) (Math.random() * 9);
					locy = 32788 + (int) (Math.random() * 13);
				}
			} else if (mapIndex == 3) {//기란마을
				if (point == 0) {
					locx = 33501 + (int) (Math.random() * 11);
					locy = 32765 + (int) (Math.random() * 9);
				} else if (point == 1) {
					locx = 33440 + (int) (Math.random() * 11);
					locy = 32784 + (int) (Math.random() * 11);
				} 
			} else if (mapIndex == 4) {//기란시장
				if (point == 0) {
					locx = 32844 + (int) (Math.random() * 2);
					locy = 32883 + (int) (Math.random() * 2);
				} else if (point == 1) {
					locx = 32801 + (int) (Math.random() * 2);
					locy = 32882 + (int) (Math.random() * 2);
				} else if (point == 2) {
					locx = 32756 + (int) (Math.random() * 2);
					locy = 32882 + (int) (Math.random() * 2);
				} else if (point == 3) {
					locx = 32743 + (int) (Math.random() * 2);
					locy = 32927 + (int) (Math.random() * 2);
				} else if (point == 4) {
					locx = 32740 + (int) (Math.random() * 2);
					locy = 32972 + (int) (Math.random() * 2);
				} else if (point == 5) {
					locx = 32800 + (int) (Math.random() * 2);
					locy = 32971 + (int) (Math.random() * 2);
				} else if (point == 6) {
					locx = 32844 + (int) (Math.random() * 2);
					locy = 32971 + (int) (Math.random() * 2);
				} else if (point == 7) {
					locx = 32846 + (int) (Math.random() * 2);
					locy = 32928 + (int) (Math.random() * 2);
				} else if (point == 8) {
					locx = 32797 + (int) (Math.random() * 2);
					locy = 32927 + (int) (Math.random() * 2);
				}
			}
			//			L1Teleport.teleport(pc, locx, locy, pc.getMapId(), pc.getHeading(), true);
			pc.start_teleport(locx, locy, pc.getMapId(), pc.getHeading(), 169, true, false);
			pc.sendPackets(new S_PacketBox(S_PacketBox.TOWN_TELEPORT, pc));
			break;

		case DragonMenu:
			break;
		case MINI_MAP_SEND:
			String targetName = null;
			int mapid = 0,
					x = 0,
					y = 0,
					Mid = 0;
			try {
				targetName = readS();
				mapid = readH();
				x = readH();
				y = readH();
				Mid = readH();
			} catch (Exception e) {
				return;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);
			if (target == null)
				pc.sendPackets(new S_ServerMessage(1782));
			else if (pc == target)
				pc.sendPackets(new S_ServerMessage(1785));
			else {
				target.sendPackets(new S_ServerMessage(1784, pc.getName()));
				target.sendPackets(new S_PacketBox(S_PacketBox.MINI_MAP_SEND, pc.getName(), mapid, x, y, Mid));
				pc.sendPackets(new S_ServerMessage(1783, target.getName()));
			}
			break;
		case CHARNAME_CHANGED:
			String sourceName = readS();
			String destinationName = readS();
			if(!client.is_shift_transfer()){
				if(!CharacterTable.getInstance().isContainNameList(sourceName) || client.getStatus().toInt() != MJClientStatus.CLNT_STS_CHANGENAME.toInt()){
					client.close();
					return;
				}
			}
			ServerBasePacket packet = S_ChangeCharName.doChangeCharName(pc, sourceName, destinationName);
			if(packet != null)
				client.sendPacket(packet);
			Account acc = client.getAccount();
			client.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
			if(acc.countCharacters() > 0)
				C_CommonClick.sendCharPacks(client);
			break;
		default:
			break;
		}
	}

	private void createNewItem(L1PcInstance pc, int i) {
		L1ItemInstance item = ItemTable.getInstance().createItem(7475);
		item.setCount(1);
		item.set_durability(i);
		item.setIdentified(true);
		if (item != null && pc != null) {
			if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.getInventory().updateItem(item, L1PcInventory.COL_DURABILITY);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		}
	}

	private void 기억저장구슬(L1PcInstance pc, L1ItemInstance useItem) {
		ArrayList<L1BookMark> books =  pc._bookmarks;		
		L1ItemInstance item = ItemTable.getInstance().createItem(700023);
		for(int i = 0; i < books.size(); i++) {
			L1ItemBookMark.addBookmark(pc, item, books.get(i));
		}
		pc.getInventory().storeItem(item);
		pc.getInventory().removeItem(useItem, 1);
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {// 메소드가 두개인데 하나사용하지않는건 주석하시는게
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance()
				.getInventory(pc.getX(), pc.getY(), pc.getMapId())
				.storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); 
			return true;
		} else {
			return false;
		}
	}

	private int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}
	
	private String readSecondPassword(){
		StringBuilder sb = new StringBuilder();
		int size = readC();
		if(size > 8)
			size = 8;
		int num = 0;
		for(int i=0; i<size; i++){
			num = readC();
			if(num < 0 || num > 9){
				return null;
			}
			sb.append(String.valueOf(num));
		}
		return sb.toString();
	}

	@Override
	public String getType() {
		return C_REPORT;
	}
}
