/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.Controller;

import java.util.Random;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;

public class ShipTimeController implements Runnable {
	private static ShipTimeController _instance;

	private Random _random = new Random(System.nanoTime());
	public static ShipTimeController getInstance() {
		if (_instance == null) {
			_instance = new ShipTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			checkShipTime(); // 배의 도착 시간을 체크

			GeneralThreadPool.getInstance().schedule(this, 5000);
		} catch (Exception e1) {
		}
	}

	private void checkShipTime() {
		int servertime = L1GameTimeClock.getInstance().getGameTime().getSeconds();
		int nowtime = servertime % 86400;		
		//잊섬 작업
		잊섬Controller 잊 = 잊섬Controller.getInstance();
		if (잊.get잊섬Start()) { 
			long time = System.currentTimeMillis();
			if(잊.is배탑승() && 잊.openTime + (180*1000) <= time) {
				잊.set배탑승(false);
				잊.openTime = time;
//				L1World.getInstance().broadcastServerMessage("\\aA출항! 잊섬 배가 출항 하였습니다.  2분후 도착합니다.");
			}
			if(!잊.is배탑승() && 잊.openTime  + (120 * 1000) < time) {				
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {//도착
					if (pc.getMapId() == 1706) {
						int i13 = 32936 + _random.nextInt(5);
						int k19 = 33037 + _random.nextInt(5);
						pc.start_teleport(i13, k19, 1700, pc.getHeading(), 169, true, false);
						pc.getInventory().consumeItem(40300, 1); // 잊혀진 섬행 배표
					}
				}
				잊.openTime = time;
				잊.set배탑승(true);
//				L1World.getInstance().broadcastServerMessage("\\aA잊섬행 배가 섬에 도착하였습니다. 탑승가능합니다.");
			}
		}

		if (nowtime >= 90 * 60 && nowtime < 180 * 60 // 1:30~3:00
				|| nowtime >= 270 * 60 && nowtime < 360 * 60 // 4:30~6:00
				|| nowtime >= 450 * 60 && nowtime < 540 * 60 // 7:30~9:00
				|| nowtime >= 630 * 60 && nowtime < 720 * 60 // 10:30~12:00
				|| nowtime >= 810 * 60 && nowtime < 900 * 60 // 13:30~15:00
				|| nowtime >= 990 * 60 && nowtime < 1080 * 60 // 16:30~18:00
				|| nowtime >= 1170 * 60 && nowtime < 1260 * 60 // 19:30~21:00
				|| nowtime >= 1350 * 60 && nowtime < 1440 * 60 // 22:30~24:00
				) { 
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {//도착
				if (pc.getMapId() == 1708) {
					pc.start_teleport(32936, 33037, 1708, 5, 169, false, false);
					pc.getInventory().consumeItem(40300, 1); // 잊혀진 섬행 배표
				}
			}
		}		
		if (nowtime >= 0 && nowtime < 90 * 60 // 0:00~1:30
				|| nowtime >= 180 * 60 && nowtime < 270 * 60 // 3:00~4:30
				|| nowtime >= 360 * 60 && nowtime < 450 * 60 // 6:00~7:30
				|| nowtime >= 540 * 60 && nowtime < 630 * 60 // 9:00~10:30
				|| nowtime >= 720 * 60 && nowtime < 810 * 60 // 12:00~13:30
				|| nowtime >= 900 * 60 && nowtime < 990 * 60 // 15:00~16:30
				|| nowtime >= 1080 * 60 && nowtime < 1170 * 60 // 18:00~19:30
				|| nowtime >= 1260 * 60 && nowtime < 1350 * 60 // 21:00~22:30
				) { 
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 84) {
					//					L1Teleport.teleport(pc, 33426, 33501, (short) 4, 0, false);
					pc.start_teleport(33426, 33501, 4, 5, 169, false, false);
					pc.getInventory().consumeItem(40301, 1); // 하이네행 배표
				}
			}
		}
		if (nowtime >= 0 && nowtime < 90 * 60 // 0:00~1:30
				|| nowtime >= 180 * 60 && nowtime < 270 * 60 // 3:00~4:30
				|| nowtime >= 360 * 60 && nowtime < 450 * 60 // 6:00~7:30
				|| nowtime >= 540 * 60 && nowtime < 630 * 60 // 9:00~10:30
				|| nowtime >= 720 * 60 && nowtime < 810 * 60 // 12:00~13:30
				|| nowtime >= 900 * 60 && nowtime < 990 * 60 // 15:00~16:30
				|| nowtime >= 1080 * 60 && nowtime < 1170 * 60 // 18:00~19:30
				|| nowtime >= 1260 * 60 && nowtime < 1350 * 60 // 21:00~22:30
				) { 
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 447) {
					//					L1Teleport.teleport(pc, 32297, 33087, (short) 440, 0, false);
					pc.start_teleport(32297, 33087, 440, pc.getHeading(), 169, false, false);
					pc.getInventory().consumeItem(40302, 1); // 해적섬행 배표
				}
			}
		}
		if (nowtime >= 90 * 60 && nowtime < 180 * 60 // 1:30~3:00
				|| nowtime >= 270 * 60 && nowtime < 360 * 60 // 4:30~6:00
				|| nowtime >= 450 * 60 && nowtime < 540 * 60 // 7:30~9:00
				|| nowtime >= 630 * 60 && nowtime < 720 * 60 // 10:30~12:00
				|| nowtime >= 810 * 60 && nowtime < 900 * 60 // 13:30~15:00
				|| nowtime >= 990 * 60 && nowtime < 1080 * 60 // 16:30~18:00
				|| nowtime >= 1170 * 60 && nowtime < 1260 * 60 // 19:30~21:00
				|| nowtime >= 1350 * 60 && nowtime < 1440 * 60 // 22:30~24:00
				) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 446) {
					//					L1Teleport.teleport(pc, 32750, 32874, (short) 445, 0, false);
					pc.start_teleport(32750, 32874, 445, pc.getHeading(), 169, false, false);
					pc.getInventory().consumeItem(40303, 1); // 숨겨진 섬 선착장행 배표
				}
			}
		}

		if (nowtime >= 3 * 1800 && nowtime < 5 * 1800
				|| nowtime >= 9 * 1800 && nowtime < 11 * 1800
				|| nowtime >= 15 * 1800 && nowtime < 17 * 1800
				|| nowtime >= 21 * 1800 && nowtime < 23 * 1800
				|| nowtime >= 27 * 1800 && nowtime < 29 * 1800
				|| nowtime >= 33 * 1800 && nowtime < 35 * 1800
				|| nowtime >= 39 * 1800 && nowtime < 41 * 1800
				|| nowtime >= 45 * 1800 && nowtime < 47 * 1800) {

			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 6) {
					//					L1Teleport.teleport(pc, 32631, 32983, (short) 0, 0, false);
					pc.start_teleport(32631, 32983, 0, pc.getHeading(), 169, false, false);
					pc.getInventory().consumeItem(40298, 1); // 말하는 섬행 배표
				}
			}
		}

		if (nowtime >= 0 * 1800 && nowtime < 2 * 1800
				|| nowtime >= 6 * 1800 && nowtime < 8 * 1800
				|| nowtime >= 12 * 1800 && nowtime < 14 * 1800
				|| nowtime >= 18 * 1800 && nowtime < 20 * 1800
				|| nowtime >= 24 * 1800 && nowtime < 26 * 1800
				|| nowtime >= 30 * 1800 && nowtime < 32 * 1800
				|| nowtime >= 36 * 1800 && nowtime < 38 * 1800
				|| nowtime >= 42 * 1800 && nowtime < 44 * 1800) {

			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 5) {
					//					L1Teleport.teleport(pc, 32538, 32728, (short) 4, 0, false);
					pc.start_teleport(32538, 32728, 4, pc.getHeading(), 169, false, false);
					pc.getInventory().consumeItem(40299, 1); // 본토행 배표
				}
			}
		}

	}
}
