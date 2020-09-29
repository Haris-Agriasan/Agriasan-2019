package agri_asan.com.agriasan06_12_19;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DomainContactUsFragment extends Fragment {

    Resources resources;

    private Firebase mRootref;
    String id;
    Firebase childRef;

    String formattedDate;
    String formattedTime;

    Button enter_message_btn;
    ImageView call_team_btn;
    TextView user_name_text;
    TextView user_city_text;
    TextView user_phone_text;

    EditText user_message_edittext;

    DomainMainActivity domainMainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);
        domainMainActivity=(DomainMainActivity) getActivity();


        enter_message_btn=root.findViewById(R.id.message_btn_contact_us);
        call_team_btn=root.findViewById(R.id.btn_call_team);

        user_name_text=root.findViewById(R.id.text_name_user);
        user_name_text.setText(domainMainActivity.Name+"");
        user_city_text=root.findViewById(R.id.text_city_user);
        user_city_text.setText(domainMainActivity.City+"");
        user_phone_text=root.findViewById(R.id.text_phone_user);
        user_phone_text.setText("0"+domainMainActivity.Phone+"");

        user_message_edittext=root.findViewById(R.id.edittext_message_user);

        enter_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(user_message_edittext.getText())){
                    user_message_edittext.setError( "پہلے اپنا پیغام لکھیں!" );
                    Toast.makeText(getContext(), "پہلے اپنا پیغام لکھیں!", Toast.LENGTH_LONG).show();
                }else{
                    update_message_db();
                    Toast.makeText(getActivity().getApplicationContext(), "آپکا پیغام درج ہو گیا ہے شکریہ !", Toast.LENGTH_LONG).show();
                    user_message_edittext.setText("");
                }

            }
        });
        call_team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "آپکا پیغام درج ہو گیا ہے شکریہ !", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "03006801388", null)));
            }
        });

        return root;


    }
    public void update_message_db(){
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
    public void onPause() {
        super.onPause();
        //Toast.makeText(getActivity().getApplicationContext(), "Subsidy Pause", Toast.LENGTH_LONG).show();
//        Fragment fragment = new HomeFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, fragment);
//        //fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }
}