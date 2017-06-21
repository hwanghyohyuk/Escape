package com.escape.angel.escape;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.os.Handler;

public class RoomActivity extends AppCompatActivity{

    private TextView tv_RoomInfo,tv_Host,tv_Guest1,tv_Guest2,tv_Guest3,tv_chatG,tv_chatH;
    private ImageView iv_Host,iv_Guest1,iv_Guest2,iv_Guest3;
    private EditText et_chatH,et_chatG;
    private Button btn_transH,btn_transG;

    private long backKeyPressedTime = 0;
    private Toast toast;

    private SharedPreferences prefs;
    private String Nick,Utype;
    private int character;

    private String RNAME,RTYPE,HOSTIP;

    private Bitmap bitmap,bitmap1,bitmap2,bitmap3,bitmap4;

    static final int PORT = 5001;
    private ServerSocket Host = null;
    private Socket Guest;
    private String msg;
    private StringBuilder hostMsg = new StringBuilder();
    private StringBuilder guestMsgBuilder = new StringBuilder();
    private Map<String, DataOutputStream> guestsMap = new HashMap<String, DataOutputStream>();

    private static final int SERVER_TEXT_UPDATE = 100;
    private static final int CLIENT_TEXT_UPDATE = 200;

    private DataInputStream guestIn;
    private DataOutputStream guestOut;
    private String guestMsg;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgg) {
            super.handleMessage(msgg);
            switch (msgg.what) {
                case SERVER_TEXT_UPDATE: {
                    hostMsg.append(msg);
                    tv_chatH.setText(hostMsg.toString());
                }
                break;
                case CLIENT_TEXT_UPDATE: {
                    guestMsgBuilder.append(guestMsg);
                    tv_chatG.setText(guestMsgBuilder.toString());
                }
                break;

            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        tv_RoomInfo = (TextView)findViewById(R.id.tv_RoomInfo);
        tv_Host = (TextView)findViewById(R.id.tv_Host);
        tv_Guest1 = (TextView)findViewById(R.id.tv_Guest1);
        tv_Guest2 = (TextView)findViewById(R.id.tv_Guest2);
        tv_Guest3 = (TextView)findViewById(R.id.tv_Guest3);
        iv_Host = (ImageView) findViewById(R.id.iv_Host);
        iv_Guest1 = (ImageView)findViewById(R.id.iv_Guest1);
        iv_Guest2 = (ImageView)findViewById(R.id.iv_Guest2);
        iv_Guest3 = (ImageView)findViewById(R.id.iv_Guest3);

        iv_Host.setBackgroundResource(R.drawable.image_border_white);
        iv_Guest1.setBackgroundResource(R.drawable.image_border_white);
        iv_Guest2.setBackgroundResource(R.drawable.image_border_white);
        iv_Guest3.setBackgroundResource(R.drawable.image_border_white);

        tv_chatH = (TextView)findViewById(R.id.tv_chatH);
        tv_chatG = (TextView)findViewById(R.id.tv_chatG);
        et_chatH = (EditText)findViewById(R.id.et_chatH);
        et_chatG = (EditText)findViewById(R.id.et_chatG);

        btn_transH = (Button)findViewById(R.id.btn_transH);
        btn_transG = (Button)findViewById(R.id.btn_transG);

        //bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slot);
        bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.mohee);
        bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.mohyuk);
        bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.mori);
        bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.moyeon);

        iv_Host.setImageBitmap(bitmap);
        iv_Guest1.setImageBitmap(bitmap);
        iv_Guest2.setImageBitmap(bitmap);
        iv_Guest3.setImageBitmap(bitmap);

        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        Nick = prefs.getString("UserName","");
        Utype= prefs.getString("Utype","");
        character = prefs.getInt("Character",0);

        Intent mIntent = getIntent();
        RNAME = mIntent.getStringExtra("RNAME");
        RTYPE = mIntent.getStringExtra("RTYPE");
        HOSTIP = mIntent.getStringExtra("HOSTIP");

    }

    @Override
    public void onStart() {
        super.onStart();
        tv_RoomInfo.setText(RTYPE+" : "+RNAME);
        tv_Host.setText(Nick);
        if(Utype.equals("HOST")){
            tv_chatG.setVisibility(View.GONE);
            et_chatG.setVisibility(View.GONE);
            btn_transG.setVisibility(View.GONE);
            Collections.synchronizedMap(guestsMap);
            try {
                Host = new ServerSocket(PORT,3);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            /** XXX 01. 첫번째. 서버가 할일 분담. 계속 접속받는것. */
                            Log.v("", "서버 대기중...");
                            try {
                                Guest = Host.accept();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.v("", Guest.getInetAddress() + "에서 접속했습니다.");

                            new Thread(new Runnable() {
                                private DataInputStream in;
                                private DataOutputStream out;
                                private String nick;

                                    @Override
                                    public void run() {

                                        try {
                                            out = new DataOutputStream(Guest.getOutputStream());
                                            in = new DataInputStream(Guest.getInputStream());
                                            nick = in.readUTF();
                                            addClient(nick, out);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        try {// 계속 듣기만!!
                                            while (in != null) {
                                                msg = in.readUTF();
                                                sendMessage(msg);
                                                handler.sendEmptyMessage(SERVER_TEXT_UPDATE);
                                            }
                                        } catch (IOException e) {
                                            // 사용접속종료시
                                            removeClient(nick);
                                        }


                                    }
                                }).start();
                            }
                        }
                    }).start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            tv_chatH.setVisibility(View.GONE);
            et_chatH.setVisibility(View.GONE);
            btn_transH.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Guest = new Socket(HOSTIP, PORT);
                        Log.v("", "클라이언트 : 서버 연결됨");

                        guestOut = new DataOutputStream(Guest.getOutputStream());
                        guestIn = new DataInputStream(Guest.getInputStream());

                        //접속하자마자 닉네임 전송하면. 서버가 이걸 닉네임으로 인식을 하고서 맵에 집어넣겠지요?
                        guestOut.writeUTF(Nick);
                        Log.v("", "클라이언트 : 메시지 전송완료");

                        while (guestIn != null) {
                            try {
                                guestMsg = guestIn.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(CLIENT_TEXT_UPDATE);
                        }
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "님이 접속하셨습니다.");
        guestsMap.put(nick, out);
    }

    public void removeClient(String nick) {
        sendMessage(nick + "님이 나가셨습니다.");
        guestsMap.remove(nick);
    }
    public void sendMessage(String msg) {
        Iterator<String> it = guestsMap.keySet().iterator();
        String key = "";
        while (it.hasNext()) {
            key = it.next();
            try {
                guestsMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            if(Utype.equals("HOST")){
            deleteToDatabase(Nick);
            prefs.edit().putString("Utype","GUEST").apply();
            }guestIn = null;
            finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(getApplicationContext(),
                "\'뒤로\' 버튼을 한번 더 누르시면 방을 나갑니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteToDatabase(String rhost){

        class DeleteData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(RoomActivity.this,
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
                    String rhost = (String)params[0];

                    Server server = new Server();
                    String serverIP = server.getSERVERIP();

                    String link=serverIP+"deleteRoom.php";
                    String data  = URLEncoder.encode("RHOST", "UTF-8") + "="
                            + URLEncoder.encode(rhost, "UTF-8");

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
                    return null;
                    //sb.toString();
                }
                catch(Exception e){

                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        DeleteData task = new DeleteData();
        task.execute(rhost);
    }

}
