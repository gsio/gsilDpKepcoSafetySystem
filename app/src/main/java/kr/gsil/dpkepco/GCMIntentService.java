package kr.gsil.dpkepco;

import java.net.URLDecoder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import kr.gsil.dpkepco.R;
import kr.gsil.dpkepco.activity.LoginActivity;
import kr.gsil.dpkepco.activity.MainActivity;
import kr.gsil.dpkepco.activity.PopupActivity;

public class GCMIntentService extends GCMBaseIntentService {
	static String id =  "";

	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {

		int icon = R.mipmap.ic_launcher;
		long when = System.currentTimeMillis();

		SharedPreferences pref = context.getSharedPreferences("gsilsamsungsystem", Activity.MODE_PRIVATE);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, message, when);

		//notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" +R.drawable.ping);
//		notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" +R.drawable.ping);
		//notification.defaults |= Notification.DEFAULT_VIBRATE;

		String title = context.getString(R.string.app_name);
		id =  pref.getString("id", id);

		Intent notificationIntent = null;
		if( id != null && !id.equals("") ) {
			notificationIntent = new Intent(context, MainActivity.class);
		} else {
			notificationIntent = new Intent(context, LoginActivity.class);
		}

		//if( message.indexOf("/W") >= 0 ) {
			AudioManager manager = (AudioManager)context.getSystemService(AUDIO_SERVICE);
			int max = manager.getStreamMaxVolume(AudioManager.RINGER_MODE_NORMAL);
			manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			manager.setStreamVolume(AudioManager.STREAM_RING, max, AudioManager.FLAG_PLAY_SOUND);
			Ringtone mRing = RingtoneManager.getRingtone(context,Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.ping));
			mRing.play();
		//}

		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(context)
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(message)
				//.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.drawable.ping))
				.setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND)
				.setTicker("");

		notification = builder.build();
		nm.notify(0, notification);
	}

	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		if( intent != null && intent.getStringExtra("msg") != null ) {

			String msg = URLDecoder.decode( intent.getStringExtra("msg"));
			Log.e("getmessage", "getmessage:" + msg);
//
//			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//			r.play();

			if( msg.indexOf("/A") >= 0 ) {
				String phone = "";
				String name = "";

				msg = msg.replaceAll("/A", "");

				phone = msg.substring(msg.indexOf("/")+1, msg.indexOf("/N"));
				name = msg.substring(msg.indexOf("N/")+2);
				msg = msg.replaceAll("/N/", "");
				msg = msg.substring(0,msg.indexOf("/"));

				Intent popupIntent = new Intent(context, PopupActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// 그리고 호출한다.
				popupIntent.putExtra("data", msg);
				popupIntent.putExtra("phone", phone);
				popupIntent.putExtra("name", name);

				context.startActivity(popupIntent);
			} else if( msg.indexOf("/W") >= 0 ) {
				String phone = "";
				msg = msg.replaceAll("/W", "");
				phone = msg.substring(msg.indexOf("/")+1);
				msg = msg.substring(0,msg.indexOf("/"));

				Intent popupIntent = new Intent(context, PopupActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// 그리고 호출한다.
				popupIntent.putExtra("data", msg);
				popupIntent.putExtra("phone", phone);

				context.startActivity(popupIntent);
			}

			generateNotification(context,msg);
		}

	}


	@Override

	protected void onRegistered(Context context, String reg_id) {
		Log.e("key register", reg_id);
	}



	@Override

	protected void onUnregistered(Context arg0, String arg1) {
		Log.e("key delete","Complate.");
	}


}