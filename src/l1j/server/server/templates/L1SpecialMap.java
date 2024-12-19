package l1j.server.server.templates;

public class L1SpecialMap{
	private String _Name;
	private int _mapid;
	private int _itemid;
	private int _itemmincount;
	private int _itemmaxcount;
	private double _expRate;
	private int _chance;
	private boolean _isItem;
	private int _dmgreduction;
	private int _mdmgreduction;
	
	private double _dmgRate;
	public double getDmgRate(){
		return _dmgRate;
	}
	public void setDmgRate(double i){
		_dmgRate = i;
	}
	
	
	public boolean isGiveItem(){
		return _isItem;
	}
	public void setGiveItem(boolean flag){
		_isItem = flag;
	}
	public int getMapId(){
		return _mapid;
	}
	public void setMapId(int id){
		_mapid = id;
	}
	
	public int getItemId(){
		return _itemid;
	}
	public void setItemId(int id){
		_itemid = id;
	}
	
	public int getItemMinCount(){
		return _itemmincount;
	}
	public void setItemMinCount(int i){
		_itemmincount = i;
	}
	
	public int getItemMaxCount(){
		return _itemmaxcount;
	}
	public void setItemMaxCount(int i){
		_itemmaxcount = i;
	}
	
	public double getExpRate(){
		return _expRate;
	}
	public void setExpRate(double i){
		_expRate = i;
	}
	
	public int getChance(){
		return _chance;
	}
	public void setChance(int i){
		_chance = i;
	}

	public String getName(){
		return _Name;
	}
	public void setName(String name){
		_Name = name;
	}
	
	public int getDmgReduction(){
		return _dmgreduction;
	}
	public void setDmgReduction(int id){
		_dmgreduction = id;
	}
	
	public int getMdmgReduction(){
		return _mdmgreduction;
	}
	public void setMdmgReduction(int id){
		_mdmgreduction = id;
	}
}
