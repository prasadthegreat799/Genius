package com.prasadthegreat.genius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import am.appwise.components.ni.NoInternetDialog;

public class MathriddleActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView mMathquestion,mMathAnswer;
    private Button mAnswerbtn,mNestquestionbtn;
    private AdView mAdview;
    private NoInternetDialog noInternetDialog;
    private TextView mTimertxt;
    private MediaPlayer timersound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathriddle);

        mTimertxt=(TextView)findViewById(R.id.timertxtviewmathriddle);

        noInternetDialog  = new NoInternetDialog.Builder(MathriddleActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();

      mAdview=findViewById(R.id.mathadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        mMathquestion=(TextView)findViewById(R.id.mathriddlequestiontxt);
        mMathAnswer=(TextView)findViewById(R.id.mathriddleanswertxt);
        mAnswerbtn=(Button)findViewById(R.id.riddlemathAnswerBtn);
        mNestquestionbtn=(Button)findViewById(R.id.nextmathriddlebtn);
        updatemathquestion();

    }
    private void updatemathquestion() {
        playsound();
        reversetimer(15,mTimertxt);
        Random number= new Random();
        int total=number.nextInt(16-1)+1;
        mDatabase= FirebaseDatabase.getInstance().getReference().child("mathriddle").child(String.valueOf(total));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Loadmathriddles question=dataSnapshot.getValue(Loadmathriddles.class);
                mMathquestion.setText(question.getQuestion());

                mAnswerbtn.setEnabled(false);
                mNestquestionbtn.setEnabled(false);
                mAnswerbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMathAnswer.setText("Answer:"+question.getAnswer());
                        mMathAnswer.setTextColor(Color.GRAY);
                    }
                });

                mNestquestionbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMathAnswer.setText("");
                        updatemathquestion();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void playsound() {
        timersound=MediaPlayer.create(MathriddleActivity.this,R.raw.sectimer2);
        timersound.start();
    }

    public void gotohome(View view) {
        if (timersound != null) {
            timersound.stop();
            timersound.release();
            timersound = null;
        }
        Intent gointent=new Intent(MathriddleActivity.this,MainActivity.class);
        gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gointent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    public void reversetimer(int seconds, final TextView txt) {

        final CountDownTimer count = new CountDownTimer(seconds * 1000 + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minute = seconds / 60;
                seconds = seconds % 60;
                if(seconds<5){
                    txt.setTextColor(Color.RED);
                }
                txt.setText(String.format("%02d", minute) + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                if (timersound != null) {
                    timersound.stop();
                    timersound.release();
                    timersound = null;
                }
                    mAnswerbtn.setEnabled(true);
                    mNestquestionbtn.setEnabled(true);

            }


        }.start();

    }

    @Override
    public void onBackPressed() {
        if (timersound != null) {
            timersound.stop();
            timersound.release();
            timersound = null;
        }
        Intent gointent=new Intent(MathriddleActivity.this,MainActivity.class);
        gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gointent);
        super.onBackPressed();
    }
}


