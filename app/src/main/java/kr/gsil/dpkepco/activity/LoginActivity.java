package kr.gsil.dpkepco.activity;

import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;
import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.AcceptPhoneVO;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.model.MobileVO;
import kr.gsil.dpkepco.util.BackPressCloseHandler;
import kr.gsil.dpkepco.util.Utility;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	EditText idmaintxt = null;
	EditText pwmaintxt = null;
	String id = "";
	String pw = "";
	String phone = "";
	
	ArrayList<MobileVO> list = new ArrayList<MobileVO>();
	
	Button loginbtn = null;
	String regId  = "";
	String webViewUserAgent = "";
	private BackPressCloseHandler backPressCloseHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		phone=getPhoneNumber();
//		registerGcm();
		init();
		backPressCloseHandler = new BackPressCloseHandler(this);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		idmaintxt = ( EditText ) findViewById(R.id.idmaintxt);
		pwmaintxt = ( EditText ) findViewById(R.id.pwmaintxt);
		loginbtn = ( Button ) findViewById(R.id.loginbtn);
		
		loginbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				eventLogin();
			}
		});
		WebView webView = new WebView(this);
		webViewUserAgent = webView.getSettings().getUserAgentString();
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
	}
	
	public String getPhoneNumber() { 
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		//
		String phone = mgr.getLine1Number();
		if(phone == null) return "";

		if( phone.indexOf("+82") == 0 ) {
			phone = "0" + phone.substring(3);
		}
		return phone;
	}
	
	private void eventLogin() {
		if( idmaintxt.getText().toString().equals("") ) {
			showToast("아이디를 입력해주세요.");
			return;
		}
		
		if( pwmaintxt.getText().toString().equals("") ) {
			showToast("패스워드를 입력해주세요.");
			return;
		}
		id = idmaintxt.getText().toString();
		id = id.replaceAll("\\p{Space}", "");
		pw = pwmaintxt.getText().toString();
		pw = pw.replaceAll("\\p{Space}", "");
		eventStartLogin( id, pw );
	}
	
	public void eventStartLogin( final String userid, final String userpw  ) {
		
		if( regId == null || regId.equals("") ) {
			if (Build.VERSION.SDK_INT > 23) {
				registerGcm();
			} else {
				registerGcm();
				return;
			}
		}
//
		pShow();
		startThread(new Runnable() {
			public void run() {
				final MobileUserVO returnUser;
				returnUser = api.mobileUserLogin(getBaseContext(), userid, userpw, regId, phone);
				runOnUiThread(new Runnable() {
					public void run() {
						if( returnUser != null ) {
							if( returnUser != null && returnUser.getId() != null && !returnUser.getId().equals("") ) {
								app.setLogin(true);
								app.setAutoLogin(true);
								app.setId(returnUser.getId());
								app.setType(returnUser.getType());
								app.setCont_id(returnUser.getCont_id());
								app.setSite_id(returnUser.getSite_id());
								app.setName(returnUser.getName());
								app.setRtype(returnUser.getDcode());
								app.setPhone(phone);
								app.setUserid(returnUser.getUserid());
								app.setCname(returnUser.getCname());
								//app.setSite_name(returnUser.getSname());
								app.setWeatherCallCnt(0);//날씨 호출 용 카운트 초기값 설정

								RunAppversion task = new RunAppversion(userid);
								task.execute();

							} else {
								showToast("로그인에 실패하였습니다.");
							}
						} else {
							showToast("아이디/패스워드를 확인해주세요");
						}
						pHide();


					}
				});
			}
		});
	}

	private class RunAppversion extends AsyncTask<Void, Void, Integer> {
		String userid = "";
		public RunAppversion(String userid){
			this.userid = userid;
		}
		@Override
		protected Integer doInBackground(Void... params) {
			int msg = 1;
			Context context = getApplicationContext();
			String app_ver = Utility.getAppVersion(context);;
			String android_ver = android.os.Build.VERSION.SDK_INT + ":" + Build.VERSION.RELEASE;
			String etc = webViewUserAgent;
			String browser_ver = "";

			api.updateVersionInfo(context, userid, app_ver, android_ver, browser_ver, etc);
			//설정 정보 가져와 비교하기
			String setting_accept_phone = context.getResources().getString(R.string.setting_accept_phone);
			if(setting_accept_phone != null && !setting_accept_phone.equals("") && setting_accept_phone.toUpperCase().equals("TRUE")){
				AcceptPhoneVO acceptPhone = api.getAcceptPhoneList(context, userid);

				if(acceptPhone == null) msg = 0;//logout
				else{
					if(!acceptPhone.isNeedDiff()) msg = 1;//login
					else{
						if(phone.equals("")) msg = 2;//logout
						else if(acceptPhone.isContainPhoneNumber(phone)) msg = 1;//login
						else msg = 3;//logout
					}
				}
			}
			return msg;
		}

		@Override
		protected void onPostExecute(Integer msg){
			switch(msg){
				case 0:
					showToast("장애가 발생하였습니다. 다시 시도해 주세요.");
					initLoginInfo();
					break;
				case 1://정상 로그인
					eventUpdateSignUpComplete();
					break;
				case 2:
					showToast("핸드폰 번호가 있는 폰에서만 로그인 가능한 아이디 입니다.");
					initLoginInfo();
					break;
				case 3:
					showToast("인증되지 않은 전화번호입니다. 담당자에게 문의해 주세요.");
					initLoginInfo();
					break;
			}
		}
	}
	
	public void eventUpdateSignUpComplete() {
		showToast("어서 오세요.");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();		
	}

	public void initLoginInfo(){
		app.setLogin(false);
		app.setAutoLogin(false);
		app.setId("");
		app.setType("");
		app.setCont_id("");
		app.setSite_id("");
		app.setName("");
		app.setRtype("");
		app.setPhone("");
		app.setUserid("");
		app.setCname("");
	}
	
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    
	public void registerGcm() {
		 
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		 
		regId = GCMRegistrar.getRegistrationId(this);
		 
		if (regId.equals("")) {
//			GCMRegistrar.unregister(this);
			GCMRegistrar.register(this, "1027548757813" );
//			1027548757813
		} else {
			if( !id.equals("") && !pw.equals("") ) {
				eventStartLogin( id, pw );
			}
			Log.e("id", regId);
			
		}
		 
	}
}
