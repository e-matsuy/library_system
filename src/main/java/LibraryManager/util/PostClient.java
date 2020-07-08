package LibraryManager.util;

import okhttp3.*;

import java.io.IOException;

public class PostClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public String doPostToGasAPI(String jsonString){
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        String url = LoadConfigurations.get("gas_url");
        String token = LoadConfigurations.get("gas_token");

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer "+token)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("error!!");
            }
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
