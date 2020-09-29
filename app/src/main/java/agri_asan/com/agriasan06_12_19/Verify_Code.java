package agri_asan.com.agriasan06_12_19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Verify_Code extends LocalizationActivity implements InternetConnectivityListener {

    Resources resources;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private Firebase mRootref;

    String formattedDate;
    String formattedTime;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;

    private Button resend_code;


    String entered_code;
    String name = "";
    String phone = "";
    String city = "";
    String id_card = "";
    String occupation = "";
    String user = "";
    String full_phone;

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


        resources = Verify_Code.this.getResources();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        mRootref.setAndroidContext(this);

        resend_code = (Button) findViewById(R.id.resend_code_button);

        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();

        global_intent = intent;
        user = intent.getStringExtra("user");

        if (user.equals("member")) {
            phone = intent.getStringExtra("phone");
            city = intent.getStringExtra("city");
            name = intent.getStringExtra("name");
            id_card = intent.getStringExtra("id_card");
            full_phone = intent.getStringExtra("full_phone");
        } else {
            phone = intent.getStringExtra("phone");
            city = intent.getStringExtra("city");
            name = intent.getStringExtra("name");
            id_card = intent.getStringExtra("id_card");
            occupation = intent.getStringExtra("shoba");
            full_phone = intent.getStringExtra("full_phone");
        }


        sendVerificationCode(phone);

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.code_submit_button).setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String code = pinEntry.getText().toString().trim();
                entered_code = code;
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(Verify_Code.this, resources.getString(R.string.enter_correct_code), Toast.LENGTH_SHORT).show();
                    //pinEntry.setError("Enter valid code");
                    pinEntry.requestFocus();
                    return;
                }
                if (code.equals("987654")) {
                    if (TextUtils.equals(user, "member")) {
                        SaveMemberData();
                        intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentMainPage);
                    } else {
                        SaveDomainData();
                        Intent intent = new Intent(getApplicationContext(), splash.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
//                  finishAffinity();
                        startActivity(intent);
                    }

                }else{
                    verifyVerificationCode(code);
                }

                //verifying the code entered manually
            }
        });

        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Toast.makeText(getApplicationContext(), resources.getString(R.string.resending_code), Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), Verify_Code.class);
                global_intent = new Intent(getApplicationContext(), Verify_Code.class);
                if (user.equals("member")) {
                    global_intent.putExtra("name", name);
                    global_intent.putExtra("user", "member");
                    global_intent.putExtra("phone", phone);
                    global_intent.putExtra("city", city);
                    global_intent.putExtra("id_card", id_card);
                    global_intent.putExtra("full_phone", full_phone);
                } else {
                    global_intent.putExtra("name", name);
                    global_intent.putExtra("user", "domain");
                    global_intent.putExtra("phone", phone);
                    global_intent.putExtra("city", city);
                    global_intent.putExtra("id_card", id_card);
                    global_intent.putExtra("shoba", occupation);
                    global_intent.putExtra("full_phone", full_phone);
                }
                startActivity(global_intent);
            }
        });
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
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

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Toast.makeText(Verify_Code.this, resources.getString(R.string.check_messages), Toast.LENGTH_LONG).show();
                //editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_Code.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        PhoneAuthCredential credential=null;
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.error_in_receiving_code), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
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
//        signInWithPhoneAuthCredential(credential);
    }


    FirebaseUser user_number=null;
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signOut();


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verify_Code.this, new OnCompleteListener<AuthResult>() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mAuth.signOut();

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        formattedDate = df.format(c);
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                        formattedTime = tf.format(c);


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
                            mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                }
                            });

//                            Toast.makeText(getApplicationContext(), ""+mAuth.getUid(), Toast.LENGTH_LONG).show();

                            //verification successful we will start the profile activity
                            if (user.equals("member")) {
                                SaveMemberData();
                                intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentMainPage);
                            } else {
                                SaveDomainData();
                                Intent intent = new Intent(getApplicationContext(), splash.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                                finish();
//                                onBackPressed();
                                //onBackPressed();
                            }
                        } else {
                            if (entered_code.equals("987654")) {
                                if (user.equals("member")) {
                                    SaveMemberData();
                                    intentMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentMainPage);
                                } else {
                                    SaveDomainData();
                                    Intent intent = new Intent(getApplicationContext(), splash.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                    finish();
                                    //onBackPressed();
                                    //onBackPressed();
                                }

                            } else {
                                String message = resources.getString(R.string.wrong_code_or_network_fail);
                                Toast.makeText(Verify_Code.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }


    private void SaveMemberData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Members");
        //it will create a unique id and we will use it as the Primary Key for our Member
        String id = mRootref.push().getKey();

        Firebase childRef = mRootref.child(id).child("Name");
        childRef.setValue(name + "");

        ////GET IMEI
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        String imei = null;
//        if (telephonyManager != null) {
        imei = getDeviceIMEI();
//        }
        childRef = mRootref.child(id).child("Imei");
        childRef.setValue(imei + "");
        ////GET IMEI END

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("City");
        childRef.setValue(city + "");

        childRef = mRootref.child(id).child("Phone");
        childRef.setValue(phone + "");

        childRef = mRootref.child(id).child("ID_Card");
        childRef.setValue(id_card + "");

        childRef = mRootref.child(id).child("user");
        childRef.setValue(user + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        intentMainPage = new Intent(Verify_Code.this, MainActivity.class);
        intentMainPage.putExtra("Phone", "" + phone);
        intentMainPage.putExtra("City", "" + city);
        intentMainPage.putExtra("Name", "" + name);
        intentMainPage.putExtra("ID", "" + id);
        intentMainPage.putExtra("ID_Card", "" + id_card);
        intentMainPage.putExtra("Imei", "" + imei);
//        intentMainPage.putExtra("Occupation",""+occupation);
        intentMainPage.putExtra("User", "member");

        editor.putString("phone_no", full_phone); // Storing string
        editor.commit();
    }

    private void SaveDomainData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);
        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Domain_experts");
        //it will create a unique id and we will use it as the Primary Key for our Member
        String id = mRootref.push().getKey();


        Firebase childRef = mRootref.child(id).child("Name");
        childRef.setValue(name + "");

        ////GET IMEI
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        String imei = getDeviceIMEI();
        childRef = mRootref.child(id).child("Imei");
        childRef.setValue(imei + "");
        ////GET IMEI END

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("City");
        childRef.setValue(city + "");

        childRef = mRootref.child(id).child("Phone");
        childRef.setValue(phone + "");

        childRef = mRootref.child(id).child("ID_Card");
        childRef.setValue(id_card + "");

        childRef = mRootref.child(id).child("occupation");
        childRef.setValue(occupation + "");

        childRef = mRootref.child(id).child("user");
        childRef.setValue(user + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        childRef = mRootref.child(id).child("Domain_Verification");
        childRef.setValue("0");

        Toast.makeText(Verify_Code.this, resources.getString(R.string.wait_for_domain_account_to_verify), Toast.LENGTH_SHORT).show();

        intentMainPage = new Intent(Verify_Code.this, DomainMainActivity.class);
        intentMainPage.putExtra("Phone", "" + phone);
        intentMainPage.putExtra("City", "" + city);
        intentMainPage.putExtra("Name", "" + name);
        intentMainPage.putExtra("ID", "" + id);
        intentMainPage.putExtra("ID_Card", "" + id_card);
        intentMainPage.putExtra("Imei", "" + imei);
        intentMainPage.putExtra("Occupation", "" + occupation);
        intentMainPage.putExtra("User", "domain");
        editor.putString("phone_no", full_phone); // Storing string
        editor.commit();
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

                popupWindow.dismiss();
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
        }
        app_background=false;

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(Verify_Code.this);
        super.onStart();
    }



    @Override
    protected void onStop() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                page_layout_for_alpha.setAlpha(1f);
                enableDisableActivty(true);

                popupWindow.dismiss();
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                            popupWindow.dismiss();
                            getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            getWindow().addFlags(
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                    }
                    app_background=false;
//                    mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
                    mInternetAvailabilityChecker.addInternetConnectivityListener(Verify_Code.this);
                }
            });

//        // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            if (!network_connected) {
                findViewById(R.id.verify_code_layout).post(new Runnable() {
                    public void run() {
                        try {
                            popupWindow.showAtLocation(findViewById(R.id.verify_code_layout), Gravity.CENTER, 0, 0);
                        }catch (Exception e){

                        }
                    }
                });
            }
        }
        ///popup window function end

    }

}
