package com.mirlab.protocol;

public class Tcp {
	private byte srcPort[];  //2
	private byte dstPort[]; //2
	private byte seqNumber[]; //4
	private byte ackNumber[]; //4
	private byte headerLenth;  //1
	private byte flags;   //1
	private byte windowSize[]; //2
	private byte checksum[]; //2
	private byte urgentPointer[]; //2
	private byte options[]; //12;
	
	
	public Tcp(){
		srcPort = new byte[2];
		dstPort = new byte[2];
		
		
		seqNumber = new byte[4];
		seqNumber[0] =(byte) 0x04;
		seqNumber[1] =(byte) 0x23;
		seqNumber[2] = (byte)0x7c;
		seqNumber[3] =(byte) 0xe5;
		
		ackNumber = new byte[4];
		ackNumber[0] = (byte)0x01;
		ackNumber[1] = (byte) 0xc1;
		ackNumber[2] = (byte)0x86;
		ackNumber[3] =(byte) 0x73;
		
		headerLenth = (byte) 0x50;
		flags = (byte) 0x10;
		
		windowSize = new byte[2];
		windowSize[0] = (byte) 0x02;
		windowSize[1] = (byte) 0x01;
		
		checksum = new byte[2];
		checksum[0] = (byte) 0x49;
		checksum[1] = (byte) 0xf6;
		
		urgentPointer = new byte[2];
		urgentPointer[0] = (byte) 0x00;
		urgentPointer[1] = (byte) 0x00;
		

		
		
		
		
	}


	public byte[] getSrcPort() {
		return srcPort;
	}


	public void setSrcPort(int srcPort) {
		this.srcPort[1] = (byte) (0xff & srcPort);
		this.srcPort[0] = (byte) ((0xff00 & srcPort)>>8);
		
	}


	public byte[] getDstPort() {
		return dstPort;
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
		
		for(int i = 0; i<4;i++){
		total[4+i] = seqNumber[i];
		total[8+i] = ackNumber[i];
		}
		
		total[12] = headerLenth;
		total[13]= flags;
		

		total[14]=windowSize[0];
		total[15]=windowSize[1];
		

		total[16]=checksum[0];
		total[17]=checksum[1];
		

		total[18]=urgentPointer[0];
		total[19]=urgentPointer[1];
		
		
		

		
		
		return total;
	}
	
	
	
}
