package com.mhd.boomerang.network.common;

/**
 * @author MH.D
 */
public class SCServiceVo {
	private Common common;
	private Message message;
	
	/**
	 * @param common
	 */
	public SCServiceVo() {
		super();
		this.common = new Common();
	}
	
	/**
	 * @param common
	 */
	public SCServiceVo(Common common) {
		super();
		this.common = common;
	}

	/**
	 * @param message
	 */
	public SCServiceVo(Message message) {
		super();
		this.message = message;
	}

	/**
	 * @param common
	 * @param message
	 */
	public SCServiceVo(Common common, Message message) {
		super();
		this.common = common;
		this.message = message;
	}
	
	public Common getCommon() {
		return common;
	}
	public void setCommon(Common common) {
		this.common = common;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
