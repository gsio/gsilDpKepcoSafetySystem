package kr.gsil.dpkepco.activity.worker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.base.BaseActivity;
import kr.gsil.dpkepco.model.MobileUserVO;
import kr.gsil.dpkepco.model.MobileWorkerVO;
import kr.gsil.dpkepco.model.MobileWtypeVO;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class RegPersonActivity extends BaseActivity {

	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	
	int currentClick = 0;
	int phoneOk = 0;
	int regOk = 0;
	int first = 0;
	int wtypeCheckOk = 0;
	
	String regid = "";
	
	String modifyGubun = "0";
	
	RelativeLayout menuBar = null;
	ImageView backIcon = null;
	TextView titleText = null;
	
	//Insert item
	EditText nameText = null;
	ImageView forCheck = null;
	ImageView wtypeCheck = null;
	
	EditText frontText = null;
	EditText endText = null;
	EditText phoneText = null;
	
	Button duplicatePhoneBtn = null;
	Button checkBlackBtn = null;
	Button firstdateBtn = null;
	
	EditText passnumberText = null;
	EditText contrynameText = null;
//
//	TextView contTitle = null;
//	LinearLayout contLayer = null;
//	Spinner contselect = null;
	
	Spinner rankselect = null;
	Spinner overselect = null;
	Spinner contselect = null;
	
	Button completebtn = null;
	
	ArrayList<MobileUserVO> contlist  = new ArrayList<MobileUserVO>();
	ArrayList<MobileWtypeVO> wlist = new ArrayList<MobileWtypeVO>();
	List<String> slist = null;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_person);
		init();
	}

	@Override
	public void init() {

		
		Intent passedIntent = getIntent();
		
		if( passedIntent.getStringExtra("gubun") != null && !passedIntent.getStringExtra("gubun").equals("") ) {
			modifyGubun = passedIntent.getStringExtra("gubun");
		} else {
			modifyGubun = "0";
		}
		
		
		// TODO Auto-generated method stub
		nameText = ( EditText ) findViewById(R.id.nameText);
		forCheck = ( ImageView ) findViewById(R.id.forCheck);
		wtypeCheck = ( ImageView ) findViewById(R.id.wtypeCheck);
		frontText = ( EditText ) findViewById(R.id.frontText);
		endText  = ( EditText ) findViewById(R.id.endText);
		phoneText= ( EditText ) findViewById(R.id.phoneText);
		duplicatePhoneBtn = ( Button ) findViewById(R.id.duplicatePhoneBtn);
		checkBlackBtn= ( Button ) findViewById(R.id.checkBlackBtn);
		passnumberText = ( EditText ) findViewById(R.id.passnumberText);
		contrynameText = ( EditText ) findViewById(R.id.contrynameText);

		rankselect = ( Spinner ) findViewById(R.id.rankselect);
		overselect = ( Spinner ) findViewById(R.id.overselect);
		contselect = ( Spinner ) findViewById(R.id.contselect);
		completebtn = ( Button ) findViewById(R.id.completebtn);
		firstdateBtn = ( Button ) findViewById(R.id.firstdateBtn);
		


		
		forCheck.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	if( currentClick == 0 ) {
            			forCheck.setImageResource(R.drawable.check_on);
            			currentClick = 1;
            			passnumberText.setEnabled(true);
            			passnumberText.setAlpha(1);
            			contrynameText.setEnabled(true);
            			contrynameText.setAlpha(1);
            	} else {
            		forCheck.setImageResource(R.drawable.check_off);
            			passnumberText.setEnabled(false);
            			passnumberText.setAlpha((float) 0.5);
            			contrynameText.setEnabled(false);
            			contrynameText.setAlpha((float) 0.5);

            		currentClick = 0;
            	}
			}
		});
		
		wtypeCheck.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	if( wtypeCheckOk == 0 ) {
            		wtypeCheck.setImageResource(R.drawable.check_on);
            		duplicatePhoneBtn.setVisibility(View.GONE);
            		phoneOk = 1;
            		wtypeCheckOk = 1;
            	} else {
            		wtypeCheck.setImageResource(R.drawable.check_off);
            		duplicatePhoneBtn.setVisibility(View.VISIBLE);
            		phoneOk = 0;
            		wtypeCheckOk = 0;
            	}
			}
		});
		
		slist = Arrays.asList(getResources().getStringArray(R.array.btype));
		

		firstdateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogFirstDatePicker();
			}
		});
		
		duplicatePhoneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( phoneText.getText().toString().equals("") ) {
					showToast("핸드폰번호를 입력해주세요.");
					return;
				}
				duplicateEvent(phoneText.getText().toString());
			}
		});
		
		checkBlackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( nameText.getText().toString().equals("")) {
					showToast("이름을 입력해주세요.");
					return;
				}
				
				if( frontText.getText().toString().equals("") || endText.getText().toString().equals("") ) {
					showToast("주민번호를 입력해주세요.");
					return;
				}
				
				String jumin =frontText.getText().toString().concat(endText.getText().toString());
				
				checkBlackEvent(nameText.getText().toString(), jumin);
			}
		});

		
		completebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				eventInsert();
			}
		});
		
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
//		
//		if( !app.getRtype().equals("99") ) {
//			contTitle.setVisibility(View.GONE);
//			contLayer.setVisibility(View.GONE);
//			contselect.setVisibility(View.GONE);
//		} else {
//			if( ! modifyGubun.equals("0") ) {
//				contTitle.setVisibility(View.GONE);
//				contLayer.setVisibility(View.GONE);
//				contselect.setVisibility(View.GONE);
//			}
//		}
		
		getWlist();
	}

	@Override
	public void setData() {
		// TODO Auto-generated method stub
		rankselect.setAdapter(new MyCustomAdapter(RegPersonActivity.this, R.layout.row, wlist)); 
		overselect.setAdapter(new MyOverCustomAdapter(RegPersonActivity.this, R.layout.row, slist));
		
		if( ( modifyGubun.equals("1") ||  modifyGubun.equals("4") ) && app.getMv() != null ) {
			MobileWorkerVO vo = app.getMv();
			
			nameText.setText(vo.getName());
			app.setTag_id(vo.getTagid());
			
			if( vo.getGubun().equals("1") ) {
				currentClick = 1;
				passnumberText.setText(vo.getPassno());
				contrynameText.setText(vo.getCountry());
				
    			passnumberText.setEnabled(true);
    			passnumberText.setAlpha(1);
    			contrynameText.setEnabled(true);
    			contrynameText.setAlpha(1);
    			forCheck.setImageResource(R.drawable.check_on);
			}
			
			frontText.setText(vo.getJumin().substring(0, 6));
			endText.setText(vo.getJumin().substring(6));
			

			if( !vo.getFirstdate().equals("") ){
				String fdate = vo.getFirstdate().substring(0, 4) + "-" +vo.getFirstdate().substring(4, 6)+ "-" +vo.getFirstdate().substring(6, 8);
				firstdateBtn.setText(fdate);
			}
			
			phoneText.setText(vo.getPhone());
			phoneOk = 1;
			regOk = 1;
			
			if( wlist != null && wlist.size() > 0 ) {
				for( int i = 0 ; i < wlist.size(); i++ ) {
					MobileWtypeVO t = ( MobileWtypeVO ) wlist.get(i);
					if( vo.getT_id().equals(t.getId()) ) {
						rankselect.setSelection(i);
					}
				}
			}
			if(vo.getBtype().equals("O")) overselect.setSelection(0);
			else if(vo.getBtype().equals("A")) overselect.setSelection(1);
			else if(vo.getBtype().equals("B")) overselect.setSelection(2);
			else if(vo.getBtype().equals("AB")) overselect.setSelection(3);
			else overselect.setSelection(4);
			/*
			if( vo.getSingoyn().equals("N") ) {
				overselect.setSelection(0);
			} else {
				overselect.setSelection(1);
			}*/

			
		} else {
			app.setTag_id("");
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		    Date currentTime = new Date ( );
		    final String today = mSimpleDateFormat.format ( currentTime );
		    firstdateBtn.setText(today);
		}
		
		getContList();
	}
	
	public void getContList() {
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
							contselect.setAdapter(new MyContCustomAdapter(RegPersonActivity.this, R.layout.row, contlist));
						}
					}
				});
			}
		});
	}
	
	public void settingdata() {

		if( app.getMv() != null ) {
			MobileWorkerVO vo = app.getMv();
			
			nameText.setText(vo.getName());
			app.setTag_id(vo.getTagid());
			
			if( vo.getGubun().equals("1") ) {
				currentClick = 1;
				passnumberText.setText(vo.getPassno());
				contrynameText.setText(vo.getCountry());
				
    			passnumberText.setEnabled(true);
    			passnumberText.setAlpha(1);
    			contrynameText.setEnabled(true);
    			contrynameText.setAlpha(1);
    			forCheck.setImageResource(R.drawable.check_on);
			}
			
			if( !vo.getJumin().equals("") ) {
				frontText.setText(vo.getJumin().substring(0, 6));
				endText.setText(vo.getJumin().substring(6));
			}
			

			
			if( !vo.getFirstdate().equals("") ){
				String fdate = vo.getFirstdate().substring(0, 4) + "-" +vo.getFirstdate().substring(4, 6)+ "-" +vo.getFirstdate().substring(6, 8);
				firstdateBtn.setText(fdate);
			}
			
			phoneText.setText(vo.getPhone());
//			phoneOk = 1;
//			regOk = 1;
//			
//			if( wlist != null && wlist.size() > 0 ) {
//				for( int i = 0 ; i < wlist.size(); i++ ) {
//					MobileWtypeVO t = ( MobileWtypeVO ) wlist.get(i);
//					if( vo.getT_id().equals(t.getId()) ) {
//						rankselect.setSelection(i);
//					}
//				}
//			}


			if(vo.getBtype().equals("O")) overselect.setSelection(0);
			else if(vo.getBtype().equals("A")) overselect.setSelection(1);
			else if(vo.getBtype().equals("B")) overselect.setSelection(2);
			else if(vo.getBtype().equals("AB")) overselect.setSelection(3);
			/*
			if( vo.getSingoyn().equals("N") ) {
				overselect.setSelection(0);
			} else {
				overselect.setSelection(1);
			}*/
			

			
		} else {
			app.setTag_id("");
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		    Date currentTime = new Date ( );
		    final String today = mSimpleDateFormat.format ( currentTime );
		    firstdateBtn.setText(today);
		}
	}
	
	public void setContData() {
		// TODO Auto-generated method stub
//		startThread(new Runnable() {
//			public void run() {
//				contlist = api.getContList(getBaseContext(),app.getSite_id());
//				if( contlist == null ) contlist = new ArrayList<MobileUserVO>();
//				runOnUiThread(new Runnable() {
//					public void run() {
//						if( contlist != null && contlist.size() > 0 ) {
//							MobileUserVO m = new MobileUserVO();
//							m.setName("업체 선택");
//							contlist.add(0, m);
//							contselect.setAdapter(new MyContCustomAdapter(RegPersonActivity.this, R.layout.row, contlist));
//						}
//						
//						setData();
//					}
//				});
//			}
//		});
	}
	
	public void eventInsert() {
		
		if( nameText.getText().toString().equals("") ) {
			showToast("성명을 입력해주세요.");
			return;
		}
		
		if( frontText.getText().toString().equals("") || endText.getText().toString().equals("") ) {
			showToast("주민번호를 입력해주세요.");
			return;
		}
				
		if( phoneText.getText().toString().equals("") ) {
			showToast("핸드폰을 입력해주세요.");
			return;
		}
		
		if( phoneOk == 0 ) {
			showToast("핸드폰 번호 중복검사를 해주세요.");
			return;
		}
		
		if( regOk == 0 ) {
			showToast("입사가능 여부를 검사해주세요.");
			return;
		}

		
		String gubun = "";
		String country = "";
		String passno = "";
		String first = "N";
		String t_id = "";
		String cont_id = "";
		String singoyn = "";
		String edudate = "";
		String firstdate = "";
		String imagename = "";
		
		final MobileUserVO m = (MobileUserVO)contselect.getSelectedItem();
		
		if( !m.getName().equals("업체 선택") ) {
			cont_id = m.getId();
		} else {
			showToast("업체를 선택해주세요.");
			return;
		}

		
		if( currentClick == 0 ) {
			gubun = "0";
		} else {
			gubun = "1";
			country = contrynameText.getText().toString();
			passno = passnumberText.getText().toString();
		}
		
		MobileWtypeVO mw = (MobileWtypeVO) rankselect.getSelectedItem();
		t_id = mw.getId();
		

		if( !firstdateBtn.getText().toString().equals("채용일 선택") ) {
			firstdate = firstdateBtn.getText().toString();
		}
		
		String s = ( String ) overselect.getSelectedItem();
		if(s.equals("미정")){
			showToast("혈액형을 선택해주세요.");
			return;
		}
		String bType = s.replace("형","");
		//showToast(bType);
		/*
		if( s.equals("비신고") ) {
			singoyn = "N";
		} else {
			singoyn = "Y";			
		}*/

		singoyn = "Y";

		if( app.getMv() != null && app.getMv().getImage() != null ) {
			imagename = "P_" + phoneText.getText().toString();
			app.getMv().setImageName(imagename);
		}
		
		if( ( modifyGubun.equals("1") || modifyGubun.equals("4") ) && app.getMv() != null ) {
			eventUpdateWorker(
					app.getMv().getId()
					, app.getMv().getPhone()
					, gubun
					, nameText.getText().toString()
					, frontText.getText().toString().concat(endText.getText().toString())
					, country
					, passno
					, t_id
					, singoyn
					, phoneText.getText().toString()
					, app.getSite_id(),edudate,firstdate
					, imagename
					,bType
					);
		} else {
			eventInsertWorker(phoneText.getText().toString()
					, gubun
					, nameText.getText().toString()
					, frontText.getText().toString().concat(endText.getText().toString())
					, country
					, passno
					, first
					, t_id
					, cont_id
					, singoyn
					, app.getSite_id(),edudate,firstdate,imagename,bType);
		}

	}
	
	
	public void checkBlackEvent(final String name, final String jumin) {

		pShow();
		startThread(new Runnable() {
			public void run() {
				final String result = api.checkBlackPerson(getBaseContext(), name, jumin);
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( result.equals("BAD") ) {
							showToast("해당근로자는 안전교육 이수가 불가능합니다.");
							regOk = 0;
						} else if( result.equals("") ) {
							showToast("등록가능한 근로자 입니다.");
							regOk = 1;
						} else {
							new AlertDialog.Builder(RegPersonActivity.this)
							.setTitle("정보가 있는 근로자")
							.setMessage("정보가 있는 근로자 입니다. 활성화 하시겠습니까?")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton("등록", new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int whichButton) {
							    	changeStatusWoker(result,"Y");
							    }})
							 .setNegativeButton("취소", null).show();
							
							
							regOk = 1;
						}
					}
				});
			}
		});
	}
	
	
	private void changeStatusWoker( final String id , final String delyn) {

		pShow();
		startThread(new Runnable() {
			public void run() {
				final String result = api.updateWorkerDel(getBaseContext(), id, delyn, "");
				runOnUiThread(new Runnable() {
					public void run() {
						if( result != null && !result.equals("") ) {
								pHide();
								showToast("정상 등록 되었습니다.");
								app.getPact().finish();
								Intent intent = new Intent( getApplicationContext(), RegPersonListActivity.class );
								startActivity(intent);
								finish();
						} else {
							pHide();
						}
					}
				});
			}
		});
	}
	
	public void duplicateEvent(final String phone) {
		pShow();
		startThread(new Runnable() {
			public void run() {
				final MobileWorkerVO m = api.mobileCheckPhone(getBaseContext(), phone);
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( m != null && !m.getId().equals("") ) {
							for( int i = 0 ; i < contlist.size(); i++ ) {
								if( contlist.get(i).getId().equals(m.getCont_id()) ) {
									showToast(  contlist.get(i).getName()+ "에 등록된 사용자 입니다.");
								}
							}
							phoneOk = 0;
						} else {
							showToast("등록 가능한 핸드폰 입니다.");
							phoneOk = 1;
						}
					}
				});
			}
		});
	}
	
	public void getWlist() {
		// TODO Auto-generated method stub
		pShow();
		startThread(new Runnable() {
			public void run() {
				wlist = api.workTypeList(getBaseContext(),"");
				if( wlist == null ) wlist = new ArrayList<MobileWtypeVO>();
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( wlist != null && wlist.size() > 0 ) {
							setData();
						}
					}
				});
			}
		});
	}
	
	public void eventInsertWorker(final String phone,final String gubun,final String name,
			final String jumin,final String country,final String passno,final String first,
			final String t_id,final String cont_id,final String singoyn, final String site_id, final String edudate, final String firstdate, final String imagename, final String bType) {
		pShow();
		startThread(new Runnable() {
			public void run() {
				final String m = api.insertWorker(getBaseContext(), phone, gubun, name, jumin, country, passno, first, t_id, cont_id, singoyn, site_id,edudate,firstdate, imagename, bType);
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( app.getMv() != null && app.getMv().getImage() != null ) {
							eventUpLoadImage(app.getMv());
						} else {
							if( m != null && !m.equals("") ) {
								showToast("등록이 정상적으로 완료 되었습니다.");
								app.getPact().finish();
								finish();
								
								if( modifyGubun.equals("4") || modifyGubun.equals("3") ) {

								} else {
									Intent intent = new Intent( getApplicationContext(), RegPersonListActivity.class );
									startActivity(intent);
								}
							} else {
								showToast("등록에 실패 하였습니다. 항목들을 확인해주세요.");
							}
						}

					}
				});
			}
		});
	}
	
	public void eventUpdateWorker(final String id,final String phone,final String gubun,final String name,
			final String jumin,final String country,final String passno, final String t_id,final String singoyn,final String cphone
			,final String site_id, final String edudate, final String firstdate, final String imagename, final String bType) {
		pShow();
		startThread(new Runnable() {
			public void run() {
				final String m = api.updateWorker(getBaseContext(), id, phone, gubun, name, jumin, country, passno, t_id, singoyn, cphone,site_id,edudate,firstdate,imagename,bType);
				runOnUiThread(new Runnable() {
					public void run() {
						pHide();
						if( app.getMv().getImage() != null ) {
							eventUpLoadImage(app.getMv());
						} else {
							if( m != null && !m.equals("") ) {
								showToast("등록이 정상적으로 완료 되었습니다.");
								app.getPact().finish();
								finish();
								
								if( modifyGubun.equals("4") || modifyGubun.equals("3") ) {

								} else {
									Intent intent = new Intent( getApplicationContext(), RegPersonListActivity.class );
									startActivity(intent);
								}
							} else {
								showToast("등록에 실패 하였습니다. 항목들을 확인해주세요.");
							}
						}
					}
				});
			}
		});
	}
	
	public void eventUpLoadImage(final MobileWorkerVO mobile ) {
		startThread(new Runnable() {
			public void run() {
				final String returnItem = api.uploadPersonImage(getBaseContext(), mobile);
				runOnUiThread(new Runnable() {
					public void run() {
						eventUpdateComplete();
					}
				});
			}
		});		
	}
	
	public void eventUpdateComplete() {
		showToast("등록이 정상적으로 완료 되었습니다.");
		app.getPact().finish();
		finish();
		
		if( modifyGubun.equals("4") || modifyGubun.equals("3") ) {

		} else {
			Intent intent = new Intent( getApplicationContext(), RegPersonListActivity.class );
			startActivity(intent);
		}
	}
	
    public class MyCustomAdapter extends ArrayAdapter<MobileWtypeVO>{

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<MobileWtypeVO> objects) {
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
			label.setText(wlist.get(position).getT_name());
			label2.setText(wlist.get(position).getId());
			
			return row;
		}	
    }
    
    public class MyOverCustomAdapter extends ArrayAdapter<String>{

		public MyOverCustomAdapter(Context context, int textViewResourceId,
				List<String> objects) {
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
			label.setText(slist.get(position).toString());
			label2.setText(Integer.toString(position));
			
			return row;
		}	
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
		    	
		    	
		    	firstdateBtn.setText(String.valueOf(year)+"-"+rmonth+"-"+rday);
		    }
	    };
	    
	    DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,  
	    cyear, cmonth, cday);
	    alert.show();
	}
	
	   public class MyContCustomAdapter extends ArrayAdapter<MobileUserVO>{

			public MyContCustomAdapter(Context context, int textViewResourceId,
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
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if( app.getMv() != null ) {
    		settingdata();
    	}
    }
    
    /******/

}

