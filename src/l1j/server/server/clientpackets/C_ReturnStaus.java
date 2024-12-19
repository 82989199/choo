package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1ReturnStatTemp;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CheckInitStat;

public class C_ReturnStaus extends ClientBasePacket {
	private static boolean isMinus(int str, int dex, int con, int wis, int intel, int cha){
		return str <= 0 || dex <= 0 | con <= 0 || wis <= 0 || intel <= 0 || cha <= 0;
	}
	
	public C_ReturnStaus(byte[] decrypt, GameClient client) {
		super(decrypt);
		int type = readC();
		L1PcInstance pc = client.getActiveChar();
		
		if (pc == null || pc.getReturnStat() == 0) {
			return;
		}

		if (type == 1) {
			pc.rst = new L1ReturnStatTemp();

			short init_hp = 0, init_mp = 0;

			byte str= (byte) readC();
			byte intel = (byte) readC();
			byte wis= (byte) readC();
			byte dex=(byte) readC();
			byte con= (byte) readC();
			byte cha=(byte) readC();
			
			if(isMinus(str, dex, con, wis, intel, cha) || (str + intel + wis + dex + con + cha > 75)){
				client.kick();
				try {
					client.kick();
				} catch (Exception e) {
				}
				System.out.println("▶ 스텟버그 추방 : "+ pc.getName());
			}
			
			pc.rst.basestr = str;
			pc.rst.baseint = intel;
			pc.rst.basewis = wis;
			pc.rst.basedex = dex;
			pc.rst.basecon = con;
			pc.rst.basecha = cha;
			
			pc.getAbility().init();
			
			pc.getAbility().setBaseStr(str);
			pc.getAbility().setBaseInt(intel);
			pc.getAbility().setBaseWis(wis);
			pc.getAbility().setBaseDex(dex);
			pc.getAbility().setBaseCon(con);
			pc.getAbility().setBaseCha(cha);
					
			pc.rst.level = 1;

			if (pc.isCrown()) { // CROWN
				init_hp = 14;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 3;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
				break;
				}
			} else if (pc.isKnight()) { // KNIGHT
				init_hp = 16;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
				break;
				}
			} else if (pc.isElf()) { // ELF
				init_hp = 15;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 4;
				break;
				}
			} else if (pc.isWizard()) { // WIZ
				init_hp = 12;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 6;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 8;
					break;
				default:
					init_mp = 6;
				break;
				}
			} else if (pc.isDarkelf()) { // DE
				init_hp = 12;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 10:
				case 11:
					init_mp = 3;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 3;
				break;
				}
			} else if (pc.isDragonknight()) { // 용기사
				init_hp = 16;
				init_mp = 2;
			} else if (pc.isBlackwizard()) { // 환술사
				init_hp = 14;
			//	switch (pc.getAbility().getBaseWis()) {
			switch (pc.rst.basewis) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 5;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 5;
				break;
				}
			} else if (pc.is전사()) {
				init_hp = 16;
	//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			}

			pc.rst.baseHp = init_hp;
			pc.rst.baseMp = init_mp;
			pc.rst.upMp = 0;
			pc.rst.upHp = 0;
			pc.rst.ac = 10;	
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
		} else if (type == 2) {
			int levelup = readC();
			try{
				if (pc.rst.level > pc.getHighLevel() || (pc.rst.level == pc.getHighLevel() && levelup != 8 )) {
					System.out.println(String.format("%s님이 잘못된 레벨로 인해 스초를 다시 시작합니다.(원본레벨 : %d, 시도한 레벨 : %d)", pc.getName(), pc.getHighLevel(), pc.rst.level));
					pc.sendPackets(new S_SystemMessage("잘못된 스텟입니다."));
					client.kick();
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					return;
				}
			}catch(Exception e){
				return;
			}
			
			if (pc.rst.level <= 50
					&& levelup != 0
					&& levelup != 7
					&& levelup != 8 )
			{
				System.out.println(String.format("%s님이 50레벨 이하인데 스탯 추가를 시도했습니다. 시도레벨 : %d", pc.getName(), pc.rst.level));
				pc.sendPackets(new S_SystemMessage("잘못된 스텟입니다."));
				try {
					client.kick();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
				return;
			}
			
			switch (levelup) {
			case 0:
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 1:
				pc.rst.str =pc.rst.str+ 1;
				if(checking(pc, pc.rst.str)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. str : %d", pc.getName(),pc.rst.str));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 2:
				pc.rst.Int =pc.rst.Int+ 1;
				if(checking(pc, pc.rst.Int)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. Int : %d", pc.getName(),pc.rst.Int));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 3:
				pc.rst.wis =pc.rst.wis+ 1;
				if(checking(pc, pc.rst.wis)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. wis : %d", pc.getName(),pc.rst.wis));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 4:
				pc.rst.dex = pc.rst.dex + 1;
				if(checking(pc, pc.rst.dex)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. dex : %d", pc.getName(),pc.rst.dex));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 5:
				pc.rst.con =pc.rst.con+ 1;
				if(checking(pc, pc.rst.con)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. con : %d", pc.getName(),pc.rst.con));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 6:
				pc.rst.cha =pc.rst.cha+ 1;
				if(checking(pc, pc.rst.cha)){
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					System.out.print(String.format("%s 스텟 오버 감지.. cha : %d", pc.getName(),pc.rst.cha));
					try {
						client.kick();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				break;
			case 7:
				
				if (pc.rst.level >= 50) {
					return;
				}
				if ((pc.rst.level + 10) < pc.getHighLevel()) {
					for (int m = 0; m < 10; m++){
						if (pc.rst.level >= 50)	break;						
						statup(pc);						
					}
					pc.sendPackets(new S_ReturnedStat(pc,S_ReturnedStat.LEVELUP));
				}
				
				
				/*if (pc.rst.level > 40) {
					return;
				}
				if ((pc.rst.level + 10) < pc.getHighLevel()) {
					for (int m = 0; m < 10; m++){						
						statup(pc);						
					}
					pc.sendPackets(new S_ReturnedStat(pc,S_ReturnedStat.LEVELUP));
				}*/
				break;
			case 8:
				int statusup = readC();
				if (pc.rst.level > 50 )	{
					switch (statusup) {
					case 1:
						pc.rst.str = pc.rst.str + 1;
						if(checking(pc, pc.rst.str)){
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							System.out.print(String.format("%s 스텟 오버 감지.. str : %d", pc.getName(),pc.rst.str));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						break;
					case 2:
						pc.rst.Int = pc.rst.Int + 1;
						if(checking(pc, pc.rst.Int)){
							System.out.print(String.format("%s 스텟 오버 감지.. Int : %d", pc.getName(),pc.rst.Int));
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						break;
					case 3:
						pc.rst.wis = pc.rst.wis + 1;
						if(checking(pc, pc.rst.wis)){
							System.out.print(String.format("%s 스텟 오버 감지.. wis : %d", pc.getName(),pc.rst.wis));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							return;
						}
						break;
					case 4:
						pc.rst.dex = pc.rst.dex + 1;
						if(checking(pc, pc.rst.dex)){
							System.out.print(String.format("%s 스텟 오버 감지.. dex : %d", pc.getName(),pc.rst.dex));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							return;
						}
						break;
					case 5:
						pc.rst.con = pc.rst.con + 1;
						if(checking(pc, pc.rst.con)){
							System.out.print(String.format("%s 스텟 오버 감지.. con : %d", pc.getName(),pc.rst.con));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							return;
						}
						break;
					case 6:
						pc.rst.cha = pc.rst.cha + 1;
						if(checking(pc, pc.rst.cha)){
							System.out.print(String.format("%s 스텟 오버 감지.. cha : %d", pc.getName(),pc.rst.cha));
							try {
								client.kick();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							return;
						}
						break;
					}
				}
				if (pc.getElixirStats() > 0) {					
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.END));
				} else {
					try {
						if (pc.rst.level >= 51)
							pc.setBonusStats(pc.rst.level - 50);
						else
							//pc.rst.bonusStats = 0;
							pc.setBonusStats(0);
						pc.setExp(pc.getReturnStat());
		
						pc.addBaseMaxHp((short) (pc.rst.baseHp - pc.getBaseMaxHp()));
						pc.addBaseMaxMp((short) (pc.rst.baseMp - pc.getBaseMaxMp()));

						pc.addBaseMaxHp((short) pc.rst.upHp);
						pc.addBaseMaxMp((short) pc.rst.upMp);
						
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxHp());
						
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));

						pc.start_teleport(32610, 32779, 4, 5, 169, true, false); // 회상의 촛불 완료 -> 글루딘 마을로 도착
						pc.start_teleport(33445, 32797, 4, 5, 169, true, false); // 회상의 촛불 완료 -> 기란 마을로 도착
						pc.getAbility().init();
						
						
						pc.getAbility().setBaseStr(pc.rst.basestr);
						pc.getAbility().setBaseInt(pc.rst.baseint);
						pc.getAbility().setBaseWis(pc.rst.basewis);
						pc.getAbility().setBaseDex(pc.rst.basedex);
						pc.getAbility().setBaseCon(pc.rst.basecon);
						pc.getAbility().setBaseCha(pc.rst.basecha);
						
						pc.getAbility().addStr((byte) pc.rst.str);
						pc.getAbility().addInt((byte) pc.rst.Int);
						pc.getAbility().addWis((byte) pc.rst.wis);
						pc.getAbility().addDex((byte) pc.rst.dex);
						pc.getAbility().addCon((byte) pc.rst.con);
						pc.getAbility().addCha((byte) pc.rst.cha);
						
						pc.resetBaseAc();
						pc.getAC().setAc(pc.getBaseAc());
						
						pc.sendPackets(new S_OwnCharStatus(pc));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.END));
						//RenewStat(pc);
						pc.RenewStat();
						
						pc.LoadCheckStatus();
						if(!CheckInitStat.CheckPcStat(pc)){
							client.kick();
//							pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
							return;
						}
						pc.setReturnStat(0);
						pc.save();
						pc.rst = null;
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				break;
			}
		} else if (type == 3) { // 스텟 초기화시 엘릭서 처리
			try {
				int str = readC();
				int intel = readC();
				int wis = readC();
				int dex = readC();
				int con = readC();
				int cha = readC();				
				if(isMinus(str, dex, con, wis, intel, cha)){
					client.kick();
					try {
						client.kick();
					} catch (Exception e) {
					}
					System.out.println("▶ 스텟버그 추방 : "+ pc.getName());
				}
				
				str -= pc.getAbility().getStr();
				intel -= pc.getAbility().getInt();
				wis -= pc.getAbility().getWis();
				dex -= pc.getAbility().getDex();
				con -= pc.getAbility().getCon();
				cha -= pc.getAbility().getCha();				

//				int elixerStatSum = str + intel + wis + dex + con + cha;
//				
//				if( elixerStatSum > pc.getElixirStats()
//						|| str < 0
//						|| intel < 0
//						|| wis < 0
//						|| dex < 0
//						|| con < 0
//						|| cha < 0)
//				{
//					pc.sendPackets(new S_SystemMessage("잘못된 스텟입니다."));
//					System.out.println("잘못");
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
//					return;
//				}

				pc.getAbility().addStr((byte) (str));
				pc.getAbility().addInt((byte) (intel));
				pc.getAbility().addWis((byte) (wis));
				pc.getAbility().addDex((byte) (dex));
				pc.getAbility().addCon((byte) (con));
				pc.getAbility().addCha((byte) (cha));

				if (pc.getLevel() >= 51)
					pc.setBonusStats(pc.getLevel() - 50);
				else
					pc.setBonusStats(0);

				pc.setExp(pc.getReturnStat());
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				
				pc.addBaseMaxHp((short) (pc.rst.baseHp - pc.getBaseMaxHp()));
				pc.addBaseMaxMp((short) (pc.rst.baseMp - pc.getBaseMaxMp()));

				pc.addBaseMaxHp((short) pc.rst.upHp);
				pc.addBaseMaxMp((short) pc.rst.upMp);
				
				pc.setCurrentHp(pc.getMaxHp());
				pc.setCurrentMp(pc.getMaxHp());
				
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.start_teleport(32610, 32779, 4, 5, 169, true, false);
				pc.LoadCheckStatus();
				if(!CheckInitStat.CheckPcStat(pc)){
					client.kick();
//					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					return;
				}
				pc.setReturnStat(0);
				pc.save();
				
				pc.rst = null;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	private boolean checking(L1PcInstance pc, int stat){
		int maximum = 45;
		if(pc.getHighLevel() >= 90){
			maximum = 50;
		}
		
		return stat > maximum || stat < 1;
	}
	
	public void statup(L1PcInstance pc) {
		int Stathp = 0;
		int Statmp = 0;
		pc.rst.level += 1;
		Stathp = CalcStat.increaseHp(pc.getType(), (byte) (pc.rst.con + pc.rst.basecon));
		Statmp = CalcStat.increaseMp(pc.getType(), (byte) (pc.rst.wis + pc.rst.basewis));

		pc.rst.upHp = pc.rst.upHp +  Stathp;
		pc.rst.upMp = pc.rst.upMp +  Statmp;
	}
}
