package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import am.appwise.components.ni.NoInternetDialog;

public class ForgotpasswordActivity extends AppCompatActivity {
    private EditText mMail;
    private Button mSubmit;
   private AdView mAdview;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        noInternetDialog  = new NoInternetDialog.Builder(ForgotpasswordActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();


          mAdview=findViewById(R.id.forgotpassadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        mMail=(EditText)findViewById(R.id.forgotpasstxt);
        mSubmit=(Button)findViewById(R.id.forgotpasssub);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mMail.getText().toString().trim();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(ForgotpasswordActivity.this,"Please,enter your registred mail id",Toast.LENGTH_LONG).show();
                }else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotpasswordActivity.this,"Check email to reset your password",Toast.LENGTH_LONG).show();
                                mMail.setText("");
                            }else{
                                Toast.makeText(ForgotpasswordActivity.this,"Please,enter the registred mail",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

}
