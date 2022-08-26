package com.example.hdida.firebaseauth.frags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hdida.firebaseauth.Upload;
import com.example.hdida.firebaseauth.User;
import com.example.hdida.firebaseauth.NetworkConnection;
import com.example.hdida.firebaseauth.R;
import com.example.hdida.firebaseauth.DrawerActivity;
import com.example.hdida.firebaseauth.SendMail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {

    ImageView pImage;
    private TextView name, price, seller, sellDate, Desc_tag, Desc_text;
    private Button button_make_offer, button_message, button_delete;
    boolean mItemClicked = false;
    private String sName, sEmail, pName, bName, bEmail;
    private int position;
    private String key;
    int imagePosition;
    String stringImageUri;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    DatabaseReference userDatabase;
    private List<User> mUser;
    private List<Upload> mUploads;
    private TextView string_villes, string_carbu, string_marque,string_model, string_puissance, string_boite, string_nbporte, string_main, string_kilo;
    private TextView string_annee, string_tel,string_options;

    @Override
    public void onStart() {
        super.onStart();
        NetworkConnection networkConnection = new NetworkConnection();
        if (networkConnection.isConnectedToInternet(getActivity())
                || networkConnection.isConnectedToMobileNetwork(getActivity())
                || networkConnection.isConnectedToWifi(getActivity())) {

        } else {
            networkConnection.showNoInternetAvailableErrorDialog(getActivity());
            return;
        }
        String testEmail = mAuth.getInstance().getCurrentUser().getEmail();
        if (testEmail.equals(sEmail)) {
            button_make_offer.setVisibility(View.GONE);
            button_message.setVisibility(View.GONE);
            button_delete.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "You are seller of this product", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buy, container, false);
        name = (TextView) v.findViewById(R.id.product_name);
        price = (TextView) v.findViewById(R.id.product_price);
        seller = (TextView) v.findViewById(R.id.product_seller);
        sellDate = (TextView) v.findViewById(R.id.product_date);
        button_make_offer = (Button) v.findViewById(R.id.offer_button);
        button_message = (Button) v.findViewById(R.id.msg_button);
        button_delete = (Button) v.findViewById(R.id.delete_button);

        string_villes = (TextView) v.findViewById(R.id.txtcity);
        string_carbu = (TextView) v.findViewById(R.id.txtcarbu);
        string_marque = (TextView) v.findViewById(R.id.txtmark);
        string_model = (TextView) v.findViewById(R.id.txtmodel);
        string_options= (TextView) v.findViewById(R.id.txtoptions);
        string_puissance = (TextView) v.findViewById(R.id.txtpower);

        string_boite = (TextView) v.findViewById(R.id.txtboite);
        string_nbporte = (TextView) v.findViewById(R.id.txtnbdoor);
        string_main = (TextView) v.findViewById(R.id.txtmain);
        string_kilo = (TextView) v.findViewById(R.id.txtkilo);
        string_annee = (TextView) v.findViewById(R.id.txtyear);
        string_tel = (TextView) v.findViewById(R.id.txtphone);


        pImage = (ImageView) v.findViewById(R.id.product_image);
        Desc_tag = (TextView) v.findViewById(R.id.Description_tag);
        Desc_text = (TextView) v.findViewById(R.id.Description);
        bName = mAuth.getInstance().getCurrentUser().getDisplayName();
        bEmail = mAuth.getInstance().getCurrentUser().getEmail();


        mUploads = new ArrayList<>();

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        Bundle bundle = getArguments();
        if (bundle != null) {

            position = bundle.getInt("position");
            pName = bundle.getString("name");
            String pImageUrl = bundle.getString("imageUrl");
            String pPrice = bundle.getString("price");
            sName = bundle.getString("userName");
            key = bundle.getString("key");
            String sstring_options=bundle.getString("selectedOptions");
            String sstring_villes = bundle.getString("string_villes");
            String sstring_carbu = bundle.getString("string_carbu");
            String sstring_marque = bundle.getString("string_marque");
            String sstring_model = bundle.getString("string_model");
            String sstring_puissance = bundle.getString("string_puissance");
            String sstring_boite = bundle.getString("string_boite");
            String sstring_nbporte = bundle.getString("string_nbporte");
            String sstring_main = bundle.getString("string_main");
            String sstring_kilo = bundle.getString("string_kilo");
            String sstring_annee = bundle.getString("string_annee");
            String sstring_tel = bundle.getString("string_tel");
            String date = bundle.getString("date");
            String desc = bundle.getString("desc");
            sEmail = bundle.getString("email");

            string_options.setText(sstring_options);
            string_model.setText(sstring_model);
            string_villes.setText(sstring_villes);
            string_carbu.setText(sstring_carbu);
            string_carbu.setText(sstring_carbu);
            string_marque.setText(sstring_marque);
            string_puissance.setText(sstring_puissance);
            string_boite.setText(sstring_boite);
            string_nbporte.setText(sstring_nbporte);
            string_main.setText(sstring_main);
            string_kilo.setText(sstring_kilo);
            string_annee.setText(sstring_annee);
            string_tel.setText(sstring_tel);

            name.setText(pName);
            price.setText( pPrice+" DH");
            seller.setText(sName);
            sellDate.setText(date);
            if (desc != null) {
                Desc_tag.setVisibility(View.VISIBLE);
                Desc_text.setVisibility(View.VISIBLE);
                Desc_text.setText(desc);
            }


            if (pImageUrl != null) {
                String photoUrl = pImageUrl;
                Glide.with(this)
                        .load(photoUrl)
                        .into(pImage);
            }


        }


        button_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", string_tel.getText().toString());
                smsIntent.putExtra("sms_body","Hi, i'm interested by your post in Hdida : "+name.getText().toString());
                startActivity(smsIntent);
            }
        });

        button_make_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String tel = "tel:" + string_tel.getText();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(tel)));

            }
        });

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert!");
                builder.setMessage("Deletion is permanent. Are you sure you want to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        return v;
    }

    private void deleteProduct() {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getActivity(), DrawerActivity.class));
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }

    private void sendEmailToSeller() {
        String email = sEmail;
        String subject = "[Hdida] Request for product " + pName;

        String msg = "unknown-user";
        if (bName != "")
            msg = bName;
        String thankMsg = "\n\nThank you for using Hdida :)";
        String autoMsg = "\n\nThis is an auto generated email. Please do not reply to this email.";

        String message = "Hey " + sName + ". " + msg + " is requesting for your product \"" + pName + "\". Wait for further response from " + msg + ". If you want you can write to " + msg + " on email id " + bEmail + " ." + thankMsg + autoMsg;
        SendMail sm2s = new SendMail(getActivity(), email, subject, message);
        sm2s.execute();
    }

    private void sendEmailToBuyer() {
        String email = bEmail;
        String subject = "[Hdida] Request Successful for " + pName;
        String thankMsg = "\n\nThank you for using Hdida :)";
        String autoMsg = "\n\nThis is an auto generated email. Please do not reply to this email.";

        String message = "Hello " + bName + ". You have requested " + sName + " for \"" + pName + "\". You can send message to " + sName + " in the app by clicking on message button." + thankMsg + autoMsg;
        SendMail sm2b = new SendMail(getActivity(), email, subject, message);
        sm2b.execute();
    }


}
