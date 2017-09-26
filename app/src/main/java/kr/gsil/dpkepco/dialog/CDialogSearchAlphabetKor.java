package kr.gsil.dpkepco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import kr.gsil.dpkepco.R;

/**
 * Created by gsil on 2017. 9. 25..
 */

public class CDialogSearchAlphabetKor extends Dialog implements View.OnClickListener{

    private View.OnClickListener mSearchClickListener;
    private View.OnClickListener mCancleClickListener;
    ListView lv_data_list  = null;
    EditText et_search  = null;
    Button btn_alphabet_kor_search  = null;
    Button btn_alphabet_kor_close  = null;

    public CDialogSearchAlphabetKor(@NonNull Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    protected CDialogSearchAlphabetKor(@NonNull Context context, View.OnClickListener searchListener, View.OnClickListener cancelListener) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mSearchClickListener = searchListener;
        this.mCancleClickListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        initOutside();
        setContentView(R.layout.cdialog_search_aphaber_kor);
        setLayout();
        setClickListener(mSearchClickListener,mCancleClickListener);
    }

    private void initOutside(){
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

    private void setClickListener(View.OnClickListener search, View.OnClickListener cancle){

        if(search!=null ) btn_alphabet_kor_search.setOnClickListener(search);
        if(cancle!=null ) btn_alphabet_kor_close.setOnClickListener(cancle);
    }
    /*
     * Layout
     */
    private void setLayout(){
        lv_data_list  = (ListView)findViewById(R.id.lv_data_list);
        et_search  = (EditText)findViewById(R.id.et_search);
        btn_alphabet_kor_search  = (Button)findViewById(R.id.btn_alphabet_kor_search);
        btn_alphabet_kor_close  = (Button)findViewById(R.id.btn_alphabet_kor_close);
        ((Button)findViewById(R.id.btn_alphabet_kor_0)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_1)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_2)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_3)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_4)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_5)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_6)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_7)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_8)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_9)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_10)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_11)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_12)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_13)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_14)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_15)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_alphabet_kor_16)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_alphabet_kor_0: break;
            case R.id.btn_alphabet_kor_1: break;
            case R.id.btn_alphabet_kor_2: break;
            case R.id.btn_alphabet_kor_3: break;
            case R.id.btn_alphabet_kor_4: break;
            case R.id.btn_alphabet_kor_5: break;
            case R.id.btn_alphabet_kor_6: break;
            case R.id.btn_alphabet_kor_7: break;
            case R.id.btn_alphabet_kor_8: break;
            case R.id.btn_alphabet_kor_9: break;
            case R.id.btn_alphabet_kor_10: break;
            case R.id.btn_alphabet_kor_11: break;
            case R.id.btn_alphabet_kor_12: break;
            case R.id.btn_alphabet_kor_13: break;
            case R.id.btn_alphabet_kor_14: break;
            case R.id.btn_alphabet_kor_15: break;
            case R.id.btn_alphabet_kor_16: break;
            case R.id.btn_alphabet_kor_search: break;
            case R.id.btn_alphabet_kor_close: break;
        }
    }
}

