package com.mirlab.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Main;
import com.mirlab.global.Global;

public class Log {

	public static void ADD_LOG(String str) {
		Main.gui.S_LOG_TEXT.append(str + "\n");
		Main.gui.S_LOG_TEXT.setCaretPosition(Main.gui.S_LOG_TEXT.getText().length());
	}

	public static void ADD_LOG_PANEL(String str, String classInfo) {
		Date date = new Date();
		Main.gui.S_LOG_TEXT.append("[" + date.toString() + "] " + "[" + classInfo + "]: " + str + "\n");
		Main.gui.S_LOG_TEXT.setCaretPosition(Main.gui.S_LOG_TEXT.getText().length());

	}
	
	public static void N_ADD_LOG_PANEL(String str, String classInfo) {
		Date date = new Date();
		Main.gui.N_LOG_TEXT.append("[" + date.toString() + "] " + "[" + classInfo + "]: " + str + "\n");
		Main.gui.N_LOG_TEXT.setCaretPosition(Main.gui.N_LOG_TEXT.getText().length());

	}
	
	public static void WR_ADD_LOG_PANEL(String str, String classInfo) {
		Date date = new Date();
		Main.gui.SN_LOG_TEXT.append("[" + date.toString() + "] " + "[" + classInfo + "]: " + str + "\n");
		Main.gui.SN_LOG_TEXT.setCaretPosition(Main.gui.SN_LOG_TEXT.getText().length());

	}

	public static void exportLog(String metricName, int metricIndex) {
		exportLog(metricName);
		exportLogSummary(metricName, metricIndex);

	}

	private static void exportLog(String metricName) {
		// TODO Auto-generated method stub

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd_HH-mm");
		String dataString = formatter.format(date);

		String address = "logs\\" + metricName + "\\" + dataString + ".txt";

		File file = new File(address);
		BufferedWriter bw = null;

		try {

			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream os = new FileOutputStream(address);
			bw = new BufferedWriter(new OutputStreamWriter(os));
			for (String value : Main.gui.S_LOG_TEXT.getText().split("\n")) {
				bw.write(value);
				bw.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void exportLogSummary(String metricName, int metricIndex) {
		String value = null;
		String address = "logs\\" + metricName + "\\summary.txt";

		File file = new File(address);
		BufferedWriter bw = null;

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd_HH-mm");
		String dataString = formatter.format(date);

		try {

			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream os = new FileOutputStream(address, true);
			bw = new BufferedWriter(new OutputStreamWriter(os));

			value = dataString + "/" + metricName + "/" + Main.gui.S_comboBoxTopoType.getSelectedItem().toString() + "/"
					+ Global.NUMBER_OF_TEST_SWITCH + "/"
					+ Main.gui.S_RESULT_TABLE.getModel().getValueAt(metricIndex, 2);
			bw.write(value);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
