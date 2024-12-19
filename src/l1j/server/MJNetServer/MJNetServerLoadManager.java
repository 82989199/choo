package l1j.server.MJNetServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Properties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import l1j.server.Config;
import l1j.server.MJNetServer.ClientManager.MJNSClientCounter;
import l1j.server.MJNetServer.Codec.MJNSDecoder;
import l1j.server.MJNetServer.Codec.MJNSEncoder;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJNetServer.Codec.MJNSLogHandler;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Network Server LoadManager.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNetServerLoadManager {
	private static MJNetServerLoadManager _instance;
	public static MJNetServerLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJNetServerLoadManager();
		return _instance;
	}
	
	public static void reload(){
		MJNetServerLoadManager tmp = _instance;
		if(tmp != null)
			tmp.close();
		_instance = new MJNetServerLoadManager();
		tmp = null;
	}

	public static void release(){
		if(_instance != null){
			_instance.close();
			_instance = null;
		}
	}
	
	public static void commands(L1PcInstance gm, String param){
		try{
			ArrayDeque<Integer> argsQ = MJCommons.parseToIntQ(param);
			if(argsQ == null || argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 2:
				if(_instance != null)
					_instance.loadConfig();
				gm.sendPackets(new S_SystemMessage("MJNetServer Config가 리로드 되었습니다."));
				break;
			case 3:
				monitorCommands(gm, param);
				break;
			default:
				throw new Exception("");	
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(".네트워크 [2.리로드컨피그][3.모니터]"));
		}
	}
	
	public static void monitorCommands(L1PcInstance gm, String args){
		try{
			String[] arr = args.split(" ");
			String name		= arr[1];
			String param	= arr[2];
			L1PcInstance pc = L1World.getInstance().getPlayer(name);
			if(pc == null || pc.getNetConnection() == null){
				gm.sendPackets(new S_SystemMessage(String.format("%s을(를) 찾을 수 없습니다.", name)));
				return;
			}
			
			GameClient 	clnt 	= pc.getNetConnection();
			Channel 	ch		= clnt.getChannel();
			if(param.equalsIgnoreCase("켬")){
				ChannelHandler logh	= ch.pipeline().get(MJNSLogHandler.class.getName());
				ChannelHandler pckh = ch.pipeline().get(MJNSHandler.class.getName());
				if(logh != null || pckh == null){
					gm.sendPackets(new S_SystemMessage("%s는(은) 현재 모니터링이 활성화되어 있습니다."));
					return;
				}
				ch.pipeline().addBefore(MJNSHandler.class.getName(), MJNSLogHandler.class.getName(), new MJNSLogHandler());
				ch.pipeline().remove(MJNSHandler.class.getName());
			}else if(param.equalsIgnoreCase("끔")){
				ChannelHandler logh	= ch.pipeline().get(MJNSLogHandler.class.getName());
				ChannelHandler pckh = ch.pipeline().get(MJNSHandler.class.getName());
				if(logh == null || pckh != null){
					gm.sendPackets(new S_SystemMessage("%s는(은) 현재 모니터링 중이지 않습니다."));
					return;					
				}
				ch.pipeline().addBefore(MJNSLogHandler.class.getName(), MJNSHandler.class.getName(), new MJNSHandler());
				ch.pipeline().remove(MJNSLogHandler.class.getName());
			}else{
				throw new Exception("");
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(".네트워크 3 [이름] [켬/끔]"));
		}
		
	}
	
	private boolean 		_isrun;
	private ChannelFuture	_servfuture;
	private EventLoopGroup	_bossG;
	private EventLoopGroup	_workG;
	
	private MJNetServerLoadManager(){
		_isrun = false;
		loadConfig();
		_bossG = new NioEventLoopGroup(1);
		_workG = new NioEventLoopGroup();
	}
	
	public boolean isRun(){
		return _isrun;
	}
	
	public void run(){
		_isrun = true;
		try{
			MJNSHandler.initializer();
			MJNSClientCounter.getInstance();
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(_bossG, _workG)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, NETWORK_BACK_LOG)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NETWORK_TIMEOUT_MILLIS)
			.childOption(ChannelOption.SO_REUSEADDR, true)
			.childOption(ChannelOption.SO_KEEPALIVE, false)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.childOption(ChannelOption.SO_RCVBUF, NETWORK_RECV_BUFFSIZE)
			.childOption(ChannelOption.SO_SNDBUF, NETWORK_SEND_BUFFSIZE)
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					ChannelPipeline p = ch.pipeline();
					p.addLast(MJNSDecoder.class.getName(), new MJNSDecoder());
					p.addLast(MJNSEncoder.class.getName(), new MJNSEncoder());
					p.addLast(MJNSHandler.class.getName(), new MJNSHandler());
				}
			});
			
			_servfuture = bootstrap.bind(NETWORK_ACCEPT_PORT);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		try{
			_isrun = false;
			if(_servfuture != null){
				_servfuture.channel().close().awaitUninterruptibly();
				_servfuture = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(_bossG != null){
				_bossG.shutdownGracefully();
				_bossG = null;
			}
			if(_workG != null){
				_workG.shutdownGracefully();
				_workG = null;
			}
			MJNSClientCounter.release();
		}
	}
	
	public static int 		NETWORK_ACCEPT_PORT;
	public static int		NETWORK_BACK_LOG;
	public static int 		NETWORK_RECV_BUFFSIZE;
	public static int 		NETWORK_SEND_BUFFSIZE;
	public static int		NETWORK_TIMEOUT_MILLIS;
	public static int		NETWORK_WELL_KNOWNPORT;
	public static int		NETWORK_CLIENT_PERMISSION;
	public static int		NETWORK__ADDRESS2ACCOUNT;
	private void loadConfig(){
		try{
			Properties settings = new Properties();
			InputStream is = new FileInputStream(new File("./config/mjnetserver.properties"));
			settings.load(is);
			is.close();
			
			NETWORK_ACCEPT_PORT				= Config.GAME_SERVER_PORT;
			NETWORK_BACK_LOG				= Config.MAX_ONLINE_USERS;
			NETWORK_RECV_BUFFSIZE 			= Integer.parseInt(settings.getProperty("NetowrkRecvBuffSize", "4096"));
			NETWORK_SEND_BUFFSIZE 			= Integer.parseInt(settings.getProperty("NetowrkSendBuffSize", "4096"));
			NETWORK_TIMEOUT_MILLIS			= Integer.parseInt(settings.getProperty("NetworkTimeoutMillis", "300"));
			NETWORK_WELL_KNOWNPORT			= Integer.parseInt(settings.getProperty("WellKnownPort", "1024"));
			NETWORK_CLIENT_PERMISSION		= Integer.parseInt(settings.getProperty("NetworkClientPermission", "2"));
			NETWORK__ADDRESS2ACCOUNT	= Integer.parseInt(settings.getProperty("NetworkAddressToAccount", "2"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
