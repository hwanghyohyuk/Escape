package com.escape.angel.escape;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Hwang on 2017-06-19.
 */

public class EditBoxDialog extends Dialog {
    public EditBoxDialog(Context context){
        super(context);
    }

    private TextView tv_dialog_title;
    private EditText et_modify_edit;
    private Button btn_modify_done;

    private String mode,hint;

    private String originName,changeName;


    private char check='1';

    private Server server = new Server();
    private String serverIP = server.getSERVERIP();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_box);

        tv_dialog_title = (TextView)findViewById(R.id.tv_dialog_title);
        et_modify_edit = (EditText)findViewById(R.id.et_modify_edit);
        btn_modify_done = (Button)findViewById(R.id.btn_modify_done);

        btn_modify_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //에디트텍스트내용을 문자열변수에 저장
                changeName = et_modify_edit.getText().toString();
                if (changeName.equals("")) {//닉네임 유효성검사
                    Toast.makeText(getContext(), "변경하실 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //디비에 저장
                    updateToDatabase(originName,changeName);
                    //디비에 저장할동안 1초의 딜레이를 줌
                    handler.postDelayed(confirm, 1000);
                }
            }
        });

    }

    public void setMode(String mode) {
        this.mode = mode;
        tv_dialog_title.setText(mode);
    }
    public void setHint(String hint){
        this.hint = hint;
        et_modify_edit.setHint(hint);

    }
    public String getName() {
        return changeName;
    }

    public char getCheck(){return check;}

    public void setName(String name) {//설정 액티비티에서 프리퍼런스(사용자 닉네임)에 담긴 내용을 저장할것
        this.originName = name;
        et_modify_edit.setFocusable(true);
    }

    Handler handler = new Handler();
    Runnable confirm = new Runnable() {
        @Override
        public void run() {
            if (check == '0') {
                Toast.makeText(getContext(), "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void updateToDatabase(String first, String last){

        class UpdateData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(getContext(),
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
                    String first = (String)params[0];
                    String last = (String)params[1];


                    String link= serverIP+"updateUser.php";

                    String data  = URLEncoder.encode("FIRST", "UTF-8") + "="
                            + URLEncoder.encode(first, "UTF-8");
                    data  += "&" + URLEncoder.encode("LAST", "UTF-8") + "="
                            + URLEncoder.encode(last, "UTF-8");


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

        UpdateData task = new UpdateData();
        task.execute(first,last);
    }

}
