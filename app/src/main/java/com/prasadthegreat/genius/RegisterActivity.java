package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNamereg,mPasswordreg,mMailreg,mPhonereg;
    private TextView mLogintxt;
    private Button mRegisterbtn;
    private FirebaseAuth mfirebaseuser;
    private AdView mAdView;
    private DatabaseReference mdatabase;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        noInternetDialog  = new NoInternetDialog.Builder(RegisterActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();

        mAdView=findViewById(R.id.registeradview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mfirebaseuser=FirebaseAuth.getInstance();
        mNamereg=(EditText)findViewById(R.id.main_nametxtreg);
        mPhonereg=(EditText)findViewById(R.id.main_phonetxtreg);
        mMailreg=(EditText)findViewById(R.id.main_regtxtmail);
        mPasswordreg=(EditText)findViewById(R.id.main_passwordtxtreg);
        mLogintxt=(TextView)findViewById(R.id.main_logintxtreg);
        mLogintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logintent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(logintent);
            }
        });

        mRegisterbtn=(Button)findViewById(R.id.main_registerbtn);
        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=mNamereg.getText().toString();
                String phone=mPhonereg.getText().toString();
                String email=mMailreg.getText().toString();
                String password=mPasswordreg.getText().toString();
                if(TextUtils.isEmpty(email) | TextUtils.isEmpty(password) | TextUtils.isEmpty(name) | TextUtils.isEmpty(phone)){
                  Toast.makeText(RegisterActivity.this,"Please,Fill all fields",Toast.LENGTH_LONG).show();
                }else{
                    registeruser(email,password,name,phone);
                }

            }

            private void registeruser(final String email, String password, final String name, final String phone) {
                mfirebaseuser.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                           String uid=currentuser.getUid();

                           mdatabase= FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                           HashMap<String,String> usermap=new HashMap<>();
                           usermap.put("Name",name);
                           usermap.put("Phone",phone);
                           usermap.put("Email",email);
                           mdatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if ((task.isSuccessful())) {
                                       Toast.makeText(RegisterActivity.this,"Registration Success...",Toast.LENGTH_LONG).show();
                                       Intent mainintent=new Intent(RegisterActivity.this,MainActivity.class);
                                       startActivity(mainintent);
                                   }
                               }
                           });
                       }else{
                           Toast.makeText(RegisterActivity.this,"Your mail already registred",Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

}
