/**
 *
 * @author user
 */

package l1j.server.swing;

import javax.swing.JOptionPane;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class Give extends javax.swing.JFrame {

	private L1PcInstance _pc;
	private JOptionPane jop = new JOptionPane();

	/** Creates new form Give */
	public Give(L1PcInstance pcc) {
		_pc = pcc;

		initComponents();        
		this.setSize(221,this.getHeight()); // 274
		this.setLocation(300, 300);
		this.setResizable(false);
		this.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	// 
	private void initComponents() {

		PcName = new javax.swing.JLabel();
		ItemNum1 = new javax.swing.JLabel();
		ItemCount1 = new javax.swing.JLabel();
		ItemNum = new javax.swing.JTextField();
		ItemCount = new javax.swing.JTextField();
		ItemEn1 = new javax.swing.JLabel();
		ItemEn = new javax.swing.JTextField();
		GiveOk = new javax.swing.JButton();
		GiveCancel = new javax.swing.JButton();

		setTitle("赠送物品");

		PcName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		PcName.setText(_pc.getName());

		ItemNum1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ItemNum1.setText("物品编号");

		ItemCount1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ItemCount1.setText("物品数量");

		ItemEn1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ItemEn1.setText("道具强化");
		ItemEn.setText("0");
		
		GiveOk.setText("赠送");
		GiveOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GiveOkActionPerformed(evt);
			}
		});

		GiveCancel.setText("取消");
		GiveCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GiveCancelActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
				.addGap(28, 28, 28)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
				.addComponent(ItemNum1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(ItemCount1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(ItemEn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(GiveOk, javax.swing.GroupLayout.Alignment.TRAILING))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
				.addComponent(ItemEn, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
				.addComponent(ItemCount, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
				.addComponent(ItemNum, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
				.addComponent(GiveCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
				.addGap(24, 24, 24))
				.addGroup(layout.createSequentialGroup()
				.addGap(76, 76, 76)
				.addComponent(PcName, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(78, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(PcName)
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(ItemNum1)
				.addComponent(ItemNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(ItemCount1)
				.addComponent(ItemCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(ItemEn1)
				.addComponent(ItemEn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				.addComponent(GiveOk)
				.addComponent(GiveCancel))
				.addContainerGap(22, Short.MAX_VALUE))
		);
		pack();
	}

//	private void GiveOkActionPerformed(java.awt.event.ActionEvent evt) {
//
//		int itemid = Integer.parseInt(ItemNum.getText());		
//		int count = Integer.parseInt(ItemCount.getText());
//		int enchant = Integer.parseInt(ItemEn.getText());		
//		String name2 = PcName.getText();		
//		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//			if(pc.getName().equalsIgnoreCase(name2)){					   
//				L1Item temp = ItemTable.getInstance().getTemplate(itemid);
//				if (temp != null) {
//					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
//					item.setEnchantLevel(enchant);
//					item.setCount(count);					
//					item.setIdentified(true);
//					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
//						pc.getInventory().storeItem(item);												
//						if(item.getItem().getType2() == 1 || item.getItem().getType2() == 2){
//							pc.sendPackets(new S_SystemMessage("管理员已创建 +"+ enchant + " " + temp.getName() + " 。"));
//							jop.showMessageDialog(this, name2+" : +"+enchant+" "+temp.getName());
//						}else{
//							pc.sendPackets(new S_SystemMessage("管理员已创建 " + temp.getName() + "(" + count + ") 。"));
//							jop.showMessageDialog(this, name2+" : "+temp.getName()+" "+count+"个");
//						}
//					} else {
//						L1ItemInstance item1 = null;
//						int createCount;
//						for (createCount = 0; createCount < count; createCount++) {
//							item1 = ItemTable.getInstance().createItem(itemid);
//							item1.setEnchantLevel(enchant);
//							if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
//								pc.getInventory().storeItem(item);
//							} else
//								break;
//						}
//						if (createCount > 0) {
//							pc.sendPackets(new S_SystemMessage("管理员已创建 " + temp.getName()+ " " + createCount + "个。"));
//							jop.showMessageDialog(this, name2+" : "+temp.getName()+" "+count+"个");
//						}
//					}
//					jop.showMessageDialog(this, "赠送完成");					
//					this.setVisible(false);
//				} else if (temp == null) {
//					jop.showMessageDialog(this, "失败：物品编号错误");
//				}		  
//			} else { 
//				if (!pc.getName().equalsIgnoreCase(name2)) {
//				}
//				jop.showMessageDialog(this, "失败：该角色未在线");
//			}
//		}
//	}
	
	private void GiveOkActionPerformed(java.awt.event.ActionEvent evt) {
	    int itemid = Integer.parseInt(ItemNum.getText());        
	    int count = Integer.parseInt(ItemCount.getText());
	    int enchant = Integer.parseInt(ItemEn.getText());        
	    String name2 = PcName.getText();        
	    L1PcInstance targetPc = null;

	    for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
	        if (pc.getName().equalsIgnoreCase(name2)) {
	            targetPc = pc;
	            break;
	        }
	    }

	    if (targetPc == null) {
	        jop.showMessageDialog(this, "失败：该角色未在线");
	        return;
	    }

	    L1Item temp = ItemTable.getInstance().getTemplate(itemid);
	    if (temp != null) {
	        L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
	        item.setEnchantLevel(enchant);
	        item.setCount(count);                    
	        item.setIdentified(true);
	        if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
	            targetPc.getInventory().storeItem(item);                                                
	            if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) {
	                targetPc.sendPackets(new S_SystemMessage("管理员已创建 +" + enchant + " " + temp.getName() + " 。"));
	                jop.showMessageDialog(this, name2 + " : +" + enchant + " " + temp.getName());
	            } else {
	                targetPc.sendPackets(new S_SystemMessage("管理员已创建 " + temp.getName() + "(" + count + ") 。"));
	                jop.showMessageDialog(this, name2 + " : " + temp.getName() + " " + count + "个");
	            }
	        } else {
	            L1ItemInstance item1 = null;
	            int createCount;
	            for (createCount = 0; createCount < count; createCount++) {
	                item1 = ItemTable.getInstance().createItem(itemid);
	                item1.setEnchantLevel(enchant);
	                if (targetPc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
	                    targetPc.getInventory().storeItem(item);
	                } else {
	                    break;
	                }
	            }
	            if (createCount > 0) {
	                targetPc.sendPackets(new S_SystemMessage("管理员已创建 " + temp.getName() + " " + createCount + "个。"));
	                jop.showMessageDialog(this, name2 + " : " + temp.getName() + " " + count + "个");
	            }
	        }
	        jop.showMessageDialog(this, "赠送完成");                    
	        this.setVisible(false);
	    } else {
	        jop.showMessageDialog(this, "失败：物品编号错误");
	    }
	}


	private void GiveCancelActionPerformed(java.awt.event.ActionEvent evt) {		
		this.setVisible(false);
	}

	private javax.swing.JButton GiveCancel;
	private javax.swing.JButton GiveOk;
	private javax.swing.JTextField ItemCount;
	private javax.swing.JLabel ItemCount1;
	private javax.swing.JTextField ItemEn;
	private javax.swing.JLabel ItemEn1;
	private javax.swing.JTextField ItemNum;
	private javax.swing.JLabel ItemNum1;
	private javax.swing.JLabel PcName;
}
