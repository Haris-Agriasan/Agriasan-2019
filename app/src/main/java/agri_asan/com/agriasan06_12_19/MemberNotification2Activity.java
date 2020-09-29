package agri_asan.com.agriasan06_12_19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MemberNotification2Activity extends FirebaseMessagingService  {


    String body="";
    String title="";
    String ID="";
    DatabaseReference reference;


    MainActivity mainActivity;

    String Phone="";



    String notification_phone="";

    Intent intent;
    SharedPreferences pref;

    ArrayList<String> list=new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.i("LOG CHECK","Checkdcdcdcdcddddddddddddddddddd 1");

        Phone="";
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Phone=pref.getString("Phone_notification", null); // getting String

//      Phone="3244601545";
        reference = FirebaseDatabase.getInstance().getReference();

        notification_phone=remoteMessage.getData().get("Phone");

        ID = remoteMessage.getData().get("ID");

        if (notification_phone != null && notification_phone.equals(Phone) && !list.contains(ID) ){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                body = remoteMessage.getData().get("body");
                title = remoteMessage.getData().get("title");
                ID = remoteMessage.getData().get("ID");
                list.add(ID);

                add_notify(body+"", title+"",ID+"");

            }else{

                body = remoteMessage.getData().get("body");
                title = remoteMessage.getData().get("title");
                ID = remoteMessage.getData().get("ID");
                list.add(ID);
                addNotification(body+"", title+"",ID+"");
            }
        }


    }








    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }

    /////////for less than O version
    private void addNotification(String body,String title, String ID) {
//        fasal="("+fasal+"/"+shoba+")";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo_fit_2) //set icon for notification
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(Notification.PRIORITY_HIGH) //set priority of notification
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setLargeIcon(largeIcon)
                        .setDefaults(Notification.DEFAULT_SOUND);
//                      .setDefaults(Notification.DEFAULT_ALL);


        Intent notificationIntent = new Intent(this, LogoSplashActivity.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
//        notificationIntent.putExtra("message", "This is a notification message");

//        editor = pref.edit();
        notificationIntent.putExtra("notify_id", ""+ID);
//        editor.putString("notify_id", notify_id); // Storing string
//        editor.apply();
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent,0);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(m, builder.build());
        }
    }
    /////////for less than O version END

    /////////NOTIFICATION ON PHONE Android O and Greater
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_notify(String body,String title, String ID){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = "my_channel_01";
        // The user-visible name of the channel.
        CharSequence name = getString(R.string.app_name);
        // The user-visible description of the channel.
        String description = getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
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
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        editor = pref.edit();
        notificationIntent.putExtra("notify_id", ""+ID);
//        editor.putString("notify_id", notify_id); // Storing string
//        editor.apply();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent,0);
        ///////////CLASS ON CLICK ON NOTIFICATION END
//        fasal="("+fasal+"/"+shoba+")";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(this)
//                .setContentTitle(message.getTitle())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.logo_fit_2)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setChannelId(CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        // Issue the notification.

        //(int) System.currentTimeMillis()
        mNotificationManager.notify(m, notification);
    }
    ////////NOTIFICATION ON PHONE Android O and Greater END


}


//        FirebaseMessaging.getInstance().subscribeToTopic("questions")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        body = remoteMessage.getData().get("body");
//                        title = remoteMessage.getData().get("title");
//                        ID = remoteMessage.getData().get("ID");
//                        list.add(ID);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            add_notify("Test", "dddd","dcvdcd");
//                        }else{
//                            addNotification(body+"", title+"",ID+"");
//                        }
//                        //String msg = getString(R.string.msg_subscribed);
//                        //if (!task.isSuccessful()) {
//                        //msg = getString(R.string.msg_subscribe_failed);
//                        //}
//                        //Log.d(TAG, msg);
//                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });