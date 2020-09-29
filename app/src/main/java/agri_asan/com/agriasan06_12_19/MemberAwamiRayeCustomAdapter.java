package agri_asan.com.agriasan06_12_19;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MemberAwamiRayeCustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;
    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    ProgressDialog progressDialog;


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Set<String> list = new HashSet<String>();
    // View lookup cache
    private static class ViewHolder {
        TextView question;
        TextView answer;
        TextView question_city;
        TextView answer_city;
        TextView question_time;
        TextView answer_time;
        TextView occupation;
        TextView fasal;
        TextView question_phone;
        TextView answer_phone;
        TextView ID;
        TextView question_name;
        TextView answer_name;
        TextView name;
        TextView type;
        TextView version_number;
        TextView feature;
        ImageView main_image;

        ArrayList<String> ques_Images_List;
        ArrayList<String> ans_Images_List;

//        Button btn_see_details;

        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info;

        LinearLayout layout_view_question_detail;
        RelativeLayout view_product_details;

    }

    public MemberAwamiRayeCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
        }
    }
    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
//        mainActivity=(MainActivity)  getContext();

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("مہربانی انتظار کریں!!!");
        progressDialog.setCancelable(false);

        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        list=pref.getStringSet("choosed_fasal",null); // getting String

        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {

            //Toast.makeText(getContext(), ""+list, Toast.LENGTH_LONG).show();


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.question = (TextView) convertView.findViewById(R.id.textview_question);
            viewHolder.answer = (TextView) convertView.findViewById(R.id.textview_answer);

            viewHolder.question_name = (TextView) convertView.findViewById(R.id.textview_name_city_question);
            viewHolder.answer_name = (TextView) convertView.findViewById(R.id.textview_name_city_answer);

            viewHolder.occupation = (TextView) convertView.findViewById(R.id.textview_occupation);
            viewHolder.fasal = (TextView) convertView.findViewById(R.id.textview_fasal);

            viewHolder.question_time = (TextView) convertView.findViewById(R.id.textview_time_question);
            viewHolder.answer_time = (TextView) convertView.findViewById(R.id.textview_time_answer);

            viewHolder.main_image = convertView.findViewById(R.id.main_image_for_question);

//            viewHolder.btn_see_details=convertView.findViewById(R.id.awami_raye_detail_button);

            viewHolder.layout_view_question_detail=convertView.findViewById(R.id.layout_view_question_detail);

            //viewHolder.txtName = (TextView) convertView.findViewById(R.id.textview_name_city_question);
//            viewHolder.txtType = (TextView) convertView.findViewById(R.id.textview_time_question);
            //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.textview_time_answer);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.main_image_for_question);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.ques_Images_List=dataModel.getQues_Images_List();
        viewHolder.ans_Images_List=dataModel.getAns_Images_List();

        viewHolder.question.setText(dataModel.getQuestion());
        viewHolder.answer.setText(dataModel.getAnswer());

        viewHolder.question_name.setText(dataModel.getQuestion_name()+"/"+dataModel.getQuestion_city());
        viewHolder.answer_name.setText(dataModel.getAnswer_name()+"/"+dataModel.getAnswer_city());


        String time_trim_q=dataModel.getQuestion_time().substring(2,5);
        int hr_q= Integer.parseInt(dataModel.getQuestion_time().substring(0,2));
        if (hr_q>12){
            hr_q=hr_q-12;
            viewHolder.question_time.setText(hr_q+time_trim_q+" PM");
        }else{
            viewHolder.question_time.setText(hr_q+time_trim_q+" AM");
        }

        String time_trim_ans=dataModel.getAnswer_time().substring(2,5);
        int hr_ans= Integer.parseInt(dataModel.getAnswer_time().substring(0,2));
        if (hr_ans>12){
            hr_ans=hr_ans-12;
            viewHolder.answer_time.setText(hr_ans+time_trim_ans+" PM");
        }else{
            viewHolder.answer_time.setText(hr_ans+time_trim_ans+" AM");
        }

        viewHolder.occupation.setText("("+dataModel.getOccupation()+")");
        viewHolder.fasal.setText(dataModel.getFasal());

        if (viewHolder.ques_Images_List.size()>=1) {
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext()).load(viewHolder.ques_Images_List.get(0))
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
//                    .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                    .into(viewHolder.main_image);
//            Picasso.get().load(viewHolder.ques_Images_List.get(0)).placeholder(R.drawable.loading_4).error(R.drawable.no_image).into(viewHolder.main_image);
        }else{
            //.placeholder(R.drawable.loading_4)
            Glide.with(getContext())
                    .load(R.drawable.no_image)
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                    .into(viewHolder.main_image);
        }


        viewHolder.layout_view_question_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "مہر بانی انتظار کریں!", Toast.LENGTH_LONG).show();
                see_details(dataModel);
            }
        });
//        viewHolder.btn_see_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "مہر بانی انتظار کریں!", Toast.LENGTH_LONG).show();
//                see_details(dataModel);
//            }
//        });

        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void see_details(DataModel dataModel){
//        progressDialog.show();
        Fragment fragment = new MembOneAnsFrag();
        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

        if (dataModel.getUser_type().toString().equals("member")){
            mainActivity=(MainActivity)  getContext();
            mainActivity.mTitle.setText("سوال اور جواب");
        }if (dataModel.getUser_type().toString().equals("domain")){
            domainMainActivity=(DomainMainActivity)  getContext();
            domainMainActivity.mTitle.setText("سوال اور جواب");
        }

        Bundle bundle = new Bundle();

        bundle.putStringArrayList("images_list_questions",dataModel.getQues_Images_List());
        bundle.putStringArrayList("images_list_answers",dataModel.getAns_Images_List());

        bundle.putString("answer_occupation", dataModel.getOccupation()+"");
        bundle.putString("answer_city", dataModel.getAnswer_city()+"");
        bundle.putString("answer_name", dataModel.getAnswer_name()+"");
        bundle.putString("answer_image_3", dataModel.getImage_3_ans()+"");
        bundle.putString("answer_image_2", dataModel.getImage_2_ans()+"");
        bundle.putString("answer_image_1", dataModel.getImage_1_ans()+"");
        bundle.putString("answer_time", dataModel.getAnswer_time()+"");
        bundle.putString("answer_date", dataModel.getAnswer_date()+"");
        bundle.putString("answer_recording", dataModel.getRecording_answer()+"");
        bundle.putString("answer", dataModel.getAnswer()+"");

        bundle.putString("question_no", dataModel.getQuestion_no()+"");
        bundle.putString("question", dataModel.getQuestion());
        //bundle.putString("question_date_time", Date_formatted+" "+Time_formatted);
        bundle.putString("image_1", dataModel.getImage_1());
        bundle.putString("image_2", dataModel.getImage_2());
        bundle.putString("image_3", dataModel.getImage_3());
        bundle.putString("member_name", dataModel.getQuestion_name());
        bundle.putString("member_city", dataModel.getQuestion_city());
        //bundle.putString("question_id", dataModel.getID());
        bundle.putString("recording", dataModel.getRecording()+"");
        //bundle.putString("member_id", dataModel.getMember_id());
        bundle.putString("fasal", dataModel.getFasal());
        bundle.putString("extra_thing", dataModel.getExtra_thing());

        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

//        progressDialog.hide();
        fragmentTransaction.commit();
    }

}

