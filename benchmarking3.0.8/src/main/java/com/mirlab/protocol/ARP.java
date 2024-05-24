package com.mirlab.protocol;

import java.util.StringTokenizer;

public class ARP {
	private byte[] hardwareType;
	private byte[] protocolType;
	private byte hardwareSize;
	private byte protocolSize;
	private byte[] opCode;
	
	private byte[] sendMac;
	private byte[] sendIp;
	
	private byte[] targetMac;
	private byte[] targetIp;

	public ARP(){
		hardwareType = new byte[2];
		hardwareType[0] = 0x00;
		hardwareType[1] = 0x01;
		
		protocolType = new byte[2];
		protocolType[0] = 0x08;
		protocolType[1] = 0x00;
		
		opCode = new byte[2];
		
		hardwareSize = 0x06;
		protocolSize = 0x04;
		
		sendMac = new byte[6];
		sendIp = new byte[4];
		targetMac = new byte[6];
		targetIp = new byte[4];
		
	}
	

	public void setOpCode(int code) {
		opCode[0] = 0x00;
		opCode[1] = (byte)code;
	}

	public void setSendIp(String str) {
		StringTokenizer st = new StringTokenizer(str, ".");
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseUnsignedInt(st.nextToken(),10);
			byte tempByte = (byte) tempInt;
			this.sendIp[i] = tempByte;
		}
	}

	public void setTargetIp(String str) {
		StringTokenizer st = new StringTokenizer(str, ".");
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseUnsignedInt(st.nextToken(),10);
			byte tempByte = (byte) tempInt;
			this.targetIp[i] = tempByte;
		}
	}
	
	public void setTargetMac(String str) {
		StringTokenizer st = new StringTokenizer(str, ":");
		for (int i = 0; i < 6; i++) {
			int temInt = Integer.parseUnsignedInt(st.nextToken(), 16);
			
			byte tempByte = (byte) temInt;
			
			this.targetMac[i] = tempByte;
		}
	}

	public void setSendMac(String str) {
		StringTokenizer st = new StringTokenizer(str, ":");
		for (int i = 0; i < 6; i++) {
			int temInt = Integer.parseUnsignedInt(st.nextToken(), 16);
			
			byte tempByte = (byte) temInt;

			this.sendMac [i] = tempByte;
		}
	}
	
	public byte[] toByte() {
		byte total[] = new byte[28];
		
		total[0] = this.hardwareType[0];
		total[1] = this.hardwareType[1];
		
		total[2] = this.protocolType[0];
		total[3] = this.protocolType[1];
		
		total[4] = this.hardwareSize;
		total[5] = this.protocolSize;
		
		total[6] = this.opCode[0];
		total[7] = this.opCode[1];
		
		for(int i = 0; i<6;i++){
			total[8+i] = this.sendMac[i];
			total[18+i] = this.targetMac[i];
		}
		
		for(int i = 0; i<4;i++){
			total[14+i] = this.sendIp[i];
			total[24+i] = this.targetIp[i];
		}
		
		
		
		
		return total;
	}

	

}
