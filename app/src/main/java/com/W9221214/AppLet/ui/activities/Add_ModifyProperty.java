package com.W9221214.AppLet.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.W9221214.AppLet.model.AppLet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.W9221214.AppLet.R;
import com.W9221214.AppLet.adapters.MediaDisplayAdapter;
import com.W9221214.AppLet.repository.Repository;
import com.W9221214.AppLet.utils.Constants;
import com.W9221214.AppLet.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


import static com.W9221214.AppLet.utils.Constants.Status.AVAILABLE;
import static com.W9221214.AppLet.utils.Constants.Status.OCCUPIED;


public class Add_ModifyProperty extends AppCompatActivity
        implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    private AppLet appLet;
    private EditText longDescription;
    private EditText title;
    private EditText numOfRooms;
    private EditText location;
    private EditText areaSqft;
    private EditText price;
    private EditText numOfBedrooms;
    private EditText contact;
    private RecyclerView mediaRecyclerView;
    private MediaDisplayAdapter mediaDisplayAdapter;

    private Repository repository;
    private boolean updating;
    private RadioButton occupiedRadio;
    private RadioButton availableRadio;
    private Uri filePath;
    private StorageReference storageReference;
    private TextView priceTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setToolbar();
        appLet = getIntent().getParcelableExtra(Constants.BundleKeys.APPLET_OBJECT_KEY);
        repository = new Repository(Add_ModifyProperty.this);
        setViews();

        setParams();
    }

    private void setViews() {
        priceTextView = findViewById(R.id.activity_add_modify_price_text_view);

        longDescription = findViewById(R.id.activity_add_modify_description_edit_text);

        mediaRecyclerView = findViewById(R.id.activity_add_modify_media_recycler_view);

        title = findViewById(R.id.activity_add_modify_edit_text);
        numOfRooms = findViewById(R.id.activity_add_modify_num_of_rooms_edit_text);
        areaSqft = findViewById(R.id.activity_add_modify_area_edit_text);
        location = findViewById(R.id.activity_add_modify_location);
        Button submitBtn = findViewById(R.id.activity_add_modify_submit_btn);

        price = findViewById(R.id.activity_add_modify_price);
        numOfBedrooms = findViewById(R.id.activity_add_modify_num_of_bedrooms);
        contact = findViewById(R.id.activity_add_modify_contact_number);
        occupiedRadio = findViewById(R.id.activity_add_modify_occupied_radio);
        availableRadio = findViewById(R.id.activity_add_modify_available_radio);

        Button selectPicFromInternalStorageBtn = findViewById(R.id
                .activity_add_modify_choose_picture);

        submitBtn.setOnClickListener(this);

        selectPicFromInternalStorageBtn.setOnClickListener(this);
    }




    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.add_modify);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setParams() {
        if (appLet != null) {
            updating = true;

            longDescription.setText(appLet.getLongDescription());
            int numberOfRooms = appLet.getNumberOfRooms();
            if (numberOfRooms > 0) {
                numOfRooms.setText(String.valueOf(numberOfRooms));
            }

            title.setText(appLet.getTitle());
            int Area = appLet.getArea();
            if (Area > 0) {
                areaSqft.setText(String.valueOf(Area));
            }

            try {
                String priceString = appLet.getPrice();

                int priceInt = Integer.valueOf(priceString);
                if (priceInt > 0) {
                    price.setText(String.valueOf(priceInt));
                }
            } catch (Exception ignored) {
            }


            location.setText(appLet.getAddress());
            int numbOfBedRooms = appLet.getNumbOfBedRooms();
            if (numbOfBedRooms > 0) {
                numOfBedrooms.setText(String.valueOf(numbOfBedRooms));
            }

            if(appLet.getContact()>0) {
                contact.setText(String.valueOf(appLet.getContact()));
            }

            if (appLet.getStatus()!=null && appLet.getStatus().equals(OCCUPIED)) {
                occupiedRadio.setChecked(true);
                availableRadio.setChecked(false);

            }
        } else {
            updating = false;
            appLet = new AppLet();
            appLet.setPhotos(new ArrayList<String>());

        }

        setMediaRecyclerView();



    }

    private void setMediaRecyclerView() {
        mediaDisplayAdapter = new MediaDisplayAdapter(appLet.getPhotos(), true
                , getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        mediaRecyclerView.setLayoutManager(layoutManager);

        mediaDisplayAdapter.setOnDeleteIconListener(new MediaDisplayAdapter.ItemDeleteListener() {
            @Override
            public void deleteIconClicked(int position) {
                appLet.getPhotos().remove(position);
                mediaDisplayAdapter.notifyDataSetChanged();
            }
        });

        mediaRecyclerView.setAdapter(mediaDisplayAdapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.activity_add_modify_submit_btn:
                submitProperty();
                break;

            case R.id.activity_add_modify_choose_picture:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }

    private void addMedia(String url) {
        if (url.isEmpty()) {
            Toast.makeText(this, "You must add an url", Toast.LENGTH_SHORT).show();
        } else {
            appLet.getPhotos().add(url);
            mediaDisplayAdapter.notifyDataSetChanged();
            Objects.requireNonNull(mediaRecyclerView.getLayoutManager())
                    .scrollToPosition(appLet.getPhotos().size() - 1);

        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    addMedia(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }



    private void submitProperty() {
        List<String> photos = appLet.getPhotos();
        if (photos == null || photos.size() < 1) {
            Toast.makeText(getBaseContext(), getString(R.string.you_must_add_at_least)
                    , Toast.LENGTH_SHORT).show();
        } else {
            try {
                appLet.setDatePutInMarket(System.currentTimeMillis());
                if (occupiedRadio.isChecked()) {

                    appLet.setStatus(OCCUPIED);
                } else {
                    appLet.setStatus(AVAILABLE);
                }

                String priceString = price.getText().toString();


                Integer ContactInfo;
                try {
                    ContactInfo = Integer.valueOf(contact.getText().toString());
                } catch (Exception ignored) {
                    ContactInfo = -1;
                }
                appLet.setContact(ContactInfo);


                appLet.setPrice(priceString);
                Integer numOfBedInt;
                try {
                    numOfBedInt = Integer.valueOf(numOfBedrooms.getText().toString());
                } catch (Exception ignored) {
                    numOfBedInt = -1;
                }
                appLet.setNumbOfBedRooms(numOfBedInt);
                appLet.setTitle(title.getText().toString());

                appLet.setLongDescription(longDescription.getText().toString());
                int areaSqftInt;
                try {
                    areaSqftInt = Integer.valueOf(areaSqft.getText().toString());
                } catch (Exception e) {
                    areaSqftInt = -1;
                }
                int numOfRoomsInt;
                try {
                    numOfRoomsInt = Integer.valueOf(numOfRooms.getText().toString());
                } catch (Exception e) {
                    numOfRoomsInt = -1;
                }
                int numOfBedroomsInt;
                try {
                    numOfBedroomsInt = numOfBedInt;
                } catch (Exception e) {
                    numOfBedroomsInt = -1;
                }

                appLet.setNumbOfBedRooms(numOfBedroomsInt);
                appLet.setArea(areaSqftInt);
                appLet.setNumberOfRooms(numOfRoomsInt);
                appLet.setAddress(location.getText().toString());

                String user_email =  Objects.requireNonNull(FirebaseAuth.getInstance()
                        .getCurrentUser()).getEmail();
                appLet.setEmail(user_email);
                if (updating) {
                    String title = appLet.getLongDescription() + " " + getString(R.string.updated);
                    String message = getString(R.string.property_updated);
                    Utils.createNotification(getApplicationContext(), title, message);
                    repository.updateListing(appLet);
                } else {
                    String title = appLet.getLongDescription() + " " + getString(R.string.added);
                    String message = getString(R.string.property_added);
                    Utils.createNotification(getApplicationContext(), title, message);
                    repository.insertListing(appLet);
                }
                goToNavigationActivity();
            } catch (Exception e) {
                Toast.makeText(Add_ModifyProperty.this, getString(R.string.error)
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void goToNavigationActivity() {
        Intent intent = new Intent(Add_ModifyProperty.this
                , Navigation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
