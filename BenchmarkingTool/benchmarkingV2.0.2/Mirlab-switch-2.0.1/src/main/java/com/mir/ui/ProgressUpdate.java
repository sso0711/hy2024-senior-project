package com.mir.ui;

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

		if (Global.TEST_METRIC == Global.CONTROL_SESSION_CAPACITY_CCD
				|| Global.TEST_METRIC == Global.NETWORK_DISCOVERY_SIZE_NS
				|| Global.TEST_METRIC == Global.FORWARDING_TABLE_CAPACITY_NRP
				|| Global.TEST_METRIC == Global.REACTIVE_PATH_PROVISIONING_RATE
				|| Global.TEST_METRIC == Global.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE) {
			Main.S_progressBar.setValue(1);
			while (!(Main.S_progressBar.getValue() == Main.S_progressBar.getMaximum())) {
				Main.S_benchmarkCondition.setText(
						"Metric: " + Global.METRIC_NAME[Global.TEST_METRIC] + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " .");

				try {
					Thread.sleep(333);// 休眠333
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				Main.S_benchmarkCondition.setText(
						"Metric: " + Global.METRIC_NAME[Global.TEST_METRIC] + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " ..");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				Main.S_benchmarkCondition.setText(
						"Metric: " + Global.METRIC_NAME[Global.TEST_METRIC] + "      Task Progress: " + currentNum + "/"
								+ numberOfMetric + "      Expected Time: " + "???" + " Second" + " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
			Main.S_benchmarkCondition.setText("Metric: " + Global.METRIC_NAME[Global.TEST_METRIC]
					+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "        Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");

		} else if (Global.TEST_METRIC == 15) {

		} else {

			for (int i = 1; i < Main.S_progressBar.getMaximum() + 1; i++) {
				// condition hint

				Main.S_progressBar.setValue(i);
				Main.S_benchmarkCondition.setText("Metric: " + Global.METRIC_NAME[Global.TEST_METRIC]
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.S_progressBar.getMaximum() - Main.S_progressBar.getValue()) + " Second" + " .");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main.S_benchmarkCondition.setText("Metric: " + Global.METRIC_NAME[Global.TEST_METRIC]
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.S_progressBar.getMaximum() - Main.S_progressBar.getValue()) + " Second" + " ..");
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Main.S_benchmarkCondition.setText("Metric: " + Global.METRIC_NAME[Global.TEST_METRIC]
						+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "      Expected Time: "
						+ (Main.S_progressBar.getMaximum() - Main.S_progressBar.getValue()) + " Second" + " ...");

				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			Main.S_benchmarkCondition.setText("Metric: " + Global.METRIC_NAME[Global.TEST_METRIC]
					+ "      Task Progress: " + currentNum + "/" + numberOfMetric + "          Time Cost: "
					+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000) + "s");
		}
	}
}
