package agri_asan.com.agriasan06_12_19;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.mikepenz.iconics.Iconics.getApplicationContext;

public class VendorDomainViewProductsCustomAdapter extends ArrayAdapter<DataModel> {

    DomainMainActivity domainMainActivity;

    ///FOR FIREBASE REFERENCE
    DatabaseReference reference;
    ////FOR FIREBASE REFERENCE END

    private ArrayList<DataModel> dataSet;
    Context mContext;
    DomainMainActivity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView product_name, product_price, product_date_time, product_verification;
        ImageView product_image_0;
        Button product_delete_btn;
        Button view_edit_product_details;
        Button view_tafseel_product;

        TextView textview_product_seen;
        TextView textview_product_total_orders;

        int order_q=0;
        DatabaseReference databaseReference_total_orders;
    }

    public VendorDomainViewProductsCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.for_member_vendor_products_row_item, data);
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
            convertView = inflater.inflate(R.layout.for_member_vendor_products_row_item, parent, false);

              viewHolder[0].product_name =  convertView.findViewById(R.id.textview_name_of_product);
              viewHolder[0].product_price =  convertView.findViewById(R.id.textview_product_price);
              viewHolder[0].product_date_time =  convertView.findViewById(R.id.textview_time_date_product);
              viewHolder[0].product_image_0 =  convertView.findViewById(R.id.main_image_of_product);
              viewHolder[0].product_delete_btn =  convertView.findViewById(R.id.delete_product);
              viewHolder[0].view_edit_product_details =  convertView.findViewById(R.id.edit_product_details);
              viewHolder[0].view_tafseel_product =  convertView.findViewById(R.id.view_tafseel_product);
              viewHolder[0].product_verification =  convertView.findViewById(R.id.textview_verification);
              viewHolder[0].textview_product_seen =  convertView.findViewById(R.id.textview_product_seen);
              viewHolder[0].textview_product_total_orders =  convertView.findViewById(R.id.textview_product_total_orders);

              result=convertView;

            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder[0].order_q=0;
        viewHolder[0].textview_product_total_orders.setText(0+"");
        viewHolder[0].databaseReference_total_orders=FirebaseDatabase.getInstance().getReference();
        try{
            final Query query=viewHolder[0].databaseReference_total_orders
                    .child("Orders").orderByChild("Product_ID").equalTo(dataModel.getProduct_ID());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        viewHolder[0].order_q=0;
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            Order product= issue.getValue(Order.class);

                            viewHolder[0].order_q = viewHolder[0].order_q + Integer.parseInt(product.getProduct_Quantity());
//                        Toast.makeText(getContext(), ""+viewHolder[0].order_q, Toast.LENGTH_LONG).show();

                        }
                        viewHolder[0].textview_product_total_orders.setText(viewHolder[0].order_q+"");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        if (!TextUtils.isEmpty(dataModel.getVerification())){
            if (dataModel.getVerification().equals("0")){
                viewHolder[0].product_verification.setText("غیر تصدیق شدہ");
            }else{
                viewHolder[0].product_verification.setText("تصدیق شدہ");
            }
        }

        viewHolder[0].textview_product_seen.setText(dataModel.getViews()+"");

        viewHolder[0].product_name.setText(dataModel.getProduct_Name());
        ////NO DELETE IMAGE 0
//        .submit(200,200).placeholder( R.drawable.loading_4 )
        Glide.with(getContext())
                .load(dataModel.getProduct_Image_0())
                .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                .into(viewHolder[0].product_image_0);
//        Picasso.get().load(dataModel.getProduct_Image_0()).resize(200,200).placeholder( R.drawable.loading_4 ).into(viewHolder[0].product_image_0);

        viewHolder[0].product_image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_winodw(dataModel.getProduct_Image_0(),v);
            }
        });

        String Date_formatted=dataModel.getProduct_Date().substring(0,6)+dataModel.getProduct_Date().substring(8);

        String Time_formatted;


        String time_trim_ans=dataModel.getProduct_Time().substring(2,5);
        int hr_ans= Integer.parseInt(dataModel.getProduct_Time().substring(0,2));
        if (hr_ans>12){
            hr_ans=hr_ans-12;
            Time_formatted=(hr_ans+time_trim_ans+" PM");
        }else{
            Time_formatted=(hr_ans+time_trim_ans+" AM");
        }

        viewHolder[0].product_date_time.setText(Date_formatted+" "+Time_formatted);

        if (dataModel.getProduct_Measure_In().equals("کوئ نہیں") || dataModel.getProduct_Measure_In()=="کوئ نہیں"){
            viewHolder[0].product_price.setText(dataModel.getProduct_Price()+"/-Rs");
        }else
        {
            String price=dataModel.getProduct_Price()+"/-Rs      "+" قیمت "+dataModel.getProduct_Litre_Kilo()+" "+dataModel.getProduct_Measure_In()+" "+"کی";
            viewHolder[0].product_price.setText(price);
        }

        viewHolder[0].product_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_delete(v,dataModel.getProduct_Name(),dataModel.getProduct_ID());
            }
        });

        viewHolder[0].view_tafseel_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CustomerViewDetailsOFProductFragment();
                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

                Bundle bundle = new Bundle();

                bundle.putString("user_type", "domain"+"");
                bundle.putString("product_name", dataModel.getProduct_Name()+"");
                bundle.putString("product_id", dataModel.getProduct_ID()+"");
                bundle.putString("product_price", dataModel.getProduct_Price()+"");
                bundle.putString("product_detail", dataModel.getProduct_Detail()+"");
                bundle.putString("vendor_contact_no", dataModel.getVendor_Contact_No()+"");
                bundle.putString("product_measure_in", dataModel.getProduct_Measure_In()+"");
                bundle.putString("product_litre_kilo", dataModel.getProduct_Litre_Kilo()+"");
                bundle.putString("no_of_images", dataModel.getProduct_No_Images()+"");
                bundle.putStringArrayList("images_list",dataModel.getImages_List());

                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });


        viewHolder[0].view_edit_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), ""+dataModel.getImages_List(), Toast.LENGTH_LONG).show();
                Fragment fragment = new DomainUpdateProductFragment();
                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

                Bundle bundle = new Bundle();

                bundle.putString("product_name", dataModel.getProduct_Name()+"");
                bundle.putString("product_id", dataModel.getProduct_ID()+"");
                bundle.putString("product_price", dataModel.getProduct_Price()+"");
                bundle.putString("product_detail", dataModel.getProduct_Detail()+"");
                bundle.putString("vendor_contact", dataModel.getVendor_Contact_No()+"");
                bundle.putString("product_measure_in", dataModel.getProduct_Measure_In()+"");
                bundle.putString("product_litre_kilo", dataModel.getProduct_Litre_Kilo()+"");
                bundle.putString("no_of_images", dataModel.getProduct_No_Images()+"");
                bundle.putStringArrayList("images_list",dataModel.getImages_List());

                bundle.putString("product_type", dataModel.getProduct_Type()+"");

                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        String answer=dataModel.getAnswer();
        //Toast.makeText(getContext(), "Check "+answer, Toast.LENGTH_LONG).show();


        return convertView;
    }
    public void popup_window_delete(View v,String product_name,String ID){
        domainMainActivity=(DomainMainActivity)  getContext();
        domainMainActivity.drawerLayout_alpha.setAlpha(0.1f);

        TextView delete_product_info;
        ImageView btn_close;
        Button btn_no;
        Button btn_yes;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_delete_product_member_vendor, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        domainMainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float s_height = displayMetrics.heightPixels;
        float s_width = displayMetrics.widthPixels;


        s_height=s_height*0.4f;
        s_width=s_width*0.8f;

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, Math.round(s_width), Math.round(s_height));

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(40);
        }
        btn_no=popupView.findViewById(R.id.btn_popup_logout_no);
        btn_close = popupView.findViewById(R.id.close_btn_popup_logout);
        btn_yes = popupView.findViewById(R.id.btn_popup_logout_yes);
        delete_product_info = popupView.findViewById(R.id.delete_product_info);
        delete_product_info.setText("( "+product_name+" )");
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    popupWindow.dismiss();
                    domainMainActivity.drawerLayout_alpha.setAlpha(1f);
                }catch (Exception e){

                }

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                domainMainActivity.drawerLayout_alpha.setAlpha(1f);

            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domainMainActivity.drawerLayout_alpha.setAlpha(1f);
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Domain_Products").child(ID);
                reference.removeValue();
                Toast.makeText(getContext(), "آپکی پرا ڈکٹ ڈیلیٹ ہو چکی ہے", Toast.LENGTH_LONG).show();
//                domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_Fourth);
//                domainMainActivity.fragNavController.switchTab(domainMainActivity.TAB_FIRST);
//                domainMainActivity.function_call_view_inventory_domain_vendor();
                popupWindow.dismiss();
            }
        });
//        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                domainMainActivity.drawerLayout_alpha.setAlpha(1f);
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                domainMainActivity.drawerLayout_alpha.setAlpha(1f);
                popupWindow.dismiss();
            }
        });
    }

    ImageView button_close;
    ImageView image_popup;
    //////POPUP WINDOW FOR SINGLE IMAGE
    public void popup_winodw(String url_image, View v){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = null;
        if (inflater != null) {
            popupView = inflater.inflate(R.layout.popup, null);
        }

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        if (popupView != null) {
            image_popup=popupView.findViewById(R.id.image_popup);
        }
        ////.placeholder( R.drawable.loading_4 )
        Glide.with(getContext())
                .load(url_image)
                .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                .into(image_popup);
//        Picasso.get().load(url_image).placeholder( R.drawable.loading_4 ).into(image_popup);

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



