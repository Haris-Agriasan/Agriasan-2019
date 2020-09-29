package agri_asan.com.agriasan06_12_19;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallGridPulseIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

import static agri_asan.com.agriasan06_12_19.MainActivity.popupWindow;
import static agri_asan.com.agriasan06_12_19.MainActivity.progressDialog;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MemberUpdateProductFragment extends Fragment {

    LinearLayout layout_fasal_change;
    String type_of_product="";
    TextView textview_product_type;

    TextView total_images;

    ImagesAdapter imagesadapter;

    private String upload_URL = "";
    JSONObject jsonObject;
    RequestQueue rQueue;
    String cmp_url="";
    String imgname="";

    ArrayList<String> images_to_firebase_db;

    InputStream input=null;
    ExifInterface exif=null;

    ImageView show_images_btn;
    RelativeLayout layout_show_images;
    RelativeLayout layout_pre_images;
    private ListView listView_images;


    ImageView loading;
    ////FOR IMAGE UPLOADING ON FIREBASE
    private StorageTask mUploadTask;
    /////FOR IMAGE UPLOADING ON FIREBASE

    DatabaseReference databaseReference;

    DatabaseReference reference;


    String Vendor_Name;
    String Vendor_City;
    String Vendor_ID_Card;
    String Vendor_ID;

    String Product_ID;
    String Product_Name;
    String Product_Price;
    String Product_Detail;
    String Vendor_Contact;
    String Product_Mesure;
    String Product_litre_kilo;
    ArrayList<String> Product_images_link;

    String formattedDate;
    String formattedTime;

    TextView kilo_litre;

    EditText product_name;
    EditText product_price;
    Button btn_add_product;
    EditText product_details;
    EditText vendor_contact_no;



    Spinner paimana_spinner;
    String Paimana;

    Context thiscontext;
    MainActivity mainActivity;

    ArrayList<Uri> image_uris;
    ArrayList<String> images_link;

    ArrayList<String> t1;

    private Firebase mRootref;


    //////////IMAGE AND CAMERA MULTIPLE
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private GridView gvGallery;
    private GridView listView;
    private GalleryAdapter galleryAdapter;
    private GalleryAdapterImageString galleryAdapterImageString;
    ImageButton btn_select_images;
    ImageButton btn_delete_images;
    Adapter adapter;
    Config config;
    //////////IMAGE AND CAMERA MULTIPLE END

    String t;


    String id;
    Firebase childRef;

    Boolean images_check=true;

    //////////UPLOADING ON FIRESTORAGE 2
    StorageReference storageRef;
    StorageReference mountainsRef;
    StorageReference mountainImagesRef;
    UploadTask uploadTask;

    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_update_product_member, container, false);
        total_images=root.findViewById(R.id.total_images);

        layout_pre_images=root.findViewById(R.id.layout_pre_images);
        layout_show_images=root.findViewById(R.id.layout_show_images);
        layout_show_images.setVisibility(View.GONE);
        show_images_btn=root.findViewById(R.id.show_images_btn);

        textview_product_type=root.findViewById(R.id.textview_product_type);
        //listView_images = root.findViewById(R.id.gv);

        layout_fasal_change=root.findViewById(R.id.layout_fasal_change);


        images_check=true;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);


        image_uris=new ArrayList<>();
        images_link=new ArrayList<>();
        t1=new ArrayList<>();

        thiscontext = container.getContext();
        mainActivity = (MainActivity) getActivity();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        paimana_spinner=root.findViewById(R.id.spinner_paimana);

        ///for spinner dadapter
        List<String> list = new ArrayList<String>();
        list.add("کوئ نہیں");
        list.add("کلو");
        list.add("لیٹر");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paimana_spinner.setAdapter(dataAdapter);

        ///FOR SPINNER ADAPTER END
        /// NEW UPLOADING
        storageRef = FirebaseStorage.getInstance().getReference("Products_images");

        // While the file names are the same, the references point to different files
        //mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        //mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


        Vendor_Name = getArguments().getString("Vendor_Name");
        Vendor_City = getArguments().getString("Vendor_City");
        Vendor_ID = getArguments().getString("Vendor_ID");
        Vendor_ID_Card = getArguments().getString("Vendor_ID_Card");

        Product_ID=getArguments().getString("product_id");
        Product_Name=getArguments().getString("product_name");
        Product_Detail=getArguments().getString("product_detail");
        Vendor_Contact=getArguments().getString("vendor_contact");
        Product_Mesure=getArguments().getString("product_measure_in");
        Product_litre_kilo=getArguments().getString("product_litre_kilo");
        Product_Price=getArguments().getString("product_price");

        type_of_product=getArguments().getString("product_type");

        Product_images_link=getArguments().getStringArrayList("images_list");

        paimana_spinner.setSelection(getIndex(paimana_spinner, Product_Mesure));

        layout_fasal_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_change_type_of_product(v);
            }
        });


        product_name=root.findViewById(R.id.product_name);
        product_name.setText(""+Product_Name);
        product_price=root.findViewById(R.id.product_price);
        product_price.setText(""+Product_Price);
        product_details=root.findViewById(R.id.product_details);
        product_details.setText(""+Product_Detail);
        vendor_contact_no=root.findViewById(R.id.vendor_contact_no);
        vendor_contact_no.setText(""+Vendor_Contact);

        textview_product_type.setText(""+type_of_product);

        btn_add_product=root.findViewById(R.id.add_product_final_btn);

        kilo_litre=root.findViewById(R.id.product_kilo_litre);
        kilo_litre.setText(""+Product_litre_kilo);

        listView = root.findViewById(R.id.gv);

        imagesadapter=new ImagesAdapter();
        imagesadapter.execute();
//        galleryAdapterImageString = new GalleryAdapterImageString(getContext().getApplicationContext(),Product_images_link);
//        listView.setAdapter(galleryAdapterImageString);

        show_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_show_images(v);
            }
        });

        config = new Config();

//        gvGallery = root.findViewById(R.id.gv);
        btn_select_images=root.findViewById(R.id.select_images_btn);
        btn_delete_images=root.findViewById(R.id.btn_delete_images);
//        btn_delete_images.setVisibility(View.INVISIBLE);
        btn_delete_images.setEnabled(false);
        btn_delete_images.setAlpha(0.2f);
        btn_select_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
        btn_delete_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
                image_uris=new ArrayList<>();
//                btn_delete_images.setVisibility(View.INVISIBLE);
                btn_delete_images.setEnabled(false);
                btn_delete_images.setAlpha(0.2f);
                images_check=false;
                layout_show_images.setVisibility(View.INVISIBLE);
            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save_product_details_db();
            }
        });

    return root;
    }
    int i;
    int j=0;
    int l=0;
    int k=0;
    Uri downloadUri;
    String path_to_image;
    String final_uri;
    public void upload_firebase(Bitmap bitmap) {

        upload_URL = "https://www.novaeno.com/agriasan/uploadProductImage.php";


        try {
            progressDialog.show();
        }catch (Exception e){

        }

        //////////////NEW CODE
        cmp_url="";
        imgname="";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("name", imgname);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);

            images_to_firebase_db.add(imgname);

            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            try {
                progressDialog.hide();
            }catch (Exception ignored) { }
//            SaveQuestionData();
            Log.e("JSONObject Here", e.toString());
//            Toast.makeText(Objects.requireNonNull(getContext()), "", Toast.LENGTH_LONG).show();

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.e("Image Uploadeddddddddd", jsonObject.toString());

                        rQueue.getCache().clear();
                        j++;
                        if(j==image_uris.size())
                        {
                            save_db_data();
                            try{
                                progressDialog.hide();
                            }catch (Exception ex)
                            {
                            }
                            mainActivity.onBackPressed();
                            Toast.makeText(mainActivity.getApplicationContext(), "آپکی پروڈکٹ اپ ڈیٹ ہو گئی ہے!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    progressDialog.hide();
                }catch (Exception ignored)
                {
                }
//                SaveQuestionData();
                Log.e("Errrrorrrr", volleyError.toString());

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 900000,
                10000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjectRequest);

        /////////NEW CODE END

        String extension = path_to_image.substring(path_to_image.lastIndexOf("."));
            mountainsRef = storageRef.child(System.currentTimeMillis()
                    + "" + extension);

            InputStream stream = null;
        uploadTask = mountainsRef.putFile(temp_uri_image);

        uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "" + exception, Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    databaseReference.child("Image_"+j).setValue(imageUrl+"");
                                    j++;
                                    if(j==image_uris.size())
                                    {
                                        save_db_data();
                                        try{
                                            progressDialog.hide();
                                        }catch (Exception ex)
                                        {
                                        }
                                        mainActivity.onBackPressed();
                                        Toast.makeText(mainActivity.getApplicationContext(), "آپکی پروڈکٹ اپ ڈیٹ ہو گئی ہے!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
    }
    Uri temp_uri_image;

    private void getImages() {

        TedBottomPicker.with(getActivity())
                .setPeekHeight(1600)
                .showTitle(false)
                .setSpacing(2)
                .setSelectMaxCount(6)
                .setSelectMaxCountErrorText(R.string.max_images_6)
                .setCompleteButtonText(R.string.shamil_karen)
                .setEmptySelectionText(R.string.no_image)
                .setSelectedUriList(image_uris)
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        image_uris= (ArrayList<Uri>) uriList;
//                        Toast.makeText(getContext(), "" + image_uris.get(0), Toast.LENGTH_LONG).show();
                        if (!uriList.isEmpty()){
                            if (layout_show_images != null) {
                                Glide.with(Objects.requireNonNull(getActivity()))
                                        .load(uriList.get(0))
                                        .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200,200))
                                        .thumbnail(Glide.with(getContext()).load(R.raw.loading_4))
                                        .into(show_images_btn);
                                int total_img=0;
                                total_img=uriList.size()-1;

                                if (uriList.size()>=2){
                                    if (total_images != null) {
                                        total_images.setText("+"+total_img);
                                    }
                                }else{
                                    if (total_images != null) {
                                        total_images.setText("");
                                    }
                                }
                                btn_delete_images.setEnabled(true);
                                btn_delete_images.setAlpha(1f);
                                layout_show_images.setVisibility(View.VISIBLE);
                                show_images_btn.setVisibility(View.VISIBLE);

                                images_check=true;
                                layout_pre_images.setVisibility(View.GONE);
                                show_images_btn.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
//        config.setCameraHeight(R.dimen.camera_height);
//        config.setToolbarTitleRes(R.string.tasaveer_len);
//        config.setSelectionLimit(6);
//        config.setSelectionMin(1);
//        ImagePickerActivity.setConfig(config);
//        Intent intent  = new Intent(getActivity().getApplicationContext(), ImagePickerActivity.class);
//        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
    }
    public void save_db_data(){
        for (int i=0;i<images_to_firebase_db.size();i++){

            cmp_url="https://novaeno.com/agriasan/agriasan_images/Products/";

            cmp_url=cmp_url+images_to_firebase_db.get(i)+".JPG";

            databaseReference.child("Image_"+i).setValue(cmp_url+"");

        }
        databaseReference.child("Product_Name").setValue(product_name.getText().toString());
        databaseReference.child("Product_Price").setValue(product_price.getText().toString());
        databaseReference.child("Product_Detail").setValue(product_details.getText().toString());
        databaseReference.child("Vendor_Contact_No").setValue(vendor_contact_no.getText().toString());
        databaseReference.child("Measure_In").setValue(paimana_spinner.getSelectedItem());
        databaseReference.child("Litre_Kilo").setValue(kilo_litre.getText().toString());
        databaseReference.child("Product_Type").setValue(textview_product_type.getText().toString());
    }
    public void Save_product_details_db(){
        images_to_firebase_db=new ArrayList<>();

        if( TextUtils.isEmpty(product_name.getText())){
            Toast.makeText(getActivity(), "پہلے شے کا نام درج کریں!", Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), ""+String.valueOf(paimana_spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
        }else if( TextUtils.isEmpty(product_price.getText())){
            Toast.makeText(getActivity(), "پہلے شے کی قیمت درج کریں!", Toast.LENGTH_LONG).show();
        }else if( TextUtils.isEmpty(product_details.getText())){
            Toast.makeText(getActivity(), "پہلے شے کی تفصیل لکھیں!", Toast.LENGTH_LONG).show();
        } else if( TextUtils.isEmpty(vendor_contact_no.getText())){
            Toast.makeText(getActivity(), "پہلے اپنا رابطہ نمبر لکھیں!", Toast.LENGTH_LONG).show();
        }
//        else if(image_uris.isEmpty()){
//            Toast.makeText(getActivity(), "پہلے شے کی تصویر درج کریں!", Toast.LENGTH_LONG).show();
//        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Member_Products").child(Product_ID);
            if (!images_check){
                Toast.makeText(getActivity(), "پہلے شے کی تصویر درج کریں!", Toast.LENGTH_LONG).show();
            }
            else if(images_check && !image_uris.isEmpty()){
//                Toast.makeText(getActivity(), "آپکی پروڈکٹ اپ ڈیٹ ہو گئی ہے!", Toast.LENGTH_LONG).show();
                try {
                    progressDialog.show();
                }catch (Exception ex)
                {
                }
                for (int j = 0; j < Product_images_link.size(); j++) {
                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("Member_Products").child(Product_ID).child("Image_"+j);
                    reference.removeValue();
                }
                databaseReference.child("Total_Images").setValue(image_uris.size()+"");
                save_db_data();
                i=0;
                j=0;
                for (i = 0; i < image_uris.size(); i++) {
                    path_to_image=image_uris.get(i).toString();
                    temp_uri_image=image_uris.get(i);
                    Bitmap bitmap=null;
                    int orientation=0;
                    try {
                        input = getContext().getContentResolver().openInputStream(temp_uri_image);
                        exif = null;
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if (input != null) { exif = new ExifInterface(input); }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        orientation = 0;
                        if (exif != null) {
                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                        }

                        bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), temp_uri_image);

                        Bitmap bmRotated = rotateBitmap(bitmap,orientation);
                        if (bmRotated != null) { upload_firebase(bmRotated); }
                        else{ upload_firebase(bitmap); }
                    } catch (Exception e) {
                        try {
                            progressDialog.hide();
                        }catch (Exception ignored)
                        {
                        }
                        Toast.makeText(getContext(), "دوبارہ کوشش کریں", Toast.LENGTH_SHORT).show();
                    }
//                    upload_firebase();
                }
            }else{
                save_db_data();
                mainActivity.onBackPressed();
                Toast.makeText(mainActivity.getApplicationContext(), "آپکی پروڈکٹ اپ ڈیٹ ہو گئی ہے!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {

        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//              t=intent.getParcelableExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//            galleryAdapter = new GalleryAdapter(getContext().getApplicationContext(),image_uris);
//            listView.setAdapter(galleryAdapter);
            btn_delete_images.setEnabled(true);
            btn_delete_images.setAlpha(1f);
            images_check=true;
            layout_show_images.setVisibility(View.VISIBLE);
            layout_pre_images.setVisibility(View.GONE);
        }
    }
    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
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
    public void popup_window_show_images(View v) {

        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_show_images, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow popupWindow_images;
        popupWindow_images = new PopupWindow(popupView, width, height, focusable);
        popupWindow_images.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindow_images.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        listView_images = popupView.findViewById(R.id.gv);

        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);
        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);
        popupWindow_images.setTouchable(true);
        popupWindow_images.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        galleryAdapter = new GalleryAdapter(mainActivity.getApplicationContext(),image_uris,root);
        listView_images.setAdapter(galleryAdapter);

        config = new Config();


        popupWindow_images.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (image_uris.size()!=0) {
                    if (layout_show_images != null) {
                        Glide.with(Objects.requireNonNull(getActivity()))
                                .load(image_uris.get(0))
                                .apply(new RequestOptions().placeholder(R.drawable.loading_4).override(200, 200))
                                .into(show_images_btn);
                        int total_img = 0;
                        total_img = image_uris.size() - 1;

                        if (image_uris.size() >= 2) {
                            if (total_images != null) {
                                total_images.setText("+" + total_img);
                            }
                        } else {
                            if (total_images != null) {
                                total_images.setText("");
                            }
                        }
                        btn_delete_images.setEnabled(true);
                        btn_delete_images.setAlpha(1f);
                        layout_show_images.setVisibility(View.VISIBLE);
                    }
                }
                else{
//                    Toast.makeText(getContext(), "CHeck111" , Toast.LENGTH_LONG).show();

                    btn_delete_images.setEnabled(false);
                    btn_delete_images.setAlpha(0.2f);
                    layout_show_images.setVisibility(View.INVISIBLE);

                    total_images.setText("");
                    show_images_btn.setVisibility(View.INVISIBLE);
                }
                try {
                    popupWindow_images.dismiss();
                }catch (Exception e){

                }
            }
        });


    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    class ImagesAdapter extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            progressDialog.show();

            galleryAdapterImageString = new GalleryAdapterImageString(Objects.requireNonNull(getContext()).getApplicationContext(),Product_images_link);


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

    public void popup_window_change_type_of_product(View v) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_member_choose_type_of_new_product, null);


        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
//        PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, width, height);


        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
//      popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                Toast.makeText(getContext(), "Check 1", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        ImageView seed;
        ImageView medicine;
        ImageView keray;

        ImageView machines;
        ImageView sabzi;
        ImageView khaad;

        seed = popupView.findViewById(R.id.beej_iksaam_button);
        medicine = popupView.findViewById(R.id.fasal_adoyaat_button);
        keray = popupView.findViewById(R.id.keray_mar_button);
        machines = popupView.findViewById(R.id.machines_button);
        sabzi = popupView.findViewById(R.id.sabzi_button);
        khaad = popupView.findViewById(R.id.khaad_iksaam_button);

        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "کپاس", Toast.LENGTH_LONG).show();
                saving_medicine_type("بیج کی اقسام");
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گندم", Toast.LENGTH_LONG).show();
                saving_medicine_type("فصل کی ادویات");
            }
        });
        keray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "چاول", Toast.LENGTH_LONG).show();
                saving_medicine_type("کیڑے مارادویات");

            }
        });
        machines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "مکئی", Toast.LENGTH_LONG).show();
                saving_medicine_type("مشینوں کی معلومات");
            }
        });
        sabzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "گنا", Toast.LENGTH_LONG).show();
                saving_medicine_type("سبزی کی غزائیت");
            }
        });
        khaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(root.getContext().getApplicationContext(), "دالیں", Toast.LENGTH_LONG).show();
                saving_medicine_type("کھاد کی اقسام");
            }
        });


    }

    private void saving_medicine_type(String type_of_product_t){
        Toast.makeText(getContext().getApplicationContext(), ""+type_of_product_t, Toast.LENGTH_LONG).show();
        this.type_of_product=type_of_product_t;
        textview_product_type.setText(type_of_product+"");
        popupWindow.dismiss();
    }



}