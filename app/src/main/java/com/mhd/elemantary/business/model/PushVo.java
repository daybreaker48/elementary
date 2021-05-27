package com.mhd.elemantary.business.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 푸시 정보 Vo class
 * Created by MH.D on 2017-04-04.
 */
public class PushVo implements Parcelable{
	private String msgId;		// msg id
	private String workday;		// send date
	private String notiMsg;		// bar title
	private String message;		// bar content
	private String notiImg;		// image url
	private String link;		// link url
	private boolean isLink;		// is Link?

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getWorkday() {
		return workday;
	}

	public void setWorkday(String workday) {
		this.workday = workday;
	}

	public String getNotiMsg() {
		return notiMsg;
	}

	public void setNotiMsg(String notiMsg) {
		this.notiMsg = notiMsg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNotiImg() {
		return notiImg;
	}

	public void setNotiImg(String notiImg) {
		this.notiImg = notiImg;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isLink() {
		return isLink;
	}

	public void setIsLink(boolean isLink) {
		this.isLink = isLink;
	}

	public PushVo() {}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(msgId);
		parcel.writeString(workday);
		parcel.writeString(notiMsg);
		parcel.writeString(message);
		parcel.writeString(notiImg);
		parcel.writeString(link);
		boolean[] booleans = {isLink};
		parcel.writeBooleanArray(booleans);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PushVo> CREATOR = new Creator<PushVo>() {
		public PushVo createFromParcel(Parcel p) {
			PushVo data = new PushVo();
			data.setMsgId(p.readString());
			data.setWorkday(p.readString());
			data.setNotiMsg(p.readString());
			data.setMessage(p.readString());
			data.setNotiImg(p.readString());
			data.setLink(p.readString());
			boolean[] booleans = new boolean[1];
			p.readBooleanArray(booleans);
			data.setIsLink(booleans[0]);

			return data;
		}

		public PushVo[] newArray(int size) {
			return new PushVo[size];
		}
	};

}