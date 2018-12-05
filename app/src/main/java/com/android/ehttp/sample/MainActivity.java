package com.android.ehttp.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.ehttp.EHttp;
import com.android.ehttp.ERequest;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;
import com.android.ehttp.body.MediaType;
import com.android.ehttp.body.Part;
import com.android.ehttp.body.PostBody;
import com.android.ehttp.body.PostFormBody;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EHttp mEHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEHttp = new EHttp();

    }


    public void postFile(View view) {
        File                 file    = new File(Environment.getExternalStorageDirectory() + "/" + "aaa.mp4");
        PostFormBody.Builder builder = new PostFormBody.Builder();
        builder.addParams("username", "asda")
                .addKeyAndFile("userfile", file);
        ERequest build = new ERequest.Builder()
                .post(builder.build())
                .url("http://192.168.2.154:8080/Test02/upload")
                .build();
        mEHttp.newCall(build).async(new RequestCallback() {
            @Override
            public void onResponse(final Response response) throws IOException {
                System.out.println(response.body().string());

            }

            @Override
            public void onFailure(IOException e) {
                System.out.println(e);
            }
        });

    }

    public void postFiles(View view) {
        File                 file    = new File(Environment.getExternalStorageDirectory() + "/" + "aaa.mp4");
        PostFormBody.Builder builder = new PostFormBody.Builder();
        ArrayList<Part>      parts   = new ArrayList<>();
        parts.add(new Part("userfile", file));
        parts.add(new Part("userfile", file));
        parts.add(new Part("userfile", file));
        builder.addParams("username", "asda")
                .addFileParts(parts);
        ERequest build = new ERequest.Builder()
                .post(builder.build())
                .url("http://192.168.2.154:8080/Test02/upload")
                .build();
        mEHttp.newCall(build).async(new RequestCallback() {
            @Override
            public void onResponse(final Response response) throws IOException {
                System.out.println(response.body().string());

            }

            @Override
            public void onFailure(IOException e) {
                System.out.println(e);
            }
        });

    }

    public void postJson(View view) {
        Map<String, String> map = new HashMap<>(16);
        map.put("aaa", "bbb");
        JSONObject jsonObject = new JSONObject(map);
        PostBody   body       = PostBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject.toString());
        ERequest build = new ERequest.Builder()
                .post(body)
                .url("http://192.168.2.154:3001/api/get")
                .build();
        mEHttp.newCall(build).async(new RequestCallback() {
            @Override
            public void onResponse(final Response response) throws IOException {
                System.out.println(response.body().string());

            }

            @Override
            public void onFailure(IOException e) {
                System.out.println(e);
            }
        });
    }

    public void postForm(View view) {

        PostFormBody.Builder builder = new PostFormBody.Builder();
        builder.addParams("123", "34526")
                .addParams("adsfg", "Adfsg");
        ERequest build = new ERequest.Builder()
                .post(builder.build())
                .url("http://192.168.2.154:3001/api/get")
                .addHeader("aa", "123")
                .addHeader("a1a", "123")
                .addHeader("a13a", "123")
                .build();
        mEHttp.newCall(build).async(new RequestCallback() {
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

    public void get(View view) {
        ERequest build = new ERequest.Builder()
                .get()
                .url("http://192.168.2.154:3001/api/get")
                .addHeader("aa", "123")
                .addHeader("a1a", "123")
                .addHeader("a13a", "123")
                .build();
        mEHttp.newCall(build).async(new RequestCallback() {
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
