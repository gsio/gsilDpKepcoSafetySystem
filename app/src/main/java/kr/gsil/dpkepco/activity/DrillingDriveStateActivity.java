package kr.gsil.dpkepco.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.dialog.CDialogInputData;
import kr.gsil.dpkepco.dialog.CDialogSelectData;
import kr.gsil.dpkepco.model.KepcoMonitorVO;
import kr.gsil.dpkepco.model.KepcoSensorVO;

public class DrillingDriveStateActivity extends BaseActivity implements View.OnClickListener {
    public DrillingDriveStateActivity activity = null;

    public static final int STATE_DRILLING = 1;//굴진
    public static final int STATE_CONSTRUCTION = 2;//조립
    public static final int STATE_CHECKING = 3;//정비
    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;

    Button btn_drilling_state_0  = null;
    Button btn_drilling_state_1  = null;
    Button btn_drilling_state_2  = null;
    public int drillingDriveState = STATE_CONSTRUCTION;
    public int changeState = STATE_CONSTRUCTION;

    int inputType = 4;
    String comeFrom = "page";
    Intent target = null;
    CDialogSelectData mCDialog = null;
    boolean isChange = false;
    @Override
    public void init() {
        btn_drilling_state_0  = (Button)findViewById(R.id.btn_drilling_state_0);
        btn_drilling_state_1  = (Button)findViewById(R.id.btn_drilling_state_1);
        btn_drilling_state_2  = (Button)findViewById(R.id.btn_drilling_state_2);
        btn_drilling_state_0.setOnClickListener(this);
        btn_drilling_state_1.setOnClickListener(this);
        btn_drilling_state_2.setOnClickListener(this);
        isChange = false;
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
        if(kepcoMonitorVO != null){
            //kepcoMonitorVO
            drillingDriveState = (int)kepcoMonitorVO.getValue4();//double value4;//굴진 상태
            setButton(drillingDriveState);
        }
        /*if(kepcoSensorVO != null){

        }*/
    }

    private void setButton(int stat){
        if(stat == DrillingDriveStateActivity.STATE_DRILLING){
            btn_drilling_state_0.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_0_on);
            btn_drilling_state_1.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_1_off);
            btn_drilling_state_2.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_2_off);
        }else if(stat == DrillingDriveStateActivity.STATE_CONSTRUCTION){
            btn_drilling_state_0.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_0_off);
            btn_drilling_state_1.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_1_on);
            btn_drilling_state_2.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_2_off);
        }else{
            btn_drilling_state_0.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_0_off);
            btn_drilling_state_1.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_1_off);
            btn_drilling_state_2.setBackgroundResource(R.drawable.bg_btn_drilling_state_lv_2_on);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drilling_drive_state);
        activity = this;
        init();
        Intent intent = getIntent();
        inputType = intent.getIntExtra("inputType", 4);
        comeFrom = intent.getStringExtra("comeFrom");
        Log.e("onCreate","inputType = "+inputType+" comeFrom = "+comeFrom);
        if(comeFrom.equals("home")){
            target = new Intent(activity, MainActivity.class);
        }else {
            target = new Intent(activity, NumericalManagementActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //변경되면 초기화
        if(isChange) app.setKepcoData(null, null);
        startActivity(target);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_drilling_state_0:
                if(drillingDriveState != STATE_DRILLING){
                    changeState = STATE_DRILLING;
                    mCDialog = new CDialogSelectData(this, R.string.drilling_state_title_txt, R.string.drilling_state_txt_0, R.string.page_popup_input_txt_3,
                            leftClickListener, rightClickListener);
                    mCDialog.show();
                }
                break;
            case R.id.btn_drilling_state_1:
                if(drillingDriveState != STATE_CONSTRUCTION){
                    changeState = STATE_CONSTRUCTION;
                    mCDialog = new CDialogSelectData(this, R.string.drilling_state_title_txt, R.string.drilling_state_txt_1, R.string.page_popup_input_txt_3,
                            leftClickListener, rightClickListener);
                    mCDialog.show();
                }
                break;
            case R.id.btn_drilling_state_2:
                if(drillingDriveState != STATE_CHECKING){
                    changeState = STATE_CHECKING;
                    mCDialog = new CDialogSelectData(this, R.string.drilling_state_title_txt, R.string.drilling_state_txt_2, R.string.page_popup_input_txt_3,
                            leftClickListener, rightClickListener);
                    mCDialog.show();
                }
                break;
        }
    }

    private View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Toast.makeText(getApplicationContext(), "왼쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();*/
            pShow();
            startThread(new Runnable() {
                public void run() {
                    api.insertTimelyValue(getBaseContext(), String.valueOf(inputType), String.valueOf(changeState), app.getUserid());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pHide();
                            drillingDriveState = changeState;
                            isChange = true;
                            setButton(drillingDriveState);
                        }
                    });
                }
            });
            mCDialog.dismiss();
        }
    };

    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Toast.makeText(getApplicationContext(), "오른쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();*/
            mCDialog.dismiss();
        }
    };
}
