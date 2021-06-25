package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EarnActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView mEarnView;
    private AdView mAdview;
    private Button mContact,mInstructions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);

     mAdview=findViewById(R.id.myearnadview);

        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        mInstructions=(Button)findViewById(R.id.instructionbtn);
        mInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent instructionIntent=new Intent(EarnActivity.this,InstructionsActivity.class);
                    startActivity(instructionIntent);
            }
        });
        mContact=(Button)findViewById(R.id.contactusbtn);
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"geniusdeveloper799@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Please,Enter Your Feedback or Complaint");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(EarnActivity.this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();

            }

        });

        mEarnView=(TextView)findViewById(R.id.earnlayoutedit);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Earn");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Earndata question=dataSnapshot.getValue(Earndata.class);
                mEarnView.setText(question.getQuestion());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
