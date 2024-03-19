package com;

import java.awt.EventQueue;

import com.mir.ui.GUI;

public class Main {

	public static GUI gui;

	/**
	 * Launch the application.主函数
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui = new GUI();
					gui.frmMirlabBenchmark.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}                                      
		});
	}
}