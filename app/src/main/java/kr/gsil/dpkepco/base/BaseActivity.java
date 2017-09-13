package kr.gsil.dpkepco.base;

import java.util.ArrayList;

import kr.gsil.dpkepco.UserSystemApplication;
import kr.gsil.dpkepco.client.HttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public abstract class BaseActivity extends AppCompatActivity {
	
	public abstract void init();
	public abstract void setData();
	
	private static ProgressDialog plg = null;
	public LinearLayout mapViewLayout = null;
	public InputMethodManager imm;
	public HttpClient api;
	public String lang = "";
	public NfcAdapter nfcAdapter;
	public PendingIntent pendingIntent;
	public LayoutInflater inflater = null;
	
	
	public UserSystemApplication app = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (UserSystemApplication)getApplication();
		
		//NFC Setting Start
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intent = new Intent(this, getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		//NFC Setting End
		
		inflater = LayoutInflater.from(this);
		api = HttpClient.getInstance();
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		progressInit();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void setCancelable(boolean value)
	{
		plg.setCancelable(value);
	}

	public void progressInit() {
		plg = new ProgressDialog(this);
		plg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		plg.setMessage("Loading...");
		plg.setCancelable(false);
		plg.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
            	pHide();
            }
        });
	}

	public void pShow() {
		if (plg != null) progressInit();		
		pHide();		
		if (!plg.isShowing()) {
			//TODO: ������ bug
			if(plg!=null)
				plg.show();
		}
	}

	public boolean pIsShow(){
		return plg.isShowing();
	}

	public void pHide() {
		if (plg != null && plg.isShowing()) {
			plg.dismiss();
		}
	}

	static Toast mToast = null;

	public synchronized void showToast(String message) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
		} else {
			mToast.setText(message);
		}
		mToast.show();
	}

	public void startThread(Runnable run) {
		new Thread(run).start();
	}

	public void setHideKeyBoard(EditText et)
	{
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	public void setShowKeyboard(EditText et)
	{
		imm.showSoftInputFromInputMethod(et.getWindowToken(), InputMethodManager.SHOW_FORCED);
		imm.showSoftInputFromInputMethod (et.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED);
		imm.toggleSoftInputFromWindow(et.getApplicationWindowToken(),  InputMethodManager.SHOW_FORCED, 0);
	}

	public void setShowKeyboardaCatchAction(EditText et, ResultReceiver receiver)
	{
		imm.showSoftInput(et, InputMethodManager.SHOW_FORCED, receiver);
	}
}
