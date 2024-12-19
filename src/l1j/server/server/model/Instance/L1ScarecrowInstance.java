package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.model.L1Attack;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;

public class L1ScarecrowInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}
	
    // 클래스 멤버 변수로 선언
    private int attackCount = 0;
    private int totalDamage = 0;
    private int minDamage = Integer.MAX_VALUE; // 최소 대미지를 저장할 변수 초기화
    private int maxDamage = Integer.MIN_VALUE; // 최대 대미지를 저장할 변수 초기화

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		if (player.isInParty() || player.isDead()) { 
			player.sendPackets("파티중에는 허수아비를 공격할 수 없습니다.");
			return;
		}
		if (attack.calcHit() && player.getAI() == null) {
			if (player.getLevel() < Config.SCARELEVEL && !player.noPlayerCK) {
				ArrayList<L1PcInstance> targetList = new ArrayList<L1PcInstance>();
				targetList.add(player);
				ArrayList<Integer> hateList = new ArrayList<Integer>();
				hateList.add(1);
				CalcExp.calcExp(player, getId(), targetList, hateList, getExp());
			}
			if (player.getLevel() >= 1 && !player.noPlayerCK) {// 탐지급 레벨
				player.getInventory().storeItem(41302, Config.tamsc1);
				player.getInventory().storeItem(40308, Config.tamsc2);
				player.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, player.getNetConnection()), true);// 탐지급
				player.getNetConnection().getAccount().tam_point += Config.tamsc;// 탐지급갯수
				player.getNetConnection().getAccount().updateTam();// 탐업뎃
			}
            int dmg = attack.calcDamage();
            if (this.getNpcId() == 7320088) {
                player.sendPackets(new S_SystemMessage("물리대미지: [" + dmg + "]"));

                attackCount++;
                totalDamage += dmg;

                // 최소 및 최대 대미지 업데이트
                if (dmg < minDamage) {
                    minDamage = dmg;
                }
                if (dmg > maxDamage) {
                    maxDamage = dmg;
                }

                if (attackCount >= 10) {
                    int averageDamage = totalDamage / attackCount;
                    player.sendPackets(new S_SystemMessage("10회 평균: [" + averageDamage + "], 최소: [" + minDamage + "], 최대: [" + maxDamage + "]"));
                    attackCount = 0;
                    totalDamage = 0;
                    minDamage = Integer.MAX_VALUE; // 최소 대미지 초기화
                    maxDamage = Integer.MIN_VALUE; // 최대 대미지 초기화
                }
            }
            if (getHeading() < 7) {
                setHeading(getHeading() + 1);
            } else {
                setHeading(0);
            }
            broadcastPacket(new S_ChangeHeading(this));
        }
        attack.action();
    }

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {}
	public void onFinalAction() {}
	public void doFinalAction() {}
}