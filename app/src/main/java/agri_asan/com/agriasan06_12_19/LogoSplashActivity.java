package agri_asan.com.agriasan06_12_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogoSplashActivity extends AwesomeSplash implements InternetConnectivityListener {

    Boolean chk_permission=false;
    int i=0;
    int PERMISSION_ALL = 1;


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    DatabaseReference reference;

    String Vendor="";
    String City="";
    String Name="";
    String Phone="";
    String ID_Card="";
    String ID="";
    String Occupation="";

    String Imei="";

    String entered_phn="";

    String a="";

    @Override
    public void initSplash(ConfigSplash configSplash) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        InternetAvailabilityChecker.init(this);


        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
        //////https://github.com/ViksaaSkool/AwesomeSplash
        //
        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.white); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
//        configSplash.setAnimLogoSplashTechnique(Techniques.Pulse); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
//

//        //Customize Path
        configSplash.setLogoSplash(R.drawable.logo_title_no_back);

        //Customize Title
        configSplash.setTitleSplash("ایگری آسان");
        configSplash.setTitleTextColor(R.color.splash_text);
        configSplash.setAnimTitleDuration(300);
//        configSplash.setAnimTitleTechnique(Techniques.StandUp);

        notify_intent=getIntent();

        notify_id=notify_intent.getStringExtra("notify_id");


    }
    Intent notify_intent;
    String notify_id;
    InternetAvailabilityChecker mInternetAvailabilityChecker;

    public void proceed_if_connected(){
        reference = FirebaseDatabase.getInstance().getReference();

        progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("مہربانی انتظار کریں!!!");
        progressDialog.setMessage(this.getResources().getString(R.string.please_wait));


        Objects.requireNonNull(progressDialog.getWindow()).setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
        params.y = 100;
        progressDialog.getWindow().setAttributes(params);


        progressDialog.setCancelable(false);


        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        editor.putString("Phone_notification", ""); // Storing
        editor.apply();

        if (pref.contains("phone_no")){
            a=pref.getString("phone_no", null); // getting String
        }else{
            a="";
        }
        permission_prompt();
    }
    @Override
    public void animationsFinished() {
        //        network_connected=true;

        InternetAvailabilityChecker.init(this);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

//        if (Build.VERSION.SDK_INT < 16) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
        proceed_if_connected();
    }
    private void UserLoginPhone(){
//        Toast.makeText(getApplicationContext(), "CHeck 1", Toast.LENGTH_LONG).show();

        entered_phn=a;
        try {
            progressDialog.show();
        }catch (Exception e){

        }
        if( (TextUtils.isEmpty(entered_phn) || entered_phn.length()<12)){
            progressDialog.cancel();
            //editText_Phone.setError( "پہلے اپنانمبردرج کریں!" );
            Toast.makeText(getApplicationContext(), "اپنا درست نمبر درج کریں!", Toast.LENGTH_LONG).show();
            Intent intent_splash = new Intent(getApplicationContext(), splash.class);
            finishAffinity();
                startActivity(intent_splash);
        }
        else{
            entered_phn=entered_phn.substring(1,4)+entered_phn.substring(5);
            final Query query = reference.child("Members").orderByChild("Phone").equalTo(entered_phn);
            if (network_connected){

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(getApplicationContext(), "CHeck 2", Toast.LENGTH_LONG).show();

                    if (dataSnapshot.exists() && network_connected) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            Member user= issue.getValue(Member.class);
                            Name=user.getName();
                            City=user.getCity();
                            Phone=user.getPhone();
                            ID=user.getID();
                            ID_Card=user.getID_Card();
                            Vendor=user.getVendor();
                            Imei=user.getImei();
                            Toast.makeText(getApplicationContext(), "خو ش آمدید " +Name, Toast.LENGTH_LONG).show();
                            // do something with the individual "issues"
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("Name", Name);
                            intent.putExtra("User", "member");
                            intent.putExtra("Phone", Phone);
                            intent.putExtra("City", City);
                            intent.putExtra("ID_Card", ID_Card);
                            intent.putExtra("ID", ID);
                            intent.putExtra("Vendor", Vendor);
                            intent.putExtra("Imei", Imei);
                            intent.putExtra("notify_id",notify_id);
                            progressDialog.cancel();
                                finishAffinity();
                                startActivity(intent);
                        }
                    }else {
                        final Query query = reference.child("Domain_experts").orderByChild("Phone").equalTo(entered_phn);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                        Domain_experts user= issue.getValue(Domain_experts.class);
                                        if (!TextUtils.isEmpty(user.getDomain_Verification())) {
                                            if (user.getDomain_Verification().equals("1") && network_connected){
                                        Name=user.getName();
                                        City=user.getCity();
                                        Phone=user.getPhone();
                                        ID=user.getID();
                                        ID_Card=user.getID_Card();
                                        Occupation=user.getOccupation();
                                        Imei=user.getImei();
                                        Toast.makeText(getApplicationContext(), "خو ش آمدید " +Name, Toast.LENGTH_LONG).show();
                                        // do something with the individual "issues"
                                        Intent intent = new Intent(getApplicationContext(), DomainMainActivity.class);
                                        intent.putExtra("Name", Name);
                                        intent.putExtra("User", "domain");
                                        intent.putExtra("Phone", Phone);
                                        intent.putExtra("City", City);
                                        intent.putExtra("ID_Card", ID_Card);
                                        intent.putExtra("ID", ID);
                                        intent.putExtra("Occupation", Occupation);
                                        intent.putExtra("Vendor", Vendor);
                                        intent.putExtra("Imei", Imei);
                                        progressDialog.cancel();
                                            finishAffinity();
                                            startActivity(intent);

                                    }else{
                                                Toast.makeText(getApplicationContext(), "ابھی آپکا اکاؤنٹ تصدیق نہی ہوا مہربانی انتظار کریں!" +Name, Toast.LENGTH_LONG).show();
                                                Intent intent_splash = new Intent(getApplicationContext(), splash.class);
                                                    finishAffinity();
                                                    startActivity(intent_splash);
                                            }
                                        }
                                    }
                                } else {
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "مہربانی  پہلے خود کو رجسٹر کروایں!", Toast.LENGTH_LONG).show();
                                    Intent intent_splash = new Intent(getApplicationContext(), splash.class);
                                        finishAffinity();
                                        startActivity(intent_splash);

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }else{
                progressDialog.cancel();
//                Toast.makeText(getApplicationContext(), "مہربانی  پہلے خود کو رجسٹر کروایں!", Toast.LENGTH_LONG).show();
                Intent intent_splash = new Intent(getApplicationContext(), splash.class);
                finishAffinity();
                startActivity(intent_splash);
            }


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean deny=true;

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (deny){
                chk_permission=true;
            }
        } else {
            deny=false;
            Toast.makeText(LogoSplashActivity.this, "پرمیشن دیے بغیر آپ استمعال نہی کر سکتے", Toast.LENGTH_SHORT).show();
            finishAffinity();
            System.exit(0);
            //If user presses deny
        }
        if (chk_permission){
            Toast.makeText(LogoSplashActivity.this, "شکریہ", Toast.LENGTH_SHORT).show();

            if (!TextUtils.isEmpty(a)) {
                UserLoginPhone();
            }else{
                Intent intent = new Intent(getApplicationContext(), splash.class);
                    finishAffinity();
                    startActivity(intent);

            }
        }

    }

    public void permission_prompt(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        // No explanation needed, we can request the permission.
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED))
        {
            chk_permission=false;
//            Toast.makeText(LogoSplashActivity.this, "record_audio", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }else{
            i++;
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED))
        {
//            Toast.makeText(LogoSplashActivity.this, "camera", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }else{
            i++;
            if (chk_permission){
                chk_permission=true;
            }
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))
        {
//            Toast.makeText(LogoSplashActivity.this, "write_storage", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            i++;
            if (chk_permission){
                chk_permission=true;
            }
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))
        {
//            Toast.makeText(LogoSplashActivity.this, "coarse_location", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }else{
            i++;
            if (chk_permission){
                chk_permission=true;
            }
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))
        {
//            Toast.makeText(LogoSplashActivity.this, "fine", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }else{
            i++;
            if (chk_permission){
                chk_permission=true;
            }
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED))
        {
//            Toast.makeText(LogoSplashActivity.this, "phn state", Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }else{
            i++;
            if (chk_permission){
                chk_permission=true;
            }
        }
//                    Toast.makeText(LogoSplashActivity.this, ""+i, Toast.LENGTH_SHORT).show();
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_ALL);
        }else{
            //!TextUtils.isEmpty(a)
            if (!a.equals("")) {
//                Toast.makeText(getApplicationContext(), "CHeck", Toast.LENGTH_LONG).show();
                UserLoginPhone();
            }else{
                Intent intent = new Intent(getApplicationContext(), splash.class);
                    finishAffinity();
                    startActivity(intent);

            }
        }
    }


    boolean network_connected=true;
    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected){
            network_connected=true;
//            InternetAvailabilityChecker.init(this);
//
//
//            mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
//            mInternetAvailabilityChecker.addInternetConnectivityListener(this);
//            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
//            proceed_if_connected();
//            animationsFinished();
        }else{
            network_connected=false;
//            InternetAvailabilityChecker.init(this);
//
//
//            mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
//            mInternetAvailabilityChecker.addInternetConnectivityListener(this);
//            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
//            View v=new View();

//            popup_window();
        }
    }


    @Override
    protected void onDestroy() {
//        mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
        super.onDestroy();
    }


}
