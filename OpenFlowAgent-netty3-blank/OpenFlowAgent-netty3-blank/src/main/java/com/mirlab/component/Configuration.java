package com.mirlab.component;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version ����ʱ�䣺2017��12��27�� ����6:42:48 ��˵��
 */
public class Configuration {
	// controller
	private String IP = "192.168.56.101";
	private int port = 6633;

	// features
	private long datapathId;
	private long n_buffers = 256L;
	private short n_tables = 64;

	// setconfig
	private int missSendLen;

	// desc
	private String manufacturerDesc = "MIRLab";
	private String hardwareDesc = "MIRLab-vswitch";
	private String softwareDesc = "2.0.2";
	private String serialNo = "None";
	private String datapathDesc = "None";

	// private getter and setter
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getDatapathId() {
		return datapathId;
	}

	public void setDatapathId(long datapathId) {
		this.datapathId = datapathId;
	}

	public long getN_buffers() {
		return n_buffers;
	}

	public void setN_buffers(long n_buffers) {
		this.n_buffers = n_buffers;
	}

	public short getN_tables() {
		return n_tables;
	}

	public void setN_tables(short n_tables) {
		this.n_tables = n_tables;
	}

	public int getMissSendLen() {
		return missSendLen;
	}

	public void setMissSendLen(int missSendLen) {
		this.missSendLen = missSendLen;
	}

	public String getManufacturerDesc() {
		return manufacturerDesc;
	}

	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}

	public String getHardwareDesc() {
		return hardwareDesc;
	}

	public void setHardwareDesc(String hardwareDesc) {
		this.hardwareDesc = hardwareDesc;
	}

	public String getSoftwareDesc() {
		return softwareDesc;
	}

	public void setSoftwareDesc(String softwareDesc) {
		this.softwareDesc = softwareDesc;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getDatapathDesc() {
		return datapathDesc;
	}

	public void setDatapathDesc(String datapathDesc) {
		this.datapathDesc = datapathDesc;
	}

}
