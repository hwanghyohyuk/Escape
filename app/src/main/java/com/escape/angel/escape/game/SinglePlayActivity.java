package com.escape.angel.escape.game;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_singleplay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        // 바로 게임 시작
        timer = (Chronometer)findViewById(R.id.timer);

        }
    //타이머 핸들러
    protected  void onStart(){
        super.onStart();
        timer.start();
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

