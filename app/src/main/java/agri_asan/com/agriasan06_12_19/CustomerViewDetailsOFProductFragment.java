package agri_asan.com.agriasan06_12_19;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
//import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.shawnlin.numberpicker.NumberPicker;

public class CustomerViewDetailsOFProductFragment extends Fragment {

    String user_type_for_views="";
    DatabaseReference databaseReferenceViewsGet, databaseReferenceViewsPut;

    LinearLayout layout_add_to_cart_btn;
    ImagesAdapter imagesadapter;

    LinearLayout layout_call_vendor;
    LinearLayout contact_us_partition;

    Resources resources;

    String Vendor_Name, Vendor_City, Vendor_ID_Card, Vendor_ID;

    String Product_ID, Product_Name, Product_Price, Product_Detail, Vendor_Contact_No, Product_Mesure
    , Product_litre_kilo;
    ArrayList<String> Product_images_link;

    String formattedDate, formattedTime;

    TextView kilo_litre;

    TextView product_name, product_price, product_details, vendor_contact_no;

    TextView textview_midaar_quantity;

    TextView product_mikdaar;
    String Paimana;

    Context thiscontext;
    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    ArrayList<Uri> image_uris;
    ArrayList<String> images_link;


    //////////IMAGE AND CAMERA MULTIPLE
    private GridView listView;
    private GalleryAdapterImageString galleryAdapterImageString;
    ProgressDialog progressDialog;
    //////////IMAGE AND CAMERA MULTIPLE END

    String id;
    Firebase childRef;
    DatabaseReference databaseReference;
    private Firebase mRootref;

    NumberPicker quantity_picker;
    String user_type="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        image_uris=new ArrayList<>();
        images_link=new ArrayList<>();

        thiscontext = container.getContext();
        View root = inflater.inflate(R.layout.fragment_customer_view_detail_product, container, false);


        databaseReferenceViewsGet=FirebaseDatabase.getInstance().getReference();

        mRootref.setAndroidContext(getActivity());
        mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Members");

        Vendor_Name = getArguments().getString("Vendor_Name");
        Vendor_City = getArguments().getString("Vendor_City");
        Vendor_ID = getArguments().getString("Vendor_ID");
        Vendor_ID_Card = getArguments().getString("Vendor_ID_Card");

        Product_ID=getArguments().getString("product_id");
        Product_Name=getArguments().getString("product_name");
        Product_Detail=getArguments().getString("product_detail");
        Vendor_Contact_No=getArguments().getString("vendor_contact_no");
        Vendor_Contact_No=Vendor_Contact_No+"";
        Product_Mesure=getArguments().getString("product_measure_in");
        Product_litre_kilo=getArguments().getString("product_litre_kilo");
        Product_Price=getArguments().getString("product_price");
        Product_images_link=getArguments().getStringArrayList("images_list");


        user_type = getArguments().getString("user_type");
        if (user_type.equals("member")){
            mainActivity=(MainActivity)  getContext();
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Members").child(mainActivity.ID);
        }
        if (user_type.equals("domain")){
            domainMainActivity=(DomainMainActivity)  getContext();
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Domain_experts").child(domainMainActivity.ID);

        }
        try {
            user_type_for_views=getArguments().getString("vendor_type");
            if (user_type_for_views != null && user_type_for_views.equals("member")) {
                user_type_for_views = "Member_Products";
            }
            if (user_type_for_views != null && user_type_for_views.equals("domain")) {
                user_type_for_views = "Domain_Products";

            }

            final Query query = databaseReferenceViewsGet.child(user_type_for_views).orderByChild("ID").equalTo(Product_ID+"");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            Vendor user= issue.getValue(Vendor.class);
                            String Views_str=user.getViews();
//                            Toast.makeText(Objects.requireNonNull(getContext()), ""+Views_str, Toast.LENGTH_LONG).show();
                            int Views=Integer.parseInt(Views_str)+1;

                            databaseReferenceViewsPut = FirebaseDatabase.getInstance().getReference()
                                    .child(user_type_for_views).child(Product_ID).child("Views");
                            databaseReferenceViewsPut.setValue(""+Views);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }


        product_mikdaar=root.findViewById(R.id.textview_mikdaar);
        product_mikdaar.setText(Product_Mesure);

        product_name=root.findViewById(R.id.product_name);
        product_name.setText(""+Product_Name);
        product_price=root.findViewById(R.id.product_price);
        product_price.setText(""+Product_Price);
        product_details=root.findViewById(R.id.product_details);
        product_details.setText(""+Product_Detail);
        product_details.setMovementMethod(new ScrollingMovementMethod());
        vendor_contact_no=root.findViewById(R.id.vendor_contact_no);
        vendor_contact_no.setText(""+Vendor_Contact_No);

        textview_midaar_quantity=root.findViewById(R.id.textview_midaar_quantity);

        layout_add_to_cart_btn=root.findViewById(R.id.layout_add_to_cart_btn);


        contact_us_partition=root.findViewById(R.id.contact_us_partition);


        quantity_picker =  root.findViewById(R.id.quantity_picker);

        if (Vendor_Contact_No.equals("null")){
            contact_us_partition.setVisibility(View.GONE);
        }

        if (Product_Mesure.equals("کلو")){
//            Toast.makeText(Objects.requireNonNull(getContext()), "AAA", Toast.LENGTH_LONG).show();
            textview_midaar_quantity.setText(Product_Mesure);
        }
        if (Product_Mesure.equals("لیٹر")){
//            Toast.makeText(Objects.requireNonNull(getContext()), "BBB", Toast.LENGTH_LONG).show();
            textview_midaar_quantity.setText(Product_Mesure);
        }

        layout_call_vendor=root.findViewById(R.id.layout_call_vendor);
        layout_call_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", Vendor_Contact_No+"", null)));
            }
        });


        layout_add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(Objects.requireNonNull(getContext()), "پروڈکٹ ٹوکری میں ڈال دی گئی ہے", Toast.LENGTH_LONG).show();
                databaseReference.child("Cart").child(Product_ID).child("ID").setValue(Product_ID);
                databaseReference.child("Cart").child(Product_ID).child("Quantity")
                        .setValue(quantity_picker.getValue()+"");
            }
        });

        kilo_litre=root.findViewById(R.id.product_kilo_litre);
        kilo_litre.setText(""+Product_litre_kilo);

        listView = root.findViewById(R.id.gv);


        imagesadapter=new ImagesAdapter();
        imagesadapter.execute();

    return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Override
    public void onPause() {
        super.onPause();
    }


    class ImagesAdapter extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            progressDialog.show();
            galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Product_images_link);


            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            listView.setAdapter(galleryAdapterImageString);

            super.onPostExecute(aVoid);
        }
    }



}