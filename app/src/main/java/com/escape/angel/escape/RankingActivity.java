package com.escape.angel.escape;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RankingActivity extends AppCompatActivity {
    private String myJSON;

    private static final String TAG_RESULTS="RESULT";
    private static final String TAG_RANKNUM="RANKNUM";
    private static final String TAG_NAME = "NAME";
    private static final String TAG_TIME = "TIME";

    private JSONArray rank = null;

    private ArrayList<HashMap<String, String>> rankingList;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        list = (ListView) findViewById(R.id.listView);
        rankingList = new ArrayList<HashMap<String,String>>();
        Server server = new Server();
        String serverIP = server.getSERVERIP();
        getData(serverIP+"getRanking.php");
    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            rank = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0;i<rank.length();i++){
                JSONObject c = rank.getJSONObject(i);
                String ranknum = Integer.toString(i+1) ;
                String name = c.getString(TAG_NAME);

                String time = c.getString(TAG_TIME);
                /*시간변환*/
                int min = Integer.parseInt(time)/60;
                int sec = Integer.parseInt(time)%60;
                if(min != 0 && sec != 0) {
                    time = Integer.toString(min) + "분 " + Integer.toString(sec) + "초";
                }else if(min != 0 && sec == 0){
                    time = Integer.toString(min) + "분";
                }else if(min == 0 && sec != 0){
                    time = Integer.toString(sec) + "초";
                }else{
                }

                HashMap<String,String> ranking = new HashMap<String, String>();
                ranking.put(TAG_RANKNUM,ranknum);
                ranking.put(TAG_NAME,name);
                ranking.put(TAG_TIME,time);
                rankingList.add(ranking);
            }
            ListAdapter adapter = new SimpleAdapter(
                    RankingActivity.this, rankingList, R.layout.ranking_list_item,
                    new String[]{TAG_RANKNUM,TAG_NAME,TAG_TIME},
                    new int[]{R.id.rankNum,R.id.name, R.id.time}
            );
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String>{
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}

