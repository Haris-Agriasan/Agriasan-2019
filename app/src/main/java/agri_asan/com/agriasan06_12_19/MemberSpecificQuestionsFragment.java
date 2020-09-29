package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static agri_asan.com.agriasan06_12_19.MainActivity.popupWindow;
import static agri_asan.com.agriasan06_12_19.MainActivity.progressDialog;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.media.MediaRecorder.AudioSource.MIC;

/**
 * Created by Belal on 1/23/2018.
 */

//////Toast.makeText(getApplicationContext(), "مہربانی  پہلے خود کو رجسٹر کروایں!", Toast.LENGTH_LONG).show();

public class MemberSpecificQuestionsFragment extends Fragment  {
    @Nullable


    MediaPlayer mediaPlayer = new MediaPlayer();


    PopupWindow popupWindow;

    DatabaseReference reference;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static MembAskedQuestAdapter adapter;

    String Total_Images;
    String Total_Answer_Images;
    String City = "";
    String Name = "";
    String Fasal = "";
    String Type_Question = "";
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


    View root;

    //////////////ADD QUESTION VARAIBLES POPUP
    String formattedDate;
    String formattedTime;
    String fasal="";

    //////////////ADD QUESTION VARAIBLES POPUP END



    //////////IMAGE AND CAMERA MULTIPLE

    int i=0;
    String path_to_image="";
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

    RelativeLayout layout_add_question;
    View view_space;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);


        view_space=root.findViewById(R.id.view_space);
        view_space.setVisibility(View.GONE);
        layout_add_question=root.findViewById(R.id.layout_add_question);
        layout_add_question.setVisibility(View.GONE);
        //layout_btn_add_question=root.findViewById(R.id.layout_btn_add_question);
        //layout_btn_add_question.setVisibility(View.INVISIBLE);
        type_checked=new ArrayList<String>();

        ////////IMAGES AND CAMERA MULTIPLE
        storageRef = FirebaseStorage.getInstance().getReference("Products_images");
        ///////IMAGES AND CAMERA MULTIPLE END

        mainActivity = (MainActivity) getActivity();

        question_no = 1;
        reference = FirebaseDatabase.getInstance().getReference();

//        MainActivity m = (MainActivity) getActivity();
        listView = (ListView) root.findViewById(R.id.list);
        dataModels = new ArrayList<>();

        fasal_type = ""+getArguments().getString("fasal");
        shoba_type = ""+getArguments().getString("shoba");
        Toast.makeText(getContext(), ""+fasal_type, Toast.LENGTH_LONG).show();

//        Toast.makeText(getActivity(), ""+mainActivity.Phone, Toast.LENGTH_LONG).show();

//.child("Phone").equalTo(mainActivity.Phone);
        final Query query = reference.child("Questions");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                question_no=1;
                dataModels=new ArrayList<>();
                adapter = new MembAskedQuestAdapter(dataModels, root.getContext());
                listView.setAdapter(adapter);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Member user = issue.getValue(Member.class);
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
                        if (!TextUtils.isEmpty(user.getTotal_Images())){
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
                        //Toast.makeText(getContext().getApplicationContext(), ""+images_list.size(), Toast.LENGTH_LONG).show();
                        if (phone.equals(mainActivity.Phone)) {
                            if (Fasal.equals(fasal_type) && Type_Question.equals(shoba_type)) {
                                dataModels.add(new DataModel(Question + "", City + "", Time + "",
                                        Fasal + "",
                                        ID + "", Name + "", Recording, Question_Image_0 + ""
                                        , images_list, images_list_answer, Member_ID + "",
                                        Date + "", question_no,
                                        Answer, Domain_City, Domain_Name, Answer_Date, Answer_Time,
                                        Domain_occupation, Recording_Answer));
                                question_no = question_no + 1;
                            }
                        }
                    }
                    Collections.reverse(dataModels);
                    adapter = new MembAskedQuestAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        listView.setAdapter(adapter);
        return root;
    }

    //////////////FUNCTIONS OF ADD QUESTION LAST PAGE

    @Override
    public void onResume() {
        super.onResume();
//        if (mainActivity.mTitle.getText().equals("جواب")){
//            mainActivity.mTitle.setText("آپکے پوچھے گئے سوالات");
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


}
