package kr.gsil.dpkepco.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.dialog.CDialogInputData;
import kr.gsil.dpkepco.model.DailyValueVO;
import kr.gsil.dpkepco.model.TimelyValueVO;
import kr.gsil.dpkepco.util.ListViewAdapter;

public class DataInputDrillingActivity extends BaseActivity implements View.OnClickListener{
    public DataInputDrillingActivity activity = null;

    Button btn_current_date  = null;
    Button btn_input_data  = null;
    ListView lv_data_list  = null;
    ArrayList<DailyValueVO> list  = new ArrayList<DailyValueVO>();
    MainListViewAdapter rAdapter;

    private CDialogInputData mCDialog = null;
    int inputType = 1;
    String comeFrom = "page";
    Intent target = null;
    @Override
    public void init() {
        btn_current_date  = (Button)findViewById(R.id.btn_current_date);
        btn_input_data  = (Button)findViewById(R.id.btn_input_data);
        lv_data_list  = (ListView)findViewById(R.id.lv_data_list);
        rAdapter = new MainListViewAdapter();
        lv_data_list.setAdapter(rAdapter);

        btn_current_date.setOnClickListener(this);
        btn_input_data.setOnClickListener(this);
        setData();
    }

    @Override
    public void setData() {

        list = null;
        if( list == null ) list = new ArrayList<DailyValueVO>();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
        Date currentTime = new Date ( );
        final String today = mSimpleDateFormat.format ( currentTime );
        btn_current_date.setText(today);
        pShow();
        startThread(new Runnable() {
            public void run() {
                list = api.getDailyValueList(getBaseContext());
                if( list == null ) list = new ArrayList<DailyValueVO>();
                runOnUiThread(new Runnable() {
                    public void run() {
                        pHide();
                        if( list != null && list.size() > 0 ) {
                            rAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Toast.makeText(getApplicationContext(), "왼쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();*/
            if(mCDialog.getId() > -1){
                pShow();
                startThread(new Runnable() {

                    public void run() {

                        final String result = api.deleteDailyValue(getBaseContext(),String.valueOf(mCDialog.getId()));
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pHide();
                                if( result != null && result != "" ) {
                                    setData();
                                }
                            }
                        });
                    }
                });
            }
            mCDialog.dismiss();
        }
    };

    private View.OnClickListener centerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Toast.makeText(getApplicationContext(), "가운데버튼 Click!!",
                    Toast.LENGTH_SHORT).show();*/
            if(mCDialog.isNew()){
                pShow();
                startThread(new Runnable() {

                    public void run() {
                        String dateStr = btn_current_date.getText().toString();
                        final String result = api.insertDailyValue(getBaseContext(),dateStr
                                , String.valueOf((int)mCDialog.getData()), app.getId());
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pHide();
                                if( result != null && result != "" ) {
                                    setData();
                                }
                            }
                        });
                    }
                });
            }else{
                pShow();
                startThread(new Runnable() {

                    public void run() {
                        final String result = api.updateDailyValue(getBaseContext(),String.valueOf(mCDialog.getId())
                                ,String.valueOf((int)mCDialog.getData()));
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pHide();
                                if( result != null && result != "" ) {
                                    setData();
                                }
                            }
                        });
                    }
                });
            }
            mCDialog.dismiss();
        }
    };
    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Toast.makeText(getApplicationContext(), "오른쪽버튼 Click!!",
                    Toast.LENGTH_SHORT).show();*/
            mCDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input_drilling);
        activity = this;
        setToolbar();
        init();
        Intent intent = getIntent();
        inputType = intent.getIntExtra("inputType", 1);
        comeFrom = intent.getStringExtra("comeFrom");
        //Log.e("onCreate","inputType = "+inputType+" comeFrom = "+comeFrom);
        if(comeFrom.equals("home")){
            target = new Intent(activity, MainActivity.class);
        }else {
            target = new Intent(activity, NumericalManagementActivity.class);
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
                startActivity(target);
                finish();
            }
        });
        title.setText(getString(R.string.page_8_btxt_1));
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }

    private void DialogFirstDatePicker(){
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String rmonth = "";
                        String rday = "";
                        if( (monthOfYear+1) < 10 ) {
                            rmonth = "0" + String.valueOf(monthOfYear+1);
                        } else {
                            rmonth = String.valueOf(monthOfYear+1);
                        }

                        if( (dayOfMonth) < 10 ) {
                            rday = "0" + String.valueOf(dayOfMonth);
                        } else {
                            rday = String.valueOf(dayOfMonth);
                        }


                        btn_current_date.setText(String.valueOf(year)+"-"+rmonth+"-"+rday);
                    }
                };

        DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,
                cyear, cmonth, cday);
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_current_date:
                DialogFirstDatePicker();
                break;
            case R.id.btn_input_data:
                /*mCDialog = new CDialogInputData(this, true, R.string.page_8_btxt_1, btn_current_date.getText().toString(), (float)0.0, R.string.page_popup_input_txt_1,
                        null, centerClickListener, rightClickListener, -1);
                mCDialog.show();*/
                openPopup(btn_current_date.getText().toString(), (float)0.0, true, -1 );
                break;
        }
    }
    private void openPopup(String date, float value, boolean isNew, int id){
        int titleId = R.string.page_8_btxt_1;

        mCDialog = new CDialogInputData(this, isNew, titleId, date, value, (isNew)?R.string.page_popup_input_txt_1: R.string.page_popup_input_txt_2,
                (isNew)?null:leftClickListener, centerClickListener, rightClickListener, id);
        mCDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(target);
        finish();
    }

    class MainListViewAdapter extends ListViewAdapter {
        ViewHolder holder = null;
        @Override
        public View setListGetView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_insert_two_line, null);
            }
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_today_value = (TextView) convertView.findViewById(R.id.tv_today_value);
            holder.tv_total_value = (TextView) convertView.findViewById(R.id.tv_total_value);
            holder.tv_av_today_value = (TextView) convertView.findViewById(R.id.tv_av_today_value);
            holder.tv_av_month_value = (TextView) convertView.findViewById(R.id.tv_av_month_value);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.btn_motify = (Button) convertView.findViewById(R.id.btn_motify);
            if( list != null && list.size() > 0 ) {
                holder.tv_date.setText(list.get(position).getInput_date().toString());
                holder.tv_today_value.setText(String.valueOf((int)list.get(position).getValue()));
                holder.tv_total_value.setText(String.valueOf((int)list.get(position).getSum_value()));
                holder.tv_av_today_value.setText(String.valueOf((int)list.get(position).getAvg_value()));
                holder.tv_av_month_value.setText(String.valueOf(0));//(int)list.get(position).getAvg_value()
                holder.tv_name.setText(list.get(position).getWriter_user_name().toString());
                //holder.tv_date.setText(list.get(position).getWrite_date().toString());
                holder.btn_motify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!list.isEmpty()) {
                            openPopup(list.get(position).getInput_date(),  (float) list.get(position).getValue(), false, list.get(position).getId() );
                        }
                    }
                });
            }
            return convertView;
        }

        @Override
        public int setCount() {
            return (list == null)? 0 : list.size();
        }
    }

    private class ViewHolder{
        public TextView tv_date = null;
        public TextView tv_today_value = null;
        public TextView tv_total_value = null;
        public TextView tv_av_today_value = null;
        public TextView tv_av_month_value = null;
        public TextView tv_name = null;
        public Button btn_motify = null;
    }
}
