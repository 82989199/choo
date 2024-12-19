package l1j.server.GameSystem.MiniGame;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.text.SimpleDateFormat;

import javolution.util.FastTable;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class LottoSystem extends Thread {

	private static LottoSystem _instance;

	private boolean _LottoStart;

	public boolean getLottoStart() {
		return _LottoStart;
	}

	public void setLottoStart(boolean Lotto) {
		_LottoStart = Lotto;
	}

	private static long sTime = 0;

	public boolean isGmOpen = false;

	private int lottoAden = 0;

	public void addLottoAden(int i) {
		lottoAden += i;
	}

	public int getLottoAden() {
		return lottoAden;
	}

	public void setLottoAden(int i) {
		lottoAden = i;
	}

	private static final FastTable<L1PcInstance> sList = new FastTable<L1PcInstance>();

	private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

	public static LottoSystem getInstance() {
		if (_instance == null) {
			_instance = new LottoSystem();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				/** 오픈 **/
				if (!isOpen() && !isGmOpen)
					continue;
				if (L1World.getInstance().getAllPlayers().size() <= 0)
					continue;

				isGmOpen = false;

				/** 시작 메세지 **/
				L1World.getInstance().broadcastServerMessage("잠시 후 LOTTO 추첨이 시작됩니다.");
				L1World.getInstance()
						.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "잠시 후 LOTTO 추첨이 시작됩니다."));

				/** 로또게임 시작 **/
				setLottoStart(true);
				Thread.sleep(5000);

				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					pc.setLotto(0);
					if (pc.getLevel() >= Config.LOTTO_LEVEL) {
						Random lotto = new Random();
						int lottonumber = 0;
						lottonumber = lotto.nextInt(L1World.getInstance().getAllPlayersSize()) + 1;
						pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
						pc.sendPackets(new S_SystemMessage("" + pc.getName() + "님의 LOTTO 응모번호는 [" + lottonumber + "번] 입니다."));
						pc.sendPackets(new S_SystemMessage("30초 후 당첨자를 발표 합니다."));
						pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
						
						LottoSystem.getInstance().addLottoAden(Config.LOTTO_BATTING);
						pc.setLotto(lottonumber);
					}
				}

				Thread.sleep(25000L);

				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG5초 후 LOTTO 당첨자 발표가 있겠습니다."));
				Thread.sleep(5000);

				/** 당첨번호 발표 **/
				getNumber();

				Thread.sleep(5000);

				/** 당첨자 발표 및 금액 배분 **/
				giveAden();

				/** 로또 게임 종료 **/

				setLottoStart(false);
				End();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 오픈 시각을 가져온다
	 *
	 * @return (String) 오픈 시각(MM-dd HH:mm)
	 */
	public String OpenTime() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(sTime);
		return ss.format(c.getTime());
	}

	private void giveAden() {
		int count = 0;
		int lcount = 0;
		if (sList.size() > 0) {
			lcount = getLottoAden() + Config.LOTTO_BONUS;
			count = lcount / sList.size();
			for (L1PcInstance c : sList) {
				if (getLottoAden() > 150000000) {
					setLottoAden(150000000);
				}
				S_EffectLocation L = new S_EffectLocation(c.getX() - 2, c.getY() - 2, 6422);
				c.sendPackets(L, false);
				c.broadcastPacket(L);
				S_EffectLocation O = new S_EffectLocation(c.getX() - 1, c.getY() - 1, 6425);
				c.sendPackets(O, false);
				c.broadcastPacket(O);
				S_EffectLocation T = new S_EffectLocation(c.getX(), c.getY(), 6430);
				c.sendPackets(T, false);
				c.broadcastPacket(T);
				S_EffectLocation T2 = new S_EffectLocation(c.getX() + 1, c.getY() + 1, 6430);
				c.sendPackets(T2, false);
				c.broadcastPacket(T2);
				S_EffectLocation O2 = new S_EffectLocation(c.getX() + 2, c.getY() + 2, 6425);
				c.sendPackets(O2, false);
				c.broadcastPacket(O2);
				c.sendPackets(new S_SystemMessage("----------------------------------------------------"));
				c.sendPackets(new S_SystemMessage("축하합니다! LOTTO에 당첨되셨습니다!"));
				c.sendPackets(new S_SystemMessage("당첨금: 후원 코인 [" + count + "개]가 지급 되었습니다."));
				c.sendPackets(new S_SystemMessage("----------------------------------------------------"));
				
				L1World.getInstance().broadcastServerMessage("\\aD[" + c.getName() + "]님이 LOTTO에 당첨 되셨습니다!");
//				c.getInventory().storeItem(40308, count); // 아데나
				c.getInventory().storeItem(3000181, count); // 후원 코인
			}
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					"\\aDLOTTO 총 상금은 후원 코인 [" + lcount + "개]이며, [" + sList.size() + "명]의 당첨자가 나왔습니다"));
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aDLOTTO의 총 상금은 후원 코인" + lcount + "개 입니다."));
			L1World.getInstance()
					.broadcastPacketToAll(new S_SystemMessage("\\aD당첨자는 [" + sList.size() + "명] 입니다."));
			setLottoAden(0);
		} else {
			lcount = getLottoAden() + Config.LOTTO_BONUS;
			L1World.getInstance()
					.broadcastPacketToAll(new S_SystemMessage("\\aDLOTTO 총 상금은 후원 코인 [" + lcount + "개] 입니다."));
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aG당첨자가 없으므로 당첨금이 이월되었습니다."));
			setLottoAden(lcount);
		}
	}

	private void getNumber() {
		Random lotto = new Random();
		int number = 0;
		number = lotto.nextInt(L1World.getInstance().getAllPlayersSize()) + 1;
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getLotto() == number) {
				if (!sList.contains(pc)) {
					sList.add(pc); // 당첨자수 추가
				}
			}
			pc.sendPackets(new S_SystemMessage("\\aDLOTTO 당첨번호는 [" + number + "번] 입니다."));
		}
	}

	private boolean isOpen() {
		Calendar oCalendar = Calendar.getInstance();
		int hour = oCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = oCalendar.get(Calendar.MINUTE);
		int second = oCalendar.get(Calendar.SECOND);

		String[] open_time = Config.LOTTO_TIME.split(",");

		for (int i = 0; i < open_time.length; i++) {
			String[] time = open_time[i].split(":");
			if (hour == Integer.valueOf(time[0]) && minute == Integer.valueOf(time[1]) && second == 0) {
				return true;
			}
		}
		return false;
	}

	/** 종료 **/
	private void End() {
		sList.clear(); // 당첨자수 초기화
		setLottoStart(false);
	}
}
