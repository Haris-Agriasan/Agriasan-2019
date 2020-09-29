package agri_asan.com.agriasan06_12_19;


import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MembAskedTypeOfQuestAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{


    private ArrayList<DataModel> dataSet;
    Context mContext;
    DomainMainActivity activity;

    MainActivity mainActivity;

    //for media player
    ProgressDialog progressDialog;

    int question_number_int=0;


    // View lookup cache
    private static class ViewHolder {
        ///FOR MEDIA PLAYER
        ImageView play ,pause;
        SeekBar seekBar;
        int flag;

        private MediaPlayer mediaPlayer;
        private int lengthOfAudio;
        private final Handler handler = new Handler();

        String url;
        //FOR MEDIA FILES END

        TextView question;
        TextView question_date_time;
        TextView fasal;
        TextView ID;
        TextView name;
        ImageView image_0;
        ImageView image_1;
        ImageView image_2;
        TextView player_time;

        TextView question_number;

        Button see_answer;

        ArrayList<String> ques_Images_List;
        ArrayList<String> ans_Images_List;

        LinearLayout recording_layout;


        //////////////////NEW
        TextView shoba_pro;
        TextView type_pro;
        ImageView image_shoba_type;
        ImageView image_fasal_type;
        String shoba_str;
        String fasal_str;
        LinearLayout layout;

    }

    public MembAskedTypeOfQuestAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.memb_asked_type_of_quest_row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;
    }



    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mainActivity=(MainActivity)  getContext();

        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder[] viewHolder = new ViewHolder[1]; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder[0] = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.memb_asked_type_of_quest_row_item, parent, false);

            viewHolder[0].image_shoba_type=convertView.findViewById(R.id.image_shoba_type);
            viewHolder[0].image_fasal_type=convertView.findViewById(R.id.image_fasal_type);
              viewHolder[0].type_pro =  convertView.findViewById(R.id.textview_shoba_type_product);

              viewHolder[0].layout=convertView.findViewById(R.id.layout);
              result=convertView;

            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder[0].type_pro.setText(dataModel.getFasal()+"/"+dataModel.getFasal_Type_Shoba());
        viewHolder[0].fasal_str=""+dataModel.getFasal();
        viewHolder[0].shoba_str=""+dataModel.getFasal_Type_Shoba();

        viewHolder[0].layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Toast.makeText(getContext(), dataModel.getFasal()+"/"+dataModel.getFasal_Type_Shoba(), Toast.LENGTH_LONG).show();

                Fragment fragment = new MemberSpecificQuestionsFragment();
                    FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();


                    mainActivity.mTitle.setText(dataModel.getFasal()+"/"+dataModel.getFasal_Type_Shoba());
                    Bundle bundle = new Bundle();


                    bundle.putString("fasal", dataModel.getFasal());
                    bundle.putString("shoba", dataModel.getFasal_Type_Shoba());

                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);



                    fragmentTransaction.commit();
            }
        });

        if (viewHolder[0].fasal_str.equals("سورج مکھی")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.flower);
        }
        if (viewHolder[0].fasal_str.equals("چاول")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.rice); }
        if (viewHolder[0].fasal_str.equals("گندم")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.gandum);
        }
        if (viewHolder[0].fasal_str.equals("کپاس")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.cotton);
        }
        if (viewHolder[0].fasal_str.equals("مکئی")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.corn_2);
        }
        if (viewHolder[0].fasal_str.equals("گنا")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.sugar_cane);
        }
        if (viewHolder[0].fasal_str.equals("سرسوں")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.mustard);
        }
//        if (viewHolder[0].fasal_str.equals("پولٹری")){
//            viewHolder[0].image_fasal_type.setImageResource(R.drawable.poultry);
//        }
//        if (viewHolder[0].fasal_str.equals("مچھلی")){
//            viewHolder[0].image_fasal_type.setImageResource(R.drawable.fish);
//        }
        if (viewHolder[0].fasal_str.equals("سبزیاں")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.vegetables);
        }        if (viewHolder[0].fasal_str.equals("سرسوں")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.mustard);
        }
//        if (viewHolder[0].fasal_str.equals("جانور")){
//            viewHolder[0].image_fasal_type.setImageResource(R.drawable.animals);
//        }
        if (viewHolder[0].fasal_str.equals("دالیں")){
            viewHolder[0].image_fasal_type.setImageResource(R.drawable.daalen);
        }

        if (viewHolder[0].shoba_str.equals("کھاد")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.sack);
        }if (viewHolder[0].shoba_str.equals("ادویات")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.medicine);
        }if (viewHolder[0].shoba_str.equals("بیج")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.seeds);
        }if (viewHolder[0].shoba_str.equals("محکمہ انھار")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.mehkama_inhaar);
        }if (viewHolder[0].shoba_str.equals("محکمہ توسیع")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.mehkama_tausee);
        }if (viewHolder[0].shoba_str.equals("مٹی")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.matti);
        }if (viewHolder[0].shoba_str.equals("کیڑے")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.keeray_icon);
        }if (viewHolder[0].shoba_str.equals("جڑی بوٹیاں")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.herb);
        }if (viewHolder[0].shoba_str.equals("ٹنل فارمنگ")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.tunnel_farming);
        }

        if (viewHolder[0].shoba_str.equals("سرسوں")){
            viewHolder[0].image_shoba_type.setImageResource(R.drawable.mustard);
        }
//








        ////NO DELETE BUTTON END
        //String s;


        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        //starting of Media player

        ///Ending of media player
        return convertView;
    }



}



