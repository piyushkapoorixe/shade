package com.piyush.shade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPostActivity extends AppCompatActivity {

    private ImageView ClickPostImage;
    private TextView ClickPostDescription;
    private Button EditPostButton, DeletePostButton;

    private DatabaseReference ClickPostRef;

    private String PostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        // receiving post key of the clicked post from the majn activity
        PostKey = getIntent().getExtras().get("PostKey").toString();
        Toast.makeText(this, "Key" + PostKey, Toast.LENGTH_SHORT).show();

        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("PostKey");

        ClickPostImage = (ImageView) findViewById(R.id.click_post_image);
        ClickPostDescription = (TextView) findViewById(R.id.click_post_description);
        EditPostButton = (Button) findViewById(R.id.edit_post);
        DeletePostButton = (Button) findViewById(R.id.delete_post);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // getting the description of the post
                String description = dataSnapshot.child("description").getValue().toString();

                // getting the postimage of the post
                String ClickImage = dataSnapshot.child("postimage").getValue().toString();

                // setting the description and postimage in activityclickpost.xml
                ClickPostDescription.setText(description);

                Picasso.with(ClickPostActivity.this).load(ClickImage).placeholder(R.drawable.profile_icon).into(ClickPostImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
