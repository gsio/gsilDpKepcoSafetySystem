package kr.gsil.dpkepco.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.gsil.dpkepco.UserSystemApplication;
import kr.gsil.dpkepco.model.DailyValueVO;
import kr.gsil.dpkepco.model.KepcoMonitorVO;
import kr.gsil.dpkepco.model.KepcoSensorVO;
import kr.gsil.dpkepco.model.MobileInfoVO;
import kr.gsil.dpkepco.model.MobileNoticeVO;
import kr.gsil.dpkepco.model.MobileEquipVO;
import kr.gsil.dpkepco.model.MobileMeasureVO;
import kr.gsil.dpkepco.model.MobileMetaVO;
import kr.gsil.dpkepco.model.MobileRiskVO;
import kr.gsil.dpkepco.model.MobileSpotVO;
import kr.gsil.dpkepco.model.MobileTunelVO;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.model.MobileVO;
import kr.gsil.dpkepco.model.MobileWorkerVO;
import kr.gsil.dpkepco.model.MobileWtypeVO;
import kr.gsil.dpkepco.model.KepcoRecoDataVO;
import kr.gsil.dpkepco.model.ScheVO;
import kr.gsil.dpkepco.model.TimelyValueVO;
import kr.gsil.dpkepco.util.CustomJsonObject;
import kr.gsil.dpkepco.model.weather.Weather;
import kr.gsil.dpkepco.model.weather.WeatherInfoVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class HttpClient {

	public static HttpClient _instance = null;
	
	public synchronized static HttpClient getInstance()
	{
		if ( _instance != null ) return _instance;
		return _instance = new HttpClient();
	}

	public ArrayList<MobileUserVO> getUserListByContType(final Context context, int site_id, int cont_type){
		ArrayList<MobileUserVO> wlist = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_USER_LIST_BY_CONTTYPE) + "?site_id=8&cont_type=99" );
		Log.e("getUserListByContType","result = "+result+" site_id = "+site_id+" cont_type = "+cont_type);
		Log.i("KEPCO_GET_USER_LIST_BY_CONTTYPE", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setId(items.getString("id"));
						if(items.has("recoid")) mobileVo.setRecoid(items.getString("recoid"));
						mobileVo.setName(items.getString("name"));
						if(items.has("cname")) mobileVo.setCname(items.getString("cname"));
						if(items.has("gubun")) mobileVo.setGubun(items.getString("gubun"));
						else mobileVo.setGubun("99");
						mobileVo.setUserid(items.getString("userid"));
						mobileVo.setGrade(items.getString("grade"));
						mobileVo.setUseyn(items.getString("useyn"));
						if(items.has("countdata")) mobileVo.setCountdata(items.getString("countdata"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}

			} catch(Exception e) {

				e.printStackTrace();
				return null;
			}

		}

		return wlist;
	}

	public void getRecoData(final Context context, int site_id){
		KepcoRecoDataVO recoData = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_MAIN_RECO_DATA) + "?site_id=8" );
		Log.e("getMainRecoData","result = "+result+" site_id = "+site_id);
		recoData = new KepcoRecoDataVO();
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);

				JSONArray jArr = new JSONArray(item.getString("item"));
				for ( int i = 0; i < jArr.length(); i++ ) {
					CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
					/*터널내 근로자 : t_gubun이 1,2인것(1:근로자,2;장비)
					터널내 관리자 : t_gubun이 3,4,5 인 것 count(3:관리자,4:감리단,5:발주처)
					터널내 외부방문자 : t_gubun이 6인것 count	*/
					if(items.has("t_gubun")){
						recoData.addCountTotal();
						int t_gubun = items.getInt("t_gubun");
						switch(t_gubun){
							case 1:case 2:recoData.addCountWorker();break;
							case 3:case 4:case 5:recoData.addCountManager();break;
							case 6:recoData.addCountVip();break;
						}
					}
				}
				((UserSystemApplication)context.getApplicationContext()).setKepcoRecoData(recoData);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public WeatherInfoVO getWeatherInfo(final Context context, String addr){
		WeatherInfoVO weather = null;
		String result = getHttpData( Weather.getYahooUrl(addr) );
		//Log.e("getWeatherInfo","result = "+result+" addr = "+addr);

		if( result != null && !result.equals("") ) {
			try{
				weather = new WeatherInfoVO();
				JSONObject jsonObj = new JSONObject(result);
					CustomJsonObject item = new CustomJsonObject(jsonObj);
					CustomJsonObject query = new CustomJsonObject(item.getString("query"));
					CustomJsonObject results = new CustomJsonObject(query.getString("results"));

					CustomJsonObject channel = new CustomJsonObject(results.getString("channel"));
				weather.setWind(channel.getString("wind"));//channel.wind; //바람세기 등//,"wind":{"chill":"77","direction":"90","speed":"14"}
				weather.setAstronomy(channel.getString("astronomy"));//channel.astronomy; //sunset,sunrise 시간//,"astronomy":{"sunrise":"6:14 am","sunset":"6:40 pm"}

					CustomJsonObject location = new CustomJsonObject(channel.getString("location"));
				weather.setCity(location.getString("city"));//location.city; //지역명 영문
				weather.setCity(location.getString("region"));//location.region; //지역명 영문
					CustomJsonObject atmosphere = new CustomJsonObject(channel.getString("atmosphere"));
				weather.setHumidity(atmosphere.getString("humidity"));//atmosphere.humidity + '%'; //강수확률//<<<<<<<<<<
					CustomJsonObject channel_item = new CustomJsonObject(channel.getString("item"));
					CustomJsonObject condition = new CustomJsonObject(channel_item.getString("condition"));

				//Math.round((condition.temp - 32)*5/9)  + '℃';  //화씨 -> 섭씨변환
				int temp = Math.round((Integer.valueOf(condition.getString("temp")) - 32)*5/9);
				weather.setToday_temp(String.valueOf(temp));//<<<<<<<<<<
				String today_code = condition.getString("code");//<<<<<<<<<<
				weather.setToday_code(today_code); //condition.code;
				String condition_text = "";
				//condition_text = condition.getString("text");
				condition_text = Weather.w_condition_text[Integer.valueOf(today_code)];
				weather.setToday_text(condition_text); //condition.text;//<<<<<<<<<<

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return weather;
	}


	public ArrayList<TimelyValueVO> getTimelyValueList(final Context context, int type){
		ArrayList<TimelyValueVO> timelylist = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_TIMELY_VALUE_LIST) + "?site_id=8&type="+type );
		Log.e("getTimelyValueList","result = "+result+" type = "+type);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				timelylist = new ArrayList<TimelyValueVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("list"));

					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						TimelyValueVO timelyVo = new TimelyValueVO();
						timelyVo.setId(items.getInt("id"));
						timelyVo.setSite_id(items.getInt("site_id"));
						timelyVo.setType(items.getInt("type"));
						timelyVo.setValue(items.getDouble("value"));
						timelyVo.setWrite_date(items.getString("write_date"));
						timelyVo.setWritetime(items.getString("writetime"));
						timelyVo.setWritetime_hms(items.getString("writetime_hms"));
						timelyVo.setWriter_user_id(items.getString("writer_user_id"));
						timelyVo.setWriter_user_name(items.getString("writer_user_name"));
						timelyVo.setIsMobile(items.getInt("isMobile"));

						timelylist.add(timelyVo);
					}
				} else {
					timelylist = null;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return timelylist;
	}

	public TimelyValueVO getTimelyValueById(final Context context, int type, int id){
		TimelyValueVO timelyVo = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_TIMELY_VALUE_BYID) + "?site_id=8&type="+type+"&id="+id );

		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				timelyVo = new TimelyValueVO();

				timelyVo.setId(item.getInt("id"));
				timelyVo.setSite_id(item.getInt("site_id"));
				timelyVo.setType(item.getInt("type"));
				timelyVo.setValue(item.getDouble("value"));
				timelyVo.setWrite_date(item.getString("write_date"));
				timelyVo.setWritetime(item.getString("writetime"));
				timelyVo.setWritetime_hms(item.getString("writetime_hms"));
				timelyVo.setWriter_user_id(item.getString("writer_user_id"));
				timelyVo.setWriter_user_name(item.getString("writer_user_name"));
				timelyVo.setIsMobile(item.getInt("isMobile"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return timelyVo;
	}

	public String insertTimelyValue( final Context context,
									//String site_id,
									String type,
									String value,
									String writer_user_id
									//,String isMobile
	)  {

		String resultString = "";
		String site_id = "8";
		//String type = "-1";
		String isMobile = "1";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("type", type ));
		params.add(new BasicNameValuePair("value", value));
		params.add(new BasicNameValuePair("writer_user_id", writer_user_id ));
		params.add(new BasicNameValuePair("isMobile", isMobile ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.KEPCO_TIMELY_VALUE_INSERT), params );
		Log.i("KEPCO_TIMELY_VALUE_INSERT", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}

		}

		return resultString;
	}

	public String updateTimelyValue( final Context context,
									String id,
									String value
	)  {

		String resultString = "";
		String isMobile = "1";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("value", value));
		params.add(new BasicNameValuePair("isMobile", isMobile ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.KEPCO_TIMELY_VALUE_UPDATE), params );
		Log.i("KEPCO_TIMELY_VALUE_UPDATE", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}

		}

		return resultString;
	}
	public String deleteTimelyValue( final Context context,
									String id)  {
		String resultString = "";

		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_TIMELY_VALUE_DELETE) + "?id=" + id);
		Log.i("KEPCO_TIMELY_VALUE_DELETE", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}

		return resultString;
	}
	public String deleteDailyValue( final Context context,
									String id)  {
		String resultString = "";

		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_DRILLING_DAILY_VALUE_DELETE) + "?id=" + id);
		Log.i("KEPCO_DRILLING_DAILY_VALUE_DELETE", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}

		return resultString;
	}

	public String updateDailyValue( final Context context,
									String id,
									String value
	)  {

		String resultString = "";
		String isMobile = "1";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("value", value));
		params.add(new BasicNameValuePair("isMobile", isMobile ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.KEPCO_DRILLING_DAILY_VALUE_UPDATE), params );
		Log.i("KEPCO_DRILLING_DAILY_VALUE_UPDATE", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}

		}

		return resultString;
	}
	public String insertDailyValue( final Context context,
										  //String site_id,
										  //String type,
										  String input_date,
										  String value,
										  String writer_user_id
										//,String isMobile
										)  {

		String resultString = "";
		String site_id = "8";
		String type = "1";
		String isMobile = "1";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("type", type ));
		params.add(new BasicNameValuePair("input_date", input_date ));
		params.add(new BasicNameValuePair("value", value));
		params.add(new BasicNameValuePair("writer_user_id", writer_user_id ));
		params.add(new BasicNameValuePair("isMobile", isMobile ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.KEPCO_DRILLING_DAILY_VALUE_INSERT), params );
		Log.i("KEPCO_DRILLING_DAILY_VALUE_INSERT", result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}

			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}

		}

		return resultString;
	}


	public DailyValueVO getDailyValueById(final Context context, int id){
		DailyValueVO dailyVo = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_DRILLING_DAILY_VALUE_BYID) + "?site_id=8&type=-1&id="+id );

		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				dailyVo = new DailyValueVO();

				dailyVo.setId(item.getInt("id"));
				dailyVo.setSite_id(item.getInt("site_id"));
				dailyVo.setType(item.getInt("type"));
				dailyVo.setInput_date(item.getString("input_date"));
				dailyVo.setValue(item.getDouble("value"));
				dailyVo.setWritetime(item.getString("writetime"));
				dailyVo.setUpdatetime(item.getString("updatetime"));
				dailyVo.setWriter_user_id(item.getString("writer_user_id"));
				dailyVo.setWriter_user_name(item.getString("writer_user_name"));
				dailyVo.setIsMobile(item.getInt("isMobile"));
				dailyVo.setSum_value(item.getDouble("sum_value"));
				dailyVo.setAvg_value(item.getDouble("avg_value"));
				dailyVo.setDay_count(item.getInt("day_count"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return dailyVo;
	}

	public ArrayList<DailyValueVO> getDailyValueList(final Context context){
		ArrayList<DailyValueVO> daillylist = null;
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_DRILLING_DAILY_VALUE) + "?site_id=8&type=-1" );
		//Log.e("getTimelyValueList","result = "+result);
		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				daillylist = new ArrayList<DailyValueVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("list"));

					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						DailyValueVO dailyVo = new DailyValueVO();
						dailyVo.setId(items.getInt("id"));
						dailyVo.setSite_id(items.getInt("site_id"));
						dailyVo.setType(items.getInt("type"));
						dailyVo.setInput_date(items.getString("input_date"));
						dailyVo.setValue(items.getDouble("value"));
						dailyVo.setWritetime(items.getString("writetime"));
						//dailyVo.setUpdatetime(items.getString("updatetime"));
						dailyVo.setWriter_user_id(items.getString("writer_user_id"));
						dailyVo.setWriter_user_name(items.getString("writer_user_name"));
						dailyVo.setIsMobile(items.getInt("isMobile"));
						dailyVo.setSum_value(items.getDouble("sum_value"));
						dailyVo.setAvg_value(items.getDouble("avg_value"));
						dailyVo.setDay_count(items.getInt("day_count"));

						daillylist.add(dailyVo);
					}
				} else {
					daillylist = null;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return daillylist;
	}
	/**
	 *
	 모니터링 데이터 =>  /json/getKepcoData

	 jo.put("monitorVO", new JSONObject(monitorVO));  --모니터 데이터
	 jo.put("sensorVO", new JSONObject(sensorVO)); --환경데이터

	 MonitorVO 데이터
	 private double total_const;//총 연장
	 private double total_meter;//누계굴진
	 private double depth;//심도
	 private double remain_meter;//잔량
	 private double today_meter;//오늘(일) 굴진량
	 private double this_mon_meter;//월 굴진량
	 private double avg_meter;//평균 굴진량
	 private double month_avg_meter;//월평균 굴진량

	 //select * From kepco_section;
	 //굴진거리에 따른 문구
	 private String text1;//지층구분
	 private String text2;//RMR암반등급
	 private String text3;//그라우팅타입
	 private String text4;//Target압력

	 //select * From timely_value;
	 //수시 입력 데이터
	 private double value1;//염분농도
	 private double value2;//막장압력
	 private double value3;//오탁수 처리량
	 private double value4;//굴진 상태
	 private double value5;//총 인력

	 sensorVO 데이터
	 private double o2;//산소
	 private double co;//일산화탄소
	 private double h2s;//황화수소
	 private double gas;//가연성가스
	 */
	public void getKepcoData( final Context context ){
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.KEPCO_GET_MONITORING_DATA) );

		if( result != null && !result.equals("") ) {

			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject monitorCO = null;
				CustomJsonObject sensorCO = null;

				CustomJsonObject item = new CustomJsonObject(jsonObj);
				Log.e("getKepcoData",item.getString("monitorVO"));
				Log.e("getKepcoData",item.getString("sensorVO"));
				if( item.getString("result").equals("true") ) {
					monitorCO = new CustomJsonObject(item.getString("monitorVO"));
					sensorCO = new CustomJsonObject(item.getString("sensorVO"));
					KepcoMonitorVO kepcoMonitorVO = new KepcoMonitorVO();

					kepcoMonitorVO.setValue1(Double.valueOf(monitorCO.getString("value1")));
					kepcoMonitorVO.setValue2(Double.valueOf(monitorCO.getString("value2")));
					kepcoMonitorVO.setValue3(Double.valueOf(monitorCO.getString("value3")));
					kepcoMonitorVO.setValue4(Double.valueOf(monitorCO.getString("value4")));
					kepcoMonitorVO.setValue5(Double.valueOf(monitorCO.getString("value5")));
					if(monitorCO.has("value6")) kepcoMonitorVO.setValue6(Double.valueOf(monitorCO.getString("value6")));
					else kepcoMonitorVO.setValue6(Double.valueOf("0.0"));

					kepcoMonitorVO.setText1(String.valueOf(monitorCO.getString("text1")));
					kepcoMonitorVO.setText2(String.valueOf(monitorCO.getString("text2")));
					kepcoMonitorVO.setText3(String.valueOf(monitorCO.getString("text3")));
					if(monitorCO.has("text4")) kepcoMonitorVO.setText4(String.valueOf(monitorCO.getString("text4")));
					else kepcoMonitorVO.setText4(String.valueOf("0.0"));

					kepcoMonitorVO.setTotal_const(Double.valueOf(monitorCO.getString("total_const")));
					kepcoMonitorVO.setTotal_meter(Double.valueOf(monitorCO.getString("total_meter")));
					kepcoMonitorVO.setDepth(Double.valueOf(monitorCO.getString("depth")));
					kepcoMonitorVO.setRemain_meter(Double.valueOf(monitorCO.getString("remain_meter")));
					kepcoMonitorVO.setToday_meter(Double.valueOf(monitorCO.getString("today_meter")));
					kepcoMonitorVO.setThis_mon_meter(Double.valueOf(monitorCO.getString("this_mon_meter")));
					kepcoMonitorVO.setAvg_meter(Double.valueOf(monitorCO.getString("avg_meter")));
					kepcoMonitorVO.setMonth_avg_meter(Double.valueOf(monitorCO.getString("month_avg_meter")));

					KepcoSensorVO kepcoSensorVO = new KepcoSensorVO();
					kepcoSensorVO.setSite_id(String.valueOf(sensorCO.getString("site_id")));
					kepcoSensorVO.setPlace_id(Integer.valueOf(sensorCO.getString("place_id")));
					kepcoSensorVO.setO2(Double.valueOf(sensorCO.getString("o2")));
					kepcoSensorVO.setCo(Double.valueOf(sensorCO.getString("co")));
					kepcoSensorVO.setH2s(Double.valueOf(sensorCO.getString("h2s")));
					kepcoSensorVO.setGas(Double.valueOf(sensorCO.getString("gas")));
					kepcoSensorVO.setWritetime(String.valueOf(sensorCO.getString("writetime")));

					((UserSystemApplication)context.getApplicationContext()).setKepcoData(kepcoMonitorVO, kepcoSensorVO);
				} else {
					//((UserSystemApplication)context.getApplicationContext()).setKepcoData(null, null);
				}
			} catch(Exception e) {

				e.printStackTrace();
			}
		}
	}

	
	public ArrayList<MobileEquipVO> preCheckList( final Context context, String level, String code1)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DEVICE_PRE_CHECK_LIST) + "?level=" +level + "&code1=" + code1 );

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setTitle(items.getString("title"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setContent_img(items.getString("content_img"));
	
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipResultCheckList( final Context context, String check_gubun, String checkdate, String e_id)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_DAILY_CHECK_LIST) + "?check_gubun=" +check_gubun + "&searchdate=" + checkdate + "&id=" + e_id );

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setGubun(items.getString("e_id"));
						mobileVo.setTitle(items.getString("title"));
						mobileVo.setContent(items.getString("content"));
						
						
						mobileVo.setCheck_gubun(items.getString("check_gubun"));
						mobileVo.setCheck_id(items.getString("check_id"));
						
						mobileVo.setCheckdate(items.getString("checkdate"));
						mobileVo.setCheck_result(items.getString("check_result"));
						mobileVo.setCheck_img(items.getString("check_img"));
						
						mobileVo.setCheck_user_id(items.getString("check_user_id"));
						mobileVo.setConfirm_user_id(items.getString("confirm_user_id"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setReason(items.getString("reason"));
	
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getDeviceBasicList( final Context context)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DEVICE_BASIC_LIST) );

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setCode1(items.getString("code1"));
	
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipDriverList( final Context context, String cont_id, String site_id)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DRIVER_LIST) + "?cont_id=" +cont_id + "&site_id=" +site_id );
		Log.i("CONSMAN_EQUIP_DRIVER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setId(items.getString("id"));
						mobileVo.setDriverName(items.getString("name"));
						mobileVo.setDriverPhone(items.getString("phone"));
						mobileVo.setDriverLicense(items.getString("license"));
						mobileVo.setDriverRank(items.getString("rank"));
	
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	
	public ArrayList<MobileEquipVO> getEquipList( final Context context, String cont_id, String site_id, String status, String searchdate)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_LIST) + "?cont_id=" +cont_id + "&site_id=" +site_id + "&status=" +status+ "&searchdate=" +searchdate );
		Log.i("CONSMAN_EQUIP_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setCategory_id(items.getString("category_id"));
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setCode1(items.getString("code1"));
						mobileVo.setU_id(items.getString("worker_id"));
						mobileVo.setDriverName(items.getString("name"));
						mobileVo.setDriverPhone(items.getString("phone"));
						mobileVo.setDriverLicense(items.getString("license"));
						
						mobileVo.setDeviceNum(items.getString("manage_num"));
						mobileVo.setRentcontname(items.getString("rent_cont_name"));
						mobileVo.setContract_form(items.getString("contract_form"));
						mobileVo.setRealsize(items.getString("realsize"));
						mobileVo.setCheck_start(items.getString("check_start"));
						mobileVo.setCheck_end(items.getString("check_end"));
						mobileVo.setInsuranceStart(items.getString("insur_start"));
						mobileVo.setInsurenceEnd(items.getString("insur_end"));
						mobileVo.setDeviceImg(items.getString("device_imge"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setSmart(items.getString("smart"));
						mobileVo.setIncomedate(items.getString("incomedate"));
						mobileVo.setOutcomedate(items.getString("outcomedate"));
						mobileVo.setConfirm_id(items.getString("confirm_id"));
						mobileVo.setTag_id(items.getString("tag_id"));
						mobileVo.setConfirm_name(items.getString("confirm_name"));
						mobileVo.setReg_user_id(items.getString("reg_user_id"));
						mobileVo.setReg_user_name(items.getString("reg_user_name"));
						mobileVo.setDevice_name(items.getString("device_name"));
						mobileVo.setCont_name(items.getString("cont_name"));
						mobileVo.setNo(items.getString("no"));
						mobileVo.setDaily_id(items.getString("daily_id"));
						
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipdDailyResultList( final Context context, String cont_id, String site_id, String searchdate)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_DAILY_LIST) + "?cont_id=" +cont_id + "&site_id=" +site_id + "&searchdate=" +searchdate );
		Log.i("CONSMAN_EQUIP_RESULT_DAILY_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setCategory_id(items.getString("category_id"));
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setCode1(items.getString("code1"));
						mobileVo.setU_id(items.getString("worker_id"));
						mobileVo.setDriverName(items.getString("name"));
						mobileVo.setDriverPhone(items.getString("phone"));
						mobileVo.setDriverLicense(items.getString("license"));
						
						mobileVo.setDeviceNum(items.getString("manage_num"));
						mobileVo.setRentcontname(items.getString("rent_cont_name"));
						mobileVo.setContract_form(items.getString("contract_form"));
						mobileVo.setRealsize(items.getString("realsize"));
						mobileVo.setCheck_start(items.getString("check_start"));
						mobileVo.setCheck_end(items.getString("check_end"));
						mobileVo.setInsuranceStart(items.getString("insur_start"));
						mobileVo.setInsurenceEnd(items.getString("insur_end"));
						mobileVo.setDeviceImg(items.getString("device_imge"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setSmart(items.getString("smart"));
						mobileVo.setIncomedate(items.getString("incomedate"));
						mobileVo.setOutcomedate(items.getString("outcomedate"));
						mobileVo.setConfirm_id(items.getString("confirm_id"));
						mobileVo.setTag_id(items.getString("tag_id"));
						mobileVo.setConfirm_name(items.getString("confirm_name"));
						mobileVo.setReg_user_id(items.getString("reg_user_id"));
						mobileVo.setReg_user_name(items.getString("reg_user_name"));
						mobileVo.setDevice_name(items.getString("device_name"));
						mobileVo.setCont_name(items.getString("cont_name"));
						mobileVo.setDstatus(items.getString("dstatus"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setNo(items.getString("no"));
						
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipdDailyResultGsilList( final Context context)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DAILY_RESULT_GSIL_LIST));
		Log.i("CONSMAN_EQUIP_DAILY_RESULT_GSIL_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setCategory_id(items.getString("category_id"));
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setCode1(items.getString("code1"));
						mobileVo.setU_id(items.getString("worker_id"));
						mobileVo.setDriverName(items.getString("name"));
						mobileVo.setDriverPhone(items.getString("phone"));
						mobileVo.setDriverLicense(items.getString("license"));
						
						mobileVo.setDeviceNum(items.getString("manage_num"));
						mobileVo.setRentcontname(items.getString("rent_cont_name"));
						mobileVo.setContract_form(items.getString("contract_form"));
						mobileVo.setRealsize(items.getString("realsize"));
						mobileVo.setCheck_start(items.getString("check_start"));
						mobileVo.setCheck_end(items.getString("check_end"));
						mobileVo.setInsuranceStart(items.getString("insur_start"));
						mobileVo.setInsurenceEnd(items.getString("insur_end"));
						mobileVo.setDeviceImg(items.getString("device_imge"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setSmart(items.getString("smart"));
						mobileVo.setIncomedate(items.getString("incomedate"));
						mobileVo.setOutcomedate(items.getString("outcomedate"));
						mobileVo.setConfirm_id(items.getString("confirm_id"));
						mobileVo.setTag_id(items.getString("tag_id"));
						mobileVo.setConfirm_name(items.getString("confirm_name"));
						mobileVo.setReg_user_id(items.getString("reg_user_id"));
						mobileVo.setReg_user_name(items.getString("reg_user_name"));
						mobileVo.setDevice_name(items.getString("device_name"));
						mobileVo.setCont_name(items.getString("cont_name"));
						mobileVo.setDstatus(items.getString("dstatus"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setNo(items.getString("no"));
						
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipdDailycCheckList( final Context context, String cont_id, String site_id, String searchdate, String check_gubun, String id)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_DAILY_CHECK_LIST) + "?cont_id=" +cont_id + "&site_id=" +site_id + "&searchdate=" +searchdate  + "&check_gubun=" +check_gubun + "&id=" +id );
		Log.i("CONSMAN_EQUIP_RESULT_DAILY_CHECK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setE_id(items.getString("e_id"));
						mobileVo.setTitle(items.getString("title"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setCheck_gubun(items.getString("check_gubun"));
						mobileVo.setCheck_id(items.getString("check_id"));
						mobileVo.setCheckdate(items.getString("checkdate"));
						mobileVo.setCheck_result(items.getString("check_result"));
						mobileVo.setCheck_img(items.getString("check_img"));
						mobileVo.setCheck_user_id(items.getString("check_user_id"));
						mobileVo.setConfirm_user_id(items.getString("confirm_user_id"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setReason(items.getString("reason"));
						
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public ArrayList<MobileEquipVO> getEquipdDailyList( final Context context, String cont_id, String site_id, String searchdate, String check_gubun)  {
		ArrayList<MobileEquipVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DAILY_LIST) + "?cont_id=" +cont_id + "&site_id=" +site_id + "&searchdate=" +searchdate  + "&check_gubun=" +check_gubun );
		Log.i("CONSMAN_EQUIP_DAILY_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileEquipVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setCategory_id(items.getString("category_id"));
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setCode1(items.getString("code1"));
						mobileVo.setU_id(items.getString("worker_id"));
						mobileVo.setDriverName(items.getString("name"));
						mobileVo.setDriverPhone(items.getString("phone"));
						mobileVo.setDriverLicense(items.getString("license"));
						
						mobileVo.setDeviceNum(items.getString("manage_num"));
						mobileVo.setRentcontname(items.getString("rent_cont_name"));
						mobileVo.setContract_form(items.getString("contract_form"));
						mobileVo.setRealsize(items.getString("realsize"));
						mobileVo.setCheck_start(items.getString("check_start"));
						mobileVo.setCheck_end(items.getString("check_end"));
						mobileVo.setInsuranceStart(items.getString("insur_start"));
						mobileVo.setInsurenceEnd(items.getString("insur_end"));
						mobileVo.setDeviceImg(items.getString("device_imge"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setSmart(items.getString("smart"));
						mobileVo.setIncomedate(items.getString("incomedate"));
						mobileVo.setOutcomedate(items.getString("outcomedate"));
						mobileVo.setConfirm_id(items.getString("confirm_id"));
						mobileVo.setTag_id(items.getString("tag_id"));
						mobileVo.setConfirm_name(items.getString("confirm_name"));
						mobileVo.setReg_user_id(items.getString("reg_user_id"));
						mobileVo.setReg_user_name(items.getString("reg_user_name"));
						mobileVo.setDevice_name(items.getString("device_name"));
						mobileVo.setCont_name(items.getString("cont_name"));
						mobileVo.setDstatus(items.getString("dstatus"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setNo(items.getString("no"));
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	public String insertEqiupCheckResult( final Context context, 
			String e_id, 
			String check_gubun, 
			String check_id,
			String checkdate,
			String check_result,
			String check_img,
			String check_user_id,
			String status,
			String reason,
			String site_id,
			String cont_id,
			String check_content,
			String deviceName)  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("e_id", e_id));
		params.add(new BasicNameValuePair("check_gubun", check_gubun ));
		params.add(new BasicNameValuePair("check_id", check_id ));
		params.add(new BasicNameValuePair("checkdate", checkdate));
		params.add(new BasicNameValuePair("check_result", check_result ));
		params.add(new BasicNameValuePair("check_img", check_img ));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("reason", reason ));
		
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("check_content", check_content ));
		params.add(new BasicNameValuePair("deviceName", deviceName ));
		
		
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_INSERT_CHECK_RESULT), params );
		Log.i("CONSMAN_EQUIP_INSERT_CHECK_RESULT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public MobileEquipVO equipTogatherWorkerList( final Context context, String id ) {
		MobileEquipVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_TOGATHER_WORKER_LIST) + "?id=" + id );
		Log.i("CONSMAN_EQUIP_TOGATHER_WORKER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileEquipVO();
					mobileVo.setNamelist(item.getString("namelist"));
					mobileVo.setIdlist(item.getString("idlist"));
					
				} else {
					mobileVo = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return mobileVo;
	}
	
	
	public String updateEqiupResultStatus( final Context context, 
			String id, 
			String status, 
			String sign,
			String incomedate,
			String confirm_user_id,
			String confirm_name)  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("sign", sign));
		params.add(new BasicNameValuePair("incomedate", incomedate ));
		params.add(new BasicNameValuePair("confirm_user_id", confirm_user_id ));
		params.add(new BasicNameValuePair("confirm_name", confirm_name ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_SATATUS), params );
		Log.i("CONSMAN_EQUIP_RESULT_SATATUS", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateCheckResultStatus( final Context context, 
			String check_gubun, 
			String checkdate, 
			String id,
			String status,
			String confirm_user_id)  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("checkdate", checkdate));
		params.add(new BasicNameValuePair("check_gubun", check_gubun ));
		params.add(new BasicNameValuePair("confirm_user_id", confirm_user_id ));
		
		
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_CHECK_STATUS), params );
		Log.i("CONSMAN_EQUIP_RESULT_CHECK_STATUS", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String updateDailyResultStatus( final Context context, 
			String checkdate, 
			String id,
			String status )  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("checkdate", checkdate));
		
		
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_RESULT_DAILY_STATUS), params );
		Log.i("CONSMAN_EQUIP_RESULT_DAILY_STATUS", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String insertEqiup( final Context context, 
			String cont_id, 
			String site_id, 
			String category_id,
			String manage_num,
			String rent_cont_name,
			String contract_form,
			String realsize,
			String check_start,
			String check_end,
			String insur_start,
			String insur_end,
			String device_img,
			String status,
			String smart,
			String driverName, 
			String driverPhone,
			String driverLicense,
			String driverRank,
			String worker_id,
			String incomedate,
			String reg_user_id,
			String reg_user_name,
			String device_name,
			String tag_id,
			String idlist )  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("category_id", category_id ));
		params.add(new BasicNameValuePair("manage_num", manage_num));
		params.add(new BasicNameValuePair("rent_cont_name", rent_cont_name ));
		params.add(new BasicNameValuePair("contract_form", contract_form ));
		params.add(new BasicNameValuePair("realsize", realsize ));
		params.add(new BasicNameValuePair("check_start", check_start ));
		params.add(new BasicNameValuePair("check_end", check_end ));
		params.add(new BasicNameValuePair("insur_start", insur_start ));
		params.add(new BasicNameValuePair("insur_end", insur_end ));
		params.add(new BasicNameValuePair("device_img", device_img ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("driverName", driverName ));
		params.add(new BasicNameValuePair("driverPhone", driverPhone ));
		params.add(new BasicNameValuePair("driverLicense", driverLicense ));
		params.add(new BasicNameValuePair("driverRank", driverRank ));
		params.add(new BasicNameValuePair("worker_id", worker_id ));		
		params.add(new BasicNameValuePair("incomedate", incomedate ));
		params.add(new BasicNameValuePair("reg_user_id", reg_user_id ));
		params.add(new BasicNameValuePair("reg_user_name", reg_user_name ));
		params.add(new BasicNameValuePair("device_name", device_name ));
		params.add(new BasicNameValuePair("tag_id", tag_id ));
		params.add(new BasicNameValuePair("idlist", idlist ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_EQUIP), params );
		Log.i("CONSMAN_INSERT_EQUIP", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = item.getString("e_id");
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String updateEqiup( final Context context, 
			String e_id,
			String cont_id, 
			String site_id, 
			String category_id,
			String manage_num,
			String rent_cont_name,
			String contract_form,
			String realsize,
			String check_start,
			String check_end,
			String insur_start,
			String insur_end,
			String device_img,
			String status,
			String smart,
			String driverName, 
			String driverPhone,
			String driverLicense,
			String driverRank,
			String worker_id,
			String incomedate,
			String reg_user_id,
			String reg_user_name,
			String device_name,
			String idlist)  {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("e_id", e_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("category_id", category_id ));
		params.add(new BasicNameValuePair("manage_num", manage_num));
		params.add(new BasicNameValuePair("rent_cont_name", rent_cont_name ));
		params.add(new BasicNameValuePair("contract_form", contract_form ));
		params.add(new BasicNameValuePair("realsize", realsize ));
		params.add(new BasicNameValuePair("check_start", check_start ));
		params.add(new BasicNameValuePair("check_end", check_end ));
		params.add(new BasicNameValuePair("insur_start", insur_start ));
		params.add(new BasicNameValuePair("insur_end", insur_end ));
		params.add(new BasicNameValuePair("device_img", device_img ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("smart", smart ));
		params.add(new BasicNameValuePair("driverName", driverName ));
		params.add(new BasicNameValuePair("driverPhone", driverPhone ));
		params.add(new BasicNameValuePair("driverLicense", driverLicense ));
		params.add(new BasicNameValuePair("driverRank", driverRank ));
		params.add(new BasicNameValuePair("worker_id", worker_id ));		
		params.add(new BasicNameValuePair("incomedate", incomedate ));
		params.add(new BasicNameValuePair("reg_user_id", reg_user_id ));
		params.add(new BasicNameValuePair("reg_user_name", reg_user_name ));
		params.add(new BasicNameValuePair("device_name", device_name ));
		params.add(new BasicNameValuePair("idlist", idlist ));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_EQUIP), params );
		Log.i("CONSMAN_UPDATE_EQUIP", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteEquip( final Context context, 
			String idlist)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DELETE) + "?idlist=" + idlist);
		Log.i("CONSMAN_EQUIP_DELETE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}

	public String duplicateEquipWorkerId( final Context context, 
			String worker_id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DUPLICATE_CHECK_USER) + "?worker_id=" + worker_id);
		Log.i("CONSMAN_EQUIP_DUPLICATE_CHECK_USER", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				resultString = item.getString("result");
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}

	
	public String deleteEquipItem( final Context context,
			String id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DELETE_ITEM) + "?e_id=" + id);
		Log.i("CONSMAN_EQUIP_DELETE_ITEM", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String insertDailyEqiup( final Context context, 
			String idlist, String cont_id, String u_id, String workdate, String status)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DAILY_INSERT) + "?idlist=" + idlist+ "&cont_id=" + cont_id+ "&u_id=" + u_id+ "&workdate=" + workdate+ "&status=" + status);
		Log.i("CONSMAN_EQUIP_DAILY_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String outEqiup( final Context context, 
			String idlist, String cont_id, String u_id, String workdate, String status)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_OUT_ITEM) + "?idlist=" + idlist+ "&cont_id=" + cont_id+ "&u_id=" + u_id+ "&workdate=" + workdate+ "&status=" + status);
		Log.i("CONSMAN_EQUIP_OUT_ITEM", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}

	public String deleteCheckList( final Context context, 
			String e_id, 
			String checkdate,
			String check_gubun)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_EQUIP_DELETE_CHECK_LIST) + "?e_id=" + e_id + "&check_gubun=" + check_gubun + "&checkdate=" + checkdate );
		Log.i("CONSMAN_EQUIP_DELETE_CHECK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public ArrayList<MobileVO> deviceList( final Context context, String gubun )  {
		ArrayList<MobileVO> devicelist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DEVICE_LIST) + "?gubun=" + gubun );
		Log.i("GSIL_DEVICELIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				devicelist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setImage(items.getString("image"));
						mobileVo.setGubun(items.getString("gubun"));
	
						devicelist.add(mobileVo);
					}
				} else {
					devicelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return devicelist;
	}
	
	
	public ArrayList<MobileVO> getDailyList( final Context context )  {
		ArrayList<MobileVO> dailylist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_LIST) );
		Log.i("GSIL_DAILYLIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				dailylist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setDate(items.getString("date"));
						dailylist.add(mobileVo);
					}
				} else {
					dailylist = null;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return dailylist;
	}

	public ArrayList<MobileVO> checkList( final Context context, String deviceId )  {
		ArrayList<MobileVO> checkList = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CHECK_LIST) + "?deviceId=" + deviceId );
		Log.i("GSIL_CHECKLIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				checkList = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setDevice_id(items.getString("device_id"));
						mobileVo.setNo(items.getString("no"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setFilename(items.getString("filename"));
						mobileVo.setVirtname(items.getString("virtname"));
						
						checkList.add(mobileVo);
					}
				} else {
					checkList = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return checkList;
	}
	
	public ArrayList<MobileVO> datecheckList( final Context context, String searchdate )  {
		ArrayList<MobileVO> datecheckList = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DATE_CHECK_LIST) + "?searchdate=" + searchdate );
		Log.i("GSIL_CHECKLIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				datecheckList = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setNfc_id(items.getString("nfc_id"));
						mobileVo.setTagid(items.getString("tagid"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setDate(items.getString("date"));
						mobileVo.setTime(items.getString("time"));
 						mobileVo.setCheckyn(items.getString("checkyn"));
						datecheckList.add(mobileVo);
					}
				} else {
					datecheckList = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		
		return datecheckList;
	}
	
	public ArrayList<MobileVO> resultCheckList( final Context context, String nfcId ,String searchdate )  {
		ArrayList<MobileVO> datecheckList = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RESULT_CHECK_LIST) + "?searchdate=" + searchdate +"&nfcId=" +nfcId );
		Log.i("GSIL_CHECKLIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				datecheckList = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setNfc_id(items.getString("nfc_id"));
						mobileVo.setYn(items.getString("yn"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setDate(items.getString("date"));
						mobileVo.setNo(items.getString("no"));
 						mobileVo.setReason(items.getString("reason"));
 						mobileVo.setFilename(items.getString("filename"));
 						
						datecheckList.add(mobileVo);
					}
				} else {
					datecheckList = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		
		return datecheckList;
	}
	 
	public String nfcInsert( final Context context, String name, String deviceId, String tagid ) throws UnsupportedEncodingException  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_NFC_INSERT) + "?name=" + URLEncoder.encode(name, "UTF-8") + "&deviceId=" + deviceId + "&tagid=" + tagid );
		Log.i("NFC_INSERT", URLEncoder.encode(name, "UTF-8"));
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteInspect( final Context context, 
			String nfcId, 
			String date )  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_INSPECT_DETAIL) + "?nfcId=" + nfcId + "&date=" + date );
		Log.i("DELETE_INSPECT_DETAIL", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String insertInspect( final Context context, 
			String nfcId, 
			String date, 
			String time,
			String checkyn)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_INSPECT) + "?nfcId=" + nfcId + "&date=" + date + "&time=" + time+ "&checkyn=" + checkyn );
		Log.i("INSERT_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateInspect( final Context context, 
			String nfcId, 
			String date,
			String checkyn)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_INSPECT) + "?nfcId=" + nfcId + "&date=" + date + "&checkyn=" + checkyn );
		Log.i("INSPECT_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String insertInspectDetail( final Context context, 
			String nfcId, 
			String date, 
			String no,
			String yn,
			String filename,
			String reason)  {
		String resultString = "";
		
		String fileexe ="";
		if( !filename.equals("") ) {
			fileexe = filename.substring((filename.indexOf(".")+1));
			filename = filename.substring(0,filename.indexOf("."));
		}
		
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nfcId", nfcId));
		params.add(new BasicNameValuePair("date", date ));
		params.add(new BasicNameValuePair("no", no ));
		params.add(new BasicNameValuePair("yn", yn));
		params.add(new BasicNameValuePair("filename", filename ));
		params.add(new BasicNameValuePair("fileexe", fileexe ));
		params.add(new BasicNameValuePair("reason", reason ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_INSPECT_DETAIL), params );
		
		
		
//		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_INSPECT_DETAIL) + "?nfcId=" + nfcId + "&date=" + date + "&no=" + no+ "&yn=" + yn + "&filename=" +filename + "&fileexe=" +fileexe + "&reason=" +reason );
		Log.i("INSEPECT_DETAIL_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public MobileVO deviceInfo( final Context context, String tagid ) {
		MobileVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DEVICE_INFO) + "?tagid=" + tagid );
		Log.i("DEVICE_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileVO();
					mobileVo.setId(item.getString("id"));
					mobileVo.setDevice_id(item.getString("device_id"));
					mobileVo.setTagid(item.getString("tagid"));
					mobileVo.setName(item.getString("name"));
					mobileVo.setGubun(item.getString("gubun"));
					mobileVo.setFilename(item.getString("image"));
					
				} else {
					mobileVo = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return mobileVo;
	}
	
	public MobileVO checkOneInfo( final Context context, String nfcId, String searchdate ) {
		MobileVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CHECK_ONE_INFO) + "?nfcId=" + nfcId + "&searchdate=" +searchdate );
		Log.i("DEVICE_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileVO();
					mobileVo.setNfc_id(item.getString("nfc_id"));
					mobileVo.setDate(item.getString("date"));
					mobileVo.setTime(item.getString("time"));
					mobileVo.setCheckyn(item.getString("checkyn"));
					
					
				} else {
					mobileVo = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return mobileVo;
	}

	
	public String duplicateNfcTagId( final Context context, String tagid ) {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DUPLICATE_NFC_TAG_ID) + "?tagid=" + tagid );
		Log.i("DEVICE_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public MobileUserVO mobileUserLogin( final Context context, String uid, String pw, String pid, String phone ) {
		MobileUserVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_USER_LOGIN) + "?uid=" + uid + "&pw=" + pw + "&pid=" + pid + "&phone=" + phone);
		Log.i("LOGIN_INFO", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileUserVO();
					mobileVo.setId(item.getString("id"));
					mobileVo.setCont_id(item.getString("cont_id"));
					mobileVo.setRole_code(item.getString("role_code"));
					mobileVo.setName(item.getString("name"));
					mobileVo.setUserid(item.getString("userid"));
					mobileVo.setPassword(item.getString("password"));
					mobileVo.setPhone(item.getString("phone"));
					mobileVo.setEmail(item.getString("email"));
					mobileVo.setGrade(item.getString("grade"));
					mobileVo.setUseyn(item.getString("useyn"));
					mobileVo.setCname(item.getString("cname"));
					mobileVo.setType(item.getString("type"));
					mobileVo.setSname(item.getString("sname"));
					mobileVo.setSite_id(item.getString("site_id"));
					mobileVo.setDtype(item.getString("dtype"));
					mobileVo.setDcode(item.getString("dcode"));
					mobileVo.setRname(item.getString("rname"));
				} else {
					mobileVo  = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return mobileVo;
	}
	
	
	public ArrayList<MobileWtypeVO> workTypeList( final Context context, String gubun )  {
		ArrayList<MobileWtypeVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORK_TYLE)+ "?gubun=" + gubun);
		Log.i("GSIL_WORK_TYPE_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWtypeVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWtypeVO mobileVo = new MobileWtypeVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setUseyn(items.getString("useyn"));
						mobileVo.setGubun(items.getString("gubun"));
	
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileUserVO> getSSNowStatusData( final Context context, String site_id )  {
		ArrayList<MobileUserVO> wlist = null;
		
		if( context == null ) {
			return null;
		}
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_SS_COUNT_DATA)+ "?site_id=" + site_id);
		Log.i("CONSMAN_GET_SS_COUNT_DATA", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setCountdata(items.getString("countdata"));
						mobileVo.setGubun(items.getString("gubun"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertWorker( final Context context, 
			String phone, 
			String gubun, 
			String name,
			String jumin,
			String country,
			String passno,
			String first,
			String t_id,
			String cont_id,
			String singoyn,
			String site_id,
			String edudate,
			String firstdate,
			String imagename)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("gubun", gubun ));
		params.add(new BasicNameValuePair("name", name ));
		params.add(new BasicNameValuePair("jumin", jumin));
		params.add(new BasicNameValuePair("country", country ));
		params.add(new BasicNameValuePair("passno", passno ));
		params.add(new BasicNameValuePair("first", first ));
		params.add(new BasicNameValuePair("t_id", t_id ));
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("singoyn", singoyn ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("tagid", "" ));
		params.add(new BasicNameValuePair("edudate", edudate ));
		params.add(new BasicNameValuePair("firstdate", firstdate ));
		params.add(new BasicNameValuePair("imagename", imagename ));


		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORKER_INSERT), params );
		
		Log.i("WORKER_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateWorker( final Context context, 
			String id,
			String phone, 
			String gubun, 
			String name,
			String jumin,
			String country,
			String passno,
			String t_id,
			String singoyn,
			String cphone,
			String site_id,
			String edudate,
			String firstdate,
			String imagename)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("gubun", gubun ));
		params.add(new BasicNameValuePair("name", name ));
		params.add(new BasicNameValuePair("jumin", jumin));
		params.add(new BasicNameValuePair("country", country ));
		params.add(new BasicNameValuePair("passno", passno ));
		params.add(new BasicNameValuePair("t_id", t_id ));
		params.add(new BasicNameValuePair("singoyn", singoyn ));
		params.add(new BasicNameValuePair("cphone", cphone ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("tagid", "" ));
		params.add(new BasicNameValuePair("edudate", edudate ));
		params.add(new BasicNameValuePair("firstdate", firstdate ));
		params.add(new BasicNameValuePair("imagename", imagename ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORKER_UPDATE), params );
		
		Log.i("WORKER_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateMWorker( final Context context, 
			String id,
			String mon,
			String after,
			String content,
			String place)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("mon", mon));
		params.add(new BasicNameValuePair("after", after ));
		params.add(new BasicNameValuePair("content", content ));
		params.add(new BasicNameValuePair("place", place ));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MWORKER_UPDATE), params );
		
		Log.i("WORKER_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public MobileWorkerVO mobileCheckPhone( final Context context, String phone ) {
		MobileWorkerVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_PHONE_CHECK) + "?phone=" + phone );
		Log.i("LOGIN_INFO", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileWorkerVO();
					mobileVo.setId(item.getString("id"));
					mobileVo.setPhone(item.getString("phone"));
					mobileVo.setGubun(item.getString("gubun"));
					mobileVo.setName(item.getString("name"));
					mobileVo.setJumin(item.getString("jumin"));
					mobileVo.setCountry(item.getString("country"));
					mobileVo.setPassno(item.getString("passno"));
					mobileVo.setFirst(item.getString("first"));
					mobileVo.setT_id(item.getString("t_id"));
					mobileVo.setCont_id(item.getString("cont_id"));
					mobileVo.setSingoyn(item.getString("singoyn"));
				} else {
					mobileVo  = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return mobileVo;
	}
	
	public ArrayList<MobileWorkerVO> workerList( final Context context, String searchname, String site_id, String cont_id, String type, String wtype, String delyn, String firstdate , String gubun)  {
		ArrayList<MobileWorkerVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORKER_LIST)  + "?searchname=" + searchname+ "&site_id=" + site_id+ "&cont_id=" + "&type=" + type+ "&wtype=" + wtype+ "&delyn=" + delyn + "&firstdate=" + firstdate + "&gubun=" + gubun);
				
		Log.i("GSIL_WORK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWorkerVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWorkerVO mobileVo = new MobileWorkerVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setJumin(items.getString("jumin"));
						mobileVo.setCountry(items.getString("country"));
						mobileVo.setPassno(items.getString("passno"));
						mobileVo.setFirst(items.getString("first"));
						mobileVo.setT_id(items.getString("t_id"));
						mobileVo.setT_gubun(items.getString("t_gubun"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSingoyn("");
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setTagid(items.getString("tagid"));
						mobileVo.setLicense(items.getString("license"));
						mobileVo.setMemo(items.getString("memo"));
						
						mobileVo.setFirstdate(items.getString("firstdate"));
						mobileVo.setDelyn(items.getString("delyn"));
						mobileVo.setDelcontent(items.getString("delcontent"));
						mobileVo.setPno(items.getString("pno"));
						mobileVo.setEdudate(items.getString("edudate"));
						mobileVo.setImageName(items.getString("eduimage"));
						
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileWorkerVO> dayilyWorkerList( final Context context, String cont_id , String searchname , String type, String gubun, String site_id, String wtype, String delyn)  {
		ArrayList<MobileWorkerVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_WORKER_LIST)  + "?workdate=" + searchname + "&cont_id=" +cont_id+ "&type=" +type+ "&gubun=" +gubun + "&site_id=" + site_id + "&wtype=" + wtype+ "&delyn=" + delyn);
		Log.i("GSIL_WORK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWorkerVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWorkerVO mobileVo = new MobileWorkerVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setJumin(items.getString("jumin"));
						mobileVo.setCountry(items.getString("country"));
						mobileVo.setPassno(items.getString("passno"));
						mobileVo.setFirst(items.getString("first"));
						mobileVo.setT_id(items.getString("t_id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSingoyn("");
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setMon(items.getString("mon"));
						mobileVo.setAfter(items.getString("after"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setPlace(items.getString("place"));
						mobileVo.setMw_id(items.getString("mw_id"));
						mobileVo.setOption(items.getString("option"));
						mobileVo.setStartyn(items.getString("startyn"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setT_gubun(items.getString("t_gubun"));
						mobileVo.setMemo(items.getString("memo"));
						if( items.getString("option").equals("0") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(0);
						} else if( items.getString("option").equals("1") ) {
							mobileVo.setJu(-1);
							mobileVo.setYa(1);
						} else if( items.getString("option").equals("2") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(1);
						}
						mobileVo.setU_id(items.getString("u_id"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileWorkerVO> getworkdayList( final Context context )  {
		ArrayList<MobileWorkerVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_WORK_LIST)  );
		Log.i("GSIL_WORK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWorkerVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWorkerVO mobileVo = new MobileWorkerVO();
						mobileVo.setWorkdate(items.getString("workdate"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	
	
	public ArrayList<MobileWorkerVO> workerFList( final Context context, String searchname, String site_id )  {
		ArrayList<MobileWorkerVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_FIRST_WORKER_LIST)  + "?searchname=" + searchname + "&site_id=" + site_id);
		Log.i("GSIL_WORK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWorkerVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWorkerVO mobileVo = new MobileWorkerVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setJumin(items.getString("jumin"));
						mobileVo.setCountry(items.getString("country"));
						mobileVo.setPassno(items.getString("passno"));
						mobileVo.setFirst(items.getString("first"));
						mobileVo.setT_id(items.getString("t_id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSingoyn(items.getString("singoyn"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setCheck(0);
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String updateWorkerContid( final Context context, 
			String id, String cont_id, String memo, String wtype)  {
		String resultString = "";
		
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("id", id ));
		params.add(new BasicNameValuePair("memo", memo ));
		params.add(new BasicNameValuePair("wtype", wtype ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORKER_CONT_UPDATE), params );
		
		Log.i("CONSMAN_WORKER_CONT_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateFirst( final Context context, 
			String idlist, String site_id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_FIRST_WORKER_UPDATE) + "?idlist=" + idlist + "&site_id=" + site_id);
		Log.i("FIRST_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String userWarning( final Context context, 
			String idlist, String massage, String phone, String name, String namelist, String user_id, String site_id, String gubun)  {
		String resultString = "";

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("idlist", idlist));
		params.add(new BasicNameValuePair("massage", massage ));
		params.add(new BasicNameValuePair("phone", phone ));
		params.add(new BasicNameValuePair("name", name ));
		params.add(new BasicNameValuePair("namelist", namelist ));
		params.add(new BasicNameValuePair("user_id", user_id ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("gubun", gubun ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_USER_WARNING), params );
		
		
		Log.i("CONSMAN_USER_WARNING", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ArrayList<MobileMetaVO> metaFList( final Context context )  {
		ArrayList<MobileMetaVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_FRIST_META_LIST));
		Log.i("metaFList", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileMetaVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					String preLevel = "1";
					MobileMetaVO premobileVo = new MobileMetaVO();
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileMetaVO mobileVo = new MobileMetaVO();
						if( items.getString("code_level").equals("2") ) {
							mobileVo.setId(items.getString("id"));
							mobileVo.setClass_code(items.getString("class_code"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setCodename(items.getString("codename"));
							mobileVo.setCode_level(items.getString("code_level"));
							mobileVo.setLevel1(items.getString("level1"));
							preLevel = items.getString("level1");
							mobileVo.setLevel2(items.getString("level2"));
							mobileVo.setLevel3(items.getString("level3"));
							mobileVo.setLevel4(items.getString("level4"));
							mobileVo.setNum_info(items.getString("num_info"));
							mobileVo.setDamage(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setGubun("0");
							premobileVo = null;
							premobileVo = mobileVo;
							wlist.add(mobileVo);
						} else if( items.getString("code_level").equals("3") && preLevel.equals(items.getString("level1")) ) {
							MobileMetaVO submobileVo = new MobileMetaVO();
							submobileVo.setId(items.getString("id"));
							submobileVo.setClass_code(items.getString("class_code"));
							submobileVo.setCode(items.getString("code"));
							submobileVo.setCodename(items.getString("codename"));
							submobileVo.setCode_level(items.getString("code_level"));
							submobileVo.setLevel1(items.getString("level1"));
							submobileVo.setLevel2(items.getString("level2"));
							submobileVo.setLevel3(items.getString("level3"));
							submobileVo.setLevel4(items.getString("level4"));
							submobileVo.setNum_info(items.getString("num_info"));
							submobileVo.setDamage(items.getString("damage"));
							submobileVo.setBin(items.getString("bin"));
							submobileVo.setKang(items.getString("kang"));
							submobileVo.setManage(items.getString("manage"));
							mobileVo.setGubun("0");
							premobileVo.setSubList(submobileVo);
						}
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileMetaVO> metaSList( final Context context, String code )  {
		ArrayList<MobileMetaVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SECOND_META_LIST) + "?code=" + code);
		Log.i("metaSList", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileMetaVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileMetaVO mobileVo = new MobileMetaVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setClass_code(items.getString("class_code"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setCodename(items.getString("codename"));
							mobileVo.setCode_level(items.getString("code_level"));
							mobileVo.setLevel1(items.getString("level1"));
							mobileVo.setLevel2(items.getString("level2"));
							mobileVo.setLevel3(items.getString("level3"));
							mobileVo.setLevel4(items.getString("level4"));
							mobileVo.setNum_info(items.getString("num_info"));
							mobileVo.setDamage(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setGubun("0");
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileMetaVO> getUpdateMetaList( final Context context, String r_id )  {
		ArrayList<MobileMetaVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_META_LIST) + "?r_id=" + r_id);
		Log.i("CONSMAN_UPDATE_META_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileMetaVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileMetaVO mobileVo = new MobileMetaVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setClass_code(items.getString("class_code"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setCodename(items.getString("codename"));
							mobileVo.setCode_level(items.getString("code_level"));
							mobileVo.setLevel1(items.getString("level1"));
							mobileVo.setLevel2(items.getString("level2"));
							mobileVo.setLevel3(items.getString("level3"));
							mobileVo.setLevel4(items.getString("level4"));
							mobileVo.setNum_info(items.getString("num_info"));
							mobileVo.setDamage(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setManager(items.getString("manager"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							mobileVo.setWork_place(items.getString("work_place"));
							mobileVo.setWorker(items.getString("worker"));
							mobileVo.setGubun(items.getString("gubun"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public String insertRiskBaic( final Context context, 
			String site_id, String cont_id, String year, String month, String chasu, String spot_id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_RISK_BASIC) + "?site_id=" + site_id + "&cont_id=" + cont_id+ "&year=" + year+ "&month=" + month+ "&chasu=" + chasu+ "&spot_id=" + spot_id);
		Log.i("RISK_BASIC_CODE_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = item.getString("r_id");
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateRiskBaic( final Context context, 
			String site_id, String cont_id, String id, String status, String reason, String startdate, String enddate)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("reason", reason));
		params.add(new BasicNameValuePair("startdate", startdate ));
		params.add(new BasicNameValuePair("enddate", enddate ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_RISK_BASIC), params );
		Log.i("CONSMAN_UPDATE_RISK_BASIC", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = item.getString("r_id");
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String insertRiskCheckCode( final Context context, 
			String r_id, String code, String work_detail, String work_detail_code, String work_level, String work_level_code, String risk_content, String d_kind, String bin, String kang, String manage, String gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("r_id", r_id));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("work_detail", work_detail ));
		params.add(new BasicNameValuePair("work_detail_code", work_detail_code ));
		params.add(new BasicNameValuePair("work_level", work_level ));
		params.add(new BasicNameValuePair("work_level_code", work_level_code ));
		params.add(new BasicNameValuePair("risk_content", risk_content ));
		params.add(new BasicNameValuePair("damage", d_kind ));
		params.add(new BasicNameValuePair("bin", bin ));
		params.add(new BasicNameValuePair("kang", kang ));
		params.add(new BasicNameValuePair("manage", manage ));
		params.add(new BasicNameValuePair("gubun", gubun ));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_RISK_CHECK_CODE), params );
		Log.i("RISK_CHECK_CODE_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String insertRiskDetail( final Context context, 
			String r_id, String code, String work_detail, String work_detail_code, String work_level, String work_level_code, 
			String device, String startdate, String enddate, String work_place, String worker,
			String risk_content, String d_kind, String bin, String kang, String manage, String manager, String gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("r_id", r_id));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("work_detail", work_detail ));
		params.add(new BasicNameValuePair("work_detail_code", work_detail_code ));
		params.add(new BasicNameValuePair("work_level", work_level ));
		params.add(new BasicNameValuePair("work_level_code", work_level_code ));
		params.add(new BasicNameValuePair("device", device ));
		params.add(new BasicNameValuePair("startdate", startdate ));
		params.add(new BasicNameValuePair("enddate", enddate ));
		params.add(new BasicNameValuePair("work_place", work_place ));
		params.add(new BasicNameValuePair("worker", worker ));
		
		params.add(new BasicNameValuePair("risk_content", risk_content ));
		params.add(new BasicNameValuePair("damage", d_kind ));
		params.add(new BasicNameValuePair("bin", bin ));
		params.add(new BasicNameValuePair("kang", kang ));
		params.add(new BasicNameValuePair("manage", manage ));
		params.add(new BasicNameValuePair("manager", manager ));
		params.add(new BasicNameValuePair("gubun", gubun ));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_RISK_DETAIL), params );
		Log.i("RISK_CHECK_CODE_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String deleteSuzoData( final Context context, 
			String suzoNum)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_SS_LOCATION_DATA) + "?section=" + suzoNum);
		Log.i("CONSMAN_DELETE_SS_LOCATION_DATA", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String deleteRiskCheckCode( final Context context, 
			String r_id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_RISK_CHECK_CODE) + "?r_id=" + r_id);
		Log.i("RISK_BASIC_CODE_DELETE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public ArrayList<MobileRiskVO> getRiskBasicList( final Context context, String site_id, String cont_id, String type, String year, String month, String chasu )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_BASIC_LIST) + "?site_id=" + site_id +  "&cont_id=" + cont_id+  "&type=" + type+  "&year=" + year+  "&month=" + month+  "&chasu=" + chasu);
		Log.i("metaSList", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSite_id(items.getString("site_id"));
							mobileVo.setCont_id(items.getString("cont_id"));
							mobileVo.setYear(items.getString("year"));
							mobileVo.setMonth(items.getString("month"));
							mobileVo.setChasu(items.getString("chasu"));
							mobileVo.setStatus(items.getString("status"));
							mobileVo.setReason(items.getString("reason"));
							mobileVo.setU_id(items.getString("u_id"));
							if( items.getString("spot_id").equals("") ) {
								mobileVo.setSpot_id("");
							} else {
								mobileVo.setSpot_id(items.getString("spot_id"));
							}
							mobileVo.setCname(items.getString("cname"));
							mobileVo.setSname(items.getString("sname"));
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							mobileVo.setStatusname(items.getString("statusname"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileRiskVO> getRiskCheckCodeList( final Context context, String id )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_CHECK_CODE_LIST) + "?id=" + id);
		Log.i("metaSList", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							
							mobileVo.setWorker(items.getString("worker"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getRiskDetailList( final Context context, String id )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_DETAIL_LIST) + "?id=" + id);
		Log.i("metaSList", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							
							mobileVo.setWorker(items.getString("worker"));
							mobileVo.setRisk_content(items.getString("risk_content"));
							mobileVo.setD_worker(items.getString("d_worker"));
							mobileVo.setD_kind(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setManager(items.getString("manager"));
							mobileVo.setGubun(items.getString("gubun"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileSpotVO> getSpotList( final Context context, String site_id, String cont_id )  {
		ArrayList<MobileSpotVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SPOT_LIST) + "?site_id=" + site_id+ "&cont_id=" + cont_id);
		Log.i("CONSMAN_SPOT_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileSpotVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileSpotVO mobileVo = new MobileSpotVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSite_id(items.getString("site_id"));
							mobileVo.setTagid(items.getString("tagid"));
							mobileVo.setName(items.getString("name"));
							mobileVo.setRisk_level(items.getString("risk_level"));
							mobileVo.setVu_level(items.getString("vu_level"));
							mobileVo.setTotal_level(items.getString("total_level"));
							mobileVo.setTar0(items.getString("tar0"));
							mobileVo.setTar1(items.getString("tar1"));
							mobileVo.setTar2(items.getString("tar2"));
							mobileVo.setTar3(items.getString("tar3"));
							mobileVo.setTar_cont_id(items.getString("tar_cont_id"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileSpotVO> getSpotConfirmList( final Context context, String site_id, String user_id, String cont_id )  {
		ArrayList<MobileSpotVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SPOT_CONFIRM_LIST) + "?site_id=" + site_id + "&user_id=" + user_id+ "&cont_id=" + cont_id);
		Log.i("CONSMAN_SPOT_CONFIRM_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileSpotVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileSpotVO mobileVo = new MobileSpotVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSite_id(items.getString("site_id"));
							mobileVo.setTagid(items.getString("tagid"));
							mobileVo.setName(items.getString("name"));
							mobileVo.setRisk_level(items.getString("risk_level"));
							mobileVo.setVu_level(items.getString("vu_level"));
							mobileVo.setTotal_level(items.getString("total_level"));
							mobileVo.setTar0(items.getString("tar0"));
							mobileVo.setTar1(items.getString("tar1"));
							mobileVo.setTar2(items.getString("tar2"));
							mobileVo.setTar3(items.getString("tar3"));
							mobileVo.setTar_cont_id(items.getString("tar_cont_id"));
							mobileVo.setCountTar(items.getString("countTar"));
							
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileSpotVO> getSpotConfirmDetailList( final Context context, String spot_id, String searchdate, String check_user_id , String gubun)  {
		ArrayList<MobileSpotVO> wlist = null;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("spot_id", spot_id ));
		params.add(new BasicNameValuePair("searchdate", searchdate ));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		params.add(new BasicNameValuePair("gubun", gubun ));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SPOT_CONFIRM_DETAIL_LIST), params );
		
		Log.i("CONSMAN_SPOT_CONFIRM_DETAIL_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileSpotVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileSpotVO mobileVo = new MobileSpotVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setName(items.getString("name"));
							mobileVo.setStartend(items.getString("startend"));
							mobileVo.setDate(items.getString("date"));
							mobileVo.setConfirm_check(items.getString("comfirm_check"));
							mobileVo.setTar_gubun(items.getString("tar_gubun"));
							mobileVo.setUname(items.getString("uname"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getTodayInspectList( final Context context, String site_id, String cont_id, String type, String searchdate, String check_user_id )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_TODAY_LIST) + "?site_id=" + site_id +  "&cont_id=" + cont_id+  "&type=" + type+ "&searchdate=" + searchdate+ "&check_user_id=" + check_user_id);
		Log.i("CONSMAN_INSPECT_TODAY_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSite_id(items.getString("site_id"));
							mobileVo.setCont_id(items.getString("cont_id"));
							mobileVo.setYear(items.getString("year"));
							mobileVo.setMonth(items.getString("month"));
							mobileVo.setChasu(items.getString("chasu"));
							mobileVo.setCname(items.getString("cname"));
							mobileVo.setSname(items.getString("sname"));
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							mobileVo.setStatus(items.getString("status"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getRiskInspectCodeList( final Context context, String id )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_CODE_LIST) + "?id=" + id);
		Log.i("CONSMAN_INSPECT_CODE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							mobileVo.setWorker(items.getString("worker"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileRiskVO> getRiskInspectViewList( final Context context, String id )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_VIEW_LIST) + "?id=" + id);
		Log.i("CONSMAN_INSPECT_VIEW_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							mobileVo.setWorker(items.getString("worker"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getInspectDetailViewList( final Context context, String id , String searchdate, String check_user_id)  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_VIEW_DETAIL_LIST) + "?id=" + id+ "&searchdate=" + searchdate+ "&check_user_id=" + check_user_id);
		Log.i("CONSMAN_INSPECT_VIEW_DETAIL_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							
							mobileVo.setWorker(items.getString("worker"));
							mobileVo.setRisk_content(items.getString("risk_content"));
							mobileVo.setD_worker(items.getString("d_worker"));
							mobileVo.setD_kind(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setManager(items.getString("manager"));
							mobileVo.setGubun(items.getString("gubun"));
							
							mobileVo.setSub_yesno(items.getString("sub_yesno"));
							
							mobileVo.setStatus(items.getString("status"));
							mobileVo.setCheck_id(items.getString("check_id"));
							mobileVo.setInspect_id(items.getString("inspect_id"));
							mobileVo.setMea_id(items.getString("mea_id"));
							mobileVo.setCheckdate(items.getString("checkdate"));
							
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getInspectDetailList( final Context context, String id , String searchdate)  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_DETAIL_LIST) + "?id=" + id+ "&searchdate=" + searchdate);
		Log.i("CONSMAN_INSPECT_DETAIL_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							
							mobileVo.setWorker(items.getString("worker"));
							mobileVo.setRisk_content(items.getString("risk_content"));
							mobileVo.setD_worker(items.getString("d_worker"));
							mobileVo.setD_kind(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setManager(items.getString("manager"));
							mobileVo.setGubun(items.getString("gubun"));
							
							mobileVo.setSub_yesno(items.getString("sub_yesno"));
							mobileVo.setSub_reason(items.getString("sub_reason"));
							mobileVo.setSub_image(items.getString("sub_image"));
							mobileVo.setSub_user(items.getString("sub_user"));
							
							mobileVo.setMain_yesno(items.getString("main_yesno"));
							mobileVo.setMain_reason(items.getString("main_reason"));
							mobileVo.setMain_image(items.getString("main_image"));
							mobileVo.setMain_user(items.getString("main_user"));
							mobileVo.setCommand(items.getString("command"));
							mobileVo.setStatus(items.getString("status"));
							mobileVo.setCheck_id(items.getString("check_id"));
							
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getInspectDetailNewList( final Context context, String id , String searchdate)  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSPECT_DETAIL_NEW_LIST) + "?id=" + id+ "&searchdate=" + searchdate);
		Log.i("CONSMAN_INSPECT_DETAIL_NEW_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setR_id(items.getString("r_id"));
							mobileVo.setCode(items.getString("code"));
							mobileVo.setWork_detail(items.getString("work_detail"));
							mobileVo.setWork_detail_code(items.getString("work_detail_code"));
							mobileVo.setWork_level(items.getString("work_level"));
							mobileVo.setWork_level_code(items.getString("work_level_code"));
							mobileVo.setDevice(items.getString("device"));
							mobileVo.setWork_place(items.getString("work_place"));
							
							mobileVo.setStartdate(items.getString("startdate"));
							mobileVo.setEnddate(items.getString("enddate"));
							
							mobileVo.setWorker(items.getString("worker"));
							mobileVo.setRisk_content(items.getString("risk_content"));
							mobileVo.setD_worker(items.getString("d_worker"));
							mobileVo.setD_kind(items.getString("damage"));
							mobileVo.setBin(items.getString("bin"));
							mobileVo.setKang(items.getString("kang"));
							mobileVo.setManage(items.getString("manage"));
							mobileVo.setManager(items.getString("manager"));
							mobileVo.setGubun(items.getString("gubun"));
							
							mobileVo.setSub_yesno(items.getString("sub_yesno"));
							
							mobileVo.setStatus(items.getString("status"));
							mobileVo.setCheck_id(items.getString("check_id"));
							mobileVo.setInspect_id(items.getString("inspect_id"));
							mobileVo.setMea_id(items.getString("mea_id"));
							mobileVo.setCheckdate(items.getString("checkdate"));
							
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	//점검결과 업데이트
	public String updateRiskInspect( final Context context, 
			String id,
			String sub_yesno,
			String sub_reason,
			String sub_image,
			String sub_user, 
			String location,
			String cont_id,
			String site_id,
			String bin,
			String kang,
			String mea_id,
			String status,
			String inspect_chasu,
			String duedate, 
			String type)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("sub_yesno", sub_yesno));
		params.add(new BasicNameValuePair("sub_reason", sub_reason ));
		params.add(new BasicNameValuePair("sub_image", sub_image ));
		params.add(new BasicNameValuePair("sub_user", sub_user ));
		params.add(new BasicNameValuePair("mstatus", status ));
		params.add(new BasicNameValuePair("location", location ));
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("bin", bin ));
		params.add(new BasicNameValuePair("kang", kang ));
		params.add(new BasicNameValuePair("mea_id", mea_id ));
		params.add(new BasicNameValuePair("inspect_chasu", inspect_chasu ));
		params.add(new BasicNameValuePair("duedate", duedate ));
		params.add(new BasicNameValuePair("type", type ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_RISK_INSPECT), params );
		Log.i("CONSMAN_UPDATE_RISK_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateRiskNewInspect( final Context context, 
			String id,
			String sub_yesno,
			String sub_reason,
			String sub_image,
			String sub_user,
			String command, 
			String main_yesno,
			String main_reason,
			String main_image,
			String main_user,
			String status,
			String location,
			String cont_id,
			String site_id,
			String bin,
			String kang,
			String mea_id,
			String mstatus,
			String inspect_chasu)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("sub_yesno", sub_yesno));
		params.add(new BasicNameValuePair("sub_reason", sub_reason ));
		params.add(new BasicNameValuePair("sub_image", sub_image ));
		params.add(new BasicNameValuePair("sub_user", sub_user ));
		params.add(new BasicNameValuePair("command", command ));
		params.add(new BasicNameValuePair("main_yesno", main_yesno ));
		params.add(new BasicNameValuePair("main_reason", main_reason ));
		params.add(new BasicNameValuePair("main_image", main_image ));
		params.add(new BasicNameValuePair("main_user", main_user ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("location", location ));
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("bin", bin ));
		params.add(new BasicNameValuePair("kang", kang ));
		params.add(new BasicNameValuePair("mea_id", mea_id ));
		params.add(new BasicNameValuePair("mstatus", mstatus ));
		params.add(new BasicNameValuePair("inspect_chasu", inspect_chasu ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_RISK_NEW_INSPECT), params );
		Log.i("CONSMAN_UPDATE_RISK_NEW_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String insertRiskInspect( final Context context, 
			String r_id,
			String check_id,
			String checkdate,
			String bin,
			String kang,
			String yesno, 
			String check_user_id,
			String sub_image,
			String sub_reason,
			String location,
			String cont_id,
			String site_id,
			String status,
			String inspect_chasu,
			String duedate,
			String type)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("r_id", r_id));
		params.add(new BasicNameValuePair("yesno", yesno));
		params.add(new BasicNameValuePair("sub_reason", sub_reason ));
		params.add(new BasicNameValuePair("sub_image", sub_image ));
		params.add(new BasicNameValuePair("check_id", check_id ));
		params.add(new BasicNameValuePair("checkdate", checkdate ));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("location", location ));
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("bin", bin ));
		params.add(new BasicNameValuePair("kang", kang ));
		params.add(new BasicNameValuePair("inspect_chasu", inspect_chasu ));
		params.add(new BasicNameValuePair("duedate", duedate ));
		params.add(new BasicNameValuePair("type", type ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_RISK_INSPECT), params );
		Log.i("CONSMAN_INSERT_RISK_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public ArrayList<MobileRiskVO> getRiskAdminConfirmList( final Context context, String checkdate, String site_id, String r_id)  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_ADMIN_CONFIRM_LIST)  + "?checkdate=" +checkdate + "&site_id=" +site_id+ "&r_id=" +r_id);
		Log.i("CONSMAN_RISK_ADMIN_CONFIRM_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setCode(items.getString("code"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setCheck_user_id(items.getString("check_user_id"));

						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileSpotVO> getTodaySpotList( final Context context, String check_user_id, String gubun, String searchdate, String startend, String tar_gubun )  {
		ArrayList<MobileSpotVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_TODAY_SPOT_LIST) + "?check_user_id=" + check_user_id+ "&gubun=" + gubun+ "&searchdate=" + searchdate +"&startend=" + startend +"&tar_gubun=" + tar_gubun);
		Log.i("CONSMAN_TODAY_SPOT_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileSpotVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileSpotVO mobileVo = new MobileSpotVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSpot_id(items.getString("spot_id"));
							mobileVo.setStartend(items.getString("startend"));
							mobileVo.setDate(items.getString("date"));
							mobileVo.setCheck_time(items.getString("check_time"));
							mobileVo.setGubun(items.getString("gubun"));
							mobileVo.setWorklevel(items.getString("worklevel"));
							mobileVo.setTitle_no(items.getString("title_no"));
							mobileVo.setContent_no(items.getString("content_no"));
							mobileVo.setTitle_text(items.getString("title_text"));
							mobileVo.setContent_text(items.getString("content_text"));
							mobileVo.setCheck_result(items.getString("check_result"));
							mobileVo.setCheck_user_id(items.getString("check_user_id"));
							mobileVo.setMea_id(items.getString("mea_id"));
							mobileVo.setMea_spot_id(items.getString("mea_spot_id"));
							mobileVo.setDuedate(items.getString("duedate"));
							mobileVo.setBef_image(items.getString("bef_image"));
							
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileSpotVO> getSpotCheckList( final Context context, String id, String gubun, String searchdate )  {
		ArrayList<MobileSpotVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SPOT_CHECK_LIST) + "?id=" + id+ "&gubun=" + gubun+ "&searchdate=" + searchdate);
		Log.i("CONSMAN_SPOT_CHECK_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileSpotVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileSpotVO mobileVo = new MobileSpotVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setSite_id(items.getString("site_id"));
							mobileVo.setName(items.getString("name"));
							mobileVo.setWorklevel(items.getString("worklevel"));
							mobileVo.setTitle_no(items.getString("title_no"));
							mobileVo.setContent_no(items.getString("content_no"));
							mobileVo.setTitle_text(items.getString("title_text"));
							mobileVo.setContent_text(items.getString("content_text"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String deleteSpotInspect( final Context context, 
			String spot_id,
			String startend,
			String date,
			String check_user_id, String tar_gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("spot_id",spot_id));
		params.add(new BasicNameValuePair("startend",startend));
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		params.add(new BasicNameValuePair("tar_gubun", tar_gubun ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_SPOT_INSPECT), params );
		Log.i("CONSMAN_DELETE_SPOT_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateSpotInspectCheck( final Context context, 
			String spot_id,
			String startend,
			String date,
			String check_user_id, 
			String tar_gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("spot_id",spot_id));
		params.add(new BasicNameValuePair("startend",startend));
		params.add(new BasicNameValuePair("searchdate", date));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		params.add(new BasicNameValuePair("tar_gubun", tar_gubun ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_SPOT_INSEPECT_CHECK), params );
		Log.i("CONSMAN_UPDATE_SPOT_INSEPECT_CHECK", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public String insertSpotInspectDetail( final Context context, 
			String spot_id,
			String startend,
			String date,
			String check_time,
			String gubun,
			String worklevel,
			String title_no,
			String content_no, 
			String title_text,
			String content_text,
			String check_result,
			String check_comment,
			String check_user_id,
			
			String cont_id,
			String location,
			String sub_image,
			String sub_reason,
			String site_id,
			String status,
			String duedate,
			String tar_gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("spot_id",spot_id));
		params.add(new BasicNameValuePair("startend",startend));
		
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("check_time", check_time));
		
		params.add(new BasicNameValuePair("gubun", gubun ));
		params.add(new BasicNameValuePair("worklevel", worklevel ));
		params.add(new BasicNameValuePair("title_no", title_no ));
		params.add(new BasicNameValuePair("content_no", content_no ));
		params.add(new BasicNameValuePair("title_text", title_text ));
		params.add(new BasicNameValuePair("content_text", content_text ));
		params.add(new BasicNameValuePair("check_result", check_result ));
		params.add(new BasicNameValuePair("check_comment", check_comment ));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("location", location ));
		params.add(new BasicNameValuePair("sub_image", sub_image ));
		params.add(new BasicNameValuePair("sub_reason", sub_reason ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("duedate", duedate ));
		params.add(new BasicNameValuePair("tar_gubun", tar_gubun ));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_SPOT_INSPECT_DETAIL), params );
		Log.i("CONSMAN_INSERT_SPOT_INSPECT_DETAIL", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateSpotInspect( final Context context, 
			String id,
			String spot_id,
			String startend,
			String date,
			String check_time,
			String gubun,
			String worklevel,
			String title_no,
			String content_no, 
			String title_text,
			String content_text,
			String check_result,
			String check_comment,
			String check_user_id,
			
			String cont_id,
			String location,
			String sub_image,
			String sub_reason,
			String site_id,
			String status,
			String duedate, String tar_gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id",id));
		params.add(new BasicNameValuePair("spot_id",spot_id));
		params.add(new BasicNameValuePair("startend",startend));
		
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("check_time", check_time));
		
		params.add(new BasicNameValuePair("gubun", gubun ));
		params.add(new BasicNameValuePair("worklevel", worklevel ));
		params.add(new BasicNameValuePair("title_no", title_no ));
		params.add(new BasicNameValuePair("content_no", content_no ));
		params.add(new BasicNameValuePair("title_text", title_text ));
		params.add(new BasicNameValuePair("content_text", content_text ));
		params.add(new BasicNameValuePair("check_result", check_result ));
		params.add(new BasicNameValuePair("check_comment", check_comment ));
		params.add(new BasicNameValuePair("check_user_id", check_user_id ));
		
		params.add(new BasicNameValuePair("cont_id", cont_id ));
		params.add(new BasicNameValuePair("location", location ));
		params.add(new BasicNameValuePair("sub_image", sub_image ));
		params.add(new BasicNameValuePair("sub_reason", sub_reason ));
		params.add(new BasicNameValuePair("site_id", site_id ));
		params.add(new BasicNameValuePair("status", status ));
		params.add(new BasicNameValuePair("duedate", duedate ));
		params.add(new BasicNameValuePair("tar_gubun", tar_gubun ));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_SPOT_INSPECT), params );
		Log.i("CONSMAN_UPDATE_SPOT_INSPECT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public MobileSpotVO confirmSpotTagid( final Context context, String id, String tagid )  {
		MobileSpotVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CONFIRM_SPOT_TAG_ID) + "?id=" + id + "&tagid=" + tagid);
		Log.i("CONSMAN_CONFIRM_SPOT_TAG_ID", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileSpotVO();
					mobileVo.setId(item.getString("id"));
					mobileVo.setSite_id(item.getString("site_id"));
					mobileVo.setTagid(item.getString("tagid"));
					mobileVo.setName(item.getString("name"));
					mobileVo.setTotal_level(item.getString("total_level"));
					mobileVo.setTar1(item.getString("tar1"));
					mobileVo.setTar2(item.getString("tar2"));
					mobileVo.setTar3(item.getString("tar3"));
				} else {
					mobileVo  = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return mobileVo;
	}
	
	public MobileSpotVO getSpotCheckOneInfo( final Context context, String id, String gubun, String searchdate )  {
		MobileSpotVO mobileVo = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CONFIRM_SPOT_TAG_ID) + "?id=" + id + "&gubun=" + gubun+ "&searchdate=" + searchdate);
		Log.i("CONSMAN_CONFIRM_SPOT_TAG_ID", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					mobileVo = new MobileSpotVO();
					mobileVo.setId(item.getString("id"));
					mobileVo.setSpot_id(item.getString("spot_id"));
					mobileVo.setGubun(item.getString("gubun"));
					mobileVo.setDate(item.getString("date"));
					mobileVo.setCheck1(item.getString("check1"));
					mobileVo.setCheck2(item.getString("check2"));
					mobileVo.setCheck3(item.getString("check3"));
					mobileVo.setTagid(item.getString("tagid"));
					mobileVo.setVu_level(item.getString("vu_level"));
					mobileVo.setRisk_level(item.getString("risk_level"));
					mobileVo.setName(item.getString("name"));
					mobileVo.setTotal_level(item.getString("total_level"));
					mobileVo.setTar1(item.getString("tar1"));
					mobileVo.setTar2(item.getString("tar2"));
					mobileVo.setTar3(item.getString("tar3"));
				} else {
					mobileVo  = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return mobileVo;
	}
	
	public ArrayList<MobileMetaVO> getDamageList( final Context context  )  {
		ArrayList<MobileMetaVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAMAGE_LIST));
		Log.i("CONSMAN_DAMAGE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileMetaVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileMetaVO mobileVo = new MobileMetaVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setDamage(items.getString("damage"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileRiskVO> getChasuList( final Context context  )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CHASU_LIST));
		Log.i("CONSMAN_CHASU_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setChasu(items.getString("chasu"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileRiskVO> getCheckdateList( final Context context, String site_id, String cont_id, String type  )  {
		ArrayList<MobileRiskVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_INSPECT_DAY_LIST)+ "?site_id=" + site_id + "&cont_id=" + cont_id+"&type=" + type);
		Log.i("CONSMAN_DAMAGE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileRiskVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileRiskVO mobileVo = new MobileRiskVO();
							mobileVo.setCheckdate(items.getString("checkdate"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	//NFC PUBLIC
	public String getNfcTagId( final Context context, String site_id, String gubun )  {
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_NFC_TAG) + "?site_id=" + site_id + "&gubun=" + gubun);
		Log.i("CONSMAN_GET_NFC_TAG", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					returnString=item.getString("tagid");
				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return returnString;
	}
	
	public String getCompanyManagerUserId( final Context context, String cont_id)  {
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_COMPANY_MANAGER_USER_ID) + "?cont_id=" + cont_id );
		Log.i("CONSMAN_COMPANY_MANAGER_USER_ID", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					returnString=item.getString("user_id");
				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return returnString;
	}
	
	
	//Insert Person Nfc id
	public String insertPersonNfcId( final Context context, 
			String site_id,
			String tagid )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id",site_id));
		params.add(new BasicNameValuePair("tagid", tagid));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_PERSON_NFC_ID), params );
		Log.i("CONSMAN_INSERT_PERSON_NFC_ID", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public String insertDailyRiskCheck( final Context context, 
			String site_id,
			String cont_id,
			String risk_code,
			String risk_name,
			String work_place,
			String work_summary,
			String workdate,
			String endhour,
			String register_user
			)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id",site_id));
		params.add(new BasicNameValuePair("cont_id",cont_id));
		params.add(new BasicNameValuePair("risk_code", risk_code));
		params.add(new BasicNameValuePair("risk_name", risk_name));
		params.add(new BasicNameValuePair("work_place", work_place));
		params.add(new BasicNameValuePair("work_summary", work_summary));
		params.add(new BasicNameValuePair("workdate", workdate));
		params.add(new BasicNameValuePair("endhour", endhour));
		params.add(new BasicNameValuePair("register_user", register_user));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_RISK_CHECK_INSERT), params );
		Log.i("CONSMAN_DAILY_RISK_CHECK_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public ArrayList<MobileVO> getCodeList( final Context context, String type  )  {
		ArrayList<MobileVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CODE_LIST)+ "?type=" + type);
		Log.i("CONSMAN_CODE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileUserVO> getContList( final Context context, String site_id  )  {
		ArrayList<MobileUserVO> wlist = null;

		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CONT_LIST)+ "?site_id=" + site_id);
		Log.i("CONSMAN_CONT_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setName(items.getString("name"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}

	
	public ArrayList<MobileUserVO> getSiteList( final Context context)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SITE_LIST));
		Log.i("CONSMAN_SITE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setName(items.getString("name"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileUserVO> getDailyContList( final Context context, String site_id, String searchdate  )  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_CONT_LIST)+ "?site_id=" + site_id+ "&searchdate=" + searchdate);
		Log.i("CONSMAN_DAILY_CONT_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
							mobileVo.setId(items.getString("id"));
							mobileVo.setName(items.getString("name"));
							wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileWorkerVO> getNotSmartPhoneWorkerList( final Context context, String searchname, String site_id, String cont_id, String type )  {
		ArrayList<MobileWorkerVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_NOT_SMARTPHONE_LIST)  + "?searchname=" + searchname+ "&site_id=" + site_id+ "&cont_id=" + cont_id+ "&type=" + type);
				
		Log.i("CONSMAN_NOT_SMARTPHONE_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileWorkerVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileWorkerVO mobileVo = new MobileWorkerVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setJumin(items.getString("jumin"));
						mobileVo.setCountry(items.getString("country"));
						mobileVo.setPassno(items.getString("passno"));
						mobileVo.setFirst(items.getString("first"));
						mobileVo.setT_id(items.getString("t_id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setSingoyn(items.getString("singoyn"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setTagid(items.getString("tagid"));
						mobileVo.setMw_id(items.getString("mw_id"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertNotWorking( final Context context, 
			String nfc,String id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_NOT_INSERT_WORKERING) + "?id=" + id + "&nfc="+ nfc);
		Log.i("CONSMAN_NOT_INSERT_WORKERING", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateMWorkerstatus( final Context context, 
			String idlist, String id, String site_id, String cont_id, String location, String content, String after, String mon, String option)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("idlist", idlist));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("after", after));
		params.add(new BasicNameValuePair("mon", mon));
		params.add(new BasicNameValuePair("option", option));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_MW_STATUS), params );
		
		Log.i("CONSMAN_UPDATE_MW_STATUS", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String todayWorkerInsertCompletePush( final Context context, 
			String searchdate, String site_id, String cont_id)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("searchdate",searchdate));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_TODAY_WORKER_INSERT_COMPLETE_PUSH), params );
		
		Log.i("CONSMAN_TODAY_WORKER_INSERT_COMPLETE_PUSH", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public ArrayList<MobileVO> getDailyRiskCheckList( final Context context, String site_id, String cont_id, String writedate)  {
		ArrayList<MobileVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_RISK_CHECK_LIST)  + "?workdate=" + writedate + "&site_id=" +site_id + "&cont_id=" +cont_id);
		Log.i("CONSMAN_DAILY_RISK_CHECK_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setRisk_code(items.getString("risk_code"));
						mobileVo.setRisk_name(items.getString("risk_name"));
						mobileVo.setWork_place(items.getString("work_place"));
						mobileVo.setWork_summary(items.getString("work_summary"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setEndhour(items.getString("endhour"));
						mobileVo.setRegister_user(items.getString("register_user"));
						mobileVo.setConfirm_user(items.getString("confirm_user"));
						mobileVo.setApprove_user(items.getString("approve_user"));
						mobileVo.setEnt_user(items.getString("ent_user"));
						mobileVo.setCheck_user(items.getString("check_user"));
						mobileVo.setComment(items.getString("comment"));
						mobileVo.setCont_name(items.getString("cont_name"));
						mobileVo.setCont_name_1(items.getString("cont_name_1"));
						mobileVo.setCont_name_2(items.getString("cont_name_2"));
						mobileVo.setCont_name_3(items.getString("cont_name_3"));
						mobileVo.setCont_name_4(items.getString("cont_name_4"));
						mobileVo.setCont_name_5(items.getString("cont_name_5"));
						
						
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileMeasureVO> getMeasureList( final Context context, String site_id, String cont_id, String writedate , String type, String gubun)  {
		ArrayList<MobileMeasureVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MEASURE_LIST)  + "?writedate=" + writedate + "&site_id=" +site_id + "&cont_id=" +cont_id+ "&type=" +type+ "&gubun=" +gubun);
		Log.i("CONSMAN_MEASURE_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileMeasureVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileMeasureVO mobileVo = new MobileMeasureVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setWritedate( items.getString("writedate"));
						mobileVo.setCont_id( items.getString("cont_id"));
						mobileVo.setUname( items.getString("uname"));
						mobileVo.setLocation( items.getString("location"));
						mobileVo.setBe_image( items.getString("be_image"));
						mobileVo.setBe_content( items.getString("be_content"));
						mobileVo.setMea_image( items.getString("mea_image"));
						mobileVo.setMea_content( items.getString("mea_content"));
						mobileVo.setMea_user_id( items.getString("mea_user_id"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setCname( items.getString("cname"));
						mobileVo.setBin( items.getString("bin"));
						mobileVo.setKang( items.getString("kang"));
						mobileVo.setInspect_id( items.getString("inspect_id"));
						mobileVo.setInspect_chasu( items.getString("inspect_chasu"));
						mobileVo.setDuedate(items.getString("duedate"));
						mobileVo.setSpot_id(items.getString("spot_id"));
						mobileVo.setBe_gubun(items.getString("be_gubun"));
						
						mobileVo.setEquip_id(items.getString("equip_id"));
						mobileVo.setEquip_check_id(items.getString("equip_check_id"));
						
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public MobileMeasureVO getMeasureInfo( final Context context, String site_id, String cont_id, String id)  {
		MobileMeasureVO wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_MEASURE_INFO)  + "?id=" + id + "&site_id=" +site_id + "&cont_id=" +cont_id);
		Log.i("CONSMAN_RISK_MEASURE_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					wlist = new MobileMeasureVO();
					wlist.setId(item.getString("id"));
					wlist.setWritedate( item.getString("writedate"));
					wlist.setCont_id( item.getString("cont_id"));
					wlist.setUname( item.getString("uname"));
					wlist.setLocation( item.getString("location"));
					wlist.setBe_image( item.getString("be_image"));
					wlist.setBe_content( item.getString("be_content"));
					wlist.setMea_image( item.getString("mea_image"));
					wlist.setMea_content( item.getString("mea_content"));
					wlist.setMea_user_id( item.getString("mea_user_id"));
					wlist.setStatus(item.getString("status"));
					wlist.setCname( item.getString("cname"));
					wlist.setBin( item.getString("bin"));
					wlist.setKang( item.getString("kang"));
					wlist.setInspect_id( item.getString("inspect_id"));
					wlist.setInspect_chasu( item.getString("inspect_chasu"));
					wlist.setDuedate(item.getString("duedate"));
					wlist.setRisk_content(item.getString("risk_content"));
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertRiskMeasure( final Context context, 
			String writedate,
			String cont_id,
			String user_id,
			String location,
			String be_image,
			String be_content,
			String bin,
			String kang,
			String site_id,
			String duedate,
			String be_gubun,
			String spot,
			String spotLoction)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("writedate",writedate));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("user_id", user_id));
		if( spot.equals("") ) {
			params.add(new BasicNameValuePair("location", location));
		} else {
			params.add(new BasicNameValuePair("location", spotLoction));
		}
		params.add(new BasicNameValuePair("be_image", be_image));
		params.add(new BasicNameValuePair("be_content", be_content));
		params.add(new BasicNameValuePair("bin", bin));
		params.add(new BasicNameValuePair("kang", kang));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("duedate", duedate));
		params.add(new BasicNameValuePair("be_gubun", be_gubun));
		params.add(new BasicNameValuePair("spot", spot));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MEASURE_INSERT), params );
		Log.i("CONSMAN_MEASURE_INSERT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateRiskMeasureResult( final Context context, 
			String id,
			String mea_image,
			String mea_content,
			String mea_user_id,
			String site_id)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id",id));
		params.add(new BasicNameValuePair("mea_image",mea_image));
		params.add(new BasicNameValuePair("mea_content", mea_content));
		params.add(new BasicNameValuePair("mea_user_id", mea_user_id));
		params.add(new BasicNameValuePair("site_id", site_id));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MEASURE_UPDATE_RESULT), params );
		Log.i("CONSMAN_MEASURE_UPDATE_RESULT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String updateRiskMeasureStatus( final Context context, 
			String id,
			String status,
			String cont_id,
			String site_id)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id",id));
		params.add(new BasicNameValuePair("status",status));
		params.add(new BasicNameValuePair("site_id",site_id));
		params.add(new BasicNameValuePair("cont_id",cont_id));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MEASURE_UPDATE_STATUS), params );
		Log.i("CONSMAN_MEASURE_UPDATE_STATUS", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	public String getMeaUsername( final Context context, String id )  {
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MEASURE_IMAGE_COUNT) + "?id=" + id );
		Log.i("CONSMAN_MEASURE_IMAGE_COUNT", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					returnString=item.getString("count");
				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return returnString;
	}
	
	
	public String insertPersonEtcContent( final Context context, 
			String content, String content_ya , String site_id, String cont_id, String type, String safe_content, String safe_content_ya)  {
		String resultString = "";

		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("content_ya", content_ya));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("safe_content", safe_content));
		params.add(new BasicNameValuePair("safe_content_ya", safe_content_ya));
		

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_PERSON_ETC), params );
		
		Log.i("CONSMAN_INSERT_PERSON_ETC", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String getPersonContent( final Context context, 
			String workdate, String site_id, String cont_id, String type)  {
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_PERSON_ETC) + "?site_id=" + site_id+ "&cont_id=" + cont_id+ "&type=" + type + "&workdate=" + workdate  );
		Log.i("CONSMAN_PERSON_ETC", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					returnString=item.getString("content");
				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return returnString;
	}
	
	public MobileVO getPersonContentYa( final Context context, 
			String workdate, String site_id, String cont_id, String type)  {
		MobileVO returnString = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_PERSON_ETC_YA) + "?site_id=" + site_id+ "&cont_id=" + cont_id+ "&type=" + type + "&workdate=" + workdate  );
		Log.i("CONSMAN_PERSON_ETC_YA", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					returnString = new MobileVO();
					returnString.setReason(item.getString("content"));
					returnString.setReason_ya(item.getString("contentya"));
					returnString.setSafe_content(item.getString("safe_content"));
					returnString.setSafe_content_ya(item.getString("safe_content_ya"));
				} else {
					returnString  = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return returnString;
	}
	
	
	
	public String updateImsi( final Context context, 
			String idlist)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_IMSI) + "?idlist=" + idlist);
		Log.i("FIRST_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteWorker( final Context context, 
			String idlist)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_WORKER) + "?idlist=" + idlist);
		Log.i("CONSMAN_DELETE_WORKER", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String cencelEduPerson( final Context context, 
			String idlist)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_EDU_PERSON_CANCEL) + "?idlist=" + idlist);
		Log.i("FIRST_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public ArrayList<MobileUserVO> getWorkingCompanyList( final Context context, String site_id, String checkdate)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WORKING_COMPANY_LIST)  + "?site_id=" + site_id + "&checkdate=" +checkdate);
		Log.i("CONSMAN_WORKING_COMPANY_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	
	public ArrayList<MobileUserVO> getRiskMeasureCompanyList( final Context context, String site_id, String checkdate)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_RISK_MEASURE_COMPANY_LIST)  + "?site_id=" + site_id + "&checkdate=" +checkdate);
		Log.i("CONSMAN_RISK_MEASURE_COMPANY_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}

	
	
	public ArrayList<MobileUserVO> getUserList( final Context context, String site_id, String cont_id, String name, String searchdate)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_USER_LIST)  + "?site_id=" +site_id + "&cont_id=" +cont_id + "&name=" +name + "&searchdate=" +searchdate);
		Log.i("CONSMAN_USER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setRole_code(items.getString("role_code"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setUserid(items.getString("userid"));
						mobileVo.setPassword(items.getString("password"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setEmail(items.getString("email"));
						mobileVo.setGrade("");
						mobileVo.setUseyn(items.getString("useyn"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setType(items.getString("type"));
						mobileVo.setSname(items.getString("sname"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setDtype(items.getString("dtype"));
						mobileVo.setDcode(items.getString("dcode"));
						mobileVo.setRname(items.getString("rname"));
						mobileVo.setMuid(items.getString("muid"));
						mobileVo.setOption_gubun(items.getString("option"));
						mobileVo.setPid(items.getString("pid"));
						if( items.getString("option").equals("0") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(0);
						} else if( items.getString("option").equals("1") ) {
							mobileVo.setJu(-1);
							mobileVo.setYa(1);
						} else if( items.getString("option").equals("2") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(1);
						}
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}

	
	public ArrayList<MobileUserVO> getUserMultiList( final Context context, String site_id, String idlist, String name)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MULTI_USER_LIST)  + "?site_id=" +site_id + "&idlist=" +idlist + "&name=" +name);
		Log.i("CONSMAN_MULTI_USER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setRole_code(items.getString("role_code"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setUserid(items.getString("userid"));
						mobileVo.setPassword(items.getString("password"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setEmail(items.getString("email"));
						mobileVo.setGrade("");
						mobileVo.setUseyn(items.getString("useyn"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setType(items.getString("type"));
						mobileVo.setSname(items.getString("sname"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setDtype(items.getString("dtype"));
						mobileVo.setDcode(items.getString("dcode"));
						mobileVo.setRname(items.getString("rname"));
						mobileVo.setMuid(items.getString("muid"));
						mobileVo.setOption_gubun(items.getString("option"));
						mobileVo.setPid(items.getString("pid"));
						if( items.getString("option").equals("0") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(0);
						} else if( items.getString("option").equals("1") ) {
							mobileVo.setJu(-1);
							mobileVo.setYa(1);
						} else if( items.getString("option").equals("2") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(1);
						}
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileUserVO> getManageUserList( final Context context, String site_id, String cont_id, String  workdate, String gubun, String type)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MANAGER_USER_LIST)  + "?site_id=" +site_id + "&cont_id=" +cont_id+ "&workdate=" +workdate+ "&gubun=" +gubun+ "&type=" +type);
		Log.i("CONSMAN_MANAGER_USER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setRole_code(items.getString("role_code"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setUserid(items.getString("userid"));
						mobileVo.setPassword(items.getString("password"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setEmail(items.getString("email"));
						mobileVo.setGrade("");
						mobileVo.setUseyn(items.getString("useyn"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setType(items.getString("type"));
						mobileVo.setSname(items.getString("sname"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setDtype(items.getString("dtype"));
						mobileVo.setDcode(items.getString("dcode"));
						mobileVo.setRname(items.getString("rname"));
						mobileVo.setOption_gubun(items.getString("option"));
						if( items.getString("option").equals("0") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(0);
						} else if( items.getString("option").equals("1") ) {
							mobileVo.setJu(-1);
							mobileVo.setYa(1);
						} else if( items.getString("option").equals("2") ) {
							mobileVo.setJu(0);
							mobileVo.setYa(1);
						}
						mobileVo.setU_id(items.getString("u_id"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertMuser( final Context context, 
			String idlist, String cont_id)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_MANAGER_USER) + "?idlist=" + idlist + "&cont_id="+cont_id);
		Log.i("CONSMAN_INSERT_MANAGER_USER", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String insertMOneuser( final Context context, 
			String id, String cont_id, String option, String searchdate)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_MANAGER_ONE_USER) + "?id=" + id + "&cont_id="+cont_id + "&option="+option + "&searchdate="+searchdate);
		Log.i("CONSMAN_INSERT_MANAGER_ONE_USER", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String insertTodayWorkerData( final Context context, 
			String w_id,
			String mon,
			String after,
			String place,
			String content,
			String workdate,
			String status,
			String u_id,
			String option,
			String startYn,
			String m_cont_id,
			String id,
			String gubun)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("w_id",w_id));
		params.add(new BasicNameValuePair("mon", mon));
		params.add(new BasicNameValuePair("after", after));
		params.add(new BasicNameValuePair("place", place));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("workdate", workdate));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("u_id", u_id));
		params.add(new BasicNameValuePair("option", option));
		params.add(new BasicNameValuePair("startYn", startYn));
		params.add(new BasicNameValuePair("m_cont_id", m_cont_id));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("gubun", gubun));

		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_TODAY_WORKER_INSERT_DATA), params );
		Log.i("CONSMAN_TODAY_WORKER_INSERT_DATA", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public String mobilePushEvent( final Context context, 
			String cont_id,
			String site_id,
			String gubun,
			String ment,
			String user_id)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("gubun", gubun));
		params.add(new BasicNameValuePair("ment", ment));
		params.add(new BasicNameValuePair("user_id", user_id));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_MOBILE_PUSH_EVENT), params );
		Log.i("CONSMAN_MOBILE_PUSH_EVENT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public String updateWorkerDel( final Context context, 
			String id,
			String delyn,
			String delContent)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("delyn", delyn));
		params.add(new BasicNameValuePair("delContent", delContent));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_WORKER_DEL), params );
		Log.i("CONSMAN_UPDATE_WORKER_DEL", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	public String updateWorkerCancel( final Context context, 
			String id,
			String delyn,
			String delContent)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("delyn", delyn));
		params.add(new BasicNameValuePair("delContent", delContent));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_WORKER_CANCEL), params );
		Log.i("CONSMAN_UPDATE_WORKER_CANCEL", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		return resultString;
	}
	
	
	
	public String checkBlackPerson( final Context context, String name, String jumin ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CHECK_WORKER_BLACK) + "?name=" + name+ "&jumin=" + jumin  );
		Log.i("CONSMAN_CHECK_WORKER_BLACK", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					if( item.getString("delyn").equals("B") ) {
						return "BAD";
					} else if( item.getString("delyn").equals("N") ) {
						returnString = item.getString("id");
						return returnString;
					}
				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	public String deleteEtc( final Context context, String id ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_ETC) + "?id=" + id  );
		Log.i("CONSMAN_DELETE_ETC", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
						returnString = "true";
						return returnString;

				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	public String deleteDailyRiskCheck( final Context context, String id ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_RISK_CHECK_DELETE) + "?id=" + id  );
		Log.i("CONSMAN_DAILY_RISK_CHECK_DELETE", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
						returnString = "true";
						return returnString;

				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	public String updateDailyRiskCheck( final Context context, String id, String register_user, String confirm_user ) {
		
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("register_user", register_user));
		params.add(new BasicNameValuePair("confirm_user", confirm_user));
		
		
	
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_RISK_CHECK_UPDATE), params );
		
		Log.i("CONSMAN_DAILY_RISK_CHECK_UPDATE", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public String deleteMWorker( final Context context, String w_id, String workdate ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_M_WORKER) + "?w_id=" + w_id+ "&workdate=" + workdate  );
		Log.i("CONSMAN_DELETE_M_WORKER", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
						returnString = "true";
						return returnString;

				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	
	public String deleteMUser( final Context context, String u_id, String workdate ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_M_USER) + "?u_id=" + u_id+ "&workdate=" + workdate  );
		Log.i("CONSMAN_DELETE_M_USER", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
						returnString = "true";
						return returnString;

				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	public String updateTodayWorkerOption( final Context context, String id, String option ) {
		
		String returnString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_TODAY_WORKER_OPTION) + "?id=" + id+ "&option=" + option  );
		Log.i("CONSMAN_UPDATE_TODAY_WORKER_OPTION", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
						returnString = "true";
						return returnString;

				} else {
					returnString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
			
		}
		
		return "";
	}
	
	public String insertworklocation( final Context context, 
			String lng, String lat, String id, String site_id)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lng", lng));
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("gubun", "1"));
		
		
	
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_USER_LOCATION), params );
		
		Log.i("CONSMAN_INSERT_USER_LOCATION", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}

	
	public MobileTunelVO getTunelInfo( final Context context, String writedate, String cont_id, String place_gubun)  {
		MobileTunelVO tunel = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_TUNEL_INFO)  + "?writedate=" + writedate + "&cont_id=" +cont_id + "&place_gubun=" +place_gubun);
		Log.i("CONSMAN_TUNEL_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					tunel = new MobileTunelVO();
					tunel.setId(item.getString("id"));
					tunel.setWritedate( item.getString("writedate"));
					tunel.setCont_id( item.getString("cont_id"));
					tunel.setPlace_gubun( item.getString("place_gubun"));
					tunel.setUpdown( item.getString("updown"));
					tunel.setToday_tunel( item.getString("today_tunel"));
					tunel.setToday_tunel_down( item.getString("today_tunel_down"));
					tunel.setBomb1( item.getString("bomb1"));
					tunel.setBomb2( item.getString("bomb2"));
					tunel.setBomb3( item.getString("bomb3"));
					tunel.setBombd1( item.getString("bombd1"));
					tunel.setBombd2( item.getString("bombd2"));
					tunel.setBombd3( item.getString("bombd3"));
				} else {
					tunel = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return tunel;
	}	
	
	public String getLastYesterdayEndtime( final Context context, String writedate, String cont_id, String place_gubun, String updown)  {
		String data = "";
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_LAST_YESTERDAY_END_TIME)  + "?writedate=" + writedate + "&cont_id=" +cont_id + "&place_gubun=" +place_gubun  + "&updown=" +updown);
		Log.i("CONSMAN_LAST_YESTERDAY_END_TIME", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					data = item.getString("endtime");
				} else {
					return "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return data;
	}	
	
	public MobileTunelVO getLastTunelInfo( final Context context, String cont_id, String place_gubun)  {
		MobileTunelVO tunel = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_LAST_TUNEL_INFO)  + "?&cont_id=" +cont_id + "&place_gubun=" +place_gubun);
		Log.i("CONSMAN_LAST_TUNEL_INFO", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					tunel = new MobileTunelVO();
					tunel.setId(item.getString("id"));
					tunel.setWritedate( item.getString("writedate"));
					tunel.setCont_id( item.getString("cont_id"));
					tunel.setPlace_gubun( item.getString("place_gubun"));
					tunel.setUpdown( item.getString("updown"));
					tunel.setToday_tunel( item.getString("today_tunel"));
					tunel.setToday_tunel_down( item.getString("today_tunel_down"));
					tunel.setBomb1( item.getString("bomb1"));
					tunel.setBomb2( item.getString("bomb2"));
					tunel.setBomb3( item.getString("bomb3"));
					tunel.setBombd1( item.getString("bombd1"));
					tunel.setBombd2( item.getString("bombd2"));
					tunel.setBombd3( item.getString("bombd3"));
				} else {
					tunel = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return tunel;
	}	
	
	public String getTunelBombCount( final Context context, String writedate, String cont_id, String place_gubun, String updown)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_TUNEL_BOMB_COUNT)  + "?writedate=" + writedate + "&cont_id=" +cont_id + "&place_gubun=" +place_gubun+ "&updown=" +updown);
		Log.i("CONSMAN_TUNEL_BOMB_COUNT", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString = item.getString("count"); 
				} else {
					resultString = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return "";
			}
		}
		return resultString;
	}
	
	
	public String insertTunelInfo( final Context context, 
			String writedate, String cont_id, String place_gubun
			, String today_tunel, String today_tunel_down
			, String bomb1, String bomb2, String bomb3
			, String bombd1, String bombd2, String bombd3 , String site_id
			)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("writedate", writedate));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("place_gubun", place_gubun));
		params.add(new BasicNameValuePair("today_tunel", today_tunel));
		params.add(new BasicNameValuePair("today_tunel_down", today_tunel_down));
		params.add(new BasicNameValuePair("bomb1", bomb1));
		params.add(new BasicNameValuePair("bomb2", bomb2));
		params.add(new BasicNameValuePair("bomb3", bomb3));
		params.add(new BasicNameValuePair("bombd1", bombd1));
		params.add(new BasicNameValuePair("bombd2", bombd2));
		params.add(new BasicNameValuePair("bombd3", bombd3));
		params.add(new BasicNameValuePair("site_id", site_id));
	
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_TUNEL_INFO), params );
		Log.i("CONSMAN_INSERT_TUNEL_INFO", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public ArrayList<MobileTunelVO> getCycleList( final Context context, String writedate, String cont_id, String place_gubun, String updown)  {
		ArrayList<MobileTunelVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_CYCLE_LIST)  + "?writedate=" +writedate + "&cont_id=" +cont_id + "&place_gubun=" +place_gubun + "&updown=" +updown);
		Log.i("CONSMAN_CYCLE_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileTunelVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileTunelVO mobileVo = new MobileTunelVO();
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setWritedate(items.getString("writedate"));
						mobileVo.setCno(items.getString("cno"));
						mobileVo.setUpdown(items.getString("updown"));
						mobileVo.setPlace_gubun(items.getString("place_gubun"));
						mobileVo.setPattern(items.getString("pattern"));
						mobileVo.setStarttime(items.getString("starttime"));
						mobileVo.setEndtime(items.getString("endtime"));
						mobileVo.setProcess_content(items.getString("process_content"));
						mobileVo.setEnd_gubun(items.getString("end_gubun"));
						mobileVo.setBigo(items.getString("bigo"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}

	public ArrayList<MobileTunelVO> getDailyCycleList( final Context context, String writedate, String cont_id)  {
		ArrayList<MobileTunelVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DAILY_CYCLE_LIST)  + "?writedate=" +writedate + "&cont_id=" + cont_id);
		Log.i("CONSMAN_DAILY_CYCLE_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileTunelVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileTunelVO mobileVo = new MobileTunelVO();
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setWritedate(items.getString("writedate"));
						mobileVo.setUpdown(items.getString("updown"));
						mobileVo.setPlace_gubun(items.getString("place_gubun"));

						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertCycleTime( final Context context, 
			String writedate, String cont_id, String place_gubun
			, String updown, String pattern
			, String starttime, String process_content, String end_gubun, String endtime, String bigo
			)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("writedate", writedate));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("place_gubun", place_gubun));
		params.add(new BasicNameValuePair("updown", updown));
		params.add(new BasicNameValuePair("pattern", pattern));
		params.add(new BasicNameValuePair("starttime", starttime));
		params.add(new BasicNameValuePair("endtime", endtime));
		params.add(new BasicNameValuePair("process_content", process_content));
		params.add(new BasicNameValuePair("end_gubun", end_gubun));
		params.add(new BasicNameValuePair("bigo", bigo));
	
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_CYCLE_TIME), params );
		Log.i("CONSMAN_INSERT_CYCLE_TIME", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateCycleTime( final Context context, 
			String writedate, String cont_id, String place_gubun
			, String updown, String pattern
			, String starttime, String process_content, String cno, String endtime, String bigo
			)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("writedate", writedate));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("place_gubun", place_gubun));
		params.add(new BasicNameValuePair("updown", updown));
		params.add(new BasicNameValuePair("cno", cno));
		params.add(new BasicNameValuePair("pattern", pattern));
		params.add(new BasicNameValuePair("starttime", starttime));
		params.add(new BasicNameValuePair("endtime", endtime));
		params.add(new BasicNameValuePair("process_content", process_content));
		params.add(new BasicNameValuePair("bigo", bigo));
	
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_CYCLE_TIME), params );
		Log.i("CONSMAN_UPDATE_CYCLE_TIME", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteCycleTime( final Context context, 
			String writedate, String cont_id, String place_gubun, String updown )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("writedate", writedate));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("place_gubun", place_gubun));
		params.add(new BasicNameValuePair("updown", updown));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_CYCLE_TIME), params );
		Log.i("CONSMAN_DELETE_CYCLE_TIME", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ArrayList<MobileUserVO> getBeaconManagerList( final Context context, String name, String site_id, String cont_id)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_BEACON_MANAGER_LIST)  + "?name=" +name + "&site_id=" + site_id+ "&cont_id=" + cont_id);
		Log.i("CONSMAN_BEACON_MANAGER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setId(items.getString("id"));
						mobileVo.setRecoid(items.getString("recoid"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setUseyn(items.getString("useyn"));
						mobileVo.setCountdata(items.getString("countdata"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String updateBeaconManager( final Context context, 
			String id, String uid , String role)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("role", role));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_BEACON_MANAGER_UPDATE), params );
		Log.i("CONSMAN_BEACON_MANAGER_UPDATE", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateUserYn( final Context context, 
			String id, String useYn )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("useyn", useYn));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_USER_YN), params );
		Log.i("CONSMAN_UPDATE_USER_YN", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ArrayList<MobileUserVO> getBeaconHistoryList( final Context context, String macaddress, String tunelnumber)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_BEACON_HISTORY_LIST)  + "?macaddress=" +macaddress  + "&tunelnumber=" +tunelnumber );
		Log.i("CONSMAN_BEACON_HISTORY_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setIsforeign(items.getString("isforeign"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setFirstdate(items.getString("firstdate"));
						mobileVo.setDevice_name(items.getString("device_name"));
						mobileVo.setT_name(items.getString("t_name"));
						mobileVo.setT_gubun(items.getString("t_gubun"));
						mobileVo.setSection(items.getString("section"));
						mobileVo.setChecktime(items.getString("checktime"));
						mobileVo.setBeacon_id(items.getString("beacon_id"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public ArrayList<MobileUserVO> getBeaconSignList( final Context context)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_BEACON_SIGN_LIST) );
		Log.i("CONSMAN_BEACON_SIGN_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("tunel"));
						mobileVo.setChecktime(items.getString("checktime"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String updateUserMission( final Context context, 
			String id , String mission)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("mission", mission));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_USER_MISSION), params );
		Log.i("CONSMAN_UPDATE_USER_MISSION", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public MobileUserVO getUserMission( final Context context, String id)  {
		MobileUserVO user = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_USER_MISSION)  + "?id=" +id);
		Log.i("CONSMAN_USER_MISSION", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					user = new MobileUserVO();
					user.setMission(item.getString("mission"));
				} else {
					user = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return user;
	}
	
	public String insertDisasterMeasure( final Context context, 
			String org, String phone, String rank, String user_id, String name, String r_gubun, String gubun )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org", org));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("rank", rank));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("r_gubun", r_gubun));
		params.add(new BasicNameValuePair("gubun", gubun));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DISASTER_INSERT), params );
		Log.i("CONSMAN_DISASTER_INSERT", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteDisasterMeasure( final Context context, 
			String org )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org", org));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DISASTER_DELETE), params );
		Log.i("CONSMAN_DISASTER_DELETE", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public ArrayList<MobileVO> getDisasterMeasureList( final Context context, String org, String user_id)  {
		ArrayList<MobileVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DISASTER_LIST)  + "?org=" +org+ "&user_id=" +user_id );
		Log.i("CONSMAN_DISASTER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setOrg(items.getString("org"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setRank(items.getString("rank"));
						mobileVo.setUser_id(items.getString("user_id"));
						mobileVo.setR_gubun(items.getString("r_gubun"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setCont_id(items.getString("cont_id"));
						mobileVo.setCname(items.getString("cname"));
						mobileVo.setGrade(items.getString("grade"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public ArrayList<MobileVO> getReportUserList( final Context context, String site_id)  {
		ArrayList<MobileVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_REPORT_USER_LIST)  + "?site_id=" +site_id );
		Log.i("CONSMAN_REPORT_USER_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setPhone(items.getString("phone"));
						mobileVo.setCategory(items.getString("category"));
						mobileVo.setNo(items.getString("no"));
						mobileVo.setOrg("4");
						mobileVo.setR_gubun("");
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertDisasterOrg( final Context context, 
			String name, String idx, String gubun, String tel, String delay_time, String distance, String bigo )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("idx", idx));
		params.add(new BasicNameValuePair("gubun", gubun));
		params.add(new BasicNameValuePair("tel", tel));
		params.add(new BasicNameValuePair("delay_time", delay_time));
		params.add(new BasicNameValuePair("distance", distance));
		params.add(new BasicNameValuePair("bigo", bigo));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_DISASTER_ORG), params );
		Log.i("CONSMAN_INSERT_DISASTER_ORG", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String deleteDisasterOrg( final Context context, 
			String site_id )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_DISASTER_ORG), params );
		Log.i("CONSMAN_DELETE_DISASTER_ORG", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ArrayList<MobileVO> getDisasterOrgList( final Context context, String site_id)  {
		ArrayList<MobileVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DISASTER_ORG_LIST)  + "?site_id=" +site_id );
		Log.i("CONSMAN_DISASTER_ORG_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setTel(items.getString("tel"));
						mobileVo.setIdx(items.getString("idx"));
						mobileVo.setGubun(items.getString("gubun"));
						mobileVo.setDelay_time(items.getString("delay_time"));
						mobileVo.setDistance(items.getString("distance"));
						mobileVo.setBigo(items.getString("bigo"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String waringPush( final Context context, String site_id, String cont_id, String name, String phone, String user_id, String gubun ) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("cont_id", cont_id));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("gubun", gubun));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_WARNING_PUSH), params );
		Log.i("CONSMAN_WARNING_PUSH", result);
		return "";
	}
	
	
	public String allCheckDevice( final Context context )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_ALL_CHECK_DEVICE), params );
		Log.i("CONSMAN_ALL_CHECK_DEVICE", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ScheVO getScheDuleData( final Context context, String site_id ) {
		ScheVO s = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SCHEDULE_DATA) + "?site_id=" + site_id );
		Log.i("CONSMAN_SCHEDULE_DATA", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					s = new ScheVO();
					s.setScheName(item.getString("scheName"));
					s.setScheData(item.getString("scheData"));
					s.setSpotName(item.getString("spotName"));
					s.setSpotData(item.getString("spotData"));
					s.setRiskName(item.getString("riskName"));
					s.setRiskData(item.getString("riskData"));
					s.setEduCount(item.getString("eduCount"));
					s.setEquipCount(item.getString("equipCount"));
					s.setEquipData(item.getString("equipData"));
					s.setEtcName(item.getString("etcName"));
					s.setEtcData(item.getString("etcData"));
					
				} else {
					s = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return s;
	}
	
	
	public ArrayList<MobileUserVO> getScheContList( final Context context, String site_id)  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_SCHEDULE_CONT_DATA)  + "?site_id=" +site_id );
		Log.i("CONSMAN_SCHEDULE_CONT_DATA", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						if( items.getString("checkyn").equals("Y") ) {
							mobileVo.setCheck(1);
						} else if( items.getString("checkyn").equals("N") ) {
							mobileVo.setCheck(0);
						}
						mobileVo.setCheckyn(items.getString("checkyn"));
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	
	}
	
	
	public String insertScheContList( final Context context, 
			String idlist)  {
		String resultString = "";
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_SCHEDULE_CONT_LIST) + "?idlist=" + idlist);
		Log.i("CONSMAN_INSERT_SCHEDULE_CONT_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	public ArrayList<MobileUserVO> getGsilMeasureList( final Context context, String site_id, String workdate  )  {
		ArrayList<MobileUserVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GSIL_MEASURE_LIST)+ "?site_id=" + site_id+ "&workdate=" + workdate);
		Log.i("CONSMAN_GSIL_MEASURE_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				wlist = new ArrayList<MobileUserVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileUserVO mobileVo = new MobileUserVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setWorkdate(items.getString("workdate"));
						mobileVo.setSname(items.getString("sname"));
						mobileVo.setSite_id(items.getString("site_id"));
						mobileVo.setUser_id(items.getString("user_id"));
						mobileVo.setUname(items.getString("uname"));
						mobileVo.setBe_image(items.getString("be_image"));
						mobileVo.setBe_content(items.getString("be_content"));
						mobileVo.setMea_content(items.getString("mea_content"));
						mobileVo.setDuedate(items.getString("duedate"));
						mobileVo.setStatus(items.getString("status"));
						mobileVo.setComfirm_id(items.getString("comfirm_id"));
						mobileVo.setCuname(items.getString("cuname"));
						
						//mobileVo.setTitle(items.getString("title"));
						
						wlist.add(mobileVo);
					}
				} else {
					wlist = null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	public String insertGsilMeaure( final Context context, 
			String workdate, String site_id, String user_id, String be_image, String be_content, String mea_content, String status , String title)  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("workdate", workdate));
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("be_image", be_image));
		params.add(new BasicNameValuePair("be_content", be_content));
		params.add(new BasicNameValuePair("mea_content", mea_content));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("title", title));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INSERT_GSIL_MEASURE), params );
		Log.i("CONSMAN_INSERT_GSIL_MEASURE", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	public String updateGsilMeaure( final Context context, 
			String id, String duedate, String status, String mea_content, String comfirm_id )  {
		String resultString = "";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("duedate", duedate));
		params.add(new BasicNameValuePair("mea_content", mea_content));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("confirm_id", comfirm_id));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_GSIL_MEASURE), params );
		Log.i("CONSMAN_UPDATE_GSIL_MEASURE", result);

		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
			
		}
		
		return resultString;
	}
	
	
	
	public ArrayList<MobileEquipVO> getTempDataToy( final Context context, String site_id )  {
		ArrayList<MobileEquipVO> wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_TUNNEL_SENSOR_INFO) + "?site_id=" + site_id  );
		Log.i("CONSMAN_GET_TUNNEL_SENSOR_INFO", result);
		if( result != null && !result.equals("") ) {
			try {
					wlist = new ArrayList<MobileEquipVO>();
					
					JSONObject jsonObj = new JSONObject(result);
					
					CustomJsonObject item = new CustomJsonObject(jsonObj);
					JSONArray jArr = new JSONArray(item.getString("item"));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject item2 = new CustomJsonObject(jArr.getJSONObject(i));
						MobileEquipVO mobileVo = new MobileEquipVO();
						mobileVo.setTunnel(item2.getString("tunnel"));
						
						
						mobileVo.setCo(item2.getString("co"));
						mobileVo.setCo2(item2.getString("co2"));
						mobileVo.setO2(item2.getString("o2"));
						mobileVo.setTemperature(item2.getString("temperature"));
						mobileVo.setHumidity(item2.getString("humidity"));
						
						mobileVo.setSection(item2.getString("section"));
						mobileVo.setWritetime(item2.getString("writetime"));
						wlist.add(mobileVo);
					}
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}
	
	
	public MobileInfoVO getGunpowerInfo( final Context context, String site_id, String cont_id )  {
		MobileInfoVO wlist = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_GUNPOWER_INFO) + "?site_id=" + site_id+ "&cont_id=" + cont_id  );
		Log.i("CONSMAN_GET_GUNPOWER_INFO", result);
		if( result != null && !result.equals("") ) {
			try {
				wlist = new MobileInfoVO();
				JSONArray jArr = new JSONArray(result);
				CustomJsonObject item = new CustomJsonObject(jArr.getJSONObject(0));
				wlist.setId(item.getString("id"));
				wlist.setSite_id(item.getString("site_id"));
				wlist.setCont_id(item.getString("cont_id"));
				wlist.setTemperature(item.getString("temperature"));
				wlist.setHumidity(item.getString("humidity"));
				wlist.setAcc_x(item.getString("acc_x"));
				wlist.setAcc_y(item.getString("acc_y"));
				wlist.setAcc_z(item.getString("acc_z"));
				wlist.setWritetime(item.getString("writetime"));
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		return wlist;
	}

	//Notice
	public ArrayList<MobileNoticeVO> getNoticeList(final Context context, String stie_id) {
		ArrayList<MobileNoticeVO> noticelist = null;
//		String result = getHttpData( HttpUrl.getUrl(context, HttpUrl.CONSMAN_NOTICE_LIST));
		String result = getHttpData( HttpUrl.getUrl(context, HttpUrl.CONSMAN_NOTICE_LIST) + "?site_id=" + stie_id ); 
		Log.i("CONSMAN_NOTICE_LIST", result);
		
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				noticelist = new ArrayList<MobileNoticeVO>();
//				Log.i("json result", item.getString("result"));
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
//					Log.i("length", String.valueOf(jArr.length()));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileNoticeVO m = new MobileNoticeVO();
						m.setId(items.getString("id"));						
//						Log.i("m id", items.getString("id"));
						m.setTitle(items.getString("title"));
						m.setDate(items.optString("date"));
						m.setHit(items.getString("hit"));
						m.setContents(items.getString("contents"));
						
						m.setSite_id(items.getString("site_id"));
						m.setFilepath1(items.getString("filepath1"));
						m.setFilename1(items.getString("filename1"));
						m.setFilepath2(items.getString("filepath2"));
						m.setFilename2(items.getString("filename2"));
						
						noticelist.add(m);
					}
				} else {
					noticelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
//		Log.i("m length", String.valueOf(noticelist.size()));
		return noticelist;
	}
	
	public MobileNoticeVO getNoticeInfo(final Context context, String id) {
		MobileNoticeVO m = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_NOTICE_INFO) + "?id=" + id ); 
		Log.i("CONSMAN_NOTICE_INFO", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					m = new MobileNoticeVO();
					m.setId(item.getString("id"));
					m.setTitle(item.getString("title"));
					m.setDate(item.getString("date"));
					m.setHit(item.getString("hit"));
					m.setContents(item.getString("contents"));
					
				} else {
					m = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}
		
		return m;
	}
	
	public String updateHit (final Context context, String id, String hit) {
		String resultString = "";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("hit", hit));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_HIT), params );
		Log.i("CONSMAN_UPDATE_HIT", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}		
		return resultString;
		
	}
	
	public ArrayList<MobileNoticeVO> getSearchNoticeList(final Context context, String site_id, String date) {
		ArrayList<MobileNoticeVO> noticelist = null;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("site_id", site_id));
		params.add(new BasicNameValuePair("date", date));
//		Log.i("date", date);
		String result = postHttpParameterData( HttpUrl.getUrl(context, HttpUrl.CONSMAN_SEARCH_NOTICE_LIST), params);
		Log.i("CONSMAN_SEARCH_NOTICE_LIST", result);
		
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				noticelist = new ArrayList<MobileNoticeVO>();
//				Log.i("json result", item.getString("result"));
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
//					Log.i("length", String.valueOf(jArr.length()));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileNoticeVO m = new MobileNoticeVO();
						m.setId(items.getString("id"));
//						Log.i("m id", items.getString("id"));
						m.setTitle(items.getString("title"));
						m.setDate(items.optString("date"));
						m.setHit(items.getString("hit"));
						m.setContents(items.getString("contents"));
						
						m.setSite_id(items.getString("site_id"));
						m.setFilepath1(items.getString("filepath1"));
						m.setFilename1(items.getString("filename1"));
						m.setFilepath2(items.getString("filepath2"));
						m.setFilename2(items.getString("filename2"));
						
						noticelist.add(m);
					}
				} else {
					noticelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
//		Log.i("m length", String.valueOf(noticelist.size()));
		return noticelist;
	}
	//Info
	public ArrayList<MobileInfoVO> getInfoList(final Context context) {
		ArrayList<MobileInfoVO> noticelist = null;
		String result = getHttpData( HttpUrl.getUrl(context, HttpUrl.CONSMAN_INFO_LIST));
		Log.i("CONSMAN_INFO_LIST", result);
		
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				noticelist = new ArrayList<MobileInfoVO>();
//					Log.i("json result", item.getString("result"));
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
//						Log.i("length", String.valueOf(jArr.length()));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileInfoVO m = new MobileInfoVO();
						m.setId(items.getString("id"));
//							Log.i("m id", items.getString("id"));
						m.setCategory(items.getString("category"));
						m.setTitle(items.getString("title"));
						m.setDate(items.optString("date"));
						m.setHit(items.getString("hit"));
						m.setContents(items.getString("contents"));
						m.setFilepath1(items.getString("filepath1"));
						m.setFilename1(items.getString("filename1"));
						m.setFilepath2(items.getString("filepath2"));
						m.setFilename2(items.getString("filename2"));
						
						noticelist.add(m);
					}
				} else {
					noticelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
//			Log.i("m length", String.valueOf(noticelist.size()));
		return noticelist;
	}
	
	public MobileInfoVO getInfoDetail(final Context context, String id) {
		MobileInfoVO m = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_INFO_DETAIL) + "?id=" + id ); 
		Log.i("CONSMAN_INFO_DETAIL", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					m = new MobileInfoVO();
					m.setId(item.getString("id"));
					m.setCategory(item.getString("category"));
					m.setTitle(item.getString("title"));
					m.setDate(item.getString("date"));
					m.setHit(item.getString("hit"));
					m.setContents(item.getString("contents"));
					m.setFilepath1(item.getString("filepath1"));
					m.setFilename1(item.getString("filename1"));
					m.setFilepath2(item.getString("filepath2"));
					m.setFilename2(item.getString("filename2"));
					
				} else {
					m = null;
				}
				
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}
		
		return m;
	}
	
	public String updateInfoHit (final Context context, String id, String hit) {
		String resultString = "";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("hit", hit));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_UPDATE_INFO_HIT), params );
		Log.i("CONSMAN_UPDATE_INFO_HIT", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}		
		return resultString;
		
	}
	
	public ArrayList<MobileInfoVO> getSearchInfoList(final Context context, String category, String date) {
		ArrayList<MobileInfoVO> noticelist = null;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("category", category));
		Log.i("category", category);
		params.add(new BasicNameValuePair("date", date));
		Log.i("date", date);
		String result = postHttpParameterData( HttpUrl.getUrl(context, HttpUrl.CONSMAN_SEARCH_INFO_LIST), params);
		Log.i("CONSMAN_SEARCH_INFO_LIST", result);
		
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				noticelist = new ArrayList<MobileInfoVO>();
//					Log.i("json result", item.getString("result"));
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
//						Log.i("length", String.valueOf(jArr.length()));
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileInfoVO m = new MobileInfoVO();
						m.setId(items.getString("id"));
//							Log.i("m id", items.getString("id"));
						m.setCategory(items.getString("category"));
						m.setTitle(items.getString("title"));
						m.setDate(items.optString("date"));
						m.setHit(items.getString("hit"));
						m.setContents(items.getString("contents"));
						m.setFilepath1(items.getString("filepath1"));
						m.setFilename1(items.getString("filename1"));
						m.setFilepath2(items.getString("filepath2"));
						m.setFilename2(items.getString("filename2"));
						
						noticelist.add(m);
					}
				} else {
					noticelist = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
//			Log.i("m length", String.valueOf(noticelist.size()));
		return noticelist;
	}
	
	public ArrayList<MobileVO> getDisasterPushList( final Context context, String site_id)  {
		ArrayList<MobileVO> disasterList = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_DISASTER_PUSH_LIST) + "?site_id=" + site_id );
		Log.i("CONSMAN_GET_DISASTER_PUSH_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				disasterList = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setNamelist(items.getString("namelist"));
						mobileVo.setName(items.getString("name"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setUser_id(items.getString("user_id"));
						mobileVo.setWritetime(items.getString("writetime"));
						mobileVo.setPhone(items.getString("phone"));
						disasterList.add(mobileVo);
					}
				} else {
					disasterList = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return disasterList;
	}

	
	public ArrayList<MobileVO> getDisasterPushBellList( final Context context, String site_id)  {
		ArrayList<MobileVO> disasterList = null;
		
		String result = getHttpData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_GET_DISASTER_PUSH_BELL_LIST) + "?site_id=" + site_id );
		Log.i("CONSMAN_GET_DISASTER_PUSH_BELL_LIST", result);
		if( result != null && !result.equals("") ) {
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				disasterList = new ArrayList<MobileVO>();
				if( item.getString("result").equals("true") ) {
					JSONArray jArr = new JSONArray(item.getString("item"));
					
					for ( int i = 0; i < jArr.length(); i++ ){
						CustomJsonObject items = new CustomJsonObject(jArr.getJSONObject(i));
						MobileVO mobileVo = new MobileVO();
						mobileVo.setId(items.getString("id"));
						mobileVo.setNamelist(items.getString("namelist"));
						mobileVo.setContent(items.getString("content"));
						mobileVo.setUser_id(items.getString("user_id"));
						mobileVo.setWritetime(items.getString("writetime"));
						mobileVo.setPhone(items.getString("phone"));
						disasterList.add(mobileVo);
					}
				} else {
					disasterList = null;
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return disasterList;
	}
	
	public String deletePushContent (final Context context, String id) {
		String resultString = "";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		
		String result = postHttpParameterData( HttpUrl.getUrl( context, HttpUrl.CONSMAN_DELETE_DISASTER_PUSH_LIST), params );
		Log.i("CONSMAN_DELETE_DISASTER_PUSH_LIST", result);
		if( result != null && !result.equals("") ) {
			try {
				JSONObject jsonObj = new JSONObject(result);
				CustomJsonObject item = new CustomJsonObject(jsonObj);
				if( item.getString("result").equals("true") ) {
					resultString  = "true";
				} else {
					resultString  = "";
				}
			} catch(Exception e) {
				//e.printStackTrace();
				return null;
			}
		}		
		return resultString;
		
	}
	
	
	/***********************************************************************************************************************/
	
    public String uploadImage( final Context context, MobileVO item, ArrayList<Bitmap> paramList ) {
    	
    	String result = "";
    	
    	if( paramList != null && paramList.size() > 0 ) {
    		for( int i = 0; i < paramList.size(); i++ ) {
    			Bitmap bitmap = (Bitmap)paramList.get(i);
    			
    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
    			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
    			byte [] byte_arr = stream.toByteArray();

    			result = uploadFile( context, byte_arr , item.getNfc_id()+"_"+(Integer.toString(i))+".jpg");
    		}
    	}
		
		if( result != null && !result.equals("") ) {
			
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "";
        
    }

    public String uploadPersonImage( final Context context, MobileWorkerVO item ) {
    	String result = "";
		Bitmap bitmap = item.getImage();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		result = uploadFile( context, byte_arr , item.getImageName());
		if( result != null && !result.equals("") ) {
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
    }
    
    public String uploadEquipImage( final Context context, MobileEquipVO item ) {
    	String result = "";
		Bitmap bitmap = item.getImage();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		result = uploadFile( context, byte_arr , item.getDeviceImg());
		if( result != null && !result.equals("") ) {
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
    }
    
    public String uploadRiskImage( final Context context, MobileRiskVO item ) {
    	
    	String result = "";
    	

		Bitmap bitmap = item.getImage();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();

//		if( item.getStatus().equals("0") || item.getStatus().equals("1")  ) {
			result = uploadFile( context, byte_arr , item.getSub_image());
//		} else {
//			result = uploadFile( context, byte_arr , item.getMain_image());
//		}
		
		if( result != null && !result.equals("") ) {
			
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "";
        
    }
    
    public String uploadSpotImage( final Context context, MobileSpotVO item ) {
    	String result = "";
		Bitmap bitmap = item.getImage();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		result = uploadFile( context, byte_arr , item.getBef_image());
		if( result != null && !result.equals("") ) {
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
    }
	
    public String uploadRiskMeasureImg( final Context context, String imageName, Bitmap saveImge ) {
    	String result = "";
		Bitmap bitmap = saveImge;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		result = uploadFile( context, byte_arr , imageName);
		if( result != null && !result.equals("") ) {
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
    }
    
    public String uploadGsilMeasureImg( final Context context, String imageName, Bitmap saveImge ) {
    	String result = "";
		Bitmap bitmap = saveImge;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		result = uploadFile( context, byte_arr , imageName);
		if( result != null && !result.equals("") ) {
			try {
				return result;	
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
    }
    

	
	/**********************************************************************************************************/
	// Basic Setting
	/**********************************************************************************************************/
	public String patchDelete(String urlStr, ArrayList<NameValuePair> paramss) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Content-Type",
		                "application/x-www-form-urlencoded");
			con.setRequestMethod("DELETE");

	        int statusCode = con.getResponseCode();
	        if (statusCode == 200) {
	        }
			
			return "";
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
	}
	
	
	public String patchUpdate(String urlStr, ArrayList<NameValuePair> paramss) {
		
		BufferedReader reader = null;
		
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(getQuery(paramss));
			writer.flush();
			
			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			
			String line;
	        int statusCode = con.getResponseCode();
	        if (statusCode == 200) {
	        	while ((line = reader.readLine()) != null) {
	        		sb.append(line);
	        	}
	        }
			
			return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		
	}
	
	@SuppressWarnings("unused")
	public String postHttpParameterData(String URL, ArrayList<NameValuePair> paramss ) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String result = "";
        HttpResponse response = null;
        
        try {
            HttpPost httppost = new HttpPost(URL);
            
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 100000);
			HttpConnectionParams.setSoTimeout(params, 100000);
            
            // Add parameters
            httppost.setEntity(new UrlEncodedFormEntity(paramss,"UTF-8"));
            response = httpclient.execute(httppost);

            if (response == null) {
                return result;
            } else {
                try {
                	int sc = response.getStatusLine().getStatusCode();
                    if( sc == 200 ) {
                    	result = inputStreamToString(response.getEntity().getContent());
                     } else {
                    	
                    	result = inputStreamToString(response.getEntity().getContent());
         				JSONObject jsonObj = new JSONObject(result);
        				
         				CustomJsonObject item = new CustomJsonObject(jsonObj);
                    	
         				if( item != null ) {
         					result = item.getString("message");
         				} else {
         					result = "Fail";
         				}
         				
         				
                     }
                    
                    
                } catch (IllegalStateException e) {
                    Log.e("GSIL", e.getLocalizedMessage(), e);
 
                } catch (IOException e) {
                    Log.e("GSIL", e.getLocalizedMessage(), e);
                }
            }
        } catch (Exception e) {
            Log.e("GSIL", e.getLocalizedMessage(), e);
        }
		
		return result;
	}
	
	public String postHttpData(String URL) {
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(URL);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 100000);
			HttpConnectionParams.setSoTimeout(params, 100000);

			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"UTF-8"));
			
			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
			
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();	
			return ""; 
		}
		
	}
	
	
	public String getHttpData( String URL) {
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpGet post = new HttpGet(URL);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 100000);
			HttpConnectionParams.setSoTimeout(params, 100000);

			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));
			String result = "";
			
			int sc = response.getStatusLine().getStatusCode();
            if( sc == 200 ) {
    			String line = null;
    			while ((line = bufreader.readLine()) != null) {
    				result += line;
    			}
             } else {
            	
     			String line = null;
     			while ((line = bufreader.readLine()) != null) {
     				result += line;
     			}
				
             	result = "";
             }
			

			
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();	
			return e.toString(); 
		}
		
	}
	
    private String inputStreamToString(InputStream is) {
    	 
        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e("GSIL", e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }

    
    public String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    
    /******************************************************************************************************
     * upload file
     * */
    
    public String uploadFile(Context context, byte[] data, String fileName) {
    	 String upLoadServerUri = HttpUrl.getUrl( context, HttpUrl.CONSMAN_FILE_UPLOAD );  
    	 
    	 
         DefaultHttpClient httpclient = new DefaultHttpClient();
         String result = "";
         HttpResponse response = null;
         
         try {
            HttpPost httppost = new HttpPost(upLoadServerUri);
             
            ByteArrayBody bab = new ByteArrayBody(data, fileName);
            
         	MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
         	reqEntity.addPart("file", bab);
         	reqEntity.addPart("filename", new StringBody(fileName));
         	httppost.setEntity(reqEntity);
            
         	
         	
         	response = httpclient.execute(httppost);

             if (response == null) {
                 return result;
             } else {
                 try {
                 	int sc = response.getStatusLine().getStatusCode();
                     if( sc == 200 ) {
                     	result = inputStreamToString(response.getEntity().getContent());
                     } else {
                     	result = "400";
                     }
                     
                 } catch (Exception e) {
                     Log.e("GSIL", e.getLocalizedMessage(), e);
                 }
             }
         } catch (Exception e) {
             Log.e("GSIL", e.getLocalizedMessage(), e);
         }
 		
 		return result;
         
    }
 
    
    public Bitmap getImage(String urls) {
    	
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpGet post = new HttpGet(urls);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 1000000);
			HttpConnectionParams.setSoTimeout(params, 1000000);

			HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
            InputStream input = b_entity.getContent();

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPurgeable = true;
            
            Bitmap bitmap = BitmapFactory.decodeStream(input);

           return bitmap;

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();	
			return null; 
		}

    }
    


}
