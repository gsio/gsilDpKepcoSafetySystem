package kr.gsil.dpkepco.activity;

import java.util.ArrayList;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.R.id;
import kr.gsil.dpkepco.R.layout;
import kr.gsil.dpkepco.activity.BeaconPersonSearchActivity.MyCustomAdapter;
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
import android.os.AsyncTask;
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

public class BeaconManageActivity extends BaseActivity {
	//Top
	RelativeLayout menuBar = null;
	ImageView backIcon = null;
	TextView titleText = null;

	//Item Component
	Button gubuncheckBtn = null;
	Button searchBtn = null;
	EditText nameBoxEditText = null;
	
	ArrayList<MobileUserVO> list  = new ArrayList<MobileUserVO>();
	ArrayList<MobileUserVO> templist  = new ArrayList<MobileUserVO>();
	ArrayList<MobileUserVO> contlist  = new ArrayList<MobileUserVO>();
	
	MainListViewAdapter rAdapter;
	ListView listview = null;
	Spinner searchnameBox = null;
	
	String contName = "";
	int check = 0;
	int yf = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_manage);
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
		nameBoxEditText = (EditText) findViewById(R.id.nameBoxEditText);
		searchnameBox= ( Spinner ) findViewById(R.id.searchnameBox);
		
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
		
		
		searchBtn = ( Button ) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if( nameBoxEditText.getText().toString().equals("") ) {
//					showToast("검색할 이름을 입력해 주세요");
//					return;
//				}
				searchWordList();
//				setEvnetChangeData();
				//getSearchEvent(nameBoxEditText.getText().toString());
			}
		});
		
		
		gubuncheckBtn = ( Button ) findViewById(R.id.gubuncheckBtn);
		gubuncheckBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gubunCheck();
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
		
		if( app.getType().equals("0") ) {
			setContList();
		} else {
			contName = app.getCont_id();
			searchnameBox.setVisibility(View.GONE);
			setData();
		}
	}

	public void setContList() {
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
							searchnameBox.setAdapter(new MyCustomAdapter(BeaconManageActivity.this, R.layout.row, contlist));
						}
						setData();
					}
				});
			}
		});
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

	
	public boolean getSearchEvent( final String searchname ) {
		// TODO Auto-generated method stub
		pShow();
		startThread(new Runnable() {
			public void run() {
				list = api.getBeaconManagerList(getBaseContext(), searchname, app.getSite_id(), contName);
				if( list == null ) list = new ArrayList<MobileUserVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						rAdapter.notifyDataSetChanged();
					}
				});
			}
		});
		return true;
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
		
		String searchText = "";
		
		if( !nameBoxEditText.getText().toString().equals("") )  {
			searchText = nameBoxEditText.getText().toString();
		}
		
		getSearchEvent(searchText);
	}
	
	
	
	public void setMirrorList() {
		templist = null;
		templist = new ArrayList<MobileUserVO>();
		if( list != null && list.size() > 0 ) {
			for ( int i = 0; i < list.size(); i ++ ) {
				MobileUserVO fm = ( MobileUserVO ) list.get(i);
				templist.add(fm);
			}
		}
	    
	    if( !nameBoxEditText.getText().toString().equals("") ) {
	    	searchWordList();
	    }
	}
	
	public void searchWordList() {
		
		ArrayList<MobileUserVO> searchList = new  ArrayList<MobileUserVO>();
		
		if( templist != null && templist.size() > 0 ) {
			for ( int i = 0; i < templist.size(); i ++ ) {
				MobileUserVO fm = ( MobileUserVO ) templist.get(i);
				if( SoundSearcher.matchString(fm.getName(), nameBoxEditText.getText().toString()) ||
						SoundSearcher.matchString(fm.getId(), nameBoxEditText.getText().toString()) 
						) {
					searchList.add(fm);
				}
			}
		}
		
		if( searchList != null && searchList.size() > 0 ) {
			list = null;
			list = new  ArrayList<MobileUserVO>();
			for ( int i = 0; i < searchList.size(); i ++ ) {
				MobileUserVO fm = ( MobileUserVO ) searchList.get(i);
				list.add(fm);
			}
			rAdapter.notifyDataSetChanged();
		} else {
			list = null;
			list = new ArrayList<MobileUserVO>();
			list = templist;
			rAdapter.notifyDataSetChanged();
		}
		
	}
	
	public void gubunCheck() {
		if( check == 0 ) {
			ArrayList<MobileUserVO> newList = new ArrayList<MobileUserVO>();
			if( templist != null && templist.size() > 0 ) {
				for( int i = 0; i < templist.size(); i++ ) {
					MobileUserVO m = (MobileUserVO) templist.get(i);
					if( m.getName().equals("") ) {
						newList.add(m);
					}
				}
				
				list = null;
				list = new ArrayList<MobileUserVO>();
				list = newList;
				gubuncheckBtn.setBackgroundColor(Color.BLACK);
				rAdapter.notifyDataSetChanged();
			}
			check = 1;
		} else {
			list = null;
			list = new ArrayList<MobileUserVO>();
			list = templist;
			gubuncheckBtn.setBackgroundColor(Color.parseColor("#2faee0"));
			rAdapter.notifyDataSetChanged();
			check = 0;
		}
	}
	
	private void eventUpdateBeaconManager( final String id) {
		if( app.getName().equals("GSIL") ) {
			return;
		}
		pShow();
		startThread(new Runnable() {
			public void run() {
				final String result = api.updateBeaconManager(getBaseContext(), id, "", "");
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( result != null && !result.equals("") ) {
								showToast("정상 삭제 되었습니다.");
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
	
	@SuppressWarnings("unused")
	private class GetDataTask extends
	AsyncTask<Void, Void, ArrayList<MobileUserVO>> {
	
		@Override
		protected ArrayList<MobileUserVO> doInBackground(
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
		protected void onPostExecute(ArrayList<MobileUserVO> result) {
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
				
				
//				if(  list.get(position).getGubun().equals("2") ) {
//					if( list.get(position).getCountdata().equals("0") ) {
//						statusTitleText.setText("3일간 미출역");
//						statusTitleText.setTextColor(Color.RED);
//					} else if( list.get(position).getCountdata().equals("") ) {
//						statusTitleText.setText("3일간 미출역");
//						statusTitleText.setTextColor(Color.RED);
//					}
//				}
				
				if( list.get(position).getUseyn().equals("N") ) {
					statusTitleText.setText("퇴사자");
					statusTitleText.setTextColor(Color.RED);
				}
			}
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					if( !list.get(position).getName().equals("")  ) {
						new AlertDialog.Builder(BeaconManageActivity.this)
						.setTitle("비콘배정자 삭제")
						.setMessage(  list.get(position).getName().toString()+" 배정자를 삭제하시겠습니까?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int whichButton) {
								eventUpdateBeaconManager( list.get(position).getId());
							}})
							.setNegativeButton("취소", null).show();
					}
				    
					return false;
				}
			});
			
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!list.isEmpty()) {
						app.setMu(null);
						list.get(position).setIndex(Integer.toString(position+1));
						app.setMu(list.get(position));
						Intent intent = new Intent(BeaconManageActivity.this, BeaconPersonSearchActivity.class);
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
