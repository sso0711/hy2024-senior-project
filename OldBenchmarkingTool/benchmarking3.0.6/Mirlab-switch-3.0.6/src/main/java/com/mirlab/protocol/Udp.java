package com.mirlab.protocol;

public class Udp {
	private byte srcPort[];  //2
	private byte dstPort[]; //2
	private byte length[]; //2
	private byte checksum[]; //2
	
	//private byte data[]; //12
	
	public Udp(){
		srcPort = new byte[2];
		dstPort = new byte[2];
		length = new byte[2];
		checksum = new byte[2];
		length[0] = (byte) 0x00;
		length[1] = (byte) 0x14;
		checksum[0] = (byte) 0x19;
		checksum[1] = (byte) 0xda;
		
	}
	
	public void setSrcPort(int srcPort) {
		this.srcPort[1] = (byte) (0xff & srcPort);
		this.srcPort[0] = (byte) ((0xff00 & srcPort)>>8);
		
	}



	public void setDstPort(int dstPort) {
		this.dstPort[1] = (byte) (0xff & dstPort);
		this.dstPort[0] = (byte) ((0xff00 & dstPort) >> 8);
	}
	
	
	public byte[] toByte() {
		byte total[] = new byte[20];
		total[0] = srcPort[0];
		total[1] = srcPort[1];
		total[2] = dstPort[0];
		total[3] = dstPort[1];
		total[4] = length[0];
		total[5] = length[1];
		total[6] = checksum[0];
		total[7] = checksum[1];
		
		for(int i = 0; i<12; i++){
			total[8+i] = (byte)0x01;
		}
		
		
		
		return total;
	}
	
	
}
