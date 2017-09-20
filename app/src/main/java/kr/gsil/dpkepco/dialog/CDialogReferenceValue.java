package kr.gsil.dpkepco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;

import kr.gsil.dpkepco.R;

/**
 * Created by gsil on 2017. 9. 20..
 */

public class CDialogReferenceValue extends Dialog {

    private String mTitle;
    private String mData;
    private int popupKind = -1;
    private View.OnClickListener mCancleClickListener;

    public CDialogReferenceValue(@NonNull Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public CDialogReferenceValue(@NonNull Context context, int popupKind, String title, String data, View.OnClickListener cancelListener) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mCancleClickListener = cancelListener;
        this.popupKind = popupKind;
        this.mTitle = title;
        this.mData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.cdialog_reference_value);
    }
}
