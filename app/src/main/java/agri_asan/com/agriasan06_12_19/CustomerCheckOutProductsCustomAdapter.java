package agri_asan.com.agriasan06_12_19;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomerCheckOutProductsCustomAdapter extends ArrayAdapter<DataModel> {

    MainActivity mainActivity;

    DomainMainActivity domainMainActivity;

    private ArrayList<DataModel> dataSet;
    Context mContext;
    DomainMainActivity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView product_name;
        TextView product_price;
        TextView textview_product_quantity;
        TextView textview_total_price;
        ImageView product_image_0;

        LinearLayout layout_main_parent;
        ViewGroup.LayoutParams params;
        DatabaseReference databaseReference;
        String User="";

    }
    public CustomerCheckOutProductsCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.for_customer_view_products_row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder[] viewHolder = new ViewHolder[1]; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder[0] = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.customer_check_out_products_row_item, parent, false);

              viewHolder[0].product_name =  convertView.findViewById(R.id.textview_name_of_product);
              viewHolder[0].product_price =  convertView.findViewById(R.id.textview_product_price);
              viewHolder[0].textview_product_quantity =  convertView.findViewById(R.id.textview_product_quantity);
              viewHolder[0].textview_total_price =  convertView.findViewById(R.id.textview_total_price);
              viewHolder[0].product_image_0 =  convertView.findViewById(R.id.main_image_of_product);
              viewHolder[0].layout_main_parent=convertView.findViewById(R.id.layout_main_parent);

              result=convertView;
            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder[0].product_name.setText(dataModel.getProduct_Name());
        Glide.with(getContext())
                .load(dataModel.getProduct_Image_0())
                .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                .into(viewHolder[0].product_image_0);


        viewHolder[0].product_image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_winodw(dataModel.getProduct_Image_0(),v);
            }
        });

        viewHolder[0].textview_product_quantity.setText("مقدار "+dataModel.getCart_product_quantity());
        int quantity=Integer.parseInt(dataModel.getCart_product_quantity());
        int per_unit_price=Integer.parseInt(dataModel.getProduct_Price());
        int total_price=quantity*per_unit_price;

        viewHolder[0].textview_total_price.setText(quantity+" x "+per_unit_price+" = "+total_price+"/-Rs");
        if (dataModel.getProduct_Measure_In().equals("کوئ نہیں") || dataModel.getProduct_Measure_In()=="کوئ نہیں"){
            viewHolder[0].product_price.setText(dataModel.getProduct_Price()+"/-Rs");
        }else
        {
            String price=dataModel.getProduct_Price()+"/-Rs      "+" قیمت "+dataModel.getProduct_Litre_Kilo()+" "+dataModel.getProduct_Measure_In()+" "+"کی";
            viewHolder[0].product_price.setText(price);
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }


    ImageView button_close;
    ImageView image_popup;

    //////POPUP WINDOW FOR SINGLE IMAGE
    public void popup_winodw(String url_image, View v){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        image_popup=popupView.findViewById(R.id.image_popup);
        ////.placeholder( R.drawable.loading_4 )
        Glide.with(getContext())
                .load(url_image)
                .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                .into(image_popup);
        button_close = popupView.findViewById(R.id.close_btn_popup);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                        popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
    }
    /////POPUP WINDOW END



}



