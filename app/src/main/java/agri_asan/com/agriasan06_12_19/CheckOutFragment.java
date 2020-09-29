package agri_asan.com.agriasan06_12_19;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CheckOutFragment extends Fragment {
    @Nullable

    DatabaseReference databaseReference;
    ArrayList<String> final_products_id, final_product_vendor_no, final_product_vendor_type
    , final_product_total_price, final_product_single_price, final_product_quantity
            , final_vendor_original_phone;

    private Firebase mRootref;

    EditText edittext_receiver_name, edittext_receiver_phone, edittext_receiver_address;

    Button btn_order;
    TextView textview_total_price;

    ArrayList<String> cart_product, cart_product_quantity;
    String user_type="";

    String User="",
     User_Phone="",
     User_ID="",
     User_Name="";
    int total_price=0;
    DatabaseReference reference;
    ArrayList<DataModel> dataModels;
    ListView list_products_to_purchase;
    private static CustomerCheckOutProductsCustomAdapter adapter;

    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    View root;

    String formattedDate, formattedTime;

    int i=0;
    String path_to_image="";
    UploadTask uploadTask;
    int j=0;
    String id;
    String key_id;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_check_out, container, false);

        final_products_id=new ArrayList<>();
        final_product_vendor_no=new ArrayList<>();
        final_product_vendor_type=new ArrayList<>();
        final_product_total_price=new ArrayList<>();
        final_product_single_price=new ArrayList<>();
        final_product_quantity=new ArrayList<>();
        final_vendor_original_phone=new ArrayList<>();

        edittext_receiver_name=root.findViewById(R.id.edittext_receiver_name);
        edittext_receiver_phone=root.findViewById(R.id.edittext_receiver_phone);
        edittext_receiver_address=root.findViewById(R.id.edittext_receiver_address);

        textview_total_price=root.findViewById(R.id.textview_total_price);
        btn_order=root.findViewById(R.id.btn_order);
        if (getArguments() != null) {
            user_type = getArguments().getString("user");
        }

        cart_product=new ArrayList<>();
        cart_product_quantity=new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        list_products_to_purchase = (ListView) root.findViewById(R.id.list_products_to_purchase);
        dataModels = new ArrayList<>();

        if (user_type.equals("member")){
            mainActivity = (MainActivity) getActivity();
            User="Members";
            User_Phone=mainActivity.Phone;
            User_ID=mainActivity.ID;
            User_Name=mainActivity.Name;

        }else if(user_type.equals("domain")){
            domainMainActivity = (DomainMainActivity) getActivity();
            User="Domain_experts";
            User_Phone=domainMainActivity.Phone;
            User_ID=domainMainActivity.ID;
            User_Name=domainMainActivity.Name;
        }

        edittext_receiver_name.setText(""+User_Name);
        edittext_receiver_phone.setText("0"+User_Phone);

        final Query query = reference.child(User+"").orderByChild("Phone").equalTo(User_Phone+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_price=0;
                textview_total_price.setText("Rs. "+total_price+"");
                cart_product=new ArrayList<>();
                cart_product_quantity=new ArrayList<>();
                btn_order.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        DatabaseReference cart=issue.child("Cart").getRef();
                        final Query query1=cart;
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot issue2 : dataSnapshot.getChildren()) {
                                        Cart cart = issue2.getValue(Cart.class);
                                        cart_product.add(""+cart.getID());
                                        cart_product_quantity.add(""+cart.getQuantity());
                                    }
                                    i=0;
                                    String temp_quantity=cart_product_quantity.get(i);
                                    final Query query_single_product=reference;
                                    query_single_product.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            total_price=0;
                                            textview_total_price.setText("Rs. "+total_price+"");
                                            dataModels=new ArrayList<>();
                                            adapter = new CustomerCheckOutProductsCustomAdapter(dataModels, root.getContext());
                                            list_products_to_purchase.setAdapter(adapter);
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot issue3 : dataSnapshot.child("Member_Products").getChildren()){
//                                                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                                    fetch_data_product_issue_function(issue3,temp_quantity,"member");
                                                }
                                                for (DataSnapshot issue3 : dataSnapshot.child("Domain_Products").getChildren()){
//                                                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                                    fetch_data_product_issue_function(issue3,temp_quantity,"domain");
                                                }
                                                adapter = new CustomerCheckOutProductsCustomAdapter(dataModels, root.getContext());
                                                list_products_to_purchase.setAdapter(adapter);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edittext_receiver_name.getText())){
                    edittext_receiver_name.setError("پہلے وصول کرنے والے کا نام لکھیں!");
                    Toast.makeText(getActivity(), "پہلے وصول کرنے والے کا نام لکھیں!", Toast.LENGTH_LONG).show();

                }else{
                    if (TextUtils.isEmpty(edittext_receiver_address.getText())){
                        edittext_receiver_address.setError("پہلے وصول کرنے والے کا پتہ لکھیں!");
                        Toast.makeText(getActivity(), "پہلے وصول کرنے والے کا پتہ لکھیں!", Toast.LENGTH_LONG).show();

                    }else {
                        if (TextUtils.isEmpty(edittext_receiver_phone.getText())){
                            edittext_receiver_phone.setError("پہلے وصول کرنے والے کا نمبر لکھیں!");
                            Toast.makeText(getActivity(), "پہلے وصول کرنے والے کا نمبر لکھیں!", Toast.LENGTH_LONG).show();
                        }else{
                            try {
                            popup_window_show_info(v);
                                place_order_function();
                            }catch (Exception e){
                            }
                        }
                    }
                }
            }
        });
        return root;
    }
        public void place_order_function(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        for (int j=0;j<final_products_id.size();j++){
            mRootref.setAndroidContext(getActivity());
            mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Orders");

            key_id = mRootref.push().getKey();

            databaseReference.child("Orders").child(key_id).child("Order_ID")
                    .setValue(key_id);
            databaseReference.child("Orders").child(key_id).child("Product_ID")
                    .setValue(final_products_id.get(j));
            databaseReference.child("Orders").child(key_id).child("Product_Vendor_Type")
                    .setValue(final_product_vendor_type.get(j));
            String temp=final_product_vendor_no.get(j);
            databaseReference.child("Orders").child(key_id).child("Product_Vendor_Phone")
                    .setValue(temp.substring(1));
            databaseReference.child("Orders").child(key_id).child("Customer_ID")
                    .setValue(User_ID);
            databaseReference.child("Orders").child(key_id).child("Customer_Type")
                    .setValue(user_type);
            databaseReference.child("Orders").child(key_id).child("Customer_Phone")
                    .setValue(User_Phone);
            databaseReference.child("Orders").child(key_id).child("Customer_Name")
                    .setValue(User_Name);
            databaseReference.child("Orders").child(key_id).child("Order_Status")
                    .setValue("زیر غور");
            databaseReference.child("Orders").child(key_id).child("Price_Total")
                    .setValue(final_product_total_price.get(j));
            databaseReference.child("Orders").child(key_id).child("Order_Date")
                    .setValue(""+formattedDate);
            databaseReference.child("Orders").child(key_id).child("Order_Time")
                    .setValue(""+formattedTime);
            databaseReference.child("Orders").child(key_id).child("Product_Price")
                    .setValue(final_product_single_price.get(j));
            databaseReference.child("Orders").child(key_id).child("Product_Quantity")
                    .setValue(final_product_quantity.get(j));
            databaseReference.child("Orders").child(key_id).child("Vendor_Original_Phone")
                    .setValue(final_vendor_original_phone.get(j));
            databaseReference.child("Orders").child(key_id).child("Receiver_Name")
                    .setValue(edittext_receiver_name.getText().toString());
            databaseReference.child("Orders").child(key_id).child("Receiver_Address")
                    .setValue(edittext_receiver_address.getText().toString());
            String receiver_phn = edittext_receiver_phone.getText().toString();
            databaseReference.child("Orders").child(key_id).child("Receiver_Phone")
                    .setValue(receiver_phn.substring(1));
        }
            Toast.makeText(getActivity(), "آپکا اورڈر وصول کر لیا گیا ہے شکریہ!", Toast.LENGTH_LONG).show();
    }
    public void popup_window_show_info(View v) {
        LayoutInflater inflater;
        if (user_type.equals("member")){
            mainActivity.drawerLayout_alpha.setAlpha(0.2f);
             inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        }else{
            domainMainActivity.drawerLayout_alpha.setAlpha(0.2f);
             inflater = (LayoutInflater) domainMainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        View popupView = inflater.inflate(R.layout.popup_info, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float s_height = displayMetrics.heightPixels;
        float s_width = displayMetrics.widthPixels;
        s_height = s_height * 0.4f;
        s_width = s_width * 0.8f;
        // create the popup window
        PopupWindow popupWindow_info;
        popupWindow_info = new PopupWindow(popupView, Math.round(s_width), Math.round(s_height));
        Button btn_popup_thank_you;
        btn_popup_thank_you=popupView.findViewById(R.id.btn_popup_thank_you);
        popupWindow_info.setOutsideTouchable(true);
        popupWindow_info.setFocusable(true);
        popupWindow_info.setTouchable(true);
        popupWindow_info.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btn_popup_thank_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    popupWindow_info.dismiss();
                }catch (Exception e){
                }
            }
        });
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    popupWindow_info.dismiss();
                }catch (Exception e){
                }
                return false;
            }
        });
        popupWindow_info.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                try {
                    popupWindow_info.dismiss();
                }catch (Exception e){
                }
                if (user_type.equals("member")){
                    mainActivity.drawerLayout_alpha.setAlpha(1f);
                }else{
                    domainMainActivity.drawerLayout_alpha.setAlpha(1f);
                }
                if (user_type.equals("member")){
                    mainActivity.onBackPressed();
                    mainActivity.onBackPressed();
                }else{
                    domainMainActivity.onBackPressed();
                    domainMainActivity.onBackPressed();
                }
            }
        });
        popupWindow_info.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
    public void fetch_data_product_issue_function(DataSnapshot issue, String temp_quantity,String user_type){
        Vendor product = issue.getValue(Vendor.class);
        String Product_Type="";
        if (!TextUtils.isEmpty(product.getProduct_Type())) {
            Product_Type=product.getProduct_Type();
        }
        String Vendor_Original_Phone=product.getPhone();
        String Product_Name = product.getProduct_Name();
        String Product_Price = product.getProduct_Price();
        String Product_Detail = product.getProduct_Detail();
        String Product_Vendor_contact_no = product.getVendor_Contact_No();
        String Product_Date = product.getProduct_Date();
        String Product_Time = product.getProduct_Time();
        String Product_Image_0 = product.getImage_0();
        String Product_ID = product.getID();
        String Product_Measure_In=product.getMeasure_In();
        String Product_Litre_Kilo=product.getLitre_Kilo();
        String Verification;
        if (!TextUtils.isEmpty(product.getVerification())) {
            Verification = product.getVerification();
        }else{
            Verification="0";
        }
        int Product_Total_images;
        if (!TextUtils.isEmpty(product.getTotal_Images()) && TextUtils.isDigitsOnly(product.getTotal_Images())) {
            Product_Total_images = Integer.parseInt(product.getTotal_Images());
        }else{
            Product_Total_images=0;
        }
        ArrayList<String> images_list=new ArrayList<>();
        images_list.add(Product_Image_0);
        if (Product_Total_images==2){
            images_list.add(product.getImage_1()); }if (Product_Total_images==3){
            images_list.add(product.getImage_1());
            images_list.add(product.getImage_2()); }if (Product_Total_images==4){
            images_list.add(product.getImage_1());
            images_list.add(product.getImage_2());
            images_list.add(product.getImage_3()); }if (Product_Total_images==5){
            images_list.add(product.getImage_1());
            images_list.add(product.getImage_2());
            images_list.add(product.getImage_3());
            images_list.add(product.getImage_4()); }if (Product_Total_images==6){
            images_list.add(product.getImage_1());
            images_list.add(product.getImage_2());
            images_list.add(product.getImage_3());
            images_list.add(product.getImage_4());
            images_list.add(product.getImage_5());
        }
        if (cart_product.contains(Product_ID)){
            int coun=cart_product.indexOf(Product_ID);
            temp_quantity=cart_product_quantity.get(coun);

            btn_order.setVisibility(View.VISIBLE);
            total_price=total_price+(Integer.parseInt(temp_quantity)*Integer.parseInt(Product_Price));
            int prod_final_price=Integer.parseInt(temp_quantity)*Integer.parseInt(Product_Price);
            final_products_id.add(Product_ID);
            final_product_vendor_no.add(Product_Vendor_contact_no);
            final_product_vendor_type.add(""+user_type);
            final_product_total_price.add(""+prod_final_price);
            final_product_single_price.add(""+Product_Price);
            final_vendor_original_phone.add(""+Vendor_Original_Phone);
            final_product_quantity.add(""+Integer.parseInt(temp_quantity));

            dataModels.add(new DataModel(Product_Name, Product_Price, Product_Detail,Product_Vendor_contact_no,
                    Product_Date, Product_Time, Product_Image_0, Product_ID,
                    Product_Measure_In, Product_Litre_Kilo, product.getTotal_Images(),
                    images_list, Verification,user_type,"cart",temp_quantity,User_ID+""));

            textview_total_price.setText("Rs. "+total_price+"");
        }
        images_list = new ArrayList<>();
        Product_Total_images=0;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (user_type.equals("member")){
            mainActivity.mTitle.setText(R.string.last_time_check);
        }else{
            domainMainActivity.mTitle.setText(R.string.last_time_check);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}

