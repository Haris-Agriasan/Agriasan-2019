package agri_asan.com.agriasan06_12_19;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GalleryAdapter extends BaseAdapter {

    ImageView image_popup;
    ImageView button_close;

    private View view;
    private Context ctx;
//    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ImageButton btn_delete_image;
    ArrayList<Uri> mArrayUri;
    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri, View v) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
        this.view=v;
    }

//    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri) {
//
//        this.ctx = ctx;
//        this.mArrayUri = mArrayUri;
//    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }


    View itemView;
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        itemView = null;
        if (inflater != null) {
            itemView = inflater.inflate(R.layout.gv_item_image_and_delete_btn, parent, false);
        }

        btn_delete_image = itemView.findViewById(R.id.btn_delete_image);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);

        ivGallery.setImageURI(mArrayUri.get(position));

        Uri uri= mArrayUri.get(position);

        btn_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mArrayUri.remove(uri);
                }catch (Exception e){

                }
                notifyDataSetChanged();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ctx.getApplicationContext(), ""+mArrayUri.get(position) , Toast.LENGTH_LONG).show();
                popup_winodw(mArrayUri.get(position).toString(),view);
            }
        });


        return itemView;
    }
    public void popup_winodw(String url_image, View v){
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        image_popup=popupView.findViewById(R.id.image_popup);
//        image_popup.setImageURI(url_image);

        Glide.with(popupView)
                .load(url_image)
                .thumbnail(Glide.with(ctx).load(R.raw.loading_4))
//                .apply(new RequestOptions().placeholder(R.drawable.loading_4))
                .into(image_popup);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //Picasso.get().load(url_image).placeholder( R.drawable.loading_4 ).into(image_popup);

        button_close = popupView.findViewById(R.id.close_btn_popup);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
    }



}
