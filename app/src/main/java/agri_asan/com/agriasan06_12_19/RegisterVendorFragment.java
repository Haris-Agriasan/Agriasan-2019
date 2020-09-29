package agri_asan.com.agriasan06_12_19;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.mikepenz.iconics.Iconics.getApplicationContext;

public class RegisterVendorFragment extends Fragment {
    LinearLayout id_1;
    LinearLayout id_2;
    LinearLayout id_3;

    String formattedDate;
    String formattedTime;

    //// FIREBASE TO SAVE DATA OF VENDOR
    private Firebase mRootref;

    EditText editText_name;
    EditText editText_city;
    EditText editText_Phone;
    EditText editText_id_card;
    EditText editText_shoba;
    Button button_register_Domain;

    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    Intent old_intent;
    String old_phone;
    String old_name;
    String old_city;
    String old_id;
    String old_id_card;
    String old_user;

    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        root = inflater.inflate(R.layout.fragment_register_vendor, container, false);

        id_1=root.findViewById(R.id.id_1);
        id_2=root.findViewById(R.id.id_2);
        id_3=root.findViewById(R.id.id_3);

        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener()
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

        old_user = getArguments().getString("user");
        if (old_user.equals("member")){
            mainActivity=(MainActivity)  getActivity();
            old_intent = this.mainActivity.getIntent();
            mRootref.setAndroidContext(this.mainActivity);
        }
        if (old_user.equals("domain")){
            domainMainActivity=(DomainMainActivity)  getActivity();
            old_intent = this.domainMainActivity.getIntent();   //DOUBT
            mRootref.setAndroidContext(this.domainMainActivity);
        }

        old_phone = getArguments().getString("phone");
        old_name = getArguments().getString("name");
        old_city = getArguments().getString("city");
        old_id = getArguments().getString("id");

        old_id_card = getArguments().getString("id_card");
//        Toast.makeText(getApplicationContext(), ""+mainActivity.Vendor, Toast.LENGTH_LONG).show();

        editText_name = (EditText) root.findViewById(R.id.edittext__domain_name);
        editText_city = (EditText) root.findViewById(R.id.edittext_domain_city);
        editText_shoba = (EditText) root.findViewById(R.id.edittext_domain_shoba);
        editText_Phone = (EditText) root.findViewById(R.id.edittext_domain_number);
        editText_id_card = (EditText) root.findViewById(R.id.edittext_domain_id_card);
        button_register_Domain = (Button) root.findViewById(R.id.register_domain_submit_button);

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
        return root;


    }
    private void RegisterDomainScreen(){
        if( TextUtils.isEmpty(editText_name.getText())){
            editText_name.setError( "پہلے اپنانام لکھیں!");
            Toast.makeText(root.getContext(), "پہلے اپنانام لکھیں!", Toast.LENGTH_LONG).show();
        }else{
            if( TextUtils.isEmpty(editText_city.getText())){
                editText_city.setError( "پہلے اپنا شہر لکھیں!");
                Toast.makeText(root.getContext(), "پہلے اپنا شہر لکھیں!", Toast.LENGTH_LONG).show();
            }else{
                if( TextUtils.isEmpty(editText_Phone.getText())){
                    editText_Phone.setError( "پہلے اپنا فون نمبر لکھیں!");
                    Toast.makeText(root.getContext(), "پہلے اپنا فون نمبر لکھیں!", Toast.LENGTH_LONG).show();
                }else{
                        if( TextUtils.isEmpty(editText_shoba.getText())){
                            editText_shoba.setError( "پہلے اپنا شعبہ لکھیں!");
                            Toast.makeText(root.getContext(), "پہلے اپنا شعبہ لکھیں!", Toast.LENGTH_LONG).show();
                        }else{
                            String name=editText_name.getText().toString();
                            String city=editText_city.getText().toString();
                            String phone=editText_Phone.getText().toString();
                            String id_card="";
                            if (!editText_id_card.getText().toString().isEmpty()){
                                id_card = editText_id_card.getText().toString();
                            }else{
                                id_card = "XXXXX-XXXXXXX-X";
                            }

                            String shoba=editText_shoba.getText().toString();

                            Intent intent = new Intent(root.getContext(), Verify_Code.class);
                            phone=phone.substring(1,4)+phone.substring(5);

                            /////MATCHING PHN NO FOR VENDOR ACCOUNT
                            if (old_phone.equals(phone))
                            {
                                intent.putExtra("name", name);
                                intent.putExtra("user", "domain");
                                intent.putExtra("vender", "yes");
                                intent.putExtra("phone", phone);
                                intent.putExtra("city", city);
                                intent.putExtra("id_card", id_card);
                                intent.putExtra("shoba", shoba);
                                SaveVendorData();
                                //startActivity(intent);
                            }else{
                                Toast.makeText(root.getContext(), "مہربانی اپنا درست نمبر درج کریں!", Toast.LENGTH_LONG).show();
                            }
                        }
//                    }
                }
            }
        }
    }
    private void SaveVendorData() {

        mRootref=new Firebase("https://agriasan-6b704.firebaseio.com/Vendors");
        //it will create a unique id and we will use it as the Primary Key for our Member
        String id = mRootref.push().getKey();

        Firebase childRef = mRootref.child(id).child("Name");
        childRef.setValue(editText_name.getText()+"");

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef=mRootref.child(id).child("City");
        childRef.setValue(editText_city.getText()+"");

        childRef=mRootref.child(id).child("Phone");
        childRef.setValue(old_phone);

        childRef=mRootref.child(id).child("ID_Card");
        childRef.setValue(editText_id_card.getText()+"");

        childRef=mRootref.child(id).child("user");
        childRef.setValue(old_user+"");

        childRef=mRootref.child(id).child("chezen");
        childRef.setValue(editText_shoba.getText()+"");

        childRef=mRootref.child(id).child("ID");
        childRef.setValue(id+"");

        childRef=mRootref.child(id).child("Old_ID");
        childRef.setValue(old_id+"");

//        Toast.makeText(root.getContext(), ""+old_id, Toast.LENGTH_LONG).show();

        DatabaseReference databaseReference;

        if (old_user.equals("member")) {
            databaseReference= FirebaseDatabase.getInstance().getReference()
                    .child("Members").child(old_id);
            Toast.makeText(root.getContext(), "آپکا سیلر اکاؤنٹ رجسٹر ہو گیا ہے شکریہ!!", Toast.LENGTH_LONG).show();

        }else{
            databaseReference= FirebaseDatabase.getInstance().getReference()
                    .child("Domain_experts").child(old_id);
            databaseReference.child("Vendor_Verification").setValue("0");
            Toast.makeText(root.getContext(), "آپکا سیلر اکاؤنٹ رجسٹر ہو گیا ہے تصدیق ہونے کاانتظار کریں شکریہ!!", Toast.LENGTH_LONG).show();
        }

        databaseReference.child("Vendor").setValue("yes");






        if (old_user.equals("member")){
            mainActivity.onBackPressed();
            mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
            mainActivity.fragNavController.switchTab(mainActivity.TAB_FIRST);
        }
        if (old_user.equals("domain")){
            domainMainActivity.onBackPressed();
//            mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
//            mainActivity.fragNavController.switchTab(mainActivity.TAB_FIRST);
        }



//        finish();
        //intentMainPage = new Intent(Verify_Code.this, DomainMainActivity.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(getActivity().root.getContext(), "Subsidy Pause", Toast.LENGTH_LONG).show();
//        Fragment fragment = new HomeFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, fragment);
//        //fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}