package kr.gsil.dpkepco.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CustomJsonObject extends JSONObject {
	
	
	public CustomJsonObject(String str) throws JSONException {
		super(str);
		/*
		if(UtilLogCat.getDebugMode()) {
			try {
				 Log.d(TAG, URLDecoder.decode(str, "UTF-8"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		*/
	}

	public CustomJsonObject(JSONObject json) throws JSONException {
		super(json.toString());
	}

	@Override
	public String getString(String name) throws JSONException {
		String get = null;
		try {
			get = URLDecoder.decode(super.getString(name), "UTF-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return get;
	}

	public boolean getResultOk() throws JSONException {
		boolean result = false;
		if (super.getString("result").equals("ok")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean getResultError() throws JSONException {
		boolean result = false;
		if (super.getString("result").equals("error")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {

		try {
			return URLDecoder.decode(super.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.toString();
	}

	/**
	 * 값이 ?�는�?체크
	 * 
	 * @param key
	 * @return true - 값이 ?�다. false 값이 ?�다.
	 * @throws JSONException
	 */
	public boolean isEmpty(String key) throws JSONException {
		if (super.getString(key).equals("")) {
			return true;
		} else {
			return false;
		}
	}
}
