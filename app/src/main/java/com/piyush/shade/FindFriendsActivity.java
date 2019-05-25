package com.piyush.shade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

//import android.widget.Toolbar;



public class FindFriendsActivity extends AppCompatActivity {

//    public class fetchfriendid {
//        FindFriendsActivity friend_id = new FindFriendsActivity();
//        String fetching(){
//            return friend_id.returnFriendId();
//        }
//    }

    private Toolbar mToolbar;

    private Intent mainActivityIntent;

    private ImageButton SearchButton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;

    private DatabaseReference allUsersDatabaseRef, PostsRef;

    //to check user authentication
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName;


    String currentUserID;

    //private DatabaseReference UsersRef;

    //public String friendsUserId;

//    public String returnFriendId(){
//        return friendsUserId;
//    }

    private Button friendButton, familyButton;
    TextView yourId, yourIdfamily;
    public   String curennt_user, curennt_user_family;
    HashMap user_Map = new HashMap();
    String keyvalue, keyvaluefamily;
    public void fetchuser(View view) {
        //String keyvalue;
        yourId = (TextView)findViewById(R.id.user_ID);
        curennt_user = yourId.getText().toString();
        //Log.e("User id", curennt_user);
        //keyvalue=curennt_user;

        //Log.e("User id", currentUserID);
        //keyvalue=allUsersDatabaseRef.child(currentUserID).child("Friendid").getKey();
        //Log.e("Id append", keyvalue) ;
        //user_Map.put("friendid", curennt_user);
        //allUsersDatabaseRef.child(currentUserID).child("Friendid").get(curennt_user);


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        /*PostsRef.child(currentUserID).child("Friendid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    keyvalue = dataSnapshot.getValue(String.class);
                    Log.e("Pre Fetched",keyvalue);
                    PostsRef.child(currentUserID).child("Friendid").setValue(keyvalue + curennt_user);
                    mainActivityIntent.putExtra("friendIdKey pre",keyvalue);
                    setResult(RESULT_OK,mainActivityIntent);
                    finish();
                    Log.e("Fetched ID 1", keyvalue);
                }catch (Exception exp)
                {
                    //Log.e("Exeeption",exp);
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        PostsRef.child(currentUserID).child("Friendid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    keyvalue = dataSnapshot.getValue(String.class);

                    PostsRef.child(currentUserID).child("Friendid").setValue(curennt_user + "-" + keyvalue);
                    mainActivityIntent.putExtra("friendIdKey",keyvalue);
//                    setResult(RESULT_OK,mainActivityIntent);
//                    finish();
                    Log.e("Fetched ID 2", keyvalue);
                }catch (Exception exp)
                {
                    //Log.e("Exeeption",exp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      //  allUsersDatabaseRef.child(currentUserID).child("Friendid").setValue(keyvalue+curennt_user);

    }







    public void fetchuserfamily(View view) {
        //String keyvalue;
        yourIdfamily = (TextView)findViewById(R.id.user_ID);
        curennt_user_family = yourIdfamily.getText().toString();
        //Log.e("User id", curennt_user);
        //keyvalue=curennt_user;

        //Log.e("User id", currentUserID);
        //keyvalue=allUsersDatabaseRef.child(currentUserID).child("Friendid").getKey();
        //Log.e("Id append", keyvalue) ;
        //user_Map.put("friendid", curennt_user);
        //allUsersDatabaseRef.child(currentUserID).child("Friendid").get(curennt_user);


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        /*PostsRef.child(currentUserID).child("Friendid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    keyvalue = dataSnapshot.getValue(String.class);
                    Log.e("Pre Fetched",keyvalue);
                    PostsRef.child(currentUserID).child("Friendid").setValue(keyvalue + curennt_user);
                    mainActivityIntent.putExtra("friendIdKey pre",keyvalue);
                    setResult(RESULT_OK,mainActivityIntent);
                    finish();
                    Log.e("Fetched ID 1", keyvalue);
                }catch (Exception exp)
                {
                    //Log.e("Exeeption",exp);
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        PostsRef.child(currentUserID).child("Friendid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    keyvaluefamily = dataSnapshot.getValue(String.class);

                    PostsRef.child(currentUserID).child("Friendid").setValue(curennt_user_family + "/"+ keyvaluefamily);
                    //mainActivityIntent.putExtra("friendIdKey",keyvaluefamily);
//                    setResult(RESULT_OK,mainActivityIntent);
//                    finish();
                    Log.e("Fetched ID family", keyvaluefamily);
                }catch (Exception exp)
                {
                    //Log.e("Exeeption",exp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //  allUsersDatabaseRef.child(currentUserID).child("Friendid").setValue(keyvalue+curennt_user);

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yourId = (TextView)findViewById(R.id.user_ID);
        setContentView(R.layout.activity_find_friends);

        mainActivityIntent = new Intent();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        mToolbar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find Friends"); // Name in the header

        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
        SearchInputText = (EditText) findViewById(R.id.search_box_input);

//        friendButton = (Button) findViewById(R.id.add_friend);
//        friendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //FindFriendsViewHolder obj = new FindFriendsViewHolder();
//                friendsUserId = "J5dP30W7nJVuTKFSIdTovHNMDqb2";
//            }
//        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchBoxInput = SearchInputText.getText().toString();

                SearchPeopleAndFriends(searchBoxInput);
            }
        });
    }

    private void SearchPeopleAndFriends(String searchBoxInput)
    {

        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query searchPeopleAndFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>
                (
                        FindFriends.class,
                        R.layout.all_users_display_layout,
                        FindFriendsViewHolder.class,
                        searchPeopleAndFriendsQuery
                ) {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder findFriendsViewHolder, FindFriends findFriends, int i) {
                findFriendsViewHolder.setFullname(findFriends.getFullname());
                findFriendsViewHolder.setStatus(findFriends.getStatus());
                findFriendsViewHolder.setUserid(findFriends.getUserid());
                findFriendsViewHolder.setProfileimage(getApplicationContext(), findFriends.getProfileimage());
            }
        };
        SearchResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        //static String friendtemp;
//        CircleImageView my_image;
//        TextView myName, myStatus;


        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

//            my_image = itemView.findViewById(R.id.all_users_profile_image);
//            myName = itemView.findViewById(R.id.all_users_profile_full_name);
//            myStatus = itemView.findViewById(R.id.all_users_status);
        }



        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname)
        {
            TextView myName = (TextView) mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);
        }

        public void setStatus(String status)
        {
            TextView myStatus = (TextView) mView.findViewById(R.id.all_users_status);
            myStatus.setText(status);
        }

        public void setUserid(String userid)
        {
            TextView yourID = (TextView) mView.findViewById(R.id.user_ID);
            TextView yourIDfamily = (TextView) mView.findViewById(R.id.user_ID);
            //friendtemp = yourID.toString();
            yourID.setText(userid);
            yourIDfamily.setText(userid);

//            Button friendButton = (Button) mView.findViewById(R.id.add_friend);
//            yourID.setText(userid);
//
//            Button familyButton = (Button) mView.findViewById(R.id.add_family);
//            yourID.setText(userid);
        }


    }

//    private void SearchPeopleAndFriends(String searchBoxInput)
//    {
//
//        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();
//
//        Query searchPeopleAndFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
//                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");
//
//        FirebaseRecyclerOptions<FindFriends> options =
//                new FirebaseRecyclerOptions.Builder<FindFriends>()
//                .setQuery(searchPeopleAndFriendsQuery, FindFriends.class)
//                .build();
//
//        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
//                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull FindFriendsViewHolder findFriendsViewHolder, int i, @NonNull FindFriends findFriends)
//            {
//                findFriendsViewHolder.myName.setText(findFriends.getFullname());
//                findFriendsViewHolder.myStatus.setText(findFriends.getStatus());
//                Picasso.with(FindFriendsActivity.this).load(findFriends.getProfileimage()).into(findFriendsViewHolder.my_image);
//
//            }
//
//            @NonNull
//            @Override
//            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
//                FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
//                return viewHolder;
//            }
//        };
//        firebaseRecyclerAdapter.startListening();
//        SearchResultList.setAdapter(firebaseRecyclerAdapter);
//
//    }
//
//
//    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
//    {
//        CircleImageView my_image;
//        TextView myName, myStatus;
//
//
//        public FindFriendsViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//
//            my_image = itemView.findViewById(R.id.all_users_profile_image);
//            myName = itemView.findViewById(R.id.all_users_profile_full_name);
//            myStatus = itemView.findViewById(R.id.all_users_status);
//        }
//    }


}


