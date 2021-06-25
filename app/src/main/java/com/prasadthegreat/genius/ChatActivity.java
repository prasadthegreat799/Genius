package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Button mSendbtn;
    private EditText mMessage;
    private DatabaseReference mDatabase;
    private AdView mAdview;
    private String name;

    ListView mylistview;
    ArrayList<String> myArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAdview=findViewById(R.id.chatadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        mSendbtn=(Button)findViewById(R.id.btnSendmsg);
        mylistview=(ListView)findViewById(R.id.listview);
        mMessage=(EditText)findViewById(R.id.textInputmsg);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("chat");



        final ArrayAdapter<String> myarrayadapter=new ArrayAdapter<String>(ChatActivity.this,android.R.layout.simple_list_item_1,myArrayList);
        mylistview.setAdapter(myarrayadapter);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String value=dataSnapshot.getValue(String.class);
                String current_uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("user").child(current_uid);
                mdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name=dataSnapshot.child("Name").getValue().toString().trim();
                        myArrayList.add(value);
                        myarrayadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myarrayadapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=mMessage.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this,"Please,Enter any data",Toast.LENGTH_LONG).show();
                }else{
                    mDatabase.push().setValue(message +"   @"+ name);
                    mMessage.setText("");
                }

            }
        });


    }
}