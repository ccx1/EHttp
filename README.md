# EHttp

目前完成

异步请求网络 get post

同步请求网络 get post

post表单提交，上传文件，上传json

下一步计划

请求池优化，request的分离

### get

#### get请求

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



### post

#### 提交JSON


        EHttp.post()
                .url("http://192.168.2.154:3001/api/get")
                .map2Json(objectObjectHashMap)
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

#### 表单提交

        EHttp.post()
                .url("http://192.168.2.154:3001/api/get")
                .map2form(objectObjectHashMap)
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


#### 上传文件，并且有请求参数

        EHttp.post()
                .url("http://192.168.2.154:3001/api/get")
                .map2formPostFile(objectObjectHashMap, keyfile,file)
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