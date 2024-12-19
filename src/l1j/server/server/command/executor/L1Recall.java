package l1j.server.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Recall implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Recall.class.getName());

	private L1Recall() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Recall();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			Collection<L1PcInstance> targets = null;
			if (arg.equalsIgnoreCase("소환")) {
				targets = L1World.getInstance().getAllPlayers();
			} else {
				targets = new ArrayList<L1PcInstance>();
				L1PcInstance tg = L1World.getInstance().getPlayer(arg);
				if (tg == null) {
					pc.sendPackets(new S_SystemMessage("그러한 캐릭터는 없습니다. "));
					return;
				}
				targets.add(tg);
			}

			for (L1PcInstance target : targets) {
				if (target.isPrivateShop()) {
					pc.sendPackets(new S_SystemMessage(target.getName() + " 님은 상점모드 입니다."));
					return;
				}
				if (pc.getMapId() == 5554) {
					target.sendPackets(new S_Ability(3, true));
				}
				
				L1Location loc = L1Teleport.getInstance().소환텔레포트(pc, 1);
				target.start_teleportForGM(loc.getX(), loc.getY(), loc.getMapId(), target.getHeading(), 169, true, true);
				pc.sendPackets(new S_SystemMessage("잠시후 ( "+target.getName()+" ) 님을 소환 합니다. "));
				target.sendPackets(new S_SystemMessage("메티스님께서 당신을 소환 하였습니다."));
			}
			
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + ".소환:캐릭터명 으로 입력해 주세요.]"));
		}
	}
}
