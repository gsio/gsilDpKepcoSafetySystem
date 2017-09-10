package kr.gsil.dpkepco.activity;

import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;
import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.model.MobileVO;
import kr.gsil.dpkepco.util.BackPressCloseHandler;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
	private BackPressCloseHandler backPressCloseHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//		phone=getPhoneNumber();
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
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
	}
	
	public String getPhoneNumber() { 
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		//
		String phone = mgr.getLine1Number();
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
		
//		if( regId == null || regId.equals("") ) {
//			if (Build.VERSION.SDK_INT > 23) {
//				registerGcm();
//			} else {
//				registerGcm();
//				return;
//			}
//		}
		
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
								//app.setSite_name(returnUser.getSname());
									eventUpdateSignUpComplete();
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
	
	public void eventUpdateSignUpComplete() {
		showToast("어서 오세요.");
//		Intent intent = new Intent(this, ReNewMenuActivity.class);
//		startActivity(intent);
		finish();		
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
