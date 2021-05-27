package com.mhd.elemantary.common.vo;

import java.util.ArrayList;


/**
 * Paging Banner Data VO Class
 * Created by MH.D on 2017-04-05.
 */
public class PagingBannerVo {

    /**
    * banner count
    */
	public int totalCount = 0;
    /**
    * 배너데이터 리스트
    */
	public ArrayList<BannerData> list = new ArrayList<BannerData>();
    /**
     * Paging Banner Data Class
     */
	public class BannerData {
		public String commonData_1; 		// 공통으로 적용될, 적용할 정보들
		public String commonData_2; 		// 공통으로 적용될, 적용할 정보들
		public String commonData_3; 		// 공통으로 적용될, 적용할 정보들
		public String info_banner_img; 		// banner image url
		public String info_banner_land;		// banner landing url
		public String info_banner_title;	// banner title
		public String info_banner_start;	// start date
		public String info_banner_end;		// end date

		public String btn_001_use;		// Y, N
		public String btn_001_img;		// img url
		public String btn_001_land;		// landing url
		public String btn_001_title;	// button title
		public String btn_001_align;	// alignment

		public String btn_002_use;		// Y, N
		public String btn_002_img;		// img url
		public String btn_002_land;		// landing url
		public String btn_002_title;	// button title
		public String btn_002_align;	// alignment

		public String btn_003_use;		// Y, N
		public String btn_003_img;		// img url
		public String btn_003_land;		// landing url
		public String btn_003_title;	// button title
		public String btn_003_align;	// alignment

		public String btn_004_use;		// Y, N
		public String btn_004_img;		// img url
		public String btn_004_land;		// landing url
		public String btn_004_title;	// button title
		public String btn_004_align;	// alignment
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
