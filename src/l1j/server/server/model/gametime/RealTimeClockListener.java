package l1j.server.server.model.gametime;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Light;

public class RealTimeClockListener implements TimeListener {

    @Override
    public void onMonthChanged(BaseTime time) {
        // 구현 내용 생략
    }

    @Override
    public void onDayChanged(BaseTime time) {
        // 구현 내용 생략
    }

    @Override
    public void onHourChanged(BaseTime time) {
        boolean isNight = time.isNight();
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
            if (isNight) {
                // 밤 시간 - 맵 밝기를 20으로 설정
                pc.sendPackets(new S_Light(pc.getId(), 20));
            } else {
                // 낮 시간 - 맵 밝기를 기본값으로 설정
                pc.sendPackets(new S_Light(pc.getId(), 0));
            }
        }
    }

    @Override
    public void onMinuteChanged(BaseTime time) {
        // 구현 내용 생략
    }

    @Override
    public void onSecondChanged(BaseTime time) {
        // 구현 내용 생략
    }
}
