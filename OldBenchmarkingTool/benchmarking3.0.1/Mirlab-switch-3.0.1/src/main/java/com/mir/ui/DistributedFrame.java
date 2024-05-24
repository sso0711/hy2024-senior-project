package com.mir.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import com.mir.distributed.client.DistributedClient;
import com.mir.distributed.server.DistributedServer;
import com.mirlab.global.Global;

public class DistributedFrame {

	public JFrame frmDistrubutedBenchmarkReport;
	private JTextField ipText;
	public static JTable table;
	public static JTable table_1;
	private JComboBox comboBoxBenchmarkT;
	public static JButton btnNewButton;
	public static JButton btnStop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DistributedFrame window = new DistributedFrame();
					window.frmDistrubutedBenchmarkReport.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DistributedFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDistrubutedBenchmarkReport = new JFrame();
		frmDistrubutedBenchmarkReport.setTitle("Distrubuted Benchmark");
		frmDistrubutedBenchmarkReport.setResizable(false);
		frmDistrubutedBenchmarkReport.setBounds(100, 100, 1080, 739);
		frmDistrubutedBenchmarkReport.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmDistrubutedBenchmarkReport.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel.setBounds(39, 70, 514, 102);
		frmDistrubutedBenchmarkReport.getContentPane().add(panel);

		comboBoxBenchmarkT = new JComboBox();
		comboBoxBenchmarkT.setModel(new DefaultComboBoxModel(new String[] { "Master", "Slave" }));
		comboBoxBenchmarkT.setSelectedIndex(0);
		comboBoxBenchmarkT.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		comboBoxBenchmarkT.setBounds(120, 10, 79, 20);
		panel.add(comboBoxBenchmarkT);

		JLabel lblBenchmarkType = new JLabel("Benchmark Type");
		lblBenchmarkType.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblBenchmarkType.setBounds(10, 10, 100, 20);
		panel.add(lblBenchmarkType);

		JLabel lblMasterIp = new JLabel("Master IP");
		lblMasterIp.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblMasterIp.setBounds(255, 10, 72, 20);
		panel.add(lblMasterIp);

		ipText = new JTextField();
		ipText.setText("127.0.0.1:8911");
		ipText.setMargin(new Insets(3, 3, 3, 3));
		ipText.setHorizontalAlignment(SwingConstants.CENTER);
		ipText.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		ipText.setBorder(new LineBorder(new Color(0, 0, 0)));
		ipText.setAutoscrolls(false);
		ipText.setBounds(337, 11, 161, 18);
		panel.add(ipText);

		btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				btnNewButton.setEnabled(false);
				btnStop.setEnabled(true);

				// Server
				if (comboBoxBenchmarkT.getSelectedIndex() == 0) {
					Global.IS_ENABLE_DISTRIBUTED = true;
					Global.IS_MASTER = true;

					DistributedServer ds = new DistributedServer();
					ds.start();

				}
				// Client
				else {
					Global.IS_ENABLE_DISTRIBUTED = true;
					Global.IS_MASTER = false;
					StringTokenizer st = new StringTokenizer(ipText.getText().toString(), ":");
					Global.MASTER_IP = st.nextToken();
					Global.MASTER_PORT = Integer.valueOf(st.nextToken());

					DistributedClient ds = new DistributedClient();
					ds.start();
				}

			}
		});
		btnNewButton.setBounds(120, 54, 93, 23);
		panel.add(btnNewButton);

		btnStop = new JButton("Stop");
		btnStop.setBounds(301, 54, 93, 23);
		btnStop.setEnabled(false);
		panel.add(btnStop);

		JLabel lblDistributedBenchmarkConfiguration = new JLabel("Distributed Benchmark Configuration");
		lblDistributedBenchmarkConfiguration.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblDistributedBenchmarkConfiguration.setIcon(
				new ImageIcon(DistributedFrame.class.getResource("/com/alee/extended/filechooser/icons/settings.png")));
		lblDistributedBenchmarkConfiguration.setBounds(39, 48, 304, 15);
		frmDistrubutedBenchmarkReport.getContentPane().add(lblDistributedBenchmarkConfiguration);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(563, 70, 482, 102);
		frmDistrubutedBenchmarkReport.getContentPane().add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null }, { null, null }, { null, null }, },
				new String[] { "IP", "Benchmark Type" }));
		scrollPane.setViewportView(table);

		JLabel lblConnectionState = new JLabel("Connection State");
		lblConnectionState.setIcon(
				new ImageIcon(DistributedFrame.class.getResource("/com/alee/examples/content/icons/presentation.png")));
		lblConnectionState.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblConnectionState.setBounds(563, 47, 304, 15);
		frmDistrubutedBenchmarkReport.getContentPane().add(lblConnectionState);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(39, 213, 1000, 455);
		frmDistrubutedBenchmarkReport.getContentPane().add(scrollPane_1);

		table_1 = new JTable();
		table_1.setRowHeight(25);
		table_1.setModel(new DefaultTableModel(
				new Object[][] { { "Topology Discovery Time", "ms", null, null, null, null },
						{ "Topology Change Detection(Link Up)", "ms", null, null, null, null },
						{ "Topology Change Detection(Link Down)", "ms", null, null, null, null },
						{ "Asynchronous Message Processing Time", "ms", null, null, null, null },
						{ "Asynchronous Message Processing Rate", "Msg/s", null, null, null, null },
						{ "Reactive Path Provisioning Time", "ms", null, null, null, null },
						{ "Reactive Path Provisioning Rate", "Flows/s", null, null, null, null },
						{ "Proactive Path Provisioning Time", "ms", "N/A", "N/A", "N/A", "N/A" },
						{ "Proactive Path Provisioning Rate", "Flows/s", "N/A", "N/A", "N/A", "N/A" },
						{ "Control Session Capacity", "Sessions", null, null, null, null },
						{ "Network Discovery Size", "Ovs", null, null, null, null },
						{ "Forwarding Table Capacity", "Flows", "N/A", "N/A", "N/A", "N/A" },
						{ "DoS Attacks", null, "N/A", "N/A", "N/A", "N/A" },
						{ "Controller Failover Time", null, "N/A", "N/A", "N/A", "N/A" },
						{ "Exception Handing", null, null, null, null, null }, },
				new String[] { "Metric", "Unit", "Master Result", "Slave 1 Result", "Slave 2 Result",
						"Slave 3 Result" }));
		table_1.getColumnModel().getColumn(0).setResizable(false);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(191);
		scrollPane_1.setViewportView(table_1);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 31, 1054, 2);
		frmDistrubutedBenchmarkReport.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 678, 1054, 2);
		frmDistrubutedBenchmarkReport.getContentPane().add(separator_1);
	}
}
