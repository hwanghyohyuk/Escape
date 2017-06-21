package com.escape.angel.escape;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MyPageActivity extends AppCompatActivity {
    private Button btn_Character,btn_MyItem;
    private ImageView iv_Main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        iv_Main = (ImageView)findViewById(R.id.iv_Main);

        iv_Main.setImageResource(R.drawable.Background);

        /*버튼 링킹*/
        btn_Character = (Button)findViewById(R.id.btn_Character);
        btn_MyItem = (Button)findViewById(R.id.btn_MyItem);

        /*버튼 클릭 이벤트*/
        btn_Character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MyPageActivity.this,CharacterSelectActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        btn_MyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MyPageActivity.this,MyItemActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
    }
}
