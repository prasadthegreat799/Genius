package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.w3c.dom.Text;

import am.appwise.components.ni.NoInternetDialog;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mEmail,mPassword;
    private TextView mForgotpassword,mNewuser;
    private FirebaseAuth mAuth;
    private AdView mAdview;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noInternetDialog  = new NoInternetDialog.Builder(LoginActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();

        mAdview=findViewById(R.id.loginaview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);



        mAuth = FirebaseAuth.getInstance();
        mLogin=(Button)findViewById(R.id.main_loginbtn);
        mEmail=(EditText)findViewById(R.id.editTextEmailLogin);
        mPassword=(EditText)findViewById(R.id.editTextPasswordLogin);
        mForgotpassword=(TextView)findViewById(R.id.ForgotpasstxtLogin);
        mForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotintent=new Intent(LoginActivity.this,ForgotpasswordActivity.class);
                startActivity(forgotintent);
            }
        });

        mNewuser=(TextView)findViewById(R.id.newusertxtLogin);
        mNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regActivity=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regActivity);
            }
        });

        mLogin=(Button)findViewById(R.id.main_loginbtn);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=mEmail.getText().toString();
                String Password=mPassword.getText().toString();
                if(TextUtils.isEmpty(Email) | TextUtils.isEmpty(Password)){
                    Toast.makeText(LoginActivity.this,"Please,Fill all fields...",Toast.LENGTH_LONG).show();
                }else{
                    loginuser(Email,Password);
                }
            }

            private void loginuser(String email, String password) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Successful.....", Toast.LENGTH_SHORT).show();
                            Intent mainintent=new Intent(LoginActivity.this,MainActivity.class);
                            mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainintent);
                        }else{
                            Toast.makeText(LoginActivity.this,"Please,enter valid email or password",Toast.LENGTH_LONG).show();
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
