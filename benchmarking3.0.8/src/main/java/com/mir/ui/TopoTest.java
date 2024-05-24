package com.mir.ui;

import javax.swing.*;

import java.awt.*;

class TopoTest extends JPanel {
	private int nm;
	private int topoType;

	TopoTest(int nm, int topoType) {
		this.nm = nm;
		this.topoType = topoType;
	}

	public void paint(Graphics g) {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image icon = toolkit.getImage("image/1.png");
		int count_x = 0;
		int count_y = 0;
		int x = 50;
		int y = 50;

		for (int i = 1; i < nm; i++) {

			if (count_x == 5) {
				count_x = 1;
				x = x + count_x * 50;
				y = y + count_x * 50;
				x = 20;
				g.drawImage(icon, x, y, 50, 50, null);// 以坐标30，30的点画半径为50像素的圆
				count_x++;
			} else {
				x = x + count_x * 50;
				g.drawImage(icon, x, y, 50, 50, null);
				count_x++;
			}
		}
	}

}