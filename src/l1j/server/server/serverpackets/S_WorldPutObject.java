package l1j.server.server.serverpackets;

import MJShiftObject.Battle.Thebe.MJThebeCharacterInfo;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJInstanceSystem.MJInstanceObject;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MJNpcMarkTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SignboardInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.MJCommons;

public class S_WorldPutObject extends ServerBasePacket {
	// 잊섬 마크 표시할지 여부...(트루 : 사용, 펄스 : 사용 안함)
	public static final boolean IS_PRESENTATION_MARK = true;
	
	private static final int SC_WORLD_PUT_OBJECT_NOTI = 0x77;
	public L1PcInstance _pc = null;
	@SuppressWarnings("unused")
	public static S_WorldPutObject put(L1PcInstance pc){	
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeC(0x08);			// point
		s.writePoint(pc.getX(), pc.getY());
		s.writeC(0x10);			// objectnumber
		s.writeBit(pc.getId());
		s.writeC(0x18); 		// objectsprite
		s.writeBit(pc.getCurrentSpriteId());
		
		s.writeC(0x20); 		// action
		if (pc.isDead())			s.writeBit(ActionCodes.ACTION_Die);
		else if (pc.isPrivateShop())s.writeBit(70L);
		else if (pc.isFishing())	s.writeBit(71L);
		else						s.writeBit(pc.getCurrentWeapon());
		s.writeC(0x28); 		// direction
		s.writeC(pc.getHeading());
		s.writeC(0x30); 		// lightRadius
		if(pc.getLight() == null)
			s.writeC(0x00);
		else
			s.writeBit(pc.getLight().getOwnLightSize());
		s.writeC(0x38); 		// objectcount
		s.writeC(0x01);
		s.writeC(0x40); 		// alignment(lawful)
		s.writeBit(pc.getLawful());
		s.writeC(0x4A);			// desc
		if (/*pc.isGm() &&*/ pc.hasSkillEffect(L1SkillId.INVISIBILITY))
			s.writeS2("");
		else{
			if(pc.is_shift_battle()){
				String server_description = pc.get_server_description();
				MJThebeCharacterInfo cInfo = pc.get_thebe_info();
				if(MJCommons.isNullOrEmpty(server_description) || cInfo == null){
					s.writeS2("미지인");
				}else{
					s.writeS2(cInfo.to_name_pair());					
				}
			}
			else if(pc.is_shift_transfer())
				s.writeS2("서버이전중");
			else
				s.writeS2(pc.getName());
		}
		s.writeC(0x52); 		// title
		s.writeS2(getTitle(pc));
		s.writeC(0x58); 		// speed data
		s.writeB(pc.isHaste());
		s.writeC(0x60); 		// emotion
		if(pc.isBrave() || pc.isBlood_lust()) s.writeC(0x01);
		else if(pc.isElfBrave())	s.writeC(0x03);
		else if(pc.isFastMovable() || pc.isFruit() || pc.isUgdraFruit()) s.writeC(0x04);
		else						s.writeC(0x00);
		s.writeC(0x68);			// drunken
		s.writeBit(0x00);
		s.writeC(0x70); 		// isghost
		s.writeB(pc.isGhost());
		s.writeC(0x78); 		// isparalyzed
		s.writeB(pc.isParalyzed());
		s.writeBit(0x80);		// isuser
		s.writeC(0x01);
		s.writeBit(0x88);		// isinvisible
		s.writeB(pc.isInvisble());
		s.writeBit(0x90); 		// ispoisoned
		s.writeB(pc.getPoison());
		s.writeBit(0x98);		// emblemid
		s.writeClanInfo(pc);
		//s.writeBit(0xA2); 	// pledgename
		//s.writeS2();
		s.writeBit(0xAA); 		// mastername
		s.writeC(0x00);
		s.writeBit(0xB0); 		// altitude
		s.writeC(0x00);
		s.writeBit(0xB8);		// hitratio
		s.writeBit(-1);
		s.writeBit(0xC0); 		// safelevel
		s.writeBit(pc.getLevel());
		s.writeBit(0xCA); 		// shop title
		if(pc.getShopChat() != null && pc.getShopChat().length > 0){
			s.writeC(pc.getShopChat().length);
			s.writeByte(pc.getShopChat());
		}else
			s.writeC(0x00);
		s.writeBit(0xD0); 		// weapon sprite
		s.writeBit(-1);
		s.writeBit(0xD8); 		// couplestate
		s.writeB(false);
		s.writeBit(0xE0); 		// boundarylevelindex
		s.writeBoundaryLevel(pc);
		s.writeBit(0xE8); 		// weakelemental
		s.writeBit(0x00);
		s.writeBit(0xF0);		// manaratio
		s.writeBit(-1);
		s.writeBit(0xF8);		// botindex
		s.writeBit(0x00);
		s.writeBit(0x100); 		// home server no
		s.writeC(0x00);
		s.writeBit(0x108); 		// team_id
		int mark = pc.get_mark_status();
		if(mark > 0 && !pc.is_combat_field()){
			pc.set_instance_status(MJEPcStatus.WORLD);
			mark = 0;
		} else {
			mark = writeTeamId(pc, true, mark);
		}
		s.writeBit(mark);
		s.writeBit(0x118); 		// speed_value_flag
		s.writeC(0x00);
		s.writeBit(0x120);		// second_speed_type
		s.writeC(0x00);
		if(pc.getClanRank() == L1Clan.군주){
			if(MJWar.isCastleOffenseClan(pc.getClan())){
				s.writeBit(0x130);
				s.writeB(true);
			}
		}

		s.writeH(0x00);
		return s;
	}
	
	public static S_WorldPutObject get(L1PcInstance pc, String name, Boolean isClan){		
		S_WorldPutObject s = new S_WorldPutObject();
		s._pc = pc;
		
		s.writeC(0x08);			// point
		s.writePoint(pc.getX(), pc.getY());
		s.writeC(0x10);			// objectnumber
		s.writeBit(pc.getId());
		s.writeC(0x18); 			// objectsprite
		s.writeBit(pc.getCurrentSpriteId());
		s.writeC(0x20); 		// action
		if (pc.isDead())			s.writeBit(ActionCodes.ACTION_Die);
		else if (pc.isPrivateShop())s.writeBit(70L);
		else if (pc.isFishing())	s.writeBit(71L);
		else						s.writeBit(pc.getCurrentWeapon());
		
		s.writeC(0x28); 		// direction
		s.writeC(pc.getHeading());
		s.writeC(0x30); 		// lightRadius
		if(pc.getLight() == null)
			s.writeC(0x00);
		else
			s.writeBit(pc.getLight().getOwnLightSize());
		s.writeC(0x38); 		// objectcount
		s.writeC(0x01);
		s.writeC(0x40); 		// alignment(lawful)
		s.writeBit(pc.getLawful());
		s.writeC(0x4A);			// desc
		if (pc.isGm() && pc.hasSkillEffect(L1SkillId.INVISIBILITY))
			s.writeS2("");
		else{
			if(pc.is_shift_battle()){
				String server_description = pc.get_server_description();
				MJThebeCharacterInfo cInfo = pc.get_thebe_info();
				if(MJCommons.isNullOrEmpty(server_description) || cInfo == null){
					s.writeS2("미지인");
				}else{
					s.writeS2(cInfo.to_name_pair());					
				}
			}
			else if(pc.is_shift_transfer())
				s.writeS2("서버이전중");
			else
				s.writeS2(name);
		}
		s.writeC(0x52); 		// title
		s.writeS2(getTitle(pc));
		s.writeC(0x58); 		// speed data
		s.writeB(pc.isHaste());
		s.writeC(0x60); 		// emotion
		if(pc.isBrave() || pc.isBlood_lust()) s.writeC(0x01);
		else if(pc.isElfBrave())	s.writeC(0x03);
		else if(pc.isFastMovable() || pc.isFruit() || pc.isUgdraFruit()) s.writeC(0x04);
		else						s.writeC(0x00);		
		s.writeC(0x68);			// drunken
		s.writeBit(0x00);
		s.writeC(0x70); 		// isghost
		s.writeB(pc.isGhost());
		s.writeC(0x78); 		// isparalyzed
		s.writeB(pc.isParalyzed());
		s.writeBit(0x80);		// isuser
		s.writeC(0x01);
		s.writeBit(0x88);		// isinvisible
		s.writeB(pc.isInvisble());
		s.writeBit(0x90); 		// ispoisoned
		s.writeB(pc.getPoison());
		s.writeBit(0x98);		// emblemid
		s.writeClanInfo(pc);
		//s.writeBit(0xA2); 	// pledgename
		//s.writeS2();
		s.writeBit(0xAA); 		// mastername
		s.writeC(0x00);
		s.writeBit(0xB0); 		// altitude
		s.writeC(0x00);
		s.writeBit(0xB8);		// hitratio
		s.writeBit(-1);
		s.writeBit(0xC0); 		// safelevel
		s.writeBit(pc.getLevel());
		s.writeBit(0xCA); 		// shop title
		if(pc.getShopChat() != null && pc.getShopChat().length > 0){
			s.writeC(pc.getShopChat().length);
			s.writeByte(pc.getShopChat());
		}else
			s.writeC(0x00);
		s.writeBit(0xD0); 		// weapon sprite
		s.writeBit(-1);
		s.writeBit(0xD8); 		// couplestate
		s.writeB(false);
		s.writeBit(0xE0); 		// boundarylevelindex
		s.writeBoundaryLevel(pc);
		s.writeBit(0xE8); 		// weakelemental
		s.writeBit(0x00);
		s.writeBit(0xF0);		// manaratio
		s.writeBit(-1);
		s.writeBit(0xF8);		// botindex
		s.writeBit(0x00);
		s.writeBit(0x100); 		// home server no
		s.writeC(0x00);
		s.writeBit(0x108); 		// team_id
		int mark = pc.get_mark_status();
		if(mark > 0 && !pc.is_combat_field()){
			pc.set_instance_status(MJEPcStatus.WORLD);
			mark = 0;
		} else {
			mark = writeTeamId(pc, isClan, mark);
		}
		s.writeBit(mark);
		s.writeBit(0x118); 		// speed_value_flag
		s.writeC(0x00);
		s.writeBit(0x120);		// second_speed_type
		s.writeC(0x00);
		if(pc.getClanRank() == L1Clan.군주){
			if(MJWar.isCastleOffenseClan(pc.getClan())){
				s.writeBit(0x130);
				s.writeB(true);
			}
		}
		
		s.writeH(0x00);
		return s;
	}
	
	public static S_WorldPutObject get(L1PcInstance pc){
		if(pc.isInvisble()){
			return get(pc, "", false);
		}
		
		if(pc.getAI() != null && (pc.getAI().getBotType() == MJBotType.REDKNIGHT || pc.getAI().getBotType() == MJBotType.PROTECTOR))
			return getRedKnight(pc);
		
		return get(pc, getDesc(pc), false);
	}
	
	public static S_WorldPutObject getinvis(L1PcInstance pc, boolean invis){
		if(invis)
			return get(pc, "", false);
		return get(pc, getDesc(pc), false);
	}
	
	public static S_WorldPutObject getinvis_party(L1PcInstance pc){
		
		if(pc.getAI() != null && (pc.getAI().getBotType() == MJBotType.REDKNIGHT || pc.getAI().getBotType() == MJBotType.PROTECTOR))
			return getRedKnight(pc);
		return get(pc, getDesc(pc), false);
	}
	
	public static S_WorldPutObject get(L1PcInstance pc, boolean isClan){
		if(pc.isInvisble()){
			return get(pc, "", false);
		}
		
		if(pc.getAI() != null && (pc.getAI().getBotType() == MJBotType.REDKNIGHT || pc.getAI().getBotType() == MJBotType.PROTECTOR))
			return getRedKnight(pc);
		return get(pc, getDesc(pc), isClan);
	}
	
	public static S_WorldPutObject getRedKnight(L1PcInstance pc){
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeC(0x08);			// point
		s.writePoint(pc.getX(), pc.getY());
		s.writeC(0x10);			// objectnumber
		s.writeBit(pc.getId());
		s.writeC(0x18); 		// objectsprite
		s.writeBit(pc.getCurrentSpriteId());
		s.writeC(0x20); 		// action
		if(pc.isDead())
			s.writeBit(ActionCodes.ACTION_Die);
		else
			s.writeBit(pc.getCurrentWeapon());
		s.writeC(0x28); 		// direction
		s.writeC(pc.getHeading());
		s.writeC(0x30); 		// lightRadius
		if(pc.getLight() != null)
			s.writeBit(pc.getLight().getOwnLightSize());
		else
			s.writeBit(0);
		s.writeC(0x38); 		// objectcount
		s.writeC(1);
		s.writeC(0x40); 		// alignment(lawful)
		s.writeC(0x00);
		s.writeC(0x4A);			// desc
		
		if(pc.getAI().getBotType() == MJBotType.PROTECTOR)
			s.writeS2(pc.getName());
		else
			s.writeS2("$16458");
		s.writeC(0x52); 		// title
		s.writeC(0x00);
		s.writeC(0x58); 		// speed data
		s.writeB(pc.isHaste());
		s.writeC(0x60); 		// emotion
		if(pc.isBrave() || pc.isBlood_lust()) s.writeC(0x01);
		else if(pc.isElfBrave())	s.writeC(0x03);
		else if(pc.isFastMovable() || pc.isFruit() || pc.isUgdraFruit()) s.writeC(0x04);
		else						s.writeC(0x00);		
		s.writeC(0x68);			// drunken
		s.writeC(0x00);
		s.writeC(0x70); 		// isghost
		s.writeB(pc.isGhost());
		s.writeC(0x78); 		// isparalyzed
		s.writeB(pc.isParalyzed());
		s.writeBit(0x80);		// isuser
		s.writeC(0x00);
		s.writeBit(0x88);		// isinvisible
		s.writeB(pc.isInvisble());
		s.writeBit(0x90); 		// ispoisoned
		s.writeB(pc.getPoison());
		s.writeBit(0x98);		// emblemid
		s.writeC(0x01);
		s.writeBit(0xA2); 		// pledgename
		s.writeS2("$16458");
		s.writeBit(0xAA); 		// mastername
		s.writeC(0x00);
		s.writeBit(0xB0); 		// altitude
		s.writeC(0x00);
		s.writeBit(0xB8);		// hitratio
		s.writeBit(-1);
		s.writeBit(0xC0); 		// safelevel
		s.writeBit(-1);
		s.writeBit(0xCA); 		// shop title
		s.writeC(0x00);
		s.writeBit(0xD0); 		// weapon sprite
		s.writeC(0x00);
		s.writeBit(0xD8); 		// couplestate
		s.writeB(false);
		s.writeBit(0xE0); 		// boundarylevelindex
		s.writeBit(pc.getLevel());
		s.writeBit(0xE8); 		// weakelemental
		s.writeBit(0x00);
		s.writeBit(0xF0);		// manaratio
		s.writeBit(-1);
		s.writeBit(0xF8);		// botindex
		s.writeC(0x00);
		s.writeBit(0x100); 		// home server no
		s.writeC(0x00);
		s.writeBit(0x108); 		// team_id
		s.writeC(13);			// 붉은기사단 마크13
		s.writeBit(0x118); 		// speed_value_flag
		s.writeC(0x00);
		s.writeBit(0x120);		// second_speed_type
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_WorldPutObject get(WorldPutBuilder builder){
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeC(0x08);			// point
		s.writePoint(builder.x, builder.y);
		s.writeC(0x10);			// objectnumber
		s.writeBit(builder.object_id);
		s.writeC(0x18); 			// objectsprite
		s.writeBit(builder.spr_id);
		s.writeC(0x20); 			// action
		s.writeBit(builder.action);
		s.writeC(0x28); 			// direction
		s.writeBit(builder.h);
		s.writeC(0x30); 			// lightRadius
		s.writeBit(builder.light_size);
		s.writeC(0x38); 			// objectcount
		s.writeC(1);
		s.writeC(0x40); 			// alignment(lawful)
		s.writeBit(builder.law_full);
		s.writeC(0x4A);			// desc
		s.writeS2(builder.name);
		s.writeC(0x52); 			// title
		s.writeS2(builder.title);
		s.writeC(0x58); 			// speed data
		s.writeBit(builder.move_speed);
		s.writeC(0x60); 			// emotion
		s.writeBit(builder.brave_speed);
		s.writeC(0x68);			// drunken
		s.writeBit(0x00);
		s.writeC(0x70); 			// isghost
		s.writeC(0);
		s.writeC(0x78); 			// isparalyzed
		s.writeB(builder.is_paralyzed);
		s.writeBit(0x80);		// isuser
		s.writeB(builder.is_user);
		s.writeBit(0x88);		// isinvisible
		s.writeB(builder.is_invisible);
		s.writeBit(0x90); 		// ispoisoned
		s.writeB(builder.is_poisoned);
		s.writeBit(0x98);		// emblemid
		s.writeBit(builder.emblem_id);
		s.writeBit(0xA2); 		// pledgename
		s.writeC(0x00);
		s.writeBit(0xAA); 		// mastername
		s.writeS2(builder.master_name);
		s.writeBit(0xB0); 		// altitude
		s.writeBit(builder.altitude);
		s.writeBit(0xB8);			// hitratio
		s.writeBit(builder.hitratio);
		s.writeBit(0xC0); 		// safelevel
		s.writeBit(builder.level);
		s.writeBit(0xD0); 		// weapon sprite
		s.writeC(builder.weapon_sprite);
		s.writeBit(0xD8); 		// couplestate
		s.writeB(builder.couplestate);
		s.writeBit(0xE0); 		// boundarylevelindex
		s.writeBit(builder.boundarylevelindex);
		s.writeBit(0xE8); 		// weakelemental
		s.writeBit(builder.weakelemental);
		s.writeBit(0xF0);			// manaratio
		s.writeBit(builder.mana_ratio);
		s.writeBit(0xF8);			// botindex
		s.writeBit(builder.botindex);
		s.writeBit(0x100); 		// home server no
		s.writeC(0x00);
		s.writeBit(0x108); 		// team_id
		s.writeBit(0x00);
		
		// 아직은 npc에서 미사용.
		s.writeBit(0x110); 		// dialog_radius
		s.writeC(0x00);
		s.writeBit(0x118); 		// speed_value_flag
		s.writeC(0x00);
		s.writeBit(0x120);		// second_speed_type
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_WorldPutObject get(L1NpcInstance npc){
		
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeC(0x08);			// point
		s.writePoint(npc.getX(), npc.getY());
		s.writeC(0x10);			// objectnumber
		s.writeBit(npc.getId());
		s.writeC(0x18); 			// objectsprite
		s.writeBit(npc.getCurrentSpriteId());
		s.writeC(0x20); 			// action
		s.writeAction(npc);
		s.writeC(0x28); 			// direction
		s.writeDirection(npc);
		s.writeC(0x30); 			// lightRadius
		s.writeBit(npc.getNpcTemplate().getLightSize());
		s.writeC(0x38); 			// objectcount
		s.writeC(1);
		s.writeC(0x40); 			// alignment(lawful)
		s.writeBit(npc.getTempLawful());
		s.writeC(0x4A);			// desc
		s.writeDesc(npc);
		s.writeC(0x52); 			// title
		s.writeTitle(npc);
		s.writeC(0x58); 			// speed data
		s.writeBit(npc.getMoveSpeed());
		s.writeC(0x60); 			// emotion
		s.writeBit(npc.getBraveSpeed());
		s.writeC(0x68);			// drunken
		s.writeBit(0x00);
		s.writeC(0x70); 			// isghost
		s.writeC(0);
		s.writeC(0x78); 			// isparalyzed
		s.writeB(npc.getParalysis());
		s.writeBit(0x80);		// isuser
		s.writeB(npc.getShopName() != null || !npc.isPassObject());
		s.writeBit(0x88);		// isinvisible
		s.writeB(npc.isInvisble());
		s.writeBit(0x90); 		// ispoisoned
		s.writeB(npc.getPoison());
		s.writeBit(0x98);		// emblemid
		s.writeEmblem(npc);
		s.writeBit(0xA2); 		// pledgename
		s.writeC(0x00);
		s.writeBit(0xAA); 		// mastername
		s.writeMaster(npc);
		s.writeBit(0xB0); 		// altitude
		s.writeBit(0x5);
		s.writeBit(0xB8);			// hitratio
		s.writeBit(-1);
		s.writeBit(0xC0); 		// safelevel
		s.writeBit(npc.getLevel());
		if(npc.getShopName() != null){
			s.writeBit(0xCA); 		// shop title
			s.writeS2(npc.getShopName());
		}
		s.writeBit(0xD0); 		// weapon sprite
		s.writeC(0x01);
		s.writeBit(0xD8); 		// couplestate
		s.writeB(false);
		s.writeBit(0xE0); 		// boundarylevelindex
		s.writeBit(0x00);
		s.writeBit(0xE8); 		// weakelemental
		s.writeBit(npc.getNpcTemplate().get_weakAttr());
		s.writeBit(0xF0);			// manaratio
		s.writeBit(-1);
		s.writeBit(0xF8);			// botindex
		s.writeBit(0x00);
		s.writeBit(0x100); 		// home server no
		s.writeC(0x00);
		s.writeBit(0x108); 		// team_id
		//s.writeBit(0x01);
		s.writeBit(MJNpcMarkTable.getInstance().get(npc.getNpcId()));
		
		// 아직은 npc에서 미사용.
		s.writeBit(0x110); 		// dialog_radius
		s.writeC(0x00);
		s.writeBit(0x118); 		// speed_value_flag
		s.writeC(0x00);
		s.writeBit(0x120);		// second_speed_type
		s.writeC(0x00);
		
		if(npc.getExplosionTime() != 0){
			s.writeBit(0x128);	// explosion_remain_time_ms
			s.writeBit(npc.getExplosionTime());
		}
		/*
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x09);
		bos.writeBit(1);
		bos.writeC(0x11);
		bos.writeBit(1);
		bos.writeC(0x18);
		bos.writeBit(1);
		s.writeBit(0x148);
		try{
			byte[] buff = bos.getBytes();
			s.writeBit(buff.length);
			s.writeBytes(buff);
			bos.close();
		}catch(Exception e){}
		*/
		s.writeH(0x00);
		return s;
	}
	
	public static class WorldPutBuilder{
		int object_id;
		int x;
		int y;
		int h;
		int action;
		int spr_id;
		int light_size;
		int law_full;
		String name;
		String title;
		int move_speed;
		int brave_speed;
		boolean is_paralyzed;
		boolean is_user;
		boolean is_invisible;
		boolean is_poisoned;
		int emblem_id;
		String pledge_name;
		String master_name;
		int altitude;
		int hitratio;
		int level;
		int weapon_sprite;
		boolean couplestate;
		int boundarylevelindex;
		int weakelemental;
		int mana_ratio;
		int botindex;
		
		public WorldPutBuilder(){
			object_id = -1;
			x = 0;
			y = 0;
			h = 0;
			action = 0;
			spr_id = 0;
			light_size = 2;
			law_full = 0;
			name = "";
			title = "";
			move_speed = 1;
			brave_speed = 1;
			is_paralyzed = false;
			is_user = false;
			is_invisible = false;
			is_poisoned = false;
			emblem_id = 0;
			pledge_name = "";
			master_name = "";
			altitude = 5;
			hitratio = -1;
			level = 1;
			weapon_sprite = 1;
			couplestate = false;
			boundarylevelindex = 0;
			weakelemental = 0;
			mana_ratio = -1;
			botindex = 0;
		}
		
		public WorldPutBuilder set_object_id(int obj_id){
			object_id = obj_id;
			return this;
		}
		public WorldPutBuilder set_object_id(){
			return set_object_id(IdFactory.getInstance().nextId());
		}
		public WorldPutBuilder set_x(int x){
			this.x = x;
			return this;
		}
		public WorldPutBuilder set_y(int y){
			this.y = y;
			return this;
		}
		public WorldPutBuilder set_h(int h){
			this.h = h;
			return this;
		}
		public WorldPutBuilder set_action(int action){
			this.action = action;
			return this;
		}
		public WorldPutBuilder set_sprid(int spr_id){
			this.spr_id = spr_id;
			return this;
		}
		public WorldPutBuilder set_right_size(int light_size){
			this.light_size = light_size;
			return this;
		}
		public WorldPutBuilder set_law_full(int law_full){
			this.law_full = law_full;
			return this;
		}
		public WorldPutBuilder set_name(String name){
			this.name = name;
			return this;
		}
		public WorldPutBuilder set_title(String title){
			this.title = title;
			return this;
		}
		public WorldPutBuilder set_move_speed(int speed){
			move_speed = speed;
			return this;
		}
		public WorldPutBuilder set_brave_speed(int speed){
			brave_speed = speed;
			return this;
		}
		public WorldPutBuilder set_is_paralyzed(boolean is_paralyzed){
			this.is_paralyzed = is_paralyzed;
			return this;
		}
		public WorldPutBuilder set_is_use(boolean is_user){
			this.is_user = is_user;
			return this;
		}
		public WorldPutBuilder set_is_invisible(boolean is_invisible){
			this.is_invisible = is_invisible;
			return this;
		}
		public WorldPutBuilder set_poisoned(boolean is_poisoned){
			this.is_poisoned = is_poisoned;
			return this;
		}
		public S_WorldPutObject build(){
			return S_WorldPutObject.get(this);
		}
	}
	
	//TODO SPR 엔피씨 끊김현상 발생시 추가
	private void writeAction(L1NpcInstance npc) {
		int npcid = npc.getNpcId();
		int spriteId = npc.getCurrentSpriteId();
		if (((npc.getShopName() != null))) {
			writeBit(70);
		} else if (((npc.getNpcTemplate().is_doppel()) && (spriteId != 31)) || (spriteId == 727) || (spriteId == 985)
				|| (spriteId == 986) || (spriteId == 6632) || (spriteId == 6634) || (spriteId == 6636)
				|| (spriteId == 6638))
			writeBit(4L);
		else if (spriteId == 110)
			writeBit(24L);
		else if (spriteId == 111)
			writeBit(4L);
		else if ((spriteId == 51) || (npcid == 60519))
			writeBit(24L);
		else if (spriteId == 816)
			writeBit(20L);
		else if (npcid == 203053) // 오전
			writeC(4);
		else if (npcid == 203052) // 오궁
			writeC(20);
		else {
			writeBit(npc.getStatus());
		}
	}
	
	private void writeDirection(L1NpcInstance npc){
		writeBit(npc.getHeading());
		/*if ((npc instanceof L1DoorInstance)) {
			L1DoorInstance door = (L1DoorInstance) npc;
			System.out.println(door.getId() + " " + door.getDirection());
			if (door.getDirection() == 0)	writeBit(0L);
			else							writeBit(0L);
		} else	writeBit(npc.getHeading());*/
	}
	
	private static String getTitle(L1PcInstance pc){
		return pc.hasSkillEffect(L1SkillId.USER_WANTED) ?
				L1PcInstance.WANTED_TITLE :
				pc.getTitle();
	}
	
	private static String getDesc(L1PcInstance pc){
		return pc.getName();
		/*
		return pc.hasSkillEffect(L1SkillId.USER_WANTED) ?
				String.format("%s\\aA[%d단계]\\fe 수배중", pc.getName(), pc.getHuntCount()) :
				pc.getName();*/
	}
	
	private void writeDesc(L1NpcInstance npc){
		
		if (npc.getNpcId() == 6200008) {
			try {
				String clanName = null;
				clanName = ((L1MerchantInstance) npc).getClanname();
				if (clanName.isEmpty() || clanName == null)
					writeC(0);
				else										
					writeS2(clanName);
			} catch (Exception e) {

			}
		} else if ((npc instanceof L1SignboardInstance)) 
			writeC(0);
		else if ((npc.getNameId() != null) && (!npc.getNameId().isEmpty())) {
			writeS2(npc.getNameId());
		}
		else 
			writeC(0);
	}
	
	private void writeTitle(L1NpcInstance npc){
		if ((npc instanceof L1FieldObjectInstance)) {
			L1NpcTalkData talkdata = NPCTalkDataTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
			if (talkdata != null) 	writeS2(talkdata.getNormalAction());
			 else					writeC(0);
		} else if ((npc instanceof L1SignboardInstance)) 
			writeS2(npc.getNameId());
		 else if ((npc.getTitle() != null) && (!npc.getTitle().isEmpty())) 
			writeS2(npc.getTitle());
		 else 
			writeC(0);
	}
	
	private void writeClanInfo(L1PcInstance pc){
		L1Clan clan = pc.getClan();
		if(clan != null)
			writeBit(clan.getEmblemId());
		else
			writeC(0x00);
		
		writeBit(0xA2);
		if(((pc.getMapId() >= 1005) && (pc.getMapId() <= 1070)) || (pc.getMapId() == 501) || (pc.getMapId() == 2009))
			writeS2(String.valueOf(pc.getMapId()));
		else
			writeS2(pc.getClanname());
	}
	
	private void writeEmblem(L1NpcInstance npc){
		if (npc.getNpcId() == 6200008) {
			int emblem = 0;
			for (L1Clan checkClan : L1World.getInstance().getAllClans()) {
				/** 1.켄트 2.오크 3.윈성 4.기란 5.하이네 6.드워프 7.아덴 8디아드 **/
				if (checkClan.getCastleId() == 4) {
					emblem = checkClan.getEmblemId();
					break;
				}
			}
			writeBit(emblem);
		} else {
			writeBit(0);
		}
	}
	
	private void writeMaster(L1NpcInstance npc){
		if (npc.getMaster() != null) 	writeS2(npc.getMaster().getName());
		else							writeS2("");
	}
	
	private void writeBoundaryLevel(L1PcInstance pc){
		writeBit(SpriteInformationLoader.levelToIndex(pc.getLevel(), pc.getCurrentSpriteId()));
	}
	
	private static int writeTeamId(L1PcInstance pc, boolean isClan, int mark){
		MJThebeCharacterInfo cInfo = pc.get_thebe_info();
		if(cInfo != null)
			return cInfo.team_info.team_id;
		
		int new_mark = mark;
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if(instobj != null){
			//new_mark = instobj.getMarkStatus(pc);
		} else if (IS_PRESENTATION_MARK && /*pc.getMapId() >= 1708 && pc.getMapId() <= 1710
				|| pc.getMapId() >= 12852 && pc.getMapId() <= 12862
				|| pc.getMapId() == 521	|| pc.getMapId() == 522 ||*/ pc.getMapId() == 99) {// 다른사람이 보여지는 기준
			if(isClan){
				new_mark = 30;
			}else{
				new_mark = 29;
			}
		}else {
			if(pc.getRedKnightClanId() != 0)//붉은기사단
				new_mark= 7;
			else 
				new_mark = 0;
		}
		
		return new_mark;
	}
	
	public static S_WorldPutObject get(byte[] b){
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeByte(b);
		return s;
	}
	
	private S_WorldPutObject(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_WORLD_PUT_OBJECT_NOTI);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_WorldPutObject";
	}
}
