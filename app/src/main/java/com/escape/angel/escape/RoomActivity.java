package com.escape.angel.escape;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RoomActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private SharedPreferences prefs;
    private String Nick,Utype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        prefs = getSharedPreferences("Pref",MODE_PRIVATE);
        Nick = prefs.getString("UserName","");
        Utype= prefs.getString("Utype","");
        Toast.makeText(getApplicationContext(),Utype,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            deleteToDatabase(Nick);
            prefs.edit().putString("Utype","GUEST").apply();
            Utype = prefs.getString("Utype","");
            Toast.makeText(getApplicationContext(),Utype,Toast.LENGTH_LONG).show();
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
                    return sb.toString();
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
