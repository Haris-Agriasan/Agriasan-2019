package agri_asan.com.agriasan06_12_19;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
/**
 * Created by Belal on 1/23/2018.
 */
public class DomainBazaarFragment extends Fragment {

    Resources resources;


    ImageView seed;
    ImageView medicine;
    ImageView keray;

    ImageView machines;
    ImageView sabzi;
    ImageView khaad;

    Button btn_become_a_seller;
    Button btn_view_products;
    DomainMainActivity domainMainActivity;

    RelativeLayout btn_layout_register_vendor;
    RelativeLayout btn_layout_seller_products;

    //layout_button_seller_products
    DatabaseReference reference;
    int c=0;

    String Vendor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bazaar, null);
        Vendor="";
        domainMainActivity=(DomainMainActivity)  getActivity();

        reference = FirebaseDatabase.getInstance().getReference();
        final Query query = reference.child("Domain_experts").orderByChild("Phone").equalTo(domainMainActivity.Phone);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Domain_experts user= issue.getValue(Domain_experts.class);
                        //Vendor=user.getVendor();
                        String verification="";
                        verification=verification+user.getVendor_Verification();
                        String t_vendor="";
                        t_vendor=t_vendor+user.getVendor();
                        if (t_vendor.equals("yes") && verification.equals("1")){
                            c++;
                            btn_layout_register_vendor.setVisibility(View.GONE);
                            btn_layout_seller_products.setVisibility(View.VISIBLE);

                        }else if(t_vendor.equals("yes") && verification.equals("0")){
                            btn_layout_seller_products.setVisibility(View.GONE);
                            btn_layout_register_vendor.setVisibility(View.GONE);
                        }
                        else {
                            btn_layout_register_vendor.setVisibility(View.VISIBLE);
                            btn_layout_seller_products.setVisibility(View.GONE);
                        }
                        //Toast.makeText(root.getContext(), ".." +Vendor, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Toast.makeText(root.getContext(), ""+mainActivity.Phone, Toast.LENGTH_LONG).show();
        //Toast.makeText(root.getContext(), "" +c, Toast.LENGTH_SHORT).show();

        seed = root.findViewById(R.id.beej_iksaam_button);
        medicine = root.findViewById(R.id.fasal_adoyaat_button);
        keray = root.findViewById(R.id.keray_mar_button);
        machines = root.findViewById(R.id.machines_button);
        sabzi = root.findViewById(R.id.sabzi_button);
        khaad = root.findViewById(R.id.khaad_iksaam_button);
        btn_become_a_seller=root.findViewById(R.id.btn_become_a_seller);
        btn_layout_register_vendor=root.findViewById(R.id.layout_button_register_vendor);
        btn_layout_seller_products=root.findViewById(R.id.layout_button_seller_products);
        btn_layout_register_vendor.setVisibility(View.GONE);
        btn_layout_seller_products.setVisibility(View.GONE);
        btn_view_products=root.findViewById(R.id.btn_view_products);

        btn_view_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(root.getContext(), "Test", Toast.LENGTH_LONG).show();
                function_call_view_inventory_domain_vendor();

            }
        });
        btn_become_a_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterVendorFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("name", domainMainActivity.Name);
                bundle.putString("phone", domainMainActivity.Phone);
                bundle.putString("city", domainMainActivity.City);
                bundle.putString("id", domainMainActivity.ID);
                bundle.putString("id_card", domainMainActivity.ID_Card);
                bundle.putString("user", domainMainActivity.User);
                bundle.putString("vendor", domainMainActivity.Vendor);
                //Toast.makeText(getApplicationContext(), ""+mainActivity.Vendor, Toast.LENGTH_LONG).show();

                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

//                Intent intent = new Intent(getActivity(), Register_Vendor.class);

//                startActivity(intent);
            }
        });

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
    private void saving_medicine_type(String medicine_type){
        Toast.makeText(getContext().getApplicationContext(), ""+medicine_type, Toast.LENGTH_LONG).show();
        Fragment fragment = new CustomerSeeProductsBazaarFragment();
        domainMainActivity.mTitle.setText(""+medicine_type);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("user","domain");
        bundle.putString("medicine_type", medicine_type);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "CustomerSeeProductBazaarFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void function_call_view_inventory_domain_vendor(){
        Fragment fragment = new DomainInventoryFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        domainMainActivity.mTitle.setText(R.string.aapki_products);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}