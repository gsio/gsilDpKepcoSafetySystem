package kr.gsil.dpkepco.activity;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PersonMenuActivity extends BaseActivity {
	
	Button regPersonBtn = null;
	//비콘 배정관리
	Button manaBeaconBtn = null;
	ImageView topImg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_menu);
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		
		topImg = ( ImageView ) findViewById(R.id.barIcon);
		topImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		manaBeaconBtn = ( Button ) findViewById(R.id.manaBeaconBtn);


		regPersonBtn = ( Button ) findViewById(R.id.regPersonBtn);
		regPersonBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { Intent intent = new Intent(PersonMenuActivity.this, RegPersonListActivity.class); startActivity(intent); }
		});



		manaBeaconBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonMenuActivity.this, BeaconManageActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
	}

}
