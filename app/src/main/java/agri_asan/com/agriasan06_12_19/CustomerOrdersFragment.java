package agri_asan.com.agriasan06_12_19;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomerOrdersFragment extends Fragment {
    @Nullable

    DatabaseReference databaseReference;
    ArrayList<String> order_products_id, order_product_vendor_no, order_product_vendor_type
    , order_product_total_price, order_product_single_price, order_product_quantity
    , order_date, order_time, order_status, order_total_price, order_receiver_address, order_receiver_name
    , order_receiver_phn, order_customer_type;

    ArrayList<String> cart_product, cart_product_quantity;
    String user_type="";

    String User="";
    String User_Products="";
    String User_Phone="";
    String User_ID="";
    String User_Name="";


    DatabaseReference reference;
    DatabaseReference reference_2;

    ArrayList<DataModel> dataModels;
    ListView list_order_products;
    private static CustomerOrderProductsCustomAdapter adapter;

    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    View root;
    String formattedDate;
    String formattedTime;

    int i=0;
    int j=0;
    String id;

    String customer_type="";
    String date="";
    String status="";
    String time="";
    String quantity="";
    String p_vendor_type="";
    String total_price="";
    String p_id="";
    String single_price="";
    String vendor_no="";
    String address="";
    String receiver_phn="";
    String name;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_customer_orders, container, false);

        order_products_id=new ArrayList<>();
        order_product_vendor_no=new ArrayList<>();
        order_product_vendor_type=new ArrayList<>();
        order_product_total_price=new ArrayList<>();
        order_product_single_price=new ArrayList<>();
        order_product_quantity=new ArrayList<>();
        order_date=new ArrayList<>();
        order_time=new ArrayList<>();
        order_status=new ArrayList<>();
        order_total_price=new ArrayList<>();
        order_receiver_address=new ArrayList<>();
        order_receiver_name=new ArrayList<>();
        order_receiver_phn=new ArrayList<>();
        order_customer_type=new ArrayList<>();

        if (getArguments() != null) {
            user_type = getArguments().getString("user");
        }

        cart_product=new ArrayList<>();
        cart_product_quantity=new ArrayList<>();

        String t="";

        reference = FirebaseDatabase.getInstance().getReference();
        reference_2 = FirebaseDatabase.getInstance().getReference();

        list_order_products = (ListView) root.findViewById(R.id.list_order_products);
        dataModels = new ArrayList<>();

        if (user_type.equals("member")){
            mainActivity = (MainActivity) getActivity();
            User="Members";
            User_Phone=mainActivity.Phone;
            User_ID=mainActivity.ID;
            User_Name=mainActivity.Name;
            User_Products="Member_Products";

        }else if(user_type.equals("domain")){
            domainMainActivity = (DomainMainActivity) getActivity();
            User="Domain_experts";
            if (domainMainActivity != null) {
                User_Phone=domainMainActivity.Phone;
                User_ID=domainMainActivity.ID;
                User_Name=domainMainActivity.Name;
                User_Products="Domain_Products";

            }
        }
        reference = FirebaseDatabase.getInstance().getReference();
        final Query query=reference.child("Orders").orderByChild("Customer_Phone").equalTo(User_Phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModels=new ArrayList<>();
                adapter = new CustomerOrderProductsCustomAdapter(dataModels, root.getContext());
                list_order_products.setAdapter(adapter);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Order product= issue.getValue(Order.class);

                        order_customer_type.add(product.getCustomer_Type());
                        customer_type=product.getCustomer_Type();
                        order_date.add(product.getOrder_Date());
                        date=product.getOrder_Date();
                        order_status.add(product.getOrder_Status());
                        status=product.getOrder_Status();
                        order_time.add(product.getOrder_Time());
                        time=product.getOrder_Time();
                        order_total_price.add(product.getPrice_Total());
                        total_price=product.getPrice_Total();
                        order_products_id.add(product.getProduct_ID()+"");
                        p_id=product.getProduct_ID();
                        order_product_single_price.add(product.getProduct_Price());
                        single_price=product.getProduct_Price();
                        order_product_quantity.add(product.getProduct_Quantity());
                        quantity=product.getProduct_Quantity();
                        order_product_vendor_no.add(product.getProduct_Vendor_Phone());
                        vendor_no=product.getProduct_Vendor_Phone();
                        order_product_vendor_type.add(product.getProduct_Vendor_Type());
                        p_vendor_type=product.getProduct_Vendor_Type();
                        order_receiver_address.add(product.getReceiver_Address());
                        address=product.getReceiver_Address();
                        order_receiver_name.add(product.getReceiver_Name());
                        name=product.getReceiver_Name();
                        order_receiver_phn.add(product.getReceiver_Phone());
                        receiver_phn=product.getReceiver_Phone();
//                        dataModels.add(new DataModel(customer_type,date,status,time,//total_price,p_id,quantity//,p_vendor_type,address,//name,receiver_phn));
                        fun(customer_type,  date,  status,  time,  total_price,  p_id,  single_price
                                ,  quantity,  vendor_no,  p_vendor_type,   address,   name,   receiver_phn); } } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
        return root; }
    private void fun(String customer_type,String date,String status,String time,String total_price,String p_id,String single_price
            ,String quantity,String vendor_no,String p_vendor_type, String address, String name, String receiver_phn){
//            Toast.makeText(getActivity(), ""+quantity, Toast.LENGTH_LONG).show();
            final Query query_2=reference_2.child("Member_Products").orderByChild("ID")
                    .equalTo(p_id);
            query_2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                    if (dataSnapshot1.exists()) {
                        for (DataSnapshot issue3 : dataSnapshot1.getChildren()){
                            fetch_data_product_issue_function(issue3,user_type+"",
                                      customer_type,  date,  status,  time,  total_price,  p_id,  single_price
                                    ,  quantity,  vendor_no,  p_vendor_type,   address,   name,   receiver_phn);
                        }
                        Collections.reverse(dataModels);
                        adapter = new CustomerOrderProductsCustomAdapter(dataModels, root.getContext());
                        list_order_products.setAdapter(adapter); } }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            final Query query_3=reference_2.child("Domain_Products").orderByChild("ID")
                    .equalTo(p_id);
            query_3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                    if (dataSnapshot1.exists()) {
                        for (DataSnapshot issue3 : dataSnapshot1.getChildren()){
                            fetch_data_product_issue_function(issue3,user_type+"",
                                      customer_type,  date,  status,  time,  total_price,  p_id,  single_price
                                    ,  quantity,  vendor_no,  p_vendor_type,   address,   name,   receiver_phn);
                        }
                        Collections.reverse(dataModels);
                        adapter = new CustomerOrderProductsCustomAdapter(dataModels, root.getContext());
                        list_order_products.setAdapter(adapter); } }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
    }
    private void fetch_data_product_issue_function(DataSnapshot issue, String user_type,
         String customer_type,String date,String status,String time,String total_price,String p_id,String single_price
         ,String quantity,String vendor_no,
         String p_vendor_type, String address, String name, String receiver_phn){
        Vendor product = issue.getValue(Vendor.class);
        String Product_Type="";
        if (product != null && !TextUtils.isEmpty(product.getProduct_Type())) {
            Product_Type = product.getProduct_Type();
        }
        String Product_Name = "";
        if (product != null) {
            Product_Name = product.getProduct_Name();
        }
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

            dataModels.add(new DataModel(
                    customer_type,date,status,time,
                                total_price,p_id,quantity
                                ,p_vendor_type,address,
                                name,receiver_phn

                    ,Product_Name, Product_Price, Product_Detail,Product_Vendor_contact_no,
                    Product_Date, Product_Time, Product_Image_0, Product_ID,
                    Product_Measure_In, Product_Litre_Kilo, product.getTotal_Images(),
                    images_list, Verification, user_type,"cart","",User_ID+""));
        images_list = new ArrayList<>();
        Product_Total_images=0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user_type.equals("member")){
            mainActivity.mTitle.setText(R.string.your_orders);
            mainActivity.layout_orders.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.v2_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.v2_down.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            domainMainActivity.mTitle.setText(R.string.your_orders);
            domainMainActivity.layout_orders.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.v2_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.v2_down.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
    }
    @Override
    public void onPause() {
        if (user_type.equals("member")){
            mainActivity.layout_orders.getBackground().setAlpha(0);
            mainActivity.v2_upper.getBackground().setAlpha(0);
            mainActivity.v2_down.getBackground().setAlpha(0);
        }else{
            domainMainActivity.layout_orders.getBackground().setAlpha(0);
            domainMainActivity.v2_upper.getBackground().setAlpha(0);
            domainMainActivity.v2_down.getBackground().setAlpha(0);
        }
        super.onPause();

    }
}

