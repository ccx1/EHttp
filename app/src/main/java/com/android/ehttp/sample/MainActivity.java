package com.android.ehttp.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.ehttp.EHttp;
import com.android.ehttp.ERequest;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;
import com.android.ehttp.body.Part;
import com.android.ehttp.body.PostFormBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void btn1(View view) {
        File                 file    = new File(Environment.getExternalStorageDirectory() + "/" + "aaa.mp4");
        PostFormBody.Builder builder = new PostFormBody.Builder();
        ArrayList<Part>      parts   = new ArrayList<>();
        parts.add(new Part("userfile", file));
        builder.addParams("username", "asda")
                .addFileParts(parts);
        ERequest build = new ERequest.Builder()
                .post(builder.build())
                .url("http://192.168.2.154:8080/Test02/upload")
                .build();
        new EHttp().newCall(build).async(new RequestCallback() {
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
}
