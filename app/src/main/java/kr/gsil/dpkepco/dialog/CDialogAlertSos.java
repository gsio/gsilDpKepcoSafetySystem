package kr.gsil.dpkepco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import kr.gsil.dpkepco.R;

/**
 * Created by gsil on 2017. 9. 21..
 */

public class CDialogAlertSos extends Dialog {

    ImageButton cdialog_btn_alimi  = null;
    ImageButton cdialog_btn_sos  = null;
    ImageButton cdialog_btn_mission  = null;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private View.OnClickListener mCenterClickListener;

    public CDialogAlertSos(@NonNull Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public CDialogAlertSos(@NonNull Context context, View.OnClickListener leftListener , View.OnClickListener centerListener , View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mCenterClickListener = centerListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = 0.8f;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
        getWindow().setAttributes(wlp);
        setContentView(R.layout.cdialog_alert_sos);
        setLayout();
        setClickListener(mLeftClickListener,mCenterClickListener ,mRightClickListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            this.dismiss();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setClickListener(View.OnClickListener left , View.OnClickListener center , View.OnClickListener right){

        if(left!=null ) cdialog_btn_alimi.setOnClickListener(left);
        if(center!=null ) cdialog_btn_sos.setOnClickListener(center);
        if(right!=null ) cdialog_btn_mission.setOnClickListener(right);
    }
    /*
     * Layout
     */
    private void setLayout(){
        cdialog_btn_alimi  = (ImageButton)findViewById(R.id.cdialog_btn_alimi);
        cdialog_btn_sos  = (ImageButton)findViewById(R.id.cdialog_btn_ok);
        cdialog_btn_mission  = (ImageButton)findViewById(R.id.cdialog_btn_mission);
    }

}

