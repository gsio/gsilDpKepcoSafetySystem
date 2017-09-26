package kr.gsil.dpkepco.activity.worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.activity.MainActivity;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.util.ListViewAdapter;

/**
 * Created by gsil on 2017. 9. 25..
 */

public class VipBeaconManageActivity extends BaseActivity {
    public VipBeaconManageActivity activity = null;
    Intent target = null;
    ListView listview = null;
    MainListViewAdapter rAdapter;
    ArrayList<MobileUserVO> list  = new ArrayList<MobileUserVO>();
    ArrayList<MobileUserVO> templist  = new ArrayList<MobileUserVO>();

    String comeFrom = "page";
    String contName = "";
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
                list = api.getBeaconManagerList(getBaseContext(), "", app.getSite_id(), contName);
                if( list == null ) list = new ArrayList<MobileUserVO>();
                runOnUiThread(new Runnable() {
                    public void run() {
                        pHide();
                        if( list != null && list.size() > 0 ) {
                            for( int i = 0; i < list.size(); i++ ) {
                                MobileUserVO m = (MobileUserVO)list.get(i);
                                templist.add(m);
                            }

                            app.setBeaconlist(list);
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
        setContentView(R.layout.activity_vip_beacon_manage);

        activity = this;
        Intent intent = getIntent();
        comeFrom = intent.getStringExtra("comeFrom");
        if(comeFrom.equals("home")){
            target = new Intent(activity, MainActivity.class);
        }else {
            target = new Intent(activity, PersonMenuActivity.class);
        }
        setToolbar(target);
        init();
    }

    private void setToolbar(final Intent target){
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
                startActivity(target);
                finish();
            }
        });
        title.setText(getString(R.string.worker_mpage_btxt_3));
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
        startActivity(target);
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
            if( list != null && list.size() > 0 ) {

                listTitleText.setText("비콘 번호 : " + list.get(position).getId());
                String infor = "";

                if( list.get(position).getName().equals("") ) {
                    subTitleText.setVisibility(View.GONE);
                } else {
                    subTitleText.setVisibility(View.VISIBLE);
                    infor = "배정자 : " + list.get(position).getName() + " / " + list.get(position).getCname();
                    subTitleText.setText(infor);
                }

                if( list.get(position).getGubun().equals("1") ) {
                    statusTitleText.setText("관리자");
                    statusTitleText.setTextColor(Color.BLUE);
                } else if( list.get(position).getGubun().equals("2") ) {
                    statusTitleText.setText("근로자");
                    statusTitleText.setTextColor(Color.BLACK);
                } else {
                    statusTitleText.setText("미배정");
                    statusTitleText.setTextColor(Color.RED);
                }

                if( list.get(position).getUseyn().equals("N") ) {
                    statusTitleText.setText("퇴사자");
                    statusTitleText.setTextColor(Color.RED);
                }
            }

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    if( !list.get(position).getName().equals("")  ) {
                        new AlertDialog.Builder(VipBeaconManageActivity.this)
                                .setTitle("비콘배정자 초기화")
                                .setMessage(  list.get(position).getName().toString()+" 배정자를 초기화하시겠습니까?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("초기화", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        eventUpdateBeaconManager( list.get(position).getId());
                                    }})
                                .setNegativeButton("취소", null).show();
                    }

                    return false;
                }
            });


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!list.isEmpty() && !list.get(position).getName().equals("") ) {
                        //
                        showToast("선택 되었습니다.");
                    }
                }
            });

            return convertView;
        }

        @Override
        public int setCount() {
            return list.size();
        }

    }

    private void eventUpdateBeaconManager( final String id) {
        if( app.getName().equals("GSIL") ) {
            return;
        }
        pShow();
        startThread(new Runnable() {
            public void run() {
                final String result = "";//api.updateBeaconManager(getBaseContext(), id, "", "");
                runOnUiThread(new Runnable() {
                    public void run() {
                        pHide();
                        if( result != null && !result.equals("") ) {
                            showToast("정상 초기화 되었습니다.");
                            setData();
                        } else {
                            rAdapter.notifyDataSetChanged();
                        }
                        //
                    }
                });
            }
        });
    }
}
