package app.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JenHttpRequest {

    public JenHttpRequest(String url, Intent intent){
        String response = sendRequest(url, intent);
        Log.e("test", response);
    }

    public String createJson( Bundle extras ){
        JSONObject obj = new JSONObject();
        for(String key: extras.keySet()){
            Object value = extras.get(key);
            try {
                obj.put(key, value.toString());
            } catch (JSONException e) {
                Log.e("test", e.getMessage());
            }
        }
        return obj.toString();
    }

    public String sendRequest(String url, Intent intent){
        Bundle extras = intent.getExtras();

        try {
            String obj = createJson(extras);

            Log.e("test", "trying...");
            Log.e("test", "length: "+obj.toString().length());
            StringEntity entity = new StringEntity(obj.toString());
            entity.setContentEncoding(HTTP.UTF_8);
            entity.setContentType("application/json");

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("Accept", "application/json");

            httppost.setEntity(entity);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity _entity = response.getEntity();

            InputStream is = _entity.getContent();

            return readInputStreamAsString(is);
        }catch (Exception e){
            Log.e("test", e.getMessage());
        }
        return null;
    }


    public static String readInputStreamAsString(InputStream in) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }
}
