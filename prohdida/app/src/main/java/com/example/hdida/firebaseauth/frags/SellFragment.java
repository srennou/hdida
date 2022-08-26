package com.example.hdida.firebaseauth.frags;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hdida.firebaseauth.NetworkConnection;
import com.example.hdida.firebaseauth.R;
import com.example.hdida.firebaseauth.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SellFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CAMERA_REQUEST = 0;
    private Spinner spinner_villes, spinner_carbu, spinner_marque, spinner_model, spinner_puissance, spinner_boite, spinner_nbporte, spinner_main;
    private EditText edittext_kilo, edittext_annee, edittel;
    private Button mButtonChooseImage, mButtonUpload;
    private EditText mEditTextFileName, mEditTextFilePrice;
    private ImageView mImageView;
    private TextView mDescription;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    FirebaseAuth mAuth;
    Uri uri;
    private boolean[] checkedItems = new boolean[16];
    private String[] Options;
    private List<String> selectedOptions = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell, container, false);

        spinner_model = v.findViewById(R.id.spinner_model);
        spinner_villes = v.findViewById(R.id.spinner_city);
        spinner_carbu = v.findViewById(R.id.spinner_carbu);
        spinner_marque = v.findViewById(R.id.spinner_marque);
        spinner_puissance = v.findViewById(R.id.spinner_puissance);
        spinner_boite = v.findViewById(R.id.spinner_boite);
        spinner_nbporte = v.findViewById(R.id.spinner_nbporte);
        spinner_main = v.findViewById(R.id.spinner_main);
        edittext_kilo = v.findViewById(R.id.edittext_kilo);
        edittext_annee = v.findViewById(R.id.edittext_annee);
        edittel = v.findViewById(R.id.edittext_tel);
        spinner_model = v.findViewById(R.id.spinner_model);
        mButtonChooseImage = v.findViewById(R.id.button_choose_image);
        mButtonUpload = v.findViewById(R.id.button_upload);
        mEditTextFileName = v.findViewById(R.id.edit_text_file_name);
        mEditTextFilePrice = v.findViewById(R.id.edit_text_file_price);
        mImageView = v.findViewById(R.id.image_view);
        mProgressBar = v.findViewById(R.id.progress_bar);
        mDescription = v.findViewById(R.id.Description);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        v.findViewById(R.id.imgadd).setOnClickListener(view -> displayMultiSelectDialog(getActivity()));


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.abath_models, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_model.setAdapter(adapter);

        spinner_marque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {


                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.alfa_models, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(adapter);
                }
                if (position == 2) {

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.alpine_models, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(adapter);
                }
                if (position == 3) {


                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.aston_models, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(adapter);
                }
                if (position == 4) {


                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.audi_models, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.abath_models, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_model.setAdapter(adapter);

            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {

                    uploadFile();
                }


            }
        });


        return v;
    }

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
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //cropImage();

            Picasso.with(getActivity()).load(mImageUri).into(mImageView);
        }
    }

    private void cropImage() {
        try {
            Intent cropIntent;

            cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(mImageUri, "image/*");

            cropIntent.putExtra("crop", " true");
            cropIntent.putExtra("outputx", 180);
            cropIntent.putExtra("outputY", 180);
            cropIntent.putExtra("aspectx", 3);
            cropIntent.putExtra("aspecty", 4);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data ", true);

            startActivityForResult(cropIntent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        String stringvilles = spinner_villes.getSelectedItem().toString();
        String string_carbu = spinner_carbu.getSelectedItem().toString();
        String string_model = spinner_model.getSelectedItem().toString();
        String string_marque = spinner_marque.getSelectedItem().toString();
        String string_puissance = spinner_puissance.getSelectedItem().toString();
        String string_boite = spinner_boite.getSelectedItem().toString();
        String string_nbporte = spinner_nbporte.getSelectedItem().toString();
        String string_main = spinner_main.getSelectedItem().toString();
        String string_kilo = edittext_kilo.getText().toString().trim();
        String string_annee = edittext_annee.getText().toString().trim();
        String string_tel = edittel.getText().toString().trim();
        if (mAuth.getInstance().getCurrentUser().getDisplayName() == null) {
            ProfileFragment profileFragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("seller", 1);
            profileFragment.setArguments(bundle);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction().replace(R.id.frag_container, profileFragment)
                    .commit();

            return;
        }
        if (mEditTextFileName.getText().toString().trim().isEmpty()) {
            mEditTextFileName.setError("Name required");
            mEditTextFileName.requestFocus();
            return;
        }

        if (mEditTextFilePrice.getText().toString().trim().isEmpty()) {
            mEditTextFilePrice.setError("Price required");
            mEditTextFilePrice.requestFocus();
            return;
        }
        if (edittext_kilo.getText().toString().trim().isEmpty()) {
            edittext_kilo.setError("kilo required");
            edittext_kilo.requestFocus();
            return;
        }
        if (edittel.getText().toString().trim().isEmpty()) {
            edittel.setError("phone number required");
            edittel.requestFocus();
            return;
        }
        if (edittext_annee.getText().toString().trim().isEmpty()) {
            edittext_annee.setError("year model required");
            edittext_annee.requestFocus();
            return;
        }

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            mImageUri = null;
                            mImageView.setImageBitmap(null);
                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (selectedOptions.size() == 0) {
                                        selectedOptions.add("None");
                                    }

                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                            uri.toString(), mEditTextFilePrice.getText().toString().trim(), mDescription.getText().toString().trim(),
                                            stringvilles, string_carbu, string_marque, string_model, string_puissance, string_boite, string_nbporte,
                                            string_main, string_kilo, string_annee, string_tel,selectedOptions);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                    mEditTextFileName.setText("");
                                    mEditTextFilePrice.setText("");
                                    mDescription.setText("");
                                    edittext_kilo.setText("");
                                    edittext_annee.setText("");
                                    edittel.setText("");

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }


    private void displayMultiSelectDialog(Context c) {
        Options = getResources().getStringArray(R.array.Options_array);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
        dialogBuilder.setTitle("Select Options");
        dialogBuilder.setMultiChoiceItems(Options, checkedItems,
                (dialogInterface, which, isSelected) -> {
                    if (isSelected) {
                        selectedOptions.add(Options[which]);
                    } else {
                        selectedOptions.remove(Options[which]);
                    }
                }
        );

        dialogBuilder.setPositiveButton("Done", (dialog, which) -> showSelectedOptions(c));
        dialogBuilder.create().show();
    }

    private void showSelectedOptions(Context c) {
        Toast.makeText(c, selectedOptions.toString(), Toast.LENGTH_SHORT).show();
    }

}
