package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RiddleofthedayanswerActivity extends AppCompatActivity {

    private EditText mSubmitansweredit;
    private Button mSubmitanswerbutton;
    private DatabaseReference mDatabase;
    private DatabaseReference mdatabasename;
    private AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddleofthedayanswer);

        mAdview=findViewById(R.id.riddleofthedayansweradview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);



        mSubmitanswerbutton=(Button)findViewById(R.id.submitriddleanswerbtn);
        mSubmitansweredit=(EditText)findViewById(R.id.riddleofthedayansweredit);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("riddleofthedayanswer");
        mSubmitanswerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submitanswer();


            }

            private void submitanswer() {
                String answer=mSubmitansweredit.getText().toString().trim();
                if(TextUtils.isEmpty(answer)){
                    Toast.makeText(RiddleofthedayanswerActivity.this,"Please,Enter The Answer",Toast.LENGTH_LONG).show();
                }else{
                    mDatabase.push().setValue(answer).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String current_uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mdatabasename=FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);
                                mdatabasename.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String name=dataSnapshot.child("Name").getValue().toString().trim() ;
                                        mDatabase.push().setValue(name);
                                        Toast.makeText(RiddleofthedayanswerActivity.this,"Answer Submited Successfully",Toast.LENGTH_LONG).show();
                                        mSubmitansweredit.setText("");
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Toast.makeText(RiddleofthedayanswerActivity.this,"Please,try after some time",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }
}
