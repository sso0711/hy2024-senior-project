package com.mirlab.protocol;

import java.util.StringTokenizer;

import com.mirlab.global.Global;

public class Ip {
	private byte version;
	private byte differentiatedSF;
	private byte[] totalLength; // 2
	private byte[] iD; // 2
	private byte flags;
	private byte fragmentOffset;
	private byte ttl;
	private byte protocolType;
	private byte[] checksum; // 2
	private byte[] srcIp; // 4
	private byte[] dstIp; // 4
	//private byte[] srcGeoIp; // 4
//	private byte[] dstGeoIp; // 4

	// private byte[] data = new byte[30];

	public Ip() {
		version = 0x45;

		differentiatedSF = 0x00;

		totalLength = new byte[2];// 2
		totalLength[0] = 0x00;
		totalLength[1] = 0x2E;

		iD = new byte[2];// 2
		iD[0] = 0x00;
		iD[1] = 0x00;

		flags = 0x00;
		fragmentOffset = 0x00;

		ttl = 0x40;

		protocolType = (byte) 0x11;

		checksum = new byte[2];// 2
		checksum[0] = (byte) 0xf7;
		checksum[1] = (byte) 0x2c;

		srcIp = new byte[4]; // 4
		dstIp = new byte[4]; // 4

	}

	public void setSrcIp(String str) {
		StringTokenizer st = new StringTokenizer(str, ".");
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseUnsignedInt(st.nextToken(), 10);
			byte tempByte = (byte) tempInt;
			this.srcIp[i] = tempByte;
		}
	}

	public void setDstIp(String str) {
		StringTokenizer st = new StringTokenizer(str, ".");
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseUnsignedInt(st.nextToken(), 10);
			byte tempByte = (byte) tempInt;
			this.dstIp[i] = tempByte;
		}
	}
	
	public void setProtocol(int protocolType){
		if(protocolType == Global.TCP_PACKET){
			this.protocolType = (byte) 0x06;
		}else if(protocolType == Global.UDP_PACKET){
			this.protocolType = (byte) 0x11;
		}
	}

	public byte[] toByte() {
		byte total[] = new byte[50];
		total[0] = version;

		total[1] = differentiatedSF;

		total[2] = totalLength[0];

		total[3] = totalLength[1];

		total[4] = iD[0];
		total[5] = iD[1];

		total[6] = flags;
		total[7] = fragmentOffset;

		total[8] = ttl;

		total[9] = protocolType;

		total[10] = checksum[0];
		total[11] = checksum[1];

		for (int i = 0; i < 4; i++) {
			total[12 + i] = srcIp[i];
			total[16 + i] = dstIp[i];
		}
		
		//data
		/*
		for (int i = 0; i < 30; i++) {
			total[20+i] = 0x11;
		}
		*/

		return total;
	}

}
