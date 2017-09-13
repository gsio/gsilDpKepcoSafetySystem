package kr.gsil.dpkepco.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;

public class LocationInTunnelActivity extends BaseActivity {

    public LocationInTunnelActivity activity = null;
    private static final String TEST_PAGE_URL = "http://14.63.198.88/KEPCO/tunnel_view";
    private WebView mWebView = null;

    @Override
    public void init() {
        mWebView = (WebView)findViewById(R.id.webView);
        setData();
    }

    @Override
    public void setData() {
        mWebView.setInitialScale(100); //scale조절
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.loadUrl(TEST_PAGE_URL);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_in_tunnel);
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
        title.setText(getString(R.string.page_title_05));
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

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView != null) mWebView = null;
        activity = null;
    }
    @Override
    public void onPause(){
        super.onPause();
    }
}