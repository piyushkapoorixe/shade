package com.piyush.shade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
//import android.widget.Toolbar;



public class PostActivity extends AppCompatActivity {
    int flag=0;
    private Toolbar mToolbar;
    private ImageButton SelectPostImage;
    private Button UpdatePostButton, UpdatePostButtonForSelf;
    private EditText PostDescription;

    private String Description;

    // reference to firebase storage
    private StorageReference PostsImagesReference;

    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl = "https://firebasestorage.googleapis.com/v0/b/shadenetwork.appspot.com/o/posts%2Fimage%3A1990921-05-201920%3A57.jpg?alt=media&token=08b81036-de1b-4fba-add8-aae369935494", current_user_id;

    

    private Uri ImageUri;

    private static final int Gallery_Pick = 1; // picking image from gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsImagesReference = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        SelectPostImage = (ImageButton) findViewById(R.id.select_post_image);
        PostDescription = (EditText) findViewById(R.id.post_description);
        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        UpdatePostButtonForSelf = (Button) findViewById(R.id.self_update_post_button);

        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); // for back arrow
        getSupportActionBar().setTitle("Update Post"); // Name in the header


        // making user go to gallery on selecting the update post image
        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

        UpdatePostButtonForSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                ValidatePostInfo();
            }
        });

    }

    private void ValidatePostInfo()
    {
        // getting the description from the 'whats on your mind'
        Description = PostDescription.getText().toString();

        // if image is not selected
        if (ImageUri == null)
        {
            Toast.makeText(PostActivity.this, "Select Post image first...what you doing dude huh?", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(PostActivity.this, "Write description first...what you doing dude huh?", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage() {
        // have to give different names to the posts images which is a challenge
        // so with the image name we will append the date and time of post updation

        //getting time and date for the post updation

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        // saving post image to firebase storage
        // ImageUri.getLastPathSegment() is for real name
        StorageReference filePath = PostsImagesReference.child("posts").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        // actual saving of image to firebase storage
//        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
//            {
//                if (task.isSuccessful())
//                {
//                    // getting the image url from the storage
//                    downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
//
//                    Toast.makeText(PostActivity.this, "Wohoo...image saved successfully to firebase storage", Toast.LENGTH_SHORT).show();
//
//                    SavingPostInformationToDatabase();
//                }
//                else
//                {
//                    String message = task.getException().getMessage();
//                    Toast.makeText(PostActivity.this, "OOPS..error occured - " + message, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            //postRandomName
                            if(flag==0) {
                                PostsRef.child(current_user_id).child("postimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PostActivity.this, "Image success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PostActivity.this, "Image fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                PostsRef.child(current_user_id).child("postimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PostActivity.this, "Image success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PostActivity.this, "Image fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
//                            PostsRef.child(current_user_id + postRandomName).child("postimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(PostActivity.this, "Image success", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(PostActivity.this, "Image fail", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                        }
                    });

                    Toast.makeText(PostActivity.this, "Image uploaded successfully to storage", Toast.LENGTH_SHORT).show();
                    SavingPostInformationToDatabase();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void SavingPostInformationToDatabase()
    {
        // getting fullname and profile image of the user from firebase database
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // checking if child exists
                if (dataSnapshot.exists())
                {
                    // getting full name and profile image of the user from the firebase database
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();

                    // saving post info in firebase database
                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("Friendid",current_user_id);
                    postsMap.put("Familyid",current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("postimage", downloadUrl);
                    postsMap.put("profileimage", userProfileImage);
                    postsMap.put("fullname", userFullName);

                    //saving
                    if(flag==0) {
                        PostsRef.child(current_user_id).updateChildren(postsMap) // + postRandomName
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            //SendUserToMainActivity();
                                            SendUserToFindFriendsActivity();
                                            Toast.makeText(PostActivity.this, "Post Added Successfully...wow!!!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(PostActivity.this, "OOPS..error occured - " + msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap) // + postRandomName
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            SendUserToMainActivity();
                                            //SendUserToFindFriendsActivity();
                                            Toast.makeText(PostActivity.this, "Post Added Successfully...wow!!!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(PostActivity.this, "OOPS..error occured - " + msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    // using the code from setup activity
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            // display image in the alllocated space for post
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        // send user to main activity if back arrow pressed
        if (id ==  android.R.id.home)
        {
            SendUserToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity()
    {

        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(mainIntent);

    }

    private void SendUserToFindFriendsActivity()
    {
        Intent findfriendsintent = new Intent(PostActivity.this, FindFriendsActivity.class);
        startActivity(findfriendsintent);


    }
}
