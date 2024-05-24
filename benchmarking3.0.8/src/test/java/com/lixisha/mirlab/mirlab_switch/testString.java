package com.lixisha.mirlab.mirlab_switch;

import org.json.JSONObject;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月19日 上午2:13:01 类说明
 */
public class testString {
	public static void main(String[] args) {
		String a = "0";
		String b = "0";

		System.out.println(a == b);

		JSONObject message = new JSONObject();

		message.put("msgType", "0");
		System.out.println(message.getString("msgType") == "0");

	}
}
