package agri_asan.com.agriasan06_12_19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Belal on 1/23/2018.
 */

public class PublicOpinionFragment extends Fragment {

    Resources resources;


    ToggleImageButton current_location_toggle;
    Boolean check_location=false;

    PopupWindow popupWindow;

    DatabaseReference reference;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static MemberAwamiRayeCustomAdapter adapter;

    String Total_Images;
    String Total_Answer_Images;
    String City = "";
    String Name = "";
    String Fasal = "";
    String Question_Image_0 = "";
    String Question_Image_1 = "";
    String Question_Image_2 = "";
    String Question_Image_3 = "";
    String Question_Image_4 = "";
    String Question_Image_5 = "";
    String Recording = "";
    String Time = "";
    String ID = "";
    String Occupation = "";
    String Question;
    String Member_ID;
    String Date = "";

    String Answer;
    String Answer_Date;
    String Answer_Image_0;
    String Answer_Image_1;
    String Answer_Image_2;
    String Answer_Image_3;
    String Answer_Image_4;
    String Answer_Image_5;
    String Answer_Time;
    String Domain_City;
    String Domain_Name;
    String Domain_occupation;
    String Recording_Answer;
    String extra_thing;
    String Fasal_Type_Shoba;


    TextView choose_fasal_edittext;
    TextView choose_shoba_edittext;



    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Set<String> set_fasal = new HashSet<String>();
    Set<String> set_shoba = new HashSet<String>();
    Set<String> set_temporary_shoba = new HashSet<String>();
    Set<String> set_temporary_fasal = new HashSet<String>();

    String fasals_selected = "";
    String shobas_selected = "";

    LinearLayout choose_fasal_layout;
    LinearLayout choose_shoba_layout;

    View root;

    /////FASAL SELECTOR POPUP
    int flag_cotton,flag_chawal,flag_gandum,flag_daalen,flag_makai,
            flag_ganna,flag_poultry,flag_fish,
            flag_sabziyan,flag_sooraj_mukhi,flag_sarson,flag_animals;

    /////FASAL SELECTOR POPUP END

    /////QUESTION TYPE POPUP
    int flag_seed,flag_medicine,flag_khaad,
            flag_mehkama_tausee,flag_mehkama_inhaar,flag_matti
            ,flag_keeray,flag_herb
            ,flag_tunnel;
    /////QUESTION TYPE POPUP END

    Context thiscontext;

    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    Intent intent;

    String user_type="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_public_opinion, null);

        resources= Objects.requireNonNull(getContext()).getResources();

        current_location_toggle=root.findViewById(R.id.current_location_toggle);
        current_location_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_location_toggle.isChecked()){
                    Toast.makeText(getActivity(), "آپکے اردگرد کی عوا می رائے!", Toast.LENGTH_LONG).show();
                    if (user_type.equals("member")){
                        check_location=true;
                        mainActivity.fragNavController.switchTab(mainActivity.TAB_Fourth);
                        mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
                        mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
                    }
                    if (user_type.equals("domain")){
                        check_location=true;
                        domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_Fourth);
                        domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_SECOND);
                    }
                }else{
                    if (user_type.equals("member")){
                        check_location=false;
                        mainActivity.fragNavController.switchTab(mainActivity.TAB_Fourth);
                        mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
                    }
                    if (user_type.equals("domain")){
                        check_location=false;
                        domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_Fourth);
                        domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_SECOND);
                    }
                }
            }
        });

        current_location_toggle=root.findViewById(R.id.current_location_toggle);



        intent = getActivity().getIntent();

        user_type = intent.getStringExtra("User");

        if (user_type.equals("member")){
            mainActivity=(MainActivity) getActivity();
        }else if (user_type.equals("domain")){
            domainMainActivity=(DomainMainActivity) getActivity();
        }

        thiscontext = container.getContext();
        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        choose_fasal_edittext = root.findViewById(R.id.edittext_fasal_name);
        choose_shoba_edittext = root.findViewById(R.id.edittext_shoba_name);


        choose_fasal_layout = root.findViewById(R.id.layout_choose_fasal);
        choose_shoba_layout = root.findViewById(R.id.layout_choose_shoba);

        choose_fasal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_fasal_fun();
            }
        });
        choose_shoba_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_shoba_fun();
            }
        });



        reference = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) root.findViewById(R.id.list_member_awami_raye);
        dataModels = new ArrayList<>();
        set_fasal = pref.getStringSet("choosed_fasal", null); // getting String
        set_shoba = null;
        set_shoba = pref.getStringSet("choosed_shoba", null); // getting String
        String check_null_shoba = set_shoba + "";
        String check_null_fasal = set_fasal + "";


        if (check_null_fasal.equals("null")) {
            //Toast.makeText(getActivity(), "nulll", Toast.LENGTH_LONG).show();
            set_temporary_fasal.add("کپاس");
            set_temporary_fasal.add("گندم");
            set_temporary_fasal.add("چاول");
            set_temporary_fasal.add("مکئی");
            set_temporary_fasal.add("گنا");
            set_temporary_fasal.add("دالیں");
//            set_temporary_fasal.add("پولٹری");
//            set_temporary_fasal.add("مچھلی");
            set_temporary_fasal.add("سبزیاں");
            set_temporary_fasal.add("سورج مکھی");
            set_temporary_fasal.add("سرسوں");
//            set_temporary_fasal.add("جانور");
            editor.putStringSet("choosed_fasal", set_temporary_fasal); // Storing string Set
            editor.commit();
            set_fasal = pref.getStringSet("choosed_fasal", null); // getting String
            check_null_fasal = "";
        }
        if (check_null_shoba.equals("null")) {
            //Toast.makeText(getActivity(), "nulll", Toast.LENGTH_LONG).show();
            set_temporary_shoba.add(resources.getString(R.string.matti));
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
            set_shoba = pref.getStringSet("choosed_shoba", null); // getting String
            check_null_shoba = "";
        }

        for (String str : set_fasal) {
            fasals_selected = fasals_selected + "" + str + " - ";
        }
        for (String str : set_shoba) {
            shobas_selected = shobas_selected + "" + str + " - ";
        }
        choose_fasal_edittext.setText(fasals_selected);
        choose_shoba_edittext.setText(shobas_selected);
        fasals_selected = "";
        shobas_selected = "";
        final Query query = reference.child("Questions");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataModels=new ArrayList<>();
                    adapter = new MemberAwamiRayeCustomAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);
                    //question_no=dataSnapshot.getChildrenCount();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Member user = issue.getValue(Member.class);
                        Fasal_Type_Shoba = user.getFasal_Type_Shoba();
                        Question = user.getQuestion(); ////////
                        City = user.getMember_City();     ////////
                        Time = user.getTime();   ////////
                        Fasal = user.getFasal();      ////////
                        ID = user.getID();
                        Name = user.getMember_Name();    ////////
                        Recording = user.getRecording();
                        Question_Image_0 = user.getImage_0();
                        Question_Image_1 = user.getImage_1();
                        Question_Image_2 = user.getImage_2();
                        Question_Image_3 = user.getImage_3();
                        Question_Image_4 = user.getImage_4();
                        Question_Image_5 = user.getImage_5();
                        Domain_Name = user.getDomain_Name();      /////
                        Answer = user.getAnswer();   ///
                        Answer_Date = user.getAnswer_Date();      /////
                        Answer_Time = user.getAnswer_Time();    ////
                        Domain_occupation = user.getDomain_Occupation();           //////
                        Recording_Answer = user.getRecording_Answer();
                        Domain_City = user.getDomain_City();
                        Answer_Image_0 = user.getAnswer_Image_0();
                        Answer_Image_1 = user.getAnswer_Image_1();
                        Answer_Image_2 = user.getAnswer_Image_2();
                        Answer_Image_3 = user.getAnswer_Image_3();
                        Answer_Image_4 = user.getAnswer_Image_4();
                        Answer_Image_5 = user.getAnswer_Image_5();
                        extra_thing = Name + " / " + City;

                        Member_ID = user.getMember_ID();
                        Date = user.getDate();
                        Total_Images=user.getTotal_Images();
                        Total_Answer_Images=user.getTotal_Answer_Images();

                        int Question_Total_images=0;
                        if (!TextUtils.isEmpty(user.getTotal_Images())){
                            Question_Total_images = Integer.parseInt(user.getTotal_Images());
                        }
                        ArrayList<String> images_list=new ArrayList<>();
//                        images_list.add(Question_Image_0);
                        if (Question_Total_images==1){
                            images_list.add(user.getImage_0());
                        }if (Question_Total_images==2){
                            images_list.add(user.getImage_0());
                            images_list.add(user.getImage_1());
                        }if (Question_Total_images==3){
                            images_list.add(user.getImage_0());
                            images_list.add(user.getImage_1());
                            images_list.add(user.getImage_2());
                        }if (Question_Total_images==4){
                            images_list.add(user.getImage_0());
                            images_list.add(user.getImage_1());
                            images_list.add(user.getImage_2());
                            images_list.add(user.getImage_3());
                        }if (Question_Total_images==5){
                            images_list.add(user.getImage_0());
                            images_list.add(user.getImage_1());
                            images_list.add(user.getImage_2());
                            images_list.add(user.getImage_3());
                            images_list.add(user.getImage_4());
                        }if (Question_Total_images==6){
                            images_list.add(user.getImage_0());
                            images_list.add(user.getImage_1());
                            images_list.add(user.getImage_2());
                            images_list.add(user.getImage_3());
                            images_list.add(user.getImage_4());
                            images_list.add(user.getImage_5());
                        }
                        int Answer_Total_images=0;
                        if (!TextUtils.isEmpty(user.getTotal_Answer_Images())){
                            Answer_Total_images = Integer.parseInt(user.getTotal_Answer_Images());
                        }
                        ///////////////////////////////////////////
                        ArrayList<String> images_list_answer=new ArrayList<>();
                        if (Answer_Total_images==1){
                            images_list_answer.add(user.getAnswer_Image_0());
                        }if (Answer_Total_images==2){
                            images_list_answer.add(user.getAnswer_Image_0());
                            images_list_answer.add(user.getAnswer_Image_1());
                        }if (Answer_Total_images==3){
                            images_list_answer.add(user.getAnswer_Image_0());
                            images_list_answer.add(user.getAnswer_Image_1());
                            images_list_answer.add(user.getAnswer_Image_2());
                        }if (Answer_Total_images==4){
                            images_list_answer.add(user.getAnswer_Image_0());
                            images_list_answer.add(user.getAnswer_Image_1());
                            images_list_answer.add(user.getAnswer_Image_2());
                            images_list_answer.add(user.getAnswer_Image_3());
                        }if (Answer_Total_images==5){
                            images_list_answer.add(user.getAnswer_Image_0());
                            images_list_answer.add(user.getAnswer_Image_1());
                            images_list_answer.add(user.getAnswer_Image_2());
                            images_list_answer.add(user.getAnswer_Image_3());
                            images_list_answer.add(user.getAnswer_Image_4());
                        }if (Answer_Total_images==6){
                            images_list_answer.add(user.getAnswer_Image_0());
                            images_list_answer.add(user.getAnswer_Image_1());
                            images_list_answer.add(user.getAnswer_Image_2());
                            images_list_answer.add(user.getAnswer_Image_3());
                            images_list_answer.add(user.getAnswer_Image_4());
                            images_list_answer.add(user.getAnswer_Image_5());
                        }
                        Double lati=0.0;
                        Double longi=0.0;
                        if (!TextUtils.isEmpty(user.getLati()))
                        {
                            lati=Double.parseDouble(user.getLati()+"");
                        }
                        if (!TextUtils.isEmpty(user.getLongi()))
                        {
                            longi=Double.parseDouble(user.getLongi()+"");
                        }
                        //Toast.makeText(getActivity(), ""+lati, Toast.LENGTH_LONG).show();

                        Location usersLocation = new Location("");//provider name is unnecessary
                        if (user_type.equals("member")){
                            usersLocation.setLatitude(mainActivity.Location_Lat);
                            usersLocation.setLongitude(mainActivity.Location_Long);
                        }
                        if (user_type.equals("domain")){
                            usersLocation.setLatitude(domainMainActivity.Location_Lat);
                            usersLocation.setLongitude(domainMainActivity.Location_Long);
                        }

                        Location questionLocation = new Location("");//provider name is unnecessary
                        questionLocation.setLatitude(lati);
                        questionLocation.setLongitude(longi);

//                        if (user_type.equals("member")){
//                            //Toast.makeText(getContext(), ""+mainActivity.Location_Lat+"   "+mainActivity.Location_Long, Toast.LENGTH_LONG).show();
//                        }
//                        if (user_type.equals("domain")){
//                            //Toast.makeText(getContext(), ""+domainMainActivity.Location_Lat+"   "+domainMainActivity.Location_Long, Toast.LENGTH_LONG).show();
//                        }

                        float distanceInMeters =  questionLocation.distanceTo(usersLocation);
                        float distanceInKM=distanceInMeters/1000;
//                        Toast.makeText(getActivity(), ""+distanceInMeters/1000, Toast.LENGTH_LONG).show();
                            distanceInKM=1f;

                        if (!check_location){ distanceInKM=1f; }
                        else{ distanceInKM=distanceInMeters/1000; }

                        if (distanceInKM<=100f){
                        //DO NOT DELETE ITS FOR CHECKING IF ANSWER GIVEN OR NOT
                        if (Answer!=null ) {
                            //|| list.contains("سورج مکھی")
                            for (String str : set_fasal) {
                                if (!TextUtils.isEmpty(Fasal)) {

                                    if (Fasal.equals(str)) {

                                        for (String str_1 : set_shoba) {
                                            if (!TextUtils.isEmpty(Fasal_Type_Shoba)) {

                                                if (Fasal_Type_Shoba.equals(str_1)) {

                                                    dataModels.add(new DataModel(Question + "", City + "", Time + "",
                                                            Fasal + "",
                                                            ID + "", Name + "", Recording+"", Question_Image_0 + "",
                                                            images_list, images_list_answer, Member_ID + "",
                                                            Date + "",
                                                            Answer, Domain_City, Domain_Name, Answer_Date, Answer_Time, Domain_occupation, Recording_Answer+"", extra_thing, user_type));
                                                }
                                            }
                                        }
                                    }
                                }
                                //Toast.makeText(getActivity(), "!"+str, Toast.LENGTH_LONG).show();
                            }
                        }
                        }

                    }
                    Collections.reverse(dataModels);
                    adapter = new MemberAwamiRayeCustomAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);
//                    listView.setOnScrollListener(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        listView.setAdapter(adapter);
        return root;

    }

    public void choose_fasal_fun() {
        fasals_selected = "";
        popup_window_select_fasal(root);
    }

    public void choose_shoba_fun() {
        shobas_selected = "";
        popup_window_select_question_type(root);
    }
    public void popup_window_select_question_type(View v) {
        ImageView seed, medicine, khaad, mehkama_tausee, mehkama_inhaar, matti, herb, keeray, tunnel;

        ImageView btn_tick_seed, btn_tick_medicine, btn_tick_khaad,
                btn_tick_mehkama_tausee, btn_tick_mehkama_inhaar,
                btn_tick_matti, btn_tick_keeray, btn_tick_herb, btn_tick_tunnel;

        Button button_shoba_choosed;

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        Set<String> set_temporary_shoba = new HashSet<String>();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.awami_raye_popup_choose_question_type, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height);


        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


        Set<String> list = new HashSet<String>();
        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        set_temporary_shoba=pref.getStringSet("choosed_shoba",null); // getting String

        button_shoba_choosed=popupView.findViewById(R.id.button_shoba_choosed);

        flag_khaad=0;
        flag_matti=0;
        flag_mehkama_inhaar=0;
        flag_mehkama_tausee=0;
        flag_seed=0;
        flag_medicine=0;
        flag_keeray=0;
        flag_herb=0;
        flag_tunnel=0;

        btn_tick_khaad=popupView.findViewById(R.id.tick_khaad);
        btn_tick_matti=popupView.findViewById(R.id.tick_matti);
        btn_tick_medicine=popupView.findViewById(R.id.tick_medicine);
        btn_tick_mehkama_inhaar=popupView.findViewById(R.id.tick_mehkama_inhaar);
        btn_tick_mehkama_tausee=popupView.findViewById(R.id.tick_mehkama_tausee);
        btn_tick_seed=popupView.findViewById(R.id.tick_seed);
        btn_tick_keeray=popupView.findViewById(R.id.tick_keeray);
        btn_tick_herb=popupView.findViewById(R.id.tick_herb);
        btn_tick_tunnel=popupView.findViewById(R.id.tick_tunnel);

        btn_tick_seed.setVisibility(View.GONE);
        btn_tick_matti.setVisibility(View.GONE);
        btn_tick_medicine.setVisibility(View.GONE);
        btn_tick_mehkama_tausee.setVisibility(View.GONE);
        btn_tick_mehkama_inhaar.setVisibility(View.GONE);
        btn_tick_khaad.setVisibility(View.GONE);
        btn_tick_keeray.setVisibility(View.GONE);
        btn_tick_herb.setVisibility(View.GONE);
        btn_tick_tunnel.setVisibility(View.GONE);


        //Toast.makeText(getActivity().getApplicationContext(), "Check "+m.City, Toast.LENGTH_LONG).show();
        seed = popupView.findViewById(R.id.seed);
        medicine = popupView.findViewById(R.id.medicine);
        khaad = popupView.findViewById(R.id.khaad);
        mehkama_tausee = popupView.findViewById(R.id.mehkama_tausee);
        mehkama_inhaar = popupView.findViewById(R.id.mehkama_inhaar);
        matti = popupView.findViewById(R.id.matti);
        keeray = popupView.findViewById(R.id.keeray);
        herb = popupView.findViewById(R.id.herb);
        tunnel = popupView.findViewById(R.id.tunnel_farming);

        button_shoba_choosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(popupView.getContext().getApplicationContext(), list+"", Toast.LENGTH_LONG).show();
                editor.putStringSet("choosed_shoba", list); // Storing string Set
                editor.commit();
                if (user_type.equals("member")){
                    mainActivity.fragNavController.switchTab(mainActivity.TAB_FIRST);
                    mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
                }
                if (user_type.equals("domain")){
                    domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_FIRST);
                    domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_SECOND);
                }
                popupWindow.dismiss();
            }
        });



        if (set_temporary_shoba.contains("بیج")){
//            Toast.makeText(popupView.getContext().getApplicationContext(), "CHECK Beej", Toast.LENGTH_LONG).show();
            flag_seed=1;
            btn_tick_seed.setVisibility(View.VISIBLE);
            list.add("بیج");
        }else{
//            Toast.makeText(popupView.getContext().getApplicationContext(), "CHECK 2 Beej", Toast.LENGTH_LONG).show();
        }
        if (set_temporary_shoba.contains("ادویات")){
            flag_medicine=1;
            btn_tick_medicine.setVisibility(View.VISIBLE);
            list.add("ادویات");
        }
        if (set_temporary_shoba.contains("کھاد")){
            flag_khaad=1;
            btn_tick_khaad.setVisibility(View.VISIBLE);
            list.add("کھاد");
        }
        if (set_temporary_shoba.contains("محکمہ توسیع")){
            flag_mehkama_tausee=1;
            btn_tick_mehkama_tausee.setVisibility(View.VISIBLE);
            list.add("محکمہ توسیع");
        }
        if (set_temporary_shoba.contains("محکمہ انھار")){
            flag_mehkama_inhaar=1;
            btn_tick_mehkama_inhaar.setVisibility(View.VISIBLE);
            list.add("محکمہ انھار");
        }
        if (set_temporary_shoba.contains("مٹی")){
            flag_matti=1;
            btn_tick_matti.setVisibility(View.VISIBLE);
            list.add("مٹی");
        }
        if (set_temporary_shoba.contains("ٹنل فارمنگ")){
            flag_tunnel=1;
            btn_tick_tunnel.setVisibility(View.VISIBLE);
            list.add("ٹنل فارمنگ");
        }
        if (set_temporary_shoba.contains("جڑی بوٹیاں")){
//            Toast.makeText(popupView.getContext().getApplicationContext(), "CHECK", Toast.LENGTH_LONG).show();
            flag_herb=1;
            btn_tick_herb.setVisibility(View.VISIBLE);
            list.add("جڑی بوٹیاں");
        }else{
//            Toast.makeText(popupView.getContext().getApplicationContext(), "CHECK 2", Toast.LENGTH_LONG).show();
        }
        if (set_temporary_shoba.contains("کیڑے")){
//            Toast.makeText(popupView.getContext().getApplicationContext(), "CHECK Keeray", Toast.LENGTH_LONG).show();
            flag_keeray=1;
            btn_tick_keeray.setVisibility(View.VISIBLE);
            list.add("کیڑے");
        }
        keeray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_keeray==0){
                    btn_tick_keeray.setVisibility(View.VISIBLE);
                    list.add("کیڑے");
                    flag_keeray=1;
                }else{
                    btn_tick_keeray.setVisibility(View.GONE);
                    list.remove("کیڑے");
                    flag_keeray=0;
                }
            }
        });
        herb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_herb==0){
                    btn_tick_herb.setVisibility(View.VISIBLE);
                    list.add("جڑی بوٹیاں");
                    flag_herb=1;
                }else{
                    btn_tick_herb.setVisibility(View.GONE);
                    list.remove("جڑی بوٹیاں");
                    flag_herb=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                //saving_question_type_for_question("ادویات");
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_seed==0){
                    btn_tick_seed.setVisibility(View.VISIBLE);
                    list.add("بیج");
                    flag_seed=1;
                }else{
                    btn_tick_seed.setVisibility(View.GONE);
                    list.remove("بیج");
                    flag_seed=0;
                }
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_medicine==0){
                    btn_tick_medicine.setVisibility(View.VISIBLE);
                    list.add("ادویات");
                    flag_medicine=1;
                }else{
                    btn_tick_medicine.setVisibility(View.GONE);
                    list.remove("ادویات");
                    flag_medicine=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                //saving_question_type_for_question("ادویات");
            }
        });
        khaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_khaad==0){
                    btn_tick_khaad.setVisibility(View.VISIBLE);
                    list.add("کھاد");
                    flag_khaad=1;
                }else{
                    btn_tick_khaad.setVisibility(View.GONE);
                    list.remove("کھاد");
                    flag_khaad=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "چاول", Toast.LENGTH_LONG).show();
                //saving_question_type_for_question("کھاد");
            }
        });
        mehkama_tausee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_mehkama_tausee==0){
                    btn_tick_mehkama_tausee.setVisibility(View.VISIBLE);
                    list.add("محکمہ توسیع");
                    flag_mehkama_tausee=1;
                }else{
                    btn_tick_mehkama_tausee.setVisibility(View.GONE);
                    list.remove("محکمہ توسیع");
                    flag_mehkama_tausee=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
                //saving_question_type_for_question("محکمہ توسیع");

            }
        });
        mehkama_inhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_mehkama_inhaar==0){
                    btn_tick_mehkama_inhaar.setVisibility(View.VISIBLE);
                    list.add("محکمہ انھار");
                    flag_mehkama_inhaar=1;
                }else{
                    btn_tick_mehkama_inhaar.setVisibility(View.GONE);
                    list.remove("محکمہ انھار");
                    flag_mehkama_inhaar=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "گنا", Toast.LENGTH_LONG).show();
                //saving_question_type_for_question("محکمہ انھار");
            }
        });
        matti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_matti==0){
                    btn_tick_matti.setVisibility(View.VISIBLE);
                    list.add("مٹی");
                    flag_matti=1;
                }else{
                    btn_tick_matti.setVisibility(View.GONE);
                    list.remove("مٹی");
                    flag_matti=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "دالیں", Toast.LENGTH_LONG).show();
            }
        });
        tunnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_tunnel==0){
                    btn_tick_tunnel.setVisibility(View.VISIBLE);
                    list.add("ٹنل فارمنگ");
                    flag_tunnel=1;
                }else{
                    btn_tick_tunnel.setVisibility(View.GONE);
                    list.remove("ٹنل فارمنگ");
                    flag_tunnel=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "دالیں", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (user_type.equals("member")){
            mainActivity.mTitle.setText(R.string.title_public_opinion);
        }

        if (user_type.equals("domain")){
            domainMainActivity.mTitle.setText(R.string.title_public_opinion);
        }
    }

    public void popup_window_select_fasal(View v) {

        ImageView cotton,gandum, chawal,makai,daalen,poultry,ganna,sabziyan,sarson,animals,sooraj_mukhi,fish;

        ImageView btn_tick_cotton, btn_tick_chawal, btn_tick_gandum, btn_tick_daalen,
                btn_tick_makai, btn_tick_ganna, btn_tick_poultry ,btn_tick_fish, btn_tick_sabziyan
        ,btn_tick_sooraj_mukhi, btn_tick_sarson, btn_tick_animals;

        Button button_fasal_choosed;

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        Set<String> set_temporary_fasal = new HashSet<String>();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.awami_raye_popup_choose_fasal, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height);



        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        Set<String> list = new HashSet<String>();

        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();


        set_temporary_fasal=pref.getStringSet("choosed_fasal",null); // getting String

        button_fasal_choosed=popupView.findViewById(R.id.button_fasal_choosed);
        flag_sabziyan=0;
        flag_cotton=0;
        flag_fish=0;
        flag_chawal=0;
        flag_daalen=0;
        flag_animals=0;
        flag_poultry=0;
        flag_sooraj_mukhi=0;
        flag_sarson=0;
        flag_makai=0;
        flag_ganna=0;
        flag_gandum=0;

        btn_tick_cotton=popupView.findViewById(R.id.tick_cotton);
        btn_tick_chawal=popupView.findViewById(R.id.tick_chawal);
        btn_tick_gandum=popupView.findViewById(R.id.tick_gandum);
        btn_tick_daalen=popupView.findViewById(R.id.tick_daalen);
        btn_tick_makai=popupView.findViewById(R.id.tick_makai);
        btn_tick_ganna=popupView.findViewById(R.id.tick_ganna);
        btn_tick_poultry=popupView.findViewById(R.id.tick_poultry);
        btn_tick_fish=popupView.findViewById(R.id.tick_fish);
        btn_tick_sabziyan=popupView.findViewById(R.id.tick_sabziyan);
        btn_tick_sooraj_mukhi=popupView.findViewById(R.id.tick_sooraj_mukhi);
        btn_tick_sarson=popupView.findViewById(R.id.tick_sarson);
        btn_tick_animals=popupView.findViewById(R.id.tick_animals);

        btn_tick_cotton.setVisibility(View.GONE);
        btn_tick_chawal.setVisibility(View.GONE);
        btn_tick_gandum.setVisibility(View.GONE);
        btn_tick_daalen.setVisibility(View.GONE);
        btn_tick_makai.setVisibility(View.GONE);
        btn_tick_ganna.setVisibility(View.GONE);
        btn_tick_poultry.setVisibility(View.GONE);
        btn_tick_fish.setVisibility(View.GONE);
        btn_tick_sabziyan.setVisibility(View.GONE);
        btn_tick_sooraj_mukhi.setVisibility(View.GONE);
        btn_tick_sarson.setVisibility(View.GONE);
        btn_tick_animals.setVisibility(View.GONE);

        if (set_temporary_fasal.contains("کپاس")){
            flag_cotton=1;
            btn_tick_cotton.setVisibility(View.VISIBLE);
            list.add("کپاس");
        }
        if (set_temporary_fasal.contains("گندم")){
            flag_gandum=1;
            btn_tick_gandum.setVisibility(View.VISIBLE);
            list.add("گندم");
        }
        if (set_temporary_fasal.contains("چاول")){
            flag_chawal=1;
            btn_tick_chawal.setVisibility(View.VISIBLE);
            list.add("چاول");
        }
        if (set_temporary_fasal.contains("مکئی")){
            flag_makai=1;
            btn_tick_makai.setVisibility(View.VISIBLE);
            list.add("مکئی");
        }
        if (set_temporary_fasal.contains("گنا")){
            flag_ganna=1;
            btn_tick_ganna.setVisibility(View.VISIBLE);
            list.add("گنا");
        }
        if (set_temporary_fasal.contains("دالیں")){
            flag_daalen=1;
            btn_tick_daalen.setVisibility(View.VISIBLE);
            list.add("دالیں");
        }
//        if (set_temporary_fasal.contains("پولٹری")){
//            flag_poultry=1;
//            btn_tick_poultry.setVisibility(View.VISIBLE);
//            list.add("پولٹری");
//        }
        if (set_temporary_fasal.contains("سبزیاں")){
            flag_sabziyan=1;
            btn_tick_sabziyan.setVisibility(View.VISIBLE);
            list.add("سبزیاں");
        }
//        if (set_temporary_fasal.contains("مچھلی")){
//            flag_fish=1;
//            btn_tick_fish.setVisibility(View.VISIBLE);
//            list.add("مچھلی");
//        }
        if (set_temporary_fasal.contains("سورج مکھی")){
            flag_sooraj_mukhi=1;
            btn_tick_sooraj_mukhi.setVisibility(View.VISIBLE);
            list.add("سورج مکھی");
        }
        if (set_temporary_fasal.contains("سرسوں")){
            flag_sarson=1;
            btn_tick_sarson.setVisibility(View.VISIBLE);
            list.add("سرسوں");
        }
//        if (set_temporary_fasal.contains("جانور")){
//            flag_animals=1;
//            btn_tick_animals.setVisibility(View.VISIBLE);
//            list.add("جانور");
//        }

        button_fasal_choosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(popupView.getContext().getApplicationContext(), list+"", Toast.LENGTH_LONG).show();
                editor.putStringSet("choosed_fasal", list); // Storing string Set
                editor.commit();
                if (user_type.equals("member")){
                    mainActivity.fragNavController.switchTab(mainActivity.TAB_FIRST);
                    mainActivity.fragNavController.switchTab(mainActivity.TAB_SECOND);
                }
                if (user_type.equals("domain")){
                    domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_FIRST);
                    domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_SECOND);
                }
                popupWindow.dismiss();

                //mainActivity.fragNavController.clearStack();

            }
        });
        //final TextView textView = popupView.findViewById(R.id.text_bazaar);
        cotton = popupView.findViewById(R.id.cotton);
        gandum = popupView.findViewById(R.id.gandum);
        chawal = popupView.findViewById(R.id.chawal);
        makai = popupView.findViewById(R.id.makai);
        ganna = popupView.findViewById(R.id.ganna);
        daalen = popupView.findViewById(R.id.daalen);
        poultry = popupView.findViewById(R.id.poultry);
        fish = popupView.findViewById(R.id.fish);
        sabziyan = popupView.findViewById(R.id.sabziyan);
        sooraj_mukhi = popupView.findViewById(R.id.sooraj_mukhi);
        sarson = popupView.findViewById(R.id.sarson);
        animals = popupView.findViewById(R.id.animals);
        BottomNavigationView navView = popupView.findViewById(R.id.navigation);

        cotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_cotton==0){
                    btn_tick_cotton.setVisibility(View.VISIBLE);
                    list.add("کپاس");
                    flag_cotton=1;
                }else{
                    btn_tick_cotton.setVisibility(View.GONE);
                    list.remove("کپاس");
                    flag_cotton=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "کپاس", Toast.LENGTH_LONG).show();
                //saving_fasal_for_question("کپاس");
            }
        });
        gandum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_gandum==0){
                    btn_tick_gandum.setVisibility(View.VISIBLE);
                    list.add("گندم");
                    flag_gandum=1;
                }else{
                    btn_tick_gandum.setVisibility(View.GONE);
                    list.remove("گندم");
                    flag_gandum=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("گندم");
            }
        });
        chawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_chawal==0){
                    btn_tick_chawal.setVisibility(View.VISIBLE);
                    list.add("چاول");
                    flag_chawal=1;
                }else{
                    btn_tick_chawal.setVisibility(View.GONE);
                    list.remove("چاول");
                    flag_chawal=0;
                }
                //list.add("چاول");
                //Toast.makeText(popupView.getContext().getApplicationContext(), "چاول", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("چاول");
            }
        });
        makai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_makai==0){
                    btn_tick_makai.setVisibility(View.VISIBLE);
                    list.add("مکئی");
                    flag_makai=1;
                }else{
                    btn_tick_makai.setVisibility(View.GONE);
                    list.remove("مکئی");
                    flag_makai=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("مکئی");
            }
        });
        ganna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_ganna==0){
                    btn_tick_ganna.setVisibility(View.VISIBLE);
                    list.add("گنا");
                    flag_ganna=1;
                }else{
                    btn_tick_ganna.setVisibility(View.GONE);
                    list.remove("گنا");
                    flag_ganna=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "گنا", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("گنا");
            }
        });
        daalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_daalen==0){
                    btn_tick_daalen.setVisibility(View.VISIBLE);
                    list.add("دالیں");
                    flag_daalen=1;
                }else{
                    btn_tick_daalen.setVisibility(View.GONE);
                    list.remove("دالیں");
                    flag_daalen=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "دالیں", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("دالیں");

            }
        });
//        poultry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (flag_poultry==0){
//                    btn_tick_poultry.setVisibility(View.VISIBLE);
//                    list.add("پولٹری");
//                    flag_poultry=1;
//                }else{
//                    btn_tick_poultry.setVisibility(View.GONE);
//                    list.remove("پولٹری");
//                    flag_poultry=0;
//                }
//                //Toast.makeText(popupView.getContext().getApplicationContext(), "پولٹری", Toast.LENGTH_LONG).show();
////                saving_fasal_for_question("پولٹری");
//
//            }
//        });
//        fish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (flag_fish==0){
//                    btn_tick_fish.setVisibility(View.VISIBLE);
//                    list.add("مچھلی");
//                    flag_fish=1;
//                }else{
//                    btn_tick_fish.setVisibility(View.GONE);
//                    list.remove("مچھلی");
//                    flag_fish=0;
//                }
//                //Toast.makeText(popupView.getContext().getApplicationContext(), "مچھلی", Toast.LENGTH_LONG).show();
////                saving_fasal_for_question("مچھلی");
//
//            }
//        });
        sabziyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_sabziyan==0){
                    btn_tick_sabziyan.setVisibility(View.VISIBLE);
                    list.add("سبزیاں");
                    flag_sabziyan=1;
                }else{
                    btn_tick_sabziyan.setVisibility(View.GONE);
                    list.remove("سبزیاں");
                    flag_sabziyan=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "مٹر", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("مٹر");

            }
        });
        sooraj_mukhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_sooraj_mukhi==0){
                    btn_tick_sooraj_mukhi.setVisibility(View.VISIBLE);
                    list.add("سورج مکھی");
                    flag_sooraj_mukhi=1;
                }else{
                    btn_tick_sooraj_mukhi.setVisibility(View.GONE);
                    list.remove("سورج مکھی");
                    flag_sooraj_mukhi=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "سورج مکھی", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("سورج مکھی");
            }
        });
        sarson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_sarson==0){
                    btn_tick_sarson.setVisibility(View.VISIBLE);
                    list.add("سرسوں");
                    flag_sarson=1;
                }else{
                    btn_tick_sarson.setVisibility(View.GONE);
                    list.remove("سرسوں");
                    flag_sarson=0;
                }
                //Toast.makeText(popupView.getContext().getApplicationContext(), "سرسوں", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question("سرسوں");

            }
        });
//        animals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (flag_animals==0){
//                    btn_tick_animals.setVisibility(View.VISIBLE);
//                    list.add("جانور");
//                    flag_animals=1;
//                }else{
//                    btn_tick_animals.setVisibility(View.GONE);
//                    list.remove("جانور");
//                    flag_animals=0;
//                }
//                //Toast.makeText(popupView.getContext().getApplicationContext(), "جانور", Toast.LENGTH_LONG).show();
////                saving_fasal_for_question("جانور");
//            }
//        });




    }
}