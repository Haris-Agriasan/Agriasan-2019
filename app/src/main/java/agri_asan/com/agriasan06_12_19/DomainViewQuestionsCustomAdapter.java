package agri_asan.com.agriasan06_12_19;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DomainViewQuestionsCustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    String Date_formatted="";

    String Time_formatted="";

    DomainMainActivity domainMainActivity;
    ////FOR POPUP WINDOW
    ImageView button_close;
    ImageView image_popup;
    String url="https://firebasestorage.googleapis.com/v0/b/agriasan-6b704.appspot.com/o/Questions_images%2F1576232007644.jpg?alt=media&token=3109d31e-d3fc-4533-b63e-a09602cf57d2";
    ////FOR POPUP WINDOW END

    private ArrayList<DataModel> dataSet;
    Context mContext;
    DomainMainActivity activity;

    //for media player
    ProgressDialog progressDialog;

//for media player

    private ViewHolder updateSeekProgress(ViewHolder viewHolder) {
        if (viewHolder.mediaPlayer != null) {
            // (viewHolder.mediaPlayer.isPlaying()) {
                viewHolder.seekBar.setProgress((int) (((float) viewHolder.mediaPlayer.getCurrentPosition() / viewHolder.lengthOfAudio) * 100));
                MyRunnable myRunnable = new MyRunnable(viewHolder);
                viewHolder.handler.postDelayed(myRunnable, 50);
            //}
        }
        return viewHolder;
    }
    public class MyRunnable implements Runnable {
        private ViewHolder myParam;
        public MyRunnable(ViewHolder myParam){
            this.myParam = myParam;
        }
        public void run(){
            myParam=updateSeekProgress(myParam);
        }
    }
    // View lookup cache
    private static class ViewHolder {
        ///FOR MEDIA PLAYER
        ImageView play ,pause;
        SeekBar seekBar;
        Player player;
        int flag;

        private MediaPlayer mediaPlayer;
        private int lengthOfAudio;
        private final Handler handler = new Handler();

        String url;
        //FOR MEDIA FILES END

        TextView question;
        TextView question_date_time;
        TextView fasal;
        TextView question_name_city;
        TextView ID;
        TextView name;
        ImageView image_0;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        TextView player_time;

        ArrayList<String> ques_Images_List;

        Button give_answer;
    }
    //////POPUP WINDOW
    public void popup_winodw(String url_image, View v){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        image_popup=popupView.findViewById(R.id.image_popup);
//        Picasso.get().load(url_image).placeholder( R.drawable.loading_4 ).into(image_popup);
        //.placeholder(R.drawable.loading_4)
        Glide.with(getContext())
                .load(url_image)
                .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                .into(image_popup);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        button_close = popupView.findViewById(R.id.close_btn_popup);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
                return true;
            }
        });
    }
    /////POPUP WINDOW END

    public DomainViewQuestionsCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.for_domina_question_row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;
    }

    private ViewHolder playAudio(ViewHolder viewHolder) {
        if(viewHolder.mediaPlayer!=null)
        {
            //viewHolder.play.setImageResource(0);
            viewHolder.mediaPlayer.start();
            viewHolder=updateSeekProgress(viewHolder);
        }
        viewHolder.play.setVisibility(View.GONE);
        viewHolder.pause.setVisibility(View.VISIBLE);
        return viewHolder;
    }
    private ViewHolder pauseAudio(ViewHolder viewHolder) {
        if(viewHolder.mediaPlayer!=null)
        {
            viewHolder.mediaPlayer.pause();
            //viewHolder.pause.setVisibility(View.GONE);
        }
        viewHolder.play.setVisibility(View.VISIBLE);
        viewHolder.pause.setVisibility(View.GONE);
        return viewHolder;
    }
    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        domainMainActivity = (DomainMainActivity) getContext();

        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder[] viewHolder = new ViewHolder[1]; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder[0] = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.for_domina_question_row_item, parent, false);
             //FOR MEDIA PLAYER
            viewHolder[0].play=convertView.findViewById(R.id.play_question);
            viewHolder[0].pause=convertView.findViewById(R.id.pause_question);
            viewHolder[0].seekBar=convertView.findViewById(R.id.seek_bar);

            //FOR MEDIA PLAYER END
              viewHolder[0].player_time =  convertView.findViewById(R.id.player_time);

              viewHolder[0].fasal =  convertView.findViewById(R.id.textview_doamin_fasal);
              viewHolder[0].question_name_city =  convertView.findViewById(R.id.textview_domain_name_city_question);
              viewHolder[0].image_0 =  convertView.findViewById(R.id.question_domain_image_0);
              viewHolder[0].image_1 =  convertView.findViewById(R.id.question_domain_image_1);
              viewHolder[0].image_2 =  convertView.findViewById(R.id.question_domain_image_2);
              viewHolder[0].question =  convertView.findViewById(R.id.textview_question_for_domain);
              viewHolder[0].question_date_time =  convertView.findViewById(R.id.textview_question_time);
            viewHolder[0].give_answer =  convertView.findViewById(R.id.enter_for_answer_button);
            result=convertView;

            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder[0].ques_Images_List=dataModel.getQues_Images_List();

        if (viewHolder[0].ques_Images_List.size()>=1)
        {
            viewHolder[0].image_0.setVisibility(View.VISIBLE);
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(viewHolder[0].ques_Images_List.get(0))
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_0);
//            Picasso.get().load(viewHolder[0].ques_Images_List.get(0)).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_0);
        }else{
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext()).load(R.drawable.no_image)
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_0);
//            Picasso.get().load(R.drawable.no_image).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_0);

        }

        //Picasso.get().load(viewHolder[0].ques_Images_List.get(0)).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_0);
        if (viewHolder[0].ques_Images_List.size()>=2)
        {
            viewHolder[0].image_1.setVisibility(View.VISIBLE);
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(viewHolder[0].ques_Images_List.get(1))
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_1);
//            Picasso.get().load(viewHolder[0].ques_Images_List.get(1)).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_1);
        }else{
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(R.drawable.no_image)
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_1);
//            Picasso.get().load(R.drawable.no_image).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_1);
        }

        if (viewHolder[0].ques_Images_List.size()>=3)
        {
            viewHolder[0].image_2.setVisibility(View.VISIBLE);
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(viewHolder[0].ques_Images_List.get(2))
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_2);
//            Picasso.get().load(viewHolder[0].ques_Images_List.get(2)).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_2);
        }
        else{
//            .placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(R.drawable.no_image)
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder[0].image_2);
//            Picasso.get().load(R.drawable.no_image).placeholder( R.drawable.loading_4 ).error(R.drawable.no_image).into(viewHolder[0].image_2);
//            viewHolder[0].image_2.setVisibility(View.INVISIBLE);
        }

        //////calling pop up window on image click
        viewHolder[0].image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_winodw(dataModel.getImage_0(),v);
            }
        });
        viewHolder[0].image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp="";
//                temp=temp+viewHolder[0].ques_Images_List.get(1);
                if (viewHolder[0].ques_Images_List.size()>=2)
                    popup_winodw(viewHolder[0].ques_Images_List.get(1),v);
            }
        });
        viewHolder[0].image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder[0].ques_Images_List.size()>=3)
                    popup_winodw(viewHolder[0].ques_Images_List.get(2),v);
            }
        });
        //////calling pop up window on image click END


        ///NO DELETE THINGS
        viewHolder[0].fasal.setText(dataModel.getFasal());
        viewHolder[0].question_name_city.setText(dataModel.getQuestion_name()+" / "+dataModel.getQuestion_city());
        //Picasso.get().load(dataModel.getImage_1()).placeholder( R.drawable.progress_animation ).into(viewHolder[0].image_1);
        //Picasso.get().load(dataModel.getImage_2()).placeholder( R.drawable.progress_animation ).into(viewHolder[0].image_2);
        //Picasso.get().load(dataModel.getImage_3()).placeholder( R.drawable.progress_animation ).into(viewHolder[0].image_3);

        try {
            Date_formatted=dataModel.getQuestion_date().substring(0,6)+dataModel.getQuestion_date().substring(8);
        }catch (Exception e){

        }
//        String Time_formatted = dataModel.getQuestion_time().substring(0,5);




        String time_trim_ans="";
        try{
            time_trim_ans=dataModel.getQuestion_time().substring(2,5);
            int hr_ans= Integer.parseInt(dataModel.getQuestion_time().substring(0,2));
            if (hr_ans>12){
                hr_ans=hr_ans-12;
                Time_formatted=(hr_ans+time_trim_ans+" PM");
            }else{
                Time_formatted=(hr_ans+time_trim_ans+" AM");
            }
        }catch(Exception e){

        }




        viewHolder[0].question.setText(dataModel.getQuestion());
        viewHolder[0].question_date_time.setText(Date_formatted+" "+Time_formatted);
        viewHolder[0].give_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
                Fragment fragment = new DomainGiveAnswerFragment();
                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

                domainMainActivity.mTitle.setText("جواب دیں");
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images_list",dataModel.getQues_Images_List());

                bundle.putString("question", dataModel.getQuestion());
                bundle.putString("question_date_time", Date_formatted+" "+Time_formatted);
                bundle.putString("image_1", dataModel.getImage_1());
                bundle.putString("image_2", dataModel.getImage_2());
                bundle.putString("image_3", dataModel.getImage_3());
                bundle.putString("member_name", dataModel.getQuestion_name());
                bundle.putString("member_city", dataModel.getQuestion_city());
                bundle.putString("question_id", dataModel.getID());
                bundle.putString("recording", dataModel.getRecording());
                bundle.putString("member_id", dataModel.getMember_id());
                bundle.putString("fasal", dataModel.getFasal());

                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);



                fragmentTransaction.commit();
            }
        });
        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        //starting of Media player

        viewHolder[0].url=dataModel.getRecording();
        progressDialog=new ProgressDialog(getContext());
        viewHolder[0].flag=0;
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewHolder[0].pause.setVisibility(View.GONE);
        viewHolder[0].mediaPlayer=new MediaPlayer();
        viewHolder[0].play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder[0].flag==0){
                    //  new Player().execute(URL);
                    viewHolder[0].player=new Player(viewHolder[0]);
                    viewHolder[0].player.execute(viewHolder[0].url);

                    viewHolder[0] = viewHolder[0].player.getViewHolder();

                }
                else {
                    if (viewHolder[0].mediaPlayer!=null){
                        viewHolder[0] =playAudio(viewHolder[0]);
                    }
                }
            }
        });
        viewHolder[0].pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder[0] =pauseAudio(viewHolder[0]);
            }
        });

        viewHolder[0].seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewHolder[0].mediaPlayer.isPlaying())
                {
                    //Toast.makeText(getContext().getApplicationContext(), "Check seekbar", Toast.LENGTH_LONG).show();
                    SeekBar tmpSeekBar = (SeekBar)v;
                    viewHolder[0].mediaPlayer.seekTo((viewHolder[0].lengthOfAudio / 100) * tmpSeekBar.getProgress() );
                    //Toast.makeText(getContext().getApplicationContext(), "Check seekbar "+tmpSeekBar.getProgress(), Toast.LENGTH_LONG).show();

                }
                else {
                    SeekBar tmpSeekBar = (SeekBar)v;
                    viewHolder[0].mediaPlayer.seekTo((viewHolder[0].lengthOfAudio / 100) * tmpSeekBar.getProgress() );
                    //Toast.makeText(getContext().getApplicationContext(), "Check seekbar "+tmpSeekBar.getProgress(), Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });
        viewHolder[0].mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int i) {
                viewHolder[0].seekBar.setSecondaryProgress(i);
                System.out.println(i);
            }
        });
        viewHolder[0].mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                viewHolder[0].seekBar.setProgress(0);
                viewHolder[0].play.setVisibility(View.VISIBLE);
            }
        });
        viewHolder[0].mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
        ///Ending of media player
        return convertView;
    }


    class Player extends AsyncTask<String, Void, Boolean> {


        private ViewHolder viewHolder1;


        public Player(ViewHolder viewHolder) {
            this.viewHolder1 = viewHolder;
        }

        public ViewHolder getViewHolder() {
            return viewHolder1;
        }

        public void setViewHolder(ViewHolder viewHolder) {
            this.viewHolder1 = viewHolder;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean prepared;

            try
            {
                viewHolder1.mediaPlayer.setDataSource(params[0]);
                viewHolder1.mediaPlayer.prepare();
                viewHolder1.lengthOfAudio = viewHolder1.mediaPlayer.getDuration();
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
                viewHolder1.flag=1;
            }
            else {
                viewHolder1.flag=0;
            }
            viewHolder1=playAudio(viewHolder1);

        }
    }
}



