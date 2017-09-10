package kr.gsil.dpkepco.activity;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.R.id;
import kr.gsil.dpkepco.R.layout;
import kr.gsil.dpkepco.base.BaseActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PopupActivity extends BaseActivity {

	String phone = "";
	String data =  "";
	String name = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		        // 키잠금 해제하기
		        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
		        // 화면 켜기
		        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		    
		    TextView tv = (TextView)findViewById(R.id.dataText);
		    Button ib = (Button)findViewById(R.id.confirmButon);
		    
		    Button disasterButon= (Button)findViewById(R.id.disasterButon);
		    Button carButon1= (Button)findViewById(R.id.carButon1);
		    Button carButon2= (Button)findViewById(R.id.carButon2);
		    
			Intent intent = getIntent();
			
			if( intent.getStringExtra("data") != null && !intent.getStringExtra("data").equals("") ) {
				data = intent.getStringExtra("data");
			} else {
				data = "";
			}
			
			if( intent.getStringExtra("phone") != null && !intent.getStringExtra("phone").equals("") ) {
				phone = intent.getStringExtra("phone");
			} else {
				phone = "";
			}

			if( intent.getStringExtra("name") != null && !intent.getStringExtra("name").equals("") ) {
				name = intent.getStringExtra("name");
			} else {
				name = "";
			}

			
			tv.setText(data + " / 신고자 : "+ name +" 전화번호 : " + phone);
		    
			if( phone.equals("") ) {
				disasterButon.setVisibility(View.GONE);
			}
			
		    ib.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//		            Intent intent = new Intent(PopupActivity.this, MenuActivity.class);
//		            startActivityForResult(intent, 0);
		            finish();
				}
			});
		    
		    disasterButon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if( !phone.equals("") ) {
						String telphone = phone;
						// TODO Auto-generated method stub
						Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
						phoneCallIntent.setData(Uri.parse("tel:"+telphone));
						startActivity(phoneCallIntent);
					} else {
						showToast("수신된 전화번호가 없습니다.");
					}
				}
			});
		    
		    carButon1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String telphone = "01037496245";
					// TODO Auto-generated method stub
					Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
					phoneCallIntent.setData(Uri.parse("tel:"+telphone));
					startActivity(phoneCallIntent);
				}
			}); 
		    
		    carButon2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String telphone = "01032084227";
					// TODO Auto-generated method stub
					Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
					phoneCallIntent.setData(Uri.parse("tel:"+telphone));
					startActivity(phoneCallIntent);
				}
			}); 
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
		
	}
	
	
}
