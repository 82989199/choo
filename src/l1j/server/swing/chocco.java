package l1j.server.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;
import com.sun.management.OperatingSystemMXBean;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.Server;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
//import l1j.server.server.utils.MJNodimShield;
import l1j.server.server.utils.SystemUtil;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

/** 成员加载 */

public class chocco extends JFrame {
	/** 构造函数 */
	public static boolean inf;
	public static String[] save_text = new String[7]; // 用于保存各字段内容的临时空间
	public static boolean serverstart; // 判断服务器是否正在运行
	public static int check = 0; // 通过界面调整大小（用于更改图像）
	public static int x_size = 0; // 用于存储初始管理器大小
	private ImageIcon logo = new ImageIcon("data/img/logo.jpg"); // 管理器标志图像
	private ImageIcon start = new ImageIcon("data/img/start.png"); // 开始按钮图像
	private ImageIcon exit = new ImageIcon("data/img/exit.png"); // 退出按钮图像
	private ImageIcon up = new ImageIcon("data/img/up.png"); // 窗口上移图像
	private ImageIcon down = new ImageIcon("data/img/down.png"); // 窗口下移图像
	private ImageIcon so1 = new ImageIcon("data/img/so1.png"); // 向上箭头图像
	private ImageIcon so2 = new ImageIcon("data/img/so2.png"); // 向上箭头图像
	private ImageIcon chating = new ImageIcon("data/img/chat.jpg"); // 聊天图像
	private ImageIcon in = new ImageIcon("data/img/in.png"); // 向右箭头图像

	private javax.swing.JButton buf; // 全体增益按钮
	private javax.swing.JTextField chat; // 聊天字段
	private javax.swing.JButton ex; // 退出按钮
	public static javax.swing.JLabel first; // 管理器初始文字
	private javax.swing.JInternalFrame iframe; // 查看各种日志的框架
	private javax.swing.JInternalFrame iframe2; // 查看用户列表的框架
	private javax.swing.JLabel label1; // 显示当前用户的标签
	public static javax.swing.JLabel label2; // --
	// private javax.swing.JLabel label3; // --
	private javax.swing.JLabel label4; // 显示当前使用的内存量
	private javax.swing.JLabel lblThread;
	private javax.swing.JLabel lblCPU;
	public static javax.swing.JLayeredPane layout1; // 类似菜单的界面布局
	private javax.swing.JLayeredPane layout2; // --
	private javax.swing.JLayeredPane layout3; // --
	private javax.swing.JLayeredPane layout4; // --
	private javax.swing.JButton logcl; // 日志窗口（各种文本窗口）清除按钮
	private javax.swing.JButton logsv; // 日志窗口保存按钮
	private javax.swing.JTextField name; // 用于私聊的字段

	private javax.swing.JButton st; // 开始按钮
	public static javax.swing.JLabel stime; // 显示服务器运行时间的标签
	private javax.swing.JButton sv; // 服务器保存按钮
	private javax.swing.JTabbedPane tabframe; // 用于分别查看各日志窗口的选项卡面板

	private javax.swing.JScrollPane spGlobalChat; // 各日志窗口滚动面板
	private javax.swing.JScrollPane spClanChat; // --
	private javax.swing.JScrollPane spPartyChat; // --
	private javax.swing.JScrollPane spWhisper; // --
	private javax.swing.JScrollPane spShop; // --
	private javax.swing.JScrollPane spTrade; // --
	private javax.swing.JScrollPane spWarehouse; // --
	private javax.swing.JScrollPane spEnchant;
	private javax.swing.JScrollPane spPickup;
	private javax.swing.JScrollPane spUserList; // --
	public static javax.swing.JTextArea txtGlobalChat; //
	public static javax.swing.JTextArea txtClanChat;
	public static javax.swing.JTextArea txtPartyChat; //
	public static javax.swing.JTextArea txtWhisper; //
	public static javax.swing.JTextArea txtShop; //
	public static javax.swing.JTextArea txtTrade; //
	public static javax.swing.JTextArea txtWarehouse; //
	public static javax.swing.JTextArea txtEnchant; //
	public static javax.swing.JTextArea txtPickup; //
	public static java.awt.List userlist; // 列表
	public static java.awt.List iplist; // 被封IP列表
	private javax.swing.JPopupMenu popmenu1; // 用于列表窗口的弹出菜单
	private javax.swing.JPopupMenu popmenu2; // 用于列表窗口的弹出菜单2
	private javax.swing.JMenuItem menu1; // 菜单1（驱逐）
	private javax.swing.JMenuItem menu2; // 菜单2 -> ??
	private javax.swing.JMenuItem menu3; // 菜单3 //
	private javax.swing.JMenuItem menu4; // 菜单4 //
	private javax.swing.JMenuItem menu5; // 菜单5 //
	private javax.swing.JMenuItem menu6; // 赠送礼物
	private javax.swing.JMenuItem menu7; // 私聊
	private javax.swing.JTabbedPane chocco; // 追加用
	public static int count = 0;

	private javax.swing.JPopupMenu pmReload;

	public static JFrame setting;
	private javax.swing.JTextField exp;
	private javax.swing.JTextField lawful;
	private javax.swing.JTextField karma;
	private javax.swing.JTextField adena;
	private javax.swing.JTextField item;
	private javax.swing.JTextField enweapon;
	private javax.swing.JTextField enarmor;
	private javax.swing.JTextField weightlimit;
	private javax.swing.JTextField chatlvl;
	private javax.swing.JTextField maxuser;
	private javax.swing.JButton set;

	private javax.swing.JLabel exp1;
	private javax.swing.JLabel lawful1;
	private javax.swing.JLabel karma1;
	private javax.swing.JLabel adena1;
	private javax.swing.JLabel item1;
	private javax.swing.JLabel enweapon1;
	private javax.swing.JLabel enarmor1;
	private javax.swing.JLabel weightlimit1;
	private javax.swing.JLabel chatlvl1;
	private javax.swing.JLabel maxuser1;

	/** 默认构造函数 */
	public chocco() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\jeong\\Desktop\\17차 덩크서버\\data\\img\\manager.png"));
		getContentPane().setBackground(Color.WHITE);
		try {
			UIManager.setLookAndFeel(new FlatLightLaf()); // 应用扁平设计
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		/** 设置各菜单/组件 */
		initComponents();
		/** 保存初始管理器大小 */
		x_size = this.getHeight();
		/** 设置初始管理器大小 */
		this.setSize(830, 500);
		/** 设置管理器启动位置 */
		this.setLocation(500, 200);
	}

	/** 设置各菜单/组件... 不能遗漏任何一个... 否则不仅编译，运行时也可能发生错误 */
	private void initComponents() {
		layout1 = new javax.swing.JLayeredPane();
		first = new javax.swing.JLabel();
		first.setHorizontalAlignment(SwingConstants.CENTER);
		st = new javax.swing.JButton((Icon) null);
		st.setForeground(Color.BLACK);
		st.setText("开始");
		st.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		ex = new javax.swing.JButton((Icon) null);
		ex.setForeground(Color.BLACK);
		ex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		ex.setText("退出");
		sv = new javax.swing.JButton();
		sv.setForeground(Color.BLACK);
		buf = new javax.swing.JButton();
		buf.setForeground(Color.BLACK);
		logsv = new javax.swing.JButton();
		logsv.setForeground(Color.BLACK);
		logcl = new javax.swing.JButton();
		logcl.setForeground(Color.BLACK);
		label1 = new javax.swing.JLabel();
		label2 = new javax.swing.JLabel();
		label4 = new javax.swing.JLabel();
		lblThread = new javax.swing.JLabel();
		lblCPU = new javax.swing.JLabel();
		layout2 = new javax.swing.JLayeredPane();
		iframe = new javax.swing.JInternalFrame();
		tabframe = new javax.swing.JTabbedPane();
		spGlobalChat = new javax.swing.JScrollPane();
		txtGlobalChat = new javax.swing.JTextArea();
		spShop = new javax.swing.JScrollPane();
		txtShop = new javax.swing.JTextArea();
		spTrade = new javax.swing.JScrollPane();
		txtTrade = new javax.swing.JTextArea();
		spClanChat = new javax.swing.JScrollPane();
		txtClanChat = new javax.swing.JTextArea();
		spPartyChat = new javax.swing.JScrollPane();
		txtPartyChat = new javax.swing.JTextArea();
		spWhisper = new javax.swing.JScrollPane();
		txtWhisper = new javax.swing.JTextArea();
		spWarehouse = new javax.swing.JScrollPane();
		txtWarehouse = new javax.swing.JTextArea();
		spEnchant = new javax.swing.JScrollPane();
		txtEnchant = new javax.swing.JTextArea();

		spPickup = new javax.swing.JScrollPane();
		txtPickup = new javax.swing.JTextArea();

		layout3 = new javax.swing.JLayeredPane();
		iframe2 = new javax.swing.JInternalFrame();
		spUserList = new javax.swing.JScrollPane();
		userlist = new java.awt.List();
		iplist = new java.awt.List();
		layout4 = new javax.swing.JLayeredPane();
		name = new javax.swing.JTextField();
		chat = new javax.swing.JTextField();
		stime = new javax.swing.JLabel();
		popmenu1 = new javax.swing.JPopupMenu();
		popmenu2 = new javax.swing.JPopupMenu();
		pmReload = new javax.swing.JPopupMenu();
		menu1 = new javax.swing.JMenuItem();
		menu2 = new javax.swing.JMenuItem();
		menu3 = new javax.swing.JMenuItem();
		menu4 = new javax.swing.JMenuItem();
		menu5 = new javax.swing.JMenuItem();
		menu6 = new javax.swing.JMenuItem();
		menu7 = new javax.swing.JMenuItem();
		chocco = new javax.swing.JTabbedPane();

		setting = new javax.swing.JFrame("Server Setting");
		chatlvl = new javax.swing.JTextField("", 5);
		maxuser = new javax.swing.JTextField("", 5);
		exp = new javax.swing.JTextField("", 5);
		adena = new javax.swing.JTextField("", 5);
		item = new javax.swing.JTextField("", 5);
		weightlimit = new javax.swing.JTextField("", 5);
		lawful = new javax.swing.JTextField("", 5);
		karma = new javax.swing.JTextField("", 5);
		enweapon = new javax.swing.JTextField("", 5);
		enarmor = new javax.swing.JTextField("", 5);
		set = new javax.swing.JButton("设置保存");

		chatlvl1 = new javax.swing.JLabel("　聊天等级");
		maxuser1 = new javax.swing.JLabel("　最大人数");
		exp1 = new javax.swing.JLabel("经验倍率");
		adena1 = new javax.swing.JLabel("金币倍率");
		item1 = new javax.swing.JLabel("物品倍率");
		weightlimit1 = new javax.swing.JLabel("负重上限");
		lawful1 = new javax.swing.JLabel("正义值倍率");
		karma1 = new javax.swing.JLabel("好友度倍率");
		enweapon1 = new javax.swing.JLabel("武器强化率");
		enarmor1 = new javax.swing.JLabel("防具强化率");

		setting.getContentPane().add(chatlvl1);
		setting.getContentPane().add(chatlvl);
		setting.getContentPane().add(maxuser1);
		setting.getContentPane().add(maxuser);
		setting.getContentPane().add(exp1);
		setting.getContentPane().add(exp);
		setting.getContentPane().add(adena1);
		setting.getContentPane().add(adena);
		setting.getContentPane().add(item1);
		setting.getContentPane().add(item);
		setting.getContentPane().add(weightlimit1);
		setting.getContentPane().add(weightlimit);
		setting.getContentPane().add(lawful1);
		setting.getContentPane().add(lawful);
		setting.getContentPane().add(karma1);
		setting.getContentPane().add(karma);
		setting.getContentPane().add(enweapon1);
		setting.getContentPane().add(enweapon);
		setting.getContentPane().add(enarmor1);
		setting.getContentPane().add(enarmor);
		set.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				setMouseClicked(evt);
			}
		});
		setting.getContentPane().add(set);

		menu1.setIcon(in);
		menu1.setText("强制退出");
		menu1.setToolTipText("通过角色名强制退出");
		menu1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu1ActionPerformed(evt);
			}
		});
		popmenu1.add(menu1);

		menu2.setIcon(in);
		menu2.setText("封禁IP");
		menu2.setToolTipText("通过角色名封禁IP");
		menu2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu2ActionPerformed(evt);
			}
		});
		popmenu1.add(menu2);

		menu7.setIcon(in);
		menu7.setText("私聊");
		menu7.setToolTipText("与该角色私聊");
		menu7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu7ActionPerformed(evt);
			}
		});
		popmenu1.add(menu7);

		menu3.setIcon(in);
		menu3.setText("角色信息");
		menu3.setToolTipText("角色信息");
		menu3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu3ActionPerformed(evt);
			}
		});
		popmenu1.add(menu3);

		menu4.setIcon(in);
		menu4.setText("删除IP（列表）");
		menu4.setToolTipText("删除列表中封禁的IP");
		menu4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu4ActionPerformed(evt);
			}
		});
		popmenu2.add(menu4);

		menu5.setIcon(in);
		menu5.setText("删除IP（数据库，列表）");
		menu5.setToolTipText("删除数据库、列表中封禁的IP");
		menu5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu5ActionPerformed(evt);
			}
		});
		popmenu2.add(menu5);

		menu6.setIcon(in);
		menu6.setText("赠送礼物");
		menu6.setToolTipText("向该角色赠送礼物");
		menu6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu6ActionPerformed(evt);
			}
		});
		popmenu1.add(menu6);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("服务器管理器");
		setName("frame");

		// 这里

		first.setFont(new java.awt.Font("돋음", 1, 13));
		first.setText("OFF ");
		first.setForeground(Color.LIGHT_GRAY);
		first.setBounds(8, 4, 50, 50);
		layout1.add(first, javax.swing.JLayeredPane.DEFAULT_LAYER);

		st.setToolTipText("服务器启动");
		st.setContentAreaFilled(false);
		st.setMaximumSize(new java.awt.Dimension(21, 18));
		st.setMinimumSize(new java.awt.Dimension(21, 18));
		st.setPreferredSize(new java.awt.Dimension(21, 18));
		st.setRequestFocusEnabled(false);
		st.setSelected(true);
		st.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				stMouseClicked(evt);
			}
		});
		st.setBounds(61, 9, 60, 18);
		layout1.add(st, javax.swing.JLayeredPane.DEFAULT_LAYER);

		ex.setToolTipText("服务器退出");
		ex.setContentAreaFilled(false);
		ex.setMaximumSize(new java.awt.Dimension(21, 18));
		ex.setMinimumSize(new java.awt.Dimension(21, 18));
		ex.setPreferredSize(new java.awt.Dimension(21, 18));
		ex.setRequestFocusEnabled(false);
		ex.setSelected(true);
		ex.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				exMouseClicked(evt);
			}
		});
		ex.setBounds(133, 9, 60, 18);
		layout1.add(ex, javax.swing.JLayeredPane.DEFAULT_LAYER);

		sv.setText("重载");
		sv.setToolTipText("重载");
		sv.setContentAreaFilled(false);
		sv.setMargin(new java.awt.Insets(0, 0, 0, 0));
		sv.setMaximumSize(new java.awt.Dimension(21, 18));
		sv.setMinimumSize(new java.awt.Dimension(21, 18));
		sv.setPreferredSize(new java.awt.Dimension(21, 18));
		sv.setRequestFocusEnabled(false);
		sv.setSelected(true);
		sv.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				reloadMouseClicked(evt);
			}
		});
		sv.setBounds(70, 36, 50, 18);
		layout1.add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);

		buf.setText("活动");
		buf.setToolTipText("活动");
		buf.setContentAreaFilled(false);
		buf.setMargin(new java.awt.Insets(0, 0, 0, 0));
		buf.setMaximumSize(new java.awt.Dimension(21, 18));
		buf.setMinimumSize(new java.awt.Dimension(21, 18));
		buf.setPreferredSize(new java.awt.Dimension(21, 18));
		buf.setRequestFocusEnabled(false);
		buf.setSelected(true);
		buf.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				bufMouseClicked(evt);
			}
		});
		buf.setBounds(143, 36, 50, 18);
		layout1.add(buf, javax.swing.JLayeredPane.DEFAULT_LAYER);

		logsv.setText("设置");
		logsv.setToolTipText("服务器设置");
		logsv.setContentAreaFilled(false);
		logsv.setMargin(new java.awt.Insets(0, 0, 0, 0));
		logsv.setMaximumSize(new java.awt.Dimension(25, 18));
		logsv.setMinimumSize(new java.awt.Dimension(25, 18));
		logsv.setPreferredSize(new java.awt.Dimension(21, 18));
		logsv.setRequestFocusEnabled(false);
		logsv.setSelected(true);
		logsv.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				logsvMouseClicked(evt);
			}
		});
		logsv.setBounds(216, 36, 33, 18);
		layout1.add(logsv, javax.swing.JLayeredPane.DEFAULT_LAYER);

		logcl.setText("清除屏幕");
		logcl.setToolTipText("日志清除");
		logcl.setContentAreaFilled(false);
		logcl.setMargin(new java.awt.Insets(0, 0, 0, 0));
		logcl.setMaximumSize(new java.awt.Dimension(25, 18));
		logcl.setMinimumSize(new java.awt.Dimension(25, 18));
		logcl.setPreferredSize(new java.awt.Dimension(21, 18));
		logcl.setRequestFocusEnabled(false);
		logcl.setSelected(true);
		logcl.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				logclMouseClicked(evt);
			}
		});
		logcl.setBounds(270, 36, 60, 18);
		layout1.add(logcl, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label1.setFont(new Font("굴림", Font.PLAIN, 12));
		label1.setText("在线人数:");
		label1.setForeground(Color.black);
		label1.setToolTipText("所有玩家");
		label1.setBounds(456, 36, 40, 18);
		layout1.add(label1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label2.setFont(new Font("굴림", Font.PLAIN, 12));
		label2.setText(" " + count);
		label2.setForeground(Color.black);
		label2.setBounds(499, 36, 43, 18);
		layout1.add(label2, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label4.setFont(new Font("굴림", Font.PLAIN, 12));
		label4.setText("内存: 1024MB");
		label4.setForeground(Color.black);
		label4.setToolTipText("使用内存");
		label4.setBounds(544, 36, 101, 18);
		layout1.add(label4, javax.swing.JLayeredPane.DEFAULT_LAYER);

		lblThread.setFont(new Font("굴림", Font.PLAIN, 12));
		lblThread.setText("线程: 1000");
		lblThread.setToolTipText("使用线程数");
		lblThread.setForeground(Color.black);
		lblThread.setBounds(650, 36, 70, 18);
		layout1.add(lblThread, javax.swing.JLayeredPane.DEFAULT_LAYER);

		lblCPU.setFont(new Font("굴림", Font.PLAIN, 12));
		lblCPU.setText("CPU : 100%");
		lblCPU.setToolTipText("CPU使用率");
		lblCPU.setForeground(Color.black);
		lblCPU.setBounds(728, 36, 70, 18);
		layout1.add(lblCPU, javax.swing.JLayeredPane.DEFAULT_LAYER);

		iframe.setTitle("监控");
		iframe.setFrameIcon(chating);
		iframe.setVisible(true);

		txtGlobalChat.setColumns(20);
		txtGlobalChat.setEditable(false);
		txtGlobalChat.setRows(5);
		spGlobalChat.setViewportView(txtGlobalChat);
		tabframe.addTab("全体", spGlobalChat);

		txtClanChat.setColumns(20);
		txtClanChat.setEditable(false);
		txtClanChat.setRows(5);
		spClanChat.setViewportView(txtClanChat);
		tabframe.addTab("血盟", spClanChat);

		txtPartyChat.setColumns(20);
		txtPartyChat.setEditable(false);
		txtPartyChat.setRows(5);
		spPartyChat.setViewportView(txtPartyChat);
		tabframe.addTab("队伍", spPartyChat);

		txtWhisper.setColumns(20);
		txtWhisper.setEditable(false);
		txtWhisper.setRows(5);
		spWhisper.setViewportView(txtWhisper);
		tabframe.addTab("私聊", spWhisper);

		txtShop.setColumns(20);
		txtShop.setEditable(false);
		txtShop.setRows(5);
		spShop.setViewportView(txtShop);

		tabframe.addTab("商店", spShop);

		txtTrade.setColumns(20);
		txtTrade.setEditable(false);
		txtTrade.setRows(5);
		spTrade.setViewportView(txtTrade);
		tabframe.addTab("交易", spTrade);

		txtWarehouse.setColumns(20);
		txtWarehouse.setEditable(false);
		txtWarehouse.setRows(5);
		spWarehouse.setViewportView(txtWarehouse);
		tabframe.addTab("仓库", spWarehouse);

		txtEnchant.setColumns(20);
		txtEnchant.setEditable(false);
		txtEnchant.setRows(5);
		spEnchant.setViewportView(txtEnchant);
		tabframe.addTab("强化", spEnchant);

		txtPickup.setColumns(20);
		txtPickup.setEditable(false);
		txtPickup.setRows(5);
		spPickup.setViewportView(txtPickup);
		tabframe.addTab("日志", spPickup);

		javax.swing.GroupLayout iframeLayout = new javax.swing.GroupLayout(iframe.getContentPane());
		iframe.getContentPane().setLayout(iframeLayout);
		iframeLayout.setHorizontalGroup(iframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(iframeLayout.createSequentialGroup()
						.addComponent(tabframe, javax.swing.GroupLayout.PREFERRED_SIZE, 700,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		iframeLayout.setVerticalGroup(iframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(tabframe, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE));

		iframe.setBounds(0, 0, 700, 350);
		layout2.add(iframe, javax.swing.JLayeredPane.DEFAULT_LAYER);
		try {
			iframe.setMaximum(true);
		} catch (java.beans.PropertyVetoException e1) {
			e1.printStackTrace();
		}

		iframe2.setTitle("用户列表");
		iframe2.setFrameIcon(logo);
		iframe2.setVisible(true);

		userlist.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				userlistMouseClicked(evt);
			}
		});

		iplist.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				iplistMouseClicked(evt);
			}
		});
		iplist.setFont(new Font("滚动", 0, 11));

		spUserList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spUserList.setViewportView(userlist);
		userlist.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				userlistActionPerformed(evt);
			}
		});
//		java.awt.List userlist = new java.awt.List();
//		java.awt.List iplist = new java.awt.List();
		chocco.addTab("PC", so1, userlist);
		chocco.addTab("IP", so2, iplist);

		javax.swing.GroupLayout iframe2Layout = new javax.swing.GroupLayout(iframe2.getContentPane());
		iframe2.getContentPane().setLayout(iframe2Layout);
		iframe2Layout.setHorizontalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(iframe2Layout.createSequentialGroup()
						.addComponent(chocco, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		iframe2Layout.setVerticalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(chocco, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE));

		iframe2.setBounds(57, 0, 115, 340);
		layout3.add(iframe2, javax.swing.JLayeredPane.DEFAULT_LAYER);
		try {
			iframe2.setMaximum(true);
		} catch (java.beans.PropertyVetoException e1) {
			e1.printStackTrace();
		}
//		try {
//		} catch (PropertyVetoException e) {
//			e.printStackTrace();
//		}

		name.setBounds(0, 0, 80, 21);
		layout4.add(name, javax.swing.JLayeredPane.DEFAULT_LAYER);

		chat.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				chatKeyPressed(evt);
			}
		});
		chat.setBounds(80, 0, 600, 21);
		layout4.add(chat, javax.swing.JLayeredPane.DEFAULT_LAYER);

		stime.setFont(new java.awt.Font("滚动", 1, 12));
		stime.setText("");
		stime.setBounds(480, 0, 180, 20);
		layout4.add(stime, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(layout4, Alignment.LEADING)
						.addComponent(layout1, Alignment.LEADING)
						.addGroup(Alignment.LEADING, layout.createSequentialGroup()
							.addComponent(layout2, GroupLayout.PREFERRED_SIZE, 680, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(layout3, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(69, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(layout1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(layout3, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
						.addComponent(layout2, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(layout4, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		getContentPane().setLayout(layout);

		pack();
		new Thread(new PerformReader()).start();
	}
	/**
	 * 管理各个事件的地方
	 */

	/** 服务器启动事件 */
//	private void stMouseClicked(java.awt.event.MouseEvent evt) {
//	    if (!serverstart) {
//	        MJNodimShield mjNodimshield = new MJNodimShield(this, true);
//	        mjNodimshield.setVisible(true);
//	        first.setText("ON");
//	        first.setForeground(Color.GREEN);
//	        try {
//	            new Thread(new Runnable() {
//	                @Override
//	                public void run() {
//	                    new Server();
//	                    new ServerStart(evt.getXOnScreen(), evt.getYOnScreen());
//	                }
//	            }).start();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        serverstart = true;
//	    } else {
//	        JOptionPane.showMessageDialog(this, "이미 서버가 실행중입니다.", "Server Message", JOptionPane.ERROR_MESSAGE);
//	    }
//	}

	/** 服务器启动事件 */
	private void stMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果服务器未运行... */
		if (!serverstart) {
			first.setText("ON");
			first.setForeground(Color.GREEN);
			try {
				new Thread(new Runnable() {
					@Override
					public void run() {
						new Server();
						new ServerStart(evt.getXOnScreen(), evt.getYOnScreen());
					}
				}).start();
			} catch (Exception e) {
			}
			serverstart = true;
			/** 如果服务器已经在运行 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器已在运行中.", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 服务器关闭事件 */
	private void exMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			/**
			 * 保存各角色和服务器数据... 编码以确保安全关闭
			 */
			int a = JOptionPane.showConfirmDialog(this, "确定要退出吗 ?", "Server Message", 2,
					JOptionPane.INFORMATION_MESSAGE);
			if (a == JOptionPane.YES_OPTION) {
				GameServer.getInstance().shutdownWithCountdown(0);
			}

		} else {
			System.exit(0);
		}
	}

	public static boolean _isManagerCommands = false;

	private void reloadMouseClicked(java.awt.event.MouseEvent evt) {
		if (serverstart) {
			if (!_isManagerCommands) {
				_isManagerCommands = true;
				new ManagerCommands(evt.getXOnScreen(), evt.getYOnScreen());
			} else
				MJMessageBox.show(this, "窗口已激活.", false);
		} else
			MJMessageBox.show(this, "服务器未在运行中.", false);
	}

	/** 活动  */
	private void bufMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			/** 处理各种活动 */
			Object smallList[] = { "全体增益", "兔子赛跑", "无限大战", "全体禁言", "解除禁言" };
			String value = (String) javax.swing.JOptionPane.showInputDialog(this, "要开始活动吗?", " Server Message",
					JOptionPane.QUESTION_MESSAGE, null, smallList, smallList[0]);
			if (value != null) {
				// javax.swing.JOptionPane.showMessageDialog(this, "준비중인 서비스
				// 입니다..", " Server Message",
				// javax.swing.JOptionPane.INFORMATION_MESSAGE);
				if (value == "兔子赛跑") {
					GameServerSetting _GameServerSetting = GameServerSetting.getInstance();
//					 _GameServerSetting.BugRaceRestart = true;
				} else if (value == "全体增益") {
					int[] allBuffSkill = { 26, 42, 48, 57, 68, 79, 158, 163, 168 };
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
					}
				} else if (value == "全体禁言") {
					L1World.getInstance().set_worldChatElabled(false);
					txtGlobalChat.append("全体禁言已执行");
				} else if (value == "解除禁言") {
					L1World.getInstance().set_worldChatElabled(true);
					txtGlobalChat.append("解除禁言已执行");
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "准备中的服务..", " Server Message",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动.", " Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 服务器设置事件 */
	private void logsvMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			chatlvl.setText("" + Config.GLOBAL_CHAT_LEVEL);
			maxuser.setText("" + Config.MAX_ONLINE_USERS);
			exp.setText("" + Config.RATE_XP);
			adena.setText("" + Config.RATE_DROP_ADENA);
			item.setText("" + Config.RATE_DROP_ITEMS);
			weightlimit.setText("" + Config.RATE_WEIGHT_LIMIT);
			lawful.setText("" + Config.RATE_LAWFUL);
			karma.setText("" + Config.RATE_KARMA);
			enweapon.setText("" + Config.ENCHANT_CHANCE_WEAPON);
			enarmor.setText("" + Config.ENCHANT_CHANCE_ARMOR);

			setting.getContentPane().setLayout(new FlowLayout());
			setting.setSize(340, 200);// 修改部分
			setting.setLocation(250, 250);
			setting.setResizable(false);
			setting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setting.setVisible(true);
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动.", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 服务器日志清除事件 */
	private void logclMouseClicked(java.awt.event.MouseEvent evt) {
		if (serverstart) {
			try {
				LoggerInstance.getInstance().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动.", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 服务器设置事件 */
	private void setMouseClicked(java.awt.event.MouseEvent evt) {
		int chatlevel = Integer.parseInt(chatlvl.getText());
		short chatlevel2 = (short) chatlevel;
		Config.GLOBAL_CHAT_LEVEL = chatlevel2;
		int Max = Integer.parseInt(maxuser.getText());
		short Max2 = (short) Max;
		Config.MAX_ONLINE_USERS = Max2;
		Float Exprate = Float.parseFloat(exp.getText());
		double Exprate2 = (double) Exprate;
		Config.RATE_XP = Exprate2;
		Float Aden = Float.parseFloat(adena.getText());
		double Aden2 = (double) Aden;
		Config.RATE_DROP_ADENA = Aden2;
		Float Droprate = Float.parseFloat(item.getText());
		double Droprate2 = (double) Droprate;
		Config.RATE_DROP_ITEMS = Droprate2;
		Float weight = Float.parseFloat(weightlimit.getText());
		double weight2 = (double) weight;
		Config.RATE_WEIGHT_LIMIT = weight2;
		Float lawfulrate = Float.parseFloat(lawful.getText());
		double lawful2 = (double) lawfulrate;
		Config.RATE_LAWFUL = lawful2;
		Float karmarate = Float.parseFloat(karma.getText());
		double karma2 = (double) karmarate;
		Config.RATE_KARMA = karma2;
		int armor = Integer.parseInt(enarmor.getText());
		Config.ENCHANT_CHANCE_ARMOR = armor;
		int enchant = Integer.parseInt(enweapon.getText());
		Config.ENCHANT_CHANCE_WEAPON = enchant;
		txtGlobalChat.append("\n          [  服务器设置  ]");
		txtGlobalChat.append("\n    [设置] 聊天等级 : " + Config.GLOBAL_CHAT_LEVEL);
		txtGlobalChat.append("\n    [设置] 最大在线用户数 : " + Config.MAX_ONLINE_USERS);
		txtGlobalChat.append("\n    [设置] 经验倍率 : " + Exprate2 + " - 金币  : " + Aden2 + " - 物品  : " + Droprate2);
		txtGlobalChat.append(
				"\n    [设置] 负重上限 : " + weight2 + " - 正义值倍率 : " + lawful2 + " - 好友度倍率 : " + karma2);
		txtGlobalChat.append("\n    [设置] [强化] 武器 : " + enchant + " - 防具 : " + armor);
		txtGlobalChat.append("\n       [  服务器设置完成 ]");
		setting.setVisible(false);
	}

	/** 服务器聊天事件 */
	private void chatKeyPressed(java.awt.event.KeyEvent evt) {
			/** 如果按下的键是Enter键 */
			if (evt.getKeyCode() == evt.VK_ENTER) {
				if (serverstart) {
					/** 如果有聊天内容... */
					if (!chat.getText().equalsIgnoreCase("")) {
						/** 如果未指定特定角色名...即为普通聊天 */
						if (name.getText().equalsIgnoreCase("")) {
							/** 发送到全体聊天区域 */
							txtTrade.append("\\aD[******]  :  " + chat.getText());
							/** 向所有在线玩家发送聊天消息 */
							L1World world = L1World.getInstance();
							world.broadcastServerMessage("\\aD[******] " + chat.getText());
						} else {
							/** 在游戏世界中找到指定角色，并向其发送消息 */
							L1PcInstance pc = L1World.getInstance().getPlayer(name.getText());
							if (pc != null) {
								/** 添加到密语聊天区域 */
								txtWhisper.append("\n[******] -> [" + name.getText() + "]  :  " + chat.getText());
								pc.sendPackets(new S_SystemMessage("[******] -> " + chat.getText()));
							} else {
								javax.swing.JOptionPane.showMessageDialog(this,
										name.getText() + " 在游戏中不存在.", "Server Message",
										javax.swing.JOptionPane.ERROR_MESSAGE);
							}
						}
						chat.setText("");
					}
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动.", "Server Message",
							javax.swing.JOptionPane.ERROR_MESSAGE);
					chat.setText("");
				}
			}

		}

	/** 使能够指定特定角色名 -> 暂时通过角色列表窗口选择 -> 与chat文本字段关联 */
	private void userlistActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果鼠标指向的事件值...即有返回的值...以防万一 */
		if (!evt.getActionCommand().equalsIgnoreCase("")) {
			/** 设置name字段 */
			name.setText(evt.getActionCommand());
		}
	}

	/** 角色列表弹出菜单 */
	private void userlistMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == evt.BUTTON3) {
			popmenu1.show(userlist, evt.getX(), evt.getY());
		}
	}

	/** IP列表弹出菜单 */
	private void iplistMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == evt.BUTTON3) {
			popmenu2.show(iplist, evt.getX(), evt.getY());
		}
	}

	/** 弹出菜单 - 强制踢出个人 */
	private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			if ((!userlist.getSelectedItem().equalsIgnoreCase("")) && (userlist.getSelectedItem() != null)) {
				try {
					L1PcInstance players = L1World.getInstance().getPlayer(userlist.getSelectedItem());
					if (players != null) {
						GameServer.disconnectChar(userlist.getSelectedItem());
						javax.swing.JOptionPane.showMessageDialog(this, "已强制踢出 " + players.getName() + ".",
								"Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
						userlist.remove(userlist.getSelectedItem());
					} else {
						javax.swing.JOptionPane.showMessageDialog(this,
								userlist.getSelectedItem() + " 在游戏中不存在.", "Server Message",
								javax.swing.JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					javax.swing.JOptionPane.showMessageDialog(this, "强制踢出 " + userlist.getSelectedItem() + " 失败.",
							"Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
				} finally {
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "未指定角色名.", "Server Message",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动.", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 弹出菜单 - 封禁个人IP */
	private void menu2ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			if ((!userlist.getSelectedItem().equalsIgnoreCase("")) && (userlist.getSelectedItem() != null)) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					if (pc.getNetConnection() != null) {
						IpTable.getInstance().banIp(pc.getNetConnection().getIp());
						iplist.add(pc.getNetConnection().getIp());
					}
					GameServer.disconnectChar(pc);
					userlist.remove(userlist.getSelectedItem());
					javax.swing.JOptionPane.showMessageDialog(this, "已强制踢出并封禁 " + pc.getName() + "。",
							"Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
				} else {
					javax.swing.JOptionPane.showMessageDialog(this,
							userlist.getSelectedItem() + " 在游戏中不存在。", " Server Message",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}


	/** 弹出菜单 - 个人角色信息 */
	private void menu3ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			if (!inf) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					new infomation(pc);
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "已经在运行中。", "Server Message",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 弹出菜单 - 删除封禁IP列表 */
	private void menu4ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			iplist.remove(iplist.getSelectedItem());
			javax.swing.JOptionPane.showMessageDialog(this, "已删除封禁列表(List): " + iplist.getSelectedItem(),
					"Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 弹出菜单 - 删除封禁IP数据库 */
	private void menu5ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			IpTable.getInstance().liftBanIp(iplist.getSelectedItem());
			iplist.remove(iplist.getSelectedItem());
			javax.swing.JOptionPane.showMessageDialog(this, "已删除封禁列表(dB, List): " + iplist.getSelectedItem(),
					"Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 弹出菜单 - 密语 */
	private void menu7ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			if (!inf) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					name.setText(pc.getName());
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "已经在运行中。", "Server Message",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 弹出菜单 - 赠送礼物 */
	private void menu6ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果服务器正在运行 */
		if (serverstart) {
			if (!inf) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					new Give(pc);
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "已经在运行中。", "Server Message",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果服务器未在运行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "服务器未启动。", "Server Message",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String args[]) {
		/** 使用线程处理事件查询。 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				Config.load();
				GrangKinConfig.load();
				new chocco().setVisible(true);
			}
		});
	}


	private static final double perToheight = 3.3D;
	public static long cpu;

	class PerformReader implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					// TODO 管理器同步时间
					Thread.sleep(500L);
					long mem = SystemUtil.getUsedMemoryMB();
					int thread = Thread.activeCount();
					cpu = (int) (getUseCpu() * 100D);
					label4.setText(String.format("内存: %d MB", mem));
					lblThread.setText(String.format("线程: %d", thread));
					lblCPU.setText(String.format("CPU: %d%%", cpu));
//					lblCPUProgressBack.setBounds(0, 0, 50, 330 - (int) (cpu * perToheight));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private double getUseCpu() {
			return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
		}
	}
}
/**
 * 今后有关活动、用户禁言、封禁、踢出、变身、物品等活动相关的内容，将通过弹出菜单或小按钮形式的独立组件提供，在不偏离现有界面的范围内制作。
 */

