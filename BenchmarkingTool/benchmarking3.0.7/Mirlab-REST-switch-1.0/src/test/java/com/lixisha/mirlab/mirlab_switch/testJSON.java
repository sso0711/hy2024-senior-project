package com.lixisha.mirlab.mirlab_switch;

import org.json.JSONObject;

import com.mirlab.enumType.MetricType;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 上午11:49:40 类说明
 */
public class testJSON {

	public static void main(String[] args) {
		JSONObject a = new JSONObject();

		a.put("type", MetricType.NORTHBOUND);

		MetricType temp = MetricType.valueOf(a.get("type").toString());

		System.out.println(temp);
		System.out.println(a.get("type").toString());

	}

}
