package agri_asan.com.agriasan06_12_19;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Belal on 1/23/2018.
 */

//////Toast.makeText(getApplicationContext(), "مہربانی  پہلے خود کو رجسٹر کروایں!", Toast.LENGTH_LONG).show();

public class OpenCartFragment extends Fragment {
    @Nullable

    ArrayList<String> cart_product;
    ArrayList<String> cart_product_quantity;
    int counter=0;
    String user="";


    String User="";
    String User_Phone="";
    String User_ID;
    String User_type="";

    Button btn_check_out;

    TextView textview_total_price;
    int total_price=0;


    PopupWindow popupWindow;

    DatabaseReference reference;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomerViewProductsCustomAdapter adapter;


    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    View root;

    String formattedDate;
    String formattedTime;

    int i=0;
    int j=0;
    String id;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_open_cart, container, false);

        if (getArguments() != null) {
            user = getArguments().getString("user");
        }
        textview_total_price=root.findViewById(R.id.textview_total_price);

        btn_check_out=root.findViewById(R.id.btn_check_out);

        btn_check_out.setVisibility(View.GONE);
        cart_product=new ArrayList<>();
        cart_product_quantity=new ArrayList<>();


        if (user.equals("member")){
            mainActivity = (MainActivity) getActivity();
            User="Members";
            User_Phone=mainActivity.Phone;
            User_ID=mainActivity.ID;
            User_type="member";
        }else if(user.equals("domain")){
            domainMainActivity = (DomainMainActivity) getActivity();
            User="Domain_experts";
            User_Phone=domainMainActivity.Phone;
            User_ID=domainMainActivity.ID;
            User_type="domain";
        }

        reference = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) root.findViewById(R.id.list);
        dataModels = new ArrayList<>();

//.child("Phone").equalTo(mainActivity.Phone);
        //.child("Phone").equalTo(mainActivity.Phone)
        final Query query = reference.child(User+"").orderByChild("Phone").equalTo(User_Phone+"");
                //.orderByValue().equalTo(""+mainActivity.ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(), "Test", Toast.LENGTH_LONG).show();

                total_price=0;
                textview_total_price.setText("Rs. "+total_price+"");
                cart_product=new ArrayList<>();
                cart_product_quantity=new ArrayList<>();

                btn_check_out.setVisibility(View.GONE);
//                dataModels=new ArrayList<>();
//                adapter = new CustomerViewProductsCustomAdapter(dataModels, root.getContext());
//                listView.setAdapter(adapter);
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
                                        String temp_quantity=cart_product_quantity.get(i)+"";
                                        final Query query_single_product=reference;
                                        query_single_product.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                total_price=0;
                                                textview_total_price.setText("Rs. "+total_price+"");
                                                dataModels=new ArrayList<>();
                                                adapter = new CustomerViewProductsCustomAdapter(dataModels, root.getContext());
                                                listView.setAdapter(adapter);
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot issue3 : dataSnapshot.child("Member_Products").getChildren()){
//                                                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                                        fetch_data_product_issue_function("member",issue3,temp_quantity+"",User_type+"");
                                                    }
                                                    for (DataSnapshot issue3 : dataSnapshot.child("Domain_Products").getChildren()){
//                                                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                                        fetch_data_product_issue_function("domain",issue3,temp_quantity+"",User_type+"");
                                                    }
                                                    adapter = new CustomerViewProductsCustomAdapter(dataModels, root.getContext());
                                                    listView.setAdapter(adapter);
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

        btn_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CheckOutFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("user",User_type+"");
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

//        listView.setAdapter(adapter);
        return root;
    }

    //////////////FUNCTIONS OF ADD QUESTION LAST PAGE

    public void fetch_data_product_issue_function(String vendor_type,DataSnapshot issue, String temp_quantity,String user_type){
        Vendor product = issue.getValue(Vendor.class);
        String Product_Type="";
        if (!TextUtils.isEmpty(product.getProduct_Type())) {
            Product_Type=product.getProduct_Type();
        }
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
            temp_quantity=cart_product_quantity.get(coun)+"";

            btn_check_out.setVisibility(View.VISIBLE);
            total_price=total_price+(Integer.parseInt(temp_quantity)*Integer.parseInt(Product_Price));
            dataModels.add(new DataModel(Product_Name, Product_Price, Product_Detail,Product_Vendor_contact_no,
                    Product_Date, Product_Time, Product_Image_0, Product_ID,
                    Product_Measure_In, Product_Litre_Kilo, product.getTotal_Images(),
                    images_list, Verification,User_type,"cart",temp_quantity,User_ID+"",vendor_type));

            textview_total_price.setText("Rs. "+total_price+"");
        }

        images_list = new ArrayList<>();

        Product_Total_images=0;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (User_type.equals("member")){
            mainActivity.mTitle.setText(R.string.cart);
            mainActivity.layout_cart.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.v1_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            mainActivity.v1_down.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            domainMainActivity.mTitle.setText(R.string.cart);
            domainMainActivity.layout_cart.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.v1_upper.setBackgroundColor(getResources().getColor(R.color.yellow));
            domainMainActivity.v1_down.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
    }
    @Override
    public void onPause() {
        if (User_type.equals("member")){
            mainActivity.layout_cart.getBackground().setAlpha(0);
            mainActivity.v1_upper.getBackground().setAlpha(0);
            mainActivity.v1_down.getBackground().setAlpha(0);
        }else{
            domainMainActivity.layout_cart.getBackground().setAlpha(0);
            domainMainActivity.v1_upper.getBackground().setAlpha(0);
            domainMainActivity.v1_down.getBackground().setAlpha(0);
        }
        super.onPause();

    }
}

