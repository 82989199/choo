package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.datatables.MJNpcMarkTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1SignboardInstance;
import l1j.server.server.utils.MJCommons;

import java.io.IOException;

import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.L1Instance.MJEffectTriggerInstance;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_WORLD_PUT_OBJECT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static int get_presentation_npc_emblem(L1NpcInstance npc){
		if (npc.getNpcId() == 6200008) {
			for (L1Clan checkClan : L1World.getInstance().getAllClans()) {
				/** 1.켄트 2.오크 3.윈성 4.기란 5.하이네 6.드워프 7.아덴 8디아드 **/
				if (checkClan.getCastleId() == 4) {
					return checkClan.getEmblemId();
				}
			}
		}
		return 0;
	}
	
	private static String get_presentation_npc_title(L1NpcInstance npc){
		if((npc instanceof L1FieldObjectInstance)){
			L1NpcTalkData talkdata = NPCTalkDataTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
			return talkdata != null ? talkdata.getNormalAction() : "";
		}
		if ((npc instanceof L1SignboardInstance))
			return npc.getNameId();
		if((npc.getTitle() != null) && (!npc.getTitle().isEmpty())) 
			return npc.getTitle();
		return null;
	}
	
	private static String get_presentation_npc_desc(L1NpcInstance npc){
		if(npc.getNpcId() == 6200008){
			String clan_name = ((L1MerchantInstance) npc).getClanname();
			return MJCommons.isNullOrEmpty(clan_name) ? null : clan_name;
		}
		else if((npc instanceof L1SignboardInstance))
			return null;
		else if((npc.getNameId() != null) && (!npc.getNameId().isEmpty()))
			return npc.getNameId();
		return null;
	}
	
	private static int get_presentation_npc_action(L1NpcInstance npc){
		int npcid = npc.getNpcId();
		int sprid = npc.getCurrentSpriteId();
		if(npc.getShopName() != null)
			return EActionCodes.act_shop.toInt();
		
		if(sprid == 110)
			return 24;
		if(sprid == 111)
			return 4;
		if(sprid == 51 || npcid == 60519)
			return 24;
		if(sprid == 816)
			return 20;
		if(npcid == 203053)
			return 4;
		if(npcid == 203052)
			return 20;
		
		if(npc.getNpcTemplate().is_doppel()){
			if(sprid != 31 || sprid == 727 || sprid == 985
			|| sprid == 98 || sprid == 6632 || sprid == 6634 || sprid == 6636
			|| sprid == 6638)
			return 4;	
		}
		return npc.getStatus();
	}
	
	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(MJEffectTriggerInstance trigger){
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		noti.set_point(trigger.get_long_location_reverse());
		noti.set_objectnumber(trigger.getId());
		noti.set_objectsprite(trigger.get_sprite());
		noti.set_direction(1);
		noti.set_action(trigger.get_action());
		noti.set_lightRadius(0);
		noti.set_objectcount(0);
		noti.set_alignment(0);
		noti.set_desc("");
		noti.set_title("");
		noti.set_speeddata(1);
		noti.set_emotion(1);
		noti.set_drunken(0);
		noti.set_isghost(false);
		noti.set_isparalyzed(false);
		noti.set_isuser(false);
		noti.set_isinvisible(false);
		noti.set_ispoisoned(false);
		noti.set_emblemid(0);
		noti.set_pledgename("");
		noti.set_mastername("");
		noti.set_altitude(0);
		noti.set_hitratio(-1);
		noti.set_safelevel(1);
		noti.set_weaponsprite(-1);
		noti.set_couplestate(0);
		noti.set_boundarylevelindex(1);
		noti.set_speed_value_flag(1);
		noti.set_second_speed_type(1);
		return noti;
	}
	
	public static SC_WORLD_PUT_OBJECT_NOTI new_namechat_isntance(L1NpcInstance npc, String name, boolean is_ghost){
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		noti.set_point(npc.getLongLocationReverse());
		noti.set_objectnumber(npc.getId());
		noti.set_objectsprite(npc.getCurrentSpriteId());
		noti.set_action(npc.getStatus());
		noti.set_direction(npc.getHeading());
		noti.set_lightRadius(npc.getNpcTemplate().getLightSize());
		noti.set_objectcount(1);
		noti.set_alignment(npc.getTempLawful());
		noti.set_desc(name);
		noti.set_title("");
		noti.set_speeddata(1);
		noti.set_emotion(1);
		noti.set_drunken(0);
		noti.set_isghost(is_ghost);
		noti.set_isparalyzed(false);
		noti.set_isuser(false);
		noti.set_isinvisible(false);
		noti.set_ispoisoned(false);
		noti.set_emblemid(0);
		noti.set_pledgename(null);
		noti.set_mastername("");
		noti.set_altitude(5);
		noti.set_hitratio(-1);
		noti.set_safelevel(1);
		noti.set_weaponsprite(1);
		noti.set_couplestate(0);
		noti.set_boundarylevelindex(0);
		return noti;
	}
	
	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(MJDogFightInstance dogfight){
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		noti.set_point(dogfight.getLongLocationReverse());
		noti.set_objectnumber(dogfight.getId());
		noti.set_objectsprite(dogfight.getCurrentSpriteId());
		noti.set_action(dogfight.getStatus());
		noti.set_direction(dogfight.getHeading());
		noti.set_lightRadius(dogfight.getNpcTemplate().getLightSize());
		noti.set_objectcount(1);
		noti.set_alignment(dogfight.getTempLawful());
		noti.set_desc(dogfight.getName());
		noti.set_title("");
		noti.set_speeddata(1);
		noti.set_emotion(1);
		noti.set_drunken(0);
		noti.set_isghost(false);
		noti.set_isparalyzed(false);
		noti.set_isuser(false);
		noti.set_isinvisible(false);
		noti.set_ispoisoned(false);
		noti.set_emblemid(0);
		noti.set_pledgename(null);
		//noti.set_mastername(dogfight.get_corner_name());
		noti.set_mastername("");
		noti.set_altitude(5);
		noti.set_hitratio((int)dogfight.getCurrentHpPercent());
		noti.set_safelevel(dogfight.getLevel());
		noti.set_weaponsprite(1);
		noti.set_couplestate(0);
		noti.set_boundarylevelindex(0);
		if(dogfight.get_explosion_time_mills() > 0)
			noti.set_explosion_remain_time_ms(dogfight.get_explosion_time_mills());
		
		/*noti.set_npc_class_id(dogfight.get_class_info().get_class_id());
		Companion companion = Companion.newInstance();
		companion.set_attack_delay_reduce(dogfight.get_attackdelay_reduce());
		companion.set_move_delay_reduce(dogfight.get_movedelay_reduce());
		companion.set_pvp_dmg_ratio(0);
		noti.set_companion(companion);*/
		return noti;
	}
	
	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1NpcInstance npc){
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		boolean is_companion = npc instanceof MJCompanionInstance;
		MJCompanionInstance companion_instance = is_companion ? (MJCompanionInstance)npc : null;
		
		noti.set_point(npc.getLongLocationReverse());
		noti.set_objectnumber(npc.getId());
		noti.set_objectsprite(npc.getCurrentSpriteId());
		noti.set_action(get_presentation_npc_action(npc));
		noti.set_direction(npc.getHeading());
		noti.set_lightRadius(npc.getNpcTemplate().getLightSize());
		noti.set_objectcount(1);
		noti.set_alignment(npc.getTempLawful());
		noti.set_desc(is_companion ? companion_instance.getName() : get_presentation_npc_desc(npc));
		noti.set_title(get_presentation_npc_title(npc));
		noti.set_speeddata(npc.getMoveSpeed());
		noti.set_emotion(npc.getBraveSpeed());
		noti.set_drunken(0);
		noti.set_isghost(false);
		noti.set_isparalyzed(npc.getParalysis() != null);
		noti.set_isuser(npc.getShopName() != null || !npc.isPassObject());
		noti.set_isinvisible(npc.isInvisble());
		noti.set_ispoisoned(npc.getPoison() != null);
		noti.set_emblemid(get_presentation_npc_emblem(npc));
		noti.set_pledgename(null);
		if(!is_companion)
			noti.set_mastername(npc.getMaster() != null ? npc.getMaster().getName() : null);
		else
			noti.set_mastername(companion_instance.get_master_name());
		noti.set_altitude(5);
		//noti.set_hitratio(is_companion ? (int)npc.getCurrentHpPercent() : -1);
		noti.set_hitratio(-1);
		noti.set_safelevel(npc.getLevel());
		if(npc.getShopName() != null)
			noti.set_shoptitle(npc.getShopName());
		noti.set_weaponsprite(1);
		noti.set_couplestate(0);
		noti.set_boundarylevelindex(0);
		noti.set_weakelemental(npc.getNpcTemplate().get_weakAttr());
		noti.set_manaratio((int)npc.getCurrentMpPercent());
		noti.set_botindex(0);
		noti.set_team_id(MJNpcMarkTable.getInstance().get(npc.getNpcId()));
		noti.set_dialog_radius(0);
		noti.set_speed_value_flag(0);
		noti.set_second_speed_type(0);
		if(npc.getExplosionTime() != 0)
			noti.set_explosion_remain_time_ms((int)npc.getExplosionTime());
		if(is_companion){
			noti.set_npc_class_id(companion_instance.get_class_info().get_class_id());
			Companion companion = Companion.newInstance();
			companion.set_attack_delay_reduce(companion_instance.get_attackdelay_reduce());
			companion.set_move_delay_reduce(companion_instance.get_movedelay_reduce());
			companion.set_pvp_dmg_ratio(companion_instance.get_pvp_dmg_ratio());
			noti.set_companion(companion);
		}
		return noti;
	}
	
	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(){
		return new SC_WORLD_PUT_OBJECT_NOTI();
	}
	private int _point;
	private int _objectnumber;
	private int _objectsprite;
	private int _action;
	private int _direction;
	private int _lightRadius;
	private int _objectcount;
	private int _alignment;
	private String _desc;
	private String _title;
	private int _speeddata;
	private int _emotion;
	private int _drunken;
	private boolean _isghost;
	private boolean _isparalyzed;
	private boolean _isuser;
	private boolean _isinvisible;
	private boolean _ispoisoned;
	private int _emblemid;
	private String _pledgename;
	private String _mastername;
	private int _altitude;
	private int _hitratio;
	private int _safelevel;
	private String _shoptitle;
	private int _weaponsprite;
	private int _couplestate;
	private int _boundarylevelindex;
	private int _weakelemental;
	private int _manaratio;
	private int _botindex;
	private int _homeserverno;
	private int _team_id;
	private int _dialog_radius;
	private int _speed_value_flag;
	private int _second_speed_type;
	private int _explosion_remain_time_ms;
	private boolean _proclamation_siege_mark;
	private int _npc_class_id;
	private Companion _companion;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private SC_WORLD_PUT_OBJECT_NOTI(){
	}
	public int get_point(){
		return _point;
	}
	public void set_point(int val){
		_bit |= 0x1L;
		_point = val;
	}
	public boolean has_point(){
		return (_bit & 0x1L) == 0x1L;
	}
	public int get_objectnumber(){
		return _objectnumber;
	}
	public void set_objectnumber(int val){
		_bit |= 0x2L;
		_objectnumber = val;
	}
	public boolean has_objectnumber(){
		return (_bit & 0x2L) == 0x2L;
	}
	public int get_objectsprite(){
		return _objectsprite;
	}
	public void set_objectsprite(int val){
		_bit |= 0x4L;
		_objectsprite = val;
	}
	public boolean has_objectsprite(){
		return (_bit & 0x4L) == 0x4L;
	}
	public int get_action(){
		return _action;
	}
	public void set_action(int val){
		_bit |= 0x8L;
		_action = val;
	}
	public boolean has_action(){
		return (_bit & 0x8L) == 0x8L;
	}
	public int get_direction(){
		return _direction;
	}
	public void set_direction(int val){
		_bit |= 0x10L;
		_direction = val;
	}
	public boolean has_direction(){
		return (_bit & 0x10L) == 0x10L;
	}
	public int get_lightRadius(){
		return _lightRadius;
	}
	public void set_lightRadius(int val){
		_bit |= 0x20L;
		_lightRadius = val;
	}
	public boolean has_lightRadius(){
		return (_bit & 0x20L) == 0x20L;
	}
	public int get_objectcount(){
		return _objectcount;
	}
	public void set_objectcount(int val){
		_bit |= 0x40L;
		_objectcount = val;
	}
	public boolean has_objectcount(){
		return (_bit & 0x40L) == 0x40L;
	}
	public int get_alignment(){
		return _alignment;
	}
	public void set_alignment(int val){
		_bit |= 0x80L;
		_alignment = val;
	}
	public boolean has_alignment(){
		return (_bit & 0x80L) == 0x80L;
	}
	public String get_desc(){
		return _desc;
	}
	public void set_desc(String val){
		_bit |= 0x100L;
		_desc = val;
	}
	public boolean has_desc(){
		return (_bit & 0x100L) == 0x100L;
	}
	public String get_title(){
		return _title;
	}
	public void set_title(String val){
		_bit |= 0x200L;
		_title = val;
	}
	public boolean has_title(){
		return (_bit & 0x200L) == 0x200L;
	}
	public int get_speeddata(){
		return _speeddata;
	}
	public void set_speeddata(int val){
		_bit |= 0x400L;
		_speeddata = val;
	}
	public boolean has_speeddata(){
		return (_bit & 0x400L) == 0x400L;
	}
	public int get_emotion(){
		return _emotion;
	}
	public void set_emotion(int val){
		_bit |= 0x800L;
		_emotion = val;
	}
	public boolean has_emotion(){
		return (_bit & 0x800L) == 0x800L;
	}
	public int get_drunken(){
		return _drunken;
	}
	public void set_drunken(int val){
		_bit |= 0x1000L;
		_drunken = val;
	}
	public boolean has_drunken(){
		return (_bit & 0x1000L) == 0x1000L;
	}
	public boolean get_isghost(){
		return _isghost;
	}
	public void set_isghost(boolean val){
		_bit |= 0x2000L;
		_isghost = val;
	}
	public boolean has_isghost(){
		return (_bit & 0x2000L) == 0x2000L;
	}
	public boolean get_isparalyzed(){
		return _isparalyzed;
	}
	public void set_isparalyzed(boolean val){
		_bit |= 0x4000L;
		_isparalyzed = val;
	}
	public boolean has_isparalyzed(){
		return (_bit & 0x4000L) == 0x4000L;
	}
	public boolean get_isuser(){
		return _isuser;
	}
	public void set_isuser(boolean val){
		_bit |= 0x8000L;
		_isuser = val;
	}
	public boolean has_isuser(){
		return (_bit & 0x8000L) == 0x8000L;
	}
	public boolean get_isinvisible(){
		return _isinvisible;
	}
	public void set_isinvisible(boolean val){
		_bit |= 0x10000L;
		_isinvisible = val;
	}
	public boolean has_isinvisible(){
		return (_bit & 0x10000L) == 0x10000L;
	}
	public boolean get_ispoisoned(){
		return _ispoisoned;
	}
	public void set_ispoisoned(boolean val){
		_bit |= 0x20000L;
		_ispoisoned = val;
	}
	public boolean has_ispoisoned(){
		return (_bit & 0x20000L) == 0x20000L;
	}
	public int get_emblemid(){
		return _emblemid;
	}
	public void set_emblemid(int val){
		_bit |= 0x40000L;
		_emblemid = val;
	}
	public boolean has_emblemid(){
		return (_bit & 0x40000L) == 0x40000L;
	}
	public String get_pledgename(){
		return _pledgename;
	}
	public void set_pledgename(String val){
		_bit |= 0x80000L;
		_pledgename = val;
	}
	public boolean has_pledgename(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public String get_mastername(){
		return _mastername;
	}
	public void set_mastername(String val){
		_bit |= 0x100000L;
		_mastername = val;
	}
	public boolean has_mastername(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public int get_altitude(){
		return _altitude;
	}
	public void set_altitude(int val){
		_bit |= 0x200000L;
		_altitude = val;
	}
	public boolean has_altitude(){
		return (_bit & 0x200000L) == 0x200000L;
	}
	public int get_hitratio(){
		return _hitratio;
	}
	public void set_hitratio(int val){
		_bit |= 0x400000L;
		_hitratio = val;
	}
	public boolean has_hitratio(){
		return (_bit & 0x400000L) == 0x400000L;
	}
	public int get_safelevel(){
		return _safelevel;
	}
	public void set_safelevel(int val){
		_bit |= 0x800000L;
		_safelevel = val;
	}
	public boolean has_safelevel(){
		return (_bit & 0x800000L) == 0x800000L;
	}
	public String get_shoptitle(){
		return _shoptitle;
	}
	public void set_shoptitle(String val){
		_bit |= 0x1000000L;
		_shoptitle = val;
	}
	public boolean has_shoptitle(){
		return (_bit & 0x1000000L) == 0x1000000L;
	}
	public int get_weaponsprite(){
		return _weaponsprite;
	}
	public void set_weaponsprite(int val){
		_bit |= 0x2000000L;
		_weaponsprite = val;
	}
	public boolean has_weaponsprite(){
		return (_bit & 0x2000000L) == 0x2000000L;
	}
	public int get_couplestate(){
		return _couplestate;
	}
	public void set_couplestate(int val){
		_bit |= 0x4000000L;
		_couplestate = val;
	}
	public boolean has_couplestate(){
		return (_bit & 0x4000000L) == 0x4000000L;
	}
	public int get_boundarylevelindex(){
		return _boundarylevelindex;
	}
	public void set_boundarylevelindex(int val){
		_bit |= 0x8000000L;
		_boundarylevelindex = val;
	}
	public boolean has_boundarylevelindex(){
		return (_bit & 0x8000000L) == 0x8000000L;
	}
	public int get_weakelemental(){
		return _weakelemental;
	}
	public void set_weakelemental(int val){
		_bit |= 0x10000000L;
		_weakelemental = val;
	}
	public boolean has_weakelemental(){
		return (_bit & 0x10000000L) == 0x10000000L;
	}
	public int get_manaratio(){
		return _manaratio;
	}
	public void set_manaratio(int val){
		_bit |= 0x20000000L;
		_manaratio = val;
	}
	public boolean has_manaratio(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public int get_botindex(){
		return _botindex;
	}
	public void set_botindex(int val){
		_bit |= 0x40000000L;
		_botindex = val;
	}
	public boolean has_botindex(){
		return (_bit & 0x40000000L) == 0x40000000L;
	}
	public int get_homeserverno(){
		return _homeserverno;
	}
	public void set_homeserverno(int val){
		_bit |= 0x80000000L;
		_homeserverno = val;
	}
	public boolean has_homeserverno(){
		return (_bit & 0x80000000L) == 0x80000000L;
	}
	public int get_team_id(){
		return _team_id;
	}
	public void set_team_id(int val){
		_bit |= 0x100000000L;
		_team_id = val;
	}
	public boolean has_team_id(){
		return (_bit & 0x100000000L) == 0x100000000L;
	}
	public int get_dialog_radius(){
		return _dialog_radius;
	}
	public void set_dialog_radius(int val){
		_bit |= 0x200000000L;
		_dialog_radius = val;
	}
	public boolean has_dialog_radius(){
		return (_bit & 0x200000000L) == 0x200000000L;
	}
	public int get_speed_value_flag(){
		return _speed_value_flag;
	}
	public void set_speed_value_flag(int val){
		_bit |= 0x400000000L;
		_speed_value_flag = val;
	}
	public boolean has_speed_value_flag(){
		return (_bit & 0x400000000L) == 0x400000000L;
	}
	public int get_second_speed_type(){
		return _second_speed_type;
	}
	public void set_second_speed_type(int val){
		_bit |= 0x800000000L;
		_second_speed_type = val;
	}
	public boolean has_second_speed_type(){
		return (_bit & 0x800000000L) == 0x800000000L;
	}
	public int get_explosion_remain_time_ms(){
		return _explosion_remain_time_ms;
	}
	public void set_explosion_remain_time_ms(int val){
		_bit |= 0x1000000000L;
		_explosion_remain_time_ms = val;
	}
	public boolean has_explosion_remain_time_ms(){
		return (_bit & 0x1000000000L) == 0x1000000000L;
	}
	public boolean get_proclamation_siege_mark(){
		return _proclamation_siege_mark;
	}
	public void set_proclamation_siege_mark(boolean val){
		_bit |= 0x2000000000L;
		_proclamation_siege_mark = val;
	}
	public boolean has_proclamation_siege_mark(){
		return (_bit & 0x2000000000L) == 0x2000000000L;
	}
	public int get_npc_class_id(){
		return _npc_class_id;
	}
	public void set_npc_class_id(int val){
		_bit |= 0x4000000000L;
		_npc_class_id = val;
	}
	public boolean has_npc_class_id(){
		return (_bit & 0x4000000000L) == 0x4000000000L;
	}
	public Companion get_companion(){
		return _companion;
	}
	public void set_companion(Companion val){
		_bit |= 0x8000000000L;
		_companion = val;
	}
	public boolean has_companion(){
		return (_bit & 0x8000000000L) == 0x8000000000L;
	}
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_point())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _point);
		if (has_objectnumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _objectnumber);
		if (has_objectsprite())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _objectsprite);
		if (has_action())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _action);
		if (has_direction())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _direction);
		if (has_lightRadius())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _lightRadius);
		if (has_objectcount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _objectcount);
		if (has_alignment())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _alignment);
		if (has_desc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(9, _desc);
		if (has_title())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(10, _title);
		if (has_speeddata())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _speeddata);
		if (has_emotion())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _emotion);
		if (has_drunken())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _drunken);
		if (has_isghost())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(14, _isghost);
		if (has_isparalyzed())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(15, _isparalyzed);
		if (has_isuser())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(16, _isuser);
		if (has_isinvisible())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(17, _isinvisible);
		if (has_ispoisoned())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(18, _ispoisoned);
		if (has_emblemid())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(19, _emblemid);
		if (has_pledgename())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(20, _pledgename);
		if (has_mastername())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(21, _mastername);
		if (has_altitude())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(22, _altitude);
		if (has_hitratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(23, _hitratio);
		if (has_safelevel())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(24, _safelevel);
		if (has_shoptitle())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(25, _shoptitle);
		if (has_weaponsprite())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(26, _weaponsprite);
		if (has_couplestate())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(27, _couplestate);
		if (has_boundarylevelindex())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(28, _boundarylevelindex);
		if (has_weakelemental())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(29, _weakelemental);
		if (has_manaratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(30, _manaratio);
		if (has_botindex())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(31, _botindex);
		if (has_homeserverno())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(32, _homeserverno);
		if (has_team_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(33, _team_id);
		if (has_dialog_radius())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(34, _dialog_radius);
		if (has_speed_value_flag())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(35, _speed_value_flag);
		if (has_second_speed_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(36, _second_speed_type);
		if (has_explosion_remain_time_ms())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(37, _explosion_remain_time_ms);
		if (has_proclamation_siege_mark())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(38, _proclamation_siege_mark);
		if (has_npc_class_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(39, _npc_class_id);
		if (has_companion())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(40, _companion);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_point()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectnumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectsprite()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_direction()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_lightRadius()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectcount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_alignment()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_title()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_speeddata()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emotion()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_drunken()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isghost()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isparalyzed()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isuser()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isinvisible()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_ispoisoned()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emblemid()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pledgename()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_mastername()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_altitude()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_hitratio()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_safelevel()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weaponsprite()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_couplestate()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_boundarylevelindex()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_point()){
			output.writeUInt32(1, _point);
		}
		if (has_objectnumber()){
			output.writeUInt32(2, _objectnumber);
		}
		if (has_objectsprite()){
			output.wirteInt32(3, _objectsprite);
		}
		if (has_action()){
			output.wirteInt32(4, _action);
		}
		if (has_direction()){
			output.wirteInt32(5, _direction);
		}
		if (has_lightRadius()){
			output.wirteInt32(6, _lightRadius);
		}
		if (has_objectcount()){
			output.wirteInt32(7, _objectcount);
		}
		if (has_alignment()){
			output.wirteInt32(8, _alignment);
		}
		if (has_desc()){
			output.writeString(9, _desc);
		}
		if (has_title()){
			output.writeString(10, _title);
		}
		if (has_speeddata()){
			output.wirteInt32(11, _speeddata);
		}
		if (has_emotion()){
			output.wirteInt32(12, _emotion);
		}
		if (has_drunken()){
			output.wirteInt32(13, _drunken);
		}
		if (has_isghost()){
			output.writeBool(14, _isghost);
		}
		if (has_isparalyzed()){
			output.writeBool(15, _isparalyzed);
		}
		if (has_isuser()){
			output.writeBool(16, _isuser);
		}
		if (has_isinvisible()){
			output.writeBool(17, _isinvisible);
		}
		if (has_ispoisoned()){
			output.writeBool(18, _ispoisoned);
		}
		if (has_emblemid()){
			output.writeUInt32(19, _emblemid);
		}
		if (has_pledgename()){
			output.writeString(20, _pledgename);
		}
		if (has_mastername()){
			output.writeString(21, _mastername);
		}
		if (has_altitude()){
			output.wirteInt32(22, _altitude);
		}
		if (has_hitratio()){
			output.wirteInt32(23, _hitratio);
		}
		if (has_safelevel()){
			output.wirteInt32(24, _safelevel);
		}
		if (has_shoptitle()){
			output.writeString(25, _shoptitle);
		}
		if (has_weaponsprite()){
			output.wirteInt32(26, _weaponsprite);
		}
		if (has_couplestate()){
			output.wirteInt32(27, _couplestate);
		}
		if (has_boundarylevelindex()){
			output.wirteInt32(28, _boundarylevelindex);
		}
		if (has_weakelemental()){
			output.wirteInt32(29, _weakelemental);
		}
		if (has_manaratio()){
			output.wirteInt32(30, _manaratio);
		}
		if (has_botindex()){
			output.writeUInt32(31, _botindex);
		}
		if (has_homeserverno()){
			output.wirteInt32(32, _homeserverno);
		}
		if (has_team_id()){
			output.writeUInt32(33, _team_id);
		}
		if (has_dialog_radius()){
			output.writeUInt32(34, _dialog_radius);
		}
		if (has_speed_value_flag()){
			output.wirteInt32(35, _speed_value_flag);
		}
		if (has_second_speed_type()){
			output.wirteInt32(36, _second_speed_type);
		}
		if (has_explosion_remain_time_ms()){
			output.writeUInt32(37, _explosion_remain_time_ms);
		}
		if (has_proclamation_siege_mark()){
			output.writeBool(38, _proclamation_siege_mark);
		}
		if (has_npc_class_id()){
			output.wirteInt32(39, _npc_class_id);
		}
		if (has_companion()){
			output.writeMessage(40, _companion);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_point(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_objectnumber(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_objectsprite(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_action(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_direction(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_lightRadius(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_objectcount(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_alignment(input.readInt32());
					break;
				}
				case 0x0000004A:{
					set_desc(input.readString());
					break;
				}
				case 0x00000052:{
					set_title(input.readString());
					break;
				}
				case 0x00000058:{
					set_speeddata(input.readInt32());
					break;
				}
				case 0x00000060:{
					set_emotion(input.readInt32());
					break;
				}
				case 0x00000068:{
					set_drunken(input.readInt32());
					break;
				}
				case 0x00000070:{
					set_isghost(input.readBool());
					break;
				}
				case 0x00000078:{
					set_isparalyzed(input.readBool());
					break;
				}
				case 0x00000080:{
					set_isuser(input.readBool());
					break;
				}
				case 0x00000088:{
					set_isinvisible(input.readBool());
					break;
				}
				case 0x00000090:{
					set_ispoisoned(input.readBool());
					break;
				}
				case 0x00000098:{
					set_emblemid(input.readUInt32());
					break;
				}
				case 0x000000A2:{
					set_pledgename(input.readString());
					break;
				}
				case 0x000000AA:{
					set_mastername(input.readString());
					break;
				}
				case 0x000000B0:{
					set_altitude(input.readInt32());
					break;
				}
				case 0x000000B8:{
					set_hitratio(input.readInt32());
					break;
				}
				case 0x000000C0:{
					set_safelevel(input.readInt32());
					break;
				}
				case 0x000000CA:{
					set_shoptitle(input.readString());
					break;
				}
				case 0x000000D0:{
					set_weaponsprite(input.readInt32());
					break;
				}
				case 0x000000D8:{
					set_couplestate(input.readInt32());
					break;
				}
				case 0x000000E0:{
					set_boundarylevelindex(input.readInt32());
					break;
				}
				case 0x000000E8:{
					set_weakelemental(input.readInt32());
					break;
				}
				case 0x000000F0:{
					set_manaratio(input.readInt32());
					break;
				}
				case 0x000000F8:{
					set_botindex(input.readUInt32());
					break;
				}
				case 0x00000100:{
					set_homeserverno(input.readInt32());
					break;
				}
				case 0x00000108:{
					set_team_id(input.readUInt32());
					break;
				}
				case 0x00000110:{
					set_dialog_radius(input.readUInt32());
					break;
				}
				case 0x00000118:{
					set_speed_value_flag(input.readInt32());
					break;
				}
				case 0x00000120:{
					set_second_speed_type(input.readInt32());
					break;
				}
				case 0x00000128:{
					set_explosion_remain_time_ms(input.readUInt32());
					break;
				}
				case 0x00000130:{
					set_proclamation_siege_mark(input.readBool());
					break;
				}
				case 0x00000138:{
					set_npc_class_id(input.readInt32());
					break;
				}
				case 0x00000142:{
					set_companion((Companion)input.readMessage(Companion.newInstance()));
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_WORLD_PUT_OBJECT_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_companion() && _companion != null){
			_companion.dispose();
			_companion = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Companion implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Companion newInstance(){
			return new Companion();
		}
		private double _move_delay_reduce;
		private double _attack_delay_reduce;
		private int _pvp_dmg_ratio;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Companion(){
		}
		public double get_move_delay_reduce(){
			return _move_delay_reduce;
		}
		public void set_move_delay_reduce(double val){
			_bit |= 0x1;
			_move_delay_reduce = val;
		}
		public boolean has_move_delay_reduce(){
			return (_bit & 0x1) == 0x1;
		}
		public double get_attack_delay_reduce(){
			return _attack_delay_reduce;
		}
		public void set_attack_delay_reduce(double val){
			_bit |= 0x2;
			_attack_delay_reduce = val;
		}
		public boolean has_attack_delay_reduce(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_pvp_dmg_ratio(){
			return _pvp_dmg_ratio;
		}
		public void set_pvp_dmg_ratio(int val){
			_bit |= 0x4;
			_pvp_dmg_ratio = val;
		}
		public boolean has_pvp_dmg_ratio(){
			return (_bit & 0x4) == 0x4;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_move_delay_reduce())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(1, _move_delay_reduce);
			if (has_attack_delay_reduce())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(2, _attack_delay_reduce);
			if (has_pvp_dmg_ratio())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _pvp_dmg_ratio);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_move_delay_reduce()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_attack_delay_reduce()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_pvp_dmg_ratio()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_move_delay_reduce()){
				output.writeDouble(1, _move_delay_reduce);
			}
			if (has_attack_delay_reduce()){
				output.writeDouble(2, _attack_delay_reduce);
			}
			if (has_pvp_dmg_ratio()){
				output.writeUInt32(3, _pvp_dmg_ratio);
			}
		}
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try{
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					default:{
						return this;
					}
					case 0x00000009:{
						set_move_delay_reduce(input.readDouble());
						break;
					}
					case 0x00000011:{
						set_attack_delay_reduce(input.readDouble());
						break;
					}
					case 0x00000018:{
						set_pvp_dmg_ratio(input.readUInt32());
						break;
					}
				}
			}
			return this;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch(Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public MJIProtoMessage copyInstance(){
			return new Companion();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
