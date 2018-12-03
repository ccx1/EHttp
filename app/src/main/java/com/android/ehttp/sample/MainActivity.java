package com.android.ehttp.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.ehttp.EHttp;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void btn1(View view) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("asdfg", "ADfsgh");
        EHttp.get()
                .url("http://192.168.2.154:3001/api/get")
                .map2params(objectObjectHashMap)
                .addHeader("aa", "123")
                .addHeader("a1a", "123")
                .addHeader("a13a", "123")
                .async(new RequestCallback() {
                    @Override
                    public void onResponse(Response response) throws IOException {
                        String string = response.body().string();
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }
                });

    }
}
