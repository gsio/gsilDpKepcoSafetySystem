package kr.gsil.dpkepco.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.activity.worker.VipBeaconManageActivity;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.dialog.CDialogAlertSos;
import kr.gsil.dpkepco.model.KepcoMonitorVO;
import kr.gsil.dpkepco.model.KepcoRecoDataVO;
import kr.gsil.dpkepco.model.KepcoSensorVO;
import kr.gsil.dpkepco.util.BackPressCloseHandler;
import kr.gsil.dpkepco.model.weather.WeatherInfoVO;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private BackPressCloseHandler backPressCloseHandler;
    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;
    KepcoRecoDataVO kepcoRecoDataVO = null;
    WeatherInfoVO weather = null;

    ImageView   img_weather = null;
    TextView    tv_weather_state = null;
    TextView	tv_weather_subsi_do = null;
    TextView	tv_weather_percent = null;
    ImageView	img_drilling_status = null;
    TextView	tv_main_pan_item_1 = null;
    TextView	tv_main_pan_item_2 = null;
    TextView	tv_main_pan_item_3 = null;
    TextView	tv_main_pan_item_4 = null;
    TextView	tv_main_pan_item_5 = null;

    TextView	tv_nav_name = null;
    TextView	tv_nav_state = null;

    int drillingDriveState = DrillingDriveStateActivity.STATE_DRILLING;
    int weatherCallCnt = 0;
    String weather_addr = "";

    @Override
    public void init() {
        weather_addr = getResources().getString(R.string.weather_addr);
        img_weather = (ImageView)findViewById(R.id.img_weather);
        tv_weather_state = (TextView)findViewById(R.id.tv_weather_state);
        tv_weather_subsi_do = (TextView)findViewById(R.id.tv_weather_subsi_do);
        tv_weather_percent = (TextView)findViewById(R.id.tv_weather_percent);
        img_drilling_status = (ImageView)findViewById(R.id.img_drilling_status);
        tv_main_pan_item_1 = (TextView)findViewById(R.id.tv_main_pan_item_1);
        tv_main_pan_item_2 = (TextView)findViewById(R.id.tv_main_pan_item_2);
        tv_main_pan_item_3 = (TextView)findViewById(R.id.tv_main_pan_item_3);
        tv_main_pan_item_4 = (TextView)findViewById(R.id.tv_main_pan_item_4);
        tv_main_pan_item_5 = (TextView)findViewById(R.id.tv_main_pan_item_5);

        setData();
    }

    @Override
    public void setData() {
        tv_nav_name.setText("이름:"+app.getName());
        tv_nav_state.setText("소속:"+app.getCname());
        pShow();
        startThread(new Runnable() {
            public void run() {
                api.getKepcoData(getBaseContext());
                api.getRecoData(getBaseContext(), 8);
                if(weather == null) weather = api.getWeatherInfo(getBaseContext(), weather_addr);
                weatherCallCnt = app.getWeatherCallCnt();
                if(weatherCallCnt >= WEATHER_CALL_BASE_CNT) {
                    weather = api.getWeatherInfo(getBaseContext(), weather_addr);
                    weatherCallCnt = 0;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pHide();
                        kepcoMonitorVO = app.getKepcoMonitor();
                        kepcoSensorVO = app.getKepcoSensor();
                        kepcoRecoDataVO = app.getKepcoRecoData();

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
                            tv_main_pan_item_1.setText(String.valueOf(kepcoMonitorVO.getValue5()));//value5;//총 인력
                            drillingDriveState = (int)kepcoMonitorVO.getValue4();//double value4;//굴진 상태
                            if(drillingDriveState == DrillingDriveStateActivity.STATE_DRILLING){
                                img_drilling_status.setImageResource(R.drawable.img_drilling_status_lv_0);
                            }else if(drillingDriveState == DrillingDriveStateActivity.STATE_CONSTRUCTION){
                                img_drilling_status.setImageResource(R.drawable.img_drilling_status_lv_1);
                            }else if(drillingDriveState == DrillingDriveStateActivity.STATE_CHECKING){
                                img_drilling_status.setImageResource(R.drawable.img_drilling_status_lv_2);
                            }else if(drillingDriveState == DrillingDriveStateActivity.STATE_PAUSE){
                                img_drilling_status.setImageResource(R.drawable.img_drilling_status_lv_3);
                            }else{
                                img_drilling_status.setImageResource(R.drawable.img_drilling_status_lv_4);
                            }

                        }
                        if(kepcoSensorVO != null){

                        }
                        if(weather != null){
                            int id = getResources().getIdentifier("icon_weather_"+weather.getToday_code(), "drawable", getBaseContext().getPackageName());
                            img_weather.setImageResource(id);
                            tv_weather_state.setText(weather.getToday_text());
                            tv_weather_subsi_do.setText(weather.getToday_temp());
                            tv_weather_percent.setText(weather.getHumidity());
                        }
                        if(kepcoRecoDataVO == null) kepcoRecoDataVO = new KepcoRecoDataVO();
                        tv_main_pan_item_2.setText(String.valueOf(kepcoRecoDataVO.getCountTotal()));//터널 내 총 인원
                        tv_main_pan_item_3.setText(String.valueOf(kepcoRecoDataVO.getCountWorker()));//터널 내 근로자
                        tv_main_pan_item_4.setText(String.valueOf(kepcoRecoDataVO.getCountManager()));//터널 내 관리자
                        tv_main_pan_item_5.setText(String.valueOf(kepcoRecoDataVO.getCountVip()));//외부 방문자


                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBasicUI();
        ((ImageButton)findViewById(R.id.btn_main_pan_item_1)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_pan_item_2)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_pan_item_3)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_pan_item_4)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_pan_item_5)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.img_drilling_status)).setOnClickListener(this);

        ((ImageButton)findViewById(R.id.btn_main_1)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_2)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_3)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_4)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_5)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_6)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_7)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_8)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.btn_main_9)).setOnClickListener(this);
        init();
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void initBasicUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowCustomEnabled(false);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "서비스 준비중 입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);
        tv_nav_name = (TextView)nav_header_view.findViewById(R.id.tv_nav_name);
        tv_nav_state = (TextView)nav_header_view.findViewById(R.id.tv_nav_state);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        doTheAutoRefresh();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null){
            handler.removeCallbacks(runnable);
        }
    }
    @Override
    public void onPause(){
        super.onPause();

        if(handler != null){
            handler.removeCallbacks(runnable);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent target = null;
        int id = item.getItemId();

        if (id == R.id.nav_direct_1) {
            target = new Intent(this, DataInputManagementActivity.class);
            target.putExtra("inputType", 5);//type 5 총인력
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();
        } else if (id == R.id.nav_direct_11) {
            target = new Intent(this, VipBeaconManageActivity.class);
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();
        } else if (id == R.id.nav_direct_2) {
            target = new Intent(this, DataInputDrillingActivity.class);
            target.putExtra("inputType", 1);//type 1 굴진량
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();
        } else if (id == R.id.nav_direct_3) {
            target = new Intent(this, DrillingDriveStateActivity.class);
            target.putExtra("inputType", 4);//type 4 굴진상태
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();

        } else if (id == R.id.nav_direct_4) {
            target = new Intent(this, DataInputManagementActivity.class);
            target.putExtra("inputType", 2);//type 2 막장압력
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();

        } else if (id == R.id.nav_direct_5) {
            target = new Intent(this, DataInputManagementActivity.class);
            target.putExtra("inputType", 1);//type 1 염분농도
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();
        } else if (id == R.id.nav_direct_6) {
            target = new Intent(this, DataInputManagementActivity.class);
            target.putExtra("inputType", 3);//type 3 오탁수 처리량
            target.putExtra("comeFrom", "home");
            startActivity(target);
            finish();
        //} else if (id == R.id.nav_direct_7) {

        } else if (id == R.id.nav_direct_8) {
            callcenter();
        } else if (id == R.id.nav_direct_9) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent target = null;
        switch(v.getId()){
            case R.id.img_drilling_status:
                target = new Intent(this, DrillingDriveStateActivity.class);
                target.putExtra("inputType", 4);//type 4 굴진상태
                target.putExtra("comeFrom", "home");
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_pan_item_1:
                showToast("서비스 준비중 입니다.");
                break;
            case R.id.btn_main_pan_item_2:
                //showToast("서비스 준비중 입니다.");
                target = new Intent(this, MainTopPersonListActivity.class);
                target.putExtra("persionCnt", kepcoRecoDataVO.getCountTotal());//터널 내 총 인원
                target.putExtra("kind", "kind_2");
                startActivity(target);
                break;
            case R.id.btn_main_pan_item_3:
                //showToast("서비스 준비중 입니다.");
                target = new Intent(this, MainTopPersonListActivity.class);
                target.putExtra("persionCnt", kepcoRecoDataVO.getCountWorker());//터널 내 근로자
                target.putExtra("kind", "kind_3");
                startActivity(target);
                break;
            case R.id.btn_main_pan_item_4:
                //showToast("서비스 준비중 입니다.");
                target = new Intent(this, MainTopPersonListActivity.class);
                target.putExtra("persionCnt", kepcoRecoDataVO.getCountManager());//터널 내 관리자
                target.putExtra("kind", "kind_4");
                startActivity(target);
                break;
            case R.id.btn_main_pan_item_5:
                //showToast("서비스 준비중 입니다.");
                target = new Intent(this, MainTopPersonListActivity.class);
                target.putExtra("persionCnt", kepcoRecoDataVO.getCountVip());//외부 방문자
                target.putExtra("kind", "kind_5");
                startActivity(target);
                break;
            case R.id.btn_main_1:
                target = new Intent(this, DrillingStatusActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_2:
                target = new Intent(this, ContingencyPlanActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_3:
                target = new Intent(this, TotalDrillingMapActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_4:

                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.fnsys.mprms");
                if( intent != null  ) {
                    startActivity(intent);
                } else {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("cctv앱설치")
                            .setMessage("cctv 앱이 필요합니다.\n설치 하시겠습니까?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    Uri u = Uri.parse("https://play.google.com/store/apps/details?id=com.fnsys.mprms");
                                    i.setData(u);
                                    startActivity(i);
                                }})
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }}).show();

                }
                break;
            case R.id.btn_main_5:
                target = new Intent(this, LocationInTunnelActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_6:
                target = new Intent(this, WorkingEnvironmentActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_7:
                //target = new Intent(this, RegPersonListActivity.class);
                target = new Intent(this, WorkforceManagementActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_8:
                //target = new Intent(this, BeaconManageActivity.class);
                target = new Intent(this, NumericalManagementActivity.class);
                startActivity(target);
                finish();
                break;
            case R.id.btn_main_9:
                mCDialog = new CDialogAlertSos(this, null , sosOkClickListener , null);
                mCDialog.show();
                break;
        }
    }

    public void callcenter() {
        String telphone = "070-7574-1728";
        // TODO Auto-generated method stub
        Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
        phoneCallIntent.setData(Uri.parse("tel:"+telphone));
        startActivity(phoneCallIntent);
    }

    public void logout(){

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        app.setLogin(false);
                        app.setAutoLogin(false);
                        app.setId("");
                        app.setType("");
                        app.setCont_id("");
                        app.setSite_id("");
                        app.setName("");
                        app.setRtype("");
                        app.setPhone("");

                        showToast("로그아웃 되었습니다.");

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }})
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }}).show();
    }

    private final Handler handler = new Handler();
    Runnable runnable = null;
    static int WEATHER_CALL_BASE_CNT = 60; //1시간에 한번씩 호출

    private void doTheAutoRefresh() {
        runnable=new Runnable() {
            @Override
            public void run() {
                setData();
                Log.e("doTheAutoRefresh","doTheAutoRefresh ================weatherCallCnt = "+weatherCallCnt);
                //app.setKepcoData(null, null);

                doTheAutoRefresh();

                app.setWeatherCallCnt(weatherCallCnt);
                weatherCallCnt++;
            }
        };
        handler.postDelayed(runnable, 1000*60);
    }

    CDialogAlertSos mCDialog = null;
/*    private View.OnClickListener alimiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast("서비스 준비중 입니다.");

            mCDialog.dismiss();
        }
    };
    private View.OnClickListener missionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast("서비스 준비중 입니다.");
            mCDialog.dismiss();
        }
    };*/
    private View.OnClickListener sosOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //showToast("서비스 준비중 입니다.");

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("응급구조요청")
                    .setMessage("응급 구조 비상 전화를 사용하시겠습니까?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("요청", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //warningpush(app.getSite_id(), app.getCont_id(), app.getName(), app.getPhone(), app.getId());
                        }})
                    .setNegativeButton("취소", null).show();
            mCDialog.dismiss();
        }
    };

    public void warningpush(final String site_id, final String cont_id, final String name, final String phone, final String id) {
        startThread(new Runnable() {
            public void run() {
                api.waringPush(getBaseContext(), site_id, cont_id,name,phone, id,"1");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "응급 구조 요청을 하였습니다.", Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }

}

