package agri_asan.com.agriasan06_12_19;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ContactUsFragment extends Fragment {

    String user_type="";
    private Firebase mRootref;
    String id;
    Firebase childRef;
    String formattedDate, formattedTime;

    Button enter_message_btn;
    ImageView call_team_btn;
    TextView user_name_text, user_city_text, user_phone_text;
    EditText user_message_edittext;
    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            user_type = getArguments().getString("user");
        }
        if (user_type != null) {
            if (user_type.equals("member")){
                mainActivity = (MainActivity) getActivity();

            }else if(user_type.equals("domain")){
                domainMainActivity = (DomainMainActivity) getActivity();
            }
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);

        enter_message_btn=root.findViewById(R.id.message_btn_contact_us);
        call_team_btn=root.findViewById(R.id.btn_call_team);

        if (user_type != null) {
            if (user_type.equals("member")){
                user_name_text=root.findViewById(R.id.text_name_user);
                user_name_text.setText(mainActivity.Name+"");
                user_city_text=root.findViewById(R.id.text_city_user);
                user_city_text.setText(mainActivity.City+"");
                user_phone_text=root.findViewById(R.id.text_phone_user);
                user_phone_text.setText("0"+mainActivity.Phone+"");
            }else if(user_type.equals("domain")){
                domainMainActivity = (DomainMainActivity) getActivity();
                user_name_text=root.findViewById(R.id.text_name_user);
                user_name_text.setText(domainMainActivity.Name+"");
                user_city_text=root.findViewById(R.id.text_city_user);
                user_city_text.setText(domainMainActivity.City+"");
                user_phone_text=root.findViewById(R.id.text_phone_user);
                user_phone_text.setText("0"+domainMainActivity.Phone+"");
            }
        }
        user_message_edittext=root.findViewById(R.id.edittext_message_user);
        enter_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(user_message_edittext.getText())){
                    user_message_edittext.setError( "پہلے اپنا پیغام لکھیں!" );
                    Toast.makeText(getContext(), "پہلے اپنا پیغام لکھیں!", Toast.LENGTH_LONG).show();
                }else{
                    if (user_type.equals("member")){
                        update_message_db_mainactivity();
                    }else if(user_type.equals("domain")){
                        update_message_db_domainmainactivity();
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "آپکا پیغام درج ہو گیا ہے شکریہ !", Toast.LENGTH_LONG).show();
                    user_message_edittext.setText("");
                }
            }
        });
        call_team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "03006801388", null)));
            }
        });
        return root;
    }
    public void update_message_db_mainactivity(){
        mRootref.setAndroidContext(getActivity());
        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Messages");
        id = mRootref.push().getKey();

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("Message");
        childRef.setValue(user_message_edittext.getText() + "");

        childRef = mRootref.child(id).child("Imei");
        childRef.setValue(mainActivity.Imei + "");

        childRef = mRootref.child(id).child("Name");
        childRef.setValue(mainActivity.Name + "");

        childRef = mRootref.child(id).child("Phone");
        childRef.setValue(mainActivity.Phone + "");

        childRef = mRootref.child(id).child("City");
        childRef.setValue(mainActivity.City + "");

        childRef = mRootref.child(id).child("ID_Card");
        childRef.setValue(mainActivity.ID_Card + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(mainActivity.ID + "");

        childRef = mRootref.child(id).child("User");
        childRef.setValue("member");

        childRef = mRootref.child(id).child("Longi");
        childRef.setValue(""+mainActivity.Location_Long);

        childRef = mRootref.child(id).child("Lati");
        childRef.setValue(""+mainActivity.Location_Lat);
    }
    public void update_message_db_domainmainactivity(){
        mRootref.setAndroidContext(getActivity());
        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Messages");
        id = mRootref.push().getKey();

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("Message");
        childRef.setValue(user_message_edittext.getText() + "");

        childRef = mRootref.child(id).child("Imei");
        childRef.setValue(domainMainActivity.Imei + "");

        childRef = mRootref.child(id).child("Name");
        childRef.setValue(domainMainActivity.Name + "");

        childRef = mRootref.child(id).child("Phone");
        childRef.setValue(domainMainActivity.Phone + "");

        childRef = mRootref.child(id).child("City");
        childRef.setValue(domainMainActivity.City + "");

        childRef = mRootref.child(id).child("ID_Card");
        childRef.setValue(domainMainActivity.ID_Card + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(domainMainActivity.ID + "");

        childRef = mRootref.child(id).child("User");
        childRef.setValue("member");

        childRef = mRootref.child(id).child("Longi");
        childRef.setValue(""+domainMainActivity.Location_Long);

        childRef = mRootref.child(id).child("Lati");
        childRef.setValue(""+domainMainActivity.Location_Lat);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (user_type.equals("member")){
            mainActivity.mTitle.setText(R.string.contact_us);
            mainActivity.v3_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.v3_down.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.layout_contact_us.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            domainMainActivity.mTitle.setText(R.string.contact_us);
            domainMainActivity.v3_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.v3_down.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.layout_contact_us.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
    }
    @Override
    public void onPause() {
        if (user_type.equals("member")){
            mainActivity.layout_contact_us.getBackground().setAlpha(0);
            mainActivity.v3_upper.getBackground().setAlpha(0);
            mainActivity.v3_down.getBackground().setAlpha(0);
        }else{
            domainMainActivity.layout_contact_us.getBackground().setAlpha(0);
            domainMainActivity.v3_upper.getBackground().setAlpha(0);
            domainMainActivity.v3_down.getBackground().setAlpha(0);
        }
        super.onPause();
    }
}