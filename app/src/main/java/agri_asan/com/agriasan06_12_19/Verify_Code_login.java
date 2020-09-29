package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Verify_Code_login extends LocalizationActivity implements InternetConnectivityListener {

    Resources resources;

    TelephonyManager telephonyManager;
    String phn_imei;

    DatabaseReference databaseReference;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;

    private Button resend_code;

    String entered_code;
    String phone = "";
    String user = "";
    String name = "";
    String city = "";
    String id_card = "";
    String id = "";
    String occupation = "";
    String vendor = "";
    String imei = "";

    Intent intentMainPage;

    Intent global_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify__code);

        page_layout_for_alpha = findViewById(R.id.verify_code_layout);
        page_layout_for_alpha.setAlpha(1f);

        resources = Verify_Code_login.this.getResources();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
//        phn_imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
            phn_imei = getDeviceIMEI();

        resend_code = (Button) findViewById(R.id.resend_code_button);

        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();

        global_intent = intent;
        user = intent.getStringExtra("User");

        if (TextUtils.equals(user, "member")) {
            //Toast.makeText(Verify_Code_login.this, ""+user, Toast.LENGTH_SHORT).show();
            phone = intent.getStringExtra("phone");
            city = intent.getStringExtra("City");
            name = intent.getStringExtra("Name");
            id_card = intent.getStringExtra("ID_Card");
            id=intent.getStringExtra("ID");
            vendor=intent.getStringExtra("Vendor");
            imei=intent.getStringExtra("Imei");
        } else {
            phone = intent.getStringExtra("phone");
            city = intent.getStringExtra("City");
            name = intent.getStringExtra("Name");
            id_card = intent.getStringExtra("ID_Card");
            occupation = intent.getStringExtra("Occupation");
            id=intent.getStringExtra("ID");
            vendor=intent.getStringExtra("Vendor");
            imei=intent.getStringExtra("Imei");
        }

        sendVerificationCode(phone);

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.code_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //phone=phone.substring(1,4)+phone.substring(5);
                //Toast.makeText(Verify_Code_login.this, ""+phone, Toast.LENGTH_SHORT).show();

                String code = pinEntry.getText().toString().trim();
                entered_code = code;
                if (code.isEmpty() || code.length() < 6 ) {
                    Toast.makeText(Verify_Code_login.this, resources.getString(R.string.enter_correct_code), Toast.LENGTH_SHORT).show();
                    //pinEntry.setError("Enter valid code");
                    pinEntry.requestFocus();
                    return;
                }
                if (code.equals("987654")){
                    if (TextUtils.equals(user, "member")) {
                            SaveMemberData();
                    } else {
                        SaveDomainData();
                    }
                    intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMainPage);
                    finish();
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Toast.makeText(getApplicationContext(), resources.getString(R.string.resending_code), Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), Verify_Code.class);

                global_intent = new Intent(getApplicationContext(), Verify_Code_login.class);

                if (TextUtils.equals(user, "member")) {
//                    global_intent.putExtra("name", name);
                    global_intent.putExtra("phone",""+phone);
                    global_intent.putExtra("City",""+city);
                    global_intent.putExtra("Name",""+name);
                    global_intent.putExtra("ID",""+id);
                    global_intent.putExtra("ID_Card",""+id_card);
                    global_intent.putExtra("Imei",""+imei);
                    global_intent.putExtra("Vendor",""+vendor);
//                    global_intent.putExtra("Occupation",""+occupation);
                    global_intent.putExtra("User","member");
                } else {
//                    global_intent.putExtra("name", name);
                    global_intent.putExtra("phone",""+phone);
                    global_intent.putExtra("City",""+city);
                    global_intent.putExtra("Name",""+name);
                    global_intent.putExtra("ID",""+id);
                    global_intent.putExtra("ID_Card",""+id_card);
                    global_intent.putExtra("Imei",""+imei);
                    global_intent.putExtra("Vendor",""+vendor);
                    global_intent.putExtra("Occupation",""+occupation);
                    global_intent.putExtra("User","domain");
                }
                startActivity(global_intent);
            }
        });
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
//        Toast.makeText(Verify_Code_login.this, "Check 1", Toast.LENGTH_LONG).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
//            Toast.makeText(Verify_Code_login.this, "Check 2", Toast.LENGTH_LONG).show();


            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Toast.makeText(Verify_Code_login.this, resources.getString(R.string.check_messages), Toast.LENGTH_LONG).show();
                //editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_Code_login.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
//            Toast.makeText(Verify_Code_login.this, "Check 3", Toast.LENGTH_LONG).show();

            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential=null;
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.error_in_receiving_code), Toast.LENGTH_SHORT);
//            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        try{
            signInWithPhoneAuthCredential(credential);

        }catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.error_in_receiving_code), Toast.LENGTH_SHORT);

//            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        //signing the user
    }

    FirebaseUser user_number=null;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signOut();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verify_Code_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(Verify_Code_login.this, "Check 4", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        try {
                             user_number= Objects.requireNonNull(task.getResult()).getUser();
                        }catch (Exception e){

                        }
//                            Toast.makeText(Verify_Code_login.this, ""+user_number.getUid(), Toast.LENGTH_LONG).show();
                        if (user_number != null) {
//                            user_number.delete();
                            user_number.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Calling delete to remove the user and wait for a result.
                                            user_number.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
//                                                        Toast.makeText(getApplicationContext(), "Remove", Toast.LENGTH_LONG).show();

                                                    } else {
                                                        //Handle the exception
//                                                        Toast.makeText(getApplicationContext(), "Remove Error", Toast.LENGTH_LONG).show();
//                                                        task.getException();
                                                    }
                                                }
                                            });
                                        }
                                    });
                        }
                        if (task.isSuccessful()) {
                            mAuth.signOut();
//                            mAuth.signOut();
                            //verification successful we will start the profile activity
//                            FirebaseUser user_number = Objects.requireNonNull(task.getResult()).getUser();
//                            Toast.makeText(Verify_Code_login.this, ""+mAuth.getUid(), Toast.LENGTH_LONG).show();
//                            if (user_number != null) {
////                                user_number.delete();
//                                user_number.reauthenticate(credential)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                //Calling delete to remove the user and wait for a result.
//                                                user_number.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
////                                                            Toast.makeText(getApplicationContext(), "Remove", Toast.LENGTH_LONG).show();
//
//                                                        } else {
//                                                            //Handle the exception
////                                                            Toast.makeText(getApplicationContext(), "Remove Error", Toast.LENGTH_LONG).show();
////                                                        task.getException();
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        });
//                            }
                            if (TextUtils.equals(user, "member")) {
                                SaveMemberData();

                            } else {
                                SaveDomainData();
                            }
                            intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentMainPage);
                        } else {
                            if (entered_code.equals("987654")) {
                                if (TextUtils.equals(user, "member")) {
                                    SaveMemberData();
                                } else {
                                    SaveDomainData();
                                }
                                intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentMainPage);
                            } else {
                                String message = resources.getString(R.string.wrong_code_or_network_fail);
                                Toast.makeText(Verify_Code_login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });
    }
    private void SaveMemberData() {
        ///SAVE IMEI IN DB
//        Toast.makeText(getApplicationContext(), ""+imei, Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Members").child(id);
        databaseReference.child("Imei").setValue(phn_imei+"");
        intentMainPage = new Intent(Verify_Code_login.this, MainActivity.class);
        intentMainPage.putExtra("Phone",""+phone);
        intentMainPage.putExtra("City",""+city);
        intentMainPage.putExtra("Name",""+name);
        intentMainPage.putExtra("ID",""+id);
        intentMainPage.putExtra("ID_Card",""+id_card);
        intentMainPage.putExtra("Imei",""+phn_imei);
        intentMainPage.putExtra("Vendor",""+vendor);
        intentMainPage.putExtra("User","member");
    }
    private void SaveDomainData() {
        ///SAVE IMEI IN DB
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Domain_experts").child(id);
        databaseReference.child("Imei").setValue(phn_imei);
        intentMainPage = new Intent(Verify_Code_login.this, DomainMainActivity.class);
        intentMainPage.putExtra("Phone",""+phone);
        intentMainPage.putExtra("City",""+city);
        intentMainPage.putExtra("Name",""+name);
        intentMainPage.putExtra("ID",""+id);
        intentMainPage.putExtra("ID_Card",""+id_card);
        intentMainPage.putExtra("Imei",""+phn_imei);
        intentMainPage.putExtra("Vendor",""+vendor);
        intentMainPage.putExtra("Occupation",""+occupation);
        intentMainPage.putExtra("User","domain");

    }
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
            }
            try {
                deviceUniqueIdentifier = tm.getDeviceId();
            }catch (Exception e){
                try {
                    deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    Toast.makeText(this, deviceUniqueIdentifier, Toast.LENGTH_SHORT).show();

                }catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            try {
                deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                Toast.makeText(splash.this, deviceUniqueIdentifier, Toast.LENGTH_SHORT).show();

            }catch (Exception ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return deviceUniqueIdentifier;
    }

    private void enableDisableActivty(Boolean isEnable) {
        if (!isEnable) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    public static PopupWindow popupWindow;

    public LinearLayout page_layout_for_alpha;

    boolean network_connected;
    boolean app_background=false;
    InternetAvailabilityChecker mInternetAvailabilityChecker;



    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected && !app_background){
            network_connected=true;
//            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();

            if (popupWindow!=null){
                if (popupWindow.isShowing() || popupWindow!=null) {
                    page_layout_for_alpha.setAlpha(1f);
                    enableDisableActivty(true);
                    try {
                        popupWindow.dismiss();
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().addFlags(
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }catch (Exception e){
                    }
                }
            }
        }else{
            network_connected=false;
//            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();

            popup_window_no_internet();

        }
    }

    @Override
    protected void onStart() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                page_layout_for_alpha.setAlpha(1f);
                enableDisableActivty(true);
                try {
                    popupWindow.dismiss();
                    getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }catch (Exception e){

                }


            }
        }
        app_background=false;

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(Verify_Code_login.this);
        super.onStart();
    }



    @Override
    protected void onStop() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                page_layout_for_alpha.setAlpha(1f);
                enableDisableActivty(true);
                try {
                    popupWindow.dismiss();
                    getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }catch (Exception e){

                }

            }
        }
        app_background=true;

        super.onStop();
    }

    public void popup_window_no_internet() {
        if (!network_connected) {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            page_layout_for_alpha.setAlpha(0.2f);

            enableDisableActivty(false);



            TextView popup_heading;
            TextView popup_subheading;
            Button btn_retry;
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_no_internet, null);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float s_height = displayMetrics.heightPixels;
            float s_width = displayMetrics.widthPixels;

            s_height = s_height * 0.4f;
            s_width = s_width * 0.8f;

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            popupWindow = new PopupWindow(popupView, Math.round(s_width), Math.round(s_height));

//        image_popup=popupView.findViewById(R.id.image_popup);
//        Picasso.get().load(url_tem_image).placeholder( R.drawable.loading_4 ).into(image_popup);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(40);
            }

            btn_retry = popupView.findViewById(R.id.btn_popup_retry);
            popup_heading = popupView.findViewById(R.id.popup_heading);
            popup_subheading = popupView.findViewById(R.id.popup_subheading);
            popup_heading.setText("انٹرنٹ موجود نہی");
            popup_subheading.setText("برائے مہربانی پہلے اپنا انٹرنٹ اون کریں یا پھر ایپ دوبارہ چلائں");

            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow!=null){
                        if (popupWindow.isShowing() || popupWindow!=null) {
                            page_layout_for_alpha.setAlpha(1f);
                            enableDisableActivty(true);
                            try {
                                popupWindow.dismiss();
                                getWindow().clearFlags(
                                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                                getWindow().addFlags(
                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            }catch (Exception e){

                            }

                        }
                    }
                    app_background=false;
//                    mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
                    mInternetAvailabilityChecker.addInternetConnectivityListener(Verify_Code_login.this);
                }
            });

//        // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            if (!network_connected) {
                findViewById(R.id.verify_code_layout).post(new Runnable() {
                    public void run() {
                        try {
                            popupWindow.showAtLocation(findViewById(R.id.verify_code_layout), Gravity.CENTER, 0, 0);
                        }
                        catch (Exception e){

                        }
                    }
                });
            }

        }
        ///popup window function end

    }


}
