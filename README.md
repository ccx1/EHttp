# EHttp

目前完成

异步请求网络 get post

同步请求网络 get post

post表单提交，上传文件，上传json

下一步计划

callpool优化

### get

#### get请求

       ERequest build = new ERequest.Builder()
                      .get()
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


### post

#### 提交JSON


        PostBody postBody = PostBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        ERequest build = new ERequest.Builder()
                .post(postBody)
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


#### 表单提交

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

#### 上传文件，并且有请求参数

1. 上传单个文件

        PostFormBody.Builder builder = new PostFormBody.Builder();
        builder.addParams("123", "34526")
                .addParams("adsfg", "Adfsg")
                .addKeyAndFile("file",new File(path));
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


2. 上传多个文件addFileParts上传多个文件。通过一个集合方式

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