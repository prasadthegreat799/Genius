package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnAdOpened;
import com.appnext.core.callbacks.OnAdClicked;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button mRiddle,mFacts,mImagedRiddle,mChat,mEarn,mRiddleOftheday;
    private FirebaseAuth mfirebaseuser;
    private AdView mAdview;
    public Toolbar mToolbar;
    private TextView mUsername;
    private TextView mRiddleofthedaytxt,mRiddleofthedaynametxt;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseuser=FirebaseAuth.getInstance();
        mUsername=(TextView)findViewById(R.id.usernamemain);
        mRiddleofthedaytxt=(TextView)findViewById(R.id.riddleofthedayhome);
        mRiddleofthedaynametxt=(TextView)findViewById(R.id.riddleofthedayhomename);


        mToolbar=(Toolbar)findViewById(R.id.mytoolbar);
        mToolbar.setTitle("Genius");
        setSupportActionBar(mToolbar);

        mAdview=findViewById(R.id.mainadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        mRiddle=(Button)findViewById(R.id.riddlesbtn);
        mFacts=(Button)findViewById(R.id.factsbtn);
        mImagedRiddle=(Button)findViewById(R.id.imagedriddlesbtn);
        mEarn=(Button)findViewById(R.id.solveandearn);
        mChat=(Button)findViewById(R.id.chat);


        mRiddleOftheday=(Button)findViewById(R.id.riddleofthedaybtn);
        mRiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent riddleactivity=new Intent(MainActivity.this,RiddleActivity.class );
                        startActivity(riddleactivity);
            }
        });

        mFacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent factIntent=new Intent(MainActivity.this, MathriddleActivity.class);
                        startActivity(factIntent);
            }
        });

        mImagedRiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent imagedintent=new Intent(MainActivity.this, WhatriddleActivity.class);
                        startActivity(imagedintent);
            }
        });

        mEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent earnIntent=new Intent(MainActivity.this,EarnActivity.class);
                        startActivity(earnIntent);
            }
        });

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent chatIntent=new Intent(MainActivity.this,ChatActivity.class);
                        startActivity(chatIntent);
            }
        });

        mRiddleOftheday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent riddleofthedayIntent=new Intent(MainActivity.this,RiddleofthedayActivity.class);
                        startActivity(riddleofthedayIntent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mfirebaseuser.getCurrentUser();
        if(currentUser==null){
            Intent startIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(startIntent);
            finish();
        }else{
            String current_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            mdatabase=FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);
            mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String name=dataSnapshot.child("Name").getValue().toString().trim() ;
                   // mUsername.setText("Welcome "+name);

                    updateriddleoftheday();
                }

                private void updateriddleoftheday() {
                    mdatabase=FirebaseDatabase.getInstance().getReference().child("riddlehome");
                    mdatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //String riddle=dataSnapshot.child("riddle").getValue().toString().trim();
                          //  String name=dataSnapshot.child("name").getValue().toString().trim();
                           // mRiddleofthedaytxt.setText(riddle);
                           // mRiddleofthedaynametxt.setText(name);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError){

                }
            });
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.mainshare){
           Intent shareintent=new Intent(Intent.ACTION_SEND);
           shareintent.setType("text/plain");
           String sharebody="This is a great app to earn money by solving problems,along with best riddles:https://play.google.com/store/apps/details?id=com.prasadthegreat.genius";
           shareintent.putExtra(Intent.EXTRA_TEXT,sharebody);
           startActivity(Intent.createChooser(shareintent,"Share using"));
        }else if(id==R.id.mainsignout){
            FirebaseAuth.getInstance().signOut();
            Intent Loginintent=new Intent(MainActivity.this,LoginActivity.class);
            Loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(Loginintent);

        }
        return super.onOptionsItemSelected(item);
    }




}
