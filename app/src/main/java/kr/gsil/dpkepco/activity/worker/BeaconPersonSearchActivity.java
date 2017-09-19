package kr.gsil.dpkepco.activity.worker;

import java.util.ArrayList;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.model.MobileWorkerVO;
import kr.gsil.dpkepco.util.ListViewAdapter;
import kr.gsil.dpkepco.util.SoundSearcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class BeaconPersonSearchActivity extends BaseActivity {

	RelativeLayout menuBar = null;
	ImageView backIcon = null;
	TextView titleText = null;
	
	String searchdate = "";
	
	WorkerListAdapter rAdapter;
	UserViewAdapter uAdapter;
	ListView listview = null;
	ListView userlistview = null;
	
	Button tab1 = null;
	Button tab2 = null;
	Button searchBtn = null;
	
	Spinner searchnameBox = null;
	EditText nameBoxEditText = null;
	
	String gubun  = "";
	String contName = "";
	
	int yf = 0;
	
	ArrayList<MobileUserVO> contlist  = new ArrayList<MobileUserVO>();
	
	ArrayList<MobileUserVO> userlist  = new ArrayList<MobileUserVO>();
	ArrayList<MobileUserVO> tempUserlist  = new ArrayList<MobileUserVO>();
	ArrayList<MobileWorkerVO> dbWlist = new ArrayList<MobileWorkerVO>();
	ArrayList<MobileWorkerVO> tempWorkerlist = new ArrayList<MobileWorkerVO>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_person_search);
		listview= (ListView) findViewById(R.id.managerList);
		userlistview= (ListView) findViewById(R.id.personchecklist);
		
		uAdapter = new UserViewAdapter();
		rAdapter = new WorkerListAdapter();
		
		listview.setAdapter(uAdapter);
		userlistview.setAdapter(rAdapter);
		
		//캡처방지
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		searchnameBox= ( Spinner ) findViewById(R.id.searchnameBox);
		nameBoxEditText= ( EditText ) findViewById(R.id.nameBoxEditText);
		searchBtn = ( Button ) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchWordList();
			}
		});
		
		tab1 = ( Button ) findViewById(R.id.tab1);
		tab1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listview.setVisibility(View.VISIBLE);
				userlistview.setVisibility(View.GONE);
				uAdapter.notifyDataSetChanged();
				
				tab1.setTextColor(Color.parseColor("#1f81ff"));
				tab2.setTextColor(Color.parseColor("#333333"));
				gubun = "0";
			}
		});
		
		tab2= ( Button ) findViewById(R.id.tab2);
		tab2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listview.setVisibility(View.GONE);
				userlistview.setVisibility(View.VISIBLE);
				rAdapter.notifyDataSetChanged();
				
				tab2.setTextColor(Color.parseColor("#1f81ff"));
				tab1.setTextColor(Color.parseColor("#333333"));
				gubun = "1";
			}
		});
		
		// TODO Auto-generated method stub
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
		
		searchnameBox.setOnItemSelectedListener(new OnItemSelectedListener() {
			
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // your code here
		    	
		    	MobileUserVO m = (MobileUserVO)searchnameBox.getSelectedItem();
		    	// your code here
		    	if( m != null ) {
		    		if(!m.getName().equals("업체 선택")) {
		    			setEvnetChangeData();
		    			yf = 1;
		    		} else {
		    			if( yf == 1 ) {
		    				setEvnetChangeData();
		    			}
		    		}
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		tab1.setTextColor(Color.parseColor("#1f81ff"));
		if( app.getType().equals("0") ) {
			setData();
		} else {
			searchnameBox.setVisibility(View.GONE);
			getUserList("");
		}
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
		startThread(new Runnable() {
			public void run() {
				contlist = api.getContList(getBaseContext(),app.getSite_id());
				if( contlist == null ) contlist = new ArrayList<MobileUserVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						if( contlist != null && contlist.size() > 0 ) {
							MobileUserVO m = new MobileUserVO();
							m.setName("업체 선택");
							contlist.add(0, m);
							searchnameBox.setAdapter(new MyCustomAdapter(BeaconPersonSearchActivity.this, R.layout.row, contlist));
						}
						getUserList("");
					}
				});
			}
		});
	}

	public void getUserList( final String searchName) {
		
		if( !app.getType().equals("0") ) {
			contName = app.getCont_id();
		}
		
		pShow();
		startThread(new Runnable() {
			public void run() {
				userlist = api.getUserList(getBaseContext(), app.getSite_id(), contName, searchName,"");
				if( userlist == null ) userlist = new ArrayList<MobileUserVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						
						if( userlist != null && userlist.size() > 0 ) {
							uAdapter.notifyDataSetChanged();
						}
						getWorkerList(searchName);
					}
				});
			}
		});	
	}
	
	public void setMirrorList() {
		tempWorkerlist = null;
		tempWorkerlist = new ArrayList<MobileWorkerVO>();
		if( dbWlist != null && dbWlist.size() > 0 ) {
			for ( int i = 0; i < dbWlist.size(); i ++ ) {
				MobileWorkerVO fm = ( MobileWorkerVO ) dbWlist.get(i);
				tempWorkerlist.add(fm);
			}
		}
		
		tempUserlist = null;
		tempUserlist = new ArrayList<MobileUserVO>();
		if( userlist != null && userlist.size() > 0 ) {
			for ( int i = 0; i < userlist.size(); i ++ ) {
				MobileUserVO fm = ( MobileUserVO ) userlist.get(i);
				tempUserlist.add(fm);
			}
		}
		
		if( !nameBoxEditText.getText().toString().replaceAll(" ", "").equals("") ) {
			searchWordList();
		}
		
	}
	
	public void searchWordList() {
		
		ArrayList<MobileWorkerVO> searchList = new  ArrayList<MobileWorkerVO>();
		ArrayList<MobileUserVO> searchUserList = new  ArrayList<MobileUserVO>();
		
		if( tempWorkerlist != null && tempWorkerlist.size() > 0 ) {
			for ( int i = 0; i < tempWorkerlist.size(); i ++ ) {
				MobileWorkerVO fm = ( MobileWorkerVO ) tempWorkerlist.get(i);
				if( SoundSearcher.matchString(fm.getName(), nameBoxEditText.getText().toString())) {
					searchList.add(fm);
				}
			}
		}
		
		if( tempUserlist != null && tempUserlist.size() > 0 ) {
			for ( int i = 0; i < tempUserlist.size(); i ++ ) {
				MobileUserVO fm = ( MobileUserVO ) tempUserlist.get(i);
				if( SoundSearcher.matchString(fm.getName(), nameBoxEditText.getText().toString())) {
					searchUserList.add(fm);
				}
			}
		}
		
		if( nameBoxEditText.getText().toString().replaceAll(" ", "").equals("") ) {
			dbWlist = null;
			dbWlist = new  ArrayList<MobileWorkerVO>();
			dbWlist = tempWorkerlist;
			rAdapter.notifyDataSetChanged();
			
			
			userlist = null;
			userlist = new  ArrayList<MobileUserVO>();
			userlist = tempUserlist;
			uAdapter.notifyDataSetChanged();
		} else {
			userlist = null;
			userlist = new  ArrayList<MobileUserVO>();
			dbWlist = null;
			dbWlist = new  ArrayList<MobileWorkerVO>();
			if( searchUserList != null && searchUserList.size() > 0 ) {
				for ( int i = 0; i < searchUserList.size(); i ++ ) {
					MobileUserVO fm = ( MobileUserVO ) searchUserList.get(i);
					userlist.add(fm);
				}
			}
			
			if( searchList != null && searchList.size() > 0 ) {
				for ( int i = 0; i < searchList.size(); i ++ ) {
					MobileWorkerVO fm = ( MobileWorkerVO ) searchList.get(i);
					dbWlist.add(fm);
				}
			}
			
			uAdapter.notifyDataSetChanged();
			rAdapter.notifyDataSetChanged();
		}
		
		

		
	}
	
	public void getWorkerList( final String searchName ) {
		
		startThread(new Runnable() {
			public void run() {
				dbWlist = api.workerList(getBaseContext(), searchName,app.getSite_id(), contName, app.getType(),"","Y","","");
				if( dbWlist == null ) dbWlist = new ArrayList<MobileWorkerVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( dbWlist != null && dbWlist.size() > 0 ) {
							rAdapter.notifyDataSetChanged();
						}
						setMirrorList();
					}
				});
			}
		});
	}
	
	
	public void setEvnetChangeData() {
		// TODO Auto-generated method stub
		if( app.getType().equals("0") ) {
			final MobileUserVO m = (MobileUserVO)searchnameBox.getSelectedItem();
			
			if( !m.getName().equals("업체 선택") ) {
				contName =m.getId();
			} else {
				contName = "";
			}
		} else {
			contName = app.getCont_id();
		}
		
//		String searchText = "";
//		
//		if( !nameBoxEditText.getText().toString().equals("") )  {
//			searchText = nameBoxEditText.getText().toString();
//		}
		
		userlist = null;
		userlist = new ArrayList<MobileUserVO>();
		dbWlist = null;
		dbWlist = new ArrayList<MobileWorkerVO>();
		
		getUserList("");
	}
	
	
	private void eventUpdateBeaconManager( final String uid, final String role ) {
		if( app.getName().equals("GSIL") ) {
			return;
		}
		pShow();
		startThread(new Runnable() {
			public void run() {
				final String result = api.updateBeaconManager(getBaseContext(), app.getMu().getId(), uid, role);
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( result != null && !result.equals("") ) {
								showToast("배정이 완료 되었습니다.");
								app.getPact().finish();
								
								Intent intent = new Intent( BeaconPersonSearchActivity.this, BeaconManageActivity.class );
								startActivity(intent);
								finish();
						} else {
							rAdapter.notifyDataSetChanged();
						}
						//
					}
				});
			}
		});
	}
	
	private MobileUserVO getDuplicateBeconUser( String id ) {
		
		if( app.getBeaconlist() != null && app.getBeaconlist().size() > 0 ) {
			for( int i = 0; i < app.getBeaconlist().size(); i++ ) {
				MobileUserVO m = ( MobileUserVO )app.getBeaconlist().get(i);
				if( m.getName().equals(id) ) {
					return m;
				}
			}
		}
		
		return null;
	}
	
	class WorkerListAdapter extends ListViewAdapter {
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
			
			if( dbWlist != null && dbWlist.size() > 0 ) {
				
				listTitleText.setText(Integer.toString((position +1))  + ". " + dbWlist.get(position).getName().toString());
				
				String phone = "";
				
				phone = dbWlist.get(position).getPhone().substring(0, 3).concat("-").concat("****").concat("-").concat(dbWlist.get(position).getPhone().substring(7));
				
				String infor = "";
				infor = dbWlist.get(position).getCname() + " / " + dbWlist.get(position).getT_name() + " / " + phone;
				
				subTitleText.setText(infor);
				
			}
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
	
	
				    
					return false;
				}
			});
			
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!dbWlist.isEmpty()) {
				    	new AlertDialog.Builder(BeaconPersonSearchActivity.this)
				    	.setTitle("비콘 배정")
				    	.setMessage( "비콘 아이디 : " + app.getMu().getIndex()+"에 " +dbWlist.get(position).getName().toString()+ "를 배정하시겠습니까?")
				    	.setIcon(android.R.drawable.ic_dialog_alert)
				    	.setPositiveButton("배정", new DialogInterface.OnClickListener() {
				    		
				    		public void onClick(DialogInterface dialog, int whichButton) {
				    			MobileUserVO m = getDuplicateBeconUser(dbWlist.get(position).getName());
				    			if( m == null ) {
				    				eventUpdateBeaconManager(dbWlist.get(position).getId(), "2");
				    			} else {
				    				showToast("비콘번호 " + m.getId() +"에 이미 배정된 사용자입니다.");	
				    			}
				    		}})
				    		.setNegativeButton("취소", null).show();
					}
				}
			});
			
			return convertView;
		}
	
		@Override
		public int setCount() {
			return dbWlist.size();
		}

	}
	
	
	
	class UserViewAdapter extends ListViewAdapter {

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
			
//			TextView statusTitleText = (TextView) convertView
//					.findViewById(R.id.statusname);
			
			
			listTitleText.setText( Integer.toString((position +1))  + ". " + userlist.get(position).getName().toString());

			String infor = "";
			infor = userlist.get(position).getCname() + " / " + userlist.get(position).getRname() + " / " + userlist.get(position).getPhone();
			
			subTitleText.setText(infor);
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!userlist.isEmpty()) {
				    	new AlertDialog.Builder(BeaconPersonSearchActivity.this)
				    	.setTitle("비콘 배정")
				    	.setMessage( "비콘 아이디 : " + app.getMu().getIndex()+"에 " +userlist.get(position).getName().toString()+ "를 배정하시겠습니까?")
				    	.setIcon(android.R.drawable.ic_dialog_alert)
				    	.setPositiveButton("배정", new DialogInterface.OnClickListener() {
				    		
				    		public void onClick(DialogInterface dialog, int whichButton) {
				    			MobileUserVO m = getDuplicateBeconUser( userlist.get(position).getName());
				    			if( m == null ) {
				    				eventUpdateBeaconManager( userlist.get(position).getId() , "1");
				    			} else {
				    				showToast("비콘번호 " + m.getId() +"에 이미 배정된 사용자입니다.");	
				    			}
				    			
				    		}})
				    		.setNegativeButton("취소", null).show();
					}
				}
			});
			
	
			return convertView;
		}

		@Override
		public int setCount() {
			return userlist.size();
		}

	}
	
	
    public class MyCustomAdapter extends ArrayAdapter<MobileUserVO>{

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<MobileUserVO> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//return super.getView(position, convertView, parent);

			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			TextView label=(TextView)row.findViewById(R.id.content);
			TextView label2=(TextView)row.findViewById(R.id.hiddenvalue);
			String dateString ="";
			if( position == 0 ) {
				dateString =  "업체 선택";
			} else {
				dateString = contlist.get(position).getName();
			}
			
			label.setText(dateString);
			label2.setText(contlist.get(position).getId());
			
			return row;
		}	
    }
}
