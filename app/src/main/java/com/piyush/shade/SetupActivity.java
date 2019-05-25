package com.piyush.shade;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText Username, FullName, CountryName, DobName, GenderName, RelationshipName, StatusName;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private CheckBox Consent;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserId;

    FindFriends ffi = new FindFriends();

    final static int Gallery_Pick = 1; // picking image from gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images"); // folder Profile Images to be created in firebase storage

        Username = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        CountryName = (EditText) findViewById(R.id.setup_country_name);
        DobName = (EditText) findViewById(R.id.setup_dob);
        GenderName = (EditText) findViewById(R.id.setup_gender);
        StatusName = (EditText) findViewById(R.id.setup_status);
        RelationshipName = (EditText) findViewById(R.id.setup_relationship_status);
        SaveInformationButton = (Button) findViewById(R.id.setup_information_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        Consent = (CheckBox) findViewById(R.id.setup_consent_checkbox);


        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });

        // redirecting user to phone gallery
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        // displaying profile image
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileimage")) // validation for app crash - checking ki if this prop is there or not
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(SetupActivity.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Profile image do not exists...", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

     // for profile image getting and allowing to crop
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null) {
            Uri ImageUri = data.getData();
                    CropImage.activity(ImageUri) // this ImageUri makes the option of files/gallery etc not come
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);
        }

        // getting uri of cropped image
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            // when cropping is done successfully
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri(); // getting uri of cropped image

                // in the folder Profile Images create a path and using the id of user we store the image
                final StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");

                // putting the cropped image in the Profile Images folder
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful())
//                        {
//                            //for firebase storage
//                            Toast.makeText(SetupActivity.this, "Profile Image storage successful...", Toast.LENGTH_SHORT).show();
//
//                            // getting the url from firebase storage
//                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
//
//                            // storing the image reference to firebase database
//                            UsersRef.child("profileimage").setValue(downloadUrl)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful())
//                                            {
//                                                // sending user to setup activity again after image stored to firebase db for the user to fill in other info in the setup activity
//                                                Intent selfIntent = new Intent(SetupActivity.this, SetupActivity.class);
//                                                startActivity(selfIntent);
//
//                                                //for firebase database
//                                                Toast.makeText(SetupActivity.this, "Profile Image stored to firebase db successfully...", Toast.LENGTH_SHORT).show();
//                                            }
//                                            else
//                                            {
//                                                String message = task.getException().getMessage();
//                                                Toast.makeText(SetupActivity.this, "OOPS...Something went wrong :- " + message, Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//                                    });
//                        }
//                    }
//                });

                //alternative for the above one - error was that image was not coming up in the setup activity


                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Toast.makeText(SetupActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = downUri.toString();
                            UsersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(SetupActivity.this, SetupActivity.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(SetupActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();

                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });
            }

            // when cropping is not done successfully
            else
            {
                Toast.makeText(SetupActivity.this, "OOPS...Error Occured - Image cropping failure...try again", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void SaveAccountSetupInformation() {
        // for consent checkbox
        String msg = "";
        if (Consent.isChecked()) {
            msg = msg + "Given";
        } else {
            msg = msg + "Not Given";
        }

        String username = Username.getText().toString();
        String fullname = FullName.getText().toString();
        String countryname = CountryName.getText().toString();
        String gendername = GenderName.getText().toString();
        String dobname = DobName.getText().toString();
        String relationshipname = RelationshipName.getText().toString();
        String statusname = StatusName.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Provide Username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this, "Please Provide Fullname", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(countryname)) {
            Toast.makeText(this, "Please Provide Country name", Toast.LENGTH_SHORT).show();
        } else {
            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("userid", currentUserId);
            userMap.put("Friendid",currentUserId);
            userMap.put("consent", msg);
            userMap.put("fullname", fullname);
            userMap.put("countryname", countryname);
            userMap.put("status", statusname);
            userMap.put("gender",  gendername);
            userMap.put("dob", dobname);
            userMap.put("relationshipstatus", relationshipname);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToMainActivity();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "OOPS...Something went wrong:- "+ message , Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
