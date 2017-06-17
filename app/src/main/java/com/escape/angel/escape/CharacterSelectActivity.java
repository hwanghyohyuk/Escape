package com.escape.angel.escape;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

public class CharacterSelectActivity extends AppCompatActivity {
    // 안녕하십니까 우하하하핳^^
// ㅣㅋ키키키
    private ImageView iv_Character1,iv_Character2,iv_Character3,iv_Character4;
    private CheckBox cb_Character1,cb_Character2,cb_Character3,cb_Character4;
    private Button btn_confirm;

    private Bitmap bitmap1,bitmap2,bitmap3,bitmap4;

    private SharedPreferences prefs;
    private Integer Character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        iv_Character1 = (ImageView)findViewById(R.id.iv_Character1);
        iv_Character2 = (ImageView)findViewById(R.id.iv_Character2);
        iv_Character3 = (ImageView)findViewById(R.id.iv_Character3);
        iv_Character4 = (ImageView)findViewById(R.id.iv_Character4);
        bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.dao);
        bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.dao);
        bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.dao);
        bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.dao);
        cb_Character1 = (CheckBox)findViewById(R.id.cb_Character1);
        cb_Character2 = (CheckBox)findViewById(R.id.cb_Character2);
        cb_Character3 = (CheckBox)findViewById(R.id.cb_Character3);
        cb_Character4 = (CheckBox)findViewById(R.id.cb_Character4);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);

        iv_Character1.setImageBitmap(bitmap1);
        iv_Character2.setImageBitmap(bitmap2);
        iv_Character3.setImageBitmap(bitmap3);
        iv_Character4.setImageBitmap(bitmap4);

        Character = prefs.getInt("Character",0);

        //캐릭터 프리퍼런스 값을 가져와서 값에 해당하는 이미지 뷰와 체크박스의 선택표시
        if(Character!=0){
            switch (Character){
                case 1:
                    iv_Character1.setBackgroundResource(R.drawable.image_border);
                    cb_Character1.setChecked(true);
                    break;
                case 2:
                    iv_Character2.setBackgroundResource(R.drawable.image_border);
                    cb_Character2.setChecked(true);
                    break;
                case 3:
                    iv_Character3.setBackgroundResource(R.drawable.image_border);
                    cb_Character3.setChecked(true);
                    break;
                case 4:
                    iv_Character4.setBackgroundResource(R.drawable.image_border);
                    cb_Character4.setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        iv_Character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_border);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(true);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(false);
            }
        });
        iv_Character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_border);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(true);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(false);
            }
        });
        iv_Character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_border);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(true);
                cb_Character4.setChecked(false);
            }
        });
        iv_Character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_border);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(true);
            }
        });
        cb_Character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_border);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(true);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(false);
            }
        });
        cb_Character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_border);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(true);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(false);
            }
        });
        cb_Character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_border);
                iv_Character4.setBackgroundResource(R.drawable.image_nonborder);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(true);
                cb_Character4.setChecked(false);
            }
        });
        cb_Character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Character1.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character2.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character3.setBackgroundResource(R.drawable.image_nonborder);
                iv_Character4.setBackgroundResource(R.drawable.image_border);
                cb_Character1.setChecked(false);
                cb_Character2.setChecked(false);
                cb_Character3.setChecked(false);
                cb_Character4.setChecked(true);
            }
        });

    }
}
