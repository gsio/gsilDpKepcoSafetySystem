package kr.gsil.dpkepco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kr.gsil.dpkepco.R;

/**
 * Created by gsil on 2017. 9. 13..
 */

public class CDialogVipNameBeacon extends Dialog {

    TextView cdialog_tv_title  = null;
    TextView cdialog_tv_date  = null;
    EditText cdialog_et_data  = null;
    TextView cdialog_tv_info  = null;
    Button cdialog_btn_init  = null;
    Button cdialog_btn_ok  = null;
    Button cdialog_btn_no  = null;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mCenterClickListener;
    private View.OnClickListener mRightClickListener;

    private String mTitle;
    private String mInfo;
    private String mData;
    private String mDate;
    private boolean isNew = true;
    private int id = -1;

    public CDialogVipNameBeacon(@NonNull Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        isNew = true;
    }

    public CDialogVipNameBeacon(@NonNull Context context, boolean isNew, String title, String date, String data, String info
            , View.OnClickListener leftListener , View.OnClickListener centerListener , View.OnClickListener rightListener, int id) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mCenterClickListener = centerListener;
        this.mRightClickListener = rightListener;
        this.mTitle = title;
        this.mInfo = info;
        this.mDate = date;
        this.mData = data;
        this.isNew = isNew;
        this.id = id;
    }
    public CDialogVipNameBeacon(@NonNull Context context, boolean isNew, int titleId, String date, String data, int infoId
            , View.OnClickListener leftListener , View.OnClickListener centerListener , View.OnClickListener rightListener, int id) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mCenterClickListener = centerListener;
        this.mRightClickListener = rightListener;
        this.mTitle = context.getString(titleId);
        this.mInfo = context.getString(infoId);
        this.mDate = date;
        this.mData = data;
        this.isNew = isNew;
        this.id = id;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.cdialog_vip_name_beacon);

        setLayout();
        setTitle(mTitle);
        setInfo(mInfo);
        //setDate(mDate);
        setData(mData);
        setClickListener(mLeftClickListener, mCenterClickListener, mRightClickListener);
        //Log.e("onCreate","mData = "+mData+" id = "+id+" mDate = "+mDate);
    }
    private void setTitle(String title){
        cdialog_tv_title.setText(title);
    }

    private void setInfo(String info){
        cdialog_tv_info.setText(info);
    }

    private void setDate(String date){
        cdialog_tv_date.setText(date);
    }


    private void setData(String data){
        cdialog_et_data.setText(data);
        cdialog_et_data.setSelection(cdialog_et_data.getText().length());
    }

    public float getData(){
        String data = cdialog_et_data.getText().toString();
        return Float.valueOf(data);
    }

    public int getId(){
        return id;
    }

    public boolean isNew(){
        return isNew;
    }
    private void setClickListener(View.OnClickListener left , View.OnClickListener center , View.OnClickListener right){
        if(left!=null && center!=null && right!=null){
            cdialog_btn_init.setOnClickListener(left);
            cdialog_btn_ok.setOnClickListener(center);
            cdialog_btn_no.setOnClickListener(right);
        }else if(left==null && center!=null && right!=null){
            cdialog_btn_init.setVisibility(View.INVISIBLE);
            cdialog_btn_ok.setOnClickListener(center);
            cdialog_btn_no.setOnClickListener(right);
        }else {

        }
    }

    /*
     * Layout
     */
    private void setLayout(){
        cdialog_btn_init  = (Button)findViewById(R.id.cdialog_btn_init);
        cdialog_btn_ok  = (Button)findViewById(R.id.cdialog_btn_ok);
        cdialog_tv_title  = (TextView)findViewById(R.id.cdialog_tv_title);
        cdialog_tv_date  = (TextView)findViewById(R.id.cdialog_tv_date);
        cdialog_et_data  = (EditText)findViewById(R.id.cdialog_et_data);
        cdialog_tv_info  = (TextView)findViewById(R.id.cdialog_tv_info);
        cdialog_btn_no  = (Button)findViewById(R.id.cdialog_btn_no);
    }

}
