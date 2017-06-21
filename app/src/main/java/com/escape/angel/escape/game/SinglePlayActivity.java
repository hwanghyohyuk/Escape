package com.escape.angel.escape.game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escape.angel.escape.NicknameActivity;
import com.escape.angel.escape.R;
import com.escape.angel.escape.RankingActivity;
import com.escape.angel.escape.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by my on 2017-06-19.
 */

public class SinglePlayActivity extends AppCompatActivity{

    private SharedPreferences prefs;
    private int character,per;
    private String Nick;

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Chronometer timer;
    private ImageView iv_User1,iv_User2,iv_User3,iv_Teacher;

    private TextView tv_Ready,tv_touch;


    private int sec = 5;
    private int touch = 0;
    private int records = 0;

    private boolean teacher = false;

    private Handler TIME = new Handler();

    private Runnable ready = new Runnable() {
        @Override
        public void run() {
            String s = Integer.toString(sec);
            if (sec>0 && sec<6) {
                tv_Ready.setText(s);
                sec -= 1;
                TIME.removeCallbacksAndMessages(0);
                TIME.postDelayed(ready,1000);
            }else {
                tv_Ready.setText("");
                TIME.removeCallbacksAndMessages(0);
            }
        }
    };
    private Runnable gamestart = new Runnable() {
        @Override
        public void run() {
            timer.setBase(SystemClock.elapsedRealtime());
            Toast.makeText(getApplicationContext(),"게임시작. 목표 : 점수 100점이상",Toast.LENGTH_SHORT).show();
            timer.start();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_singleplay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        // 바로 게임 시작

        //사용자가 설정한 캐릭터 가져오기
        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        character = prefs.getInt("Character",0);
        Nick = prefs.getString("UserName","");

        iv_User1 = (ImageView)findViewById(R.id.iv_User1);
        iv_User2 = (ImageView)findViewById(R.id.iv_User2);
        iv_User3 = (ImageView)findViewById(R.id.iv_User3);
        iv_Teacher = (ImageView)findViewById(R.id.iv_Teacher);

        timer = (Chronometer)findViewById(R.id.timer);
        tv_Ready = (TextView)findViewById(R.id.tv_Ready);
        tv_touch = (TextView)findViewById(R.id.tv_touch);

        iv_Teacher.setImageResource(R.drawable.t_front);

        switch (character){

            case 1:
                iv_User1.setImageResource(R.drawable.mohee);
                iv_User2.setImageResource(R.drawable.mohee);
                iv_User3.setImageResource(R.drawable.mohee);
                iv_User1.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_User1.setImageResource(R.drawable.mohyuk);
                iv_User2.setImageResource(R.drawable.mohyuk);
                iv_User3.setImageResource(R.drawable.mohyuk);
                iv_User1.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_User1.setImageResource(R.drawable.mori);
                iv_User2.setImageResource(R.drawable.mori);
                iv_User3.setImageResource(R.drawable.mori);
                iv_User1.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_User1.setImageResource(R.drawable.moyeon);
                iv_User2.setImageResource(R.drawable.moyeon);
                iv_User3.setImageResource(R.drawable.moyeon);
                iv_User1.setVisibility(View.VISIBLE);
                break;

        }

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ++records;
                per=(int)(Math.random() * 10);
                if(per>6 && teacher==false){
                    teacher=true;
                    iv_Teacher.setImageResource(R.drawable.t_back);}
                if(per<6 && teacher ==true){
                    teacher=false;
                    iv_Teacher.setImageResource(R.drawable.t_front);}
            }
        });
        }

    //화면 터치 이벤트

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_UP:    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_DOWN:    //화면을 터치했을때
                if (teacher != true) {
                    touch+=2;
                    tv_touch.setText("점수 : " + touch + "점 +2");
                } else {
                    touch -= 5;
                    tv_touch.setText("점수 : " + touch + "점 -5");
                }

                if(touch<0){
                    timer.stop();
                    Toast.makeText(getApplicationContext(),"미션 실패!", Toast.LENGTH_SHORT).show();
                    TIME.removeCallbacksAndMessages(0);
                    finish();
                 }else if(touch>=0&&touch<30) {
                    iv_User1.setVisibility(View.VISIBLE);
                    iv_User2.setVisibility(View.GONE);
                }else if(touch>=30&&touch<60) {
                    iv_User1.setVisibility(View.GONE);
                    iv_User2.setVisibility(View.VISIBLE);
                    iv_User3.setVisibility(View.GONE);
                }else if(touch>=60&&touch<100) {
                    iv_User2.setVisibility(View.GONE);
                    iv_User3.setVisibility(View.VISIBLE);
                }else {
                    timer.stop();
                    Toast.makeText(getApplicationContext(),"미션 성공! 기록:"+records+"초", Toast.LENGTH_SHORT).show();
                    TIME.removeCallbacksAndMessages(0);
                    finish();
                }

                break;
            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }
    //타이머 핸들러
    protected  void onStart(){
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        TIME.postDelayed(gamestart, 6000);
        TIME.postDelayed(ready,1000);
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(getApplicationContext(),
                "\'뒤로\' 버튼을 한번 더 누르시면 게임을 종료합니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
    protected void onDestroy(){
        super.onDestroy();
        TIME.removeCallbacksAndMessages(0);
        timer.stop();
    }

    }

