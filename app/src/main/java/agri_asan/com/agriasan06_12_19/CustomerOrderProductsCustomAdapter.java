package agri_asan.com.agriasan06_12_19;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomerOrderProductsCustomAdapter extends ArrayAdapter<DataModel> {

    MainActivity mainActivity;

    DomainMainActivity domainMainActivity;

    ///FOR FIREBASE REFERENCE
    DatabaseReference reference;
    ////FOR FIREBASE REFERENCE END

    private ArrayList<DataModel> dataSet;
    Context mContext;
    DomainMainActivity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView textview_name_of_product;
        TextView textview_product_price;
        TextView textview_quantity;
        TextView textview_total_price;
        TextView textview_vendor_contact;
        TextView textview_order_status;
        TextView textview_order_date;
        TextView textview_order_time;
        TextView textview_receiver_name;
        TextView textview_receiver_phn;
        TextView textview_receiver_address;

        LinearLayout layout_call_vendor;

        ImageView main_image_of_product;

        LinearLayout layout_main_parent;
        ViewGroup.LayoutParams params;

        DatabaseReference databaseReference;

        String User="";

    }
    public CustomerOrderProductsCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.customer_order_products_row_item, data);
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
            convertView = inflater.inflate(R.layout.customer_order_products_row_item, parent, false);

              viewHolder[0].layout_main_parent=convertView.findViewById(R.id.layout_main_parent);
              viewHolder[0].textview_name_of_product=convertView.findViewById(R.id.textview_name_of_product);
              viewHolder[0].textview_product_price=convertView.findViewById(R.id.textview_product_price);
              viewHolder[0].main_image_of_product=convertView.findViewById(R.id.main_image_of_product);
              viewHolder[0].textview_quantity=convertView.findViewById(R.id.textview_quantity);
              viewHolder[0].textview_total_price=convertView.findViewById(R.id.textview_total_price);
              viewHolder[0].textview_vendor_contact=convertView.findViewById(R.id.textview_vendor_contact);
              viewHolder[0].layout_call_vendor=convertView.findViewById(R.id.layout_call_vendor);
              viewHolder[0].textview_order_status=convertView.findViewById(R.id.textview_order_status);
              viewHolder[0].textview_order_date=convertView.findViewById(R.id.textview_order_date);
              viewHolder[0].textview_order_time=convertView.findViewById(R.id.textview_order_time);
              viewHolder[0].textview_receiver_name=convertView.findViewById(R.id.textview_receiver_name);
              viewHolder[0].textview_receiver_phn=convertView.findViewById(R.id.textview_receiver_phn);
              viewHolder[0].textview_receiver_address=convertView.findViewById(R.id.textview_receiver_address);

              result=convertView;
            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        if (dataModel != null) {
            viewHolder[0].textview_name_of_product.setText(dataModel.getProduct_Name());
            Glide.with(getContext())
                    .load(dataModel.getProduct_Image_0())
                    .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                    .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                    .into(viewHolder[0].main_image_of_product);
            viewHolder[0].textview_product_price.setText(dataModel.getProduct_Price());
            viewHolder[0].textview_quantity.setText("مقدار "+dataModel.getProduct_Quantity());
            viewHolder[0].textview_total_price.setText(
                    ""+dataModel.getProduct_Quantity()+" x "+dataModel.getProduct_Price()+" = "+dataModel.getPrice_Total()+"/-Rs");

            viewHolder[0].textview_order_status.setText(dataModel.getOrder_Status());
            viewHolder[0].textview_vendor_contact.setText(dataModel.getVendor_Contact_No());
            viewHolder[0].textview_receiver_name.setText(dataModel.getReceiver_Name());
            viewHolder[0].textview_receiver_phn.setText("0"+dataModel.getReceiver_Phone());
            viewHolder[0].textview_receiver_address.setText(dataModel.getReceiver_Address());
            viewHolder[0].textview_receiver_address.setMovementMethod(new ScrollingMovementMethod());

            String Date_formatted=dataModel.getOrder_Date().substring(0,6)+dataModel.getOrder_Date().substring(8);
            viewHolder[0].textview_order_date.setText(Date_formatted);

            String Time_formatted;

            String time_trim_ans=dataModel.getOrder_Time().substring(2,5);
            int hr_ans= Integer.parseInt(dataModel.getOrder_Time().substring(0,2));
            if (hr_ans>12){
                hr_ans=hr_ans-12;
                Time_formatted=(hr_ans+time_trim_ans+" PM");
            }else{
                Time_formatted=(hr_ans+time_trim_ans+" AM");
            }

            viewHolder[0].textview_order_time.setText(Time_formatted);

            viewHolder[0].main_image_of_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup_winodw(dataModel.getProduct_Image_0(),v);
                }
            });

            viewHolder[0].layout_call_vendor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ""+dataModel.getVendor_Contact_No(), null)));
                }
            });
            if (dataModel.getProduct_Measure_In().equals("کوئ نہیں") || dataModel.getProduct_Measure_In()=="کوئ نہیں"){
                viewHolder[0].textview_product_price.setText(dataModel.getProduct_Price()+"/-Rs");
            }else
            {
                String price=dataModel.getProduct_Price()+"/-Rs      "+" قیمت "+dataModel.getProduct_Litre_Kilo()+" "+dataModel.getProduct_Measure_In()+" "+"کی";
                viewHolder[0].textview_product_price.setText(price);
            }
        }
        viewHolder[0].layout_main_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), ""+dataModel.getImages_List(), Toast.LENGTH_LONG).show();
                Fragment fragment = new CustomerViewDetailsOFProductFragment();
                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                if (dataModel != null) {
                    bundle.putString("vendor_type", dataModel.getProduct_Vendor_Type()+"");
                    bundle.putString("user_type", dataModel.getCustomer_Type()+"");
                    bundle.putString("product_name", dataModel.getProduct_Name()+"");
                    bundle.putString("product_id", dataModel.getProduct_ID());
                    bundle.putString("product_price", dataModel.getProduct_Price()+"");
                    bundle.putString("product_detail", dataModel.getProduct_Detail()+"");
                    bundle.putString("vendor_contact_no", dataModel.getVendor_Contact_No()+"");
                    bundle.putString("product_measure_in", dataModel.getProduct_Measure_In()+"");
                    bundle.putString("product_litre_kilo", dataModel.getProduct_Litre_Kilo()+"");
                    bundle.putString("no_of_images", dataModel.getProduct_No_Images()+"");
                    bundle.putStringArrayList("images_list",dataModel.getImages_List());
                }
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

        return convertView;
    }


    ImageView image_popup;

    //////POPUP WINDOW FOR SINGLE IMAGE
    private void popup_winodw(String url_image, View v){
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

        ImageView button_close = null;
        if (popupView != null) {
            button_close = popupView.findViewById(R.id.close_btn_popup);
        }
        if (button_close != null) {
            button_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        if (popupView != null) {
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
    //                        popupWindow.dismiss();
                    return true;
                }
            });
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
    }
    /////POPUP WINDOW END
}



