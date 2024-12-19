package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class Ranker implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static Ranker newInstance(String name, MJEClassesType type){
		Ranker rnk = newInstance();
		rnk.set_name(name);
		rnk.set_class(type.toInt());
		return rnk;
	}
	
	public static Ranker newInstance(ResultSet rs) throws SQLException{
		return newInstance(rs.getString("char_name"), MJEClassesType.fromGfx(rs.getInt("class")));
	}
	
	public static Ranker newInstance(){
		return new Ranker();
	}
	
	private int _rating;
	private int _rank;
	private int _previous_rank;
	private int _class;
	private String _name;
	private int _uid;
	private int _score;
	private int _class_rating;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private Ranker(){
	}
	
	public Ranker deepCopy(){
		Ranker rnk = newInstance();
		rnk.set_rating(_rating);
		rnk.set_rank(_rank);
		rnk.set_previous_rank(_previous_rank);
		rnk.set_class(_class);
		rnk.set_name(_name);
		//rnk.set_class_rating(_class_rating);
		return rnk;
	}
	
	public int get_rating(){
		return _rating;
	}
	public void set_rating(int val){
		_bit |= 0x00000001;
		_rating = val;
	}
	public boolean has_rating(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_rank(){
		return _rank;
	}
	public void set_rank(int val){
		set_previous_rank(_rank);
		_bit |= 0x00000002;
		_rank = val;
	}
	public boolean has_rank(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_previous_rank(){
		return _previous_rank;
	}
	public void set_previous_rank(int val){
		_bit |= 0x00000004;
		_previous_rank = val;
	}
	public boolean has_previous_rank(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public int get_class(){
		return _class;
	}
	public void set_class(int val){
		_bit |= 0x00000008;
		_class = val;
	}
	public boolean has_class(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x00000010;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public int get_uid(){
		return _uid;
	}
	public void set_uid(int val){
		_bit |= 0x00000020;
		_uid = val;
	}
	public boolean has_uid(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	public int get_score(){
		return _score;
	}
	public void set_score(int val){
		_bit |= 0x00000040;
		_score = val;
	}
	public boolean has_score(){
		return (_bit & 0x00000040) == 0x00000040;
	}
	public int get_class_rating(){
		return _class_rating;
	}
	public void set_class_rating(int val){
		_bit |= 0x00000080;
		_class_rating = val;
	}
	public boolean has_class_rating(){
		return (_bit & 0x00000080) == 0x00000080;
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
		if (has_rating())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _rating);
		if (has_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _rank);
		if (has_previous_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _previous_rank);
		if (has_class())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _class);
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _name);
		if (has_uid())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _uid);
		if (has_score())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _score);
		if (has_class_rating())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _class_rating);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_rank()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_class()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_rating()){
			output.wirteInt32(1, _rating);
		}
		if (has_rank()){
			output.wirteInt32(2, _rank);
		}
		if (has_previous_rank()){
			output.wirteInt32(3, _previous_rank);
		}
		if (has_class()){
			output.wirteInt32(4, _class);
		}
		if (has_name()){
			output.writeString(5, _name);
		}
		if (has_uid()){
			output.wirteInt32(6, _uid);
		}
		if (has_score()){
			output.wirteInt32(7, _score);
		}
		if (has_class_rating()){
			output.wirteInt32(8, _class_rating);
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
					set_rating(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_rank(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_previous_rank(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_class(input.readInt32());
					break;
				}
				case 0x0000002A:{
					set_name(input.readString());
					break;
				}
				case 0x00000030:{
					set_uid(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_score(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_class_rating(input.readInt32());
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
		return new Ranker();
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
