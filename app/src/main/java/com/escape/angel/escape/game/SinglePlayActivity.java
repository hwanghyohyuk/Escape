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

    private Server server = new Server();
    private String serverIP = server.getSERVERIP();
    private SharedPreferences prefs;
    private int character,per;
    private String Nick;

    private char check;

    private Chronometer timer;
    private ImageView iv_User1,iv_User2,iv_User3,iv_Teacher;
            ;
    private TextView tv_Ready,tv_touch;

    private final int FAEND=10;

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
            Toast.makeText(getApplicationContext(),"게임시작. 목표 : 터치 90번",Toast.LENGTH_SHORT).show();
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
                if(per>6 && teacher==false)
                    teacher=true;
                    iv_Teacher.setImageResource(R.drawable.t_back);
                if(per<6 && teacher ==true)
                    teacher=false;
                    iv_Teacher.setImageResource(R.drawable.t_front);
            }
        });
        }

    //화면 터치 이벤트

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                if(teacher!=true)
                touch++;
                else
                    touch -=10;

                tv_touch.setText("횟수 : "+touch+"번");
                if(touch<30) {
                }else if(touch>=30&&touch<60) {
                    iv_User1.setVisibility(View.GONE);
                    iv_User2.setVisibility(View.VISIBLE);
                }else if(touch>=60&&touch<90) {
                    iv_User2.setVisibility(View.GONE);
                    iv_User3.setVisibility(View.VISIBLE);
                }else {
                    timer.stop();
                    Toast.makeText(getApplicationContext(),"성공! 기록:"+records+"초", Toast.LENGTH_SHORT).show();
                    insertToDatabase(Nick,records);
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

    protected void onDestroy(){
        super.onDestroy();
        timer.stop();
    }
    private void insertToDatabase(String name,int time){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(getApplicationContext(),
                        "Please Wait", null, true, true);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String time = (String)params[1];

                    String link= serverIP+"insertRanking.php";

                    String data  = URLEncoder.encode("NAME", "UTF-8") + "="
                            + URLEncoder.encode(name, "UTF-8");
                    data  += "&"+URLEncoder.encode("TIME", "UTF-8") + "="
                            + URLEncoder.encode(time, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    String v = sb.toString();
                    check = v.charAt(1);
                    return null;
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name);
    }
    }

