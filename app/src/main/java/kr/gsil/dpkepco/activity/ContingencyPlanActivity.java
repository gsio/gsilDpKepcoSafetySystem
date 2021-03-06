package kr.gsil.dpkepco.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.KepcoMonitorVO;
import kr.gsil.dpkepco.model.KepcoSensorVO;

public class ContingencyPlanActivity extends BaseActivity {

    public ContingencyPlanActivity activity = null;
    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;

    TextView tv_data_10 = null;
    TextView tv_data_11 = null;
    TextView tv_data_20 = null;
    TextView tv_data_40 = null;
    TextView tv_data_60 = null;
    TextView tv_data_70 = null;
    ImageView iv_status_40  = null;
    @Override
    public void init() {
        tv_data_10 = (TextView)findViewById(R.id.tv_data_10);
        tv_data_11 = (TextView)findViewById(R.id.tv_data_11);
        tv_data_20 = (TextView)findViewById(R.id.tv_data_20);
        tv_data_40 = (TextView)findViewById(R.id.tv_data_40);
        tv_data_60 = (TextView)findViewById(R.id.tv_data_60);
        tv_data_70 = (TextView)findViewById(R.id.tv_data_70);
        iv_status_40  = (ImageView)findViewById(R.id.iv_status_40);
        setData();
    }

    @Override
    public void setData() {
        if(kepcoMonitorVO == null || kepcoSensorVO == null){
            pShow();
            startThread(new Runnable() {
                public void run() {
                    api.getKepcoData(getBaseContext());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pHide();
                            kepcoMonitorVO = app.getKepcoMonitor();
                            kepcoSensorVO = app.getKepcoSensor();
                            setDataValue();
                        }
                    });
                }
            });
        }else{
            setDataValue();
        }
    }

    private void setDataValue(){
        /**
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
         */
        if(kepcoMonitorVO != null){
            //kepcoMonitorVO
            tv_data_10.setText(kepcoMonitorVO.getText1());//String text1;//지층구분
            tv_data_11.setText(kepcoMonitorVO.getText3());//String text3;//그라우팅타입
            tv_data_20.setText(kepcoMonitorVO.getText2());//String text2;//RMR암반등급
            float value1 = (float)kepcoMonitorVO.getValue1();
            if(value1 < 2.0){
                iv_status_40.setImageResource(R.drawable.item_status_0);//평시
            }else if(value1 > 2.5){
                iv_status_40.setImageResource(R.drawable.item_status_2);//위험
            }else{
                iv_status_40.setImageResource(R.drawable.item_status_1);//주의
            }

            tv_data_40.setText(String.format("%,.1f", value1));//double value1;//염분농도
            tv_data_60.setText(kepcoMonitorVO.getText4());//String text4;//Target압력
            tv_data_70.setText(String.format("%,.1f", (float)kepcoMonitorVO.getValue2()));//double value2;//막장압력
        }
        if(kepcoSensorVO != null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contingency_plan);
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
        title.setText(getString(R.string.page_title_02));
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent target = new Intent(activity, MainActivity.class);
        startActivity(target);
        finish();
    }
}
