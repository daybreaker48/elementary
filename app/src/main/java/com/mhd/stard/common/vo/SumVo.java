package com.mhd.stard.common.vo;

import java.util.ArrayList;


/**
 * Sum Data VO Class
 * Created by MH.D on 2021-11-08.
 */
public class SumVo {
	/**
	 * api
	 */
	public String api = "";
    /**
    * count
    */
	public int cnt = 0;
	/**
	 * count 2
	 */
	public int cn2 = 0;
	/**
    * data. msg
    */
	public ArrayList<SumData> msg = new ArrayList<SumData>();
	/**
	 * data. msg
	 */
	public ArrayList<SumData2> sub = new ArrayList<SumData2>();
    /**
     * Data Class
     */
	public class SumData {
		public String thdate; 		// 리포트 일
        public String top; 	    	// 완료 카운트
		public String bottom; 		// 총 카운트
		public String aver; 	    // 평균값. 리포트에 활용

		public String getThdate() {
			return thdate;
		}
		public void setThdate(String thdate) {
			this.thdate = thdate;
		}
		public String getTop() {
			return top;
		}
		public void setTop(String top) {
			this.top = top;
		}
		public String getBottom() {
			return bottom;
		}
		public void setBottom(String bottom) {
			this.bottom = bottom;
		}
		public String getAver() {
			return aver;
		}
		public void setAver(String aver) {
			this.aver = aver;
		}
	}
	public class SumData2 {
		public String tbsubject; 		// 과목
		public String tdend; 	    	// 종료여부
		public String scount; 			// 주간 총 카운트

		public String getTbsubject() {
			return tbsubject;
		}
		public void setTbsubject(String tbsubject) {
			this.tbsubject = tbsubject;
		}
		public String getTdend() {
			return tdend;
		}
		public void setTdend(String tdend) {
			this.tdend = tdend;
		}
		public String getScount() {
			return scount;
		}
		public void setScount(String scount) {
			this.scount = scount;
		}
	}

	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public int getCn2() {
		return cn2;
	}
	public void setCn2(int cn2) {
		this.cn2 = cn2;
	}
	public ArrayList<SumData> getMsg() {
		return msg;
	}
	public void setMsg(ArrayList<SumData> msg) {
		this.msg = msg;
	}
	public ArrayList<SumData2> getSub() {
		return sub;
	}
	public void setSub(ArrayList<SumData2> sub) {
		this.sub = sub;
	}
}



/**
 * Data Sample
 *
 *
 * {
 "totalCount":"4",
 "list" : [
 {
 "commonData_1":"1220928",
 "commonData_2":"20160513-033904",
 "commonData_3":"N",
 "info_banner_img":"N",
 "info_banner_land":"N",
 "info_banner_title":"N",
 "info_banner_start":"N",
 "info_banner_end":"N",
 "btn_001_use":"20160428-084000",
 "btn_001_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_001_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_001_title":"아파트 관리비 자동납부 할인",
 "btn_001_align":"",
 "btn_002_use":"20160428-084000",
 "btn_002_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_002_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_002_title":"아파트 관리비 자동납부 할인",
 "btn_002_align":"",
 "btn_003_use":"20160428-084000",
 "btn_003_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_003_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_003_title":"아파트 관리비 자동납부 할인",
 "btn_003_align":"",
 "btn_004_use":"20160428-084000",
 "btn_004_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_004_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_004_title":"아파트 관리비 자동납부 할인",
 "btn_004_align":""
 },{
 "commonData_1":"1220928",
 "commonData_2":"20160513-033904",
 "commonData_3":"N",
 "info_banner_img":"N",
 "info_banner_land":"N",
 "info_banner_title":"N",
 "info_banner_start":"N",
 "info_banner_end":"N",
 "btn_001_use":"20160428-084000",
 "btn_001_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_001_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_001_title":"아파트 관리비 자동납부 할인",
 "btn_001_align":"",
 "btn_002_use":"20160428-084000",
 "btn_002_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_002_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_002_title":"아파트 관리비 자동납부 할인",
 "btn_002_align":"",
 "btn_003_use":"20160428-084000",
 "btn_003_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_003_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_003_title":"아파트 관리비 자동납부 할인",
 "btn_003_align":"",
 "btn_004_use":"20160428-084000",
 "btn_004_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_004_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_004_title":"아파트 관리비 자동납부 할인",
 "btn_004_align":""
 },{
 "commonData_1":"1220928",
 "commonData_2":"20160513-033904",
 "commonData_3":"N",
 "info_banner_img":"N",
 "info_banner_land":"N",
 "info_banner_title":"N",
 "info_banner_start":"N",
 "info_banner_end":"N",
 "btn_001_use":"20160428-084000",
 "btn_001_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_001_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_001_title":"아파트 관리비 자동납부 할인",
 "btn_001_align":"",
 "btn_002_use":"20160428-084000",
 "btn_002_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_002_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_002_title":"아파트 관리비 자동납부 할인",
 "btn_002_align":"",
 "btn_003_use":"20160428-084000",
 "btn_003_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_003_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_003_title":"아파트 관리비 자동납부 할인",
 "btn_003_align":"",
 "btn_004_use":"20160428-084000",
 "btn_004_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_004_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_004_title":"아파트 관리비 자동납부 할인",
 "btn_004_align":""
 },{
 "commonData_1":"1220928",
 "commonData_2":"20160513-033904",
 "commonData_3":"N",
 "info_banner_img":"N",
 "info_banner_land":"N",
 "info_banner_title":"N",
 "info_banner_start":"N",
 "info_banner_end":"N",
 "btn_001_use":"20160428-084000",
 "btn_001_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_001_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_001_title":"아파트 관리비 자동납부 할인",
 "btn_001_align":"",
 "btn_002_use":"20160428-084000",
 "btn_002_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_002_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_002_title":"아파트 관리비 자동납부 할인",
 "btn_002_align":"",
 "btn_003_use":"20160428-084000",
 "btn_003_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_003_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_003_title":"아파트 관리비 자동납부 할인",
 "btn_003_align":"",
 "btn_004_use":"20160428-084000",
 "btn_004_img":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/ios03.png",
 "btn_004_land":"//static11.samsungcard.com/wcms/app/login_banner/__icsFiles/afieldfile/2016/04/28/and03.png",
 "btn_004_title":"아파트 관리비 자동납부 할인",
 "btn_004_align":""
 }
 ]
 }
 *
 */
