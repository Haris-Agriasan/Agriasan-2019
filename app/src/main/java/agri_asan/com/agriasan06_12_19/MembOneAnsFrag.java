package agri_asan.com.agriasan06_12_19;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static agri_asan.com.agriasan06_12_19.MainActivity.progressDialog;

//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();


public class MembOneAnsFrag extends Fragment implements View.OnClickListener{



    View black_divider_view;
    LinearLayout linear_layout_for_answer;

    DatabaseReference databaseReference;

    String answer_name,answer_city,answer,
            occupation;

    TextView answer_submit;

    String question,question_date_time
    ,image_1,image_2,image_3,member_name
    ,member_city,question_id,member_id,recording_question,fasal,recording_answer;

    TextView text_view_question,text_view_question_time,text_view_fasal;

    TextView answer_date_time;

    TextView domain_name ,domain_occupation;

//    MainActivity mainActivity;


    ImagesAdapter imagesadapter;
    ///FOR MEDIA PLAYER
    ImageView play ,pause;
    ImageView play_answer ,pause_answer;

    SeekBar seekBar;
    SeekBar seekBar_answer;
    Player player;
    Player_Answer player_answer;
    int flag;
    int flag_answer;
    String url_question="https://www.roshne.com//Tilawat_AlSudaes/001-AlSudaes.mp3";
    String url_answer="https://www.roshne.com//Tilawat_AlSudaes/001-AlSudaes.mp3";
    FileInputStream fis = null;
//    ProgressDialog progressDialog;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer_answer;
    private int lengthOfAudio;
    private int lengthOfAudio_answer;
    private final Handler handler = new Handler();
    private final Handler handler_answer = new Handler();
    private final Runnable r = new Runnable()
    {
        @Override
        public void run() {
            updateSeekProgress();
        }
    };
    private final Runnable r_answer = new Runnable()
    {
        @Override
        public void run() {
            updateSeekProgress_answer();
        }
    };
    @Override
    public void onClick(View v) {
    }
    ////END MEDIA PLAYER

    View root;

    TextView player_time;
    TextView player_time_answer;

    TextView textView_question_no;
    String question_no;

    ////FOR POPUP WINDOW
    ImageView image_popup;
    ////FOR POPUP WINDOW END

    ArrayList<String> Question_images_link;
    ArrayList<String> Answer_images_link;

    ArrayList<String> Question_images_link_loading;

    GridView listView;
    GridView gridView;
    private GalleryAdapterImageString galleryAdapterImageString;
    private GalleryAdapterImageString galleryAdapterImageString_2;

    LinearLayout layout_recorder_answer;
    LinearLayout layout_recorder_question;

    LinearLayout layout_for_question_images;
    LinearLayout layout_top;

    LinearLayout layout_sawal_title;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        Toast.makeText(getContext(), "مہربانی انتظار کریں!!!", Toast.LENGTH_LONG).show();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.frag_member_one_ans, container, false);

        //مہربانی انتظار کریں!!!

//        progressDialog.show();




        ///IMAGE THINGS

        linear_layout_for_answer=root.findViewById(R.id.linear_layout_for_answer);
        black_divider_view=root.findViewById(R.id.black_divider_view);


        player_time=root.findViewById(R.id.player_time_question_one_qa_memebr);
        player_time_answer=root.findViewById(R.id.player_time_answer_one_qa_memebr);

        textView_question_no=root.findViewById(R.id.textview_member_question_no_one_qa_memebr);


        answer_date_time=root.findViewById(R.id.textview_answer_time_one_qa_memebr);

        Question_images_link=getArguments().getStringArrayList("images_list_questions");
        Answer_images_link=getArguments().getStringArrayList("images_list_answers");

        Question_images_link_loading=new ArrayList<>();
        Question_images_link_loading.add("https://www.novaeno.com/agriasan/loading_4.gif");


        listView = root.findViewById(R.id.gv);

        gridView=root.findViewById(R.id.gv_answer);

        imagesadapter=new ImagesAdapter();
        imagesadapter.execute();

//        imagesadapter.onPreExecute();
//        imagesadapter.onPostExecute(null);
//        galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Question_images_link_loading);
//        listView.setAdapter(galleryAdapterImageString);
//
//        galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Answer_images_link);
//        gridView.setAdapter(galleryAdapterImageString);


        ///NOTE DELETE COMMENT FOR IMAGES OF QUESTION
//        Picasso.get().load(question_image_1_str).placeholder( R.drawable.loading_4 ).into(question_image_1);




        ////MEDIA PLAYER
        progressDialog=new ProgressDialog(getContext());
        flag=0;
        flag_answer=0;
        progressDialog.setMessage("مہربانی انتظار کریں!!!");
        progressDialog.setCancelable(false);
        mediaPlayer=new MediaPlayer();
        mediaPlayer_answer=new MediaPlayer();

        //MEDIA PLAYER FOR ANSWER
        play_answer=root.findViewById(R.id.play_answer_one_qa_member);
        pause_answer=root.findViewById(R.id.pause_answer_one_qa_member);
        pause_answer.setVisibility(View.GONE);
        seekBar_answer=root.findViewById(R.id.seek_bar_answer_one_qa_member);
        play_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer!=null){
                    pauseAudio();
                }
                if (flag_answer==0){
                    //  new Player().execute(URL);
                    player_answer=new Player_Answer();
                    player_answer.execute(url_answer);
                    play_answer.setVisibility(View.GONE);
                    pause_answer.setVisibility(View.VISIBLE);
                }
                else {
                    if (mediaPlayer_answer!=null){
                        playAudio_answer();
                    }
                }
            }
        });
        pause_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAudio_answer();
            }
        });
        seekBar_answer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer_answer.isPlaying()) {
                    SeekBar tmpSeekBar = (SeekBar)v;
                    mediaPlayer_answer.seekTo((lengthOfAudio_answer / 100) * tmpSeekBar.getProgress() );
                    long i= (lengthOfAudio_answer / 100) * tmpSeekBar.getProgress(); }
                else { SeekBar tmpSeekBar = (SeekBar)v;
                    mediaPlayer_answer.seekTo((lengthOfAudio_answer / 100) * tmpSeekBar.getProgress() );
                     }
                return false;
            }
        });
        mediaPlayer_answer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int i) {
                seekBar_answer.setSecondaryProgress(i);
                System.out.println(i);
            }
        });
        mediaPlayer_answer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar_answer.setProgress(0);
                pause_answer.setVisibility(View.GONE);
                play_answer.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer_answer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
        });
        ///media player for ANSWER END
        ///media player for question recording
        play=root.findViewById(R.id.play_question_one_qa_memebr);
        pause=root.findViewById(R.id.pause_question_one_qa_memebr);
        pause.setVisibility(View.GONE);
        seekBar=root.findViewById(R.id.seek_bar_question_one_qa_memebr);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer_answer!=null){
                    pauseAudio_answer();
                }
                if (flag==0){
                    //  new Player().execute(URL);
                    player=new Player();
                    player.execute(url_question);
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
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer.isPlaying())
                {
                    SeekBar tmpSeekBar = (SeekBar)v;
                    mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
                    long i= (lengthOfAudio / 100) * tmpSeekBar.getProgress();
                }
                else {
                    SeekBar tmpSeekBar = (SeekBar)v;
                    mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
                }
                return false;
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int i) {
                seekBar.setSecondaryProgress(i);
                System.out.println(i);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
        });

        ///END MEDIA PLAYER

        answer_city=getArguments().getString("answer_city");
        answer_name=getArguments().getString("answer_name");
        question = getArguments().getString("question");
        image_1 = getArguments().getString("image_1");
        image_2 = getArguments().getString("image_2");
        image_3 = getArguments().getString("image_3");
        member_name = getArguments().getString("member_name");
        member_id = getArguments().getString("member_id");
        question_id = getArguments().getString("question_id");
        question_date_time = getArguments().getString("question_date_time");
        member_city = getArguments().getString("member_city");
        recording_question = getArguments().getString("recording");
        recording_answer = getArguments().getString("answer_recording");

        if (answer_name.equals("waiting_for_answer")){
            linear_layout_for_answer.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(question_id)){
            databaseReference= FirebaseDatabase.getInstance().getReference()
                    .child("Questions").child(question_id);
            databaseReference.child("Read").setValue("yes");
        }



        url_question=recording_question;
        url_answer=recording_answer;

        layout_recorder_question=root.findViewById(R.id.layout_recorder_question);
        layout_recorder_answer=root.findViewById(R.id.layout_recorder_answer);


        if (url_question.equals("")){
            layout_recorder_question.setVisibility(View.GONE);
        }
        if (url_answer.equals("")){
            layout_recorder_answer.setVisibility(View.GONE);
        }

        question_no = getArguments().getString("question_no");
        String extra_thing=getArguments().getString("extra_thing");


        if (TextUtils.isEmpty(extra_thing) || extra_thing.equals("null") || extra_thing=="null"||TextUtils.equals(extra_thing ,"null")){
            //Toast.makeText(getContext(), "Check 1", Toast.LENGTH_LONG).show();
            if (question_no.equals("0")){
                textView_question_no.setText("آپکے سوال کا جواب آ چکا ہے");
            }else{
                textView_question_no.setText("آپکے سوال نمبر "+question_no+" کا جواب آ چکا ہے");
            }
        }else{
            //Toast.makeText(getContext(), "Check 2", Toast.LENGTH_LONG).show();
            textView_question_no.setText(extra_thing);
        }

        answer = getArguments().getString("answer");
        String answer_date=getArguments().getString("answer_date");
        String answer_time=getArguments().getString("answer_time");

        String Time_formatted_answer = "";
        String Date_formatted_answer="";

        if (!answer_date.equals("")){
            Date_formatted_answer=answer_date.substring(0,6)+answer_date.substring(8);
//        String Time_formatted_answer = answer_time.substring(0,5);
            Time_formatted_answer = "";

            String time_trim_ans=answer_time.substring(2,5);
            int hr_ans= Integer.parseInt(answer_time.substring(0,2));
            if (hr_ans>12){
                hr_ans=hr_ans-12;
                Time_formatted_answer=(hr_ans+time_trim_ans+" PM");
            }else{
                Time_formatted_answer=(hr_ans+time_trim_ans+" AM");
            }
        }

        /////////////////////////NOT DELETE COMMENT FOR RECORDING ORIGINAL
        fasal = getArguments().getString("fasal");
        String occupation=getArguments().getString("answer_occupation");
        answer_date_time.setText(Date_formatted_answer+" "+Time_formatted_answer);
        text_view_question= root.findViewById(R.id.textview_question_one_qa_memebr);
        text_view_question.setText(question);
        text_view_fasal=root.findViewById(R.id.textview_member_fasal_one_qa_memebr);
        text_view_fasal.setText(fasal);
        text_view_question_time=root.findViewById(R.id.textview_question_time_one_qa_memebr);
        text_view_question_time.setText(question_date_time);
        domain_name=root.findViewById(R.id.textview_domain_name_city_one_qa_memebr);
        domain_name.setText(answer_name+" / "+answer_city);
        domain_occupation=root.findViewById(R.id.textview_domain_occupation_one_qa_memebr);
        domain_occupation.setText("("+occupation+")");
        answer_submit=root.findViewById(R.id.text_answer_one_qa_page);
        layout_for_question_images=root.findViewById(R.id.layout_for_question_images);
        layout_top=root.findViewById(R.id.layout_top);

        layout_sawal_title=root.findViewById(R.id.layout_sawal_title);


        answer_submit.setText(answer);


        if (answer_name.equals("waiting_for_answer")){


            textView_question_no.setText("آپکا پوچھا گیا سوال");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    0.2f
            );
            text_view_question_time.setLayoutParams(param);
            text_view_question_time.setGravity(Gravity.CENTER);
            black_divider_view.setVisibility(View.GONE);

            LinearLayout.LayoutParams param_2 = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    0.45f
            );
            layout_recorder_question.setLayoutParams(param_2);

            LinearLayout.LayoutParams param_3 = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    1.6f
            );
            LinearLayout.LayoutParams param_4 = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    0.4f
            );

            layout_top.setLayoutParams(param_4);
            layout_for_question_images.setLayoutParams(param_3);

            LinearLayout.LayoutParams param_5 = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    0.4f
            );
            layout_sawal_title.setLayoutParams(param_5);
        }
        return root;
    }

    /////////////IMAGE FUNCTIONS
    ///FUNCTIONS TO PLAY MEDIA FILE OF QUESTIONS
    @Override
    public void onPause() {
        if (mediaPlayer!=null)
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        if (mediaPlayer_answer!=null)
            if (mediaPlayer_answer.isPlaying()) {
                mediaPlayer_answer.stop();
            }

//        if (mainActivity.mTitle.getText().toString().equals("سوال اور جواب"))
//        {
//            mainActivity.mTitle.setText("عوامی رائے");
//        }
//        if (mainActivity.mTitle.getText().toString().equals("جواب"))
//        {
//            mainActivity.mTitle.setText("آپکے پوچھے گئے سوالات");
//        }
        super.onPause();
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
    private void updateSeekProgress_answer() {
        if (mediaPlayer_answer != null) {
            if (mediaPlayer_answer.isPlaying()) {
                seekBar_answer.setProgress((int) (((float) mediaPlayer_answer.getCurrentPosition() / lengthOfAudio_answer) * 100));
                long i=mediaPlayer_answer.getCurrentPosition()/1000;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String temp=LocalTime.ofSecondOfDay(i)+"";
                    player_time_answer.setText(" "+temp.substring(3,temp.length())+" ");
                }

                handler_answer.postDelayed(r_answer, 50);
            }
        }
    }
    class ImagesAdapter extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            progressDialog.show();
            galleryAdapterImageString_2 = new GalleryAdapterImageString(getContext().getApplicationContext(),Question_images_link);
            galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Answer_images_link);

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


//            galleryAdapterImageString = new GalleryAdapterImageString(Objects.requireNonNull(getContext()).getApplicationContext(),Question_images_link);
//            listView.setAdapter(galleryAdapterImageString);
//            galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Answer_images_link);
//            gridView.setAdapter(galleryAdapterImageString);

//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(galleryAdapterImageString_2);
            gridView.setAdapter(galleryAdapterImageString);

//            progressDialog.hide();
            super.onPostExecute(aVoid);
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
    class Player_Answer extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean prepared;

            try
            {
                mediaPlayer_answer.setDataSource(params[0]);
                mediaPlayer_answer.prepare();
                lengthOfAudio_answer = mediaPlayer_answer.getDuration();

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
                flag_answer=1;
            }
            else {
                flag_answer=0;
            }
            playAudio_answer();
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
    private void playAudio_answer() {
        if(mediaPlayer_answer!=null)
        {
            pause_answer.setVisibility(View.VISIBLE);
            play_answer.setVisibility(View.GONE);
            mediaPlayer_answer.start();
            updateSeekProgress_answer();
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
    private void pauseAudio_answer() {
        if(mediaPlayer_answer!=null)
        {
            pause_answer.setVisibility(View.GONE);
            play_answer.setVisibility(View.VISIBLE);
            mediaPlayer_answer.pause();
        }
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
    //FOR PALAYING AUDIO FILE OF QUESTION
}