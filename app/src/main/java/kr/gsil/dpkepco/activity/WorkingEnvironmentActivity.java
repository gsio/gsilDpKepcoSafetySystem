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

public class WorkingEnvironmentActivity extends BaseActivity {

    public WorkingEnvironmentActivity activity = null;
    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;
    TextView tv_data_20  = null;
    ImageView iv_status_20  = null;
    Button btn_info_20  = null;
    TextView tv_data_30  = null;
    ImageView iv_status_30  = null;
    Button btn_info_30  = null;
    TextView tv_data_40  = null;
    ImageView iv_status_40  = null;
    Button btn_info_40  = null;
    TextView tv_data_50  = null;
    ImageView iv_status_50  = null;
    Button btn_info_50  = null;
    TextView tv_data_60  = null;
    ImageView iv_status_60  = null;
    Button btn_info_60  = null;
    TextView tv_data_70  = null;
    ImageView iv_status_70  = null;
    Button btn_info_70  = null;
    @Override
    public void init() {
        tv_data_20  = (TextView)findViewById(R.id.tv_data_20);
        iv_status_20  = (ImageView)findViewById(R.id.iv_status_20);
        btn_info_20  = (Button)findViewById(R.id.btn_info_20);
        tv_data_30  = (TextView)findViewById(R.id.tv_data_30);
        iv_status_30  = (ImageView)findViewById(R.id.iv_status_30);
        btn_info_30  = (Button)findViewById(R.id.btn_info_30);
        tv_data_40  = (TextView)findViewById(R.id.tv_data_40);
        iv_status_40  = (ImageView)findViewById(R.id.iv_status_40);
        btn_info_40  = (Button)findViewById(R.id.btn_info_40);
        tv_data_50  = (TextView)findViewById(R.id.tv_data_50);
        iv_status_50  = (ImageView)findViewById(R.id.iv_status_50);
        btn_info_50  = (Button)findViewById(R.id.btn_info_50);
        tv_data_60  = (TextView)findViewById(R.id.tv_data_60);
        iv_status_60  = (ImageView)findViewById(R.id.iv_status_60);
        btn_info_60  = (Button)findViewById(R.id.btn_info_60);
        tv_data_70  = (TextView)findViewById(R.id.tv_data_70);
        iv_status_70  = (ImageView)findViewById(R.id.iv_status_70);
        btn_info_70  = (Button)findViewById(R.id.btn_info_70);
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
         private double value6;//수위


        //kepcoSensorVO
        private double o2;//산소
        private double co;//일산화탄소
        private double h2s;//황화수소
        private double gas;//가연성가스
        */
        if(kepcoMonitorVO != null){
            //kepcoMonitorVO
            int val3 = (int)kepcoMonitorVO.getValue3();
            if(val3 < 500){
                iv_status_60.setImageResource(R.drawable.item_status_0);//평시
            }else if(val3 > 1000){
                iv_status_60.setImageResource(R.drawable.item_status_2);//위험
            }else{
                iv_status_60.setImageResource(R.drawable.item_status_1);//주의
            }
            tv_data_60.setText(String.format("%,d", val3));//double value3;//오탁수 처리량
            float val6 = 1.0f;//(float)kepcoMonitorVO.getValue6();
            if(val6 < 2){
                iv_status_70.setImageResource(R.drawable.item_status_0);//평시
            }else if(val6 > 2.5){
                iv_status_70.setImageResource(R.drawable.item_status_2);//위험
            }else{
                iv_status_70.setImageResource(R.drawable.item_status_1);//주의
            }
            tv_data_70.setText(String.format("%,.1f", val6));//double value6;//집수정 지하수위
        }
        if(kepcoSensorVO != null){
            float o2 = (float)kepcoSensorVO.getO2();
            if(o2 < 18){
                iv_status_20.setImageResource(R.drawable.item_status_0);//평시
            }else if(o2 > 23.5){
                iv_status_20.setImageResource(R.drawable.item_status_2);//위험
            }else{
                iv_status_20.setImageResource(R.drawable.item_status_1);//주의
            }
            tv_data_20.setText(String.format("%,.1f", o2));//double o2;//산소
            float co = (float)kepcoSensorVO.getCo();
            if(co <= 50){
                iv_status_30.setImageResource(R.drawable.item_status_0);//평시
            //}else if(co > 50){
            //    iv_status_30.setImageResource(R.drawable.item_status_2);//위험
            }else{
                //iv_status_30.setImageResource(R.drawable.item_status_1);//주의
                iv_status_30.setImageResource(R.drawable.item_status_2);//위험
            }
            tv_data_30.setText(String.format("%,.1f", co));//double co;//일산화탄소
            float h2s = (float)kepcoSensorVO.getH2s();
            if(h2s < 30){
                iv_status_40.setImageResource(R.drawable.item_status_0);//평시
            //}else if(h2s > 2.5){
            //    iv_status_40.setImageResource(R.drawable.item_status_2);//위험
            }else{
                //iv_status_40.setImageResource(R.drawable.item_status_1);//주의
                iv_status_40.setImageResource(R.drawable.item_status_2);//위험
            }
            tv_data_40.setText(String.format("%,.1f", h2s));//double h2s;//황화수소
            float gas = (float)kepcoSensorVO.getGas();
            if(gas < 10){
                iv_status_50.setImageResource(R.drawable.item_status_0);//평시
            //}else if(gas > 2.5){
            //    iv_status_50.setImageResource(R.drawable.item_status_2);//위험
            }else{
                //iv_status_50.setImageResource(R.drawable.item_status_1);//주의
                iv_status_50.setImageResource(R.drawable.item_status_2);//위험
            }
            tv_data_50.setText(String.format("%,.1f", gas));//double gas;//가연성가스
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_environment);
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
        title.setText(getString(R.string.page_title_06));
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