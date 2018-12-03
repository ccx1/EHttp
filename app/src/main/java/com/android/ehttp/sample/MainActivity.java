package com.android.ehttp.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.ehttp.EHttp;
import com.android.ehttp.ERequest;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;
import com.android.ehttp.body.PostFormBody;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void btn1(View view) {
        PostFormBody.Builder builder = new PostFormBody.Builder();
        builder.addParams("123", "34526")
                .addParams("adsfg", "Adfsg");
        ERequest build = new ERequest.Builder()
                .post(builder.build())
                .url("http://192.168.2.154:3001/api/get")
                .addHeader("aa", "123")
                .addHeader("a1a", "123")
                .addHeader("a13a", "123")
                .setReadTimeOut(10)
                .setWriteTimeOut(10)
                .setConnectTimeOut(10)
                .build();
        new EHttp().newCall(build).async(new RequestCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println(response.body().string());
            }

            @Override
            public void onFailure(IOException e) {
                System.out.println(e);
            }
        });

    }
}
