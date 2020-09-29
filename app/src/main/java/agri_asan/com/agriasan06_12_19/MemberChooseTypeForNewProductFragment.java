package agri_asan.com.agriasan06_12_19;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MemberChooseTypeForNewProductFragment extends Fragment {
    ImageView seed;
    ImageView medicine;
    ImageView keray;

    ImageView machines;
    ImageView sabzi;
    ImageView khaad;

    String Vendor_Name;
    String Vendor_City;
    String Vendor_ID_Card;
    String Vendor_ID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_member_choose_type_of_new_product, container, false);

        Vendor_Name = getArguments().getString("Vendor_Name");
        Vendor_City = getArguments().getString("Vendor_City");
        Vendor_ID = getArguments().getString("Vendor_ID");
        Vendor_ID_Card = getArguments().getString("Vendor_ID_Card");

        seed = root.findViewById(R.id.beej_iksaam_button);
        medicine = root.findViewById(R.id.fasal_adoyaat_button);
        keray = root.findViewById(R.id.keray_mar_button);
        machines = root.findViewById(R.id.machines_button);
        sabzi = root.findViewById(R.id.sabzi_button);
        khaad = root.findViewById(R.id.khaad_iksaam_button);
//        Toast.makeText(root.getContext().getApplicationContext(), ""+Vendor_ID_Card+"  "+Vendor_City+"  "+Vendor_ID, Toast.LENGTH_LONG).show();

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

        return root;
    }

    private void saving_medicine_type(String type_of_product){
        Toast.makeText(getContext().getApplicationContext(), ""+type_of_product, Toast.LENGTH_LONG).show();
        Fragment fragment = new MemberAddProductFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("type_of_product", type_of_product);
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