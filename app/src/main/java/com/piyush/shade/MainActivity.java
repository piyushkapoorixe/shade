package com.piyush.shade;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.lang.String;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //public static final int REQUEST_CODE = 101;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList, clickPost; // This view is for showing the posts in android device screen
    private Toolbar mToolbar;  // used to bring in the navigation menu
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CircleImageView NavProfileImage; // diplaying profile image of user
    private TextView NavProfileUserName, userProfName; // display name of user below profile image

    private ImageButton AddNewPostButton;  // button to add new post

    //to check user authentication
    private FirebaseAuth mAuth;
    private DatabaseReference profileUserRef;


    private DatabaseReference UsersRef, PostsRef;

    public String currentUserID;
    //String friendsUserId;
//    String keyvalue="gjhgjb";
//    //String keyvalue=;
//    String[] keyvalarr;
//    private String friendId = " ";

//    public void fetuserfriend(){
//        UsersRef.child(currentUserID).child("Friendid").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    keyvalue = dataSnapshot.getValue(String.class);
//                    //UsersRef.child(currentUserID).child("Friendid").setValue(keyvalue + curennt_user);
//                    //Log.e("Fetched ID", keyvalue);
//                }catch (Exception exp)
//                {
//                    //Log.e("Exeeption",exp);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        Log.e("Fetche",keyvalue);
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) { // casting done here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //keyvalarr=new String[20];
        mAuth = FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            SendUserToLoginActivity();
        }else{
            currentUserID = mAuth.getCurrentUser().getUid();
        }
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfName = (TextView) findViewById(R.id.welcome);


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);  // used to bring in the navigation menu
        setSupportActionBar(mToolbar); // setting it up in main activity to be displayed on android
        getSupportActionBar().setTitle("Home"); // Title for the toolbar

        AddNewPostButton = (ImageButton) findViewById(R.id.add_new_post_button);  // button to add new post


        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout); // drawer layout for drawing of the menu from left
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close); // check strings.xml in values folder
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view); // for navigation menu


        // show all posts
        postList = (RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true); // new post at top and old at bottom
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);


        View navView = navigationView.inflateHeaderView(R.layout.navigation_header); // that header containing profile image and username

        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_header);

//        profileUserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String myProfileName = dataSnapshot.child("fullname").getValue().toString();
//                userProfName.setText(myProfileName);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
                                                 }

        // getting username and image from firebase
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {   //eyvalue = dataSnapshot.child("Friendid").getValue(String.class);

                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("fullname")) // validation for app crash - checking ki if this prop is there or not
                    {
                        // getting the full name from firebase database
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        // displaying on the nav header
                        NavProfileUserName.setText(fullname);
                    }

                    if (dataSnapshot.hasChild("profileimage"))
                    {
                        // getting the profile image from firebase database
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        // displaying on the nav header
                        //Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
            }
        });



        DisplayAllUsersPosts();


    }



//    private void DisplayAllUsersPosts()
//    {
//
//        FirebaseRecyclerOptions<Posts> options =
//                new FirebaseRecyclerOptions.Builder<Posts>()
//                .setQuery(PostsRef, Posts.class)
//                .build();
//
//        FirebaseRecyclerAdapter<Posts, PostsViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) // Posts.class, R.layout.all_posts_layout, PostsViewHolder.class, PostsRef
//                {
//                    @Override
//                    protected void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, int i, @NonNull Posts posts)
//                    {
//                        postsViewHolder.username.setText(posts.getFullname());
//                        postsViewHolder.time.setText(" " + posts.getTime());
//                        postsViewHolder.date.setText(" " + posts.getDate());
//                        postsViewHolder.description.setText(posts.getDescription());
//                        Picasso.get().load(posts.getProfileimage()).into(postsViewHolder.user_post_image); // .placeholder(R.drawable.profile_image
//                        Picasso.get().load(posts.getPostimage()).into(postsViewHolder.postImage);
//                    }
//
//                    @NonNull
//                    @Override
//                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout, parent, false);
//                        PostsViewHolder viewHolder = new PostsViewHolder(view);
//                        /*return new PostsViewHolder(view);*/ return viewHolder;
//                    }
//                };
//        adapter.startListening();
//        postList.setAdapter(adapter);
//
//
//    }
//
//
//
//    public static class PostsViewHolder extends RecyclerView.ViewHolder
//    {
//
//
//        TextView username, time, date, description;
//        CircleImageView user_post_image;
//        ImageView postImage;
//
//        public PostsViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//
//            username = itemView.findViewById(R.id.post_user_name);
//            time = itemView.findViewById(R.id.post_time);
//            date = itemView.findViewById(R.id.post_date);
//            description = itemView.findViewById(R.id.post_description);
//
//            user_post_image = itemView.findViewById(R.id.post_profile_image);
//            postImage = itemView.findViewById(R.id.post_image);
//        }
//
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this,"check it "+friendId,Toast.LENGTH_SHORT).show();
    }

    private void DisplayAllUsersPosts()
    {

//        Query query;
//
//        if (friendId.contains(currentUserID)){
//            query = PostsRef.orderByChild("uid").startAt(friendId).endAt(currentUserID + "\uf8ff");
//        }
//        else

            Query query = PostsRef.orderByChild("Friendid").startAt(currentUserID).endAt(currentUserID + "\uf8ff");

            FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                            (
                                    Posts.class,
                                    R.layout.all_posts_layout,
                                    PostsViewHolder.class,
                                    //PostsRef
                                    // for showing posts of only the user
                                    query
                                    //PostsRef.orderByChild("Friendid").startAt(currentUserID).endAt(currentUserID + "\uf8ff")
                                    //UsersRef.orderByChild("Friendid").startAt(currentUserID).endAt(currentUserID + "\uf8ff")
                            ) {
                        @Override
                        protected void populateViewHolder(PostsViewHolder viewHolder, Posts model, int position) {

                            //final String PostKey = getRef(position).getKey();

                            viewHolder.setFullname(model.getFullname());
                            viewHolder.setTime(model.getTime());
                            viewHolder.setDate(model.getDate());
                            viewHolder.setDescription(model.getDescription());
                            viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());
                            viewHolder.setPostimage(getApplicationContext(), model.getPostimage());

//                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent clickPostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
//                                clickPostIntent.putExtra("PostKey", PostKey);
//                                startActivity(clickPostIntent);
//                            }
//                        });
                        }
                    };
            postList.setAdapter(firebaseRecyclerAdapter);

        //firebaseRecyclerAdapter.startListening();
    }



    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public PostsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.with(ctx).load(profileimage).into(image);
        }

        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(Context ctx1,  String postimage)
        {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx1).load(postimage).into(PostImage);
        }
    }



    private void SendUserToPostActivity()
    {
        Intent addNewPostIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(addNewPostIntent);
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }



    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }



    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void UserMenuSelector(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_post:
                SendUserToPostActivity();
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                SendUserToProfileActivity();
                break;

            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_friends:
                Toast.makeText(this, "Friend List", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_find_friends:
                SendUserToFindFriendsActivity();
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;

//            case R.id.nav_messages:
//                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
//                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK){
//            friendId = data.getStringExtra("friendIdKey");
//            Toast.makeText(this,friendId,Toast.LENGTH_SHORT).show();
//            Log.e("TAG",friendId);
//        }
//    }

    private void SendUserToFindFriendsActivity()
    {
        Intent findfriendsintent = new Intent(MainActivity.this, FindFriendsActivity.class);
        //startActivityForResult(findfriendsintent,REQUEST_CODE);
        startActivity(findfriendsintent);


    }

    private void SendUserToProfileActivity()
    {
        Intent profileintent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profileintent);
    }
}