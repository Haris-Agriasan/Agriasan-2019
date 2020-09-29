package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedbottompicker.TedRxBottomPicker;

import static agri_asan.com.agriasan06_12_19.MainActivity.popupWindow;
import static agri_asan.com.agriasan06_12_19.MainActivity.progressDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.media.MediaRecorder.AudioSource.MIC;


public class HomeFragment extends Fragment {
    @Nullable


    TextView total_images;
    ImageView show_images_btn;

    ArrayList<String> for_fasal_check;

    RelativeLayout layout_show_images;

    MediaPlayer mediaPlayer = new MediaPlayer();

    DatabaseReference reference;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static MembAskedTypeOfQuestAdapter adapter;

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

    MainActivity mainActivity;

    int question_no;

    ImageView add_question_button_home;

    View root;

    //////////////ADD QUESTION VARAIBLES POPUP
    TextView question_type_textview;
    TextView fasal_textview;


    EditText question_detail_textview;
    String question_detail="";

    String formattedDate;
    String formattedTime;
    String question_type;
    String fasal;
    private Firebase mRootref;


    Button add_question_button;

    //Variable for Images

    private StorageReference mStorageRef;
    private StorageReference mStorageRef_audio;
    private DatabaseReference mDatabaseRef;

    private Uri mImageUri;

    private StorageTask mUploadTask;

    Upload upload;

    private ProgressBar mProgressBar;

    ///For Recording thing
    private Button record;
    TextView record_text;
    private MediaRecorder myAudioRecorder;

    String timeStamp;
    Timer t;
    TimerTask tt;
    Chronometer myChronometer;

    ImageView play;
    ImageView record_icon;
    ImageView stop_icon;

    private String outputFile;

    String recodring_link="";


    //////////IMAGE AND CAMERA MULTIPLE
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private ListView listView_images;
    private GalleryAdapter galleryAdapter;
    ImageButton btn_select_images;
    ImageButton btn_delete_images;
    Config config;

    ArrayList<Uri> image_uris;

    int i=0;
    String path_to_image="";
    StorageReference mountainsRef;
    StorageReference storageRef;
    UploadTask uploadTask;
    int j=0;
    String id;
    Firebase childRef;
    //////////IMAGE AND CAMERA MULTIPLE END

    /////////for new home page of types of questions asked

    ArrayList<String> type_checked;
    String fasal_type="";
    String shoba_type="";

    private Context mContext;

    String Type_Question = "";

    ArrayList<DataModel> dataModels_notify;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Resources resources;

    public void notify_question_details(){
        final Query query_for_notify = reference.child("Questions").child(mainActivity.notify_id);

        query_for_notify.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Member user = dataSnapshot.getValue(Member.class);

                    String read="";
                    if (!TextUtils.isEmpty(user.getRead())){
                        read=user.getRead();
                    }
                    if (read.equals("")){
//                    Toast.makeText(getContext(), "CHECKED", Toast.LENGTH_LONG).show();

                    Type_Question= ""+user.getFasal_Type_Shoba();
                    Recording_Answer = user.getRecording_Answer();
                    Domain_occupation = user.getDomain_Occupation();
                    Domain_City = user.getDomain_City();
                    Answer = user.getAnswer();
                    Answer_Date = user.getAnswer_Date();
                    Answer_Time = user.getAnswer_Time();
                    Answer_Image_0 = user.getAnswer_Image_0();
                    Answer_Image_1 = user.getAnswer_Image_1();
                    Answer_Image_2 = user.getAnswer_Image_2();
                    Answer_Image_3 = user.getAnswer_Image_3();
                    Answer_Image_4 = user.getAnswer_Image_4();
                    Answer_Image_5 = user.getAnswer_Image_5();
                    Domain_Name = user.getDomain_Name();

                    Question="";
                    Question = Question+user.getQuestion();
                    City = user.getMember_City();
                    Time = user.getTime();
                    Fasal = ""+user.getFasal();
                    ID = user.getID();
                    Name = user.getMember_Name();
                    Recording = user.getRecording();
                    Question_Image_0 = user.getImage_0();
                    Question_Image_1 = user.getImage_1();
                    Question_Image_2 = user.getImage_2();
                    Question_Image_3 = user.getImage_3();
                    Question_Image_4 = user.getImage_4();
                    Question_Image_5 = user.getImage_5();

                    Member_ID = user.getMember_ID();
                    Date = user.getDate();
                    Total_Images=user.getTotal_Images();
                    Total_Answer_Images=user.getTotal_Answer_Images();

                    int Question_Total_images=0;
                    if (!TextUtils.isEmpty(user.getTotal_Answer_Images())){
                        Question_Total_images = Integer.parseInt(user.getTotal_Images());
                    }
                    ArrayList<String> images_list=new ArrayList<>();
                    //images_list.add(Question_Image_0);
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
                    ArrayList<String> images_list_answer=new ArrayList<>();
                    if (Answer_Total_images==1){
                        images_list_answer.add(user.getAnswer_Image_0());
                    }
                    if (Answer_Total_images==2){
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
                    String phone="";
                    phone=""+user.getMember_Phone();

                    dataModels_notify = new ArrayList<>();

                    dataModels_notify.add(new DataModel(Question + "", City + "", Time + "",
                            Fasal + "",
                            ID + "", Name + "", Recording + "", Question_Image_0 + ""
                            , images_list, images_list_answer, Member_ID + "",
                            Date + "", 0,
                            Answer, Domain_City, Domain_Name, Answer_Date, Answer_Time,
                            Domain_occupation, Recording_Answer));

                    DataModel dataModel = dataModels_notify.get(0);

                    Fragment fragment = new MembOneAnsFrag();
                    FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

                    mainActivity.mTitle.setText("جواب");
                    Bundle bundle = new Bundle();

                    bundle.putStringArrayList("images_list_questions",dataModel.getQues_Images_List());
                    bundle.putStringArrayList("images_list_answers",dataModel.getAns_Images_List());

                    bundle.putString("answer_occupation", dataModel.getOccupation()+"");
                    bundle.putString("answer_city", dataModel.getAnswer_city()+"");
                    bundle.putString("answer_name", dataModel.getAnswer_name()+"");
                    bundle.putString("answer_time", dataModel.getAnswer_time()+"");
                    bundle.putString("answer_date", dataModel.getAnswer_date()+"");
                    bundle.putString("answer_recording", dataModel.getRecording_answer()+"");
                    bundle.putString("answer", dataModel.getAnswer()+"");

                    bundle.putString("question_no", dataModel.getQuestion_no()+"");
                    bundle.putString("extra_thing", dataModel.getExtra_thing()+"");

                    String Date_formatted=dataModel.getQuestion_date().substring(0,6)+dataModel.getQuestion_date().substring(8);
                    String Time_formatted = dataModel.getQuestion_time().substring(0,5);

                    bundle.putString("question", dataModel.getQuestion());
                    bundle.putString("question_date_time", Date_formatted+" "+Time_formatted);
                    bundle.putString("member_name", dataModel.getQuestion_name());
                    bundle.putString("member_city", dataModel.getQuestion_city());
                    bundle.putString("question_id", dataModel.getID());
                    bundle.putString("recording", dataModel.getRecording());
                    bundle.putString("fasal", dataModel.getFasal());

                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);


                    mainActivity.notify_id=null;

                    editor = pref.edit();
                    editor.remove("notify_id");
                    editor.apply();

                    try{
                        fragmentTransaction.commit();
                    }catch (Exception e){
                    }

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        popup_add_q_showing=false;

        try {
            popupWindow.dismiss();
        }catch (Exception e){

        }


        resources= Objects.requireNonNull(getContext()).getResources();

        mainActivity = (MainActivity) getActivity();

        for_fasal_check=new ArrayList<>();
//        for_fasal_check.add("جانور");
//        for_fasal_check.add("مچھلی");
//        for_fasal_check.add("پولٹری");

        pref = mainActivity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        reference = FirebaseDatabase.getInstance().getReference();

        if (!TextUtils.isEmpty(mainActivity.notify_id)){
            if (!mainActivity.notify_id.equals("")){
                notify_question_details();
            }
        }

        mContext = getContext();

        type_checked=new ArrayList<String>();

        ////////IMAGES AND CAMERA MULTIPLE
        storageRef = FirebaseStorage.getInstance().getReference("Products_images");
        ///////IMAGES AND CAMERA MULTIPLE END


        question_no = 1;

//        MainActivity m = (MainActivity) getActivity();


        listView = (ListView) root.findViewById(R.id.list);
        dataModels = new ArrayList<>();
        final Query query = reference.child("Questions");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModels=new ArrayList<>();
                type_checked=new ArrayList<>();
                adapter = new MembAskedTypeOfQuestAdapter(dataModels, root.getContext());
                listView.setAdapter(adapter);
                if (dataSnapshot.exists()) {
                    //question_no=dataSnapshot.getChildrenCount();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        Toast.makeText(getActivity(), "check", Toast.LENGTH_LONG).show();
                        Member question = issue.getValue(Member.class);
                        String phone_db="";
                        if (!TextUtils.isEmpty(question.getMember_Phone()))
                        {
                            phone_db=question.getMember_Phone();
                        }
                        if (phone_db.equals(mainActivity.Phone)){
                        shoba_type=question.getFasal_Type_Shoba();
                        fasal_type=question.getFasal();
                            if (!type_checked.contains(""+fasal_type+shoba_type)){
                            type_checked.add(""+fasal_type+shoba_type);
                            dataModels.add(new DataModel(fasal_type,shoba_type));
                        }
                        }
                    }
                    Collections.reverse(dataModels);
                    adapter = new MembAskedTypeOfQuestAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        add_question_button_home = root.findViewById(R.id.add_question_button);

        add_question_button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_q_showing=false;
                popup_window_choose_fasal(view);

            }
        });

        try {
            popupWindow.dismiss();
        }catch (Exception e){

        }
        return root;
    }

    public void popup_window_choose_fasal(View v) {
        ImageView kapas,gandum, chawal,makai,daalen,poultry,ganna,sabziyan,sarson,jaanwor,sooraj_mukhi,fish;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_choose_fasal, null);


        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
//        PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, width, height);


        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                Toast.makeText(getContext(), "Check 1", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }


        kapas = popupView.findViewById(R.id.cotton);
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
        jaanwor = popupView.findViewById(R.id.animals);

        kapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "کپاس", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.kappas));
            }
        });
        gandum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.gandum));
            }
        });
        chawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "چاول", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.chawal));

            }
        });
        makai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.makai));

            }
        });
        ganna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گنا", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.ganna));
            }
        });
        daalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "دالیں", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.daalen));

            }
        });
//        poultry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(root.getContext().getApplicationContext(), "پولٹری", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question(resources.getString(R.string.poultry));
//
//            }
//        });
//        fish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(root.getContext().getApplicationContext(), "مچھلی", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question(resources.getString(R.string.fish));
//
//            }
//        });
        sabziyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "سبزیاں", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.sabziyan));

            }
        });
        sooraj_mukhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "سورج مکھی", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.sooraj_mukhi));
            }
        });
        sarson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "سرسوں", Toast.LENGTH_LONG).show();
                saving_fasal_for_question(resources.getString(R.string.sarson));

            }
        });
//        jaanwor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(root.getContext().getApplicationContext(), "جانور", Toast.LENGTH_LONG).show();
//                saving_fasal_for_question(resources.getString(R.string.janwar));
//
//            }
//        });
    }

    public void saving_fasal_for_question(String fasal) {
        Toast.makeText(getContext().getApplicationContext(), "" + fasal, Toast.LENGTH_LONG).show();
        this.fasal=fasal;

//        if (for_fasal_check != null && for_fasal_check.contains(fasal)) {
//            saving_question_type_for_question("");
//        }else{
//        Toast.makeText(getContext(), "Check 2", Toast.LENGTH_SHORT).show();



        popupWindow.dismiss();

        if (isPopup_add_q_showing){
            fasal_textview.setText(""+fasal);

        }else{
            popup_window_choose_question_type(root);
        }
//        }

//        Bundle bundle = new Bundle();
//        bundle.putString("fasal", fasal+"");
//        fragment.setArguments(bundle);
    }

    public void popup_window_choose_question_type(View v) {
        ImageView seed, medicine, khaad, matti, mehkama_tausee, mehkama_inhaar, herb, keeray, tunnel;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_choose_question_type, null);

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



        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        seed = popupView.findViewById(R.id.seed);
        medicine = popupView.findViewById(R.id.medicine);
        khaad = popupView.findViewById(R.id.khaad);
        mehkama_tausee = popupView.findViewById(R.id.mehkama_tausee);
        mehkama_inhaar = popupView.findViewById(R.id.mehkama_inhaar);
        matti = popupView.findViewById(R.id.matti);
        keeray = popupView.findViewById(R.id.keeray);
        herb = popupView.findViewById(R.id.herb);
        tunnel = popupView.findViewById(R.id.tunnel_farming);

        keeray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.keeray));
            }
        });
        herb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.herb));
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "کپاس", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.beej));
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.medicines));
            }
        });

        khaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "چاول", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.khaad));

            }
        });
        mehkama_tausee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.mehkama_tousee));

            }
        });
        mehkama_inhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گنا", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.mehkama_inhaar));
            }
        });
        matti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(root.getContext().getApplicationContext(), ""+mattie, Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.matti));

            }
        });
        tunnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "ٹنل فارمنگ", Toast.LENGTH_LONG).show();
                saving_question_type_for_question(resources.getString(R.string.tunnel_farming));

            }
        });
    }

    private void saving_question_type_for_question(String q_type) {
        Toast.makeText(getContext().getApplicationContext(), "" + q_type, Toast.LENGTH_LONG).show();
        question_type=q_type;
//        Toast.makeText(getContext(), "Check 3", Toast.LENGTH_SHORT).show();


//        popup_window_add_question(root);

        try {
            popupWindow.dismiss();
        }catch (Exception e){

        }
        if (isPopup_add_q_showing){
            question_type_textview.setText(""+question_type);

        }else{



            popup_add_q_showing=false;

            recodring_link="";

            image_uris=new ArrayList<>();

            question_detail="";
            popup_window_add_question(root);
        }
    }

    boolean popup_add_q_showing;
//    String newToken;

    boolean isPopup_add_q_showing=false;
    public void popup_window_add_question(View v){

        isPopup_add_q_showing=true;
        LinearLayout layout_fasal_change;
        LinearLayout layout_question_type_change;

        popup_add_q_showing=true;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_add_question, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        layout_show_images=popupView.findViewById(R.id.layout_show_images);
        ////////IMAGES AND CAMERA MULTIPLE


//        MediaPlayer mediaPlayer = new MediaPlayer();

        show_images_btn=popupView.findViewById(R.id.show_images_btn);
        total_images=popupView.findViewById(R.id.total_images);
//        listView_images = popupView.findViewById(R.id.gv);
        btn_select_images=popupView.findViewById(R.id.select_images_btn);
        btn_delete_images=popupView.findViewById(R.id.btn_delete_images);
        if (image_uris.isEmpty()){
            btn_delete_images.setEnabled(false);
            btn_delete_images.setAlpha(0.2f);
            layout_show_images.setVisibility(View.INVISIBLE);
        }
        layout_fasal_change=popupView.findViewById(R.id.layout_fasal_change);

        layout_question_type_change=popupView.findViewById(R.id.layout_question_type_change);

        layout_fasal_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_choose_fasal(v);
//                popup_window_show_images(v);
            }
        });

        layout_question_type_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_choose_question_type(v);
            }
        });

        btn_select_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
        btn_delete_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_uris=new ArrayList<Uri>();
                btn_delete_images.setEnabled(false);
                btn_delete_images.setAlpha(0.2f);
                layout_show_images.setVisibility(View.INVISIBLE);
            }
        });
        ///////IMAGES AND CAMERA MULTIPLE END

        show_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_show_images(v);
            }
        });
        config = new Config();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);
        //Toast.makeText(getActivity().getApplicationContext(), ""+m.Name, Toast.LENGTH_LONG).show();
        //question_type = getArguments().getString("question_type");
        //fasal = getArguments().getString("fasal");
        add_question_button = popupView.findViewById(R.id.add_question_final_button);
        question_type_textview = popupView.findViewById(R.id.textview_fasal_type);
        fasal_textview = popupView.findViewById(R.id.textview_fasal_name);

//        mImageView1=popupView.findViewById(R.id.image_1);
//        mImageView2=popupView.findViewById(R.id.image_2);
//        mImageView3=popupView.findViewById(R.id.image_3);

        mStorageRef = FirebaseStorage.getInstance().getReference("Questions_images");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mProgressBar = popupView.findViewById(R.id.progress_bar);

        mProgressBar.setVisibility(View.INVISIBLE);


        //recording thing
        record=(Button) popupView.findViewById(R.id.recording_start);
        record_text= popupView.findViewById(R.id.record_text);

        myChronometer = (Chronometer) popupView.findViewById(R.id.chronometer_time);

        record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");

        play= (ImageView) popupView.findViewById(R.id.play_recording);
        record_icon= (ImageView) popupView.findViewById(R.id.record_icon);
        stop_icon= (ImageView) popupView.findViewById(R.id.stop_icon);

        if (recodring_link.isEmpty()){
//            Toast.makeText(getContext(), "Check 1", Toast.LENGTH_SHORT).show();
            play.setVisibility(View.INVISIBLE);
        }else{
//            Toast.makeText(getContext(), "Check 2", Toast.LENGTH_SHORT).show();
            play.setVisibility(View.VISIBLE);
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_audio();
            }
        });
//        record_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                record_audio();
//            }
//        });

        stop_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
                record.setEnabled(true);

                if(tt!=null)
                {
                    tt.cancel();
                    t.cancel();
                }
                myAudioRecorder.stop();
                myChronometer.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                record_icon.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                stop_icon.setVisibility(View.GONE);

                Toast.makeText(getContext(), "آپکا سوال رکارڈ کر لیا گیا ہے", Toast.LENGTH_LONG).show();

                UploadingReady_audio();

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    mediaPlayer=new MediaPlayer();
                    record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
                    play.setImageResource(R.drawable.play);
                    record.setEnabled(true);

                }else{
                    try {
                        try {
                            myChronometer.setText("");
                        } catch (Exception e) {
                            // make something
                        }
                        play.setImageResource(R.drawable.stop);
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        record_text.setText("آپکا مسلہ");

                        record.setEnabled(false);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
//                                play.setVisibility(View.VISIBLE);
                                mediaPlayer.stop();
                                mediaPlayer=new MediaPlayer();
                                record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
                                record.setEnabled(true);
//                                mediaPlayer.release();
                                play.setImageResource(R.drawable.play);
                            }
                        });
//                    Toast.makeText(getContext(), "Playing Recording", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // make something
                    }
                }


            }
        });


        ///to get recording permission and prepare recording
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        } else {
            if (recodring_link.isEmpty()){
//            Toast.makeText(getContext(), "Check 1", Toast.LENGTH_SHORT).show();
                play.setVisibility(View.INVISIBLE);
                recordAudio();
            }else{
//            Toast.makeText(getContext(), "Check 2", Toast.LENGTH_SHORT).show();
                play.setVisibility(View.VISIBLE);
            }
        }

        /////end recording

        question_type_textview.setText(""+question_type);
        fasal_textview.setText(""+fasal);

        question_detail_textview = popupView.findViewById(R.id.question_detail);
//        question_detail=question_detail_textview.getText().toString();


        question_detail_textview.setText(question_detail);


        add_question_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    // make something
                }
                question_detail_textview = popupView.findViewById(R.id.question_detail);
                question_detail=question_detail_textview.getText().toString();
                SaveQuestionData();

            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopup_add_q_showing=false;
                if (mediaPlayer!=null)
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
//                Toast.makeText(getContext(), "Check 4", Toast.LENGTH_SHORT).show();

//                popup_add_q_showing=false;

                popupWindow.dismiss();

            }
        });

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup_add_q_showing=false;
//                popupWindow.dismiss();
                return true;
            }
        });

//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//
//                    Toast.makeText(getContext(), "Check 5", Toast.LENGTH_SHORT).show();
//
//                    popupWindow.dismiss();
//
//                    return true;
//
//                }
//
//                return false;
//
//            }
//        });


    }


    public void popup_window_show_images(View v) {

        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_show_images, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow popupWindow_images;
        popupWindow_images = new PopupWindow(popupView, width, height);
        popupWindow_images.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView_images = popupView.findViewById(R.id.gv);

        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);
        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);
        popupWindow_images.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow_images.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow_images.showAtLocation(popupView, Gravity.CENTER, 0, 0);



        galleryAdapter = new GalleryAdapter(mainActivity.getApplicationContext(),image_uris, root);
        listView_images.setAdapter(galleryAdapter);

        config = new Config();

        popupWindow_images.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss()
            {
                if (image_uris.size()!=0){
                if (layout_show_images != null) {
                    Glide.with(Objects.requireNonNull(getActivity()))
                            .load(image_uris.get(0))
                            .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                            .into(show_images_btn);
                    int total_img=0;
                    total_img=image_uris.size()-1;

                    if (image_uris.size()>=2){
                        if (total_images != null) {
                            total_images.setText("+"+total_img);
                        }
                    }else{
                        if (total_images != null) {
                            total_images.setText("");
                        }
                    }

                    btn_delete_images.setEnabled(true);
                    btn_delete_images.setAlpha(1f);
                    layout_show_images.setVisibility(View.VISIBLE);
                }
                }
                else{
//                    Toast.makeText(getContext(), "CHeck111" , Toast.LENGTH_LONG).show();

                    btn_delete_images.setEnabled(false);
                    btn_delete_images.setAlpha(0.2f);
                    layout_show_images.setVisibility(View.INVISIBLE);

                    total_images.setText("");
                    show_images_btn.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    //////////////FUNCTIONS OF ADD QUESTION LAST PAGE
    //record audio function
    private void recordAudio() {
        stop_icon.setVisibility(View.GONE);
        if (recodring_link.isEmpty()){
//            Toast.makeText(getContext(), "Check 1", Toast.LENGTH_SHORT).show();
            play.setVisibility(View.INVISIBLE);
        }else{
//            Toast.makeText(getContext(), "Check 2", Toast.LENGTH_SHORT).show();
            play.setVisibility(View.VISIBLE);
        }
//        play.setVisibility(View.INVISIBLE);
        record_icon.setVisibility(View.VISIBLE);

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+timeStamp+"_recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        Uri record_uri = Uri.parse(""+outputFile);
        mImageUri=record_uri;
    }

    ///uploading Audio to Firebase
    private void UploadingReady_audio() {
//        mProgressBar.setVisibility(View.VISIBLE);
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(getContext(), "انتظار کریں", Toast.LENGTH_SHORT).show();
        } else {
            try {
                progressDialog.show();
            } catch (Exception e) {
                // make something
            }
            uploadFile_audio();
        }
    }

    private void uploadFile_audio() {
        mStorageRef_audio = FirebaseStorage.getInstance().getReference("Questions_recordings");
        Uri file = Uri.fromFile(new File(outputFile));
        //Toast.makeText(getContext(), ""+file, Toast.LENGTH_SHORT).show();
        mImageUri=file;

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef_audio.child(timeStamp+"_recording.3gp");
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progressDialog.hide();
                            } catch (Exception e) {
                                // make something
                            }
//                            mProgressBar.setProgress(0);
                        }
                    }, 500);

                    Toast.makeText(getContext(), "آپکے انتظار کا شکریہ", Toast.LENGTH_LONG).show();
//                    mProgressBar.setVisibility(View.INVISIBLE);
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recodring_link = uri.toString();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                    mProgressBar.setProgress((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext().getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    //uploading audio to firebase END

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {
            image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//              t=intent.getParcelableExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//            galleryAdapter = new GalleryAdapter(getContext().getApplicationContext(),image_uris);
//            listView_images.setAdapter(galleryAdapter);
            btn_delete_images.setEnabled(true);
            btn_delete_images.setAlpha(1f);
            layout_show_images.setVisibility(View.VISIBLE);
        }
    }
    InputStream input=null;
    ExifInterface exif=null;
    private void SaveQuestionData() {
        images_to_firebase_db=new ArrayList<>();
        mRootref.setAndroidContext(getActivity());
        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Questions");
        if (!question_detail.isEmpty())
        {
            //it will create a unique id and we will use it as the Primary Key for our Member
            id = mRootref.push().getKey();

            childRef = mRootref.child(id).child("Member_Name");
            childRef.setValue(mainActivity.Name + "");
            try {
                progressDialog.show();
            }catch (Exception ex)
            {
            }
            i=0;
            j=0;
            if (image_uris.size()==0){
                upload_data_firebase();
            }
            for (i = 0; i < image_uris.size(); i++) {
                path_to_image=image_uris.get(i).toString();
                temp_uri_image=image_uris.get(i);
                Bitmap bitmap=null;
                int orientation=0;
                try {
                    input = mContext.getContentResolver().openInputStream(temp_uri_image);
                    exif = null;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (input != null) { exif = new ExifInterface(input); }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    orientation = 0;
                    if (exif != null) {
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                    }

                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), temp_uri_image);

                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                    if (bmRotated != null) { upload_firebase(bmRotated); }
                    else{ upload_firebase(bitmap); }
                } catch (Exception e) {
                    try {
                        progressDialog.hide();
                    }catch (Exception ignored)
                    {
                    }
                    Toast.makeText(getContext(), "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                }

            }
        }else if(!image_uris.isEmpty() && !question_detail.isEmpty()){
            id = mRootref.push().getKey();
            childRef = mRootref.child(id).child("Member_Name");
            childRef.setValue(mainActivity.Name + "");
            try {
                progressDialog.show();
            }catch (Exception ex)
            {
            }
            i=0;
            j=0;
            if (image_uris.size()==0){
                upload_data_firebase();
            }
            for (i = 0; i < image_uris.size(); i++) {
                path_to_image=image_uris.get(i).toString();
                temp_uri_image=image_uris.get(i);
                try {
                    input = mContext.getContentResolver().openInputStream(temp_uri_image);
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

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), temp_uri_image);
                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                    if (bmRotated != null) { upload_firebase(bmRotated); }
                    else{ upload_firebase(bitmap); }
                } catch (Exception e) {
                    try {
                        progressDialog.hide();
                    }catch (Exception ignored)
                    {
                    }
                    Toast.makeText(getContext(), "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                }
//                upload_firebase();
            }
        }else if(!image_uris.isEmpty() && !recodring_link.isEmpty()){
            //it will create a unique id and we will use it as the Primary Key for our Member
            id = mRootref.push().getKey();

            childRef = mRootref.child(id).child("Member_Name");
            childRef.setValue(mainActivity.Name + "");
            try {
                progressDialog.show();
            }catch (Exception ex)
            {
            }
            i=0;
            j=0;
            if (image_uris.size()==0){
                upload_data_firebase();
            }
            for (i = 0; i < image_uris.size(); i++) {
                path_to_image=image_uris.get(i).toString();
                temp_uri_image=image_uris.get(i);
                try {
                    input = mContext.getContentResolver().openInputStream(temp_uri_image);
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

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), temp_uri_image);
                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                    if (bmRotated != null) { upload_firebase(bmRotated); }
                    else{ upload_firebase(bitmap); }
                } catch (Exception e) {
                    try {
                        progressDialog.hide();
                    }catch (Exception ignored)
                    {
                    }
                    Toast.makeText(getContext(), "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                }
//                upload_firebase();
            }
        }else if(!recodring_link.isEmpty()){
            //it will create a unique id and we will use it as the Primary Key for our Member
            id = mRootref.push().getKey();

            childRef = mRootref.child(id).child("Member_Name");
            childRef.setValue(mainActivity.Name + "");
            try {
                progressDialog.show();
            }catch (Exception ex)
            {
            }
            i=0;
            j=0;
            if (image_uris.size()==0){
                upload_data_firebase();
            }
            for (i = 0; i < image_uris.size(); i++) {
                path_to_image=image_uris.get(i).toString();
                temp_uri_image=image_uris.get(i);
                try {
                    input = mContext.getContentResolver().openInputStream(temp_uri_image);
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

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), temp_uri_image);
                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                    if (bmRotated != null) { upload_firebase(bmRotated); }
                    else{ upload_firebase(bitmap); }
                } catch (Exception e) {
                    try {
                        progressDialog.hide();
                    }catch (Exception ignored)
                    {
                    }
                    Toast.makeText(getContext(), "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                }
//                upload_firebase();
            }
        }else{
            try {
                progressDialog.hide();
            } catch (Exception e) {
                // make something
            }
            Toast.makeText(getActivity().getApplicationContext(), "پہلے سوال لکھیں یا رکارڈ کریں!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (mainActivity.mTitle.getText().equals("جواب")){
            mainActivity.mTitle.setText("آپکے پوچھے گئے سوالات");
//        }
    }
    @Override
    public void onPause() {
        if (mediaPlayer!=null)
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        super.onPause();
    }

    ///////////////FUNCTIONS OF ADD QUESTION LAST PAGE END
    ///////////////MULTIPLE IMAGES AND CAMERA
    private void getImages() {

        TedBottomPicker.with(getActivity())
                .setPeekHeight(1600)
                .showTitle(false)
                .setSpacing(2)
                .setSelectMaxCount(6)
                .setSelectMaxCountErrorText(R.string.max_images_6)
                .setCompleteButtonText(R.string.shamil_karen)
                .setEmptySelectionText(R.string.no_image)
                .setSelectedUriList(image_uris)
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        image_uris = (ArrayList<Uri>) uriList;
//                        Toast.makeText(getContext(), "" + image_uris.get(0), Toast.LENGTH_LONG).show();
                        if (!uriList.isEmpty()){
                            if (layout_show_images != null) {
                                Glide.with(Objects.requireNonNull(getActivity()))
                                        .load(uriList.get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                                        .into(show_images_btn);
                                int total_img=0;
                                total_img=uriList.size()-1;

                                if (uriList.size()>=2){
                                    if (total_images != null) {
                                        total_images.setText("+"+total_img);
                                    }
                                }else{
                                    if (total_images != null) {
                                        total_images.setText("");
                                    }
                                }

                                btn_delete_images.setEnabled(true);
                                btn_delete_images.setAlpha(1f);
                                layout_show_images.setVisibility(View.VISIBLE);
                                show_images_btn.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                });
    }


    private String upload_URL = "";
    JSONObject jsonObject;
    RequestQueue rQueue;
    String cmp_url="";
    String imgname="";


    ArrayList<String> images_to_firebase_db;
    Uri temp_uri_image=null;
    public void upload_firebase(Bitmap bitmap) {

        upload_URL = "https://www.novaeno.com/agriasan/uploadQuestionImage.php";

        progressDialog.show();

        //////////////NEW CODE
        cmp_url="";
        imgname="";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("name", imgname);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);

            images_to_firebase_db.add(imgname);

            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            try {
                progressDialog.hide();
            }catch (Exception ignored) { }
            SaveQuestionData();
            Log.e("JSONObject Here", e.toString());
//            Toast.makeText(Objects.requireNonNull(getContext()), "", Toast.LENGTH_LONG).show();

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.e("Image Uploadeddddddddd", jsonObject.toString());

                        rQueue.getCache().clear();
                        j++;

                        if(j==image_uris.size())
                        {
                            upload_data_firebase();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    progressDialog.hide();
                }catch (Exception ignored)
                {
                }
                SaveQuestionData();
                Log.e("Errrrorrrr", volleyError.toString());

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 900000,
                10000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjectRequest);

        /////////NEW CODE END

    }




    /////MULTIPLE IMAGES AND CAMERA END


    public void upload_data_firebase(){
        childRef = mRootref.child(id).child("Total_Images");
        childRef.setValue("" + image_uris.size());

        for (int i=0;i<images_to_firebase_db.size();i++){

            cmp_url="https://novaeno.com/agriasan/agriasan_images/Questions/";

            cmp_url=cmp_url+images_to_firebase_db.get(i)+".JPG";

            childRef = mRootref.child(id).child("Image_"+i);
            childRef.setValue(""+cmp_url);
        }

        childRef = mRootref.child(id).child("Member_City");
        childRef.setValue(mainActivity.City + "");

        childRef = mRootref.child(id).child("Member_Phone");
        childRef.setValue(mainActivity.Phone + "");

        childRef = mRootref.child(id).child("Member_ID_Card");
        childRef.setValue(mainActivity.ID_Card + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        childRef = mRootref.child(id).child("Member_ID");
        childRef.setValue(mainActivity.ID + "");

        childRef = mRootref.child(id).child("Question");
        childRef.setValue(question_detail + "");

        childRef = mRootref.child(id).child("Fasal");
        childRef.setValue(fasal + "");

//        childRef = mRootref.child(id).child("Token");
//        childRef.setValue(mainActivity.newToken + "");

        childRef = mRootref.child(id).child("Fasal_Type_Shoba");
        childRef.setValue(question_type + "");

        childRef = mRootref.child(id).child("Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("User");
        childRef.setValue("member");

        childRef = mRootref.child(id).child("Recording");
        childRef.setValue(""+recodring_link);

        childRef = mRootref.child(id).child("Longi");
        childRef.setValue(""+mainActivity.Location_Long);

        childRef = mRootref.child(id).child("Lati");
        childRef.setValue(""+mainActivity.Location_Lat);



//        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "آپکا سوال درج کر لیا گیا ہے شکریہ!", Toast.LENGTH_LONG).show();

        try {
            Thread.sleep(1500); //1000 milliseconds is one second.
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        popup_add_q_showing=false;
        popupWindow.dismiss();
        if (mediaPlayer!=null)
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

        try {
            progressDialog.hide();
        }catch (Exception ignored)
        {
        }
    }
    ////RECORDING AUDIO
    public void record_audio(){
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        }
//        record.setText("");
        record_text.setText("");
        if(tt!=null)
        {
            tt.cancel();
            t.cancel();
        }
        recordAudio();
        try {
            myChronometer.start();
            myChronometer.setBase(SystemClock.elapsedRealtime());
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
        record.setEnabled(false);
        record_icon.setVisibility(View.GONE);
        play.setVisibility(View.INVISIBLE);
        stop_icon.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "ریکارڈنگ شروع ہو گئی ہے", Toast.LENGTH_LONG).show();

        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (myAudioRecorder!=null){
//                            record.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
                            record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myChronometer.stop();
                            myAudioRecorder = null;
                            record.setEnabled(true);

                            record_icon.setVisibility(View.VISIBLE);
                            stop_icon.setVisibility(View.GONE);
                            play.setImageResource(R.drawable.play);
                            play.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(), "60 سیکنڈ پورے ہو گیے ہیں", Toast.LENGTH_LONG).show();
                            UploadingReady_audio();
                        }else{
                            Toast.makeText(getContext(), "Nothing to stop", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };  //5000 means 5 sec  //1000 means 1 sec
        t.schedule(tt,60000);
    }

    @Override
    public void onStart() {


//        try {
//            popupWindow.dismiss();
//        }catch (Exception e){
//        }
//        if (popup_add_q_showing){
//            popup_window_add_question(root);
//        }
        super.onStart();
    }

    @Override
    public void onStop() {
//        if (popup_add_q_showing){
//            question_detail=question_detail_textview.getText().toString();
//        }
        super.onStop();
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




//////old code of uploading images to firebase

//        InputStream stream = null;
//        try {
//            stream = new FileInputStream(new File("" + path_to_image));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        uploadTask = mountainsRef.putStream(stream);
//        uploadTask = mountainsRef.putFile(temp_uri_image);
////        uploadTask = mountainsRef.put;
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Toast.makeText(getContext(), "" + exception, Toast.LENGTH_LONG).show();
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                if (taskSnapshot.getMetadata() != null) {
//                    if (taskSnapshot.getMetadata().getReference() != null) {
//                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                String imageUrl = uri.toString();
//                                childRef = mRootref.child(id).child("Image_"+j);
//                                childRef.setValue(""+imageUrl);
////                                    progressDialog.dismiss();
//                                j++;
//
//                                if(j==image_uris.size())
//                                {
//
//                                    upload_data_firebase();
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });

//////old code of uploading images to firebase end
