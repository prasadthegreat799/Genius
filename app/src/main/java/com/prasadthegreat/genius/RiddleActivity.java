package com.prasadthegreat.genius;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Random;

import am.appwise.components.ni.NoInternetDialog;

public class RiddleActivity extends AppCompatActivity {

    private Button mNextquestion,mCheckAnswer;
    private TextView mAnswerTxt,mRiddlequesiton;
    private DatabaseReference mDatabaseRef;
    private AdView mAdview;
    private NoInternetDialog noInternetDialog;
    private TextView mTimertxt;
    private MediaPlayer timersound;
    private EditText mAnswer;
    int num_question=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);

        noInternetDialog  = new NoInternetDialog.Builder(RiddleActivity.this)
                .setBgGradientStart(Color.parseColor("#00a5ff"))
                .setBgGradientCenter(Color.parseColor("#00a5ff"))
                .setBgGradientEnd(Color.parseColor("#00a5ff"))
                .build();


      mAdview=findViewById(R.id.riddleadview);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);


        mTimertxt=(TextView)findViewById(R.id.timertxtviewnormalriddle);
        mAnswer=(EditText)findViewById(R.id.answeredittxt);
        mRiddlequesiton=(TextView)findViewById(R.id.riddlequestiontxt);
        mAnswerTxt=(TextView)findViewById(R.id.riddleanswertxt);
        mNextquestion=(Button)findViewById(R.id.nextriddlebtn);
        mCheckAnswer=(Button)findViewById(R.id.checkanswerbtn);
        updatequestion();
    }

    private void updatequestion() {
        mCheckAnswer.setEnabled(true);
       playsound();
        Random number= new Random();
        int total=number.nextInt(50-1)+1;
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("riddle").child(String.valueOf(total));
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Loadriddles question=dataSnapshot.getValue(Loadriddles.class);
                mRiddlequesiton.setText(question.getQuestion());


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
                        String  answer=mAnswer.getText().toString().toLowerCase().trim();
                        String  Correctanswer=question.getAnswer().toString().toLowerCase().trim();
                        mCheckAnswer.setEnabled(false);
                        if (timersound != null){
                            timersound.stop();
                            timersound.release();
                            timersound = null;
                        }
                        if(answer.equals(Correctanswer)){
                            mAnswerTxt.setText("Correct Answer");
                            mAnswerTxt.setTextColor(Color.GREEN);
                        }else{
                            mAnswerTxt.setText("The Correct Answer is:"+Correctanswer);
                            mAnswerTxt.setTextColor(Color.RED);
                        }

                    }

                }.start();


              mNextquestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAnswerTxt.setText("");
                       count.cancel();
                        if (timersound != null){
                            timersound.stop();
                            timersound.release();
                            timersound = null;
                        }
                        mAnswer.setText("");
                        if(num_question<=3){
                            num_question=num_question+1;
                            updatequestion();

                        }else{
                            Toast.makeText(getApplicationContext(),"Your submited 3 question",Toast.LENGTH_LONG).show();
                        }



                    }
                });

              mCheckAnswer.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      count.cancel();
                      String  answer=mAnswer.getText().toString().toLowerCase().trim();
                      String  Correctanswer=question.getAnswer().toString().toLowerCase().trim();
                      if (timersound != null){
                          timersound.stop();
                          timersound.release();
                          timersound = null;
                      }
                      if(answer.equals(Correctanswer)){
                          mAnswerTxt.setText("Correct Answer");
                          mAnswerTxt.setTextColor(Color.GREEN);
                      }else{
                          mAnswerTxt.setText("The Correct Answer is:"+Correctanswer);
                          mAnswerTxt.setTextColor(Color.RED);
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

    public void com(String answer,String Correctanswer){

    }

    private void playsound() {
        timersound=MediaPlayer.create(RiddleActivity.this,R.raw.sectimer);
        timersound.start();
    }

    public void gotohome(View view) {
        if (timersound != null) {
            timersound.stop();
            timersound.release();
            timersound = null;
        }
        Intent gointent=new Intent(RiddleActivity.this,MainActivity.class);
        gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gointent);
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
        Intent gointent=new Intent(RiddleActivity.this,MainActivity.class);
        gointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gointent);
        super.onBackPressed();
    }
}
