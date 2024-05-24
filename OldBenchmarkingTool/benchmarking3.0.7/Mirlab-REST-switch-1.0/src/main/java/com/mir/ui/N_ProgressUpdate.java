package com.mir.ui;

import com.Main;
import com.mirlab.enumType.NorthboundMetric;
import com.mirlab.global.Global;

//更新进度条的class

public class N_ProgressUpdate extends Thread {

	private int numberOfMetric;
	private int currentNum;

	public void NupdateStart(int numberOfMetric, int currentNum) {

		this.numberOfMetric = numberOfMetric;
		this.currentNum = currentNum;

		this.start();
	}

	@Override
	public void run() {

		if (Global.northboundMetric == NorthboundMetric.CONTROL_SESSION_CAPACITY_CCD
				|| Global.northboundMetric == NorthboundMetric.NETWORK_DISCOVERY_SIZE_NS
				|| Global.northboundMetric == NorthboundMetric.FORWARDING_TABLE_CAPACITY_NRP
				|| Global.northboundMetric == NorthboundMetric.REACTIVE_PATH_PROVISIONING_RATE
				|| Global.northboundMetric == NorthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE) {
			Main.gui.N_progressBar.setValue(1);
			while (!(Main.gui.N_progressBar.getValue() == Main.gui.N_progressBar.getMaximum())) {
				Main.gui.N_benchmarkCondition.setText(
						"Metric: " + Global.northboundMetric + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " .");

				try {
					Thread.sleep(333);// 休眠333
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				Main.gui.N_benchmarkCondition.setText(
						"Metric: " + Global.northboundMetric + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " ..");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				Main.gui.N_benchmarkCondition.setText(
						"Metric: " + Global.northboundMetric + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
			Main.gui.N_benchmarkCondition.setText("Metric: " + Global.northboundMetric
					+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "        Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");

		} else if (Global.northboundMetric == NorthboundMetric.CONTROLLER_FAILOVER_TIME_RELIABILITY) {

		} else {

			for (int i = 1; i < Main.gui.N_progressBar.getMaximum() + 1; i++) {
				// condition hint

				Main.gui.N_progressBar.setValue(i);
				Main.gui.N_benchmarkCondition.setText("Metric: " + Global.northboundMetric
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.N_progressBar.getMaximum() - Main.gui.N_progressBar.getValue()) + " Second" + " .");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main.gui.N_benchmarkCondition.setText("Metric: " + Global.northboundMetric
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.N_progressBar.getMaximum() - Main.gui.N_progressBar.getValue()) + " Second" + " ..");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Main.gui.N_benchmarkCondition.setText("Metric: " + Global.northboundMetric
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.gui.N_progressBar.getMaximum() - Main.gui.N_progressBar.getValue()) + " Second" + " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			Main.gui.N_benchmarkCondition.setText("Metric: " + Global.northboundMetric
					+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "          Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");
		}
	}
}
