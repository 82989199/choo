package l1j.server.MJEffectSystem;

import java.util.Random;

import l1j.server.MJEffectSystem.Realize.OnChaseThread;
import l1j.server.MJEffectSystem.Realize.OnEffectThread;
import l1j.server.MJEffectSystem.Realize.OnRadiationThread;
import l1j.server.MJEffectSystem.Realize.OnRotationThread;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;

public class MJEffectModel{
	protected static Random _rnd = new Random(System.nanoTime());
	private static final int ET_RANDOM 		= 0;
	private static final int ET_TIGHTEN 	= 1;
	private static final int ET_ROTATE		= 2;
	private static final int ET_RADIATION	= 3;
	private static final int ET_CHASE		= 4;
	private static final int[] ETS_RANDOM = new int[]{
		ET_TIGHTEN, ET_ROTATE, ET_RADIATION, ET_CHASE
	};
	private int				_type;
	private int 			_effectId;		// 이펙트 id
	private int				_actId;
	private int 			_impact;		// 대미지가 미칠 반경
	private int 			_range;			// 범위
	private int 			_dmgMin;		// 최소 대미지
	private int				_dmgMax;		// 최대 대미지
	private int				_delay;			// delay
	private int				_zpos;			// 좁혀 들어오는 크기
	private int				_ratio;			// 확률
	
	public MJEffectModel(){
		
	}
	
	public void setType(String s){
		if(s.equalsIgnoreCase("Random"))
			_type = ET_RANDOM;
		else if(s.equalsIgnoreCase("Tighten"))
			_type = ET_TIGHTEN;
		else if(s.equalsIgnoreCase("Rotate"))
			_type = ET_ROTATE;
		else if(s.equalsIgnoreCase("Radiation"))
			_type = ET_RADIATION;
		else if(s.equalsIgnoreCase("Chase"))
			_type = ET_CHASE;
	}
	
	public int getEffectId(){
		return _effectId;
	}
	
	public void setEffectId(int i){
		_effectId = i;
	}
	
	public int getActionId(){
		return _actId;
	}
	
	public void setActionId(int i){
		_actId = i;
	}
	
	public int getImpact(){
		return _impact;
	}
	
	public void setImpact(int i){
		_impact = i;
	}
	
	public int getRange(){
		return _range;
	}
	
	public void setRange(int i){
		_range = i;
	}
	
	public int getDamageMin(){
		return _dmgMin;
	}
	
	public void setDamageMin(int i){
		_dmgMin = i;
	}
	
	public int getDamageMax(){
		return _dmgMax;
	}
	
	public void setDamageMax(int i){
		_dmgMax = i;
	}
	
	public int getDelay(){
		return _delay;
	}
	
	public void setDelay(int i){
		_delay = i;
	}
	
	public int getZPos(){
		return _zpos;
	}
	
	public void setZPos(int i){
		_zpos = i;
	}
	
	public void setRatio(int i){
		_ratio = i;
	}
	
	public int getRatio(){
		return _ratio;
	}
	
	public boolean isEffectSet(){
		return _rnd.nextInt(100) + 1 <= _ratio;
	}
	
	public void runEffect(L1NpcInstance npc){
		int tmpType = _type;
		if(_type == ET_RANDOM)
			tmpType = ETS_RANDOM[_rnd.nextInt(ETS_RANDOM.length)];
		
		switch(tmpType){
		case ET_TIGHTEN:
			GeneralThreadPool.getInstance().execute(new OnEffectThread(this, npc));
			break;
		case ET_ROTATE:
			GeneralThreadPool.getInstance().execute(new OnRotationThread(this, npc));
			break;
		case ET_RADIATION:
			GeneralThreadPool.getInstance().execute(new OnRadiationThread(this, npc));
			break;
		case ET_CHASE:
			GeneralThreadPool.getInstance().execute(new OnChaseThread(this, npc));
			break;
		}
	}
}
