package kr.gsil.dpkepco.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileEquipVO;
import kr.gsil.dpkepco.model.MobileWorkerVO;
import kr.gsil.dpkepco.util.ListViewAdapter;
import kr.gsil.dpkepco.util.SoundSearcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
public class RegPersonListActivity extends BaseActivity {

	Button nextIcon = null;
	
	RelativeLayout menuBar = null;
	ImageView backIcon = null;
	TextView titleText = null;
	
	EditText searchnameBox = null;
	Button searchBtn = null;
	
	ArrayList<MobileWorkerVO> templist = new ArrayList<MobileWorkerVO>();
	
	ArrayList<MobileWorkerVO> list  = new ArrayList<MobileWorkerVO>();
	MainListViewAdapter rAdapter;
	ListView listview = null;
	
	int f = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_person_list);
		app.setPact(this);
		app.setReflash(true);
		listview= (ListView) findViewById(R.id.personlist);
		rAdapter = new MainListViewAdapter();
		listview.setAdapter(rAdapter);
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		

		searchnameBox = (EditText) findViewById(R.id.searchnameBox);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		
		backIcon = ( ImageView ) findViewById(R.id.backIcon);
		backIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		titleText = ( TextView ) findViewById(R.id.titleText);
		titleText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		menuBar = ( RelativeLayout ) findViewById(R.id.menuBar);
		menuBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		nextIcon = ( Button ) findViewById(R.id.nextIcon);
		nextIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.setMv(null);
				Intent intent = new Intent(getApplicationContext(), RegPersonActivity.class);
				startActivity(intent);
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchWordList();
			}
		});
		
		setData();
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
		pShow();
		startThread(new Runnable() {
			public void run() {
				list = api.workerList(getBaseContext(), "",app.getSite_id(), app.getCont_id(), app.getType(),"","Y","","");
				if( list == null ) list = new ArrayList<MobileWorkerVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( list != null && list.size() > 0 ) {
							setMirrorList();
							rAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		});
	}
	
	public boolean getSearchEvent( final String searchname ) {
		// TODO Auto-generated method stub
		pShow();
		startThread(new Runnable() {
			public void run() {
				list = api.workerList(getBaseContext(), searchname,app.getSite_id(), app.getCont_id(), app.getType(),"","Y","","");
				if( list == null ) list = new ArrayList<MobileWorkerVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( list != null && list.size() > 0 ) {
							setMirrorList();
							rAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		});
		return true;
	}
	
	
	public void setMirrorList() {
		templist = null;
		templist = new ArrayList<MobileWorkerVO>();
		if( list != null && list.size() > 0 ) {
			for ( int i = 0; i < list.size(); i ++ ) {
				MobileWorkerVO fm = ( MobileWorkerVO ) list.get(i);
				templist.add(fm);
			}
		}
		
	    if( !searchnameBox.getText().toString().equals("") ) {
	    	searchWordList();
	    }
		
	}
	
	public void searchWordList() {
		
		ArrayList<MobileWorkerVO> searchList = new  ArrayList<MobileWorkerVO>();
		
		if( templist != null && templist.size() > 0 ) {
			for ( int i = 0; i < templist.size(); i ++ ) {
				MobileWorkerVO fm = ( MobileWorkerVO ) templist.get(i);
				if( SoundSearcher.matchString(fm.getName(), searchnameBox.getText().toString()) 
					|| SoundSearcher.matchString(fm.getPhone(), searchnameBox.getText().toString())) {
					searchList.add(fm);
				}
			}
		}
		
		if( searchList != null && searchList.size() > 0 ) {
			list = null;
			list = new  ArrayList<MobileWorkerVO>();
			for ( int i = 0; i < searchList.size(); i ++ ) {
				MobileWorkerVO fm = ( MobileWorkerVO ) searchList.get(i);
				list.add(fm);
			}
			rAdapter.notifyDataSetChanged();
		} else {
			
			list = null;
			list = new  ArrayList<MobileWorkerVO>();
			list = templist;
			rAdapter.notifyDataSetChanged();
			
		}
		
	}
	
	private void eventUpdateWorkerDel( final MobileWorkerVO m , final String delyn) {
		if( app.getName().equals("GSIL") ) {
			return;
		}
		startThread(new Runnable() {
			public void run() {
				final String result = api.updateWorkerDel(getBaseContext(), m.getId(), delyn, "");
				runOnUiThread(new Runnable() {
					public void run() {
						
						if( result != null && !result.equals("") ) {
								showToast("정상 삭제 되었습니다.");
								setData();
						} else {
							pHide();
							rAdapter.notifyDataSetChanged();
						}
						//
					}
				});
			}
		});
	}
	
	
	@SuppressWarnings("unused")
	private class GetDataTask extends
	AsyncTask<Void, Void, ArrayList<MobileWorkerVO>> {
	
		@Override
		protected ArrayList<MobileWorkerVO> doInBackground(
				Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				;
			}
			return list;
		}
		
	
		@Override
		protected void onPostExecute(ArrayList<MobileWorkerVO> result) {
			super.onPostExecute(result);
		}
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
				
				listTitleText.setText(list.get(position).getName().toString());
				
				String phone = "";
				
				phone = list.get(position).getPhone().substring(0, 3).concat("-").concat(list.get(position).getPhone().substring(3, 7)).concat("-").concat(list.get(position).getPhone().substring(7));
				
				String infor = "";
				infor = list.get(position).getCname() + " / " + list.get(position).getT_name() + " / " + phone;
				
				subTitleText.setText(infor);
				
				if( list.get(position).getFirst().equals("N") ) {
					statusTitleText.setText("");
					statusTitleText.setTextColor(Color.RED);
				} else {
					statusTitleText.setText("");
					statusTitleText.setTextColor(Color.BLUE);
				}
			}
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub

			    	new AlertDialog.Builder(RegPersonListActivity.this)
			    	.setTitle("근로자삭제")
			    	.setMessage(  list.get(position).getName().toString()+"를 삭제하시겠습니까?")
			    	.setIcon(android.R.drawable.ic_dialog_alert)
			    	.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			    		
			    		public void onClick(DialogInterface dialog, int whichButton) {
			    			eventUpdateWorkerDel( list.get(position), "N");
			    		}})
			    		.setNegativeButton("취소", null).show();
				    
					return false;
				}
			});
			
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!list.isEmpty()) {
						app.setMv(list.get(position));
						Intent intent = new Intent(RegPersonListActivity.this, RegPersonActivity.class);
						intent.putExtra("gubun", "1");
						startActivity(intent);
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
	
	class eventEnterkeyListenner implements OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			// TODO Auto-generated method stub
			if( arg1 == EditorInfo.IME_ACTION_DONE || (arg2 != null && arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
				getSearchEvent(searchnameBox.getText().toString());
				return true;
			}
			
			return false;
		}


		
	}
}
