package l1j.server.server.templates;

import l1j.server.server.utils.SystemUtil;

public class L1Boss {
	private int npcid;
	private String mapname;
	private int x;
	private int y;
	private int map;
	private int rnd;
	private int[][] time;
	private String monname;
	private String[] yoil;
	private boolean _ment;
	private boolean _yn;
	
	private String message;
	private String ynmessage;
	
	public int getNpcId() {
		return npcid;
	}
	public void setNpcId(int id) {
		this.npcid = id;
	}
	public String getMapName() {
		return mapname;
	}
	public void setMapName(String name) {
		this.mapname = name;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int mapid) {
		this.map = mapid;
	}
	public int getRndLoc(){
		return rnd;
	}
	public void setRndLoc(int i){
		this.rnd = i;
	}
	
	public String getMonName() {
		return monname;
	}
	public void setMonName(String mon) {
		this.monname = mon;
	}
	public int[][] getTime(){
		return time;
	}
	public void setTime(int[][] time){
		this.time = time;
	}
	public String[] getYoil() {
		return yoil;
	}
	public void setYoil(String[] yoil) {
		this.yoil = yoil;
	}
	
	public boolean isMent(){
		return _ment;
	}
	public void setMent(boolean flag){
		this._ment = flag;
	}
	
	public boolean isYn(){
		return _yn;
	}
	public void setYn(boolean flag){
		this._yn = flag;
	}
	
	public String getMentMessage(){
		return message;
	}
	public void setMentMessage(String ment){
		this.message = ment;
	}
	
	public String getYnMessage(){
		return ynmessage;
	}
	public void setYnMessage(String ment){
		this.ynmessage = ment;
	}
	
	private int _display_effect;
	public void set_display_effect(int display_effect){
		_display_effect = display_effect;
	}
	
	public int get_display_effect(){
		return _display_effect;
	}
	
	private int nonespawntimernd;
	
	public int getnonespawntime(){
		return nonespawntimernd;
	}
	public void setnonespawntime(int i){
		this.nonespawntimernd = i;
	}
	
	/**
	 * 스폰해야할 시간인지 확인해주는 함수.
	 * @param h
	 * @param m
	 * @return
	 */
	public boolean isSpawnTime(int h, int m, long current_time){

		String now_y = SystemUtil.getYoil(System.currentTimeMillis());
		boolean isYoil = false;
		for(String y : yoil) {
			if(y.equalsIgnoreCase("전체") || y.equalsIgnoreCase(now_y))
				isYoil = true;
		}
		if(isYoil == false)
			return false;
		
		for(int[] t : time){
			if(t[0]==h && t[1]==m) {
				return true;
			}
		}
		return false;
	}
}
