package agri_asan.com.agriasan06_12_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class Register_Domain extends LocalizationActivity implements InternetConnectivityListener {

    String entered_phn;

    EditText editText_name;
    EditText editText_city;
    EditText editText_Phone;
    EditText editText_id_card;
    EditText editText_shoba;
    Button button_register_Domain;

    Boolean alreay_acount=false;
    DatabaseReference reference;

    Resources resources;


    LinearLayout id_1;
    LinearLayout id_2;
    LinearLayout id_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.activity_register__domain);

        id_1=findViewById(R.id.id_1);
        id_2=findViewById(R.id.id_2);
        id_3=findViewById(R.id.id_3);

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if (isVisible){
                    id_1.setVisibility(View.GONE);
                    id_2.setVisibility(View.GONE);
                    id_3.setVisibility(View.GONE);
                }else{
                    id_1.setVisibility(View.VISIBLE);
                    id_2.setVisibility(View.VISIBLE);
                    id_3.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(getApplicationContext(), ""+isVisible, Toast.LENGTH_LONG).show();
            }
        });

        page_layout_for_alpha = findViewById(R.id.layout_register_domain);
        page_layout_for_alpha.setAlpha(1f);




        resources=Register_Domain.this.getResources();
        reference = FirebaseDatabase.getInstance().getReference();

        editText_name = (EditText) findViewById(R.id.edittext__domain_name);
        editText_name.requestFocus();
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.showSoftInput(editText_name, 0);
        }
        editText_city = (EditText) findViewById(R.id.edittext_domain_city);
        editText_shoba = (EditText) findViewById(R.id.edittext_domain_shoba);
        editText_Phone = (EditText) findViewById(R.id.edittext_domain_number);
        editText_id_card = (EditText) findViewById(R.id.edittext_domain_id_card);
        button_register_Domain = (Button) findViewById(R.id.register_domain_submit_button);

        button_register_Domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDomainScreen();
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
                    String data = editText_Phone.getText().toString();
                    editText_Phone.setText(data + "-");
                    editText_Phone.setSelection(length + 1);
                }
            }
        });
        //code to add automatic dash in phone number END

        //code to add automatic dash in ID card
        editText_id_card.addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevL = editText_id_card.getText().toString().length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prevL < length) && (length == 5 || length == 13)) {
                    String data = editText_id_card.getText().toString();
                    editText_id_card.setText(data   + "-");
                    editText_id_card.setSelection(length + 1);
                }
            }
        });
        //code to add automatic dash in ID card END

    }

    private void RegisterDomainScreen(){
        if( TextUtils.isEmpty(editText_name.getText())){
            editText_name.setError( resources.getString(R.string.enter_your_name_first));
            Toast.makeText(getApplicationContext(), resources.getString(R.string.enter_your_name_first), Toast.LENGTH_LONG).show();
        }else{
            if( TextUtils.isEmpty(editText_city.getText())){
                editText_city.setError( resources.getString(R.string.enter_your_city_first));
                Toast.makeText(getApplicationContext(), resources.getString(R.string.enter_your_city_first), Toast.LENGTH_LONG).show();
            }else{
                if( TextUtils.isEmpty(editText_Phone.getText())){
                    editText_Phone.setError( resources.getString(R.string.enter_your_phn_first));
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.enter_your_phn_first), Toast.LENGTH_LONG).show();
                }else{
//                    if( TextUtils.isEmpty(editText_id_card.getText())){
//                        editText_id_card.setError( "پہلے اپنا شناختی کارڈ نمبر لکھیں!");
//                        Toast.makeText(getApplicationContext(), "پہلے اپنا شناختی کارڈ نمبر لکھیں!", Toast.LENGTH_LONG).show();
//                    }else{
                        alreay_acount=false;
                        if( TextUtils.isEmpty(editText_shoba.getText())){
                            editText_shoba.setError( resources.getString(R.string.enter_your_shoba_first));
                            Toast.makeText(getApplicationContext(), resources.getString(R.string.enter_your_shoba_first), Toast.LENGTH_LONG).show();
                        }else{
                            entered_phn=editText_Phone.getText().toString();

                            entered_phn=entered_phn.substring(1,4)+entered_phn.substring(5);
                            if (entered_phn.length()!=10){
                                editText_Phone.setError( resources.getString(R.string.enter_correct_phn_no));
                                Toast.makeText(getApplicationContext(), resources.getString(R.string.enter_correct_phn_no), Toast.LENGTH_LONG).show();
                            }else {
                                entered_phn = editText_Phone.getText().toString();

                                entered_phn = entered_phn.substring(1, 4) + entered_phn.substring(5);
                                ///CHECKING IF MEMBER ACCOUNT ALREAY REGISTERED ON THIS NUMBERS
                                final Query query = reference.child("Members").orderByChild("Phone").equalTo(entered_phn);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                                Member user = issue.getValue(Member.class);
                                                alreay_acount = true;
                                                editText_Phone.setError( resources.getString(R.string.phn_no_already_exist));

                                                Toast.makeText(getApplicationContext(), resources.getString(R.string.phn_no_already_exist), Toast.LENGTH_LONG).show();
                                                break;
                                                // do something with the individual "issues"
                                            }
                                        } else {
                                            final Query query = reference.child("Domain_experts").orderByChild("Phone").equalTo(entered_phn);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                                            alreay_acount = true;
                                                            editText_Phone.setError( resources.getString(R.string.phn_no_already_exist));

                                                            Toast.makeText(getApplicationContext(), resources.getString(R.string.phn_no_already_exist), Toast.LENGTH_LONG).show();
                                                            break;
                                                        }
                                                    } else {
                                                            String name = editText_name.getText().toString();
                                                            String city = editText_city.getText().toString();
                                                            String phone = editText_Phone.getText().toString();
                                                            String id_card="";
                                                            if (!editText_id_card.getText().toString().isEmpty()){
                                                                id_card = editText_id_card.getText().toString();
                                                            }else{
                                                                id_card = "XXXXX-XXXXXXX-X";
                                                            }
                                                            String shoba = editText_shoba.getText().toString();

                                                            Intent intent = new Intent(getApplicationContext(), Verify_Code.class);

                                                            intent.putExtra("full_phone", phone);
                                                            phone = phone.substring(1, 4) + phone.substring(5);
                                                            intent.putExtra("name", name);
                                                            intent.putExtra("user", "domain");
                                                            intent.putExtra("phone", phone);
                                                            intent.putExtra("city", city);
                                                            intent.putExtra("id_card", id_card);
                                                            intent.putExtra("shoba", shoba);

                                                            Toast.makeText(getApplicationContext(), resources.getString(R.string.please_wait), Toast.LENGTH_LONG).show();
                                                            startActivity(intent);
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
//                    }
                }
            }
        }
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
        mInternetAvailabilityChecker.addInternetConnectivityListener(Register_Domain.this);
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
                    mInternetAvailabilityChecker.addInternetConnectivityListener(Register_Domain.this);
                }
            });

//        // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            if (!network_connected) {
                findViewById(R.id.layout_register_domain).post(new Runnable() {
                    public void run() {
                        try {
                            popupWindow.showAtLocation(findViewById(R.id.layout_register_domain), Gravity.CENTER, 0, 0);
                        }catch (Exception e){

                        }
                    }
                });
            }
        }
        ///popup window function end

    }
}
