package agri_asan.com.agriasan06_12_19;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MemberViewImagesCustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

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

        Button btn_see_details;

        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info;
    }

    public MemberViewImagesCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item_images_multiple, data);
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

            viewHolder.main_image = convertView.findViewById(R.id.main_image_for_question);



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


        //Picasso.get().load(dataModel.getImage_1()).placeholder( R.drawable.progress_animation ).into(viewHolder.main_image);



        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

