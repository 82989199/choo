package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class L1Clear implements L1CommandExecutor{

	private L1Clear(){ }

	public static L1CommandExecutor getInstance(){
		return new L1Clear();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 20)) { // 20 범위 내에 오브젝트를 찾아서
			if (obj instanceof L1MonsterInstance){ // 몬스터라면
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				npc.receiveDamage(pc, 1000000); // 대미지
				if (npc.getCurrentHp() <= 0){
					pc.sendPackets(new S_SkillSound(obj.getId() , 4661));//2059
					pc.broadcastPacket(new S_SkillSound(obj.getId() , 4661));
				}else{
					pc.sendPackets(new S_SkillSound(obj.getId() , 4661));
					pc.broadcastPacket(new S_SkillSound(obj.getId() , 4661));
				}
			}
		}
	}
}
