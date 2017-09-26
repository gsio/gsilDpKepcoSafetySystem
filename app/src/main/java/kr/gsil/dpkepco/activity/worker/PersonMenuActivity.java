package kr.gsil.dpkepco.activity.worker;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.activity.WorkforceManagementActivity;
import kr.gsil.dpkepco.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonMenuActivity extends BaseActivity implements View.OnClickListener{

	public PersonMenuActivity activity = null;
	Intent backIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_menu);
		activity = this;
		backIntent = new Intent(activity, WorkforceManagementActivity.class);
		setToolbar();
		init();
	}

	private void setToolbar(){
		ActionBar actionBar = getSupportActionBar();
		// Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
		actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
		actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

		//layout을 가지고 와서 actionbar에 포팅을 시킵니다.
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
		TextView title = (TextView)actionbar.findViewById(R.id.title_text);
		((Button) actionbar.findViewById(R.id.btn_title_left)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(backIntent);
				finish();
			}
		});
		title.setText(getString(R.string.worker_mpage_title));
		actionBar.setCustomView(actionbar);

		//액션바 양쪽 공백 없애기
		Toolbar parent = (Toolbar)actionbar.getParent();
		parent.setContentInsetsAbsolute(0,0);
	}
	@Override
	public void init() {
		((Button)findViewById(R.id.btn_reg_person)).setOnClickListener(this);
		((Button)findViewById(R.id.btn_beacon_manage)).setOnClickListener(this);
		((Button)findViewById(R.id.btn_vip_beacon_manage)).setOnClickListener(this);
		setData();
	}

	@Override
	public void setData() {

	}

	@Override
	public void onClick(View v) {
		Intent target = null;
		switch(v.getId()){
			case R.id.btn_reg_person:
				target = new Intent(this, RegPersonListActivity.class);
				startActivity(target);
				//finish();
				break;
			case R.id.btn_beacon_manage:
				target = new Intent(this, BeaconManageActivity.class);
				startActivity(target);
				//finish();
				break;
			case R.id.btn_vip_beacon_manage:
				target = new Intent(this, VipBeaconManageActivity.class);
				target.putExtra("comeFrom", "page");
				startActivity(target);
				break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(backIntent);
		finish();
	}
}
