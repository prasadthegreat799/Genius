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
import android.widget.EditText;
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

public class WhatriddleActivity extends AppCompatActivity {

    private Button mNestquesionbtn,mCheckAnswer;
    private TextView mQuestiontxt,mAnswertxt;
    private EditText mAnswerEdittext;
    private DatabaseReference mDatabase;
    private AdView mAdview;
    private NoInternetDialog noInternetDialog;
    private TextView mTimertxt;
    private MediaPlayer timersound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatriddle);

        mTimertxt=(TextView)findViewById(R.id.timertxtviewwhatriddle);

        noInternetDialog  = new NoInternetDialog.Builder(WhatriddleActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();

        mAdview=findViewById(R.id.whatadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);



        mCheckAnswer=(Button)findViewById(R.id.checkanswerbtnwhat);
        mAnswerEdittext=(EditText)findViewById(R.id.answeredittxtwhatami);
        mNestquesionbtn=(Button)findViewById(R.id.nextwhatriddlebtn);
        mQuestiontxt=(TextView)findViewById(R.id.whatriddlequestiontxt);
        mAnswertxt=(TextView)findViewById(R.id.whatriddleanswertxt);
        updatewhathquestion();
    }

    private void updatewhathquestion() {
        playsound();
        Random number= new Random();
        int total=number.nextInt(44-1)+1;
        mDatabase= FirebaseDatabase.getInstance().getReference().child("whatamiriddle").child(String.valueOf(total));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Loadmathriddles question=dataSnapshot.getValue(Loadmathriddles.class);
               mQuestiontxt.setText(question.getQuestion());

                final CountDownTimer count = new CountDownTimer(15 * 1000 + 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        int minute = seconds / 60;
                        seconds = seconds % 60;
                        if(seconds<5){
                            mTimertxt.setTextColor(Color.RED);
                            mTimertxt.setText(String.format("%02d", minute) + ":" + String.format("%02d", seconds));
                        }else{
                            mTimertxt.setText(String.format("%02d", minute) + ":" + String.format("%02d", seconds));
                            mTimertxt.setTextColor(Color.GRAY);
                        }


                    }
                    @Override
                    public void onFinish() {
                        String  answer=mAnswerEdittext.getText().toString().toLowerCase().trim();
                        String  Correctanswer=question.getAnswer().toString().toLowerCase().trim();
                        if (timersound != null){
                            timersound.stop();
                            timersound.release();
                            timersound = null;
                        }
                        if(answer.equals(Correctanswer)){
                            mAnswertxt.setText("Correct Answer");
                            mAnswertxt.setTextColor(Color.GREEN);
                        }else{
                            mAnswertxt.setText("The Correct Answer is:"+Correctanswer);
                            mAnswertxt.setTextColor(Color.RED);
                        }

                    }

                }.start();



               mNestquesionbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       mAnswertxt.setText("");
                        mAnswerEdittext.setText("");
                        count.cancel();
                        if (timersound != null){
                            timersound.stop();
                            timersound.release();
                            timersound = null;
                        }
                        updatewhathquestion();
                    }
                });

                mCheckAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count.cancel();
                        String  answer=mAnswerEdittext.getText().toString().toLowerCase().trim();
                        String  Correctanswer=question.getAnswer().toString().toLowerCase().trim();
                        if (timersound != null){
                            timersound.stop();
                            timersound.release();
                            timersound = null;
                        }
                        if(answer.equals(Correctanswer)){
                            mAnswertxt.setText("Correct Answer");
                            mAnswertxt.setTextColor(Color.GREEN);
                        }else{
                            mAnswertxt.setText("The Correct Answer is:"+Correctanswer);
                            mAnswertxt.setTextColor(Color.RED);
                        }
                        mCheckAnswer.setEnabled(false);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void playsound() {
        timersound=MediaPlayer.create(WhatriddleActivity.this,R.raw.sectimer);
        timersound.start();
    }

    public void gotohome(View view) {
        if (timersound != null) {
            timersound.stop();
            timersound.release();
            timersound = null;
        }
        Intent homeintent=new Intent(WhatriddleActivity.this,MainActivity.class);
        homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeintent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    public void onBackPressed() {

        if (timersound != null) {
            timersound.stop();
            timersound.release();
            timersound = null;
        }
        Intent gointent=new Intent(WhatriddleActivity.this,MainActivity.class);
        gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gointent);
        super.onBackPressed();
    }
}
