package l1j.server.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import MJShiftObject.MJEShiftObjectType;
import io.netty.channel.Channel;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.server.Controller.GameSoundController;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.PerformanceTimer;

import l1j.server.server.serverpackets.S_Sound;

public class GameClient {
	
//	private TimerTask checkPositionTask; // TimerTask 선언
	private static Timer 	_observerTimer = new Timer();
	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();
	public boolean						isAuthPass;
	public int[] 							charStat;
	private ClientThreadObserver 	observer;
	private Account 						account;
	private Channel 						chnnel;
	private L1PcInstance 				activeCharInstance;
	private boolean 						close;
	private int 								chatCount;
	private SendBusiness 				_business;
	private InetAddress					_inetAddress;
	private boolean 						_isLoginRecord;
	private boolean 						_isUpdate;
	private MJClientStatus 			_status;
//	public boolean auth = false;  // 인증 린카
	
	public GameClient(Channel channel) {
		isAuthPass					= false;
		close 						= false;
		_isLoginRecord 				= false;
		_isUpdate 					= false;
		chnnel 						= channel;
		charStat					= new int[6];
		InetSocketAddress inetAddr 	= (InetSocketAddress)channel.remoteAddress();
		_inetAddress				= inetAddr.getAddress();
		observer					= new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000);
		if (Config.AUTOMATIC_KICK > 0) {
			observer.start();
		}
		
		_threadPool.execute(_HcPacket);
	}

	public Channel getChannel(){
		return chnnel;
	}
	
	public void get_client_close(GameClient gc) {
		new S_CommonNews().UpDate(gc.getAccountName(), "0");
	}

	public void packetwaitgo(byte[] bb) {
		if (bb == null) return;
		try {
			_status.process(this, bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void kick() {
		sendPacket(new S_Disconnect());
		if (chnnel != null)
			chnnel.close();
	}

	public void close() throws Exception {
		
		if (!close) {
			close = true;
			try {
				if (activeCharInstance != null) {
					quitGame(activeCharInstance);
					synchronized (activeCharInstance) {
						if (!activeCharInstance.isPrivateShop()) {
							activeCharInstance.logout();
						}						
						setActiveChar(null);
					}
				}
			} catch (Exception e) {
			}
			try {
				LoginController.getInstance().logout(this);
				stopObsever();
			} catch (Exception e) {
			}
			try {
				if (chnnel != null)
					chnnel.close();
			} catch (Exception e) {
			}
		}
	}

	public void setActiveChar(L1PcInstance pc) {
		activeCharInstance = pc;
	}

	public L1PcInstance getActiveChar() {
		return activeCharInstance;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public String getAccountName() {
		if (account == null) {
			return null;
		}
		String name = account.getName();
		return name;
	}

	public static void quitGame(L1PcInstance pc) {
		pc.remove_companion();
		if (pc.getTradeID() != 0) {
			L1Trade trade = new L1Trade();
			trade.TradeCancel(pc);
		}
		if (pc.isInParty()) {
			pc.getParty().leaveMember(pc);
		}
		if (pc.isInChatParty()) {
			pc.getChatParty().leaveMember(pc);
		}
		Object[] petList = pc.getPetList().values().toArray();
		for (Object petObject : petList) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				pet.unloadMaster();
			}
			if (petObject instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) petObject;
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
				}
			}
		}
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			doll.deleteDoll(false);
		}
		Object[] followerList = pc.getFollowerList().values().toArray();
		for (Object followerObject : followerList) {
			L1FollowerInstance follower = (L1FollowerInstance) followerObject;
			follower.setParalyzed(true);
			follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(),
					follower.getHeading(), follower.getMapId());
			follower.deleteMe();
		}
		pc.stopEtcMonitor();
		pc.setOnlineStatus(0);
		
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
			if(userData != null){
				SC_ATTENDANCE_USER_DATA_EXTEND.update(pc.getAccountName(), userData);
				pc.setAttendanceData(null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getIp() {
		if(_inetAddress == null)
			return null;
		
		return _inetAddress.getHostAddress();
	}
	
	public String getHostname() {
		if(_inetAddress == null)
			return null;
		
		return _inetAddress.getHostAddress();
	}

	public InetAddress getAddress(){
		return _inetAddress;
	}
	
	public boolean isConnected() {
		return chnnel != null && chnnel.isActive();
	}

	public void stopObsever() {
		observer.cancel();
	}

	public boolean isClosed() {
		if (!chnnel.isActive())
			return true;
		else {
			return false;
		}
	}

	class ClientThreadObserver extends TimerTask {
		private int _checkct = 1;

		private final int _disconnectTimeMillis;

		public ClientThreadObserver(int disconnectTimeMillis) {
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start() {
			_observerTimer.scheduleAtFixedRate(this, _disconnectTimeMillis, _disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				if (!chnnel.isActive()) {
					cancel();
					return;
				}
				if (_checkct > 0) {
					_checkct = 0;
					return;
				}
				if(GameClient.this.getStatus().toInt() != MJClientStatus.CLNT_STS_ENTERWORLD.toInt()){
					close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				cancel();
			}
		}

		public void packetReceived() {
			++_checkct;
		}
	}
	
	

	public void handle(byte[] data){
		observer.packetReceived();
		
		// 린카 인증 START
//				if(data.length == 1 && data[0] == 0x11 && auth == false) {
//					auth = true;
//					return;
//				}
//				
//				if(auth == false) {
//					System.out.println("타 접속기 접속, 접속강제종료");
//					chnnel.close();
//					return;
//				}
				
				//린카 인증 END
		
		int op 	= data[0] & 0xff;
		
		if(op == Opcodes.C_USE_SPELL){
			if(activeCharInstance != null){
				if(activeCharInstance.isGm()){
					System.out.println("["+activeCharInstance.getName()+"] 유저 실행속도 : " + activeCharInstance._PerformanceTimer2.get());
					activeCharInstance._PerformanceTimer2.reset();
				}
			}			
			_HcPacket.requestWork(data);
			return;
		}
		
		
		_status.process(this, data);
	}
	
	
	
	HcPacket _HcPacket = new HcPacket(this, 5);
	
	public class HcPacket implements Runnable {
		private final BlockingQueue<byte[]> _queue;
		private byte[] c = {6,0,1,2,3,4,5};
		private GameClient _GameClient = null;
		int sleeptime;
		public HcPacket(GameClient _Client) {
			_queue = new LinkedBlockingQueue<byte[]>();
			_GameClient = _Client;
		}

		public HcPacket(GameClient _Client , int capacity) {
			_queue = new LinkedBlockingQueue<byte[]>(capacity);
			_GameClient = _Client;
		}
		
		public void requestclose() {
			requestWork(c);
		}
		
		public void requestWork(byte data[]) {
			if(_queue.size() > 5)return;
			_queue.offer(data);
		}
		
		public void requestclear() {
			_queue.clear();
		}
		
		
		PerformanceTimer timer = new PerformanceTimer();
		int opcode2;
		public void run() {
			byte[] data;
			while (!isClosed()) {
				try {
					data = _queue.poll(3000, TimeUnit.MILLISECONDS);
				if (data != null&&!isClosed()) {
					try {
			            if(activeCharInstance != null){
			            	activeCharInstance.setSpelldelay(0);
			            	_status.process(_GameClient, data);
			            	
			            	sleeptime = activeCharInstance.getSpelldelay();
			            	
			            	
			            	
		    				if(activeCharInstance.isGm()){
		    				System.out.println("["+activeCharInstance.getName()+"] 서버처리속도 : " + activeCharInstance._PerformanceTimer.get());
		    				System.out.println("");
		    				activeCharInstance._PerformanceTimer.reset();
		    				}
			            	
			            	
			            	if(sleeptime > 0){
								Thread.sleep((int)(sleeptime));
							}
						}
					} catch (Exception e) {}//}
				}
				} catch (InterruptedException e1) {}
			}
			_queue.clear();
			return;
		}
	}
	
	public void sendPacketNonClear(ServerBasePacket bp){
		if(_business == null){
			_business = new SendBusiness();
			GeneralThreadPool.getInstance().execute(_business);
		}
		_business.in(bp.getBytes());
	}
	
	public void sendPacket(ServerBasePacket bp, boolean isClear){
		if(_business == null){
			_business = new SendBusiness();
			GeneralThreadPool.getInstance().execute(_business);
		}
		_business.in(bp.getBytes());
		if(isClear) bp.clear();
	}
	
	public void sendPacket(ServerBasePacket bp){
		sendPacket(bp, true);
	}
	
	public void sendPacket(MJIProtoMessage message, int messageId){
		sendPacket(message, messageId, true);
	}
	
	public void sendPacket(MJIProtoMessage message, int messageId, boolean isClear){
		if(message.isInitialized()){
			sendPacket(message.writeTo(MJEProtoMessages.fromInt(messageId)), isClear);
			if(isClear)
				message.dispose();
		}else{
			MJEProtoMessages.printNotInitialized(getActiveChar() == null ? getIp() : getActiveChar().getName(), messageId, message.getInitializeBit());
		}
	}
	
	public void sendPacket(ProtoOutputStream stream){
		sendPacket(stream, true);
	}
	
	public void sendPacket(ProtoOutputStream stream, boolean isClear){
		if(_business == null){
			_business = new SendBusiness();
			GeneralThreadPool.getInstance().execute(_business);
		}
		
		if(!stream.isCreated())
			stream.createProtoBytes();
		
		_business.in(stream.getProtoBytes());
		if(isClear) stream.dispose();
	}
	
	public void directSendPacket(java.util.Collection<ProtoOutputStream> col){
		for(ProtoOutputStream stream : col){
			if(!stream.isCreated())
				stream.createProtoBytes();
			_business.in(stream.getProtoBytes());
		}
	}
	
	class SendBusiness implements Runnable{
		private final ArrayBlockingQueue<byte[]> _workQ;
		
		SendBusiness(){
			_workQ = new ArrayBlockingQueue<byte[]>(256);
		}
		
		public void in(byte[] data){
			if(data != null){
				_workQ.offer(data);
			}
		}
		
		@Override
		public void run() {
			while(chnnel.isActive()){
				boolean isFlush = false;
				try{
					byte[] data = _workQ.poll(3000L, TimeUnit.MILLISECONDS);
					if(data != null && chnnel.isWritable()){
						if(_workQ.size() <= 0){
							chnnel.writeAndFlush(data);
							isFlush = true;
						}else{
							chnnel.write(data);
						}
					}
				}catch(InterruptedException e){
				}catch(Exception e){
					e.printStackTrace();
				}
				if(chnnel != null && chnnel.isActive() && !isFlush)
					chnnel.flush();
			}
			_workQ.clear();
		}
	}

	public int getChatCount() {
		return chatCount;
	}

	public void setChatCount(int i) {
		chatCount = i;
	}
	
	public void setLoginRecord(boolean b){
		_isLoginRecord = b;
	}
	public boolean isLoginRecord(){
		return _isLoginRecord;
	}
	
	/** MJCSWSystem **/
	public boolean isUpdate(){
		return _isUpdate;
	}
	
	public void setUpdate(boolean b){
		_isUpdate = b;
	}
	
	public MJClientStatus getStatus(){
		return _status;
	}
	public void setStatus(MJClientStatus sts){
		if(sts.toInt() == MJClientStatus.CLNT_STS_AUTHLOGIN.toInt()){
			if(_status.toInt() == MJClientStatus.CLNT_STS_ENTERWORLD.toInt()){
				try{
					throw new Exception();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		_status = sts;
	}
	public void setStatus2(MJClientStatus sts){
		_status = sts;
	}
	
	
	private MJEShiftObjectType m_shift_type = MJEShiftObjectType.NONE;
	private String m_server_identity = "";
	private String m_server_description = "";
	public MJEShiftObjectType get_shift_type(){
		return m_shift_type;
	}
	public void set_shift_type(MJEShiftObjectType shift_type){
		m_shift_type = shift_type;
	}
	public boolean is_shift_client(){
		return !m_shift_type.equals(MJEShiftObjectType.NONE);
	}
	public boolean is_shift_transfer(){
		return m_shift_type.equals(MJEShiftObjectType.TRANSFER);
	}
	public boolean is_shift_battle(){
		return m_shift_type.equals(MJEShiftObjectType.BATTLE);
	}
	public String get_server_identity(){
		return m_server_identity;
	}
	public void set_server_identity(String server_identity){
		m_server_identity = server_identity;
	}
	public void set_server_description(String server_description){
		m_server_description = server_description;
	}
	public String get_server_description(){
		return m_server_description;
	}
	
	private int m_second_password_failure_count = 0;
	public int get_second_password_failure_count(){
		return m_second_password_failure_count;
	}
	public int inc_second_password_failure_count(){
		return ++m_second_password_failure_count;
	}
	public void reset_second_password_failure_count(){
		m_second_password_failure_count = 0;
	}
}
