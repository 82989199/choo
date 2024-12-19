package l1j.server.server.model.skill;

import java.sql.Timestamp;

import l1j.server.server.Controller.SkillDataController;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.IntRange;

public class SkillData {
	
	private L1PcInstance pc;
	
	public SkillData(L1PcInstance cha) {
		pc = cha;
	}
	
	private boolean str_ice;
	
	public boolean get_str_ice() {
		return str_ice;
	} 
	
	private int str_ice_time;
	public int get_str_ice_time() {
		return str_ice_time;
	}
	public void set_str_ice_time(int i) {
		this.str_ice_time = i;
	}
	
	public void start_str_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		if (!str_ice) {
			str_ice = true;
			str_ice_time = 900;
			pc.addHitup(5);
			pc.addDmgup(3);
			pc.getAbility().addAddedStr(1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7954), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7954), true);
			SkillDataController.getInstance().add_str_ice(pc);
		}
	}
	public void end_str_ice() {
		if (str_ice) {
			str_ice = false;
			str_ice_time = 0;
			pc.addHitup(-5);
			pc.addDmgup(-3);
			pc.getAbility().addAddedStr(-1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
		}
	}
	
	private boolean dex_ice;
	
	public boolean get_dex_ice() {
		return dex_ice;
	} 
	
	private int dex_ice_time;
	public int get_dex_ice_time() {
		return dex_ice_time;
	}
	public void set_dex_ice_time(int i) {
		this.dex_ice_time = i;
	}
	
	public void start_dex_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		if (!dex_ice) {
			dex_ice = true;
			dex_ice_time = 900;
			pc.addBowHitup(5);
			pc.addBowDmgup(3);
			pc.getAbility().addAddedDex((byte) 1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7952), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7952), true);
			SkillDataController.getInstance().add_dex_ice(pc);
		}
	}
	public void end_dex_ice() {
		if (dex_ice) {
			dex_ice = false;
			dex_ice_time = 0;
			pc.addBowHitup(-5);
			pc.addBowDmgup(-3);
			pc.getAbility().addAddedDex(-1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);
		}
	}
	
	private boolean int_ice;
	
	public boolean get_int_ice() {
		return int_ice;
	} 
	
	private int int_ice_time;
	public int get_int_ice_time() {
		return int_ice_time;
	}
	public void set_int_ice_time(int i) {
		this.int_ice_time = i;
	}
	
	public void start_int_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		if (!int_ice) {
			int_ice = true;
			int_ice_time = 900;
			pc.addMaxMp(50);
			pc.getAbility().addSp(2);
			pc.getAbility().addAddedInt(1);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7956), true);			
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7956), true);
			SkillDataController.getInstance().add_int_ice(pc);
		}
	}
	public void end_int_ice() {
		if (int_ice) {
			int_ice = false;
			int_ice_time = 0;
			pc.addMaxMp(-50);
			pc.getAbility().addSp(-2);
			pc.getAbility().addAddedInt(-1);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
		}
	}
	private L1ItemInstance halpas;
	public L1ItemInstance getHalpas() {
		return halpas;
	}
	public void setHalpas(L1ItemInstance item) {
		this.halpas = item;
	}

	private Timestamp halpasTime;
	public Timestamp getHalpasTime() {
		return halpasTime;
	}
	public void setHalpasTime(Timestamp time) {
		this.halpasTime = time;
	}
	
	public boolean isHalpasTime() {
		if (halpasTime == null) {
			return false;
		}

		if (System.currentTimeMillis() - halpasTime.getTime() > 0) {
			return false;
		}
		return true;
	}

}
