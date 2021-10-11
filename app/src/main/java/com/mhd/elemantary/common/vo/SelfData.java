package com.mhd.elemantary.common.vo;

/**
 * selflist data class
 * Created by MH.D on 2021-06-24.
 */
public class SelfData {
	private String selfidx;
	private String selfitem;
	private String selfComplete;

	public String getSelfIdx() {
		return selfidx;
	}
	public void setSelfIdx(String selfidx) {
		this.selfidx = selfidx;
	}
	public String getSelfItem() {
		return selfitem;
	}
	public void setSelfItem(String title) {
		this.selfitem = title;
	}
	public String getSelfComplete() {
		return selfComplete;
	}
	public void setSelfComplete(String selfComplete) {
		this.selfComplete = selfComplete;
	}
}
