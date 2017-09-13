package kr.gsil.dpkepco.client;
import kr.gsil.dpkepco.R;

import android.content.Context;

public class HttpUrl {
	
	//api final value
	public static final int CONSMAN_DEVICE_LIST 							= 0;
	public static final int CONSMAN_NFC_INSERT 								= 1;
	public static final int CONSMAN_DEVICE_INFO 							= 2;
	public static final int CONSMAN_CHECK_LIST 								= 3;
	public static final int CONSMAN_CHECK_ONE_INFO 							= 4;
	public static final int CONSMAN_INSERT_INSPECT 							= 5;
	public static final int CONSMAN_INSERT_INSPECT_DETAIL 					= 6;
	public static final int CONSMAN_DATE_CHECK_LIST 						= 7;
	public static final int CONSMAN_UPDATE_INSPECT 							= 8;
	public static final int CONSMAN_DUPLICATE_NFC_TAG_ID					= 9;
	public static final int CONSMAN_DELETE_INSPECT_DETAIL					= 10;
	public static final int CONSMAN_RESULT_CHECK_LIST						= 11;
	public static final int CONSMAN_DAILY_LIST								= 12;
	public static final int CONSMAN_IMAGE_URL								= 13;
	public static final int CONSMAN_FILE_UPLOAD								= 14;
	public static final int CONSMAN_USER_LOGIN								= 15;
	public static final int CONSMAN_WORK_TYLE								= 16;
	public static final int CONSMAN_PHONE_CHECK								= 17;
	public static final int CONSMAN_WORKER_INSERT							= 18;
	public static final int CONSMAN_WORKER_LIST								= 19;
	public static final int CONSMAN_FIRST_WORKER_LIST						= 20;
	public static final int CONSMAN_FIRST_WORKER_UPDATE						= 21;
	public static final int CONSMAN_DAILY_WORKER_LIST						= 22;
	public static final int CONSMAN_DAILY_WORK_LIST							= 23;
	public static final int CONSMAN_WORKER_UPDATE							= 24;
	public static final int CONSMAN_MWORKER_UPDATE							= 25;
	public static final int CONSMAN_FRIST_META_LIST							= 26;
	public static final int CONSMAN_SECOND_META_LIST						= 27;
	public static final int CONSMAN_INSERT_RISK_BASIC						= 28;
	public static final int CONSMAN_DELETE_RISK_CHECK_CODE					= 30;
	public static final int CONSMAN_INSERT_RISK_CHECK_CODE					= 29;
	public static final int CONSMAN_RISK_BASIC_LIST							= 31;
	public static final int CONSMAN_RISK_CHECK_CODE_LIST					= 32;
	public static final int CONSMAN_RISK_DETAIL_LIST						= 33;
	public static final int CONSMAN_INSERT_RISK_DETAIL						= 34;
	public static final int CONSMAN_UPDATE_RISK_BASIC						= 35;
	public static final int CONSMAN_UPDATE_META_LIST						= 36;
	public static final int CONSMAN_SPOT_LIST								= 37;
	public static final int CONSMAN_INSPECT_TODAY_LIST						= 38;
	public static final int CONSMAN_INSPECT_CODE_LIST						= 39;
	public static final int CONSMAN_INSPECT_DETAIL_LIST						= 40;
	public static final int CONSMAN_UPDATE_RISK_INSPECT						= 41;
	public static final int CONSMAN_TODAY_SPOT_LIST							= 42;
	public static final int CONSMAN_SPOT_CHECK_LIST							= 43;
	public static final int CONSMAN_INSERT_SPOT_INSPECT_DETAIL				= 44;
	public static final int CONSMAN_UPDATE_SPOT_INSPECT						= 45;
	public static final int CONSMAN_CONFIRM_SPOT_TAG_ID						= 46;
	public static final int CONSMAN_SPOT_CHECK_ONE_INFO						= 47;
	public static final int CONSMAN_DAMAGE_LIST								= 48;
	public static final int CONSMAN_CHASU_LIST								= 49;
	public static final int CONSMAN_RISK_INSPECT_DAY_LIST					= 50;
	public static final int CONSMAN_GET_NFC_TAG								= 51;
	public static final int CONSMAN_INSERT_PERSON_NFC_ID					= 52;
	public static final int CONSMAN_CONT_LIST								= 53;
	public static final int CONSMAN_NOT_SMARTPHONE_LIST						= 54;
	public static final int CONSMAN_NOT_INSERT_WORKERING					= 55;
	public static final int CONSMAN_UPDATE_MW_STATUS						= 56;
	public static final int CONSMAN_MEASURE_LIST							= 57;
	public static final int CONSMAN_MEASURE_IMAGE_COUNT						= 58;
	public static final int CONSMAN_MEASURE_INSERT							= 59;
	public static final int CONSMAN_MEASURE_UPDATE_RESULT					= 60;
	public static final int CONSMAN_MEASURE_UPDATE_STATUS					= 61;
	public static final int CONSMAN_INSERT_PERSON_ETC						= 62;
	public static final int CONSMAN_PERSON_ETC								= 63;
	public static final int CONSMAN_UPDATE_IMSI								= 64;
	public static final int CONSMAN_USER_LIST								= 65;
	public static final int CONSMAN_MANAGER_USER_LIST						= 66;
	public static final int CONSMAN_INSERT_MANAGER_USER						= 67;
	public static final int CONSMAN_UPDATE_EDU_PERSON_CANCEL				= 68;
	public static final int CONSMAN_TODAY_WORKER_INSERT_DATA				= 69;
	public static final int CONSMAN_MOBILE_PUSH_EVENT						= 70;
	public static final int CONSMAN_DELETE_WORKER							= 71;
	public static final int CONSMAN_UPDATE_WORKER_DEL						= 72;
	public static final int CONSMAN_CHECK_WORKER_BLACK						= 73;
	public static final int CONSMAN_DELETE_M_WORKER							= 74;
	public static final int CONSMAN_DELETE_M_USER							= 75;
	public static final int CONSMAN_UPDATE_TODAY_WORKER_OPTION				= 76;
	public static final int CONSMAN_INSERT_MANAGER_ONE_USER					= 77;
	public static final int CONSMAN_INSERT_USER_LOCATION					= 78;
	public static final int CONSMAN_INSPECT_DETAIL_NEW_LIST					= 79;
	public static final int CONSMAN_UPDATE_RISK_NEW_INSPECT					= 80;
	public static final int CONSMAN_RISK_MEASURE_INFO						= 81;
	public static final int CONSMAN_TUNEL_INFO								= 82;
	public static final int CONSMAN_TUNEL_BOMB_COUNT						= 83;
	public static final int CONSMAN_INSERT_TUNEL_INFO						= 84;
	public static final int CONSMAN_PERSON_ETC_YA							= 85;
	public static final int CONSMAN_UPDATE_CYCLE_TIME						= 86;
	public static final int CONSMAN_INSERT_CYCLE_TIME						= 87;
	public static final int CONSMAN_CYCLE_LIST								= 88;
	public static final int CONSMAN_DAILY_CYCLE_LIST						= 89;
	public static final int CONSMAN_DELETE_CYCLE_TIME						= 90;
	public static final int CONSMAN_LAST_TUNEL_INFO							= 91;
	
	public static final int CONSMAN_INSPECT_VIEW_LIST						= 92;
	public static final int CONSMAN_INSPECT_VIEW_DETAIL_LIST				= 93;
	public static final int CONSMAN_INSERT_RISK_INSPECT						= 94;
	public static final int CONSMAN_RISK_ADMIN_CONFIRM_LIST					= 95;
	public static final int CONSMAN_DELETE_SPOT_INSPECT						= 96;
	public static final int CONSMAN_SPOT_CONFIRM_LIST						= 97;
	public static final int CONSMAN_SPOT_CONFIRM_DETAIL_LIST				= 98;
	public static final int CONSMAN_UPDATE_SPOT_INSEPECT_CHECK				= 99;
	public static final int CONSMAN_COMPANY_MANAGER_USER_ID					= 100;
	
	public static final int CONSMAN_DEVICE_PRE_CHECK_LIST					= 101;
	public static final int CONSMAN_DEVICE_BASIC_LIST						= 102;
	public static final int CONSMAN_INSERT_EQUIP							= 103;
	public static final int CONSMAN_UPDATE_EQUIP							= 104;
	public static final int CONSMAN_EQUIP_DRIVER_LIST						= 105;
	public static final int CONSMAN_EQUIP_INSERT_CHECK_RESULT				= 106;
	public static final int CONSMAN_EQUIP_LIST								= 107;
	public static final int CONSMAN_EQUIP_DELETE							= 108;
	public static final int CONSMAN_EQUIP_RESULT_CHECK_LIST					= 109;
	public static final int CONSMAN_EQUIP_RESULT_SATATUS					= 110;
	public static final int CONSMAN_EQUIP_RESULT_CHECK_STATUS				= 111;
	public static final int CONSMAN_EQUIP_DAILY_LIST						= 112;
	public static final int CONSMAN_EQUIP_DAILY_INSERT						= 113;
	public static final int CONSMAN_EQUIP_DELETE_CHECK_LIST					= 114;
	public static final int CONSMAN_EQUIP_RESULT_DAILY_STATUS				= 115;
	public static final int CONSMAN_EQUIP_RESULT_DAILY_LIST					= 116;
	public static final int CONSMAN_EQUIP_RESULT_DAILY_CHECK_LIST			= 117;
	public static final int CONSMAN_EQUIP_DELETE_ITEM						= 118;
	public static final int CONSMAN_EQUIP_OUT_ITEM							= 119;
	public static final int CONSMAN_EQUIP_TOGATHER_WORKER_LIST				= 120;
	
	public static final int CONSMAN_WORKER_CONT_UPDATE						= 121;
	public static final int CONSMAN_USER_WARNING							= 122;
	
	public static final int CONSMAN_BEACON_MANAGER_LIST						= 123;
	public static final int CONSMAN_BEACON_MANAGER_UPDATE					= 124;
	public static final int CONSMAN_UPDATE_USER_YN							= 125;
	public static final int CONSMAN_BEACON_HISTORY_LIST						= 126;
	public static final int CONSMAN_WARNING_PUSH							= 127;
	public static final int CONSMAN_DISASTER_INSERT							= 128;
	public static final int CONSMAN_DISASTER_LIST							= 129;
	public static final int CONSMAN_DISASTER_DELETE							= 130;
	
	public static final int CONSMAN_UPDATE_USER_MISSION						= 131;
	public static final int CONSMAN_USER_MISSION							= 132;
	public static final int CONSMAN_DELETE_ETC								= 133;
	public static final int CONSMAN_EQUIP_DUPLICATE_CHECK_USER				= 134;
	public static final int CONSMAN_UPDATE_WORKER_CANCEL					= 135;
	public static final int CONSMAN_DAILY_CONT_LIST							= 136;
	
	public static final int CONSMAN_DISASTER_ORG_LIST						= 137;
	public static final int CONSMAN_DELETE_DISASTER_ORG						= 138;
	public static final int CONSMAN_INSERT_DISASTER_ORG						= 139;
	public static final int CONSMAN_ALL_CHECK_DEVICE						= 140;
	public static final int CONSMAN_BEACON_SIGN_LIST						= 141;
	public static final int CONSMAN_SCHEDULE_DATA							= 142;
	public static final int CONSMAN_SCHEDULE_CONT_DATA						= 143;
	public static final int CONSMAN_INSERT_SCHEDULE_CONT_LIST				= 144;
	public static final int CONSMAN_MULTI_USER_LIST							= 145;
	public static final int CONSMAN_SITE_LIST								= 146;
	public static final int CONSMAN_GSIL_MEASURE_LIST						= 147;
	public static final int CONSMAN_INSERT_GSIL_MEASURE						= 148;
	public static final int CONSMAN_UPDATE_GSIL_MEASURE						= 149;
	public static final int CONSMAN_REPORT_USER_LIST						= 150;
	public static final int CONSMAN_EQUIP_DAILY_RESULT_GSIL_LIST			= 151;
	public static final int CONSMAN_NOTICE_LIST								= 152;
	public static final int CONSMAN_NOTICE_INFO								= 153;
	public static final int CONSMAN_UPDATE_HIT   							= 154;
	public static final int CONSMAN_SEARCH_NOTICE_LIST						= 155;
	
	public static final int CONSMAN_INFO_LIST								= 156;
	public static final int CONSMAN_INFO_DETAIL								= 157;
	public static final int CONSMAN_UPDATE_INFO_HIT							= 158;
	public static final int CONSMAN_SEARCH_INFO_LIST						= 159;	

	public static final int CONSMAN_SITE_INFO_LIST							= 160;
	
	public static final int CONSMAN_INFO_DOWNLOAD							= 161;
	
	public static final int CONSMAN_LAST_YESTERDAY_END_TIME					= 162;
	public static final int CONSMAN_WORKING_COMPANY_LIST					= 163;
	public static final int CONSMAN_RISK_MEASURE_COMPANY_LIST				= 164;
	public static final int CONSMAN_TODAY_WORKER_INSERT_COMPLETE_PUSH		= 165;
	public static final int CONSMAN_NOTICE_DOWNLOAD							= 166;
	public static final int CONSMAN_GET_GUNPOWER_INFO						= 167;
	public static final int CONSMAN_GET_TUNNEL_SENSOR_INFO					= 168;
	public static final int CONSMAN_GET_DISASTER_PUSH_LIST					= 169;
	public static final int CONSMAN_DELETE_DISASTER_PUSH_LIST				= 170;
	
	public static final int CONSMAN_DAILY_RISK_CHECK_LIST					= 171;
	public static final int CONSMAN_DAILY_RISK_CHECK_INSERT					= 172;
	public static final int CONSMAN_DAILY_RISK_CHECK_DELETE					= 173;
	public static final int CONSMAN_CODE_LIST								= 174;
	public static final int CONSMAN_DAILY_RISK_CHECK_UPDATE					= 175;
	public static final int CONSMAN_GET_DISASTER_PUSH_BELL_LIST				= 176;
	
	public static final int CONSMAN_GET_SS_COUNT_DATA						= 177;
	public static final int CONSMAN_DELETE_SS_LOCATION_DATA					= 178;

	public static final int KEPCO_GET_MONITORING_DATA						= 179;
	public static final int KEPCO_GET_DRILLING_DAILY_VALUE					= 180;
	public static final int KEPCO_GET_DRILLING_DAILY_VALUE_BYID				= 181;
	public static final int KEPCO_DRILLING_DAILY_VALUE_INSERT				= 182;
	public static final int KEPCO_DRILLING_DAILY_VALUE_UPDATE				= 183;
	public static final int KEPCO_DRILLING_DAILY_VALUE_DELETE				= 184;

	public static final int KEPCO_GET_TIMELY_VALUE_LIST						= 185;
	public static final int KEPCO_GET_TIMELY_VALUE_BYID						= 186;
	public static final int KEPCO_TIMELY_VALUE_INSERT						= 187;
	public static final int KEPCO_TIMELY_VALUE_UPDATE						= 187;
	public static final int KEPCO_TIMELY_VALUE_DELETE						= 187;


	
	public static String getUrl(Context context, int idx) {
		String apiUrl = String.format(context.getResources().getStringArray(R.array.gsilsafety_url)[idx]);
		if(apiUrl==null){
			return "apiUrl is null";
		}
		return apiUrl;
	}

}
