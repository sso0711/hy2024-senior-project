package com.mirlab.lib;

import java.util.Date;

import com.mir.ui.Main;

public class Log {

	public static void ADD_LOG(String str) {
		Main.S_LOG_TEXT.append(str + "\n");
		Main.S_LOG_TEXT.setCaretPosition(Main.S_LOG_TEXT.getText().length());
	}

	public static void ADD_LOG_PANEL(String str, String classInfo) {
		Date date = new Date();
		Main.S_LOG_TEXT.append("[" + date.toString() + "] " + "[" + classInfo + "]: " + str + "\n");
		Main.S_LOG_TEXT.setCaretPosition(Main.S_LOG_TEXT.getText().length());

	}

}
