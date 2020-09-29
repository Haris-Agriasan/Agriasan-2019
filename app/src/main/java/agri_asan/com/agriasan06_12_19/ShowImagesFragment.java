package agri_asan.com.agriasan06_12_19;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ShowImagesFragment extends Fragment {



    private ListView listView_images;
    private GalleryAdapterImageString galleryAdapter;

    ArrayList<Uri> image_uris;
    ArrayList<String> image_strings;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_show_images, container, false);
        listView_images = root.findViewById(R.id.gv);

        image_strings = getArguments().getStringArrayList("images_list");

        galleryAdapter = new GalleryAdapterImageString(getContext().getApplicationContext(),image_strings);
        listView_images.setAdapter(galleryAdapter);


        return root;


    }

}