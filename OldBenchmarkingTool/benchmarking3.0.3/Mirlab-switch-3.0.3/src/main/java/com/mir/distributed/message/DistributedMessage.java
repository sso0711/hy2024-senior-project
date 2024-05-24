package com.mir.distributed.message;

import org.json.JSONObject;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月13日 下午8:23:36 类说明
 */
public enum DistributedMessage {
	// Singleton*********************************
	INSTANCE;

	private DistributedMessage() {

	}

	public JSONObject msgType0(int BenchmarkingTool_ID) {

		JSONObject j = new JSONObject();

		j.put("msgType", "0");
		j.put("BenchmarkingTool_ID", BenchmarkingTool_ID);

		return j;

	}
}
