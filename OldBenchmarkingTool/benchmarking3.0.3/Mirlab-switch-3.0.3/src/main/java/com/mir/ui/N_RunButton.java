package com.mir.ui;

import javax.swing.JOptionPane;

import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月11日 上午9:58:03 类说明
 */
public class N_RunButton {
	public static void go() {
		try {
			Initializer.TEST_CONTROLLER_IF_IT_IS_OPEN();// 创建一个node链接controller？
			Thread.sleep(3000);

		} catch (InterruptedException e) {
			Global.CONTROLLER_IS_OPEN = false;
			e.printStackTrace();
		}

		if (Global.CONTROLLER_IS_OPEN) {// controller 在线

			startTasks();

		} else {// controller 不在线
			Initializer.INITIAL_CHANNEL_POOL();
			System.out.println("Controller is offline!");
			JOptionPane.showMessageDialog(null, "Please check if controller is running.", "Disconnected",
					JOptionPane.ERROR_MESSAGE);// 跳出新窗口

		}
	}

	public static void startTasks() {

	}
}
