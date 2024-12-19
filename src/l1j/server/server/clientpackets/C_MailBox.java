package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_MailBox extends ClientBasePacket {

	private static final int TYPE_PRIVATE_MAIL = 0; // 개인 편지
	private static final int TYPE_BLOODPLEDGE_MAIL = 1; // 혈맹 편지
	private static final int TYPE_KEPT_MAIL = 2; // 보관 편지

	private static final int READ_PRIVATE_MAIL = 16; // 개인 편지읽기
	private static final int READ_BLOODPLEDGE_MAIL = 17; // 혈맹 편지읽기
	private static final int READ_KEPT_MAIL_ = 18; // 보관함 편지읽기

	private static final int WRITE_PRIVATE_MAIL = 32; // 개인 편지쓰기
	private static final int WRITE_BLOODPLEDGE_MAIL = 33; // 혈맹 편지쓰기

	private static final int DEL_PRIVATE_MAIL = 48; // 개인 편지삭제
	private static final int DEL_BLOODPLEDGE_MAIL = 49; // 혈맹 편지삭제
	private static final int DEL_KEPT_MAIL = 50; // 보관함 편지삭제

	private static final int TO_KEEP_MAIL = 64; // 편지 보관하기

	private static final int PRICE_PRIVATEMAIL = 50; // 개인 편지 가격

	private static final int DEL_PRIVATE_LIST_MAIL = 96; // 개인 편지리스트삭제
	private static final int DEL_BLOODPLEDGE_LIST_MAIL = 97; // 혈맹 편지리스트삭제
	private static final int DEL_KEEP_LIST = 98; // 보관편지 리스트삭제

	private static final int PRICE_BLOODPLEDGEMAIL = 1000; // 혈맹 편지 가격

	private static final int SIZE_PRIVATE_MAILBOX = 40; // 개인 편지함 크기
	private static final int SIZE_BLOODPLEDGE_MAILBOX = 80; // 혈맹 편지함 크기
	private static final int SIZE_KEPTMAIL_MAILBOX = 10; // 편지보관함 크기z

	private static final String C_MailBox = "[C] C_MailBox";

	public C_MailBox(byte abyte0[], GameClient client) {
		super(abyte0);
		int type = readC();
		
		if (client == null) {
			return;
		}
		
		L1PcInstance pc = client.getActiveChar();
		
		if (pc == null) {
			return;
		}
		
		switch (type) {
		case TYPE_PRIVATE_MAIL:
//			if (pc.isGm())
//				LetterList(pc, TYPE_PRIVATE_MAIL, 1000);
//			else
//				LetterList(pc, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			LetterList(pc, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			break;
		case TYPE_BLOODPLEDGE_MAIL:
			LetterList(pc, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
			break;
		case TYPE_KEPT_MAIL:
			LetterList(pc, TYPE_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
			break;
		case READ_PRIVATE_MAIL:
			ReadLetter(pc, READ_PRIVATE_MAIL, 0);
			break;
		case READ_BLOODPLEDGE_MAIL:
			ReadLetter(pc, READ_BLOODPLEDGE_MAIL, 0);
			break;
		case READ_KEPT_MAIL_:
			ReadLetter(pc, READ_KEPT_MAIL_, 0);
			break;
		case WRITE_PRIVATE_MAIL:
			WritePrivateMail(pc);
			break;
		case WRITE_BLOODPLEDGE_MAIL:
			WriteBloodPledgeMail(pc);
			break;
		case DEL_PRIVATE_MAIL:
			DeleteLetter(pc, DEL_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
			break;
		case DEL_BLOODPLEDGE_MAIL:
			DeleteLetter(pc, DEL_BLOODPLEDGE_MAIL, TYPE_BLOODPLEDGE_MAIL);
			break;
		case DEL_KEPT_MAIL:
			DeleteLetter(pc, DEL_KEPT_MAIL, TYPE_KEPT_MAIL);
			break;
		case TO_KEEP_MAIL:
			SaveLetter(pc, TO_KEEP_MAIL, TYPE_KEPT_MAIL);
			break;
		case DEL_PRIVATE_LIST_MAIL:
			DeleteLetter_List(pc, DEL_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			break;
		case DEL_BLOODPLEDGE_LIST_MAIL:
			DeleteLetter_List(pc, DEL_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
			break;
		case DEL_KEEP_LIST:
			DeleteLetter_List(pc, DEL_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
			break;
		default:
			// LetterList(pc,type);
		}
	}

	private void DeleteLetter_List(L1PcInstance pc, int deletetype, int type) {
		int delete_num = readD();
		for (int i = 0; i < delete_num; i++) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			pc.sendPackets(new S_LetterList(pc, deletetype, id, true));
		}
	}

	private boolean payMailCost(final L1PcInstance RECEIVER, final int PRICE) {
		int AdenaCnt = RECEIVER.getInventory().countItems(L1ItemId.ADENA);
		if (AdenaCnt < PRICE) {
			RECEIVER.sendPackets(new S_ServerMessage(189, ""));
			return false;
		}

		RECEIVER.getInventory().consumeItem(L1ItemId.ADENA, PRICE);
		return true;
	}

	private void WritePrivateMail(L1PcInstance sender, String receiver, String subject, String content, int paper){
		/** 폭탄 편지 방지 소스 **/
		long postdelaytime = System.currentTimeMillis() / 1000;
		if (!sender.isGm() && sender.getPostDelay() + 1 > postdelaytime) {
			long time = (sender.getPostDelay() + 1) - postdelaytime;
			sender.sendPackets(time + "초 후에 보낼수 있습니다.");
			System.out.println("" + sender.getName() + " 라는 유저가 폭탄편지를 보내려 합니다.");
			return;
		}
		/** 폭탄 편지 방지 소스 **/
		
		if(sender.getLevel() < Config.To_ChatLevel){
			sender.sendPackets(new S_SystemMessage("레벨"+Config.To_ChatLevel+" 이하는 편지를 보낼 수 없습니다."));
			return;
		}
		
		if(!payMailCost(sender, PRICE_PRIVATEMAIL))
			return;
		
		Timestamp dTime = new Timestamp(System.currentTimeMillis());
		if (!checkCountMail(sender, receiver, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX))
			return;

		L1PcInstance target = L1World.getInstance().getPlayer(receiver);
		if (target != null) {
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(target.getId());
			if (exList.contains(1, sender.getName())) {
				sender.sendPackets(new S_ServerMessage(3082));
				return;
			}
		}
		int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), receiver, TYPE_PRIVATE_MAIL, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			target.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject)); // 받는사람
			
			if(target.isGm()){
				if(GMCommands._sleepingMessage != null && !GMCommands._sleepingMessage.equalsIgnoreCase(""))
					WritePrivateMail(target, sender.getName(), GMCommands._sleepingTitle, GMCommands._sleepingMessage, paper);
			}
		}
		
		sender.setPostDelay(postdelaytime + 5);
	}
	
	private void WritePrivateMail(L1PcInstance sender) {
		int 		paper 			= readH(); // 편지지
		String 	receiverName 	= readS();
		String 	subject 			= readSS();
		String 	content 			= readSS();
		
		WritePrivateMail(sender, receiverName, subject, content, paper);
	}

	private void WriteBloodPledgeMail(L1PcInstance sender) {
		if (!payMailCost(sender, PRICE_BLOODPLEDGEMAIL))
			return;

		if(sender == null)
			return;
		
		/** 폭탄 편지 방지 소스 **/
		long postdelaytime = System.currentTimeMillis() / 1000;
		if (!sender.isGm() && sender.getPostDelay() + 1 > postdelaytime) {
			long time = (sender.getPostDelay() + 1) - postdelaytime;
			sender.sendPackets(time + "초 후에 보낼수 있습니다.");
//			System.out.println("" + sender.getName() + " 라는 새끼가 폭탄편지를 보내려 합니다.");
			return;
		}
		/** 폭탄 편지 방지 소스 **/
		
		int paper = readH(); // 편지지

		Timestamp dTime = new Timestamp(System.currentTimeMillis());
		String receiverName = readS();
		String subject = readSS();
		String content = readSS();

		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(receiverName.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}
		
		if(targetClan ==null){
			return;
		}
		
		String name;
		L1PcInstance target = null;
		CopyOnWriteArrayList<ClanMember> clanMemberList = targetClan.getClanMemberList();
		try {
			for (int i = 0, a = clanMemberList.size(); i < a; i++) {
				name = clanMemberList.get(i).name;
				target = L1World.getInstance().getPlayer(name);
				if (!checkCountMail(sender, name, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX))
					continue;				
				if (name.equalsIgnoreCase(sender.getName()))
					continue;
				int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), name, TYPE_BLOODPLEDGE_MAIL, subject, content);
			
				if (target != null && target.getOnlineStatus() != 0) {
					target.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_BLOODPLEDGE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject)); // 받는사람
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sender.setPostDelay(postdelaytime + 5);
	}
	
	private void DeleteLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().deleteLetter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, true));
	}
	
	private void ReadLetter(L1PcInstance pc, int type, int read) {
		int id = readD();
		LetterTable.getInstance().CheckLetter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, read));
	}

	private void LetterList(L1PcInstance pc, int type, int count) {
		pc.sendPackets(new S_LetterList(pc, type, count));
	}
	
	private void SaveLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().SaveLetter(id, letterType);
		pc.sendPackets(new S_LetterList(pc, type, id, true));
	}

	private boolean checkCountMail(L1PcInstance from, String to, int type, int max) {
		int cntMailInMailBox = LetterTable.getInstance().getLetterCount(to, type);
		if (cntMailInMailBox >= max) { // 편지함 만땅
			from.sendPackets(new S_SystemMessage(to + "님의 편지함이 가득차서, 새 편지를 보낼 수 없습니다."));
			return false;
		}
		return true;
	}

	@Override
	public String getType() {
		return C_MailBox;
	}
}
