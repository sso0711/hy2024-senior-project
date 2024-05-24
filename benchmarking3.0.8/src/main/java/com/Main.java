package com;

import com.mir.ui.GUI;

import java.awt.*;

public class Main {

	public static GUI gui;

	/**
	 * Launch the application. main 함수
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui = new GUI();
					gui.frmMirlabBenchmark.setVisible(true); // JFrame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}                                      
		});
	}
}