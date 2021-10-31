package com.mhd.stard.common.vo;

/**
 * login vo class
 * Created by MH.D on 2017-04-05.
 */
public class LoginVo {

	public String usrUuId;
	public String usrToken;
	public String usrInf_3;
	public String usrInf_4;
	public String usrInf_5;
	public String usrInf_6;
	public String usrInf_7;
	public LoginSubInfVo lsiv = null;

	public String getUsrUuId() {
		return usrUuId;
	}

	public void setUsrUuId(String usrUuId) {
		this.usrUuId = usrUuId;
	}

	public String getUsrToken() {
		return usrToken;
	}

	public void setUsrToken(String usrToken) {
		this.usrToken = usrToken;
	}

	public String getUsrInf_3() {
		return usrInf_3;
	}

	public void setUsrInf_3(String usrInf_3) {
		this.usrInf_3 = usrInf_3;
	}

	public String getUsrInf_4() {
		return usrInf_4;
	}

	public void setUsrInf_4(String usrInf_4) {
		this.usrInf_4 = usrInf_4;
	}

	public String getUsrInf_5() {
		return usrInf_5;
	}

	public void setUsrInf_5(String usrInf_5) {
		this.usrInf_5 = usrInf_5;
	}

	public String getUsrInf_6() {
		return usrInf_6;
	}

	public void setUsrInf_6(String usrInf_6) {
		this.usrInf_6 = usrInf_6;
	}

	public String getUsrInf_7() {
		return usrInf_7;
	}

	public void setUsrInf_7(String usrInf_7) {
		this.usrInf_7 = usrInf_7;
	}

	public LoginSubInfVo getLsiv() {
		return lsiv;
	}

	public void setLsiv(LoginSubInfVo lsiv) {
		this.lsiv = lsiv;
	}

}
