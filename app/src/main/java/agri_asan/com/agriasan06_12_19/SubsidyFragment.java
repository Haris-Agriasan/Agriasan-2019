package agri_asan.com.agriasan06_12_19;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static agri_asan.com.agriasan06_12_19.MainActivity.popupWindow;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Belal on 1/23/2018.
 */

public class SubsidyFragment extends Fragment {

    Resources resources;


    Button btn;

    private Context mContext;
    private Activity mActivity;

    private LinearLayout mRelativeLayout;

    private PopupWindow mPopupWindow;

    MainActivity mainActivity;
    DomainMainActivity domainMainActivity;
    Intent intent;
    String user_type="";

    View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        root= inflater.inflate(R.layout.fragment_subsidy, null);



        intent = getActivity().getIntent();

        user_type = intent.getStringExtra("User");

        if (user_type.equals("member")){
            mainActivity=(MainActivity) getActivity();
        }else if (user_type.equals("domain")){
            domainMainActivity=(DomainMainActivity) getActivity();
        }

        mContext = getContext();

        // Get the activity
        mActivity = getActivity();

        // Get the widgets reference from XML layout
        mRelativeLayout = root.findViewById(R.id.l1);


        return root;
    }
    public void onResume() {
        super.onResume();
        if (user_type.equals("member")){
            mainActivity.mTitle.setText("سبسڈی");
        }

        if (user_type.equals("domain")){
            domainMainActivity.mTitle.setText("سبسڈی");
        }
    }
}
