package com.prasadthegreat.genius;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Utilities;

public class RiddleofthedayActivity extends AppCompatActivity {
    private EditText mRiddle;
    private Button mSubmit;
    private AdView mAdview;
    private DatabaseReference mDatabase;
    private DatabaseReference mdatabasename;
    private TextView mriddleanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddleoftheday);


        mAdview=findViewById(R.id.riddleofthedayadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);



        mriddleanswer=(TextView)findViewById(R.id.submitriddleanswer);
        mRiddle=(EditText)findViewById(R.id.riddleofthedayedit);
        mSubmit=(Button)findViewById(R.id.submitriddlebtn);


        mriddleanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent answerintent=new Intent(RiddleofthedayActivity.this,RiddleofthedayanswerActivity.class);
                    startActivity(answerintent);


            }
        });
        mDatabase= FirebaseDatabase.getInstance().getReference().child("riddleoftheday");
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   riddleofthedayquestion();

            }

            private void riddleofthedayquestion() {
                String riddledata = mRiddle.getText().toString();
                if (TextUtils.isEmpty(riddledata)) {
                    Toast.makeText(RiddleofthedayActivity.this, "Please,Enter the riddle", Toast.LENGTH_LONG).show();
                } else {
                    mDatabase.push().setValue(riddledata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mdatabasename = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);
                                mdatabasename.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("Name").getValue().toString().trim();
                                        mDatabase.push().setValue(name);
                                        Toast.makeText(RiddleofthedayActivity.this, "Data Submited Successfully", Toast.LENGTH_LONG).show();
                                        mRiddle.setText("");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(RiddleofthedayActivity.this, "Please,try after some time", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
