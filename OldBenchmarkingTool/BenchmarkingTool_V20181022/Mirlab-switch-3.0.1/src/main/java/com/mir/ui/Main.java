package com.mir.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;

/**
 * 
 * @author 3.0.1 Lee Gilho
 *
 */

public class Main {

	private JFrame frmMirlabBenchmark;

	public static JTextArea S_LOG_TEXT;
	public static JTextArea S_TOPO_TEXT;
	public static JTextArea S_TOPOLOGY_TEXT;

	public static JComboBox S_comboBoxTestType = new JComboBox();

	private JTextField S_controller2IpPort;
	public static JTextField S_controller1IpPort;
	public static JList S_metricList;
	private JScrollPane S_scrollPane_2;
	private JTabbedPane S_tabbedPane;
	public static JProgressBar S_progressBar;
	private JTextField S_controller3IpPort;
	private JComboBox S_comboBoxControllerMode;
	private JComboBox S_comboBoxControllerType;
	public static JTable S_RESULT_TABLE;
	public static JButton S_startButton;
	public static JLabel S_benchmarkCondition;
	public static JProgressBar S_progressBarTotal;
	private JTextField S_numberOfS;
	private JTextField S_hostIP_1;
	private JTextField S_hostIP_2;
	private JTextField S_textField_3;
	private JTextField S_textField_4;
	private JComboBox S_comboBoxOFProtocolType;
	private JComboBox S_comboBoxTopoType;
	private JComboBox S_packetComboBox;
	private JComboBox S_provisioningComboBox;

	/*************************************************/

	public static JTextArea N_LOG_TEXT;
	public static JTextArea N_TOPO_TEXT;
	public static JTextArea N_TOPOLOGY_TEXT;

	public static JComboBox N_comboBoxTestType = new JComboBox();

	private JTextField N_controller2IpPort;
	private JTextField N_controller1IpPort;
	public static JList N_metricList;
	private JScrollPane N_scrollPane_2;
	private JTabbedPane N_tabbedPane;
	public static JProgressBar N_progressBar;
	private JTextField N_controller3IpPort;
	private JComboBox N_comboBoxControllerMode;
	private JComboBox N_comboBoxControllerType;
	public static JTable N_RESULT_TABLE;
	public static JButton N_startButton;
	public static JLabel N_benchmarkCondition;
	public static JProgressBar N_progressBarTotal;
	private JTextField N_numberOfS;
	private JTextField N_hostIP_1;
	private JTextField N_hostIP_2;
	private JTextField N_textField_3;
	private JTextField N_textField_4;
	private JComboBox N_comboBoxOFProtocolType;
	private JComboBox N_comboBoxTopoType;
	private JComboBox N_packetComboBox;
	private JComboBox N_provisioningComboBox;

	/*************************************************/
	/**
	 * SN_bound part , author : Leegilho
	 */

	public static JTextArea SN_LOG_TEXT;
	public static JTextArea SN_TOPO_TEXT;
	public static JTextArea SN_TOPOLOGY_TEXT;

	public static JComboBox SN_comboBoxTestType = new JComboBox();

	private JTextField SN_controller2IpPort;
	private JTextField SN_controller1IpPort;
	public static JList SN_metricList;
	private JScrollPane SN_scrollPane_2;
	private JTabbedPane SN_tabbedPane;
	public static JProgressBar SN_progressBar;
	private JTextField SN_controller3IpPort;
	private JComboBox SN_comboBoxControllerMode;
	private JComboBox SN_comboBoxControllerType;
	public static JTable SN_RESULT_TABLE;
	public static JButton SN_startButton;
	public static JLabel SN_benchmarkCondition;
	public static JProgressBar SN_progressBarTotal;
	private JTextField SN_numberOfS;
	private JTextField SN_hostIP_1;
	private JTextField SN_hostIP_2;
	private JTextField SN_textField_3;
	private JTextField SN_textField_4;
	private JComboBox SN_comboBoxOFProtocolType;
	private JComboBox SN_comboBoxTopoType;
	private JComboBox SN_packetComboBox;
	private JComboBox SN_provisioningComboBox;

	/*************************************************/

	public static DistributedFrame dr = new DistributedFrame();
	public static ConfigurationFrame cf;
	public static FileReader reader; // to read files

	/**
	 * Launch the application.主函数
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					cf = new ConfigurationFrame();
					cf.frmConfiguration.setVisible(false);
					window.frmMirlabBenchmark.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame. 系统自带
	 */
	private void initialize() {
		frmMirlabBenchmark = new JFrame();
		frmMirlabBenchmark.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmMirlabBenchmark.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmMirlabBenchmark.setTitle("Mir-Lab SDN Benchmark v2.0.0");
		frmMirlabBenchmark.setResizable(false);
		frmMirlabBenchmark.setBounds(new Rectangle(50, 50, 50, 50));
		frmMirlabBenchmark.setBounds(100, 100, 1080, 768);
		frmMirlabBenchmark.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMirlabBenchmark.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1074, 31);
		frmMirlabBenchmark.getContentPane().add(menuBar);
		menuBar.setToolTipText("");

		JMenu fileMB = new JMenu("File");
		fileMB.setIcon(new ImageIcon(Main.class.getResource("/com/alee/examples/groups/filechooser/icons/folder.png")));
		menuBar.add(fileMB);

		JMenuItem newMI = new JMenuItem("New");
		newMI.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/filechooser/icons/file_icon.png")));
		fileMB.add(newMI);

		JMenuItem openMI = new JMenuItem("Open");
		openMI.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/ninepatch/icons/open.png")));
		fileMB.add(openMI);

		JMenuItem saveMI = new JMenuItem("Save As...");
		saveMI.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/ninepatch/icons/saveas.png")));
		fileMB.add(saveMI);

		JMenu windowMB = new JMenu("Configuration");
		windowMB.setIcon(new ImageIcon(Main.class.getResource("/com/alee/examples/groups/window/icons/window.png")));
		menuBar.add(windowMB);

		JMenuItem reportWindowMI = new JMenuItem("Distributed Benchmark");
		reportWindowMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				dr.frmDistrubutedBenchmarkReport.setVisible(true);
			}
		});

		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cf.frmConfiguration.setVisible(true);

			}
		});
		mntmConfiguration.setIcon(new ImageIcon(Main.class.getResource("/com/alee/utils/icons/selection/gripper.png")));
		windowMB.add(mntmConfiguration);
		reportWindowMI
				.setIcon(new ImageIcon(Main.class.getResource("/com/alee/examples/groups/table/icons/table.png")));
		windowMB.add(reportWindowMI);

		// by Joon .. judged in line 145

		JMenuItem OpenTopology = new JMenuItem("OpenTopology");
		OpenTopology.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\Cloud\\workspace\\Mirlab-switch-2.0.1\\mininet");
				if (chooser.showOpenDialog(frmMirlabBenchmark) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						reader = new FileReader(file.getAbsolutePath());
						System.out.println(file.getName() + " is opened");
						if (file.getName().contains("mn") == true) {
							Topo_import topology = new Topo_import(reader);
							Global.JSONMSG = topology.JSONMSG;
							S_numberOfS.setText(Integer.toString(topology.JSONMSG.getJSONArray("switches").length()));
							N_numberOfS.setText(Integer.toString(topology.JSONMSG.getJSONArray("switches").length()));
							topology.JSONMSG.getJSONArray("switches").length();
							S_comboBoxTopoType.setSelectedIndex(2);
							N_comboBoxTopoType.setSelectedIndex(2);
						} else {
							System.out.println("wrong file");
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		OpenTopology.setIcon(new ImageIcon(Main.class.getResource("/com/alee/utils/icons/selection/gripper.png")));
		windowMB.add(OpenTopology);
		reportWindowMI
				.setIcon(new ImageIcon(Main.class.getResource("/com/alee/examples/groups/table/icons/table.png")));
		windowMB.add(OpenTopology);

		// this is for opening the saved mininet topology

		JMenu helpMB = new JMenu("Help");
		helpMB.setIcon(
				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/transition/icons/toolbar/2.png")));
		menuBar.add(helpMB);

		JMenuItem aboutMI = new JMenuItem("About");
		aboutMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Hanyang MIR LAB.\nSDN Benchmark v1.7.8.5", "MIR LAB.",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		aboutMI.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/ninepatch/icons/icon.png")));
		helpMB.add(aboutMI);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 37, 1054, 695);
		frmMirlabBenchmark.getContentPane().add(tabbedPane_1);

		JPanel southbound = new JPanel();
		southbound.setLayout(null);
		tabbedPane_1.addTab("southbound", null, southbound, null);

		JPanel northbound = new JPanel();
		northbound.setLayout(null);
		tabbedPane_1.addTab("northbound", null, northbound, null);

		JPanel SNbound = new JPanel();
		SNbound.setLayout(null);
		tabbedPane_1.addTab("SNbound", null, SNbound, null);

		sounthbound(southbound);
		northbound(northbound);

		//
		SN_Gui sn = new SN_Gui();
		sn.snbound(SNbound);
	}

	private void sounthbound(JPanel southbound) {
		JScrollPane metricsPane = new JScrollPane();
		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		metricsPane.setAutoscrolls(true);
		metricsPane.setBounds(41, 32, 242, 299);
		// frmMirlabBenchmark.getContentPane().add(scrollPane);
		southbound.add(metricsPane);

		S_metricList = new JList();
		S_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
		S_metricList.setAutoscrolls(false);
		metricsPane.setViewportView(S_metricList);
		S_metricList.setModel(new AbstractListModel() {
			String[] values = new String[] { "1.Topology Discovery Time",
					"2.Topology Change Detection Time(Link Down/Up)", "3.Asynchronous Message Processing Time",
					"4.Asynchronous Message Processing Rate", "5.Reactive Path Provisioning Time",
					"6.Reactive Path Provisioning Rate", "7.*Proactive Path Provisioning Time",
					"8.*Proactive Path Provisioning Rate", "9. Test (Metric 1~6)",
					"---------------SDN controller should be reboot to test metric 10~16",
					"10.Control Session Capacity: CCn", "11.Network Discovery Size: Ns",
					"12.Forwarding Table Capacity: Nrp", "13.*Exception Handling : Security",
					"14.*DoS attacks: Security", "15.*Controller Failover Time: Reliability",
					"16.*Network Re-provisioning Time: Reliability Node failure vs. Link Failure", };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		S_metricList.setSelectedIndex(0);

		S_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));

		// 关于run按钮的代码
		S_startButton = new JButton("Run");
		S_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 按键之后开始實行下列代碼
				Main.S_startButton.setEnabled(false);// run按钮不可按
				Main.S_metricList.setEnabled(false);// metric 栏不可选择

				// metric index，获得metric的选择值并赋值给global变数
				Global.TEST_METRIC = S_metricList.getSelectedIndex();

				// --Benchmark configuration 1
				// OF Version
				Global.OPENFLOW_VERSION = Global.OF_V_3;
				// -- Benchmark configuration 2
				// topology type 圆形或者直线
				Global.TOPO_TYPE = S_comboBoxTopoType.getSelectedIndex();
				// --Benchmark configuration 3
				// node # node数量
				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) S_numberOfS.getText().toString());

				// test time
				Global.TEST_TIME = 5;

				// Benchmark configuration 4
				// Host Ip
				Global.HOST_IP[0] = Integer.parseInt((String) S_hostIP_1.getText().toString());
				Global.HOST_IP[1] = Integer.parseInt((String) S_hostIP_2.getText().toString());

				// SDN controller configuration 一
				// Controller Mode （Standalone， Cluster）
				Global.CONTROLLER_MODE = S_comboBoxControllerMode.getSelectedIndex();
				if (Global.CONTROLLER_MODE == Global.CONTROLLER_MODE_STANDALONE) {
					// standalone mode 完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(S_controller1IpPort.getText().toString(), ":");// 分词器，分离ip和port
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 1000;
					// Global.BUFF_SIZE = 1000000;
				} else {
					// cluster mode 未完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(S_controller1IpPort.getText().toString(), ":");
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 200;
					// Global.BUFF_SIZE = 40000;
				}

				// packet loss rate 未完成
				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());

				// SDN controller configuration 二
				// Controller Type
				// CONTROLLER_TYPE_ONOS = 0;
				// CONTROLLER_TYPE_FLOODLIGHT = 1;
				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
				Global.CONTROLLER_TYPE = S_comboBoxControllerType.getSelectedIndex();

				// host size
				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());

				// Benchmark configuration 5
				// Asynchronous message type
				// unknowns type asynchronous message type
				if (S_packetComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (S_packetComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
				} else if (S_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
				} else {
					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
				}

				// Benchmark configuration 6
				// Unknown packet type （TCP， UDP）
				// provisioning unknow type
				if (S_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (S_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
				} else {
					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
				}

				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());

				// bulk size
				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());

				new Thread(new Runnable() {
					public void run() {
						S_RunButton.go();
					}
				}).start();

			}
		}

		);
		S_startButton.setBounds(460, 244, 93, 23);
		southbound.add(S_startButton);

		JButton btnStop = new JButton("Force Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Initializer.INITIAL_CHANNEL_POOL();
				S_progressBarTotal.setMaximum(0);
				S_progressBarTotal.setValue(0);

				S_progressBar.setMaximum(0);
				S_progressBar.setValue(0);

			}
		});
		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStop.setBounds(804, 244, 93, 23);
		southbound.add(btnStop);

		S_tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		S_tabbedPane.setBounds(41, 344, 992, 310);
		southbound.add(S_tabbedPane);
		JScrollPane scrollPane_3 = new JScrollPane();
		S_tabbedPane.addTab("Result",
				new ImageIcon(Main.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
				scrollPane_3, null);

		S_RESULT_TABLE = new JTable();
		S_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		S_RESULT_TABLE.setRowHeight(26);
		S_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		S_RESULT_TABLE.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
						{ "Proactive Path Provisioning Time", "ms", "N/A", null, null, null },
						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", null, null, null },
						{ "Control Session Capacity", "Sessions", null, null, null, null }, },
				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
		S_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
		S_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
		S_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
		S_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
		scrollPane_3.setViewportView(S_RESULT_TABLE);

		S_LOG_TEXT = new JTextArea();
		S_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_LOG_TEXT.setLineWrap(true);
		S_TOPO_TEXT = new JTextArea();
		S_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_TOPO_TEXT.setLineWrap(true);

		S_tabbedPane.addTab("Log",
				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
				new JScrollPane(S_LOG_TEXT), null);

		S_TOPOLOGY_TEXT = new JTextArea();
		S_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
		S_TOPOLOGY_TEXT.setLineWrap(true);

		S_tabbedPane.addTab("Topology",
				new ImageIcon(Main.class.getResource("/com/alee/examples/content/icons/presentation.png")),
				new JScrollPane(S_TOPO_TEXT), null);

		JLabel lblNewLabel = new JLabel("Metrics");
		lblNewLabel.setIcon(
				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel.setBounds(41, 10, 93, 15);
		southbound.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1
				.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblNewLabel_1.setBounds(309, 10, 170, 15);
		southbound.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(309, 167, 724, 67);
		southbound.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Controller Mode");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(48, 10, 88, 20);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("Controller IP");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(48, 40, 88, 18);
		panel.add(lblNewLabel_3);

		S_controller2IpPort = new JTextField();
		S_controller2IpPort.setEditable(false);
		S_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller2IpPort.setAutoscrolls(false);
		S_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller2IpPort.setText("166.104.28.130:6633");
		S_controller2IpPort.setBounds(316, 40, 160, 18);
		panel.add(S_controller2IpPort);

		// first IP
		S_controller1IpPort = new JTextField();
		S_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller1IpPort.setAutoscrolls(false);
		S_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller1IpPort.setText("192.168.1.:6633");
		S_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller1IpPort.setBounds(146, 40, 160, 18);
		panel.add(S_controller1IpPort);

		S_controller3IpPort = new JTextField();
		S_controller3IpPort.setEditable(false);
		S_controller3IpPort.setText("166.104.28.131:6633");
		S_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
		S_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		S_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_controller3IpPort.setAutoscrolls(false);
		S_controller3IpPort.setBounds(486, 40, 160, 18);
		panel.add(S_controller3IpPort);

		S_comboBoxControllerMode = new JComboBox();
		S_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
		S_comboBoxControllerMode.setSelectedIndex(0);
		S_comboBoxControllerMode.setBounds(146, 10, 126, 20);
		panel.add(S_comboBoxControllerMode);

		JLabel lblControllerMode = new JLabel("Controller Mode");
		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblControllerMode.setBounds(48, 10, 88, 20);
		// panel.add(lblControllerMode);

		S_comboBoxControllerType = new JComboBox();
		S_comboBoxControllerType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				S_packetComboBox.removeAllItems();
				S_provisioningComboBox.removeAllItems();
				if (S_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_ONOS) {

					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
				} else if (S_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_FLOODLIGHT) {
					S_packetComboBox.removeAllItems();
					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
				} else {

					S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP", "ECHO" }));
					S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP" }));
				}
			}
		});

		S_comboBoxControllerType.setBounds(520, 10, 126, 20);
		panel.add(S_comboBoxControllerType);
		S_comboBoxControllerType
				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
		S_comboBoxControllerType.setSelectedIndex(0);
		S_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblControllerType = new JLabel("Controller Type");
		lblControllerType.setBounds(422, 10, 88, 20);
		panel.add(lblControllerType);
		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
		lblNewLabel_2.setIcon(new ImageIcon(Main.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_2.setBounds(309, 142, 204, 15);
		southbound.add(lblNewLabel_2);

		S_progressBar = new JProgressBar();
		S_progressBar.setBackground(Color.WHITE);
		S_progressBar.setStringPainted(true);
		S_progressBar.setBounds(460, 287, 573, 13);
		southbound.add(S_progressBar);

		S_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
		S_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
		S_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
		S_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
		S_benchmarkCondition.setBounds(293, 344, 771, 15);
		southbound.add(S_benchmarkCondition);

		S_progressBarTotal = new JProgressBar();
		S_progressBarTotal.setStringPainted(true);
		S_progressBarTotal.setBackground(Color.WHITE);
		S_progressBarTotal.setBounds(460, 321, 573, 13);
		southbound.add(S_progressBarTotal);

		JLabel lblCurrentProgress = new JLabel("Current Progress");
		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblCurrentProgress.setBounds(309, 285, 141, 15);
		southbound.add(lblCurrentProgress);

		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalProgress.setBounds(309, 319, 141, 15);
		southbound.add(lblTotalProgress);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_1.setBounds(309, 32, 724, 101);
		southbound.add(panel_1);

		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
		panel_1.add(lblNumberOfSwitch);

		S_numberOfS = new JTextField();
		S_numberOfS.setText("10");
		S_numberOfS.setMargin(new Insets(3, 3, 3, 3));
		S_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
		S_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_numberOfS.setAutoscrolls(false);
		S_numberOfS.setBounds(191, 40, 79, 18);
		panel_1.add(S_numberOfS);

		S_comboBoxOFProtocolType = new JComboBox();
		S_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
		S_comboBoxOFProtocolType.setSelectedIndex(3);
		S_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
		panel_1.add(S_comboBoxOFProtocolType);

		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblOfProtocolVer.setBounds(10, 10, 100, 20);
		panel_1.add(lblOfProtocolVer);

		S_hostIP_1 = new JTextField();
		S_hostIP_1.setText("10");
		S_hostIP_1.setMargin(new Insets(3, 3, 3, 3));
		S_hostIP_1.setHorizontalAlignment(SwingConstants.CENTER);
		S_hostIP_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_hostIP_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_hostIP_1.setAutoscrolls(false);
		S_hostIP_1.setBounds(479, 40, 28, 18);
		panel_1.add(S_hostIP_1);

		JLabel lblHostIp = new JLabel("Host IP start from");
		lblHostIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblHostIp.setBounds(370, 40, 99, 18);
		panel_1.add(lblHostIp);

		S_hostIP_2 = new JTextField();
		S_hostIP_2.setText("0");
		S_hostIP_2.setMargin(new Insets(3, 3, 3, 3));
		S_hostIP_2.setHorizontalAlignment(SwingConstants.CENTER);
		S_hostIP_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_hostIP_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_hostIP_2.setAutoscrolls(false);
		S_hostIP_2.setBounds(530, 40, 28, 18);
		panel_1.add(S_hostIP_2);

		S_textField_3 = new JTextField();
		S_textField_3.setEditable(false);
		S_textField_3.setText("0");
		S_textField_3.setMargin(new Insets(3, 3, 3, 3));
		S_textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		S_textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_textField_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_textField_3.setAutoscrolls(false);
		S_textField_3.setBounds(584, 40, 28, 18);
		panel_1.add(S_textField_3);

		S_textField_4 = new JTextField();
		S_textField_4.setEditable(false);
		S_textField_4.setText("2");
		S_textField_4.setMargin(new Insets(3, 3, 3, 3));
		S_textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		S_textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_textField_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		S_textField_4.setAutoscrolls(false);
		S_textField_4.setBounds(638, 40, 28, 18);
		panel_1.add(S_textField_4);

		JLabel label_1 = new JLabel(".");
		label_1.setBounds(517, 41, 6, 15);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel(".");
		label_2.setBounds(568, 41, 6, 15);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel(".");
		label_3.setBounds(622, 41, 6, 15);
		panel_1.add(label_3);

		S_comboBoxTopoType = new JComboBox();
		S_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
		S_comboBoxTopoType.setSelectedIndex(0);
		S_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_comboBoxTopoType.setBounds(517, 10, 79, 20);
		panel_1.add(S_comboBoxTopoType);

		JLabel label_4 = new JLabel("Topology Type");
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_4.setBounds(370, 10, 88, 20);
		panel_1.add(label_4);

		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketType.setBounds(10, 69, 171, 20);
		panel_1.add(lblPacketType);

		S_packetComboBox = new JComboBox();
		S_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
		S_packetComboBox.setSelectedIndex(0);
		S_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_packetComboBox.setBounds(191, 68, 79, 20);
		panel_1.add(S_packetComboBox);

		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUnknownPacketType.setBounds(370, 68, 144, 20);
		panel_1.add(lblUnknownPacketType);

		S_provisioningComboBox = new JComboBox();
		S_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
		S_provisioningComboBox.setSelectedIndex(0);
		S_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		S_provisioningComboBox.setBounds(518, 69, 78, 20);
		panel_1.add(S_provisioningComboBox);

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void northbound(JPanel northbound) {
		JScrollPane metricsPane = new JScrollPane();
		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		metricsPane.setAutoscrolls(true);
		metricsPane.setBounds(41, 32, 242, 299);
		// frmMirlabBenchmark.getContentPane().add(scrollPane);
		northbound.add(metricsPane);

		N_metricList = new JList();
		N_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
		N_metricList.setAutoscrolls(false);
		metricsPane.setViewportView(N_metricList);
		N_metricList.setModel(new AbstractListModel() {
			String[] values = new String[] { "1.Topology Discovery Time",
					"2.Topology Change Detection Time(Link Down/Up)", "3.Asynchronous Message Processing Time",
					"4.Asynchronous Message Processing Rate", "5.Reactive Path Provisioning Time",
					"6.Reactive Path Provisioning Rate", "7.*Proactive Path Provisioning Time",
					"8.*Proactive Path Provisioning Rate", "9. Test (Metric 1~6)",
					"---------------SDN controller should be reboot to test metric 10~16",
					"10.Control Session Capacity: CCn", "11.Network Discovery Size: Ns",
					"12.Forwarding Table Capacity: Nrp", "13.*Exception Handling : Security",
					"14.*DoS attacks: Security", "15.*Controller Failover Time: Reliability",
					"16.*Network Re-provisioning Time: Reliability Node failure vs. Link Failure", };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		N_metricList.setSelectedIndex(0);

		N_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));

		// 关于run按钮的代码
		N_startButton = new JButton("Run");
		N_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 按键之后开始實行下列代碼

				Main.N_startButton.setEnabled(false);// run按钮不可按
				Main.N_metricList.setEnabled(false);// metric 栏不可选择

				// metric index，获得metric的选择值并赋值给global变数
				Global.TEST_METRIC = N_metricList.getSelectedIndex();

				// --Benchmark configuration 1
				// OF Version
				Global.OPENFLOW_VERSION = Global.OF_V_3;
				// -- Benchmark configuration 2
				// topology type 圆形或者直线
				Global.TOPO_TYPE = N_comboBoxTopoType.getSelectedIndex();
				// --Benchmark configuration 3
				// node # node数量
				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) N_numberOfS.getText().toString());

				// test time
				Global.TEST_TIME = 5;

				// Benchmark configuration 4
				// Host Ip
				Global.HOST_IP[0] = Integer.parseInt((String) N_hostIP_1.getText().toString());
				Global.HOST_IP[1] = Integer.parseInt((String) N_hostIP_2.getText().toString());

				// SDN controller configuration 一
				// Controller Mode （Standalone， Cluster）
				Global.CONTROLLER_MODE = N_comboBoxControllerMode.getSelectedIndex();
				if (Global.CONTROLLER_MODE == Global.CONTROLLER_MODE_STANDALONE) {
					// standalone mode 完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(N_controller1IpPort.getText().toString(), ":");// 分词器，分离ip和port
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 1000;
					// Global.BUFF_SIZE = 1000000;
				} else {
					// cluster mode 未完成
					// SDN controller configuration 三
					StringTokenizer st = new StringTokenizer(N_controller1IpPort.getText().toString(), ":");
					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
					// Global.NODE_SIZE = 200;
					// Global.BUFF_SIZE = 40000;
				}

				// packet loss rate 未完成
				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());

				// SDN controller configuration 二
				// Controller Type
				// CONTROLLER_TYPE_ONOS = 0;
				// CONTROLLER_TYPE_FLOODLIGHT = 1;
				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
				Global.CONTROLLER_TYPE = N_comboBoxControllerType.getSelectedIndex();

				// host size
				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());

				// Benchmark configuration 5
				// Asynchronous message type
				// unknowns type asynchronous message type
				if (N_packetComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (N_packetComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
				} else if (N_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
				} else {
					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
				}

				// Benchmark configuration 6
				// Unknown packet type （TCP， UDP）
				// provisioning unknow type
				if (N_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
				} else if (N_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
				} else {
					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
				}

				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());

				// bulk size
				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());

				new Thread(new Runnable() {
					public void run() {
						N_RunButton.go();
					}
				}).start();

			}
		}

		);
		N_startButton.setBounds(460, 244, 93, 23);
		northbound.add(N_startButton);

		JButton btnStop = new JButton("Force Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Initializer.INITIAL_CHANNEL_POOL();
				N_progressBarTotal.setMaximum(0);
				N_progressBarTotal.setValue(0);

				N_progressBar.setMaximum(0);
				N_progressBar.setValue(0);

			}
		});
		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStop.setBounds(804, 244, 93, 23);
		northbound.add(btnStop);

		N_tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		N_tabbedPane.setBounds(41, 344, 992, 310);
		northbound.add(N_tabbedPane);
		JScrollPane scrollPane_3 = new JScrollPane();
		N_tabbedPane.addTab("Result",
				new ImageIcon(Main.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
				scrollPane_3, null);

		N_RESULT_TABLE = new JTable();
		N_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		N_RESULT_TABLE.setRowHeight(26);
		N_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		N_RESULT_TABLE.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
						{ "Proactive Path Provisioning Time", "ms", "N/A", null, null, null },
						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", null, null, null },
						{ "Control Session Capacity", "Sessions", null, null, null, null }, },
				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
		N_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
		N_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
		N_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
		N_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
		scrollPane_3.setViewportView(N_RESULT_TABLE);

		N_LOG_TEXT = new JTextArea();
		N_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_LOG_TEXT.setLineWrap(true);
		N_TOPO_TEXT = new JTextArea();
		N_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_TOPO_TEXT.setLineWrap(true);

		N_tabbedPane.addTab("Log",
				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
				new JScrollPane(N_LOG_TEXT), null);

		N_TOPOLOGY_TEXT = new JTextArea();
		N_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
		N_TOPOLOGY_TEXT.setLineWrap(true);

		N_tabbedPane.addTab("Topology",
				new ImageIcon(Main.class.getResource("/com/alee/examples/content/icons/presentation.png")),
				new JScrollPane(N_TOPO_TEXT), null);

		JLabel lblNewLabel = new JLabel("Metrics");
		lblNewLabel.setIcon(
				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel.setBounds(41, 10, 93, 15);
		northbound.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1
				.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblNewLabel_1.setBounds(309, 10, 170, 15);
		northbound.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(309, 167, 724, 67);
		northbound.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("Controller Mode");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(48, 10, 88, 20);
		panel.add(label);

		JLabel lblNewLabel_3 = new JLabel("Controller IP");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(48, 40, 88, 18);
		panel.add(lblNewLabel_3);

		N_controller2IpPort = new JTextField();
		N_controller2IpPort.setEditable(false);
		N_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller2IpPort.setAutoscrolls(false);
		N_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller2IpPort.setText("166.104.28.130:6633");
		N_controller2IpPort.setBounds(316, 40, 160, 18);
		panel.add(N_controller2IpPort);

		// first IP
		N_controller1IpPort = new JTextField();
		N_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller1IpPort.setAutoscrolls(false);
		N_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller1IpPort.setText("192.168.1.:6633");
		N_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller1IpPort.setBounds(146, 40, 160, 18);
		panel.add(N_controller1IpPort);

		N_controller3IpPort = new JTextField();
		N_controller3IpPort.setEditable(false);
		N_controller3IpPort.setText("166.104.28.131:6633");
		N_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
		N_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
		N_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_controller3IpPort.setAutoscrolls(false);
		N_controller3IpPort.setBounds(486, 40, 160, 18);
		panel.add(N_controller3IpPort);

		N_comboBoxControllerMode = new JComboBox();
		N_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
		N_comboBoxControllerMode.setSelectedIndex(0);
		N_comboBoxControllerMode.setBounds(146, 10, 126, 20);
		panel.add(N_comboBoxControllerMode);

		JLabel lblControllerMode = new JLabel("Controller Mode");
		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblControllerMode.setBounds(48, 10, 88, 20);
		// panel.add(lblControllerMode);

		N_comboBoxControllerType = new JComboBox();
		N_comboBoxControllerType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				N_packetComboBox.removeAllItems();
				N_provisioningComboBox.removeAllItems();
				if (N_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_ONOS) {

					N_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					N_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
				} else if (N_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_FLOODLIGHT) {
					N_packetComboBox.removeAllItems();
					N_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
					N_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
				} else {

					N_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP", "ECHO" }));
					N_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP" }));
				}
			}
		});

		N_comboBoxControllerType.setBounds(520, 10, 126, 20);
		panel.add(N_comboBoxControllerType);
		N_comboBoxControllerType
				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
		N_comboBoxControllerType.setSelectedIndex(0);
		N_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblControllerType = new JLabel("Controller Type");
		lblControllerType.setBounds(422, 10, 88, 20);
		panel.add(lblControllerType);
		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
		lblNewLabel_2.setIcon(new ImageIcon(Main.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_2.setBounds(309, 142, 204, 15);
		northbound.add(lblNewLabel_2);

		N_progressBar = new JProgressBar();
		N_progressBar.setBackground(Color.WHITE);
		N_progressBar.setStringPainted(true);
		N_progressBar.setBounds(460, 287, 573, 13);
		northbound.add(N_progressBar);

		N_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
		N_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
		N_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
		N_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
		N_benchmarkCondition.setBounds(293, 344, 771, 15);
		northbound.add(N_benchmarkCondition);

		N_progressBarTotal = new JProgressBar();
		N_progressBarTotal.setStringPainted(true);
		N_progressBarTotal.setBackground(Color.WHITE);
		N_progressBarTotal.setBounds(460, 321, 573, 13);
		northbound.add(N_progressBarTotal);

		JLabel lblCurrentProgress = new JLabel("Current Progress");
		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblCurrentProgress.setBounds(309, 285, 141, 15);
		northbound.add(lblCurrentProgress);

		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalProgress.setBounds(309, 319, 141, 15);
		northbound.add(lblTotalProgress);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_1.setBounds(309, 32, 724, 101);
		northbound.add(panel_1);

		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
		panel_1.add(lblNumberOfSwitch);

		N_numberOfS = new JTextField();
		N_numberOfS.setText("10");
		N_numberOfS.setMargin(new Insets(3, 3, 3, 3));
		N_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
		N_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_numberOfS.setAutoscrolls(false);
		N_numberOfS.setBounds(191, 40, 79, 18);
		panel_1.add(N_numberOfS);

		N_comboBoxOFProtocolType = new JComboBox();
		N_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
		N_comboBoxOFProtocolType.setSelectedIndex(3);
		N_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
		panel_1.add(N_comboBoxOFProtocolType);

		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblOfProtocolVer.setBounds(10, 10, 100, 20);
		panel_1.add(lblOfProtocolVer);

		N_hostIP_1 = new JTextField();
		N_hostIP_1.setText("10");
		N_hostIP_1.setMargin(new Insets(3, 3, 3, 3));
		N_hostIP_1.setHorizontalAlignment(SwingConstants.CENTER);
		N_hostIP_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_hostIP_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_hostIP_1.setAutoscrolls(false);
		N_hostIP_1.setBounds(479, 40, 28, 18);
		panel_1.add(N_hostIP_1);

		JLabel lblHostIp = new JLabel("Host IP start from");
		lblHostIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblHostIp.setBounds(370, 40, 99, 18);
		panel_1.add(lblHostIp);

		N_hostIP_2 = new JTextField();
		N_hostIP_2.setText("0");
		N_hostIP_2.setMargin(new Insets(3, 3, 3, 3));
		N_hostIP_2.setHorizontalAlignment(SwingConstants.CENTER);
		N_hostIP_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_hostIP_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_hostIP_2.setAutoscrolls(false);
		N_hostIP_2.setBounds(530, 40, 28, 18);
		panel_1.add(N_hostIP_2);

		N_textField_3 = new JTextField();
		N_textField_3.setEditable(false);
		N_textField_3.setText("0");
		N_textField_3.setMargin(new Insets(3, 3, 3, 3));
		N_textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		N_textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_textField_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_textField_3.setAutoscrolls(false);
		N_textField_3.setBounds(584, 40, 28, 18);
		panel_1.add(N_textField_3);

		N_textField_4 = new JTextField();
		N_textField_4.setEditable(false);
		N_textField_4.setText("2");
		N_textField_4.setMargin(new Insets(3, 3, 3, 3));
		N_textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		N_textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_textField_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		N_textField_4.setAutoscrolls(false);
		N_textField_4.setBounds(638, 40, 28, 18);
		panel_1.add(N_textField_4);

		JLabel label_1 = new JLabel(".");
		label_1.setBounds(517, 41, 6, 15);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel(".");
		label_2.setBounds(568, 41, 6, 15);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel(".");
		label_3.setBounds(622, 41, 6, 15);
		panel_1.add(label_3);

		N_comboBoxTopoType = new JComboBox();
		N_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
		N_comboBoxTopoType.setSelectedIndex(0);
		N_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_comboBoxTopoType.setBounds(517, 10, 79, 20);
		panel_1.add(N_comboBoxTopoType);

		JLabel label_4 = new JLabel("Topology Type");
		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_4.setBounds(370, 10, 88, 20);
		panel_1.add(label_4);

		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketType.setBounds(10, 69, 171, 20);
		panel_1.add(lblPacketType);

		N_packetComboBox = new JComboBox();
		N_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
		N_packetComboBox.setSelectedIndex(0);
		N_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_packetComboBox.setBounds(191, 68, 79, 20);
		panel_1.add(N_packetComboBox);

		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUnknownPacketType.setBounds(370, 68, 144, 20);
		panel_1.add(lblUnknownPacketType);

		N_provisioningComboBox = new JComboBox();
		N_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
		N_provisioningComboBox.setSelectedIndex(0);
		N_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		N_provisioningComboBox.setBounds(518, 69, 78, 20);
		panel_1.add(N_provisioningComboBox);

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private void snbound(JPanel snbound) {
//		JScrollPane metricsPane = new JScrollPane();
//		metricsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
//		metricsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		metricsPane.setAutoscrolls(true);
//		metricsPane.setBounds(41, 32, 242, 299);
//		// frmMirlabBenchmark.getContentPane().add(scrollPane);
//		snbound.add(metricsPane);
//
//		SN_metricList = new JList();
//		SN_metricList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_metricList.setBorder(BorderFactory.createTitledBorder("Select One Item"));
//		SN_metricList.setAutoscrolls(false);
//		metricsPane.setViewportView(SN_metricList);
//		SN_metricList.setModel(new AbstractListModel() {
//			String[] values = new String[] { "1.Topology Discovery Time",
//					"2.Topology Change Detection Time(Link Down/Up)", "3.Asynchronous Message Processing Time",
//					"4.Asynchronous Message Processing Rate", "5.Reactive Path Provisioning Time",
//					"6.Reactive Path Provisioning Rate", "7.*Proactive Path Provisioning Time",
//					"8.*Proactive Path Provisioning Rate", "9. Test (Metric 1~6)",
//					"---------------SDN controller should be reboot to test metric 10~16",
//					"10.Control Session Capacity: CCn", "11.Network Discovery Size: Ns",
//					"12.Forwarding Table Capacity: Nrp", "13.*Exception Handling : Security",
//					"14.*DoS attacks: Security", "15.*Controller Failover Time: Reliability",
//					"16.*Network Re-provisioning Time: Reliability Node failure vs. Link Failure", };
//
//			public int getSize() {
//				return values.length;
//			}
//
//			public Object getElementAt(int index) {
//				return values[index];
//			}
//		});
//		SN_metricList.setSelectedIndex(0);
//
//		SN_comboBoxTestType.setModel(new DefaultComboBoxModel(new String[] { "Throughput", "Latency" }));
//
//		// 关于run按钮的代码
//		SN_startButton = new JButton("Run");
//		SN_startButton.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_startButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {// 按键之后开始實行下列代碼
//				Main.SN_startButton.setEnabled(false);// run按钮不可按
//				Main.SN_metricList.setEnabled(false);// metric 栏不可选择
//
//				// metric index，获得metric的选择值并赋值给global变数
//				Global.TEST_METRIC = SN_metricList.getSelectedIndex();
//
//				// --Benchmark configuration 1
//				// OF Version
//				Global.OPENFLOW_VERSION = Global.OF_V_3;
//				// -- Benchmark configuration 2
//				// topology type 圆形或者直线
//				Global.TOPO_TYPE = SN_comboBoxTopoType.getSelectedIndex();
//				// --Benchmark configuration 3
//				// node # node数量
//				Global.NUMBER_OF_TEST_SWITCH = Integer.parseInt((String) SN_numberOfS.getText().toString());
//
//				// test time
//				Global.TEST_TIME = 5;
//
//				// Benchmark configuration 4
//				// Host Ip
//				Global.HOST_IP[0] = Integer.parseInt((String) SN_hostIP_1.getText().toString());
//				Global.HOST_IP[1] = Integer.parseInt((String) SN_hostIP_2.getText().toString());
//
//				// SDN controller configuration 一
//				// Controller Mode （Standalone， Cluster）
//				Global.CONTROLLER_MODE = SN_comboBoxControllerMode.getSelectedIndex();
//				if (Global.CONTROLLER_MODE == Global.CONTROLLER_MODE_STANDALONE) {
//					// standalone mode 完成
//					// SDN controller configuration 三
//					StringTokenizer st = new StringTokenizer(SN_controller1IpPort.getText().toString(), ":");// 分词器，分离ip和port
//					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
//					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
//					// Global.NODE_SIZE = 1000;
//					// Global.BUFF_SIZE = 1000000;
//				} else {
//					// cluster mode 未完成
//					// SDN controller configuration 三
//					StringTokenizer st = new StringTokenizer(SN_controller1IpPort.getText().toString(), ":");
//					Global.SDN_CONTROLLER_IP[0] = st.nextToken();
//					Global.SDN_CONTROLLER_PORT = Integer.parseInt(st.nextToken().toString());
//					// Global.NODE_SIZE = 200;
//					// Global.BUFF_SIZE = 40000;
//				}
//
//				// packet loss rate 未完成
//				Global.PACKET_LOSS_RATE = Double.parseDouble(cf.packetLossRate.getText());
//
//				// SDN controller configuration 二
//				// Controller Type
//				// CONTROLLER_TYPE_ONOS = 0;
//				// CONTROLLER_TYPE_FLOODLIGHT = 1;
//				// CONTROLLER_TYPE_OPENDAYLIGHT = 2;
//				Global.CONTROLLER_TYPE = SN_comboBoxControllerType.getSelectedIndex();
//
//				// host size
//				Global.NODE_SIZE = Integer.parseInt(cf.hostSize.getText());
//
//				// Benchmark configuration 5
//				// Asynchronous message type
//				// unknowns type asynchronous message type
//				if (SN_packetComboBox.getSelectedItem().toString().equals("TCP")) {
//					Global.UNKNOWN_PACKET_TYPE = Global.TCP_PACKET;// =6
//				} else if (SN_packetComboBox.getSelectedItem().toString().equals("UDP")) {
//					Global.UNKNOWN_PACKET_TYPE = Global.UDP_PACKET;
//				} else if (SN_packetComboBox.getSelectedItem().toString().equals("ECHO")) {
//					Global.UNKNOWN_PACKET_TYPE = Global.ECHO_PACKET;
//				} else {
//					Global.UNKNOWN_PACKET_TYPE = Global.ARP_REQUEST;
//				}
//
//				// Benchmark configuration 6
//				// Unknown packet type （TCP， UDP）
//				// provisioning unknow type
//				if (SN_provisioningComboBox.getSelectedItem().toString().equals("TCP")) {
//					Global.PROVISINIONING_PACKET_TYPE = Global.TCP_PACKET;// =6
//				} else if (SN_provisioningComboBox.getSelectedItem().toString().equals("UDP")) {
//					Global.PROVISINIONING_PACKET_TYPE = Global.UDP_PACKET;
//				} else {
//					Global.PROVISINIONING_PACKET_TYPE = Global.ARP_REPLY;
//				}
//
//				Global.GAP_TIME = Integer.parseInt(cf.gapTime.getText()) * 1000000;
//				Global.MAX_SAMPLE = Integer.parseInt(cf.maxSample.getText());
//				Global.MIN_SAMPLE = Integer.parseInt(cf.minSample.getText());
//
//				// bulk size
//				Global.CURRENT_BULK_SIZE = Integer.parseInt(cf.bulkSize.getText());
//
//				new Thread(new Runnable() {
//					public void run() {
//						SN_RunButton.go();
//					}
//				}).start();
//
//			}
//		}
//
//		);
//		SN_startButton.setBounds(460, 244, 93, 23);
//		snbound.add(SN_startButton);
//
//		JButton btnStop = new JButton("Force Stop");
//		btnStop.setEnabled(false);
//		btnStop.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Initializer.INITIAL_CHANNEL_POOL();
//				SN_progressBarTotal.setMaximum(0);
//				SN_progressBarTotal.setValue(0);
//
//				SN_progressBar.setMaximum(0);
//				SN_progressBar.setValue(0);
//
//			}
//		});
//		btnStop.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		btnStop.setBounds(804, 244, 93, 23);
//		snbound.add(btnStop);
//
//		SN_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//
//		SN_tabbedPane.setBounds(41, 344, 992, 310);
//		snbound.add(SN_tabbedPane);
//		JScrollPane scrollPane_3 = new JScrollPane();
//		SN_tabbedPane.addTab("Result",
//				new ImageIcon(Main.class.getResource("/com/alee/managers/style/icons/component/tableHeader.png")),
//				scrollPane_3, null);
//
//		SN_RESULT_TABLE = new JTable();
//		SN_RESULT_TABLE.setFont(new Font("Times New Roman", Font.PLAIN, 11));
//		SN_RESULT_TABLE.setRowHeight(26);
//		SN_RESULT_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
//		SN_RESULT_TABLE.setModel(new DefaultTableModel(
//				new Object[][] { { "Topology Discovery Time", "ms", null, "Network Discovery Size", "Ovs", "" },
//						{ "Topology Change Detection(Link Up)", "ms", null, "Forwarding Table Capacity", "Flows", "" },
//						{ "Topology Change Detection(Link Down)", "ms", null, "DoS Attacks", null, "N/A" },
//						{ "Asynchronous Message Processing Time", "ms", null, "Controller Failover Time", null, "N/A" },
//						{ "Asynchronous Message Processing Rate", "Msg/s", null, "Exception Handing", null, "N/A" },
//						{ "Reactive Path Provisioning Time", "ms", null, "", null, null },
//						{ "Reactive Path Provisioning Rate", "Flows/s", null, "", null, null },
//						{ "Proactive Path Provisioning Time", "ms", "N/A", null, null, null },
//						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", null, null, null },
//						{ "Control Session Capacity", "Sessions", null, null, null, null }, },
//				new String[] { "Metric", "Unit", "Result", "Metric", "Unit", "Result" }));
//		SN_RESULT_TABLE.getColumnModel().getColumn(0).setPreferredWidth(215);
//		SN_RESULT_TABLE.getColumnModel().getColumn(0).setMinWidth(215);
//		SN_RESULT_TABLE.getColumnModel().getColumn(1).setPreferredWidth(55);
//		SN_RESULT_TABLE.getColumnModel().getColumn(1).setMinWidth(55);
//		SN_RESULT_TABLE.getColumnModel().getColumn(2).setMinWidth(75);
//		SN_RESULT_TABLE.getColumnModel().getColumn(3).setPreferredWidth(215);
//		SN_RESULT_TABLE.getColumnModel().getColumn(3).setMinWidth(215);
//		SN_RESULT_TABLE.getColumnModel().getColumn(4).setPreferredWidth(55);
//		SN_RESULT_TABLE.getColumnModel().getColumn(4).setMinWidth(55);
//		SN_RESULT_TABLE.getColumnModel().getColumn(5).setMinWidth(75);
//		scrollPane_3.setViewportView(SN_RESULT_TABLE);
//
//		SN_LOG_TEXT = new JTextArea();
//		SN_LOG_TEXT.setMargin(new Insets(20, 10, 20, 10));
//		SN_LOG_TEXT.setLineWrap(true);
//		SN_TOPO_TEXT = new JTextArea();
//		SN_TOPO_TEXT.setMargin(new Insets(20, 10, 20, 10));
//		SN_TOPO_TEXT.setLineWrap(true);
//
//		SN_tabbedPane.addTab("Log",
//				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/edit.png")),
//				new JScrollPane(SN_LOG_TEXT), null);
//
//		SN_TOPOLOGY_TEXT = new JTextArea();
//		SN_TOPOLOGY_TEXT.setMargin(new Insets(20, 10, 20, 10));
//		SN_TOPOLOGY_TEXT.setLineWrap(true);
//
//		SN_tabbedPane.addTab("Topology",
//				new ImageIcon(Main.class.getResource("/com/alee/examples/content/icons/presentation.png")),
//				new JScrollPane(SN_TOPO_TEXT), null);
//
//		JLabel lblNewLabel = new JLabel("Metrics");
//		lblNewLabel.setIcon(
//				new ImageIcon(Main.class.getResource("/com/alee/examples/groups/menubar/icons/menubar/radio1.png")));
//		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		lblNewLabel.setBounds(41, 10, 93, 15);
//		snbound.add(lblNewLabel);
//
//		JLabel lblNewLabel_1 = new JLabel("Benchmark Configuration");
//		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		lblNewLabel_1
//				.setIcon(new ImageIcon(Main.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
//		lblNewLabel_1.setBounds(309, 10, 170, 15);
//		snbound.add(lblNewLabel_1);
//
//		JPanel panel = new JPanel();
//		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//		panel.setBounds(309, 167, 724, 67);
//		snbound.add(panel);
//		panel.setLayout(null);
//
//		JLabel label = new JLabel("Controller Mode");
//		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		label.setBounds(48, 10, 88, 20);
//		panel.add(label);
//
//		JLabel lblNewLabel_3 = new JLabel("Controller IP");
//		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblNewLabel_3.setBounds(48, 40, 88, 18);
//		panel.add(lblNewLabel_3);
//
//		SN_controller2IpPort = new JTextField();
//		SN_controller2IpPort.setEditable(false);
//		SN_controller2IpPort.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_controller2IpPort.setAutoscrolls(false);
//		SN_controller2IpPort.setMargin(new Insets(3, 3, 3, 3));
//		SN_controller2IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_controller2IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_controller2IpPort.setText("166.104.28.130:6633");
//		SN_controller2IpPort.setBounds(316, 40, 160, 18);
//		panel.add(SN_controller2IpPort);
//
//		// first IP
//		SN_controller1IpPort = new JTextField();
//		SN_controller1IpPort.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_controller1IpPort.setAutoscrolls(false);
//		SN_controller1IpPort.setMargin(new Insets(3, 3, 3, 3));
//		SN_controller1IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_controller1IpPort.setText("192.168.1.:6633");
//		SN_controller1IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_controller1IpPort.setBounds(146, 40, 160, 18);
//		panel.add(SN_controller1IpPort);
//
//		SN_controller3IpPort = new JTextField();
//		SN_controller3IpPort.setEditable(false);
//		SN_controller3IpPort.setText("166.104.28.131:6633");
//		SN_controller3IpPort.setMargin(new Insets(3, 3, 3, 3));
//		SN_controller3IpPort.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_controller3IpPort.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_controller3IpPort.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_controller3IpPort.setAutoscrolls(false);
//		SN_controller3IpPort.setBounds(486, 40, 160, 18);
//		panel.add(SN_controller3IpPort);
//
//		SN_comboBoxControllerMode = new JComboBox();
//		SN_comboBoxControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_comboBoxControllerMode.setModel(new DefaultComboBoxModel(new String[] { "Standalone", "Cluster" }));
//		SN_comboBoxControllerMode.setSelectedIndex(0);
//		SN_comboBoxControllerMode.setBounds(146, 10, 126, 20);
//		panel.add(SN_comboBoxControllerMode);
//
//		JLabel lblControllerMode = new JLabel("Controller Mode");
//		lblControllerMode.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblControllerMode.setBounds(48, 10, 88, 20);
//		// panel.add(lblControllerMode);
//
//		SN_comboBoxControllerType = new JComboBox();
//		SN_comboBoxControllerType.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent e) {
//				SN_packetComboBox.removeAllItems();
//				SN_provisioningComboBox.removeAllItems();
//				if (SN_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_ONOS) {
//
//					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
//					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
//				} else if (SN_comboBoxControllerType.getSelectedIndex() == Global.CONTROLLER_TYPE_FLOODLIGHT) {
//					SN_packetComboBox.removeAllItems();
//					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
//					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
//				} else {
//
//					SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP", "ECHO" }));
//					SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "ARP" }));
//				}
//			}
//		});
//
//		SN_comboBoxControllerType.setBounds(520, 10, 126, 20);
//		panel.add(SN_comboBoxControllerType);
//		SN_comboBoxControllerType
//				.setModel(new DefaultComboBoxModel(new String[] { "ONOS", "Floodlight", "OpendayLight" }));
//		SN_comboBoxControllerType.setSelectedIndex(0);
//		SN_comboBoxControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//
//		JLabel lblControllerType = new JLabel("Controller Type");
//		lblControllerType.setBounds(422, 10, 88, 20);
//		panel.add(lblControllerType);
//		lblControllerType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//
//		JLabel lblNewLabel_2 = new JLabel("SDN Controller Configuration");
//		lblNewLabel_2.setIcon(new ImageIcon(Main.class.getResource("/com/alee/laf/filechooser/icons/computer.png")));
//		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		lblNewLabel_2.setBounds(309, 142, 204, 15);
//		snbound.add(lblNewLabel_2);
//
//		SN_progressBar = new JProgressBar();
//		SN_progressBar.setBackground(Color.WHITE);
//		SN_progressBar.setStringPainted(true);
//		SN_progressBar.setBounds(460, 287, 573, 13);
//		snbound.add(SN_progressBar);
//
//		SN_benchmarkCondition = new JLabel("Please select a metric and click 'Run' button...");
//		SN_benchmarkCondition.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		SN_benchmarkCondition.setHorizontalTextPosition(SwingConstants.CENTER);
//		SN_benchmarkCondition.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_benchmarkCondition.setBounds(293, 344, 771, 15);
//		snbound.add(SN_benchmarkCondition);
//
//		SN_progressBarTotal = new JProgressBar();
//		SN_progressBarTotal.setStringPainted(true);
//		SN_progressBarTotal.setBackground(Color.WHITE);
//		SN_progressBarTotal.setBounds(460, 321, 573, 13);
//		snbound.add(SN_progressBarTotal);
//
//		JLabel lblCurrentProgress = new JLabel("Current Progress");
//		lblCurrentProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		lblCurrentProgress.setBounds(309, 285, 141, 15);
//		snbound.add(lblCurrentProgress);
//
//		JLabel lblTotalProgress = new JLabel("Total Progress");
//		lblTotalProgress.setFont(new Font("Times New Roman", Font.BOLD, 12));
//		lblTotalProgress.setBounds(309, 319, 141, 15);
//		snbound.add(lblTotalProgress);
//
//		JPanel panel_1 = new JPanel();
//		panel_1.setLayout(null);
//		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
//
//				null, null));
//		panel_1.setBounds(309, 32, 724, 101);
//		snbound.add(panel_1);
//
//		JLabel lblNumberOfSwitch = new JLabel("Number of Switch");
//		lblNumberOfSwitch.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblNumberOfSwitch.setBounds(10, 40, 100, 18);
//		panel_1.add(lblNumberOfSwitch);
//
//		SN_numberOfS = new JTextField();
//		SN_numberOfS.setText("10");
//		SN_numberOfS.setMargin(new Insets(3, 3, 3, 3));
//		SN_numberOfS.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_numberOfS.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_numberOfS.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_numberOfS.setAutoscrolls(false);
//		SN_numberOfS.setBounds(191, 40, 79, 18);
//		panel_1.add(SN_numberOfS);
//
//		SN_comboBoxOFProtocolType = new JComboBox();
//		SN_comboBoxOFProtocolType.setModel(new DefaultComboBoxModel(new String[] { "1.0", "1.1", "1.2", "1.3", "1.4" }));
//		SN_comboBoxOFProtocolType.setSelectedIndex(3);
//		SN_comboBoxOFProtocolType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_comboBoxOFProtocolType.setBounds(191, 10, 79, 20);
//		panel_1.add(SN_comboBoxOFProtocolType);
//
//		JLabel lblOfProtocolVer = new JLabel("OF Protocol Version");
//		lblOfProtocolVer.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblOfProtocolVer.setBounds(10, 10, 100, 20);
//		panel_1.add(lblOfProtocolVer);
//
//		SN_hostIP_1 = new JTextField();
//		SN_hostIP_1.setText("10");
//		SN_hostIP_1.setMargin(new Insets(3, 3, 3, 3));
//		SN_hostIP_1.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_hostIP_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_hostIP_1.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_hostIP_1.setAutoscrolls(false);
//		SN_hostIP_1.setBounds(479, 40, 28, 18);
//		panel_1.add(SN_hostIP_1);
//
//		JLabel lblHostIp = new JLabel("Host IP start from");
//		lblHostIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblHostIp.setBounds(370, 40, 99, 18);
//		panel_1.add(lblHostIp);
//
//		SN_hostIP_2 = new JTextField();
//		SN_hostIP_2.setText("0");
//		SN_hostIP_2.setMargin(new Insets(3, 3, 3, 3));
//		SN_hostIP_2.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_hostIP_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_hostIP_2.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_hostIP_2.setAutoscrolls(false);
//		SN_hostIP_2.setBounds(530, 40, 28, 18);
//		panel_1.add(SN_hostIP_2);
//
//		SN_textField_3 = new JTextField();
//		SN_textField_3.setEditable(false);
//		SN_textField_3.setText("0");
//		SN_textField_3.setMargin(new Insets(3, 3, 3, 3));
//		SN_textField_3.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_textField_3.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_textField_3.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_textField_3.setAutoscrolls(false);
//		SN_textField_3.setBounds(584, 40, 28, 18);
//		panel_1.add(SN_textField_3);
//
//		SN_textField_4 = new JTextField();
//		SN_textField_4.setEditable(false);
//		SN_textField_4.setText("2");
//		SN_textField_4.setMargin(new Insets(3, 3, 3, 3));
//		SN_textField_4.setHorizontalAlignment(SwingConstants.CENTER);
//		SN_textField_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_textField_4.setBorder(new LineBorder(new Color(0, 0, 0)));
//		SN_textField_4.setAutoscrolls(false);
//		SN_textField_4.setBounds(638, 40, 28, 18);
//		panel_1.add(SN_textField_4);
//
//		JLabel label_1 = new JLabel(".");
//		label_1.setBounds(517, 41, 6, 15);
//		panel_1.add(label_1);
//
//		JLabel label_2 = new JLabel(".");
//		label_2.setBounds(568, 41, 6, 15);
//		panel_1.add(label_2);
//
//		JLabel label_3 = new JLabel(".");
//		label_3.setBounds(622, 41, 6, 15);
//		panel_1.add(label_3);
//
//		SN_comboBoxTopoType = new JComboBox();
//		SN_comboBoxTopoType.setModel(new DefaultComboBoxModel(new String[] { "Linear", "Ring", "Mininet" }));
//		SN_comboBoxTopoType.setSelectedIndex(0);
//		SN_comboBoxTopoType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_comboBoxTopoType.setBounds(517, 10, 79, 20);
//		panel_1.add(SN_comboBoxTopoType);
//
//		JLabel label_4 = new JLabel("Topology Type");
//		label_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		label_4.setBounds(370, 10, 88, 20);
//		panel_1.add(label_4);
//
//		JLabel lblPacketType = new JLabel("Asynchronous Message Type");
//		lblPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblPacketType.setBounds(10, 69, 171, 20);
//		panel_1.add(lblPacketType);
//
//		SN_packetComboBox = new JComboBox();
//		SN_packetComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP", "ECHO", "ARP" }));
//		SN_packetComboBox.setSelectedIndex(0);
//		SN_packetComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_packetComboBox.setBounds(191, 68, 79, 20);
//		panel_1.add(SN_packetComboBox);
//
//		JLabel lblUnknownPacketType = new JLabel("Unknown Packet Type");
//		lblUnknownPacketType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		lblUnknownPacketType.setBounds(370, 68, 144, 20);
//		panel_1.add(lblUnknownPacketType);
//
//		SN_provisioningComboBox = new JComboBox();
//		SN_provisioningComboBox.setModel(new DefaultComboBoxModel(new String[] { "TCP", "UDP" }));
//		SN_provisioningComboBox.setSelectedIndex(0);
//		SN_provisioningComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
//		SN_provisioningComboBox.setBounds(518, 69, 78, 20);
//		panel_1.add(SN_provisioningComboBox);
//
//		try {
//			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) {
//					javax.swing.UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//			}
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
