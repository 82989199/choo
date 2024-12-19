package l1j.server.server.monitor;


import java.io.BufferedWriter;
//////////// 날짜폴더별로 파일생성하기////////////////
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.swing.chocco;

public class FileLogger implements Logger {
	private static String date = "";	
	
	private ArrayList<String> _chatlog;
	private ArrayList<String> _commandlog;
	private ArrayList<String> _connectionlog;
	private ArrayList<String> _enchantlog;
	private ArrayList<String> _tradelog;
	private ArrayList<String> _warehouselog;
	private ArrayList<String> _itemactionlog;
	private ArrayList<String> _levellog;	/**  78레벨 부터 레벨업할 경우 levellog 기록 */
	private ArrayList<String> _error;
	private ArrayList<String> _cmd;
	private ArrayList<String> _shop;
	public FileLogger() {
		_chatlog = new ArrayList<String>(1024);
		_commandlog = new ArrayList<String>(512);
		_connectionlog = new ArrayList<String>(1024);
		_enchantlog = new ArrayList<String>(1024);
		_tradelog = new ArrayList<String>(512);
		_warehouselog = new ArrayList<String>(512);
		_itemactionlog = new ArrayList<String>(512);
		_levellog = new ArrayList<String>(512);	/**  78레벨 부터 레벨업할 경우 levellog 기록  */
		_error = new ArrayList<String>(1024);
		_cmd	= new ArrayList<String>(1024);
		_shop	= new ArrayList<String>(1024);
	}
	
	public void addCmd(String s){
		synchronized(_cmd){
			_cmd.add(s);
		}
	}
	
	public void addShop(String itemName, int count, long price, String npc, String pc){
		addShop(String.format("[%s] 엔피씨 : %s, 캐릭명 : %s, 아이템 : %s, 갯수 : %d, 가격 : %d\r\n", getLocalTime(), npc, pc, itemName, count, price));
	}
	
	public void addShop(String s){
		synchronized(_shop){
			_shop.add(s);
		}
		chocco.txtShop.append(s);
	}
	
	public void addChat(ChatType type, L1PcInstance pc, String msg) {
		String log = "";
		
		switch (type) {
		case Clan:
			log = String.format("%s\t혈맹(%s)\t[%s]\t%s\r\n", getLocalTime(), pc.getClanname(), pc.getName(), msg);
			chocco.txtClanChat.append(log);
			break;

		case Global:
			log = String.format("%s\t전체\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtGlobalChat.append(log);
			break;

		case Normal:
			log = String.format("%s\t일반\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtGlobalChat.append(log);
			break;

		case Alliance:
			log = String.format("%s\t동맹\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtClanChat.append(log);
			break;

		case Guardian:
			log = String.format("%s\t수호\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtClanChat.append(log);
			break;

		case Party:
			log = String.format("%s\t파티\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtPartyChat.append(log);			
			break;

		case Group:
			log = String.format("%s\t그룹\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			chocco.txtPartyChat.append(log);			
			break;

		case Shouting:
			log = String.format("%s\t외침\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break; 
		}
		synchronized (_warehouselog) {
			_chatlog.add(log);
		}
	}
	
	public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg) {
		// 시간 귓말 케릭->케릭\t내용
		String log = String.format("%s\t귓말\t[%s] -> [%s]\t%s\r\n", getLocalTime(), pcfrom.getName(), pcto.getName(), msg);
		chocco.txtWhisper.append(log);
		synchronized (_chatlog) {
			_chatlog.add(log);
		}
	}

	public void addCommand(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_commandlog) {
			_commandlog.add(msg);
		}
	}
	
	public void addConnection(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}
	}

	public void addError(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_error) {
			_error.add(msg);
		}
	}
	public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean success) {
		// 시간 계정:케릭 상태 아이템
		String msg = String.format("%s\t%s:[%s]\t%s\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), (success ? "성공" : "실패"), getFormatItemName(item, 1));
		chocco.txtEnchant.append(msg);
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}
	}

	public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 시간 성공 계정:케릭 [아이디]템이름(갯수) -> 계정:케릭
		// 거래 성공시 로그 기록 남기기 
		if(pcfrom == null || pcto == null || item == null)
			return;
		
		String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", getLocalTime(), (success ? "OO완료OO" : "XX취소XX"), pcfrom.getAccountName(), "["+pcfrom.getName()+"]", getFormatItemName(item, count), pcto.getAccountName(), pcto.getName());
		chocco.txtTrade.append(msg);
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}
	public void 개인상점구매(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 시간 성공 계정:케릭 [아이디]템이름(갯수) -> 계정:케릭
		// 거래 성공시 로그 기록 남기기 
		String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", getLocalTime(), (success ? "상점구매" : "상점취소"), pcfrom.getAccountName(), "["+pcfrom.getName()+"]", getFormatItemName(item, count), pcto.getAccountName(), pcto.getName());
		chocco.txtTrade.append(msg);
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}

	public void addWarehouse(WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = "";

		// 시간 타입 동작 계정:케릭명 [아이디]아이템(갯수)
		switch (type) {
		case Private:
			msg = String.format("%s\t개인:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "맡기기" : "찾기"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;

		case Clan:
			msg = String.format("%s\t혈맹(%s):%s\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getClanname(), (put ? "맡기기" : "찾기"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;

		case Package:
			msg = String.format("%s\t패키지:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "맡기기" : "찾기"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;

		case Elf:
			msg = String.format("%s\t요정:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "맡기기" : "찾기"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
			
		}
		chocco.txtWarehouse.append(msg);
		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}
	}
	
	public void addItemAction(ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = "";

		// 시간 타입 계정:케릭명 [아이디]아이템(갯수)
		switch (type) {
		case Pickup:
			msg = String.format("%s\t줍기\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			chocco.txtPickup.append(msg);
			break;
			/* 오토루팅 로그 기록 남기지 않게 변경 
		case AutoLoot:
			msg = String.format("%s\t오토루팅\t%s:%s\t%s\r\n", getLocalTime(), pc.getAccountName(), "["+pc.getName()+"]", getFormatItemName(item, count));
			break;
			 */
		case DeathDrop:
			msg = String.format("%s\t페널떨굼\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
		case Drop:
			msg = String.format("%s\t떨굼\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;

		case Delete:
			msg = String.format("%s\t삭제\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
		case del:
			msg = String.format("%s\t증발\t%s:%s\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;	
			
		}
		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}
	}
	
	/**  78레벨 부터 레벨업할 경우 levellog 기록  */
	public void addLevel(L1PcInstance pc, int level) {
		String msg = "";
		
		msg = String.format("%s\t%s:%s\tLevelUp %d\r\n", getLocalTime(), pc.getAccountName(), "["+pc.getName()+"]", level);
		synchronized (_levellog) {
			_levellog.add(msg);
		}
	}
	
	public void addAll(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		
		synchronized (_chatlog) {
			_chatlog.add(msg);
		}
		
		synchronized (_commandlog) {
			_commandlog.add(msg);
		}
		
		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}
		
		synchronized(_error) {
			_error.add(msg);
		}
		
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}
		
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
		
		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}
		
		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}
		
		/**  78레벨 부터 레벨업할 경우 levellog 기록  */
		synchronized (_levellog) {
			_levellog.add(msg);
		}
	}
	
	public void flush() throws IOException {
		synchronized (_chatlog) {
			if (!_chatlog.isEmpty()) {
				writeLog(_chatlog, "채팅.txt");
				_chatlog.clear();
			}
		}
		
		synchronized (_commandlog) {
			if (!_commandlog.isEmpty()) {
				writeLog(_commandlog, "명령어.txt");
				_commandlog.clear();
			}
		}
		
		synchronized (_connectionlog) {
			if (!_connectionlog.isEmpty()) {
				writeLog(_connectionlog, "로그인.txt");
				_connectionlog.clear();
			}
		}
		
		synchronized(_error) {
			if (!_error.isEmpty()) {
				writeLog(_error, "에러로그.txt");
				_error.clear();
			}
		}
		
		synchronized (_enchantlog) {
			if (!_enchantlog.isEmpty()) {
				writeLog(_enchantlog, "인챈트.txt");
				_enchantlog.clear();
			}
		}
		
		synchronized (_tradelog) {
			if (!_tradelog.isEmpty()) {
				writeLog(_tradelog, "교환,시장.txt");
				_tradelog.clear();
			}
		}
		
		synchronized (_warehouselog) {
			if (!_warehouselog.isEmpty()) {
				writeLog(_warehouselog, "창고.txt");
				_warehouselog.clear();
			}
		}
		
		synchronized (_itemactionlog) {
			if (!_itemactionlog.isEmpty()) {
				writeLog(_itemactionlog, "아이템로그.txt");
				_itemactionlog.clear();
			}
		}
		
		/**  78레벨 부터 레벨업할 경우 levellog 기록  */
		synchronized (_levellog) {
			if (!_levellog.isEmpty()) {
				writeLog(_levellog, "레벨업.txt");
				_levellog.clear();
			}
		}
		
		synchronized(_cmd){
			if(!_cmd.isEmpty()){
				writeLog(_cmd, "CMD.txt");
				_cmd.clear();
			}
		}
		
		synchronized(_shop){
			if(!_shop.isEmpty()){
				writeLog(_shop, "NPC상점.txt");
				_shop.clear();
			}
		}
		
		chocco.txtGlobalChat.setText("");
		chocco.txtClanChat.setText("");
		chocco.txtPartyChat.setText("");
		chocco.txtWhisper.setText("");
		chocco.txtShop.setText("");
		chocco.txtTrade.setText("");
		chocco.txtWarehouse.setText("");
		chocco.txtEnchant.setText("");
		chocco.txtPickup.setText("");
	}
	//** 날짜별로 폴더생성해서 로그저장하기 **//	
	private static String getDate(){
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh-ss", Locale.KOREA);
		return s.format(Calendar.getInstance().getTime());
	}
	
	public void writeLog(List<String> log, String filename) throws IOException {
		//** 날짜별로 폴더생성해서 로그저장하기 **//
		File f = null;
		String sTemp = "";
		sTemp = getDate();
		StringTokenizer s 	= new StringTokenizer(sTemp, " ");
		StringBuilder sb 	= new StringBuilder(256);
		date = s.nextToken();
		sb.append("logDB/").append(date);
		f = new File(sb.toString());
		//f = new File("LogDB/"+date);
		if(!f.exists()) f.mkdir();
		//** 날짜별로 폴더생성해서 로그저장하기  **//
		sb.append("/").append(filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(sb.toString(), true));
		//BufferedWriter w = new BufferedWriter(new FileWriter("LogDB/"+ date + "/" + filename, true));
		PrintWriter pw = new PrintWriter(w, true);
		for (int i = 0, n = log.size(); i < n; i++) {
			pw.print(log.get(i));
		}
		pw.close();
		pw = null;
		w.close();
		w = null;
		sTemp = null;
		date = null;
	}

	public String getLocalTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar localtime = new GregorianCalendar();

		return formatter.format(localtime.getTime());
	}
	
	public String getFormatItemName(L1ItemInstance item, int count) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("[").append(item.getId()).append("]");
		if(item.getEnchantLevel() > 0)
			sb.append("+").append(item.getEnchantLevel());
		else if(item.getEnchantLevel() < 0)
			sb.append(item.getEnchantLevel());
		sb.append(item.getName());
		if(item.isStackable())
			sb.append("(").append(count).append(")");
		return sb.toString();
	}
}
