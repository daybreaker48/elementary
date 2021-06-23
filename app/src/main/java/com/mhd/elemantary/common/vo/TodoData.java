package com.mhd.elemantary.common.vo;

/**
 * todolist data class
 * Created by MH.D on 2021-06-23.
 */
public class TodoData {
	private String subject;
	private String textbook;
	private String daily;

	public String getSubject() {
		return subject;
	}
	public void setSubject(String title) {
		this.subject = title;
	}

	public String getTextbook() {
		return textbook;
	}
	public void setTextbook(String title) {
		this.textbook = title;
	}

	public String getDailyProgress() {
		return daily;
	}
	public void setDailyProgress(String title) {
		this.daily = title;
	}
}
