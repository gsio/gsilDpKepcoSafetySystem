package kr.gsil.dpkepco.activity;

import java.util.ArrayList;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileVO;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;

public class LoadingActivity extends BaseActivity {
	Handler handler = new Handler();
	String myDeviceId ="";
	String myPhoneNumber = "";
	String device_version = "";
	
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    
	ArrayList<MobileVO> list = new ArrayList<MobileVO>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		handler.postDelayed(run, 2000);
//		setData();
	}



	@Override
    protected void onDestroy() {
		super.onDestroy();
    }
	
	void appStart(){
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
    		if( app.isLogin() ) {
//    			Intent intent = new Intent(this, ReNewMenuActivity.class);
//    			startActivity(intent);
    			finish();
    		} else {
    			app.setPid(myDeviceId);
    			Intent intent = new Intent(this, LoginActivity.class);
    			startActivity(intent);
    			finish();
    		}
        }

	}
	
	Runnable run = new Runnable() {
		
		@Override
		public void run() {
			appStart();
		}
	};
	
	
	   //권한 허가시 들어가는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
            	//showToast(Integer.toString(grantResults[1]));
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
            		if( app.isLogin() ) {
//            			Intent intent = new Intent(this, ReNewMenuActivity.class);
//            			startActivity(intent);
            			finish();
            		} else {
            			app.setPid(myDeviceId);
            			Intent intent = new Intent(this, LoginActivity.class);
            			startActivity(intent);
            			finish();
            		}
                }
                return;
        }
    }
    //마시멜로 부터 권한 가지고오기
    private void checkPermission() {
        if (
               
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED

                ) {
            requestPermissions(new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.NFC,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.VIBRATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.READ_PHONE_STATE
            },
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
    		if( app.isLogin() ) {
//    			Intent intent = new Intent(this, ReNewMenuActivity.class);
//    			startActivity(intent);
    			finish();
    		} else {
    			app.setPid(myDeviceId);
    			Intent intent = new Intent(this, LoginActivity.class);
    			startActivity(intent);
    			finish();
    		}
        }
    }

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData() {

	}
	
	public String getPhoneNumber() { 
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		//
		String phone = mgr.getLine1Number();
		if( phone.indexOf("+82") == 0 ) {
			phone = "0" + phone.substring(3);
		}
		
		app.setPhone(phone);
		
		return phone;
	}
	

	// 장치 아이디 얻기 
	public String getDeviceId(){ 
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		return mgr.getDeviceId(); 
	}
	
	
}