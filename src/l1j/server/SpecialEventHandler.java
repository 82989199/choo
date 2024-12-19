package l1j.server;

import static l1j.server.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.COMA_B;
import static l1j.server.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.God_buff;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

enum SpecialEvent {
    BugRace, // 虫子赛跑
    AllBuf,  // 全体BUFF
    InfinityFight, // 无限战斗
    DoNotChatEveryone, // 禁止全体聊天
    DoChatEveryone // 允许全体聊天
};

// 游戏内全体事件处理类
public class SpecialEventHandler {

    private static volatile SpecialEventHandler uniqueInstance = null; // 单例实例

    private SpecialEventHandler() {
    }

    public static SpecialEventHandler getInstance() {
        if (uniqueInstance == null) {
            synchronized (SpecialEventHandler.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SpecialEventHandler();
                }
            }
        }

        return uniqueInstance;
    }

    // 发放昏睡祝福硬币
    public void doGiveEventStaff() {
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
            if (pc.getNetConnection() != null) {
                pc.getInventory().storeItem(30104, 1);
                L1ItemInstance item = ItemTable.getInstance().createItem(30104);
                pc.sendPackets(new S_GMHtml("发送者:" + pc.getName() + "", "物品:"+item.getLogName()+" 已到达。"));
                pc.sendPackets("\\aH管理员已发放昏睡祝福硬币。");
            }
        }
    }

    // 发放龙之黄玉
    public void doGiveEventStaff1() {
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
            if (pc.getNetConnection() != null) {
                pc.getInventory().storeItem(7241, 1);
                L1ItemInstance item = ItemTable.getInstance().createItem(7241);
                pc.sendPackets(new S_GMHtml("发送者:" + pc.getName() + "", "物品:"+item.getLogName()+" 已到达。"));
                pc.sendPackets("\\aH管理员已发放龙之黄玉。");
            }
        }
    }

    // 发放BOSS召唤卷轴
    public void doGiveEventStaff2() {
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
            if (pc.getNetConnection() != null) {
                pc.getInventory().storeItem(3000123, 1);
                L1ItemInstance item = ItemTable.getInstance().createItem(3000123);
                pc.sendPackets(new S_GMHtml("发送者:"+pc.getName()+"♥", "♥"+item.getLogName()+" 1张已到达♥　　 　(仅可在野外使用)　(请与血盟成员一起进行团队副本)"));
                pc.sendPackets("\\aG管理员已发放召唤卷轴1张。");
            }
        }
    }

    // 禁止世界聊天
    public void doNotChatEveryone() {
        L1World.getInstance().set_worldChatElabled(false);
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("世界聊天即将被禁用。"));
    }

    // 允许世界聊天
    public void doChatEveryone() {
        L1World.getInstance().set_worldChatElabled(true);
        L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("世界聊天即将被启用。"));
    }

}
