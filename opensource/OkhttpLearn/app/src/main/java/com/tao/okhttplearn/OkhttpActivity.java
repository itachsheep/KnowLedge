package com.tao.okhttplearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpActivity extends AppCompatActivity {

    private String TAG = "OkHttpClient";
    OkHttpClient client;
    Cache cache;
    Request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        File cacheFile = new File(getCacheDir().toString(),"cache");
        LogUtil.i(TAG,"cacheFile "+cacheFile.toString());
        int cacheSize = 10 * 1024 * 1024;
        cache = new Cache(cacheFile,cacheSize);

        client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        request = new Request.Builder()
                .url("http://192.168.5.51:8080/123.txt").build();

    }

    public void asynRequest(View view){
          Response response1 = client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i(TAG,"asynRequest onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i(TAG,"asynRequest onResponse");
            }
          });

    }

    public void syncRequest(View view){
        new Thread(){
            @Override
            public void run() {
                try {

                    LogUtil.i(TAG,"send request for onclick!!!!");
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    LogUtil.i(TAG,"call = "+call+", response ="+response);
                    //Log.i(TAG,"call = "+call+", response ="+response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}



