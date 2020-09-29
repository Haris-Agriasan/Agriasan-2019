package agri_asan.com.agriasan06_12_19;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

import static agri_asan.com.agriasan06_12_19.MainActivity.popupWindow;
import static agri_asan.com.agriasan06_12_19.MainActivity.progressDialog;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.mikepenz.iconics.Iconics.getApplicationContext;

public class MemberAddProductFragment extends Fragment {



    LinearLayout layout_fasal_change;

    TextView total_images;

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

    ////FOR IMAGE UPLOADING ON FIREBASE
    private ProgressBar mProgressBar;
    private StorageTask mUploadTask;
    /////FOR IMAGE UPLOADING ON FIREBASE


    String Vendor_Name;
    String Vendor_City;
    String Vendor_ID_Card;
    String Vendor_ID;

    String formattedDate;
    String formattedTime;

    TextView kilo_litre;

    EditText product_name;
    EditText product_price;
    Button btn_add_product;
    EditText product_details;
    EditText vendor_contact_no;

    TextView textview_product_type;

    Spinner paimana_spinner;

    Context thiscontext;
    MainActivity mainActivity;

    ArrayList<Uri> image_uris;
    ArrayList<String> images_link;

    ArrayList<String> t1;

    private Firebase mRootref;


    //////////IMAGE AND CAMERA MULTIPLE
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private GridView gvGallery;
    private ListView listView_images;
    private GalleryAdapter galleryAdapter;
    ImageButton btn_select_images;
    ImageButton btn_delete_images;
    Adapter adapter_images;
    Config config;
    //////////IMAGE AND CAMERA MULTIPLE END

    String t;


    String id;
    Firebase childRef;


    //////////UPLOADING ON FIRESTORAGE 2
    StorageReference mountainsRef;
    StorageReference mountainImagesRef;
    UploadTask uploadTask;


    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        config = new Config();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c);

        image_uris=new ArrayList<>();
        images_link=new ArrayList<>();
        t1=new ArrayList<>();

        thiscontext = container.getContext();

        root = inflater.inflate(R.layout.fragment_add_product_member, container, false);


        layout_fasal_change=root.findViewById(R.id.layout_fasal_change);

        total_images=root.findViewById(R.id.total_images);
        layout_show_images=root.findViewById(R.id.layout_show_images);
        layout_show_images.setVisibility(View.INVISIBLE);
        show_images_btn=root.findViewById(R.id.show_images_btn);

        kilo_litre=root.findViewById(R.id.product_kilo_litre);

        mProgressBar = root.findViewById(R.id.progress_bar);

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

        mProgressBar.setVisibility(View.INVISIBLE);

        show_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_show_images(v);
            }
        });


        layout_fasal_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window_change_type_of_product(v);
            }
        });


        Vendor_Name = getArguments().getString("Vendor_Name");
        Vendor_City = getArguments().getString("Vendor_City");
        Vendor_ID = getArguments().getString("Vendor_ID");
        Vendor_ID_Card = getArguments().getString("Vendor_ID_Card");

        textview_product_type=root.findViewById(R.id.textview_product_type);

        type_of_product = getArguments().getString("type_of_product");
        textview_product_type.setText(type_of_product+"");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float s_height = displayMetrics.heightPixels;
        float s_width = displayMetrics.widthPixels;

        s_height = s_height * 0.4f;
        s_width = s_width * 0.8f;




        product_name=root.findViewById(R.id.product_name);
        product_price=root.findViewById(R.id.product_price);
        product_details=root.findViewById(R.id.product_details);
        vendor_contact_no=root.findViewById(R.id.vendor_contact_no);
        btn_add_product=root.findViewById(R.id.add_product_final_btn);





//        gvGallery = root.findViewById(R.id.gv);
        listView_images = root.findViewById(R.id.gv);
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
//                listView_images.setAdapter(null);
                image_uris=new ArrayList<>();
//                btn_delete_images.setVisibility(View.INVISIBLE);
                btn_delete_images.setEnabled(false);
                btn_delete_images.setAlpha(0.2f);
                layout_show_images.setVisibility(View.INVISIBLE);



            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save_product_details_db();
                //upload_firebase();
            }
        });

    return root;
    }
    int i;
    int j=0;
    String path_to_image;
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
                            upload_data_firebase();
                            try{
                                progressDialog.hide();
                            }catch (Exception ex)
                            {
                            }
                            Toast.makeText(getContext(), "آپکی پروڈکٹ درج کر لی گئی ہے اب تصدیق ہونے کا انتظار کریں!", Toast.LENGTH_LONG).show();

                            mainActivity.onBackPressed();
                            mainActivity.onBackPressed();
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


//        String extension = path_to_image.substring(path_to_image.lastIndexOf("."));
//            mountainsRef = storageRef.child(System.currentTimeMillis()
//                    + "" + extension);
//
//            InputStream stream = null;
//        uploadTask = mountainsRef.putFile(temp_uri_image);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(getContext(), "" + exception, Toast.LENGTH_LONG).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    if (taskSnapshot.getMetadata() != null) {
//                        if (taskSnapshot.getMetadata().getReference() != null) {
//                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String imageUrl = uri.toString();
//
//                                    childRef = mRootref.child(id).child("Image_"+j);
//                                    childRef.setValue(""+imageUrl);
////                                    Toast.makeText(getActivity(), ""+imageUrl, Toast.LENGTH_LONG).show();
//
//                                    j++;
//                                    if(j==image_uris.size())
//                                    {
//
//                                        upload_data_firebase();
//                                        try{
//                                            progressDialog.hide();
//                                        }catch (Exception ex)
//                                        {
//                                        }
//                                        mainActivity.onBackPressed();
//                                        mainActivity.onBackPressed();
//                                        //Toast.makeText(getActivity(), "آپکی پروڈکٹ اپ ڈیٹ ہو گئی ہے!", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            });
    }

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
                            }
                        }
                    }
                });

    }
    Uri temp_uri_image;
    public void Save_product_details_db(){
        images_to_firebase_db=new ArrayList<>();

        if( TextUtils.isEmpty(product_name.getText())){
            Toast.makeText(getActivity(), "پہلے شے کا نام درج کریں!", Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), ""+String.valueOf(paimana_spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
        }else if( TextUtils.isEmpty(product_price.getText())){
            Toast.makeText(getActivity(), "پہلے شے کی قیمت درج کریں!", Toast.LENGTH_LONG).show();
        }else if( TextUtils.isEmpty(product_details.getText())){
            Toast.makeText(getActivity(), "پہلے شے کی تفصیل لکھیں!", Toast.LENGTH_LONG).show();
        }else if( TextUtils.isEmpty(vendor_contact_no.getText())){
            Toast.makeText(getActivity(), "پہلے اپنا رابطہ نمبر لکھیں!", Toast.LENGTH_LONG).show();
        }else if(image_uris.isEmpty()){
            Toast.makeText(getActivity(), "پہلے شے کی تصویر درج کریں!", Toast.LENGTH_LONG).show();
        }else {
            mRootref.setAndroidContext(getActivity());
            mRootref = new Firebase("https://agriasan-6b704.firebaseio.com/Member_Products");
            //it will create a unique id and we will use it as the Primary Key for our Member
            id = mRootref.push().getKey();
            try {
                progressDialog.show();
            }catch (Exception ex)
            {
            }
            i=0;
            j=0;
            upload_data_firebase();
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
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        //progressDialog.show();

        super.onActivityResult(requestCode, resuleCode, intent);
//        progressDialog.show();

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//              t=intent.getParcelableExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//            galleryAdapter = new GalleryAdapter(getContext().getApplicationContext(),image_uris);
//            listView_images.setAdapter(galleryAdapter);
            btn_delete_images.setEnabled(true);
            btn_delete_images.setAlpha(1f);
            layout_show_images.setVisibility(View.VISIBLE);

        }
        //progressDialog.dismiss();

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
        PopupWindow popupWindow_images;
        popupWindow_images = new PopupWindow(popupView, width, height);
        popupWindow_images.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listView_images = popupView.findViewById(R.id.gv);

        popupWindow_images.setOutsideTouchable(true);
        popupWindow_images.setFocusable(true);
        popupWindow_images.setTouchable(true);
        popupWindow_images.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow_images.showAtLocation(popupView, Gravity.CENTER, 0, 0);


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


    String type_of_product="";
    public void upload_data_firebase(){
        childRef = mRootref.child(id).child("Member_Name");
        childRef.setValue(mainActivity.Name + "");



        for (int i=0;i<images_to_firebase_db.size();i++){

            cmp_url="https://novaeno.com/agriasan/agriasan_images/Products/";

            cmp_url=cmp_url+images_to_firebase_db.get(i)+".JPG";

            childRef = mRootref.child(id).child("Image_"+i);
            childRef.setValue(""+cmp_url);

        }


        childRef = mRootref.child(id).child("Member_City");
        childRef.setValue(mainActivity.City + "");

        childRef = mRootref.child(id).child("Phone");
        childRef.setValue(mainActivity.Phone + "");

        childRef = mRootref.child(id).child("Member_ID_Card");
        childRef.setValue(mainActivity.ID_Card + "");

        childRef = mRootref.child(id).child("ID");
        childRef.setValue(id + "");

        childRef = mRootref.child(id).child("Member_ID");
        childRef.setValue(mainActivity.ID + "");

        childRef = mRootref.child(id).child("Product_Name");
        childRef.setValue(product_name.getText().toString() + "");

        childRef = mRootref.child(id).child("Product_Type");
        childRef.setValue(textview_product_type.getText().toString() + "");

        childRef = mRootref.child(id).child("Product_Price");
        childRef.setValue(product_price.getText().toString() + "");

        childRef = mRootref.child(id).child("Product_Detail");
        childRef.setValue(product_details.getText().toString() + "");

        childRef = mRootref.child(id).child("Vendor_Contact_No");
        childRef.setValue(vendor_contact_no.getText().toString() + "");

        childRef = mRootref.child(id).child("Product_Date");
        childRef.setValue(formattedDate + "");

        childRef = mRootref.child(id).child("Product_Time");
        childRef.setValue(formattedTime + "");

        childRef = mRootref.child(id).child("User");
        childRef.setValue("member");

        childRef = mRootref.child(id).child("Vendor_Name");
        childRef.setValue("" + Vendor_Name);

        childRef = mRootref.child(id).child("Vendor_City");
        childRef.setValue("" + Vendor_City);

        childRef = mRootref.child(id).child("Vendor_ID");
        childRef.setValue("" + Vendor_ID);

        childRef = mRootref.child(id).child("Measure_In");
        childRef.setValue("" + String.valueOf(paimana_spinner.getSelectedItem()) );

        childRef = mRootref.child(id).child("Vendor_ID_Card");
        childRef.setValue("" + Vendor_ID_Card);

        childRef = mRootref.child(id).child("Total_Images");
        childRef.setValue("" + image_uris.size());

        childRef = mRootref.child(id).child("Longi");
        childRef.setValue(""+mainActivity.Location_Long);

        childRef = mRootref.child(id).child("Lati");
        childRef.setValue(""+mainActivity.Location_Lat);

        childRef = mRootref.child(id).child("Litre_Kilo");
        childRef.setValue("" + kilo_litre.getText().toString());

        childRef = mRootref.child(id).child("Verification");
        childRef.setValue("0");

        childRef = mRootref.child(id).child("Verification");
        childRef.setValue("0");
        childRef = mRootref.child(id).child("Views");
        childRef.setValue("0");


        try {
            Thread.sleep(1000); //1000 milliseconds is one second.
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }


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
