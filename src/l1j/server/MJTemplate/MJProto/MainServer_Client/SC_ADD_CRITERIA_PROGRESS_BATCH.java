package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ADD_CRITERIA_PROGRESS_BATCH implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ADD_CRITERIA_PROGRESS_BATCH newInstance(){
		return new SC_ADD_CRITERIA_PROGRESS_BATCH();
	}
	private int _total_pages;
	private int _current_page;
	private java.util.LinkedList<CriteriaProgress> _criteria_progress;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ADD_CRITERIA_PROGRESS_BATCH(){
	}
	public int get_total_pages(){
		return _total_pages;
	}
	public void set_total_pages(int val){
		_bit |= 0x1;
		_total_pages = val;
	}
	public boolean has_total_pages(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_current_page(){
		return _current_page;
	}
	public void set_current_page(int val){
		_bit |= 0x2;
		_current_page = val;
	}
	public boolean has_current_page(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<CriteriaProgress> get_criteria_progress(){
		return _criteria_progress;
	}
	public void add_criteria_progress(CriteriaProgress val){
		if(!has_criteria_progress()){
			_criteria_progress = new java.util.LinkedList<CriteriaProgress>();
			_bit |= 0x4;
		}
		_criteria_progress.add(val);
	}
	public boolean has_criteria_progress(){
		return (_bit & 0x4) == 0x4;
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
		if (has_total_pages())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _total_pages);
		if (has_current_page())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _current_page);
		if (has_criteria_progress()){
			for(CriteriaProgress val : _criteria_progress)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_total_pages()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_current_page()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_criteria_progress()){
			for(CriteriaProgress val : _criteria_progress){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_total_pages()){
			output.writeUInt32(1, _total_pages);
		}
		if (has_current_page()){
			output.writeUInt32(2, _current_page);
		}
		if (has_criteria_progress()){
			for(CriteriaProgress val : _criteria_progress){
				output.writeMessage(3, val);
			}
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
					set_total_pages(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_current_page(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_criteria_progress((CriteriaProgress)input.readMessage(CriteriaProgress.newInstance()));
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
		return new SC_ADD_CRITERIA_PROGRESS_BATCH();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_criteria_progress()){
			_criteria_progress.clear();
			_criteria_progress = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
