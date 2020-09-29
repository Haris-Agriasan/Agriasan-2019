package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.TestLooperManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import android.widget.SeekBar;
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
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.media.MediaRecorder.AudioSource.MIC;
import static android.os.Environment.getExternalStoragePublicDirectory;

import static agri_asan.com.agriasan06_12_19.DomainMainActivity.progressDialog;

public class DomainGiveAnswerFragment extends Fragment implements View.OnClickListener,
        View.OnTouchListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {

    TextView total_images;

    ImagesAdapter imagesadapter;

    private String upload_URL = "";
    JSONObject jsonObject;
    RequestQueue rQueue;
    String cmp_url="";
    String imgname="";


    ArrayList<String> images_to_firebase_db;


    ImageView show_images_btn;
    RelativeLayout layout_show_images;
    ////FOR POPUP WINDOW
    ImageView image_popup;
    String url="";
    ////FOR POPUP WINDOW END

    String answer_name,answer_city,answer,
            occupation,domain_id,domain_phone,domain_id_card;

    Button submit_answer;

    EditText answer_submit;

    String question,question_date_time
    ,image_1,image_2,image_3,member_name
    ,member_city,question_id,member_id,recording,fasal;

    TextView text_view_question,text_view_question_time,text_view_fasal
    ,text_view_question_name_city;

    String recording_url;

    DomainMainActivity domainMainActivity;

    TextView domain_name ,domain_occupation;

    String domain_name_str;

    ///FOR MEDIA PLAYER
    ImageView play ,pause;
    SeekBar seekBar;
    Player player;
    int flag;
    private MediaPlayer mediaPlayer;
    private int lengthOfAudio;
    private final Handler handler = new Handler();
    private final Runnable r = new Runnable()
    {
        @Override
        public void run() {
            updateSeekProgress();
        }
    };
    @Override
    public void onClick(View v) {
    }
    ////END MEDIA PLAYER



    ///FIEBASE THINGS
    DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private StorageReference mStorageRef_audio;
    private StorageTask mUploadTask;
    private DatabaseReference mDatabaseRef;
    ///END FIREBASE THINGS

    View root;

    ///For Recording thing
    private Uri mImageUri;
    private Button record;
    private MediaRecorder myAudioRecorder;

    String timeStamp;
    Timer t;
    TimerTask tt;
    Chronometer myChronometer;

    ImageView play_recording;
    ImageView record_icon;
    ImageView stop_icon;

    private String outputFile;

    String recodring_link="";
    ///FOR RECORDIG END
    DomainMainActivity domainMainActivity_2;

    ////PICTURE ADDING


    String formattedDate;
    String formattedTime;

    private Firebase mRootref;




    Upload upload;

    TextView player_time;

    ArrayList<String> Product_images_link;


    String out;

    //////////IMAGE AND CAMERA MULTIPLE QUESTION
    private GridView listView;
    private GalleryAdapterImageString galleryAdapterImageString;
    //////////IMAGE AND CAMERA MULTIPLE QUESTION END

    //////////IMAGE AND CAMERA MULTIPLE ANSWER
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private GridView gvGallery;
    private ListView listView_images;
    private GalleryAdapter galleryAdapter;
    ImageButton btn_select_images;
    ImageButton btn_delete_images;
    Adapter adapter_images;
    ArrayList<Uri> image_uris;
    Config config;
    int i;
    int j=0;
    String path_to_image;

    StorageReference storageRef;
    StorageReference mountainsRef;
    UploadTask uploadTask;
    Firebase childRef;
    //////////IMAGE AND CAMERA MULTIPLE ANSWER END

    LinearLayout layout_question_recording;

    TextView record_text;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_domain_give_answer, container, false);

        layout_show_images=root.findViewById(R.id.layout_show_images);
        layout_show_images.setVisibility(View.INVISIBLE);
        show_images_btn=root.findViewById(R.id.show_images_btn);

        layout_question_recording=root.findViewById(R.id.layout_question_recording);


        submit_answer = root.findViewById(R.id.enter_for_answer_button_ans_page);

        ///IMAGE THINGS
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        player_time=root.findViewById(R.id.player_time_ans_page);

//        question_image_1=root.findViewById(R.id.question_domain_image_1_ans_page);
//        question_image_2=root.findViewById(R.id.question_domain_image_2_ans_page);
//        question_image_3=root.findViewById(R.id.question_domain_image_3_ans_page);

        Product_images_link=getArguments().getStringArrayList("images_list");

        listView = root.findViewById(R.id.gv);

        imagesadapter=new ImagesAdapter();
        imagesadapter.execute();
//        galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Product_images_link);
//        listView.setAdapter(galleryAdapterImageString);

        ////////CAMERA AND GALLERY MULTIPLE FOR ANSWER
        image_uris=new ArrayList<>();
        config = new Config();
//        listView_images = root.findViewById(R.id.answer_gv);

        show_images_btn=root.findViewById(R.id.show_images_btn);
        total_images=root.findViewById(R.id.total_images);
        btn_select_images=root.findViewById(R.id.select_images_btn);
        btn_delete_images=root.findViewById(R.id.btn_delete_images);
        btn_delete_images.setEnabled(false);
        btn_delete_images.setAlpha(0.2f);
        btn_select_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
        btn_delete_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listView_images.setAdapter(null);
                image_uris=new ArrayList<>();
                btn_delete_images.setEnabled(false);
                btn_delete_images.setAlpha(0.2f);
                layout_show_images.setVisibility(View.INVISIBLE);

            }
        });
        show_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_show_images(v);
            }
        });
        storageRef = FirebaseStorage.getInstance().getReference("Questions_images");

        ////////CAMERA AND GALLERY MULTIPLE FOR ANSWER END

        ///NOTE DELETE COMMENT FOR IMAGES OF QUESTION

//        Picasso.get().load(question_image_1_str).placeholder( R.drawable.progress_animation ).into(question_image_1);

        ///NOTE DELETE COMMENT FOR IMAGES OF QUESTION END

        mStorageRef = FirebaseStorage.getInstance().getReference("Questions_images");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        ////IMAGE THINGS END
        domainMainActivity_2=(DomainMainActivity) getActivity();
        /////RECORDING THING
        record=(Button) root.findViewById(R.id.recording_start_domain_ans);
        record_text= root.findViewById(R.id.record_text);

        myChronometer = (Chronometer) root.findViewById(R.id.chronometer_time_domain_ans);

//        record.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
        record_text.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");


        play_recording = (ImageView) root.findViewById(R.id.play_recording_domain_ans);
        record_icon= (ImageView) root.findViewById(R.id.record_icon_domain_ans);
        stop_icon= (ImageView) root.findViewById(R.id.stop_icon_domain_ans);

        play_recording.setVisibility(View.INVISIBLE);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_audio();
            }
        });

        stop_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                record.setText("60 سیکنڈ میں اپنا مسلہ بیان کریں");
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
                play_recording.setVisibility(View.VISIBLE);
                stop_icon.setVisibility(View.GONE);

                Toast.makeText(getContext(), "آپکا جواب رکارڈ کر لیا گیا ہے", Toast.LENGTH_LONG).show();

                UploadingReady_audio();

            }
        });

        play_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    play_recording.setVisibility(View.INVISIBLE);
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            play_recording.setVisibility(View.VISIBLE);
                            mediaPlayer.release();
                        }
                    });
//                    Toast.makeText(getContext(), "Playing Recording", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
        });


        ///to get recording permission and prepare recording
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        } else {
            recordAudio();
        }

        /////end recording

        ////MEDIA PLAYER
        flag=0;
        mediaPlayer=new MediaPlayer();


        play=root.findViewById(R.id.play_question_ans_page);
        pause=root.findViewById(R.id.pause_question_ans_page);
        pause.setVisibility(View.GONE);
        seekBar=root.findViewById(R.id.seek_bar_ans_page);
        //play.setOnClickListener(this);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(root.getContext().getApplicationContext(), "PLAY", Toast.LENGTH_LONG).show();
                if (flag==0){
                    //  new Player().execute(URL);
                    player=new Player();
                    player.execute(url);
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                }
                else {
                    if (mediaPlayer!=null){
                        playAudio();
                    }
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAudio();
            }
        });
//        pause.setOnClickListener(this::onClick);
        seekBar.setOnTouchListener(this::onTouch);
        mediaPlayer.setOnBufferingUpdateListener(this::onBufferingUpdate);
        mediaPlayer.setOnCompletionListener(this::onCompletion);

        mediaPlayer.setOnInfoListener(this);

        ///END MEDIA PLAYER

        domainMainActivity=(DomainMainActivity) getActivity();

        question = getArguments().getString("question");
        image_1 = getArguments().getString("image_1");
        image_2 = getArguments().getString("image_2");
        image_3 = getArguments().getString("image_3");
        member_name = getArguments().getString("member_name");
        member_id = getArguments().getString("member_id");
        question_id = getArguments().getString("question_id");
        question_date_time = getArguments().getString("question_date_time");
        member_city = getArguments().getString("member_city");
        recording = getArguments().getString("recording");

        ///NOT DELTE COMMENT FOR RECORDING ORIGINAL
        url=recording;

        if (url.isEmpty() || url.equals(null) || url.equals("null")){
            layout_question_recording.setVisibility(View.GONE);
        }
        fasal = getArguments().getString("fasal");

        text_view_question= root.findViewById(R.id.textview_question_for_domain_ans_page);
        text_view_question.setText(question);
        text_view_question_name_city= root.findViewById(R.id.textview_domain_name_city_question_ans_page);
        text_view_question_name_city.setText(member_name+" / "+member_city);
        text_view_fasal=root.findViewById(R.id.textview_doamin_fasal_ans_page);
        text_view_fasal.setText(fasal);
        text_view_question_time=root.findViewById(R.id.textview_question_time_ans_page);
        text_view_question_time.setText(question_date_time);
        domain_name=root.findViewById(R.id.textview_domain_name_city_question_ans_page_domain);
        domain_name.setText(domainMainActivity.Name+" / "+domainMainActivity.City);
        domain_occupation=root.findViewById(R.id.textview_doamin_occupation_ans_page_domain);
        domain_occupation.setText("("+domainMainActivity.Occupation+")");

        domain_name_str=domainMainActivity_2.Name;
        domain_phone=domainMainActivity_2.Phone;
        domain_id=domainMainActivity_2.ID;
        domain_id_card=domainMainActivity_2.ID_Card;
        answer_city=domainMainActivity_2.City;
        occupation=domainMainActivity_2.Occupation;

        answer_submit=root.findViewById(R.id.edit_text_answer_for_domain_ans_page_domain);


        submit_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer=answer_submit.getText().toString();
                SaveAnswerData();
            }
        });

        return root;
    }




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

    //record audio functionS
    private void recordAudio() {
        stop_icon.setVisibility(View.GONE);
        play_recording.setVisibility(View.INVISIBLE);
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
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(getContext(), "انتظار کریں", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile_audio();
        }
    }

    private void uploadFile_audio() {

        mStorageRef_audio = FirebaseStorage.getInstance().getReference("Answer_recordings");


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
                        }
                    }, 500);

                    Toast.makeText(getContext(), "آپکے انتظار کا شکریہ", Toast.LENGTH_LONG).show();
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

    ///FUNCTIONS TO PLAY MEDIA FILE OF QUESTIONS
    @Override
    public void onPause() {
        if (mediaPlayer!=null)
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        super.onPause();
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int i) {
        seekBar.setSecondaryProgress(i);
        System.out.println(i);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        seekBar.setProgress(0);
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                progressDialog.show();

                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                progressDialog.dismiss();
                break;
        }
        return true;
    }
    private void updateSeekProgress() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / lengthOfAudio) * 100));
                long i=mediaPlayer.getCurrentPosition()/1000;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String temp=LocalTime.ofSecondOfDay(i)+"";
                    player_time.setText(" "+temp.substring(3,temp.length())+" ");
                }

                handler.postDelayed(r, 50);
            }
        }
    }
    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean prepared;

            try
            {
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepare();
                lengthOfAudio = mediaPlayer.getDuration();

                prepared = true;

            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean){
                flag=1;
            }
            else {
                flag=0;
            }
            playAudio();

        }
    }

    private void playAudio() {
        if(mediaPlayer!=null)
        {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            mediaPlayer.start();
            updateSeekProgress();
        }
    }
    private void pauseAudio() {
        if(mediaPlayer!=null)
        {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mediaPlayer.isPlaying())
        {
            SeekBar tmpSeekBar = (SeekBar)v;
            mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
            long i= (lengthOfAudio / 100) * tmpSeekBar.getProgress();
            //player_time.setText(tmpSeekBar.getProgress()+"");
            //player_time.setText(LocalTime.ofSecondOfDay(i)+"");

        }
        else {
            SeekBar tmpSeekBar = (SeekBar)v;
            mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
            player_time.setText(tmpSeekBar.getProgress()+"");
            //int i =(lengthOfAudio / 100) * tmpSeekBar.getProgress();
            //player_time.setText(LocalTime.ofSecondOfDay(i)+"");


        }
        return false;
    }

    //FOR PALAYING AUDIO FILE OF QUESTION

    //////CAMERA AND GALLERY MULTIPLE
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
                        image_uris= (ArrayList<Uri>) uriList;
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
                                    total_images.setText("+"+total_img);
                                }else{
                                    total_images.setText("");
                                }

                                btn_delete_images.setEnabled(true);
                                btn_delete_images.setAlpha(1f);
                                layout_show_images.setVisibility(View.VISIBLE);
                                show_images_btn.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                });

//        config.setCameraHeight(R.dimen.camera_height);
//        config.setToolbarTitleRes(R.string.tasaveer_len);
//        config.setSelectionLimit(6);
//        config.setSelectionMin(1);
//        ImagePickerActivity.setConfig(config);
//        Intent intent  = new Intent(getActivity().getApplicationContext(), ImagePickerActivity.class);
//        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);

    }
    public void upload_firebase(Bitmap bitmap) {
        upload_URL = "https://www.novaeno.com/agriasan/uploadAnswerImage.php";

        progressDialog.show();
//        String extension = path_to_image.substring(path_to_image.lastIndexOf("."));
//        mountainsRef = storageRef.child(System.currentTimeMillis()
//                + "" + extension);

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
                MainActivity.progressDialog.hide();
            }catch (Exception ignored) { }
//            SaveQuestionData();
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
                            save_db_data();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    MainActivity.progressDialog.hide();
                }catch (Exception ignored)
                {
                }
//                SaveQuestionData();
                Log.e("Errrrorrrr", volleyError.toString());

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 900000,
                10000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjectRequest);

        /////////NEW CODE END

//        InputStream stream = null;
//        try {
//            stream = new FileInputStream(new File("" + path_to_image));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        uploadTask = mountainsRef.putStream(stream);

//        uploadTask = mountainsRef.putFile(temp_uri_image);
//
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
//
//                                databaseReference.child("Answer_Image_"+j)
//                                        .setValue(""+imageUrl);
//                                //childRef = mRootref.child(id).child("Image_"+j);
//                                //childRef.setValue(""+imageUrl);
////                                    progressDialog.dismiss();
//                                j++;
//                                if(j==image_uris.size())
//                                {
//                                    save_db_data();
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }


    Uri temp_uri_image=null;

    ////to save data finally

    InputStream input=null;
    ExifInterface exif=null;
    private void SaveAnswerData() {
        images_to_firebase_db=new ArrayList<>();

        if (answer_submit.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity().getApplicationContext(), "پہلےاپناجواب درج کریں", Toast.LENGTH_LONG).show();
        }else {
//            try {
//                progressDialog.show();
//            }catch (Exception ex)
//            {
//            }
            //Toast.makeText(getActivity().getApplicationContext(), ""+answer, Toast.LENGTH_LONG).show();
            databaseReference= FirebaseDatabase.getInstance().getReference()
                    .child("Questions").child(question_id);
            i=0;
            j=0;
            if (image_uris.size()==0){
                save_db_data();
            }else{
                for (i = 0; i < image_uris.size(); i++) {
                    path_to_image=image_uris.get(i).toString();
                    temp_uri_image=image_uris.get(i);
                    try {
                        input = getContext().getContentResolver().openInputStream(temp_uri_image);
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
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    upload_firebase();
                }
            }
        }
    }
    //////CAMERA AND GALLERY MULTIPLE END
    public void save_db_data() {
        databaseReference.child("Answer").setValue(answer);
        databaseReference.child("Total_Answer_Images").setValue(image_uris.size()+"");

        for (int i=0;i<images_to_firebase_db.size();i++){

            cmp_url="https://novaeno.com/agriasan/agriasan_images/Answers/";

            cmp_url=cmp_url+images_to_firebase_db.get(i)+".JPG";

            databaseReference.child("Answer_Image_"+i)
                    .setValue(""+cmp_url);

//            childRef = mRootref.child(id).child("Image_"+i);
//            childRef.setValue(""+cmp_url);
        }

        databaseReference.child("Recording_Answer").setValue(recodring_link);
        databaseReference.child("Domain_Name").setValue(domain_name_str);  //
        databaseReference.child("Domain_Phone").setValue(domain_phone);  //
        databaseReference.child("Answer_Time").setValue(formattedTime);  //
        databaseReference.child("Answer_Date").setValue(formattedDate);  //
        databaseReference.child("Domain_ID").setValue(domain_id);  //
        databaseReference.child("Domain_ID_Card").setValue(domain_id_card);
        databaseReference.child("Domain_Occupation").setValue(occupation);  //
        databaseReference.child("Domain_City").setValue(answer_city);  //
        Toast.makeText(domainMainActivity.getApplicationContext(), "آپکا جواب درج کر لیا گیا ہے شکریہ!", Toast.LENGTH_LONG).show();
        //ek fragment pichay jaata
        try{
            progressDialog.hide();
        }catch (Exception ex)
        {
        }
        getActivity().onBackPressed();
        //getActivity().onBackPressed();
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
    public void popup_window_show_images(View v) {

        LayoutInflater inflater = (LayoutInflater) domainMainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_show_images, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        PopupWindow popupWindow_images;

        popupWindow_images = new PopupWindow(popupView, width, height);
        popupWindow_images.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        popupWindow_images.setFocusable(true);
        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setTouchable(true);
        popupWindow_images.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow_images.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        listView_images = popupView.findViewById(R.id.gv);

        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);

        galleryAdapter = new GalleryAdapter(domainMainActivity.getApplicationContext(),image_uris,root);
        listView_images.setAdapter(galleryAdapter);

        popupWindow_images.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (image_uris.size()!=0){
                    if (layout_show_images != null) {
                    Glide.with(Objects.requireNonNull(getActivity()))
                            .load(image_uris.get(0))
                            .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                            .into(show_images_btn);
                    int total_img=0;
                    total_img=image_uris.size()-1;

                    if (image_uris.size()>=2){
                        total_images.setText("+"+total_img);
                    }else{
                        total_images.setText("");
                    }

                    btn_delete_images.setEnabled(true);
                    btn_delete_images.setAlpha(1f);
                    layout_show_images.setVisibility(View.VISIBLE);
                }}
                else{
//                    Toast.makeText(getContext(), "CHeck111" , Toast.LENGTH_LONG).show();

                    btn_delete_images.setEnabled(false);
                    btn_delete_images.setAlpha(0.2f);
                    layout_show_images.setVisibility(View.INVISIBLE);

                    total_images.setText("");
                    show_images_btn.setVisibility(View.INVISIBLE);
                }

                popupWindow_images.dismiss();
            }
        });

        config = new Config();
    }

    public void record_audio(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
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
        play_recording.setVisibility(View.INVISIBLE);
        stop_icon.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "ریکارڈنگ شروع ہو گئی ہے", Toast.LENGTH_LONG).show();

        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                domainMainActivity_2.runOnUiThread(new Runnable() {
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
                            play_recording.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(), "60 سیکنڈ پورے ہو گیے ہیں", Toast.LENGTH_LONG).show();
                            UploadingReady_audio();
                        }else{
                            Toast.makeText(getContext(), "Nothing to stop", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };  //5000 means 5 sec
        t.schedule(tt,60000);
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

    class ImagesAdapter extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            progressDialog.show();

            galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Product_images_link);


            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(galleryAdapterImageString);




            super.onPostExecute(aVoid);
        }
    }

}