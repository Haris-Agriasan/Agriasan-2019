package agri_asan.com.agriasan06_12_19;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import android.content.Intent;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class FirebaseBackgroundService extends Service {
    private DatabaseReference f;
    private ValueEventListener handler;
    private Timer mTimer;

    Intent intent;

    MainActivity mainActivity;
    DatabaseReference reference;


    String Phone;
    List<String> keys;
    DatabaseReference databaseReference;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public String notify_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        if (intent != null && intent.getExtras() != null){
            Phone = intent.getStringExtra("Phone");
            notify_id= intent.getStringExtra("notify_id");
        }

        Log.e("Log", ""+Phone);



        reference = FirebaseDatabase.getInstance().getReference();

        keys=new ArrayList<>();



        f= FirebaseDatabase.getInstance().getReference();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 5000, 2 * 1000);

//        Log.e("Log", "Runningjscbjscjsncjdn");
        Firebase.setAndroidContext(this);
        //f=new Firebase("https://agriasan-6b704.firebaseio.com/");


//        handler = new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onDataChange(DataSnapshot arg0) {
//                Log.e("Log", "Runningjscbjscjsncjdn");
//
////                postNotif(arg0.getValue().toString());
//                add_notify(arg0.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//
//        };

//        f.addValueEventListener(handler);

    }
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
//            f.addValueEventListener(handler);
            if (intent != null && intent.getExtras() != null){
                Phone = intent.getStringExtra("Phone");
                notify_id= intent.getStringExtra("notify_id");
            }
            reference = FirebaseDatabase.getInstance().getReference();
            reference.keepSynced(true);

            Log.e("Log", "Runninggggggggggggggggggggggggg");
            final Query query = reference;
            query.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Questions").exists()){
                        dataSnapshot.getChildren();
                        int co=0;
                        for (DataSnapshot issue : dataSnapshot.child("Questions").getChildren()) {
                            Member Question = issue.getValue(Member.class);
                            String id_ck="";
                            if (dataSnapshot.child("Questions").child(Question.getID()+"").child("Answer").exists())
                            {
                                Log.e("Log", "Check 1");

                                co++;
//                            Toast.makeText(FirebaseBackgroundService.this,""+Phone, Toast.LENGTH_SHORT).show();
                                for (DataSnapshot issue_2 : dataSnapshot.child("Questions").getChildren()) {
//                                    Log.e("Log", "Check 2");

                                    Member question = issue_2.getValue(Member.class);
//                        Toast.makeText(FirebaseBackgroundService.this,"Check 2", Toast.LENGTH_SHORT).show();

                                    String phone_db="";
                                    String read="";
                                    String notify="";
                                    if (!TextUtils.isEmpty(question.getMember_Phone()))
                                    {
                                        phone_db=question.getMember_Phone();
                                    }
                                    if (!TextUtils.isEmpty(question.getRead()))
                                    {
                                        read=question.getRead();
                                    }
                                    if (!TextUtils.isEmpty(question.getNotify()))
                                    {
                                        notify=question.getNotify();
                                    }

                                    if (phone_db.equals(Phone) && read.equals("") && notify.equals("")){
                                        Log.e("Log", phone_db);
                                        Log.e("Log", "Check 3");
                                        Log.e("Log", ""+question.getAnswer());
                                        Log.e("Log", ""+question.getTime());
                                        Log.e("Log", ""+question.getID());
                                        if (!TextUtils.isEmpty(question.getAnswer())){
                                            Log.e("Log", "Check 4");

                                            boolean chk_key=true;
                                            for (int i=0;i<keys.size();i++){
                                                if (keys.get(i).equals(question.getID())){
                                                    chk_key=false;
                                                    break;
                                                }
                                                else{
                                                    chk_key=true;
                                                }
                                            }
                                            if (chk_key){
                                                Log.e("Log", "Check 5");
                                                databaseReference= FirebaseDatabase.getInstance().getReference()
                                                        .child("Questions").child(question.getID());
                                                databaseReference.child("Notify").setValue("yes");
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    keys.add(question.getID());
                                                    add_notify(question.getMember_Name(),
                                                            question.getFasal()
                                                            ,question.getFasal_Type_Shoba(), question.getID());
                                                }else{
                                                    keys.add(question.getID());
                                                    addNotification(question.getMember_Name(),
                                                            question.getFasal()
                                                            ,question.getFasal_Type_Shoba(), question.getID());
                                                }
                                            }

                                        }
                                    }
//                        }
                                }
//
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        if (intent != null && intent.getExtras() != null){
            Phone = intent.getStringExtra("Phone");
            notify_id= intent.getStringExtra("notify_id");
        }
//        Phone = intent.getStringExtra("Phone");
        return START_STICKY;
    }
    /////////NOTIFICATION ON PHONE Android O and Greater
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_notify(String mem_name, String fasal, String shoba, String n_id){
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
        editor = pref.edit();
        notificationIntent.putExtra("notify_id", ""+n_id);
        editor.putString("notify_id", notify_id); // Storing string
        editor.apply();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent,0);
        ///////////CLASS ON CLICK ON NOTIFICATION END
        fasal="("+fasal+"/"+shoba+")";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(this)
                .setContentTitle(mem_name+" ! آپکے سوال کا جواب آ چکا ہے ")
                .setContentText(fasal+" کے سوال کی تفصیل دیکھنے کے لیے کلک کریں ")
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
    /////////for less than O version
    private void addNotification(String mem_name, String fasal, String shoba, String n_id) {
        fasal="("+fasal+"/"+shoba+")";
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo_fit_2) //set icon for notification
                        .setContentTitle(mem_name+" ! آپکے سوال کا جواب آ چکا ہے ")
                        .setContentText(fasal+" کے سوال کی تفصیل دیکھنے کے لیے کلک کریں ")
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

        editor = pref.edit();
        notificationIntent.putExtra("notify_id", ""+n_id);
        editor.putString("notify_id", notify_id); // Storing string
        editor.apply();
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent,0);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(m, builder.build());
    }
    /////////for less than O version END

    //    private void postNotif(String notifString) {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        int icon = R.drawable.notification_new;
//        Notification notification = new Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
////		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        Context context = getApplicationContext();
//        CharSequence contentTitle = "Background" + Math.random();
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, contentTitle, notifString, contentIntent);
//        mNotificationManager.notify(1, notification);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void add_notify(String string){
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // The id of the channel.
//        String id = "my_channel_01";
//        // The user-visible name of the channel.
//        CharSequence name = getString(R.string.app_name);
//        // The user-visible description of the channel.
//        String description = getString(R.string.app_name);
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(id, name,importance);
//        // Configure the notification channel.
//        mChannel.setDescription(description);
//        mChannel.enableLights(true);
//        // Sets the notification light color for notifications posted to this
//        // channel, if the device supports this feature.
//        mChannel.setLightColor(Color.GREEN);
//        mChannel.enableVibration(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        assert mNotificationManager != null;
//        mNotificationManager.createNotificationChannel(mChannel);
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// Sets an ID for the notification, so it can be updated.
//        int notifyID = 1;
//// The id of the channel.
//        String CHANNEL_ID = "my_channel_01";
//        ///////////CLASS ON CLICK ON NOTIFICATION
//        Intent notificationIntent = new Intent(this, LogoSplashActivity.class);
////        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        editor = pref.edit();
////        notificationIntent.putExtra("notify_id", ""+n_id);
////        editor.putString("notify_id", notify_id); // Storing string
////        editor.apply();
//
//        Random random = new Random();
//        int m = random.nextInt(9999 - 1000) + 1000;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent,0);
//        ///////////CLASS ON CLICK ON NOTIFICATION END
////        fasal="("+fasal+"/"+shoba+")";
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);
//        // Create a notification and set the notification channel.
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle(string)
//                .setContentText("SUB TEST")
//                .setSmallIcon(R.mipmap.logo_fit_2)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setChannelId(CHANNEL_ID)
//                .setLargeIcon(largeIcon)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build();
//        // Issue the notification.
//
//        //(int) System.currentTimeMillis()
////        startForeground(m, notification);
//        mNotificationManager.notify(m, notification);
//    }
}


//        final Query query = reference;
//        query.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    add_notify(dataSnapshot.getValue().toString());
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        f= FirebaseDatabase.getInstance().getReference();