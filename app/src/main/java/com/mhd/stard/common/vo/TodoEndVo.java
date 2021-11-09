package com.mhd.stard.common.vo;

import java.util.ArrayList;


/**
 * Todo End Data VO Class
 * Created by MH.D on 2021-11-09.
 */
public class TodoEndVo {
	/**
	 * api
	 */
	public String api = "";
    /**
    * count
    */
	public int cnt = 0;
	/**
    * data. msg
    */
	public ArrayList<TodoEndListData> msg = new ArrayList<TodoEndListData>();
    /**
     * Paging Banner Data Class
     */
	public class TodoEndListData {
		public String subject; 		// 과목명
        public String detail; 		// 세부항목
		public String title; 		// 학습지이름
		public String oneday; 		// 하루분량
		public String total; 		// 총분량
		public String rest; 		// 남은량
		public String section; 		// 종료일일
		public String idx;      	// 종료일일
		public String tdstart;      	// 종료일일
		public String tddate;      	// 종료일일
		public String tdenddate;      	// 종료일일

		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

        public String getOneday() {
            return oneday;
        }
        public void setOneday(String oneday) {
            this.oneday = oneday;
        }

        public String getTotal() {
            return total;
        }
        public void setTotal(String total) {
            this.total = total;
        }

        public String getRest() {
            return rest;
        }
        public void setRest(String rest) {
            this.rest = rest;
        }

        public String getSection() {
            return section;
        }
        public void setSection(String section) {
            this.section = section;
        }

        public String getIdx() {
            return idx;
        }
        public void setIdx(String idx) {
            this.idx = idx;
        }

        public String getTdstart() {
            return tdstart;
        }
        public void setTdstart(String tdstart) {
            this.tdstart = tdstart;
        }

        public String getTddate() {
            return tddate;
        }
        public void setTddate(String tddate) {
            this.tddate = tddate;
        }

        public String getTdenddate() {
            return tdenddate;
        }
        public void setTdenddate(String tdenddate) {
            this.tdenddate = tdenddate;
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

	public ArrayList<TodoEndListData> getMsg() {
		return msg;
	}

	public void setMsg(ArrayList<TodoEndListData> msg) {
		this.msg = msg;
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
