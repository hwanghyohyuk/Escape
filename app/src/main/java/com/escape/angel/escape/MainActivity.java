package com.escape.angel.escape;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ImageView iv_Main;
    private TextView tv_NickName;
    private Button btn_GameStart,btn_MyPage,btn_Setting,btn_Ranking;

    private long backKeyPressedTime = 0;
    private Toast toast;

    private SharedPreferences prefs;
    private String Nick,Utype;
    private Integer Character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        prefs.edit().putString("Utype","GUEST").apply();
        Utype = prefs.getString("Utype","");
        Character = prefs.getInt("Character",0); //캐릭터 프리퍼런스 값 가져오기. 값이 없다면 0으로 가져옴
        if (Character == 0){//캐릭터 프리퍼런스 값이 0일때, 즉 최초 접속일때
            prefs.edit().putInt("Character",1).apply();//1의 값으로 저장
        }

        /*버튼 링킹*/
        btn_GameStart=(Button)findViewById(R.id.btn_GameStart);
        btn_MyPage=(Button)findViewById(R.id.btn_MyPage);
        btn_Setting=(Button)findViewById(R.id.btn_Setting);
        btn_Ranking=(Button)findViewById(R.id.btn_Ranking);

        tv_NickName = (TextView)findViewById(R.id.tv_NickName);

        iv_Main = (ImageView)findViewById(R.id.iv_Main);

        iv_Main.setImageResource(R.drawable.MainBackground);


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

    protected void onStart(){
        super.onStart();
        Nick = prefs.getString("UserName","");
        tv_NickName.setText(Utype+" : "+Nick+"님 환영합니다.   ");
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

