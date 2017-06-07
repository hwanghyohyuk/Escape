package com.escape.angel.escape;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbySingleActivity extends AppCompatActivity {
    /*버튼 선언*/
    Button btn_Tutorial,btn_SoloPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_single);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        /*버튼 링킹*/
        btn_Tutorial = (Button)findViewById(R.id.btn_Tutorial);
        btn_SoloPlay = (Button)findViewById(R.id.btn_SoloPlay);

        /*버튼 클릭 이벤트*/
        btn_Tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*튜토리얼*/
            }
        });
        btn_SoloPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*혼자해보기*/
            }
        });
    }
}
