package com.mir.ui;

import com.Main;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.global.Global;

//更新进度条的class

public class ProgressUpdate extends Thread {

	private int numberOfMetric;
	private int currentNum;

	public void updateStart(int numberOfMetric, int currentNum) {

		this.numberOfMetric = numberOfMetric;
		this.currentNum = currentNum;

		this.start();
	}

	@Override
	public void run() {

		if (Global.southboundMetric == SouthboundMetric.CONTROL_SESSION_CAPACITY_CCD
				|| Global.southboundMetric == SouthboundMetric.NETWORK_DISCOVERY_SIZE_NS
				|| Global.southboundMetric == SouthboundMetric.FORWARDING_TABLE_CAPACITY_NRP
				|| Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_RATE
				|| Global.southboundMetric == SouthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE) {
			Main.gui.S_progressBar.setValue(1);
			while (!(Main.gui.S_progressBar.getValue() == Main.gui.S_progressBar.getMaximum())) {
				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: " + "???" + " Second" + " .");

				try {
					Thread.sleep(333);// 休眠333
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: " + "???" + " Second" + " ..");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: " + "???" + " Second" + " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
			Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
					+ currentNum + "/" + numberOfMetric + "        Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");

		} else if (Global.southboundMetric == SouthboundMetric.CONTROLLER_FAILOVER_TIME_RELIABILITY) {

		} else {

			for (int i = 1; i < Main.gui.S_progressBar.getMaximum() + 1; i++) {
				// condition hint

				Main.gui.S_progressBar.setValue(i);
				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.S_progressBar.getMaximum() - Main.gui.S_progressBar.getValue()) + " Second" + " .");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.S_progressBar.getMaximum() - Main.gui.S_progressBar.getValue()) + " Second"
						+ " ..");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
						+ currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.S_progressBar.getMaximum() - Main.gui.S_progressBar.getValue()) + " Second"
						+ " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			Main.gui.S_benchmarkCondition.setText("Metric: " + Global.southboundMetric + "      Task Progress: "
					+ currentNum + "/" + numberOfMetric + "          Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");
		}
	}
}
