/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.utils.SQLUtil;

public class MobSkillTable {

	private static Logger _log = Logger.getLogger(MobSkillTable.class.getName());
	private final boolean _initialized;
	private static MobSkillTable _instance;
	private final HashMap<Integer, L1MobSkill> _mobskills;
	public static MobSkillTable getInstance() {
		if (_instance == null) {
			_instance = new MobSkillTable();
		}
		return _instance;
	}

	public boolean isInitialized() {
		return _initialized;
	}

	private MobSkillTable() {
		_mobskills = new HashMap<Integer, L1MobSkill>(512);
		loadMobSkillData();
		_initialized = true;
	}

	public static void reload() {
		MobSkillTable oldInstance = _instance;
		_instance = new MobSkillTable();
		oldInstance._mobskills.clear();
		oldInstance = null;
	}
	
	private void loadMobSkillData() {
		Connection con = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs1 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT mobid,count(*) as cnt FROM mobskill group by mobid");

			int count = 0;
			int mobid = 0;
			int actNo = 0;

			pstm2 = con.prepareStatement("SELECT * FROM mobskill where mobid = ? order by mobid,actNo");

			for (rs1 = pstm1.executeQuery(); rs1.next();) {
				mobid = rs1.getInt("mobid"); // 스킬을 사용하는 NPC의 ID
				count = rs1.getInt("cnt");
				ResultSet rs2 = null;

				try {
					pstm2.setInt(1, mobid);
					L1MobSkill mobskill = new L1MobSkill(count);
					mobskill.set_mobid(mobid);

					rs2 = pstm2.executeQuery();
					while (rs2.next()) {
						actNo = rs2.getInt("actNo");
						// 해당 몬스터가 스킬 사용 시 스킬 사용 순서, 반드시 0부터 시작하며 1씩 늘어난다.
						
						mobskill.setMobName(rs2.getString("mobname"));
						// 해당 몬스터의 이름
						
						mobskill.setType(actNo, rs2.getInt("type"));
						// 1:물리 공격, 2：마법 공격, 3：사몬, 4：강제 변신
						
						mobskill.setTriggerRandom(actNo, rs2.getInt("TriRnd"));
						// 스킬 발동 조건：랜덤인 확률(1~100)
						
						mobskill.setTriggerHp(actNo, rs2.getInt("TriHp"));
						// 스킬 발동 조건：HP가 %이하일때 발동(1~100)
						
						mobskill.setTriggerCompanionHp(actNo, rs2.getInt("TriCompanionHp"));
						// 스킬 발동 조건：동족의 HP가%이하로 발동(1~100)
						// 이 스킬이 발동하면 타겟을 HP비율이 제일 적은 동족 NPC로 변경합니다.
						
						mobskill.setTriggerRange(actNo, rs2.getInt("TriRange"));
						// 스킬 발동 조건：triRange<0의 경우, 대상과의 거리가 절대치(triRange) 이하 때 발동
						// triRange>0의 경우, 대상과의 거리가 triRange 이상 때 발동
						// 음수 -기호를 사용한다. -5는 5셀이라는 뜻이다.
						
						mobskill.setTriggerCount(actNo, rs2.getInt("TriCount"));
						// 스킬 발동 조건：스킬이 몇번 발동하는지
						
						mobskill.setChangeTarget(actNo, rs2.getInt("ChangeTarget"));
						// 스킬 발동 후, 타겟을 변경하는 경우로 지정합니다. (기본값 0)
						
						mobskill.setRange(actNo, rs2.getInt("Range"));
						// 물리 공격에만 사용한다.
						// 공격할 수 있는 거리 (5 = 5칸)
						
						mobskill.setAreaWidth(actNo, rs2.getInt("AreaWidth"));
						// 물리 공격에만 사용한다.
						// 범위 공격의 가로폭, 단체 공격이라면 0을 설정, 범위 공격한다면 0이상을 설정
						// Width와 Height의 설정은 공격자로부터 봐 가로폭을 Width, 깊이를 Height로 한다.
						// Width는 1을 지정하면, 타겟을 중심으로서 좌우 1까지가 대상이 된다.
						
						mobskill.setAreaHeight(actNo, rs2.getInt("AreaHeight"));
						// 물리 공격에만 사용한다.
						// 범위 공격의 깊이, 단체 공격이라면 0을 설정, 범위 공격한다면 1이상을 설정						
						
						mobskill.setLeverage(actNo, rs2.getInt("Leverage"));
						// 마법 공격에 사용된다.
						// 데미지의 배율, 값이 높아질수록 대미지가 더 강해진다.
						
						mobskill.setSkillId(actNo, rs2.getInt("SkillId"));
						// 마법 공격에만 사용한다.
						// 마법을 사용하는 경우, skills 테이블에 기재되어 있는 skillId를 지정
						
						mobskill.setGfxid(actNo, rs2.getInt("Gfxid"));
						// 물리 공격에만 사용한다.
						// 물리 공격의 모션 그래픽을 지정하는 경우 기입한다.
						// 예：드레이크의 불길, 용의 꼬리·목모습등
						
						mobskill.setActid(actNo, rs2.getInt("Actid"));
						// 물리 공격에만 사용한다.
						// 물리 공격의 그래픽 액션 ID
						// 예 : 오우거의 도끼질, 스콜피온의 꼬리 공격
						
						mobskill.setSummon(actNo, rs2.getInt("SummonId"));
						// 서먼으로 소환하려는 몬스터의 NPCID
						
						mobskill.setSummonMin(actNo, rs2.getInt("SummonMin"));
						// 서먼으로 소환하려는 몬스터의 최소 수
						
						mobskill.setSummonMax(actNo, rs2.getInt("SummonMax"));
						// 서먼으로 소환하려는 몬스터의 최대 수
						
						mobskill.setPolyId(actNo, rs2.getInt("PolyId"));
						// 강제 변신시키려는 ID이다. 즉, Polymorphs 테이블을 참조한다.
						// 강제 변신시키는 대상은 15셀 이내에 있는 플레이어가 대상자가된다.
						// 벽은 관통하지 않는다.
					}
					_mobskills.put(new Integer(mobid), mobskill);
				} catch (SQLException e1) {
					_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);

				} finally {
					SQLUtil.close(rs2);
				}
			}

		} catch (SQLException e2) {
			_log.log(Level.SEVERE, "error while creating mobskill table", e2);

		} finally {
			SQLUtil.close(rs1);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	public L1MobSkill getTemplate(int id) {
		return _mobskills.get(id);
	}
}
