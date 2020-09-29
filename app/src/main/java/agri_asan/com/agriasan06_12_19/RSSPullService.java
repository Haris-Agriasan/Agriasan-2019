package agri_asan.com.agriasan06_12_19;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.RequiresApi;

public class RSSPullService extends IntentService {
    DatabaseReference reference;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RSSPullService(String name) {
        super(name);
    }
    public RSSPullService(){
        super("");

    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        reference = FirebaseDatabase.getInstance().getReference();
        for (int i=0;i<100;i++) {
            Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
        }
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        toast();
                    }
                }, 1000L);
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
    }

    public void toast(){
        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();

    }
    /////////NOTIFICATION ON PHONE
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_notify(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // The id of the channel.
        String id = "my_channel_01";

        // The user-visible name of the channel.
        CharSequence name = getString(R.string.title_home);

        // The user-visible description of the channel.
        String description = getString(R.string.title_subsidy);

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id, name,importance);

        // Configure the notification channel.
        mChannel.setDescription(description);

        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.GREEN);

        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        assert mNotificationManager != null;
        mNotificationManager.createNotificationChannel(mChannel);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Sets an ID for the notification, so it can be updated.
        int notifyID = 1;
// The id of the channel.
        String CHANNEL_ID = "my_channel_01";


        ///////////CLASS ON CLICK ON NOTIFICATION
        Intent notificationIntent = new Intent(this, LogoSplashActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        ///////////CLASS ON CLICK ON NOTIFICATION END

        //////////
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(this)
                .setContentTitle("آپکے سوال کا جواب آ چکا ہے !")
                .setContentText("تفصیل دیکھنے کے لیے کلک کریں")
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .build();

// Issue the notification.
        mNotificationManager.notify(1, notification);
    }
    ////////NOTIFICATION ON PHONE END
}