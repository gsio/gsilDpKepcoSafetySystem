package kr.gsil.dpkepco.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.activity.worker.VipBeaconManageActivity;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.util.ListViewAdapter;

public class MainTopPersonListActivity extends BaseActivity {
    public MainTopPersonListActivity activity = null;
    ListView listview = null;
    MainListViewAdapter rAdapter;
    ArrayList<MobileUserVO> list  = new ArrayList<MobileUserVO>();
    ArrayList<MobileUserVO> templist  = new ArrayList<MobileUserVO>();
    int persionCnt = 0;
    String kind = "";
    String contName = "";
    int titleId = -1;
    @Override
    public void init() {
        listview= (ListView) findViewById(R.id.personlist);
        rAdapter = new MainListViewAdapter();
        listview.setAdapter(rAdapter);

        contName = app.getCont_id();
        setData();
    }

    @Override
    public void setData() {
        pShow();
        startThread(new Runnable() {
            public void run() {
                list = api.getRecoDataAll(getBaseContext(), Integer.parseInt(app.getSite_id()));
                if( list == null ) list = new ArrayList<MobileUserVO>();
                runOnUiThread(new Runnable() {
                    public void run() {
                        pHide();
                        if( list != null && list.size() > 0 ) {
                            for( int i = 0; i < list.size(); i++ ) {
                                MobileUserVO m = (MobileUserVO)list.get(i);

                        /*터널내 근로자 : t_gubun이 1,2인것(1:근로자,2;장비)
					터널내 관리자 : t_gubun이 3,4,5 인 것 count(3:관리자,4:감리단,5:발주처)
					터널내 외부방문자 : t_gubun이 6인것 count	*/
                                switch(titleId){
                                    case R.string.page_main_top_txt_1:
                                        break;
                                    case R.string.page_main_top_txt_2:
                                        templist.add(m);
                                        break;
                                    case R.string.page_main_top_txt_3:
                                        if(m.getT_gubun().equals("1") || m.getT_gubun().equals("2")) templist.add(m);
                                        break;
                                    case R.string.page_main_top_txt_4:
                                        if(m.getT_gubun().equals("3") || m.getT_gubun().equals("4") || m.getT_gubun().equals("5")) templist.add(m);
                                        break;
                                    case R.string.page_main_top_txt_5:
                                        if(m.getT_gubun().equals("6")) templist.add(m);
                                        break;
                                }

                            }

                            //app.setBeaconlist(list);
                        }
                        rAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_top_person_list);
        activity = this;
        Intent intent = getIntent();
        persionCnt = intent.getIntExtra("persionCnt", 0);
        kind = intent.getStringExtra("kind");

        if(kind.equals("kind_1")) {
            titleId = R.string.page_main_top_txt_1;
        }else if(kind.equals("kind_2")){
            titleId = R.string.page_main_top_txt_2;
        }else if(kind.equals("kind_3")){
            titleId = R.string.page_main_top_txt_3;
        }else if(kind.equals("kind_4")){
            titleId = R.string.page_main_top_txt_4;
        }else if(kind.equals("kind_5")){
            titleId = R.string.page_main_top_txt_5;
        }
        setToolbar(titleId, persionCnt);
        init();
    }

    private void setToolbar(final int titleId, final int cnt){
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
                //Intent target = new Intent(activity, MainActivity.class);
                //startActivity(target);
                finish();
            }
        });
        Resources res = getResources();
        String text = String.format(res.getString(titleId), cnt);
        title.setText(text);
        //title.setText(getString(R.string.worker_mpage_btxt_3));
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent target = new Intent(activity, MainActivity.class);
        //startActivity(target);
        finish();
    }

    class MainListViewAdapter extends ListViewAdapter {

        @Override
        public View setListGetView(final int position, View convertView,
                                   ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_person_list,
                        null);
            }


            TextView listTitleText = (TextView) convertView
                    .findViewById(R.id.wname);

            TextView subTitleText = (TextView) convertView
                    .findViewById(R.id.infoname);

            TextView statusTitleText = (TextView) convertView
                    .findViewById(R.id.statusname);
            if( templist != null && templist.size() > 0 ) {

                String name = "이름 : " + templist.get(position).getName();
                if(templist.get(position).getBtype() != null && !templist.get(position).getBtype().equals("")) name += " / " + templist.get(position).getBtype()+"형";
                if(templist.get(position).getAge() != null && !templist.get(position).getAge().equals("") && !templist.get(position).getAge().equals("0")) name += " (" + templist.get(position).getAge()+"세)";
                listTitleText.setText(name);
                String infor = "";

                if( templist.get(position).getT_name().equals("") ) {
                    subTitleText.setVisibility(View.GONE);
                } else {
                    subTitleText.setVisibility(View.VISIBLE);
                    infor = "직종 : " + templist.get(position).getT_name() + " / 회사 : " + list.get(position).getCont_name();
                    subTitleText.setText(infor);
                }
                    /*터널내 근로자 : t_gubun이 1,2인것(1:근로자,2;장비)
					터널내 관리자 : t_gubun이 3,4,5 인 것 count(3:관리자,4:감리단,5:발주처)
					터널내 외부방문자 : t_gubun이 6인것 count	*/
                if( templist.get(position).getT_gubun().equals("1")) {
                    statusTitleText.setText("근로자");
                    statusTitleText.setTextColor(Color.BLACK);
                } else if( templist.get(position).getT_gubun().equals("2") ) {
                    statusTitleText.setText("근로자(장비)");
                    statusTitleText.setTextColor(Color.BLACK);
                } else if( templist.get(position).getT_gubun().equals("3") ) {
                    statusTitleText.setText("관리자");
                    statusTitleText.setTextColor(Color.BLUE);
                } else if( templist.get(position).getT_gubun().equals("4") ) {
                    statusTitleText.setText("감리단");
                    statusTitleText.setTextColor(Color.BLUE);
                } else if( templist.get(position).getT_gubun().equals("5") ) {
                    statusTitleText.setText("발주처");
                    statusTitleText.setTextColor(Color.BLUE);
                } else if( templist.get(position).getT_gubun().equals("6") ) {
                    statusTitleText.setText("외부방문자");
                    statusTitleText.setTextColor(Color.BLUE);
                } else {
                    statusTitleText.setText("미배정");
                    statusTitleText.setTextColor(Color.RED);
                }

            }

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
/*                    if( !templist.get(position).getName().equals("")  ) {
                        new AlertDialog.Builder(VipBeaconManageActivity.this)
                                .setTitle("비콘배정자 초기화")
                                .setMessage(  templist.get(position).getName().toString()+" 배정자를 초기화하시겠습니까?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("초기화", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        eventUpdateBeaconManager( templist.get(position).getId());
                                    }})
                                .setNegativeButton("취소", null).show();
                    }*/

                    return false;
                }
            });


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!templist.isEmpty() && !templist.get(position).getName().equals("") ) {
                        //
                        showToast("선택 되었습니다.");
                    }
                }
            });

            return convertView;
        }

        @Override
        public int setCount() {
            return templist.size();
        }

    }
}
