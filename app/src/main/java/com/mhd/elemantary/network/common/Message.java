package com.mhd.elemantary.network.common;

/**
 * message parameter data
 */
public class Message {
	
	public String msgC = "";
	public String msgKrnCn = "";
	public String msgMarkDvC = "";
	

	public String getMsgC() {
		return msgC;
	}

	public void setMsgC(String msgC) {
		this.msgC = msgC;
	}

	public String getMsgKrnCn() {
		return msgKrnCn;
	}

	public void setMsgKrnCn(String msgKrnCn) {
		this.msgKrnCn = msgKrnCn;
	}

	public String getMsgMarkDvC() {
		return msgMarkDvC;
	}

	public void setMsgMarkDvC(String msgMarkDvC) {
		this.msgMarkDvC = msgMarkDvC;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("msgC : " + msgC);
		builder.append(", msgKrnCn : " + msgKrnCn);
		builder.append(", msgMarkDvC : " + msgMarkDvC);
		return builder.toString();
	}
}