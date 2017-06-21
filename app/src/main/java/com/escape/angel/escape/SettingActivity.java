package com.escape.angel.escape;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private ImageView iv_Main;
    private Switch swc_Bgm,swc_Eff;
    private TextView tv_NickName;
    private Button btn_Modify, btn_Maker;

    private SharedPreferences prefs;
    private boolean Bgm, Eff;
    private String Nick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        swc_Bgm = (Switch)findViewById(R.id.swc_Bgm);
        swc_Eff = (Switch)findViewById(R.id.swc_Eff);


        iv_Main = (ImageView)findViewById(R.id.iv_Main);

        iv_Main.setImageResource(R.drawable.background);

        tv_NickName = (TextView)findViewById(R.id.tv_NickName);

        btn_Modify = (Button)findViewById(R.id.btn_Modify);
        btn_Maker = (Button)findViewById(R.id.btn_Maker);

        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        Bgm = prefs.getBoolean("BGM",true);
        Eff = prefs.getBoolean("EFF",true);

        swc_Bgm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("BGM",isChecked).apply();
            }
        });

        swc_Eff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("EFF",isChecked).apply();
            }
        });

        btn_Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //닉네임 수정 코드
                updateNickDialog(Nick);
            }
        });
        btn_Maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //개발자 커스텀다이얼로그

            }
        });
    }

    protected void onStart(){
        super.onStart();
        swc_Bgm.setChecked(Bgm);
        swc_Eff.setChecked(Eff);
        Nick = prefs.getString("UserName","");
        tv_NickName.setText(Nick);
    }

    public void updateNickDialog(String name){
        final EditBoxDialog dialog = new EditBoxDialog(this);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dia) {
                dialog.setMode("닉네임");
                dialog.setHint("변경할 닉네임 입력");
                dialog.setName(Nick);//변경 전 닉네임
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dia) {
                if(dialog.getCheck()=='0') {
                    Nick = dialog.getName();
                    prefs.edit().putString("UserName", Nick).apply();//변경 후 닉네임
                    tv_NickName.setText(Nick);
                }
            }
        });

        dialog.show();
    }
}
