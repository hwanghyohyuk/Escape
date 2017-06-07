package com.escape.angel.escape;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView tv_NickName;
    Button btn_GameStart,btn_MyPage,btn_Setting,btn_Ranking;

    private long backKeyPressedTime = 0;
    private Toast toast;

    public SharedPreferences prefs;
    public String Nick,Utype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        Nick = prefs.getString("UserName","");
        prefs.edit().putString("Utype","GUEST").apply();
        Utype = prefs.getString("Utype","");

        /*버튼 링킹*/
        btn_GameStart=(Button)findViewById(R.id.btn_GameStart);
        btn_MyPage=(Button)findViewById(R.id.btn_MyPage);
        btn_Setting=(Button)findViewById(R.id.btn_Setting);
        btn_Ranking=(Button)findViewById(R.id.btn_Ranking);

        tv_NickName = (TextView)findViewById(R.id.tv_NickName);
        tv_NickName.setText(Utype+" : "+Nick+"님 환영합니다.   ");


        /*버튼 클릭 이벤트*/
        btn_GameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this,LobbyActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        btn_MyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this,MyPageActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        btn_Ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this,RankingActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
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
}

