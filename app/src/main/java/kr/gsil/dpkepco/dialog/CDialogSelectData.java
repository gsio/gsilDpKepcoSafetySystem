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

public class CDialogSelectData extends Dialog {

    TextView cdialog_tv_title  = null;
    TextView cdialog_tv_data  = null;
    TextView cdialog_tv_info  = null;
    Button cdialog_btn_ok  = null;
    Button cdialog_btn_no  = null;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    private String mTitle;
    private String mInfo;
    private String mData;

    public CDialogSelectData(@NonNull Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public CDialogSelectData(@NonNull Context context, String title, String data, String info, View.OnClickListener leftListener , View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mTitle = title;
        this.mInfo = info;
        this.mData = data;
    }
    public CDialogSelectData(@NonNull Context context, int titleId, int dataId, int infoId, View.OnClickListener leftListener , View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mTitle = context.getString(titleId);
        this.mInfo = context.getString(infoId);
        this.mData = context.getString(dataId);;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.cdialog_select_data);

        setLayout();
        setTitle(mTitle);
        setInfo(mInfo);
        setData(mData);
        setClickListener(mLeftClickListener, mRightClickListener);
    }
    private void setTitle(String title){
        cdialog_tv_title.setText(title);
    }

    private void setInfo(String info){
        cdialog_tv_info.setText(info);
    }

    private void setData(String date){
        cdialog_tv_data.setText(date);
    }

    private void setClickListener(View.OnClickListener left , View.OnClickListener right){
        if(left!=null && right!=null){
            cdialog_btn_ok.setOnClickListener(left);
            cdialog_btn_no.setOnClickListener(right);
        }else if(left==null && right!=null){
            cdialog_btn_ok.setVisibility(View.INVISIBLE);
            cdialog_btn_no.setOnClickListener(right);
        }else {

        }
    }

    /*
     * Layout
     */
    private void setLayout(){
        cdialog_btn_ok  = (Button)findViewById(R.id.cdialog_btn_ok);
        cdialog_tv_title  = (TextView)findViewById(R.id.cdialog_tv_title);
        cdialog_tv_data  = (TextView)findViewById(R.id.cdialog_tv_data);
        cdialog_tv_info  = (TextView)findViewById(R.id.cdialog_tv_info);
        cdialog_btn_no  = (Button)findViewById(R.id.cdialog_btn_no);
    }

}
