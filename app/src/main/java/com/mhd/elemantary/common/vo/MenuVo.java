package com.mhd.elemantary.common.vo;

import java.util.ArrayList;


/**
 * Kids per Menu Data VO Class
 * Created by MH.D on 2021-10-19.
 */
public class MenuVo {

	public ArrayList<MenuData> msg = new ArrayList<MenuData>();

	public class MenuData {
		public String menuname; 		// 아이디
		public String kidname; 		// 이름

		public String getMenuname() {
			return menuname;
		}

		public void setMenuname(String menuname) {
			this.menuname = menuname;
		}

		public String getKidname() {
			return kidname;
		}

		public void setKidname(String kidname) {
			this.kidname = kidname;
		}
	}

	public ArrayList<MenuData> getMsg() {
		return msg;
	}

	public void setMsg(ArrayList<MenuData> msg) {
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
