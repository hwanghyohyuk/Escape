package com.escape.angel.escape.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.escape.angel.escape.R;

/**
 * Created by my on 2017-06-19.
 */

public class SinglePlayActivity extends AppCompatActivity{

    //
    private Chronometer timer;
    private TextView tv_Ready,tv_FA;
    private Button btn_FA;

    private final int FAEND=10;

    private int sec = 5;
    private int fa = 0;

    private Handler handler = new Handler();

    private Runnable ready = new Runnable() {
        @Override
        public void run() {
            String s = Integer.toString(sec);
            if (sec>0 && sec<6) {
                tv_Ready.setText(s);
                sec -= 1;
                handler.removeCallbacksAndMessages(0);
                handler.postDelayed(ready, 1000);
            }else {
                tv_Ready.setText("");

            }
        }
    };
    private Runnable gamestart = new Runnable() {

        @Override
        public void run() {
            timer.setBase(SystemClock.elapsedRealtime());
            Toast.makeText(getApplicationContext(),"게임시작",Toast.LENGTH_SHORT).show();
            timer.start();
            onMission(1);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_singleplay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        // 바로 게임 시작


        timer = (Chronometer)findViewById(R.id.timer);
        tv_Ready = (TextView)findViewById(R.id.tv_Ready);
        tv_FA = (TextView)findViewById(R.id.tv_FA);
        btn_FA = (Button)findViewById(R.id.btn_FA);

        tv_FA.setVisibility(View.GONE);
        btn_FA.setVisibility(View.GONE);

        btn_FA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa++;
            }
        });
        }
    //타이머 핸들러
    protected  void onStart(){
        super.onStart();
        handler.postDelayed(gamestart, 6000);
        handler.postDelayed(ready,1000);
    }

    protected void onDestroy(){
        super.onDestroy();
        timer.stop();
    }

    private void onMission(int m){
        switch (m){
            case 1:
                //초성퀴즈
                FirstAlpha();
                break;
            case 2:
                //빗질

                break;
            case 3:
                //화장

                break;
            case 4:
                //도시락

                break;
            case 5:
                //춤추기

                break;
        }
    }

    private Boolean FirstAlpha(){
        tv_FA.setText(fa);
        tv_FA.setVisibility(View.VISIBLE);
        btn_FA.setVisibility(View.VISIBLE);
        if(fa<10){
            this.FirstAlpha();
        } else{
            return true;
        }
        return this.FirstAlpha();
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

