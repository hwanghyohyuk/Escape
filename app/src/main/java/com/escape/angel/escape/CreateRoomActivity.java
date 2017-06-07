package com.escape.angel.escape;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import static java.sql.Types.NULL;

public class CreateRoomActivity extends AppCompatActivity {

    private EditText et_RoomTitle;
    private RadioGroup rbg_Mode;
    private Button btn_Cancel,btn_Create;

    public SharedPreferences prefs;
    public String Nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        et_RoomTitle = (EditText) findViewById(R.id.et_RoomTitle);
        rbg_Mode = (RadioGroup)findViewById(R.id.rbg_Mode);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_Create = (Button) findViewById(R.id.btn_Create);

        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        Nick = prefs.getString("UserName","");

        //취소버튼 이벤트
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
        //생성버튼 이벤트
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomTitle = et_RoomTitle.getText().toString();
                RadioButton selectedRB = (RadioButton)findViewById(rbg_Mode.getCheckedRadioButtonId());
                String roomType = selectedRB.getText().toString();

                if(roomTitle.equals("")){//방제목 확인 방제목이 없다면
                    //방 만들기 제목 확인
                    Toast wToast = Toast.makeText(getApplicationContext(),"방 제목이 입력하세요.",Toast.LENGTH_SHORT);
                    wToast.show();
                    et_RoomTitle.requestFocus();
                }else {//방제목이 있다면
                    // 방 만들기
                    //웹서버에 방제목 호스트 IP, 게임구분, 호스트 닉네임 등록
                    String RNAME=roomTitle;
                    String RTYPE=roomType;
                    String RHOST = Nick;//이부분은 프리퍼런스에 저장된 기기사용자의 닉네임을 가져와서 변수에 저장할것
                    String HOSTIP = getLocalIpAddress();
                    //사용자 구분을 HOST로 변경하여 액티비티 진입
                    /**작업해야할 부분 사용자 타입 프리퍼런스 변경*/
                    //UTYPE(프리퍼런스) = "HOST";
                    prefs.edit().putString("Utype","HOST").apply();
                    //데이터베이스에 저장
                    Toast wToast = Toast.makeText(getApplicationContext(),RNAME+","+RTYPE+","+RHOST+","+HOSTIP,Toast.LENGTH_SHORT);
                    wToast.show();
                    insertToDatabase(RNAME,RTYPE,RHOST,HOSTIP);
                    //액티비티 전환
                    Intent mIntent = new Intent(getApplicationContext(),RoomActivity.class);
                    startActivity(mIntent);
                    finish();
                }
            }
        });

    }

    /*사용자 IP 가져오는 함수*/
    public static String getLocalIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = ( NetworkInterface ) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = ( InetAddress ) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
/**작업해야할부분 데이터 삽입*/
    /*데이터베이스에 데이터 입력함수*/
    private void insertToDatabase(String rname, String rtype,String rhost,String hostip){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(CreateRoomActivity.this,
                        "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String rname = (String)params[0];
                    String rtype = (String)params[1];
                    String rhost = (String)params[2];
                    String hostip = (String)params[3];

                    Server server = new Server();
                    String serverIP = server.getSERVERIP();

                    String link=serverIP+"insertRoom.php";
                    String data  = URLEncoder.encode("RNAME", "UTF-8") + "="
                            + URLEncoder.encode(rname, "UTF-8");
                    data += "&" + URLEncoder.encode("RTYPE", "UTF-8") + "="
                            + URLEncoder.encode(rtype, "UTF-8");
                    data += "&" + URLEncoder.encode("RHOST", "UTF-8") + "="
                            + URLEncoder.encode(rhost, "UTF-8");
                    data += "&" + URLEncoder.encode("HOSTIP", "UTF-8") + "="
                            + URLEncoder.encode(hostip, "UTF-8");


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
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){

                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(rname,rtype,rhost,hostip);
    }

}
