package com.mhd.stard.network.common;

/**
 * network common parameter data ( user login info )
 * @author MH.D
 */
public class Common {

	/** unique number */
	public String browserUid = "";
	/** screen ID */
	public String ajaxUid = "";
	/** used value */
	public String cellosessionkey = "";

	public String getBrowserUid() { return browserUid; }
	public void setBrowserUid(String browserUid) { this.browserUid = browserUid; }
	public String getAjaxUid() { return ajaxUid; }
	public void setAjaxUid(String ajaxUid) { this.ajaxUid = ajaxUid; }
	public String getCellosessionkey() { return cellosessionkey; }
	public void setCellosessionkey(String cellosessionkey) { this.cellosessionkey = cellosessionkey; }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("browserUid : " + browserUid);
		builder.append(", ajaxUid : " + ajaxUid);
		builder.append(", cellosessionkey : " + cellosessionkey);
		return builder.toString();
	}
}