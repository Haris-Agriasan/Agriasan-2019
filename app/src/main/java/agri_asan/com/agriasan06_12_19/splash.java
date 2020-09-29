package agri_asan.com.agriasan06_12_19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Cache;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.firebase.client.Firebase;
//import com.firebase.client.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.Query;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class splash extends LocalizationActivity implements InternetConnectivityListener {

    boolean check_verify_code=true;

    RelativeLayout splash_view;

    RelativeLayout change_lang;
    //cache
    int i = 0;

    Boolean phn_change=false;

    String phn_no_cache = "";

    Boolean chk_permission = false;
    ProgressDialog progressDialog;


    EditText editText_Phone;
    Button button_register_Member;
    Button button_register_Domain;
    Button button_phone_login;

    ImageView image;

//    private Firebase mRootref;
    DatabaseReference reference;

    String Vendor = "";
    String City = "";
    String Name = "";
    String Phone = "";
    String ID_Card = "";
    String ID = "";
    String Occupation = "";

    String Imei = "";
    String User;

    String entered_phn;

    int check =0;

    String data;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Set<String> set_temporary_shoba = new HashSet<String>();
    Set<String> set_temporary_fasal = new HashSet<String>();
    int PERMISSION_ALL = 1;

    InternetAvailabilityChecker mInternetAvailabilityChecker;

    View parent;
    //    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        InternetAvailabilityChecker.init(this);



        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(splash.this);

        check=0;

        splash_view=findViewById(R.id.splash_view);
        parent=getWindow().getDecorView().findViewById(android.R.id.content);





//        splash.this.setLanguage("en");

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return; } }
//        imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
        imei=getDeviceIMEI();
        change_lang = findViewById(R.id.change_lang);
        change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (splash.this.getCurrentLanguage().getLanguage().toString().equals("ur")){
                    splash.this.setLanguage("en");
                }else{
                    splash.this.setLanguage("ur");
                }
            }
        });
        permission_prompt();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        //////ADDING ALL THE ITEMS FOR AWAMI RAYE PAGE
        set_temporary_fasal.add("کپاس");
        set_temporary_fasal.add("گندم");
        set_temporary_fasal.add("چاول");
        set_temporary_fasal.add("مکئی");
        set_temporary_fasal.add("گنا");
        set_temporary_fasal.add("دالیں");
//        set_temporary_fasal.add("پولٹری");
//        set_temporary_fasal.add("مچھلی");
        set_temporary_fasal.add("سبزیاں");
        set_temporary_fasal.add("سورج مکھی");
        set_temporary_fasal.add("سرسوں");
//        set_temporary_fasal.add("جانور");

        editor.putStringSet("choosed_fasal", set_temporary_fasal); // Storing string Set
        editor.apply();

        set_temporary_shoba.add("مٹی");
        set_temporary_shoba.add("محکمہ انھار");
        set_temporary_shoba.add("محکمہ توسیع");
        set_temporary_shoba.add("کھاد");
        set_temporary_shoba.add("ادویات");
        set_temporary_shoba.add("بیج");
        set_temporary_shoba.add("کیڑے");
        set_temporary_shoba.add("جڑی بوٹیاں");
        set_temporary_shoba.add("ٹنل فارمنگ");
        editor.putStringSet("choosed_shoba", set_temporary_shoba); // Storing string Set
        editor.commit();
        //////ADDING ALL THE ITEMS FOR AWAMI RAYE PAGE END

        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("مہربانی انتظار کریں!!!");
        progressDialog.setMessage(this.getResources().getString(R.string.please_wait));


        progressDialog.setCancelable(false);

        phn_no_cache = pref.getString("phone_no", null); // getting String

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = tf.format(c);

//        mRootref.setAndroidContext(this);


        reference = FirebaseDatabase.getInstance().getReference();

        image = (ImageView) findViewById(R.id.logo);

        editText_Phone = (EditText) findViewById(R.id.phone_edittext);
        editText_Phone.setText(phn_no_cache);
        button_register_Member = (Button) findViewById(R.id.register_member_button);
        button_register_Domain = (Button) findViewById(R.id.register_domain_button);
        button_phone_login = (Button) findViewById(R.id.phone_submit_button);

        button_phone_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_verify_code=true;
                permission_prompt();
                //Toast.makeText(splash.this, ""+chk_permission, Toast.LENGTH_SHORT).show();
                if (chk_permission && pref.contains("phone_no")) {
                    ///if permissions granted and cache have phone no
                    phn_no_cache = pref.getString("phone_no", null); // getting String
                    entered_phn=editText_Phone.getText().toString();
//                    Toast.makeText(getApplicationContext(), entered_phn+"___"+phn_no_cache, Toast.LENGTH_LONG).show();
                    UserLoginPhone();
                } else if (chk_permission ) {
                    UserLogincheck();
                }
            }
        });
        button_register_Member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission_prompt();
                if (chk_permission) {
                    RegisterMemberScreen();
                }
            }
        });
        button_register_Domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission_prompt();
                if (chk_permission) {
                    RegisterDomainScreen();
                }
            }
        });
        //code to add automatic dash in phone number
        editText_Phone.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevL = editText_Phone.getText().toString().length();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prevL < length) && (length == 4)) {
                    data = editText_Phone.getText().toString();

                    editText_Phone.setText(data + "-");
                    editText_Phone.setSelection(length + 1);
                }
            }
        });

        //code to add automatic dash in phone number END
    }

    private void RegisterMemberScreen() {
        Intent intent = new Intent(getApplication(), Register_Member.class);
        mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
        startActivity(intent);
    }

    private void RegisterDomainScreen() {
        Intent intent = new Intent(getApplication(), Register_Domain.class);
        mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
        startActivity(intent);
    }
    TelephonyManager telephonyManager;
    String imei;
    private void UserLoginPhone() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return; } }
//        imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
        imei=getDeviceIMEI();
        try {
            progressDialog.show();
        }catch (Exception d){

        }

        entered_phn=editText_Phone.getText().toString();

        editor.putString("phone_no", entered_phn); // Storing string
        editor.commit();
        if( TextUtils.isEmpty(editText_Phone.getText()) || entered_phn.length()<12){
            progressDialog.cancel();
            editText_Phone.setError(splash.this.getResources().getString(R.string.enter_correct_phn_no));
            Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.enter_correct_phn_no), Toast.LENGTH_LONG).show();
        }else{
            entered_phn=entered_phn.substring(1,4)+entered_phn.substring(5);
            final Query query = reference.child("Members").orderByChild("Phone").equalTo(entered_phn);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            Member user= issue.getValue(Member.class);
                            Name=user.getName();
                            City=user.getCity();
                            Phone=user.getPhone();
                            ID=user.getID();
                            ID_Card=user.getID_Card();
                            Vendor=user.getVendor();
                            Imei=user.getImei();

                            // do something with the individual "issues"
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            check++;
                            intent.putExtra("Name", Name);
                            intent.putExtra("User", "member");
                            intent.putExtra("Phone", Phone);
                            intent.putExtra("City", City);
                            intent.putExtra("ID_Card", ID_Card);
                            intent.putExtra("ID", ID);
                            intent.putExtra("Vendor", Vendor);
                            intent.putExtra("Imei", Imei);

                            progressDialog.cancel();
                            if (user.getImei().equals(imei) && check==1){
                                Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.welcome), Toast.LENGTH_LONG).show();


                                phn_change=false;
                                finishAffinity();
                                mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
                                startActivity(intent);
                                break;
                            }else{
                                phn_change=true;
                                UserLogincheck();
                                break;
                            }
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
                                            if (user.getDomain_Verification().equals("1")){
                                        Name=user.getName();
                                        City=user.getCity();
                                        Phone=user.getPhone();
                                        ID=user.getID();
                                        ID_Card=user.getID_Card();
                                        Occupation=user.getOccupation();
                                        Imei=user.getImei();


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
                                        if (user.getImei().equals(imei)){
                                            Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.welcome), Toast.LENGTH_LONG).show();

                                            phn_change=false;
                                            finishAffinity();
                                            mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
                                            startActivity(intent);
                                            break;
                                        }
                                        else{
                                            phn_change=true;
                                            UserLogincheck();
                                            break;
                                        }
                                        }else{
                                            Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.wait_for_domain_account_to_verify), Toast.LENGTH_LONG).show();
                                                progressDialog.cancel();
                                            }
                                    }

                                    }
                                }else {
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.register_yourself_first), Toast.LENGTH_LONG).show();
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

        }


    }
    private void UserLogincheck(){
        entered_phn=editText_Phone.getText().toString();
        editor.putString("phone_no", entered_phn); // Storing string
        editor.commit();
        if( TextUtils.isEmpty(editText_Phone.getText()) || entered_phn.length()<12){
            editText_Phone.setError( this.getResources().getString(R.string.enter_correct_phn_no) );
            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.enter_correct_phn_no), Toast.LENGTH_LONG).show();
        }else {

            if (check_verify_code) {


                Intent intent = new Intent(getApplicationContext(), Verify_Code_login.class);
                entered_phn = entered_phn.substring(1, 4) + entered_phn.substring(5);
                final Query query = reference.child("Members").orderByChild("Phone").equalTo(entered_phn);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                Member user = issue.getValue(Member.class);
                                Name = user.getName();
                                City = user.getCity();
                                Phone = user.getPhone();
                                ID = user.getID();
                                ID_Card = user.getID_Card();
                                Vendor = user.getVendor();
                                Imei = user.getImei();
//                            Toast.makeText(getApplicationContext(), "خو ش آمدید " +Name, Toast.LENGTH_LONG).show();
                                intent.putExtra("Name", Name);
                                intent.putExtra("User", "member");
                                intent.putExtra("Phone", Phone);
                                intent.putExtra("City", City);
                                intent.putExtra("ID_Card", ID_Card);
                                intent.putExtra("ID", ID);
                                intent.putExtra("Vendor", Vendor);
                                intent.putExtra("Imei", Imei);

                                if (getDeviceIMEI().equals("" + Imei)) {
                                    UserLoginPhone();
                                    break;
                                }

                                User = "member";
                                intent.putExtra("phone", entered_phn);
                                intent.putExtra("user", User);
                                mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
                                startActivity(intent);
                                break;
                            }
                        } else {
                            final Query query = reference.child("Domain_experts").orderByChild("Phone").equalTo(entered_phn);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                            Domain_experts user = issue.getValue(Domain_experts.class);
                                            if (!TextUtils.isEmpty(user != null ? user.getDomain_Verification() : null)) {
                                                if (user.getDomain_Verification().equals("1")) {

                                                    Name = user.getName();
                                                    City = user.getCity();
                                                    Phone = user.getPhone();
                                                    ID = user.getID();
                                                    ID_Card = user.getID_Card();
                                                    Occupation = user.getOccupation();
                                                    Imei = user.getImei();
//                                              Intent intent = new Intent(getApplicationContext(), DomainMainActivity.class);
                                                    intent.putExtra("Name", Name);
                                                    intent.putExtra("User", "domain");
                                                    intent.putExtra("Phone", Phone);
                                                    intent.putExtra("City", City);
                                                    intent.putExtra("ID_Card", ID_Card);
                                                    intent.putExtra("ID", ID);
                                                    intent.putExtra("Occupation", Occupation);
                                                    intent.putExtra("Vendor", Vendor);
                                                    intent.putExtra("Imei", Imei);

                                                    User = "domain";

                                                    if (getDeviceIMEI().equals("" + Imei)) {
                                                        UserLoginPhone();
                                                        break;
                                                    }

                                                    intent.putExtra("phone", entered_phn);
                                                    intent.putExtra("user", User);
                                                    mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
                                                    startActivity(intent);
                                                    break;
                                                } else {
                                                    Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.wait_for_domain_account_to_verify), Toast.LENGTH_LONG).show();
                                                    progressDialog.cancel();
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), splash.this.getResources().getString(R.string.register_yourself_first), Toast.LENGTH_LONG).show();
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

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //If user presses allow
                i++;
            } else {
                //If user presses deny
            }

            if (i==4){
                chk_permission=true;
                Toast.makeText(splash.this, this.getResources().getString(R.string.thanks), Toast.LENGTH_SHORT).show();
            }else{
                chk_permission=false;
            }

    }

    public void permission_prompt(){
        ////NEW PERMISSION
        List<String> listPermissionsNeeded = new ArrayList<>();
        // No explanation needed, we can request the permission.
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED))
        {
            //Toast.makeText(splash.this, ""+chk_permission, Toast.LENGTH_SHORT).show();
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }else{
            chk_permission=true;
            //Toast.makeText(splash.this, ""+chk_permission, Toast.LENGTH_SHORT).show();

        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }else{
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.GET_ACCOUNTS);
        }else{
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }else{
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }else{
            chk_permission=true;
        }
        if((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED))
        {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }else{
            chk_permission=true;
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_ALL);
        }
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
//                    Toast.makeText(splash.this, deviceUniqueIdentifier, Toast.LENGTH_SHORT).show();

                }catch (Exception ex){
                    Toast.makeText(splash.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            try {
                deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                Toast.makeText(splash.this, deviceUniqueIdentifier, Toast.LENGTH_SHORT).show();

            }catch (Exception ex){
                Toast.makeText(splash.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return deviceUniqueIdentifier;
    }

    boolean network_connected=true;

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected && !app_background){
            network_connected=true;
//            Toast.makeText(getApplicationContext(), "Connected "+isConnected+" "+app_background, Toast.LENGTH_LONG).show();

            if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                popupWindow.dismiss();
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                splash_view.setAlpha(1f);
                enableDisableActivty(true);

                try {
                    check_verify_code=false;
                    permission_prompt();
                    //Toast.makeText(splash.this, ""+chk_permission, Toast.LENGTH_SHORT).show();
                    if (chk_permission && pref.contains("phone_no")) {
                        ///if permissions granted and cache have phone no
                        phn_no_cache = pref.getString("phone_no", null); // getting String
                        entered_phn = editText_Phone.getText().toString();
//                    Toast.makeText(getApplicationContext(), entered_phn+"___"+phn_no_cache, Toast.LENGTH_LONG).show();
                        UserLoginPhone();
                    }
                } catch (Exception e) {
                }
            }
            }
//            proceed_if_connected();
//            animationsFinished();
        }else{
            network_connected=false;
//            Toast.makeText(getApplicationContext(), "Not connected "+isConnected+" "+app_background, Toast.LENGTH_LONG).show();
//            View v=new View();

            popup_window_no_internet(parent);

//            popup_window();
        }
    }

    boolean app_background=false;
    @Override
    protected void onStop() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                splash_view.setAlpha(1f);
                enableDisableActivty(true);

                popupWindow.dismiss();
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
        }
        app_background=true;
//        Toast.makeText(splash.this, "Background", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                splash_view.setAlpha(1f);
                enableDisableActivty(true);

                popupWindow.dismiss();
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
        app_background=false;
        InternetAvailabilityChecker.init(this);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(splash.this);

        //        Toast.makeText(splash.this, "Foregroundd", Toast.LENGTH_SHORT).show();

        super.onStart();
    }

    PopupWindow popupWindow;

    ////popup widnow function
    public void popup_window_no_internet(View parent) {
        if (!network_connected) {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            splash_view=findViewById(R.id.splash_view);

            splash_view.setAlpha(0.2f);
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

//        View Parent = findViewById(R.id.splash_view);
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
                    mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
                    if (popupWindow!=null){
                        if (popupWindow.isShowing() || popupWindow!=null) {
                            popupWindow.dismiss();
                            getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            getWindow().addFlags(
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            splash_view.setAlpha(1f);
                            enableDisableActivty(true);
                        }
                    }
                    app_background=false;

                    InternetAvailabilityChecker.init(splash.this);


                    mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
                    mInternetAvailabilityChecker.addInternetConnectivityListener(splash.this);

//                  mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();


                }
            });

//        // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            if (!network_connected) {
                findViewById(R.id.splash_view).post(new Runnable() {
                    public void run() {
                        try {
                            popupWindow.showAtLocation(findViewById(R.id.splash_view), Gravity.CENTER, 0, 0);
                        }catch (Exception e){

                        }
                    }
                });
            }

        }
        ///popup window function end

    }

    private void enableDisableActivty(Boolean isEnable) {
        if (!isEnable) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected void onDestroy() {
//        mInternetAvailabilityChecker.removeAllInternetConnectivityChangeListeners();
        super.onDestroy();
    }
}
