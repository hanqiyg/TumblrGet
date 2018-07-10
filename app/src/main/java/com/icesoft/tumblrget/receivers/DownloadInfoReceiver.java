package com.icesoft.tumblrget.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.icesoft.tumblrget.App;
import com.icesoft.tumblrget.Constant;

/**
 * @author Administrator
 */
public class DownloadInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String format = "Download complete for [ %s ].";
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equalsIgnoreCase(action)){
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
            if(id != -1L){
                String url = App.getInstance().getDownloadDao().getSourceUrlById(id);
                if(url != null){
                    Toast.makeText(context,String.format(format,url),Toast.LENGTH_LONG);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean allowSoundNotice = pref.getBoolean("notifications_new_message", Constant.DEFAULT_SOUND_NOTICE);
                    if(allowSoundNotice){
                        String ringtone = pref.getString("notifications_new_message_ringtone", Constant.DEFAULT_RINGTONE);
                        if(ringtone != null){
                            Uri uri = Uri.parse(ringtone);
                            if(uri != null){
                                Ringtone mRingtone = RingtoneManager.getRingtone(context,uri);
                                mRingtone.play();
                            }
                        }
                        boolean vibrate = pref.getBoolean("notifications_new_message_vibrate",Constant.DEFAULT_VIBRATE);
                        if(vibrate){
                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                             vibrator.vibrate(500L);
                        }
                    }
                }
            }
        }
    }
}
