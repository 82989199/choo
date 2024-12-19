package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJRankSystem.*;
import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJTemplate.MJAbstractComparable;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeHelper;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_TOP_RANKER_NOTI extends MJAbstractComparable<SC_TOP_RANKER_NOTI> implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static final double _expError = 0.05D;
	
	public static boolean isError(int e1, int e2){
		double diff = e1 - e2;
		return diff < ((double)e2 * _expError);
	}
	
	private static final Byte[] _rank2RankLevel;
	private static final MJRankBuff[] _buffs;
	static{
		_rank2RankLevel = new Byte[201];
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 0, 10, (byte)11);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 11, 20, (byte)10);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 21, 40, (byte)9);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 41, 60, (byte)8);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 61, 80, (byte)7);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 81, 100, (byte)6);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 101, 120, (byte)5);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 121, 140, (byte)4);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 141, 160, (byte)3);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 161, 180, (byte)2);
		MJArrangeHelper.<Byte>setArrayValues(_rank2RankLevel, 181, 200, (byte)1);
		
		_buffs = new MJRankBuff[]{
				null,
				new MJRankBuffLv1(),
				new MJRankBuffLv2(),
				new MJRankBuffLv3(),
				new MJRankBuffLv4(),
				new MJRankBuffLv5(),
				new MJRankBuffLv6(),
				new MJRankBuffLv7(),
				new MJRankBuffLv8(),
				new MJRankBuffLv9(),
				new MJRankBuffLv10(),
				new MJRankBuffLv11(),
		};
	}
	
	public static SC_TOP_RANKER_NOTI newInstance(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = newInstance();
		noti.set_objectId(pc.getId());
		noti.set_exp(pc.getExp());
		
		Ranker rnk = Ranker.newInstance(pc.getName(), MJEClassesType.fromGfx(pc.getClassId()));
		noti.set_class_ranker(rnk);
		noti.set_class(rnk.get_class());
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		rnk = rnk.deepCopy();
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		noti.set_total_ranker(rnk);
		noti.set_characterInstance(pc);
		noti.set_almost_lower_class(false);
		noti.set_almost_lower_total(false);
		noti.set_almost_upper_class(false);
		noti.set_almost_upper_total(false);
		return noti;
	}
	
	public static SC_TOP_RANKER_NOTI newInstance(ResultSet rs) throws SQLException{
		SC_TOP_RANKER_NOTI noti = newInstance();
		noti.set_objectId(rs.getInt("objid"));
		noti.set_exp(rs.getInt("Exp"));
		
		Ranker rnk = Ranker.newInstance(rs);
		noti.set_class_ranker(rnk);
		noti.set_class(rnk.get_class());
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		
		rnk = rnk.deepCopy();
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		noti.set_total_ranker(rnk);
		noti.set_characterInstance(L1World.getInstance().getPlayer(rnk.get_name()));
		noti.set_almost_lower_class(false);
		noti.set_almost_lower_total(false);
		noti.set_almost_upper_class(false);
		noti.set_almost_upper_total(false);
		return noti;
	}
	
	public static SC_TOP_RANKER_NOTI newDummyInstance(){
		SC_TOP_RANKER_NOTI noti = newInstance();
		noti.set_objectId(1);
		noti.set_exp(100);
		
		Ranker rnk = Ranker.newInstance("", MJEClassesType.fromRand());
		noti.set_class_ranker(rnk);
		noti.set_class(rnk.get_class());
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		
		rnk = rnk.deepCopy();
		rnk.set_previous_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		rnk.set_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		noti.set_total_ranker(rnk);
		noti.set_characterInstance(null);
		noti.set_almost_lower_class(false);
		noti.set_almost_lower_total(false);
		noti.set_almost_upper_class(false);
		noti.set_almost_upper_total(false);
		return noti;
	}
	
	public static SC_TOP_RANKER_NOTI newInstance(){
		return new SC_TOP_RANKER_NOTI();
	}
	
	private int _objectId;
	public void set_objectId(int val){
		_objectId = val;
	}
	
	public int get_objectId(){
		return _objectId;
	}
	
	private L1PcInstance _characterInstance;
	public L1PcInstance get_characterInstance(){
		return _characterInstance;
	}
	
	public void set_characterInstance(L1PcInstance pc){
		_characterInstance = pc;
	}
	
	private int _exp;
	public void set_exp(int exp){
		_exp = exp;
	}
	
	public int get_exp(){
		if(_characterInstance != null)
			_exp = _characterInstance.getExp();
		return _exp;
	}
	
	private int _class;
	public int get_class(){
		return _class;
	}
	
	public void set_class(int cls){
		_class = cls;
	}
	
	private Ranker _total_ranker;
	private Ranker _class_ranker;
	private boolean _almost_upper_total;
	private boolean _almost_lower_total;
	private boolean _almost_upper_class;
	private boolean _almost_lower_class;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TOP_RANKER_NOTI(){
	}
	
	public void buff(){
		if(!MJRankLoadManager.MRK_SYS_ISON)
			return;
		
		int prev_place = _total_ranker.get_previous_rank();
		int prev_rating = prev_place <= 0 || prev_place >= _rank2RankLevel.length ? 0 : _rank2RankLevel[prev_place];
		int current_rating = _total_ranker.get_rating();
		
		if(prev_rating != current_rating){
			if(prev_rating != 0)
				_buffs[prev_rating].offBuff(this);
			if(current_rating != 0){
				_buffs[current_rating].onBuff(this);
			}
		}
	}
	
	public void onBuff(){
		int current_rating = _total_ranker.get_rating();
		if(current_rating != 0){
			if(_characterInstance != null)
				_characterInstance.sendPackets(writeTo(MJEProtoMessages.SC_TOP_RANKER_NOTI), true);
			_buffs[current_rating].onBuff(this);
		}
	}
	
	public void offBuff(){
		int current_rating = _total_ranker.get_rating();
		if(current_rating != 0){
			_buffs[current_rating].offBuff(this);
			_total_ranker.set_rating(0);
		}
	}
	
	public boolean isInRank(){
		return _characterInstance == null || _characterInstance.getLevel() >= MJRankLoadManager.MRK_SYS_MINLEVEL;
	}
	
	public void updateRating(int class_place, int total_place){
		if(MJRankLoadManager.MRK_SYS_ISON){
			//int class_rating = class_place <= 0 || class_place >= _rank2RankLevel.length ? 0 : _rank2RankLevel[class_place];
			int total_rating = total_place <= 0 || total_place >= _rank2RankLevel.length ? 0 : _rank2RankLevel[total_place];
			/*if(total_rating > 0 && (_characterInstance != null && !_characterInstance.is_top_ranker())){
				_characterInstance.sendPackets(writeTo(MJEProtoMessages.SC_TOP_RANKER_NOTI), true);
				_buffs[total_rating].onBuff(this);
			}*/
			/*
			_class_ranker.set_rating(total_rating);
			_total_ranker.set_rating(total_rating);
			_class_ranker.set_class_rating(class_rating);
			_total_ranker.set_class_rating(class_rating);
			*/
			
			_class_ranker.set_rating(total_rating);
			_total_ranker.set_rating(total_rating);
			if(total_rating < 9 && class_place <= 3){
				_class_ranker.set_class_rating(11);
				_total_ranker.set_class_rating(11);				
			}
		}
	}
	
	public void updatePlace(int class_place, int total_place){
		_class_ranker.set_rank(class_place);
		_total_ranker.set_rank(total_place);
		
		int limit_place = MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1;
		int prev_place = _class_ranker.get_previous_rank();
		if(prev_place == limit_place && class_place < limit_place)
			_class_ranker.set_previous_rank(class_place);
		
		limit_place = MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1;
		prev_place = _total_ranker.get_previous_rank();
		if(prev_place == limit_place && total_place < limit_place)
			_total_ranker.set_previous_rank(total_place);
	}
	
	public void updateAlmost(SC_TOP_RANKER_NOTI prev_class, SC_TOP_RANKER_NOTI prev_total){
		_almost_upper_class = false;
		_almost_upper_total = false;
		if(prev_class != null){
			_almost_upper_class = isError(prev_class.get_exp(), get_exp());
			prev_class.set_almost_lower_class(_almost_upper_class);
		}
		
		if(prev_total != null){
			_almost_upper_total = isError(prev_total.get_exp(), get_exp());
			prev_total.set_almost_lower_total(_almost_upper_total);
		}
	}
	
	public void updatePlace(){
		_total_ranker.set_rank(MJRankLoadManager.MRK_SYS_TOTAL_RANGE + 1);
		_class_ranker.set_rank(MJRankLoadManager.MRK_SYS_CLASS_RANGE + 1);
		_class_ranker.set_rating(0);
		_total_ranker.set_rating(0);
		_class_ranker.set_class_rating(0);
		_total_ranker.set_class_rating(0);
	}
	
	public void updateAlmost(){
		_almost_upper_total = 
		_almost_lower_total = 
		_almost_upper_class = 
		_almost_lower_class = false;
	}
	
	public Ranker get_total_ranker(){
		return _total_ranker;
	}
	public void set_total_ranker(Ranker val){
		_bit |= 0x00000001;
		_total_ranker = val;
	}
	public boolean has_total_ranker(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public Ranker get_class_ranker(){
		return _class_ranker;
	}
	public void set_class_ranker(Ranker val){
		_bit |= 0x00000002;
		_class_ranker = val;
	}
	public boolean has_class_ranker(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public boolean get_almost_upper_total(){
		return _almost_upper_total;
	}
	public void set_almost_upper_total(boolean val){
		_bit |= 0x00000004;
		_almost_upper_total = val;
	}
	public boolean has_almost_upper_total(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public boolean get_almost_lower_total(){
		return _almost_lower_total;
	}
	public void set_almost_lower_total(boolean val){
		_bit |= 0x00000008;
		_almost_lower_total = val;
	}
	public boolean has_almost_lower_total(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public boolean get_almost_upper_class(){
		return _almost_upper_class;
	}
	public void set_almost_upper_class(boolean val){
		_bit |= 0x00000010;
		_almost_upper_class = val;
	}
	public boolean has_almost_upper_class(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public boolean get_almost_lower_class(){
		return _almost_lower_class;
	}
	public void set_almost_lower_class(boolean val){
		_bit |= 0x00000020;
		_almost_lower_class = val;
	}
	public boolean has_almost_lower_class(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_total_ranker())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _total_ranker);
		if (has_class_ranker())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _class_ranker);
		if (has_almost_upper_total())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _almost_upper_total);
		if (has_almost_lower_total())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _almost_lower_total);
		if (has_almost_upper_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _almost_upper_class);
		if (has_almost_lower_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _almost_lower_class);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_total_ranker()){
			output.writeMessage(1, _total_ranker);
		}
		if (has_class_ranker()){
			output.writeMessage(2, _class_ranker);
		}
		if (has_almost_upper_total()){
			output.writeBool(3, _almost_upper_total);
		}
		if (has_almost_lower_total()){
			output.writeBool(4, _almost_lower_total);
		}
		if (has_almost_upper_class()){
			output.writeBool(5, _almost_upper_class);
		}
		if (has_almost_lower_class()){
			output.writeBool(6, _almost_lower_class);
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
				case 0x0000000A:{
					set_total_ranker((Ranker)input.readMessage(Ranker.newInstance()));
					break;
				}
				case 0x00000012:{
					set_class_ranker((Ranker)input.readMessage(Ranker.newInstance()));
					break;
				}
				case 0x00000018:{
					set_almost_upper_total(input.readBool());
					break;
				}
				case 0x00000020:{
					set_almost_lower_total(input.readBool());
					break;
				}
				case 0x00000028:{
					set_almost_upper_class(input.readBool());
					break;
				}
				case 0x00000030:{
					set_almost_lower_class(input.readBool());
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
		return new SC_TOP_RANKER_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_total_ranker() && _total_ranker != null){
			_total_ranker.dispose();
			_total_ranker = null;
		}
		if (has_class_ranker() && _class_ranker != null){
			_class_ranker.dispose();
			_class_ranker = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	@Override
	public int compareTo(SC_TOP_RANKER_NOTI o) {
		return o.get_exp() - get_exp();
	}
}
