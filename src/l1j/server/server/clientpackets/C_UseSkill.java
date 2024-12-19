package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJCharacterActionSystem.SpellActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_UseSkill extends ClientBasePacket {
	public C_UseSkill(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		long currentMillis = System.currentTimeMillis();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.get_teleport() || pc.isDead())
			return;
		
		int skillId = (readC() * 8) + readC() + 1;
		MJPacketParser parser = SpellActionHandlerFactory.create(skillId);
		if(parser == null)
			return;
		
		
		/**배운 스킬인지 검사 */
		if(pc.isSkillMastery(skillId) == false){
			return;
		}
		
		if(Config.IS_SPELL_DELAY_RUN){
			if(pc.lastSpellUseMillis > currentMillis){
				if(++pc.lastSpellUsePending > Config.SPELL_DELAYOVER_PENDING){
					System.out.println(String.format("[%s] 스킬 딜레이 오버, 스킬아이디 : %d, 오차 : %d", pc.getName(), skillId, pc.lastSpellUseMillis - currentMillis));
					return;
				}
			}else
				pc.lastSpellUsePending = 0;
		}
		parser.parse(pc, this);
		parser.doWork();
		
		long l = SpriteInformationLoader.getInstance().getUseSpellInterval(pc, skillId) - Config.SPELL_DELAYERROR;
		pc.lastSpellUseMillis = currentMillis + (l > 0 ? l : 0L);
	}
}
