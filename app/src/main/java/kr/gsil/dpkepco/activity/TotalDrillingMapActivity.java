package kr.gsil.dpkepco.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.KepcoMonitorVO;
import kr.gsil.dpkepco.model.KepcoSensorVO;

public class TotalDrillingMapActivity extends BaseActivity{

    public TotalDrillingMapActivity activity = null;
    KepcoMonitorVO kepcoMonitorVO = null;
    KepcoSensorVO kepcoSensorVO = null;
    private Integer[] mapImgData = {
        R.drawable.bg_drilling_map_01,
        R.drawable.bg_drilling_map_02,
        R.drawable.bg_drilling_map_03,
        R.drawable.bg_drilling_map_04,
        R.drawable.bg_drilling_map_05,
        R.drawable.bg_drilling_map_06,
        R.drawable.bg_drilling_map_07,
        R.drawable.bg_drilling_map_08,
        R.drawable.bg_drilling_map_09,
        R.drawable.bg_drilling_map_10,
        R.drawable.bg_drilling_map_11,
        R.drawable.bg_drilling_map_12,
        R.drawable.bg_drilling_map_13,
        R.drawable.bg_drilling_map_14,
        R.drawable.bg_drilling_map_15,
        R.drawable.bg_drilling_map_16,
        R.drawable.bg_drilling_map_17,
        R.drawable.bg_drilling_map_18,
        R.drawable.bg_drilling_map_19,
        R.drawable.bg_drilling_map_20,
        R.drawable.bg_drilling_map_21,
        R.drawable.bg_drilling_map_22,
        R.drawable.bg_drilling_map_23,
        R.drawable.bg_drilling_map_24,
        R.drawable.bg_drilling_map_25,
        R.drawable.bg_drilling_map_26,
        R.drawable.bg_drilling_map_27,
        R.drawable.bg_drilling_map_28,
        R.drawable.bg_drilling_map_29,
        R.drawable.bg_drilling_map_30,
        R.drawable.bg_drilling_map_31,
        R.drawable.bg_drilling_map_32,
        R.drawable.bg_drilling_map_33,
        R.drawable.bg_drilling_map_34,
        R.drawable.bg_drilling_map_35,
        R.drawable.bg_drilling_map_36,
        R.drawable.bg_drilling_map_37,
        R.drawable.bg_drilling_map_38,
        R.drawable.bg_drilling_map_39,
        R.drawable.bg_drilling_map_40,
        R.drawable.bg_drilling_map_41,
        R.drawable.bg_drilling_map_42,
        R.drawable.bg_drilling_map_43,
        R.drawable.bg_drilling_map_44,
        R.drawable.bg_drilling_map_45,
        R.drawable.bg_drilling_map_46,
        R.drawable.bg_drilling_map_47,
        R.drawable.bg_drilling_map_48,
        R.drawable.bg_drilling_map_49,
        R.drawable.bg_drilling_map_50,
        R.drawable.bg_drilling_map_51,
        R.drawable.bg_drilling_map_52,
        R.drawable.bg_drilling_map_53,
        R.drawable.bg_drilling_map_54,
        R.drawable.bg_drilling_map_55,
        R.drawable.bg_drilling_map_56,
        R.drawable.bg_drilling_map_57,
        R.drawable.bg_drilling_map_58,
        R.drawable.bg_drilling_map_59,
        R.drawable.bg_drilling_map_60,
        R.drawable.bg_drilling_map_61,
        R.drawable.bg_drilling_map_62,
        R.drawable.bg_drilling_map_63,
        R.drawable.bg_drilling_map_64,
        R.drawable.bg_drilling_map_65,
        R.drawable.bg_drilling_map_66,
        R.drawable.bg_drilling_map_67,
        R.drawable.bg_drilling_map_68,
        R.drawable.bg_drilling_map_69,
        R.drawable.bg_drilling_map_70,
        R.drawable.bg_drilling_map_71,
        R.drawable.bg_drilling_map_72,
        R.drawable.bg_drilling_map_73,
        R.drawable.bg_drilling_map_74,
        R.drawable.bg_drilling_map_75,
        R.drawable.bg_drilling_map_76,
        R.drawable.bg_drilling_map_77,
        R.drawable.bg_drilling_map_78,
        R.drawable.bg_drilling_map_79,
        R.drawable.bg_drilling_map_80,
        R.drawable.bg_drilling_map_81,
        R.drawable.bg_drilling_map_82,
        R.drawable.bg_drilling_map_83,
        R.drawable.bg_drilling_map_84,
        R.drawable.bg_drilling_map_85,
        R.drawable.bg_drilling_map_86,
        R.drawable.bg_drilling_map_87,
        R.drawable.bg_drilling_map_88,
        R.drawable.bg_drilling_map_89,
        R.drawable.bg_drilling_map_90,
        R.drawable.bg_drilling_map_91,
        R.drawable.bg_drilling_map_92,
        R.drawable.bg_drilling_map_93,
        R.drawable.bg_drilling_map_94,
        R.drawable.bg_drilling_map_95,
        R.drawable.bg_drilling_map_96,
        R.drawable.bg_drilling_map_97,
        R.drawable.bg_drilling_map_98,
        R.drawable.bg_drilling_map_99,
        R.drawable.bg_drilling_map_100,
        R.drawable.bg_drilling_map_101,
        R.drawable.bg_drilling_map_102,
        R.drawable.bg_drilling_map_103,
        R.drawable.bg_drilling_map_104,
        R.drawable.bg_drilling_map_105,
        R.drawable.bg_drilling_map_106,
        R.drawable.bg_drilling_map_107,
        R.drawable.bg_drilling_map_108,
        R.drawable.bg_drilling_map_109,
        R.drawable.bg_drilling_map_110,
        R.drawable.bg_drilling_map_111,
        R.drawable.bg_drilling_map_112
    };

    private Integer[] aniImgCircle = {
        R.drawable.img_ani_pc_0,
        R.drawable.img_ani_pc_1,
        R.drawable.img_ani_pc_2,
        R.drawable.img_ani_pc_3,
        R.drawable.img_ani_pc_4,
        R.drawable.img_ani_pc_5,
        R.drawable.img_ani_pc_6,
        R.drawable.img_ani_pc_7,
        R.drawable.img_ani_pc_8,
        R.drawable.img_ani_pc_9,
        R.drawable.img_ani_pc_10,
        R.drawable.img_ani_pc_11
    };
    kr.gsil.dpkepco.util.DrillingMapListView listview = null;
    ScrollView scrollView = null;
    MapSliceAdapter mAdapter = null;
    public ImageView aniImageView = null;
    public int drillingMetter = 1;

    @Override
    public void init() {
        scrollView = (ScrollView)findViewById(R.id.sv_map_pan);
        listview = (kr.gsil.dpkepco.util.DrillingMapListView) findViewById(R.id.lv_map_slice);
        setData();
    }

    @Override
    public void setData() {

        mAdapter = new MapSliceAdapter(this, mapImgData);
        listview.setAdapter(mAdapter);
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
            drillingMetter = (int)kepcoMonitorVO.getTotal_meter();//double total_meter;//누계굴진
        }
        if(kepcoSensorVO != null){

        }
        setDrillingMatter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_drilling_map);
        activity = this;
        setToolbar();
        init();

    }

    private int getTotalHeightofListView() {

        ListAdapter LvAdapter = listview.getAdapter();
        int listviewElementsheight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listview);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listviewElementsheight += mView.getMeasuredHeight();
            break;
        }
        return listviewElementsheight;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setDrillingMatter();

        //if(sv_map_pan != null) sv_map_pan.scrollTo(0, currentPosition*40);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent target = new Intent(activity, MainActivity.class);
        startActivity(target);
        finish();
    }

    private void setDrillingMatter(){
        if(scrollView != null){
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            final int height = display.getHeight();
            //Log.e("onResume","width = "+width + " height = "+height);

            listview.setPositionInfo(drillingMetter, (float)width/1080);
            //scrollView.scrollTo(0, scrollView.getBottom());
            scrollView.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    if(drillingMetter > 0) scrollView.scrollTo(0, drillingMetter - height/2);
                }
            });
        }
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }
    private void startAnimation(){

        aniImageView.setImageResource(R.drawable.fram_animation_drilling_position);

        AnimationDrawable imgAnimation = (AnimationDrawable) aniImageView.getDrawable();

        if (imgAnimation .isRunning()) {
            imgAnimation .stop();
        }
        imgAnimation .start();

    }

    public class MapSliceAdapter extends ArrayAdapter<Integer> implements SectionIndexer {
        private Context context;
        private Integer[] values;
        String[] sections ;
        HashMap<String, Integer> positionIndexer;

        public MapSliceAdapter(Context context, Integer[] values) {
            super(context, R.layout.item_map_slice, values);
            this.context = context;
            this.values = values;
            positionIndexer = new HashMap<String, Integer>();
            int size = values.length;

            for (int x = 0; x < size; x++) {
                positionIndexer.put(String.valueOf(x), values[x]);
            }
            Set<String> sectionLetters = positionIndexer.keySet();
            ArrayList<String> sectionList = new ArrayList<>(sectionLetters);
            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.item_map_slice, parent, false);
            com.ssomai.android.scalablelayout.ScalableLayout sl = (com.ssomai.android.scalablelayout.ScalableLayout) rowView.findViewById(R.id.slMain);
            sl.setBackgroundResource(values[position]);
            return rowView;
        }

        @Override
        public Object[] getSections() {
            return sections;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            return positionIndexer.get(sections[sectionIndex]);
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
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
        title.setText(getString(R.string.page_title_03));
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }
}
