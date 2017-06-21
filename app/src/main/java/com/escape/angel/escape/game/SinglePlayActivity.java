package com.escape.angel.escape.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.escape.angel.escape.R;

/**
 * Created by my on 2017-06-19.
 */

public class SinglePlayActivity extends AppCompatActivity{

    private SharedPreferences prefs;
    private int character;

    private Chronometer timer;
    private ImageView iv_User1,iv_User2,iv_User3;
    private TextView tv_Ready,tv_FA;
    private Button btn_FA;

    private final int FAEND=10;

    private int sec = 5;
    private int touch = 0;

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
            Toast.makeText(getApplicationContext(),"게임시작. 목표 : 터치 30번",Toast.LENGTH_SHORT).show();
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

        iv_User1 = (ImageView)findViewById(R.id.iv_User1);
        iv_User2 = (ImageView)findViewById(R.id.iv_User2);
        iv_User3 = (ImageView)findViewById(R.id.iv_User3);

        timer = (Chronometer)findViewById(R.id.timer);
        tv_Ready = (TextView)findViewById(R.id.tv_Ready);

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
        }

    //화면 터치 이벤트

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                touch++;
                Toast.makeText(getApplicationContext(), "터치 "+touch+"번", Toast.LENGTH_SHORT).show();
                if(touch<10) {
                }else if(touch>=10&&touch<20) {
                    iv_User1.setVisibility(View.GONE);
                    iv_User2.setVisibility(View.VISIBLE);
                }else if(touch>=20&&touch<30) {
                    iv_User2.setVisibility(View.GONE);
                    iv_User3.setVisibility(View.VISIBLE);
                }else {
                    timer.stop();
                    Toast.makeText(getApplicationContext(),"성공! 기록:"+timer.getBase()+"초", Toast.LENGTH_SHORT).show();
                    TIME.removeCallbacksAndMessages(0);
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

    protected void onDestroy(){
        super.onDestroy();
        timer.stop();
    }

        /*
        게임 시작

타이머시작
선생님 수시로 돌아봄 ( if 돌아볼때 걸리면 목숨(총3개) 줄어듬
			(if 목숨 0개 되면 탈락. 게임종료))
	{
	첫번째 미션 수행 (랜덤)
	 - 선생님한테 안걸리게 ^^
	게이지 다채우면 자리이동

	두번쨰 미션 수행 (랜덤)
		-5번째까지 똑같-
	}
타이머 종료
아이템 획득
선생님한테 안걸리고 탈출 성~공
         */
    }

