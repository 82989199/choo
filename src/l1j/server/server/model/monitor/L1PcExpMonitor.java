package l1j.server.server.model.monitor;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;

public class L1PcExpMonitor extends L1PcMonitor {
	private int _old_lawful;
	private int _old_exp;

	public L1PcExpMonitor(int oId) {
		super(oId);
	}

	@Override
	public void execTask(L1PcInstance pc) {
		if (_old_lawful != pc.getLawful()) {
			_old_lawful = pc.getLawful();
			S_Lawful s_lawful = new S_Lawful(pc.getId(), _old_lawful);
			pc.sendPackets(s_lawful);
			pc.broadcastPacket(s_lawful);
//			lawful_bonus(pc);
		}

		if (_old_exp != pc.getExp()) {
			_old_exp = pc.getExp();
			pc.onChangeExp();
		}
	}
	
//	static class LawfulBonusInfo{//0612여기부터
//		int ac;
//		int mr;
//		int sp;
//		int add_damage;
//		int min_lawful;
//		int max_lawful;
//		int level_num;
//		
//		LawfulBonusInfo(int ac, int mr, int sp, int add_damage, int min_lawful, int max_lawful, int level_num){
//			this.ac = ac;
//			this.mr = mr;
//			this.sp = sp;
//			this.add_damage = add_damage;
//			this.min_lawful = min_lawful;
//			this.max_lawful = max_lawful;
//			this.level_num = level_num;
//		}
//		
//		boolean is_range(L1PcInstance pc){
//			int lawful = pc.getLawful();
//			return lawful >= min_lawful && lawful <= max_lawful;
//		}
//		
//		void off_update(L1PcInstance pc){
//			pc.getAC().addAc(ac * -1);
//			pc.getResistance().addMr(mr * -1);
//			pc.getAbility().addSp(sp * -1);
//			pc.addDmgRate(add_damage * -1);
//			pc.addBowDmgRate(add_damage * -1);
//			// do off
//			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, level_num, false));
//		}
//		
//		void on_update(L1PcInstance pc){
//			int current_bapo_level = pc.get_bapo_level();
//			if(current_bapo_level == level_num)
//				return;
//			
//			LawfulBonusInfo prev_bonus = m_lawful_bonuses[current_bapo_level];
//			prev_bonus.off_update(pc);
//			
//			// do on
//			pc.set_bapo_level(level_num);
//			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, level_num, true));
//			
//			pc.getAC().addAc(ac);
//			pc.getResistance().addMr(mr);
//			pc.getAbility().addSp(sp);
//			pc.addDmgRate(add_damage);
//			pc.addBowDmgRate(add_damage);
//			pc.sendPackets(new S_OwnCharStatus(pc));
//			pc.sendPackets(new S_SPMR(pc));
//		}
//	}
//	private static final LawfulBonusInfo[] m_lawful_bonuses;
//	public static final int NONE_STATE_BAPO_LEVEL = 6;
//	static{
//		// ac, mr, sp, add_damage, min_lawful, max_lawful, level_num
//		m_lawful_bonuses = new LawfulBonusInfo[]{
//			new LawfulBonusInfo(-2, 3, 0, 0, 1001, 10000, 0),
//			new LawfulBonusInfo(-4, 6, 0, 0, 10001, 20000, 1),
//			new LawfulBonusInfo(-6, 9, 0, 0, 20000, 99999, 2),
//			new LawfulBonusInfo(0, 0, 1, 1, -10000, -1, 3),
//			new LawfulBonusInfo(0, 0, 2, 3, -20000, -10001, 4),
//			new LawfulBonusInfo(0, 0, 3, 5, -99999, -20001, 5),
//			new LawfulBonusInfo(0, 0, 0, 0, 1000, 0, NONE_STATE_BAPO_LEVEL),
//		};
//		
//	}
//	
//	private void lawful_bonus(L1PcInstance pc){
//		if(pc.getLevel() < Config.NEW_PLAYER){
//			m_lawful_bonuses[NONE_STATE_BAPO_LEVEL].on_update(pc);
//			return;
//		}
//		
//		for(LawfulBonusInfo bInfo : m_lawful_bonuses){
//			if(bInfo.is_range(pc)){
//				bInfo.on_update(pc);
//				break;
//			}
//		}
//	}//0612여기까지
	/*
	private void LawfulBonus(L1PcInstance pc) {
		int ACvalue = 0;
		int MRvalue = 0;
		int SPvalue = 0;
		int ATvalue = 0;
		 int bapo = 0;
		if (pc.getLawful() >= 30000 && pc.getLawful() <= 32768) {
			ACvalue = -6;
			MRvalue = 9;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 2;
			pc.setNBapoLevel(bapo);
		} else if (pc.getLawful() >= 20000 && pc.getLawful() <= 29999) {
			ACvalue = -4;
			MRvalue = 6;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 1;
			pc.setNBapoLevel(bapo);
		} else if (pc.getLawful() >= 10000 && pc.getLawful() <= 19999) {
			ACvalue = -2;
			MRvalue = 3;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 0;
			pc.setNBapoLevel(bapo);
		} else if (pc.getLawful() >= -9999 && pc.getLawful() <= 9999) {
			SPvalue = 0;
			ATvalue = 0;
			ACvalue = 0;
			MRvalue = 0;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 7;
			pc.setNBapoLevel(bapo);
		} else if (pc.getLawful() <= -10000 && pc.getLawful() >= -19999) {
			SPvalue = 1;
			ATvalue = 1;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 3;
			pc.setNBapoLevel(bapo);
		} else if (pc.getLawful() <= -20000 && pc.getLawful() >= -29999) {
			SPvalue = 2;
			ATvalue = 3;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 4;
			pc.setNBapoLevel(bapo);
		//} else if (pc.getLawful() <= -30000 && pc.getLawful() >= -32768) {
		} else {
			SPvalue = 3;
			ATvalue = 5;
			pc.setOBapoLevel(pc.getNBapoLevel());
			bapo = 5;
			pc.setNBapoLevel(bapo);
		}
		
		// 
		if (pc.getOBapoLevel() != pc.getNBapoLevel()) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, pc.getOBapoLevel(), false));
			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, pc.getNBapoLevel(), true));
			pc.setOBapoLevel(pc.getNBapoLevel());
			if (pc.getLevel() < Config.NEW_PLAYER){
				pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 6, true));
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 6, false));
			}
			// 조우(라우풀) 갱신 시 혈마크 사라지는 문제.
			//pc.sendPackets(new S_OwnCharPack(pc));
		}
		//
		
		if (ACvalue != 0 && MRvalue != 0) {
			if (ACvalue != pc.LawfulAC) {
				if (pc.LawfulAC != 0) {
					pc.getAC().addAc(pc.LawfulAC * -1);
				}
				pc.LawfulAC = ACvalue;
				pc.getAC().addAc(ACvalue);
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			if (MRvalue != pc.LawfulMR) {
				if (pc.LawfulMR != 0) {
					pc.getResistance().addMr(pc.LawfulMR * -1);
				}
				pc.LawfulMR = MRvalue;
				pc.getResistance().addMr(MRvalue);
				pc.sendPackets(new S_SPMR(pc));
				
			}
			
			if (SPvalue != pc.LawfulSP){
				if(pc.LawfulSP != 0){
					pc.getAbility().addSp(pc.LawfulSP * -1);
				}
				pc.LawfulSP = SPvalue;
				pc.getAbility().addSp(SPvalue);
				pc.sendPackets(new S_SPMR(pc));
			}
			if (pc.LawfulSP != 0) {
				pc.getAbility().addSp(pc.LawfulSP * 1);
				pc.LawfulSP = 0;
				pc.sendPackets(new S_SPMR(pc));
			}
			
		} else {
			if (pc.LawfulAC != 0) {
				pc.getAC().addAc(pc.LawfulAC * -1);
				pc.LawfulAC = 0;
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			if (pc.LawfulMR != 0) {
				pc.getResistance().addMr(pc.LawfulMR * -1);
				pc.LawfulMR = 0;
				pc.sendPackets(new S_SPMR(pc));
				
			}
			
			if (ATvalue != 0){
				if (pc.LawfulAT != 0) {
					pc.setBapodmg(pc.LawfulAT * -1);
				}
				pc.LawfulAT = ATvalue;
				pc.setBapodmg(ATvalue);
				pc.sendPackets(new S_OwnCharStatus(pc));
				
			} else if (pc.LawfulAT != 0) {
				pc.setBapodmg(pc.LawfulAT * -1);
				pc.LawfulAT = 0;
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			
			if (SPvalue != 0) {
				if (pc.LawfulSP != 0) {
					pc.getAbility().addSp(pc.LawfulSP * -1);
				}
				pc.LawfulSP = SPvalue;
				pc.getAbility().addSp(SPvalue);
				pc.sendPackets(new S_SPMR(pc));
			} else if (pc.LawfulSP != 0) {
				pc.getAbility().addSp(pc.LawfulSP * -1);
				pc.LawfulSP = 0;
				pc.sendPackets(new S_SPMR(pc));
			}
		}
	}*/
	
}
