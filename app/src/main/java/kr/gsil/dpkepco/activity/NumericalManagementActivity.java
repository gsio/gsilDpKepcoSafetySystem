package kr.gsil.dpkepco.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;

public class NumericalManagementActivity extends BaseActivity implements View.OnClickListener{

    public NumericalManagementActivity activity = null;

    @Override
    public void init() {
        ((Button)findViewById(R.id.btn_today_drilling)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_drilling_drive_state)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_pressure)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_solt_rate)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_polluted_water)).setOnClickListener(this);

        setData();
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerical_management);
        activity = this;
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
                Intent target = new Intent(activity, MainActivity.class);
                startActivity(target);
                finish();
            }
        });
        title.setText(getString(R.string.page_title_08));
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }

    /**
     type 1 염분농도
     type 2 막장압력
     type 3 오탁수 처리량
     type 4 굴진상태
     type 5 총인력
     */
    @Override
    public void onClick(View v) {
        Intent target = null;
        switch(v.getId()){
            case R.id.btn_today_drilling:
                target = new Intent(this, DataInputDrillingActivity.class);
                target.putExtra("inputType", 1);//type 1 굴진량
                target.putExtra("comeFrom", "page");
                startActivity(target);
                finish();
                break;
            case R.id.btn_drilling_drive_state:
                target = new Intent(this, DrillingDriveStateActivity.class);
                target.putExtra("inputType", 4);//type 4 굴진상태
                target.putExtra("comeFrom", "page");
                startActivity(target);
                finish();
                break;
            case R.id.btn_pressure:
                target = new Intent(this, DataInputManagementActivity.class);
                target.putExtra("inputType", 2);//type 2 막장압력
                target.putExtra("comeFrom", "page");
                startActivity(target);
                finish();
                break;
            case R.id.btn_solt_rate:
                target = new Intent(this, DataInputManagementActivity.class);
                target.putExtra("inputType", 1);//type 1 염분농도
                target.putExtra("comeFrom", "page");
                startActivity(target);
                finish();
                break;
            case R.id.btn_polluted_water:
                target = new Intent(this, DataInputManagementActivity.class);
                target.putExtra("inputType", 3);//type 3 오탁수 처리량
                target.putExtra("comeFrom", "page");
                startActivity(target);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent target = new Intent(activity, MainActivity.class);
        startActivity(target);
        finish();
    }
}
