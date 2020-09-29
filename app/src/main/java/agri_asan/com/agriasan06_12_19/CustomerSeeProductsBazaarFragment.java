package agri_asan.com.agriasan06_12_19;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CustomerSeeProductsBazaarFragment extends Fragment {
    Resources resources;
    ToggleImageButton current_location_toggle;
    Boolean check_location=false;

    DatabaseReference reference;
    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    ArrayList<DataModel> dataModels;
    ArrayList<DataModel> dataModels_domain;
    ListView listView;
    private static CustomerViewProductsCustomAdapter adapter;

    String type_of_product_selected="";

    Intent old_intent;

    public String old_user;

    Fragment current_frag;
    FragmentTransaction ft;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_customer_see_products_bazaar, container, false);

        old_user = getArguments().getString("user");
        if (old_user.equals("member")){
            mainActivity=(MainActivity)  getActivity();
        }
        if (old_user.equals("domain")){
            domainMainActivity=(DomainMainActivity)  getActivity();
        }
        current_frag = (Fragment) getFragmentManager().findFragmentByTag("CustomerSeeProductBazaarFragment");
        ft = getFragmentManager().beginTransaction();

        current_location_toggle=root.findViewById(R.id.current_location_toggle);
        current_location_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_location_toggle.isChecked()){
                    Toast.makeText(getActivity(), "آپکے اردگرد کی عوا می رائے!", Toast.LENGTH_LONG).show();
                    if (old_user.equals("member")){
                        check_location=true;
                        ft.detach(current_frag);
                        ft.attach(current_frag);
                        ft.commit();
                    }
                    if (old_user.equals("domain")){
                        check_location=true;
                        ft.detach(current_frag);
                        ft.attach(current_frag);
                        ft.commit();
                    }
                }else{
                    if (old_user.equals("member")){
                        check_location=false;
                        ft.detach(current_frag);
                        ft.attach(current_frag);
                        ft.commit();
                    }
                    if (old_user.equals("domain")){
                        check_location=false;
                        ft.detach(current_frag);
                        ft.attach(current_frag);
                        ft.commit();
                    }
                }
            }
        });

        root.setAlpha(1f);

        reference = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) root.findViewById(R.id.list_member_inventory_products);
        dataModels = new ArrayList<>();
        dataModels_domain = new ArrayList<>();

        type_of_product_selected = getArguments().getString("medicine_type");

        ////PRODUCTS
        final Query query = reference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModels=new ArrayList<>();
                adapter = new CustomerViewProductsCustomAdapter(dataModels, root.getContext());
                listView.setAdapter(adapter);
                if (dataSnapshot.child("Member_Products").exists() || dataSnapshot.child("Domain_Products").exists()) {
                    dataSnapshot.getChildren();
                    for (DataSnapshot issue : dataSnapshot.child("Member_Products").getChildren()) {
                        issue_function(issue,"member");
                    }
                    for (DataSnapshot issue : dataSnapshot.child("Domain_Products").getChildren()) {
                        issue_function(issue,"domain");
                    }
                    Collections.reverse(dataModels);
                    adapter = new CustomerViewProductsCustomAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        ////PRODUCTS END
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (old_user.equals("member")){
                mainActivity.mTitle.setText("بازار");
            }
            if (old_user.equals("domain")){
                domainMainActivity.mTitle.setText("بازار");
            }
            //getActivity().finish();
            ft.remove(current_frag);
            ft.commit();
        }catch (Exception e){

        }

    }

    public void issue_function(DataSnapshot issue, String vendor_type){
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
        Double lati=0.0;
        Double longi=0.0;
        if (!TextUtils.isEmpty(product.getLati()))
        {
            lati=Double.parseDouble(product.getLati()+"");
        }
        if (!TextUtils.isEmpty(product.getLongi()))
        {
            longi=Double.parseDouble(product.getLongi()+"");
        }
//        Toast.makeText(getActivity(), ""+lati, Toast.LENGTH_LONG).show();

        Location usersLocation = new Location("");//provider name is unnecessary
        if (old_user.equals("member")){
            usersLocation.setLatitude(mainActivity.Location_Lat);
            usersLocation.setLongitude(mainActivity.Location_Long);
        }
        if (old_user.equals("domain")){
            usersLocation.setLatitude(domainMainActivity.Location_Lat);
            usersLocation.setLongitude(domainMainActivity.Location_Long);
        }

        Location questionLocation = new Location("");//provider name is unnecessary
        questionLocation.setLatitude(lati);
        questionLocation.setLongitude(longi);

        float distanceInMeters =  questionLocation.distanceTo(usersLocation);
        float distanceInKM=distanceInMeters/1000;
        distanceInKM=1f;

        if (!check_location){ distanceInKM=1f; }
        else{ distanceInKM=distanceInMeters/1000; }
        if (distanceInKM<=100f){

            if (Product_Type.equals(type_of_product_selected) && Verification.equals("1")) {
            dataModels.add(new DataModel(Product_Name, Product_Price, Product_Detail,Product_Vendor_contact_no,
                    Product_Date, Product_Time, Product_Image_0, Product_ID,
                    Product_Measure_In, Product_Litre_Kilo, product.getTotal_Images(),
                    images_list, Verification,old_user+"",vendor_type+""));
            images_list = new ArrayList<>();
        }
        }

        Product_Total_images=0;
    }
}