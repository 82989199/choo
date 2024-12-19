package l1j.server.MJSurveySystem;

import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_IconMessage;
import l1j.server.server.serverpackets.S_NotificationMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1BoardPost;

public class MJSurveyFactory {
	private static final S_SystemMessage _isOverEvent = new S_SystemMessage("이미 종료된 이벤트입니다.");
	
	public static MJInterfaceSurvey createLFCSurvey(){
		return new MJInterfaceSurvey(){
			@Override
			public void survey(L1PcInstance pc, int num, boolean isYes) {
				L1BoardPost bp = L1BoardPost.findByIdLfc(num - 1000);
				if(bp == null){
					pc.sendPackets("잘못된 응답입니다.");
					return;
				}
				String content = bp.getContent();
				String[] content_array = content.split(" ");
				String target_name = content_array[1];
				int type_id = Integer.parseInt(content_array[0]);
				L1PcInstance owner = L1World.getInstance().findpc(target_name);
				if(owner == null){
					pc.sendPackets(String.format("%s님을 찾을 수 없습니다.", target_name));
					MJLFCCreator.unregistLfc(pc);
					return;
				}
				
				if(!isYes){
					if(owner != null)
						owner.sendPackets(String.format("%s님이 결투를 취소했습니다.", pc.getName()));
					MJLFCCreator.unregistLfc(pc);
					return;
				}
				
				MJLFCCreator.create(pc, owner, type_id);
				MJLFCCreator.unregistLfc(pc);				
			}
		};
	}
	
	public static MJInterfaceSurvey createObjectCallSurvey(){
		return new MJInterfaceSurvey(){
			@Override
			public void survey(L1PcInstance pc, int num, boolean isYes) {
				if(!isYes)
					return;
				
				L1Object obj = L1World.getInstance().findObject(num);
				if(obj == null){
					pc.sendPackets(_isOverEvent, false);
					return;
				}
				
				if(obj instanceof L1Character){
					L1Character c = (L1Character)obj;
					if(c.isDead()){
						pc.sendPackets(_isOverEvent, false);
						return;						
					}
				}
				
				MJPoint pt = MJPoint.newInstance(obj.getX(), obj.getY(), 3, obj.getMapId());
				pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 169, true, false);
			}
		};
	}
	
	public static boolean isMegaphoneSpeaking = false;
	public static MJInterfaceSurvey createMegaphoneSurvey(int megaphoneObjectId, String message, int duration_second){
		return new MJInterfaceSurvey(){
			@Override
			public void survey(L1PcInstance pc, int num, boolean isYes){
				if(!isYes)
					return;
				
				L1ItemInstance item = pc.getInventory().findItemObjId(num);
				if(item == null){
					pc.sendPackets("확성기를 찾을 수 없습니다.");
					return;
				}
				
				if(isMegaphoneSpeaking){
					pc.sendPackets("이미 확성기 메시지가 재생되고 있습니다. 잠시 후 다시 이용해주세요.");
					return;
				}
				isMegaphoneSpeaking = true;
				final ServerBasePacket[] pcks = new ServerBasePacket[]{
						S_IconMessage.getMessage(String.format("[%s]님의 확성알림.", pc.getName()), new MJSimpleRgb(255,255,255), 17, duration_second),
						//S_IconMessage.getMessage(String.format("[%s]님의 확성알림.", pc.getName()), MJSimpleRgb.red(), 17, duration_second),
						S_NotificationMessage.get(S_NotificationMessage.DISPLAY_POSITION_BOTTOM, message, MJSimpleRgb.red(), duration_second) };
				L1World.getInstance().getAllPlayerStream()
				.filter((L1PcInstance t) -> t != null && t.getAI() == null)
				.forEach((L1PcInstance t) -> {
					t.sendPackets(pcks, false);
				});
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						isMegaphoneSpeaking = false;
					}
				}, duration_second * 1000L);
				pc.getInventory().removeItem(num, 1);
				for(ServerBasePacket pck : pcks)
					pck.clear();
			}
		};
	}
}
