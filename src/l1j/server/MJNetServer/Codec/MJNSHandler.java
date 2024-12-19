package l1j.server.MJNetServer.Codec;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJNetServer.Buffer.MJByteBufferFactory;
import l1j.server.MJNetServer.ClientManager.MJNSClientCounter;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.IpConnectDelay;
import l1j.server.server.utils.SystemUtil;

/**********************************
 * 
 * MJ Network Server System Handler. made by mjsoft, 2017.
 * 
 **********************************/
public class MJNSHandler extends ChannelInboundHandlerAdapter {
	private static final AttributeKey<GameClient> _clntAttr = AttributeKey.newInstance("GameClient");
	private static final AttributeKey<MJCryptor> _cptAttr = AttributeKey.newInstance("MJCryptor");

	private static ConcurrentHashMap<ChannelHandlerContext, GameClient> _clnts = null;
	private static Collection<GameClient> _clntsSnap = null;

	public static void initializer() {
		if (_clnts == null)
			_clnts = new ConcurrentHashMap<ChannelHandlerContext, GameClient>(Config.MAX_ONLINE_USERS);
	}

	public static Collection<GameClient> getClients() {
		Collection<GameClient> col = _clntsSnap;
		return col == null ? _clntsSnap = Collections.unmodifiableCollection(_clnts.values()) : col;
	}

	public static GameClient getClient(ChannelHandlerContext ctx) {
		return _clnts.get(ctx);
	}

	public static MJCryptor getCryptor(ChannelHandlerContext ctx) {
		return ctx.channel().attr(_cptAttr).get();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		int port = inetAddr.getPort();
		if (port <= MJNetServerLoadManager.NETWORK_WELL_KNOWNPORT) {
			print(inetAddr.getAddress().getHostAddress(), port, "사용자가 WellKnown-Port로 접속을 시도하여 차단했습니다.");
			ctx.close();
			return;
		}

		if (_clnts.size() + 1 >= Config.MAX_ONLINE_USERS) {
			print(inetAddr.getAddress().getHostAddress(), port, String.format("접속 아이피가 %d개를 초과하여 자동 차단했습니다.", Config.MAX_ONLINE_USERS));
			ctx.close();
			return;
		}

		if (Config.IP_DELAY_CHECK_USE) {
			IpConnectDelay.getInstance().addConnetCount(inetAddr.getAddress().getHostAddress());

			System.out.println(inetAddr.getAddress().getHostAddress() + " / 접근횟수 " + IpConnectDelay.getInstance().getConnetCount(inetAddr.getAddress().getHostAddress()));
			if (IpConnectDelay.getInstance().getConnetCount(inetAddr.getAddress().getHostAddress()) >= 3) {
				if (IpConnectDelay.getInstance().ipDelayChech(inetAddr.getAddress().getHostAddress())) {
					IpConnectDelay.getInstance().setBanIp(inetAddr.getAddress().getHostAddress());
					System.out.println("[공격의심 IP 접속시도 절단] IP:" + inetAddr.getAddress().getHostAddress());
					ctx.close();
					return;
				}

				if (IpConnectDelay.getInstance().isDelayBan(inetAddr.getAddress().getHostAddress())) {
					System.out.println("[공격의심으로 차단된 IP 접속시도 절단] IP:" + inetAddr.getAddress().getHostAddress());
					ctx.close();
					return;
				}
			}
			IpConnectDelay.getInstance().setDelayInsert(inetAddr.getAddress().getHostAddress());
		}

		if (!Config.shutdownCheck && ctx.channel().isActive()) {
			int nSize = MJNSClientCounter.getInstance().isPermission(inetAddr.getAddress());
			if (nSize == -1) {
				print(inetAddr.getAddress().getHostAddress(), port, String.format("%d회 이상의 연결을 시도하여 차단합니다.", MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION));
				ctx.close();
				return;
			}
			// print(inetAddr.getAddress().getHostAddress(), port,
						// String.format("접속시도: (%d/%d)", nSize, _clnts.size() + 1));

						print(inetAddr.getAddress().getHostAddress(), port, String.format("[접속시도중]:(%d/%d)", nSize, _clnts.size() + 1));

						// print(inetAddr.getAddress().getHostAddress(), port,
						// String.format("[접속시도중] Thread:"+Thread.activeCount()+" /
						// Memory:"+SystemUtil.getUsedMemoryMB()+"MB (%d/%d)", nSize,
						// _clnts.size() + 1));
						ByteBuf buf = MJByteBufferFactory.createKey(ctx);
						ctx.writeAndFlush(buf);
						GameClient clnt = new GameClient(ctx.channel());

						_clnts.put(ctx, clnt);
						ctx.channel().attr(_clntAttr).set(clnt);
						MJCryptor crt = new MJCryptor();
						crt.initKey(Opcodes.getSeed());
						ctx.channel().attr(_cptAttr).set(crt);
						clnt.setStatus(MJClientStatus.CLNT_STS_HANDSHAKE);
					} else {
						ctx.close();
					}

				}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		byte[] b = (byte[]) msg;
		GameClient clnt = ctx.channel().attr(_clntAttr).get();
		if (clnt == null) {
			print(ctx, "client가 없는 socket의 데이터 전송으로 인해 차단합니다.");
			ctx.close();
		} else {
			try {
				clnt.handle(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		try {
			print(ctx, "연결 종료[정상]");
			GameClient clnt = _clnts.remove(ctx);
			if (clnt != null) {
				InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
				MJNSClientCounter.getInstance().decrese(inetAddr.getAddress());
				clnt.get_client_close(clnt);
				clnt.close();
				clnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		try {
			cause.printStackTrace(); //TEST 원인찾기
			print(ctx, "연결 종료[비정상(팅김)].");
			GameClient clnt = _clnts.remove(ctx);
			if (clnt != null) {
				InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
				MJNSClientCounter.getInstance().decrese(inetAddr.getAddress());
				clnt.get_client_close(clnt);
				clnt.close();
				clnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ctx.close();
		} catch (Exception e) {
		}
	}

	public static void print(ChannelHandlerContext ctx, String message) {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		print(inetAddr.getAddress().getHostAddress(), inetAddr.getPort(), message);
	}

	public static void print(String ip, int port, String message) {
		System.out.println(String.format("[%s][%s:%d] %s\r\n", getLocalTime(), ip, port, message));
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}
}
