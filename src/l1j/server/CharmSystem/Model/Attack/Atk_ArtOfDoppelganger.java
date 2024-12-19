package l1j.server.CharmSystem.Model.Attack;

import java.util.ArrayList;

import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.S_WorldPutObject.WorldPutBuilder;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJCommons;

public class Atk_ArtOfDoppelganger extends CharmSkillModel{
	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(!(attacker instanceof L1PcInstance))
			return 0D;
		
		if(MJCommons.isUnbeatable(defender) || !isPlay(attacker, t_item))
			return 0D;
		
		int pure = getProbability(attacker, t_item);
		if(!isInPercent(pure))
			return 0D;
		
		long cur_time = System.currentTimeMillis();
		if(cur_time < attacker.getDoppelTime())
			return 0D;
		//TODO 分身系统
		L1PcInstance attacker_pc = (L1PcInstance)attacker;
		int min_attack_count = 2;//// 最小打击次数
		int max_attack_count = 4 + t_item.getEnchantLevel();//最大打击次数
		int ox = attacker.getX();
		int oy = attacker.getY();
		int tx = defender.getX();
		int ty = defender.getY();
		int oh = attacker.getHeading();
		int o_spr = attacker.getCurrentSpriteId();
		int o_act = attacker_pc.getCurrentWeapon() + 1;
		if(o_spr == 11685 || o_spr == 12015)
			o_act = 1;
		
		int attack_count = Math.max(_rnd.nextInt((max_attack_count + 1)), min_attack_count);
		L1Item item = attacker_pc.getWeapon() == null ? null : attacker_pc.getWeapon().getItem();
		int t1 = item == null ? 4 : item.getType1();
		boolean is_long = t1 == 20 || t1 == 2922;
		long interval = attacker.getCurrentSpriteInterval(EActionCodes.fromInt(o_act));
		long d_time = (attack_count * interval) + 500;
		attacker.setDoppelTime(cur_time + d_time);
		ArrayList<ChaserItem> items = new ArrayList<ChaserItem>(3);
		if(!is_long){
			items.add(new ChaserItem(tx - 1, ty, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
			items.add(new ChaserItem(tx, ty + 1, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
			items.add(new ChaserItem(tx + 1, ty - 1, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
		}else{
			interval += 100;
			items.add(new ChaserItem(ox - 1, oy, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
			items.add(new ChaserItem(ox, oy + 1, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
			items.add(new ChaserItem(ox + 1, oy - 1, oh, o_spr, o_act, eff_id, defender.getId(), defender.getHeading(), is_long));
		}
		ChaserWork work = new ChaserWork(ox, oy, items, attack_count, interval, defender);
		work.work();
		
		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(attacker.getId(), eff_id));
		return (_rnd.nextInt(max_val + 1) + min_val) * attack_count;
	}
	
	class ChaserWork implements Runnable{
		int ox;
		int oy;
		ArrayList<ChaserItem> items;
		int attack_count;
		long interval;
		L1Character target;
		int target_x;
		int target_y;
		int target_map_id;
		ChaserWork(
				int ox,
				int oy,
				ArrayList<ChaserItem> items,
				int attack_count,
				long internal,
				L1Character target
				){
			this.ox = ox;
			this.oy = oy;
			this.items = items;
			this.attack_count = attack_count;
			this.interval = internal;
			this.target = target;
			target_x = target.getX();
			target_y = target.getY();
			target_map_id = target.getMapId();
		}
		
		void work(){
			GeneralThreadPool.getInstance().execute(this);
		}

		@Override
		public void run(){
			ArrayList<L1Object> receivers = L1World.getInstance().getVisiblePoint(new L1Location(target_x, target_y, target_map_id), 15);
			try{
				for(ChaserItem cItem : items)
					cItem.show(receivers);
				
				for(int i=attack_count - 1; i>=0; --i){	
					Thread.sleep(interval);
					if(target == null || target.isDead() || target_map_id != target.getMapId() || !MJCommons.isDistance(target.getX(), target.getY(), target_map_id, target_x, target_y, target_map_id, 3))
						return;
					
					for(ChaserItem cItem : items)
						cItem.send_action(receivers, target_x, target_y);
					Thread.sleep(100);
				}				
			}catch(Exception e){
				return;
			}finally{
				for(ChaserItem cItem : items)
					cItem.remove(receivers);
			}
		}
	}
	
	class ChaserItem{
		int object_id;
		int x;
		int y;
		int heading;
		int sprite_id;
		int action_id;
		int eff_id;
		int target_id;
		int target_heading;
		boolean is_long;
		ChaserItem(
		int x,
		int y,
		int heading,
		int sprite_id,
		int action_id,
		int eff_id,
		int target_id,
		int target_heading,
		boolean is_long){
			this.object_id = IdFactory.getInstance().nextId();
			this.x = x;
			this.y = y;
			this.heading = heading;
			this.sprite_id = sprite_id;
			this.action_id = action_id;
			this.eff_id = eff_id;
			this.target_id = target_id;
			this.target_heading = target_heading;
			this.is_long = is_long;
		}
		
		void show(ArrayList<L1Object> receivers){
			S_WorldPutObject s = new WorldPutBuilder()
					.set_object_id(object_id)
					.set_x(x)
					.set_y(y)
					.set_h(heading)
					.set_action(action_id - 1)
					.set_sprid(sprite_id)
					.build();
			S_SkillSound sound = new S_SkillSound(object_id, eff_id);
			for(L1Object o : receivers){
				if(o == null || !(o instanceof L1PcInstance))
					continue;
				L1PcInstance pc = (L1PcInstance)o;
				pc.sendPackets(sound, false);
				pc.sendPackets(s, false);
			}
			sound.clear();
			s.clear();
		}
		
		void send_action(ArrayList<L1Object> receivers, int tx, int ty){
			ServerBasePacket s = null;
			if(!is_long)
				s = new S_AttackStatus(object_id, target_heading, target_id, action_id);
			else{
				int act_id = action_id;
				if (sprite_id == 3860 || sprite_id == 15053) {
					act_id = 21;
				}
				s = new S_UseArrowSkill(object_id, x, y, heading, act_id, target_id, 8916, x, y, true);
			}
			for(L1Object o : receivers){
				if(o == null || !(o instanceof L1PcInstance))
					continue;
				L1PcInstance pc = (L1PcInstance)o;
				pc.sendPackets(s, false);
			}
			s.clear();
		}
		
		void remove(ArrayList<L1Object> receivers){
			S_RemoveObject s = new S_RemoveObject(object_id);
			for(L1Object o : receivers){
				if(o == null || !(o instanceof L1PcInstance))
					continue;
				L1PcInstance pc = (L1PcInstance)o;
				pc.sendPackets(s, false);
			}
			s.clear();
		}
	}
	
}
