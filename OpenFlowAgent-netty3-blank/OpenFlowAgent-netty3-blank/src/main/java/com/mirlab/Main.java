package com.mirlab;

import com.mirlab.component.Agent;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version ����ʱ�䣺2018��1��3�� ����11:19:17 ��˵��
 */
public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Agent agent1 = new Agent(1L);
		//Agent agent2 = new Agent(2L);

		//agent1.createPort(2);
		//agent2.createPort(2);

		//agent1.getPort(1).setConnectedPort(agent2.getPort(2));

		agent1.startOpenFlowClient();
		//agent2.startOpenFlowClient();
	}

}
