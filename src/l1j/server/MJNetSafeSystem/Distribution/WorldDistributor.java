package l1j.server.MJNetSafeSystem.Distribution;

import static l1j.server.server.Opcodes.C_ACCEPT_XCHG;
import static l1j.server.server.Opcodes.C_ACTION;
import static l1j.server.server.Opcodes.C_ADD_BUDDY;
import static l1j.server.server.Opcodes.C_ADD_XCHG;
import static l1j.server.server.Opcodes.C_ALIVE;
import static l1j.server.server.Opcodes.C_ANSWER;
import static l1j.server.server.Opcodes.C_ASK_XCHG;
import static l1j.server.server.Opcodes.C_ATTACK;
import static l1j.server.server.Opcodes.C_ATTACK_CONTINUE;
import static l1j.server.server.Opcodes.C_BANISH_PARTY;
import static l1j.server.server.Opcodes.C_BAN_MEMBER;
import static l1j.server.server.Opcodes.C_BOARD_DELETE;
import static l1j.server.server.Opcodes.C_BOARD_LIST;
import static l1j.server.server.Opcodes.C_BOARD_READ;
import static l1j.server.server.Opcodes.C_BOARD_WRITE;
import static l1j.server.server.Opcodes.C_BOOKMARK;
import static l1j.server.server.Opcodes.C_BUYABLE_SPELL;
import static l1j.server.server.Opcodes.C_BUY_SELL;
import static l1j.server.server.Opcodes.C_BUY_SPELL;
import static l1j.server.server.Opcodes.C_CANCEL_XCHG;
import static l1j.server.server.Opcodes.C_CHANGE_ACCOUNTINFO;
import static l1j.server.server.Opcodes.C_CHANGE_CASTLE_SECURITY;
import static l1j.server.server.Opcodes.C_CHANGE_DIRECTION;
import static l1j.server.server.Opcodes.C_CHANNEL;
import static l1j.server.server.Opcodes.C_CHAT;
import static l1j.server.server.Opcodes.C_CHAT_PARTY_CONTROL;
import static l1j.server.server.Opcodes.C_CHECK_INVENTORY;
import static l1j.server.server.Opcodes.C_CHECK_PK;
import static l1j.server.server.Opcodes.C_CREATE_PLEDGE;
import static l1j.server.server.Opcodes.C_DEAD_RESTART;
import static l1j.server.server.Opcodes.C_DELETE_BOOKMARK;
import static l1j.server.server.Opcodes.C_DEPOSIT;
import static l1j.server.server.Opcodes.C_DESTROY_ITEM;
import static l1j.server.server.Opcodes.C_DIALOG;
import static l1j.server.server.Opcodes.C_DROP;
import static l1j.server.server.Opcodes.C_DUEL;
import static l1j.server.server.Opcodes.C_EMBLEM;
import static l1j.server.server.Opcodes.C_EXCHANGEABLE_SPELL;
import static l1j.server.server.Opcodes.C_EXCHANGE_SPELL;
import static l1j.server.server.Opcodes.C_EXIT_GHOST;
import static l1j.server.server.Opcodes.C_EXTENDED;
import static l1j.server.server.Opcodes.C_EXTENDED_PROTOBUF;
import static l1j.server.server.Opcodes.C_FAR_ATTACK;
import static l1j.server.server.Opcodes.C_FIX;
import static l1j.server.server.Opcodes.C_FIXABLE_ITEM;
import static l1j.server.server.Opcodes.C_GET;
import static l1j.server.server.Opcodes.C_GIVE;
import static l1j.server.server.Opcodes.C_GOTO_PORTAL;
import static l1j.server.server.Opcodes.C_HACTION;
import static l1j.server.server.Opcodes.C_HYPERTEXT_INPUT_RESULT;
import static l1j.server.server.Opcodes.C_INVITE_PARTY_TARGET;
import static l1j.server.server.Opcodes.C_JOIN_PLEDGE;
import static l1j.server.server.Opcodes.C_LEAVE_PARTY;
import static l1j.server.server.Opcodes.C_LEAVE_PLEDGE;
import static l1j.server.server.Opcodes.C_LOGOUT;
import static l1j.server.server.Opcodes.C_MAIL;
import static l1j.server.server.Opcodes.C_MARRIAGE;
import static l1j.server.server.Opcodes.C_MATCH_MAKING;
import static l1j.server.server.Opcodes.C_MOVE;
import static l1j.server.server.Opcodes.C_NPC_ITEM_CONTROL;
import static l1j.server.server.Opcodes.C_ONOFF;
import static l1j.server.server.Opcodes.C_OPEN;
import static l1j.server.server.Opcodes.C_PERSONAL_SHOP;
import static l1j.server.server.Opcodes.C_PLATE;
import static l1j.server.server.Opcodes.C_PLEDGE_WATCH;
import static l1j.server.server.Opcodes.C_QUERY_BUDDY;
import static l1j.server.server.Opcodes.C_QUERY_CASTLE_SECURITY;
import static l1j.server.server.Opcodes.C_QUERY_PERSONAL_SHOP;
import static l1j.server.server.Opcodes.C_QUIT;
import static l1j.server.server.Opcodes.C_RANK_CONTROL;
import static l1j.server.server.Opcodes.C_REMOVE_BUDDY;
import static l1j.server.server.Opcodes.C_RESTART;
import static l1j.server.server.Opcodes.C_RETURN_SUMMON;
import static l1j.server.server.Opcodes.C_SAVEIO;
import static l1j.server.server.Opcodes.C_SHUTDOWN;
import static l1j.server.server.Opcodes.C_SLAVE_CONTROL;
import static l1j.server.server.Opcodes.C_SUMMON;
import static l1j.server.server.Opcodes.C_TAX;
import static l1j.server.server.Opcodes.C_THROW;
import static l1j.server.server.Opcodes.C_TITLE;
import static l1j.server.server.Opcodes.C_UPLOAD_EMBLEM;
import static l1j.server.server.Opcodes.C_USE_ITEM;
import static l1j.server.server.Opcodes.C_USE_SPELL;
import static l1j.server.server.Opcodes.C_VOICE_CHAT;
import static l1j.server.server.Opcodes.C_WAR;
import static l1j.server.server.Opcodes.C_WAREHOUSE_CONTROL;
import static l1j.server.server.Opcodes.C_WHO;
import static l1j.server.server.Opcodes.C_WHO_PARTY;
import static l1j.server.server.Opcodes.C_WHO_PLEDGE;
import static l1j.server.server.Opcodes.C_WITHDRAW;

import l1j.server.Config;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.C_ActionUi;
import l1j.server.server.clientpackets.C_AddBookmark;
import l1j.server.server.clientpackets.C_AddBuddy;
import l1j.server.server.clientpackets.C_Amount;
import l1j.server.server.clientpackets.C_Attack;
import l1j.server.server.clientpackets.C_AttackNew;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.clientpackets.C_BanClan;
import l1j.server.server.clientpackets.C_BanParty;
import l1j.server.server.clientpackets.C_Board;
import l1j.server.server.clientpackets.C_BoardBack;
import l1j.server.server.clientpackets.C_BoardDelete;
import l1j.server.server.clientpackets.C_BoardRead;
import l1j.server.server.clientpackets.C_BoardWrite;
import l1j.server.server.clientpackets.C_Buddy;
import l1j.server.server.clientpackets.C_CallPlayer;
import l1j.server.server.clientpackets.C_ChangeHeading;
import l1j.server.server.clientpackets.C_CharcterConfig;
import l1j.server.server.clientpackets.C_Chat;
import l1j.server.server.clientpackets.C_ChatParty;
import l1j.server.server.clientpackets.C_CheckPK;
import l1j.server.server.clientpackets.C_Clan;
import l1j.server.server.clientpackets.C_ClanAttention;
import l1j.server.server.clientpackets.C_ClanMatching;
import l1j.server.server.clientpackets.C_Craft;
import l1j.server.server.clientpackets.C_CreateClan;
import l1j.server.server.clientpackets.C_CreateParty;
import l1j.server.server.clientpackets.C_DelBuddy;
import l1j.server.server.clientpackets.C_DeleteBookmark;
import l1j.server.server.clientpackets.C_DeleteInventoryItem;
import l1j.server.server.clientpackets.C_Deposit;
import l1j.server.server.clientpackets.C_Door;
import l1j.server.server.clientpackets.C_Drawal;
import l1j.server.server.clientpackets.C_DropItem;
import l1j.server.server.clientpackets.C_Emblem;
import l1j.server.server.clientpackets.C_EnterPortal;
import l1j.server.server.clientpackets.C_ExitGhost;
import l1j.server.server.clientpackets.C_ExtraCommand;
import l1j.server.server.clientpackets.C_Fight;
import l1j.server.server.clientpackets.C_FishClick;
import l1j.server.server.clientpackets.C_FixWeaponList;
import l1j.server.server.clientpackets.C_GiveItem;
import l1j.server.server.clientpackets.C_Horun;
import l1j.server.server.clientpackets.C_HorunOK;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.clientpackets.C_ItemUSe2;
import l1j.server.server.clientpackets.C_JoinClan;
import l1j.server.server.clientpackets.C_KeepALIVE;
import l1j.server.server.clientpackets.C_LeaveClan;
import l1j.server.server.clientpackets.C_LeaveParty;
import l1j.server.server.clientpackets.C_LoginToServerOK;
import l1j.server.server.clientpackets.C_LoginToServerWrap;
import l1j.server.server.clientpackets.C_MailBox;
import l1j.server.server.clientpackets.C_MoveChar;
import l1j.server.server.clientpackets.C_NPCAction;
import l1j.server.server.clientpackets.C_NPCTalk;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.clientpackets.C_Party;
import l1j.server.server.clientpackets.C_PetMenu;
import l1j.server.server.clientpackets.C_PickUpItem;
import l1j.server.server.clientpackets.C_Pledge;
import l1j.server.server.clientpackets.C_PledgeContent;
import l1j.server.server.clientpackets.C_Propose;
import l1j.server.server.clientpackets.C_Quit;
import l1j.server.server.clientpackets.C_Rank;
import l1j.server.server.clientpackets.C_Report;
import l1j.server.server.clientpackets.C_Restart;
import l1j.server.server.clientpackets.C_Result;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.C_ReturnToLogin;
import l1j.server.server.clientpackets.C_SecurityStatus;
import l1j.server.server.clientpackets.C_SecurityStatusSet;
import l1j.server.server.clientpackets.C_SelectList;
import l1j.server.server.clientpackets.C_SelectTarget;
import l1j.server.server.clientpackets.C_Shop;
import l1j.server.server.clientpackets.C_ShopList;
import l1j.server.server.clientpackets.C_SkillBuy;
import l1j.server.server.clientpackets.C_SkillBuyOK;
import l1j.server.server.clientpackets.C_TalkIslandQuest;
import l1j.server.server.clientpackets.C_TaxRate;
import l1j.server.server.clientpackets.C_Teleport;
import l1j.server.server.clientpackets.C_Title;
import l1j.server.server.clientpackets.C_Trade;
import l1j.server.server.clientpackets.C_TradeAddItem;
import l1j.server.server.clientpackets.C_TradeCancel;
import l1j.server.server.clientpackets.C_TradeOK;
import l1j.server.server.clientpackets.C_UsePetItem;
import l1j.server.server.clientpackets.C_UseSkill;
import l1j.server.server.clientpackets.C_War;
import l1j.server.server.clientpackets.C_WhPw;
import l1j.server.server.clientpackets.C_Who;
import l1j.server.server.clientpackets.C_아덴상점;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.utils.MJHexHelper;

public class WorldDistributor extends Distributor{

	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		ClientBasePacket 	cbp	= null;
		L1PcInstance pc = clnt.getActiveChar();
		if(Config.is_leaf){
		System.out.println(MJHexHelper.toString(data, data.length));
		}
		switch(op){
		case C_CHANNEL:
			return new C_Report(data, clnt);
		case C_EXTENDED_PROTOBUF:	
			if(MJEProtoMessages.existsProto(clnt, data))
				return null;
			
			//System.out.println(MJHexHelper.toString(data, data.length));
			cbp = new C_ActionUi(data, clnt);				
			cbp.clear();
			cbp = null;
			
			cbp = new C_Craft(data, clnt);
			if (pc != null && pc.getLevel() <= Config.QestLevel_End) {//원본80
				cbp.clear();
				cbp = null;
				return new C_TalkIslandQuest(data, clnt);
			}
			return cbp;
		case C_ATTACK: 
		case C_FAR_ATTACK: 
			return new C_Attack(data, clnt);
		case C_DIALOG: 
			return new C_NPCTalk(data, clnt);
		case C_USE_ITEM: 
			cbp = new C_ItemUSe(data, clnt);
			cbp.clear();
			cbp = null;
			return new C_ItemUSe2(data, clnt);
		case C_VOICE_CHAT: 
			return new C_ReturnStaus(data, clnt); 
		case C_PLEDGE_WATCH:
			return new C_ClanAttention(data, clnt);
		case C_SHUTDOWN:
			return new C_PledgeContent(data, clnt);
		case C_ATTACK_CONTINUE:
			return new C_AttackNew(data, clnt); 
		case C_EXTENDED:
			return new C_아덴상점(data, clnt); 
		case C_SAVEIO: 
			return new C_CharcterConfig(data, clnt); 
		case C_OPEN: 
			return new C_Door(data, clnt); 
		case C_TITLE: 
			return new C_Title(data, clnt); 
		case C_EMBLEM:
			return new C_Clan(data, clnt); 
		case C_MATCH_MAKING: 
			return new C_ClanMatching(data, clnt); 
		case C_BOARD_DELETE: 
			return new C_BoardDelete(data, clnt); 
		case C_WHO_PLEDGE: 
			return new C_Pledge(data, clnt); 
		case C_CHANGE_DIRECTION: 
			return new C_ChangeHeading(data, clnt); 
		case C_HACTION: 
			return new C_NPCAction(data, clnt); 
		case C_USE_SPELL:
			return new C_UseSkill(data, clnt); 
		case C_UPLOAD_EMBLEM: 
			return new C_Emblem(data, clnt); 
		case C_CANCEL_XCHG: 
			return new C_TradeCancel(data, clnt); 
		case C_BOOKMARK: 
			return new C_AddBookmark(data, clnt); 
		case C_CREATE_PLEDGE: 
			return new C_CreateClan(data, clnt); 
		case C_MARRIAGE: 
			return new C_Propose(data, clnt); 
		case C_BUYABLE_SPELL: 
			return new C_SkillBuy(data, clnt); 
		case C_BOARD_LIST: 
			return new C_BoardBack(data, clnt); 
		case C_PERSONAL_SHOP:
			return new C_Shop(data, clnt); 
		case C_BOARD_READ: 
			return new C_BoardRead(data, clnt); 
		case C_ASK_XCHG: 
			return new C_Trade(data, clnt); 
		case C_ALIVE: 
			return new C_KeepALIVE(data, clnt); 
		case C_ANSWER: 
			return new C_Attr(data, clnt); 
		case C_BUY_SELL:
			return new C_Result(data, clnt); 
		case C_DEPOSIT: 
			return new C_Deposit(data, clnt); 
		case C_WITHDRAW: 
			return new C_Drawal(data, clnt); 
		case C_ONOFF: 
			return new C_LoginToServerOK(data, clnt); 
		case C_BUY_SPELL: 
			return new C_SkillBuyOK(data, clnt); 
		case C_ADD_XCHG:
			return new C_TradeAddItem(data, clnt); 
		case C_ADD_BUDDY: 
			return new C_AddBuddy(data, clnt); 
		case C_LOGOUT: 
			return new C_ReturnToLogin(data, clnt); 
		case C_ACCEPT_XCHG:
			return new C_TradeOK(data, clnt);
		case C_CHECK_PK: 
			return new C_CheckPK(data, clnt); 
		case C_TAX: 
			return new C_TaxRate(data, clnt); 
		case C_RESTART:  
			return new C_NewCharSelect(data, clnt); 
		case C_DEAD_RESTART:  
			return new C_Restart(data, clnt); 
		case C_QUERY_BUDDY: 
			return new C_Buddy(data, clnt); 
		case C_DROP: 
			return new C_DropItem(data, clnt);
		case C_LEAVE_PARTY:
			return new C_LeaveParty(data, clnt); 
		case C_BAN_MEMBER: 
			return new C_BanClan(data, clnt); 
		case C_PLATE: 
			return new C_Board(data, clnt); 
		case C_DESTROY_ITEM:
			return new C_DeleteInventoryItem(data, clnt); 
		case C_WHO_PARTY: 
			return new C_Party(data, clnt); 
		case C_GET:
			return new C_PickUpItem(data, clnt); 
		case C_WHO: 
			return new C_Who(data, clnt); 
		case C_GIVE:
			return new C_GiveItem(data, clnt);
		case C_MOVE: 
			return new C_MoveChar(data, clnt);
		case C_DELETE_BOOKMARK:
			return new C_DeleteBookmark(data, clnt); 	
		case C_LEAVE_PLEDGE: 
			return new C_LeaveClan(data, clnt); 
		case C_BANISH_PARTY: 
			return new C_BanParty(data, clnt); 
		case C_REMOVE_BUDDY: 
			return new C_DelBuddy(data, clnt); 
		case C_WAR: 
			return new C_War(data, clnt); 
		case C_QUERY_PERSONAL_SHOP: 
			return new C_ShopList(data, clnt); 
		case C_JOIN_PLEDGE:
			return new C_JoinClan(data, clnt); 
		case C_ACTION: 
			return new C_ExtraCommand(data, clnt);
		case C_BOARD_WRITE: 
			return new C_BoardWrite(data, clnt);
		case C_INVITE_PARTY_TARGET: 
			return new C_CreateParty(data, clnt); 
		case C_GOTO_PORTAL:
			return new C_EnterPortal(data, clnt);
		case C_HYPERTEXT_INPUT_RESULT: 
			return new C_Amount(data, clnt); 
		case C_FIXABLE_ITEM:
			return new C_FixWeaponList(data, clnt);
		case C_FIX:
			return new C_SelectList(data, clnt);
		case C_EXIT_GHOST:
			return new C_ExitGhost(data, clnt);
		case C_CHANGE_ACCOUNTINFO: 
		case C_SUMMON:
			return new C_CallPlayer(data, clnt); 
		case C_THROW: 
			return new C_FishClick(data, clnt); 
		case C_SLAVE_CONTROL:
			return new C_SelectTarget(data, clnt);
		case C_CHECK_INVENTORY: 
			return new C_PetMenu(data, clnt); 
		case C_NPC_ITEM_CONTROL: 
			return new C_UsePetItem(data, clnt); 
		case C_RETURN_SUMMON: 
			return new C_Teleport(data, clnt); 
		case C_RANK_CONTROL: 
			return new C_Rank(data, clnt); 
		case C_CHAT:
			return new C_Chat(data, clnt);
		case C_CHAT_PARTY_CONTROL: 
			return new C_ChatParty(data, clnt);
		case C_DUEL: 
			return new C_Fight(data, clnt);
		case C_MAIL: 
			return new C_MailBox(data, clnt); 
		case C_WAREHOUSE_CONTROL: 
			return new C_WhPw(data, clnt); 
		case C_EXCHANGEABLE_SPELL: 
			return new C_Horun(data, clnt); 
		case C_EXCHANGE_SPELL: 
			return new C_HorunOK(data, clnt); 
		case C_QUERY_CASTLE_SECURITY: 
			return new C_SecurityStatus(data, clnt); 
		case C_CHANGE_CASTLE_SECURITY: 
			return new C_SecurityStatusSet(data, clnt); 
		case C_QUIT:
			return new C_Quit(data, clnt);
			
		case Opcodes.C_ENTER_WORLD:
			if(clnt.getStatus().toInt() == MJClientStatus.CLNT_STS_CHANGENAME.toInt() && pc != null){
				MJCopyMapObservable.getInstance().resetPosition(pc);
				MJRaidSpace.getInstance().getBackPc(pc);
				C_NewCharSelect.restartProcess(pc);
				
				if(clnt.getAccount().getCPW() != null && !clnt.isLoginRecord()){
					clnt.getAccount().setwaitpacket(data);
					if(SelectCharacterDistributor._passwordInputWindow == null)
						SelectCharacterDistributor._passwordInputWindow = new S_CharPass(S_CharPass._비번입력창);
					clnt.sendPacket(SelectCharacterDistributor._passwordInputWindow, false);
					return null;
				}
				return new C_LoginToServerWrap(data, clnt);
			}
		default:
			break;
		}
		toInvalidOp(clnt, op, data.length, "World", false);
		return null;
	}

}
