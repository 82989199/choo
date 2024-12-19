package l1j.server.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyMember;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST_CHANGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_STATUS;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_SYNC_PERIODIC_INFO;
import l1j.server.server.Controller.BraveavatarController;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class L1Party {

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(L1Party.class.getName());

	protected final List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>();
	protected final HashMap<Integer, Integer> _membersMark = new HashMap<Integer, Integer>();
	
	private L1PcInstance _leader = null;

	public void addMember(L1PcInstance pc) {
		if (pc == null) {
			throw new NullPointerException();
		}
		if (_membersList.size() == Config.MAX_PT && !_leader.isGm() || _membersList.contains(pc)) {
			pc.sendPackets(new S_ServerMessage(417));
			return;
		}

		if (_membersList.isEmpty()) {
			setLeader(pc);
		}

		if (pc.isCrown()) {
			if (SkillsTable.getInstance().spellCheck(pc.getId(), 121)) {
				BraveavatarController.getInstance().addMember(pc);
			}
		}

		_membersMark.put(pc.getId(), 0);
		_membersList.add(pc);
		pc.setParty(this);
		
		SC_PARTY_MEMBER_LIST noti = SC_PARTY_MEMBER_LIST.newInstance();
		noti.set_leader_name(_leader.getName());
		noti.add_member(PartyMember.newInstance(pc, 0));
		if(getNumOfMembers() > 1){
			SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
			PartyMember pm = PartyMember.newInstance(pc, 0);
			change.set_new_user(pm);
			
			ProtoOutputStream stream = change.writeTo(MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE);
			for (L1PcInstance member : getMembers()) {
				if(member.getId() != pc.getId()){
					member.sendPackets(stream, false);
					noti.add_member(PartyMember.newInstance(member, _membersMark.get(member.getId())));
				}
			}
			stream.dispose();
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
	}

	protected void removeMember(L1PcInstance pc) {
		L1Character _target = pc;
		if (!_membersList.contains(pc)) {
			return;
		}

		SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
		change.set_out_user(pc.getName());
		broadcast(change, MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE, true);
		_membersList.remove(pc);
		// 리더가 탈퇴할경우 리스트에서 삭제
		if (pc.isCrown()) {
			if (isLeader(pc)) {
				BraveavatarController.getInstance().removeMember(pc);
			}
		}
		pc.setParty(null);
		// 파티에서 제외되었을때, 브레이브 아바타 효과를 제거한다.
		if (pc.getPbavatar()) {
			BraveavatarController.getInstance().brave_end(pc);
		}
		
		if (pc._CubeEffect) {
			if (_target.hasSkillEffect(L1SkillId.CUBE_AVATAR))
				_target.removeSkillEffect(L1SkillId.CUBE_AVATAR);
			if (_target.hasSkillEffect(L1SkillId.CUBE_RICH))
				_target.removeSkillEffect(L1SkillId.CUBE_RICH);
			if (_target.hasSkillEffect(L1SkillId.CUBE_GOLEM))
				_target.removeSkillEffect(L1SkillId.CUBE_GOLEM);
			if (_target.hasSkillEffect(L1SkillId.CUBE_OGRE))
				_target.removeSkillEffect(L1SkillId.CUBE_OGRE);
			pc._CubeEffect = false;
		}
	}

	public boolean isVacancy() {
		return _membersList.size() < Config.MAX_PT;
	}

	public int getVacancy() {
		return Config.MAX_PT - _membersList.size();
	}

	public boolean isMember(L1PcInstance pc) {
		return _membersList.contains(pc);
	}

	protected void setLeader(L1PcInstance pc) {
		_leader = pc;
	}

	public L1PcInstance getLeader() {
		return _leader;
	}

	public boolean isLeader(L1PcInstance pc) {
		return pc.getId() == _leader.getId();
	}

	public boolean isAutoDivision(L1PcInstance pc) {
		return pc.getPartyType() == 1 || pc.getPartyType() == 4;
	}

	public String getMembersNameList() {
		String _result = new String("");
		for (L1PcInstance pc : _membersList) {
			_result = _result + pc.getName() + " ";
		}
		return _result;
	}

	public void refresh(L1PcInstance pc) {// 파티추가
		L1PcInstance leader = getLeader();
		for (L1PcInstance member : getMembers()) {
			if(member == null || pc.getId() == member.getId())
				continue;
			
			if (leader != null && leader.getId() == pc.getId()) {
				// NameColor
				member.sendPackets(new S_Party(0x6c2, pc));
			} else {
				// NameColor
				member.sendPackets(new S_Party(0x6c1, pc));
			}
		}
	}

	public void memberDie(L1PcInstance pc) {// 파티원 죽엇을때
		for (L1PcInstance member : getMembers()) {
			if (pc.getId() == member.getId()) {
				continue;
			}
			// NameColor
			member.sendPackets(new S_Party(0x6c0, pc));
		}
	}
	
	public void broadcastPartyList(){
		SC_PARTY_MEMBER_LIST noti = SC_PARTY_MEMBER_LIST.newInstance();
		noti.set_leader_name(_leader.getName());
		for (L1PcInstance member : getMembers()) {
			
			PartyMember pm = PartyMember.newInstance(member, _membersMark.get(member.getId()));
			noti.add_member(pm);
		}
		broadcast(noti, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
	}
	
	public void refreshPartyList(){
		SC_PARTY_SYNC_PERIODIC_INFO info = SC_PARTY_SYNC_PERIODIC_INFO.newInstance();
		for (L1PcInstance member : getMembers()) {
			
			SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance();
			status.set_object_id(member.getId());
			status.set_location(member.getLongLocation());
			status.set_world(member.getMapId());
			status.set_mark(_membersMark.get(member.getId()));
			status.set_mp_ratio((int)member.getCurrentHpPercent());
			status.set_hp_ratio((int)member.getCurrentMpPercent());
			status.set_name(member.getName());
			status.set_status(1);
			info.add_status(status);
		}
		broadcast(info, MJEProtoMessages.SC_PARTY_SYNC_PERIODIC_INFO, true);
	}
	
	public void handshakePartyMemberStatus(L1PcInstance pc1, L1PcInstance pc2){
		SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance();
		status.set_name(pc1.getName());
		status.set_object_id(pc1.getId());
		status.set_location(pc1.getLongLocationReverse());
		status.set_hp_ratio((int)pc1.getCurrentHpPercent());
		status.set_mp_ratio((int)pc1.getCurrentMpPercent());
		status.set_mark(_membersMark.get(pc1.getId()));
		status.set_status(pc1.isDead() ? 0 : 1);
		status.set_world(pc1.getMapId());
		pc2.sendPackets(status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
		
		status = SC_PARTY_MEMBER_STATUS.newInstance();
		status.set_name(pc2.getName());
		status.set_object_id(pc2.getId());
		status.set_location(pc2.getLongLocationReverse());
		status.set_hp_ratio((int)pc2.getCurrentHpPercent());
		status.set_mp_ratio((int)pc2.getCurrentMpPercent());
		status.set_mark(_membersMark.get(pc2.getId()));
		status.set_status(pc2.isDead() ? 0 : 1);
		status.set_world(pc2.getMapId());
		pc1.sendPackets(status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
	}

	public void refreshPartyMemberStatus(L1PcInstance pc){
		SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance();
		status.set_name(pc.getName());
		status.set_object_id(pc.getId());
		status.set_location(pc.getLongLocationReverse());
		status.set_hp_ratio((int)pc.getCurrentHpPercent());
		status.set_mp_ratio((int)pc.getCurrentMpPercent());
		status.set_mark(_membersMark.get(pc.getId()));
		status.set_status(pc.isDead() ? 0 : 1);
		status.set_world(pc.getMapId());
		broadcast(status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
	}
	
	protected void breakup() {
		for (L1PcInstance member : getMembers()) {
			removeMember(member);
			member.sendPackets(new S_ServerMessage(418));
		}
	}

	public void passLeader(L1PcInstance pc) { // 리더위임
		setLeader(pc);
		broadcastPartyList();
		/*SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
		change.set_leader_name(pc.getName());
		broadcast(change, MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE, true);*/
	}

	public void leaveMember(L1PcInstance pc) {
		if (isLeader(pc) || getNumOfMembers() == 2) {
			breakup();
		} else {
			removeMember(pc);
			for (L1PcInstance member : getMembers()) {
				sendLeftMessage(member, pc);
			}
			sendLeftMessage(pc, pc);
		}
	}

	public void kickMember(L1PcInstance pc) { // 리더추방
		if (getNumOfMembers() == 2) {
			breakup();
		} else {
			removeMember(pc);
			for (L1PcInstance member : getMembers()) {
				sendLeftMessage(member, pc);
			}
			sendKickMessage(pc);
		}
		pc.sendPackets(new S_ServerMessage(419)); // 파티로부터 추방되었습니다.
	}

	public L1PcInstance[] getMembers() {
		return _membersList.toArray(new L1PcInstance[_membersList.size()]);
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}

	private void sendKickMessage(L1PcInstance kickpc) {
		kickpc.sendPackets(new S_ServerMessage(419));
	}

	protected void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
		sendTo.sendPackets(new S_ServerMessage(420, left.getName()));
	}

	public List<L1PcInstance> getList() {// 하딘 파티 멤버 리스트
		return _membersList;
	}
	
	public Stream<L1PcInstance> createMembersStream(){
		return _membersList.stream();
	}
	
	public Stream<L1PcInstance> createVisibleMembersStream(L1PcInstance pc){
		int mid = pc.getMapId();
		L1Location pt = pc.getLocation();
		return createMembersStream().filter(
				(L1PcInstance m) -> 
					m.getMapId() == mid && pt.getTileLineDistance(m.getLocation()) <= 50
				);
	}
	
	public void broadcast(ServerBasePacket packet){
		createMembersStream().forEach((L1PcInstance pc) -> pc.sendPackets(packet, false));
		packet.clear();
	}
	
	public void broadcast(MJIProtoMessage message, int messageId){
		broadcast(message, messageId, true);
	}
	
	public void broadcast(MJIProtoMessage message, MJEProtoMessages e, boolean isClear){
		broadcast(message, e.toInt(), isClear);
	}
	
	public void broadcast(MJIProtoMessage message, MJEProtoMessages e){
		broadcast(message, e, true);
	}
	
	public void broadcast(MJIProtoMessage message, int messageId, boolean isClear){
		if(message.isInitialized()){
			ProtoOutputStream stream = message.writeTo(MJEProtoMessages.fromInt(messageId));
			for (L1PcInstance member : getMembers()) {
				member.sendPackets(stream, false);
			}
			if(isClear)
				message.dispose();
		}else{
			MJEProtoMessages.printNotInitialized("party broadcast.", messageId, message.getInitializeBit());
		}
	}
	
	public void broadcastRootMent(ServerBasePacket packet){
		createMembersStream().filter((L1PcInstance pc) -> pc.RootMent).forEach((L1PcInstance pc) -> pc.sendPackets(packet, false));
		packet.clear();
	}

	public void setPartyMark(L1PcInstance target, int markId){
		_membersMark.put(target.getId(), markId);
		SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
		change.set_new_user(PartyMember.newInstance(target, markId));
		broadcast(change, MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE, true);
	}
}
