package agri_asan.com.agriasan06_12_19;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.AudioSource.MIC;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MemberInventoryFragment extends Fragment {
    DatabaseReference reference;
    Button btn_add_prodcut;
    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;

    String Vendor_Name;
    String Vendor_City;
    String Vendor_ID_Card;
    String Vendor_ID;

    String Vendor;
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static VendorMemberViewProductsCustomAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member_products, container, false);

        root.setAlpha(1f);
//        Vendor=intent.getStringExtra("Vendor");
        //Vendor = getArguments().getString("");

        mainActivity=(MainActivity)  getActivity();

        reference = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) root.findViewById(R.id.list_member_inventory_products);
        dataModels = new ArrayList<>();

//        mainActivity.mTitle.setText("آپکی پروڈکٹس");
        final Query query = reference.child("Member_Products").orderByChild("Phone").equalTo(mainActivity.Phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModels=new ArrayList<>();
                adapter = new VendorMemberViewProductsCustomAdapter(dataModels, root.getContext());
                listView.setAdapter(adapter);
                if (dataSnapshot.exists()) {
                    //question_no=dataSnapshot.getChildrenCount();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Vendor product = issue.getValue(Vendor.class);
                        String Product_Name = product.getProduct_Name();
                        String Views=product.getViews();
                        String Product_Price = product.getProduct_Price();
                        String Product_Detail = product.getProduct_Detail();
                        String Vendor_Contact_No = product.getVendor_Contact_No();
                        String Product_Date = product.getProduct_Date();
                        String Product_Time = product.getProduct_Time();
                        String Product_Image_0 = product.getImage_0();
                        String Product_ID = product.getID();
                        String Product_Measure_In=product.getMeasure_In();
                        String Product_Litre_Kilo=product.getLitre_Kilo();
                        String Verification = product.getVerification();

                        String Product_Type=product.getProduct_Type();

                        int Product_Total_images=0;
                        if (!TextUtils.isEmpty(product.getTotal_Images())){
                            Product_Total_images = Integer.parseInt(product.getTotal_Images());
                        }
                        ArrayList<String> images_list=new ArrayList<>();
                        images_list.add(Product_Image_0);
                        if (Product_Total_images==2){
                            images_list.add(product.getImage_1());
                        }if (Product_Total_images==3){
                            images_list.add(product.getImage_1());
                            images_list.add(product.getImage_2());
                        }if (Product_Total_images==4){
                            images_list.add(product.getImage_1());
                            images_list.add(product.getImage_2());
                            images_list.add(product.getImage_3());
                        }if (Product_Total_images==5){
                            images_list.add(product.getImage_1());
                            images_list.add(product.getImage_2());
                            images_list.add(product.getImage_3());
                            images_list.add(product.getImage_4());
                        }if (Product_Total_images==6){
                            images_list.add(product.getImage_1());
                            images_list.add(product.getImage_2());
                            images_list.add(product.getImage_3());
                            images_list.add(product.getImage_4());
                            images_list.add(product.getImage_5());
                        }
                        dataModels.add(new DataModel(Product_Name, Product_Price, Product_Detail,Vendor_Contact_No,
                                Product_Date, Product_Time,Product_Image_0,Product_ID,
                                Product_Measure_In, Product_Litre_Kilo,product.getTotal_Images(),
                                Product_Type , images_list, Verification,Views));
                        images_list=new ArrayList<>();
                        Product_Total_images=0;
                    }
                    Collections.reverse(dataModels);
                    adapter = new VendorMemberViewProductsCustomAdapter(dataModels, root.getContext());
                    listView.setAdapter(adapter);
//                    listView.setOnScrollListener(adapter);
                }
//                adapter= new DomainViewQuestionsCustomAdapter(dataModels, root.getContext());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final Query query_2 = reference.child("Vendors").orderByChild("Phone").equalTo(mainActivity.Phone);
        query_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Member product= issue.getValue(Member.class);
                        Vendor_Name=product.getName();
                        Vendor_City=product.getCity();
                        Vendor_ID_Card=product.getID_Card();
                        Vendor_ID=product.getID();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btn_add_prodcut=root.findViewById(R.id.btn_add_product);
        btn_add_prodcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MemberChooseTypeForNewProductFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("Vendor_Name",Vendor_Name);
                bundle.putString("Vendor_City",Vendor_City);
                bundle.putString("Vendor_ID",Vendor_ID);
                bundle.putString("Vendor_ID_Card",Vendor_ID_Card);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.mTitle.setText(R.string.title_bazaar);
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

}