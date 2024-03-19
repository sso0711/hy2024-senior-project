package com.mir.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigurationFrame {

	public JFrame frmConfiguration;
	public JTextField minSample;
	public JTextField maxSample;
	public JTextField gapTime;
	public JTextField bulkSize;
	public JTextField hostSize;
	public JTextField packetLossRate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigurationFrame window = new ConfigurationFrame();
					window.frmConfiguration.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConfigurationFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConfiguration = new JFrame();
		frmConfiguration.setTitle("Configuration");
		frmConfiguration.setResizable(false);
		frmConfiguration.setBounds(100, 100, 450, 300);
		frmConfiguration.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmConfiguration.getContentPane().setLayout(null);

		JLabel label = new JLabel(" Threshold margine(ms)");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label.setBounds(79, 58, 126, 20);
		frmConfiguration.getContentPane().add(label);

		JLabel label_1 = new JLabel("Valid Sample Size");
		label_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_1.setBounds(83, 88, 100, 20);
		frmConfiguration.getContentPane().add(label_1);

		minSample = new JTextField();
		minSample.setText("200");
		minSample.setMargin(new Insets(3, 3, 3, 3));
		minSample.setHorizontalAlignment(SwingConstants.CENTER);
		minSample.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		minSample.setBorder(new LineBorder(new Color(0, 0, 0)));
		minSample.setAutoscrolls(false);
		minSample.setBounds(248, 89, 36, 18);
		frmConfiguration.getContentPane().add(minSample);

		maxSample = new JTextField();
		maxSample.setText("35000");
		maxSample.setMargin(new Insets(3, 3, 3, 3));
		maxSample.setHorizontalAlignment(SwingConstants.CENTER);
		maxSample.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		maxSample.setBorder(new LineBorder(new Color(0, 0, 0)));
		maxSample.setAutoscrolls(false);
		maxSample.setBounds(313, 89, 43, 18);
		frmConfiguration.getContentPane().add(maxSample);

		gapTime = new JTextField();
		gapTime.setText("10");
		gapTime.setMargin(new Insets(3, 3, 3, 3));
		gapTime.setHorizontalAlignment(SwingConstants.CENTER);
		gapTime.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		gapTime.setBorder(new LineBorder(new Color(0, 0, 0)));
		gapTime.setAutoscrolls(false);
		gapTime.setBounds(248, 59, 108, 18);
		frmConfiguration.getContentPane().add(gapTime);

		bulkSize = new JTextField();
		bulkSize.setText("1");
		bulkSize.setMargin(new Insets(3, 3, 3, 3));
		bulkSize.setHorizontalAlignment(SwingConstants.CENTER);
		bulkSize.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bulkSize.setBorder(new LineBorder(new Color(0, 0, 0)));
		bulkSize.setAutoscrolls(false);
		bulkSize.setBounds(251, 176, 105, 18);
		frmConfiguration.getContentPane().add(bulkSize);

		JLabel bulkSizet = new JLabel("Auto-find Bulk Size Start From");
		bulkSizet.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bulkSizet.setBounds(83, 175, 158, 20);
		frmConfiguration.getContentPane().add(bulkSizet);

		JLabel lblNumberOfHost = new JLabel("Number of Host(1~250/port)");
		lblNumberOfHost.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblNumberOfHost.setBounds(83, 146, 144, 20);
		frmConfiguration.getContentPane().add(lblNumberOfHost);

		hostSize = new JTextField();
		hostSize.setText("100");
		hostSize.setMargin(new Insets(3, 3, 3, 3));
		hostSize.setHorizontalAlignment(SwingConstants.CENTER);
		hostSize.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		hostSize.setBorder(new LineBorder(new Color(0, 0, 0)));
		hostSize.setAutoscrolls(false);
		hostSize.setBounds(251, 147, 105, 18);
		frmConfiguration.getContentPane().add(hostSize);

		JLabel lblPacketLossRate = new JLabel("Packet Loss Rate Threshold(1)");
		lblPacketLossRate.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblPacketLossRate.setBounds(83, 118, 158, 20);
		frmConfiguration.getContentPane().add(lblPacketLossRate);

		packetLossRate = new JTextField();
		packetLossRate.setText("0.05");
		packetLossRate.setMargin(new Insets(3, 3, 3, 3));
		packetLossRate.setHorizontalAlignment(SwingConstants.CENTER);
		packetLossRate.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		packetLossRate.setBorder(new LineBorder(new Color(0, 0, 0)));
		packetLossRate.setAutoscrolls(false);
		packetLossRate.setBounds(251, 118, 105, 18);
		frmConfiguration.getContentPane().add(packetLossRate);

		JLabel label_5 = new JLabel("~");
		label_5.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		label_5.setBounds(292, 88, 11, 20);
		frmConfiguration.getContentPane().add(label_5);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 27, 424, 2);
		frmConfiguration.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 244, 424, 2);
		frmConfiguration.getContentPane().add(separator_1);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmConfiguration.setVisible(false);
			}
		});
		btnNewButton.setBounds(178, 211, 93, 23);
		frmConfiguration.getContentPane().add(btnNewButton);
	}
}
