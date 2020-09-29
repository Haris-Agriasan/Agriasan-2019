package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class DomainMainActivity extends AppCompatActivity implements InternetConnectivityListener {

    LinearLayout layout_deliver_orders;
    View v1_upper;
    View v2_upper;
    View v3_upper;
    View v4_upper;
    View v5_upper;

    View v2_down;
    View v1_down;
    View v3_down;
    View v4_down;
    View v5_down;
    LinearLayout layout_cart;

    String loading_url="https://www.novaeno.com/agriasan/loading_4.gif";
    InputStream input=null;
    ExifInterface exif=null;

    DatabaseReference reference;

    boolean doubleBackToExitPressedOnce = false;
    public static ProgressDialog progressDialog;

    String phn_imei;

    Intent intent;
    public String User;
    public String City;
    public String Name;
    public String Phone;
    public String ID;
    public String ID_Card;
    public String Occupation;
    public String Vendor;

    public String Imei;

    DomainMainActivity domainMainActivity;

    public BottomBar mBottomBar;
    public FragNavController fragNavController;

    //indices to fragments
    public int TAB_FIRST = FragNavController.TAB1;
    public int TAB_SECOND = FragNavController.TAB2;
    public int TAB_THIRD = FragNavController.TAB3;
    public int TAB_Fourth = FragNavController.TAB4;
    public int TAB_Five = FragNavController.TAB5;



    Toolbar toolbar;
    ImageView menu_expand;

    View v;

    public LinearLayout layout;
    public DrawerLayout drawerLayout;
    public DrawerLayout drawerLayout_alpha;

    NavigationView navView;

    ImageView image_nav;
    TextView textview_name_city_nav;
    TextView textview_phone_nav;
    TextView textview_id_card_nav;

    Button btn_contact_us;
    LinearLayout layout_contact_us;

    Button btn_logout;
    LinearLayout layout_logout;
    LinearLayout layout_orders;



    ImageView img_headphone;
    View view_right_headphone;
    View view_left_headphone;

    TextView mTitle;

    FragmentManager fm;

    ////FOR GETTING CURRENT LOCATION
    LocationManager locationManager;
    LocationListener locationListener;

    ///CACHE LOCATION
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public double Location_Long;
    public double Location_Lat;

    TelephonyManager telephonyManager;

    View parent;


    Resources resources;

    public static PopupWindow popupWindow_update;
    PackageInfo pInfo;
    int verCode=0;
    String Play_store_ver_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        parent=getWindow().getDecorView().findViewById(android.R.id.content);

        drawerLayout_alpha = (DrawerLayout) findViewById(R.id.drawer_layout);

        resources = DomainMainActivity.this.getResources();

        reference = FirebaseDatabase.getInstance().getReference();

        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            verCode = pInfo.versionCode;

            ////////////CHECKING IF UPDATE AVAILABLE ON PLAYSTORE
            final Query query_update_app = reference.child("Update");
            query_update_app.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                            Play_store_ver_code= issue.getValue()+"";
//                            Toast.makeText(MainActivity.this,issue.getValue()+"", Toast.LENGTH_SHORT).show();

                            if (verCode!=Integer.parseInt(Play_store_ver_code)){
                                popup_window_update_available(parent);
                            }else{
                            }

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ////////////CHECKING IF UPDATE AVAILABLE ON PLAYSTORE END


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        InternetAvailabilityChecker.init(this);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(DomainMainActivity.this);


        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        phn_imei=telephonyManager.getDeviceId();
        phn_imei=getDeviceIMEI();
        ///CACHE FOR LOCATION
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String last_loc_lat=pref.getString("last_loc_lat", null); // getting String
        String last_loc_lon=pref.getString("last_loc_lon", null); // getting String
        //Toast.makeText(MainActivity.this,last_loc_lat.toString()+"  "+last_loc_lon.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(DomainMainActivity.this,phn_imei, Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(last_loc_lon) || last_loc_lon!=null){
            Location_Long=Double.parseDouble(last_loc_lon);
        }
        if (!TextUtils.isEmpty(last_loc_lat) || last_loc_lat!=null){
            Location_Lat=Double.parseDouble(last_loc_lat);
        }
        //////////GET CURRENT LOCATION
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                editor.putString("last_loc_lat", location.getLatitude()+""); // Storing
                editor.putString("last_loc_lon", location.getLongitude()+""); // Storing
                editor.apply();
                Location_Lat=location.getLatitude();
                Location_Long=location.getLongitude();
                //Log.i("LOCATION", location.toString());
                //Toast.makeText(MainActivity.this,location.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        /////GET CURRENT LOCATION
        setContentView(R.layout.domain_activity_main);
        checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);
        drawerLayout_alpha = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout_alpha.setAlpha(1f);

        intent = getIntent();

        User=intent.getStringExtra("User");
        City=intent.getStringExtra("City");
        Name=intent.getStringExtra("Name");
        Phone=intent.getStringExtra("Phone");
        ID=intent.getStringExtra("ID");
        ID_Card=intent.getStringExtra("ID_Card");
        Occupation=intent.getStringExtra("Occupation");
        Vendor=intent.getStringExtra("Vendor");
        Imei=intent.getStringExtra("Imei");
        ////////////CHECKING IMEI AGAIN AND AGAIN
        reference = FirebaseDatabase.getInstance().getReference();
        final Query query = reference.child("Domain_experts").orderByChild("Phone").equalTo(Phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        Toast.makeText(getActivity(), "check", Toast.LENGTH_LONG).show();
                        Domain_experts member = issue.getValue(Domain_experts.class);

                        if (member != null && Phone.equals(member.getPhone())) {
                            if (!phn_imei.equals(member.getImei())) {
                                //                                editor.remove("phone_no");
                                Toast.makeText(getApplicationContext(),
                                        resources.getString(R.string.apka_account_dusray_mobile_say_login_ho_gya_hai), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), splash.class);
                                //                              finishAffinity();
                                finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ////////////CHECKING IMEI AGAIN AND AGAIN END

//        Toast.makeText(getApplicationContext(), ""+Imei, Toast.LENGTH_LONG).show();


        layout =  findViewById(R.id.layout);
        configureNavigationDrawer();
        configureToolbar();


        ///setting account information in drawer
        View header = navView.getHeaderView(0);
        textview_name_city_nav = (TextView) header.findViewById(R.id.text_name_city);
        image_nav = header.findViewById(R.id.profile_image);
        textview_phone_nav = (TextView) header.findViewById(R.id.text_phone);
        textview_id_card_nav = (TextView) header.findViewById(R.id.text_id_card);
        textview_name_city_nav.setText(Name);
        textview_phone_nav.setText("0"+Phone);
        textview_id_card_nav.setText(ID_Card);
        image_nav.setImageResource(R.drawable.man);
        image_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TedBottomPicker.with(DomainMainActivity.this)
                        .setPeekHeight(1600)
                        .showTitle(false)
                        .setSpacing(2)
//                        .setSelectedUri()
                        .setCompleteButtonText(R.string.shamil_karen)
                        .setEmptySelectionText(R.string.no_image)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                try {
                                    input = getApplicationContext().getContentResolver().openInputStream(uri);
                                    exif = null;
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            if (input != null) { exif = new ExifInterface(input); }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    int orientation = 0;
                                    if (exif != null) {
                                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                ExifInterface.ORIENTATION_UNDEFINED);
                                    }

                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                                    path_to_image=uri.toString();
                                    upload_firebase(uri,ID,"domain",bmRotated);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(DomainMainActivity.this, "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        img_headphone=header.findViewById(R.id.img_headphone);
        view_left_headphone=header.findViewById(R.id.view_left_headphone);
        view_right_headphone=header.findViewById(R.id.view_right_headphone);
        btn_contact_us=header.findViewById(R.id.btn_contact_us);
        layout_contact_us=header.findViewById(R.id.layout_contact_us);
        layout_logout=header.findViewById(R.id.layout_logout);

        layout_cart=header.findViewById(R.id.layout_cart);
        layout_orders=header.findViewById(R.id.layout_orders);

        btn_logout=header.findViewById(R.id.btn_logout);
        layout_deliver_orders=header.findViewById(R.id.layout_deliver_orders);

        v1_upper=header.findViewById(R.id.v1_upper);
        v2_upper=header.findViewById(R.id.v2_upper);
        v3_upper=header.findViewById(R.id.v3_upper);
        v4_upper=header.findViewById(R.id.v4_upper);
        v5_upper=header.findViewById(R.id.v5_upper);

        v1_down=header.findViewById(R.id.v1_down);
        v2_down=header.findViewById(R.id.v2_down);
        v3_down=header.findViewById(R.id.v3_down);
        v4_down=header.findViewById(R.id.v4_down);
        v5_down=header.findViewById(R.id.v5_down);
        //FragNav
        //list of fragments
        List<Fragment> fragments = new ArrayList<>(8);

        //add fragments to list
        fragments.add(new DomainBazaarFragment());
        fragments.add(new PublicOpinionFragment());
        fragments.add(new DomainHomeFragment());
        fragments.add(new SubsidyFragment());
        fragments.add(new DomainContactUsFragment());
//        fragments.add(ThirdFragment.newInstance(0));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.fragment_container,fragments);
        //End of FragNav

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        //BottomBar menu

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setMaxFixedTabs(4);
        mBottomBar.setDefaultTabPosition(2);
        mBottomBar.setItems(R.menu.bottombar_menu);
//        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //switch between tabs
                switch (menuItemId) {
                    case R.id.navigation_bazaar:
                        unselect_contact_us();
                        fm=getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_FIRST);
                        fm=getSupportFragmentManager();
                        mTitle.setText(R.string.title_bazaar);
                        ///TO GET RID OF DUPLICATE FRAGMENTS
                        break;
                    case R.id.navigation_public_opinion:
                        unselect_contact_us();
                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_SECOND);
                        mTitle.setText(R.string.title_public_opinion);
                        break;
                    case R.id.navigation_home:
                        unselect_contact_us();

                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_THIRD);
                        mTitle.setText("سوالات");
                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        break;
                    case R.id.navigation_subsidy:
                        unselect_contact_us();

                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_Fourth);
                        mTitle.setText(R.string.title_subsidy);
                        fm=getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        break;
                }
            }
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.navigation_bazaar:
                        unselect_contact_us();
                        fm=getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_FIRST);
                        fm=getSupportFragmentManager();
                        mTitle.setText(R.string.title_bazaar);
                        ///TO GET RID OF DUPLICATE FRAGMENTS
                        break;
                    case R.id.navigation_public_opinion:
                        unselect_contact_us();
                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_SECOND);
                        mTitle.setText(R.string.title_public_opinion);
                        break;
                    case R.id.navigation_home:
                        unselect_contact_us();

                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_THIRD);
                        mTitle.setText("سوالات");
                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        break;
                    case R.id.navigation_subsidy:
                        unselect_contact_us();

                        fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        fragNavController.switchTab(TAB_Fourth);
                        mTitle.setText(R.string.title_subsidy);
                        fm=getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        break;
                }

            }
        });
        //End of BottomBar menu
        layout_deliver_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                mTitle.setText(R.string.orders_to_deliver);
                mBottomBar.selectTabAtPosition(2,true);
                ///TO GET RID OF DUPLICATE FRAGMENTS
                fm=getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                funct_orders_to_deliver();
            }
        });
        layout_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                mTitle.setText(R.string.ham_say_rabta_karen);
                mBottomBar.selectTabAtPosition(2,true);
                ///TO GET RID OF DUPLICATE FRAGMENTS
                fm=getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                funct_open_cart();
            }
        });


        layout_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                drawerLayout.closeDrawers();
                mBottomBar.selectTabAtPosition(2,true);
//                btn_logout.getBackground().setAlpha(0);
//                fragNavController.switchTab(TAB_Five);
                ///TO GET RID OF DUPLICATE FRAGMENTS
                fm=getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                mTitle.setText(resources.getString(R.string.contact_us));
                funct_contact_us();
                //funct_contact_us_page();
            }
        });
        layout_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                mTitle.setText(R.string.your_orders);
                mBottomBar.selectTabAtPosition(2,true);
                ///TO GET RID OF DUPLICATE FRAGMENTS
                fm=getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                funct_open_orders();
            }
        });
        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_headphone.getBackground().setAlpha(0);
                view_left_headphone.getBackground().setAlpha(0);
                view_right_headphone.getBackground().setAlpha(0);
                btn_contact_us.getBackground().setAlpha(0);

                drawerLayout.setAlpha(0.3f);
                popup_window(v);

            }
        });//Color.parseColor("#55FF0000")
        ////FOR NAVIGATION DRAWER
//        new DrawerBuilder()
        ////FOR NAVIGATION DRAWER END

        ////////////CHECKING IF Profile Image Exist
        final Query query_profile = reference.child("Domain_experts").orderByChild("Phone").equalTo(Phone);
        query_profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Member member = issue.getValue(Member.class);
                        String p_image="";
                        if (Phone.equals(member.getPhone())){
                            if (!TextUtils.isEmpty(member.getProfile_Image())){
                                p_image=member.getProfile_Image();
//                                image_nav.setImageResource(R.drawable.background_1);
//                                Picasso.get().load(p_image).resize(150,150).placeholder( R.drawable.loading_4 ).into(image_nav);
                                //.submit(150,150).placeholder(R.drawable.loading_4)
//                                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image_nav);
                                Glide.with(getApplicationContext())
//                                        .asGif()
                                        .load(p_image)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.loading_4)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .override(150,150))
//                                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loading_4))
                                        .into(image_nav);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ////////////CHECKING IF Profile Image Exist END
    }


    public void popup_window_update_available(View parent) {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        drawerLayout_alpha.setAlpha(0.2f);

        Button btn_update;
        Button btn_popup_later;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_update_available, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float s_height = displayMetrics.heightPixels;
        float s_width = displayMetrics.widthPixels;

        s_height = s_height * 0.4f;
        s_width = s_width * 0.8f;

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow_update = new PopupWindow(popupView, Math.round(s_width), Math.round(s_height));

//        image_popup=popupView.findViewById(R.id.image_popup);
//        Picasso.get().load(url_tem_image).placeholder( R.drawable.loading_4 ).into(image_popup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow_update.setElevation(40);
        }

        btn_update = popupView.findViewById(R.id.btn_popup_update);
        btn_popup_later = popupView.findViewById(R.id.btn_popup_later);

        popupWindow_update.setFocusable(false);
        popupWindow_update.setOutsideTouchable(true);
        popupWindow_update.setTouchable(true);
        popupWindow_update.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_update.dismiss();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=agri_asan.com.agriasan06_12_19"));
                startActivity(browserIntent);
                drawerLayout_alpha.setAlpha(1f);

            }
        });

        btn_popup_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_update.dismiss();
                drawerLayout_alpha.setAlpha(1f);
            }
        });

//        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        findViewById(R.id.fragment_container).post(new Runnable() {
            public void run() {
                try {
                    popupWindow_update.showAtLocation(findViewById(R.id.fragment_container), Gravity.CENTER, 0, 0);
                }catch (Exception e){

                }
            }
        });


        popupWindow_update.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                drawerLayout_alpha.setAlpha(1f);
            }
        });



    }


    public static PopupWindow popupWindow;

    boolean network_connected=true;
    boolean app_background=false;
    InternetAvailabilityChecker mInternetAvailabilityChecker;



    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        drawerLayout_alpha = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (isConnected && !app_background){
            network_connected=true;
//            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();

            if (popupWindow!=null){
                if (popupWindow.isShowing() || popupWindow!=null) {
                    drawerLayout_alpha = (DrawerLayout) findViewById(R.id.drawer_layout);
                    try {
                        drawerLayout_alpha.setAlpha(1f);
                    }catch (Exception e){

                    }
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
                drawerLayout_alpha.setAlpha(1f);
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
        mInternetAvailabilityChecker.addInternetConnectivityListener(DomainMainActivity.this);
        super.onStart();
    }



    @Override
    protected void onStop() {
        if (popupWindow!=null){
            if (popupWindow.isShowing() || popupWindow!=null) {
                drawerLayout_alpha.setAlpha(1f);
                enableDisableActivty(true);

                try {
                    popupWindow.dismiss();
                }catch (Exception e){

                }
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

            drawerLayout_alpha.setAlpha(0.2f);

            enableDisableActivty(false);



            TextView popup_heading;
            TextView popup_subheading;
            Button btn_retry;
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = null;
            if (inflater != null) {
                popupView = inflater.inflate(R.layout.popup_no_internet, null);
            }

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
            popup_heading.setText(this.getResources().getString(R.string.no_internet));
            popup_subheading.setText(this.getResources().getString(R.string.please_enable_your_internet));

            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow!=null){
                        if (popupWindow.isShowing() || popupWindow!=null) {
                            drawerLayout_alpha.setAlpha(1f);
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
                    mInternetAvailabilityChecker.addInternetConnectivityListener(DomainMainActivity.this);
                }
            });

//        // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            if (!network_connected) {
                findViewById(R.id.fragment_container).post(new Runnable() {
                    public void run() {
                        try {
                            popupWindow.showAtLocation(findViewById(R.id.fragment_container), Gravity.CENTER, 0, 0);
                        }catch (Exception e){

                        }
                    }
                });
            }

        }
        ///popup window function end

    }


    StorageReference storageRef;
    UploadTask uploadTask;

    String path_to_image;

    StorageReference mountainsRef;
    DatabaseReference databaseReference;

    private String upload_URL = "";
    JSONObject jsonObject;
    RequestQueue rQueue;
    String cmp_url="";
    String imgname="";


    public void upload_firebase(Uri uri, String id, String user, Bitmap bitmap) {

        upload_URL = "https://www.novaeno.com/agriasan/uploadProfileImage.php";

        if (user.equals("member")){
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Members").child(id);
            storageRef = FirebaseStorage.getInstance().getReference("Members_Profile_Images");
        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Domain_experts").child(id);
            storageRef = FirebaseStorage.getInstance().getReference("Domains_Profile_Images");
        }
        progressDialog.show();
        String extension = path_to_image.substring(path_to_image.lastIndexOf("."));
        mountainsRef = storageRef.child(System.currentTimeMillis()
                + "" + extension);

        //////////////NEW CODE
        cmp_url="";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("name", imgname);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        cmp_url="https://novaeno.com/agriasan/agriasan_images/Profile/";
                        Log.e("Image Uploadeddddddddd", jsonObject.toString());
                        rQueue.getCache().clear();

//                        text.setText(cmp_url);
                        cmp_url=cmp_url+imgname+".JPG";
//                        Toast.makeText(getApplication(), ""+cmp_url, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        databaseReference.child("Profile_Image").setValue(cmp_url);


                        Glide.with(getApplicationContext())
                                .load(cmp_url)
                                .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                                .into(image_nav);

                        Toast.makeText(getApplicationContext(), "آپکی تصویر لگ گئی ہے!", Toast.LENGTH_LONG).show();

                        try{
                            progressDialog.hide();
                        }catch (Exception ex)
                        {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Errroooorrrrrr", volleyError.toString());

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 900000,
                10000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        rQueue = Volley.newRequestQueue(DomainMainActivity.this);
        rQueue.add(jsonObjectRequest);

        /////////NEW CODE END
    }
    public void funct_open_orders(){
        Toast.makeText(getApplicationContext(), resources.getString(R.string.please_wait), Toast.LENGTH_LONG).show();

        Fragment fragment = new CustomerOrdersFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("user","domain");
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    public void funct_open_cart(){
        //Toast.makeText(getContext().getApplicationContext(), ""+fasal, Toast.LENGTH_LONG).show();

        Fragment fragment = new OpenCartFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("user","domain");
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }
    public void funct_contact_us(){
        Toast.makeText(getApplicationContext(), resources.getString(R.string.please_wait), Toast.LENGTH_LONG).show();

        Fragment fragment = new ContactUsFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("user","domain");
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }
    public void funct_orders_to_deliver(){
        //Toast.makeText(getContext().getApplicationContext(), ""+fasal, Toast.LENGTH_LONG).show();

        Fragment fragment = new VendorOrdersToDeliverFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("user","domain");
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        if (mTitle.getText().toString().equals("سوالات"))
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
//                super.onBackPressed();
                return;
            }else{
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "مہربانی پیچھے جانے کے لیے دبارہ بیک کا بٹن دابایں", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }else if (mTitle.getText().toString().equals("سبسڈی"))
        {
            mBottomBar.selectTabAtPosition(2,true);
            fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fragNavController.switchTab(TAB_THIRD);
            mTitle.setText("سوالات");

        }else if (mTitle.getText().toString().equals("عوامی رائے"))
        {
            mBottomBar.selectTabAtPosition(2,true);
            fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fragNavController.switchTab(TAB_THIRD);
            mTitle.setText("سوالات");
        }else if (mTitle.getText().toString().equals("بازار"))
        {
            mBottomBar.selectTabAtPosition(2,true);
            fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fragNavController.switchTab(TAB_THIRD);
            mTitle.setText("سوالات");
        }else if (mTitle.getText().toString().equals("ہم سے رابطہ کریں")) {
            mBottomBar.selectTabAtPosition(2,true);
            fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fragNavController.switchTab(TAB_THIRD);
            mTitle.setText("سوالات");
        }
        else{
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mTitle= (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("سوالات");
        menu_expand=toolbar.findViewById(R.id.menu_expand);
        menu_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.menu_icon);


//        actionbar.setDisplayHomeAsUpEnabled(true);

    }
    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // manage other entries if you have it ...
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
    ////popup widnow function
    public void popup_window(View v){
        ImageView btn_close;
        Button btn_no;
        Button btn_yes;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_logout, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float s_height = displayMetrics.heightPixels;
        float s_width = displayMetrics.widthPixels;

        s_height=s_height*0.4f;
        s_width=s_width*0.8f;


        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, Math.round(s_width), Math.round(s_height));




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(40);
        }
//        image_popup=popupView.findViewById(R.id.image_popup);
//        Picasso.get().load(url_tem_image).placeholder( R.drawable.loading_4 ).into(image_popup);

        btn_no=popupView.findViewById(R.id.btn_popup_logout_no);
        btn_close = popupView.findViewById(R.id.close_btn_popup_logout);
        btn_yes = popupView.findViewById(R.id.btn_popup_logout_yes);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout.getBackground().setAlpha(0);
                popupWindow.dismiss();
                drawerLayout.setAlpha(1f);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout.getBackground().setAlpha(0);
                popupWindow.dismiss();
                drawerLayout.setAlpha(1f);
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btn_logout.getBackground().setAlpha(0);
                popupWindow.dismiss();
                drawerLayout.setAlpha(1f);
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), splash.class);
                finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                finishAffinity();
                startActivity(intent);
            }
        });
//        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn_logout.getBackground().setAlpha(0);
                popupWindow.dismiss();
                drawerLayout.setAlpha(1f);
                return true;
            }
        });
    }
    ///popup window function end
    public void function_call_view_inventory_domain_vendor(){
        Fragment fragment = new DomainInventoryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
//        domainMainActivity.mTitle.setText(R.string.aapki_products);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                DomainMainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            DomainMainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
//            Toast
//                    .makeText(MainActivity.this,
//                            "Permission already granted",
//                            Toast.LENGTH_SHORT)
//                    .show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DomainMainActivity.this,
                        "شکریہ",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(DomainMainActivity.this,
                        "آپنے پرمیشن نہی دی",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DomainMainActivity.this,
                        "شکریہ",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(DomainMainActivity.this,
                        "آپنے پرمیشن نہی دی",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

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


    public void unselect_contact_us(){
        img_headphone.getBackground().setAlpha(0);
        view_left_headphone.getBackground().setAlpha(0);
        view_right_headphone.getBackground().setAlpha(0);
        btn_contact_us.getBackground().setAlpha(0);
    }

    private void enableDisableActivty(Boolean isEnable) {
        if (!isEnable) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

}
