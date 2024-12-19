package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.HUNTER_BLESS;
import static l1j.server.server.model.skill.L1SkillId.BUYER_COOLTIME;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectCommandExecutor;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleCommandExecutor;
import javolution.util.FastTable;
import kr.cholong.PushItemSystem.PushItemController;
import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.Server;
import l1j.server.SpecialEventHandler;
import l1j.server.DogFight.MJDogFightCommandExecutor;
import l1j.server.GameSystem.AutoSystemController;
import l1j.server.GameSystem.MiniGame.HellController;
import l1j.server.GameSystem.MiniGame.LottoSystem;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCompanion.MJCompanionCommandExecutor;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
import l1j.server.MJInstanceSystem.Loader.MJInstanceLoadManager;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJItemEnchantSystem.MJItemEnchantSystemLoadManager;
import l1j.server.MJKDASystem.MJKDALoadManager;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET.CHART_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_EMBLEM_SWITCH_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.Payment.MJPaymentGmHandler;
import l1j.server.Payment.MJPaymentUserHandler;
import l1j.server.Stadium.StadiumManager;
import l1j.server.TowerOfDominance.DominanceDataLoader;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv1;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv10;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv11;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv2;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv3;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv4;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv5;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv6;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv7;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv8;
import l1j.server.TowerOfDominance.BossController.DominanceFloorLv9;
import l1j.server.TowerOfDominance.DominanceBoss;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.BuyerController;
import l1j.server.server.Controller.CrockController;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.Controller.잊섬Controller;
import l1j.server.server.command.L1Commands;
import l1j.server.server.command.executor.L1AllBuff;
import l1j.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.datatables.AuctionSystemTable;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.MJAlchemyProbabilityBox;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanRanking;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_Chainfo;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ChatMessageNoti;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_MARK_SEE;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Ping;
import l1j.server.server.serverpackets.S_Restart;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowCmd;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TestPacket;
import l1j.server.server.serverpackets.S_TestShop;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerMessage;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1Command;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.DeadLockDetector;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.MJFullStater;
import l1j.server.server.utils.MJProcessPlayer;
import l1j.server.server.utils.Message;
import l1j.server.server.utils.SQLUtil;

public class GMCommands {

	private static Logger _log = Logger.getLogger(GMCommands.class.getName());
	private static GMCommands _instance;
	public static L1PcInstance _gm;
	private static Random _random = new Random(System.nanoTime());
	public List<L1MonsterInstance> fieldbosslist = new ArrayList<L1MonsterInstance>();

	public static boolean 제작체크;

	private GMCommands() {
	}

	public static GMCommands getInstance() {
		if (_instance == null) {
			_instance = new GMCommands();
		}
		return _instance;
	}

	public boolean Stop = true;

	private String complementClassName(String className) {
		if (className.contains(".")) {
			return className;
		}
		return "l1j.server.server.command.executor." + className;
	}

	private boolean executeDatabaseCommandWithoutPermission(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return false;
			}
			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			exe.execute(pc, name, arg);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "error gm command", e);
		}
		return false;

	}

	private boolean executeDatabaseCommand(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return false;
			}
			if (pc.getAccessLevel() < command.getLevel()) {
				pc.sendPackets(new S_ServerMessage(74, "커멘드 " + name));
				// \f1%0은사용할 수없습니다.
				return true;
			}

			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			exe.execute(pc, name, arg);

			/** 파일로그저장 **/
			// ChatLogTable.getInstance().storeChat(pc, null, arg, 15);
			LoggerInstance.getInstance().addCommand(pc.getName() + ": " + name + " " + arg);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "error gm command", e);
		}
		return false;
	}

	public void handleCommandsWithoutPermission(L1PcInstance gm, String cmdLine) {
		if (gm.getNetConnection() == null || gm.getNetConnection().getAccount() == null
				|| gm.getNetConnection().getAccount().getAccessLevel() != 5048) {
			return;
		}

		StringTokenizer token = new StringTokenizer(cmdLine);
		// 최초의 공백까지가 커맨드, 그 이후는 공백을 단락으로 한 파라미터로서 취급한다
		String cmd = token.nextToken();
		String param = "";
		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();

		// 데이타베이스화 된 커멘드
		executeDatabaseCommandWithoutPermission(gm, cmd, param);
	}
	
	private static void test_lfc(L1PcInstance gm, String param){
		StringTokenizer token = new StringTokenizer(param);
		String name = token.nextToken();
		L1PcInstance pc = L1World.getInstance().findpc(name);
		if(pc == null){
			gm.sendPackets(String.format("%s님을 찾을 수 없습니다.", name));
		}
		L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", gm.getName()));
		MJLFCCreator.registLfc(gm, 3);
		gm.sendPackets(String.format("%s님에게 결투를 신청했습니다.", name));
		pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("%s님이 결투를 신청했습니다.", gm.getName()), bp.getId() + 1000, MJSurveyFactory.createLFCSurvey(), 30 * 1000));
	}
	
	
	public void handleCommands(L1PcInstance gm, String cmdLine) {
		StringTokenizer token = new StringTokenizer(cmdLine);
		// 최초의 공백까지가 커맨드, 그 이후는 공백을 단락으로 한 파라미터로서 취급한다
		String cmd = "";
		if (token.hasMoreTokens())
			cmd = token.nextToken();
		else
			cmd = cmdLine;
		String param = "";
		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();
		
		

		// 데이타베이스화 된 커멘드
		if (executeDatabaseCommand(gm, cmd, param)) {
			if (!cmd.equalsIgnoreCase(".")) {
				_lastCommands.put(gm.getId(), cmdLine);
			}
			return;
		}

		if (gm.getAccessLevel() < Config.GMCODE && cmd.equalsIgnoreCase("가라") == false) {
			gm.sendPackets(new S_ServerMessage(74, "커맨드 " + cmd));
			return;
		}

		/** 파일로그저장 **/
		LoggerInstance.getInstance().addCommand(gm.getName() + ": " + cmd + " " + param);
		// ChatLogTable.getInstance().storeChat(gm, null, cmdLine, 15);
		// GM에 개방하는 커맨드는 여기에 쓴다
		_gm = gm;

		// 삭제예정소스
		
		if (cmd.equalsIgnoreCase("잊섬시작")) {
			잊섬Controller.getInstance().isGmOpen = true;
			잊섬Controller.getInstance().set잊섬Start(true);
			잊섬Controller.getInstance().openTime = System.currentTimeMillis();
			L1World.getInstance().broadcastServerMessage(" 잠시후 잊혀진 섬 입장이 가능합니다.");
		} else if (cmd.equalsIgnoreCase("잊섬종료")) {
			잊섬Controller.getInstance().isGmOpen = false;
			잊섬Controller.getInstance().set잊섬Start(false);
			잊섬Controller.getInstance().end_pc_map();
			L1World.getInstance().broadcastServerMessage("잠시후 잊혀진 섬 입장이 불가능합니다.");
		} else if (cmd.equalsIgnoreCase("지옥시작")) {
			HellController.getInstance().isGmOpen3 = true;
			HellController.getInstance().setHellStart(true);
			L1World.getInstance().broadcastServerMessage("잠시후 [지옥] 문이 열립니다.");
		} else if (cmd.equalsIgnoreCase("지옥종료")) {
			HellController.getInstance().isGmOpen3 = false;
			HellController.getInstance().setHellStart(false);
			HellController.getInstance().TelePort5();
			L1World.getInstance().broadcastServerMessage("잠시후 [지옥] 문이 닫혔습니다.");
		} else if (cmd.equalsIgnoreCase("테베시작")) {
			CrockController.getInstance().gmopen = true;
			CrockController.getInstance().setGmOpen(true);
		} else if (cmd.equalsIgnoreCase("테베종료")) {
			CrockController.getInstance().gmopen = false;
			CrockController.getInstance().setGmOpen(false);
		}
		 
		switch (cmd) {
		case "아이피딜레이체크":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String status = st.nextToken();
				if (status.equalsIgnoreCase("켬")) {
					if (Config.IP_DELAY_CHECK_USE) {
						gm.sendPackets(new S_SystemMessage("이미 딜레이 체크 상태입니다."));
						return;
					}
					Config.IP_DELAY_CHECK_USE = true;
					gm.sendPackets(new S_SystemMessage("딜레이 체크 상태로 돌입합니다."));
				} else if (status.equalsIgnoreCase("끔")) {
					if (!Config.IP_DELAY_CHECK_USE) {
						gm.sendPackets(new S_SystemMessage("딜레이 체크 상태가 아닙니다."));
						return;
					}
					Config.IP_DELAY_CHECK_USE = false;
					gm.sendPackets(new S_SystemMessage("딜레이 체크 상태가 해지되고 정상적인 플레이가 가능합니다."));
				}
			} catch (Exception eee) {
				gm.sendPackets(new S_SystemMessage(".딜레이체크 [켬/끔] 으로 입력하세요."));
				gm.sendPackets(new S_SystemMessage("[ 중복 로그인 패킷 공격시에만 사용하세요 ]"));
			}
			break;
		case "투견":
			MJDogFightCommandExecutor.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "코드":
			MJPaymentGmHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "충전":
			MJPaymentUserHandler.do_execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "펫":
			MJCompanionCommandExecutor.exec(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "밤낮":			
			if(L1WorldMap.getInstance().getMap((short)54).isTeleportable()){
				L1WorldMap.getInstance().getMap((short)54).set_isTeleportable(false);
			}else {
				L1WorldMap.getInstance().getMap((short)54).set_isTeleportable(true);
			}
			break;
			
		case "캐시경험치":
			do_cache_exp(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "대항전":
			new MJShiftBattleCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "서버이동":
			if(Config.USE_SHIFT_SERVER)
				new MJShiftObjectCommandExecutor().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "test":{
			gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fY$27707"));
			gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fY$27708"));
//			gm.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
			/*
			int time = (86400000 * Config.TIME_N_BUFF);
			Timestamp ts = new Timestamp(System.currentTimeMillis() + time);
			
			System.out.println(time / 1000);
			System.out.println(ts.toString());
			*/
			//gm.get_companion().send_action(19);
//			gm.sendPackets(new S_AttackPacketForNpc(gm, gm.get_companion().getId(), 1));
//			gm.sendPackets(new S_AttackMissPacket(gm.get_companion(), gm.getId(), 1));
			/*L1Object obj = L1World.getInstance().findObject(271475752);
			SC_COMPANION_COMBAT_DATA_NOTI cnoti = SC_COMPANION_COMBAT_DATA_NOTI.newInstance();
			cnoti.set_melee_buff_dmg(1);
			cnoti.set_melee_cri_hit(1);
			cnoti.set_melee_cri_server_hit(1);
			cnoti.set_melee_dice_dmg(1);
			cnoti.set_melee_elemental_dmg(1);
			cnoti.set_melee_hit(1);
			cnoti.set_melee_last_dmg(1);
			cnoti.set_melee_stat_dmg(1);
			cnoti.set_melee_wild_dmg(1);
			cnoti.set_obj_id(271475701);
			cnoti.set_spell_buff_dmg(1);
			cnoti.set_spell_cri_hit(1);
			cnoti.set_spell_cri_server_hit(1);
			cnoti.set_spell_dice_dmg(1);
			cnoti.set_spell_dmg_multi(1);
			cnoti.set_spell_hit(1);
			cnoti.set_spell_id(1);
			cnoti.set_spell_last_dmg(1);
			cnoti.set_spell_server_hit(1);
			cnoti.set_spell_stat_dmg(1);
			cnoti.set_spell_wild_dmg(1);
			cnoti.set_total_dmg(1);
			gm.sendPackets(cnoti, MJEProtoMessages.SC_COMPANION_COMBAT_DATA_NOTI, true);
			*/
			
			/*
			for(L1Object obj : L1World.getInstance().getVisibleObjects(gm, 15)){
				if(obj == null || !(obj instanceof L1NpcInstance))
					continue;
				
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.broadcastPacket(new S_NpcChatPacket(npc, "test", 2));
			}*/
			
//			BugRaceController.getInstance()._is_downs[0] = true;
//			BugRaceController.getInstance()._is_downs[1] = true;
//			BugRaceController.getInstance()._is_downs[2] = true;
//			BugRaceController.getInstance()._is_downs[3] = true;
//			BugRaceController.getInstance()._is_downs[4] = true;
			/*
			final L1PcInstance p = gm;
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run(){
					try{
						while(p != null && p.getNetConnection() != null){
							additem.beginnerItem(p, 4100160, 1, 0, 129, 0, true);
							if(p.getInventory().getItems().size() > 170)
							{
								for (L1ItemInstance item : p.getInventory().getItems()) {
									if (!item.isEquipped()) {
										p.getInventory().removeItem(item);
									}
								}
								p.getInventory().storeItem(3000035, 400);
								MJClientStatus.TestMillis = System.currentTimeMillis();
							}
						}						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});*/
			
			/*
			L1ItemInstance item = gm.getInventory().findItemId(40100);
			int id = item.getId();
			byte[] buff = new byte[]{ Opcodes.S_CHANGE_ITEM_USE, (byte)(id & 0xff), (byte)((id >> 8) & 0xff), (byte)((id >> 16) & 0xff), (byte)((id >> 24) & 0xff), 29, 29 };
			S_BuilderPacket sb = new S_BuilderPacket(buff.length, buff);
			gm.sendPackets(sb);*/
			
			/*
			SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
			noti.add_item_info(ItemInfo.newInstance(item, 0));
			noti.set_on_start(false);
			noti.set_owner_oid(gm.getId());
			gm.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);*/
			/*
			for(L1NpcInstance npc : gm.getPetList().values()){
				SC_COMPANION_STATUS_NOTI noti = SC_COMPANION_STATUS_NOTI.newInstance();
				noti.set_ac(gm.getAc());
				noti.set_attackdelay_reduce(1D);
				noti.set_base_con(gm.getAbility().getCon());
				noti.set_base_int(gm.getAbility().getInt());
				noti.set_base_max_hp(gm.getMaxMp());
				noti.set_base_str(gm.getAbility().getStr());
				noti.set_bonus_con(1);
				noti.set_bonus_int(1);
				noti.set_bonus_str(1);
				noti.set_class_id(11);
				noti.set_elixir_use_count(0);
				noti.set_exp(gm.getExp());
				noti.set_friend_ship_guage(1);
				noti.set_friend_ship_marble(1);
				noti.set_hp(gm.getCurrentHp());
				noti.set_level(gm.getLevel());
				noti.set_minus_exp_penalty(true);
				noti.set_movedelay_reduce(1D);
				noti.set_mr(gm.getMr());
				try {
					noti.set_name(npc.getName().getBytes("MS949"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				noti.set_obj_id(npc.getId());
				noti.set_pvp_dmg_ratio(1);
				noti.set_remain_stats(11);
				noti.set_temp_con(15);
				noti.set_temp_int(15);
				noti.set_temp_max_hp(gm.getMaxHp());
				noti.set_temp_str(15);
				gm.sendPackets(noti,MJEProtoMessages.SC_COMPANION_STATUS_NOTI, true);
			}
			*/
			
			/*
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run(){
					int[] actions = new int[]{1,2,18,19,30};
					for(int i=0; i<100; ++i){
						gm.send_action(actions[MJRnd.next(actions.length)]);
					}
				}
			});*/
			/*
			mapping = MAPPING.newInstance();
			mapping.set_server_no(26);
			mapping.set_team_id(26);
			info.add_mapping_info(mapping);


			mapping = MAPPING.newInstance();
			mapping.set_server_no(27);
			mapping.set_team_id(27);
			info.add_mapping_info(mapping);

			mapping = MAPPING.newInstance();
			mapping.set_server_no(28);
			mapping.set_team_id(28);
			info.add_mapping_info(mapping);


			mapping = MAPPING.newInstance();
			mapping.set_server_no(29);
			mapping.set_team_id(29);
			info.add_mapping_info(mapping);

			mapping = MAPPING.newInstance();
			mapping.set_server_no(30);
			mapping.set_team_id(30);
			info.add_mapping_info(mapping);*/
			/*
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
				SC_TEAM_ID_SERVER_NO_MAPPING_INFO info = SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance();
				MAPPING mapping = MAPPING.newInstance();
				mapping.set_server_no(1);
				mapping.set_team_id(24);
				info.add_mapping_info(mapping);
				
				mapping = MAPPING.newInstance();
				mapping.set_server_no(2);
				mapping.set_team_id(25);
				info.add_mapping_info(mapping);
				
				mapping = MAPPING.newInstance();
				mapping.set_server_no(3);
				mapping.set_team_id(26);
				info.add_mapping_info(mapping);
				pc.sendPackets(info, MJEProtoMessages.SC_TEAM_ID_SERVER_NO_MAPPING_INFO, false);
			}*/
			/*
			gm.curePoison();
			gm.sendPackets(new S_DoActionGFX(gm.getId(), ActionCodes.ACTION_Idle));
			gm.broadcastPacket(new S_DoActionGFX(gm.getId(), ActionCodes.ACTION_Idle));
			Broadcaster.broadcastPacket(gm, new S_CharVisualUpdate(gm));
			L1PolyMorph.undoPolyPrivateShop(gm);*/
			//gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false));
//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false)); 
//			L1CurseParalysis.curse(gm, 10000, 16000);
//			L1ParalysisPoison.doInfection(gm, 9999);
//			gm.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false)); 
			/*
			SC_HUNTING_GUIDE_BOOKMARK_NOTI noti = SC_HUNTING_GUIDE_BOOKMARK_NOTI.newInstance();
			HUNTING_GUIDE_BOOKMARK_DATA data = HUNTING_GUIDE_BOOKMARK_DATA.newInstance();
			data.set_desc("hello");
			data.set_icon(3030);
			data.set_level(70);
			data.set_mapNo(4);
			data.set_seq(1);
			data.set_strIndex("0");
			data.set_x(32767);
			data.set_y(32767);
			noti.add_bookmarks(data);
			gm.sendPackets(noti, Integer.parseInt(param));*/
			break;
		}
		case "test1":{
			
			
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
				SC_TEAM_EMBLEM_SWITCH_NOTI noti = SC_TEAM_EMBLEM_SWITCH_NOTI.newInstance();
				noti.set_is_turn_on(true);
				
				SC_CHANGE_TEAM_NOTI_PACKET pnoti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
				pnoti.set_object_id(gm.getId());
				pnoti.set_object_team_id(4);
				pc.sendPackets(noti, MJEProtoMessages.SC_TEAM_EMBLEM_SWITCH_NOTI, false);
				pc.sendPackets(pnoti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, false);
			}
			/*SC_THEBE_CAPTURE_INFO_NOTI_PACKET noti = SC_THEBE_CAPTURE_INFO_NOTI_PACKET.newInstance();
			CapturePointT cpt = CapturePointT.newInstance();
			cpt.set_capture_point(1);
			cpt.set_homeserverno(1);
			cpt.set_team_id(4);
			noti.add_points(cpt);
			cpt = CapturePointT.newInstance();
			cpt.set_capture_point(2);
			cpt.set_homeserverno(2);
			cpt.set_team_id(5);
			noti.add_points(cpt);
			cpt = CapturePointT.newInstance();
			cpt.set_capture_point(3);
			cpt.set_homeserverno(3);
			cpt.set_team_id(6);
			noti.set_remain_time_for_next_capture_event(10);
			noti.add_points(cpt);
			gm.sendPackets(noti, MJEProtoMessages.SC_THEBE_CAPTURE_INFO_NOTI_PACKET, true);*/
			break;
		}
		case "test2":{
			SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
			noti.set_object_id(gm.getId());
			noti.set_object_team_id(Integer.parseInt(param));
			gm.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, true);
			break;
		}
		case "test3":{
			SC_BASECAMP_CHART_NOTI_PACKET noti = SC_BASECAMP_CHART_NOTI_PACKET.newInstance();
			noti.set_team_points(1000000);	
			noti.set_winner_team_id(1);
			for(int i=1; i<=10; ++i){
				CHART_INFO cInfo = CHART_INFO.newInstance();
				cInfo.set_id(i);
				cInfo.set_user_name(String.format("men%d", i));
				cInfo.set_user_points(i * 10000);
				noti.add_charts(cInfo);
			}
			gm.sendPackets(noti, MJEProtoMessages.SC_BASECAMP_CHART_NOTI_PACKET, true);
			break;
		}
		case "서버이전":
			do_shift_server(gm, param);
			break;
		case "경기장":
			StadiumManager.getInstance().open_stadium(gm, param);
			break;
		case "경기종료":
			StadiumManager.getInstance().quit_stadium(gm, param);
			break;
		case "결투":
			test_lfc(gm, param);
			break;
		case "선물지급":
			present(gm, param);
			break;
		case "인형박스":
			MJAlchemyProbabilityBox.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "도움말":
			showHelp(gm);
			break;
		case "혈맹정보":
			searchclaner(gm, param);
			break;
		case "지배보스":
			DominanceBoss(gm, param);
			break;
		case "케릭검사":
			chainfo(gm, param);
			break;
		case "자동트리":
			gm.isAutoTreeple = true;
			break;
		case "고정버프":
			befixed(gm, param);
			break;
		case "케릭삭제":
			standBy77(gm, param);
			break;
		case "기운축복":
			Blessleaf(gm, param);
			break;
		case "무기인챈성공":
			EnchantWeaponSuccess(gm, param);
			break;
		case "방어인챈성공":
			EnchantArmorSuccess(gm, param);
			break;
		case "에르자베":
			ErzabeBox(gm, param);
			break;
		case "샌드웜":
			SandwormBox(gm, param);
			break;
		case "계정검사":
			AccountCheck(gm, param);
			break;
		case "계정확인":
			AccountCheck1(gm, param);
			break;
		case "진데스진레":
			Jindeath(gm);
			break;
		case "진데스살키":
			Jindeath1(gm);
			break;
		case "진데스진싸":
			Jindeath2(gm);
			break;
		case "진데스제로스":
			Jindeath3(gm);
			break;
		case "진데스데불":
			Jindeath4(gm);
			break;
		case "진데스론론":
			Jindeath5(gm);
			break;
		case "진데스포효":
			Jindeath6(gm);
			break;
		case "진데스섬체":
			Jindeath7(gm);
			break;
		case "진데스커검":
			Jindeath8(gm);
			break;
		case "진데스태풍":
			Jindeath9(gm);
			break;
		case "진데스악몽":
			Jindeath10(gm);
			break;
		case "진데스나발":
			Jindeath11(gm);
			break;
		case "데페작업":
			jakjak(gm);
			break;
		case "아머작업":
			jakjak2(gm);
			break;
		case "카배작업":
			jakjak3(gm);
			break;
		case "데몬작업":
			dolldemon(gm);
			break;
		case "얼녀작업":
			doice(gm);
			break;
		case "타락작업":
			dolltarak(gm);
			break;
		case "데스작업":
			dolldeath(gm);
			break;
		case "룬스톤카배":
			Stone(gm);
			break;
		case "룬스톤디스":
			Stone1(gm);
			break;
		case "룬스톤데페":
			Stone2(gm);
			break;
		case "룬스톤아머":
			Stone3(gm);
			break;
		case "보스":
			addEventBoss(gm, param);
			break;
		case "판매삭제":
			AuctionSystemTable.getInstance().deleteTopic(param);
			break;
		case "판매":
			countR1(gm, param);
			break;
		case "구매":
			countR2(gm, param);
			break;
		case "판매취소":
			countR3(gm, param);
			break;
		case "판매완료":
			countR4(gm, param);
			break;
		case "구매취소":
			countR5(gm, param);
			break;
		case "프로토":
			showProto(gm, param);
			break;
		case "던전타이머":
			DungeonTimeLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "저주":
			CurseCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "저주초기화":
			CurseInitializeCharacter(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "인챈트시스템":
			MJItemEnchantSystemLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "캡챠":
			MJCaptchaLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "캐릭터상태":
			setCharacterInstanceStatus(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "배틀시스템":
			MJCombatLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "북시스템":
			BQSLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "출석체크":
			MJAttendanceLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "홈피보기":
			gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://minamldh.wixsite.com/adam", -1));
			break;
		case "표기셋팅":
			setPresentationCode(gm, param);
			break;
		case "마법회피율":
			findMagicDodge(gm, param);
			break;
		case "오브젝트리스트":
			showObjectList(gm, param);
			break;
		case "덤프":
			MJProcessPlayer.dumpLog();
			break;
		case "네트워크":
			MJNetServerLoadManager.commands(gm, param);
			break;
		case "보호모드":
			setProtectionMode(gm, param);
			break;
		case "넷세이프":
			MJNetSafeLoadManager.commands(gm, param);
			break;
		case "핑":
			ping(gm, param);
			break;
		case "킬차트":
			MJKDALoadManager.commands(gm, param);
			break;
		case "채팅테스트":
			showChat(gm, param);
			break;
		case "좌표":
			좌표(gm);
			break;
		case ",":
			showNextProto(gm, param);
			break;
		case "커맨드":
			gm.sendPackets(S_ShowCmd.get("cmd."));
			break;
		case "워리스트":
			showWarList(gm);
			break;
		case "유즈타입":
			showUseType(gm, param);
			break;
		case "캐릭압류":
			setCharBlock(gm, param);
			break;
		case "캐릭압류해제":
			setCharBlockDelete(gm, param);
			break;
		case "잠수모드":
			setSleepingMode(gm, param);
			break;
		case "잠수해제":
			unSetSleepingMode(gm, param);
			break;
		case "/":
			showNextEffect(gm);
			break;
		case "이펙":
			showEffectInit(gm, param);
			break;
		case "혈마크":
			clanMark(gm, param);
			break;
		case "mj":
			mjClear(gm, param);
			break;
		case "인스턴스":
			MJInstanceLoadManager.commands(gm, param);
			break;
		case "레이드":
			MJRaidLoadManager.commands(gm, param);
			break;
		case "구슬":
			MJCTLoadManager.commands(gm, param);
			break;
		case "가라":
			nocall(gm, param);
			break;
		case "랭킹시스템":
			MJRankLoadManager.getInstance().execute(new MJCommandArgs().setOwner(gm).setParam(param));
			break;
		case "혈맹랭킹갱신":
			L1ClanRanking.getInstance().gmcommand();
			gm.sendPackets("\\aA■ 혈맹레이드 랭킹이 갱신 되었습니다 ■");
			break;
		case "아이템":
			execute(gm, param, param);
			break;
		case "아데나":
			execute1(gm, param, param);
			break;
		case "이벤트":
			Event(gm, param);
			break;
		case "사":
			testshop(gm, param);
			break;
		case "이벤트2":
			Event2(gm, param);
			break;
		case "수렵":
			huntEvent(gm, param);
			break;
		case "소환이벤트":
			Event1(gm, param);
			break;
		case "노딜":
			NoDelayUser(gm);
			break;
		case "계좌":
			doolyHelp1(gm);
			break;
		case "주변밴":
			LargeAreaBan(gm, param);
			break;
		case "광역밴":
			LargeAreaIPBan(gm, param);
			break;
		case "파티":
			party(gm, param);
			break;
		case "아지트지급":
			GiveHouse(gm, param);
			break;
		case "공성시작":
			castleWarStart(gm, param);
			break;
		case "수배테스트":
			hunt(gm, param);
			break;
		case "브이아이피":
			vipsetting(gm, param);
			break;
		case "푸쉬시스템":
			pushSystem(gm, param);
			break;
		case "공성종료":
			castleWarExit(gm, param);
			break;
		case "메세지테스트":
			gm.sendPackets(new S_TestPacket(S_TestPacket.a));
			break;
		case "봇소환":
			봇소환(gm, param);
			break;
		case "서버저장":
			serversave(gm);
			break;
		case "전체선물":
			allpresent(gm, param);
			break;
		case "압류해제":
			accountdel(gm, param);
			break;
		case "경험치복구":
			returnEXP(gm, param);
			break;
		case "오토루팅":
			autoloot(gm, param);
			break;
		case "페널티":
			ExpTable.expPenaltyReLoad();
			gm.sendPackets("■ 페널티 리로드 완료 ■");
			break;
		case "인형청소":
			인형청소(gm);
			break;
		case "밸런스":
			CharacterBalance(gm, param);
			break;
		case "무인상점":
			privateShop(gm);
			break;
		case "아이콘":
			icon(gm, param);
			break;
		case "라이트":
		case "맵핵":
			maphack(gm, param);
			break;
		case "오픈대기":
			standBy(gm, param);
			break;
		case "스텟":
			fullstat(gm, param);
			break;
		case "상점검사":
			상점검사(gm);
			break;
		case "이팩트":
			effect(gm, param);
			break;
		case "유저인벤삭제":
			targetInventoryDelete(gm, param);
			break;
		case "계정추가":
			addaccount(gm, param);
			break;
		case "전체소환":
			allrecall(gm);
			break;
		case "코마지급":
			SpecialEventHandler.getInstance().doGiveEventStaff();
			break;
		case "보스소환주문서지급":
			SpecialEventHandler.getInstance().doGiveEventStaff2();
			break;
		case "토파즈지급":
			SpecialEventHandler.getInstance().doGiveEventStaff1();
			break;
		case "압류목록":
			search_banned(gm);
			break;
		case "암호변경":
			changePassword(gm, param);
			break;
		case "마을":
			unprison(gm, param);
			break;
		case "숨계":
			unprison2(gm, param);
			break;
		case "채금해제":
			chatx(gm, param);
			break;
		case "텔렉":
			tell(gm);
			break;
		case "검색":
			searchDatabase(gm, param);
			break;
		case "피케이":
			Pvp(gm, param);
			break;
		case "계정":
			account_Cha(gm, param);
			break;
		case "렙작":
			levelup2(gm, param);
			break;
		case "상점추방":
			ShopKick(gm, param);
			break;
		case "감옥":
			hold(gm, param);
			break;
		case "메모리반환":
			메모리반환(gm);
			break;
		case "포트변경":
			changePort(gm, param);
			break;
		case "인벤삭제":
			InventoryDelete(gm, param);
			break;
		case "아지트리로드":
			reloadHouse(gm, param);
			break;
		case "탐선물":
			탐선물(gm, param);
			break;
		case "전체정리":
			전체정리(gm);
			break;
		case "데드락":
			GeneralThreadPool.getInstance().execute(new DeadLockDetector(gm));
			break;
		case "공속체크":
			gm.AttackSpeedCheck2 = 1;
			gm.sendPackets("\\fY허수아비를 10회공격해주세요.");
			break;
		case "이속체크":
			gm.MoveSpeedCheck = 1;
			gm.sendPackets("\\fY한 방향으로 10회무빙해주세요.");
			break;
		case "마법체크":
			gm.magicSpeedCheck = 1;
			gm.sendPackets("\\fY원하는마법을 10회사용해주세요.");
			break;
		case "이미지삭제":
			this.get_delete_gfx(gm);
			break;
		case "버경확인":
			bug_race_check(gm);
			break;
		case "버경조작":
			bug_race_rate(gm, param);
			break;
		case "보스알림":
			spawnNotifyOnOff(gm, param);
			break;
		case "테스트":
			testCommands(gm, param);
			break;
		case "아덴시세":
			UserCommands.getInstance().AdSc(gm);
			break;
			
		case "제한상점":
			limitShop(gm, param);
			break;
	
		case "매니아체크":
			gm.sendPackets(
					S_ShowCmd.getPlayMovieNoti("http://kimsuin123.megaplug.kr/pricemania/pricePreviewboard1.html", -1));
			break;
		case "비트코인":
			gm.sendPackets(S_ShowCmd.getPlayMovieNoti("https://www.bithumb.com/u5/US506", -1));
			break;
		case "그랑카인":
			testcheck(gm);
			break;
		case "로또게임":
			if (Config.LOTTO == true) {
				LottoSystem.getInstance().isGmOpen = true; // 명령어부분에 추가
			}
			break;
		case "그랑카인초기화":
			GrangKinConfig.load();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getAI() != null)
					continue;
				// 로봇구별하는 구문..
				if (pc.getAccount() != null) {
					if (pc.getAccount().getGrangKinAngerStat() != 0) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1,
								555 + pc.getAccount().getGrangKinAngerStat(), false));
						SC_REST_EXP_INFO_NOTI.send(pc);
					}
					pc.getAccount().setGrangKinAngerTime(0, pc);
				}
			}

			grangKinDB_Reset();
			gm.sendPackets("그랑카인의 분노가 초기화 되었습니다.");
			break;
		case "타입":
			int type = Integer.parseInt(param);
			gm.sendPackets("\\aA요리타입 : " + type);
			gm.sendPackets(new S_PacketBox(53, type, 1800));
			break;
		case "헌터버프":
			try {
				gm.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS, 4992, 1800L, true));
			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		case "제작체크":
			if (!제작체크) {
				제작체크 = true;
				gm.sendPackets("\\aA■ 제작체크 ON! ■");
			} else {
				제작체크 = false;
				gm.sendPackets("\\aA■ 제작체크 OFF ■");
			}
			/** MJCTSystem **/
			break;
		case "테스트마크":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String code = st.nextToken();
				if (code.equalsIgnoreCase("켬")) {
					Config.테스트 = true;
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						for (L1Object npc : L1World.getInstance().getVisibleObjects(pc, 18)) {
							if (((npc instanceof L1AuctionBoardInstance)) || ((npc instanceof L1BoardInstance))
									|| ((npc instanceof L1GuardInstance)) || ((npc instanceof L1MerchantInstance))
									|| ((npc instanceof L1TeleporterInstance)))
								pc.sendPackets(S_WorldPutObject.get((L1NpcInstance) npc));
						}
					}
				} else if (code.equalsIgnoreCase("끔")) {
					Config.테스트 = false;
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						for (L1Object npc : L1World.getInstance().getVisibleObjects(pc, 18)) {
							if (((npc instanceof L1AuctionBoardInstance)) || ((npc instanceof L1BoardInstance))
									|| ((npc instanceof L1GuardInstance)) || ((npc instanceof L1MerchantInstance))
									|| ((npc instanceof L1TeleporterInstance)))
								pc.sendPackets(S_WorldPutObject.get((L1NpcInstance) npc));
						}
					}
				}

			} catch (Exception e) {
				gm.sendPackets(".테스트마크 [켬/끔]");
			}
			break;
		case "화면메세지":
			try {
				StringTokenizer st = new StringTokenizer(param);
				int number = Integer.parseInt(st.nextToken());
				int number2 = Integer.parseInt(st.nextToken());
				gm.sendPackets(new S_TestPacket(S_TestPacket.a, number, number2, "00 ff ff"));
			} catch (Exception e) {
				gm.sendPackets("화면메세지 [gfx번호] [메세지번호] 입력");
				gm.sendPackets("gfx는 4천이면 8000 *2배다.");
				gm.sendPackets("메세지 4천이면 2002 2가 붙는다.");
			}
			break;
		case "전체버프":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String status = st.nextToken();
				L1AllBuff.getInstance().execute(gm, param, status);
			} catch (Exception e) {
				gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
				gm.sendPackets(new S_ChatPacket(gm, " 1:풀업 2:축복  3:흑사 4:코마"));
				gm.sendPackets(new S_ChatPacket(gm, "----------------------------------------------------"));
			}
			break;
		case "리로드몬스폰":
			try {
				StringTokenizer st = new StringTokenizer(param);
				int mapid = Integer.parseInt(st.nextToken());
				L1World.getInstance().getMapObject(mapid);
				SpawnTable.getInstance().reload(mapid);
				gm.sendPackets("몬스폰리스트 (지정맵) 리로드 완료");
			} catch (Exception e) {
				gm.sendPackets(".리로드몬스폰 (맵아이디)");
			}

			break;
		case "엔코인충전":
			try {
				StringTokenizer tokenizer = new StringTokenizer(param);
				String name = tokenizer.nextToken();
				int coin = Integer.parseInt(tokenizer.nextToken());
				L1PcInstance tg = L1World.getInstance().getPlayer(name);
				if (tg != null) {
					tg.addNcoin(coin);
					tg.getNetConnection().getAccount().updateNcoin();
					tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"\\aAN코인 (\\aG" + coin + "\\aA) 원이 충전 되었습니다. 감사합니다."));
					tg.sendPackets("\\aAN코인 (\\aG " + coin + " \\aA) 원이 충전 되었습니다. 감사합니다.");
					tg.sendPackets("\\aA잔여금액: [\\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 원");

					gm.sendPackets("\\aA캐릭명:[\\aG" + name + "\\aA]" + "충전금액:[\\aG" + coin + "\\aA] 완료");
					gm.sendPackets("\\aA잔여금액: [\\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 확인됨");
				} else {
					gm.sendPackets("현재 접속하지 않은 캐릭터 입니다.");
				}
			} catch (Exception e) {
				gm.sendPackets("[엔코인충전] [케릭명] [금액] 입력.");
			}
			break;
		case "엔코인회수":
			try {
				StringTokenizer tokenizer = new StringTokenizer(param);
				String name = tokenizer.nextToken();
				int coin = Integer.parseInt(tokenizer.nextToken());
				L1PcInstance tg = L1World.getInstance().getPlayer(name);
				if (tg != null) {
					tg.addNcoin1(coin);
					tg.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
							"\\aAN코인 (\\aG" + coin + "\\aA) 원이 회수 되었습니다. 감사합니다."));
					tg.sendPackets("\\aAN코인 (\\aG " + coin + " \\aA) 원이 회수 되었습니다. 감사합니다.");
					tg.sendPackets("\\aA잔여금액: [\\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 원");

					gm.sendPackets("\\aA캐릭명:[\\aG" + name + "\\aA]" + "회수금액:[\\aG" + coin + "\\aA] 완료");
					gm.sendPackets("\\aA잔여금액: [\\aG" + tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 확인됨");
				} else {
					gm.sendPackets("현재 접속하지 않은 캐릭터 입니다.");
				}
			} catch (Exception e) {
				gm.sendPackets("[엔코인회수] [케릭명] [금액] 입력.");
			}
			break;
		case "엔코인확인":
			try {
				StringTokenizer tokenizer = new StringTokenizer(param);
				String name = tokenizer.nextToken();
				L1PcInstance tg = L1World.getInstance().getPlayer(name);
				if (tg != null) {
					gm.sendPackets("\\aA잔여금액: 캐릭명:[\\aG" + name + "\\aA] N코인:[\\aG"
							+ tg.getNetConnection().getAccount().Ncoin_point + "\\aA] 확인됨");
				} else {
					gm.sendPackets("현재 접속하지 않은 캐릭터 입니다.");
				}
			} catch (Exception e) {
				gm.sendPackets("[엔코인확인] [케릭명] 입력.");
			}
			break;
		case "챗":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				String msg = st.nextToken();
				for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
					listner.sendPackets(new S_ChatPacket(name, 0x03, msg));
				}
			} catch (Exception e) {
				gm.sendPackets(".챗 [캐릭명] [채팅말] 입력");
			}
			break;
		case "성혈조작":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				int number = Integer.parseInt(st.nextToken());
				L1Clan clan = L1World.getInstance().findClan(name);
				if (clan == null) {
					gm.sendPackets("혈맹이 존재하지 않습니다.");
				} else {
					clan.setCastleId(number);
					L1World.getInstance().removeClan(clan);
					L1World.getInstance().storeClan(clan);
					ClanTable.getInstance().updateClan(clan);
					gm.sendPackets(name + " 혈맹정보가 변경됐습니다.");
				}
			} catch (Exception e) {
				gm.sendPackets(".성혈조작 [혈이름] [성번호] 입력");
				gm.sendPackets("켄트1,오크2,윈다3,기란4,하이5,웰던6,아덴7,디아드8");
			}
			break;
		case "상점리로드":
			try {
				int npcid = Integer.parseInt(param);
				L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
				ShopTable.getInstance().Reload(npcid);
				gm.sendPackets("엔피씨 : " + npc.get_name() + " 리로드 되었습니다.");
			} catch (Exception e) {
				gm.sendPackets(".상점리로드 엔피씨ID");
			}
			break;
		case "혈가입":
			try {
				StringTokenizer st = new StringTokenizer(param);
				String name = st.nextToken();
				String clanname = st.nextToken();
				L1PcInstance pc = L1World.getInstance().getPlayer(name);
				L1Clan clan = L1World.getInstance().findClan(clanname);
				if (pc == null) {
					gm.sendPackets("그런 유저는 없습니다.");
					return;
				}
				if (clan == null) {
					gm.sendPackets("그런 혈맹은 없습니다.");
					return;
				}
				if (pc.getClanid() != 0) {
					gm.sendPackets("" + pc.getName() + "님은 혈맹이 있기때문에 탈퇴 시키겠습니다.");
					pc.ClearPlayerClanData(clan);
					clan.removeClanMember(pc.getName());
					gm.save();
					return;
				}

				for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
					clanMembers.sendPackets(new S_ServerMessage(94, pc.getName()));
					// \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
				}
				pc.setClanid(clan.getClanId());
				pc.setClanname(clanname);
				pc.setClanRank(L1Clan.수련);
				pc.setTitle("");
				pc.setClanMemberNotes("");
				pc.sendPackets(new S_CharTitle(pc.getId(), ""));
				Broadcaster.broadcastPacket(pc, new S_CharTitle(pc.getId(), ""));
				clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(), pc.getType(),
						pc.getOnlineStatus(), pc);
				pc.save(); // DB에 캐릭터 정보를 기입한다
				pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
				pc.sendPackets(new S_ServerMessage(95, clanname)); // \f1%0 혈맹에
																	// 가입했습니다.
				pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, true);
			} catch (Exception e) {
				gm.sendPackets(".혈가입 [캐릭명] [혈맹이름] 입력");
			}
			break;
		case "혈탈퇴":
			try {
				StringTokenizer tokenizer = new StringTokenizer(param);
				String pcName = tokenizer.nextToken();
				L1PcInstance pc = L1World.getInstance().getPlayer(pcName);
				if (pc == null) {
					gm.sendPackets("그런 유저는 없습니다.");
					return;
				}
				L1Clan clan = pc.getClan();
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				for (int i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(ServerMessage.LEAVE_CLAN, param, clan.getClanName()));// \f1%0이
																														// %1혈맹을
																														// 탈퇴했습니다.
				}
				pc.ClearPlayerClanData(clan);
				clan.removeClanMember(pc.getName());
				pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, true);
			} catch (Exception e) {
				gm.sendPackets(".혈탈퇴 [캐릭명] 입력");
			}
			break;
		 case "자동사냥": UserCommands.getInstance().autoHunt(gm, param); break;
		 case "자동사냥종료": autoHuntExit(gm); break;
//		 case "붉은전쟁선포": UserCommands.getInstance().blooodWarStart(gm, param); break;
		 case "할파스":
			 toHalpasTime(gm);
			 break;
		case ".":
			if (!_lastCommands.containsKey(gm.getId())) {
				gm.sendPackets(new S_ServerMessage(74, "커맨드 " + cmd));
				// \f1%0은사용할 수없습니다.
				return;
			}
			redo(gm, param);
			break;
		default:
			gm.sendPackets(new S_SystemMessage("[Command] 커멘드 " + cmd + " 는 존재하지 않습니다. "));
			break;
		}
	}
	
	private void toHalpasTime(L1PcInstance pc) {
		Timestamp time = pc.get_skill().getHalpasTime();
		if (time == null) {
			pc.sendPackets(new S_SystemMessage("할파스의 신의: 아직 발동이 되지 않았습니다."));
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		pc.sendPackets(new S_SystemMessage(String.format("할파스의 신의: [%s일 %s시 %s분]에 발동.", date, hour, minute)));
	}

	private void testcheck(L1PcInstance pc) {
		int grangKinOneStep = GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME;
		int grangKinTwoStep = GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME;
		int grangKinThreeStep = GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME;
		int grangKinFourStep = GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME;
		int grangKinFiveStep = GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME;
		int grangKinSixStep = GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME;

		int real_time = 0;
		int minute = 0;
		int hour = 0;
		if (pc.getAccount().getGrangKinAngerStat() == 1) {
			real_time = grangKinOneStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 2) {
			real_time = grangKinTwoStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 3) {
			real_time = grangKinThreeStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 4) {
			real_time = grangKinFourStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 5) {
			real_time = grangKinFiveStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		} else if (pc.getAccount().getGrangKinAngerStat() == 6) {
			real_time = grangKinSixStep - pc.getGrangKinAngerSafeTime();
			hour = real_time >= 60 ? real_time / 60 : 0;
			minute = real_time % 60;
		}

		if (real_time != 0) {
			pc.sendPackets("그랑카인의 분노 해제시 까지 마을대기 " + hour + "시간 " + minute + "분 남았습니다.");
		} else {
			pc.sendPackets("현재 그랑카인의 분노 상태가 아닙니다.");
		}
	}

	private void pushSystem(L1PcInstance gm, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			String name = tok.nextToken();

			if (name.equalsIgnoreCase("켬")) {
				if (PushItemController.isPushSystem()) {
					gm.sendPackets(new S_SystemMessage("이미 푸쉬시스템이 활성화 되어있습니다."));
					return;
				}
				PushItemController.setPushSystem(true);
				gm.sendPackets(new S_SystemMessage("푸쉬시스템이 시작 되었습니다."));
			} else if (name.equalsIgnoreCase("끔")) {
				if (!PushItemController.isPushSystem()) {
					gm.sendPackets(new S_SystemMessage("푸쉬시스템이 활성화 상태가 아닙니다."));
					return;
				}
				PushItemController.setPushSystem(false);
				gm.sendPackets(new S_SystemMessage("푸쉬시스템이 중지 되었습니다."));
			} else {
				gm.sendPackets(new S_SystemMessage("잘못된 요청입니다."));
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".푸쉬시스템 [켬/끔]"));
		}
	}

	private void bug_race_rate(L1PcInstance pc, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			int num = Integer.parseInt(tok.nextToken());
			int speed = Integer.parseInt(tok.nextToken());
			BugRaceController.getInstance().setSpeed(num - 1, speed);
			pc.sendPackets(String.format("레인 %d번의 속도를 %d로 변경하였습니다..", num, speed));
		} catch (Exception e) {
			pc.sendPackets("커멘드: 버경조작 [레인] [속도] 로 입력 바랍니다.");
		}
	}
	private void limitShop(L1PcInstance pc, String s) {
		try {
			StringTokenizer tok = new StringTokenizer(s);
			String num = tok.nextToken();
			if(num.equals("초기화")){
				LimitShopController.deleteTime = true;
				LimitShopController.getInstance().AlldeleteCharacterLimitShop();				
				for(L1PcInstance _pc : L1World.getInstance().getAllPlayers()){
					_pc.getLimitShop().clear();
				}
				LimitShopController.deleteTime = false;
				pc.sendPackets("초기화가 완료되었습니다.");
			}
		} catch (Exception e) {
			pc.sendPackets("제한상점 [초기화]");
		}
	}
	
	

	private void bug_race_check(L1PcInstance pc) {
		try {
			for (int i = 0; i < 5; ++i)
				pc.sendPackets(String.format("레인 : %d, 속도 : %d", i + 1, BugRaceController.getInstance().getSpeed(i)));
		} catch (Exception e) {
			pc.sendPackets("커멘드: 버경확인 으로 입력 바랍니다.");
		}
	}

	private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

	public void add_list(L1NpcInstance npc) {
		synchronized (list) {
			if (!list.contains(npc))
				list.add(npc);
		}
	}

	private void get_delete_gfx(L1PcInstance pc) {
		try {
			Iterator<L1NpcInstance> iter = list.iterator();
			L1NpcInstance npc = null;

			while (iter.hasNext()) {
				npc = iter.next();
				if (npc == null)
					continue;
				npc.deleteMe();
			}

			list.clear();
			pc.sendPackets("'이미지 삭제' 가 완료 되었습니다.");
		} catch (Exception e) {
			pc.sendPackets("커멘드: .이미지삭제 로 입력 바랍니다.");
		}
	}

	private static Random _rnd = new Random();

	public static Random getRnd() {
		return _rnd;
	}

	
	private void autoHuntExit(L1PcInstance pc) {
		AutoSystemController.getInstance().clearList();
		L1World.getInstance().broadcastServerMessage("\\aG모든 유저의 자동사냥을 종료 시킵니다.");
	}

	/*
	 * private void showCastleWarTime(L1PcInstance pc, String param) {
	 * L1Castle[] castles = WarTimeController.getInstance().getCastleArray();
	 * Calendar[] startTime =
	 * WarTimeController.getInstance().getWarStartTimeArray(); Calendar[]
	 * endTime = WarTimeController.getInstance().getWarEndTimeArray();
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 * try { StringTokenizer tok = new StringTokenizer(param); String name =
	 * tok.nextToken(); for (int i = 0; i < castles.length; i++) { L1Castle
	 * castle = castles[i]; if (castle.getName().startsWith(name)) {
	 * pc.sendPackets(String.format("%s: %s ~ %s", castle.getName(),
	 * formatter.format(startTime[i].getTime()),
	 * formatter.format(endTime[i].getTime())))); } } Calendar cal =
	 * Calendar.getInstance(); pc.sendPackets(String.format( "서버시간: %s",
	 * formatter.format(cal.getTime())))); } catch (Exception e) {
	 * pc.sendPackets(".공성시간 (켄트,오크,윈다,기란,하이,드워,아덴,디아)")); } }
	 */

//	private void maphack(L1PcInstance gm, String param) {
//		try {
//			StringTokenizer st = new StringTokenizer(param);
//			String on = st.nextToken();
//			if (on.equalsIgnoreCase("켬")) {
//				gm.sendPackets(new S_Ability(3, true));
//				gm.sendPackets("라이트 : 켬");
//			} else if (on.equals("끔")) {
//				gm.sendPackets(new S_Ability(3, false));
//				gm.sendPackets("라이트 : 끔");
//			}
//		} catch (Exception e) {
//			gm.sendPackets(".라이트 [켬, 끔] 으로 설정 하세요.");
//		}
//	}
	
	private void maphack(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("켬")) {
				gm.sendPackets(new S_Ability(3, true));
				gm.sendPackets("라이트 : 켬");
			} else if (on.equals("끔")) {
				gm.sendPackets(new S_Ability(3, false));
				gm.sendPackets("라이트 : 끔");
			}
		} catch (Exception e) {
			gm.sendPackets(".라이트 [켬, 끔] 으로 설정 하세요.");
		}
	}

	public static int get_random(int min, int max) {
		if (min > max)
			return min;
		return _rnd.nextInt(max - min + 1) + min;
	}

	// TODO GM 커멘드 추가
	private void showHelp(L1PcInstance gm) {
		gm.sendPackets(new S_ChatPacket(gm, "################# 운영자 명령어 ################"));
		gm.sendPackets(new S_ChatPacket(gm, "가라  감시  감옥  검색  계정  계좌  공격  귀환  노딜  누구", 1));
		gm.sendPackets(new S_ChatPacket(gm, "답장  데무  데방  레벨  렙작  로봇  마을  무인  배치  버프", 1));
		gm.sendPackets(new S_ChatPacket(gm, "변신  보스  서먼  셋팅  소생  소환  속도  스폰  위치  이동", 1));
		gm.sendPackets(new S_ChatPacket(gm, "저주  정리  정보  조회  좌표  찾기  채금  챗  추방  출두", 1));
		gm.sendPackets(new S_ChatPacket(gm, "충전  캡챠  코드  텔렉  투명  펫  피바  핑", 1));
		gm.sendPackets(new S_ChatPacket(gm, "광역밴  대항전  데드락  데스크  레이드  렙선물  리로드", 1));
		gm.sendPackets(new S_ChatPacket(gm, "몬스터  몹속도  밸런스  샌드웜  아이콘  아이템  이미지", 1));
		gm.sendPackets(new S_ChatPacket(gm, "이벤트  이펙트  주변밴  킬차트  탐선물  페널티  프로토", 1));
		gm.sendPackets(new S_ChatPacket(gm, "피케이  혈마크  혈맹밴  혈탈퇴", 1));
		gm.sendPackets(new S_ChatPacket(gm, "개인버프  계정검사  계정압류  계정정보  계정추가", 1));
		gm.sendPackets(new S_ChatPacket(gm, "계정확인  공개채금  공성시간  공성시작  공성종료", 1));
		gm.sendPackets(new S_ChatPacket(gm, "공속체크  광역추방  교환불가  구매  구매취소", 1));
		gm.sendPackets(new S_ChatPacket(gm, "그랑카인  기운축복  네트워크  넷세이프  데몬작업", 1));
		gm.sendPackets(new S_ChatPacket(gm, "데스작업  데페작업  드랍불가  랭킹갱신  마법체크", 1));
		gm.sendPackets(new S_ChatPacket(gm, "밴아이피  보호모드  상점검사  상점추방  서버이동", 1));
		gm.sendPackets(new S_ChatPacket(gm, "서버저장  성혈조작  아머작업  암호변경  압류목록", 1));
		gm.sendPackets(new S_ChatPacket(gm, "압류해제  에르자베  영구추방  영자상점  오토루팅", 1));
		gm.sendPackets(new S_ChatPacket(gm, "오토루팅  오픈대기  유즈타입  이속체크  인공지능", 1));
		gm.sendPackets(new S_ChatPacket(gm, "인벤삭제  인스턴스  인형박스  인형청소  잊섬시작", 1));
		gm.sendPackets(new S_ChatPacket(gm, "잊섬종료  잠수모드  잠수해제  전체버프  전체선물", 1));
		gm.sendPackets(new S_ChatPacket(gm, "전체정리  제작체크  지배보스  창고불가  채금해제", 1));
		gm.sendPackets(new S_ChatPacket(gm, "카배작업  캐릭압류  케릭검사  퀴즈변경  타락작업", 1));
		gm.sendPackets(new S_ChatPacket(gm, "통합버프  파티소환  판매  판매삭제  판매완료", 1));
		gm.sendPackets(new S_ChatPacket(gm, "판매취소  편지삭제  포트변경  표기셋팅  혈맹정보", 1));
		gm.sendPackets(new S_ChatPacket(gm, "화면버프", 1));
		gm.sendPackets(new S_ChatPacket(gm, "경험치복구  던전타이머  디비초기화  랭킹시스템", 1));
		gm.sendPackets(new S_ChatPacket(gm, "룬스톤데페  룬스톤디스  룬스톤아머  룬스톤카배", 1));
		gm.sendPackets(new S_ChatPacket(gm, "마법회피율  메모리반환  배틀시스템  버프아이콘", 1));
		gm.sendPackets(new S_ChatPacket(gm, "상점리로드  소환이벤트  아이템삭제  아이피추방", 1));
		gm.sendPackets(new S_ChatPacket(gm, "아지트지급  엔코인충전  엔코인확인  엔코인회수", 1));
		gm.sendPackets(new S_ChatPacket(gm, "요리아이콘  인벤이미지  저주초기화  진데스나발", 1));
		gm.sendPackets(new S_ChatPacket(gm, "진데스데불  진데스론론  진데스살키  진데스섬체", 1));
		gm.sendPackets(new S_ChatPacket(gm, "진데스악몽  진데스진레  진데스진싸  진데스커검", 1));
		gm.sendPackets(new S_ChatPacket(gm, "진데스태풍  진데스포효  캐릭터상태  토파즈지급", 1));
		gm.sendPackets(new S_ChatPacket(gm, "패널티조정  푸쉬시스템  혈맹경험치", 1));
		gm.sendPackets(new S_ChatPacket(gm, "리로드몬스폰  무기인챈성공  방어인챈성공", 1));
		gm.sendPackets(new S_ChatPacket(gm, "아지트리로드  유저인벤삭제  인챈트시스템", 1));
		gm.sendPackets(new S_ChatPacket(gm, "자동사냥종료  진데스제로스  캐릭압류해제", 1));
		gm.sendPackets(new S_ChatPacket(gm, "그랑카인초기화  오브젝트리스트", 1));
		gm.sendPackets(new S_ChatPacket(gm, "보스소환주문서지급  아이피딜레이체크", 1));
//		gm.sendPackets(new S_ChatPacket(gm, "인챈트시스템(tb_enchanties..)  캐릭터상태", 1));
	}

	private static Map<Integer, String> _lastCommands = new HashMap<Integer, String>();

	private void searchDatabase(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int type = Integer.parseInt(tok.nextToken());
			String name = tok.nextToken();
			searchObject(gm, type, "%" + name + "%");
		} catch (Exception e) {
			gm.sendPackets(".검색 [0~5] [name]을 입력 해주세요.");
			gm.sendPackets("0=잡템, 1=무기, 2=갑옷, 3=엔피시, 4=변신, 5=엔피시(gfxid)");
		}
	}

	private void 전체정리(L1PcInstance gm) {
		int cnt = 0;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				mon.die(gm);
				cnt++;
			}

		}
		gm.sendPackets("몬스터 " + cnt + "마리를 죽였습니다.");
	}

	private void doolyHelp1(L1PcInstance gm) {
		gm.sendPackets("----------------------------------------------------");
		gm.sendPackets("입금 계좌 : SC제일은행 / 없음");
		gm.sendPackets("계좌번호 : 없음");
		gm.sendPackets("----------------------------------------------------");
	}

	private void autoloot(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String type = tok.nextToken();
			if (type.equalsIgnoreCase("리로드")) {
				AutoLoot.getInstance();
				AutoLoot.reload();
				gm.sendPackets("오토루팅 설정이 리로드 되었습니다.");
			} else if (type.equalsIgnoreCase("검색")) {
				String nameid = tok.nextToken();
				Selector.exec(String.format(
						"select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from etcitem) as b on a.item_id=b.item_id where b.name like '%%%s%%'",
						nameid), new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
				Selector.exec(String.format(
						"select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from armor) as b on a.item_id=b.item_id where b.name like '%%%s%%'",
						nameid), new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
				Selector.exec(String.format(
						"select b.item_id, b.name FROM autoloot as a inner join (select item_id, name from weapon) as b on a.item_id=b.item_id where b.name like '%%%s%%'",
						nameid), new FullSelectorHandler() {
							@Override
							public void result(ResultSet rs) throws Exception {
								while (rs.next())
									gm.sendPackets(String.format("[%d]%s", rs.getInt("item_id"), rs.getString("name")));
							}
						});
			} else {
				String nameid = tok.nextToken();
				int itemid = 0;
				try {
					itemid = Integer.parseInt(nameid);
				} catch (NumberFormatException e) {
					itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
					if (itemid == 0) {
						gm.sendPackets("해당 아이템이 발견되지 않습니다. ");
						return;
					}
				}

				L1Item temp = ItemTable.getInstance().getTemplate(itemid);
				if (temp == null) {
					gm.sendPackets("해당 아이템이 발견되지 않습니다. ");
					return;
				}
				if (type.equalsIgnoreCase("추가")) {
					if (AutoLoot.getInstance().isAutoLoot(itemid)) {
						gm.sendPackets("이미 오토루팅 목록에 있습니다.");
						return;
					}
					AutoLoot.getInstance().storeId(itemid);
					gm.sendPackets("오토루팅 항목에 추가 했습니다.");
				} else if (type.equalsIgnoreCase("삭제")) {
					if (!AutoLoot.getInstance().isAutoLoot(itemid)) {
						gm.sendPackets("오토루팅 항목에 해당 아이템이 없습니다.");
						return;
					}
					gm.sendPackets("오토루팅 항목에서 삭제 했습니다.");
					AutoLoot.getInstance().deleteId(itemid);
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".오토루팅 리로드");
			gm.sendPackets(".오토루팅 추가|삭제 itemid|name");
			gm.sendPackets(".오토루팅 검색 name");
		}
	}

	private void LargeAreaBan(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int range = Integer.parseInt(st.nextToken());
			Integer reason = S_LoginResult.banServerCodes.get(st.nextToken());
			if (reason == null)
				throw new Exception("");
			int count = 0;
			IpTable iptable = IpTable.getInstance();
			pc.sendPackets("----------------------------------------------------");
			for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, range)) {
				Account.ban(player.getAccountName(), reason); // 계정을 BAN시킨다.
				iptable.banIp(player.getNetConnection().getIp()); // BAN 리스트에
				// IP를 추가한다.
				pc.sendPackets(player.getName() + ", (" + player.getAccountName() + ")");
				player.logout();
				player.getNetConnection().kick();
				count++;
			}
			pc.sendPackets("주변의 유저 " + count + "명을 영구추 시키셨습니다.");
			pc.sendPackets("----------------------------------------------------");

		} catch (Exception e) {
			pc.sendPackets(".주변밴  [범위] [압류사유번호]");
		}
	}

	private void spawnNotifyOnOff(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("켬")) {
				gm.setBossNotify(true);
				gm.sendPackets("보스알림:켬 (보스소환 및 알림이 실행 되었습니다)");
			} else if (on.equals("끔")) {
				gm.setBossNotify(false);
				gm.sendPackets("보스알림:끔 (보스소환 및 알림이 종료 되었습니다)");
			}
		} catch (Exception e) {
			gm.sendPackets(".보스알림 [켬, 끔]으로 설정하세요. 현재(" + (gm.isBossNotify() == true ? "켜짐" : "꺼짐") + ")입니다.");
		}
	}

	public void execute1(L1PcInstance pc, String cmdName, String arg) {
		try {

			StringTokenizer stringtokenizer = new StringTokenizer(arg);

			int count = Integer.parseInt(stringtokenizer.nextToken());
			L1ItemInstance adena = pc.getInventory().storeItem(L1ItemId.ADENA, count);

			if (adena != null) {
				pc.sendPackets((new StringBuilder()).append(count).append("아데나를 생성했습니다.").toString());
			}
		} catch (Exception e) {
			pc.sendPackets((new StringBuilder()).append(".아데나 [액수]로 입력해 주세요. ").toString());
		}
	}

	public void execute(L1PcInstance pc, String param, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String nameid = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int attrenchant = 0;
			if (st.hasMoreTokens()) {
				attrenchant = Integer.parseInt(st.nextToken());
			}

			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets("\\f2해당 아이템이 발견되지 않았습니다.");
					return;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					item.setIdentified(true);
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_SystemMessage("\\f2아이템 생성 완료"));
						pc.sendPackets(new S_SystemMessage("\\f2"+item.getLogName()+" | ID: "+itemid+" | 인챈트: "+enchant+""));
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						item.setAttrEnchantLevel(attrenchant);
						item.setIdentified(true);
						if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item);
						} else {
							break;
						}
					}
					if (createCount > 0) {
						pc.sendPackets(new S_SystemMessage("\\f2"+item.getLogName()+" | ID: "+itemid+" | 인챈트: "+enchant+""));
						;
					}
				}
			} else {
				pc.sendPackets("\\f2해당 아이템이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			pc.sendPackets("\\f2.아이템   이름   수량   인챈트   속성1~20   확인0~1");
		}
	}

	private void huntEvent(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String type = st.nextToken();
			if (type.equalsIgnoreCase("시작")) {
				Config.HUNT_EVENT = true;
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "크리스마스 수렵 이벤트가 진행 됩니다. 사슴과 멧돼지를 모조리 잡아주세요!"));
				L1World.getInstance()
						.broadcastPacketToAll(new S_ChatPacket(gm, "\\aA크리스마스 수렵 이벤트가 진행 됩니다. 사슴과 멧돼지를 모조리 잡아주세요!"));

			} else if (type.equalsIgnoreCase("종료")) {
				Config.HUNT_EVENT = false;
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"크리스마스 수렵 이벤트가 종료 되었습니다. 나오신 '루돌프의 종'를 기란마을 산타에게 가져다 주세요."));
				L1World.getInstance().broadcastPacketToAll(
						new S_ChatPacket(gm, "\\aA크리스마스 수렵 이벤트가 종료 되었습니다. 나오신 '루돌프의 종'를 기란마을 산타에게 가져다 주세요."));
			}
		} catch (Exception e) {
			gm.sendPackets(".수렵 [시작 and 종료]");
		}
	}
	
	private void testshop(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int code = Integer.parseInt(st.nextToken());
			gm.sendPackets(new S_TestShop(gm.getId(), code));
		} catch (Exception e) {
			gm.sendPackets("[.사][숫자]");			
		}
	}
	
	private void Event(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String event = st.nextToken();
			String time1 = st.nextToken();
			int time = Integer.parseInt(time1);
			if (event.equalsIgnoreCase("메티스버프")) {
				L1SpawnUtil.Gmspawn(7320260, 33430, 32807, (short) 4, 6, time * 60 * 60 * 1000);

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("메티스버프1")) {
				L1SpawnUtil.Gmspawn(526, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("메티스버프2")) {
				L1SpawnUtil.Gmspawn(529, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("메티스버프3")) {
				L1SpawnUtil.Gmspawn(530, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("메티스버프4")) {
				L1SpawnUtil.Gmspawn(531, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프5")) {
				L1SpawnUtil.Gmspawn(532, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프6")) {
				L1SpawnUtil.Gmspawn(533, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프7")) {
				L1SpawnUtil.Gmspawn(534, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프8")) {
				L1SpawnUtil.Gmspawn(535, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프9")) {
				L1SpawnUtil.Gmspawn(536, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프10")) {
				L1SpawnUtil.Gmspawn(537, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프11")) {
				L1SpawnUtil.Gmspawn(538, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
				
			} else if (event.equalsIgnoreCase("메티스버프12")) {
				L1SpawnUtil.Gmspawn(539, 33430, 32807, (short) 4, 5, time * 60 * 60 * 1000);// 기란
				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"\\aD곧이어 메티스의 축복 버프가 " + time + "\\aD시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("\\aD곧이어 메티스의 축복 버프가 " + time + "시간 동안 기란 마을에 출현 합니다.");
			}
		} catch (Exception e) {
			gm.sendPackets("[.이벤트] [메티스버프] [시간]");
			gm.sendPackets("예).이벤트 메티스버프 1~12");
		}
	}

	private void Event2(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String event = st.nextToken();
			String time1 = st.nextToken();
			int time = Integer.parseInt(time1);
			if (event.equalsIgnoreCase("드래곤헌터")) {
				L1SpawnUtil.Gmspawn(7320159, 33441, 32813, (short) 4, 6, time * 60 * 60 * 1000);

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"드래곤 헌터의 가호 이벤트가 시작되었습니다. 곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("특급용사1")) {
				L1SpawnUtil.Gmspawn(526, 33447, 32793, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"특급 용사 이벤트(1) 가 시작되었습니다. 곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("특급용사2")) {
				L1SpawnUtil.Gmspawn(529, 33445, 32791, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"특급 용사 이벤트(2) 가 시작되었습니다. 곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("특급용사3")) {
				L1SpawnUtil.Gmspawn(530, 33449, 32795, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"특급 용사 이벤트(3) 가 시작되었습니다. 곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다.");

			} else if (event.equalsIgnoreCase("특급용사4")) {
				L1SpawnUtil.Gmspawn(531, 33444, 32797, (short) 4, 5, time * 60 * 60 * 1000);// 기란

				gm.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
						"특급 용사 이벤트(4) 가 시작되었습니다. 곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다."));
				L1World.getInstance().broadcastServerMessage("곧이어 (" + time + ") 시간 동안 기란 마을에 출현 합니다.");
			}
		} catch (Exception e) {
			gm.sendPackets("[.이벤트] [이벤트명] [시간]");
			gm.sendPackets("[이벤트명]: 드래곤헌터/특급용사1~4");
		}
	}

	private void Event1(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String event = st.nextToken();

			if (event.equalsIgnoreCase("부대1")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 45545, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 7000091, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 7000092, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 7000089, 0, 0, 0);
				System.out.println("▶소환이벤트 시작 : 첫번째 부대 출현◀");

				L1World.getInstance().broadcastServerMessage("\\aA소환이벤트: 잠시후 메티스의 근위부대가 출현 합니다.");

			} else if (event.equalsIgnoreCase("부대2")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 45203, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 45206, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 45257, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 45263, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 45341, 0, 0, 0);
				System.out.println("▶소환이벤트 시작 : 두번째 부대 출현◀");

				L1World.getInstance().broadcastServerMessage("\\aA소환이벤트: 곧 이어 메티스의 근위부대가 출현 합니다.");

			} else if (event.equalsIgnoreCase("부대3")) {
				int rx = _random.nextInt(5);
				int ry = _random.nextInt(5);

				int rx1 = _random.nextInt(10);
				int ry1 = _random.nextInt(10);

				int rx2 = _random.nextInt(15);
				int ry2 = _random.nextInt(15);

				int ux = 32926 + rx;
				int uy = 33250 + ry;
				int um = 4;

				int ux1 = 32926 + rx1;
				int uy1 = 33250 + ry1;

				int ux2 = 32926 + rx2;
				int uy2 = 33250 + ry2;

				L1SpawnUtil.spawnfieldboss(ux, uy, (short) um, 707001, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux1, uy1, (short) um, 707002, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707007, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707008, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707013, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707015, 0, 0, 0);
				L1SpawnUtil.spawnfieldboss(ux2, uy2, (short) um, 707016, 0, 0, 0);
				System.out.println("▶소환이벤트 시작 : 세번째 부대 출현◀");

				L1World.getInstance().broadcastServerMessage("\\aA소환이벤트: 최종 메티스의 근위부대가 출현 합니다.");
			}
		} catch (Exception e) {
			gm.sendPackets("[.소환이벤트] [이벤트명]");
			gm.sendPackets("[소환이벤트명]: 부대1/부대2/ 부대3/부대4/부대5");
		}
	}

	private void LargeAreaIPBan(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);

			String charName = st.nextToken();
			String banIp = "";

			L1PcInstance player = L1World.getInstance().getPlayer(charName);

			if (player != null) {
				banIp = player.getNetConnection().getIp();

				String[] banIpArr = banIp.split("\\.");

				IpTable iptable = IpTable.getInstance();
				pc.sendPackets("----------------------------------------------------");
				Account.ban(player.getAccountName(), S_LoginResult.BANNED_REASON_NOMANNER); // 계정을
																							// BAN시킨다.
				player.logout();
				player.getNetConnection().kick();
				for (int i = 1; i <= 255; i++) {
					iptable.banIp(banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + "." + i);
				}

				pc.sendPackets("IP: " + banIpArr[0] + "." + banIpArr[1] + "." + banIpArr[2] + ".1~255 광역 차단.");
				pc.sendPackets("----------------------------------------------------");
			}
		} catch (Exception e) {
			pc.sendPackets(".광역밴  [범위]");
		}
	}

	private void vipsetting(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			L1PcInstance vip = L1World.getInstance().findpc(name);

			if (vip == null) {
				pc.sendPackets("접속중이니 않거나, 설정이 잘못되었습니다.");
				return;
			}
			if (BuyerController.isadd(name) == false) {
				pc.sendPackets("등급조정됨,처리안된경우 삭제후 등록");
				BuyerController.delbuyer(name);
			}

			String memo = tok.nextToken();
			if (memo == null || memo.length() < 1 || memo.length() > 20) {
				pc.sendPackets("[" + memo + "]메모 오류.");
				return;
			}
			int level = Integer.parseInt(tok.nextToken());
			if (level == 0) {
				pc.sendPackets("buyer 삭제 = " + name);
				return;
			}
			if (level >= 1 && level <= 3) {
				BuyerController.addbuyer(name, level, memo);
				if (vip != null)
					pc.sendPackets("\\aA" + name + "님을 [\\aG" + level + "\\aA]단계로 설정 하였습니다.");
				vip.sendPackets("VIP [" + level + "]단계 설정되었습니다.");
				vip.sendPackets("VIP:09:00시 마다 인벤에 '부가아이템 창고'로 지급 됩니다.");
				vip.sendPackets(new S_SkillSound(vip.getId(), 12444));
			} else {
				pc.sendPackets("단계는 (1~3) 삭제는 0");
			}
		} catch (Exception e) {
			pc.sendPackets(".vip 캐릭터 메모 단계(0~3,0은삭제)");
		}
	}

	private void hunt(L1PcInstance pc, String cmd) {
		int price = 1000; // 수배 금액
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String name = tok.nextToken();
			if (name == null || name.equals(""))
				throw new Exception();

			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target == null) {
				pc.sendPackets(String.format("%s 님을 찾을 수 없습니다.", name));
				return;
			}
			if (target.hasSkillEffect(L1SkillId.USER_WANTED)) {
				pc.sendPackets(String.format("%s 님은 이미 수배중입니다.", name));
				return;
			}
			if (target.isGm()) {
				pc.sendPackets("감히 운영자에게 수배를 걸 수 없습니다.");
				return;
			}
//			if (!(pc.getInventory().checkItem(40308, price))) { // 아데나
			if (!(pc.getInventory().checkItem(3000181, price))) { //  후원 코인
//				pc.sendPackets("아데나가 부족합니다");
				pc.sendPackets("후원 코인이 부족합니다.");
				return;
			}

			S_SystemMessage message = new S_SystemMessage(String.format("%s 님께서 %s님에게 수배를 걸었습니다.", pc.getName(), target.getName()));
			pc.sendPackets(message, false);
			target.sendPackets(message);
			target.setSkillEffect(L1SkillId.USER_WANTED, -1);
			target.doWanted(true);
//			pc.getInventory().consumeItem(40308, price); // 아데나
			pc.getInventory().consumeItem(3000181, price); // 후원 코인
		} catch (Exception e) {
			pc.sendPackets("----------------------------------------------------");
			pc.sendPackets(new S_SystemMessage("근/원/마법 대미지/명중/AC/리덕션이 모두 +3씩 상승"));
			pc.sendPackets(new S_SystemMessage("금액: 후원 코인 1000개 (자동 차감)"));
			pc.sendPackets(new S_SystemMessage("\\aG.수배   캐릭터명"));
			pc.sendPackets("----------------------------------------------------");
			
		}
	}

	private void serversave(L1PcInstance pc) {
		Saveserver();// 서버세이브 메소드 선언
		pc.sendPackets("서버저장이 완료되었습니다.");
	}

	/** 서버저장* */
	private void Saveserver() {
		/** 전체플레이어를 호출* */
		Collection<L1PcInstance> list = null;
		list = L1World.getInstance().getAllPlayers();
		for (L1PcInstance player : list) {
			if (player == null)
				continue;
			try {
				/** 피씨저장해주고* */
				player.save();
				/** 인벤도 저장하고* */
				player.saveInventory();

			} catch (Exception ex) {
				/** 예외 인벤저장* */
				player.saveInventory();
				System.out.println("저장 명령어 에러(인벤만 저장함): " + ex);
			}
		}
	}

	private void privateShop(L1PcInstance pc) {
		try {
			if (!pc.isPrivateShop()) {
				pc.sendPackets("개인상점 상태에서 사용이 가능합니다.");
				return;
			}

			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			pc.stopHpMpRegeneration();
			pc.set무인상점(true);
			try {
				pc.save();
				pc.saveInventory();
			} catch (Exception e) {
			}
			client.setActiveChar(null);
			client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
			client.sendPacket(new S_Unknown2(1)); // 리스버튼을 위한 구조변경 // Episode U

		} catch (Exception e) {
		}
	}

	private void GiveHouse(L1PcInstance pc, String poby) {
		try {
			StringTokenizer st = new StringTokenizer(poby);
			String pobyname = st.nextToken();
			int pobyhouseid = Integer.parseInt(st.nextToken());
			L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
			if (target != null) {
				if (target.getClanid() != 0) {
					L1Clan TargetClan = L1World.getInstance().getClan(target.getClanid());
					L1House pobyhouse = HouseTable.getInstance().getHouseTable(pobyhouseid);
					TargetClan.setHouseId(pobyhouseid);
					ClanTable.getInstance().updateClan(TargetClan);
					pc.sendPackets(target.getClanname() + " 혈맹에게 " + pobyhouse.getHouseName() + "번을 지급하였습니다.");
					for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
						tc.sendPackets("메티스로부터 " + pobyhouse.getHouseName() + "번을 지급 받았습니다.");
					}
				} else {
					pc.sendPackets(target.getName() + "님은 혈맹에 속해 있지 않습니다.");
				}
			} else {
				pc.sendPackets(new S_ServerMessage(73, pobyname));
			}
		} catch (Exception e) {
			pc.sendPackets(".아지트지급 <지급할혈맹원> <아지트번호>");
		}
	}

	private void castleWarStart(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			int minute = Integer.parseInt(tok.nextToken());
			Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
			if (minute != 0)
				cal.add(Calendar.MINUTE, minute);

			MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(name);
			if (war == null)
				throw new Exception();

			war.nextCalendar(cal);
			MJCastleWarBusiness.getInstance().updateCastle(war.getCastleId());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			gm.sendPackets(String.format(".공성시간이 %s 으로 변경 되었습니다.", formatter.format(cal.getTime())));
			gm.sendPackets(param + "분 뒤 공성이 시작합니다.");
			formatter = null;
		} catch (Exception e) {
			gm.sendPackets(".공성시작 [성이름두글자] [분]");
		}
	}

	private void castleWarExit(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(name);
			if (war == null)
				throw new Exception();

			war.close();
		} catch (Exception e) {
			gm.sendPackets(".공성종료 [성이름두글자]");
		}
	}

	private void party(L1PcInstance gm, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(cmdName);
			String cmd = tok.nextToken();
			if (cmd.equals("주변")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				int range = 3;// 현재주변3칸
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(gm, range)) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					} // 파티있는유저제외
					if (Targetpc.isPrivateShop()) {
						continue;
					} // 무인제외
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + "님을 내파티에 참가시켰습니다.");
				}
				gm.sendPackets(range + "칸 안의 유저를 내파티에 참가시켰습니다.");
			} else if (cmd.equals("화면")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(gm)) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + "님을 내파티에 참가시켰습니다.");
				}
				gm.sendPackets("화면안의 유저를 내파티에 참가시켰습니다.");
			} else if (cmd.equals("전체")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				int range = 3;// 현재주변3칸
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + "님을 내파티에 참가시켰습니다.");
				}
				gm.sendPackets(range + "칸 안의 유저를 내파티에 참가시켰습니다.");
			} else if (cmd.equals("참가")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				if (TargetPc.getParty() != null) {
					gm.sendPackets(TargetPc.getName() + "님은 파티가 없습니다.");
				} else {
					TargetPc.getParty().addMember(gm);
					gm.sendPackets(TargetPc.getName() + "님의 파티에 참가했습니다.");
				}
			} else if (cmd.equals("초대")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				if (TargetPc.getParty() != null) {
					TargetPc.getParty().kickMember(TargetPc);
				}
				party.addMember(TargetPc);
				gm.sendPackets(TargetPc.getName() + "님을 내파티에 강제참가시켰습니다.");
			} else if (cmd.equals("강제초대")) {
				L1Party party = new L1Party();
				if (gm.getParty() == null) {
					party.addMember(gm);
				} else {
					party = gm.getParty();
				}
				// int range = 3;// 현재주변3칸
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (gm.getName().equals(Targetpc.getName())) {
						continue;
					}
					if (Targetpc.isPrivateShop()) {
						continue;
					}
					if (Targetpc.getParty() != null) {
						Targetpc.getParty().kickMember(Targetpc);
					}
					party.addMember(Targetpc);
					gm.sendPackets(Targetpc.getName() + "님을 내파티에 참가시켰습니다.");
				}
				gm.sendPackets("접속중인 유저를 내파티에 강제참가시켰습니다.");
			} else if (cmd.equals("파장")) {
				if (gm.getParty() == null) {
					gm.sendPackets("참가중인파티가없습니다.");
				} else {
					gm.getParty().passLeader(gm);
					gm.sendPackets("파장을 뺐었습니다.");
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".파티 [주변,화면,전체,참가 (유저이름)]");
			gm.sendPackets(".파티 [초대 (유저이름),강제초대,파장]");
		}
	}

	private void effect(L1PcInstance pc, String param) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int sprid = Integer.parseInt(stringtokenizer.nextToken());
			pc.sendPackets(new S_SkillSound(pc.getId(), sprid));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), sprid));
		} catch (Exception e) {
			pc.sendPackets(".이팩트 [숫자] 라고 입력해 주세요.");
		}
	}

	private int 최소값(int itemid) {
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement(
						"SELECT * FROM shop WHERE item_id = ? AND selling_price NOT IN (-1) ORDER BY selling_price ASC limit 1");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int temp = 0;
					if (rs.getInt("pack_count") > 1)
						temp = rs.getInt("selling_price") / rs.getInt("pack_count");
					else {
						temp = rs.getInt("selling_price");
					}
					int i = temp;
					return i;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int 최대값(int itemid) {
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement(
						"SELECT purchasing_price FROM shop WHERE item_id = ? ORDER BY purchasing_price DESC limit 1");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int i = rs.getInt("purchasing_price");
					return i;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void changePort(L1PcInstance pc, String poby) {
		try {
			StringTokenizer token = new StringTokenizer(poby);
			int port = Integer.valueOf(token.nextToken()).intValue();

			Server.createServer().changePort(port);

			pc.sendPackets("\\aA포트: 포트가 [\\aG" + port + "\\aA]번으로 변경되었습니다.");
			pc.sendPackets("\\aH서버내부 게임포트가 변경처리된것! 기존유저 살아남고,신규 접속불가");
			pc.sendPackets("\\aH신규영입하려면 접속기포트 [\\aG" + port + "]\\aH번으로 변경하기");
		} catch (Exception e) {
			pc.sendPackets("경고: 입력법이 틀렸습니다.");
		}
	}

	private void 상점검사(L1PcInstance gm) {
		try {
			ArrayList<Integer> itemids = new ArrayList<Integer>();
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int cnt;
			Iterator i$;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT item_id FROM shop");
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (!itemids.contains(Integer.valueOf(rs.getInt("item_id")))) {
						itemids.add(Integer.valueOf(rs.getInt("item_id")));
					}
				}
				cnt = 0;
				for (i$ = itemids.iterator(); i$.hasNext();) {
					int itemid = ((Integer) i$.next()).intValue();
					int 구매최저가 = 최소값(itemid);
					int 판매최고가 = 최대값(itemid);
					if ((구매최저가 != 0) && (구매최저가 < 판매최고가)) {
						gm.sendPackets(new S_ChatPacket(gm,
								"검출됨! [템 " + itemid + " : [구매값 " + 구매최저가 + "] [매입값 " + 판매최고가 + "]"));
					}
					cnt++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parseStat(String s) throws Exception {
		if (s.equalsIgnoreCase("힘"))
			return "str";
		else if (s.equalsIgnoreCase("덱스"))
			return "dex";
		else if (s.equalsIgnoreCase("콘"))
			return "con";
		else if (s.equalsIgnoreCase("인트"))
			return "int";
		else if (s.equalsIgnoreCase("위즈"))
			return "wis";
		else if (s.equalsIgnoreCase("카리"))
			return "cha";
		throw new Exception(s);
	}

	public void fullstat(L1PcInstance pc, String param) {
		try {
			String[] arr = param.split(" ");
			if (arr == null || arr.length < 2)
				throw new Exception();

			String s = parseStat(arr[0]);
			MJFullStater.running(pc, s, Integer.parseInt(arr[1]));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(
					String.format(".스텟 [힘/덱스/콘/인트/위즈/카리] [올릴수] 남은스텟 %d", (pc.getLevel() - 50 - pc.getBonusStats()))));
		}
	}

	private void 좌표(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 5 > curtime) {
				long time = (pc.getQuizTime2() + 5) - curtime;
				pc.sendPackets(new S_SystemMessage(time + "초 후 사용할 수 있습니다."));
				return;
			}
			Updator.exec(
					"UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)",
					new Handler() {
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setString(1, pc.getAccountName());
						}
					});
			pc.sendPackets(new S_SystemMessage("해당 계정 모든 케릭터가 기란으로 좌표가 변경 되었습니다"));
			pc.setQuizTime(curtime);
		} catch (Exception e) {
		}
	}

	private void standBy(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("켬")) {
				if (Config.STANDBY_SERVER) {
					gm.sendPackets("이미 대기 상태로 돌입하였습니다.");
					return;
				}
				Config.STANDBY_SERVER = true;
				L1World.getInstance()
						.broadcastPacketToAll(new S_ChatPacket("서버 상태: 오픈 대기 중 / 일부 기능이 차단됩니다.", Opcodes.S_MESSAGE));
			} else if (status.equalsIgnoreCase("끔")) {
				if (!Config.STANDBY_SERVER) {
					gm.sendPackets("대기 상태가 아닙니다.");
					return;
				}

				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						// Config.load();
						Config.STANDBY_SERVER = false;

						S_PacketBox grn = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "서버 상태: 오픈");
						L1World.getInstance().broadcastServerMessage("서버 상태: 오픈");

						L1World.getInstance().getAllPlayerStream().filter(pc -> pc != null).forEach(pc -> {
							if (pc.getAI() != null) {
								if (pc.getAI().getBotType() == MJBotType.ILLUSION
										|| pc.getAI().getBotType() == MJBotType.FISH
										|| pc.getAI().getBotType() == MJBotType.WANDER)
									pc.getAI().setRandLawful();
							} else {
								pc.sendPackets(grn, false);
							}
						});
						grn.clear();
					}
				});
			}
		} catch (Exception eee) {
			gm.sendPackets(".오픈대기 [켬/끔] 으로 입력하세요.");
			gm.sendPackets("켬 - 오픈대기 상태로 전환 | 끔 - 일반모드로 게임시작");
		}
	}

	private void 메모리반환(L1PcInstance gm) {
		gm.sendPackets("메모리가 반환되었습니다.");
		System.out.println("강제로 가비지 처리를 진행 합니다.");
		System.gc();
		System.out.println("메모리 정리가 완료 되었습니다.");
	}

	private void hold(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				holdnow(gm, target);
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".감옥 캐릭명 으로 입력해주세요.");
		}
	}

	private void holdnow(L1PcInstance gm, L1PcInstance target) {
		try {
			// L1Teleport.teleport(target, 32736, 32799, (short) 34, 5, true);
			// L1Teleport.teleport(target, 32835, 32782, (short) 701, 5, true);
			target.start_teleport(32835, 32782, 701, target.getHeading(), 169, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 님 감옥으로 이동됨.").toString());
			target.sendPackets("감옥에 감금 되었습니다.");
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void nocall(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = null; // q
			target = L1World.getInstance().getPlayer(pcName);
			if (target != null) { // 타겟
				// L1Teleport.teleport(target, 33437, 32812, (short) 4, 5,
				// true);
				target.start_teleport(33437, 32812, 4, target.getHeading(), 169, true, true);
			} else {
				gm.sendPackets("접속중이지 않는 유저 ID 입니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".가라 (보낼케릭터명) 으로 입력해 주세요.");
		}
	}

	private void searchObject(L1PcInstance gm, int type, String name) {
		try {
			String qry = null;
			switch (type) {
			case 0:// etcitem
				qry = String.format("select item_id, name, name_id from etcitem where name Like '%s'", name);
				break;
			case 1:// weapon
				qry = String.format("select item_id, name, name_id from weapon where name Like '%s'", name);
				break;
			case 2: // armor
				qry = String.format("select item_id, name, name_id from armor where name Like ' '%s'", name);
				break;
			case 3: // npc
				qry = String.format("select npcid, name, note from npc where name Like ' '%s'", name);
				break;
			case 4: // polymorphs
				qry = String.format("select polyid, name,id from polymorphs where name Like ' '%s'", name);
				break;
			case 5: // npc(gfxid)
				qry = String.format("select gfxid, name,note from npc where name Like ' '%s'", name);
				break;
			default:
				gm.sendPackets(".검색 [0~5] [name]을 입력 해주세요.");
				gm.sendPackets("0=잡템, 1=무기, 2=갑옷, 3=엔피시, 4=변신, 5=엔피시(gfxid)");
				return;
			}

			Selector.exec(qry, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					int i = 0;
					while (rs.next()) {
						++i;
						gm.sendPackets(
								String.format("[%s]-[%s]-%s", rs.getString(1), rs.getString(2), rs.getString(3)));
					}
					gm.sendPackets(String.format("총 [%d]개 검색이 되었습니다.", i));
				}
			});
		} catch (Exception e) {
		}
	}

	private void redo(L1PcInstance pc, String arg) {
		try {
			String lastCmd = _lastCommands.get(pc.getId());
			if (arg.isEmpty()) {
				pc.sendPackets("커맨드 " + lastCmd + " 을(를) 재실행합니다.");
				handleCommands(pc, lastCmd);
			} else {
				StringTokenizer token = new StringTokenizer(lastCmd);
				String cmd = token.nextToken() + " " + arg;
				pc.sendPackets("커맨드 " + cmd + " 을(를) 재실행합니다.");
				handleCommands(pc, cmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			pc.sendPackets(".재실행 커맨드에러");
		}
	}

	private void unprison(L1PcInstance pc, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				unprisonnow(pc, target);
			} else {
				pc.sendPackets(".마을 캐릭명");
				pc.sendPackets("그런 이름의 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			pc.sendPackets(".마을 캐릭명");
		}
	}

	private void unprisonnow(L1PcInstance gm, L1PcInstance target) {
		try {
			int i = 33437;
			int j = 32803;
			short k = 4;
			// L1Teleport.teleport(target, i, j, k, 5, false);
			target.start_teleport(i, j, k, 5, 169, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 님을 마을로 이동.").toString());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void unprison2(L1PcInstance pc, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				unprisonnow2(pc, target);
			} else {
				pc.sendPackets(".숨계 캐릭명");
				pc.sendPackets("그런 이름의 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			pc.sendPackets(".숨계 캐릭명");
		}
	}

	private void unprisonnow2(L1PcInstance gm, L1PcInstance target) {
		try {
			int i = 32681;
			int j = 32853;
			short k = 2005;
			// L1Teleport.teleport(target, i, j, k, 5, false);
			target.start_teleport(i, j, k, target.getHeading(), 169, false, true);
			gm.sendPackets((new StringBuilder()).append(target.getName()).append(" 님을 숨계로 이동.").toString());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void chatx(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = null;
			target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target.killSkillEffectTimer(L1SkillId.STATUS_CHAT_PROHIBITED);
				target.sendPackets(new S_SkillIconGFX(36, 0));
				target.sendPackets(new S_ServerMessage(288));
				gm.sendPackets("해당캐릭의 채금을 해제 했습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".채금해제 캐릭터명 이라고 입력해 주세요.");
		}
	}

	private void tell(L1PcInstance gm) {
		try {
			gm.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, gm, gm.getWeapon()), true);
			gm.start_teleport(gm.getX(), gm.getY(), gm.getMapId(), gm.getHeading(), 169, true, false);
		} catch (Exception e) {
		}
	}

	public void levelup2(L1PcInstance gm, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String user = tok.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(user);
			int level = Integer.parseInt(tok.nextToken());
			if (level == target.getLevel()) {
				return;
			}
			if (!IntRange.includes(level, 1, 99)) {
				gm.sendPackets("1-99의 범위에서 지정해 주세요");
				return;
			}
			target.setExp(ExpTable.getExpByLevel(level));
			gm.sendPackets(target.getName() + "님의 레벨이 변경됨! .검 [캐릭명]으로 확인요망");
		} catch (Exception e) {
			gm.sendPackets(".렙작 [캐릭명] [레벨] 입력");
		}
	}

	private void NoDelayUser(L1PcInstance pc) {
		int SearchCount = 0;
		pc.sendPackets("----------------------------------------------------");
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player.getNoDelayTime() > 0 || player.getSpeedHackCount() > 10) {
				String type = player.getNoDelayTime() > 0 ? "(노딜)" : "";
				type += player.getSpeedHackCount() > 10 ? "(스핵)" : "";

				pc.sendPackets("Lv." + player.getLevel() + ", " + player.getName() + " " + type);
				SearchCount++;
			}
		}
		pc.sendPackets(SearchCount + "명의 노딜사용자 발견!");
		pc.sendPackets("----------------------------------------------------");
	}

	private void 탐선물(L1PcInstance gm, String param) {
		// TODO 자동 생성된 메소드 스텁
		try {
			StringTokenizer st = new StringTokenizer(param);
			String 이름 = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			L1PcInstance 유저 = L1World.getInstance().getPlayer(이름);
			if (유저 != null) {
				유저.getNetConnection().getAccount().tam_point += id;
				유저.getNetConnection().getAccount().updateTam();
				try {
					유저.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, 유저.getNetConnection()), true);
				} catch (Exception e) {
				}
				gm.sendPackets(유저.getName() + "에게 탐 " + id + "개를 지급 하였습니다.");
				Message.getInstance().get_system_message(유저, "\\aA당신에게 메티스님께서 탐 '\\aG" + id + "\\aA' 을 지급 하였습니다.");
			} else
				gm.sendPackets("존재하지 않는 유저 입니다.");
		} catch (Exception e) {
		}
	}

	private void allrecall(L1PcInstance gm) {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (!pc.isGm() && !pc.isPrivateShop()) {
					recallnow(gm, pc);
				}
			}
		} catch (Exception e) {
			gm.sendPackets(".전체소환 커맨드 에러");
		}
	}

	private void 봇소환(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String 이름 = st.nextToken();

			L1PcInstance target = L1World.getInstance().getPlayer(이름);
			// L1Teleport.teleport(target, gm.getX(), gm.getY(), gm.getMapId(),
			// target.getHeading(), true);
			target.start_teleport(gm.getX(), gm.getY(), gm.getMapId(), target.getHeading(), 169, true, true);
			gm.sendPackets("게임 마스터에게 소환되었습니다.");
		} catch (Exception e) {
			gm.sendPackets(".봇소환 캐릭명");
		}
	}

	private void recallnow(L1PcInstance gm, L1PcInstance target) {
		try {
			// L1Teleport.teleportToTargetFront(target, gm, 2 , 0);
			L1Teleport.getInstance().teleportToTargetFront(gm, target, 2, true);
			// target.sendPackets("게임 마스터에게 소환되었습니다."));
		} catch (Exception e) {
			_log.log(Level.SEVERE, "", e);
		}
	}

	private void ShopKick(L1PcInstance gm, String param) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
				gm.sendPackets((new StringBuilder()).append(target.getName()).append("님을 추방 했습니다.").toString());
				GameServer.disconnectChar(target);
			} else {
				gm.sendPackets("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".상점추방 캐릭명");
		}
	}

	private void icon(L1PcInstance pc, String param) {
		try {
			//StringTokenizer st = new StringTokenizer(param);
			//int iconId = Integer.parseInt(st.nextToken(), 10);
			/*pc.sendPackets(new S_EtcPacket(3));	// 카운트다운
			pc.sendPackets(new S_EtcPacket(2));	// 좌측하단 표시
			pc.sendPackets(new S_EtcPacket(4));
			pc.sendPackets(new S_EtcPacket(5));	// 시간 삭제
			pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_LIST, iconId));*/
			SC_EVENT_COUNTDOWN_NOTI_PACKET.send(pc, 10, "111");
			//pc.sendPackets(MJPacketFactory.createTime(10));
			//pc.sendPackets(MJPacketFactory.create(MJPacketFactory.MSPF_IDX_OFFTIME));
		} catch (Exception exception) {
			pc.sendPackets(".아이콘 [actid]를 입력 해주세요.");
		}
	}

	private void chainfo(L1PcInstance gm, String param) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			String s = stringtokenizer.nextToken();
			gm.sendPackets(new S_Chainfo(1, s));
		} catch (Exception exception21) {
			gm.sendPackets(new S_SystemMessage(".케릭검사 캐릭터명"));
		}
	}

	private void 인형청소(L1PcInstance gm) {
		int count = 0;
		int ccount = 0;
		for (Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1DollInstance) {
				L1DollInstance 인형 = (L1DollInstance) obj;
				if (인형.getMaster() == null) {
					count++;
					인형.deleteMe();
				} else if (((L1PcInstance) 인형.getMaster()).getNetConnection() == null) {
					ccount++;
					인형.deleteMe();
				}
			}
		}
		gm.sendPackets("인형청소 갯수 - 주인X: " + count + "  주인접종: " + ccount);
	}

	private void CharacterBalance(L1PcInstance pc, String param) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			StringTokenizer st = new StringTokenizer(param);

			String charName = st.nextToken();
			int addDamage = Integer.parseInt(st.nextToken());
			int addDamageRate = Integer.parseInt(st.nextToken());
			int addReduction = Integer.parseInt(st.nextToken());
			int addReductionRate = Integer.parseInt(st.nextToken());

			L1PcInstance player = L1World.getInstance().getPlayer(charName);

			if (player != null) {
				player.setAddDamage(addDamage);
				player.setAddDamageRate(addDamageRate);
				player.setAddReduction(addReduction);
				player.setAddReductionRate(addReductionRate);
				player.save();
			} else {
				int i = 0;
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement(
						"update characters set AddDamage = ?, AddDamageRate = ?, AddReduction = ?, AddReductionRate = ? where char_name = ?");
				pstm.setInt(++i, addDamage);
				pstm.setInt(++i, addDamageRate);
				pstm.setInt(++i, addReduction);
				pstm.setInt(++i, addReductionRate);
				pstm.setString(++i, charName);
				pstm.executeQuery();
			}

		} catch (Exception e) {
			pc.sendPackets(".밸런스 [캐릭명] [추타] [추타확률] [리덕] [리덕확률]");
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void AddAccount(L1PcInstance gm, String account, String passwd, String Ip, String Host) {
		try {
			String login = null;
			String password = null;
			java.sql.Connection con = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = null;
			PreparedStatement pstm = null;

			password = passwd;

			statement = con.prepareStatement("select * from accounts where login Like '" + account + "'");
			ResultSet rs = statement.executeQuery();

			if (rs.next())
				login = rs.getString(1);
			if (login != null) {
				gm.sendPackets("이미 계정이 있습니다.");
				return;
			} else {
				String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,charslot=?,gamepassword=?,notice=?";
				pstm = con.prepareStatement(sqlstr);
				pstm.setString(1, account);
				pstm.setString(2, password);
				pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				pstm.setInt(4, 0);
				pstm.setString(5, Ip);
				pstm.setString(6, Host);
				pstm.setInt(7, 0);
				pstm.setInt(8, 6);
				pstm.setInt(9, 0);
				pstm.setInt(10, 0);
				pstm.execute();
				gm.sendPackets("계정 추가가 완료되었습니다.");
			}

			rs.close();
			pstm.close();
			statement.close();
			con.close();
		} catch (Exception e) {
		}
	}

	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // 숫자가 아니라면
					&& !Character.isUpperCase(str.charAt(i)) // 대문자가 아니라면
					&& !Character.isLowerCase(str.charAt(i))) { // 소문자가 아니라면
				check = false;
				break;
			}
		}
		return check;
	}

	private void reloadHouse(L1PcInstance gm, String cmd) {
		try {
			HouseTable.reload();
			gm.sendPackets("아지트 입찰시간이 재갱신되었습니다.");
		} catch (Exception e) {
			gm.sendPackets(".아지트갱신 으로 입력하세요.");
		}
	}

	// 인벤삭제
	private void InventoryDelete(L1PcInstance pc, String param) {
		try {
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (!item.isEquipped()) {
					pc.getInventory().removeItem(item);
				}
			}
		} catch (Exception e) {
			pc.sendPackets(".인벤삭제");
		}
	}

	private void targetInventoryDelete(L1PcInstance user, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String char_name = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(char_name);
			for (L1ItemInstance item : user.getInventory().getItems()) {
				if (!item.isEquipped()) {
					target.getInventory().removeItem(item);
				}
			}
		} catch (Exception e) {
			user.sendPackets(".유저인벤삭제 [접속중인 캐릭명] 입력.");
		}
	}

	private void addaccount(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String user = tok.nextToken();
			String passwd = tok.nextToken();

			if (user.length() < 4) {
				gm.sendPackets("입력하신 계정명의 자릿수가 너무 짧습니다.");
				gm.sendPackets("최소 4자 이상 입력해 주십시오.");
				return;
			}
			if (passwd.length() < 4) {
				gm.sendPackets("입력하신 암호의 자릿수가 너무 짧습니다.");
				gm.sendPackets("최소 4자 이상 입력해 주십시오.");
				return;
			}

			if (passwd.length() > 12) {
				gm.sendPackets("입력하신 암호의 자릿수가 너무 깁니다.");
				gm.sendPackets("최대 12자 이하로 입력해 주십시오.");
				return;
			}

			if (isDisitAlpha(passwd) == false) {
				gm.sendPackets("암호에 허용되지 않는 문자가 포함 되어 있습니다.");
				return;
			}
			AddAccount(gm, user, passwd, "127.0.0.1", "127.0.0.1");
		} catch (Exception e) {
			gm.sendPackets(".계정추가 [계정명] [암호] 를 입력 해주세요.");
		}
	}

	private void allpresent(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int itemid = Integer.parseInt(st.nextToken(), 10);
			int enchant = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			Collection<L1PcInstance> player = null;
			player = L1World.getInstance().getAllPlayers();
			for (L1PcInstance target : player) {
				if (target == null)
					continue;
				if (!target.isGhost() && !target.isPrivateShop()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setCount(count);
					item.setEnchantLevel(enchant);
					if (item != null) {
						if (target.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							target.getInventory().storeItem(item);
						}
					}
					/*target.sendPackets(new S_GMHtml("전체선물:" + target.getName() + "",
							"아이템:" + item.getLogName() + " 수량:" + count + " 획득"));*/
					target.sendPackets("전체 선물: " + item.getLogName() + " " + count + "개 획득 인벤토리 확인하세요");
					target.sendPackets(new S_SkillSound(target.getId(), 1091));// 비둘기액션
					target.sendPackets(new S_SkillSound(target.getId(), 4856));// 하트액션
					// target.sendPackets("\\aD운영자의 전체선물 :
					// "+item.getLogName())); // item.getLogName
					// //item.getViewName
				}
			}
		} catch (Exception exception) {
			gm.sendPackets(".전체선물 [템ID] [인첸] [갯수]");
		}
	}

	private void returnEXP(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				int oldLevel = target.getLevel();
				int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
				int exp = 0;
				if (oldLevel >= 1 && oldLevel < 11) {
					exp = 0;
				} else if (oldLevel >= 11 && oldLevel < 45) {
					exp = (int) (needExp * 0.1);
				} else if (oldLevel == 45) {
					exp = (int) (needExp * 0.09);
				} else if (oldLevel == 46) {
					exp = (int) (needExp * 0.08);
				} else if (oldLevel == 47) {
					exp = (int) (needExp * 0.07);
				} else if (oldLevel == 48) {
					exp = (int) (needExp * 0.06);
				} else if (oldLevel >= 49) {
					exp = (int) (needExp * 0.05);
				}
				target.addExp(+exp);
				target.save();
				target.saveInventory();

				gm.sendPackets("해당 캐릭터 +5 상승");
			} else {
				gm.sendPackets("해당 캐릭터 미접속 상태.");
			}
		} catch (Exception e) {
			gm.sendPackets(".경치복구 [캐릭명]");
		}
	}

	// .계정 -----------------------------------------------------------------
	// 같은계정에 있는 캐릭 검사
	private void account_Cha(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			account_Cha2(gm, name);
		} catch (Exception e) {
			gm.sendPackets(".계정 아이디");
		}
	}

	private void account_Cha2(L1PcInstance gm, String param) {
		try {
			String s_account = null;
			String s_name = param;
			String s_level = null;
			String s_clan = null;
			String s_bonus = null;
			String s_online = null;
			String s_hp = null;
			String s_mp = null;
			String s_type = null;// 추가
			int count = 0;
			int count0 = 0;
			java.sql.Connection con0 = null; // 이름으로 objid를 검색하기 위해
			con0 = L1DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement0 = null;
			statement0 = con0.prepareStatement(
					"select account_name, Clanname  from characters where char_name = '" + s_name + "'");
			ResultSet rs0 = statement0.executeQuery();
			while (rs0.next()) {
				s_account = rs0.getString(1);
				s_clan = rs0.getString(2);
				gm.sendPackets("\\aD------------------------------------------");
				gm.sendPackets("\\aE캐릭 : " + s_name + "(" + s_account + ")  클랜 : " + s_clan);// +"
				// 클래스:"
				// +
				// s_type
				count0++;
			}
			java.sql.Connection con = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = null;
			statement = con.prepareStatement(
					"select " + "char_name," + "level," + "Clanname," + "BonusStatus," + "OnlineStatus," + "MaxHp,"
							+ "MaxMp, " + "Type " + " from characters where account_name = '" + s_account + "'");
			gm.sendPackets("\\aD------------------------------------------");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				s_name = rs.getString(1);
				s_level = rs.getString(2);
				s_clan = rs.getString(3);
				s_bonus = rs.getString(4);
				s_online = rs.getString(5);
				s_hp = rs.getString(6);
				s_mp = rs.getString(7);
				s_type = rs.getString(8);
				gm.sendPackets("접속:[" + s_online + "] 랩:" + s_level + " [" + s_name + "] 클래스:" + s_type + "");
				count++;
			}
			rs0.close();
			statement0.close();
			con0.close();
			rs.close();
			statement.close();
			con.close();
			gm.sendPackets("\\aF0군주 1기사 2요정 3법사 4다엘 5용기사 6환술");
			gm.sendPackets("\\aD------------------------------------------");
		} catch (Exception e) {
		}
	}

	// .계정 -----------------------------------------------------------------

	private void Pvp(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String type = st.nextToken();

			if (type.equals("켬")) {
				Config.ALT_NONPVP = true;
				Config.setParameterValue("AltNonPvP", "true");
				L1World.getInstance()
						.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "지금이후 PvP가 정상적으로 가능합니다."));
			} else if (type.equals("끔")) {
				Config.ALT_NONPVP = false;
				Config.setParameterValue("AltNonPvP", "false");
				L1World.getInstance()
						.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "지금이후 PvP가 일정시간동안 불가능 합니다."));
			}

		} catch (Exception exception) {
			gm.sendPackets(".피케이 [켬/끔]");
		}
	}

	private void search_banned(L1PcInstance paramL1PcInstance) {
		Selector.exec(
				"select accounts.login, characters.char_name from accounts,characters where accounts.banned=62 || accounts.banned=87 || accounts.banned=95 and accounts.login=characters.account_name ORDER BY accounts.login ASC",
				new FullSelectorHandler() {
					@Override
					public void result(ResultSet rs) throws Exception {
						int i = 0;
						while (rs.next()) {
							++i;
							paramL1PcInstance
									.sendPackets(String.format("계정:[%s], 캐릭터명:[%s]", rs.getString(1), rs.getString(2)));
						}

						paramL1PcInstance.sendPackets(String.format("총 [%d]개의 압류계정/캐릭터가 검색되었습니다.", i));
					}
				});
	}

	private void accountdel(L1PcInstance gm, String param) {
		StringTokenizer tokenizer = new StringTokenizer(param);
		String chaname = tokenizer.nextToken();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, chaname);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				gm.sendPackets(String.format("DB에 %s 캐릭명이 존재 하지 않습니다.", chaname));
				return;
			}

			String account = rs.getString("account_name");
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("select * from accounts WHERE login=?");
			pstm.setString(1, account);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				gm.sendPackets(String.format("계정명 %s 이(가) 존재 하지 않습니다.", account));
				return;
			}

			IpTable.getInstance();
			IpTable.reload();

			String host = rs.getString("ip");
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("UPDATE accounts SET banned = 0 WHERE login= ?");
			pstm.setString(1, account);
			pstm.executeUpdate();
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("delete from ban_ip where ip=?");
			pstm.setString(1, host);
			pstm.executeUpdate();

			gm.sendPackets(String.format("%s(%s) : %s 밴 해제 완료.", chaname, account, host));
		} catch (Exception e) {
			gm.sendPackets(".압류해제 캐릭명으로 입력해주세요.");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	// private void GiveAbyssPoint(L1PcInstance pc, String poby) {
	// try {
	// StringTokenizer st = new StringTokenizer(poby);
	// String pobyname = st.nextToken();
	// int point = Integer.parseInt(st.nextToken());
	// L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
	// if (target != null) {
	// target.addAbysspoint(point);
	// pc.sendPackets(target.getName() + "님에게 [어비스포인트 " +
	// point + "점] 지급."));
	// }
	// } catch (Exception e) {
	// pc.sendPackets(".어포지급 [지급캐릭명] [지급할포인트]"));
	// }
	//
	// }

	// private void GiveClanPoint(L1PcInstance pc, String poby) { // 혈맹 경험치 부여
	// try {
	// StringTokenizer st = new StringTokenizer(poby);
	// String pobyname = st.nextToken();
	// int point = Integer.parseInt(st.nextToken());
	// L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
	// if (target != null) {
	// if (target.getClanid() != 0) {
	// L1Clan TargetClan = L1World.getInstance().getClan(target.getClanname());
	// TargetClan.addClanExp(point);
	// ClanTable.getInstance().updateClan(TargetClan);
	// pc.sendPackets(target.getClanname() + " 혈맹에게 [경험치 " +
	// point + "] 지급."));
	// for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
	// tc.sendPackets("게임마스터로부터 혈맹경험치 [" + point + "] 를 지급
	// 받았습니다."));
	// }
	// } else {
	// pc.sendPackets(target.getName() + "님은 혈맹에 속해 있지
	// 않습니다."));
	// }
	// } else {
	// pc.sendPackets(new S_ServerMessage(73, pobyname));
	// }
	// } catch (Exception e) {
	// pc.sendPackets(".혈맹경험치 [지급할혈맹군주이름] [지급할 포인트]"));
	// }
	//
	// }

	private void changePassword(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String user = tok.nextToken();
			String passwd = tok.nextToken();

			if (passwd.length() < 4) {
				gm.sendPackets("입력하신 암호의 자릿수가 너무 짧습니다.");
				gm.sendPackets("최소 4자 이상 입력해 주십시오.");
				return;
			}

			if (passwd.length() > 12) {
				gm.sendPackets("입력하신 암호의 자릿수가 너무 깁니다.");
				gm.sendPackets("최대 12자 이하로 입력해 주십시오.");
				return;
			}

			if (isDisitAlpha(passwd) == false) {
				gm.sendPackets("암호에 허용되지 않는 문자가 포함되었습니다.");
				return;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(user);
			if (target != null) {
				to_Change_Passwd(gm, target, passwd);
			} else {
				if (!to_Change_Passwd(gm, user, passwd))
					gm.sendPackets("그런 이름을 가진 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".암호변경 [캐릭명] [암호]로 입력해주세요.");
		}
	}

	private void to_Change_Passwd(L1PcInstance gm, L1PcInstance pc, String passwd) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String password = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			password = passwd;
			statement = con.prepareStatement(
					"select account_name from characters where char_name Like '" + pc.getName() + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login Like '" + login + "'");
				pstm.setString(1, password);
				pstm.execute();
				gm.sendPackets("암호변경 계정: [" + login + "] 암호: [" + passwd + "]");
				gm.sendPackets(pc.getName() + " 암호변경 완료.");
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
	}

	private boolean to_Change_Passwd(L1PcInstance pc, String name, String passwd) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String password = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			password = passwd;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + name + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login Like '" + login + "'");
				pstm.setString(1, password);
				pstm.execute();
				pc.sendPackets("암호변경 계정: [" + login + "] 암호: [" + passwd + "]");
				pc.sendPackets("해당 캐릭터 암호변경 완료. (미접속중)");
			}
			return true;
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
		return false;
	}

	private void 퀴즈체인지(L1PcInstance gm, L1PcInstance pc, String newquiz) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String quiz = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			quiz = newquiz;
			statement = con.prepareStatement(
					"select account_name from characters where char_name Like '" + pc.getName() + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login Like '" + login + "'");
				pstm.setString(1, quiz);
				pstm.execute();
				gm.sendPackets("퀴즈변경계정: [" + login + "] 퀴즈: [" + quiz + "]");
				gm.sendPackets(pc.getName() + " 님의 퀴즈변경 완료.");
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
	}

	private boolean 퀴즈체인지(L1PcInstance pc, String name, String newquiz) {
		PreparedStatement statement = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String login = null;
			String quiz = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			quiz = newquiz;
			statement = con.prepareStatement("select account_name from characters where char_name Like '" + name + "'");
			rs = statement.executeQuery();

			while (rs.next()) {
				login = rs.getString(1);
				pstm = con.prepareStatement("UPDATE accounts SET quiz=? WHERE login Like '" + login + "'");
				pstm.setString(1, quiz);
				pstm.execute();
				pc.sendPackets("퀴즈변경계정: [" + login + "] 암호: [" + quiz + "]");
				pc.sendPackets("해당 캐릭터 퀴즈변경 완료. (미접속중)");
			}
			return true;
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(statement);
			SQLUtil.close(con);
		}
		return false;
	}

	private static int delItemlist[] = { 307, 308, 309, 310, 311, 312, 313, 314, 21095, 30146, 30147, 30150 };

	public synchronized static void 아놀드이벤트삭제() {
		try {
			if (delItemlist.length <= 0)
				return;

			for (L1PcInstance tempPc : L1World.getInstance().getAllPlayers()) {
				if (tempPc == null)
					continue;
				for (int i = 0; i < delItemlist.length; i++) {
					L1ItemInstance[] item = tempPc.getInventory().findItemsId(delItemlist[i]);
					if (item != null && item.length > 0) {
						for (int o = 0; o < item.length; o++) {
							tempPc.getInventory().removeItem(item[o]);
						}
					}
					try {
						PrivateWarehouse pw = WarehouseManager.getInstance()
								.getPrivateWarehouse(tempPc.getAccountName());
						L1ItemInstance[] item2 = pw.findItemsId(delItemlist[i]);
						if (item2 != null && item2.length > 0) {
							for (int o = 0; o < item2.length; o++) {
								pw.removeItem(item2[o]);
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getClanid() > 0) {
							ClanWarehouse cw = WarehouseManager.getInstance().getClanWarehouse(tempPc.getClanname());
							L1ItemInstance[] item3 = cw.findItemsId(delItemlist[i]);
							if (item3 != null && item3.length > 0) {
								for (int o = 0; o < item3.length; o++) {
									cw.removeItem(item3[o]);
								}
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getPetList().size() > 0) {
							for (L1NpcInstance npc : tempPc.getPetList().values()) {
								L1ItemInstance[] pitem = npc.getInventory().findItemsId(delItemlist[i]);
								if (pitem != null && pitem.length > 0) {
									for (int o = 0; o < pitem.length; o++) {
										npc.getInventory().removeItem(pitem[o]);
									}
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
			try {
				for (L1Object obj : L1World.getInstance().getAllItem()) {
					if (!(obj instanceof L1ItemInstance))
						continue;
					L1ItemInstance temp_item = (L1ItemInstance) obj;
					if (temp_item.getItemOwner() == null) {
						if (temp_item.getX() == 0 && temp_item.getY() == 0)
							continue;
					}
					for (int ii = 0; ii < delItemlist.length; ii++) {
						if (delItemlist[ii] == temp_item.getItemId()) {
							L1Inventory groundInventory = L1World.getInstance().getInventory(temp_item.getX(),
									temp_item.getY(), temp_item.getMapId());
							groundInventory.removeItem(temp_item);
							break;
						}
					}

				}
			} catch (Exception e) {
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < delItemlist.length; i++) {
				sb.append(+delItemlist[i]);
				if (i < delItemlist.length - 1) {
					sb.append(",");
				}
			}
			Delete(sb.toString());

			/*
			 * for(int i = 0; i < delItemlist.length; i++){
			 * Delete(delItemlist[i]); wareDelete(delItemlist[i]);
			 * ClanwareDelete(delItemlist[i]); }
			 */
		} catch (Exception e) {
		}
	}

	private static void Delete(String id_name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete FROM _cha_inv_items WHERE item_id IN (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
		}
		try {
			pstm = con.prepareStatement("delete FROM character_warehouse WHERE item_id in (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
		}
		try {
			pstm = con.prepareStatement("delete FROM clan_warehouse WHERE item_id in (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void mjClear(L1PcInstance gm, String param) {
		int id = 0;
		int space = 0;
		int yspace = 0;
		try {
			String[] arr = param.split(" ");
			id = Integer.parseInt(arr[0]);
			space = Integer.parseInt(arr[1]);
			yspace = Integer.parseInt(arr[2]);
		} catch (Exception e) {
			return;
		}

		GeneralThreadPool.getInstance().execute(new EffectThread(gm, id, space, yspace));

	}

	public class EffectThread implements Runnable {
		private L1PcInstance _owner;
		private int _effect;
		private int _x_space;
		private int _z_space;

		public EffectThread(L1PcInstance owner, int effect, int x, int z) {
			_owner = owner;
			_effect = effect;
			_x_space = x;
			_z_space = z;
		}

		@Override
		public void run() {
			if (_owner == null)
				return;

			ArrayList<L1Object> objs = L1World.getInstance().getVisibleObjects(_owner);
			int tx = 0;
			int ty = 0;
			int cx = 0;
			int cy = 0;
			int[][] cpos = new int[4][2];

			try {
				S_EffectLocation[] pcks = new S_EffectLocation[4];
				for (int width = 15; width >= 0; width -= _z_space) {
					int left = _owner.getX() - width;
					int top = _owner.getY() - width;
					int right = _owner.getX() + width;
					int bottom = _owner.getY() + width;
					for (int i = 0; i < width * 2; i += _x_space) {
						cpos[0][0] = left + i;
						cpos[0][1] = top;

						cpos[1][0] = right;
						cpos[1][1] = top + i;

						cpos[2][0] = right - i;
						cpos[2][1] = bottom;

						cpos[3][0] = left;
						cpos[3][1] = bottom - i;
						for (int j = 0; j < 4; j++) {
							pcks[j] = new S_EffectLocation(cpos[j][0], cpos[j][1], _effect);
							_owner.sendPackets(pcks[j], false);
							Broadcaster.broadcastPacket(_owner, pcks[j], true);

							for (L1Object obj : objs) {
								if (!(obj instanceof L1MonsterInstance))
									continue;

								cx = Math.abs(obj.getX() - cpos[j][0]);
								cy = Math.abs(obj.getY() - cpos[j][1]);
								if (cx < 3 && cy < 3)
									((L1MonsterInstance) obj).receiveDamage(_owner,
											((L1MonsterInstance) obj).getCurrentHp());
							}
						}
					}
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void findMagicDodge(L1PcInstance gm, String param) {
		L1PcInstance pc = L1World.getInstance().findpc(param);
		if (pc != null) {
			gm.sendPackets(String.format("%s의 마법 회피율 %d", param, pc.getMagicDodgeProbability()));
		} else {
			gm.sendPackets(String.format("%s를 찾을 수 없습니다.", param));
		}
	}

	@SuppressWarnings("resource")
	private void testCommands(L1PcInstance gm, String param) {
		SC_SPELL_BUFF_NOTI.sendFatigueOn(gm, 2, 10);
		// gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, gm, 1, 15));

		/*
		 * int x = gm.getX(); int y = gm.getY(); MJRectangle rt =
		 * (MJRectangle)MJRectangle.newInstance(gm.getX() - 1, gm.getY() - 1,
		 * gm.getX() + 1, gm.getY() + 1, gm.getMapId()); Box[] boxes =
		 * rt.to_line_box(); for(Box box : boxes)
		 * gm.sendPackets(SC_BOX_ATTR_CHANGE_NOTI_PACKET.create_box(gm.getMapId(
		 * ), box, 0));
		 */

		// gm.sendPackets(new S_EffectLocation(gm.getX(), gm.getY(), 13923));
		// gm.sendPackets(S_Einhasad.newInstance(gm));
		// gm.sendPackets(new S_CharVisualUpdate(gm));
		// gm.sendPackets(new S_Paralysis(new byte[]{ 0x0d, (byte)0xff,
		// (byte)0xff, 0x07, 0x00 }));

		/*
		 * \\ StringTokenizer st = new StringTokenizer(param); try { int first =
		 * 0; //int second = 0; try { first = Integer.parseInt(st.nextToken());
		 * //second = Integer.parseInt(st.nextToken()); gm.sendPackets(new
		 * S_SkillIconGFX(first, -1)); } catch (NumberFormatException e) { } }
		 * catch (NumberFormatException e) { }
		 */
		// gm.sendPackets(new S_SkillIconGFX(25, 10));

		/*
		 * S_PacketBox box = new S_PacketBox(S_PacketBox.BUFFICON, 2949, 0,
		 * false, true); gm.sendPackets(box);
		 */

		/*
		 * byte[] buff = new byte[10]; for (int i = 7; i < 10; ++i) buff[i] =
		 * 60; gm.sendPackets(new S_UnityIcon(buff));
		 */

		/*
		 * L1Object obj = L1World.getInstance().findNpc(param); if(obj != null)
		 * gm.sendPackets(new S_UseAttackSkill(gm, obj.getId(), 17229,
		 * obj.getX(), obj.getY(), 18));
		 */
		// gm.sendPackets(info, MJEProtoMessages.SC_PARTY_SYNC_PERIODIC_INFO,
		// true);
		/*
		 * SC_PARTY_MEMBER_LIST list = SC_PARTY_MEMBER_LIST.newInstance();
		 * PartyMember member = PartyMember.newInstance();
		 * member.set_accountid(0); member.set_alive_time_stamp(0);
		 * member.set_game_class(gm.getType()); member.set_gender(1);
		 * member.set_hp_ratio(100); int pt = (gm.getY() << 16) & 0xffff0000; pt
		 * |= (gm.getX() & 0x0000ffff); member.set_location(pt);
		 * member.set_mp_ratio(100); member.set_name(gm.getName());
		 * member.set_object_id(gm.getId()); member.set_party_mark(1);
		 * member.set_server_no(1); member.set_world(4); // mapid
		 * list.add_member(member); list.set_leader_name(gm.getName());
		 * gm.sendPackets(list, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
		 */

		/*
		 * SC_PARTY_OPERATION_RESULT_NOTI noti =
		 * SC_PARTY_OPERATION_RESULT_NOTI.newInstance();
		 * noti.set_type(ePARTY_OPERATION_TYPE.
		 * ePARTY_OPERATION_TYPE_INVITE_ACCEPT); noti.set_actor_name("hello");
		 * ProtoOutputStream stream =
		 * ProtoOutputStream.newInstance(noti.getSerializedSize() +
		 * WireFormat.WRITE_EXTENDED_SIZE, 0x033D); gm.sendPackets(stream,
		 * true);
		 */

		// gm.sendPackets(new S_Party("party", gm.getId(), gm.getName(),
		// gm.getName()));
		/*
		 * GeneralThreadPool.getInstance().execute(new Runnable(){
		 * 
		 * @Override public void run(){ for(int i=400; i<2000; ++i){
		 * gm.sendPackets(S_ShowCmd.get(String.valueOf(i)));
		 * gm.sendPackets(S_ShowCmd.getProto8(i));
		 * gm.sendPackets(S_ShowCmd.getProtoA(i));
		 * 
		 * try{ SC_ACTIVE_SPELL_EX_INFO exInfo =
		 * SC_ACTIVE_SPELL_EX_INFO.newInstance(); info inf = info.newInstance();
		 * inf.set_spellid(L1SkillId.DESPERADO - 1); inf.set_spelltype(1);
		 * inf.set_graphic(17235); inf.add_value(1000); exInfo.add_infos(inf);
		 * ProtoOutputStream stream =
		 * ProtoOutputStream.newInstance(exInfo.getSerializedSize() +
		 * WireFormat.WRITE_EXTENDED_SIZE, i); exInfo.writeTo(stream);
		 * gm.sendPackets(stream, true); Thread.sleep(500); }catch(Exception
		 * e){} } } });
		 */
		/*
		 * SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		 * noti.set_noti_type(eNotiType.END);
		 * noti.set_spell_id(L1SkillId.CUBE_AVATAR); noti.set_duration(0);
		 * noti.set_off_icon_id(3101); noti.set_is_good(true);
		 * gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(),
		 * true);
		 */
		// gm.send_effect(17233);
		// gm.send_effect(17235);

		/*
		 * SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		 * noti.set_noti_type(eNotiType.RESTAT);
		 * noti.set_spell_id(L1SkillId.FOCUS_SPRITS); noti.set_duration(15);
		 * noti.set_duration_show_type(eDurationShowType.
		 * TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC); noti.set_on_icon_id(4832);
		 * noti.set_off_icon_id(4832); noti.set_icon_priority(3);
		 * noti.set_tooltip_str_id(5272); noti.set_new_str_id(0);
		 * noti.set_end_str_id(5273); noti.set_is_good(true);
		 * gm.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(),
		 * true); gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_OGRE, 15,
		 * true)); gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_RICH,
		 * 15, true));
		 * gm.sendPackets(S_InventoryIcon.icoNew(L1SkillId.CUBE_AVATAR, 15,
		 * true));
		 */
		// gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0)); // 전사꺼

		// CS_PARTY_CONTROL_REQ pck = CS_PARTY_CONTROL_REQ.newInstance();
		// pck.set_remain_time(10);
		// try {
		// pck.set_event_desc("\\fW$12125".getBytes("MS949"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// gm.sendPackets(pck, MJEProtoMessages.CS_PARTY_CONTROL_REQ, true);

		// gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16,
		// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, gm.getElfAttr()));
		// gm.sendPackets(new S_ACTION_UI(146, 10));
		/*
		 * GeneralThreadPool.getInstance().execute(new Runnable(){
		 * 
		 * @Override public void run(){ int j=0; for(int i=10; i<=14; ++i, ++j){
		 * gm.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16,
		 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, gm.getElfAttr()));
		 * gm.sendPackets(new S_ACTION_UI(145, i));
		 * gm.sendPackets(String.valueOf(i)); try { Thread.sleep(100); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } } });
		 */

		/*
		 * SC_EVENT_COUNTDOWN_NOTI_PACKET pck =
		 * SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance();
		 * pck.set_remain_time(10); try {
		 * pck.set_event_desc("\\fW$12125".getBytes("MS949")); } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); }
		 * gm.sendPackets(pck, MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET,
		 * true);
		 */
		// gm.sendPackets(new S_OwnCharStatus(gm));
		/*
		 * SC_SPECIAL_RESISTANCE_NOTI noti =
		 * SC_SPECIAL_RESISTANCE_NOTI.newInstance(); Value v =
		 * Value.newInstance(); v.set_kind(eKind.FEAR); v.set_value(4);
		 * noti.add_pierce(v);
		 * 
		 * v = Value.newInstance(); v.set_kind(eKind.SPIRIT); v.set_value(30);
		 * noti.add_resistance(v); ProtoOutputStream stream =
		 * ProtoOutputStream.newInstance(noti.getSerializedSize() +
		 * WireFormat.WRITE_EXTENDED_SIZE, 0x03F7); try { noti.writeTo(stream);
		 * gm.sendPackets(stream, true); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */

		// gm.sendPackets(new S_SkillIconShield(4, 0));

		/*
		 * MJCommandArgs args = new
		 * MJCommandArgs().setOwner(gm).setParam(param);
		 * 
		 * 
		 * int mark = 0; L1PcInstance pc = null; try { mark = args.nextInt(); pc
		 * = L1World.getInstance().getPlayer(args.nextString()); } catch
		 * (MJCommandArgsIndexException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } if(pc == null) return;
		 * 
		 * pc.set_mark_status(mark); pc.do_simple_teleport(pc.getX(), pc.getY(),
		 * pc.getMapId());
		 */
		/*
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET box_pck =
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET.newInstance(); Integer r = 2; Integer
		 * a = Integer.parseInt(param); int left = gm.getX() - r; int top =
		 * gm.getY() - r; int right = gm.getX() + r; int bottom = gm.getY() + r;
		 * 
		 * box_pck.set_worldNumber(gm.getMapId()); Box box = Box.newInstance();
		 * box_pck.set_box(box); box_pck.set_attribute(a);
		 * 
		 * // - box.set_sx(left); box.set_sy(top); box.set_ex(right);
		 * box.set_ey(top + 1); gm.sendPackets(box_pck.writeTo(MJEProtoMessages.
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 * 
		 * // | box.set_sx(right - 1); box.set_sy(top); box.set_ex(right);
		 * box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages.
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 * 
		 * // _ box.set_sx(left); box.set_sy(bottom - 1); box.set_ex(right);
		 * box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages.
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 * 
		 * // | box.set_sx(left); box.set_sy(top); box.set_ex(left + 1);
		 * box.set_ey(bottom); gm.sendPackets(box_pck.writeTo(MJEProtoMessages.
		 * SC_BOX_ATTR_CHANGE_NOTI_PACKET));
		 */

		// gm.sendPackets(new S_War(4, gm.getClanname(), "컨트롤"));
		// gm.sendPackets("$" + param);
		// L1World.getInstance().broadcastPacketToAll(new S_IconMessage(true));

		// gm.sendPackets(gm.hasSkillEffect(L1SkillId.SHAPE_CHANGE) + "");
		// gm.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 490),
		// true);
		/*
		 * L1Object obj = L1World.getInstance().findObject(269064985);
		 * L1Character c = (L1Character)obj; MJServerPacketBuilder builder =
		 * null; try { // 파티원 다이, 다시 살아남 //builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6C).addD(
		 * 269064817).addH(0x00); // 리더변경 //builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6A).addD(
		 * 269064817).addH(0x00); // 좌측 파티창에 참가 //builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x69).addD(
		 * 269064817).addS(c.getName()).addC(0).addH(0x00).addD(c.getMapId()).
		 * addH(c.getX()).addH(c.getY()); //builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(0x6E).addC(0x01)
		 * .addD(269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).
		 * addC(0x00); //builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(159).addD(
		 * 269064817).addD(c.getMapId()).addH(c.getX()).addH(c.getY()).addC(0x00
		 * ); builder = new
		 * MJServerPacketBuilder(32).addC(Opcodes.S_EVENT).addC(111).addS(c.
		 * getName()).addH(c.getMapId()).addH(c.getX()).addH(c.getY()); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } byte[] b = builder.toArray(); S_BuilderPacket
		 * s = new S_BuilderPacket(b.length, b); gm.sendPackets(s, true);
		 * builder.close(); builder.dispose();
		 */
		// 269064817
		// gm.sendPackets(new S_Message_YN(3325, 3325, param));
		// gm.sendPackets(S_IconMessage.getGmMessage(String.format("[%s]님의
		// 확성기(일반)메시지", gm.getName())));
		// gm.sendPackets(S_NotificationMessage.get(S_NotificationMessage.DISPLAY_POSITION_TOP,
		// param, MJSimpleRgb.red(), 15));
		// gm.sendPackets(new S_IconMessage(false));

		// gm.start_teleport(gm.getX(), gm.getY(), gm.getMapId(),
		// gm.getHeading(), 169, false, true);
		// gm.sendPackets(new S_OwnCharStatus2(gm));
		// gm.sendPackets(S_WorldPutObject.put(gm));
		/*
		 * if(EventThread.getInstance()._leftBDoor.isClose()){
		 * EventThread.getInstance()._leftBDoor.down();
		 * EventThread.getInstance()._rightBDoor.down(); }else{
		 * EventThread.getInstance()._leftBDoor.up();
		 * EventThread.getInstance()._rightBDoor.up(); }
		 * 
		 * if(EventThread.getInstance()._centerBDoor.isClose()){
		 * EventThread.getInstance()._centerBDoor.down(); }else{
		 * EventThread.getInstance()._centerBDoor.up(); }
		 */

		// gm.sendPackets(new S_DisplayEffect(Integer.parseInt(param)));
		/*
		 * Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		 * String[] command = new String[]{ "cmd", "/C",
		 * "C:\\Program Files\\Java\\jdk1.8.0_101\\bin\\jstack",
		 * MJProcessPlayer.getPid(), ">",
		 * String.format("dump\\[%02d-%02d-%02d]dump.txt",
		 * cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),
		 * cal.get(Calendar.SECOND)) }; MJProcessPlayer mpp = new
		 * MJProcessPlayer(); try { mpp.byRuntime(command); } catch (Exception
		 * e) { e.printStackTrace(); }
		 */

		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(new S_IconMessage(gm, param));
		/*
		 * try{ int i = Integer.parseInt(param);
		 * gm.sendPackets(S_IconMessage.getMessage(param, MJSimpleRgb.red(), i,
		 * 10)); }catch(Exception e){ e.printStackTrace(); }
		 */
		// gm.sendPackets(S_ChatMessageNoti.getNotice(param, "메티스"));
		// gm.sendPackets(S_ShowCmd.getQuestDesc(3511, 1465));

		/*
		 * L1NpcInstance obj = L1World.getInstance().findNpc(param); if(obj ==
		 * null) System.out.println("null"); else{ System.out.println(param +
		 * " " + obj.getX() + " " + obj.getY()); gm.sendPackets(new
		 * S_SkillSound(obj.getId(), 8150)); }
		 */
		// gm.sendPackets(new S_IconMessage(false));
		// gm.sendPackets(S_ShowCmd.getPkMessageAtBattleServer("\\aGtest\\aL",
		// "test2"));
		// gm.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int)
		// System.currentTimeMillis() / 1000, 8265, 4181));
		// gm.sendPackets(new S_IconMessage(false));
		/*
		 * L1PcInstance pc = L1World.getInstance().getPlayer(param); if(pc !=
		 * null) pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO,
		 * false));
		 */
		// gm.sendPackets(new S_SkillSound(gm.getId(), 1043));
		// gm.setPoisonEffect(0);
	}

	private static int _effectNum = 0;
	private static Object _effectLock = new Object();

	private void showNextEffect(L1PcInstance gm) {
		int num = 1;

		synchronized (_effectLock) {
			num = _effectNum;
			_effectNum++;
		}
		gm.sendPackets(new S_SkillSound(gm.getId(), num));
		gm.sendPackets(String.format("[%d]", num));
	}

	private void showNameId(L1PcInstance gm, String param) {
		try {
			S_ServerMessage sm = new S_ServerMessage(403, "$" + param);
			gm.sendPackets(sm, true);
		} catch (Exception e) {

		}
	}

	private void showEffectInit(L1PcInstance gm, String param) {
		int effNum = 0;
		try {
			StringTokenizer st = new StringTokenizer(param);
			effNum = Integer.parseInt(st.nextToken(), 10);
		} catch (Exception e) {
		}
		synchronized (_effectLock) {
			_effectNum = effNum;
			gm.sendPackets(effNum + "으로 초기화 되었습니다.");
		}
	}

	public static String _sleepingMessage;
	public static String _sleepingTitle;

	private void setSleepingMode(L1PcInstance gm, String param) {
		int num = 0;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			StringTokenizer st = new StringTokenizer(param);
			num = Integer.parseInt(st.nextToken(), 10);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_sleeping_messages where id=?");
			pstm.setInt(1, num);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_sleepingTitle = rs.getString("title");
				_sleepingMessage = rs.getString("content");
				gm.sendPackets(String.format("잠수 메시지가 [%s]로 설정되었습니다.", _sleepingMessage));
			} else
				gm.sendPackets(String.format("[%d]번의 잠수 메시지는 없습니다.", num));
		} catch (Exception e) {
			gm.sendPackets(".잠수모드 [잠수메시지 번호] 를 입력해주세요.");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void unSetSleepingMode(L1PcInstance gm, String param) {
		_sleepingTitle = null;
		_sleepingMessage = null;
		gm.sendPackets("잠수모드가 해제 되었습니다.");
	}

	private void deleteUseType(L1PcInstance gm) {
		try {
			for (L1ItemInstance item : m_use_type_items) {
				gm.sendPackets(new S_DeleteInventoryItem(item));
			}
			m_use_type_items.clear();
		} catch (Exception e) {
		}
	}

	private static ArrayList<L1ItemInstance> m_use_type_items = new ArrayList<L1ItemInstance>();
	private void showUseType(L1PcInstance gm, String param) {
		try {
			if (param.equalsIgnoreCase("삭제")) {
				deleteUseType(gm);
				return;
			}

			StringTokenizer st = new StringTokenizer(param);
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken()) + start;
			SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
			L1Item temp = ItemTable.getInstance().getTemplate(410016);
			for (int i = start; i <= end; i++){
				L1ItemInstance item = new L1ItemInstance();
				item.setId(i);
				item.setItem(temp);
				item.setBless(temp.getBless());
				item.setIdentified(false);
				noti.add_item_info(ItemInfo.newInstance(item, i, String.valueOf(i).getBytes(), item.getBless()));
				m_use_type_items.add(item);
			}
			gm.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
		} catch (Exception e) {
			gm.sendPackets(".유즈타입 [시작번호] [끝번호]");
		}
	}

	private void grangKinDB_Reset() {
		PreparedStatement pstm = null;
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET GrangKinAngerTime=?");
			pstm.setInt(1, 0);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showWarList(L1PcInstance gm) {
		try {
			int i = 0;
			L1World.getInstance().createWarStream().forEach((MJWar war) -> {
				gm.sendPackets(war.toString());
			});
		} catch (Exception e) {
		}
	}

	private static void setCharBlock(L1PcInstance gm, String param) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception("");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from characters where char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();

			if (!rs.next()) {
				gm.sendPackets(String.format("[%s]을(를) 찾을 수 없습니다.", name));
				return;
			}

			int objid = rs.getInt("objid");
			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("insert ignore into tb_character_block set objid=?, name=?");
			pstm.setInt(1, objid);
			pstm.setString(2, name);
			pstm.executeUpdate();

			gm.sendPackets(String.format("캐릭터 [%s]을(를) 압류했습니다.", name));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\f3-------------------------------------------------------------------"));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "당신의 계정내 현재 캐릭터가 압류 되었습니다."));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\f3-------------------------------------------------------------------"));
			pc.sendPackets("\\f1당신의 계정내 현재 캐릭터가 압류 되었습니다.");
			GeneralThreadPool.getInstance().schedule(new DelayRestart(pc), 3000);
		} catch (Exception e) {
			gm.sendPackets(".캐릭압류 [압류 캐릭터이름]");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	static class DelayRestart implements Runnable {
		private L1PcInstance _pc;

		DelayRestart(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			try {
				if (_pc != null) {
					_pc.sendPackets(new S_Restart(_pc.getId(), 1), true);
					_pc.logout();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void setCharBlockDelete(L1PcInstance gm, String param) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception("");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from tb_character_block where name=?");
			pstm.setString(1, name);
			pstm.executeUpdate();
			gm.sendPackets(String.format("캐릭터 [%s]을(를) 압류를 해제 했습니다.", name));
		} catch (Exception e) {
			gm.sendPackets(".캐릭압류해제 [해제 캐릭터이름]");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static boolean isCharacterBlock(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_character_block where name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}

	private int _currentNum = -1 + 825;

	private void showProto(L1PcInstance gm, String param) {
		try {
			_currentNum = Integer.parseInt(param);
		} catch (Exception e) {
		}
	}

	private void showNextProto(L1PcInstance gm, String param) {
		gm.sendPackets(S_ShowCmd.get(String.valueOf(_currentNum)));
		if (_currentNum != 13 && _currentNum != 14) {
			gm.sendPackets(S_ShowCmd.getProto8(_currentNum));
			gm.sendPackets(S_ShowCmd.getProtoA(_currentNum));
			/*try{
			SC_HUNTING_GUIDE_BOOKMARK_NOTI noti = SC_HUNTING_GUIDE_BOOKMARK_NOTI.newInstance();
			HUNTING_GUIDE_BOOKMARK_DATA data = HUNTING_GUIDE_BOOKMARK_DATA.newInstance();
			data.set_desc("hello");
			data.set_icon(3030);
			data.set_level(70);
			data.set_mapNo(4);
			data.set_seq(0);
			data.set_strIndex("0");
			data.set_x(32767);
			data.set_y(32767);
			noti.add_bookmarks(data);
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(noti.getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, _currentNum);
				try{
					noti.writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			gm.sendPackets(stream);
			}catch(Exception e){
				e.printStackTrace();
			}*/
		}
		_currentNum++;
	}
	
	
	
	private void present(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			//String name = st.nextToken();
			String nameid = st.nextToken();
			/*L1PcInstance target = L1World.getInstance().getPlayer(name);			
			boolean worldpc = true;
			
			if (target == null) {
				worldpc = false;
				target = CharacterTable.getInstance().restoreCharacter(name);
				if (target == null) {
					pc.sendPackets(new S_SystemMessage("존재하지 않는 캐릭터입니다."));
					return;
				}
				CharacterTable.getInstance().restoreInventory(target);
			}*/

			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int itemid = 0;
			int Attrenchant = 0;
			if (st.hasMoreTokens()) {
				Attrenchant = Integer.parseInt(st.nextToken());
			}
			int bless = 0;
			if (st.hasMoreTokens()) {
				bless = Integer.parseInt(st.nextToken());
			}
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않았습니다."));
					return;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					for(L1PcInstance _targetpc : L1World.getInstance().getAllPlayers()){
						L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(0);
						item.setCount(count);
						SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(_targetpc.getAccountName());		
						
						
						pwh.storeTradeItem(item);
						_targetpc.sendPackets("선물: " + item.getLogName() + " " + count + "개 획득 부가아이템창고를 확인하세요");
						_targetpc.sendPackets(new S_SkillSound(_targetpc.getId(), 4856));
						
					}
					pc.sendPackets(new S_SystemMessage("\\aD "+ temp.getName() + " 보냄"));
				} else {
					L1ItemInstance item = null;
					int createCount;
					for(L1PcInstance _targetpc : L1World.getInstance().getAllPlayers()){
						for (createCount = 0; createCount < count; createCount++) {
							item = ItemTable.getInstance().createItem(itemid);
							item.setEnchantLevel(enchant);
							item.setAttrEnchantLevel(Attrenchant);
							if (bless == 129) {
								item.setBless(bless);
							}
							SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(_targetpc.getAccountName());		
							
							pwh.storeTradeItem(item);
							_targetpc.sendPackets(new S_GMHtml("선물:"+_targetpc.getName()+"" , ""+item.getLogName()+"("+count+") 를(을) 획득함. 부가아이템 창고를 확인하세요"));
							_targetpc.sendPackets(new S_SkillSound(_targetpc.getId(), 4856));
							
						}
					}
					pc.sendPackets(new S_SystemMessage("\\aD "+ temp.getName() + " 보냄"));
				}
			} else {
				pc.sendPackets(new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다."));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".[선물지급] [아이템ID] [갯수] [인챈] [속성] [봉인129]"));
		}
	}

	private void showChat(L1PcInstance gm, String param) {
		gm.sendPackets(S_ChatMessageNoti.getNotice(param, "하하"));
		// gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_WHISPER,
		// param, null, "메티스", null, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ARENA_OBSERVER, "아레나옵저버", MJSimpleRgb.red(),
				gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ARENA_TEAM, "아레나팀", MJSimpleRgb.red(), gm.getName(),
				gm, -1));
		gm.sendPackets(
				S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_CLASS, "클래스", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_ALLIANCE, "혈맹알리언스", MJSimpleRgb.red(),
				gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_NOTICE, "혈맹공지", MJSimpleRgb.red(),
				gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_PLEDGE_PRINCE, "혈맹왕자", MJSimpleRgb.red(),
				gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_ROOM_ARENA_ALL, "룸아레나올", MJSimpleRgb.red(),
				gm.getName(), gm, -1));
		gm.sendPackets(
				S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_TEAM, "팀", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(
				S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_TRADE, "트레이드", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_CHAT_PARTY, "채팅파티", MJSimpleRgb.red(), gm.getName(),
				gm, -1));
		gm.sendPackets(
				S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_SHOUT, "샤우트", MJSimpleRgb.red(), gm.getName(), gm, -1));
		gm.sendPackets(S_ChatMessageNoti.get(S_ChatMessageNoti.CHAT_HUNT_PARTY, "헌트파티", MJSimpleRgb.red(), gm.getName(),
				gm, -1));
	}

	private void ping(L1PcInstance gm, String param) {
		if (param.equalsIgnoreCase("켬")) {
			S_Ping._isRun = true;
			gm.sendPackets(S_Ping.getForGM(), false);
		} else if (param.equalsIgnoreCase("끔")) {
			S_Ping._isRun = false;
			S_Ping._lastMs = 0L;
		}
	}

	public static boolean IS_PROTECTION = false;

	private void setProtectionMode(L1PcInstance gm, String param) {
		try {
			if (param.equalsIgnoreCase("켬")) {
				IS_PROTECTION = true;
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "현재 보호모드로 인해, 페널티가 적용 되지 않습니다. 안심하시고 사냥하셔도 됩니다."),
						true);
			} else if (param.equalsIgnoreCase("끔")) {
				IS_PROTECTION = false;
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "잠시후 보호모드가 해지 됩니다. 페널티에 주의 하시길 바랍니다."), true);
			} else if (param.equalsIgnoreCase("상태")) {

			} else
				throw new Exception("");
			gm.sendPackets(String.format("현재 보호모드 상태 [%s짐]", IS_PROTECTION ? "켜" : "꺼"));
		} catch (Exception e) {
			gm.sendPackets(".보호모드 [켬/끔/상태]");
		}
	}

	// 커멘드 새로추가
	private void AccountCheck(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			CharacterTable.getInstance().CharacterAccountCheck(pc, name);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".계정검사 [캐릭터명]"));
		}
	}

	private void AccountCheck1(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();
			CharacterTable.getInstance().CharacterAccountCheck1(pc, name);
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".계정확인 [캐릭터명]"));
		}
	}
	
	private void jakjak(L1PcInstance gm) { // 데페
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(210125);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				"누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(
				new S_SystemMessage("누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void jakjak2(L1PcInstance gm) { // 아머
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(5559);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				"누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(
				new S_SystemMessage("누군가가 샌드 웜의 모래 주머니에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}
	private void Jindeath(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(505011);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath1(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(505015);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath2(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(505012);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath3(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(620);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath4(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(625);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath5(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(626);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath6(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(623);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath7(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(618);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath8(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(619);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath9(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(616);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath10(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(622);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	private void Jindeath11(L1PcInstance gm) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(617);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 \\f3("+ item.getName() +")\\f2 를(을) 획득하였습니다."));
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("어느 아덴 용사 님께서 진 데스나이트의 유물 상자에서 ("+ item.getName() +") 를(을) 획득하였습니다."));
	}
	
	private void jakjak3(L1PcInstance gm) { // 카배
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(41148);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 에르자베의 알에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_SystemMessage("누군가가 에르자베의 알에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void dolldemon(L1PcInstance gm) { // 데몬인형
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(745);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_ChatPacket(gm, "\\fH누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
	}

	private void dolldeath(L1PcInstance gm) { // 데스인형
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(746);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_ChatPacket(gm, "\\fH누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
	}

	private void dolltarak(L1PcInstance gm) { // 타락인형
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(3000352);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_ChatPacket(gm, "\\fH누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
	}

	private void doice(L1PcInstance gm) { // 얼녀작업
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(3000352);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_ChatPacket(gm, "\\fH누군가가 " + item.getName() + " 합성에 성공 하였습니다."));
	}

	private void Stone(L1PcInstance gm) { // 룬스톤 카배
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(41148);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 빛나는 룬스톤에서 " + item.getName() + " 를(을) 획득하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_SystemMessage("누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void Stone1(L1PcInstance gm) { // 룬스톤 디스
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(40222);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 빛나는 룬스톤에서 " + item.getName() + " 를(을) 획득하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_SystemMessage("누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void Stone2(L1PcInstance gm) { // 룬스톤 데페
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(210125);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 빛나는 룬스톤에서 " + item.getName() + " 를(을) 획득하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_SystemMessage("누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void Stone3(L1PcInstance gm) { // 룬스톤 아머
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(5559);
		L1World.getInstance().broadcastPacketToAll(
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "누군가가 빛나는 룬스톤에서 " + item.getName() + " 를(을) 획득하였습니다."));
		L1World.getInstance()
				.broadcastPacketToAll(new S_SystemMessage("누군가가 빛나는 룬스톤에서 (" + item.getName() + ") 를(을) 획득하였습니다."));
	}

	private void addEventBoss(L1PcInstance gm, String param) {
		// TODO 자동 생성된 메소드 스텁
		try {
			StringTokenizer st = new StringTokenizer(param);
			String BossName = st.nextToken();
			if (BossName.equalsIgnoreCase("커츠")) {
				L1SpawnUtil.spawn2(32854, 33261, (short) 4, 45600, 0, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1SpawnUtil.spawn2(32854, 33262, (short) 4, 50000059, 3, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(1/6)]:돌격! 커츠의 부대가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(1/6)]:돌격! 커츠의 부대가 소환 되었습니다"), true);
			} else if (BossName.equalsIgnoreCase("데스나이트")) {
				L1SpawnUtil.spawn2(32856, 33263, (short) 4, 45601, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(2/6)]:돌격! 데스나이트의 부대가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(2/6)]:돌격! 데스나이트의 부대가 소환 되었습니다"), true);
			} else if (BossName.equalsIgnoreCase("피닉스")) {
				L1SpawnUtil.spawn2(32853, 33265, (short) 4, 45617, 50, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(3/6)]:돌격! 피닉스의 부대가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(3/6)]:돌격! 피닉스의 부대가 소환 되었습니다"), true);
			} else if (BossName.equalsIgnoreCase("마령군왕라이아")) {
				L1SpawnUtil.spawn2(32854, 33259, (short) 4, 45863, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(4/6)]:돌격! 마령군왕 라이아의 부대가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(4/6)]:돌격! 마령군왕 라이아의 부대가 소환 되었습니다"),
						true);
			} else if (BossName.equalsIgnoreCase("사신그림리퍼")) {
				L1SpawnUtil.spawn2(32854, 33266, (short) 4, 7310077, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(5/6)]:돌격! 사신 그림리퍼의 부대가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(5/6)]:돌격! 사신 그림리퍼의 부대가 소환 되었습니다"),
						true);
			} else if (BossName.equalsIgnoreCase("공포의안타라스")) {
				L1SpawnUtil.spawn2(32848, 33260, (short) 4, 7310154, 0, 3600 * 1000, 0);
				L1World.getInstance().broadcastServerMessage("\\aH[이벤트 보스(6/6)]:돌격! 마지막 몬스터가 소환 되었습니다.");
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fR[이벤트 보스(6/6)]:돌격! 마지막 몬스터가 소환 되었습니다."), true);
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage(".보스 [보스이름] ex<커츠/데스나이트/피닉스/마령군왕라이아/사신그림리퍼/공포의안타라스"));
		}
	}

	/*
	 * private void setMaxLevel(L1PcInstance gm, String param){ try{ String[]
	 * arr = param.split(" "); if(arr[0].equalsIgnoreCase("설정")){ int i =
	 * Integer.parseInt(arr[1]); Config.MAX_LEVEL = i; Config.MAX_LEVEL_EXP =
	 * ExpTable.getExpByLevel(i) + 5; }else if(arr[0].equalsIgnoreCase("확인")){
	 * }else throw new Exception(); gm.sendPackets(String.format(
	 * "현재 설정된 최대 레벨 : %d(exp : %d)", Config.MAX_LEVEL, Config.MAX_LEVEL_EXP)));
	 * }catch(Exception e){ gm.sendPackets(".최대레벨 [확인]")); gm.sendPackets(
	 * ".최대레벨 [설정] [레벨]")); } }
	 */

	public void setPresentationCode(L1PcInstance gm, String param) {
		try {
			L1ItemInstance.presentationCode = Integer.parseInt(param);
			gm.sendPackets(String.format("표기번호가 %d로 설정되었습니다.", L1ItemInstance.presentationCode));
		} catch (Exception e) {
			gm.sendPackets(String.format(".표기셋팅 숫자"));
		}
	}

	public void showObjectList(L1PcInstance gm, String param) {
		StringBuilder sb = new StringBuilder(256);
		for (L1Object obj : gm.getKnownObjects()) {
			if (obj instanceof L1Character) {
				L1Character c = (L1Character) obj;
				sb.append(c.getName()).append(" ").append(obj.getX()).append(" ").append(obj.getY()).append("\n");
			}
		}

		gm.sendPackets(sb.toString());
	}

	public static void setCharacterInstanceStatus(MJCommandArgs args) {
		try {
			String name = args.nextString();
			int status = args.nextInt();
			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			if (pc == null) {
				args.notify(String.format("캐릭터이름 : %s를 찾을 수 없습니다.", name));
				return;
			}
			MJEPcStatus e_status = MJEPcStatus.fromInt(status);
			if (e_status.equals(MJEPcStatus.NONE)) {
				StringBuilder sb = new StringBuilder(256);
				MJEPcStatus[] statuses = MJEPcStatus.values();
				sb.append("상태 값을 정확히 입력해주세요 ->\r\n");
				for (int i = 0; i < statuses.length; ++i) {
					if (i != 0)
						sb.append(", ");
					sb.append("[").append(statuses[i].toInt()).append(".").append(statuses[i]).append("]");
				}
				args.notify(sb.toString());
				return;
			}
			pc.set_instance_status(e_status);
		} catch (Exception e) {
			args.notify(".캐릭터상태 [캐릭터이름] [상태값]");
		} finally {
			args.dispose();
		}
	}

	private void CurseCharacter(MJCommandArgs args) {
		try {
			String name = args.nextString();
			if (name == null || name.equalsIgnoreCase(""))
				throw new Exception();

			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			if (pc == null) {
				args.notify(String.format("%s을(를) 찾을 수 없습니다.", name));
				return;
			}

			pc.add_lateral_damage(args.nextInt());
			pc.add_lateral_reduction(args.nextInt());
			pc.add_lateral_magic_rate(args.nextInt());
			pc.update_lateral_status();
			args.notify(String.format("%s의 현재 상태 - 대미지:%d, 리덕:%d,마법대미지:%d", name, pc.get_lateral_damage(),
					pc.get_lateral_reduction(), pc.get_lateral_magic_rate()));
		} catch (Exception e) {
			args.notify(".저주 [캐릭터이름] [대미지] [리덕션] [마법대미지]");
		}
	}

	private void CurseInitializeCharacter(MJCommandArgs args) {
		try {
			String command_type = args.nextString();
			if (command_type.equalsIgnoreCase("전체")) {
				L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
					return pc != null && pc.getAI() == null;
				}).forEach((L1PcInstance pc) -> {
					pc.set_lateral_damage(0);
					pc.set_lateral_reduction(0);
					pc.set_lateral_magic_rate(0);
				});
				Updator.truncate("tb_lateral_status");
				args.notify("전체 데이터를 초기화했습니다.");
			} else if (command_type.equalsIgnoreCase("캐릭")) {
				String name = args.nextString();
				if (name == null || name.equalsIgnoreCase(""))
					throw new Exception();
				L1PcInstance pc = L1World.getInstance().getPlayer(name);
				if (pc == null) {
					args.notify(String.format("%s을(를) 찾을 수 없습니다.", name));
					return;
				}
				pc.delete_lateral_status();
				args.notify(String.format("%s의 데이터를 초기화했습니다.", name));
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			args.notify(".저주초기화 [전체] | [캐릭(캐릭 메티스)]");
		}
	}

	private void clanMark(L1PcInstance pc, String param) {
		// TODO 자동 생성된 메소드 스텁
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			if (onoff.equalsIgnoreCase("켬")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, true), true);
				pc.sendPackets(new S_MARK_SEE(pc, 0, true), true);
				pc.sendPackets("혈마크 표기를 시작 합니다.");
			} else if (onoff.equalsIgnoreCase("끔")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, false), true);
				pc.sendPackets(new S_MARK_SEE(pc, 1, false), true);
				pc.sendPackets("혈마크 표기를 종료 합니다.");
			} else {
				pc.sendPackets(new S_SystemMessage(".혈마크 [켬 / 끔]"));
				return;
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".혈마크 [켬 / 끔]"));
		}
	}

	// TODO 중개 거래 게시판
	private static void LetterList(L1PcInstance pc, int type, int count) {
		pc.sendPackets(new S_LetterList(pc, type, count));
	}

	public static void WriteCancle(String buyer, String seller) {
		int nu1 = 949;
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date currentTime = new Date();
		String dTime = formatter.format(currentTime);
		String subject = "구매취소내역";
		String content = "구매자 :" + buyer + "" + "\n\n상대방이 구매를 취소하엿습니다." + "\n입금 대기상태로 바뀝니다.";
		String name = "거래관리자";
		L1PcInstance target = L1World.getInstance().getPlayer(seller);
		LetterTable.getInstance().writeLetter(nu1, dTime, name, seller, 0, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "구매취소: " + buyer + "님이 구매를 취소하였습니다."));
			LetterList(target, 0, 20);
			target.sendPackets(new S_SkillSound(target.getId(), 1091));
			target.sendPackets(new S_ServerMessage(428));
		}
	}

	public static void WriteComplete(String buyer, int count, String seller) {
		int nu1 = 949;
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date currentTime = new Date();
		String dTime = formatter.format(currentTime);
		String subject = "판매완료내역";
		String content = "후원 코인:" + count + "개" + "\n판매자 :" + seller + "" + "\n물품 구매가 완료되었습니다. " + "\n\n① 캐릭터를 재접속 하세요."
				+ "\n② 인벤토리 창을 여세요." + "\n③ [부가 아이템 창고] 아이콘을 누릅니다." + "\n④ 후원 코인을 수령하세요.";
		String name = "거래관리자";
		L1PcInstance target = L1World.getInstance().getPlayer(buyer);
		LetterTable.getInstance().writeLetter(nu1, dTime, name, buyer, 0, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			target.sendPackets(
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "판매완료: '부가아이템창고'로 지급 되었습니다(리스타트후 수령가능)"));
			target.killSkillEffectTimer(BUYER_COOLTIME);
			LetterList(target, 0, 20);
			target.sendPackets(new S_SkillSound(target.getId(), 1091));
			target.sendPackets(new S_ServerMessage(428));
		}
	}

	public static void WriteBuyLetter(String player, int count, int sellcount, String bankname, String name,
			String number, String sellername, String buyername) {
		int nu1 = 949;
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date currentTime = new Date();
		String dTime = formatter.format(currentTime);
		String subject = "구매 신청 내역";
		String content = "후원 코인: " + count + "개." + "\n구매 금액 : " + sellcount + "원. " + "\n은행 : " + bankname + ""
				+ "\n예금주 : " + name + "" + "\n계좌번호 : " + number + "" + "\n판매자 캐릭터 : " + sellername + "" + "\n구매자 캐릭터 : "
				+ buyername + "" + "\n\n판매자는 입금완료후 반드시" + "\n.판매완료 (게시번호) 를 하셔야" + "\n구매자에게 후원 코인이 지급됩니다."
				+ "\n\n판매자가 악의적으로 판매완료를" + "\n거부할 경우 메티스로 문의." + "\n입금전 반드시 상대방이" + "\n접속중인지 확인후 거래를"
				+ "\n진행해 주시기 바랍니다.";
		String project = "거래관리자";
		L1PcInstance target = L1World.getInstance().getPlayer(player);
		LetterTable.getInstance().writeLetter(nu1, dTime, project, player, 0, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			LetterList(target, 0, 20);
			target.sendPackets(new S_SkillSound(target.getId(), 1091));
			target.sendPackets(new S_ServerMessage(428));
		}
	}

	public static void WriteSellLetter(String player, int count, int sellcount, String bankname, String name,
			String number, String charname) {
		int nu1 = 949;
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date currentTime = new Date();
		String dTime = formatter.format(currentTime);
		String subject = "판매 등록 내역";
		String content = "후원 코인: " + count + "개." + "\n판매 금액 : " + sellcount + "원. " + "\n은행 : " + bankname + ""
				+ "\n예금주 : " + name + "" + "\n계좌번호 : " + number + "" + "\n판매자 캐릭터 : " + charname + ""
				+ "\n\n위 내용과 상이할 경우 " + "\n.판매취소 (게시글번호)" + "\n하신후 재등록 하시기 바랍니다." + "\n\n이용자간 거래로 발생한 피해는"
				+ "\n어떠한 경우에도 책임지지 않습니다.";
		String project = "거래관리자";
		L1PcInstance target = L1World.getInstance().getPlayer(player);
		LetterTable.getInstance().writeLetter(nu1, dTime, project, player, 0, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			LetterList(target, 0, 20);
			target.sendPackets(new S_SkillSound(target.getId(), 1091));
			target.sendPackets(new S_ServerMessage(428));
		}
	}

	private static void countR1(L1PcInstance pc, String count) {
		StringTokenizer st = new StringTokenizer(count);
		try {
			int i = 0;
			int sellcount = 0;
			String bankname = null;
			String name = null;
			String numeber = null;
			try {
				i = Integer.parseInt(st.nextToken());
				sellcount = Integer.parseInt(st.nextToken());
				bankname = st.nextToken();
				name = st.nextToken();
				numeber = st.nextToken();
			} catch (NumberFormatException e) {
			}

			if (i <= 0) {
				pc.sendPackets(".판매  (판매금액)을 적어주세요.");
				return;
			}
			if (i > Config.MAX_SELL_ADENA) {
				pc.sendPackets("최대판매: " + Config.MAX_SELL_ADENA + " 개 이상 판매하실 수 없습니다.");
				return;
			}
			if (i >= 1 && i < Config.MIN_SELL_ADENA) {
				pc.sendPackets("최소판매: " + Config.MIN_SELL_ADENA + " 개 이상 입니다.");
				return;
			}
			if (sellcount < Config.MIN_SELL_CASH) {
				pc.sendPackets("최소 판매금액: " + Config.MIN_SELL_CASH + "원 이상 입니다.");
				return;
			}
			if (sellcount > Config.MAX_SELL_CASH) {
				pc.sendPackets("최대 판매금액: " + Config.MAX_SELL_CASH + "원 이하 입니다.");
				return;
			}

			String selltype = "판매 중";
//			L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.ADENA); // 아데나
//			L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.ADENA1); // 1억 아데나 수표
			L1Item temp = ItemTable.getInstance().getTemplate(L1ItemId.DUNK_COIN); // 후원 코인
			L1ItemInstance adena = new L1ItemInstance(temp, i);

//			if (pc.getInventory().checkItem(L1ItemId.ADENA, i)) {
//				pc.getInventory().consumeItem(L1ItemId.ADENA, i);
			if (pc.getInventory().checkItem(L1ItemId.DUNK_COIN, i)) {
				pc.getInventory().consumeItem(L1ItemId.DUNK_COIN, i);
				AuctionSystemTable.getInstance().writeTopic(pc, selltype, adena, i, sellcount, bankname, numeber, name);
				WriteSellLetter(pc.getName(), i, sellcount, bankname, name, numeber, pc.getName());
//				WriteSellLetter("메티스", i, sellcount, bankname, name, numeber, pc.getName()); // 운영자에게 편지
			} else {
				pc.sendPackets("수량이 맞는지 확인하시기 바랍니다.");
			}
		} catch (Exception exception) {
//			pc.sendPackets(".판매 (아데나) (판매금액) (은행명) (예금주) (계좌)");
			pc.sendPackets("----------------------------------------------------");
			pc.sendPackets("\\aG【예시】");
			pc.sendPackets("\\aG.판매  10000  10000  신한  홍길동  0123456789(-없이)");
			pc.sendPackets("----------------------------------------------------");
			pc.sendPackets(".판매   코인   현금   은행   예금주   계좌번호");
		}
	}

	private static void countR2(L1PcInstance pc, String count) {
		if (pc.hasSkillEffect(BUYER_COOLTIME)) {
			int n = pc.getSkillEffectTimeSec(BUYER_COOLTIME);
			pc.sendPackets("이미 다른 물품을 구매중이거나 쿨타임이 적용중입니다.");
			pc.sendPackets(new S_SystemMessage(String.format("%d초 후에 구매신청을 하실 수 있습니다.", n)));
			return;
		}
		int i = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			i = Integer.parseInt(count);
		} catch (NumberFormatException e) {
		}
		String charname = null;
		int adenacount = 0;
		int sellcount = 0;
		int status = 0;
		String bank = null;
		String banknumeber = null;
		String bankname = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT  id, name, count, sellcount, status, bank, banknumber, bankname from Auction where id Like '"
							+ i + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				charname = rs.getString("name");
				adenacount = rs.getInt("count");
				sellcount = rs.getInt("sellcount");
				status = rs.getInt("status");
				bank = rs.getString("bank");
				banknumeber = rs.getString("banknumber");
				bankname = rs.getString("bankname");
			}

			if (charname == null) {
				pc.sendPackets("잘못된 게시번호이거나 게시번호를 입력하지 않았습니다.");
				return;
			}
			if (status == 1) {
				pc.sendPackets("이미 다른사람이 구매신청을 한 상태입니다.");
				return;
			}
			if (status == 2) {
				pc.sendPackets("해당 게시물은 이미 판매가 완료되었습니다.");
				return;
			}

			try {
				int time = 600;
				WriteBuyLetter(pc.getName(), adenacount, sellcount, bank, bankname, banknumeber, charname,
						pc.getName());
				WriteBuyLetter(charname, adenacount, sellcount, bank, bankname, banknumeber, charname, pc.getName());
//				WriteBuyLetter("메티스", adenacount, sellcount, bank, bankname, banknumeber, charname, pc.getName()); // 운영자에게 편지
				AuctionSystemTable.getInstance().AuctionUpdate(i, pc.getName(), pc.getAccountName(), "입금대기", 1);
				pc.setSkillEffect(BUYER_COOLTIME, time * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			pc.sendPackets(".구매 (게시글번호)");
			pc.sendPackets(".구매 0001");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static void countR3(L1PcInstance pc, String count) {
		int i = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			i = Integer.parseInt(count);
		} catch (NumberFormatException e) {
		}
		int itemid = 0;
		int itemcount = 0;
		String account = null;
		int status = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT  item_id ,count ,AccountName ,status from Auction where id Like '" + i + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				itemid = rs.getInt("item_id");
				itemcount = rs.getInt("count");
				account = rs.getString("AccountName");
				status = rs.getInt("status");
			}
			if (status == 1) {
				pc.sendPackets("입금 대기상태 에서는 판매취소가 불가능 합니다.");
				return;
			}
			if (status == 2) {
				pc.sendPackets("판매가 완료된 상태입니다.");
				return;
			}
			if (pc.getAccountName().equalsIgnoreCase(account) || pc.isGm()) {
				pc.getInventory().storeItem(itemid, itemcount);
				AuctionSystemTable.getInstance().deleteTopic(i);
				pc.sendPackets("정상적으로 취소되었습니다");
			} else {
				pc.sendPackets("등록하신 물품이 아닙니다");
			}
		} catch (SQLException e) {
			pc.sendPackets(".판매취소 (게시글번호) 라고 적어주십시요.");
			pc.sendPackets("예)  .판매취소 0035");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static void countR4(L1PcInstance pc, String count) {
		int i = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			i = Integer.parseInt(count);
		} catch (NumberFormatException e) {
		}
		String charname = null;
		int sellcount = 0;
		int status = 0;
		String buyerName = null;
		String buyerAccountName = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT  name, count, status, buyername, buyerAccountName from Auction where id Like '" + i + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				charname = rs.getString("name");
				sellcount = rs.getInt("count");
				status = rs.getInt("status");
				buyerName = rs.getString("buyername");
				buyerAccountName = rs.getString("buyerAccountName");
			}

			if (status == 1) {
				if (pc.getName().equalsIgnoreCase(charname) || pc.isGm()) {
					AuctionSystemTable.getInstance().AuctionComplete(i);
					try {
						WriteComplete(buyerName, sellcount, charname);
//						WriteComplete("메티스", sellcount, charname); // 운영자에게 편지
//						Warehouse.present(buyerAccountName, 40308, 0, sellcount); // 아데나
//						Warehouse.present(buyerAccountName, 400254, 0, sellcount); // 1억 아데나 수표
						Warehouse.present(buyerAccountName, 3000181, 0, sellcount); //  후원 코인
					} catch (Exception e) {
						e.printStackTrace();
					}
					pc.sendPackets("정상적으로 판매완료 되었습니다.");
				} else {
					pc.sendPackets("당신은 해당글의 판매자가 아닙니다.");
				}
			} else {
				pc.sendPackets("아직 구매 신청자가 없습니다.");
			}
		} catch (SQLException e) {
			pc.sendPackets(".판매완료(게시글번호)");
			pc.sendPackets(".판매완료 0017");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private static void countR5(L1PcInstance pc, String count) {
		int i = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			i = Integer.parseInt(count);
		} catch (NumberFormatException e) {
		}
		int status = 0;
		String charname = null;
		String buyername = null;
		String buyeraccount = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT  name, status, buyername, buyerAccountName from Auction where id Like '" + i + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				charname = rs.getString("name");
				status = rs.getInt("status");
				buyeraccount = rs.getString("buyerAccountName");
				buyername = rs.getString("buyername");
			}
			if (status == 0) {
				pc.sendPackets("구매 취소가 불가능한 상태입니다.");
				return;
			}
			if (status == 2) {
				pc.sendPackets("판매가 완료된 상태입니다.");
				return;
			}
			if (pc.getAccountName().equalsIgnoreCase(buyeraccount) || pc.isGm()) {
				AuctionSystemTable.getInstance().AuctionUpdate(i, "", "", "입금대기", 0);
				WriteCancle(buyername, charname);
//				WriteCancle("메티스", charname); // 운영자에게 편지
				pc.sendPackets("정상적으로 취소되었습니다");
			} else {
				pc.sendPackets("등록하신 물품이 아닙니다");
			}
		} catch (SQLException e) {
			pc.sendPackets(".구매취소 (게시글번호) 라고 적어주십시요.");
			pc.sendPackets("예)  .구매취소 0035");
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	// TODO 중개 거래 게시판

	private void standBy77(L1PcInstance gm, String param) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(param);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			int objid = 0;
			String acname = null;
			if (target != null) {
				target.sendPackets(new S_Disconnect());
			}
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT objid, account_name FROM characters WHERE char_name=?");
				pstm.setString(1, pcName);
				rs = pstm.executeQuery();
				while (rs.next()) {
					objid = rs.getInt(1);
					acname = rs.getString(2);
				}
				if (objid == 0) {
					gm.sendPackets(new S_SystemMessage("디비에 해당 유저의 이름이 존재하지 않습니다."), true);

				} else {
					gm.sendPackets(new S_SystemMessage(acname + "계정 " + pcName + "님의 케릭터를 삭제 합니다."), true);
					CharacterTable.getInstance().deleteCharacter(acname, pcName);
					gm.sendPackets(new S_SystemMessage("해당유저를 정상적으로 삭제 하였습니다."), true);
				}
			} catch (SQLException e) {

			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}

		} catch (Exception eee) {
			gm.sendPackets(new S_SystemMessage(".케릭삭제 [케릭명]"), true);
		}
	}

	private void Blessleaf(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._Blessleaf = true;
				gm.sendPackets("" + target.getName() + " 님의 기운을 잃은 아이템 성공율이 1회성으로 100% 적용됩니다.");
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".기운축복 캐릭명 으로 입력해주세요.");
		}
	}

	private void EnchantWeaponSuccess(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._EnchantWeaponSuccess = true;
				gm.sendPackets("" + target.getName() + " 님의 무기인챈성공율이 1회성으로 100% 적용됩니다.");
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".무기인챈성공 캐릭명 으로 입력해주세요.");
		}
	}

	private void EnchantArmorSuccess(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._EnchantArmorSuccess = true;
				gm.sendPackets("" + target.getName() + " 님의 방어인챈성공율이 1회성으로 100% 적용됩니다.");
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".방어인챈성공 캐릭명 으로 입력해주세요.");
		}
	}

	private void ErzabeBox(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._ErzabeBox = true;
				gm.sendPackets("(에르자베의 알)" + target.getName() + " 님의 4대마법 습득율이 1회성으로 100% 적용됩니다.");
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".에르자베 캐릭명 으로 입력해주세요.");
		}
	}

	private void SandwormBox(L1PcInstance gm, String pcName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				target._SandwormBox = true;
				gm.sendPackets("(샌드웜의 주머니)" + target.getName() + " 님의 4대마법 습득율이 1회성으로 100% 적용됩니다.");
			} else {
				gm.sendPackets("그런 캐릭터는 없습니다.");
			}
		} catch (Exception e) {
			gm.sendPackets(".샌드웜 캐릭명 으로 입력해주세요.");
		}
	}

	private void befixed(L1PcInstance gm, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String phone = tok.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target == null) {
				gm.sendPackets("캐릭터가 없습니다.");
				return;
			}

			Account account = Account.load(target.getAccountName());
			if (account.getphone() != null) {
				gm.sendPackets("이미 고정회원 입니다.");
				return;
			}
			account.setphone("00000000000");
			Account.updatePhone(account);
			gm.sendPackets(" " + phone + " 설정 완료 되었습니다.");
			target.sendPackets("고정신청이 완료되어 버프가 발동 됩니다.");
			보안버프(target);
		} catch (Exception e) {
		}
	}

	private static void 보안버프(L1PcInstance gm) {
		gm.getAC().addAc(-1);
		gm.sendPackets(new S_PacketBox(gm, S_PacketBox.ICON_SECURITY_SERVICES));
		gm.sendPackets(new S_OwnCharStatus(gm));
	}

	private void searchclaner(L1PcInstance gm, String name) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			String charinfo = null;
			charinfo = String.format(
					"select account_name, char_name, level, clanname, OnlineStatus from characters where clanname Like '%s'",
					target.getClanname());

			if (target.getClan() == null) {
				gm.sendPackets("검색하신 캐릭터는 혈맹에 가입하지 않은 상태입니다.");
				return;
			}

			Selector.exec(charinfo, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					int i = 0;
					gm.sendPackets(String.format("================== 혈맹 정보 ==================", i));
					while (rs.next()) {
						++i;
						if (rs.getInt(5) > 0) {
							gm.sendPackets(String.format("\\fY계정:%s 캐릭명:%s 레벨:%s 혈맹:%s", rs.getString(1),
									rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
						}
					}
				}
			});

			String claninfo = null;
			claninfo = String.format("select total_m, current_m from clan_data where clan_name Like '%s'",
					target.getClanname());

			Selector.exec(claninfo, new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) {
						gm.sendPackets(
								String.format("\\fU총 혈맹원 : [%s] 접속중인 혈맹원 : %s", rs.getString(1), rs.getString(2)));
					}
				}
			});
		} catch (Exception e) {
			gm.sendPackets("검색 하신 캐릭명이 틀리거나 존재 하지 않습니다.");
		}
	}

	private void DominanceBoss(L1PcInstance gm, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int commandnum = Integer.parseInt(st.nextToken(), 10);
			List<DominanceBoss> list = DominanceDataLoader.getList();
			if (commandnum == 0) {
				DominanceDataLoader.reload();
				gm.sendPackets("지배의탑 보스 정보를 리로드 합니다.");
			}
			if (list.size() > 0) {
				for (DominanceBoss b : list) {
					try {
						switch (commandnum) {
						case 1:
							if (b.getBossNum() == 1) {
								DominanceFloorLv1 zenis = new DominanceFloorLv1(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								zenis.Start();
								gm.sendPackets("지배의 탑 1층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 2:
							if (b.getBossNum() == 2) {
								DominanceFloorLv2 sier = new DominanceFloorLv2(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								sier.Start();
								gm.sendPackets("지배의 탑 2층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 3:
							if (b.getBossNum() == 3) {
								DominanceFloorLv3 vampire = new DominanceFloorLv3(b.getNpcId(), b.getMapX(),
										b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(),
										b.getEffectNum());
								vampire.Start();
								gm.sendPackets("지배의 탑 3층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 4:
							if (b.getBossNum() == 4) {
								DominanceFloorLv4 zombie = new DominanceFloorLv4(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								zombie.Start();
								gm.sendPackets("지배의 탑 4층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 5:
							if (b.getBossNum() == 5) {
								DominanceFloorLv5 kuger = new DominanceFloorLv5(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								kuger.Start();
								gm.sendPackets("지배의 탑 5층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 6:
							if (b.getBossNum() == 6) {
								DominanceFloorLv6 mummy = new DominanceFloorLv6(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								mummy.Start();
								gm.sendPackets("지배의 탑 6층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 7:
							if (b.getBossNum() == 7) {
								DominanceFloorLv7 iris = new DominanceFloorLv7(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								iris.Start();
								gm.sendPackets("지배의 탑 7층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 8:
							if (b.getBossNum() == 8) {
								DominanceFloorLv8 bald = new DominanceFloorLv8(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								bald.Start();
								gm.sendPackets("지배의 탑 8층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 9:
							if (b.getBossNum() == 9) {
								DominanceFloorLv9 rich = new DominanceFloorLv9(b.getNpcId(), b.getMapX(), b.getMapY(),
										b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(), b.getEffectNum());
								rich.Start();
								gm.sendPackets("지배의 탑 9층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 10:
							if (b.getBossNum() == 10) {
								DominanceFloorLv10 ugnus = new DominanceFloorLv10(b.getNpcId(), b.getMapX(),
										b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(),
										b.getEffectNum());
								ugnus.Start();
								gm.sendPackets("지배의 탑 10층 보스 소환 이벤트를 진행합니다");
							}
							break;
						case 11:
							if (b.getBossNum() == 11) {
								DominanceFloorLv11 riper = new DominanceFloorLv11(b.getNpcId(), b.getMapX(),
										b.getMapY(), b.getMapId(), b.isMentuse(), b.getMent(), b.isAllEffect(),
										b.getEffectNum());
								riper.Start();
								gm.sendPackets("지배의 탑 정상 보스 소환 이벤트를 진행합니다");
							}
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			gm.sendPackets("보스스폰 : .지배보스 0~11(0=리로드, 1~11=층별 보스)");
		}
	}
	
	public static void do_test_shift_server(L1PcInstance gm, String param){
		try{
			String[] array = param.split(" ");
			L1PcInstance pc = L1World.getInstance().findpc(array[0]);
			if(pc == null){
				gm.sendPackets(String.format("%s(은)는 없는 캐릭터입니다.", param));
				return;
			}
			String server_identity = array[1];
			try {
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.BATTLE, server_identity);
				gm.sendPackets(String.format("%s을 이동시켰습니다.", param));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}catch(Exception e){
			gm.sendPackets(".test1 [캐릭명] [이전서버 식별 이름]");
		}
	}
	
	public static void do_test_returner_server(L1PcInstance gm, String param){
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				try {
					MJShiftObjectManager.getInstance().do_returner();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1000);
	}
	
	public static void do_shift_server(L1PcInstance gm, String param){
		try{
			String[] array = param.split(" ");
			L1PcInstance pc = L1World.getInstance().findpc(array[0]);
			if(pc == null){
				gm.sendPackets(String.format("%s(은)는 없는 캐릭터입니다.", param));
				return;
			}
			
			try {
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, array[1]);
				gm.sendPackets(String.format("%s을 이동시켰습니다.", param));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}catch(Exception e){
			gm.sendPackets(".서버이전 [캐릭명] [이전서버 식별 이름]");
		}
	}

	private void do_cache_exp(MJCommandArgs args){
		try{
			final String character_name = args.nextString();
			if(MJCommons.isNullOrEmpty(character_name))
				throw new Exception();
			
			MJObjectWrapper<LevelExpPair> wrapper = new MJObjectWrapper<LevelExpPair>();
			Selector.exec("select * from character_exp_cache where character_name=?", new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, character_name);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if(rs.next()){
						wrapper.value = new LevelExpPair();
						wrapper.value.level = rs.getInt("lvl");
						wrapper.value.exp = rs.getInt("exp");
					}
				}
			});
			if(wrapper.value == null){
				args.notify(String.format("%s님의 캐시 경험치 데이터를 찾을 수 없습니다.", character_name));
				return;
			}
			L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
			if(pc == null){
				Updator.exec("update characters set level=?, exp=? where char_name=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, wrapper.value.level);
						pstm.setInt(++idx, wrapper.value.exp);
						pstm.setString(++idx, character_name);
					}
				});
				args.notify(String.format("%s님의 데이터베이스 데이터를 업데이트 했습니다.(레벨:%d, 경험치:%d) ", 
						character_name, wrapper.value.level, wrapper.value.exp));
			}else{
				pc.setExp(wrapper.value.exp);
				args.notify(String.format("%s님의 경험치를 %d로 변경했습니다.", wrapper.value.exp));
			}
		}catch(Exception e){
			args.notify(".캐시경험치 [캐릭명]");
		}
	}

	static class LevelExpPair{
		int level;
		int exp;
	}
}
