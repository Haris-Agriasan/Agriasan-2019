package agri_asan.com.agriasan06_12_19;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static agri_asan.com.agriasan06_12_19.R.drawable.ic_dashboard_black_24dp;
import static agri_asan.com.agriasan06_12_19.R.drawable.loading_4;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.mikepenz.iconics.Iconics.getApplicationContext;
import com.shawnlin.numberpicker.NumberPicker;
public class CustomerViewProductsCustomAdapter extends ArrayAdapter<DataModel> {

    DatabaseReference reference_for_remove_product;

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
        TextView product_name;
        TextView product_price;
        TextView product_date_time;
        TextView product_vendor_contact;
        ImageView product_image_0;
        Button view_edit_product_details;

        ImageView btn_call_vendor;

        LinearLayout layout_quantity;
        View view_for_quantity;
        LinearLayout layout_main_parent;
        ViewGroup.LayoutParams params;
        View view_remove_product_cart;
        Button btn_remove_product_cart;

        NumberPicker quantity_picker;
        TextView textview_midaar_quantity;

        DatabaseReference databaseReference;

        String User="";

    }
    public CustomerViewProductsCustomAdapter(ArrayList<DataModel> data, Context context) {
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
            convertView = inflater.inflate(R.layout.for_customer_view_products_row_item, parent, false);

              viewHolder[0].product_name =  convertView.findViewById(R.id.textview_name_of_product);
              viewHolder[0].product_price =  convertView.findViewById(R.id.textview_product_price);
              viewHolder[0].product_date_time =  convertView.findViewById(R.id.textview_time_date_product);
              viewHolder[0].product_vendor_contact =  convertView.findViewById(R.id.textview_vendor_contact_product);
              viewHolder[0].product_image_0 =  convertView.findViewById(R.id.main_image_of_product);
              viewHolder[0].view_edit_product_details =  convertView.findViewById(R.id.view_product_details);
              viewHolder[0].btn_call_vendor =  convertView.findViewById(R.id.btn_call_vendor);

              viewHolder[0].layout_quantity=convertView.findViewById(R.id.layout_quantity);
              viewHolder[0].view_for_quantity=convertView.findViewById(R.id.view_for_quantity);
              viewHolder[0].layout_main_parent=convertView.findViewById(R.id.layout_main_parent);
              viewHolder[0].view_remove_product_cart=convertView.findViewById(R.id.view_remove_product_cart);
              viewHolder[0].btn_remove_product_cart=convertView.findViewById(R.id.btn_remove_product_cart);

              viewHolder[0].quantity_picker=convertView.findViewById(R.id.quantity_picker);
              viewHolder[0].textview_midaar_quantity=convertView.findViewById(R.id.textview_midaar_quantity);
              result=convertView;
            convertView.setTag(viewHolder[0]);
        } else {
            viewHolder[0] = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder[0].product_name.setText(dataModel.getProduct_Name());
        ////NO DELETE IMAGE 0
        ///.submit(200,200).placeholder( loading_4 )
        Glide.with(getContext())
                .load(dataModel.getProduct_Image_0())
                .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                .into(viewHolder[0].product_image_0);
//        Picasso.get().load(dataModel.getProduct_Image_0()).resize(200,200).placeholder( loading_4 ).into(viewHolder[0].product_image_0);


        viewHolder[0].product_image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_winodw(dataModel.getProduct_Image_0(),v);
            }
        });
        String Date_formatted=dataModel.getProduct_Date().substring(0,6)+dataModel.getProduct_Date().substring(8);
        String vendor_contact=dataModel.getVendor_Contact_No();
//        String Time_formatted = dataModel.getProduct_Time().substring(0,5);


        String Time_formatted;


        String time_trim_ans=dataModel.getProduct_Time().substring(2,5);
        int hr_ans= Integer.parseInt(dataModel.getProduct_Time().substring(0,2));
        if (hr_ans>12){
            hr_ans=hr_ans-12;
            Time_formatted=(hr_ans+time_trim_ans+" PM");
        }else{
            Time_formatted=(hr_ans+time_trim_ans+" AM");
        }


        if (dataModel.getCart()!=null){
            if (dataModel.getCart().equals("cart")){

                if (dataModel.getUser_type().equals("member")){
                    viewHolder[0].User="Members";
                }else if (dataModel.getUser_type().equals("domain")){
                    viewHolder[0].User="Domain_experts";
                }

                viewHolder[0].databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child(viewHolder[0].User).child(dataModel.getMember_id());
//                Toast.makeText(getContext(), "CART", Toast.LENGTH_LONG).show();
                viewHolder[0].layout_quantity.setVisibility(View.VISIBLE);
                viewHolder[0].view_for_quantity.setVisibility(View.VISIBLE);
                viewHolder[0].view_remove_product_cart.setVisibility(View.VISIBLE);
                viewHolder[0].btn_remove_product_cart.setVisibility(View.VISIBLE);
                viewHolder[0].params=viewHolder[0].layout_main_parent.getLayoutParams();

                viewHolder[0].params.height = 300;
                viewHolder[0].layout_main_parent.setLayoutParams(viewHolder[0].params);

                viewHolder[0].quantity_picker.setValue(Integer.parseInt(dataModel.getCart_product_quantity()));

                viewHolder[0].quantity_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder[0].databaseReference.child("Cart").child(dataModel.getProduct_ID())
                                .child("Quantity").setValue(viewHolder[0].quantity_picker.getValue()+"");
                    }
                });
                viewHolder[0].quantity_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                        Toast.makeText(getContext(), ""+newVal, Toast.LENGTH_LONG).show();
                        viewHolder[0].databaseReference.child("Cart").child(dataModel.getProduct_ID())
                                .child("Quantity").setValue(newVal+"");
                    }
                });
//                viewHolder[0].quantity_picker.setAccessibilityDescriptionEnabled(false);
                viewHolder[0].quantity_picker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                    @Override
                    public void onScrollStateChange(NumberPicker picker, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {
                            viewHolder[0].databaseReference.child("Cart").child(dataModel.getProduct_ID())
                                    .child("Quantity").setValue(picker.getValue()+"");
                        }
                    }
                });
                viewHolder[0].btn_remove_product_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getContext(), ""+dataModel.getProduct_ID(), Toast.LENGTH_LONG).show();
                        reference_for_remove_product=FirebaseDatabase.getInstance().getReference()
                                .child(viewHolder[0].User).
                                        child(dataModel.getMember_id()).child("Cart").child(dataModel.getProduct_ID());
                        reference_for_remove_product.removeValue();
                        notifyDataSetChanged();
                    }
                });
            }
        }else{
            viewHolder[0].layout_quantity.setVisibility(View.GONE);
            viewHolder[0].view_for_quantity.setVisibility(View.GONE);
            viewHolder[0].view_remove_product_cart.setVisibility(View.GONE);
            viewHolder[0].btn_remove_product_cart.setVisibility(View.GONE);

            viewHolder[0].params=viewHolder[0].layout_main_parent.getLayoutParams();

            viewHolder[0].params.height = 230;
            viewHolder[0].layout_main_parent.setLayoutParams(viewHolder[0].params);
        }


        viewHolder[0].btn_call_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ""+vendor_contact, null)));
            }
        });


        viewHolder[0].product_date_time.setText(Date_formatted+" "+Time_formatted);
        viewHolder[0].product_vendor_contact.setText(vendor_contact);

        if (dataModel.getProduct_Measure_In().equals("کوئ نہیں") || dataModel.getProduct_Measure_In()=="کوئ نہیں"){
            viewHolder[0].product_price.setText(dataModel.getProduct_Price()+"/-Rs");
        }else
        {
            String price=dataModel.getProduct_Price()+"/-Rs      "+" قیمت "+dataModel.getProduct_Litre_Kilo()+" "+dataModel.getProduct_Measure_In()+" "+"کی";
            viewHolder[0].product_price.setText(price);
        }
        viewHolder[0].view_edit_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), ""+dataModel.getImages_List(), Toast.LENGTH_LONG).show();
                Fragment fragment = new CustomerViewDetailsOFProductFragment();
                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

                Bundle bundle = new Bundle();

                bundle.putString("vendor_type", dataModel.getProduct_Vendor_Type()+"");
                bundle.putString("user_type", dataModel.getUser_type()+"");
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



