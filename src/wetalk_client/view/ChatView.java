package test2;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class GUI extends JFrame {

	private JPanel contentPane;
	//换色名单
	private HashMap<Integer,Color> cellList = new HashMap<Integer,Color>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//以下为任务栏
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Actions");
		menuBar.add(mnNewMenu);
		
		JButton moreOption = new JButton("moreOption");
		moreOption.setBorder(BorderFactory.createCompoundBorder());
		mnNewMenu.add(moreOption);
		
		
		//以下为左边
		JPanel panelLeft = new JPanel();
		panelLeft.setBounds(0, 0, 750, 738);
		contentPane.add(panelLeft);
		panelLeft.setLayout(null);
		
		//打字框
		JTextArea textField = new JTextArea();
		textField.setBounds(0, 600, 750, 128);
		textField.setFont(new Font("Calibri", Font.BOLD, 15));
		textField.setBorder(BorderFactory.createLoweredBevelBorder());
		panelLeft.add(textField);
		textField.setColumns(10);
		
		//显示框
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 45, 750, 550);
		textArea.setEditable(false);
		textArea.setText("\n"
				+ "                                         WeTalk \n"
				+ "  - Tap the friend botton to add or delete friend\n"
				+ "  - The friend status would show up on your friend list\n"
				+ "  - Friend list is located on the left of homepage\n"
				+ "  - To chat with your friends, you need click the name\n"
				+ "  - The top of the chatbox will shows your friend's name\n"
				+ "  - The middle part of the chatbox is the chat history\n"
				+ "  - Enter your message at the bottom of the chatbox\n"
				+ "  - Press enter to send, if you recive any message\n"
				+ "  - Message will show on chatbox or remind you in red\n");
		textArea.setFont(new Font("Calibri",Font.PLAIN,18));
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		panelLeft.add(textArea);
		
		//好友名称
		JTextField txtTitle = new JTextField();
		txtTitle.setEditable(false);
		txtTitle.setHorizontalAlignment(SwingConstants.CENTER);
		txtTitle.setText("  Chatting with user: hhh");
		txtTitle.setFont(new Font("Calibri",Font.CENTER_BASELINE,20));
		txtTitle.setBackground(Color.WHITE);
		txtTitle.setBounds(0, 0, 750, 40);
		txtTitle.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelLeft.add(txtTitle);
		txtTitle.setColumns(10);
		
		//以下为右边
		JPanel panelRight = new JPanel();
		panelRight.setBounds(750, 0, 235, 740);
		contentPane.add(panelRight);
		panelRight.setLayout(null);
		
		//发送键
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(new Font("Calibri", Font.BOLD, 18));
		btnNewButton.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(10, 700, 220, 30);
		panelRight.add(btnNewButton);
		
		//好友栏
		JList<String> list = new JList<String>();
		list.setBackground(SystemColor.control);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setValueIsAdjusting(true);
		list.setBounds(10, 0, 220, 690);
		String[] arrString = {"usernmae: hhh","usernmae: test2"};
		for(int i = 0; i < arrString.length; i++) {
			cellList.put(i, Color.WHITE); //好友变白
		}
		list.setCellRenderer(new MyRenderer());
		list.setListData(arrString);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelRight.add(list);
		
		
	}

	//换色器
	private class MyRenderer extends DefaultListCellRenderer{
		private static final long serialVersionUID = 3L;
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			for(int row : cellList.keySet()) {
				if (index == row) {
					setBackground(cellList.get(row));
				}
			}
			return this;
		}
	}
}
