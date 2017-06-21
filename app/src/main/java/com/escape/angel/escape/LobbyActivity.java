package com.escape.angel.escape;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.escape.angel.escape.game.SinglePlayActivity;

import static com.escape.angel.escape.R.id.iv_Main;

public class LobbyActivity extends AppCompatActivity {
    /*버튼 선언*/
    private Button btn_Single,btn_Multi;
    private ImageView iv_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);



        iv_Main = (ImageView)findViewById(R.id.iv_Main);

        iv_Main.setImageResource(R.drawable.Background);


        /*버튼 링킹*/
        btn_Single = (Button)findViewById(R.id.btn_Single);
        btn_Multi = (Button)findViewById(R.id.btn_Multi);

        /*버튼 클릭 이벤트*/
        btn_Single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //싱글플레이
                Intent mIntent = new Intent(LobbyActivity.this,SinglePlayActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        btn_Multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //멀티플레이
                Intent mIntent = new Intent(LobbyActivity.this,RoomListActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
    }
}
